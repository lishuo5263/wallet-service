package com.ecochain.ledger.mapper;

import com.ecochain.ledger.model.UserCrad;

public interface UserCradMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCrad record);

    int insertSelective(UserCrad record);

    UserCrad selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCrad record);

    int updateByPrimaryKey(UserCrad record);
}