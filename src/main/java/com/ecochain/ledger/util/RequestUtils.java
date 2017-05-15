package com.ecochain.ledger.util;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ecochain.ledger.constants.CookieConstant;



/**
 * request工具类
 * 
 */
public class RequestUtils {

	/**
	 * 
	 * @Title: geCookieValue
	 * @Description: get cookievalue 如果为空，自己以uuid来生成并存到cookie
	 * @param @param cookieKey
	 * @param @param request
	 * @param @param response
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws IOException 
	 */
	public static String geCookieValue(String cookieKey,
			HttpServletRequest request, HttpServletResponse response){
		Cookie[] cookies = request.getCookies();
		if (StringUtil.isNotEmpty(cookieKey)) {
			if (null != cookies && cookies.length > 0) {
				for (Cookie cookie : cookies) {
					if (cookieKey.equals(cookie.getName())) {
						return cookie.getValue();
					}
				}
			}else{
	            if(StringUtil.isNotEmpty(request.getParameter(cookieKey))){
	                return request.getParameter(cookieKey);
	            }
	        }
		}else{
            if(StringUtil.isNotEmpty(request.getParameter(cookieKey))){
                return request.getParameter(cookieKey);
            }
		}
//		String gotoURL = request.getParameter("returnUrl");
//		if(gotoURL==null){
//			response.sendRedirect("");
//		}
//		System.out.println("setDomain - gotoURL:"+gotoURL);
		// 生成一个
		String csessionid = UUID.randomUUID().toString().replaceAll("-", "");
		Cookie cookie = new Cookie(cookieKey, csessionid);
		// 设置路径
//		 cookie.setDomain("pcshop.cc");
		/*String domain = ConfigUtil.getValue("domain");
		if(StringUtil.isNotEmpty(domain)){
			cookie.setDomain(domain);
		}*/
		cookie.setPath("/");
		response.addCookie(cookie);

		return csessionid;
	}
	/**
	 * @describe:从请求中获取sessionId
	 * @author: zhangchunming
	 * @date: 2016年11月9日下午3:19:12
	 * @param sessionKey
	 * @param request
	 * @return: String
	 */
	public static String getSessionId(String sessionKey,HttpServletRequest request){
	    if (StringUtil.isNotEmpty(sessionKey)) {
	        String sessionId = request.getParameter(sessionKey);
	        if(StringUtil.isNotEmpty(sessionId)){
	            return sessionId;
	        }
        }
	    String sessionId = UUID.randomUUID().toString().replaceAll("-", "");
        return sessionId;
	}
	/**
	 * @describe:获取sessionId
	 * @author: zhangchunming
	 * @date: 2016年11月10日上午9:37:52
	 * @param sessionKey
	 * @param request
	 * @return: String
	 */
	public static String getSessionValue(String sessionKey,HttpServletRequest request){
        if (StringUtil.isNotEmpty(sessionKey)) {
            String sessionId = (String)request.getSession().getAttribute(sessionKey);
            if(StringUtil.isNotEmpty(sessionId)){
                return sessionId;
            }
        }
        String sessionId = UUID.randomUUID().toString().replaceAll("-", "");
        request.getSession().setAttribute(sessionKey, sessionId);
        return sessionId;
    }
	/**
	 * @describe:从请求中获取参数
	 * @author: zhangchunming
	 * @date: 2016年11月13日下午5:06:50
	 * @param sessionKey
	 * @param request
	 * @return: String
	 */
	public static String getRequestValue(String sessionKey,HttpServletRequest request){
	    if (StringUtil.isNotEmpty(sessionKey)) {
            String sessionId = (String)request.getParameter(sessionKey);
            if(StringUtil.isNotEmpty(sessionId)){
                sessionId = Base64.getFromBase64(sessionId);
                return sessionId;
            }
        }
        String sessionId = "";
        if("APP".equals(request.getParameter("source"))){
            sessionId = UUID.randomUUID().toString().replaceAll("-", "")+":APP";
        }else{
            sessionId = UUID.randomUUID().toString().replaceAll("-", "");
        }
        return sessionId;
    }
	/**
	 * @describe:清除session标识
	 * @author: zhangchunming
	 * @date: 2016年11月10日上午9:38:09
	 * @param sessionKey
	 * @param request
	 * @return: void
	 */
	public static void delSessionValue(String sessionKey,HttpServletRequest request){
        if (StringUtil.isNotEmpty(sessionKey)) {
            request.getSession().removeAttribute(sessionKey);
        }
    }
	/**
	 * @Description:set 向cookie存值
	 * 
	 * @param request
	 * @param response
	 * @param cookieKey
	 * @param cookieValue
	 */
	public static void setCookieValue(HttpServletRequest request,
			HttpServletResponse response, String cookieKey, String cookieValue) {
		if (request.isRequestedSessionIdFromCookie()) {
			Cookie cookie = new Cookie(cookieKey, cookieValue);
			cookie.setMaxAge(-1);
			// cookie.setDomain(".kyj.com");
			/*String domain = ConfigUtil.getValue("domain");
			if(StringUtil.isNotEmpty(domain)){
				cookie.setDomain(domain);
			}*/
			cookie.setPath("/");
			response.addCookie(cookie);
		}
	}
	
	/**
	 * 日期：2016-4-28<br>
	 * 版本：v1.0<br>
	 * 描述：getCookieValueByKey(通过key获取cookieValue,如果cookieValue为null,则返回null) <br>
	 * 创建日期：2016-4-28 下午2:49:51 <br>
	 * 创建人 : zhangjian<br>
	 * @param cookieKey
	 * @param request
	 * @param response
	 * @return
	 * String
	 * @Exception 异常对象 <br>
	 */
	public static String getCookieValueByKey(String cookieKey,HttpServletRequest request, HttpServletResponse response){
		String cookieValue = null;
		if (StringUtil.isEmpty(cookieKey)) {
			return null;
		}
		if (null == request.getCookies() || request.getCookies().length <= 0) {
			return null;
		}
		for (Cookie cookie : request.getCookies()) {
			if (cookieKey.equals(cookie.getName())) {
				cookieValue = cookie.getValue();
			}
		}
		return cookieValue;
	}

	//del cookie
	public static void delCookie(HttpServletRequest request,HttpServletResponse response,String key){
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie:cookies){
			if(cookie.getName().equals(CookieConstant.CSESSIONID)){
				cookie.setMaxAge(0);
				/*String domain = ConfigUtil.getValue("domain");
				if(StringUtil.isNotEmpty(domain)) cookie.setDomain(domain);*/
				cookie.setPath("/");
				response.addCookie(cookie);
				break;
			}
		}
	}
	
	public static void delWebCookie(HttpServletRequest request,HttpServletResponse response,String key){
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie:cookies){
			if(cookie.getName().equals(CookieConstant.ADMIN_SESSION)){
				cookie.setMaxAge(0);
				/*String domain = ConfigUtil.getValue("domain");
				if(StringUtil.isNotEmpty(domain)) cookie.setDomain(domain);*/
				cookie.setPath("/");
				response.addCookie(cookie);
				break;
			}
		}
	}
}
