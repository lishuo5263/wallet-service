package com.ecochain.ledger.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ecochain.ledger.model.PageData;

/**
 * Created by Lisandro on 2017/5/23.
 */
@Service("digitalCoinService")
public interface DigitalCoinService {

    Map getCoinPrice(String coinName);
    /**
     * @describe:查询所有币种
     * @author: zhangchunming
     * @date: 2017年7月19日下午2:33:14
     * @return: PageData
     */
    List<PageData> getAllCoinPrice();
}
