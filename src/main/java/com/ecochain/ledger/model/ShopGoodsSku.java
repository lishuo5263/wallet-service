package com.ecochain.ledger.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by LiShuo on 2016/10/25.
 */
public class ShopGoodsSku implements Serializable{
    private static final long serialVersionUID = -2642986921259964719L;
    private Integer skuId;

    private Integer goodsId;

    private BigDecimal vipprice;

    private BigDecimal price;

    private Short stock;

    private String featureGroup;

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public BigDecimal getVipprice() {
        return vipprice;
    }

    public void setVipprice(BigDecimal vipprice) {
        this.vipprice = vipprice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Short getStock() {
        return stock;
    }

    public void setStock(Short stock) {
        this.stock = stock;
    }

    public String getFeatureGroup() {
        return featureGroup;
    }

    public void setFeatureGroup(String featureGroup) {
        this.featureGroup = featureGroup == null ? null : featureGroup.trim();
    }
}