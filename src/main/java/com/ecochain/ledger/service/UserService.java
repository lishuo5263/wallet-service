package com.ecochain.ledger.service;


import java.util.Map;

import com.ecochain.ledger.model.PageData;


public interface UserService {
    /**
     * @describe:用户注册
     * @author: zhangchunming
     * @date: 2016年10月21日下午7:48:02
     * @param pd
     * @param versionNo
     * @throws Exception
     * @return: boolean
     */
    public boolean addUser(PageData pd,String versionNo) throws Exception;
    
    /**
     * 用户登录
     * @param pd
     * @return Map
     * @throws Exception
     */
    public Map<String,Object> login(PageData pd,String versionNo) throws Exception;
    /**
     * 查询锁定登录标识符
     * @param pd
     * @param versionNo
     */
    public PageData findLockLoginStatus(PageData pd,String versionNo) throws Exception;
    
    /**
     * 查询锁定短信发送标识符
     * @param pd
     */
    public PageData findLockSmsStatus(PageData pd,String versionNo) throws Exception;
    /**
     * 根据手机号查询用户
     * @param pd
     */
    public PageData findbyPhone(PageData pd,String versionNo) throws Exception;
    
    /**
     * 根据用户编码查询用户
     * @param pd
     */
    public PageData findUserByUserCode(PageData pd,String versionNo) throws Exception;
    
    /**
     * 根据手机号查询用户
     * @param pd
     */
    public boolean findIsExist(PageData pd,String versionNo) throws Exception;
    
    /**
     * 根据手机号查询是否存在上级会员
     * @param pd
     */
    public boolean findIsExistUpFriendship(PageData pd,String versionNo) throws Exception;
    
    /**
     * 根据被推介人user_id查询上级会员
     * @param recomuser_id
     */
    public PageData findUpFriendship(String recomuser_id,String versionNo) throws Exception;
    
    /**
     * 根据手机号查询上级会员
     * @param pd
     */
    public PageData findMaxFriendship(PageData pd,String versionNo) throws Exception;
    
    /**
     * 新建用户关系
     * @param pd
     * 
     */
    public boolean addUserFriendShip(PageData pd,String versionNo) throws Exception;
    
    /**
     * 实名用户
     * @param pd
     */
    public boolean realUser(PageData pd,String versionNo) throws Exception;
    
    /**
     * 修改密码
     * @param pd
     */
    public boolean modifyPwd(PageData pd,String versionNo) throws Exception;
    
    /**
     * 修改密码
     * @param pd
     */
    public boolean modifypwdByUserCode(PageData pd,String versionNo) throws Exception;
    
    /**
     * 修改手机
     * @param pd
     */
    public boolean modifyPhone(PageData pd,String versionNo) throws Exception;
    
    /**
     * 修改用户资料
     * @param pd
     */
    public boolean modifyUserDetail(PageData pd,String versionNo) throws Exception;
        
    /**
     * 修改锁定登录标识符
     * @param pd
     */
    public boolean modifyLockLoginStatus(PageData pd,String versionNo) throws Exception;
    
    /**
     * 修改锁定短信发送标识符
     * @param pd
     */
    public boolean modifyLockSmsStatus(PageData pd,String versionNo) throws Exception;
    /**
     * @describe:会员充值
     * @author: zhangchunming
     * @date: 2016年11月19日下午8:21:21
     * @param pd
     * @param versionNo
     * @throws Exception
     * @return: boolean
     */
    public boolean userRecharge(PageData pd,String versionNo) throws Exception;
    /**
     * @describe:绑定手机号
     * @author: zhangchunming
     * @date: 2016年12月5日下午3:43:36
     * @param pd
     * @param versionNo
     * @throws Exception
     * @return: boolean
     */
    public boolean bindPhone(PageData pd,String versionNo) throws Exception;
    /**
     * @describe:根据账号查询手机号
     * @author: zhangchunming
     * @date: 2016年12月5日下午4:24:10
     * @param account
     * @param versionNo
     * @throws Exception
     * @return: boolean
     */
    public PageData getPhoneByAccount(String account,String versionNo) throws Exception;
}
