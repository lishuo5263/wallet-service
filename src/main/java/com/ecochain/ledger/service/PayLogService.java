package com.ecochain.ledger.service;

import com.ecochain.ledger.model.PageData;

public interface PayLogService {
    
    boolean deleteById(Integer id,String versionNo) throws Exception;

    boolean insert(PageData pd,String versionNo) throws Exception;

    boolean insertSelective(PageData pd,String versionNo) throws Exception;

    PageData selectById(Integer id,String versionNo) throws Exception;

    boolean updateByIdSelective(PageData pd,String versionNo) throws Exception;

    boolean updateById(PageData pd,String versionNo) throws Exception;
    /**
     * @describe:判断支付日志是否插入
     * @author: zhangchunming
     * @date: 2016年12月22日上午11:03:40
     * @param pay_no
     * @param versionNo
     * @throws Exception
     * @return: boolean
     */
    boolean isHasPayLog(String pay_no,String versionNo) throws Exception;
}
