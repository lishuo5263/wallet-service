package com.ecochain.ledger.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;


@WebFilter(filterName="crossOriginFilter",urlPatterns="/*")
public class CrossOriginFilter implements Filter{
	 private FilterConfig config = null;

	    @Override
	    public void init(FilterConfig config) throws ServletException {
	        this.config = config;
	    }

	    @Override
	    public void destroy() {
	        this.config = null;
	    }

	    /**
	     * 
	     * @author wwhhf
	     * @since 2016/5/30
	     * @comment 跨域的设置
	     */
	    @Override
	    public void doFilter(ServletRequest request, ServletResponse response,
	            FilterChain chain) throws IOException, ServletException {
	        HttpServletResponse httpResponse = (HttpServletResponse) response;
	        // 表明它允许"http://xxx"发起跨域请求
	        HttpServletResponse res = (HttpServletResponse) response;  
	        res.setContentType("text/html;charset=UTF-8");  
	           res.setHeader("Access-Control-Allow-Origin", "*");  
	           res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");  
	           res.setHeader("Access-Control-Max-Age", "0");  
	           res.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");  
	           res.setHeader("Access-Control-Allow-Credentials", "true");  
	           res.setHeader("XDomainRequestAllowed","1");  
	           chain.doFilter(request, response);
	    }

}
