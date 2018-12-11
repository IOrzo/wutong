package com.sixtofly.wutong;

import com.alibaba.fastjson.JSON;
import com.sixtofly.entity.Data;
import com.sixtofly.entity.ScanList;
import com.sixtofly.entity.ScanResult;
import com.sixtofly.entity.WuTong;
import com.sixtofly.util.DateHelper;
import com.sixtofly.util.HttpUtilManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpException;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainRun {
    public static final String url_prex = "http://kjgz.sto-express.cn";
    public static final String query_url = "/Bill/GetTrace";
    public static final String COOKIE = "_ati=7830530365951;ASP.NET_SessionId=va0is1xw5lzledoiuip52543";
    public static final int MAX_QUERY_COUNT = 500;

    public static Logger logger = Logger.getLogger(MainRun.class);
//    private  String path = System.getProperty("user.dir") + File.separator;
    private  String path = "src/main/resources/";
//    private String path = System.getProperty("user.dir") + File.separator;
    private String configPath = path + "config/tk.ini";
    private String dataPath = path + "data";
    private Properties propertie;

    public static void main(String[] args) {
        MainRun mainRun = new MainRun();
        mainRun.init();
        if(ArrayUtils.isNotEmpty(args)){
            mainRun.start(args[0]);
        }else {
            mainRun.start(StringUtils.EMPTY);
        }
    }

    public void init(){
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                    new File(configPath)));
            propertie = new Properties();
            propertie.load(in);
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<String> getDataSource(String fileName){
        List<String> list = null;
        list = DataCsv.readCsv(fileName);
        return list == null ? Collections.EMPTY_LIST : list;
    }

    public void start(String cookie){
        if(StringUtils.isBlank(cookie)){
            cookie = MainRun.COOKIE;
        }


        File file = new File(dataPath);
        File[] files = file.listFiles((filePath) -> {
            if(filePath.getName().endsWith(".csv")){
                return true;
            }else {
                return false;
            }
        });
        if(files.length > 0){
            for(File dataFile : files){
                this.run(dataFile.getAbsolutePath(), cookie);
            }
        }

    }

    private void run(String path, String cookie){
        HttpUtilManager httpUtilManager = HttpUtilManager.getInstance();
        List<String> data = this.getDataSource(path);
        List<ScanResult> resultList = new ArrayList<>();
        // 对id进行分组
        List<String> ids = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        //%0A
        for (int i = 0; i < data.size(); i++) {
            sb.append("\n");
            sb.append(data.get(i));
            if((i + 1) % MainRun.MAX_QUERY_COUNT == 0 && i != 0){
                String temp = sb.toString().substring(1);
                ids.add(temp);
                sb = new StringBuilder();
            }
        }
        if(StringUtils.isNotBlank(sb.toString())){
            ids.add(sb.toString().substring(1));
        }
        for(String id : ids){
            try {
                Map<String, String> params = new HashMap<>(2);
                params.put("billCode", id);
                String result = httpUtilManager.requestHttpPost(MainRun.url_prex, MainRun.query_url, params, cookie);
                WuTong wuTong = JSON.parseObject(result, WuTong.class);
                List<ScanResult> list = this.analysis(wuTong);
                resultList.addAll(list);
                System.out.println(result);
            } catch (HttpException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            this.exportDate(this.getDesktopPath(),resultList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 对结果进行分析
    public List<ScanResult> analysis(WuTong source){
        List<ScanResult> list = new ArrayList<>();
        List<Data> data = source.getData();
        for(Data scan : data){
            ScanResult scanResult = new ScanResult();
            for (int i = scan.getScanList().size() - 1; i > 0; i--) {
                ScanList scanList = scan.getScanList().get(i);
                scanResult.setId(scanList.getId());
                if(i == scan.getScanList().size() - 1){
                    if(scanList.getScanType().equals("签收")){
                        scanResult.setEndScanType(scanList.getScanType());
                        scanResult.setEndScanDate(scanList.getScanDate());
                        scanResult.setEndUploadSiteName(scanList.getUploadSiteName());
                    } else if(scanList.getScanType().equals("到件")){
                        scanResult.setArriveScanType(scanList.getScanType());
                        scanResult.setArriveScanDate(scanList.getScanDate());
                        scanResult.setArriveUploadSiteName(scanList.getUploadSiteName());
                        break;
                    }
                }else {
                    if(scanList.getScanType().equals("客户签收") || scanList.getScanType().equals("菜鸟驿站") || scanList.getScanType().equals("第三方签收")){
                        scanResult.setArriveScanType(scanList.getScanType());
                        scanResult.setArriveScanDate(scanList.getScanDate());
                        scanResult.setArriveUploadSiteName(scanList.getUploadSiteName());
                        break;
                    }
                    if(scanList.getScanType().equals("到件")){
                        scanResult.setArriveScanType(scanList.getScanType());
                        scanResult.setArriveScanDate(scanList.getScanDate());
                        scanResult.setArriveUploadSiteName(scanList.getUploadSiteName());
                        break;
                    }
                }
            }
            // 计算两个日期差值
            this.computeDays(scanResult);
            list.add(scanResult);
        }
        return list;
    }

    private void computeDays(ScanResult scanResult){
        String arriveScanDate = scanResult.getArriveScanDate();
        String endScanDate = scanResult.getEndScanDate();
        if(StringUtils.isBlank(endScanDate)){
            scanResult.setDays(88);
            return;
        }
        if(StringUtils.isBlank(arriveScanDate)){
            scanResult.setDays(88);
            return;
        }
        endScanDate = scanResult.getEndScanDate().substring(0,10);
        arriveScanDate = scanResult.getArriveScanDate().substring(0,10);
        try {
            Date arrive = DateHelper.parseDate(arriveScanDate, DateHelper.YYYY_MM_DD);
            Date end = DateHelper.parseDate(endScanDate, DateHelper.YYYY_MM_DD);
            int days = (int) DateHelper.intervalDays(end , arrive);
            scanResult.setDays(days);
            if(days > 0){
                Date judge = DateHelper.parseDate(scanResult.getArriveScanDate(), DateHelper.YYYY_MM_DD_HH_mm_ss);
                Date temp = new Date(judge.getTime());
                temp.setHours(12);
                temp.setMinutes(0);
                temp.setSeconds(0);
                if(judge.getTime() > temp.getTime()){
                    scanResult.setRemark("已过12点");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(scanResult.getArriveScanType().equals("客户签收") || scanResult.getArriveScanType().equals("菜鸟驿站") || scanResult.getArriveScanType().equals("第三方签收")){
            scanResult.setRemark("第三方签收");
        }
    }

    public void exportDate(String path, List<ScanResult> scanResults) throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        String sheetName = "申通数据";
        //第一页
        HSSFSheet sheet = wb.createSheet(sheetName);
        //第一行数据
        HSSFRow userBorrowRow = sheet.createRow(0);
        userBorrowRow.createCell(0).setCellValue("序列号");
        userBorrowRow.createCell(1).setCellValue("订单号");
        userBorrowRow.createCell(2).setCellValue("扫描类型");
        userBorrowRow.createCell(3).setCellValue("扫描网点");
        userBorrowRow.createCell(4).setCellValue("扫描时间");
        userBorrowRow.createCell(5).setCellValue("扫描类型");
        userBorrowRow.createCell(6).setCellValue("扫描网点");
        userBorrowRow.createCell(7).setCellValue("扫描时间");
        userBorrowRow.createCell(8).setCellValue("相差天数");
        userBorrowRow.createCell(9).setCellValue("备注");
        // 设置数据格式
        HSSFCellStyle cellStyle = wb.createCellStyle();
        HSSFDataFormat format = wb.createDataFormat();
        cellStyle.setDataFormat(format.getFormat("0"));
        int i = 0;
        for (ScanResult scanResult : scanResults) {
            //插入数据
            HSSFRow hssfRow = sheet.createRow(i + 1);
            //写入数据
            //插入序列号
            hssfRow.createCell(0).setCellValue(i + 1);
            hssfRow.createCell(1).setCellValue(scanResult.getId());
            hssfRow.createCell(2).setCellValue(scanResult.getArriveScanType());
            hssfRow.createCell(3).setCellValue(scanResult.getArriveUploadSiteName());
            hssfRow.createCell(4).setCellValue(scanResult.getArriveScanDate());
            hssfRow.createCell(5).setCellValue(scanResult.getEndScanType() == null ? StringUtils.EMPTY : scanResult.getEndScanType());
            hssfRow.createCell(6).setCellValue(scanResult.getEndUploadSiteName() == null ? StringUtils.EMPTY : scanResult.getEndUploadSiteName());
            if(Objects.nonNull(scanResult.getEndScanDate())){
                hssfRow.createCell(7).setCellValue(scanResult.getEndScanDate());
            }else {
                hssfRow.createCell(7).setCellValue(StringUtils.EMPTY);
            }
            hssfRow.createCell(8).setCellValue(scanResult.getDays());
            hssfRow.createCell(9).setCellValue(scanResult.getRemark());
            i++;
        }
        //保存Excel
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "申通数据" + df.format(new Date()) + ".xls";
        File file = new File(path + File.separator + fileName);

        FileOutputStream output = new FileOutputStream(file.toString());
        wb.write(output);
        output.flush();
        output.close();
    }

    private String getDesktopPath(){
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File file = fsv.getHomeDirectory();
        return file.getPath();
    }
}
