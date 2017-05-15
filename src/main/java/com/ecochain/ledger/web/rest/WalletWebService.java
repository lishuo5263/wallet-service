package com.ecochain.ledger.web.rest;

 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/api/rest")
@Api(value = "leger service")
public class WalletWebService {
	
	    @ResponseBody  
	    @RequestMapping(value = "/hello", method = RequestMethod.GET)  
	    @ApiOperation(nickname = "用于测试传参使用nickname", value = "用于测试传参使用value", notes = "用于测试传参使用notes")  
	    public String hello(@ApiParam(value = "hello param") @RequestParam String apiParam) {  
	        return "Hello , " + apiParam;  
	    }  
	    
	    @RequestMapping("/test")
	    @ApiOperation(nickname = "测试test接口nickname", value = "测试test接口value", notes = "测试test接口notes")  
	    public String home() {
	        return "Hello Ldger World";
	    }
	
 
}