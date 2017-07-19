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
        if("BTC".equals(pd.getString("coin_name"))){//比特币转账
            pd.put("btc_amnt", pd.getString("coin_amnt"));
        }
        boolean updateSubResult = updateSub(pd, versionNo);
        logger.info("------------------内部转账----从钱包扣钱updateSubResult："+updateSubResult);
        
        //获取对方信息
        PageData userInfo = userLoginService.getUserInfoByAccount(pd.getString("revbankaccno"), Constant.VERSION_NO);
        boolean updateAddResult = false;
        if(updateSubResult){
            PageData wallet = new PageData();
            if("BTC".equals(pd.getString("coin_name"))){//比特币转账
                wallet.put("btc_amnt", pd.getString("coin_amnt"));
            }
            wallet.put("user_id", String.valueOf(userInfo.get("user_id")));//对方user_id
            wallet.put("operator", pd.getString("operator"));
            updateAddResult = updateAdd(wallet, versionNo);
            logger.info("------------------内部转账----向钱包加钱updateAddResult：---------------"+updateAddResult);
            logger.info("------------------内部转账end---------转账结果："+(updateAddResult&&updateSubResult));
        }
        if(updateAddResult&&updateSubResult){
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
            accDetail.put("remark2", pd.getString("account"));//自己账号  
            accDetail.put("remark3", pd.getString("revbankaccno"));//对方账号  
           pd.put("remark2", pd.getString("account"));
            pd.put("remark3", pd.getString("revbankaccno"));
            /*logger.info("====================测试代码========start================");
            String kql_url =null;
            List<PageData> codeList =sysGenCodeService.findByGroupCode("QKL_URL", Constant.VERSION_NO);
            for(PageData mapObj:codeList){
                if("QKL_URL".equals(mapObj.get("code_name"))){
                    kql_url = mapObj.get("code_value").toString();
                }
            }
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
            logger.info("====================测试代码=======end=================");*/
            
            accDetailService.insertSelective(accDetail, Constant.VERSION_NO);
        }
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

    @Override
    public boolean exchangeRMB2Coin(PageData pd) throws Exception {
        return (Integer)dao.update("UserWalletMapper.exchangeRMB2Coin", pd)>0;
    }

    @Override
    public boolean exchangeCoin2RMB(PageData pd) throws Exception {
        return (Integer)dao.update("UserWalletMapper.exchangeCoin2RMB", pd)>0;
    }

    @Override
    public boolean companySubCoin(PageData pd) throws Exception {
        return (Integer)dao.update("UserWalletMapper.companySubCoin", pd)>0;
    }

    @Override
    public boolean companyAddCoin(PageData pd) throws Exception {
        return (Integer)dao.update("UserWalletMapper.companyAddCoin", pd)>0;
    }

    @Override
    public boolean withDrawalSub(PageData pd) throws Exception {
        return (Integer)dao.update("UserWalletMapper.withDrawalSub", pd)>0;
    }
}
