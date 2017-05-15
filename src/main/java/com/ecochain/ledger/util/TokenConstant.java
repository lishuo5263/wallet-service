package com.ecochain.ledger.util;

/**
 * 
* @ClassName: TokenConstant 
* @Description: token常量
* @author zhangb
* @date 2016-5-27 下午3:45:21
 */
public interface TokenConstant {

	/**
	 * 过期时间
	 */
//	Long TOKEN_EXPIRYTIME = 7*24*60*60*1000l;
	Long TOKEN_EXPIRYTIME = 60*60*1000l;
	
	//生成token所需盐
	String TOKEN_SALT = "kyj2016$";
	
	//token存放resis中前缀常量
	String TOKEN_CONSTANT = "token:";
}
