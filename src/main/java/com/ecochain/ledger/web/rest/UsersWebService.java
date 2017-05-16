package com.ecochain.ledger.web.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecochain.ledger.base.BaseWebService;
import com.ecochain.ledger.constants.CodeConstant;
import com.ecochain.ledger.constants.Constant;
import com.ecochain.ledger.constants.CookieConstant;
import com.ecochain.ledger.model.Page;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.service.SendVodeService;
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
import com.github.pagehelper.PageInfo;
import com.sun.jna.Library;
import com.sun.jna.Native;

/*
 * 总入口
 */
@Controller
@RequestMapping(value = "/api/user")
public class UsersWebService extends BaseWebService {

    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private UserService userService;
    @Autowired
    private SendVodeService sendVodeService;
    
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
     * @describe:查询接收转账资产账户信息
     * @author: lishuo
     * @date: 2017年3月10日09:49:02
     * @param request
     * @return: AjaxResponse
     */
    @RequestMapping(value="/findAcceptInfo",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse findAcceptInfo(HttpServletRequest request, Page page)throws Exception{
        AjaxResponse ar = new AjaxResponse();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            if(StringUtil.isNotEmpty(pd.getString("real_account"))){
                PageData pageData =this.userDetailsService.findAcceptInfo(pd);
                if(pageData==null ||pageData.get("user_id")==null){
                    return fastReturn("接口参数异常,账户"+pd.getString("real_account")+"缺少rela_user_id,资产转入转出失败！", false, "接口参数异常,账户"+pd.getString("real_account")+"缺少rela_user_id,资产转入转出失败！", CodeConstant.NEED_ACCEPT_INTO);
                }else if(pageData.get("public_key")==null){
                    return  fastReturn("接口参数异常,账户"+pd.getString("real_account")+"缺少relauser_public_key,资产转入转出失败！", false, "接口参数异常,账户"+pd.getString("real_account")+"缺少relauser_public_key,资产转入转出失败！", CodeConstant.NEED_ACCEPT_INTO);
                }else{
                    return fastReturn(pageData,true,"查询接收转账资产账户信息成功！", CodeConstant.SC_OK);
                }
            }else{
                ar = fastReturn("接口参数异常,缺少property_id,查询接收转账资产账户信息成功失败！", false, "接口参数异常,缺少property_id,查询接收转账资产账户信息成功失败！", CodeConstant.PARAM_ERROR);
            }
        }catch (Exception e){
            e.printStackTrace();
            ar = fastReturn("系统异常,资产转入转出失败！", false, "系统异常,资产转入转出失败！", CodeConstant.SYS_ERROR);
        }
        return ar;
    }
    
    @RequestMapping(value="/listPageUser",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse listPageUser(HttpServletRequest request)throws Exception{
        AjaxResponse ar = new AjaxResponse();
        PageData pd = this.getPageData();
        Map<String,Object> data = new HashMap<String,Object>();
        List<PageData> listPageUser = userDetailsService.listPageUsers(pd);
        data.put("pageInfo", new PageInfo<PageData>(listPageUser));
        /*data.put("pd", pd);
        data.put("page", pd.getPage());
        data.put("rows", pd.getRows());*/
        ar.setSuccess(true);
        ar.setData(data);
        return ar;
    }
	
	
	/**
	 * 请求登录，验证用户
	 */
	@RequestMapping(value="/login", method=RequestMethod.POST)
	@ResponseBody
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
                ar.setMessage("请输入登陆账号！");
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
                    ar.setData(data);
                    ar.setSuccess(true);
                    ar.setMessage("登陆成功！");
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
    @RequestMapping(value="/register", method=RequestMethod.POST)
    @ResponseBody
    public AjaxResponse register(HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data  = new HashMap<String,Object>();
        try {
            PageData pd = new PageData();
            pd = this.getPageData();
            logger.info("--------------register  pd value is "+pd.toString());
            String account = StringUtil.isEmpty(pd.getString("account"))?null:pd.getString("account").trim();
            String password = StringUtil.isEmpty(pd.getString("password"))?null:pd.getString("password").trim();
            String mail = StringUtil.isEmpty(pd.getString("mail"))?null:pd.getString("mail").trim();
            if(StringUtil.isEmpty(account)){
                ar.setSuccess(false);
                ar.setMessage("请输入用户名！");
                ar.setErrorCode(CodeConstant.USER_NO_EXISTS);
                return ar;
            }
            if(!Validator.isAccountByLetterAndNum(account)){
                ar.setSuccess(false);
                ar.setMessage("用户名应包含字母和数字哦，请重新设置！");
                ar.setErrorCode(CodeConstant.ERROE_PASSWORD_LETTER_NUM);
                return ar;
            }
            if(StringUtil.isEmpty(mail)){
                ar.setSuccess(false);
                ar.setMessage("请输入邮箱！");
                ar.setErrorCode(CodeConstant.MAIL_NULL);
                return ar;
            }
            if(StringUtil.isEmpty(password)){
                ar.setSuccess(false);
                ar.setMessage("请输入密码！");
                ar.setErrorCode(CodeConstant.ERROE_PASSWORD_NULL);
                return ar;
            }
            password = MD5Util.getMd5Code(password);
            /*if(password.length()<6||password.length()>16){
                ar.setSuccess(false);
                ar.setMessage("密码应为6-16位数，请重新设置");
                ar.setErrorCode(CodeConstant.ERROE_PASSWORD_6_16);
                return ar;
            }*/
            
            
            //判断用户是否已存在
            if(userDetailsService.findIsExist(account,Constant.VERSION_NO)){
                ar.setSuccess(false);
                ar.setMessage("该账号已注册！");
                ar.setErrorCode(CodeConstant.ACCOUNT_EXISTS);
                return ar;
            }
            pd.put("role_id", "2");
            pd.put("account", account);
            pd.put("status", "1");//会员状态默认启用
            pd.put("password", password);
            pd.put("lastlogin_ip", InternetUtil.getRemoteAddr(request));
            pd.put("lastlogin_port", InternetUtil.getRemotePort(request));
            
            /*String jsonStr = HttpUtil.sendPostData("http://192.168.10.47:8332/get_new_key", "");
            String jsonStr = HttpUtil.sendPostData(""+ PocConstants.BlockChainURLTest+"/get_new_key", "");
            JSONObject jsonObj = JSONObject.parseObject(jsonStr);
            pd.put("public_key", jsonObj.getJSONObject("result").getString("PubKey"));
            pd.put("address", jsonObj.getJSONObject("result").getString("PubKey"));*/
            
            if(!userDetailsService.addUser(pd,Constant.VERSION_NO)){            
                ar.setSuccess(false);
                ar.setMessage("注册失败！");
                ar.setErrorCode(CodeConstant.REGISTER_FAIL);
                return ar;
            }
            
            /*StringBuffer buf = new StringBuffer();
            while(buf.length()<32){
                buf.append(pd.get("user_id")+pd.getString("account"));
            }
            char[] seeds = buf.substring(0, 32).toCharArray();
            char[] pubkey = new char[64];
            char[] prikey = new char[64];
            char[] errmsg = new char[64];
            String seedsStr = buf.substring(0, 32)+"\0";
            byte[] seedsByte = seedsStr.getBytes();
            byte[] pubkeyByte = new byte[64];
            byte[] prikeyByte = new byte[64];
            byte[] errmsgByte = new byte[64];
            
            String pubkey = "";
            String prikey = "";
            String errmsg = "";
//            System.setProperty("jna.encoding", "UTF-8");
//            int getPriPubKey = 11;
            logger.info("=================掉动态库开始========================");
            System.out.println("运行结果："+Clibrary.INSTANCE.TestLib(1,2,'+'));
            logger.info("=================动态库测试========================");
            Clibrary.INSTANCE.InitCrypt();
            int getPriPubKey = Clibrary.INSTANCE.GetPriPubKey(seedsByte,pubkeyByte,prikeyByte,errmsgByte);
            pubkey = new String(pubkeyByte);
            prikey = new String(prikeyByte);
            errmsg = new String(errmsgByte);
            Clibrary.INSTANCE.StopCrypt();
            if(getPriPubKey==0){
                System.out.println("pubkey="+pubkey+",prikey="+prikey+",errmsg="+errmsg);
                pd.put("seeds", seedsStr);
                pd.put("public_key", pubkey.toString());
                pd.put("address", pubkey.toString());
                pd.put("id", pd.get("user_id"));
                logger.info("调动态库pd value="+pd.toString());
                userDetailsService.updateByIdSelective(pd, Constant.VERSION_NO);
            }
            logger.info("=================掉动态库结束=============返回值getPriPubKey="+getPriPubKey);*/
            PageData userInfo = userDetailsService.getUserInfoByAccount(account,Constant.VERSION_NO);
            String sessionId = RequestUtils.getRequestValue(CookieConstant.CSESSIONID,request);
            SessionUtil.setAttributeForUser(sessionId, JSON.toJSONString(userInfo));
            data.put("CSESSIONID", Base64.getBase64(sessionId));
            data.put("role_id", pd.getString("role_id"));
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
    @RequestMapping(value="/index", method=RequestMethod.POST)
    @ResponseBody
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
    @RequestMapping(value="/logout")
    @ResponseBody
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
    @RequestMapping(value="/forgetpwd", method=RequestMethod.POST)
    @ResponseBody
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
                ar.setMessage("登陆账号不能为空，请重新输入");
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
    public static void main(String[] args) {
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
