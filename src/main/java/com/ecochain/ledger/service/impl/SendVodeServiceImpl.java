package com.ecochain.ledger.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ecochain.ledger.dao.DaoSupport;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.service.SendVodeService;

@Component("sendVodeService")
public class SendVodeServiceImpl implements SendVodeService {

    @Resource(name = "daoSupport")
    private DaoSupport dao;
    @Override
    public String findVcodeByPhone(String phone, String versionNo) throws Exception {
        boolean isSend = isSendBy30Minute(phone,versionNo);
        if(!isSend){
            return null;
        }
        return (String)dao.findForObject("SendVcodeMapper.findVcodeByPhone", phone);
    }

    @Override
    public Integer findCountByMinute(String phone, String versionNo) throws Exception {
        return (Integer)dao.findForObject("SendVcodeMapper.findCountByMinute", phone);
    }

    @Override
    public Integer findCountByDay(String phone, String versionNo) throws Exception {
        return (Integer)dao.findForObject("SendVcodeMapper.findCountByDay", phone);
    }

    @Override
    public boolean addVcode(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.save("SendVcodeMapper.addVcode", pd)>0;
    }

    @Override
    public boolean isSendBy30Minute(String phone, String versionNo) throws Exception {
        return (Integer)dao.findForObject("SendVcodeMapper.isSendBy30Minute", phone)>0;
    }

    @Override
    public Integer findCountBy30Minute(String phone, String versionNo) throws Exception {
        return (Integer)dao.findForObject("SendVcodeMapper.findCountBy30Minute", phone);
    }

    @Override
    public boolean smsCountIsOver(PageData pd, String versionNo) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

}
