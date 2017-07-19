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
    
}