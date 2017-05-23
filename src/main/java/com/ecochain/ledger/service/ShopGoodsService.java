package com.ecochain.ledger.service;

import com.ecochain.ledger.model.Page;
import com.ecochain.ledger.model.PageData;

import java.util.List;
import java.util.Map;

/**
 * Created by LiShuo on 2016/10/24.
 */
public interface ShopGoodsService {

    /**
     * 促销商品下单前查询商品库存
     * @param goodsId
     * @return
     */
    Integer queryGoodsStock(Integer goodsId);

    /**
     * 对促销商品进行库存操做
     * @param type
     * @param goodsId
     * @return
     */
    boolean updateGoodStockByType(String type, Integer goodsId);

    /**
     * 获取促销商品库存数量
     * @param goodsId
     * @return
     */
    String getGoodsStock(String goodsId);

    /**
     * 分页获取促销商品
     * @return
     */
    PageData getHotGoods(Page page) throws  Exception;

    /**
     * 商品搜索（暂时只支持一个查询条件） like %%
     */
    PageData searchGood(Page page) throws Exception;

    /**
     * 根据首页列表进入商品页面（品牌+商品简要信息+搜索条件）
     */
    List<Map<String,Object>> showGoods(String catId, String categoryId);

    /**
     * 进入商品详情页
     */
    List showGoodsDetail(String catId, String goodsId, String isPromote);


    /**
     * 根据选中的品牌或者属性进行商品查询
     * @param brandId
     * @param catId
     * @return
     */
    List<Map<String,Object>> showBySearchInfo(String brandId, String catId);

    /**
     ** 根据选中的品牌或者属性进行商品分页查询
     * @param page
     * @return
     * @throws Exception
     */
    List<Map<String,Object>>  listPageShowBySearchInfo(Page page)throws Exception;


    /**
     * 我的购物车商品信息
     */
    List<Map<String,Object>> serchMyCart(Integer userId);

    /**
     * 猜你喜欢 5个商品
     */
    List<Map<String,Object>> guessYourLike(String goodsId);

    /**
     * 猜你喜欢接口：查询商品当前最细分类下销售量前5的商品（除去当前商品）
     */
    List<Map<String,Object>> goodsDetailGuessYourLike(String goodsId);


    /**
     * 首页进入列表分页
     * @param page
     * @return
     * @throws Exception
     */
    List<Map<String,Object>>  listPageshowGoods(Page page)throws Exception;

    /**
     * 列表按品牌条件分页
     * @param page
     * @return
     * @throws Exception
     */
    PageData listPageshowGoodsBuyBrandId(Page page)throws Exception;

    /**
     * 列表全部搜索条件分页
     * @param page
     * @return
     * @throws Exception
     */
    PageData listPageshowGoodsBuyUnionInfo(Page page)throws Exception;

    /**
     * 列表价格+销量索条件分页
     * @param page
     * @return
     * @throws Exception
     */
    PageData listPageshowGoodsBuySearchInfo(Page page)throws Exception;

    PageData listPageshowGoodsBuySearchInfo2(Page page)throws Exception;

    List getShopHotGoods(); //PHP 商城秒杀临时不分页接口

    /**
     * 秒杀商品下单定时不支付恢复库存
     * @param orderNo goodsId
     * @return
     */
    boolean updateGoodStock(String goodsId, String orderNo);

    /**
     * 手机端按定位城市信息查询对应的  商城商品|店铺商品|店铺信息
     * @param page
     * @return
     * @throws Exception
     */
    PageData unionQueryByCity(Page page)throws Exception;

    /**
     * 手机端本地搜索tag查询
     * @return
     */
    List searchTagInfo();


    /**
     * 根据skuValue 查询多价格商品信息
     * @param skuValue
     * @return
     */
    Map getSkuGoodsInfo(String skuValue);

    /**
     * 根据skuValue 查询多价格商品剩余库存
     * @param skuValue,type
     * @return
     */
    boolean updateMutityGoodStockByType(String type, String skuValue);
    
    /**
     * @describe:添加商品
     * @author: zhangchunming
     * @date: 2017年4月26日下午4:23:37
     * @param shopGoods
     * @throws Exception
     * @return: boolean
     */
    boolean addGoods(PageData pd)throws Exception;
    
    /**
     * @describe:查询一个没有绑定卖家的商品
     * @author: zhangchunming
     * @date: 2017年4月26日下午6:58:18
     * @throws Exception
     * @return: PageData
     */
    public PageData getOneShopGoods() throws Exception;

    /**
     * @author Lisandro
     * @date 2017年5月3日14:38:17
     * @describe 立即购买查询人民币商品信息
     * @param goodsId
     * @return
     */
    List queryRMBGoodsDetailInfoByGoodsId(String goodsId);
}
