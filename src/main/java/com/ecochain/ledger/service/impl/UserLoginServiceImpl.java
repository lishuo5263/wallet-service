package com.ecochain.ledger.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ecochain.ledger.dao.DaoSupport;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.service.UserLoginService;

@Component("userLoginService")
public class UserLoginServiceImpl implements UserLoginService {
    
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Override
    public boolean deleteById(Integer id, String versionNo) throws Exception {
        return (Integer)dao.delete("UserLoginMapper.deleteById", id)>0;
    }

    @Override
    public boolean insert(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.save("UserLoginMapper.insert", pd)>0;
    }

    @Override
    public boolean insertSelective(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.save("UserLoginMapper.insertSelective", pd)>0;
    }

    @Override
    public PageData selectById(Integer id, String versionNo) throws Exception {
        return (PageData)dao.findForObject("UserLoginMapper.selectById", id);
    }

    @Override
    public boolean updateByIdSelective(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UserLoginMapper.updateByIdSelective", pd)>0;
    }

    @Override
    public boolean updateById(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UserLoginMapper.updateById", pd)>0;
    }

    @Override
    public PageData getUserByAccount(PageData pd, String versionNo) throws Exception {
        return (PageData)dao.findForObject("UserLoginMapper.getUserByAccount", pd);
    }

    @Override
    public boolean updateLoginTimeById(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UserLoginMapper.updateLoginTimeById", pd)>0;
    }

    @Override
    public boolean findIsExist(String account, String versionNo) throws Exception {
        return (Integer)dao.findForObject("UserLoginMapper.findIsExist", account)>0;
    }

    @Override
    public boolean updateErrorTimesById(Integer id, String versionNo) throws Exception {
        return (Integer)dao.update("UserLoginMapper.updateErrorTimesById", id)>0;
    }

    @Override
    public Integer getErrorTimes(String account, String versionNo) throws Exception {
        return (Integer)dao.findForObject("UserLoginMapper.getErrorTimes", account);
    }

    @Override
    public boolean modifyPwd(String account, String password, String versionNo) throws Exception {
        PageData pd  = new PageData();
        pd.put("account", account);
        pd.put("password", password);
        return (Integer)dao.update("UserLoginMapper.modifyPwd", pd)>0;
    }

    @Override
    public boolean modifyPhone(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UserLoginMapper.modifyPhone", pd)>0;
    }

    @Override
    public boolean modifypwdByUserId(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UserLoginMapper.modifypwdByUserId", pd)>0;
    }

    @Override
    public PageData getUserLoginByUserId(String user_id, String versionNo) throws Exception {
        return (PageData)dao.findForObject("UserLoginMapper.getUserLoginByUserId", user_id);
    }

    @Override
    public PageData getUserLoginByAccount(String account, String versionNo) throws Exception {
        return (PageData)dao.findForObject("UserLoginMapper.getUserLoginByAccount", account);
    }

    @Override
    public PageData getUserInfoByAccount(String account, String versionNo) throws Exception {
        return (PageData)dao.findForObject("UserLoginMapper.getUserInfoByAccount", account);
    }

    @Override
    public PageData getUserInfoByUserId(String user_id, String versionNo) throws Exception {
        return (PageData)dao.findForObject("UserLoginMapper.getUserInfoByUserId", user_id);
    }
    

}
