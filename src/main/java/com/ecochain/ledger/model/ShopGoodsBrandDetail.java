package com.ecochain.ledger.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by LiShuo on 2016/10/24.
 */
public class ShopGoodsBrandDetail implements Serializable{
    private static final long serialVersionUID = -7223408436049241825L;
    private Integer id;

    private Integer brandDetailId;

    private String brandDetailName;

    private Date addTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBrandDetailId() {
        return brandDetailId;
    }

    public void setBrandDetailId(Integer brandDetailId) {
        this.brandDetailId = brandDetailId;
    }

    public String getBrandDetailName() {
        return brandDetailName;
    }

    public void setBrandDetailName(String brandDetailName) {
        this.brandDetailName = brandDetailName == null ? null : brandDetailName.trim();
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}