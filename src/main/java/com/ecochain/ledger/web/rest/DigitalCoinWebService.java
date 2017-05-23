package com.ecochain.ledger.web.rest;


import com.alibaba.fastjson.JSONObject;
import com.ecochain.ledger.annotation.LoginVerify;
import com.ecochain.ledger.base.BaseWebService;
import com.ecochain.ledger.constants.CodeConstant;
import com.ecochain.ledger.constants.Constant;
import com.ecochain.ledger.constants.CookieConstant;
import com.ecochain.ledger.model.Page;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.service.DigitalCoinService;
import com.ecochain.ledger.service.UserWalletService;
import com.ecochain.ledger.util.AjaxResponse;
import com.ecochain.ledger.util.RequestUtils;
import com.ecochain.ledger.util.SessionUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by LiShuo on 2017/05/23.
 */
@RestController
@RequestMapping(value = "/api/rest/digitalCoin")
public class DigitalCoinWebService extends BaseWebService {

    private Logger logger = Logger.getLogger(DigitalCoinWebService.class);

    @Autowired
    private DigitalCoinService digitalCoinService;

    @Autowired
    private UserWalletService userWalletService;

    /**
     * 获取当前币种价格
     */
    @GetMapping("getCoinPrice")
    @ApiOperation(nickname = "getCoinPrice", value = "获取当前币种价格", notes = "获取当前币种价格！！")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coinName", value = "列如 HLB", required = true, paramType = "query", dataType = "String"),
    })
    public AjaxResponse getCoinPrice(HttpServletRequest request, Page page){
        PageData pd = new PageData();
        pd = this.getPageData();
        page.setPd(pd);
        AjaxResponse ar= new AjaxResponse();
        try{
            Map<String, Object> map = this.digitalCoinService.getCoinPrice(request.getParameter("coinName"));
            map.put("coin_rate",map.get("coin_rate").toString().split(":")[0]);
            ar=fastReturn(map,true,"获取当前币种价格成功！",CodeConstant.SC_OK);
        }catch(Exception e){
            logger.debug(e.toString(), e);
            ar=fastReturn(null,false,"系统异常，获取当前币种价格失败！",CodeConstant.SYS_ERROR);
        }
        return ar;
    }

    /**
     * 获取买家账户信息
     */
    @LoginVerify
    @GetMapping("getBuyerInfo")
    @ApiOperation(nickname = "getBuyerInfo", value = "获取买家账户信息", notes = "获取买家账户信息！！")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "CSESSIONID", value = "CSESSIONID", required = true, paramType = "query", dataType = "String"),
    })
    public AjaxResponse getBuyerInfo(HttpServletRequest request, Page page){
        PageData pd = new PageData();
        pd = this.getPageData();
        page.setPd(pd);
        AjaxResponse ar= new AjaxResponse();
        try{
            String userstr = SessionUtil.getAttibuteForUser(RequestUtils.getRequestValue(CookieConstant.CSESSIONID, request));
            JSONObject user = JSONObject.parseObject(userstr);
            ar=fastReturn(this.userWalletService.getWalletByUserId(String.valueOf(user.get("id")), Constant.VERSION_NO),true,"获取买家账户信息！",CodeConstant.SC_OK);
        }catch(Exception e){
            logger.debug(e.toString(), e);
            ar=fastReturn(null,false,"系统异常，获取买家账户信息失败！",CodeConstant.SYS_ERROR);
        }
        return ar;
    }
}
