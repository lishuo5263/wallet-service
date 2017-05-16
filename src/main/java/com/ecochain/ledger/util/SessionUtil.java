package com.ecochain.ledger.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;


public class SessionUtil{

    @Autowired
    private  CacheManager cacheManager;
	
	public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    //用户超时分钟
	private  Integer sexp = 30;
	
	//验证码超时时间
	private  Integer cexp = 10;
	
	//把用户名入到 Redis中   1小时
	public  void setAttributeForUser(String name, String value) {
//		redisStringTemplate.setex(name+":"+LoginConstant.USER_NAME, sexp*60, value);
	    if(name.contains("app")){
	        cacheManager.set(name+":"+LoginConstant.USER_NAME, value, sexp*60*2*24*30);
	    }else{
	        cacheManager.set(name+":"+LoginConstant.USER_NAME, value, sexp*60);
	    }
	}

	//取用户信息
    public  String getAttibuteForUser(String name) {
        if(!cacheManager.isExist(name + ":" + LoginConstant.USER_NAME)){
            return "";
        }
        String value = (String)cacheManager.get(name + ":" + LoginConstant.USER_NAME);
        if(name.contains("app")){
          //重新设置时间 
            if(null != value){
                //一个月
//              redisStringTemplate.expire(name + ":" + LoginConstant.USER_NAME, sexp*60);
                 cacheManager.set(name+":"+LoginConstant.USER_NAME, value, sexp*60*2*24*30);
            } 
        }else{
            //重新设置时间 
            if(null != value){
                //30分钟
//              redisStringTemplate.expire(name + ":" + LoginConstant.USER_NAME, sexp*60);
                 cacheManager.set(name+":"+LoginConstant.USER_NAME, value, sexp*60);
            } 
        }
        return value;
    }
    //删除用户信息
    public  void delAttibuteForUser(String name) {
        if(cacheManager.isExist(name + ":" + LoginConstant.USER_NAME)){
            cacheManager.del(name + ":" + LoginConstant.USER_NAME);
        }
    }
    
	public  void setAPPForUser(String name, String value) {
        cacheManager.set(name+":"+LoginConstant.APP_USER_NAME, value, sexp*60*2*24*30);
    }
	
	//取用户信息
    public  String getAPPForUser(String name) {
        if(!cacheManager.isExist(name + ":" + LoginConstant.APP_USER_NAME)){
            return "";
        }
        String value = (String)cacheManager.get(name + ":" + LoginConstant.APP_USER_NAME);
        //重新设置时间 
        if(null != value){
            //30分钟
             cacheManager.set(name+":"+LoginConstant.APP_USER_NAME, value, sexp*60*2*24*30);
        }
        return value;
    }
    //删除用户信息
    public  void delAPPForUser(String name) {
        if(cacheManager.isExist(name + ":" + LoginConstant.APP_USER_NAME)){
            cacheManager.del(name + ":" + LoginConstant.APP_USER_NAME);
        }
    }
	//把验证码放到Redis中  10分钟
	public void setAttributeForCode(String name, String value) {
	    cacheManager.set(name+":"+LoginConstant.LOGIN_CODE, value, cexp*60);
	}


	
	//取验证码
	public  String getAttibuteForCode(String name) {
		String vcode = (String)cacheManager.get(name + ":" + LoginConstant.LOGIN_CODE);
		return vcode;
	}
	public  void delAttibuteForCode(String name) {
        cacheManager.del(name + ":" + LoginConstant.LOGIN_CODE);
    }

	//校验移动端token
	public  boolean CheckToken(String token) {
		if(StringUtil.isEmpty(token)){
			return false;
		}
		String bt = new String(Base64.decodeBase64(token));
		if(StringUtil.isEmpty(bt)){
			return false;
		}
		String[] bts = bt.split(":");
		if(bts.length > 0){
			String account = bts[0];
			String tomeOutStr = bts[1];
			if(StringUtil.isEmpty(account) || StringUtil.isEmpty(tomeOutStr)){
				return false;
			}
			long currentTime = System.currentTimeMillis();
			if((Long.valueOf(tomeOutStr) - currentTime) < 0){
				return false;
			}
            String serverToken = (String)cacheManager.get(TokenConstant.TOKEN_CONSTANT+account);
			if(StringUtil.isEmpty(serverToken)){
				return false;
			}
			boolean equals = token.equals(serverToken);
			return equals;
		}
		return false;
	}

	//生成移动端token并存放到redis
	public  String setAttributeForToken(String accname, String pwd, long expiryOutBySeconds) {
		long loginTime = System.currentTimeMillis();
		String cookieValue = LoginAuthHelper.genAuthCookieValue(accname, pwd, TokenConstant.TOKEN_EXPIRYTIME*1000+loginTime, expiryOutBySeconds);
		System.out.println(cookieValue);
//		redisStringTemplate.setex(TokenConstant.TOKEN_CONSTANT+accname, TokenConstant.TOKEN_EXPIRYTIME.intValue(), cookieValue);
		cacheManager.set(TokenConstant.TOKEN_CONSTANT+accname, cookieValue, TokenConstant.TOKEN_EXPIRYTIME.intValue());
		return cookieValue;
	}
}
