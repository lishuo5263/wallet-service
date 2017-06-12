package com.ecochain.ledger.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecochain.ledger.constants.Constant;
import com.ecochain.ledger.dao.DaoSupport;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.service.*;
import com.ecochain.ledger.util.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("userWalletService")
public class UserWalletServiceImpl implements UserWalletService {

    private final Logger logger = Logger.getLogger(UserWalletServiceImpl.class);
    @Resource(name = "daoSupport")
    private DaoSupport dao;
    @Resource
    private UsersDetailsService userDetailsService;
    @Resource
    private PayOrderService payOrderService;
    @Resource
    private AccDetailService accDetailService;
    @Resource
    private UserLoginService userLoginService;
    @Resource
    private SysGenCodeService sysGenCodeService;
    
    @Override
    public boolean deleteById(Integer id, String versionNo) throws Exception {
        return (Integer)dao.delete("UserWalletMapper.deleteById", id)>0;
    }

    @Override
    public boolean insert(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.save("UserWalletMapper.insert", pd)>0;
    }

    @Override
    public boolean insertSelective(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.save("UserWalletMapper.insertSelective", pd)>0;
    }

    @Override
    public PageData selectById(Integer id, String versionNo) throws Exception {
        return (PageData)dao.findForObject("UserWalletMapper.selectById", id);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public boolean updateByIdSelective(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UserWalletMapper.updateByIdSelective", pd)>0;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public boolean updateById(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UserWalletMapper.updateByIdSelective", pd)>0;
    }

    @Override
    public PageData getWalletByUserId(String user_id, String versionNo) throws Exception {
        return (PageData)dao.findForObject("UserWalletMapper.getWalletByUserId", user_id);
    }
    
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public boolean transferAccount(PageData pd, String versionNo) throws Exception {
        logger.info("------------------内部转账start---------------");
        boolean updateSubResult = updateSub(pd, versionNo);
        logger.info("------------------内部转账----从钱包扣钱updateSubResult："+updateSubResult);
        /*String mobile_phone = pd.getString("revbankaccno");//revbankaccno对方账户即对方手机号
        PageData usersDetails = usersDetailsService.findbyPhone(mobile_phone, Constant.VERSION_NO);*/
       /* PageData userLoginPD = new PageData();
        userLoginPD.put("account", pd.getString("revbankaccno"));*/
        
        //获取对方信息
        PageData userInfo = userLoginService.getUserInfoByAccount(pd.getString("revbankaccno"), Constant.VERSION_NO);
        boolean updateAddResult = false;
        if(updateSubResult){
            PageData wallet = new PageData();
            wallet.put("coin_amnt", pd.getString("coin_amnt"));
            wallet.put("user_id", String.valueOf(userInfo.get("user_id")));//对方user_id
            wallet.put("operator", pd.getString("operator"));
            updateAddResult = updateAdd(wallet, versionNo);
            logger.info("------------------内部转账----向钱包加钱updateAddResult：---------------"+updateAddResult);
            logger.info("------------------内部转账end---------转账结果："+(updateAddResult&&updateSubResult));
        }
        if(updateAddResult&&updateSubResult){
            /*//生成支付订单
            PageData payOrder = new PageData();
            payOrder.put("user_id", pd.get("user_id"));
            payOrder.put("pay_no", pd.getString("pay_no"));//支付号
            payOrder.put("fee_type", "3");//转三界石
            payOrder.put("cuy_type", "1");//1-三界通2-三界宝3-人民币
            payOrder.put("txamnt", pd.getString("future_currency"));//1-三界通2-三界宝3-人民币
            payOrder.put("status", "1");//0-待审核 1-成功 2-失败
            payOrder.put("revbankaccno", pd.getString("revbankaccno"));//revbankaccno对方账户即对方手机号
            payOrder.put("txdate", DateUtil.getCurrDateTime());
            payOrder.put("operator", pd.getString("operator"));
            payOrderService.insertSelective(payOrder, Constant.VERSION_NO);*/
            //生成账户流水
            PageData accDetail = new PageData();
            accDetail.put("user_id", pd.get("user_id"));
            accDetail.put("acc_no", "08");
            pd.put("acc_no", "08");//进区块链
            accDetail.put("user_type", pd.getString("user_type"));
            accDetail.put("rela_user_id", String.valueOf(userInfo.get("user_id")));//对方账户
            pd.put("rela_user_id", String.valueOf(userInfo.get("user_id")));//进区块链
            accDetail.put("rela_userlevel", "");//充值、转账、提现关联级别设为空
            accDetail.put("coin_amnt", pd.getString("coin_amnt"));
            accDetail.put("coin_name", pd.getString("coin_name"));
            accDetail.put("caldate", DateUtil.getCurrDateTime());
            pd.put("caldate", DateUtil.getCurrDateTime());
            accDetail.put("cntflag", "1");
            pd.put("cntflag", "1");//进区块链
            accDetail.put("status", "6");
            pd.put("status", "6");
            accDetail.put("otherno", pd.getString("flowno"));
            accDetail.put("other_amnt", pd.getString("hlb_amnt"));
            accDetail.put("other_source", "转出合链币");
            pd.put("other_source", "转出合链币");
            accDetail.put("operator", pd.getString("operator"));
            accDetail.put("remark1","转账-HLC");
            pd.put("remark1", "转账-HLC");
            /*if(Validator.isMobile(userInfo.getString("account"))){
                accDetail.put("remark1", "我转账给"+FormatNum.convertPhone(userInfo.getString("account")));  
            }else{
                accDetail.put("remark1", "我转账给"+(userInfo.getString("account").length()>10?userInfo.getString("account").substring(0, 10)+"...":userInfo.getString("account")));  
            }*/
            accDetail.put("remark2", pd.getString("account"));//自己账号  
            accDetail.put("remark3", pd.getString("revbankaccno"));//对方账号  
           pd.put("remark2", pd.getString("account"));
            pd.put("remark3", pd.getString("revbankaccno"));
            logger.info("====================测试代码========start================");
            String kql_url =null;
            List<PageData> codeList =sysGenCodeService.findByGroupCode("QKL_URL", Constant.VERSION_NO);
            for(PageData mapObj:codeList){
                if("QKL_URL".equals(mapObj.get("code_name"))){
                    kql_url = mapObj.get("code_value").toString();
                }
            }
            
//            String jsonStr = HttpUtil.sendPostData("http://192.168.200.83:8332/get_new_key", "");
            String jsonStr = HttpUtil.sendPostData(kql_url+"/get_new_key", "");
            JSONObject keyJsonObj = JSONObject.parseObject(jsonStr);
            PageData keyPd = new PageData();
            keyPd.put("data",Base64.getBase64((JSON.toJSONString(pd))));
            keyPd.put("publicKey",keyJsonObj.getJSONObject("result").getString("publicKey"));
            keyPd.put("privateKey",keyJsonObj.getJSONObject("result").getString("privateKey"));
            System.out.println("keyPd value is ------------->"+JSON.toJSONString(keyPd));
            //2. 获取公钥签名
            String signJsonObjStr =HttpUtil.sendPostData(kql_url+"/send_data_for_sign",JSON.toJSONString(keyPd));
            JSONObject signJsonObj = JSONObject.parseObject(signJsonObjStr);
            Map<String, Object> paramentMap =new HashMap<String, Object>();
            paramentMap.put("publickey",keyJsonObj.getJSONObject("result").getString("publicKey"));
            paramentMap.put("data",Base64.getBase64((JSON.toJSONString(pd))));
            paramentMap.put("sign",signJsonObj.getString("result"));
            String result = HttpUtil.sendPostData(kql_url+"/send_data_to_sys", JSON.toJSONString(paramentMap));
            JSONObject json = JSON.parseObject(result);
            if(StringUtil.isNotEmpty(json.getString("result"))){
                accDetail.put("hash", json.getString("result")); 
            }
            logger.info("====================测试代码=======end=================");
            
            accDetailService.insertSelective(accDetail, Constant.VERSION_NO);
            /*//插入对方账户流水
            PageData accDetail1 = new PageData();
            accDetail1.put("user_id", userInfo.get("user_id"));
            accDetail1.put("acc_no", "34");
            accDetail1.put("user_type", userInfo.getString("user_type"));
            accDetail1.put("rela_user_id", pd.get("user_id"));//转账人的user_id
            accDetail1.put("wlbi_amnt", pd.getString("future_currency"));
            accDetail1.put("caldate", DateUtil.getCurrDateTime());
            accDetail1.put("cntflag", "1");
            accDetail1.put("status", "4");
            accDetail1.put("otherno", payOrder.getString("pay_no"));
            accDetail1.put("other_amnt", pd.getString("future_currency"));
            accDetail1.put("other_source", "转入三界石");
            accDetail1.put("operator", pd.getString("operator"));
            //获取当前登陆用户的登陆信息
            PageData userLogin = userLoginService.getUserLoginByUserId(String.valueOf(pd.get("user_id")), Constant.VERSION_NO);
            if(Validator.isMobile(userLogin.getString("account"))){
                accDetail1.put("remark1", FormatNum.convertPhone(userLogin.getString("account"))+"转给我");  
            }else{
                accDetail1.put("remark1", userLogin.getString("account").length()>10?userLogin.getString("account").substring(0, 10)+"...转给我":userLogin.getString("account")+"转给我");  
            }
            accDetail1.put("remark2", userLogin.getString("account"));  
            accDetailService.insertSelective(accDetail1, Constant.VERSION_NO);*/
        }
        /*if(updateAddResult&&updateSubResult){
            List<PageData> codeList =sysGenCodeService.findByGroupCode("SENDSMS_FLAG", Constant.VERSION_NO);
            String smsflag ="";
            for(PageData mapObj:codeList){
                if("SENDSMS_FLAG".equals(mapObj.get("code_name"))){
                    smsflag = mapObj.get("code_value").toString();
                }
            }
            if("1".equals(smsflag)){
                PageData userInfo_self = userLoginService.getUserInfoByUserId(String.valueOf(pd.get("user_id")), versionNo);
                PageData userInfo_other = userLoginService.getUserInfoByUserId(String.valueOf(userInfo.get("user_id")), versionNo);
                
                String content_self = "尊敬的会员"+userInfo_self.getString("account")+"您好，您已成功向账户"+userInfo_other.getString("account")+"转账"+pd.getString("future_currency")+"个三界石。如非本人操作，请联系客服！";
                SMSUtil.sendSMS_ChinaNet1(userInfo_self.getString("mobile_phone"), content_self, SMSUtil.notice_productid);
                String content_other = "尊敬的会员"+userInfo_other.getString("account")+"您好，账户"+userInfo_self.getString("account")+"给您转账了"+pd.getString("future_currency")+"个三界石。请登录账号查收。";
                SMSUtil.sendSMS_ChinaNet1(userInfo_other.getString("mobile_phone"), content_other, SMSUtil.notice_productid);
            }
        }*/
        
        return(updateAddResult&&updateSubResult);
    }

    @Override
    public boolean updateSub(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UserWalletMapper.updateSub", pd)>0;
    }

    @Override
    public boolean updateAdd(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UserWalletMapper.updateAdd", pd)>0;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public boolean payNowBySJT(PageData pd, String versionNo) throws Exception {
        /*boolean result1 = false;
        boolean result2 = false;
        //判断供应商信息是否存在
        boolean isExistSupplierInfo = isExistSupplierInfo(pd.getString("shop_order_no"), versionNo);
        if(isExistSupplierInfo){
             result1 = (Integer)dao.update("UserWalletMapper.payNowBySJT", pd)>0;//用户扣钱
             if(result1){
                 result2 = addFrozeMoneyToSupplier(pd, versionNo); //供应商加钱
             }
        }
        return (result1&&result2);*/
        return (Integer)dao.update("UserWalletMapper.payNowBySJT", pd)>0;//用户扣钱
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public boolean payNowByRMB(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UserWalletMapper.payNowByRMB", pd)>0;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public boolean payNowByFrozeSJT(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UserWalletMapper.payNowByFrozeSJT", pd)>0;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public boolean payNowByFrozeRMB(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UserWalletMapper.payNowByFrozeRMB", pd)>0;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public boolean addFrozeMoneyToSupplier(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UserWalletMapper.addFrozeMoneyToSupplier", pd)>0;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public boolean addMoneyToSupplier(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UserWalletMapper.addMoneyToSupplier", pd)>0;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public boolean transfering(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UserWalletMapper.transfering", pd)>0;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public boolean transferSuccess(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UserWalletMapper.transferSuccess", pd)>0;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public boolean transferfail(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UserWalletMapper.transferfail", pd)>0;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public boolean isExistSupplierInfo(String shop_order_no, String versionNo) throws Exception {
        return (Integer)dao.findForObject("UserWalletMapper.isExistSupplierInfo", shop_order_no)>0;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public boolean payStoreBySJT(PageData pd, String versionNo) throws Exception {
        boolean result1 = false;
        boolean result2 = false;
        //判断店铺信息是否存在
        boolean isExistStoreInfo = isExistStoreInfo(pd.getString("store_order_sn"), versionNo);
        if(isExistStoreInfo){
             result1 = (Integer)dao.update("UserWalletMapper.payNowBySJT", pd)>0;//用户扣钱
             if(result1){
                 result2 = addFrozeWlbToStore(pd, versionNo); //店铺加钱
             }
        }
        /*result1 = (Integer)dao.update("UserWalletMapper.payNowBySJT", pd)>0;//用户扣钱
        if(result1){
            result2 = addFrozeWlbToStore(pd, versionNo); //店铺加钱
        }*/
        return (result1&&result2);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public boolean isExistStoreInfo(String store_order_sn, String versionNo) throws Exception {
        return (Integer)dao.findForObject("UserWalletMapper.isExistStoreInfo", store_order_sn)>0;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public boolean addFrozeWlbToStore(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UserWalletMapper.addFrozeWlbToStore", pd)>0;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public boolean addWlbToStore(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UserWalletMapper.addWlbToStore", pd)>0;
    }

    @Override
    public PageData getWalletByAccount(String account, String versionNo) throws Exception {
        return (PageData)dao.findForObject("UserWalletMapper.getWalletByAccount", account);
    }

    @Override
    public boolean updateSubByAccount(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UserWalletMapper.updateSubByAccount", pd)>0;
    }

    @Override
    public boolean exchangeSJS2RMB(PageData pd) throws Exception {
        return (Integer)dao.update("UserWalletMapper.exchangeSJS2RMB", pd)>0;
    }

    @Override
    public boolean exchangeRMB2SJS(PageData pd) throws Exception {
        return (Integer)dao.update("UserWalletMapper.exchangeRMB2SJS", pd)>0;
    }

    @Override
    public boolean withDrawalSubMoney(PageData pd) throws Exception {
        return (Integer)dao.update("UserWalletMapper.withDrawalSubMoney", pd)>0;
    }

    @Override
    public boolean withDrawalSubFrozeMoney() throws Exception {
        return (Integer)dao.update("UserWalletMapper.withDrawalSubFrozeMoney", null)>0;
    }

    @Override
    public boolean withDrawalAddMoney() throws Exception {
        return (Integer)dao.update("UserWalletMapper.withDrawalAddMoney", null)>0;
    }

    @Override
    public boolean exchangeHLB2RMB(PageData pd) throws Exception {
        return (Integer)dao.update("UserWalletMapper.exchangeHLB2RMB", pd)>0;
    }

    @Override
    public boolean exchangeRMB2HLB(PageData pd) throws Exception {
        return (Integer)dao.update("UserWalletMapper.exchangeRMB2HLB", pd)>0;
    }
    
    @Override
    public boolean payNowByHLB(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UserWalletMapper.payNowByHLB", pd)>0;//用户扣钱
    }
}
