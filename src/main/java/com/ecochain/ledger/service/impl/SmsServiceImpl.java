package com.ecochain.ledger.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ecochain.ledger.dao.DaoSupport;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.service.SmsService;

@Component("smsService")
public class SmsServiceImpl implements SmsService {

    @Resource(name = "daoSupport")
    private DaoSupport dao;
    @Override
    public int findSendCntByPhone(String phone, long second, String versionNo) throws Exception {
        return 0;
    }

    @Override
    public String findSendsmsDetail(String phone, String versionNo) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean addSendsmsDetail(PageData smsDetail, String versionNo) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isBlackPhone(String phone, String versionNo) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

}
