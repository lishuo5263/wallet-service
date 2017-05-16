package com.ecochain.ledger.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ecochain.ledger.dao.DaoSupport;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.service.UsersDetailsService;
import com.ecochain.ledger.util.Logger;
import com.ecochain.ledger.util.StringUtil;
import com.ecochain.ledger.web.rest.UsersWebService.Clibrary;
import com.github.pagehelper.PageHelper;

@Service("userDetailsService")
public class UsersDetailsServiceImpl implements UsersDetailsService{

    private Logger logger = Logger.getLogger(this.getClass());
    @Resource(name = "daoSupport")
    private DaoSupport dao;
    
    @Override
    public PageData getUserByAccAndPass(PageData pd, String versionNo) throws Exception {
        return (PageData) dao.findForObject("UserLoginMapper.getUserByAccAndPass", pd);
    }

    @Override
    public boolean updateLoginTimeById(PageData pd, String versionNo) throws Exception {
        return (Integer) dao.update("UserLoginMapper.updateLoginTimeById", pd)>0;
    }

    @Override
    public PageData getUserInfoByUserId(Integer user_id, String versionNo) throws Exception {
        return (PageData) dao.findForObject("UsersDetailsMapper.getUserInfoByUserId", user_id);
    }

    @Override
    public boolean findIsExist(String account, String versionNo) throws Exception {
        return (Integer)dao.findForObject("UsersDetailsMapper.findIsExist", account)>0;
    }

    @Override
    @Transactional(propagation =Propagation.REQUIRED)
    public boolean addUser(PageData pd, String versionNo) throws Exception {
        //添加用户详细表
        dao.save("UsersDetailsMapper.insertSelective", pd);
        //添加用户登陆表
        dao.save("UserLoginMapper.insertSelective", pd);
        
        StringBuffer buf = new StringBuffer();
        while(buf.length()<32){
            buf.append(pd.get("user_id")+pd.getString("account"));
        }
        String seedsStr = buf.substring(0, 32)+"\0";
        logger.info("seeds="+seedsStr);
        byte[] seedsByte = seedsStr.getBytes();
        byte[] pubkeyByte = new byte[64];
        byte[] prikeyByte = new byte[64];
        byte[] errmsgByte = new byte[64];
        
        String pubkey = "";
        String prikey = "";
        String errmsg = "";
        logger.info("=================掉动态库开始========================");
        Clibrary.INSTANCE.InitCrypt();
        int getPriPubKey = Clibrary.INSTANCE.GetPriPubKey(seedsByte,pubkeyByte,prikeyByte,errmsgByte);
        pubkey = new String(pubkeyByte);
        prikey = new String(prikeyByte);
        errmsg = new String(errmsgByte);
        Clibrary.INSTANCE.StopCrypt();
        System.out.println("pubkey="+pubkey+",prikey="+prikey+",errmsg="+errmsg);
        logger.info("=================掉动态库结束=============返回值getPriPubKey="+getPriPubKey);
        errmsg = StringUtil.isNotEmpty(errmsg)?errmsg.trim():errmsg;
        if(getPriPubKey==0&&"success".equals(errmsg)){
            PageData tpd = new PageData();
            tpd.put("seeds", seedsStr);
            tpd.put("public_key", pubkey.trim());
            tpd.put("address", pubkey.trim());
            tpd.put("id", pd.get("user_id"));
            logger.info("调动态库tpd value="+tpd.toString());
            updateByIdSelective(tpd, versionNo);
        }else{
            throw new RuntimeException("===================掉动态库失败================================");
        }
        
        return true;
    }

    @Override
    public Integer getUserCount() throws Exception {
        return (Integer)dao.findForObject("UsersDetailsMapper.getUserCount", null);
    }

    @Override
    public PageData getUserInfoByAccount(String account, String versionNo) throws Exception {
        return (PageData) dao.findForObject("UsersDetailsMapper.getUserInfoByAccount", account);
    }
    
    public PageData findAcceptInfo(PageData pd) throws Exception {
        return (PageData) dao.findForObject("UserLoginMapper.findAcceptInfo", pd);
    }

    @Override
    public boolean updateByIdSelective(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UsersDetailsMapper.updateByIdSelective", pd)>0;
    }

    @Override
    public boolean insertSelective(PageData pd, String versionNo) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean modifyPhone(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("UsersDetailsMapper.modifyPhone", pd)>0;
    }

    @Override
    public List<PageData> listPageUsers(PageData pd) throws Exception {
        if (pd.getPage() != null && pd.getRows() != null) {
            PageHelper.startPage(pd.getPage(), pd.getRows());
        }
        List<PageData> list = (List<PageData>)dao.findForList("UsersDetailsMapper.listPageUsers", pd);
        return list;
    }
}
