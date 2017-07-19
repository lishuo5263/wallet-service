package com.ecochain.ledger.service;

import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.model.UserCrad;

/**
 * Created by Lisandro on 2017/7/18.
 */
public interface UserCardService {

    int addBankCard(UserCrad userCrad);

    int findCardByCardNo(PageData pd) throws Exception;
}
