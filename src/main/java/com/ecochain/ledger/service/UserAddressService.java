package com.ecochain.ledger.service;

import java.util.List;

import com.ecochain.ledger.model.PageData;

public interface UserAddressService {
    boolean deleteById(Integer id,String versionNo) throws Exception;

    boolean insert(PageData pd,String versionNo) throws Exception;

    boolean insertSelective(PageData pd,String versionNo) throws Exception;

    PageData selectById(Integer id,String versionNo) throws Exception;

    boolean updateByIdSelective(PageData pd,String versionNo) throws Exception;

    boolean updateById(PageData pd,String versionNo) throws Exception;
    /**
     * @describe:查询地址列表
     * @author: zhangchunming
     * @date: 2016年10月28日上午10:56:31
     * @param pd
     * @param versionNo
     * @throws Exception
     * @return: List<PageData>
     */
    List<PageData> getUserAddressList(PageData pd,String versionNo) throws Exception; 
    /**
     * @describe:查询单条地址
     * @author: zhangchunming
     * @date: 2016年11月12日下午2:04:29
     * @param pd
     * @param versionNo
     * @throws Exception
     * @return: PageData
     */
    PageData getOneAddress(PageData pd,String versionNo) throws Exception; 
    /**
     * @describe:设置默认地址
     * @author: zhangchunming
     * @date: 2016年10月28日上午10:55:51
     * @param pd
     * @param versionNo
     * @throws Exception
     * @return: boolean
     */
    boolean setDefault(PageData pd,String versionNo) throws Exception;
    /**
     * @describe:取消默认地址
     * @author: zhangchunming
     * @date: 2016年10月28日上午10:56:13
     * @param pd
     * @param versionNo
     * @throws Exception
     * @return: boolean
     */
    boolean calcelDefault(PageData pd,String versionNo) throws Exception;
    /**
     * @describe:设置默认地址(setDefault和calcelDefault)
     * @author: zhangchunming
     * @date: 2016年10月28日上午10:58:14
     * @param pd
     * @param versionNo
     * @return
     * @throws Exception
     * @return: boolean
     */
    boolean setDefaultAddress(PageData pd,String versionNo) throws Exception;
    /**
     * @describe:根据address_id查询地址
     * @author: zhangchunming
     * @date: 2016年11月17日下午4:57:06
     * @param address_id
     * @param versionNo
     * @throws Exception
     * @return: PageData
     */
    PageData getOneAddressById(String address_id,String versionNo) throws Exception;
    
}