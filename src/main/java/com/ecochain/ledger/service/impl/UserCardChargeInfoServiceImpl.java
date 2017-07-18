package com.ecochain.ledger.service.impl;

import com.ecochain.ledger.mapper.UserCardChargeInfoMapper;
import com.ecochain.ledger.model.UserCardChargeInfo;
import com.ecochain.ledger.service.UserCardChargeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Lisandro on 2017/7/18.
 */
@Component("userCardChargeInfoService")
public class UserCardChargeInfoServiceImpl implements UserCardChargeInfoService {

    @Autowired
    private UserCardChargeInfoMapper userCardChargeInfoMapper;

    @Override
    public int insert(UserCardChargeInfo userCardChargeInfo) {
        return this.userCardChargeInfoMapper.insert(userCardChargeInfo);
    }
}
