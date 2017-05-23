package com.ecochain.ledger.web.rest;


import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecochain.ledger.base.BaseWebService;
import com.ecochain.ledger.constants.CodeConstant;
import com.ecochain.ledger.model.Page;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.service.ShopGoodsService;
import com.ecochain.ledger.util.AjaxResponse;
import com.ecochain.ledger.util.JedisUtil;
import com.ecochain.ledger.util.RedisConstantUtil;


/**
 * Created by LiShuo on 2016/10/24.
 */
@RestController
@RequestMapping(value = "/api/rest/goods")
public class ShopGoodsWebService extends BaseWebService {

    @Autowired
    private ShopGoodsService shopGoodsService;

    /*@Autowired
    private CacheManager cacheManager;*/

    /**
     * 商城促销商品查询
     * @param request
     * @param page
     * @return
     */

   /* @RequestMapping("/getHotGoods")
    public AjaxResponse getHotGoods(HttpServletRequest request, Page page){
        PageData pd = new PageData();
        logBefore(logger, "商城促销商品查询");
        try {
            pd = this.getPageData();
            String currentPage = pd.getString("currentPage");
            String city = pd.getString("city");
            String activityId=pd.getString("activityId");//秒杀自动化
            AjaxResponse ar= new AjaxResponse();
            PageData pageResult =new PageData();
            page.setPd(pd);
            if(StringUtils.isNotEmpty( activityId )){
                if(StringUtils.isNotEmpty(city)){
                    if (cacheManager.isExist(RedisConstantUtil.SHOPHOTGOODS+RedisConstantUtil.ANDCITY+city+RedisConstantUtil.ANDACTIVITYID+activityId+RedisConstantUtil.CURRENTPAGE +currentPage)) {
                        ar = fastReturn(cacheManager.get(RedisConstantUtil.SHOPHOTGOODS+RedisConstantUtil.ANDCITY+city+RedisConstantUtil.ANDACTIVITYID+activityId+RedisConstantUtil.CURRENTPAGE +currentPage), true, "商城促销商cityID为"+city+"活动activityId为"+activityId+"品查询缓存成功！", CodeConstant.SC_OK);
                        logAfter(logger);
                    } else {
                        pageResult = this.shopGoodsService.getHotGoods(page);
                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS+RedisConstantUtil.ANDCITY+city+RedisConstantUtil.ANDACTIVITYID+activityId+RedisConstantUtil.CURRENTPAGE +currentPage, pageResult, 1800);
                        ar = fastReturn(pageResult, true, "商城促销商cityID为"+city+"活动activityId为"+activityId+"品查询成功！",CodeConstant.SC_OK);
                        logAfter(logger);
                    }
                }else{
                    if (cacheManager.isExist(RedisConstantUtil.SHOPHOTGOODS+RedisConstantUtil.ANDACTIVITYID+activityId+RedisConstantUtil.CURRENTPAGE +currentPage)) {
                        ar = fastReturn(cacheManager.get(RedisConstantUtil.SHOPHOTGOODS+RedisConstantUtil.ANDACTIVITYID+activityId+RedisConstantUtil.CURRENTPAGE +currentPage), true, "商城促销商品查询缓存成功！",CodeConstant.SC_OK);
                        logAfter(logger);
                    } else {
                        //PHP 未接分页，接了直接放开
                *//*List result=new ArrayList();
                result = this.shopGoodsService.getShopHotGoods();
                cacheManager.set(RedisConstantUtil.SHOPHOTGOODS+RedisConstantUtil.CURRENTPAGE +currentPage, result, 1800);
                ar = fastReturn(result, true, "商城促销商品查询成功！",CodeConstant.SC_OK);*//*
                        pageResult = this.shopGoodsService.getHotGoods(page);
                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS+RedisConstantUtil.ANDACTIVITYID+activityId+RedisConstantUtil.CURRENTPAGE +currentPage, pageResult, 1800);
                        ar = fastReturn(pageResult, true, "商城促销商品查询成功！",CodeConstant.SC_OK);
                        logAfter(logger);
                    }
                }
            }else{
                return fastReturn(null,false,"查询失败，秒杀活动缺少activityId参数！",CodeConstant.PARAM_ERROR);
            }
        }catch (Exception e){
            logger.info(e.toString(), e);
            ar=fastReturn(null,false,"系统异常，商城促销商品查询失败！",CodeConstant.SYS_ERROR);
        }
        return ar;
    }*/

    /**
     *手机端按定位城市信息查询对应的  商城商品|店铺商品|店铺信息
     */

  /*  @RequestMapping(value="unionQueryByCity",method = RequestMethod.POST)
    public AjaxResponse unionQueryByCity(HttpServletRequest request, Page page){
        try{
            PageData pd = new PageData();
            pd = this.getPageData();
            String info="";
            String cacheInfo="";
            AjaxResponse ar= new AjaxResponse();
            String tagId = pd.getString("tagId");
            String city = pd.getString("city");
            String tagType = pd.getString("tagType");
            String currentPage = pd.getString("currentPage");
            page.setPd(pd);
            if(StringUtils.isNotEmpty(tagId)){
                if(StringUtils.isNotEmpty(tagType)){
                    if("1".equals(tagType)){
                        logger.info("*****************************unionQueryByCity查询的类型为==========>>>>>>>>>"+tagType+"***************查询店铺信息*********************");
                        info="查询店铺city为"+city+"信息成功！";
                        cacheInfo="查询店铺缓存city为"+city+"信息成功";
                    }else if("2".equals(tagType)){
                        logger.info("*****************************unionQueryByCity查询的类型为==========>>>>>>>>>"+tagType+"***************查询店铺商品信息*********************");
                        info="查询店铺商品city为"+city+"信息成功";
                        cacheInfo="查询店铺商品city为"+city+"缓存信息成功";
                    }else if("3".equals(tagType)){
                        logger.info("*****************************unionQueryByCity查询的类型为==========>>>>>>>>>"+tagType+"***************查询商城商品信息*********************");
                        info="查询商城商品city为"+city+"信息成功";
                        cacheInfo="查询商城商品city为"+city+"缓存信息成功";
                    }
                    //执行查询逻辑
                    PageData pageResult =new PageData();
                    if(StringUtils.isNotEmpty(city)) {
                        if (cacheManager.isExist(RedisConstantUtil.UNIONQUERY+RedisConstantUtil.ANDTAGID+tagId+RedisConstantUtil.ANDCITY+city+RedisConstantUtil.ANDTAGTYPE+tagType+RedisConstantUtil.CURRENTPAGE +currentPage)) {
                            ar = fastReturn(cacheManager.get(RedisConstantUtil.UNIONQUERY+RedisConstantUtil.ANDTAGID+tagId+RedisConstantUtil.ANDCITY+city+RedisConstantUtil.ANDTAGTYPE+tagType+RedisConstantUtil.CURRENTPAGE +currentPage), true, cacheInfo, CodeConstant.SC_OK);
                            logAfter(logger);
                        } else {
                            pageResult = this.shopGoodsService.unionQueryByCity(page);
                            cacheManager.set(RedisConstantUtil.UNIONQUERY+RedisConstantUtil.ANDTAGID+tagId+RedisConstantUtil.ANDCITY+city+RedisConstantUtil.ANDTAGTYPE+tagType+RedisConstantUtil.CURRENTPAGE +currentPage, pageResult, 1800);
                            return  fastReturn(pageResult, true, info, CodeConstant.SC_OK);
                        }
                    }else{
                        if (cacheManager.isExist(RedisConstantUtil.UNIONQUERY+RedisConstantUtil.CURRENTPAGE +currentPage)) {
                            return fastReturn(cacheManager.get(RedisConstantUtil.UNIONQUERY+RedisConstantUtil.CURRENTPAGE +currentPage), true, cacheInfo,CodeConstant.SC_OK);
                        }
                        pageResult = this.shopGoodsService.unionQueryByCity(page);
                        cacheManager.set(RedisConstantUtil.UNIONQUERY+RedisConstantUtil.CURRENTPAGE +currentPage, pageResult, 1800);
                        ar = fastReturn(pageResult, true, info,CodeConstant.SC_OK);
                        logAfter(logger);
                    }
                }else{
                    ar=fastReturn(null,false,"系统异常，请求参数tagType有误！",CodeConstant.PARAM_ERROR);
                }
            }else{
                ar=fastReturn(null,false,"系统异常，请求参数tagId有误！",CodeConstant.PARAM_ERROR);
            }
        } catch(Exception e){
            logger.info(e.toString(), e);
            ar=fastReturn(null,false,"系统异常，首页商品搜索失败！",CodeConstant.SYS_ERROR);
        }
        return ar;
    }*/

    /**
     * 手机端本地搜索tag查询
     * @return
     */

   /* @RequestMapping(value="searchTagInfo",method = RequestMethod.POST)
    public AjaxResponse searchTagInfo(HttpServletRequest request, Page page){
        AjaxResponse ar= new AjaxResponse();
        List resultList =new ArrayList();
        logBefore(logger, "手机端本地搜索tag信息");
        try{
            if (cacheManager.isExist(RedisConstantUtil.SEARCHTAGINFO)) {
                ar = fastReturn( cacheManager.get(RedisConstantUtil.SEARCHTAGINFO), true, "查询手机端本地搜索tag信息缓存成功！",CodeConstant.SC_OK);
                logAfter(logger);
            } else {
                resultList = this.shopGoodsService.searchTagInfo();
                cacheManager.set(RedisConstantUtil.SEARCHTAGINFO,resultList, 1800);
                ar = fastReturn(resultList, true, "手机端本地搜索tag信息成功！",CodeConstant.SC_OK);
                logAfter(logger);
            }
        } catch(Exception e){
            logger.info(e.toString(), e);
            ar=fastReturn(null,false,"系统异常，首页商品搜索失败！",CodeConstant.SYS_ERROR);
        }
        return ar;
    }*/

    /**
     *首页商品搜索
     */
    /*@RequestMapping(value="searchMsg/{searchMsg}/{currentPage}")
    public AjaxResponse searchMsg(@PathVariable String searchMsg,@PathVariable String currentPage){*/

   /* @RequestMapping(value="searchMsg",method = RequestMethod.GET)
    public AjaxResponse searchMsg(HttpServletRequest request, Page page){
        PageData pd = new PageData();
        pd = this.getPageData();
        String searchMsg = pd.getString("searchMsg");
        logger.info("*****************************searchMsg=======================>>>>>>>>>"+searchMsg+"************************************************");
        String currentPage = pd.getString("currentPage");
        AjaxResponse ar= new AjaxResponse();
        logBefore(logger, "首页商品搜索");
        try{
            page.setPd(pd);
            if(ShopGoodsWebService.isNotEmpty(searchMsg)){
                PageData pageResult =new PageData();
                if (cacheManager.isExist(RedisConstantUtil.SEARCHGOOD+searchMsg )) {
                    ar = fastReturn( cacheManager.get(RedisConstantUtil.SEARCHGOOD+ searchMsg), true, "首页商品搜索缓存成功！",CodeConstant.SC_OK);
                    logAfter(logger);
                } else {
                    pageResult = this.shopGoodsService.searchGood(page);
                    cacheManager.set(RedisConstantUtil.SEARCHGOOD+searchMsg, pageResult, 1800);
                    ar = fastReturn(pageResult, true, "首页商品搜索成功！",CodeConstant.SC_OK);
                    logAfter(logger);
                }
            }else{
                ar=fastReturn(null,false,"系统异常，请求参数searchMsg有误！",CodeConstant.PARAM_ERROR);
            }
        } catch(Exception e){
            logger.info(e.toString(), e);
            ar=fastReturn(null,false,"系统异常，首页商品搜索失败！",CodeConstant.SYS_ERROR);
        }
        return ar;
    }*/

    /**
     *分类按品牌+类别搜索
     */
   /* @RequestMapping(value="showBySearchInfo",method = RequestMethod.GET)

    public AjaxResponse showBySearchInfo(@RequestParam(value="brandId",required = false) String brandId, @RequestParam(value="catId",required = false) String catId){
        AjaxResponse ar= new AjaxResponse();
        logBefore(logger, "分类按品牌+类别搜索");
        try{
            List result =new ArrayList();
            if(ShopGoodsWebService.isNotEmpty(brandId) && ShopGoodsWebService.isNotEmpty(catId)){
                if(cacheManager.isExist(RedisConstantUtil.SHOWBYSEARCHINFOBRAND+brandId+ RedisConstantUtil.ANDCAT+catId)){
                    ar=fastReturn((List)this.shopGoodsService.showBySearchInfo(brandId,catId),true,"分类按品牌+类别搜索缓存成功！",CodeConstant.SC_OK);
                    logAfter(logger);
                }else{
                    ar=fastReturn(this.shopGoodsService.showBySearchInfo(brandId,catId),true,"分类按品牌+类别搜索成功！",CodeConstant.SC_OK);
                    cacheManager.set(RedisConstantUtil.SHOWBYSEARCHINFOBRAND+brandId+ RedisConstantUtil.ANDCAT+catId,result,1800);
                    logAfter(logger);
                }
            }else if(ShopGoodsWebService.isNotEmpty(catId)) {
                    if(cacheManager.isExist(RedisConstantUtil.SHOWBYSEARCHINFOCAT+catId)){
                        ar=fastReturn((List)this.shopGoodsService.showBySearchInfo(null,catId),true,"类别查询缓存成功！",CodeConstant.SC_OK);
                        logAfter(logger);
                    }else{
                        result =this.shopGoodsService.showBySearchInfo(null,catId);
                        cacheManager.set(RedisConstantUtil.SHOWBYSEARCHINFOCAT+catId,result,1800);
                        ar=fastReturn(result,true,"类别查询成功！",CodeConstant.SC_OK);
                        logAfter(logger);
                    }
            }else if(ShopGoodsWebService.isNotEmpty(brandId)){
                    if(cacheManager.isExist(RedisConstantUtil.SHOWBYSEARCHINFOBRAND+brandId)){
                        ar=fastReturn((List)this.shopGoodsService.showBySearchInfo(brandId,null),true,"分类品牌查询缓存成功！",CodeConstant.SC_OK);
                        logAfter(logger);
                    }else{
                        result =this.shopGoodsService.showBySearchInfo(brandId,null);
                        cacheManager.set(RedisConstantUtil.SHOWBYSEARCHINFOBRAND+brandId,result,1800);
                        ar=fastReturn(result,true,"分类品牌查询成功！",CodeConstant.SC_OK);
                        logAfter(logger);
                    }
                }else{
                ar=fastReturn(null,false,"请求参数有误，分类按品牌+类别搜索失败！",CodeConstant.UNCHECK);
            }
        } catch(Exception e){
            logger.info(e.toString(), e);
            ar=fastReturn(null,false,"系统异常，分类按品牌+类别搜索失败！",CodeConstant.SYS_ERROR);
        }
        return ar;
    }*/

    /**
     *分类按品牌+类别搜索(分页)
     */
    /*@RequestMapping(value="listPageShowBySearchInfo",method = RequestMethod.GET)

    public AjaxResponse listPageShowBySearchInfo(HttpServletRequest request, Page page){
        PageData pd = new PageData();
        pd = this.getPageData();
        page.setPd(pd);
    try{
        AjaxResponse ar= new AjaxResponse();
        String brandId=(String)pd.get("brandId");
        String catId=(String)pd.get("catId");
        logBefore(logger, "分类按品牌+类别分页搜索");//品牌查询直接重定向到   category/showGoods
            List result =new ArrayList();
            if(ShopGoodsWebService.isNotEmpty(brandId) && ShopGoodsWebService.isNotEmpty(catId)){
                if(cacheManager.isExist(RedisConstantUtil.SHOWBYSEARCHINFOBRAND+brandId+ RedisConstantUtil.ANDCAT+catId)){
                    ar=fastReturn((List)this.shopGoodsService.showBySearchInfo(brandId,catId),true,"分类按品牌+类别分页搜索缓存成功！",CodeConstant.SC_OK);
                    logAfter(logger);
                }else{
                    ar=fastReturn(this.shopGoodsService.listPageShowBySearchInfo(page),true,"分类按品牌+类别分页搜索成功！",CodeConstant.SC_OK);
                    cacheManager.set(RedisConstantUtil.SHOWBYSEARCHINFOBRAND+brandId+ RedisConstantUtil.ANDCAT+catId,result,1800);
                    logAfter(logger);
                }
            }else if(ShopGoodsWebService.isNotEmpty(catId)) {
                if(cacheManager.isExist(RedisConstantUtil.SHOWBYSEARCHINFOCAT+catId)){
                    ar=fastReturn((List)this.shopGoodsService.showBySearchInfo(null,catId),true,"类别分页查询缓存成功！",CodeConstant.SC_OK);
                    logAfter(logger);
                }else{
                    result =this.shopGoodsService.listPageShowBySearchInfo(page);
                    cacheManager.set(RedisConstantUtil.SHOWBYSEARCHINFOCAT+catId,result,1800);
                    ar=fastReturn(result,true,"类别分页查询存成功！",CodeConstant.SC_OK);
                    logAfter(logger);
                }
            }else if(ShopGoodsWebService.isNotEmpty(brandId)){
                if(cacheManager.isExist(RedisConstantUtil.SHOWBYSEARCHINFOBRAND+brandId)){
                    ar=fastReturn((List)this.shopGoodsService.showBySearchInfo(brandId,null),true,"分类品牌分页查询缓存成功！",CodeConstant.SC_OK);
                    logAfter(logger);
                }else{
                    result =this.shopGoodsService.listPageShowBySearchInfo(page);
                    cacheManager.set(RedisConstantUtil.SHOWBYSEARCHINFOBRAND+brandId,result,1800);
                    ar=fastReturn(result,true,"分类品牌分页查询成功！",CodeConstant.SC_OK);
                    logAfter(logger);
                }
            }else{
                ar=fastReturn(null,false,"请求参数有误，分类按品牌+类别分页搜索失败！",CodeConstant.UNCHECK);
            }
        } catch (ClassCastException e){
            logger.info(e.toString(), e);
            ar=fastReturn(null,false,"接口参数类型异常，分类按品牌+类别分页搜索失败！",CodeConstant.PARAM_ERROR);
        }catch(NullPointerException e){
            logger.info(e.toString(), e);
            ar=fastReturn(null,false,"接口参数异常，分类按品牌+类别分页搜索失败！",CodeConstant.PARAM_ERROR);
        }catch(Exception e){
            logger.info(e.toString(), e);
            ar=fastReturn(null,false,"系统异常，分类按品牌+类别分页搜索失败！",CodeConstant.SYS_ERROR);
        }
        return ar;
    }*/

    /**
     * 商品详情页搜索
     */

    @PostMapping("showGoodsDetail")
    @ApiOperation(nickname = "showGoodsDetail", value = "商品详情页搜索", notes = "商品详情页搜索！！")
    @ApiImplicitParams({
           /* @ApiImplicitParam(name = "type", value = "查询类型", required = true, paramType = "body", dataType = "BlogArticleBeen"),
            @ApiImplicitParam(name = "path", value = "url上的数据", required = true, paramType = "path", dataType = "Long"),
            @ApiImplicitParam(name = "query", value = "query类型参数", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "apiKey", value = "header中的数据", required = true, paramType = "header", dataType = "String"),*/

            @ApiImplicitParam(name = "catId", value = "分类ID", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsId", value = "商品ID", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "isPromote", value = "商品订单类型 0普通订单 1秒杀订单 2人民币订单", required = true, paramType = "query", dataType = "String"),
    })

    public AjaxResponse showGoodsDetail(HttpServletRequest request,Page page){
        PageData pd = new PageData();
        pd = this.getPageData();
        AjaxResponse ar= new AjaxResponse();
    try{
        String catId=(String)pd.get("catId") ;
        String goodsId=(String)pd.get("goodsId");
        String isPromote=(String)pd.get("isPromote");
        logBefore(logger, "商品详情页搜索");
             if(ShopGoodsWebService.isNotEmpty(catId) && ShopGoodsWebService.isNotEmpty(goodsId)) {
                List result = new ArrayList();
                if (JedisUtil.exists(RedisConstantUtil.SHOWGOODSDETAILCAT + catId+ RedisConstantUtil.ANDGOODS+goodsId)) {
                    ar = fastReturn((List) JedisUtil.getInfo(RedisConstantUtil.SHOWGOODSDETAILCAT + catId+ RedisConstantUtil.ANDGOODS+goodsId), true, "商品详情页搜索缓存成功！",CodeConstant.SC_OK);
                    logAfter(logger);
                } else {
                    result = this.shopGoodsService.showGoodsDetail(catId,goodsId,isPromote);
                    JedisUtil.set(RedisConstantUtil.SHOWGOODSDETAILCAT + catId+ RedisConstantUtil.ANDGOODS+goodsId, result, 1800);
                    ar = fastReturn(result, true, "商品详情页搜索成功！",CodeConstant.SC_OK);
                    logAfter(logger);
                }
             }else if(!ShopGoodsWebService.isNotEmpty(goodsId)){
                 ar=fastReturn(null,false,"系统异常，请求参数goodsId有误！",CodeConstant.PARAM_ERROR);
             }else if(!ShopGoodsWebService.isNotEmpty(catId)){
                 ar=fastReturn(null,false,"系统异常，请求参数catId有误！",CodeConstant.PARAM_ERROR);
             }
        } catch (ClassCastException e){
            logger.info(e.toString(), e);
            ar=fastReturn(null,false,"接口参数类型异常，商品详情页搜索失败！",CodeConstant.PARAM_ERROR);
        }catch(NullPointerException e){
            logger.info(e.toString(), e);
            ar=fastReturn(null,false,"接口参数异常，商品详情页搜索失败！",CodeConstant.PARAM_ERROR);
        }catch(Exception e){
            logger.info(e.toString(), e);
            ar=fastReturn(null,false,"系统异常，商品详情页搜索失败！",CodeConstant.SYS_ERROR);
        }
        return ar;
    }

    /*
     * 猜你喜欢 5个商品
    /*@RequestMapping(value="guessYourLike/{goodsId}")
    public AjaxResponse guessYourLike(@PathVariable String goodsId){*/
    /*@RequestMapping(value="guessYourLike")

    public AjaxResponse guessYourLike(HttpServletRequest request, Page page){
        PageData pd = new PageData();
        pd = this.getPageData();
        AjaxResponse ar= new AjaxResponse();
        logBefore(logger, "猜你喜欢 5个商品");
        try{
            String goodsId=(String)pd.get("goodsId");
            if(ShopGoodsWebService.isNotEmpty(goodsId)) {
                List result = new ArrayList();
                if (cacheManager.isExist(RedisConstantUtil.GUESSYOURLIKE + goodsId)) {
                    ar = fastReturn((List) cacheManager.get(RedisConstantUtil.GUESSYOURLIKE + goodsId), true, "查询猜你喜欢 5个商品缓存成功！", CodeConstant.SC_OK);
                    logAfter(logger);
                } else {
                    result = this.shopGoodsService.guessYourLike(goodsId);
                    cacheManager.set(RedisConstantUtil.GUESSYOURLIKE + goodsId, result, 1800);
                    ar = fastReturn(result, true, "查询猜你喜欢 5个商品成功！",CodeConstant.SC_OK);
                    logAfter(logger);
                }
            }else{
                ar=fastReturn(null,false,"系统异常，请求参数goodsId有误！",CodeConstant.PARAM_ERROR);
            }
        } catch (ClassCastException e){
            logger.info(e.toString(), e);
            ar=fastReturn(null,false,"接口参数类型异常，猜你喜欢搜索失败！",CodeConstant.PARAM_ERROR);
        }catch(NullPointerException e){
            logger.info(e.toString(), e);
            ar=fastReturn(null,false,"接口参数异常，猜你喜欢搜索失败！",CodeConstant.PARAM_ERROR);
        } catch(Exception e){
            logger.info(e.toString(), e);
            ar=fastReturn(null,false,"系统异常，猜你喜欢搜索失败！",CodeConstant.PARAM_ERROR);
        }
        return ar;
    }

    *//**
     * 猜你喜欢接口：查询商品当前最细分类下销售量前5的商品（除去当前商品）
     *//*
    *//*@RequestMapping(value="goodsDetailGuessYourLike/{goodsId}/{type}")
    public AjaxResponse goodsDetailGuessYourLike(@PathVariable String goodsId,@PathVariable String type){*//*
    @RequestMapping(value="goodsDetailGuessYourLike")

    public AjaxResponse goodsDetailGuessYourLike(HttpServletRequest request, Page page){
        PageData pd = new PageData();
        pd = this.getPageData();
        AjaxResponse ar= new AjaxResponse();
        try{
            String goodsId=(String)pd.get("goodsId");
            String type=(String)pd.get("type");
            logBefore(logger, "猜你喜欢接口：查询"+ type+" 商品当前最细分类下销售量前5的商品（除去当前商品）");
            if(ShopGoodsWebService.isNotEmpty(goodsId)) {
                    List result = new ArrayList();
                    if("shop".equals(type)){
                        if (cacheManager.isExist(RedisConstantUtil.GOODSDETAILGUESSYOURLIKE + goodsId)) {
                            ar = fastReturn((List) cacheManager.get(RedisConstantUtil.GOODSDETAILGUESSYOURLIKE + goodsId), true, "查询"+ type+" 商品当前最细分类下销售量前5的商品（除去当前商品）缓存成功！",CodeConstant.SC_OK);
                            logAfter(logger);
                        } else {
                            result = this.shopGoodsService.goodsDetailGuessYourLike(goodsId);
                            cacheManager.set(RedisConstantUtil.GOODSDETAILGUESSYOURLIKE + goodsId, result, 1800);
                            ar = fastReturn(result, true, "查询"+ type+" 商品当前最细分类下销售量前5的商品（除去当前商品）成功！",CodeConstant.SC_OK);
                            logAfter(logger);
                        }
                    }else if("store".equals(type)){
                        if (cacheManager.isExist(RedisConstantUtil.STOREGOODSDETAILGUESSYOURLIKE + goodsId)) {
                            ar = fastReturn((List) cacheManager.get(RedisConstantUtil.STOREGOODSDETAILGUESSYOURLIKE + goodsId), true, "查询"+ type+" 商品当前最细分类下销售量前5的商品（除去当前商品）缓存成功！",CodeConstant.SC_OK);
                            logAfter(logger);
                        } *//*else {
                            result = this.storeGoodsService.goodsDetailGuessYourLike(goodsId);
                            cacheManager.set(RedisConstantUtil.STOREGOODSDETAILGUESSYOURLIKE + goodsId, result, 1800);
                            ar = fastReturn(result, true, "查询"+ type+" 商品当前最细分类下销售量前5的商品（除去当前商品）成功！",CodeConstant.SC_OK);
                            logAfter(logger);
                        }*//*
                    }else{
                        ar=fastReturn(null,false,"系统异常，请求参数type有误！",CodeConstant.PARAM_ERROR);
                    }
              }else{
                    ar=fastReturn(null,false,"系统异常，请求参数goodsId有误！",CodeConstant.PARAM_ERROR);
            }
        }  catch (ClassCastException e){
            logger.info(e.toString(), e);
            ar=fastReturn(null,false,"接口参数类型异常，当前最细分类下销售量前5的商品（除去当前商品）搜索失败！",CodeConstant.PARAM_ERROR);
        }catch(NullPointerException e){
            logger.info(e.toString(), e);
            ar=fastReturn(null,false,"接口参数异常，当前最细分类下销售量前5的商品（除去当前商品）搜索失败！",CodeConstant.PARAM_ERROR);
        }catch(Exception e){
            logger.info(e.toString(), e);
            ar=fastReturn(null,false,"系统异常，当前最细分类下销售量前5的商品（除去当前商品）搜索失败！",CodeConstant.SYS_ERROR);
        }
        return ar;
    }*/


    public static boolean isNotEmpty(String parameter){
        return StringUtils.isNotEmpty(parameter);
    }
}

