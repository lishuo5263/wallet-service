package com.ecochain.ledger.mapper;

import java.util.List;
import java.util.Map;

import com.ecochain.ledger.model.DigitalCoin;
import com.ecochain.ledger.model.PageData;

public interface DigitalCoinMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DigitalCoin record);

    int insertSelective(DigitalCoin record);

    DigitalCoin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DigitalCoin record);

    int updateByPrimaryKey(DigitalCoin record);

    Map getCoinPrice(String coinName);
    
    List<PageData> getAllCoinPrice();
}