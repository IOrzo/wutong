package com.sixtofly.entity;

import java.util.List;

public class Data {

    private String Id;
    private String SignMan;
    private String SendMan;
    private String UploadSiteName;
    private String SendSiteName;
    private String SendSiteMobile;
    private String TargetSiteName;
    private String TargetSiteMobile;
    private String CurrentSiteName;
    private String CurrentSiteMobile;
    private String CurrentType;
    private String SignTime;
    private String UploadTime;
    private List<ScanList> ScanList;
    private String BillCodeList;
    private int BillCodeCount;
    private Bill Bill;
    private List<CpsList> CpsList;

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getId() {
        return Id;
    }

    public void setSignMan(String SignMan) {
        this.SignMan = SignMan;
    }

    public String getSignMan() {
        return SignMan;
    }

    public void setSendMan(String SendMan) {
        this.SendMan = SendMan;
    }

    public String getSendMan() {
        return SendMan;
    }

    public void setUploadSiteName(String UploadSiteName) {
        this.UploadSiteName = UploadSiteName;
    }

    public String getUploadSiteName() {
        return UploadSiteName;
    }

    public void setSendSiteName(String SendSiteName) {
        this.SendSiteName = SendSiteName;
    }

    public String getSendSiteName() {
        return SendSiteName;
    }

    public void setSendSiteMobile(String SendSiteMobile) {
        this.SendSiteMobile = SendSiteMobile;
    }

    public String getSendSiteMobile() {
        return SendSiteMobile;
    }

    public void setTargetSiteName(String TargetSiteName) {
        this.TargetSiteName = TargetSiteName;
    }

    public String getTargetSiteName() {
        return TargetSiteName;
    }

    public void setTargetSiteMobile(String TargetSiteMobile) {
        this.TargetSiteMobile = TargetSiteMobile;
    }

    public String getTargetSiteMobile() {
        return TargetSiteMobile;
    }

    public void setCurrentSiteName(String CurrentSiteName) {
        this.CurrentSiteName = CurrentSiteName;
    }

    public String getCurrentSiteName() {
        return CurrentSiteName;
    }

    public void setCurrentSiteMobile(String CurrentSiteMobile) {
        this.CurrentSiteMobile = CurrentSiteMobile;
    }

    public String getCurrentSiteMobile() {
        return CurrentSiteMobile;
    }

    public void setCurrentType(String CurrentType) {
        this.CurrentType = CurrentType;
    }

    public String getCurrentType() {
        return CurrentType;
    }

    public void setSignTime(String SignTime) {
        this.SignTime = SignTime;
    }

    public String getSignTime() {
        return SignTime;
    }

    public void setUploadTime(String UploadTime) {
        this.UploadTime = UploadTime;
    }

    public String getUploadTime() {
        return UploadTime;
    }

    public void setScanList(List<ScanList> ScanList) {
        this.ScanList = ScanList;
    }

    public List<ScanList> getScanList() {
        return ScanList;
    }

    public void setBillCodeList(String BillCodeList) {
        this.BillCodeList = BillCodeList;
    }

    public String getBillCodeList() {
        return BillCodeList;
    }

    public void setBillCodeCount(int BillCodeCount) {
        this.BillCodeCount = BillCodeCount;
    }

    public int getBillCodeCount() {
        return BillCodeCount;
    }

    public void setBill(Bill Bill) {
        this.Bill = Bill;
    }

    public Bill getBill() {
        return Bill;
    }

    public void setCpsList(List<CpsList> CpsList) {
        this.CpsList = CpsList;
    }

    public List<CpsList> getCpsList() {
        return CpsList;
    }
}