package com.ecochain.ledger.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ecochain.ledger.dao.DaoSupport;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.service.PayLogService;

@Component("payLogService")
public class PayLogServiceImpl implements PayLogService {

    @Resource(name = "daoSupport")
    private DaoSupport dao;
    @Override
    public boolean deleteById(Integer id, String versionNo) throws Exception {
        return (Integer)dao.delete("PayLogMapper.deleteById", id)>0;
    }

    @Override
    public boolean insert(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.save("PayLogMapper.insert", pd)>0;
    }

    @Override
    public boolean insertSelective(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.save("PayLogMapper.insertSelective", pd)>0;
    }

    @Override
    public PageData selectById(Integer id, String versionNo) throws Exception {
        return (PageData)dao.findForObject("PayLogMapper.selectById", id);
    }

    @Override
    public boolean updateByIdSelective(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("PayLogMapper.updateByIdSelective", pd)>0;
    }

    @Override
    public boolean updateById(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("PayLogMapper.updateById", pd)>0;
    }

    @Override
    public boolean isHasPayLog(String pay_no, String versionNo) throws Exception {
        return (Integer)dao.findForObject("PayLogMapper.isHasPayLog", pay_no)>0;
    }

}
