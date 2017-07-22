package com.ecochain.ledger.web.rest;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import tk.mybatis.mapper.util.StringUtil;

import com.alibaba.fastjson.JSONObject;
import com.ecochain.ledger.annotation.LoginVerify;
import com.ecochain.ledger.base.BaseWebService;
import com.ecochain.ledger.constants.CodeConstant;
import com.ecochain.ledger.constants.Constant;
import com.ecochain.ledger.constants.CookieConstant;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.service.AccDetailService;
import com.ecochain.ledger.service.BankCardService;
import com.ecochain.ledger.service.DigitalCoinService;
import com.ecochain.ledger.service.PayOrderService;
import com.ecochain.ledger.service.SendVodeService;
import com.ecochain.ledger.service.SysGenCodeService;
import com.ecochain.ledger.service.SysMaxnumService;
import com.ecochain.ledger.service.UserBankService;
import com.ecochain.ledger.service.UserLoginService;
import com.ecochain.ledger.service.UserWalletService;
import com.ecochain.ledger.service.UsersDetailsService;
import com.ecochain.ledger.util.AjaxResponse;
import com.ecochain.ledger.util.DateUtil;
import com.ecochain.ledger.util.Logger;
import com.ecochain.ledger.util.MD5Util;
import com.ecochain.ledger.util.OrderGenerater;
import com.ecochain.ledger.util.RequestUtils;
import com.ecochain.ledger.util.SessionUtil;
import com.ecochain.ledger.util.Validator;
import com.github.pagehelper.PageInfo;

/**
 * 账户控制类
 * @author zhangchunming
 */
@RestController
@RequestMapping("/api/rest/bankcard")
@Api(value = "账户管理")
public class BankCardWebSerivce extends BaseWebService{
    
    private final Logger logger = Logger.getLogger(BankCardWebSerivce.class);
    
    @Autowired
    private BankCardService bankCardService;

    /**
     * @describe:查询银行列表
     * @author: zhangchunming
     * @date: 2017年7月20日下午10:15:26
     * @param request
     * @return: AjaxResponse
     */
    @LoginVerify
    @PostMapping("/getBankCardList")
    @ApiOperation(nickname = "获取银行列表", value = "获取银行列表", notes = "获取银行列表")
    @ApiImplicitParams({
    })
    public AjaxResponse getBankCardList(HttpServletRequest request){
        AjaxResponse ar = new AjaxResponse();
        Map<String,Object> data = new HashMap<String, Object>();
        try {
            List<PageData> bankCardList = bankCardService.getBankCardList();
            data.put("bankCardList", bankCardList);
            ar.setData(data);
            ar.setSuccess(true);
            ar.setMessage("查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }   
        return ar;
    }
    
    /**
     * @describe:添加银行
     * @author: zhangchunming
     * @date: 2017年7月20日下午10:35:39
     * @param request
     * @return: AjaxResponse
     */
    @LoginVerify
    @PostMapping("/addBankCard")
    @ApiOperation(nickname = "添加银行（用于后台系统）", value = "添加银行（用于后台系统）", notes = "添加银行（用于后台系统）")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "bank_name", value = "银行名称", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "bank_brief", value = "银行缩写", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "icon", value = "图标地址", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse addBankCard(HttpServletRequest request){
        AjaxResponse ar = new AjaxResponse();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            if(StringUtil.isEmpty(pd.getString("bank_name"))){
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setMessage("请输入银行名称！");
                ar.setSuccess(false);
                return ar;
            }
            if(StringUtil.isEmpty(pd.getString("bank_brief"))){
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setMessage("请输入银行简写！");
                ar.setSuccess(false);
                return ar;
            }
            if(StringUtil.isEmpty(pd.getString("icon"))){
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setMessage("请输入银行图标！");
                ar.setSuccess(false);
                return ar;
            }
            boolean exist = bankCardService.isExist(pd.getString("bank_name"));
            if(exist){
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setMessage("该银行已添加，请勿重复添加！");
                ar.setSuccess(false);
                return ar;
            }
            boolean insert = bankCardService.insert(pd);
            if(insert){
                ar.setMessage("添加成功！");
                ar.setSuccess(true);
                return ar;
            }else{
                ar.setErrorCode(CodeConstant.UPDATE_FAIL);
                ar.setMessage("添加失败！");
                ar.setSuccess(false);
                return ar;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }   
        return ar;
    }
    
    /**
     * @describe:删除银行
     * @author: zhangchunming
     * @date: 2017年7月20日下午10:35:21
     * @param request
     * @return: AjaxResponse
     */
    @LoginVerify
    @PostMapping("/delBankCard")
    @ApiOperation(nickname = "删除银行（用于后台系统）", value = "删除银行（用于后台系统）", notes = "删除银行（用于后台系统）")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "主键ID", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse delBankCard(HttpServletRequest request){
        AjaxResponse ar = new AjaxResponse();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            if(StringUtil.isEmpty(pd.getString("id"))){
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setMessage("请输入ID！");
                ar.setSuccess(false);
                return ar;
            }
            boolean deleteById = bankCardService.deleteById(Integer.valueOf(pd.getString("id")));
            if(deleteById){
                ar.setMessage("删除成功！");
                ar.setSuccess(true);
                return ar;
            }else{
                ar.setErrorCode(CodeConstant.UPDATE_FAIL);
                ar.setMessage("删除失败！");
                ar.setSuccess(false);
                return ar;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }   
        return ar;
    }
    
    /**
     * @describe:编辑银行
     * @author: zhangchunming
     * @date: 2017年7月20日下午10:35:21
     * @param request
     * @return: AjaxResponse
     */
    @LoginVerify
    @PostMapping("/updateBankCard")
    @ApiOperation(nickname = "编辑银行（用于后台系统）", value = "编辑银行（用于后台系统）", notes = "编辑银行（用于后台系统）")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "主键ID", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "bank_name", value = "主键ID", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "bank_brief", value = "主键ID", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "icon", value = "主键ID", required = true, paramType = "query", dataType = "String")
    })
    public AjaxResponse updateBankCard(HttpServletRequest request){
        AjaxResponse ar = new AjaxResponse();
        PageData pd = new PageData();
        pd = this.getPageData();
        try {
            if(StringUtil.isEmpty(pd.getString("id"))){
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setMessage("请输入ID！");
                ar.setSuccess(false);
                return ar;
            }
            
            if(StringUtil.isEmpty(pd.getString("bank_name"))){
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setMessage("请输入银行名称！");
                ar.setSuccess(false);
                return ar;
            }
            if(StringUtil.isEmpty(pd.getString("bank_brief"))){
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setMessage("请输入银行简写！");
                ar.setSuccess(false);
                return ar;
            }
            if(StringUtil.isEmpty(pd.getString("icon"))){
                ar.setErrorCode(CodeConstant.PARAM_ERROR);
                ar.setMessage("请输入银行图标！");
                ar.setSuccess(false);
                return ar;
            }
            
            boolean updateByIdSelective = bankCardService.updateByIdSelective(pd);
            if(updateByIdSelective){
                ar.setMessage("编辑成功！");
                ar.setSuccess(true);
                return ar;
            }else{
                ar.setErrorCode(CodeConstant.UPDATE_FAIL);
                ar.setMessage("编辑失败！");
                ar.setSuccess(false);
                return ar;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setErrorCode(CodeConstant.SYS_ERROR);
            ar.setMessage("网络繁忙，请稍候重试！");
        }   
        return ar;
    }
}
