package com.ecochain.ledger.service.impl;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ecochain.ledger.constants.CodeConstant;
import com.ecochain.ledger.constants.Constant;
import com.ecochain.ledger.dao.DaoSupport;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.service.AccDetailService;
import com.ecochain.ledger.service.PayOrderService;
import com.ecochain.ledger.service.SysGenCodeService;
import com.ecochain.ledger.service.UsersDetailsService;
import com.ecochain.ledger.service.UserLoginService;
import com.ecochain.ledger.service.UserService;
import com.ecochain.ledger.service.UserWalletService;
import com.ecochain.ledger.util.DateUtil;
import com.ecochain.ledger.util.Logger;
import com.ecochain.ledger.util.MD5Util;

@Component("userService")
public class UserServiceImpl implements UserService {

    private final Logger logger = Logger.getLogger(UserServiceImpl.class);
    @Resource(name = "daoSupport")
    private DaoSupport dao;
    
    @Resource(name = "userLoginService")
    private UserLoginService userLoginService;
    
    @Resource(name = "userDetailsService")
    private UsersDetailsService userDetailsService;
    
    @Resource(name = "userWalletService")
    private UserWalletService userWalletService;
    
    @Autowired
    private SysGenCodeService sysGencodeService;
    
    @Autowired
    private PayOrderService payOrderService;
    
    @Autowired
    private AccDetailService accDetailService;
    
    
    @Override
    @Transactional(propagation =Propagation.REQUIRED)
    public boolean addUser(PageData pd, String versionNo) throws Exception {
        logger.info("创建用户开始************************start******************************");
        logger.info("插入用户详细表---------------users_details-----------------");
        //添加用户详细
        userDetailsService.insertSelective(pd, Constant.VERSION_NO);
        logger.info("插入用户登陆表---------------user_login-----------------");
        //添加用户登陆
        userLoginService.insertSelective(pd, Constant.VERSION_NO);
        logger.info("插入用户钱包表---------------user_wallet-----------------");
        //添加用户钱包账户
        userWalletService.insertSelective(pd, Constant.VERSION_NO);
        
        logger.info("创建用户结束**************************end**************************");
        return true;
    }

    @Override
    @Transactional(propagation =Propagation.REQUIRED)
    public Map<String,Object> login(PageData pd, String versionNo) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        PageData user = userLoginService.getUserByAccount(pd, Constant.VERSION_NO);
        if (null != user) {
            if (user.getString("password").equals(MD5Util.getMd5Code(pd.getString("password")))) {
                if (String.valueOf(user.get("status")).equals(Constant.USING)) {  //1-启用 0-禁用
                        //登录成功后需要修改登录时间
                        pd.put("id", user.get("id"));
                        userLoginService.updateLoginTimeById(pd, Constant.VERSION_NO);//修改最后一次登录时间，ip，端口号
                        result.put("status", CodeConstant.SC_OK);
                        result.put(Constant.LOGIN_USER, user);
                        result.put("msg", "ok");

                } else {
                    result.put("status", CodeConstant.USER_DISABLE);
                    result.put("msg", "该用户已被禁用");
                }
            } else {
                result.put("status", CodeConstant.ERROR_PASSWORD);
                result.put("msg", "密码错误");
                logger.info("-----------密码错误，更新登陆错误次数---------------");
                pd.put("id", user.get("id"));
                userLoginService.updateErrorTimesById((Integer)pd.get("id"), Constant.VERSION_NO);
            }
        } else {
            result.put("status", CodeConstant.USERMOBILE_NOEXISTS);
            result.put("msg", "该用户不存在");
        }
        return result;
    }

    @Override
    public PageData findLockLoginStatus(PageData pd, String versionNo) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PageData findLockSmsStatus(PageData pd, String versionNo) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PageData findbyPhone(PageData pd, String versionNo) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PageData findUserByUserCode(PageData pd, String versionNo) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean findIsExist(PageData pd, String versionNo) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean findIsExistUpFriendship(PageData pd, String versionNo) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public PageData findUpFriendship(String recomuser_id, String versionNo) throws Exception {
        return  (PageData)dao.findForObject("UserFriendshipMapper.findUpFriendship", recomuser_id);
    }

    @Override
    public PageData findMaxFriendship(PageData pd, String versionNo) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean addUserFriendShip(PageData pd, String versionNo) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean realUser(PageData pd, String versionNo) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean modifyPwd(PageData pd, String versionNo) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean modifypwdByUserCode(PageData pd, String versionNo) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    @Transactional(propagation =Propagation.REQUIRED)
    public boolean modifyPhone(PageData pd, String versionNo) throws Exception {
        boolean usersDetailsResult = userDetailsService.modifyPhone(pd, versionNo);
        boolean userLoginResult = userLoginService.modifyPhone(pd, versionNo);
        return (usersDetailsResult&&userLoginResult);
    }

    @Override
    public boolean modifyUserDetail(PageData pd, String versionNo) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean modifyLockLoginStatus(PageData pd, String versionNo) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean modifyLockSmsStatus(PageData pd, String versionNo) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public boolean userRecharge(PageData pd, String versionNo) throws Exception {
        logger.info("------------会员充值------------start-------------");
        //支付成功修改订单状态
        PageData payOrder = new PageData();
        payOrder.put("pay_no", pd.getString("pay_no"));
        payOrder.put("bank_tradeno", pd.getString("bank_tradeno"));
        payOrder.put("bank_tradestatus", pd.getString("bank_tradestatus"));
        payOrder.put("confirm_time", DateUtil.getCurrDateTime());
        payOrder.put("status", "1");//交易成功
        boolean orderResult = payOrderService.updateStatusByPayNo(payOrder, versionNo);
        logger.info("------------会员充值-------------更新订单orderResult："+orderResult+",参数payOrder.toString():"+payOrder.toString());
        /*if(orderResult){
          //插入账户流水
            PageData accDetail = new PageData();
            accDetail.put("user_id", pd.get("user_id"));
            accDetail.put("acc_no", "06");
            accDetail.put("rmb_amnt", String.valueOf(pd.get("txamnt")));
            accDetail.put("caldate", DateUtil.getCurrDateTime());
            accDetail.put("cntflag", "1");
            accDetail.put("status", "4");
            accDetail.put("otherno", pd.getString("pay_no"));
            accDetail.put("other_amnt", String.valueOf(pd.get("txamnt")));
            accDetail.put("other_source", "会员充值");
            accDetail.put("operator", pd.getString("operator"));
            boolean accDetailResult = accDetailService.insertSelective(accDetail, versionNo);
            logger.info("------------会员充值-------------插入账户流水accDetailResult："+accDetailResult);
            logger.info("------------会员充值------------end-------------");
        }*/
        return orderResult;
    }

    @Override
    public boolean bindPhone(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("com.qkl.wlsc.provider.dao.UsersDetailsMapper.bindPhone", pd)>0;
    }

    @Override
    public PageData getPhoneByAccount(String account, String versionNo) throws Exception {
        return (PageData)dao.findForObject("com.qkl.wlsc.provider.dao.UsersDetailsMapper.getPhoneByAccount", account);
    }
}
