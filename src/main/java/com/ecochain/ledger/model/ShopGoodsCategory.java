package com.ecochain.ledger.model;

import java.io.Serializable;

/**
 * Created by LiShuo on 2016/10/25.
 */
public class ShopGoodsCategory implements Serializable{

    private static final long serialVersionUID = 1753156290856230901L;

    private Short catId;

    private String catName;

    private Short grade;

    private String catDesc;

    private String brandId;

    private String catImg;

    private String parentId;

    private Boolean sortOrder;

    private Boolean showInNav;

    private Boolean isShow;

    private Short isLevel;

    private String levelOneName;

    public Short getCatId() {
        return catId;
    }

    public void setCatId(Short catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName == null ? null : catName.trim();
    }

    public Short getGrade() {
        return grade;
    }

    public void setGrade(Short grade) {
        this.grade = grade;
    }

    public String getCatDesc() {
        return catDesc;
    }

    public void setCatDesc(String catDesc) {
        this.catDesc = catDesc == null ? null : catDesc.trim();
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId == null ? null : brandId.trim();
    }

    public String getCatImg() {
        return catImg;
    }

    public void setCatImg(String catImg) {
        this.catImg = catImg == null ? null : catImg.trim();
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public Boolean getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Boolean sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getShowInNav() {
        return showInNav;
    }

    public void setShowInNav(Boolean showInNav) {
        this.showInNav = showInNav;
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public Short getIsLevel() {
        return isLevel;
    }

    public void setIsLevel(Short isLevel) {
        this.isLevel = isLevel;
    }

    public String getLevelOneName() {
        return levelOneName;
    }

    public void setLevelOneName(String levelOneName) {
        this.levelOneName = levelOneName;
    }
}