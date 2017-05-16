package com.ecochain.ledger.service;


import com.ecochain.ledger.model.PageData;

public interface UserLoginService {
    boolean deleteById(Integer id,String versionNo) throws Exception;

    boolean insert(PageData pd,String versionNo) throws Exception;

    boolean insertSelective(PageData pd,String versionNo) throws Exception;

    PageData selectById(Integer id,String versionNo) throws Exception;

    boolean updateByIdSelective(PageData pd,String versionNo) throws Exception;

    boolean updateById(PageData pd,String versionNo) throws Exception;
    
    PageData getUserByAccount(PageData pd,String versionNo) throws Exception;
    /**
     * @describe:根据账号查询登陆信息
     * @author: zhangchunming
     * @date: 2016年12月9日上午10:25:29
     * @param account
     * @param versionNo
     * @throws Exception
     * @return: PageData
     */
    PageData getUserLoginByAccount(String account,String versionNo) throws Exception;
    /**
     * @describe:根据账号查询用户信息
     * @author: zhangchunming
     * @date: 2016年12月9日上午10:48:42
     * @param account
     * @param versionNo
     * @throws Exception
     * @return: PageData
     */
    PageData getUserInfoByAccount(String account,String versionNo) throws Exception;
    /**
     * @describe:更新登录时间
     * @author: zhangchunming
     * @date: 2016年10月22日上午11:22:56
     * @param pd
     * @param versionNo
     * @throws Exception
     * @return: boolean
     */
    boolean updateLoginTimeById(PageData pd,String versionNo) throws Exception;
    /**
     * @describe:判断用户是否存在
     * @author: zhangchunming
     * @date: 2016年10月25日下午5:09:18
     * @param account
     * @param versionNo
     * @throws Exception
     * @return: boolean
     */
    boolean findIsExist(String account,String versionNo) throws Exception;
    /**
     * @describe:更新错误次数
     * @author: zhangchunming
     * @date: 2016年10月26日下午12:52:50
     * @param id
     * @param versionNo
     * @throws Exception
     * @return: boolean
     */
    boolean updateErrorTimesById(Integer id,String versionNo) throws Exception;
    /**
     * @describe:查询错误登陆次数
     * @author: zhangchunming
     * @date: 2016年10月26日下午1:19:18
     * @param account
     * @param versionNo
     * @throws Exception
     * @return: Integer
     */
    Integer getErrorTimes(String account,String versionNo) throws Exception;
    /**
     * @describe:修改手机号
     * @author: zhangchunming
     * @date: 2016年10月26日下午2:06:44
     * @param account
     * @param password
     * @param versionNo
     * @throws Exception
     * @return: boolean
     */
    boolean modifyPwd(String account,String password,String versionNo) throws Exception;
    /**
     * @describe:修改手机号
     * @author: zhangchunming
     * @date: 2016年10月26日下午3:04:47
     * @param pd
     * @param versionNo
     * @throws Exception
     * @return: boolean
     */
    boolean modifyPhone(PageData pd,String versionNo)throws Exception;
    /**
     * @describe:修改密码
     * @author: zhangchunming
     * @date: 2016年10月26日下午3:14:51
     * @param pd
     * @param versionNo
     * @throws Exception
     * @return: boolean
     */
    boolean modifypwdByUserId(PageData pd,String versionNo)throws Exception;
    /**
     * @describe:根据user_id查询用户登陆表
     * @author: zhangchunming
     * @date: 2016年12月2日下午7:32:20
     * @param user_id
     * @param versionNo
     * @throws Exception
     * @return: PageData
     */
    PageData getUserLoginByUserId(String user_id,String versionNo)throws Exception;
    /**
     * @describe:根据用户ID查询用户信息
     * @author: zhangchunming
     * @date: 2016年12月10日下午2:57:13
     * @param user_id
     * @param versionNo
     * @throws Exception
     * @return: PageData
     */
    PageData getUserInfoByUserId(String user_id,String versionNo)throws Exception;
}