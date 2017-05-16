package com.ecochain.ledger.service;

import com.ecochain.ledger.model.PageData;

public interface QklLibService {
    
    /**
     * @describe:获取公私钥
     * @author: zhangchunming
     * @date: 2017年4月25日下午5:16:22
     * @param seedsStr
     * @throws Exception
     * @return: PageData
     */
    public  PageData getPriPubKey(String seedsStr) throws Exception;
    
    /**
     * @describe:发送数据到区块链
     * @author: zhangchunming
     * @date: 2017年4月25日下午5:16:43
     * @param seedsStr
     * @param data
     * @throws Exception
     * @return: PageData
     */
    public  String sendDataToSys(String seedsStr,Object data) throws Exception;
}
