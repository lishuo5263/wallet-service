package com.ecochain.ledger.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecochain.ledger.annotation.LoginVerify;
import com.ecochain.ledger.base.BaseWebService;
import com.ecochain.ledger.constants.CodeConstant;
import com.ecochain.ledger.constants.Constant;
import com.ecochain.ledger.constants.CookieConstant;
import com.ecochain.ledger.model.Page;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.model.UserCardChargeInfo;
import com.ecochain.ledger.model.UserCrad;
import com.ecochain.ledger.service.SendVodeService;
import com.ecochain.ledger.service.ShopOrderInfoService;
import com.ecochain.ledger.service.SysGenCodeService;
import com.ecochain.ledger.service.UserCardChargeInfoService;
import com.ecochain.ledger.service.UserCardService;
import com.ecochain.ledger.service.UserLoginService;
import com.ecochain.ledger.service.UserService;
import com.ecochain.ledger.service.UsersDetailsService;
import com.ecochain.ledger.util.AjaxResponse;
import com.ecochain.ledger.util.Base64;
import com.ecochain.ledger.util.InternetUtil;
import com.ecochain.ledger.util.MD5Util;
import com.ecochain.ledger.util.RequestUtils;
import com.ecochain.ledger.util.SessionUtil;
import com.ecochain.ledger.util.StringUtil;
import com.ecochain.ledger.util.Validator;
import com.ecochain.ledger.util.sms.SMSUtil;
import com.sun.jna.Library;
import com.sun.jna.Native;

/*
 * 总入口
 */
@RestController
@RequestMapping(value = "/api/user")
@Api(value = "用户Service")
public class UsersWebService extends BaseWebService {

    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private UserService userService;
    @Autowired
    private SendVodeService sendVodeService;
    @Autowired
    private ShopOrderInfoService shopOrderInfoService;
    @Autowired
    private SysGenCodeService sysGenCodeService;
    
    private UserCardService userCardService;
    @Autowired
    private UserCardChargeInfoService userCardChargeInfoService;

    @Resource(name="userDetailsService")
    private UsersDetailsService userDetailsService;
    
    /* ==================================================登录过滤========================================================== */
    
    /* ==================================================登录过滤========================================================== */
    
    
    /**
     * 获取登录用户的IPinfomodel
     * @throws Exception 
     */
/*  public void getRemortIP(String USERNAME) throws Exception {
        PageData pd = new PageData();
        HttpServletRequest request = this.getRequest();
        String ip = "";
        if (request.getHeader("x-forwarded-for") == null) {
            ip = request.getRemoteAddr();
        }else{
            ip = request.getHeader("x-forwarded-for");
        }
        pd.put("USERNAME", USERNAME);
        pd.put("IP", ip);
        userDetailsService.saveIP(pd);
    }  */


    /**
     * @describe:充值信息展示
     * @author: lisandro
     * @date: 2017年7月18日16:26:29
     * @param request
     * @return: AjaxResponse
     */
    @LoginVerify
    @ResponseBody
    @PostMapping("/showChargeInfo")
    @ApiOperation(nickname = "充值信息展示", value = "充值信息展示", notes = "充值信息展示！")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "CSESSIONID", value = "会话token", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse showChargeInfo(HttpServletRequest request, Page page)throws Exception{
        AjaxResponse ar = new AjaxResponse();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            int remark=Random.class.newInstance().nextInt(98)+1;
            String strNew = "";
            String str = "abcdefghijklmnopqrstuvwxyz";
            char[] b = str.toCharArray();
            for(int i=0;i<4;i++){
                int index =(int) (Math.random()*b.length);
                strNew += b[index];
            }
            Map<String,Object> chargeInfo =new HashMap();
            chargeInfo.put("acceptNo","6222020200112232123");
            chargeInfo.put("acceptName","北京和链共赢科技有限公司");
            chargeInfo.put("companyBankName","工商银行望京支行");
            chargeInfo.put("remark",strNew.toUpperCase());
            chargeInfo.put("remarkPrice",remark <10 ?new String("0"+remark):remark);
            UserCardChargeInfo userCardChargeInfo =new UserCardChargeInfo();
            userCardChargeInfo.setStatus("0");
            userCardChargeInfo.setCreateTime(new Date());
            userCardChargeInfo.setUserId((Integer) user.get("id"));
            userCardChargeInfo.setRemark(chargeInfo.get("remark").toString());
            userCardChargeInfo.setAccepteNo(chargeInfo.get("acceptNo").toString());
            userCardChargeInfo.setAcceptName(chargeInfo.get("acceptName").toString());
            userCardChargeInfo.setCompanyBankName(chargeInfo.get("companyBankName").toString());
            userCardChargeInfo.setRemarkPrice(Integer.valueOf(chargeInfo.get("remarkPrice").toString()));
            userCardChargeInfoService.insert(userCardChargeInfo);
            return fastReturn(chargeInfo,true,"充值信息展示！",CodeConstant.SC_OK);
        }catch (Exception e){
            e.printStackTrace();
            ar = fastReturn("系统异常,充值信息展示失败！", false, "系统异常,充值信息展示失败！", CodeConstant.SYS_ERROR);
        }
        return ar;
    }

    /**
     * @describe:添加银行卡
     * @author: lisandro
     * @date: 2017年7月18日16:26:29
     * @param request
     * @return: AjaxResponse
     */
    @LoginVerify
    @ResponseBody
    @GetMapping("/addBankCard")
    @ApiOperation(nickname = "添加银行卡", value = "添加银行卡", notes = "失败返回示列：" +
            "{\n" +
            "  \"errorCode\": -22,\n" +
            "  \"message\": \"您已有此银行卡，请勿重复添加！\",\n" +
            "  \"page\": null,\n" +
            "  \"data\": \"您已有此银行卡，请勿重复添加!\",\n" +
            "  \"success\": false\n" +
            "}！")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cardNo", value = "卡号", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "bankName", value = "银行名称", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "isDefault", value = "1为默认此卡", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "CSESSIONID", value = "会话token", required = true, paramType = "query", dataType = "String")

    })
    public AjaxResponse addBankCard(HttpServletRequest request, Page page)throws Exception{
        AjaxResponse ar = new AjaxResponse();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            pd.put("userId", String.valueOf(user.get("id")));
            if(StringUtil.isNotEmpty(pd.getString("bankName"))&&StringUtil.isNotEmpty(pd.getString("cardNo"))&&StringUtil.isNotEmpty(pd.getString("cardNo"))){
                if(userCardService.findCardByCardNo(pd) > 0){
                    return fastReturn("您已有此银行卡，请勿重复添加!",false,"您已有此银行卡，请勿重复添加！",CodeConstant.BANK_EXISTS);
                }
                UserCrad userCrad=new UserCrad();
                userCrad.setStatus(1);
                userCrad.setCreateTime(new Date());
                userCrad.setBankName(pd.getString("bankName"));
                userCrad.setCardNo(Integer.valueOf(pd.getString("cardNo")));
                userCrad.setIsDefault(pd.getString("isDefault") != null ? pd.getString("isDefault"):"0");
                if(userCardService.addBankCard(userCrad) > 0){
                    return fastReturn(true,true,"添加银行卡成功！",CodeConstant.SC_OK);
                }else{
                    ar = fastReturn("系统异常,添加银行卡失败！", false, "系统异常,添加银行卡失败！", CodeConstant.SYS_ERROR);
                }
            }else{
                return fastReturn("缺少参数，添加银行卡失败!",false,"缺少参数，添加银行卡失败！",CodeConstant.PARAM_ERROR);
            }
        }catch (Exception e){
            e.printStackTrace();
            ar = fastReturn("系统异常,添加银行卡失败！", false, "系统异常,添加银行卡失败！", CodeConstant.SYS_ERROR);
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
    @ResponseBody
    @GetMapping("/setDefaultCard")
    @ApiOperation(nickname = "设置默认银行卡", value = "添加银行卡", notes = "设置默认银行卡")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cardNo", value = "卡号", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "bankName", value = "银行名称", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "isDefault", value = "1为默认此卡", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "CSESSIONID", value = "会话token", required = true, paramType = "query", dataType = "String")

    })
    public AjaxResponse setDefaultCard(HttpServletRequest request, Page page)throws Exception{
        AjaxResponse ar = new AjaxResponse();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            pd.put("userId", String.valueOf(user.get("id")));
            if(StringUtil.isNotEmpty(pd.getString("bankName"))&&StringUtil.isNotEmpty(pd.getString("cardNo"))&&StringUtil.isNotEmpty(pd.getString("cardNo"))){
                if(userCardService.findCardByCardNo(pd) > 0){
                    return fastReturn("您已有此银行卡，请勿重复添加!",false,"您已有此银行卡，请勿重复添加！",CodeConstant.BANK_EXISTS);
                }
                UserCrad userCrad=new UserCrad();
                userCrad.setStatus(1);
                userCrad.setCreateTime(new Date());
                userCrad.setBankName(pd.getString("bankName"));
                userCrad.setCardNo(Integer.valueOf(pd.getString("cardNo")));
                userCrad.setIsDefault(pd.getString("isDefault") != null ? pd.getString("isDefault"):"0");
                if(userCardService.addBankCard(userCrad) > 0){
                    return fastReturn(true,true,"添加银行卡成功！",CodeConstant.SC_OK);
                }else{
                    ar = fastReturn("系统异常,添加银行卡失败！", false, "系统异常,添加银行卡失败！", CodeConstant.SYS_ERROR);
                }
            }else{
                return fastReturn("缺少参数，添加银行卡失败!",false,"缺少参数，添加银行卡失败！",CodeConstant.PARAM_ERROR);
            }
        }catch (Exception e){
            e.printStackTrace();
            ar = fastReturn("系统异常,添加银行卡失败！", false, "系统异常,添加银行卡失败！", CodeConstant.SYS_ERROR);
        }
        return ar;
    }

    /*@RequestMapping(value="/listPageUser",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse listPageUser(HttpServletRequest request)throws Exception{
        AjaxResponse ar = new AjaxResponse();
        PageData pd = this.getPageData();
        Map<String,Object> data = new HashMap<String,Object>();
        List<PageData> listPageUser = userDetailsService.listPageUsers(pd);
        data.put("pageInfo", new PageInfo<PageData>(listPageUser));
        data.put("pd", pd);
        data.put("page", pd.getPage());
        data.put("rows", pd.getRows());
        ar.setSuccess(true);
        ar.setData(data);
        return ar;
    }*/
	
	
	/**
	 * 请求登录，验证用户
	 */
	@PostMapping("/login")
	@ApiOperation(nickname = "登录接口", value = "用户登录", notes = "用户登录！")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "account", value = "登录账号", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "source", value = "来源：APP或其他，APP的必填", required = false, paramType = "query", dataType = "String")
	})
	public AjaxResponse login(HttpServletRequest request,HttpServletResponse response){
	    AjaxResponse ar = new AjaxResponse();
		Map<String,Object> data  = new HashMap<String,Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
            String account = StringUtil.isEmpty(pd.getString("account"))?null:pd.getString("account").trim();
            String password = StringUtil.isEmpty(pd.getString("password"))?null:pd.getString("password").trim();
            if(StringUtil.isEmpty(account)){
                ar.setSuccess(false);
                ar.setMessage("请输入登录账号！");
                ar.setErrorCode(CodeConstant.USER_NO_EXISTS);
                return ar;
            }
            if(StringUtil.isEmpty(password)){
                ar.setSuccess(false);
                ar.setMessage("请输入密码！");
                ar.setErrorCode(CodeConstant.ERROE_PASSWORD_NULL);
                return ar;
            }
            pd.put("account", account);
            password = MD5Util.getMd5Code(password);
            pd.put("password", password);
            pd = userDetailsService.getUserByAccAndPass(pd,Constant.VERSION_NO);
            if(pd != null){
                if("1".equals(pd.getString("status"))){
                    pd.put("lastlogin_ip", InternetUtil.getRemoteAddr(request));
                    pd.put("lastlogin_port", InternetUtil.getRemotePort(request));
                    userDetailsService.updateLoginTimeById(pd,Constant.VERSION_NO);
                    
                    PageData userInfo = userDetailsService.getUserInfoByUserId((Integer)pd.get("user_id"),Constant.VERSION_NO);
                    String sessionId = RequestUtils.getRequestValue(CookieConstant.CSESSIONID,request);
                    SessionUtil.setAttributeForUser(sessionId, JSON.toJSONString(userInfo));
                    data.put("CSESSIONID", Base64.getBase64(sessionId));
                    data.put("user_name", userInfo.getString("user_name"));
                    ar.setData(data);
                    ar.setSuccess(true);
                    ar.setMessage("登录成功！");
                    return ar;
                }else{
                    ar.setSuccess(false);
                    ar.setErrorCode(CodeConstant.ERROR_BLACK);
                    ar.setMessage("您已被加入黑名单，如有疑问请联系官方客服！");
                    return ar;
                }
            }else{
                ar.setSuccess(false);
                ar.setErrorCode(CodeConstant.USERNAMEORPWD_ERROR);
                ar.setMessage("账号或密码有误！");
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
    
	 /**
     * @describe:注册
     * @author: zhangchunming
     * @date: 2017年3月2日下午3:36:32
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    @PostMapping("/register")
    @ApiOperation(nickname = "用户注册", value = "用户注册", notes = "用户注册")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "user_name", value = "会员名称（中英文字符均可、4-8个字符以内）", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "password", value = "创建密码（字母数字组合，6-16个字符以内）", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "confirmPassword", value = "确认密码", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "account", value = "输入手机号（11位数字）", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "vcode", value = "手机短信验证码", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse register(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data  = new HashMap<String,Object>();
        try {
            PageData pd = new PageData();
            pd = this.getPageData();
            logger.info("--------------register  pd value is "+pd.toString());
            String account = StringUtil.isEmpty(pd.getString("account"))?null:pd.getString("account").trim();
            String password = StringUtil.isEmpty(pd.getString("password"))?null:pd.getString("password").trim();
            String user_name = StringUtil.isEmpty(pd.getString("user_name"))?null:pd.getString("user_name").trim();
            String vcode = StringUtil.isEmpty(pd.getString("vcode"))?null:pd.getString("vcode").trim();
            String confirmPassword = StringUtil.isEmpty(pd.getString("confirmPassword"))?null:pd.getString("confirmPassword").trim();
            if(StringUtil.isEmpty(user_name)){
                ar.setSuccess(false);
                ar.setMessage("请输入会员名称！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            if(user_name.length()<4||user_name.length()>8){
                ar.setSuccess(false);
                ar.setMessage("请输入4-8个字符！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            if(StringUtil.isEmpty(password)){
                ar.setSuccess(false);
                ar.setMessage("请输入密码！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            if(password.length()<6||password.length()>16){
                ar.setSuccess(false);
                ar.setMessage("密码为6-16字母或数字组合，请重新设置");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            /*if(!Validator.isAccountByLetterAndNum(password)){
                ar.setSuccess(false);
                ar.setMessage("密码为6-16字母数字组合，请重新设置");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }*/
            
            if(StringUtil.isEmpty(confirmPassword)){
                ar.setSuccess(false);
                ar.setMessage("确认密码不能为空！");
                ar.setErrorCode(CodeConstant.ERROE_CONFIRM_PASSWORD_NULL);
                return ar;
            }
            if(!pd.getString("password").equals(confirmPassword)){
                ar.setSuccess(false);
                ar.setMessage("两次密码输入不一致！");
                ar.setErrorCode(CodeConstant.ERROE_PASSWORD_DIFFERENT);
                return ar;
            }
            
            if(StringUtil.isEmpty(account)){
                ar.setSuccess(false);
                ar.setMessage("请输入手机号！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(!Validator.isMobile(account)){
                ar.setSuccess(false);
                ar.setMessage("手机号格式不正确！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            if(StringUtil.isEmpty(vcode)){
                ar.setSuccess(false);
                ar.setMessage("请输入验证码！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            //半小时之内的短信验证码有效
            String tVcode =sendVodeService.findVcodeByPhone(pd.getString("account"),Constant.CUR_SYS_CODE); 
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
            
            
            password = MD5Util.getMd5Code(password);
            //判断用户是否已存在
            if(userDetailsService.findIsExist(account,Constant.VERSION_NO)){
                ar.setSuccess(false);
                ar.setMessage("该手机号已注册！");
                ar.setErrorCode(CodeConstant.ACCOUNT_EXISTS);
                return ar;
            }
            pd.put("account", account);
            pd.put("user_type", 1);
            pd.put("mobile_phone", account);//买家
            pd.put("status", "1");//会员状态默认启用
            pd.put("password", password);
            pd.put("lastlogin_ip", InternetUtil.getRemoteAddr(request));
            pd.put("lastlogin_port", InternetUtil.getRemotePort(request));
            
            if(!userDetailsService.addUser(pd,Constant.VERSION_NO)){            
                ar.setSuccess(false);
                ar.setMessage("注册失败！");
                ar.setErrorCode(CodeConstant.REGISTER_FAIL);
                return ar;
            }
            
            PageData userInfo = userDetailsService.getUserInfoByAccount(account,Constant.VERSION_NO);
            String sessionId = RequestUtils.getRequestValue(CookieConstant.CSESSIONID,request);
            SessionUtil.setAttributeForUser(sessionId, JSON.toJSONString(userInfo));
            data.put("CSESSIONID", Base64.getBase64(sessionId));
            ar.setData(data);
            ar.setSuccess(true);
            ar.setMessage("注册成功！");
            return ar;
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
            ar.setErrorCode(CodeConstant.SYS_ERROR);
        }
        return ar;
    }
    
    /**
     * 访问系统首页
     */
    @LoginVerify
    @PostMapping("/index")
    @ApiOperation(nickname = "获取用户信息", value = "获取用户信息", notes = "获取用户信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "CSESSIONID", value = "会话token", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse index(HttpServletRequest request){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String,Object>();
        try {
            String sessionId = RequestUtils.getRequestValue(CookieConstant.CSESSIONID,request);
            String userstr = SessionUtil.getAttibuteForUser(sessionId);
            JSONObject user = JSONObject.parseObject(userstr);
            PageData userInfo = userDetailsService.getUserInfoByUserId(user.getInteger("user_id"), Constant.VERSION_NO);
            data.put("userInfo", userInfo);
            ar.setData(data);
            ar.setSuccess(true);
            ar.setMessage("查询成功！");
            return ar;
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
            ar.setErrorCode(CodeConstant.SYS_ERROR);
        }
        return ar;
    }
    
    /**
     * 进入首页后的默认页面
     * @return
     */
    @RequestMapping(value="/login_default")
    public String defaultPage(){
        return "system/admin/default";
    }
    
    /**
     * 用户注销
     * @param request
     * @return
     */
    @PostMapping(value="/logout")
    @ApiOperation(nickname = "退出登录", value = "退出登录", notes = "退出登录！")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "CSESSIONID", value = "登录token", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse logout(HttpServletRequest request)throws Exception{
        AjaxResponse ar = new AjaxResponse();
        SessionUtil.delAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
        ar.setSuccess(true);
        ar.setMessage("退出成功！");
        return ar; 
    }
    
    /**
     * @describe:忘记密码
     * @author: zhangchunming
     * @date: 2016年10月26日下午1:40:57
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    @PostMapping("/forgetpwd")
    @ApiOperation(nickname = "忘记密码", value = "忘记密码，输入新密码", notes = "忘记密码，输入新密码！")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "account", value = "登录账号", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "password", value = "新密码", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "cfPassWord", value = "确认密码", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "vcode", value = "验证码", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse forgetpwd(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        try {
            String account  =request.getParameter("account");
//            String phone  =request.getParameter("phone");
            String passWord  =request.getParameter("password");
            String cfPassWord  =request.getParameter("cfPassWord");
            String vcode  =request.getParameter("vcode");
            if(StringUtil.isEmpty(account)){
                ar.setSuccess(false);
                ar.setMessage("登录账号不能为空，请重新输入");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            /*if(!Validator.isMobile(phone)){
                ar.setSuccess(false);
                ar.setMessage("手机号为11位数字，请重新输入");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }*/
            if(StringUtil.isEmpty(vcode.trim())){
                ar.setSuccess(false);
                ar.setMessage("验证码不能为空！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(StringUtil.isEmpty(passWord)||StringUtil.isEmpty(cfPassWord)){
                ar.setSuccess(false);
                ar.setMessage("密码不能为空!");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            } 
            if(passWord.length()<6||passWord.length()>16){
                ar.setSuccess(false);
                ar.setMessage("密码应为6-16位数，请重新设置");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            /*if(!Validator.isPasswordByLetterAndNum(passWord)){
                ar.setSuccess(false);
                ar.setMessage("密码应包含字母和数字哦，请重新设置");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }*/

            if(!passWord.equals(cfPassWord)){
                ar.setSuccess(false);
                ar.setMessage("两次密码输入不一致，请重新输入！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            if(!userLoginService.findIsExist(account, Constant.VERSION_NO)){
                ar.setSuccess(false);
                ar.setMessage("账号不存在，请重新输入！");
                ar.setErrorCode(CodeConstant.USER_NO_EXISTS);
                return ar;
            }
            PageData userInfo = userService.getPhoneByAccount(account, Constant.VERSION_NO);
            if(!"1".equals(userInfo.getString("user_type"))&&!"2".equals(userInfo.getString("user_type"))){//目前只支持普通会员和创业会员使用忘记密码功能
                ar.setSuccess(false);
                ar.setMessage("目前只有普通会员和创业会员才可以使用忘记密码功能哦！");
                ar.setErrorCode(CodeConstant.ERROR_DISABLE);
                return ar;
            }
            if(StringUtil.isEmpty(userInfo.getString("mobile_phone"))){
                ar.setSuccess(false);
                ar.setMessage("您的账号未绑定手机号，忘记密码功能无法使用，请重新注册！");
                ar.setErrorCode(CodeConstant.USERMOBILE_NOEXISTS);
                return ar;
            }
            //半小时之内的短信验证码有效
            String tVcode =sendVodeService.findVcodeByPhone(userInfo.getString("mobile_phone"), Constant.VERSION_NO); 
            if(tVcode ==null||!vcode.trim().equals(tVcode.trim())){
                ar.setSuccess(false);
                ar.setMessage("验证码输入不正确！");
                ar.setErrorCode(CodeConstant.ERROR_VCODE);
                return ar;
            }
            if(!userLoginService.modifyPwd(account, MD5Util.getMd5Code(passWord),Constant.VERSION_NO)){         
                ar.setSuccess(false);
                ar.setMessage("设置密码失败！");
                ar.setErrorCode(CodeConstant.UPDATE_FAIL);
                return ar;
            }
                ar.setSuccess(true);
                ar.setMessage("设置密码成功！");
                return ar;
            
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
            ar.setErrorCode(CodeConstant.SYS_ERROR);
        }
        return ar;
    }
    
    
    /**
     * @describe:跳往会员中兴
     * @author: zhangchunming
     * @date: 2017年4月26日下午3:03:06
     * @param request
     * @return: AjaxResponse
     */
    @LoginVerify
    @PostMapping("/toMemberCenter")
    @ApiOperation(nickname = "个人中心", value = "个人中心，查询用户信息", notes = "个人中心，查询用户信息！")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "CSESSIONID", value = "会话token", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse toMemberCenter(HttpServletRequest request){
        logBefore(logger,"去往用户中兴");
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String,Object>();
        try {
            PageData pd = new PageData();
            pd  = this.getPageData();
            //从session中查询用户信息
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            Integer user_id = user.getInteger("id");
            
            //获取用户信息
            pd.put("user_id", user_id);
            PageData userInfo = userDetailsService.getUserInfoByUserId(user_id, Constant.VERSION_NO);
            data.put("userInfo", userInfo);
                
            //商城订单列表
            
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
                /*for(PageData supplier:supplierList){
                    supplierIdList.add((Integer)supplier.get("id"));
                }
                pd.put("supplierList", supplierList);*/
                pd.put("supplier_id", String.valueOf(oneSupplier.get("id")));
            } else {
                pd.put("user_id", String.valueOf(user.get("id")));
                pd.put("user_type", String.valueOf(user.getString("user_type")));
            }
            
            PageData orderPD = new PageData();
            List<PageData> shopOrderList = null;
            if("6".equals(user.getString("user_type"))||"7".equals(user.getString("user_type"))){//6-国内物流 7-境外物流
                //分页查询商城订单列表
                orderPD.put("user_type", user.getString("user_type"));
            }else if("4".equals(user.getString("user_type"))){//卖家
                //分页查询商城订单列表
                orderPD.put("supplier_id", String.valueOf(user.get("id")));
            }else if("1".equals(user.getString("user_type"))){//买家
                orderPD.put("user_id", String.valueOf(user.get("id")));
            }
            shopOrderList =  shopOrderInfoService.listShopOrderByPage(orderPD);//查询所有订单
            if (shopOrderList != null && shopOrderList.size()>0) {
                List<String> orderIdList = new ArrayList<String>();
                for (PageData shopOrder : shopOrderList) {
                    orderIdList.add(String.valueOf(shopOrder.get("order_id")));
                }
                PageData shopOrder = new PageData();
                if ("4".equals(user.getString("user_type"))) {//供应商
//                    shopOrder.put("supplierIdList", supplierIdList);
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
            data.put("shopOrderList", shopOrderList);
            ar.setData(data);
            ar.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
            ar.setErrorCode(CodeConstant.SYS_ERROR);
        }
        logAfter(logger);
        return ar;
    }
    
    @LoginVerify
    @PostMapping("/getUserInfo")
    @ApiOperation(nickname = "获取用户基本信息", value = "获取用户基本信息", notes = "获取用户基本信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "CSESSIONID", value = "会话token", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse getUserInfo(HttpServletRequest request){
        logBefore(logger,"获取用户信息");
        AjaxResponse ar = new AjaxResponse();
        try {
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            PageData userInfo = userDetailsService.getUserInfoByUserId(user.getInteger("id"), Constant.VERSION_NO);
            ar.setSuccess(true);
            ar.setData(userInfo);
            ar.setMessage("获取用户信息成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
            ar.setErrorCode(CodeConstant.SYS_ERROR);
        }
        logAfter(logger);
        return ar;
    }
    
    /**
     * @describe:设置交易密码
     * @author: zhangchunming
     * @date: 2017年5月31日下午7:53:07
     * @param request
     * @return: AjaxResponse
     */
    @LoginVerify
    @PostMapping("/setTransPassword")
    @ApiOperation(nickname = "设置交易密码", value = "设置交易密码", notes = "设置交易密码")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "CSESSIONID", value = "会话token", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "trans_password", value = "交易密码", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "comfirm_trans_password", value = "确认交易密码", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse setTransPassword(HttpServletRequest request){
        logBefore(logger,"----------设置交易密码-------------");
        AjaxResponse ar = new AjaxResponse();
        PageData pd = this.getPageData();
        try {
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            pd.put("user_id", String.valueOf(user.get("id")));
            if(StringUtil.isEmpty(pd.getString("trans_password"))){
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setMessage("请输入交易密码！");
                ar.setSuccess(false);
                return ar;
            }
            
            if(StringUtil.isEmpty(pd.getString("comfirm_trans_password"))){
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setMessage("请输入确认密码！");
                ar.setSuccess(false);
                return ar;
            }
            
            if(!pd.getString("trans_password").equals(pd.getString("comfirm_trans_password"))){
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setMessage("两次密码设置不一致！");
                ar.setSuccess(false);
                return ar;
            }
            pd.put("trans_password", MD5Util.getMd5Code(pd.getString("trans_password")));
            boolean setTransPassword = userDetailsService.setTransPassword(pd);
            if(setTransPassword){
                ar.setSuccess(true);
                ar.setMessage("交易密码设置成功！");
                return ar;
            }else{
                ar.setSuccess(false);
                ar.setErrorCode(CodeConstant.UPDATE_FAIL);
                ar.setMessage("交易密码设置失败！");
                return ar;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
            ar.setErrorCode(CodeConstant.SYS_ERROR);
        }
        logAfter(logger);
        return ar;
    }
    
    /**
     * @describe:发送验证码
     * @author: zhangchunming
     * @date: 2017年7月18日下午8:37:44
     * @param request
     * @param response
     * @return: AjaxResponse
     */
    @PostMapping("/sendVcode")
    @ApiOperation(nickname = "发送短信验证码", value = "发送短信验证码", notes = "发送短信验证码")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse sendVcode(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        try {
            PageData pd  = new PageData();
            pd = this.getPageData();
            String phone = StringUtil.isEmpty(pd.getString("phone"))?null:pd.getString("phone").trim();
            if(StringUtil.isEmpty(pd.getString("phone"))){
                ar.setSuccess(false);
                ar.setMessage("请输入手机号！");
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                return ar;
            }
            
            List<PageData> codeList =sysGenCodeService.findByGroupCode("SENDSMS_FLAG", Constant.VERSION_NO);
            String smsflag ="";
            String sms_day_num = "";
            String sms_half_hour_num = "";
            for(PageData mapObj:codeList){
                if("SENDSMS_FLAG".equals(mapObj.get("code_name"))){
                    smsflag = mapObj.get("code_value").toString();
                    logger.info("---------发送验证码--------短信发送标识smsflag："+smsflag);
                }else if("SMS_DAY_NUM".equals(mapObj.get("code_name"))){
                    sms_day_num = mapObj.get("code_value").toString();
                    logger.info("---------发送验证码--------短信每天限量sms_day_num："+sms_day_num);
                }else if("SMS_HALF_HOUR_NUM".equals(mapObj.get("code_name"))){
                    sms_half_hour_num = mapObj.get("code_value").toString();
                    logger.info("---------发送验证码--------短信半小时限量sms_half_hour_num："+sms_half_hour_num);
                }
            }
            if(StringUtil.isEmpty(smsflag)||StringUtil.isEmpty(sms_day_num)||StringUtil.isEmpty(sms_half_hour_num)){
                ar.setSuccess(false);
                ar.setMessage("系统短信参数配置有误");
                ar.setErrorCode(CodeConstant.SYS_ERROR);
                return ar;
            }
            pd.put("sms_day_num", sms_day_num);
            //判断短信发送量是否超出限制
            Integer findCountBy30Minute = sendVodeService.findCountBy30Minute(phone, Constant.VERSION_NO);
            if(findCountBy30Minute>=Integer.valueOf(sms_half_hour_num)){
                ar.setSuccess(false);
                ar.setMessage("验证码发送太频繁，请半小时后操作！");
                ar.setErrorCode(CodeConstant.ERROE_SMS_FREQUENTLY);
                return ar;
            }
            Integer findCountByDay = sendVodeService.findCountByDay(phone, Constant.VERSION_NO);
            if(findCountByDay>=Integer.valueOf(sms_day_num)){
                ar.setSuccess(false);
                ar.setMessage("验证码发送量超出当天限制，请明天再进行操作！");
                ar.setErrorCode(CodeConstant.ERROE_SMS_OVER);
                return ar;
            }
            String vCode = "";//验证码
            String content = "";//发送内容
            if("0".equals(smsflag)){
                vCode = "888888";
            }else{
                String mobile_code = (int)((Math.random()*9+1)*100000)+"";
                content = new String("您正在注册合链钱包账号，验证码："+mobile_code+"，打死也不能告诉别人哦！");
                if(StringUtil.isNotEmpty(content)){
                    pd.put("phone", phone);
                    pd.put("content", content);
                    pd.put("productid", SMSUtil.vcode_productid);
//                    Boolean sendSms = logSmsService.sendSms(pd, Constant.VERSION_NO);
                    List<PageData> smsList =sysGenCodeService.findByGroupCode("SMS", Constant.VERSION_NO);
                    String username = null;
                    String password = null;
                    for(PageData mapObj:smsList){
                        if("USERNAME".equals(mapObj.get("code_name"))){
                            username = mapObj.get("code_value").toString();
                        }else if("PASSWORD".equals(mapObj.get("code_name"))){
                            password = mapObj.get("code_value").toString();
                        }
                    }
                    SMSUtil.username = username;
                    SMSUtil.password = password;
                    Boolean sendSms = SMSUtil.sendSMS_ChinaNet1(phone, content, SMSUtil.vcode_productid);//发送成功，验证码插入数据库
                    if(sendSms!=null&&sendSms){
                        vCode = mobile_code;
                    }else if((sendSms!=null&&!sendSms)||sendSms==null){
                        ar.setSuccess(false);
                        ar.setMessage("验证码发送失败");
                        ar.setErrorCode(CodeConstant.SMS_GET_ERROR);
                        return ar;
                    }else{
                        ar.setSuccess(false);
                        ar.setMessage("当日获取手机验证码数量已达上限！");
                        ar.setErrorCode(CodeConstant.SMS_GET_ERROR);
                        return ar;
                    }
                }else{
                    ar.setSuccess(false);
                    ar.setMessage("短信内容不能为空！");
                    ar.setErrorCode(CodeConstant.PARAM_ERROR);
                    return ar;
                }
                
            }
            pd.put("vcode", vCode);
            pd.put("phone", phone);
            if(!sendVodeService.addVcode(pd, Constant.VERSION_NO)){//验证码进库
                logger.info("addVcode fail ,phone is "+phone +"!");             
            }
            ar.setSuccess(true);
            ar.setMessage("验证码发送成功！");
            return ar;
            
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("网络繁忙，请稍候重试！");
            ar.setErrorCode(CodeConstant.SYS_ERROR);
        }
        return ar;
    }
    public static void main(String[] args) throws  Exception{
      //初始  
        /*BigInteger num = new BigInteger("0");  
        num = num.setBit(2);  
        System.out.println(num);  
        num = num.setBit(1);  
        System.out.println(num);  
        System.out.println(num.testBit(2));  
        System.out.println(num.testBit(1));  
        System.out.println(num.testBit(3));  
        System.out.println("".length());
        String a = "KzopQgjVwAG2cqEHLWLJ9DKU5LpiUjcbREKoX3LbVgzPVC4S3Hynl".substring(0, 32);
        System.out.println(a.length());
        System.out.println(a.toString());
        
        System.out.println("KzopQgjVwAG2cqEHLWLJ9DKU5LpiUjcbREKoX3LbVgzPVC4S3Hynl");
        System.out.println(Base64.getBase64("KzopQgjVwAG2cqEHLWLJ9DKU5LpiUjcbREKoX3LbVgzPVC4S3Hynl"));
        System.out.println(Base64.getFromBase64("S3pvcFFnalZ3QUcyY3FFSExXTEo5REtVNUxwaVVqY2JSRUtvWDNMYlZnelBWQzRTM0h5bmw="));*/
        String str = "abcdefghijklmnopqrstuvwxyz";
        String strNew = "";
        char[] b = str.toCharArray();
        for(int i=0;i<4;i++){
            int index =(int) (Math.random()*b.length);
            strNew += b[index];
        }
        System.out.println(strNew);
        String base64 = Base64.getBase64("9fd50096398d4b428d57da0f4bffbb67");
        System.out.println("base64="+base64);
        
        char[] a = {'1','2'};
        System.out.println(a.toString());
        
    }
    
    public interface Clibrary extends Library { 

//        Dll INSTANCE = (Dll) Native.loadLibrary("btcsign", Dll.class);//加载动态库文件
        
            public Clibrary INSTANCE = (Clibrary) Native.loadLibrary("/data/lib/libbtcsign.so", Clibrary.class);
            //获得公钥和私钥,第一个参数密钥种子为可选项，如果有值，则长度为32
//            public int GetPriPubKey( String seeds, String prikey, String pubkey, String errmsg);
            public int GetPriPubKey( byte[] seeds, byte[] prikey, byte[] pubkey, byte[] errmsg);

            //获得数据的sign,输入私钥和数据，返回签名
//            public int GetDataSign( String prikey, String data, String sign, String errmsg);
            public int GetDataSign(byte[] prikey, byte[] data, byte[] sign, byte[] errmsg);
            public void InitCrypt();
            public void StopCrypt();
   }
   /* public interface Clibrary extends Library { 
        
        public Clibrary INSTANCE = (Clibrary) Native.loadLibrary("/data/lib/libtest_lib.so", Clibrary.class);
        int TestLib(int a,int b,char op);
    }*/
    /*public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary)
            Native.loadLibrary((Platform.isWindows() ? "msvcrt" : "c"),CLibrary.class);
        void printf(String format, Object... args);
    }

    public static void main(String[] args) {
        CLibrary.INSTANCE.printf("============================================Hello, World\n");
        for (int i=0;i < args.length;i++) {
            CLibrary.INSTANCE.printf("Argument %d: %s\n", i, args[i]);
        }
    }*/

}
