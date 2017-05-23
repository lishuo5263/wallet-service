package com.ecochain.ledger.service.impl;

import com.ecochain.ledger.mapper.DigitalCoinMapper;
import com.ecochain.ledger.service.DigitalCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by LiShuo on 2017/05/23.
 */
@Component("digitalCoinService")
public class DigitalCoinServiceImpl implements DigitalCoinService {

    @Autowired
    private DigitalCoinMapper digitalCoinMapper;

    @Override
    public Map getCoinPrice(String coinName) {
        return  digitalCoinMapper.getCoinPrice(coinName);
    }
}
