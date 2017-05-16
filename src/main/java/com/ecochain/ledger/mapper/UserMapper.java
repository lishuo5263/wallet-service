package com.ecochain.ledger.mapper;


import com.ecochain.ledger.model.User;

import java.util.List;
import java.util.Map;

/**
 * Created by Lisandro on 2017年5月15日15:02:24.
 */
public interface UserMapper {
    public User findUserInfo();
    List<Map<String, Object>> getAllUserInfo();
}
