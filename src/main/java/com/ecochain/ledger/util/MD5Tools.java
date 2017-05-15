package com.ecochain.ledger.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**  
 *
 *
 * @description: 与md5加密相关的操作
 * @author: AlvinLi  
 * @date: 2014年10月15日 下午5:46:14
 * @status: 完成
 * @Version: V1.0  
 *
 */
public class MD5Tools {

	 /**
	 * @description: MD5加密，Base64编码
	 * @status: 完成
	 *
	 * @param msg 被加密字符串
	 * @param salt 盐
	 * @return
	 */
	public static String encrypt(String msg,String salt){
		try {
			String rawValue = msg + Strings.nullToEmpty(salt).trim();
			byte[] bytesOfMsg = rawValue.getBytes(StandardCharsets.UTF_8);
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] theDigest = md.digest(bytesOfMsg);
			return Base64.encodeBase64String(theDigest);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

    /**
     * @description: 判断md5的签名是否正确
     * @status: 完成
     *
     * @param paramsMap
     * @param salt 签名盐
     * @param signStr 请求中的签名字符串
     * @return
     */
//    public static boolean isSignRight( Map<String, String> paramsMap,String salt,String signStr){
//        if("".equals(salt) || "".equals(signStr))
//            return false;
//        String rawStr = HttpTools.createLinkString(HttpTools.paraFilter(paramsMap));
//        String sysSign = encrypt(rawStr,signStr);
//        return sysSign.equals(signStr);
//    }
	
	
}
