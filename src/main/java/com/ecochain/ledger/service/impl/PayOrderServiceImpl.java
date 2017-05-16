package com.ecochain.ledger.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ecochain.ledger.constants.Constant;
import com.ecochain.ledger.dao.DaoSupport;
import com.ecochain.ledger.model.Page;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.service.AccDetailService;
import com.ecochain.ledger.service.PayLogService;
import com.ecochain.ledger.service.PayOrderService;
import com.ecochain.ledger.service.SysGenCodeService;
import com.ecochain.ledger.service.UserWalletService;
import com.ecochain.ledger.util.Logger;
import com.ecochain.ledger.util.sms.SMSUtil;

@Component("payOrderService")
public class PayOrderServiceImpl implements PayOrderService {

    private Logger logger = Logger.getLogger(PayOrderServiceImpl.class);
    @Resource(name = "daoSupport")
    private DaoSupport dao;
    
    @Resource
    private PayLogService payLogService;
    @Resource
    private AccDetailService accDetailService;

    @Resource
    private UserWalletService userWalletService;
    @Resource
    private PayOrderService payOrderService;
    @Resource
    private SysGenCodeService sysGenCodeService;

    @Override
    public boolean deleteById(Integer id, String versionNo) throws Exception {
        return (Integer)dao.delete("PayOrderMapper.deleteById", id)>0;
    }

    @Override
    public boolean insert(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.save("PayOrderMapper.insert", pd)>0;
    }

    @Override
    public boolean insertSelective(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.save("PayOrderMapper.insertSelective", pd)>0;
    }

    @Override
    public PageData selectById(Integer id, String versionNo) throws Exception {
        return (PageData)dao.findForObject("PayOrderMapper.selectById", id);
    }

    @Override
    public boolean updateByIdSelective(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("PayOrderMapper.updateByIdSelective", pd)>0;
    }

    @Override
    public boolean updateById(PageData pd, String versionNo) throws Exception {
        return (Integer)dao.update("PayOrderMapper.updateById", pd)>0;
    }

    @Override
    @Transactional(propagation =Propagation.REQUIRED)
    public boolean addPayOrder(PageData pd, String versionNo) throws Exception {
        //创建支付订单
        boolean payOrderResult = insertSelective(pd, versionNo);
        //创建支付日志
        boolean payLogResult = payLogService.insertSelective(pd, versionNo);
        
        return (payOrderResult&&payLogResult);
    }

	@Override
	public PageData selectByPayNo(String pay_no, String versionNo) throws Exception {
		 return (PageData)dao.findForObject("PayOrderMapper.selectByPayno", pay_no);
	}

    @Override
    public boolean updateStatusByPayNo(PageData pd, String versionNo) throws Exception {
        //需添加支付日志
        return (Integer)dao.update("PayOrderMapper.updateStatusByPayNo", pd)>0;
    }

    @Override
    public List<PageData> listPageBigGit(Page page, String versionNo) throws Exception {
        return (List<PageData>)dao.findForList("PayOrderMapper.listPageBigGit", page);
    }

    @Override
    public PageData listPageWithDrawal(Page page, String versionNo) throws Exception {
        List<PageData> list = (List<PageData>)dao.findForList("PayOrderMapper.listPageWithDrawal", page);
        PageData tpd = new PageData();
        tpd.put("list", list);
        tpd.put("page", page);
        return tpd;
    }

    @Override
    public PageData listPageTransferSJS(Page page, String versionNo) throws Exception {
        List<PageData> list = (List<PageData>)dao.findForList("PayOrderMapper.listPageTransferSJS", page);
        PageData tpd = new PageData();
        tpd.put("list", list);
        tpd.put("page", page);
        return tpd;
    }

    @Override
    @Transactional(propagation =Propagation.REQUIRED)
    public boolean updateWithDrawalSuccess() throws Exception {
        //提现审核【通过】流程
        logger.info("*******************提现审核【通过】定时器更新流程******************start**********");
        boolean withDrawalSubFrozeMoneyResult = userWalletService.withDrawalSubFrozeMoney();
        logger.info("*********提现审核通过时********定时扣减冻结余额************结果withDrawalSubFrozeMoneyResult:"+withDrawalSubFrozeMoneyResult);
        boolean addWithDrawalAccDetailResult = false;
        if(withDrawalSubFrozeMoneyResult){
            addWithDrawalAccDetailResult = accDetailService.addWithDrawalAccDetail();
            logger.info("********提现审核通过时*********定时添加账户流水************结果addWithDrawalAccDetailResult:"+addWithDrawalAccDetailResult);
            if(addWithDrawalAccDetailResult){
                List<PageData> codeList =sysGenCodeService.findByGroupCode("SENDSMS_FLAG", Constant.VERSION_NO);
                String smsflag ="";
                for(PageData mapObj:codeList){
                    if("SENDSMS_FLAG".equals(mapObj.get("code_name"))){
                        smsflag = mapObj.get("code_value").toString();
                        logger.info("---------提现发送短信--------短信发送标识smsflag："+smsflag);
                    }
                }
                if("1".equals(smsflag)){
                    List<PageData> smsList = this.getWithDrawalSms(Constant.VERSION_NO);
                    for(PageData sms:smsList){
                        SMSUtil.sendSMS_ChinaNet1(sms.getString("mobile_phone"), sms.getString("sms"), SMSUtil.vcode_productid);
                    }
                }
                
                boolean updateConfirmTime = payOrderService.updateConfirmTime();
                logger.info("********提现审核通过时*********定时更新支付工单确认时间************结果updateConfirmTime:"+updateConfirmTime);
            }
        }
        logger.info("**********提现审核通过时*******定时更新总结果(withDrawalSubFrozeMoneyResult&&addWithDrawalAccDetailResult):"+(withDrawalSubFrozeMoneyResult&&addWithDrawalAccDetailResult));
        logger.info("*******************提现审核【通过】定时器更新流程******************end**********");

        //提现审核【拒绝】流程
        logger.info("*******************提现审核【拒绝】定时器更新流程******************start**********");
        boolean withDrawalAddMoneyResult = userWalletService.withDrawalAddMoney();
        logger.info("*********提现审核【拒绝】时********定时回滚账户余额************结果withDrawalAddMoneyResult:"+withDrawalAddMoneyResult);
        logger.info("*******************提现审核【拒绝】定时器更新流程******************end**********");
        return true;
    }

    @Override
    @Transactional(propagation =Propagation.REQUIRED)
    public boolean applyWithDrawal(PageData pd,String versionNo) throws Exception {
        boolean addPayOrderResult = this.insertSelective(pd, Constant.VERSION_NO);
        boolean withDrawalSubMoney = false;
        if(addPayOrderResult){
            PageData userWallet = new PageData();
            userWallet.put("user_id", String.valueOf(pd.get("user_id")));
            userWallet.put("money", pd.getString("money"));
            withDrawalSubMoney = userWalletService.withDrawalSubMoney(userWallet);
        }
        logger.info("************申请提现*********总结果(addPayOrderResult&&withDrawalSubMoney):"+(addPayOrderResult&&withDrawalSubMoney));
        return (addPayOrderResult&&withDrawalSubMoney);
    }

    @Override
    public boolean isLockPayOrder(String pay_no, String versionNo) throws Exception {
        return (Integer)dao.findForObject("PayOrderMapper.isLockPayOrder", pay_no)>0;
    }

    @Override
    public boolean lockPayOrder(String pay_no, String versionNo) throws Exception {
        return (Integer)dao.update("PayOrderMapper.lockPayOrder", pay_no)>0;
    }

    @Override
    public boolean unlockPayOrder(String pay_no, String versionNo) throws Exception {
        return (Integer)dao.update("PayOrderMapper.unlockPayOrder", pay_no)>0;
    }

    @Override
    public boolean updateConfirmTime() throws Exception {
        return (Integer)dao.update("PayOrderMapper.updateConfirmTime", null)>0;
    }

    @Override
    public boolean isHasWithDrawaling(String user_id) throws Exception {
        return (Integer)dao.findForObject("PayOrderMapper.isHasWithDrawaling", user_id)>0;
    }

    @Override
    public PageData listPageRecharge(Page page, String versionNo) throws Exception {
        List<PageData> list = (List<PageData>)dao.findForList("PayOrderMapper.listPageRecharge", page);
        PageData tpd = new PageData();
        tpd.put("list", list);
        tpd.put("page", page);
        return tpd;
    }

    @Override
    public List<PageData> getWithDrawalSms(String versionNo) throws Exception {
        return (List<PageData>)dao.findForList("PayOrderMapper.getWithDrawalSms", versionNo);
    }

    @Override
    public boolean updateStatusByHash(PageData pd, String versionNo) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }
}
