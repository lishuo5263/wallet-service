package com.ecochain.ledger.web.rest;


import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import com.ecochain.ledger.base.BaseWebService;
import com.ecochain.ledger.constants.CodeConstant;
import com.ecochain.ledger.model.Page;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.model.ShopCart;
import com.ecochain.ledger.model.ShopGoods;
import com.ecochain.ledger.service.ShopCartService;
import com.ecochain.ledger.service.ShopGoodsService;
import com.ecochain.ledger.util.AjaxResponse;
import com.ecochain.ledger.util.StringUtil;

/**
 * Created by LiShuo on 2016/10/26.
 */
@RestController
@RequestMapping(value = "/api/rest/mycart")
public class ShopCartWebService extends BaseWebService {

    private Logger logger = Logger.getLogger(ShopCartWebService.class);

    @Autowired
    private ShopCartService shopCartService;

    @Autowired
    private ShopGoodsService shopGoodsService;

    /**
     * 新增购物车
     */
    @PostMapping("/insertMyCat")
    public AjaxResponse insertMyCart(ShopCart shopCart){
        logger.info("**************************添加购物车参数为："+shopCart.toString()+"*****************");
        AjaxResponse ar= new AjaxResponse();
        Map<String,Object> map =new HashMap<String,Object>();
        //测试 insert
        /*shopCart.setGoodsId(4146);
        shopCart.setSkuId(199);
        shopCart.setUserId(999);
        shopCart.setGoodsPrice(new BigDecimal("99.999"));
        shopCart.setNum(2);
        shopCart.setSkuValue("红色,700,3.0,发货,发个,返工后和,3.0000000030000000000000000344443");
        shopCart.setSkuInfo("{\"颜色\":\"黑色\",\"规格\":\"个\",\"数量\":1,\"重量\":\"100g\"}");*/
    try{
        if(shopCart == null){
            return fastReturn(null,false,"接口参数异常，添加购物车失败！", CodeConstant.PARAM_ERROR);
        }else if(shopCart.getSkuInfo().length() < 0){
            return fastReturn(null,false,"接口参数skuInfo异常，添加除购物车失败！",CodeConstant.PARAM_ERROR);
        }else if(shopCart.getGoodsId()==null){
            return fastReturn(null,false,"接口参数goodsId异常，添加除购物车失败！",CodeConstant.PARAM_ERROR);
        }/*else if(shopCart.getSkuId()==null){
            return fastReturn(null,false,"接口参数skuId异常，添加除购物车失败！",CodeConstant.PARAM_ERROR);
        }*/else if(shopCart.getUserId()==null){
            return fastReturn(null,false,"接口参数userId异常，添加除购物车失败！",CodeConstant.PARAM_ERROR);
        }else if(shopCart.getNum()==null){
            return fastReturn(null,false,"接口参数num异常，添加除购物车失败！",CodeConstant.PARAM_ERROR);
        }
        int result=0;
        int addNum=shopCart.getNum();
            ShopCart shopCart1=this.shopCartService.queryCartGoods(String.valueOf(shopCart.getUserId()),String.valueOf(shopCart.getGoodsId()),shopCart.getSkuValue());
            if(shopCart1 != null && shopCart1.getUserId() !=null){  //添加过此商品 update num
                shopCart.setNum(shopCart1.getNum()+addNum);
                map=this.shopCartService.serchMyCartGoodsPrice(shopCart1.getGoodsId());
                if(map.containsKey("goods_price")){
                    shopCart.setGoodsPrice((BigDecimal)map.get("goods_price"));
                }
                if(shopCart.getSkuValue() !=null && !shopCart.getSkuValue().equals(shopCart1.getSkuValue())){ //相同SKU信息直接insert否则uopdate
                    result=this.shopCartService.insertMyCart(shopCart);
                }else{
                    result=this.shopCartService.updateMyCart(shopCart);
                }
                if(result > 0){
                    ar=fastReturn(shopCart,true,"添加购物车成功！",CodeConstant.SC_OK);
                }else{
                    ar=fastReturn(null,false,"系统异常，添加购物车失败！",CodeConstant.SYS_ERROR);
                }
            }else{
                this.shopCartService.insertMyCart(shopCart);//未添加过此商品
            }
            ar=fastReturn(shopCart,true,"添加购物车成功！",CodeConstant.SC_OK);
       }catch (ClassCastException e){
            logger.debug(e.toString(), e);
            ar=fastReturn(null,false,"接口参数类型异常，添加购物车失败！",CodeConstant.PARAM_ERROR);
       }catch(NullPointerException e){
            logger.debug(e.toString(), e);
            ar=fastReturn(null,false,"接口参数异常，添加购物车失败！",CodeConstant.PARAM_ERROR);
       }catch(Exception e){
            logger.debug(e.toString(), e);
            ar=fastReturn(null,false,"系统异常，添加购物车失败！",CodeConstant.SYS_ERROR);
        }
        return ar;
    }

    /**
     * 修改购物车
     */
    @PostMapping("/updateMyCart")
    public String updateMyCart(ShopCart shopCart,HttpServletResponse response) throws  Exception{
        PageData accDetail = new PageData();
        /*System.out.println(cacheManager.get("ShopHotGoods116"));
        cacheManager.set("A",1,1800);
        Integer A=(Integer)cacheManager.get("A");
        System.out.println(A);
        cacheManager.set("A",(Integer)cacheManager.get("A")-1,1800);
        Integer A1=(Integer)cacheManager.get("A");
        System.out.println(A1);
        cacheManager.set("A",(Integer)cacheManager.get("A")-1,1800);
        Integer A2=(Integer)cacheManager.get("A");
        System.out.println(A2);*/
        Jedis jedis=new Jedis("139.224.65.73",6379);
        System.out.println(jedis.get("mobileCategoryCache2"));
        return null;
       // return "redirect:/return/toShowView/"+2+"?pay_no=2222&status=1&fee_type=1";
        //response.sendRedirect("redirect:/return/toShowView/"+2+"?pay_no=2222&status=1&fee_type=1");
       /* AjaxResponse ar= new AjaxResponse();
        PageData pd =new PageData();
        pd.put("sanRate",this.r8SanRateService.getBestNewSanRate());
        ar.setData(pd);*/
        /*pd.put("store_order_sn","161119205744357092111121");
        pd.put("usercode","999");
        pd.put("phone","15011478695");
        this.storeOrderInfoService.storeOrderPayCallBack(pd,"1.00");*/
       // return ar;
    }


    /**
     * 删除购物车
     */
    @PostMapping("/deleteMyCart")
    public AjaxResponse deleteMyCart(HttpServletRequest request, Page page){
        PageData pd = new PageData();
        pd = this.getPageData();
        AjaxResponse ar= new AjaxResponse();
    try{
        Integer id=Integer.valueOf((String)pd.get("id"));
        Integer userId=Integer.valueOf((String)pd.get("userId"));
        if(id == null|| id==0){
            return fastReturn(null,false,"接口参数id异常，删除购物车失败！",CodeConstant.PARAM_ERROR);
        }else if(userId == null|| userId==0){
            return fastReturn(null,false,"接口参数userId异常，删除购物车失败！",CodeConstant.PARAM_ERROR);
        }
            ShopCart shopCart=this.shopCartService.queryCartGoodsID(id,userId);
            if(shopCart !=null && shopCart.getUserId() !=null){
                int result = this.shopCartService.deleteMyCart(id);
                if (result == 0) {
                    ar = fastReturn(null, true, "购物车中无此商品！",CodeConstant.SC_OK);
                    return ar;
                }
                ar = fastReturn(result, true, "删除购物车成功！",CodeConstant.SC_OK);
              }else{
                return fastReturn(null,false,"数据库无此购物车信息，删除购物车失败！",CodeConstant.PARAM_ERROR);
              }
            }catch (ClassCastException e){
                logger.debug(e.toString(), e);
                ar=fastReturn(null,false,"接口参数类型异常，删除购物车失败！",CodeConstant.PARAM_ERROR);
            }catch(NullPointerException e){
                logger.debug(e.toString(), e);
                ar=fastReturn(null,false,"接口参数异常，删除购物车失败！",CodeConstant.PARAM_ERROR);
            } catch (Exception e) {
                logger.debug(e.toString(), e);
                ar = fastReturn(null, false, "系统异常，删除购物车失败！",CodeConstant.SYS_ERROR);
            }
        return ar;
    }

    /**
     * 批量删除购物车
     */
    @PostMapping("/batchDelete")
    public AjaxResponse batchDelete(HttpServletRequest request, Page page){
        PageData pd = new PageData();
        pd = this.getPageData();
        page.setPd(pd);
        try{
        AjaxResponse ar= new AjaxResponse();
        String[] id=pd.getString("id").split(",");
        String userId=(String)pd.get("userId");
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("userId",userId);
        if(!StringUtil.isNotEmpty(userId)){
            return fastReturn(null,false,"接口参数userId异常，删除购物车失败！",CodeConstant.PARAM_ERROR);
        }else if(id !=null && id.length >0) {
                List<String> list = Arrays.asList(id);
                map.put("list",list);
                List list2=this.shopCartService.queryCartAllGoodsID(map);
                if(list.size() ==list2.size()){
                    this.shopCartService.batchDelete(list);
                    ar=fastReturn(null,true,"批量删除购物车成功！",CodeConstant.SC_OK);
                }else{
                    return fastReturn(null,false,"接口参数id与数据库不匹配，批量删除购物车失败！",CodeConstant.PARAM_ERROR);
                }
            }else{
                ar=fastReturn(null,false,"接口参数id异常，批量删除购物车失败！",CodeConstant.PARAM_ERROR);
            }
        } catch (ClassCastException e){
            logger.debug(e.toString(), e);
            ar=fastReturn(null,false,"接口参数类型异常，批量删除购物车失败！",CodeConstant.PARAM_ERROR);
        }catch(NullPointerException e){
            logger.debug(e.toString(), e);
            ar=fastReturn(null,false,"接口参数异常，批量删除购物车失败！",CodeConstant.PARAM_ERROR);
        }catch(Exception e){
            logger.debug(e.toString(), e);
            ar=fastReturn(null,false,"系统异常，批量删除购物车失败！",CodeConstant.SYS_ERROR);
        }
        return ar;
    }


    /**
     * 查询购物车
     */
    @GetMapping("/serchMyCart")
    public AjaxResponse serchMyCart(HttpServletRequest request, Page page){
        PageData pd = new PageData();
        pd = this.getPageData();
        AjaxResponse ar= new AjaxResponse();
        Integer userId=Integer.valueOf((String)pd.get("userId"));
        try{
            if(userId !=null && userId!=0){
                List result =new ArrayList();
                /*if(cacheManager.isExist(RedisConstantUtil.MYCAT+userId)){
                    ar=fastReturn((List)cacheManager.get(RedisConstantUtil.MYCAT + userId),true,"查询购物车缓存成功！", CodeConstant.SC_OK);
                    logAfter(logger);
                }else{
                }*/
                //TODO 若做缓存需要做数据和redis同步
                result =this.shopGoodsService.serchMyCart(userId);
                //cacheManager.set(RedisConstantUtil.MYCAT+userId,result,1800);
                ar=fastReturn(result,true,"查询首页商品列表成功！",CodeConstant.SC_OK);
            }
        }catch (ClassCastException e){
            logger.debug(e.toString(), e);
            ar=fastReturn(null,false,"接口参数userId类型异常，查询购物车失败！",CodeConstant.PARAM_ERROR);
        }catch(NullPointerException e){
            logger.debug(e.toString(), e);
            ar=fastReturn(null,false,"接口参数userId异常，查询购物车失败！",CodeConstant.PARAM_ERROR);
        } catch(Exception e){
            logger.debug(e.toString(), e);
            ar=fastReturn(null,false,"系统异常，查询购物车失败！",CodeConstant.SYS_ERROR);
        }
        return ar;
    }


    /**
     * 购物车跳转生成订单必要信息查询
     */
    @GetMapping("/myCartToGenerateOrder")
    @ApiOperation(nickname = "myCartToGenerateOrder", value = "购物车跳转生成订单必要信息查询", notes = "购物车跳转生成订单必要信息查询！！")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "查询类型", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "num", value = "购买个数", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsId", value = "商品ID", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "isPromote", value = "商品订单类型 0普通订单 1秒杀订单 2人民币订单", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse myCartToGenerateOrder(HttpServletRequest request, Page page){
        PageData pd = new PageData();
        pd = this.getPageData();
        page.setPd(pd);
        AjaxResponse ar= new AjaxResponse();
        Map<String, Object> map=new HashMap<String, Object>();
        List<ShopGoods> listt =new ArrayList<ShopGoods>();
        String userId=null;
        String goodsId=null;
        String num=null;
        String[] id=null;
        try{
        String type=(String)pd.get("type");
        String isPromote=(String)pd.get("isPromote");
        if(type.equals("directBuy")){
            userId =(String)pd.get("userId");
            goodsId=(String)pd.get("goodsId");
            num=(String)pd.get("num");
            map.put("num",num);
        }else if(type.equals("generateOrder")){
            userId =(String)pd.get("userId");
            id=pd.getString("id").split(",");
            num=(String)pd.get("num");
            map.put("userId",userId);
            map.put("num",num);
        }else if(type.equals("RMBBuy")){  //购买人民币种类商品
            goodsId=(String)pd.get("goodsId");
            map.put("toGenerateOrderInfo",this.shopGoodsService.queryRMBGoodsDetailInfoByGoodsId(goodsId));
            return fastReturn(map,true,"查询立即购买人民币商品信息成功！",CodeConstant.SC_OK);
        }
        logger.info("购物车跳转生成订单必要信息查询");
            if(!StringUtil.isNotEmpty(userId)){
                return fastReturn(null,false,"接口参数userId异常，购物车跳转生成订单必要信息查询失败！",CodeConstant.PARAM_ERROR);
            }else if(StringUtil.isNotEmpty(goodsId) && StringUtil.isNotEmpty(num)){   //立即购买信息查询  PHP秒杀下单
                listt=this.shopCartService.queryGoodsDetailInfoByGoodsId(goodsId,isPromote);
                if(listt !=null && listt.size() >0){
                    listt.get(0).setNum(num);
                    map.put("toGenerateOrderInfo",listt);
                    map.remove("userId");
                    map.remove("list");
                    map.remove("num");
                    return fastReturn(map,true,"查询立即购买商品信息成功！",CodeConstant.SC_OK);
                }else{
                    return fastReturn(map,true,"接口查询立即购买商品信息失败，查询不到相关信息！",CodeConstant.SC_OK);
                }
            }else if(id !=null && id.length >0) {
                    List<String> list = Arrays.asList(id);
                    map.put("list",list);
                    List list2=this.shopCartService.queryCartAllGoodsID(map);
                    if(list.size() ==list2.size()){ //去查询跳转生成订单页面数据查询
                        list=this.shopCartService.myCartToGenerateOrder(map);
                        map.put("toGenerateOrderInfo",list);
                        map.remove("userId");
                        map.remove("list");
                        map.remove("num");
                        ar=fastReturn(map,true,"购物车跳转生成订单必要信息查询成功！",CodeConstant.SC_OK);
                    }else{
                        return fastReturn(null,false,"接口参数id与数据库不匹配，购物车跳转生成订单必要信息查询失败！",CodeConstant.SC_OK);
                    }
            }else{
                ar=fastReturn(null,false,"接口参数id异常，购物车跳转生成订单必要信息查询失败！",CodeConstant.PARAM_ERROR);
            }
        } catch (ClassCastException e){
            logger.debug(e.toString(), e);
            ar=fastReturn(null,false,"接口参数类型异常，购物车跳转生成订单必要信息查询失败！",CodeConstant.PARAM_ERROR);
        }catch(NullPointerException e){
            logger.debug(e.toString(), e);
            ar=fastReturn(null,false,"接口参数异常，购物车跳转生成订单必要信息查询失败！",CodeConstant.PARAM_ERROR);
        }catch(Exception e){
            logger.debug(e.toString(), e);
            ar=fastReturn(null,false,"系统异常，购物车跳转生成订单必要信息查询失败！",CodeConstant.SYS_ERROR);
        }
        return ar;
    }
}
