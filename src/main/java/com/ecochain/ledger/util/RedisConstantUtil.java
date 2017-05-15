package com.ecochain.ledger.util;

/**
 * Created by LiShuo on 2016/10/29.
 */
public interface RedisConstantUtil {

    static final String MYCAT="MyCart";//购物车缓存key

    static final String SHOWGOODSCAT="ShowGoodsCat";//商品品牌缓存key

    static final String ANDBRAND="AndBrand";//商品属性缓存key

    static final String SHOWBYSEARCHINFOBRAND="ShowBySearchInfoBrand";//商品品牌缓存key

    static final String SHOWBYSEARCHINFOCAT="ShowBySearchInfoCat";//商品属性缓存key

    static final String ANDCAT="AndCat";//商品属性缓存key

    static final String GUESSYOURLIKE="guessYourLike";//猜你喜欢缓存key

    static final String GOODSDETAILGUESSYOURLIKE="goodsDetailGuessYourLike";//商品详情猜你喜欢缓存key

    static final String SHOWGOODSDETAILCAT="showGoodsDetailCat";//商品详情缓存key1

    static final String ANDGOODS="AndGoods";//商品详情缓存key2

    static final String SEARCHGOOD="searchGood";//首页搜索缓存key

    static final String STORESEARCHGOOD="storeSearchGood";//店铺首页搜索缓存key

    static final String STORECATEGORY="storeCategory";//店铺首页搜索缓存key

    static final String MOBILEHOMESTORECATEGORY="MobileHomeStoreCategory";//手机店铺首页第一条列表搜索缓存key

    static final String MOBILEHOMESTORECATEGORYANDCATID="MobileHomeStoreCategoryAndCatId";//手机店铺首页列表搜索缓存key

    static final String STORESHOWGOODSCAT="storeShowGoodsCat";//三界本地分类品牌缓存key1

    static final String ANDSTORESHOWGOODSBRAND="AndBrand";//三界本地分类品牌缓存key2

    static final String STORESHOWBYSEARCHINFOBRAND="storeShowBySearchInfoBrand";//三界本地商品详情搜索缓存key1

    static final String STORESHOWBYSEARCHINFOCAT="storeShowBySearchInfoCat";//三界本地商品详情相关类别key2

    static final String STOREANDCATID="AndCatId";//三界本地商品详情搜索缓存key2

    static final String STORESHOWGOODSDETAILCAT="sotreShowGoodsDetailCat";//三界本地商品详情缓存key1

    static final String ANDSTOREGOODS="AndStoreGoods";//三界本地商品详情缓存key2

    static final String ANDSTOREGOODSID="AndStoreGoodsId";//三界本地商品详情缓存key3

    static final String STOREGOODSDETAILGUESSYOURLIKE="storeGoodsDetailGuessYourLike";//三界本地商品详情猜你喜欢缓存key

    static final String SHOWSTOREINFODETAIL="showStoreInfoDetail";//三界本地店铺详细信息缓存key

    static final String SHOWSTOREGOODSCANBUYDETAIL="showStoreGoodsCanBuyDetail";//三界本地店铺在售商品缓存key

    static final String R8EXCHANGERATE="r8ExChangeRate";//三界宝利率缓存key

    static final String SHOWSTOREGOODS="showStoreGoods";//三界本地首页列表进入店铺商品页面 展示一级属性 缓存key

    static final String ANDAREA="AndArea";//三界本地首页列表进入店铺商品页面 展示区县 缓存key

    static final String ANDSTART="AndStart";//三界本地首页列表按价格 缓存key1

    static final String SHOWSTOREGOODSCAT="showStoreGoodsCat";//三界本地首页列表进入店铺商品页面 展示一级属性 缓存key1

    static final String ANDAREAID="AndAreaID";//三界本地首页列表进入店铺商品页面 展示区县 缓存key2

    static final String AREA="Area";//三界本地首页列表进入店铺商品页面 展示区县 缓存key

    static final String SHOPBRAND="shopBrand";//商城查询条件品牌查询条件缓存key

    static final String START="start";//商城查询条件价格查询条件缓存key1

    static final String ANDEND="AndEnd";//商城查询条件价格查询条件缓存key2

    static final String CURRENTPAGE="AndCurrentPage";//商城分页缓存key2

    static final String ANDVIPPRICE="AndVipPrice";//商城列表搜索条件价格分页缓存key1

    static final String ANDGOODSPRICE="AndGoodsPrice";//商城列表搜索条件价格分页缓存key2

    static final String ANDGOODSSALES="AndgoodsSales";//商城列表搜索条件销量分页缓存key

    static final String REDISGOODSCACHE="RedisGoodsCache";//商城商品redis缓存key

    static final String SHOPHOTGOODS="ShopHotGoods";//商城促销商品缓存key

    static final String ANDCITY="AndCity";//商品地区缓存key

    static final String STOREDISTRIBUTEINFO="STOREDISTRIBUTEINFO";//店铺爆款商品缓存key

    static final String STOREHOTGOODS="StoreHotGoods";//已开辟分站店铺信息缓存key

    static final String SHOPREDPACKAGE="ShopRedPackage";//商城红包缓存key

    static final String SHOPREDPACKAGECOUNT="ShopRedPackageCount";//商城红包数量缓存key

    static final String UNIONQUERY="unionQuery";//商城商品&&店铺商品&&店铺信息缓存key

    static final String ANDTAGID="AndTagId";//商城商品&&店铺商品&&店铺信息缓存key

    static final String ANDTAGTYPE="AndTagType";//商城商品&&店铺商品&&店铺信息缓存key

    static final String SEARCHTAGINFO="searchTagInfo";//查询tag信息缓存key

    static final String ANDACTIVITYID="AndActivityId";//查询秒杀爆款缓存key
}

