/*package com.ecochain.ledger.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.ecochain.ledger.annotation.LoginVerify;
import com.ecochain.ledger.base.BaseWebService;
import com.ecochain.ledger.constants.CodeConstant;
import com.ecochain.ledger.constants.Constant;
import com.ecochain.ledger.constants.CookieConstant;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.service.AccDetailService;
import com.ecochain.ledger.service.ShopOrderInfoService;
import com.ecochain.ledger.service.SysGenCodeService;
import com.ecochain.ledger.service.SysMaxnumService;
import com.ecochain.ledger.service.UserLoginService;
import com.ecochain.ledger.service.UserWalletService;
import com.ecochain.ledger.service.UsersDetailsService;
import com.ecochain.ledger.util.AjaxResponse;
import com.ecochain.ledger.util.DateUtil;
import com.ecochain.ledger.util.Logger;
import com.ecochain.ledger.util.RequestUtils;
import com.ecochain.ledger.util.SessionUtil;
import com.ecochain.ledger.util.StringUtil;
*//**
 * 支付控制类
 * @author zhangchunming
 *//*
@RestController
@RequestMapping(value = "/api/pay")
@Api(value = "支付")
public class PayWebService extends BaseWebService{
    
    private final Logger logger = Logger.getLogger(PayWebService.class);
    
    @Autowired
    private AccDetailService accDetailService;
    @Autowired
    private SysGenCodeService sysGenCodeService;
    @Autowired
    private UserWalletService userWalletService;
    @Autowired
    private SysMaxnumService sysMaxnumService;
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private UsersDetailsService usersDetailsService;
    @Autowired
    private ShopOrderInfoService shopOrderInfoService;

    *//**
     * @param request
     * @describe:立即支付
     * @author: zhangchunming
     * @date: 2016年11月9日下午9:59:50
     * @return: AjaxResponse
     *//*
    @LoginVerify
    @PostMapping("/payNow")
    @ApiOperation(nickname = "立即支付", value = "立即支付", notes = "立即支付！")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "CSESSIONID", value = "会话token", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "order_no", value = "订单号", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "order_amount", value = "付款金额", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "trans_password", value = "交易密码", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse payNow(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        AjaxResponse ar = new AjaxResponse();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            pd.put("user_id", String.valueOf(user.get("id")));
            pd.put("seeds", user.getString("seeds"));
            pd.put("user_type", String.valueOf(user.getString("user_type")));
            pd.put("operator", String.valueOf(user.getString("account")));
            
            if (StringUtil.isEmpty(pd.getString("order_no"))) {
                ar.setSuccess(false);
                ar.setMessage("订单号不能为空");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(StringUtil.isEmpty(pd.getString("trans_password"))){
                ar.setMessage("交易密码不能为空！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setSuccess(false);
                return ar;
            }
            Boolean existTransPassword = usersDetailsService.isExistTransPassword(pd);
            if(!existTransPassword){
                ar.setMessage("交易密码错误！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setSuccess(false);
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
            String hlb_amnt = String.valueOf(userWallet.get("hlb_amnt"));
            if (new BigDecimal(String.valueOf(shopOrderInfo.get("order_amount"))).compareTo(new BigDecimal(hlb_amnt)) > 0) {
                ar.setSuccess(false);
                ar.setMessage("您的合链币余额不足，请使用钱包兑换！");
                ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                return ar;
            }
            pd.put("order_no", shopOrderInfo.getString("order_no"));
            pd.put("shop_order_no", shopOrderInfo.getString("order_no"));
            pd.put("order_id", String.valueOf(shopOrderInfo.get("order_id")));
            pd.put("shop_order_id", String.valueOf(shopOrderInfo.get("order_id")));
            pd.put("order_amount", shopOrderInfo.get("order_amount"));
            pd.put("mobile_phone", user.getString("mobile_phone"));

            pd.put("shop_order_no", shopOrderInfo.getString("order_no"));

            //商品表里有供应商有专门价格无需查询兑换费率
            //锁定订单
            boolean lockOrderByOrderNo = shopOrderInfoService.lockOrderByOrderNo(pd);
            logger.info("支付订单锁定结果lockOrderByOrderNo："+lockOrderByOrderNo);
            
            boolean payNow = shopOrderInfoService.payNow(pd, Constant.VERSION_NO);
            if (payNow) {
                ar.setSuccess(true);
                ar.setMessage("交易处理中...请前往账单查看支付结果");
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
}
*/