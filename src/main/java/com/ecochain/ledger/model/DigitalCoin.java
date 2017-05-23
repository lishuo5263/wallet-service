package com.ecochain.ledger.model;

import java.util.Date;

public class DigitalCoin {
    private Integer id;

    private String coinName;

    private String coinNameBrief;

    private String coinRate;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName == null ? null : coinName.trim();
    }

    public String getCoinNameBrief() {
        return coinNameBrief;
    }

    public void setCoinNameBrief(String coinNameBrief) {
        this.coinNameBrief = coinNameBrief == null ? null : coinNameBrief.trim();
    }

    public String getCoinRate() {
        return coinRate;
    }

    public void setCoinRate(String coinRate) {
        this.coinRate = coinRate == null ? null : coinRate.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}