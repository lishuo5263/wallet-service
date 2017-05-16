package com.ecochain.ledger.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ecochain.ledger.dao.DaoSupport;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.service.UserAddressService;
import com.ecochain.ledger.util.StringUtil;

@Component("userAddressService")
public class UserAddressServiceImpl implements UserAddressService {

    @Resource(name = "daoSupport")
    private DaoSupport dao;
    @Override
    public boolean deleteById(Integer id, String versionNo) throws Exception {
        return (Integer)dao.delete("UserAddressMapper.deleteById", id)>0;
    }

    @Override
    public boolean insert(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.save("UserAddressMapper.insert", pd)>0;
    }

    @Override
    @Transactional(propagation =Propagation.REQUIRED)
    public boolean insertSelective(PageData pd, String versionNo) throws Exception {
        if(StringUtil.isNotEmpty(pd.getString("default"))&&"1".equals(pd.getString("default"))){
            calcelDefault(pd, versionNo);
        }
        return (Integer)dao.save("UserAddressMapper.insertSelective", pd)>0;
    }

    @Override
    public PageData selectById(Integer id, String versionNo) throws Exception {
        return (PageData)dao.findForObject("UserAddressMapper.selectById", id);
    }

    @Override
    public boolean updateByIdSelective(PageData pd, String versionNo) throws Exception {
        if(StringUtil.isNotEmpty(pd.getString("default"))&&"1".equals(pd.getString("default"))){
            calcelDefault(pd, versionNo);
        }
        return (Integer)dao.update("UserAddressMapper.updateByIdSelective", pd)>0;
    }

    @Override
    public boolean updateById(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UserAddressMapper.updateById", pd)>0;
    }

    @Override
    public List<PageData> getUserAddressList(PageData pd, String versionNo) throws Exception {
        return (List<PageData>)dao.findForList("UserAddressMapper.getUserAddressList", pd);
    }

    @Override
    public boolean setDefault(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UserAddressMapper.setDefault", pd)>0;
    }

    @Override
    public boolean calcelDefault(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UserAddressMapper.calcelDefault", pd)>0;
    }

    @Override
    @Transactional(propagation =Propagation.REQUIRED)
    public boolean setDefaultAddress(PageData pd, String versionNo) throws Exception {
        //先取消默认地址
        calcelDefault(pd, versionNo);
        //再设置默认地址
        boolean setDefalut = setDefault(pd, versionNo);
        return setDefalut;
    }

    @Override
    public PageData getOneAddress(PageData pd, String versionNo) throws Exception {
        return (PageData)dao.findForObject("UserAddressMapper.getOneAddress", pd);
    }

    @Override
    public PageData getOneAddressById(String address_id, String versionNo) throws Exception {
        return (PageData)dao.findForObject("UserAddressMapper.getOneAddressById", address_id);
    }
    
}
