package com.ecochain.ledger.service.impl;

import com.ecochain.ledger.dao.DaoSupport;
import com.ecochain.ledger.mapper.UserCradMapper;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.model.UserCrad;
import com.ecochain.ledger.service.UserCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by Lisandro on 2017/7/18.
 */
@Component("userCardService")
public class UserCardServiceImpl implements UserCardService {

    private final Logger logger = LoggerFactory.getLogger(UserCardServiceImpl.class);

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Resource
    private UserCradMapper userCradMapper;

    @Override
    public int addBankCard(UserCrad userCrad) {
        return this.userCradMapper.insert(userCrad);
    }

    @Override
    public int findCardByCardNo(PageData pd)throws  Exception {
        if(Integer.valueOf(pd.getString("isDefault"))!= 0){
            return (Integer)dao.findForObject("com.ecochain.ledger.mapper.UserCradMapper.findCardByCardNo", pd);
        }else{
            return (Integer)dao.findForObject("com.ecochain.ledger.mapper.UserCradMapper.findCardByCardNo2", pd);
        }
    }
}
