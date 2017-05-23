package com.ecochain.ledger.service.impl;

import com.ecochain.ledger.dao.DaoSupport;
import com.ecochain.ledger.mapper.*;
import com.ecochain.ledger.model.Page;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.model.ShopGoods;
import com.ecochain.ledger.service.ShopGoodsService;
import com.ecochain.ledger.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LiShuo on 2016/10/24.
 */
@Component("shopGoodsService")
public class ShopGoodsServiceImpl implements ShopGoodsService {

    @Resource
    private ShopGoodsMapper shopGoodsMapper;

    @Resource
    private ShopGoodsBrandMapper shopGoodsBrandMapper;

    @Resource
    private ShopGoodsBrandDetailMapper shopGoodsBrandDetailMapper;

    @Resource
    private ShopGoodsCategoryMapper shopGoodsCategoryMapper;

    @Resource
    private ShopGoodsSkuMapper shopGoodsSkuMapper;

    @Resource
    private ShopSupplierMapper shopSupplierMapper;

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Override
    public Integer queryGoodsStock(Integer goodsId) {
        return this.shopGoodsMapper.queryGoodsStock(goodsId);
    }

    @Override
    public boolean updateGoodStockByType(String type,Integer goodsId) {
        if("del".equals(type)){
            return this.shopGoodsMapper.delGoodStockByType(goodsId) > 0;
        }else if("add".equals(type)){
            return this.shopGoodsMapper.addGoodStockByType(goodsId) > 0;
        }
        return false;
    }

    @Override
    public String getGoodsStock(String goodsId) {
        return this.shopGoodsMapper.getGoodsStock(goodsId);
    }

    @Override
    public List showGoodsDetail(String catId, String goodsId,String isPromote) {
        List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
        Map<String,Object> map=new HashMap<>();
        List<ShopGoods> querySku=new ArrayList<ShopGoods>();
        StringBuffer sb=new StringBuffer();
        List<Map<String, Object>> goodsType =this.shopGoodsCategoryMapper.searchGoodsType(catId); //商品分类
        for (Map<String, Object> m : goodsType){
            for (String k : m.keySet()){
                sb.append(m.get(k)+"->");
            }
        }
        map.put("goodsType",sb);
        map.put("goodsListAttr",this.shopGoodsBrandDetailMapper.showGoodsAttr(catId));//商品属性
        map.put("goodsListParam",this.shopGoodsMapper.SearchgoodsListParam(catId));//商品参数
        if("1".equals(isPromote)){  //秒杀商品详情
            map.put("goodsListDetail",this.shopGoodsMapper.queryHotGoodsDetailInfoById(goodsId));
        }else{
            map.put("goodsListDetail",this.shopGoodsMapper.queryGoodsDetailInfoById(goodsId));//普通商品详情
        }
        querySku=(ArrayList)map.get("goodsListDetail");
        /*if(querySku !=null && querySku.size() >0){
            if(querySku.get(0).getIsSkuType()!=0){
                map.put("goodsListSku",this.shopGoodsSkuMapper.searchGoodsSku(goodsId));//商品sku参数
            }
        }*/
        map.put("goodsSupplierInfo",this.shopSupplierMapper.searchSupplierInfo(goodsId));//供应商名称
        map.put("goodsOtherBrand",this.shopGoodsBrandDetailMapper.searchGoodsOtherBrand(goodsId));//同类其他品牌名称查询
        map.put("goodsOtherInfo",this.shopGoodsMapper.searchGoodsOtherInfo(goodsId));//同类其他品牌商品查询
        map.put("goodsSameType",this.shopGoodsCategoryMapper.searchGoodsSameType(catId));//相关分类
        result.add(map);
        return result;
    }

    @Override
    public List<Map<String,Object>> showBySearchInfo(String brandId, String catId) {
        List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
        Map<String,Object> map=new HashMap<>();
        if(StringUtils.isNotEmpty(brandId) && StringUtils.isNotEmpty(catId)){
            map.put("AllList",this.shopGoodsMapper.showBySearchInfo(brandId,catId));
        }else{
            if(StringUtils.isNotEmpty(catId)){
                map.put("showCatgoryResult",this.shopGoodsMapper.showBySearchInfo(null,catId));
            }else if(StringUtils.isNotEmpty(brandId)){
                map.put("showBrandResult",this.shopGoodsMapper.showBySearchInfo(brandId,null));
            }
        }
        result.add(map);
        return result;
    }


    @Override
    public List<Map<String, Object>> listPageShowBySearchInfo(Page page) throws Exception {
        //TODO 详情列表点击品牌和先管分类暂不做，需要做重定向
        Map<String,Object> map =new HashMap<>();
        List<Map<String,Object>> list =new ArrayList<>();
        /*if(StringUtils.isNotEmpty(page.getPd().get("catId"))){
            map.put("showCatgoryResult",(List<PageData>)dao.findForList("com.qkl.wlsc.provider.dao.ShopGoodsBrandDetailMapper.showGoodsAttrBuylistPage", page));
        }else if(StringUtils.isNotEmpty(page.getPd().get("brandId"))){
            map.put("showBrandResult",(List<PageData>)dao.findForList("com.qkl.wlsc.provider.dao.ShopGoodsBrandDetailMapper.showGoodsAttrBuylistPage", page));
        }
        map.put("pageInfo",page);
        list.add(map);*/
        return list;
    }

    @Override
    public List<Map<String, Object>> guessYourLike(String catId) {
         List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
        Map<String,Object> map=new HashMap<>();
        map.put("guessYourLike",this.shopGoodsMapper.guessYourLike(catId));
        result.add(map);
        return result;
    }

    @Override
    public List<Map<String, Object>> goodsDetailGuessYourLike(String goodsId) {
        List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
        Map<String,Object> map=new HashMap<>();
        map.put("goodsGuessYourLike",this.shopGoodsMapper.goodsDetailGuessYourLike(goodsId));
        result.add(map);
        return result;
    }

    @Override
    public List<Map<String,Object>> serchMyCart(Integer userId) {
        List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
        Map<String,Object> map=new HashMap<>();
        map.put("serchMyCartResult",this.shopGoodsMapper.serchMyCart(userId));
        result.add(map) ;
        //result.add(0,this.shopGoodsMapper.serchMyCartYourLike(userId)); 赞不用购物车的猜你喜欢
        return result;
    }

    @Override
    public List<Map<String,Object>> showGoods(String categoryId,String brandId) {
        List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();//把品牌和查询条件放入此list返回
        Map<String,Object> map=new HashMap<>();
            map.put("brandList",this.shopGoodsBrandDetailMapper.showGoods(brandId));
            map.put("goodsInfo",this.shopGoodsMapper.queryGoodsInfo(categoryId));
            map.put("goodsListAttr",this.shopGoodsBrandDetailMapper.showGoodsAttr(categoryId));
        result.add(map);
        return result;
    }

    @Override
    public List<Map<String,Object>>  listPageshowGoods(Page page) throws Exception {  //列表详情页
        Map<String,Object> map =new HashMap<>();
        List<Map<String,Object>> list =new ArrayList<>();
        map.put("goodsListAttr",(List<PageData>)dao.findForList("com.qkl.wlsc.provider.dao.ShopGoodsBrandDetailMapper.showGoodsAttrBuylistPage", page));
        map.put("brandList",(List<PageData>)dao.findForList("com.qkl.wlsc.provider.dao.ShopGoodsBrandDetailMapper.showGoodsBuylistPage", page));
        map.put("goodsInfo",(List<PageData>)dao.findForList("com.qkl.wlsc.provider.dao.ShopGoodsMapper.queryGoodsInfoBuylistPage", page));
        map.put("pageInfo",page);
        list.add(map);
        return list;
    }

    //PHP 商城秒杀临时不分页接口
    @Override
    public List getShopHotGoods(){
        Map<String,Object> map =new HashMap<>();
        List<Map<String,Object>> list =new ArrayList<>();
        map.put("getHotGoods", this.shopGoodsMapper.getShopHotGoods());
        list.add(map);
        return list;
    }

    @Override
    public List searchTagInfo() {
        Map<String,Object> map =new HashMap<>();
        List<Map<String,Object>> list =new ArrayList<>();
        map.put("searchTagInfo", this.shopGoodsMapper.searchTagInfo());
        list.add(map);
        return list;
    }

    @Override
    public PageData unionQueryByCity(Page page) throws Exception {
        PageData tpd = new PageData();
        if("1".equals(page.getPd().getString("tagType"))){
            List<PageData> unionQueryByCity = (List<PageData>)dao.findForList("com.qkl.wlsc.provider.dao.StoreGoodsMapper.listPageUnionQueryStoreInfoByCity", page);
            tpd.put("unionQueryByCity", unionQueryByCity);
        }
        if("1".equals(page.getPd().getString("tagId"))){
            List<PageData> unionQueryByCity = (List<PageData>)dao.findForList("com.qkl.wlsc.provider.dao.StoreGoodsMapper.listPageTotalStoreByCity", page);
            tpd.put("unionQueryByCity", unionQueryByCity);
        }
        tpd.put("page", page);
        return tpd;
    }

    @Override
    public PageData getHotGoods(Page page) throws  Exception{
        PageData tpd = new PageData();
        if(StringUtil.isNotEmpty(page.getPd().getString("activityId"))){
            List<PageData> activityInfo = (List<PageData>)dao.findForList("com.qkl.wlsc.provider.dao.ShopGoodsMapper.getActivityInfo", page);
            tpd.put("activityInfo", activityInfo);
        }
        List<PageData> goodsInfo = (List<PageData>)dao.findForList("com.qkl.wlsc.provider.dao.ShopGoodsMapper.getShopHotGoods", page);
        //List<PageData> goodsInfo = (List<PageData>)dao.findForList("com.qkl.wlsc.provider.dao.ShopGoodsMapper.listPageGetHotGoods", page);
        tpd.put("getHotGoods", goodsInfo);
        tpd.put("page", page);
        return tpd;
    }

    @Override
    public PageData searchGood(Page page) throws Exception {
        PageData tpd = new PageData();
        List<PageData> searchGood = (List<PageData>)dao.findForList("com.qkl.wlsc.provider.dao.ShopGoodsMapper.listPageSearchGood", page);
        tpd.put("searchGood", searchGood);
        tpd.put("page", page);
        if(searchGood != null && searchGood.size() >0){  //组装商品的品牌信息
            tpd.put("brandList",(List)dao.findForList("com.qkl.wlsc.provider.dao.ShopGoodsBrandDetailMapper.showGoodsBrandInfo",searchGood.get(0).get("brand_id")));
        }
        return tpd;
    }

    @Override
    public PageData listPageshowGoodsBuySearchInfo(Page page) throws Exception { // 列表点击价格排序搜索
        PageData tpd = new PageData();
        List<PageData> goodsInfo = (List<PageData>)dao.findForList("com.qkl.wlsc.provider.dao.ShopGoodsMapper.listPageshowGoodsBuySearchInfo", page);
        tpd.put("goodsInfo", goodsInfo);
        tpd.put("page", page);
        return tpd;
    }

    @Override
    public PageData listPageshowGoodsBuySearchInfo2(Page page) throws Exception { // 列表点击销量排序搜索
        PageData tpd = new PageData();
        List<PageData> goodsInfo = (List<PageData>)dao.findForList("com.qkl.wlsc.provider.dao.ShopGoodsMapper.listPageshowGoodsBuySearchInfo2", page);
        tpd.put("goodsInfo", goodsInfo);
        tpd.put("page", page);
        return tpd;
    }

    @Override
    public PageData listPageshowGoodsBuyBrandId(Page page) throws Exception {
        PageData tpd = new PageData();
        List<PageData> brandList = (List<PageData>)dao.findForList("com.qkl.wlsc.provider.dao.ShopGoodsBrandDetailMapper.showGoods", page.getPd().getString("brandId"));
        List<Map<String,Object>> goodsListAttr = (List<Map<String,Object>>)dao.findForList("com.qkl.wlsc.provider.dao.ShopGoodsBrandDetailMapper.showGoodsAttr", page.getPd().getString("catId"));
        if(StringUtil.isNotEmpty(page.getPd().getString("brandId")) && "0".equals(page.getPd().getString("brandId"))){
            List<PageData> goodsInfo =(List<PageData>)dao.findForList("com.qkl.wlsc.provider.dao.ShopGoodsMapper.listPageQueryGoodsInfo", page);
            tpd.put("goodsInfo", goodsInfo);
        }else{
            List<PageData> goodsInfo =(List<PageData>)dao.findForList("com.qkl.wlsc.provider.dao.ShopGoodsMapper.listPageshowGoodsBuyBrandId", page);
            tpd.put("goodsInfo", goodsInfo);
        }
        tpd.put("brandList", brandList);
        tpd.put("goodsListAttr", goodsListAttr);
        tpd.put("page", page);
        return tpd;
    }

    @Override
    public PageData listPageshowGoodsBuyUnionInfo(Page page) throws Exception {  //列表点击品牌+价格区间搜索
        PageData tpd = new PageData();
        if(StringUtil.isNotEmpty(page.getPd().getString("brandId")) && "0".equals(page.getPd().getString("brandId"))){
            List<PageData> goodsInfo =(List<PageData>)dao.findForList("com.qkl.wlsc.provider.dao.ShopGoodsMapper.queryGoodsInfoBuylistPage", page);
            tpd.put("goodsInfo", goodsInfo);
        }else{
            List<PageData> goodsInfo =(List<PageData>)dao.findForList("com.qkl.wlsc.provider.dao.ShopGoodsMapper.listPageshowGoodsBuyUnionInfo", page);
            tpd.put("goodsInfo", goodsInfo);
        }
        tpd.put("page", page);
        return tpd;
    }

    @Override
    public Map getSkuGoodsInfo(String skuValue) {
        return this.shopGoodsMapper.getSkuGoodsInfo(skuValue);
    }

    @Override
    public boolean updateMutityGoodStockByType(String type ,String skuValue) {
        if("del".equals(type)){
            return this.shopGoodsMapper.delSkuGoodStockByType(skuValue) > 0;
        }else if("add".equals(type)){
            return this.shopGoodsMapper.addSkuGoodStockByType(skuValue) > 0;
        }
        return false;
    }

    @Override
    public boolean updateGoodStock(String goodsId,String orderNo) {
        return this.shopGoodsMapper.updateGoodStock(goodsId,orderNo);
    }

    @Override
    public boolean addGoods(PageData pd) throws Exception {
        return (Integer)dao.save("com.qkl.wlsc.provider.dao.ShopGoodsMapper.addGoods", pd)>0;
    }

    @Override
    public PageData getOneShopGoods() throws Exception {
        return (PageData)dao.findForObject("com.qkl.wlsc.provider.dao.ShopGoodsMapper.getOneShopGoods", null);
    }

    @Override
    public List queryRMBGoodsDetailInfoByGoodsId(String goodsId) {
        return this.shopGoodsMapper.queryRMBGoodsDetailInfoByGoodsId(goodsId);
    }
}
