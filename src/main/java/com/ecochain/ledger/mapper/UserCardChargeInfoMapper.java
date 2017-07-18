package com.ecochain.ledger.mapper;

import com.ecochain.ledger.model.UserCardChargeInfo;

public interface UserCardChargeInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCardChargeInfo record);

    int insertSelective(UserCardChargeInfo record);

    UserCardChargeInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCardChargeInfo record);

    int updateByPrimaryKey(UserCardChargeInfo record);
}