package com.ecochain.ledger.service;

import com.ecochain.ledger.model.PageData;

public interface ShopSupplierService {

    public PageData getSupplierByUserId(String user_id,String versionNo) throws Exception;
}
