package com.ecochain.ledger.mapper;

import com.ecochain.ledger.model.UsersDetails;


public interface UsersDetailsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UsersDetails record);

    int insertSelective(UsersDetails record);

    UsersDetails selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UsersDetails record);

    int updateByPrimaryKey(UsersDetails record);

    Integer selectUserType(Integer userId);
}