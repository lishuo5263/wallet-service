/*package com.ecochain.ledger.web.rest;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.ecochain.ledger.annotation.LoginVerify;
import com.ecochain.ledger.base.BaseWebService;
import com.ecochain.ledger.constants.CodeConstant;
import com.ecochain.ledger.constants.Constant;
import com.ecochain.ledger.constants.CookieConstant;
import com.ecochain.ledger.model.Page;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.model.ShopOrderGoods;
import com.ecochain.ledger.service.ShopGoodsService;
import com.ecochain.ledger.service.ShopOrderGoodsService;
import com.ecochain.ledger.service.ShopOrderInfoService;
import com.ecochain.ledger.service.ShopSupplierService;
import com.ecochain.ledger.service.SysGenCodeService;
import com.ecochain.ledger.service.UserWalletService;
import com.ecochain.ledger.util.AjaxResponse;
import com.ecochain.ledger.util.DateUtil;
import com.ecochain.ledger.util.RedisConstantUtil;
import com.ecochain.ledger.util.RequestUtils;
import com.ecochain.ledger.util.StringUtil;

*//**
 * Created by LiShuo on 2016/10/28.
 *//*
@Controller
@RequestMapping("/shopOrder")
public class ShopOrderWebService extends BaseWebService {

    @Autowired
    private ShopOrderInfoService shopOrderInfoService;
    @Autowired
    private ShopGoodsService shopGoodsService;
    @Autowired
    private ShopOrderGoodsService shopOrderGoodsService;
    @Autowired
    private SessionUtil sessionUtil;
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private SysGenCodeService sysGenCodeService;
    @Autowired
    private UserWalletService userWalletService;
    @Autowired
    private ShopSupplierService shopSupplierService;

    *//**
     * @describe 商城&店铺取消订单
     * @author: lishuo
     * @date 2016-12-12
     *//*
    @LoginVerify
    @RequestMapping(value = "/cancleOrder")
    @ResponseBody
    public AjaxResponse cancleOrder(HttpServletRequest request, Page page) {
        Map<String, Object> data = new HashMap<String, Object>();
        AjaxResponse ar = new AjaxResponse();
        String type = request.getParameter("type");
        String orderNo = request.getParameter("orderNo");
        if (StringUtil.isNotEmpty(type)) {
            if (StringUtil.isNotEmpty(orderNo)) {
                //根据type去查询时shop 还是store订单 暂时商城秒杀订单不支持取消订单操作
                if ("shop".equals(type)) {
                    data = this.shopOrderInfoService.getOrderInfo(type, orderNo);
                    if (data != null && data.size() > 0 &&"1".equals(data.get("order_status").toString()) ) {
                        if ("0".equals(data.get("is_promote").toString())) {
                            if (this.shopOrderInfoService.updateCancleState(type, orderNo, "", "")) {
                                return fastReturn("取消商城订单成功！", true, "取消商城订单成功！", CodeConstant.SC_OK);
                            }
                        } else if ("1".equals(data.get("is_promote").toString())) {
                            if (this.shopOrderInfoService.updateCancleState(type, orderNo, "", data.get("is_promote").toString())) {
                                return fastReturn("取消秒杀商城订单成功！", true, "取消秒杀商城订单成功！", CodeConstant.SC_OK);
                            }
                        }
                    }else{
                        return fastReturn(data, true, "取消商城订单失败，订单号为  "+orderNo+" 的数据不满足要求！", CodeConstant.SYS_ERROR);
                    }
                } else if ("store".equals(type)) {
                    data = this.shopOrderInfoService.getOrderInfo(type, orderNo);
                    if (data != null && data.size() > 0 && "0".equals(data.get("order_status").toString())) {
                        //进行update 订单操作
                        if ("1".equals(data.get("is_hot").toString())) {
                            if (this.shopOrderInfoService.updateCancleState(type, orderNo, data.get("is_hot").toString(), "")) {
                                return fastReturn("取消爆款店铺订单成功！", true, "取消爆款店铺订单成功！", CodeConstant.SC_OK);
                            }
                        } else if ("0".equals(data.get("is_hot").toString())) {
                            if (this.shopOrderInfoService.updateCancleState(type, orderNo, "", "")) {
                                return fastReturn("取消店铺订单成功！", true, "取消店铺订单成功！", CodeConstant.SC_OK);
                            }
                        }
                    } else {
                        return fastReturn(data, true, "取消店铺订单失败，订单号为  "+orderNo+"  的数据不满足要求！", CodeConstant.SYS_ERROR);
                    }
                } else {
                    return fastReturn(null, false, "取消订单失败，数据库查询不到订单类型为->" + type + " 订单号为：" + orderNo + " 的订单！", CodeConstant.PARAM_ERROR);
                }
            } else {
                return fastReturn(null, false, "取消订单失败，orderNo参数为空！", CodeConstant.PARAM_ERROR);
            }
        } else {
            return fastReturn(null, false, "取消订单失败，type参数为空！", CodeConstant.PARAM_ERROR);
        }
        return ar;
    }

    //@LoginVerify
    @ResponseBody
    @RequestMapping(value = "insertShopOrder", method = RequestMethod.POST, consumes = "application/json")
    public synchronized AjaxResponse insertShopOrder(@RequestBody String shopOrderGoods, HttpServletRequest request) throws Exception {
        AjaxResponse ar = new AjaxResponse();
        List<ShopOrderGoods> shopOrderGood = null;
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, ShopOrderGoods.class);
            objectMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            shopOrderGood = objectMapper.readValue(shopOrderGoods, javaType);
            String userstr = sessionUtil.getAttibuteForUser(Base64.getFromBase64(shopOrderGood.get(0).getCsessionid()));
            JSONObject user = JSONObject.parseObject(userstr);
            if(user == null || !user.containsKey("seeds")){
                return fastReturn(null, false, "下单失败，登录超时，请重新登陆！", CodeConstant.UNLOGIN);
            }
            System.out.println("sessionKey中用户信息------------>"+user.toJSONString());
            if (!StringUtil.isNotEmpty(String.valueOf(shopOrderGood.get(0).getUserId()))) {
                return fastReturn(null, false, "订单生成失败，userId参数为空！", CodeConstant.PARAM_ERROR);
            } else if (!StringUtil.isNotEmpty(String.valueOf(shopOrderGood.get(0).getAddressId()))) {
                return fastReturn(null, false, "订单生成失败，addressId参数为空！", CodeConstant.PARAM_ERROR);
            } else if (!StringUtil.isNotEmpty(shopOrderGood.get(0).getPostscript())) {
                return fastReturn(null, false, "订单生成失败，postscript参数为空！", CodeConstant.PARAM_ERROR);
            } else if (!StringUtil.isNotEmpty(shopOrderGood.get(0).getIsPromote())) {
                return fastReturn(null, false, "订单生成失败，isPromote参数为空！", CodeConstant.PARAM_ERROR);
            }

            for (int i = 0; i < shopOrderGood.size(); i++) {
                if (Integer.valueOf(shopOrderGood.get(i).getGoodsNumber()) == 0) {
                    return fastReturn(null, false, "订单生成失败，订单商品数量不能为空！", CodeConstant.PARAM_ERROR);
                } else if (!StringUtil.isNotEmpty(shopOrderGood.get(i).getSkuInfo())) {
                    shopOrderGood.get(i).setSkuInfo("无sku信息");
                    //return fastReturn(null, false, "订单生成失败，第"+i+"个商品信息中 skuInfo参数为空！",CodeConstant.PARAM_ERROR);
                } else if (!StringUtil.isNotEmpty(String.valueOf(shopOrderGood.get(i).getGoodsNumber()))) {
                    return fastReturn(null, false, "订单生成失败，第" + i + "个商品信息中 goodsNumber参数为空！", CodeConstant.PARAM_ERROR);
                } else if (!StringUtil.isNotEmpty(String.valueOf(shopOrderGood.get(0).getGoodsId()))) {
                    return fastReturn(null, false, "订单生成失败，第" + i + "个商品信息中 goodsId参数为空！", CodeConstant.PARAM_ERROR);
                } else if (!StringUtil.isNotEmpty(String.valueOf(shopOrderGood.get(0).getIsPromote()))) {
                    return fastReturn(null, false, "订单生成失败，第" + i + "个商品信息中 idPromote参数为空！", CodeConstant.PARAM_ERROR);
                }
            }
            if (shopOrderGood != null && shopOrderGood.get(0).getCsessionid().length() >= 32) {
                if ("0".equals(shopOrderGood.get(0).getIsPromote())) {  //正常商品下单
                    logger.info("--------商城普通下单参数shopOrderGood：" + shopOrderGoods);
                    logBefore(logger, "生成用户ID为： " + shopOrderGood.get(0).getUserId() + "+的订单");
                    List<Map<String, Object>> result = new ArrayList();
                    shopOrderGood.get(0).setOrderNo(OrderGenerater.generateOrderNo(shopOrderGood.get(0).getUserCode()));
                    shopOrderGood.get(0).setOrderStatus(2);
                    shopOrderGood.get(0).setUserId((Integer)user.get("id"));
                    shopOrderGood.get(0).setShippingFee(new BigDecimal(0));
                    shopOrderGood.get(0).setIntegralMoney(new BigDecimal(0));
                    shopOrderGood.get(0).setTradeHash(user.getString("seeds"));
                    result = this.shopOrderInfoService.insertShopOrder(shopOrderGood);
                    if (result.get(0).get("ErrorInsert") != null) {
                        return fastReturn(result, false, "订单生成失败，goodsId参数为空！", CodeConstant.PARAM_ERROR);
                    } else if (result.get(0).get("ErrorInsertByUsersType") != null) {
                        return fastReturn(result, false, "订单生成失败，usersType参数为空无法确定购买价格！", CodeConstant.ERROR_NO_USERTYPE);
                    } else if (result.get(0).get("ErrorInsertNotAllowByUsersType") != null) {
                        return fastReturn(result, false, "订单生成失败，只有买家才可以购买商品哦！", CodeConstant.ERROR_NO_PERMISSION);
                    } else if (result.get(0).get("ErrorInsertByBlockChain") != null) {
                        return fastReturn(result, false, "订单生成失败，调用区块链接口发生错误", CodeConstant.ERROR_BLOCKCHAIN);
                    } else {
                        ar = fastReturn(result, true, "生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单成功！", CodeConstant.SC_OK);
                    }
                } else if ("1".equals(shopOrderGood.get(0).getIsPromote())) {  //促销商品下单
                    if (shopOrderGood.get(0).getGoodsNumber() > 1) {
                        return fastReturn(null, false, "订单生成失败，秒杀商品每次只能购买1件！", CodeConstant.ERROR_NO_REPEATSECKILLGOODS);
                    }
                    logger.info("--------商城秒殺下单参数shopOrderGood：" + shopOrderGoods);
                    List<Map<String, Object>> result = new ArrayList();
                    if (Integer.valueOf(shopOrderGood.get(0).getActivityId()) > 0) {
                        //先查询秒杀模板开启状态和开启时间和结束时间进行下单逻辑判断
                        Map activeInfo = this.shopOrderGoodsService.queryActiveInfo(shopOrderGood.get(0).getActivityId());
                        if (activeInfo != null) {
                            if (StringUtil.isNotEmpty("start_time") && StringUtil.isNotEmpty("end_time")) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                String dateString_01 = activeInfo.get("start_time").toString();
                                String dateString_02 = activeInfo.get("end_time").toString();
                                Date startTime = sdf.parse(dateString_01);
                                Date endTime = sdf.parse(dateString_02);
                                Date sysTime = sdf.parse(sdf.format(new Date()));
                                if (sysTime.compareTo(startTime) == -1) {
                                    return fastReturn(result, false, "下单失败，秒杀活动暂未开始！", CodeConstant.ERROR_SECKILL_UNOPEN);
                                } else if (sysTime.compareTo(endTime) == 1) {
                                    return fastReturn(result, false, "下单失败，秒杀活动已经结束！！", CodeConstant.ERROR_ACTIVITE_END);
                                } else if ((sysTime.compareTo(startTime) == 1) && (sysTime.compareTo(endTime) == 0)) {
                                    return this.insertShopOrder(shopOrderGood);
                                } else if ((sysTime.compareTo(startTime) == 1) && (sysTime.compareTo(endTime) == -1)) {
                                    return this.insertShopOrder(shopOrderGood);
                                }
                                Integer hour=Integer.valueOf(activeInfo.get("start_time").toString().substring(11,13)); //模板开始 小时
                                Integer minute=Integer.valueOf(activeInfo.get("start_time").toString().substring(14,16)); //模板开始 分钟
                                Integer mouth=Integer.valueOf(activeInfo.get("start_time").toString().substring(5,7)); //模板开始 月份
                                Integer date=Integer.valueOf(activeInfo.get("start_time").toString().substring(8,10));  //模板开始 日
                                Integer endHour=Integer.valueOf(activeInfo.get("end_time").toString().substring(11,13)); //模板结束 小时
                                Integer endMinute=Integer.valueOf(activeInfo.get("end_time").toString().substring(14,16)); //模板结束 份数
                                Integer endMouth=Integer.valueOf(activeInfo.get("start_time").toString().substring(5,7));  //模板结束 月份
                                Integer endDate=Integer.valueOf(activeInfo.get("end_time").toString().substring(8,10));     //模板结束 日
                                Integer sysHour=Integer.valueOf(DateUtil.formatDate(new Date(),"HH:mm:ss").substring(0,2)); //系统小时
                                Integer sysMinute=Integer.valueOf(DateUtil.formatDate(new Date(),"HH:mm:ss").substring(3,5)); //系统分钟
                                Integer sysMouth=Integer.valueOf(DateUtil.formatDate(new Date(),"MM-dd").substring(0,2)); //系统月份
                                Integer sysDate=Integer.valueOf(DateUtil.formatDate(new Date(),"MM-dd").substring(3,5));  //系统日
                                String sysYear = DateUtil.formatDate(new Date(), "yyyy");  //系统年
                                String year = activeInfo.get("start_time").toString().substring(0, 4); //模板开始 年
                                String endYear = activeInfo.get("end_time").toString().substring(0, 4); //模板开始 年
                                if (sysYear.equals(year) && sysYear.equals(endYear)) { //年内活动
                                    if ((mouth == endMouth && mouth == sysMouth) && (sysDate >= date && sysDate <= endDate)) {
                                         if(sysDate <= endDate && sysDate >= date){
                                            if(sysDate == endDate){
                                                if(sysHour == endHour){
                                                    if(endMinute < sysMinute){  //21   22
                                                        return fastReturn(result, false, "下单失败，秒杀活动已经结束！",CodeConstant.ERROR_ACTIVITE_END);
                                                    }
                                                    return this.insertShopOrder(shopOrderGood);
                                                }else if(sysHour >= endHour) {
                                                    return this.insertShopOrder(shopOrderGood);
                                                }
                                            }else if(sysDate ==date){
                                                if (sysHour == hour){
                                                    if(endMinute < sysMinute){  //21   22
                                                        return fastReturn(result, false, "下单失败，爆款活动已经结束！",CodeConstant.ERROR_ACTIVITE_END);
                                                    }
                                                    if(sysMinute < minute){  //21   22
                                                        return fastReturn(result, false, "下单失败，秒杀活动暂未开始！",CodeConstant.ERROR_SECKILL_UNOPEN);
                                                    }
                                                    return this.insertShopOrder(shopOrderGood);
                                                }else if(sysHour >= hour) {  //20   19    02   30  && sysMinute >= minute
                                                    return this.insertShopOrder(shopOrderGood);
                                                }else{
                                                    return fastReturn(result, false, "下单失败，秒杀活动暂未开始！",CodeConstant.ERROR_SECKILL_UNOPEN);
                                                }
                                            }
                                        } else if (sysDate < endDate && sysDate >= date) {
                                             return this.insertShopOrder(shopOrderGood);
                                         }
                                    } else {
                                        return fastReturn(result, false, "下单失败，秒杀活动已经结束！",CodeConstant.ERROR_ACTIVITE_END);
                                    }
                                } else if (!sysYear.equals(year) && endYear.equals(sysYear)) { //跨年活动 已经第二年
                                    if (sysDate < endDate && sysDate < date) {
                                        return this.insertShopOrder(shopOrderGood);
                                    } else if (sysDate == endDate) {
                                        if (sysHour == endHour) {
                                            if (endMinute < sysMinute) {
                                                return fastReturn(result, false, "下单失败，秒杀活动已经结束！！",CodeConstant.ERROR_ACTIVITE_END);
                                            }
                                            return this.insertShopOrder(shopOrderGood);
                                        } else if (sysHour <= endHour) {
                                            return this.insertShopOrder(shopOrderGood);
                                        }else{
                                            return fastReturn(result, false, "下单失败，秒杀活动已经结束！！",CodeConstant.ERROR_ACTIVITE_END);
                                        }
                                    }
                                }else{
                                    return fastReturn(result, false, "下单失败，秒杀活动暂未开始！！",CodeConstant.ERROR_SECKILL_UNOPEN);
                                }
                            } else {
                                return fastReturn(result, false, "下单失败，秒杀活动模板配置秒杀开始时间和结束时间有误！", CodeConstant.PARAM_ERROR);
                            }
                        } else {
                            return fastReturn(result, false, "下单失败，无此秒杀活动！", CodeConstant.ERROR_NO_ACTIVITY);
                        }
                    } else {
                        return fastReturn(result, false, "下单失败，ActivityId值不正确，ActivityId值为" + shopOrderGood.get(0).getActivityId() + "！", CodeConstant.PARAM_ERROR);
                    }
                } else {
                    ar = fastReturn(null, false, "订单生成失败，csessionid参数为空！", CodeConstant.PARAM_ERROR);
                }
                logAfter(logger);
            } else {
                return fastReturn(null, false, "下单失败，用户未登陆！", CodeConstant.UNLOGIN);
            }
        } catch (NullPointerException e) {
            logger.debug(e.toString(), e);
            ar = fastReturn(null, false, "系统异常，生成用户ID为: " + shopOrderGood.get(0).getUserId() + "的订单失败！！", CodeConstant.PARAM_ERROR);
        } catch (Exception e) {
            logger.debug(e.toString(), e);
            ar = fastReturn(null, false, "系统异常，生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单失败！", CodeConstant.SYS_ERROR);
        }
        return ar;
    }

    *//**
     * 秒杀订单公共方法
     *
     * @param shopOrderGood
     * @return
     * @author lishuo
     * @date 2017-1-6 15:46:58
     *//*
    private synchronized AjaxResponse insertShopOrder(List<ShopOrderGoods> shopOrderGood) throws Exception {
        String SecKillState = "false";
        boolean redisCategoryKeyFlag = false;
        List<Map<String, Object>> result = new ArrayList();
        List<PageData> codeList = this.sysGenCodeService.findByGroupCode("SECKILL_STATUS", Constant.VERSION_NO);
        for (Map<String, Object> codeMap1 : codeList) {
            if ("SECKILL_STATUS".equals(codeMap1.get("code_name"))) {
                SecKillState = codeMap1.get("code_value").toString();
            }
        }
        if ("true".equals(SecKillState)) {
            //2016年12月17日 最新规则每人不限制场地一天只能秒杀一个商品
            int secKillCount = this.shopOrderInfoService.querySecKillCount(shopOrderGood.get(0).getUserId());
            if (secKillCount < 1) {
            
            *1.查询Redis看是否有库存缓存key，无---->查SQL set库存key， 有直接2
            *2.按userId&goodsId查询此用户当天是否购买过此商品，无----->缓存key值-1
            *3.下单前查询此商品的库存， if库存量 >0  进行库存-1， 可进行下单
            *4.下单若失败，对Redis缓存key值进行+1 db库存+1
            *5 TODO 此规则未做多库存商品减库存逻辑
            
                redisCategoryKeyFlag = cacheManager.isExist(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId());//秒杀商品等同立即购买1订单1个商品
                if (redisCategoryKeyFlag) { //该秒杀商品有库存
                    if ((Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) > 0) {
                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) - 1, 60 * 60 * 24);
                        Integer buyConut = this.shopOrderGoodsService.queryGoodsByCount(shopOrderGood.get(0).getUserId(), shopOrderGood.get(0).getGoodsId());
                        if (buyConut == 0) {
                            String type = "del";
                            Integer goodStock = this.shopGoodsService.queryGoodsStock(shopOrderGood.get(0).getGoodsId());
                            if (goodStock > 0) {
                                boolean flag = this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                if (flag) {
                                    try {  //执行秒杀下单操做
                                        type = "add";
                                        shopOrderGood.get(0).setOrderStatus(1);
                                        shopOrderGood.get(0).setShippingFee(new BigDecimal(0));
                                        shopOrderGood.get(0).setIntegralMoney(new BigDecimal(0));
                                        shopOrderGood.get(0).setOrderNo(OrderGenerater.generateOrderNo(shopOrderGood.get(0).getUserCode()));
                                        result = this.shopOrderInfoService.insertShopOrder(shopOrderGood);
                                        if (result.get(0).get("ErrorInsert") != null) {
                                            this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                            return fastReturn(result, false, "订单生成失败，goodsId参数为空！", CodeConstant.PARAM_ERROR);
                                        } else if (result.get(0).get("ErrorInsertByUsersType") != null) {
                                            this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                            return fastReturn(result, false, "订单生成失败，usersType参数为空无法确定购买价格！", CodeConstant.ERROR_NO_USERTYPE);
                                        } else if (result.get(0).get("ErrorInsertNotAllowByUsersType") != null) {
                                            this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                            return fastReturn(result, false, "订单生成失败，用户类型只能为普通会员和创业会员，商品无法购买！", CodeConstant.ERROR_NO_PERMISSION);
                                        } else if (result != null && result.size() > 0) {
                                            return fastReturn(result, true, "生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单成功！", CodeConstant.SC_OK);
                                        } else {
                                            this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                            return fastReturn(result, false, "系统异常，生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单失败！", CodeConstant.SYS_ERROR);
                                        }
                                    } catch (Exception e) {
                                        type = "add";
                                        this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                        logger.info(e.toString(), e);
                                        return fastReturn(null, false, "系统异常，生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单失败！", CodeConstant.SYS_ERROR);
                                    }
                                } else {
                                    type = "add";
                                    boolean addGoodStockResult = this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                    if (addGoodStockResult) {
                                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                        return fastReturn(null, false, "减库存时发生异常，订单生成失败！", CodeConstant.SYS_ERROR);
                                    }
                                }
                            } else {
                                cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), -1, 60 * 60 * 24);
                                return fastReturn(null, false, "订单生成失败，此款秒杀商品已无库存！", CodeConstant.SYS_HOTGOODSKILL_ERROR);
                            }
                        } else {
                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                            return fastReturn(null, false, "订单生成失败，用户不能重复秒杀同款商品！", CodeConstant.SYS_HOTGOODSREPEATKILL_ERROR);
                        }
                    } else {
                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), -1, 60 * 60 * 24);
                        return fastReturn(null, false, "订单生成失败，此款秒杀商品已无库存！", CodeConstant.SYS_HOTGOODSKILL_ERROR);
                    }
                } else {
                    String goodsStock = this.shopGoodsService.getGoodsStock(String.valueOf(shopOrderGood.get(0).getGoodsId())); //从数据库获取秒杀商品个数并设置缓存
                    if (Integer.valueOf(goodsStock) > 0) {
                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), Integer.valueOf(goodsStock) - 1, 60 * 60 * 24);
                        Integer buyConut = this.shopOrderGoodsService.queryGoodsByCount(shopOrderGood.get(0).getUserId(), shopOrderGood.get(0).getGoodsId());
                        if (buyConut == 0) {
                            String type = "del";
                            Integer goodStock = this.shopGoodsService.queryGoodsStock(shopOrderGood.get(0).getGoodsId());
                            if (goodStock > 0) {
                                boolean flag = this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                if (flag) {
                                    try {  //执行秒杀下单操做
                                        type = "add";
                                        shopOrderGood.get(0).setOrderStatus(1);
                                        shopOrderGood.get(0).setShippingFee(new BigDecimal(0));
                                        shopOrderGood.get(0).setIntegralMoney(new BigDecimal(0));
                                        shopOrderGood.get(0).setOrderNo(OrderGenerater.generateOrderNo(shopOrderGood.get(0).getUserCode()));
                                        result = this.shopOrderInfoService.insertShopOrder(shopOrderGood);
                                        if (result.get(0).get("ErrorInsert") != null) {
                                            this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                            return fastReturn(result, false, "订单生成失败，goodsId参数为空！", CodeConstant.PARAM_ERROR);
                                        } else if (result.get(0).get("ErrorInsertByUsersType") != null) {
                                            this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                            return fastReturn(result, false, "订单生成失败，usersType参数为空无法确定购买价格！", CodeConstant.ERROR_NO_USERTYPE);
                                        } else if (result.get(0).get("ErrorInsertNotAllowByUsersType") != null) {
                                            this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                            return fastReturn(result, false, "订单生成失败，用户类型只能为普通会员和创业会员，商品无法购买！", CodeConstant.ERROR_NO_PERMISSION);
                                        } else if (result != null && result.size() > 0) {
                                            return fastReturn(result, true, "生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单成功！", CodeConstant.SC_OK);
                                        } else {
                                            this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                            return fastReturn(result, false, "系统异常，生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单失败！", CodeConstant.SYS_ERROR);
                                        }
                                    } catch (Exception e) {
                                        type = "add";
                                        this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                        logger.info(e.toString(), e);
                                        return fastReturn(null, false, "系统异常，生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单失败！", CodeConstant.SYS_ERROR);
                                    }
                                } else {
                                    type = "add";
                                    boolean addGoodStockResult = this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                    if (addGoodStockResult) {
                                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                        return fastReturn(null, false, "减库存时发生异常，订单生成失败！", CodeConstant.SYS_ERROR);
                                    }
                                }
                            } else {
                                cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), -1, 60 * 60 * 24);
                                return fastReturn(null, false, "订单生成失败，此款秒杀商品已无库存！", CodeConstant.SYS_HOTGOODSKILL_ERROR);
                            }
                        } else {
                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                            return fastReturn(null, false, "订单生成失败，用户不能重复秒杀同款商品！", CodeConstant.SYS_HOTGOODSREPEATKILL_ERROR);
                        }
                    } else {
                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), -1, 60 * 60 * 24);
                        return fastReturn(null, false, "订单生成失败，此款秒杀商品已无库存！", CodeConstant.SYS_HOTGOODSKILL_ERROR);
                    }
                }
            } else {
                return fastReturn(secKillCount, false, "订单生成失败，用户每次秒杀活动只能购买一件商品！", CodeConstant.ERROR_NO_REPEATSECKILL);
            }
        } else {
            
            *  ！！不限制用户每场秒杀商品数量但仅每个秒杀商品只能购买一次！！
            *1.查询Redis看是否有库存缓存key，无---->查SQL set库存key， 有直接2
            *2.按userId&goodsId查询此用户当天是否购买过此商品，无----->缓存key值-1
            *3.下单前查询此商品的库存， if库存量 >0  进行库存-1， 可进行下单
            *4.下单若失败，对Redis缓存key值进行+1 db库存+1
            *5.秒杀减库存减少多SKU的库存
            
            if (shopOrderGood.get(0).getIsMutilPrice() != null) {   //SKU秒杀商品下单逻辑
                if (shopOrderGood.get(0).getIsMutilPrice() == 1) { //多价格减库存下单
                    if (StringUtil.isNotEmpty(shopOrderGood.get(0).getSkuValue())) {
                        redisCategoryKeyFlag = cacheManager.isExist(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId());//秒杀商品等同立即购买1订单1个商品
                        if (redisCategoryKeyFlag) { //该秒杀商品有库存
                            Map skuGoods = this.shopGoodsService.getSkuGoodsInfo(shopOrderGood.get(0).getSkuValue()); //从数据库获取秒杀商品个数并设置缓存
                            if (skuGoods != null && Integer.valueOf(skuGoods.get("gmp_goods_id").toString()).intValue() == shopOrderGood.get(0).getGoodsId()) {
                                if ((Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) > 0) {
                                    cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) - 1, 60 * 60 * 24);
                                    Integer buyConut = this.shopOrderGoodsService.queryGoodsByCount(shopOrderGood.get(0).getUserId(), shopOrderGood.get(0).getGoodsId());
                                    if (buyConut == 0) {
                                        String type = "del";
                                        //Integer goodStock=this.shopGoodsService.queryMutityGoodsStock(shopOrderGood.get(0).getSkuValue());
                                        if (skuGoods.get("gmp_stock") != null && Integer.valueOf(skuGoods.get("gmp_stock").toString()) > 0) {
                                            boolean flag = this.shopGoodsService.updateMutityGoodStockByType(type, shopOrderGood.get(0).getSkuValue());
                                            if (flag) {
                                                try {  //执行秒杀下单操做
                                                    type = "add";
                                                    shopOrderGood.get(0).setOrderStatus(1);
                                                    shopOrderGood.get(0).setShippingFee(new BigDecimal(0));
                                                    shopOrderGood.get(0).setIntegralMoney(new BigDecimal(0));
                                                    shopOrderGood.get(0).setOrderNo(OrderGenerater.generateOrderNo(shopOrderGood.get(0).getUserCode()));
                                                    result = this.shopOrderInfoService.insertShopOrder(shopOrderGood);
                                                    if (result.get(0).get("ErrorInsert") != null) {
                                                        this.shopGoodsService.updateMutityGoodStockByType(type, shopOrderGood.get(0).getSkuValue());
                                                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                                        return fastReturn(result, false, "订单生成失败，goodsId参数为空！", CodeConstant.PARAM_ERROR);
                                                    } else if (result.get(0).get("ErrorInsertByUsersType") != null) {
                                                        this.shopGoodsService.updateMutityGoodStockByType(type, shopOrderGood.get(0).getSkuValue());
                                                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                                        return fastReturn(result, false, "订单生成失败，usersType参数为空无法确定购买价格！", CodeConstant.ERROR_NO_USERTYPE);
                                                    } else if (result.get(0).get("ErrorInsertNotAllowByUsersType") != null) {
                                                        this.shopGoodsService.updateMutityGoodStockByType(type, shopOrderGood.get(0).getSkuValue());
                                                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                                        return fastReturn(result, false, "订单生成失败，用户类型只能为普通会员和创业会员，商品无法购买！", CodeConstant.ERROR_NO_PERMISSION);
                                                    } else if (result != null && result.size() > 0) {
                                                        return fastReturn(result, true, "生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单成功！", CodeConstant.SC_OK);
                                                    } else {
                                                        this.shopGoodsService.updateMutityGoodStockByType(type, shopOrderGood.get(0).getSkuValue());
                                                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                                        return fastReturn(result, false, "系统异常，生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单失败！", CodeConstant.SYS_ERROR);
                                                    }
                                                } catch (Exception e) {
                                                    type = "add";
                                                    this.shopGoodsService.updateMutityGoodStockByType(type, shopOrderGood.get(0).getSkuValue());
                                                    cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                                    logger.info(e.toString(), e);
                                                    return fastReturn(null, false, "系统异常，生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单失败！", CodeConstant.SYS_ERROR);
                                                }
                                            } else {
                                                type = "add";
                                                boolean addGoodStockResult = this.shopGoodsService.updateMutityGoodStockByType(type, shopOrderGood.get(0).getSkuValue());
                                                if (addGoodStockResult) {
                                                    cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                                    return fastReturn(null, false, "减库存时发生异常，订单生成失败！", CodeConstant.SYS_ERROR);
                                                }
                                            }
                                        } else {
                                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), -1, 60 * 60 * 24);
                                            return fastReturn(null, false, "订单生成失败，此款秒杀商品已无库存！", CodeConstant.SYS_HOTGOODSKILL_ERROR);
                                        }
                                    } else {
                                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                        return fastReturn(null, false, "订单生成失败，用户不能重复秒杀同款商品！", CodeConstant.SYS_HOTGOODSREPEATKILL_ERROR);
                                    }
                                } else {
                                    cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), -1, 60 * 60 * 24);
                                    return fastReturn(null, false, "订单生成失败，此款秒杀商品已无库存！", CodeConstant.SYS_HOTGOODSKILL_ERROR);
                                }
                            } else {
                                return fastReturn(null, false, "订单生成失败，下单商品属性与数据库不一致！", CodeConstant.ERROR_SKU_NOT_SAME);
                            }
                        } else {
                            Map skuGoods = this.shopGoodsService.getSkuGoodsInfo(shopOrderGood.get(0).getSkuValue()); //从数据库获取秒杀商品个数并设置缓存
                            if (skuGoods != null && Integer.valueOf(skuGoods.get("gmp_goods_id").toString()).intValue() == shopOrderGood.get(0).getGoodsId()) {
                                if (Integer.valueOf(skuGoods.get("gmp_stock").toString()) > 0) {
                                    cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), Integer.valueOf(skuGoods.get("gmp_stock").toString()) - 1, 60 * 60 * 24);
                                    Integer buyConut = this.shopOrderGoodsService.queryGoodsByCount(shopOrderGood.get(0).getUserId(), shopOrderGood.get(0).getGoodsId());
                                    if (buyConut == 0) {
                                        String type = "del";
                                        //this.shopGoodsService.queryMutityGoodsStock(shopOrderGood.get(0).getSkuValue());
                                        if (skuGoods.get("gmp_stock") != null && Integer.valueOf(skuGoods.get("gmp_stock").toString()) > 0) {
                                            boolean flag = this.shopGoodsService.updateMutityGoodStockByType(type, shopOrderGood.get(0).getSkuValue());
                                            if (flag) {
                                                try {  //执行秒杀下单操做
                                                    type = "add";
                                                    shopOrderGood.get(0).setOrderStatus(1);
                                                    shopOrderGood.get(0).setShippingFee(new BigDecimal(0));
                                                    shopOrderGood.get(0).setIntegralMoney(new BigDecimal(0));
                                                    shopOrderGood.get(0).setOrderNo(OrderGenerater.generateOrderNo(shopOrderGood.get(0).getUserCode()));
                                                    result = this.shopOrderInfoService.insertShopOrder(shopOrderGood);
                                                    if (result.get(0).get("ErrorInsert") != null) {
                                                        this.shopGoodsService.updateMutityGoodStockByType(type, shopOrderGood.get(0).getSkuValue());
                                                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                                        return fastReturn(result, false, "订单生成失败，goodsId参数为空！", CodeConstant.PARAM_ERROR);
                                                    } else if (result.get(0).get("ErrorInsertByUsersType") != null) {
                                                        this.shopGoodsService.updateMutityGoodStockByType(type, shopOrderGood.get(0).getSkuValue());
                                                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                                        return fastReturn(result, false, "订单生成失败，usersType参数为空无法确定购买价格！", CodeConstant.ERROR_NO_USERTYPE);
                                                    } else if (result.get(0).get("ErrorInsertNotAllowByUsersType") != null) {
                                                        this.shopGoodsService.updateMutityGoodStockByType(type, shopOrderGood.get(0).getSkuValue());
                                                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                                        return fastReturn(result, false, "订单生成失败，用户类型只能为普通会员和创业会员，商品无法购买！", CodeConstant.ERROR_NO_PERMISSION);
                                                    } else if (result != null && result.size() > 0) {
                                                        return fastReturn(result, true, "生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单成功！", CodeConstant.SC_OK);
                                                    } else {
                                                        this.shopGoodsService.updateMutityGoodStockByType(type, shopOrderGood.get(0).getSkuValue());
                                                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                                        return fastReturn(result, false, "系统异常，生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单失败！", CodeConstant.SYS_ERROR);
                                                    }
                                                } catch (Exception e) {
                                                    type = "add";
                                                    this.shopGoodsService.updateMutityGoodStockByType(type, shopOrderGood.get(0).getSkuValue());
                                                    cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                                    logger.info(e.toString(), e);
                                                    return fastReturn(null, false, "系统异常，生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单失败！", CodeConstant.SYS_ERROR);
                                                }
                                            } else {
                                                type = "add";
                                                boolean addGoodStockResult = this.shopGoodsService.updateMutityGoodStockByType(type, shopOrderGood.get(0).getSkuValue());
                                                if (addGoodStockResult) {
                                                    cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                                    return fastReturn(null, false, "减库存时发生异常，订单生成失败！", CodeConstant.SYS_ERROR);
                                                }
                                            }
                                        } else {
                                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), -1, 60 * 60 * 24);
                                            return fastReturn(null, false, "订单生成失败，此款秒杀商品已无库存！", CodeConstant.SYS_HOTGOODSKILL_ERROR);
                                        }
                                    } else {
                                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                        return fastReturn(null, false, "订单生成失败，用户不能重复秒杀同款商品！", CodeConstant.SYS_HOTGOODSREPEATKILL_ERROR);
                                    }
                                } else {
                                    cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), -1, 60 * 60 * 24);
                                    return fastReturn(null, false, "订单生成失败，此款秒杀商品已无库存！", CodeConstant.SYS_HOTGOODSKILL_ERROR);
                                }
                            } else {
                                return fastReturn(null, false, "订单生成失败，下单商品属性与数据库不一致！", CodeConstant.ERROR_SKU_NOT_SAME);
                            }
                        }
                    } else {
                        return fastReturn(result, false, "订单生成失败，多价格skuValue参数为空！", CodeConstant.PARAM_ERROR);
                    }
                }else {   //普通秒杀下单逻辑
                    redisCategoryKeyFlag = cacheManager.isExist(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId());//秒杀商品等同立即购买1订单1个商品
                    if (redisCategoryKeyFlag) { //该秒杀商品有库存
                        if ((Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) > 0) {
                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) - 1, 60 * 60 * 24);
                            Integer buyConut = this.shopOrderGoodsService.queryGoodsByCount(shopOrderGood.get(0).getUserId(), shopOrderGood.get(0).getGoodsId());
                            if (buyConut == 0) {
                                String type = "del";
                                Integer goodStock = this.shopGoodsService.queryGoodsStock(shopOrderGood.get(0).getGoodsId());
                                if (goodStock > 0) {
                                    boolean flag = this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                    if (flag) {
                                        try {  //执行秒杀下单操做
                                            type = "add";
                                            shopOrderGood.get(0).setOrderStatus(1);
                                            shopOrderGood.get(0).setShippingFee(new BigDecimal(0));
                                            shopOrderGood.get(0).setIntegralMoney(new BigDecimal(0));
                                            shopOrderGood.get(0).setOrderNo(OrderGenerater.generateOrderNo(shopOrderGood.get(0).getUserCode()));
                                            result = this.shopOrderInfoService.insertShopOrder(shopOrderGood);
                                            if (result.get(0).get("ErrorInsert") != null) {
                                                this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                                cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                                return fastReturn(result, false, "订单生成失败，goodsId参数为空！", CodeConstant.PARAM_ERROR);
                                            } else if (result.get(0).get("ErrorInsertByUsersType") != null) {
                                                this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                                cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                                return fastReturn(result, false, "订单生成失败，usersType参数为空无法确定购买价格！", CodeConstant.ERROR_NO_USERTYPE);
                                            } else if (result.get(0).get("ErrorInsertNotAllowByUsersType") != null) {
                                                this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                                cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                                return fastReturn(result, false, "订单生成失败，用户类型只能为普通会员和创业会员，商品无法购买！", CodeConstant.ERROR_NO_PERMISSION);
                                            } else if (result != null && result.size() > 0) {
                                                return fastReturn(result, true, "生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单成功！", CodeConstant.SC_OK);
                                            } else {
                                                this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                                cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                                return fastReturn(result, false, "系统异常，生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单失败！", CodeConstant.SYS_ERROR);
                                            }
                                        } catch (Exception e) {
                                            type = "add";
                                            this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                            logger.info(e.toString(), e);
                                            return fastReturn(null, false, "系统异常，生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单失败！", CodeConstant.SYS_ERROR);
                                        }
                                    } else {
                                        type = "add";
                                        boolean addGoodStockResult = this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                        if (addGoodStockResult) {
                                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                            return fastReturn(null, false, "减库存时发生异常，订单生成失败！", CodeConstant.SYS_ERROR);
                                        }
                                    }
                                } else {
                                    cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), -1, 60 * 60 * 24);
                                    return fastReturn(null, false, "订单生成失败，此款秒杀商品已无库存！", CodeConstant.SYS_HOTGOODSKILL_ERROR);
                                }
                            } else {
                                cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                return fastReturn(null, false, "订单生成失败，用户不能重复秒杀同款商品！", CodeConstant.SYS_HOTGOODSREPEATKILL_ERROR);
                            }
                        } else {
                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), -1, 60 * 60 * 24);
                            return fastReturn(null, false, "订单生成失败，此款秒杀商品已无库存！", CodeConstant.SYS_HOTGOODSKILL_ERROR);
                        }
                    } else {
                        String goodsStock = this.shopGoodsService.getGoodsStock(String.valueOf(shopOrderGood.get(0).getGoodsId())); //从数据库获取秒杀商品个数并设置缓存
                        if (Integer.valueOf(goodsStock) > 0) {
                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), Integer.valueOf(goodsStock) - 1, 60 * 60 * 24);
                            Integer buyConut = this.shopOrderGoodsService.queryGoodsByCount(shopOrderGood.get(0).getUserId(), shopOrderGood.get(0).getGoodsId());
                            if (buyConut == 0) {
                                String type = "del";
                                Integer goodStock = this.shopGoodsService.queryGoodsStock(shopOrderGood.get(0).getGoodsId());
                                if (goodStock > 0) {
                                    boolean flag = this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                    if (flag) {
                                        try {  //执行秒杀下单操做
                                            type = "add";
                                            shopOrderGood.get(0).setOrderStatus(1);
                                            shopOrderGood.get(0).setShippingFee(new BigDecimal(0));
                                            shopOrderGood.get(0).setIntegralMoney(new BigDecimal(0));
                                            shopOrderGood.get(0).setOrderNo(OrderGenerater.generateOrderNo(shopOrderGood.get(0).getUserCode()));
                                            result = this.shopOrderInfoService.insertShopOrder(shopOrderGood);
                                            if (result.get(0).get("ErrorInsert") != null) {
                                                this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                                cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                                return fastReturn(result, false, "订单生成失败，goodsId参数为空！", CodeConstant.PARAM_ERROR);
                                            } else if (result.get(0).get("ErrorInsertByUsersType") != null) {
                                                this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                                cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                                return fastReturn(result, false, "订单生成失败，usersType参数为空无法确定购买价格！", CodeConstant.ERROR_NO_USERTYPE);
                                            } else if (result.get(0).get("ErrorInsertNotAllowByUsersType") != null) {
                                                this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                                cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                                return fastReturn(result, false, "订单生成失败，用户类型只能为普通会员和创业会员，商品无法购买！", CodeConstant.ERROR_NO_PERMISSION);
                                            } else if (result != null && result.size() > 0) {
                                                return fastReturn(result, true, "生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单成功！", CodeConstant.SC_OK);
                                            } else {
                                                this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                                cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                                return fastReturn(result, false, "系统异常，生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单失败！", CodeConstant.SYS_ERROR);
                                            }
                                        } catch (Exception e) {
                                            type = "add";
                                            this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                            logger.info(e.toString(), e);
                                            return fastReturn(null, false, "系统异常，生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单失败！", CodeConstant.SYS_ERROR);
                                        }
                                    } else {
                                        type = "add";
                                        boolean addGoodStockResult = this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                        if (addGoodStockResult) {
                                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                            return fastReturn(null, false, "减库存时发生异常，订单生成失败！", CodeConstant.SYS_ERROR);
                                        }
                                    }
                                } else {
                                    cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), -1, 60 * 60 * 24);
                                    return fastReturn(null, false, "订单生成失败，此款秒杀商品已无库存！", CodeConstant.SYS_HOTGOODSKILL_ERROR);
                                }
                            } else {
                                cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                return fastReturn(null, false, "订单生成失败，用户不能重复秒杀同款商品！", CodeConstant.SYS_HOTGOODSREPEATKILL_ERROR);
                            }
                        } else {
                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), -1, 60 * 60 * 24);
                            return fastReturn(null, false, "订单生成失败，此款秒杀商品已无库存！", CodeConstant.SYS_HOTGOODSKILL_ERROR);
                        }
                    }
                }
            } else {   //普通秒杀下单逻辑(APP端调用)
                redisCategoryKeyFlag = cacheManager.isExist(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId());//秒杀商品等同立即购买1订单1个商品
                if (redisCategoryKeyFlag) { //该秒杀商品有库存
                    if ((Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) > 0) {
                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) - 1, 60 * 60 * 24);
                        Integer buyConut = this.shopOrderGoodsService.queryGoodsByCount(shopOrderGood.get(0).getUserId(), shopOrderGood.get(0).getGoodsId());
                        if (buyConut == 0) {
                            String type = "del";
                            Integer goodStock = this.shopGoodsService.queryGoodsStock(shopOrderGood.get(0).getGoodsId());
                            if (goodStock > 0) {
                                boolean flag = this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                if (flag) {
                                    try {  //执行秒杀下单操做
                                        type = "add";
                                        shopOrderGood.get(0).setOrderStatus(1);
                                        shopOrderGood.get(0).setShippingFee(new BigDecimal(0));
                                        shopOrderGood.get(0).setIntegralMoney(new BigDecimal(0));
                                        shopOrderGood.get(0).setOrderNo(OrderGenerater.generateOrderNo(shopOrderGood.get(0).getUserCode()));
                                        result = this.shopOrderInfoService.insertShopOrder(shopOrderGood);
                                        if (result.get(0).get("ErrorInsert") != null) {
                                            this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                            return fastReturn(result, false, "订单生成失败，goodsId参数为空！", CodeConstant.PARAM_ERROR);
                                        } else if (result.get(0).get("ErrorInsertByUsersType") != null) {
                                            this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                            return fastReturn(result, false, "订单生成失败，usersType参数为空无法确定购买价格！", CodeConstant.ERROR_NO_USERTYPE);
                                        } else if (result.get(0).get("ErrorInsertNotAllowByUsersType") != null) {
                                            this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                            return fastReturn(result, false, "订单生成失败，用户类型只能为普通会员和创业会员，商品无法购买！", CodeConstant.ERROR_NO_PERMISSION);
                                        } else if (result != null && result.size() > 0) {
                                            return fastReturn(result, true, "生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单成功！", CodeConstant.SC_OK);
                                        } else {
                                            this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                            return fastReturn(result, false, "系统异常，生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单失败！", CodeConstant.SYS_ERROR);
                                        }
                                    } catch (Exception e) {
                                        type = "add";
                                        this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                        logger.info(e.toString(), e);
                                        return fastReturn(null, false, "系统异常，生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单失败！", CodeConstant.SYS_ERROR);
                                    }
                                } else {
                                    type = "add";
                                    boolean addGoodStockResult = this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                    if (addGoodStockResult) {
                                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                        return fastReturn(null, false, "减库存时发生异常，订单生成失败！", CodeConstant.SYS_ERROR);
                                    }
                                }
                            } else {
                                cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), -1, 60 * 60 * 24);
                                return fastReturn(null, false, "订单生成失败，此款秒杀商品已无库存！", CodeConstant.SYS_HOTGOODSKILL_ERROR);
                            }
                        } else {
                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                            return fastReturn(null, false, "订单生成失败，用户不能重复秒杀同款商品！", CodeConstant.SYS_HOTGOODSREPEATKILL_ERROR);
                        }
                    } else {
                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), -1, 60 * 60 * 24);
                        return fastReturn(null, false, "订单生成失败，此款秒杀商品已无库存！", CodeConstant.SYS_HOTGOODSKILL_ERROR);
                    }
                } else {
                    String goodsStock = this.shopGoodsService.getGoodsStock(String.valueOf(shopOrderGood.get(0).getGoodsId())); //从数据库获取秒杀商品个数并设置缓存
                    if (Integer.valueOf(goodsStock) > 0) {
                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), Integer.valueOf(goodsStock) - 1, 60 * 60 * 24);
                        Integer buyConut = this.shopOrderGoodsService.queryGoodsByCount(shopOrderGood.get(0).getUserId(), shopOrderGood.get(0).getGoodsId());
                        if (buyConut == 0) {
                            String type = "del";
                            Integer goodStock = this.shopGoodsService.queryGoodsStock(shopOrderGood.get(0).getGoodsId());
                            if (goodStock > 0) {
                                boolean flag = this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                if (flag) {
                                    try {  //执行秒杀下单操做
                                        type = "add";
                                        shopOrderGood.get(0).setOrderStatus(1);
                                        shopOrderGood.get(0).setShippingFee(new BigDecimal(0));
                                        shopOrderGood.get(0).setIntegralMoney(new BigDecimal(0));
                                        shopOrderGood.get(0).setOrderNo(OrderGenerater.generateOrderNo(shopOrderGood.get(0).getUserCode()));
                                        result = this.shopOrderInfoService.insertShopOrder(shopOrderGood);
                                        if (result.get(0).get("ErrorInsert") != null) {
                                            this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                            return fastReturn(result, false, "订单生成失败，goodsId参数为空！", CodeConstant.PARAM_ERROR);
                                        } else if (result.get(0).get("ErrorInsertByUsersType") != null) {
                                            this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                            return fastReturn(result, false, "订单生成失败，usersType参数为空无法确定购买价格！", CodeConstant.ERROR_NO_USERTYPE);
                                        } else if (result.get(0).get("ErrorInsertNotAllowByUsersType") != null) {
                                            this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                            return fastReturn(result, false, "订单生成失败，用户类型只能为普通会员和创业会员，商品无法购买！", CodeConstant.ERROR_NO_PERMISSION);
                                        } else if (result != null && result.size() > 0) {
                                            return fastReturn(result, true, "生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单成功！", CodeConstant.SC_OK);
                                        } else {
                                            this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                            return fastReturn(result, false, "系统异常，生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单失败！", CodeConstant.SYS_ERROR);
                                        }
                                    } catch (Exception e) {
                                        type = "add";
                                        this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                        logger.info(e.toString(), e);
                                        return fastReturn(null, false, "系统异常，生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单失败！", CodeConstant.SYS_ERROR);
                                    }
                                } else {
                                    type = "add";
                                    boolean addGoodStockResult = this.shopGoodsService.updateGoodStockByType(type, shopOrderGood.get(0).getGoodsId());
                                    if (addGoodStockResult) {
                                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                                        return fastReturn(null, false, "减库存时发生异常，订单生成失败！", CodeConstant.SYS_ERROR);
                                    }
                                }
                            } else {
                                cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), -1, 60 * 60 * 24);
                                return fastReturn(null, false, "订单生成失败，此款秒杀商品已无库存！", CodeConstant.SYS_HOTGOODSKILL_ERROR);
                            }
                        } else {
                            cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), (Integer) cacheManager.get(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId()) + 1, 60 * 60 * 24);
                            return fastReturn(null, false, "订单生成失败，用户不能重复秒杀同款商品！", CodeConstant.SYS_HOTGOODSREPEATKILL_ERROR);
                        }
                    } else {
                        cacheManager.set(RedisConstantUtil.SHOPHOTGOODS + shopOrderGood.get(0).getGoodsId(), -1, 60 * 60 * 24);
                        return fastReturn(null, false, "订单生成失败，此款秒杀商品已无库存！", CodeConstant.SYS_HOTGOODSKILL_ERROR);
                    }
                }
            }
        }
        return fastReturn(null, false, "系统异常，生成用户ID为：" + shopOrderGood.get(0).getUserId() + "的订单失败！", CodeConstant.SYS_ERROR);
    }

    *//**
     * @param request
     * @param page
     * @describe:分页查询订单列表
     * @author: zhangchunming
     * @date: 2016年10月27日上午11:46:28
     * @return: AjaxResponse
     *//*
    @LoginVerify
    @RequestMapping(value = "/listPageShopOrder", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse listPageShopOrder(HttpServletRequest request, Page page) {
        AjaxResponse ar = new AjaxResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            PageData pd = new PageData();
            pd = this.getPageData();
            //获取用户信息
            String userstr = sessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            
            //供应商List
            List<PageData> supplierList = null;
            //供应商IDList
            List<Integer> supplierIdList = new ArrayList<Integer>();
            PageData oneSupplier = null;
            if ("4".equals(user.getString("user_type"))) {//供应商
                //根据user_id查询供应商信息
//                supplierList = shopOrderInfoService.getSupplierByUserId(String.valueOf(user.get("id")), Constant.VERSION_NO);
                oneSupplier = shopOrderInfoService.getOneSupplierByUserId(String.valueOf(user.get("id")), Constant.VERSION_NO);
                if (oneSupplier == null) {
                    logger.error("--------查询商城订单列表-----------根据user_id查找不到供应商信息！");
                    ar.setSuccess(false);
                    ar.setMessage("查找不到供应商信息！");
                    ar.setErrorCode(CodeConstant.NO_EXISTS);
                    return ar;
                }
                for(PageData supplier:supplierList){
                    supplierIdList.add((Integer)supplier.get("id"));
                }
                pd.put("supplierList", supplierList);
                pd.put("supplier_id", String.valueOf(oneSupplier.get("id")));
            } else {
                pd.put("user_id", String.valueOf(user.get("id")));
            }
            page.setPd(pd);
            PageData shopOrderPD = shopOrderInfoService.listPageShopOrder(page, Constant.VERSION_NO);//查询所有订单
            List<PageData> shopOrderList = null;
            if (shopOrderPD != null) {
                page = (Page) shopOrderPD.get("page");
                page.setPd(null);
                shopOrderList = (List<PageData>) shopOrderPD.get("list");
                if (shopOrderList != null && shopOrderList.size() > 0) {
                    List<String> orderIdList = new ArrayList<String>();
                    for (PageData shopOrder : shopOrderList) {
                        orderIdList.add(String.valueOf(shopOrder.get("order_id")));
                    }
                    PageData shopOrder = new PageData();
                    if ("4".equals(user.getString("user_type"))) {//供应商
//                        shopOrder.put("supplierIdList", supplierIdList);
                        shopOrder.put("supplier_id", String.valueOf(oneSupplier.get("id")));
                    } else {
                        shopOrder.put("user_id", String.valueOf(user.get("id")));
                    }
                    shopOrder.put("orderIdList", orderIdList);
                    List<PageData> shopGoods = shopOrderInfoService.getGoodsByOrderId(shopOrder, Constant.VERSION_NO);

                    if (shopGoods != null && shopGoods.size() > 0) {
                        for (PageData newShopOrder : shopOrderList) {
                            List<PageData> shopGoodsList = new ArrayList<PageData>();
                            for (PageData newShopGoods : shopGoods) {
                                if (newShopGoods.getString("shop_order_id").equals(String.valueOf(newShopOrder.get("order_id")))) {
                                    shopGoodsList.add(newShopGoods);
                                }
                            }
                            newShopOrder.put("shopGoodsList", shopGoodsList);
                        }
                    }
                }
            }
            data.put("shopOrderList", shopOrderList);
            ar.setData(data);
            ar.setPage(page);
            ar.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
        }
        return ar;
    }

    
    @LoginVerify
    @RequestMapping(value = "/listPageShopOrder", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse listPageShopOrder(HttpServletRequest request, Page page) {
        AjaxResponse ar = new AjaxResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            PageData pd = new PageData();
            pd = this.getPageData();
            //获取用户信息
            String userstr = sessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
          //商城订单列表
            PageData orderPD = new PageData();
            if("3".equals(user.getString("user_type"))){//物流公司
                //分页查询商城订单列表
                page.setShowCount(5);
                PageData shopOrderPD =  shopOrderInfoService.listPageShopOrder(page, Constant.VERSION_NO);//查询所有订单
                List<PageData> shopOrderList = (List<PageData>)shopOrderPD.get("list");
                page = (Page) shopOrderPD.get("page");
                data.put("shopOrderList", shopOrderList);
            }else if("2".equals(user.getString("user_type"))){//卖家
                //分页查询商城订单列表
                orderPD.put("supplier_id", String.valueOf(user.get("id")));
                page.setShowCount(5);
                page.setPd(orderPD);
                PageData shopOrderPD =  shopOrderInfoService.listPageShopOrder(page, Constant.VERSION_NO);//查询所有订单
                List<PageData> shopOrderList = (List<PageData>)shopOrderPD.get("list");
                page = (Page) shopOrderPD.get("page");
                data.put("shopOrderList", shopOrderList);
            }else if("1".equals(user.getString("user_type"))){//买家
                orderPD.put("user_id", String.valueOf(user.get("id")));
                page.setPd(orderPD);
                page.setShowCount(5);
                PageData shopOrderPD =  shopOrderInfoService.listPageShopOrder(page, Constant.VERSION_NO);//查询所有订单
                List<PageData> shopOrderList = (List<PageData>)shopOrderPD.get("list");
                data.put("shopOrderList", shopOrderList);
                page = (Page) shopOrderPD.get("page");
            }else{
                data.put("shopOrderList", null);
            }
            
            ar.setData(data);
            ar.setPage(page);
            ar.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
        }
        return ar;
    }
    
    *//**
     * @param request
     * @describe:按状态查询商城订单数量
     * @author: zhangchunming
     * @date: 2016年10月28日下午1:10:13
     * @return: AjaxResponse
     *//*
    @LoginVerify
    @RequestMapping(value = "/getShopOrderNumByStatus", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse getShopOrderNumByStatus(HttpServletRequest request) {
        AjaxResponse ar = new AjaxResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            PageData pd = new PageData();
            pd = this.getPageData();
            String key = RequestUtils.getCookieValueByKey(CookieConstant.CSESSIONID, request, response);
            String userstr = sessionUtil.getAttibuteForUser(key);
            String userstr = sessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            if ("1".equals(user.getString("user_type")) || "2".equals(user.getString("user_type"))) {//普通会员、创业会员
                pd.put("user_id", String.valueOf(user.get("id")));
                PageData shopOrderNum = shopOrderInfoService.getShopOrderNumByStatus(pd, Constant.VERSION_NO);
                data.put("shopOrderNum", shopOrderNum);
            } else if ("4".equals(user.getString("user_type"))) {
                PageData supplier = shopOrderInfoService.getOneSupplierByUserId(String.valueOf(user.get("id")), Constant.VERSION_NO);
                if (supplier == null) {
                    logger.error("--------根据用户信息查不到对应的供应商信息");
                    data.put("shopOrderNum", null);
                } else {
                    pd.put("supplier_id", String.valueOf(supplier.get("id")));
//                    PageData shopOrderNum = shopOrderInfoService.getShopOrderNumByStatus(pd, Constant.VERSION_NO);
                    PageData shopOrderNum = shopOrderInfoService.getShopOrderNumByStatusForSupplier(pd);
                    data.put("shopOrderNum", shopOrderNum);
                }
            } else {
                data.put("shopOrderNum", null);
            }
            ar.setData(data);
            ar.setMessage("查询成功！");
            ar.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
        }
        return ar;
    }

    *//**
     * @param request
     * @describe:查询订单总数
     * @author: zhangchunming
     * @date: 2016年10月28日下午2:40:23
     * @return: AjaxResponse
     *//*
    @LoginVerify
    @RequestMapping(value = "/getOrderTotalNum", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse getOrderTotalNum(HttpServletRequest request) {
        AjaxResponse ar = new AjaxResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            PageData pd = new PageData();
            String key = RequestUtils.getCookieValueByKey(CookieConstant.CSESSIONID, request, response);
            String userstr = sessionUtil.getAttibuteForUser(key);
            String userstr = sessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            String user_id = String.valueOf(user.get("id"));
//            Integer shopOrderTotalNum = shopOrderInfoService.getShopOrderTotalNum(user_id, Constant.VERSION_NO);
            if ("4".equals(user.getString("user_type"))) {//供应商
                PageData supplier = shopSupplierService.getSupplierByUserId(user_id, Constant.VERSION_NO);
                if (supplier == null) {
                    logger.error("----------根据用户信息查不到对应的供应商信息---------------");
                    data.put("shopOrderTotalNum", null);
                } else {
                    pd.put("supplier_id", String.valueOf(supplier.get("id")));
                    Integer shopOrderTotalNum = shopOrderInfoService.getShopOrderTotalNum(pd, Constant.VERSION_NO);
                    data.put("shopOrderTotalNum", shopOrderTotalNum);
                }

            } else if ("3".equals(user.getString("user_type"))) {//店铺
                Integer storeId = storeInfoService.getStoreIdByUserId(user_id, Constant.VERSION_NO);
                if (storeId == null) {
                    logger.error("----------根据用户信息查不到对应的店铺信息---------------");
                    data.put("storeOrderTotalNum", null);
                } else {
                    pd.put("store_id", storeId);
                    Integer storeOrderTotalNum = storeOrderInfoService.getStoreOrderTotalNum(pd, Constant.VERSION_NO);
                    data.put("storeOrderTotalNum", storeOrderTotalNum);
                }
            } else {//普通会员、创业会员
                pd.put("user_id", user_id);
                Integer shopOrderTotalNum = shopOrderInfoService.getShopOrderTotalNum(pd, Constant.VERSION_NO);
                Integer storeOrderTotalNum = storeOrderInfoService.getStoreOrderTotalNum(pd, Constant.VERSION_NO);
                data.put("shopOrderTotalNum", shopOrderTotalNum);
                data.put("storeOrderTotalNum", storeOrderTotalNum);
            }
            ar.setData(data);
            ar.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
        }
        return ar;
    }

    *//**
     * @param request
     * @describe:确认收货,更改订单状态
     * @author: zhangchunming
     * @date: 2016年11月08日下午17:23:50
     * @return: AjaxResponse
     *//*
    @LoginVerify
    @RequestMapping(value="/confirmReceipt", method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse confirmReceipt(HttpServletRequest request){
        AjaxResponse ar = new AjaxResponse();
        try {
            String userstr = sessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            PageData pd = new PageData();
            pd = this.getPageData();
            pd.put("user_id", user.getInteger("id"));
            String  order_no = pd.getString("order_no");
            if(StringUtil.isEmpty(order_no)){
                ar.setSuccess(false);
                ar.setMessage("订单号不能为空");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(StringUtil.isEmpty(pd.getString("goods_id"))){
                ar.setSuccess(false);
                ar.setMessage("商品ID不能为空");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
//            PageData shopOrder = shopOrderInfoService.selectById(Integer.valueOf(order_id), Constant.VERSION_NO);
            pd.put("shop_order_no", order_no);
            PageData orderGoods = shopOrderGoodsService.getOrderGoods(pd, Constant.VERSION_NO);
            if(orderGoods==null){
                ar.setSuccess(false);
                ar.setMessage("订单不存在");
                ar.setErrorCode(CodeConstant.ORDER_NO_EXISTS);
                return ar;
            }
            boolean lastShopOrder = shopOrderInfoService.isLastShopOrder(pd, Constant.VERSION_NO);
            if(lastShopOrder){
                boolean updateStateByOrderNo = shopOrderInfoService.updateStateByOrderNo(pd, Constant.VERSION_NO);
                if(updateStateByOrderNo){
                    ar.setSuccess(true);
                    ar.setMessage("确认收货成功！");
                    return ar;
                }else{
                    ar.setSuccess(false);
                    ar.setMessage("确认收货失败");
                    ar.setErrorCode(CodeConstant.UPDATE_FAIL);
                }
            }else{
                PageData shopOrder = shopOrderInfoService.getShopOrderByOrderNo(pd, Constant.VERSION_NO);
                pd.put("user_type", user.getString("user_type"));
                pd.put("wlbi_amnt", shopOrder.getString("order_amount"));//应付款金额
                pd.put("other_amnt", shopOrder.getString("order_amount"));//应付款金额
                pd.put("otherno", shopOrder.getString("order_no"));//业务订单号
                pd.put("operator", user.getString("mobile_phone"));
                
//                pd.put("shop_order_no", shopOrder.getString("order_no"));
                //获取三界行情
                BigDecimal sanhq =new BigDecimal(0);//这里需要获取SANHQ数据
                String Sanexrate="";
               if(cacheManager.isExist(RedisConstantUtil.R8EXCHANGERATE)){
                   Sanexrate=(String)cacheManager.get(RedisConstantUtil.R8EXCHANGERATE);
                    logAfter(logger);
                }else{
                   Sanexrate =R8ExChangeController.getSanExrate().equals("")?"0":R8ExChangeController.getSanExrate();  
                   try{
                       if(StringUtil.isNotEmpty(Sanexrate)){
                           cacheManager.set(RedisConstantUtil.R8EXCHANGERATE,Sanexrate,WlscConstants.WLSC_R8EXCHANGE_RATE_SECOND);
                       }
                       sanhq =new BigDecimal(Sanexrate);//这里需要获取SANHQ数据
                   }catch(Exception e){
                      String codeName = "SANEXRATE";
                      List<PageData> codeList =sysGenCodeService.findByGroupCode("SANEXRATE", Constant.VERSION_NO);
                      for(PageData code:codeList){
                          if(codeName.equals(code.get("code_name"))){
                              Sanexrate = code.get("code_value").toString();
                          }
                      }                                        
                       sanhq =new BigDecimal(Sanexrate);
                   }
                } 
                pd.put("sanhq",sanhq);
                
//                pd.put("shop_order_no", shopOrder.getString("order_no"));
                
                //商品成本价及供应商拿到的价格，无需获取费率
                String  rate = "";
                List<PageData> codeList =sysGenCodeService.findByGroupCode("LIMIT_RATE", Constant.VERSION_NO);
                for(PageData code:codeList){
                    if("EXCHANGE_RATE".equals(code.get("code_name"))){
                        rate = code.get("code_value").toString();
                    }
                }   
                pd.put("rate", rate);
                boolean updateCheckStatus = shopOrderInfoService.confirmReceipt(pd, Constant.VERSION_NO);
                if(updateCheckStatus){
                    logger.info("------确认收货计算奖励-----------start---------------");
                    PageData consume = new PageData();
                    consume.put("order_no", pd.getString("order_no"));
                    consume.put("sanhq", pd.getString("sanhq"));
                    consume.put("shop_order_no", pd.getString("shop_order_no"));
                    if("1".equals(pd.getString("user_type"))){//普通会员消费奖励计算\
                        if(calBounsService.calBounsMallConsumePt(consume, Constant.VERSION_NO)){
                            logger.error("calBounsService.calBounsMallConsumePt fail !普通会员消费奖励计算接口，商城  失败！");
                        }
                    }else if("2".equals(pd.getString("user_type"))){
                        if(calBounsService.calBounsMallConsumeVip(consume, Constant.VERSION_NO)){
                            logger.error("calBounsService.calBounsMallConsumeVip fail !创业会员消费奖励计算接口 ,商城   失败！");
                        }
                    }
                    if(calBounsService.calBounsProviderSales(consume, Constant.VERSION_NO)){
                        logger.error("calBounsService.calBounsProviderSales fail !供应商销售业绩奖励计算接口 推荐奖励   失败！");
                    }
                    logger.info("------确认收货计算奖励-----------end---------------");
                    ar.setSuccess(true);
                    ar.setMessage("确认收货成功");
                    return ar;
                }else{
                    ar.setSuccess(false);
                    ar.setMessage("确认收货失败");
                    ar.setErrorCode(CodeConstant.UPDATE_FAIL);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
            ar.setErrorCode(CodeConstant.SYS_ERROR);
        }   
        return ar;
    }

//    @LoginVerify
    @RequestMapping(value = "/confirmReceipt", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse confirmReceipt(HttpServletRequest request) {
        AjaxResponse ar = new AjaxResponse();
        try {
            String userstr = sessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            PageData pd = new PageData();
            pd = this.getPageData();
//            pd.put("user_id", String.valueOf(user.get("id")));
            String order_no = pd.getString("order_no");
            if (StringUtil.isEmpty(order_no)) {
                ar.setSuccess(false);
                ar.setMessage("订单号不能为空");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            PageData shopOrder = shopOrderInfoService.getShopOrderAndUserInfo(pd);
            if (shopOrder == null) {
                ar.setSuccess(false);
                ar.setMessage("订单不存在");
                ar.setErrorCode(CodeConstant.ORDER_NO_EXISTS);
                return ar;
            }
            //获取三界行情
            BigDecimal sanhq = new BigDecimal(0);//这里需要获取SANHQ数据
            String Sanexrate = "";
            if (cacheManager.isExist(RedisConstantUtil.R8EXCHANGERATE)) {
                Sanexrate = (String) cacheManager.get(RedisConstantUtil.R8EXCHANGERATE);
            } else {
                Sanexrate = R8ExChangeController.getSanExrate().equals("") ? "0" : R8ExChangeController.getSanExrate();
                try {
                    sanhq = new BigDecimal(Sanexrate);//这里需要获取SANHQ数据
                    if (StringUtil.isNotEmpty(Sanexrate)) {
                        cacheManager.set(RedisConstantUtil.R8EXCHANGERATE, Sanexrate, WlscConstants.WLSC_R8EXCHANGE_RATE_SECOND);
                    }
                } catch (Exception e) {
                    String codeName = "SANEXRATE";
                    List<PageData> codeList = sysGenCodeService.findByGroupCode("SANEXRATE", Constant.VERSION_NO);
                    for (PageData code : codeList) {
                        if (codeName.equals(code.get("code_name"))) {
                            Sanexrate = code.get("code_value").toString();
                        }
                    }
                    sanhq = new BigDecimal(Sanexrate);
                }
            }
            pd.put("sanhq", sanhq);
            pd.put("user_id", String.valueOf(shopOrder.get("user_id")));
            pd.put("user_type", shopOrder.getString("user_type"));
            pd.put("wlbi_amnt", String.valueOf(shopOrder.get("order_amount")));//应付款金额
            pd.put("other_amnt", String.valueOf(shopOrder.get("order_amount")));//应付款金额
            pd.put("otherno", shopOrder.getString("order_no"));//业务订单号
            pd.put("operator", shopOrder.getString("mobile_phone"));
            pd.put("shop_order_no", shopOrder.getString("order_no"));
            pd.put("order_no", shopOrder.getString("order_no"));
            pd.put("order_id", String.valueOf(shopOrder.get("order_id")));
            boolean shopReceiptResult = shopOrderInfoService.shopReceipt(pd, Constant.VERSION_NO);
            if (shopReceiptResult) {
                logger.info("------确认收货计算奖励-----------start---------------");
                PageData consume = new PageData();
                consume.put("order_no", pd.getString("order_no"));
                consume.put("sanhq", String.valueOf(pd.get("sanhq")));
                consume.put("shop_order_no", pd.getString("shop_order_no"));
                if ("1".equals(pd.getString("user_type"))) {//普通会员消费奖励计算\
                    if (calBounsService.calBounsMallConsumePt(consume, Constant.VERSION_NO)) {
                        logger.error("calBounsService.calBounsMallConsumePt fail !普通会员消费奖励计算接口，商城  失败！");
                    }
                } else if ("2".equals(pd.getString("user_type"))) {
                    if (calBounsService.calBounsMallConsumeVip(consume, Constant.VERSION_NO)) {
                        logger.error("calBounsService.calBounsMallConsumeVip fail !创业会员消费奖励计算接口 ,商城   失败！");
                    }
                }
                if (calBounsService.calBounsProviderSales(consume, Constant.VERSION_NO)) {
                    logger.error("calBounsService.calBounsProviderSales fail !供应商销售业绩奖励计算接口 推荐奖励   失败！");
                }
                logger.info("------确认收货计算奖励-----------end---------------");
                ar.setMessage("操作成功！");
                ar.setSuccess(true);
                return ar;
            } else {
                ar.setMessage("操作失败");
                ar.setSuccess(false);
                ar.setErrorCode(CodeConstant.UPDATE_FAIL);
                return ar;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
            ar.setErrorCode(CodeConstant.SYS_ERROR);
        }
        return ar;
    }

    @LoginVerify
    @RequestMapping(value = "/deliverGoods", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deliverGoods(HttpServletRequest request) {
        AjaxResponse ar = new AjaxResponse();
        try {
            String key = RequestUtils.getCookieValueByKey(CookieConstant.CSESSIONID, request, response);
            String userstr = sessionUtil.getAttibuteForUser(key);
            String userstr = sessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            PageData pd = new PageData();
            pd = this.getPageData();
            if (StringUtil.isEmpty(pd.getString("shop_order_no"))) {
                ar.setSuccess(false);
                ar.setMessage("订单号不能为空");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if (StringUtil.isEmpty(pd.getString("goods_id"))) {
                ar.setSuccess(false);
                ar.setMessage("商品ID不能为空");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if (StringUtil.isEmpty(pd.getString("logistics_no"))) {
                ar.setSuccess(false);
                ar.setMessage("快递单号不能为空");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if (StringUtil.isEmpty(pd.getString("logistics_name"))) {
                ar.setSuccess(false);
                ar.setMessage("快递公司不能为空");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            PageData shopOrder = shopOrderGoodsService.getOrderGoods(pd, Constant.VERSION_NO);
            if (shopOrder == null) {
                ar.setMessage("商品订单关联表中不存在该条信息！");
                ar.setSuccess(false);
                ar.setErrorCode(CodeConstant.NO_EXISTS);
            }
            pd.put("rec_id", shopOrder.get("rec_id"));
            pd.put("state", "3");//发货
            boolean deliverGoods = shopOrderInfoService.deliverGoods(pd, Constant.VERSION_NO);
            if (deliverGoods) {
                ar.setSuccess(true);
                ar.setMessage("发货成功");
                return ar;
            }
            ar.setSuccess(false);
            ar.setMessage("发货失败");
            ar.setErrorCode(CodeConstant.UPDATE_FAIL);
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
            ar.setErrorCode(CodeConstant.SYS_ERROR);
        }
        return ar;
    }

    *//**
     * @param request
     * @describe:立即支付
     * @author: zhangchunming
     * @date: 2016年11月9日下午9:59:50
     * @return: AjaxResponse
     *//*
    @LoginVerify
    @RequestMapping(value = "/payNow", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse payNow(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        AjaxResponse ar = new AjaxResponse();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            String userstr = sessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            pd.put("user_id", String.valueOf(user.get("id")));
            pd.put("seeds", user.getString("seeds"));
            pd.put("user_type", String.valueOf(user.getString("user_type")));
            pd.put("operator", String.valueOf(user.getString("account")));
            if(StringUtil.isEmpty(pd.getString("order_id"))){
                ar.setSuccess(false);
                ar.setMessage("订单ID不能为空");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if (StringUtil.isEmpty(pd.getString("order_no"))) {
                ar.setSuccess(false);
                ar.setMessage("订单号不能为空");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(StringUtil.isEmpty(pd.getString("order_amount"))){
                ar.setSuccess(false);
                ar.setMessage("付款金额不能为空");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
//            PageData shopOrderInfo = shopOrderInfoService.selectById(Integer.valueOf(pd.getString("order_id")), Constant.VERSION_NO);
            PageData shopOrderInfo = shopOrderInfoService.getShopOrderByOrderNo(pd, Constant.VERSION_NO);
            if ("2".equals(shopOrderInfo.getString("order_status"))) {//已支付
                ar.setSuccess(false);
                ar.setMessage("已支付");
                ar.setErrorCode(CodeConstant.pay_finish);
                return ar;
            }
            if (StringUtil.isNotEmpty(shopOrderInfo.getString("trade_hash"))||"1".equals(shopOrderInfo.getString("is_lock"))) {//已支付
                ar.setSuccess(false);
                ar.setMessage("交易正在处理，请勿重复支付！");
                ar.setErrorCode(CodeConstant.pay_finish);
                return ar;
            }
            
            if ("1".equals(String.valueOf(shopOrderInfo.get("is_promote"))) && "4".equals(shopOrderInfo.getString("order_status"))) {//订单自动取消
                ar.setSuccess(false);
                ar.setMessage("未在规定时间内支付，订单自动取消！");
                ar.setErrorCode(CodeConstant.ERROR_CALCEL);
                return ar;
            } else if ("4".equals(shopOrderInfo.getString("order_status"))) {//订单自动取消
                ar.setSuccess(false);
                ar.setMessage("订单已取消！");
                ar.setErrorCode(CodeConstant.ERROR_CALCEL);
                return ar;
            }

            PageData userWallet = userWalletService.getWalletByUserId(String.valueOf(user.get("id")), Constant.VERSION_NO);
            String future_currency = String.valueOf(userWallet.get("future_currency"));
            if (new BigDecimal(String.valueOf(shopOrderInfo.get("order_amount"))).compareTo(new BigDecimal(future_currency)) > 0) {
                ar.setSuccess(false);
                ar.setMessage("积分余额不足，请充值");
                ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                return ar;
            }
            pd.put("order_no", shopOrderInfo.getString("order_no"));
            pd.put("order_id", String.valueOf(shopOrderInfo.get("order_id")));
            pd.put("order_amount", shopOrderInfo.get("order_amount"));
            pd.put("mobile_phone", user.getString("mobile_phone"));

            pd.put("shop_order_no", shopOrderInfo.getString("order_no"));

            //商品表里有供应商有专门价格无需查询兑换费率
            String  rate = "";
            List<PageData> codeList =sysGenCodeService.findByGroupCode("LIMIT_RATE", Constant.VERSION_NO);
            for(PageData code:codeList){
                if("EXCHANGE_RATE".equals(code.get("code_name"))){
                    rate = code.get("code_value").toString();
                }
            }
            if(StringUtil.isEmpty(rate)){

            }
            pd.put("rate", rate);
            //锁定订单
            boolean lockOrderByOrderNo = shopOrderInfoService.lockOrderByOrderNo(pd);
            logger.info("支付订单锁定结果lockOrderByOrderNo："+lockOrderByOrderNo);
            
            boolean payNow = shopOrderInfoService.payNow(pd, Constant.VERSION_NO);
            if (payNow) {
                ar.setSuccess(true);
                ar.setMessage("交易处理中...请前往账单查看兑换结果");
                data.put("order_no", pd.getString("order_no"));
                data.put("order_amount", shopOrderInfo.get("order_amount"));
                data.put("pay_time", DateUtil.getCurrDateTime());
                ar.setData(data);
                return ar;
            }
            ar.setSuccess(false);
            ar.setMessage("支付失败");
            ar.setErrorCode(CodeConstant.UPDATE_FAIL);
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            //解锁订单
            try {
                boolean unLockOrderByOrderNo = shopOrderInfoService.unLockOrderByOrderNo(pd);
                logger.info("支付订单解锁结果unLockOrderByOrderNo："+unLockOrderByOrderNo);
            } catch (Exception e1) {
                e1.printStackTrace();
                ar.setSuccess(false);
                ar.setMessage("网络繁忙，请稍候重试！");
                ar.setErrorCode(CodeConstant.SYS_ERROR);
            }
        }
        return ar;
    }

    *//**
     * @param request
     * @describe:根据订单号查询订单信息
     * @author: zhangchunming
     * @date: 2016年11月14日下午3:39:04
     * @return: AjaxResponse
     *//*
    @LoginVerify
    @RequestMapping(value = "/getShopOrderByOrderNo", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse getShopOrderByOrderNo(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        AjaxResponse ar = new AjaxResponse();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            String userstr = sessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            pd.put("user_id", String.valueOf(user.get("id")));
            if (StringUtil.isEmpty(pd.getString("order_no"))) {
                ar.setSuccess(false);
                ar.setMessage("请输入订单号");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            PageData shopOrderInfo = shopOrderInfoService.getShopOrderByOrderNo(pd, Constant.VERSION_NO);
            data.put("shopOrderInfo", shopOrderInfo);
            ar.setData(data);
            ar.setSuccess(true);
            ar.setMessage("查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
            ar.setErrorCode(CodeConstant.SYS_ERROR);
        }
        return ar;
    }

    *//**
     * @param request
     * @describe:商品退货关闭订单
     * @author: zhangchunming
     * @date: 2016年12月26日下午4:06:47
     * @return: AjaxResponse
     *//*
    @RequestMapping(value = "/refundGoods", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse refundGoods(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        AjaxResponse ar = new AjaxResponse();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            if (StringUtil.isEmpty(pd.getString("rec_id"))) {
                ar.setSuccess(false);
                ar.setMessage("请输入订单商品关联表ID！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if (StringUtil.isEmpty(pd.getString("state"))) {
                ar.setSuccess(false);
                ar.setMessage("请输入订单状态！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if (!"2".equals(pd.getString("state")) || !"3".equals(pd.getString("state")) || !"10".equals(pd.getString("state")) || !"11".equals(pd.getString("state")) || !"12".equals(pd.getString("state")) || !"13".equals(pd.getString("state"))) {
                ar.setSuccess(false);
                ar.setMessage("订单状态输入有误！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            boolean refundResult = false;
            //申请或拒绝退款退货
            if ("2".equals(pd.getString("state")) || "3".equals(pd.getString("state")) || "10".equals(pd.getString("state")) || "12".equals(pd.getString("state"))) {
                refundResult = shopOrderInfoService.applyAndRefuseRefundGoods(pd);
                if (refundResult) {
                    ar.setSuccess(true);
                    ar.setMessage("操作成功！");
                    return ar;
                } else {
                    ar.setErrorCode(CodeConstant.UPDATE_FAIL);
                    ar.setSuccess(false);
                    ar.setMessage("操作失败！");
                    return ar;
                }
                if("10".equals(pd.getString("state"))){//一键退款
                    if(refundResult){
                        ar.setSuccess(true);
                        ar.setMessage("操作成功！"); 
                        return ar;
                    }else{
                        ar.setErrorCode(CodeConstant.UPDATE_FAIL);
                        ar.setSuccess(false);
                        ar.setMessage("操作失败！"); 
                        return ar;
                    }
                }else if("12".equals(pd.getString("state"))){//一键退货
                    if(refundResult){
                        ar.setSuccess(true);
                        ar.setMessage("操作成功！"); 
                        return ar;
                    }else{
                        ar.setErrorCode(CodeConstant.UPDATE_FAIL);
                        ar.setSuccess(false);
                        ar.setMessage("操作失败！"); 
                        return ar;
                    }
                }
            }
            //同意退款退货
            if ("11".equals(pd.getString("state")) || "13".equals(pd.getString("state"))) {
                refundResult = shopOrderInfoService.refundGoods(pd);
                if ("11".equals(pd.getString("state"))) {//一键退款
                    if (refundResult) {
                        ar.setSuccess(true);
                        ar.setMessage("退款成功！");
                        return ar;
                    } else {
                        ar.setErrorCode(CodeConstant.UPDATE_FAIL);
                        ar.setSuccess(false);
                        ar.setMessage("退款失败！");
                        return ar;
                    }
                } else if ("13".equals(pd.getString("state"))) {//一键退货
                    if (refundResult) {
                        ar.setSuccess(true);
                        ar.setMessage("退货成功！");
                        return ar;
                    } else {
                        ar.setErrorCode(CodeConstant.UPDATE_FAIL);
                        ar.setSuccess(false);
                        ar.setMessage("退货失败！");
                        return ar;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
            ar.setErrorCode(CodeConstant.SYS_ERROR);
        }
        return ar;
    }

    public static void main(String[] args) {
        System.out.println(Integer.valueOf("2147483647"));
    }
}
*/