package com.ecochain.ledger.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.ecochain.ledger.annotation.LoginVerify;
import com.ecochain.ledger.constants.CodeConstant;
import com.ecochain.ledger.constants.CookieConstant;
import com.ecochain.ledger.util.AjaxResponse;
import com.ecochain.ledger.util.Const;
import com.ecochain.ledger.util.RequestUtils;
import com.ecochain.ledger.util.SessionUtil;
import com.ecochain.ledger.util.StringUtil;


public class GlobalInterceptor implements HandlerInterceptor{

	private static Logger log = Logger.getLogger(GlobalInterceptor.class);
	/*@Autowired
    private CacheManager cacheManager;*/
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
	    response.setCharacterEncoding("utf-8");
//		String versioncode = request.getParameter("versioncode");
		String platform = request.getParameter("platform");
		String code = request.getParameter("code");
		String params = request.getParameter("params");
		log.info(platform+"::::平台号-----------");
		log.info("RequestURL=="+request.getRequestURI());
		int codel = 0;
		if(code != null) codel = Integer.parseInt(code);
		AjaxResponse ar = new AjaxResponse();
		//根据请求参数长度来判断请求是否安全
		if(StringUtil.isNotEmpty(params)){
			System.out.println("参数长度:"+params.length());
			System.out.println("接收到参数长度"+code);
			if(params.length() != codel){
			    ar.setErrorCode(CodeConstant.SC_UNKNOWN);
			    ar.setMessage("传入参数有误");
			    ar.setSuccess(false);
				response.getOutputStream().print(JSONObject.toJSONString(ar));
				return false;
			}
		}
		
		String path = request.getServletPath();
        if(path.matches(Const.NO_INTERCEPTOR_PATH)){
            return true;
        }else{
    		//验证登录
    		if(handler instanceof HandlerMethod){
    			HandlerMethod method = (HandlerMethod)handler;
    			if(method != null){
    				LoginVerify loginVerify = method.getMethod().getAnnotation(LoginVerify.class);
    				if(loginVerify != null){
    				    String user = null;
    				    user = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
    					if(StringUtil.isEmpty(user)){
    					    ar.setErrorCode(CodeConstant.UNLOGIN);
                            ar.setMessage("未登录");
                            ar.setSuccess(false);
                            response.getWriter().print(JSONObject.toJSONString(ar));
                            response.getWriter().close();
                            return false;
    					}
    				}
    			}
    		}
        }
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
