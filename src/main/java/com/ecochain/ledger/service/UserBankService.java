package com.ecochain.ledger.service;

import java.util.List;

import com.ecochain.ledger.model.PageData;


public interface UserBankService {
    boolean deleteById(Integer id) throws Exception;

    boolean insert(PageData pd) throws Exception;

    boolean insertSelective(PageData pd) throws Exception;

    boolean updateByIdSelective(PageData pd) throws Exception;

    boolean updateById(PageData pd) throws Exception;
    
    List<PageData> getBankList(Integer user_id) throws Exception;
    /**
     * @describe:设置默认卡
     * @author: zhangchunming
     * @date: 2017年7月19日下午7:19:31
     * @param pd
     * @throws Exception
     * @return: boolean
     */
    boolean setDefault(PageData pd)throws Exception;
    /**
     * @describe:取消默认卡
     * @author: zhangchunming
     * @date: 2017年7月19日下午7:19:47
     * @param user_id
     * @throws Exception
     * @return: boolean
     */
    boolean cancelDefault(Integer user_id)throws Exception;
    /**
     * @describe:判断卡号是否已添加
     * @author: zhangchunming
     * @date: 2017年7月19日下午7:43:24
     * @param pd
     * @throws Exception
     * @return: boolean
     */
    boolean isExist(PageData pd)throws Exception;

    
}