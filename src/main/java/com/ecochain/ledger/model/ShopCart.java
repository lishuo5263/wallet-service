package com.ecochain.ledger.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by LiShuo on 2016/10/25.
 */
public class ShopCart implements Serializable{
    private static final long serialVersionUID = -8456560310377481368L;
    private Integer id;

    private Integer goodsId;

    private String skuValue;

    private String skuInfo;

    private BigDecimal goodsPrice;

    private Integer num;

    private Integer skuId;

    private Date addtime;

    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getSkuInfo() {
        return skuInfo;
    }

    public void setSkuInfo(String skuInfo) {
        this.skuInfo = skuInfo;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSkuValue() {
        return skuValue;
    }

    public void setSkuValue(String skuValue) {
        this.skuValue = skuValue;
    }

    @Override
    public String toString() {
        return "ShopCart{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", skuInfo='" + skuInfo + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", num=" + num +
                ", skuId=" + skuId +
                ", addtime=" + addtime +
                ", userId=" + userId +
                '}';
    }
}