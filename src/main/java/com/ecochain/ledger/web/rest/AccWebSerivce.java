package com.ecochain.ledger.web.rest;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import tk.mybatis.mapper.util.StringUtil;

import com.alibaba.fastjson.JSONObject;
import com.ecochain.ledger.annotation.LoginVerify;
import com.ecochain.ledger.base.BaseWebService;
import com.ecochain.ledger.constants.CodeConstant;
import com.ecochain.ledger.constants.Constant;
import com.ecochain.ledger.constants.CookieConstant;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.service.AccDetailService;
import com.ecochain.ledger.service.DigitalCoinService;
import com.ecochain.ledger.service.PayOrderService;
import com.ecochain.ledger.service.SendVodeService;
import com.ecochain.ledger.service.SysGenCodeService;
import com.ecochain.ledger.service.SysMaxnumService;
import com.ecochain.ledger.service.UserBankService;
import com.ecochain.ledger.service.UserLoginService;
import com.ecochain.ledger.service.UserWalletService;
import com.ecochain.ledger.service.UsersDetailsService;
import com.ecochain.ledger.util.AjaxResponse;
import com.ecochain.ledger.util.DateUtil;
import com.ecochain.ledger.util.Logger;
import com.ecochain.ledger.util.MD5Util;
import com.ecochain.ledger.util.OrderGenerater;
import com.ecochain.ledger.util.RequestUtils;
import com.ecochain.ledger.util.RestUtil;
import com.ecochain.ledger.util.SessionUtil;
import com.ecochain.ledger.util.Validator;
import com.github.pagehelper.PageInfo;

/**
 * 账户控制类
 * @author zhangchunming
 */
@RestController
@RequestMapping("/api/rest/acc")
@Api(value = "账户管理")
public class AccWebSerivce extends BaseWebService{
    
    private final Logger logger = Logger.getLogger(AccWebSerivce.class);
    
    @Autowired
    private AccDetailService accDetailService;
    @Autowired
    private SysGenCodeService sysGenCodeService;
    @Autowired
    private PayOrderService payOrderService;
    /*@Autowired
    private ISessionUtil SessionUtil;*/
    @Autowired
    private UserWalletService userWalletService;
    @Autowired
    private UsersDetailsService usersDetailsService;
    @Autowired
    private SysMaxnumService sysMaxnumService;
   /* @Autowired
    private CacheManager cacheManager;*/
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private DigitalCoinService digitalCoinService;
    @Autowired
    private SendVodeService sendVodeService;
    @Autowired
    private UserBankService userBankService;

    /**
     * @describe:查询账户列表
     * @author: zhangchunming
     * @date: 2016年11月7日下午8:02:29
     * @param request
     * @param page
     * @return: AjaxResponse
     */
    @LoginVerify
    @PostMapping("/listPageAcc")
    @ApiOperation(nickname = "账户流水", value = "账户流水", notes = "账户流水")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "CSESSIONID", value = "会话token", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "page", value = "当前页", required = false, paramType = "query", dataType = "String")
    })
    public AjaxResponse listPageAcc(HttpServletRequest request){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String, Object>();
        try {
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            PageData pd = new PageData();
            pd = this.getPageData();
            pd.put("user_id", user.get("id"));
            //查询账户列表
            List<PageData> listPageAcc  = accDetailService.listPageAcc(pd);
            //查询小计
//            PageData subTotal = accDetailService.getSubTotal(pd, Constant.VERSION_NO);
//            data.put("subTotal", subTotal);
            data.put("pageInfo", new PageInfo<PageData>(listPageAcc));
            ar.setData(data);
            ar.setSuccess(true);
            ar.setMessage("查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }   
        return ar;
    }
    /**
     * @describe:提现接口
     * @author: zhangchunming
     * @date: 2016年11月1日下午1:51:41
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    /*@LoginVerify
    @RequestMapping(value="/withdrawal1", method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse withdrawal1(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        try {
            PageData pd = new PageData();
            pd = this.getPageData();
            logger.info("**************提现*******pd value is "+pd.toString());
            String key = RequestUtils.getCookieValueByKey(CookieConstant.CSESSIONID, request, response);
            String userstr = SessionUtil.getAttibuteForUser(key);
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            String userType = user.getString("user_type");
            boolean hasWithDrawaling = payOrderService.isHasWithDrawaling(String.valueOf(user.get("id")));
            if(hasWithDrawaling){
                ar.setSuccess(false);
                ar.setMessage("您的上笔提现订单火速处理中，请等待处理完毕再进行此操作！");
                ar.setErrorCode(CodeConstant.ERROR_PROCESSING);
                return ar;
            }
            if(StringUtil.isEmpty(pd.getString("money"))){
                ar.setSuccess(false);
                ar.setMessage("请输入提现金额");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(!Validator.isMoney(pd.getString("money"))){
                ar.setSuccess(false);
                ar.setMessage("提现金额格式有误，请按提示输入");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(StringUtil.isEmpty(pd.getString("pay_type"))){
                ar.setSuccess(false);
                ar.setMessage("请选择提现账号");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if("1".equals(pd.getString("pay_type"))){//1-支付宝 2-微信 3-银联  
               if(StringUtil.isEmpty(pd.getString("revbankaccno"))){//提现账号（支付宝/银行卡号）
                   ar.setSuccess(false);
                   ar.setMessage("请输入您的支付宝账号");
                   ar.setErrorCode(CodeConstant.PARAM_ERROR);
                   return ar;
               }
               if(StringUtil.isEmpty(pd.getString("name"))){//所选银行名称或者支付宝名称
                   ar.setSuccess(false);
                   ar.setMessage("请输入您的支付宝名称");
                   ar.setErrorCode(CodeConstant.PARAM_ERROR);
                   return ar;
               } 
            }else if("3".equals(pd.getString("pay_type"))){//银联提现
                if(StringUtil.isEmpty(pd.getString("revorgname"))){//所选银行名称或者支付宝名称
                    ar.setSuccess(false);
                    ar.setMessage("请选择银行");
                    ar.setErrorCode(CodeConstant.PARAM_ERROR);
                    return ar;
                }
                if(StringUtil.isEmpty(pd.getString("revbankaccno"))){//提现账号（支付宝/银行卡号）
                    ar.setSuccess(false);
                    ar.setMessage("输入您的银行卡卡号");
                    ar.setErrorCode(CodeConstant.PARAM_ERROR);
                    return ar;
                } 
                if(StringUtil.isEmpty(pd.getString("name"))){//持卡人姓名
                    ar.setSuccess(false);
                    ar.setMessage("输入持卡人真实姓名");
                    ar.setErrorCode(CodeConstant.PARAM_ERROR);
                    return ar;
                } 
            }
            
            String codeName = "";
            if("1".equals(userType)){
                ar.setSuccess(false);
                ar.setMessage("对不起，您没有提现权限");
                ar.setErrorCode(CodeConstant.ERROR_NO_WITHDRAWAL);
                return ar;
            }else if("2".equals(userType)){
                ar.setSuccess(false);
                ar.setMessage("对不起，您没有提现权限");
                ar.setErrorCode(CodeConstant.ERROR_NO_WITHDRAWAL);
                return ar;
            }else if("3".equals(userType)){//店铺
                codeName = "DP_WITHDRAWAL_LIMIT";
            }else if("4".equals(userType)){//供应商
                codeName = "DL_WITHDRAWAL_LIMIT";
            }else if("5".equals(userType)){//代理商
                codeName = "GY_WITHDRAWAL_LIMIT";
            }
            
            PageData userWallet = userWalletService.getWalletByUserId(String.valueOf(user.get("id")), Constant.VERSION_NO);
            if((new BigDecimal(pd.getString("money"))).compareTo(new BigDecimal(String.valueOf(userWallet.get("money"))))>0){
                ar.setSuccess(false);
                ar.setMessage("余额不足，无法提现");
                ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                return ar;
            }
            *//**************************提现金额上下限判断-----------start***********************//*
            //提现上限下限（现金/每次）
            String uplimit = "";
            String lowlimit = "";
            List<PageData> codeList =  sysGenCodeService.findByGroupCode("LIMIT_RATE", Constant.VERSION_NO);
            for(PageData code:codeList){
                if(codeName.equals(code.get("code_name"))){
                    uplimit = String.valueOf(code.get("uplimit"));
                    lowlimit = String.valueOf(code.get("lowlimit"));
                }
            }
            if(StringUtil.isNotEmpty(uplimit)&&new BigDecimal(pd.getString("money")).compareTo(new BigDecimal(uplimit))>0){//超出上线
                ar.setSuccess(false);
                ar.setMessage("提现金额超出单次上限，请重新输入");
                ar.setErrorCode(CodeConstant.ERROR_OVER_UPLIMIT);
                return ar;
            }
            if(StringUtil.isNotEmpty(lowlimit)&&new BigDecimal(pd.getString("money")).compareTo(new BigDecimal(lowlimit))<0){
                ar.setSuccess(false);
                ar.setMessage("提现金额低于单次下限，请重新输入");
                ar.setErrorCode(CodeConstant.ERROR_LOWER_LOWLIMIT);
                return ar;
            }
            *//**************************提现金额上下限判断-----------end***********************//*
            //生成支付号
            Long tMaxno =sysMaxnumService.findMaxNo("payno", Constant.VERSION_NO);
            if(tMaxno==null){
                logger.info("payno  tSysMaxnum findMaxNo  is null!");
                ar.setSuccess(false);
                ar.setMessage("系统繁忙,请稍后重试！");
                ar.setErrorCode(CodeConstant.SYS_ERROR);
                return ar;
            }
            String pay_no =tMaxno.toString();
            
            pd.put("user_id", user.get("id"));
            pd.put("pay_no", pay_no);//工单号
            pd.put("fee_type", "2");//工单号
            pd.put("cuy_type", "3");//1-三界石2-三界宝3-人民币
            pd.put("revorgname", "3");//银行名称、微信、支付宝名称
            pd.put("revbankaccno", pd.getString("revbankaccno"));//银行、支付宝账号
            if(StringUtil.isNotEmpty(pd.getString("name"))){
                pd.put("remark2", pd.getString("name"));//持卡人姓名或支付宝姓名
            }
            pd.put("txamnt", pd.getString("money"));//1-三界石2-三界宝3-人民币
            pd.put("status", "0");//0-待审核 1-成功 2-失败
            pd.put("txdate", DateUtil.getCurrDateTime());
            pd.put("operator", user.getString("mobile_phone"));
            if(payOrderService.applyWithDrawal(pd, Constant.VERSION_NO)){
                String phone = user.getString("mobile_phone");
                String content = "";
                if("1".equals(pd.getString("pay_type"))){//1-支付宝 2-微信 3-银联
                    content = "您的提现申请已经通过，我们已给您的账号（支付宝账号["+pd.getString("revbankaccno")+"]）转账，请注意查收！";
                }else if("3".equals(pd.getString("pay_type"))){//1-支付宝 2-微信 3-银联
                    //银行尾号后四位
                    String banklast4 =  pd.getString("revbankaccno").substring(pd.getString("revbankaccno").length()-4);
                    content = "您的提现申请已经通过，我们已给您的账号（建设银行尾号["+banklast4+"]）转账，请注意查收！";
                }
                if(StringUtil.isNotEmpty(phone)&&StringUtil.isNotEmpty(content)){
                    List<PageData> codeList1 =sysGenCodeService.findByGroupCode("SENDSMS_FLAG", Constant.VERSION_NO);
                    String smsflag ="";
                    for(PageData mapObj:codeList1){
                        if("SENDSMS_FLAG".equals(mapObj.get("code_name"))){
                            smsflag = mapObj.get("code_value").toString();
                            logger.info("---------发送验证码--------短信发送标识smsflag："+smsflag);
                        }
                    }
                    if(StringUtil.isEmpty(smsflag)){
                        ar.setSuccess(false);
                        ar.setMessage("系统短信参数配置有误");
                        ar.setErrorCode(CodeConstant.SYS_ERROR);
                        return ar;
                    }
                    if("1".equals(smsflag)){
                        SMSUtil.sendSMS_ChinaNet1(phone, content, SMSUtil.notice_productid);
                    }
                }
                ar.setSuccess(true);
                ar.setMessage("提现申请成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }   
        return ar;
    }*/
    
    @LoginVerify
    @PostMapping("/withdrawal")
    @ApiOperation(nickname = "提现", value = "提现", notes = "提现")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "CSESSIONID", value = "会话token", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "coin_name", value = "币种名称", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "address", value = "提现地址", required = false, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "revbankaccno", value = "银行账号", required = false, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "revbankdepname", value = "开户行", required = false, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "amount", value = "提现数量", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "network_fee", value = "网络手续费", required = false, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "trans_password", value = "资金密码", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "vcode", value = "验证码", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse withdrawal(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        try {
            PageData pd = new PageData();
            pd = this.getPageData();
            logger.info("**************提现*******pd value is "+pd.toString());
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            pd.put("account", user.getString("account"));
            pd.put("user_name", user.getString("user_name"));
            pd.put("user_id", String.valueOf(user.get("id")));
//            String userType = user.getString("user_type");
            /*boolean hasWithDrawaling = payOrderService.isHasWithDrawaling(String.valueOf(user.get("id")));
            if(hasWithDrawaling){
                ar.setSuccess(false);
                ar.setMessage("您的上笔提现订单火速处理中，请等待处理完毕再进行此操作！");
                ar.setErrorCode(CodeConstant.ERROR_PROCESSING);
                return ar;
            }*/
            if(StringUtil.isEmpty(pd.getString("coin_name"))){
                ar.setSuccess(false);
                ar.setMessage("请输入提现币种！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if("RMB".equals(pd.getString("coin_name"))){
                if(StringUtil.isEmpty(pd.getString("revbankaccno"))||StringUtil.isEmpty(pd.getString("revbankdepname"))){
                    ar.setSuccess(false);
                    ar.setMessage("请选择银行！");
                    ar.setErrorCode(CodeConstant.PARAM_ERROR);
                    return ar;
                }
                if(StringUtil.isEmpty(pd.getString("amount"))){
                    ar.setSuccess(false);
                    ar.setMessage("请输入提现金额");
                    ar.setErrorCode(CodeConstant.PARAM_ERROR);
                    return ar;
                }
                if(!Validator.isMoney2(pd.getString("amount"))){
                    ar.setSuccess(false);
                    ar.setMessage("提现金额格式有误，请按提示输入");
                    ar.setErrorCode(CodeConstant.PARAM_ERROR);
                    return ar;
                }
            }else{
                if(StringUtil.isEmpty(pd.getString("address"))){
                    ar.setSuccess(false);
                    ar.setMessage("请添加提现地址！");
                    ar.setErrorCode(CodeConstant.PARAM_ERROR);
                    return ar;
                }
                if(StringUtil.isEmpty(pd.getString("amount"))){
                    ar.setSuccess(false);
                    ar.setMessage("请输入提币数量");
                    ar.setErrorCode(CodeConstant.PARAM_ERROR);
                    return ar;
                }
                if(!Validator.isMoney4(pd.getString("amount"))){
                    ar.setSuccess(false);
                    ar.setMessage("提币数量格式有误，请按提示输入");
                    ar.setErrorCode(CodeConstant.PARAM_ERROR);
                    return ar;
                }
                if(StringUtil.isEmpty(pd.getString("network_fee"))){
                    ar.setSuccess(false);
                    ar.setMessage("请输入网络手续费！");
                    ar.setErrorCode(CodeConstant.PARAM_ERROR);
                    return ar;
                }
            }
            
            if(StringUtil.isEmpty(pd.getString("trans_password"))){
                ar.setSuccess(false);
                ar.setMessage("请输入资金密码！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            if(StringUtil.isEmpty(pd.getString("vcode"))){
                ar.setSuccess(false);
                ar.setMessage("请输入短信验证码！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            /*if(StringUtil.isEmpty(pd.getString("pay_type"))){
                ar.setSuccess(false);
                ar.setMessage("请选择提现账号");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }*/
            /*if("1".equals(pd.getString("pay_type"))){//1-支付宝 2-微信 3-银联  
               if(StringUtil.isEmpty(pd.getString("revbankaccno"))){//提现账号（支付宝/银行卡号）
                   ar.setSuccess(false);
                   ar.setMessage("请输入您的支付宝账号");
                   ar.setErrorCode(CodeConstant.PARAM_ERROR);
                   return ar;
               }
               if(StringUtil.isEmpty(pd.getString("name"))){//所选银行名称或者支付宝名称
                   ar.setSuccess(false);
                   ar.setMessage("请输入您的支付宝名称");
                   ar.setErrorCode(CodeConstant.PARAM_ERROR);
                   return ar;
               } 
            }else if("3".equals(pd.getString("pay_type"))){//银联提现
                if(StringUtil.isEmpty(pd.getString("revorgname"))){//所选银行名称或者支付宝名称
                    ar.setSuccess(false);
                    ar.setMessage("请选择银行");
                    ar.setErrorCode(CodeConstant.PARAM_ERROR);
                    return ar;
                }
                if(StringUtil.isEmpty(pd.getString("revbankaccno"))){//提现账号（支付宝/银行卡号）
                    ar.setSuccess(false);
                    ar.setMessage("输入您的银行卡卡号");
                    ar.setErrorCode(CodeConstant.PARAM_ERROR);
                    return ar;
                } 
                if(StringUtil.isEmpty(pd.getString("name"))){//持卡人姓名
                    ar.setSuccess(false);
                    ar.setMessage("输入持卡人真实姓名");
                    ar.setErrorCode(CodeConstant.PARAM_ERROR);
                    return ar;
                } 
            }else if("2".equals(pd.getString("pay_type"))){//微信提现
                if(StringUtil.isEmpty(pd.getString("revbankaccno"))){//提现账号（支付宝/银行卡号）
                    ar.setSuccess(false);
                    ar.setMessage("请输入您的微信账号");
                    ar.setErrorCode(CodeConstant.PARAM_ERROR);
                    return ar;
                }
            }*/
            
            /*String codeName = "";
            if("1".equals(userType)){
                ar.setSuccess(false);
                ar.setMessage("对不起，您没有提现权限");
                ar.setErrorCode(CodeConstant.ERROR_NO_WITHDRAWAL);
                return ar;
            }else if("2".equals(userType)){
                ar.setSuccess(false);
                ar.setMessage("对不起，您没有提现权限");
                ar.setErrorCode(CodeConstant.ERROR_NO_WITHDRAWAL);
                return ar;
            }else if("3".equals(userType)){//店铺
                codeName = "DP_WITHDRAWAL_LIMIT";
            }else if("4".equals(userType)){//供应商
                codeName = "DL_WITHDRAWAL_LIMIT";
            }else if("5".equals(userType)){//代理商
                codeName = "GY_WITHDRAWAL_LIMIT";
            }*/
            
            PageData userWallet = userWalletService.getWalletByUserId(String.valueOf(user.get("id")), Constant.VERSION_NO);
            
            if("RMB".equals(pd.getString("coin_name"))){
                if((new BigDecimal(pd.getString("amount"))).compareTo(new BigDecimal(String.valueOf(userWallet.get("money"))))>0){
                    ar.setSuccess(false);
                    ar.setMessage("您的余额不足！");
                    ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                    return ar;
                }
            }else if("BTC".equals(pd.getString("coin_name"))){
                if((new BigDecimal(pd.getString("amount"))).compareTo(new BigDecimal(String.valueOf(userWallet.get("btc_amnt"))))>0){
                    ar.setSuccess(false);
                    ar.setMessage("您的余额不足！");
                    ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                    return ar;
                }
            }else if("LTC".equals(pd.getString("coin_name"))){
                if((new BigDecimal(pd.getString("amount"))).compareTo(new BigDecimal(String.valueOf(userWallet.get("ltc_amnt"))))>0){
                    ar.setSuccess(false);
                    ar.setMessage("您的余额不足！");
                    ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                    return ar;
                }
            }else if("ETH".equals(pd.getString("coin_name"))){
                if((new BigDecimal(pd.getString("amount"))).compareTo(new BigDecimal(String.valueOf(userWallet.get("eth_amnt"))))>0){
                    ar.setSuccess(false);
                    ar.setMessage("您的余额不足！");
                    ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                    return ar;
                }
            }else if("ETC".equals(pd.getString("coin_name"))){
                if((new BigDecimal(pd.getString("amount"))).compareTo(new BigDecimal(String.valueOf(userWallet.get("etc_amnt"))))>0){
                    ar.setSuccess(false);
                    ar.setMessage("您的余额不足！");
                    ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                    return ar;
                }
            }else if("HLC".equals(pd.getString("coin_name"))){
                if((new BigDecimal(pd.getString("amount"))).compareTo(new BigDecimal(String.valueOf(userWallet.get("hlc_amnt"))))>0){
                    ar.setSuccess(false);
                    ar.setMessage("您的余额不足！");
                    ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                    return ar;
                }
            }
            pd.put("trans_password", MD5Util.getMd5Code(pd.getString("trans_password")));
            Boolean existTransPassword = usersDetailsService.isExistTransPassword(pd);
            if(!existTransPassword){
                ar.setMessage("交易密码错误！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setSuccess(false);
                return ar;
            }
            
            //半小时之内的短信验证码有效
            String tVcode =sendVodeService.findVcodeByPhone(user.getString("account"),Constant.CUR_SYS_CODE); 
           /* if(tVcode==null||!pd.getString("vcode").equals(tVcode)){
                ar.setSuccess(false);
                ar.setMessage("验证码输入不正确！");
                return ar;
            }*/
            if(tVcode==null){
                ar.setSuccess(false);
                ar.setMessage("验证码已失效，请重新获取");
                ar.setErrorCode(CodeConstant.OVERTIME_VCODE);
                return ar;
            }else if(!pd.getString("vcode").equalsIgnoreCase(tVcode)){
                ar.setSuccess(false);
                ar.setMessage("验证码错误，请重新输入");
                ar.setErrorCode(CodeConstant.ERROR_VCODE);
                return ar;
            }
            
            /**************************提现金额上下限判断-----------start***********************//*
            //提现上限下限（现金/每次）
            String uplimit = "";
            String lowlimit = "";
            List<PageData> codeList =  sysGenCodeService.findByGroupCode("LIMIT_RATE", Constant.VERSION_NO);
            for(PageData code:codeList){
                if(codeName.equals(code.get("code_name"))){
                    uplimit = String.valueOf(code.get("uplimit"));
                    lowlimit = String.valueOf(code.get("lowlimit"));
                }
            }
            if(StringUtil.isNotEmpty(uplimit)&&new BigDecimal(pd.getString("money")).compareTo(new BigDecimal(uplimit))>0){//超出上线
                ar.setSuccess(false);
                ar.setMessage("提现金额超出单次上限，请重新输入");
                ar.setErrorCode(CodeConstant.ERROR_OVER_UPLIMIT);
                return ar;
            }
            if(StringUtil.isNotEmpty(lowlimit)&&new BigDecimal(pd.getString("money")).compareTo(new BigDecimal(lowlimit))<0){
                ar.setSuccess(false);
                ar.setMessage("提现金额低于单次下限，请重新输入");
                ar.setErrorCode(CodeConstant.ERROR_LOWER_LOWLIMIT);
                return ar;
            }
            *//**************************提现金额上下限判断-----------end***********************/
            //生成支付号
            /*Long tMaxno =sysMaxnumService.findMaxNo("payno", Constant.VERSION_NO);
            if(tMaxno==null){
                logger.info("payno  tSysMaxnum findMaxNo  is null!");
                ar.setSuccess(false);
                ar.setMessage("系统繁忙,请稍后重试！");
                ar.setErrorCode(CodeConstant.SYS_ERROR);
                return ar;
            }
            String pay_no =tMaxno.toString();*/
            String pay_no =OrderGenerater.generateOrderNo();
            
            pd.put("user_id", user.get("id"));
            pd.put("pay_no", pay_no);//工单号
            pd.put("fee_type", "2");//费用类型：提现
            pd.put("cuy_type", "1");//1-人民币 2-BTC 3-LTC 4-ETH 5-ETC 6-HLC
            pd.put("revorgname", "3");//银行名称、微信、支付宝名称
           /* if(StringUtil.isNotEmpty(pd.getString("name"))){
                pd.put("remark2", pd.getString("name"));//持卡人姓名或支付宝姓名
            }*/
            pd.put("txamnt", pd.getString("amount"));//1-人民币 2-BTC 3-LTC 4-ETH 5-ETC 6-HLC
            pd.put("status", "1");//0-待审核 1-成功 2-失败（无需审核直接成功！）
            pd.put("txdate", DateUtil.getCurrDateTime());
            pd.put("confirm_time", pd.getString("txdate"));//确认时间（无需审核直接成功！）
            pd.put("operator", user.getString("account"));
            if(payOrderService.applyWithDrawal(pd, Constant.VERSION_NO)){
                if(!"RMB".equals(pd.getString("coin_name"))){
                    logger.info("============提现========区块链钱包接口调用========start================");
                    String kql_url =null;
                    List<PageData> codeList =sysGenCodeService.findByGroupCode("QKL_URL", Constant.VERSION_NO);
                    for(PageData mapObj:codeList){
                        if("QKL_URL".equals(mapObj.get("code_name"))){
                            kql_url = mapObj.get("code_value").toString();
                        }
                    }
                    PageData userLoginInfo = userLoginService.getUserLoginByAccount(pd.getString("account"), Constant.VERSION_NO);
                    String jsonStr = RestUtil.restGetPath(kql_url+"/sendMoney/"+pd.getString("user_name")+"/"+userLoginInfo.getString("password")+"/"+pd.getString("amount")+"/"+pd.getString("address"));
                    JSONObject jsonObj = JSONObject.parseObject(jsonStr);
                    if(!jsonObj.getBoolean("success")){
                        logger.info("message="+jsonObj.getString("message"));
                        throw new Exception(jsonObj.getString("message"));
                    }
                    
                   /* PageData tpd = new PageData();
                    tpd.put("public_key", jsonObj.getString("data"));
                    tpd.put("address", jsonObj.getString("data"));
                    tpd.put("id", pd.get("user_id"));
                    logger.info("调动态库tpd value="+tpd.toString());
                    this.updateByIdSelective(tpd, versionNo);*/
                    logger.info("==========提现==========区块链钱包接口调用========end================");
                }
                ar.setSuccess(true);
                ar.setMessage("提现申请成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }   
        return ar;
    }
    /*
     * 导出会员账单到EXCEL
     * @return
     */
   /* @LoginVerify
    @RequestMapping(value="/excel", method=RequestMethod.POST)
    public ModelAndView exportExcel(HttpServletRequest request){
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        try{
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            pd.put("user_id", user.get("id"));
            Map<String,Object> dataMap = new HashMap<String,Object>();
            List<String> titles = new ArrayList<String>();
            
            titles.add("订单号");      //1
            titles.add("时间");       //2
            titles.add("交易类型");           //3
            titles.add("三界石");           //4
            titles.add("三界宝");           //5
            titles.add("人民币");           //6
            
            dataMap.put("titles", titles);
            
            //查询账户列表
            List<PageData> listPageAcc = accDetailService.getAccList(pd, Constant.VERSION_NO);
            //查询小计
            PageData subTotal = accDetailService.getSubTotal(pd, Constant.VERSION_NO);
            List<PageData> varList = new ArrayList<PageData>();
            for(int i=0;i<listPageAcc.size();i++){
                PageData vpd = new PageData();
                vpd.put("var1", listPageAcc.get(i).getString("otherno"));     //1
                vpd.put("var2", listPageAcc.get(i).getString("caldate"));       //2
                vpd.put("var3", listPageAcc.get(i).getString("acc_name"));         //3
                vpd.put("var4", listPageAcc.get(i).get("wlbi_amnt"));    //4
                vpd.put("var5", listPageAcc.get(i).get("wlbao_amnt"));        //5
                vpd.put("var6", listPageAcc.get(i).get("rmb_amnt"));        //6
                varList.add(vpd);
            }
            
            dataMap.put("varList", varList);
            
            ObjectExcelView erv = new ObjectExcelView();                    //执行excel操作
            
            mv = new ModelAndView(erv,dataMap);
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv;
    }*/
    
    /*
     * 导出会员账单到EXCEL
     * @return
     */
    /*@LoginVerify
    @RequestMapping(value="/exportExcel", method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse exportExcel(HttpServletRequest request){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String,Object>();
        PageData pd = new PageData();
        pd = this.getPageData();
        try{
            if(StringUtil.isEmpty(pd.getString("startTime"))&&StringUtil.isEmpty(pd.getString("endTime"))&&StringUtil.isEmpty(pd.getString("search_date"))){
                ar.setSuccess(false);
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setMessage("请选择导出时间且时间不能超出3个月哦！");
                return ar;
            }
            if(StringUtil.isEmpty(pd.getString("search_date"))){
                if(StringUtil.isEmpty(pd.getString("startTime"))&&!StringUtil.isEmpty(pd.getString("endTime"))){
                    ar.setSuccess(false);
                    ar.setErrorCode(CodeConstant.PARAM_ERROR);
                    ar.setMessage("请选择开始时间，注意时间不能超出3个月哦！");
                    return ar; 
                }
                if(!StringUtil.isEmpty(pd.getString("startTime"))&&StringUtil.isEmpty(pd.getString("endTime"))){
                    ar.setSuccess(false);
                    ar.setErrorCode(CodeConstant.PARAM_ERROR);
                    ar.setMessage("请选择结束时间，注意时间不能超出3个月哦！");
                    return ar; 
                }
            }
            if(DateUtil.monthsBetween(pd.getString("startTime"),pd.getString("endTime"))>3){//只能导出3个月内的数据
                ar.setSuccess(false);
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setMessage("目前只支持导出3个月以内的数据哦，请重新选择导出时间！");
                return ar; 
            }
            if(DateUtil.compareDateStr(pd.getString("startTime"),pd.getString("endTime"))>0){//开始时间必须小于结束时间
                ar.setSuccess(false);
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setMessage("结束时间必须大于等于开始时间哦！");
                return ar; 
            }
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            pd.put("user_id", user.get("id"));
            //查询账户列表
            List<PageData> listPageAcc = accDetailService.getAccList(pd, Constant.VERSION_NO);
            //查询小计
            PageData subTotal = accDetailService.getSubTotal(pd, Constant.VERSION_NO);
            data.put("listPageAcc", listPageAcc);
            data.put("subTotal", subTotal);
            ar.setData(data);
            ar.setMessage("查询成功！");
            ar.setSuccess(true);
        } catch(Exception e){
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }
        return ar;
    }*/
    
    /**
     * @describe:查询账户余额
     * @author: zhangchunming
     * @date: 2016年11月1日下午7:20:54
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    @LoginVerify
    @PostMapping("/getWallet")
    @ApiOperation(nickname = "查询账户余额", value = "查询账户余额", notes = "查询账户余额")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "CSESSIONID", value = "会话token", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse getWallet(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String,Object>();
        try {
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            PageData userWallet = userWalletService.getWalletByUserId(String.valueOf(user.get("id")), Constant.VERSION_NO);
            BigDecimal totalMoney = new BigDecimal(String.valueOf(userWallet.get("money")));
            List<PageData> listCoin = digitalCoinService.getAllCoinPrice();
            BigDecimal hlc_money = new BigDecimal("0");
            BigDecimal btc_money = new BigDecimal("0");
            BigDecimal ltc_money = new BigDecimal("0");
            BigDecimal eth_money = new BigDecimal("0");
            BigDecimal etc_money = new BigDecimal("0");
            for(PageData coin:listCoin){
                if("HLC".equals(coin.getString("coin_name"))){//合链币
                    String coinPrice  = coin.getString("coin_rate").split(":")[0];
                    userWallet.put("hlc_rate", coinPrice);
                    String hlc_amnt =String.valueOf(userWallet.get("hlc_amnt"));
                    hlc_money = new BigDecimal(hlc_amnt).multiply(new BigDecimal(coinPrice));
                    totalMoney = hlc_money.add(totalMoney);
                }else if("BTC".equals(coin.getString("coin_name"))){
                    String coinPrice  = coin.getString("coin_rate").split(":")[0];
                    userWallet.put("btc_rate", coinPrice);
                    String btc_amnt =String.valueOf(userWallet.get("btc_amnt"));
                    btc_money = new BigDecimal(btc_amnt).multiply(new BigDecimal(coinPrice));
                    totalMoney = btc_money.add(totalMoney);
                    userWallet.put("btc_address", user.getString("address"));
                }else if("LTC".equals(coin.getString("coin_name"))){
                    String coinPrice  = coin.getString("coin_rate").split(":")[0];
                    userWallet.put("ltc_rate", coinPrice);
                    String ltc_amnt =String.valueOf(userWallet.get("ltc_amnt"));
                    ltc_money = new BigDecimal(ltc_amnt).multiply(new BigDecimal(coinPrice));
                    totalMoney = ltc_money.add(totalMoney);
                }else if("ETH".equals(coin.getString("coin_name"))){
                    String coinPrice  = coin.getString("coin_rate").split(":")[0];
                    userWallet.put("eth_rate", coinPrice);
                    String eth_amnt =String.valueOf(userWallet.get("eth_amnt"));
                    eth_money = new BigDecimal(eth_amnt).multiply(new BigDecimal(coinPrice));
                    totalMoney = eth_money.add(totalMoney);
                }else if("ETC".equals(coin.getString("coin_name"))){
                    String coinPrice  = coin.getString("coin_rate").split(":")[0];
                    userWallet.put("etc_rate", coinPrice);
                    String etc_amnt =String.valueOf(userWallet.get("etc_amnt"));
                    etc_money = new BigDecimal(etc_amnt).multiply(new BigDecimal(coinPrice));
                    totalMoney = etc_money.add(totalMoney);
                }
            }
            String hlc_percent = hlc_money.multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%";
            String btc_percent = btc_money.multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%";
            String ltc_percent = ltc_money.multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%";
            String eth_percent = eth_money.multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%";
            String etc_percent = etc_money.multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%";
           
            String money = String.valueOf(userWallet.get("money"));
            String money_percent = new BigDecimal(money).multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%";
            userWallet.put("hlc_percent", hlc_percent);
            userWallet.put("btc_percent", btc_percent);
            userWallet.put("ltc_percent", ltc_percent);
            userWallet.put("eth_percent", eth_percent);
            userWallet.put("etc_percent", etc_percent);
            userWallet.put("money_percent", money_percent);
            userWallet.put("totalMoney", totalMoney);
            
            data.put("userWallet", userWallet);
            ar.setData(data);
            ar.setSuccess(true);
            ar.setMessage("查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }   
        return ar;
    }
    
    
    /**
     * @describe:查询账户余额
     * @author: zhangchunming
     * @date: 2016年11月1日下午7:20:54
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    @LoginVerify
    @PostMapping("/getWalletByArray")
    @ApiOperation(nickname = "查询账户余额", value = "查询账户余额", notes = "查询账户余额")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "CSESSIONID", value = "会话token", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse getWalletByArray(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String,Object>();
        try {
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            PageData userWallet = userWalletService.getWalletByUserId(String.valueOf(user.get("id")), Constant.VERSION_NO);
            BigDecimal totalMoney = new BigDecimal(String.valueOf(userWallet.get("money")));
            List<PageData> listCoin = digitalCoinService.getAllCoinPrice();
            BigDecimal hlc_money = new BigDecimal("0");
            BigDecimal btc_money = new BigDecimal("0");
            BigDecimal ltc_money = new BigDecimal("0");
            BigDecimal eth_money = new BigDecimal("0");
            BigDecimal etc_money = new BigDecimal("0");
            for(PageData coin:listCoin){
                if("HLC".equals(coin.getString("coin_name"))){//合链币
                    String coinPrice  = coin.getString("coin_rate").split(":")[0];
                    String hlc_amnt =String.valueOf(userWallet.get("hlc_amnt"));
                    hlc_money = new BigDecimal(hlc_amnt).multiply(new BigDecimal(coinPrice));
                    totalMoney = hlc_money.add(totalMoney);
                }else if("BTC".equals(coin.getString("coin_name"))){
                    String coinPrice  = coin.getString("coin_rate").split(":")[0];
                    String btc_amnt =String.valueOf(userWallet.get("btc_amnt"));
                    btc_money = new BigDecimal(btc_amnt).multiply(new BigDecimal(coinPrice));
                    totalMoney = btc_money.add(totalMoney);
                }else if("LTC".equals(coin.getString("coin_name"))){
                    String coinPrice  = coin.getString("coin_rate").split(":")[0];
                    String ltc_amnt =String.valueOf(userWallet.get("ltc_amnt"));
                    ltc_money = new BigDecimal(ltc_amnt).multiply(new BigDecimal(coinPrice));
                    totalMoney = ltc_money.add(totalMoney);
                }else if("ETH".equals(coin.getString("coin_name"))){
                    String coinPrice  = coin.getString("coin_rate").split(":")[0];
                    String eth_amnt =String.valueOf(userWallet.get("eth_amnt"));
                    eth_money = new BigDecimal(eth_amnt).multiply(new BigDecimal(coinPrice));
                    totalMoney = eth_money.add(totalMoney);
                }else if("ETC".equals(coin.getString("coin_name"))){
                    String coinPrice  = coin.getString("coin_rate").split(":")[0];
                    String etc_amnt =String.valueOf(userWallet.get("etc_amnt"));
                    etc_money = new BigDecimal(etc_amnt).multiply(new BigDecimal(coinPrice));
                    totalMoney = etc_money.add(totalMoney);
                }
            }
            List<PageData> coinList = new ArrayList<PageData>();
            //人民币
            PageData coin1 = new PageData();
            String money =String.valueOf(userWallet.get("money"));
            coin1.put("address", "");
            coin1.put("coin_name", "RMB");
            coin1.put("coin_name_brief", "人民币");
            coin1.put("coin_amnt", userWallet.get("money"));
            coin1.put("froze_amnt", userWallet.get("froze_rmb_amnt"));
            coin1.put("coin_price", "");
            coin1.put("percent", new BigDecimal(money).multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%");
            coinList.add(coin1);
            for(PageData coin:listCoin){
                if("HLC".equals(coin.getString("coin_name"))){//合链币
                    String coinPrice  = coin.getString("coin_rate").split(":")[0];
                    String hlc_amnt =String.valueOf(userWallet.get("hlc_amnt"));
                    hlc_money = new BigDecimal(hlc_amnt).multiply(new BigDecimal(coinPrice));
                    coin.put("address", "");
                    coin.put("coin_amnt", userWallet.get("hlc_amnt"));
                    coin.put("froze_amnt", userWallet.get("froze_hlc_amnt"));
                    coin.put("coin_price", coinPrice);
                    coin.put("percent", hlc_money.multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%");
                }else if("BTC".equals(coin.getString("coin_name"))){
                    String coinPrice  = coin.getString("coin_rate").split(":")[0];
                    String btc_amnt =String.valueOf(userWallet.get("btc_amnt"));
                    btc_money = new BigDecimal(btc_amnt).multiply(new BigDecimal(coinPrice));
                    coin.put("address", user.getString("address"));
                    coin.put("coin_amnt", userWallet.get("btc_amnt"));
                    coin.put("froze_amnt", userWallet.get("froze_btc_amnt"));
                    coin.put("coin_price", coinPrice);
                    coin.put("percent", btc_money.multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%");
                }else if("LTC".equals(coin.getString("coin_name"))){
                    String coinPrice  = coin.getString("coin_rate").split(":")[0];
                    String ltc_amnt =String.valueOf(userWallet.get("ltc_amnt"));
                    ltc_money = new BigDecimal(ltc_amnt).multiply(new BigDecimal(coinPrice));
                    coin.put("address", "");
                    coin.put("coin_amnt", userWallet.get("ltc_amnt"));
                    coin.put("froze_amnt", userWallet.get("froze_ltc_amnt"));
                    coin.put("coin_price", coinPrice);
                    coin.put("percent", ltc_money.multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%");
                }else if("ETH".equals(coin.getString("coin_name"))){
                    String coinPrice  = coin.getString("coin_rate").split(":")[0];
                    String eth_amnt =String.valueOf(userWallet.get("eth_amnt"));
                    eth_money = new BigDecimal(eth_amnt).multiply(new BigDecimal(coinPrice));
                    coin.put("address", "");
                    coin.put("coin_amnt", userWallet.get("eth_amnt"));
                    coin.put("froze_amnt", userWallet.get("froze_eth_amnt"));
                    coin.put("coin_price", coinPrice);
                    coin.put("percent", eth_money.multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%");
                }else if("ETC".equals(coin.getString("coin_name"))){
                    String coinPrice  = coin.getString("coin_rate").split(":")[0];
                    String etc_amnt =String.valueOf(userWallet.get("etc_amnt"));
                    etc_money = new BigDecimal(etc_amnt).multiply(new BigDecimal(coinPrice));
                    coin.put("address", "");
                    coin.put("coin_amnt", userWallet.get("etc_amnt"));
                    coin.put("froze_amnt", userWallet.get("froze_etc_amnt"));
                    coin.put("coin_price", coinPrice);
                    coin.put("percent", etc_money.multiply(new BigDecimal("100")).divide(totalMoney, 2, RoundingMode.HALF_UP).toString()+"%");
                }
                coinList.add(coin);
            }
            
            data.put("coinList", coinList);
            data.put("totalMoney", totalMoney);
            ar.setData(data);
            ar.setSuccess(true);
            ar.setMessage("查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }   
        return ar;
    }
    /**
     * @describe:转账
     * @author: zhangchunming
     * @date: 2016年11月1日下午7:25:39
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    @LoginVerify
    @RequestMapping(value="/transferAccount", method=RequestMethod.POST)
    @PostMapping("/login")
    @ApiOperation(nickname = "转币", value = "转币", notes = "转币！")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "CSESSIONID", value = "会话token", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "revbankaccno", value = "对方账号", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "money", value = "转账金额", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "coin_name", value = "币种名称（BTC）", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "remark4", value = "备注", required = false, paramType = "query", dataType = "String")
    })
    public AjaxResponse transferAccount(HttpServletRequest request,HttpServletResponse response){
        logBefore(logger, "---------转币----transferAccount-----------");
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data =  new HashMap<String,Object>();
        try {
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            
            String userType = user.getString("user_type");
            PageData pd = new PageData();
            pd = this.getPageData();
            pd.remove("CSESSIONID");
            pd.put("bussType", "transferAccount");
            pd.put("user_name", user.getString("user_name"));
            pd.put("create_time", DateUtil.getCurrDateTime());
            pd.put("account", user.getString("account"));
            pd.put("revbankaccno", pd.getString("revbankaccno")==null?"":pd.getString("revbankaccno").trim());
            pd.put("money", pd.getString("money")==null?"":pd.getString("money").trim());
            if(StringUtil.isEmpty(pd.getString("revbankaccno"))){
                ar.setSuccess(false);
                ar.setMessage("请输入对方账户");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(StringUtil.isEmpty(pd.getString("money"))){
                ar.setSuccess(false);
                ar.setMessage("请输入转账金额");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(!Validator.isMoney4(pd.getString("money"))){
                ar.setSuccess(false);
                ar.setMessage("转账金额格式有误！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(new BigDecimal(pd.getString("money")).compareTo(new BigDecimal("0"))==0){
                ar.setSuccess(false);
                ar.setMessage("转账金额不得为0！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            if(StringUtil.isEmpty(pd.getString("coin_name"))){
                ar.setSuccess(false);
                ar.setMessage("请输入币种名称！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            if(!"BTC".equals(pd.getString("coin_name"))){
                ar.setSuccess(false);
                ar.setMessage("目前仅支持比特币！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            /*if(!usersDetailsService.findIsExist(pd.getString("revbankaccno"), Constant.VERSION_NO)){//revbankaccno对方账户即对方手机号
                ar.setSuccess(false);
                ar.setMessage("对方账户不存在，请重新输入");
                ar.setErrorCode(CodeConstant.ERROR_NO_ACCOUNT);
                return ar;
            }*/
            if(!userLoginService.findIsExist(pd.getString("revbankaccno"), Constant.VERSION_NO)){
                ar.setSuccess(false);
                ar.setMessage("对方账户不存在，请重新输入");
                ar.setErrorCode(CodeConstant.ERROR_NO_ACCOUNT);
                return ar;
            }
            PageData userLogin = userLoginService.getUserLoginByUserId(String.valueOf(user.get("id")), Constant.VERSION_NO);
            if(pd.getString("revbankaccno").equals(userLogin.getString("account"))){
                ar.setSuccess(false);
                ar.setMessage("不能转入自己账户，请重新输入");
                ar.setErrorCode(CodeConstant.ERROR_DISABLE);
                return ar;
            }
            /*PageData userInfo  = userLoginService.getUserInfoByAccount(pd.getString("revbankaccno"), Constant.VERSION_NO);
            if("3".equals(userInfo.getString("user_type"))){
                ar.setSuccess(false);
                ar.setMessage("对方账户是店铺会员身份，不允许转入三界石！");
                ar.setErrorCode(CodeConstant.DISABLE_TURN_IN);
                return ar;
            }*/
            /*//转账上限、下限查询
            String  uplimit = "";
            String  lowlimit = "";
            String codeName = "";
            if("1".equals(userType)){
                ar.setSuccess(false);
                ar.setMessage("抱歉，您没有转三界石权限，只有创业会员和店铺会员才有哦！");
                ar.setErrorCode(CodeConstant.ERROR_NO_TRANSFER);
                return ar;
            }else if("2".equals(userType)){
                codeName = "ZS_TRANSFER_LIMIT";
            }else if("3".equals(userType)){
                codeName = "DP_TRANSFER_LIMIT";
            }else if("4".equals(userType)){//供应商
                ar.setSuccess(false);
                ar.setMessage("抱歉，您没有转三界石权限，只有创业会员和店铺会员才有哦！");
                ar.setErrorCode(CodeConstant.ERROR_NO_TRANSFER);
                return ar;
            }else if("5".equals(userType)){//代理商
                ar.setSuccess(false);
                ar.setMessage("抱歉，您没有转三界石权限，只有创业会员和店铺会员才有哦！");
                ar.setErrorCode(CodeConstant.ERROR_NO_TRANSFER);
                return ar;
//                codeName = "DL_TRANSFER_LIMIT";
            }
            List<PageData> codeList =sysGenCodeService.findByGroupCode("LIMIT_RATE", Constant.VERSION_NO);
            for(PageData code:codeList){
                if(codeName.equals(code.get("code_name"))){
                    uplimit = code.get("uplimit").toString();
                    lowlimit = code.get("lowlimit").toString();
                }
            }
            if(StringUtil.isNotEmpty(uplimit)&&new BigDecimal(pd.getString("money")).compareTo(new BigDecimal(uplimit))>0){
                ar.setSuccess(false);
                ar.setMessage("转账金额不能超出上限");
                ar.setErrorCode(CodeConstant.ERROR_OVER_UPLIMIT);
                return ar;
            }
            if(StringUtil.isNotEmpty(lowlimit)&&new BigDecimal(pd.getString("money")).compareTo(new BigDecimal(lowlimit))<0){
                ar.setSuccess(false);
                ar.setMessage("转账金额不能低于下限");
                ar.setErrorCode(CodeConstant.ERROR_LOWER_LOWLIMIT);
                return ar;
            }*/
            PageData userWallet = userWalletService.getWalletByUserId(String.valueOf(user.get("id")), Constant.VERSION_NO);
            if("BTC".equals(pd.getString("coin_name"))){//比特币转账
                String btc_amnt = String.valueOf(userWallet.get("btc_amnt"));
                if(new BigDecimal(pd.getString("money")).compareTo(new BigDecimal(btc_amnt))>0){
                    ar.setSuccess(false);
                    ar.setMessage("您的比特币余额不足！");
                    ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                    return ar;
                }
            }
            //开始转账
            pd.put("user_id", user.get("id"));
            pd.put("user_type", userType);
            pd.put("coin_amnt", pd.getString("money"));
            pd.put("operator", user.getString("account"));
            pd.remove("money");
            //生成支付号
            /*Long tMaxno =sysMaxnumService.findMaxNo("payno", Constant.VERSION_NO);
            if(tMaxno==null){
                logger.info("payno  tSysMaxnum findMaxNo  is null!");
                ar.setSuccess(false);
                ar.setMessage("系统繁忙,请稍后重试！");
                ar.setErrorCode(CodeConstant.SYS_ERROR);
                return ar;
            }
            String pay_no =tMaxno.toString();*/
            String flowno =OrderGenerater.generateOrderNo();
            pd.put("flowno", flowno);
            userWalletService.transferAccount(pd, Constant.VERSION_NO);
            data.put("flowno", flowno);
            data.put("create_time", DateUtil.dateToStamp(DateUtil.getCurrDateTime()));
            data.put("revbankaccno", pd.getString("revbankaccno"));
            data.put("money", "-"+pd.getString("coin_amnt"));
            data.put("coin_name", pd.getString("coin_name"));
            data.put("remark1","转账-"+pd.getString("coin_name"));//说明
            data.put("remark4", pd.getString("remark4"));//备注
            ar.setData(data);
            ar.setSuccess(true);
            ar.setMessage("转账成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }finally{
            logAfter(logger);
        }   
        return ar;
    }
    /**
     * @describe:充值创建支付订单
     * @author: zhangchunming
     * @date: 2016年11月2日上午9:34:05
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    @LoginVerify
    @RequestMapping(value="/recharge", method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse recharge(HttpServletRequest request,HttpServletResponse response){
        logBefore(logger, "充值----recharge");
        AjaxResponse ar = new AjaxResponse();
        try {
            String key = RequestUtils.getCookieValueByKey(CookieConstant.CSESSIONID, request, response);
            String userstr = SessionUtil.getAttibuteForUser(key);
            JSONObject user = JSONObject.parseObject(userstr);
            String userType = user.getString("user_type");//用户类型:1-普通会员 2-创业会员 3-店铺 4-供应商 5-代理商
            PageData pd = new PageData();
            pd = this.getPageData();
            
            if(StringUtil.isEmpty(pd.getString("money"))){
                ar.setSuccess(false);
                ar.setMessage("请输入充值金额");
                return ar;
            }
            if(!Validator.isMoney(pd.getString("money"))){
                ar.setSuccess(false);
                ar.setMessage("三界石必须为整数，不能为小数");
                return ar;
            }
            if(StringUtil.isEmpty(pd.getString("pay_type"))){
                ar.setSuccess(false);
                ar.setMessage("请选择支付方式");
                return ar;
            }
            if("4".equals(user.getString("user_type"))){//供应商
                ar.setSuccess(false);
                ar.setMessage("供应商无充值权限");
                return ar;
            }
            //费率查询
            String  feeRate = "";
            String codeName = "";
            if("1".equals(userType)){
                codeName = "PT_RATE";
            }else if("2".equals(userType)){
                codeName = "ZS_RATE";
            }else if("3".equals(userType)){
                codeName = "DP_RATE";
            }else if("5".equals(userType)){
                codeName = "DL_RATE";
            }
            List<PageData> codeList =sysGenCodeService.findByGroupCode("LIMIT_RATE", Constant.VERSION_NO);
            for(PageData code:codeList){
                if(codeName.equals(code.get("code_name"))){
                    feeRate = code.get("code_value").toString();
                }
            }
            ar.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }finally{
            logAfter(logger);
        }   
        return ar;
    }
    
    /**
     * @describe:跳往充值页面
     * @author: zhangchunming
     * @date: 2016年11月3日上午9:38:06
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    /*@LoginVerify
    @RequestMapping(value="/toRecharge", method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse toRecharge(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String, Object>();
        try {
            String key = RequestUtils.getCookieValueByKey(CookieConstant.CSESSIONID, request, response);
            String userstr = SessionUtil.getAttibuteForUser(key);
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            String userType = user.getString("user_type");
            PageData userWallet = userWalletService.getWalletByUserId(String.valueOf(user.get("id")), Constant.VERSION_NO);
            //费率查询
            String  feeRate = "";
            String codeName = "";
            if("1".equals(userType)){
                codeName = "PT_RATE";
            }else if("2".equals(userType)){
                codeName = "ZS_RATE";
            }else if("3".equals(userType)){
                codeName = "DP_RATE";
            }else if("5".equals(userType)){
                codeName = "DL_RATE";
            }
            List<PageData> codeList =sysGenCodeService.findByGroupCode("LIMIT_RATE", Constant.VERSION_NO);

            for(PageData code:codeList){
                if(codeName.equals(code.get("code_name")==null?"":code.getString("code_name").trim())){
                    feeRate = code.get("code_value").toString();
                }
            }
            
            data.put("user_type", userType);//会员身份
            data.put("future_currency", userWallet.get("future_currency"));//三界石余额
            data.put("feeRate", feeRate);
            ar.setData(data);
            ar.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }   
        return ar;
    }*/
    /**
     * @describe:跳往转账页面
     * @author: zhangchunming
     * @date: 2016年11月3日上午10:04:19
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    /*@LoginVerify
    @RequestMapping(value="/toTransferAcc", method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse toTransferAcc(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String, Object>();
        try {
            String key = RequestUtils.getCookieValueByKey(CookieConstant.CSESSIONID, request, response);
            String userstr = SessionUtil.getAttibuteForUser(key);
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            String userType = user.getString("user_type");
            PageData userWallet = userWalletService.getWalletByUserId(String.valueOf(user.get("id")), Constant.VERSION_NO);
            //转账上限、下限查询
            String  uplimit = "";
            String  lowlimit = "";
            String codeName = "";
            if("1".equals(userType)){
                ar.setSuccess(false);
                ar.setMessage("对不起，您没有转账权限");
                ar.setErrorCode(CodeConstant.ERROR_NO_TRANSFER);
                return ar;
            }else if("2".equals(userType)){
                codeName = "ZS_TRANSFER_LIMIT";
            }else if("3".equals(userType)){
                codeName = "DP_TRANSFER_LIMIT";
            }else if("4".equals(userType)){//供应商
                ar.setSuccess(false);
                ar.setMessage("对不起，您没有转账权限");
                ar.setErrorCode(CodeConstant.ERROR_NO_TRANSFER);
                return ar;
            }else if("5".equals(userType)){//代理商
                codeName = "DL_TRANSFER_LIMIT";
            }
            List<PageData> codeList =sysGenCodeService.findByGroupCode("LIMIT_RATE", Constant.VERSION_NO);
            for(PageData code:codeList){
                if(codeName.equals(code.get("code_name"))){
                    uplimit = code.get("uplimit").toString();
                    lowlimit = code.get("lowlimit").toString();
                }
            }
            
            data.put("user_type", userType);//会员身份
            data.put("future_currency", userWallet.getString("future_currency"));//三界石余额
            data.put("uplimit", uplimit);
            data.put("lowlimit", lowlimit);
            ar.setData(data);
            ar.setSuccess(true);
            ar.setMessage("转账成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }   
        return ar;
    }*/
    /**
     * @describe:跳往提现页面
     * @author: zhangchunming
     * @date: 2016年11月3日上午10:43:59
     * @param request
     * @param response
     * @return: AjaxResponse
     *//*
    @LoginVerify
    @RequestMapping(value="/toWithdrawal", method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse toWithdrawal(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String, Object>();
        try {
            String key = RequestUtils.getCookieValueByKey(CookieConstant.CSESSIONID, request, response);
            String userstr = SessionUtil.getAttibuteForUser(key);
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            String userType = user.getString("user_type");
            PageData userWallet = userWalletService.getWalletByUserId(String.valueOf(user.get("id")), Constant.VERSION_NO);
            PageData withdrawalAcc = withdrawalAccService.selectByUserId(String.valueOf(user.get("id")), Constant.VERSION_NO);
            //转账上限、下限查询
            String  uplimit = "";
            String  lowlimit = "";
            String codeName = "";
            if("1".equals(userType)){
                ar.setSuccess(false);
                ar.setMessage("对不起，您没有提现权限");
                ar.setErrorCode(CodeConstant.ERROR_NO_WITHDRAWAL);
                return ar;
            }else if("2".equals(userType)){
                ar.setSuccess(false);
                ar.setMessage("对不起，您没有提现权限");
                ar.setErrorCode(CodeConstant.ERROR_NO_WITHDRAWAL);
                return ar;
            }else if("3".equals(userType)){
                codeName = "DP_WITHDRAWAL_LIMIT";
            }else if("4".equals(userType)){//供应商
                codeName = "DL_WITHDRAWAL_LIMIT";
            }else if("5".equals(userType)){//代理商
                codeName = "GY_WITHDRAWAL_LIMIT";
            }
            List<PageData> codeList =sysGenCodeService.findByGroupCode("LIMIT_RATE", Constant.VERSION_NO);
            for(PageData code:codeList){
                if(codeName.equals(code.get("code_name"))){
                    uplimit = code.get("uplimit").toString();
                    lowlimit = code.get("lowlimit").toString();
                }
            }
            
            data.put("user_type", userType);//会员身份
            data.put("money", userWallet.get("money"));//人民币余额
            data.put("uplimit", uplimit);
            data.put("lowlimit", lowlimit);
            data.put("withdrawalAcc", withdrawalAcc);
            if(withdrawalAcc!=null){
                List<Map> withdrawalAcclist = new ArrayList<Map>();
                if(!StringUtil.isEmpty(withdrawalAcc.getString("alipay_account"))){
                    Map<String,Object> map = new HashMap<String, Object>();
                    map.put("pay_type", "1");
                    map.put("acc_name", "支付宝（"+withdrawalAcc.getString("alipay_account")+"）");
                    withdrawalAcclist.add(map);
                }
                if(!StringUtil.isEmpty(withdrawalAcc.getString("wechat_account"))){
                    Map<String,Object> map = new HashMap<String, Object>();
                    map.put("pay_type", "2");
                    map.put("acc_name", "微信（"+withdrawalAcc.getString("wechat_account")+"）");
                    withdrawalAcclist.add(map);
                }
                if(!StringUtil.isEmpty(withdrawalAcc.getString("bank_account"))){
                    Map<String,Object> map = new HashMap<String, Object>();
                    map.put("pay_type", "3");
                    map.put("acc_name", withdrawalAcc.getString("bank_name")+"（尾号"+withdrawalAcc.getString("bank_account").substring(withdrawalAcc.getString("bank_account").length()-4)+"）");
                    withdrawalAcclist.add(map);
                }
                data.put("withdrawalAcclist", withdrawalAcclist);
            }
            ar.setData(data);
            ar.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }   
        return ar;
    }*/
    /**
     * @describe:查询交易类型列表
     * @author: zhangchunming
     * @date: 2016年11月10日下午7:44:17
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    /*@LoginVerify
    @RequestMapping(value="/getAccTypeList", method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse getAccTypeList(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String, Object>();
        try {
            String key = RequestUtils.getCookieValueByKey(CookieConstant.CSESSIONID, request, response);
            String userstr = SessionUtil.getAttibuteForUser(key);
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            PageData pd = new PageData();
            pd = this.getPageData();
            pd.put("user_type", user.getString("user_type"));
            //交易类型列表
            List<PageData> accTypeList = accDetailService.getAccTypeList(pd, Constant.VERSION_NO);
            data.put("accTypeList", accTypeList);
            
            //交易时间列表
            List<Map> dateList = new ArrayList<Map>();
            Map<String,Object> tsDate1 = new HashMap<String,Object>();
            tsDate1.put("search_date", "day");
            tsDate1.put("date_name", "今天");
            dateList.add(tsDate1);
            Map<String,Object> tsDate2 = new HashMap<String,Object>();
            tsDate2.put("search_date", "week");
            tsDate2.put("date_name", "一周内");
            dateList.add(tsDate2);
            Map<String,Object> tsDate3 = new HashMap<String,Object>();
            tsDate3.put("search_date", "month");
            tsDate3.put("date_name", "一个月内");
            dateList.add(tsDate3);
            data.put("dateList", dateList);
            
            //币种类型列表
            List<Map> currencyList = new ArrayList<Map>();
            if("2".equals(user.getString("user_type"))){
                Map<String,Object> currency1 = new HashMap<String,Object>();
                currency1.put("currency", "1");
                currency1.put("currency_name", "三界石");
                currencyList.add(currency1);
                Map<String,Object> currency2 = new HashMap<String,Object>();
                currency2.put("currency", "2");
                currency2.put("currency_name", "三界宝");
                currencyList.add(currency2);
                Map<String,Object> currency3 = new HashMap<String,Object>();
                currency3.put("currency", "3");
                currency3.put("currency_name", "人民币");
                currencyList.add(currency3);
            }else{
                Map<String,Object> currency1 = new HashMap<String,Object>();
                currency1.put("currency", "1");
                currency1.put("currency_name", "三界石");
                currencyList.add(currency1);
                Map<String,Object> currency3 = new HashMap<String,Object>();
                currency3.put("currency", "3");
                currency3.put("currency_name", "人民币");
                currencyList.add(currency3);
            }
            data.put("currencyList", currencyList);
            ar.setData(data);
            ar.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }   
        return ar;
    }*/
    
    /**
     * @describe:获取银行卡列表
     * @author: zhangchunming
     * @date: 2016年11月25日下午2:07:32
     * @param request
     * @param response
     * @return: AjaxResponse
     *//*
    @RequestMapping(value="/getBankList", method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse getBankList(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String, Object>();
        try {
            
            if(cacheManager.isExist("bankList")){
                logger.info("------------从redis中取银行列表-------------");
                data.put("bankList", cacheManager.get("bankList"));
            }else{
                List<PageData> codeList =  sysGenCodeService.findByGroupCode("PAY_TYPE", Constant.VERSION_NO);
                List<String> bankList  = new ArrayList<String>();
                for(PageData code:codeList){
                    bankList.add(code.getString("description"));
                }
                data.put("bankList", bankList);
                cacheManager.set("bankList", bankList, 60*60);//设置一个小时
            }
            ar.setData(data);
            ar.setSuccess(true);
            ar.setMessage("查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }   
        return ar;
    }*/
    
    /**
     * @describe:转账时查询对方账户信息并做隐藏处理
     * @author: zhangchunming
     * @date: 2016年12月2日下午8:10:26
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    /*@LoginVerify
    @RequestMapping(value="/getAccount", method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse getAccount(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String, Object>();
        try {
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            pd.put("revbankaccno", pd.getString("revbankaccno")==null?"":pd.getString("revbankaccno").trim());
            if(StringUtil.isEmpty(pd.getString("revbankaccno"))){
                ar.setSuccess(false);
                ar.setMessage("请输入对方账户");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            pd.put("account", pd.getString("revbankaccno"));
            PageData userLogin = userLoginService.getUserByAccount(pd, Constant.VERSION_NO);
            if(userLogin==null){
                ar.setMessage("对方账户不存在！");
                ar.setErrorCode(CodeConstant.ERROR_NO_ACCOUNT);
                ar.setSuccess(false);
                return ar;
            }
            PageData usersDetail = usersDetailsService.selectById((Integer)userLogin.get("user_id"), Constant.VERSION_NO);
            if(Validator.isMobile(usersDetail.getString("user_name"))){
                data.put("user_name", FormatNum.convertPhone((usersDetail.getString("user_name"))));
            }else{
                data.put("user_name", FormatNum.convertRealName(usersDetail.getString("user_name")));
            }
            ar.setData(data);
            ar.setSuccess(true);
            ar.setMessage("查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }   
        return ar;
    }*/
    
    /**
     * @describe:查询转三界宝记录
     * @author: zhangchunming
     * @date: 2016年12月9日下午3:47:19
     * @param request
     * @param page
     * @return: AjaxResponse
     *//*
    @LoginVerify
    @RequestMapping(value="/listPageAccOutdetail", method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse listPageAccOutdetail(HttpServletRequest request,Page page){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String, Object>();
        try {
            PageData pd  = new PageData();
            pd = this.getPageData();
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            pd.put("user_id", String.valueOf(user.get("id")));
            page.setPd(pd);
            PageData listPagePD = accOutdetailService.listPageAccOutdetail(page, Constant.VERSION_NO);
            if(listPagePD!=null){
                data.put("list", listPagePD.get("list"));
                ar.setData(data);
                page = (Page)listPagePD.get("page");
                page.setPd(null);
                ar.setPage(page);
            }
            ar.setSuccess(true);
            ar.setMessage("查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }   
        return ar;
    }*/
    
    /**
     * @describe:查询兑换记录
     * @author: zhangchunming
     * @date: 2016年12月19日上午11:48:35
     * @param request
     * @param page
     * @return: AjaxResponse
     */
    /*@LoginVerify
    @RequestMapping(value="/listPageExchangeRecode", method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse listPageExchangeRecode(HttpServletRequest request,Page page){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String, Object>();
        try {
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            PageData pd = new PageData();
            pd = this.getPageData();
            pd.put("user_id", user.get("id"));
            page.setPd(pd);
            //查询账户列表
            PageData exchageRecodePD = curExchageRecodeService.listPageExchangeRecode(page);
            List<PageData> listExchageRecode = null;
            if(exchageRecodePD!=null){
                listExchageRecode = (List<PageData>)exchageRecodePD.get("list");
                page = (Page)exchageRecodePD.get("page");
                page.setPd(null);
                data.put("listExchageRecode", listExchageRecode);
            }else{
                data.put("listExchageRecode", null);
            }
            ar.setData(data);
            ar.setPage(page);
            ar.setSuccess(true);
            ar.setMessage("查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }   
        return ar;
    }*/
    
    /**
     * @describe:跳往兑换页面
     * @author: zhangchunming
     * @date: 2016年12月20日上午10:31:20
     * @param request
     * @return: AjaxResponse
     */
   /* @LoginVerify
    @RequestMapping(value="/toExchange", method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse toExchange(HttpServletRequest request){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String, Object>();
        try {
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            PageData userWallet = userWalletService.getWalletByUserId(String.valueOf(user.get("id")), Constant.VERSION_NO);
            data.put("future_currency", String.valueOf(userWallet.get("future_currency")));
            data.put("money", String.valueOf(userWallet.get("money")));
            String exchange_rate = "EXCHANGE_RATE";
            List<PageData> codeList =sysGenCodeService.findByGroupCode("LIMIT_RATE", Constant.VERSION_NO);
            for(PageData code:codeList){
                if("EXCHANGE_RATE".equals(code.getString("code_name"))){
                    exchange_rate = code.getString("code_value");
                }
            }
            data.put("exchange_rate", exchange_rate);
            ar.setData(data);
            ar.setSuccess(true);
            ar.setMessage("查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }   
        return ar;
    }*/
    
    /**
     * @describe:虚拟币-人民币兑换（币种兑换）
     * @author: zhangchunming
     * @date: 2017年5月22日下午3:14:16
     * @param request
     * @return: AjaxResponse
     */
    @LoginVerify
    @ResponseBody
    @RequestMapping(value="/currencyExchange", method=RequestMethod.POST)
    @ApiOperation(nickname = "currencyExchange", value = "获取用户兑换信息", notes = "获取用户兑换信息！！")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "CSESSIONID", value = "CSESSIONID", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "coin_name", value = "币种名称 列如：BTC", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "buy_in_out", value = "兑换类型 列如： 1：RMB->BTC  2:BTC->RMB", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "exchange_num", value = "购买个数", required = true, paramType = "query", dataType = "String"),
            //@ApiImplicitParam(name = "rmb_amnt", value = "当前用户人民币数额", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "errorCode", value = "-118 人民币数额不正确- 50 账户余额不足 -15 参数有误 -34 系统异常", required = false, paramType = "query", dataType = "String"),
    })
    public AjaxResponse currencyExchange(HttpServletRequest request){
        logBefore(logger, "***************currencyExchange-币种兑换***************");
        AjaxResponse ar = new AjaxResponse();
        try {
            PageData pd = new PageData();
            pd = this.getPageData();
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            Map<String,Object> data =  new HashMap<String,Object>();
            pd.remove("CSESSIONID");//add by zhangchunming
            pd.put("bussType", "currencyExchange");
            pd.put("user_name", user.getString("user_name"));
            pd.put("create_time", DateUtil.getCurrDateTime());
            logger.info("************币种兑换 pd value is"+pd.toString());
            
            if(StringUtil.isEmpty(pd.getString("coin_name"))){
                ar.setMessage("请输入币种名称！");
                ar.setSuccess(false);
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(StringUtil.isEmpty(pd.getString("exchange_num"))){
                ar.setMessage("请输入可兑换数额！");
                ar.setSuccess(false);
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(!Validator.isMoney4(pd.getString("exchange_num"))){
                ar.setMessage("兑换数额格式有误！");
                ar.setSuccess(false);
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(new BigDecimal(pd.getString("exchange_num")).compareTo(new BigDecimal("0"))==0){
                ar.setSuccess(false);
                ar.setMessage("兑换数额不得为0！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            PageData userWallet = userWalletService.getWalletByUserId(String.valueOf(user.get("id")), Constant.VERSION_NO);
            Map<String,Object> map= digitalCoinService.getCoinPrice(pd.getString("coin_name"));
            String coinPrice  = map.get("coin_rate").toString().split(":")[0];
            String exchange_num =String.valueOf(pd.getString("exchange_num"));
            if("1".equals(pd.getString("buy_in_out"))){ //RMB->BTC
                pd.put("rmb_amnt",new BigDecimal(coinPrice).multiply(new BigDecimal(pd.get("exchange_num").toString())).setScale(2,BigDecimal.ROUND_UP));
                if(new BigDecimal(exchange_num).multiply(new BigDecimal(coinPrice)).setScale(2,BigDecimal.ROUND_UP).compareTo(new BigDecimal(String.valueOf(userWallet.get("money").toString()))) > 0){
                    ar.setMessage("账户余额不足，兑换失败！");
                    ar.setSuccess(false);
                    ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                    return ar;
                }
            }else if("2".equals(pd.getString("buy_in_out"))){ //HLB->RMB
                pd.put("rmb_amnt",new BigDecimal(coinPrice).multiply(new BigDecimal(pd.get("exchange_num").toString())).setScale(2,BigDecimal.ROUND_DOWN));
                if("BTC".equals(pd.getString("exchangeCoin"))){
                    if(new BigDecimal(pd.getString("exchange_num")).compareTo(new BigDecimal(String.valueOf(userWallet.get("btc_amnt"))))>0){
                        ar.setMessage("账户余额不足，请重新输入！");
                        ar.setSuccess(false);
                        ar.setErrorCode(CodeConstant.BALANCE_NOT_ENOUGH);
                        return ar;
                    }
                }
                
            }
            pd.put("flowno", OrderGenerater.generateOrderNo());
            data.put("flowno", pd.getString("flowno"));
            pd.put("otherno", pd.getString("flowno"));
            pd.put("user_id", String.valueOf(user.get("id")));
            pd.put("acc_no","95");
            pd.put("caldate",new Date());
            pd.put("coin_rate",coinPrice);
//            pd.put("coinPrice", new BigDecimal(coinPrice).multiply(new BigDecimal(pd.get("exchange_num").toString())));
            pd.put("buy_in_out",pd.getString("buy_in_out"));
            pd.put("coin_amnt",pd.getString("exchange_num"));
            
            pd.put("status","6");
            pd.put("cntflag","1");
            pd.put("remark1","币种兑换");
            boolean currencyExchange = accDetailService.currencyExchange(pd, Constant.VERSION_NO);
            if(currencyExchange){
                data.put("create_time", DateUtil.dateToStamp(DateUtil.getCurrDateTime()));
                if ("2".equals(pd.getString("buy_in_out"))) {//BTC->RMB
                    data.put("money", "+"+new BigDecimal(coinPrice).multiply(new BigDecimal(pd.get("exchange_num").toString())));
                }else{
                    data.put("money", "+"+pd.getString("coin_amnt"));
                }
                data.put("coin_name", pd.getString("coin_name"));
                data.put("remark1","兑换-"+pd.getString("coin_name"));//说明
//                data.put("remark4", pd.getString("remark4"));//备注
//                data.put("hash",currencyExchange.getString("hash"));
                ar.setData(data);
                ar.setSuccess(true);
                ar.setMessage("兑换成功！");
                return ar;
            }else{
                ar.setSuccess(false);
                ar.setMessage("兑换失败！");
                ar.setErrorCode(CodeConstant.UPDATE_FAIL);
                return ar;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }finally{
            logAfter(logger);
        }   
        return ar;
    }

    /**
     * @describe:查询转三界石记录
     * @author: zhangchunming
     * @date: 2016年12月20日下午5:27:27
     * @param request
     * @param page
     * @return: AjaxResponse
     */
    /*@LoginVerify
    @RequestMapping(value="/listPageTransferSJS", method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse listPageTransferSJS(HttpServletRequest request,Page page){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String, Object>();
        try {
            PageData pd  = new PageData();
            pd = this.getPageData();
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            pd.put("user_id", String.valueOf(user.get("id")));
            page.setPd(pd);
            PageData listPagePD = accDetailService.listPageTransferSJS(page, Constant.VERSION_NO);
            if(listPagePD!=null){
                data.put("list", listPagePD.get("list"));
                ar.setData(data);
                page = (Page)listPagePD.get("page");
                page.setPd(null);
                ar.setPage(page);
            }
            ar.setSuccess(true);
            ar.setMessage("查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }   
        return ar;
    }
    */
    /**
     * @describe:查询账户详情
     * @author: zhangchunming
     * @date: 2017年5月25日下午4:06:48
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    @LoginVerify
    @PostMapping("/getAccDetail")
    @ApiOperation(nickname = "查询账户详情", value = "查询账户详情", notes = "查询账户详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "CSESSIONID", value = "会话token", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "id", value = "账单id", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse getAccDetail(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String,Object>();
        try {
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            PageData pd = this.getPageData();
            if(StringUtil.isEmpty(pd.getString("id"))){
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setSuccess(false);
                ar.setMessage("请输入账单id");
                return ar;
            }
            pd.put("user_id", user.get("id"));
            PageData accDetail = accDetailService.getAccDetail(pd);
            data.put("accDetail", accDetail);
            ar.setData(data);
            ar.setSuccess(true);
            ar.setMessage("查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }   
        return ar;
    }
    
    /**
     * @describe:获取银行卡列表
     * @author: zhangchunming
     * @date: 2016年11月25日下午2:07:32
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    @LoginVerify
    @PostMapping("/getBankList")
    @ApiOperation(nickname = "查询银行卡列表", value = "查询银行卡列表", notes = "查询银行卡列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "CSESSIONID", value = "会话token", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse getBankList(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String, Object>();
        try {
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            List<PageData> bankList = userBankService.getBankList(user.getInteger("id"));
            data.put("bankList", bankList);
            ar.setData(data);
            ar.setSuccess(true);
            ar.setMessage("查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }   
        return ar;
    }
    
    @LoginVerify
    @PostMapping("/addBank")
    @ApiOperation(nickname = "添加银行卡", value = "添加银行卡", notes = "添加银行卡，错误码：-15参数错误（按返回信息提示）、-34网络繁忙！、-49绑定失败！、-119此卡号已绑定，请勿重复绑定！")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "CSESSIONID", value = "会话token", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "bank_name", value = "开户行", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "bank_no", value = "银行卡号", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse addBank(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String, Object>();
        PageData pd  = new PageData();
        pd = this.getPageData();
        try {
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            pd.put("user_id", user.getInteger("id"));
            if(StringUtil.isEmpty(pd.getString("bank_name"))){
                ar.setMessage("请选择开户行！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setSuccess(false);
                return ar;
            }
            if(StringUtil.isEmpty(pd.getString("bank_no"))){ 
                ar.setMessage("请输入银行卡号！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setSuccess(false);
                return ar;
            }
            boolean exist = userBankService.isExist(pd);
            if(exist){
                ar.setMessage("此卡号已绑定，请勿重复绑定！");
                ar.setErrorCode(CodeConstant.ERROR_BANK_BINDED);
                ar.setSuccess(false);
                return ar;
            }
            boolean insert = userBankService.insert(pd);
            if(insert){
                ar.setData(data);
                ar.setSuccess(true);
                ar.setMessage("绑定成功！");
            }else{
                ar.setData(data);
                ar.setErrorCode(CodeConstant.UPDATE_FAIL);
                ar.setSuccess(false);
                ar.setMessage("绑定失败！");
            }
            return ar;
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }   
        return ar;
    }
    
    
    /**
     * @describe:设置默认银行卡
     * @author: lisandro
     * @date: 2017年7月18日22:34:01
     * @param request
     * @return: AjaxResponse
     */
    @LoginVerify
    @PostMapping("/setDefaultCard")
    @ApiOperation(nickname = "设置默认银行卡", value = "设置默认银行卡", notes = "设置默认银行卡")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "is_default", value = "1为默认此卡", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "CSESSIONID", value = "会话token", required = true, paramType = "query", dataType = "String")

    })
    public AjaxResponse setDefaultCard(HttpServletRequest request)throws Exception{
        AjaxResponse ar = new AjaxResponse();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            pd.put("user_id", String.valueOf(user.get("id")));
            if(StringUtil.isEmpty(pd.getString("id"))){
                ar.setMessage("请输入ID！");
                ar.setSuccess(false);
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            boolean setDefault = userBankService.setDefault(pd);
            if(setDefault){
                ar.setMessage("设置成功！");
                ar.setSuccess(true);
                return ar;
            }else{
                ar.setErrorCode(CodeConstant.UPDATE_FAIL);
                ar.setMessage("设置失败！");
                ar.setSuccess(true);
                return ar;
            }
        }catch (Exception e){
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }
        return ar;
    }
    
    
    
    @PostMapping("/rechargeBTC")
    @ApiOperation(nickname = "充值比特币", value = "充值比特币", notes = "充值比特币")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "address", value = "充值地址", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse rechargeBTC(HttpServletRequest request,HttpServletResponse response){
        logBefore(logger, "----------充值BTC----recharge");
        AjaxResponse ar = new AjaxResponse();
        PageData pd =  new PageData();
        pd = this.getPageData();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            HttpEntity<String> formEntity = new HttpEntity<String>(pd.getString("address"), headers);
            String result = null;
            for (int i = 0; i < 10; i++) {
                result = restTemplate.postForObject("http://faucet.xeno-genesis.com/", formEntity, String.class);
            }
            ar.setMessage(result);
            ar.setSuccess(true);
            return ar;
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }finally{
            logAfter(logger);
        }   
        return ar;
    }
    
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>("mmdtv1ZWS1GWcRdoieMVNYcKS9u3azvoki", headers);
        String result = null;
        for (int i = 0; i < 5; i++) {
            result = restTemplate.postForObject("http://faucet.xeno-genesis.com/", formEntity, String.class);
        }
        System.out.println("result="+result);
    }
}
