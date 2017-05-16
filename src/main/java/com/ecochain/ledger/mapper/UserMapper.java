package com.ecochain.ledger.mapper;


import java.util.List;
import java.util.Map;

import com.ecochain.ledger.model.User;
import com.ecochain.ledger.util.MyMapper;

/**
 * Created by Lisandro on 2017年5月15日15:02:24.
 */
public interface UserMapper extends MyMapper<User>{
    public User findUserInfo();
    List<Map<String, Object>> getAllUserInfo();
}
