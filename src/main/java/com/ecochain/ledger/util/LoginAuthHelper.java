package com.ecochain.ledger.util;

import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.binary.Base64;

/**
 * 
 * @author coooding
 * @date 2015年4月18日 下午5:05:26
 */
public class LoginAuthHelper {

    /**
     * @description: 依据用户名，密码，超时时间生成一个hash字符串
     * @status: 完成
     * @param accname 用户名
     * @param pwd 密码
     * @param expiryOutBySeconds 超时时间(用毫秒秒表示)
     */
    public static String genHashUserInfoStr(String accname,String pwd,long expiryOutBySeconds){
        String enPwd = MD5Tools.encrypt(pwd,null);
        StringBuilder sb = new StringBuilder();
        sb.append(accname).append(enPwd).append(String.valueOf(expiryOutBySeconds)).append("key");
        return MD5Tools.encrypt(sb.toString(),null);
    }

    /**
     * @description: 生成自动登录cookie的值的内容
     * @param accname 用户名
     * @param pwd 密码
     * @param timeOutStr 到期时间字符串
     * @param expiryOutBySeconds 超时时间(用毫秒秒表示)
     */
    public static String genAuthCookieValue(String accname,String pwd,long timeOutStr,long expiryOutBySeconds){
        StringBuilder sb = new StringBuilder();
        String hashUserInfo = genHashUserInfoStr(accname,pwd,expiryOutBySeconds);
        sb.append(accname).append(":").append(timeOutStr).append(":").append(hashUserInfo);
        System.out.println(sb.toString());
        return Base64.encodeBase64String(sb.toString().getBytes(StandardCharsets.UTF_8));
    }
    
    public static void main(String[] args){
    	
    	String accname = "test";
    	String pwd = "abc123";
    	long loginTime = System.currentTimeMillis();
    	long expiryTime = 1000;
    	long timeOut = loginTime + expiryTime;
    	String cookieValue = genAuthCookieValue(accname, pwd, timeOut, expiryTime);
    	String string = new String(Base64.decodeBase64(cookieValue));
    	System.out.println(cookieValue);
    	System.out.println(string);
    }
}
