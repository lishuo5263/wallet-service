package com.ecochain.ledger.model;

import java.util.Date;

public class UserCardChargeInfo {
    private Integer id;

    private Integer userId;

    private String accepteNo;

    private String acceptName;

    private String companyBankName;

    private String remark;

    private Integer remarkPrice;

    private String status;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAccepteNo() {
        return accepteNo;
    }

    public void setAccepteNo(String accepteNo) {
        this.accepteNo = accepteNo;
    }

    public String getAcceptName() {
        return acceptName;
    }

    public void setAcceptName(String acceptName) {
        this.acceptName = acceptName == null ? null : acceptName.trim();
    }

    public String getCompanyBankName() {
        return companyBankName;
    }

    public void setCompanyBankName(String companyBankName) {
        this.companyBankName = companyBankName == null ? null : companyBankName.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getRemarkPrice() {
        return remarkPrice;
    }

    public void setRemarkPrice(Integer remarkPrice) {
        this.remarkPrice = remarkPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}