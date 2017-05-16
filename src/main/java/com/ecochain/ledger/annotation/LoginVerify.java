package com.ecochain.ledger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
* @ClassName: LoginVerify 
* @Description: 登录验证注解
* @author zhangb
* @date 2016-5-10 下午8:46:00
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface LoginVerify {

}
