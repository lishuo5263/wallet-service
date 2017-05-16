package com.ecochain.ledger.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;

/**投资管理卡号转换
 * <p>Description：手机号，身份证，银行卡号转换  </p>
 * @project_Name yc_util
 * @class_Name FormatNum.java
 * @author liuyang
 * @date 2015年4月15日
 * @version v1.0
 */
public class FormatNum {

	/**
	 * 身份证
	 * <p> (转换规则：显示前三位)  </p>
	 * @Title: convertIdentityCard 
	 * @param param String 身份证号
	 * @return param String 加密的身份证号
	 * @create author liuyang
	 * @create date 2015年4月15日
	 */
	public static String convertIdentityCard(String param) {
		if (StringUtils.isNotBlank(param)) {
			param = StringUtils.substring(param, 0, 3) + "***************";
		}
		return param;
	}

	/**
	 * 手机号
	 * <p> (转换规则：显示前三位和后三位)  </p>
	 * @Title: convertPhone 
	 * @param param String 手机号
	 * @return param String 加密的手机号
	 * @create author liuyang
	 * @create date 2015年4月15日
	 */
	public static String convertPhone(String param) {
		if (StringUtils.isNotBlank(param) && param.length() == 11) {
			param = param.substring(0, 3) + "*****" + param.substring(8, 11);
		}
		return param;
	}

	/**
	 * 银行卡号 
	 * <p> (转换规则：显示前三位和后三位)  </p>
	 * @Title: convertBankCard 
	 * @param param String 银行卡号
	 * @return param String 加密的银行卡号
	 * @create author liuyang
	 * @create date 2015年4月15日
	 */
	public static String convertBankCard(String param) {
		if (StringUtils.isNotBlank(param) && param.length() > 4) {
			param = param.substring(0, 3) + "*************"+ param.substring(param.length() - 3, param.length());
		}
		return param;
	}
	
	public static String convertEzbUserName(String param) {
		if (StringUtils.isNotBlank(param) && param.length() > 1) {
			param = param.substring(0, 1) + "*************"+ param.substring(param.length() - 1, param.length());
		}
		return param;
	}

	/**
	 * 座机号
	 * <p> (转换规则：之过滤电话的中间4位，不过滤区号和分机号)  </p>
	 * @Title: convertPhone 
	 * @param param String 座机号
	 * @return param String 加密的座机号
	 * @create author lian
	 * @create date 2015年5月2日
	 */
	public static String convertFixedPhone(String param) {
		/*if (StringUtils.isNotBlank(param) &&param.lastIndexOf("-")==-1) {//不带区号不带分机号，例:2586395(7位)或25369856(8位)
			if(param.length()==7){
				param = param.substring(0, 1) + "****"+param.substring(6);
			}else if(param.length()==8){
				param = param.substring(0, 2) + "****"+param.substring(6);
			}
		}else if(StringUtils.isNotBlank(param) &&param.lastIndexOf("-")<5){//带区号不带分机号，例:010-2586395(7位)或010-25369856(8位)
			String area=param.substring(0,param.lastIndexOf("-"))+"-";
			String fixedPhone = param.substring(param.lastIndexOf("-")+1);
			if(fixedPhone.length()==7){
				fixedPhone = fixedPhone.substring(0, 1) + "****"+fixedPhone.substring(5);
			}else if(fixedPhone.length()==8){
				fixedPhone = fixedPhone.substring(0, 2) + "****"+fixedPhone.substring(6);
			}
			param=area+fixedPhone;
		}else if(StringUtils.isNotBlank(param) &&param.lastIndexOf("-")>5){//带区号带分机号，例:010-2586395-012(7位)或010-25369856-015(8位)
			String area=param.substring(0,param.indexOf("-"))+"-";
			String fixedPhone = param.substring(param.indexOf("-")+1,param.lastIndexOf("-"));
			String extension = param.substring(param.lastIndexOf("-"));
			if(fixedPhone.length()==7){
				fixedPhone = fixedPhone.substring(0, 1) + "****"+fixedPhone.substring(5);
			}else if(fixedPhone.length()==8){
				fixedPhone = fixedPhone.substring(0, 2) + "****"+fixedPhone.substring(6);
			}
			param=area+fixedPhone+extension;
		}*/
		return StringUtils.substring(param, 0, 3)+"******"+  StringUtils.substring(param, -3);
	}
	
	@SuppressWarnings("unused")
	public static String formatCommaAnd2Point(Object obj) {
		BigDecimal decimal = formatComma2BigDecimal(obj);
		DecimalFormat df = new DecimalFormat("#,###.##");
		String decimalStr = df.format(decimal).equals(".00") ? "0.00" : df
				.format(decimal);
		if (decimalStr.startsWith(".")) {
			decimalStr = "0" + decimalStr;
		}
		return decimalStr.replace(",", "");
	}

	private static BigDecimal formatComma2BigDecimal(Object obj) {
		String val = String.valueOf(obj);
		if (val == null)
			return new BigDecimal("0.00");

		val = val.replaceAll(",", "");
		if (!isNumber(val))
			return new BigDecimal("0.00");

		BigDecimal decimal = new BigDecimal(val);
		decimal = decimal.setScale(2, RoundingMode.HALF_UP);

		return decimal;

	}

	private static boolean isNumber(String value) {
		return isInteger(value) || isDouble(value);
	}

	private static boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			if (value.contains("."))
				return true;
			return false;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static String convertEUserName(String eUserName){
		return StringUtils.substring(eUserName, 0, 1)+"***"+StringUtils.substring(eUserName,-1);
	}
	public static String convertRealName(String realName){
        return "*"+StringUtils.substring(realName, 1, realName.length());
    }
	
	public static void main(String[] args) {
//		System.out.println(convertFixedPhone("010-2729079-012"));
		System.out.println(convertFixedPhone("010-27290791-015"));
		System.out.println(convertEUserName("张三"));
		System.out.println(convertPhone("18618382548"));
		System.out.println(convertRealName("张春明"));
	}
}
