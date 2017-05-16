package com.ecochain.ledger.service;


public interface SmsBlackService {
    /**
     * @describe:查询手机号是否在黑名单
     * @author: zhangchunming
     * @date: 2016年10月22日下午3:14:02
     * @param phone
     * @param versionNo
     * @throws Exception
     * @return: int
     */
    public boolean isBlackPhone(String phone,String versionNo) throws Exception;
}
