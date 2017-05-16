package com.ecochain.ledger.service;

import java.util.List;

import com.ecochain.ledger.model.Page;
import com.ecochain.ledger.model.PageData;

public interface PayOrderService {

    boolean deleteById(Integer id,String versionNo) throws Exception;

    boolean insert(PageData pd,String versionNo) throws Exception;

    boolean insertSelective(PageData pd,String versionNo) throws Exception;

    PageData selectById(Integer id,String versionNo) throws Exception;
    /**
     * @describe:根据支付号查询支付工单
     * @author: zhangchunming
     * @date: 2016年11月23日下午5:46:18
     * @param PayNo
     * @param versionNo
     * @throws Exception
     * @return: PageData
     */
    PageData selectByPayNo(String PayNo,String versionNo) throws Exception;

    boolean updateByIdSelective(PageData pd,String versionNo) throws Exception;

    boolean updateById(PageData pd,String versionNo) throws Exception;
    /**
     * @describe:添加支付记录
     * @author: zhangchunming
     * @date: 2016年11月1日下午5:21:13
     * @param pd
     * @param versionNo
     * @throws Exception
     * @return: boolean
     */
    boolean addPayOrder(PageData pd,String versionNo) throws Exception;
    /**
     * @describe:根据支付号修改订单状态
     * @author: zhangchunming
     * @date: 2016年11月2日下午7:08:05
     * @param pd
     * @param versionNo
     * @throws Exception
     * @return: boolean
     */
    boolean updateStatusByPayNo(PageData pd,String versionNo) throws Exception;
    /**
     * @describe:根据hash修改支付工单状态
     * @author: zhangchunming
     * @date: 2017年3月20日下午6:13:02
     * @param pd
     * @param versionNo
     * @throws Exception
     * @return: boolean
     */
    boolean updateStatusByHash(PageData pd,String versionNo) throws Exception;
    /**
     * @describe:查询大礼包列表
     * @author: zhangchunming
     * @date: 2016年12月2日下午3:16:50
     * @param page
     * @param versionNo
     * @throws Exception
     * @return: List<PageData>
     */
    List<PageData> listPageBigGit(Page page,String versionNo) throws Exception;
    /**
     * @describe:分页查询提现记录
     * @author: zhangchunming
     * @date: 2016年12月17日下午7:58:10
     * @param page
     * @param versionNo
     * @throws Exception
     * @return: List<PageData>
     */
    PageData listPageWithDrawal(Page page,String versionNo) throws Exception;
    /**
     * @describe:分页查询转三界石记录
     * @author: zhangchunming
     * @date: 2016年12月20日下午5:30:16
     * @param page
     * @param versionNo
     * @throws Exception
     * @return: PageData
     */
    PageData listPageTransferSJS(Page page,String versionNo) throws Exception;
    /**
     * @describe:定时更新提现完成订单
     * @author: zhangchunming
     * @date: 2016年12月21日下午2:18:08
     * @throws Exception
     * @return: boolean
     */
    boolean updateWithDrawalSuccess()throws Exception;
    /**
     * @describe:申请提现，插支付工单，扣减账户余额到冻结余额
     * @author: zhangchunming
     * @date: 2016年12月21日下午8:19:04
     * @param pd
     * @throws Exception
     * @return: boolean
     */
    boolean applyWithDrawal(PageData pd,String versionNo)throws Exception;
    /**
     * @describe:判断是否锁定
     * @author: zhangchunming
     * @date: 2016年12月22日上午11:42:53
     * @param pay_no
     * @param versionNo
     * @throws Exception
     * @return: boolean
     */
    boolean isLockPayOrder(String pay_no,String versionNo)throws Exception;
    /**
     * @describe:锁定支付工单
     * @author: zhangchunming
     * @date: 2016年12月22日上午11:43:32
     * @param pay_no
     * @param versionNo
     * @throws Exception
     * @return: boolean
     */
    boolean lockPayOrder(String pay_no,String versionNo)throws Exception;
    /**
     * @describe:解锁支付工单
     * @author: zhangchunming
     * @date: 2016年12月22日上午11:54:37
     * @param pay_no
     * @param versionNo
     * @throws Exception
     * @return: boolean
     */
    boolean unlockPayOrder(String pay_no,String versionNo)throws Exception;
    /**
     * @describe:提现成功或者拒绝时设置确认时间
     * @author: zhangchunming
     * @date: 2016年12月22日下午5:14:33
     * @throws Exception
     * @return: boolean
     */
    boolean updateConfirmTime()throws Exception;
    /**
     * @describe:提现时判断是否存在提现中的订单，若有则不允许提现
     * @author: zhangchunming
     * @date: 2016年12月23日下午11:47:13
     * @param user_id
     * @throws Exception
     * @return: boolean
     */
    boolean isHasWithDrawaling(String user_id)throws Exception;
    
    /**
     * @describe:查询充值记录
     * @author: zhangchunming
     * @date: 2017年1月16日上午10:38:09
     * @param page
     * @param versionNo
     * @throws Exception
     * @return: PageData
     */
    PageData listPageRecharge(Page page, String versionNo) throws Exception;
    /**
     * @describe:
     * @author: zhangchunming
     * @date: 2017年1月21日上午11:28:48
     * @param versionNo
     * @throws Exception
     * @return: List<PageData>
     */
    List<PageData> getWithDrawalSms(String versionNo)throws Exception;
}
