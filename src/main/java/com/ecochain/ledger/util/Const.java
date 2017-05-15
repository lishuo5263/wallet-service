package com.ecochain.ledger.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;

/**
 * 项目名称：
 * @author:fh
 * 
*/
public class Const {
	public static final String SESSION_SECURITY_CODE = "sessionSecCode";
	public static final String SESSION_USER = "sessionUser";
	public static final String SESSION_ROLE_RIGHTS = "sessionRoleRights";
	
	public static final String SESSION_menuList = "menuList";			//当前菜单
	public static final String SESSION_allmenuList = "allmenuList";		//全部菜单
	
	public static final String SESSION_QX = "QX";
	public static final String SESSION_userpds = "userpds";			
	
	public static final String SESSION_USERROL = "USERROL";				//用户对象
	public static final String SESSION_USERNAME = "USERNAME";			//用户名
	
	public static final String TRUE = "T";
	public static final String FALSE = "F";
	public static final String LOGIN = "/login_toLogin.do";				//登录地址
	
//	public static final String SYSNAME = "admin00/head/SYSNAME.txt";	//系统名称路径
//	public static final String PAGE	   = "admin00/head/PAGE.txt";		//分页条数配置路径
//	public static final String EMAIL   = "admin00/head/EMAIL.txt";		//邮箱服务器配置路径
//	public static final String SMS1   = "admin00/head/SMS1.txt";		//短信账户配置路径1
//	public static final String SMS2   = "admin00/head/SMS2.txt";		//短信账户配置路径2
	public static final String SYSNAME = "admin00"+File.separator+"head"+File.separator+"SYSNAME.txt";	//系统名称路径
	public static final String PAGE	   = "admin00"+File.separator+"head"+File.separator+"PAGE.txt";		//分页条数配置路径
	public static final String EMAIL   = "admin00"+File.separator+"head"+File.separator+"EMAIL.txt";		//邮箱服务器配置路径
	public static final String SMS1   = "admin00"+File.separator+"head"+File.separator+"SMS1.txt";		//短信账户配置路径1
	public static final String SMS2   = "admin00"+File.separator+"head"+File.separator+"SMS2.txt";		//短信账户配置路径2
	
	public static final String FILEPATH = "uploadify/uploads/";			//文件上传路径
	
	public static final String NO_INTERCEPTOR_PATH = ".*/((login)|(logout)|(register)|(SystemAppVersion)|(code)|(app)|(websocket)|(ueditor)|(js)|(css)).*";	//不对匹配该值的访问路径拦截（正则）
	public static ApplicationContext WEB_APP_CONTEXT = null; //该值会在web容器启动时由WebAppContextListener初始化
	
	/**
	 * APP Constants
	 */
	//app注册接口_请求协议参数)
	public static final String[] APP_REGISTERED_PARAM_ARRAY = new String[]{"countries","uname","passwd","title","full_name","company_name","countries_code","area_code","telephone","mobile"};
	public static final String[] APP_REGISTERED_VALUE_ARRAY = new String[]{"国籍","邮箱帐号","密码","称谓","名称","公司名称","国家编号","区号","电话","手机号"};
	
	//app登录接口_请求协议中的参数
	public static final String[] APP_LOGIN_PARAM_ARRAY = new String[]{"uname","passwd"};
	public static final String[] APP_LOGIN_VALUE_ARRAY = new String[]{"邮箱账号","密码"};
	
	//app登录状态接口_请求协议中的参数
	public static final String[] APP_LOGINSTATUS_PARAM_ARRAY = new String[]{"app_id","status"};
	public static final String[] APP_LOGINSTATUS_VALUE_ARRAY = new String[]{"app登录用户ID","登录状态"};	
	
	//忘记密码,查找用户账户是否存在接口_请求协议中的参数
	public static final String[] APP_FORGOTPASSWORD_PARAM_ARRAY = new String[]{"uname"};
	public static final String[] APP_FORGOTPASSWORD_VALUE_ARRAY = new String[]{"邮箱账号"};
	
	
	public static final String RESOURCES_BASE = "http://localhost:8080/DADCMS/";/*资源服务器*/
	public static final String RESOURCES_LOCAL = "E:\\apache-tomcat-7.0.57\\me-webapps\\DADCMS\\";/*资源服务器地址*/
	public static final String ACTIVITY_CONTESTANT_ID_WIDTH262_HEIGHT164 = "activity_contestant_id_width262_height164/";/*线上活动参赛选手图小图，规格262*164*/
	public static final String ACTIVITY_CONTESTANT_ID_WIDTH640_HEIGHT400 = "activity_contestant_id_width640_height400/";/*线上活动参赛选手图大图，规格600*400*/
	public static final String ACTIVITY_ID_WIDTH225_HEIGHT360 = "activity_id_width225_height360/";/*线上活动详情，规格225*360  此图片不做压缩处理，为单独上传操作*/
	public static final String ACTIVITY_ID_WIDTH_HEIGHT = "activity_id_width_height/";
	public static final String ACTIVITY_ORIGINAL_IMG = "original/activity/";/*活动原图*/
	public static final String ACTIVITY_ID_WIDTH_HEIGHT_MAX = "activity_id_width_height_max/";/*活动裁剪后的图*/
	public static final String NEWS_ORIGINAL_IMG = "original/news/";/*新闻原图*/
	public static final String NEWS_ID_WIDTH_HEIGHT= "news_id_width_height/";
	public static final String NEWS_ID_WIDTH_HEIGHT_MAX= "news_id_width_height_max/";/*新闻轮播，规格640*400*/
	public static final String NEWS_ID_WIDTH_HEIGHT_MIN= "news_id_width_height_min/";/*新闻列表，规格200*125*/
	public static final String COLLECT_NEWS_WIDTH_HEIGHT= "collect/news/";/*我的新闻收藏，规格200*125*/
	public static final String COLLECT_ACTIVITY_WIDTH_HEIGHT= "collect/activity/";/*我的活动收藏，规格200*125*/
	public static final String CONTESTANT_ORIGINAL_IMG = "original/contestant/";/*选手原图*/
	public static final String UEDITOR_PATH = "ueditor/jsp/";/*富文本框图片保存路径*/
	
//	public static final String RESOURCES_BASE = "http://115.28.39.196:8383/";
//	public static final String RESOURCES_LOCAL = File.separator + "tomcat" + File.separator + "resources8383" + File.separator + "webapps" + File.separator;
//	public static final String ACTIVITY_CONTESTANT_ID_WIDTH262_HEIGHT164 = "activity_contestant_id_width262_height164" + File.separator;
//	public static final String ACTIVITY_CONTESTANT_ID_WIDTH640_HEIGHT400 = "activity_contestant_id_width640_height400" + File.separator;
//	public static final String ACTIVITY_ID_WIDTH225_HEIGHT360 = "activity_id_width225_height360/";
//	public static final String ACTIVITY_ID_WIDTH_HEIGHT = "activity_id_width_height" + File.separator;
//	public static final String ACTIVITY_ORIGINAL_IMG = "original" + File.separator + "activity" + File.separator;
//	public static final String ACTIVITY_ID_WIDTH_HEIGHT_MAX = "activity_id_width_height_max" + File.separator;
//	public static final String NEWS_ORIGINAL_IMG = "original" + File.separator + "news" + File.separator;
//	public static final String NEWS_ID_WIDTH_HEIGHT = "news_id_width_height" + File.separator;
//	public static final String NEWS_ID_WIDTH_HEIGHT_MAX = "news_id_width_height_max" + File.separator;
//	public static final String NEWS_ID_WIDTH_HEIGHT_MIN = "news_id_width_height_min" + File.separator;
//	public static final String COLLECT_NEWS_WIDTH_HEIGHT = "collect" + File.separator + "news" + File.separator;
//	public static final String COLLECT_ACTIVITY_WIDTH_HEIGHT = "collect" + File.separator + "activity" + File.separator;
//	public static final String CONTESTANT_ORIGINAL_IMG = "original" + File.separator + "contestant" + File.separator;
//	public static final String UEDITOR_PATH = File.separator+"tomcat"+File.separator+"cms8787"+File.separator+"webapps"+File.separator+"DADCMS"+File.separator+"ueditor"+File.separator+"jsp"+File.separator;/*富文本框图片保存路径*/
	public static final String SHARE_ACTIVITY = "share/activity/";/*活动静态页寸存放地址*/
	public static final String SHARE_NEWS = "share/news/";/*新闻静态页存放地址*/
	public static final String TEMPLATE_ACTIVITYSHARE = "template/activityshare.html";/*活动模板页地址*/
	public static final String TEMPLATE_NEWSSHARE = "template/newsshare.html";/*新闻模板页地址*/
    public static void main(String[] args) {
	  System.out.println(File.separator); 
    }
}
