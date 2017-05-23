package com.ecochain.ledger.mapper;

import com.ecochain.ledger.model.DigitalCoin;

import java.util.Map;

public interface DigitalCoinMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DigitalCoin record);

    int insertSelective(DigitalCoin record);

    DigitalCoin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DigitalCoin record);

    int updateByPrimaryKey(DigitalCoin record);

    Map getCoinPrice(String coinName);
}