package com.sixtofly.entity;


import org.apache.commons.lang3.StringUtils;

public class ScanResult {
    private String id;  //订单号
    private String arriveScanType; // 签收
    private String arriveUploadSiteName; // 签收扫描网点
    private String arriveScanDate; // 签收扫描时间
    private String endScanType; // 到件
    private String endUploadSiteName; // 到件扫描网点
    private String endScanDate; // 到件扫描时间
    private int days;
    private String remark = StringUtils.EMPTY; // 备注

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArriveScanType() {
        return arriveScanType;
    }

    public void setArriveScanType(String arriveScanType) {
        this.arriveScanType = arriveScanType;
    }

    public String getArriveUploadSiteName() {
        return arriveUploadSiteName;
    }

    public void setArriveUploadSiteName(String arriveUploadSiteName) {
        this.arriveUploadSiteName = arriveUploadSiteName;
    }


    public String getEndScanType() {
        return endScanType;
    }

    public void setEndScanType(String endScanType) {
        this.endScanType = endScanType;
    }

    public String getEndUploadSiteName() {
        return endUploadSiteName;
    }

    public void setEndUploadSiteName(String endUploadSiteName) {
        this.endUploadSiteName = endUploadSiteName;
    }

    public String getArriveScanDate() {
        return arriveScanDate;
    }

    public void setArriveScanDate(String arriveScanDate) {
        this.arriveScanDate = arriveScanDate;
    }

    public String getEndScanDate() {
        return endScanDate;
    }

    public void setEndScanDate(String endScanDate) {
        this.endScanDate = endScanDate;
    }
}
