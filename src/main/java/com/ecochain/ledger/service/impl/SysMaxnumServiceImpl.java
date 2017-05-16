package com.ecochain.ledger.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ecochain.ledger.dao.DaoSupport;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.service.SysMaxnumService;

@Component("sysMaxnumService")
public class SysMaxnumServiceImpl implements SysMaxnumService {

    @Resource(name = "daoSupport")
    private DaoSupport dao;
    @Override
    public Long findMaxNo(String notype, String versionNo) throws Exception {//此处需要加锁，防止高并发读取脏数据
        Long tCode = null;
        boolean sysmax = false;
        int  count = 0 ; 
        try {
            while(!sysmax){
                PageData tSysMaxnum =  (PageData)dao.findForObject("SysMaxnumMapper.findMaxnum", notype);
                if(tSysMaxnum==null){
                    return null;
                }
                tCode =  (Long)tSysMaxnum.get("code");
                tSysMaxnum.put("code", tCode+1);
                sysmax = (Integer)dao.update("SysMaxnumMapper.modifyMaxnum", tSysMaxnum)>0;
                ++count; 
                if(count==3){
                    break;
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }  
        return tCode;
    }

    @Override
    public boolean modifyMaxNo(PageData pd, String versionNo) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

}
