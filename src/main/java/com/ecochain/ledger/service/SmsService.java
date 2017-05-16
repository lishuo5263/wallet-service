package com.ecochain.ledger.service;


import com.ecochain.ledger.model.PageData;

public interface SmsService {

	
	/**
	 * 查询用户短信发送次数信息
	 * @param userCode
	 */
	public int findSendCntByPhone(String phone,long second,String versionNo) throws Exception;
	/**
	 * 查询短信内容
	 * @param Phone
	 * @param sysCode
	 */
	public String findSendsmsDetail(String  phone,String versionNo) throws Exception;
	/**
	 * 添加发送验证码短信
	 * @param smsDetail
	 */
	public boolean addSendsmsDetail(PageData smsDetail,String versionNo) throws Exception;
	
    /**
     * @describe:
     * @author: zhangchunming
     * @date: 2016年10月22日下午1:45:39
     * @param phone
     * @param versionNo
     * @throws Exception
     * @return: int
     */
    public boolean isBlackPhone(String phone,String versionNo) throws Exception;
	
}
