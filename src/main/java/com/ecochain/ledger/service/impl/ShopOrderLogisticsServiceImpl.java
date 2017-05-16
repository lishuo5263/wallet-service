package com.ecochain.ledger.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ecochain.ledger.dao.DaoSupport;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.service.ShopOrderLogisticsService;


@Component("shopOrderLogisticsService")
public class ShopOrderLogisticsServiceImpl implements ShopOrderLogisticsService {

    @Resource(name = "daoSupport")
    private DaoSupport dao;
    @Override
    public boolean deleteById(Integer id, String versionNo) throws Exception {
        return (Integer)dao.delete("ShopOrderLogisticsMapper.deleteById", id)>0;
    }

    @Override
    public boolean insert(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.save("ShopOrderLogisticsMapper.insert", pd)>0;
    }

    @Override
    public boolean insertSelective(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.save("ShopOrderLogisticsMapper.insertSelective", pd)>0;
    }

    @Override
    public PageData selectById(Integer id, String versionNo) throws Exception {
        return (PageData)dao.findForObject("ShopOrderLogisticsMapper.selectById", id);
    }

    @Override
    public boolean updateByIdSelective(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("ShopOrderLogisticsMapper.updateByIdSelective", pd)>0;
    }

    @Override
    public boolean updateById(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("ShopOrderLogisticsMapper.updateById", pd)>0;
    }

}
