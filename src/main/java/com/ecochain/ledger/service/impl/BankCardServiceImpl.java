package com.ecochain.ledger.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ecochain.ledger.dao.DaoSupport;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.service.BankCardService;
@Component("bankCardService")
public class BankCardServiceImpl implements BankCardService {

    @Resource(name = "daoSupport")
    private DaoSupport dao;
    
    @Override
    public boolean deleteById(Integer id) throws Exception {
        return (Integer)dao.delete("BankCardMapper.deleteById", id)>0;
    }

    @Override
    public boolean insert(PageData pd) throws Exception {
        return (Integer)dao.save("BankCardMapper.insert", pd)>0;
    }

    @Override
    public boolean insertSelective(PageData pd) throws Exception {
        return (Integer)dao.save("BankCardMapper.insertSelective", pd)>0;
    }

    @Override
    public boolean updateByIdSelective(PageData pd) throws Exception {
        return (Integer)dao.update("BankCardMapper.updateByIdSelective", pd)>0;
    }

    @Override
    public boolean updateById(PageData pd) throws Exception {
        return (Integer)dao.update("BankCardMapper.updateById", pd)>0;
    }

    @Override
    public List<PageData> getBankCardList() throws Exception {
        return (List<PageData>)dao.findForList("BankCardMapper.getBankCardList", null);
    }

    @Override
    public boolean isExist(String bank_name) throws Exception {
        return (Integer)dao.findForObject("BankCardMapper.isExist", bank_name)>0;
    }

}
