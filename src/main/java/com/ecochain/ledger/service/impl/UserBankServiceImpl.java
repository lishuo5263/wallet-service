package com.ecochain.ledger.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ecochain.ledger.dao.DaoSupport;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.service.UserBankService;
import com.ecochain.ledger.util.Logger;
@Component("userBankService")
public class UserBankServiceImpl implements UserBankService {

//    private final Logger logger = Logger.getLogger(UserBankServiceImpl.class);
    
    @Resource(name = "daoSupport")
    private DaoSupport dao;
    
    @Override
    public boolean deleteById(Integer id) throws Exception {
        return (Integer)dao.delete("UserBankMapper.deleteById", id)>0;
    }

    @Override
    public boolean insert(PageData pd) throws Exception {
        return (Integer)dao.save("UserBankMapper.insert", pd)>0;
    }

    @Override
    public boolean insertSelective(PageData pd) throws Exception {
        return (Integer)dao.save("UserBankMapper.insertSelective", pd)>0;
    }

    @Override
    public boolean updateByIdSelective(PageData pd) throws Exception {
        return (Integer)dao.update("UserBankMapper.updateByIdSelective", pd)>0;
    }

    @Override
    public boolean updateById(PageData pd) throws Exception {
        return (Integer)dao.update("UserBankMapper.updateById", pd)>0;
    }

    @Override
    public List<PageData> getBankList(Integer user_id) throws Exception {
        return (List<PageData>)dao.findForList("UserBankMapper.getBankList", user_id);
    }

    @Override
    @Transactional(propagation =Propagation.REQUIRED)
    public boolean setDefault(PageData pd) throws Exception {
        boolean cancelDefault = cancelDefault(Integer.valueOf(pd.getString("user_id")));
        if(cancelDefault){
            return (Integer)dao.update("UserBankMapper.setDefault", pd)>0;
        }
        return false;
    }

    @Override
    public boolean cancelDefault(Integer user_id) throws Exception {
        return (Integer)dao.update("UserBankMapper.cancelDefault", user_id)>0;
    }

    @Override
    public boolean isExist(PageData pd) throws Exception {
        return (Integer)dao.findForObject("UserBankMapper.isExist", pd)>0;
    }

}
