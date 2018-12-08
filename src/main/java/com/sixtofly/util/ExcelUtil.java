package com.sixtofly.util;

import com.alibaba.fastjson.JSON;
import com.sixtofly.entity.Constant;
import com.sixtofly.entity.Statistics;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * @describe:（类描述）
 * @author：（创建人）xyb
 * @creationTime：（创建时间）2018-12-0511:19
 * @modifier：（修改人）
 * @modificationTime：（修改时间）
 * @修改备注：
 * @Note:
 */
public class ExcelUtil {
    public static DecimalFormat df = new DecimalFormat("#");

    /**
     *  读取excel文件
     * @param path 文件路径
     * @throws Exception
     */
    public static <T> List<T> read(String path, Class<T> clazz) throws Exception {
        File file = new File(path);
        if(!file.isFile()){
            throw new Exception("文件不存在");
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);
        // 如果没有数据直接返回
        if(Objects.isNull(sheet)){
            return ListUtils.EMPTY_LIST;
        }
        Field[] fields = clazz.getDeclaredFields();
        List<T> list = new ArrayList<>();
        for(int i = 1; i <= sheet.getLastRowNum(); i++){
            Row row = sheet.getRow(i);
            int end = row.getLastCellNum();
            int len = fields.length;
            // 取列长度和属性长度中较小的值
            len = end > len ? len : end;
            T entity = clazz.newInstance();
            for(int j = 0; j < len; j++) {
                Cell cell = row.getCell(j);
                Object obj = getValue(cell);
                BeanUtils.setProperty(entity, fields[j].getName(), obj);
            }
            list.add(entity);
            // 遍历列值
            for (int j = 0; j < end; j++) {
                Cell cell = row.getCell(j);
                Object obj = getValue(cell);
                System.out.println(obj);
            }
        }
        return list;
    }

    /**
     * 写入文件
     * @param path 文件路径,默认为桌面
     * @param fileName 文件名称,默认为时间
     * @throws IOException
     */
    public static <T> int write(List<T> data, String fileName, String path) throws IOException {
        if(CollectionUtils.isEmpty(data)){
            return 0;
        }
        //保存Excel
        if(StringUtils.isBlank(path)){
            path = Constant.DESKTOP_PAHT;
        }
        if(StringUtils.isBlank(fileName)){
            SimpleDateFormat sdf = new SimpleDateFormat("YYYYmmddMMhhss");
            fileName = sdf.format(Calendar.getInstance().getTime());
        }

        HSSFWorkbook wb = new HSSFWorkbook();
        //第一页
        HSSFSheet sheet = wb.createSheet(fileName);
        //第一行数据 创建标题
        HSSFRow userBorrowRow = sheet.createRow(0);
        T title = data.get(0);
        Field[] fields = title.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            userBorrowRow.createCell(i).setCellValue(fields[i].getName());
        }
        // 设置数据格式
//        HSSFCellStyle cellStyle = wb.createCellStyle();
//        HSSFDataFormat format = wb.createDataFormat();
//        cellStyle.setDataFormat(format.getFormat("0"));
        // 插入数据
        int n = 1;
        for (T entity : data) {
            HSSFRow row = sheet.createRow(n++);
            for (int i = 0; i < fields.length; i++) {
                String value;
                try {
                    value = BeanUtils.getProperty(entity, fields[i].getName());
                } catch (Exception e) {
                    e.printStackTrace();
                    value = StringUtils.EMPTY;
                }
                row.createCell(i).setCellValue(value);
            }
        }
        File file = new File(path + File.separator + fileName + ".xls");
        FileOutputStream output = new FileOutputStream(file.toString());
        wb.write(output);
        output.flush();
        output.close();
        return data.size();
    }

    private static Object getValue(Cell cell){
        Object obj = null;
        if(Objects.isNull(cell)){
            return null;
        }
        switch (cell.getCellType()){
            case BOOLEAN:
                obj = cell.getBooleanCellValue();
                break;
            case ERROR:
                obj = cell.getErrorCellValue();
                break;
            case NUMERIC:
                obj = df.format(cell.getNumericCellValue());
                break;
            case STRING:
                obj = cell.getStringCellValue();
                break;
            default:
                break;
        }
        return obj;
    }

    public static void main(String[] args) {
        String path = "src/main/resources/";
        path += "statistics.xlsx";
        List<Statistics> list = null;
        try {
            list = ExcelUtil.read(path, Statistics.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ExcelUtil.write(list, null ,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
