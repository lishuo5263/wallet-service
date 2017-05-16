package com.ecochain.ledger.service;


import com.ecochain.ledger.model.PageData;

public interface SendVodeService {
    /**
     * @describe:根据手机号查询验证码
     * @author: zhangchunming
     * @date: 2016年10月22日下午2:49:18
     * @param phone
     * @param versionNo
     * @throws Exception
     * @return: String
     */
    public String findVcodeByPhone(String phone,String versionNo) throws Exception;
    /**
     * @describe:查询一分钟之内验证码发送次数
     * @author: zhangchunming
     * @date: 2016年10月22日下午2:50:00
     * @param phone
     * @param versionNo
     * @throws Exception
     * @return: Integer
     */
    public Integer findCountByMinute(String phone,String versionNo) throws Exception;
    /**
     * @describe:查询一天之内验证码发送次数
     * @author: zhangchunming
     * @date: 2016年10月22日下午2:50:38
     * @param phone
     * @param versionNo
     * @throws Exception
     * @return: Integer
     */
    public Integer findCountByDay(String phone,String versionNo) throws Exception;
    /**
     * @describe:发送验证码
     * @author: zhangchunming
     * @date: 2016年10月22日下午2:52:10
     * @param pd
     * @param versionNo
     * @throws Exception
     * @return: boolean
     */
    public boolean addVcode(PageData pd,String versionNo) throws Exception;
    /**
     * @describe:查询30分钟内是否发送验证码
     * @author: zhangchunming
     * @date: 2016年10月22日下午5:06:53
     * @param phone
     * @param versionNo
     * @throws Exception
     * @return: boolean
     */
    public boolean isSendBy30Minute(String phone,String versionNo) throws Exception;
    /**
     * @describe:查询30分钟内验证码发送次数
     * @author: zhangchunming
     * @date: 2016年12月7日下午4:26:34
     * @param phone
     * @param versionNo
     * @throws Exception
     * @return: Integer
     */
	public Integer findCountBy30Minute(String phone,String versionNo) throws Exception;
	
	/**
	 * @describe:查询半小时或者当天短信是否超过限制数量
	 * @author: zhangchunming
	 * @date: 2017年2月10日上午10:17:43
	 * @param phone
	 * @param versionNo
	 * @throws Exception
	 * @return: Integer
	 */
	public boolean smsCountIsOver(PageData pd,String versionNo) throws Exception;
}
