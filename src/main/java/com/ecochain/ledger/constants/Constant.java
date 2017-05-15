package com.ecochain.ledger.constants;


/**
 * 常量类
 * @Title: Constants.java 
 * @Package com.qkl.tfcc.api.common
 * @Description: 
 * @author kezhiyi   
 * @date 2016年8月17日 下午4:10:15 
 * @version V1.0
 */
public class Constant {
	
	public static final String SESSION_MANAGER = "user_name";
	public static final String OPERATIONS = "operations";
	public static final String MODULES = "modules";
	public static final int SUCCESS = 1;
	public static final int FAIL = 0;
	public static final String USING = "1";// 启用
	public static final int FORBIDDEN = 2;// 禁用
	public static final String LOGIN_USER = "loginUser";
	public static final String DEFAULT_PWD = "password";
	public static final int PAGE_SIZE = 20;
	public static final String CUR_SYS_CODE = "poc";
	public static final String DELETE = "9";	
	public static final String ROLE_ID_LIST = "roleIdList";
	public static final String VERSION_NO = "1.0.0";
	public static final String LOCKED ="1";
	public static final String UN_LOCKED ="0";
//	public static final Date  SYS_BUILDDATE =  DateUtil.getDateByString("2016-08-10");
	
	public static final int PIC_HEAD_HEIGHT = 122;//用户头像高
    public static final int PIC_HEAD_WIDTH = 122;//用户头像宽
    public static final String PIC_HEAD_PATH = "uploadfile/img/head/";//头像图片
    public static final String PIC_TEMP_PATH = "uploadtemp/img/head/";//头像图片临时文件路径
    public static final String PIC_QRCODE_PATH = "uploadfile/img/qrcode/";//二维码图片
    public static final String DP_PAY_QRCODE_PATH = "uploadfile/img/dp_pay_qrcode/";//店铺收款二维码地址
    public static final String IMG_PATH = "uploadfile/img/logo.png";//logo路径
}
