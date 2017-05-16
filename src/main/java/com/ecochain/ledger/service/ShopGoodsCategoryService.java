package com.ecochain.ledger.service;

import java.util.List;

import com.ecochain.ledger.model.Page;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.model.ShopGoodsCategory;

/**
 * Created by LiShuo on 2016/10/25.
 */
public interface ShopGoodsCategoryService {

    /**
     * 首页商品列表
     * @return
     */
    List<ShopGoodsCategory> showAll();

    /**
     * 手机端首页商品列表
     * @return
     */
    List mobileShowAll(Integer level);

    /**
     * 手机会场信息查询
     * @return
     */
    PageData mobileHomeRoom(Page page) throws Exception;
}
