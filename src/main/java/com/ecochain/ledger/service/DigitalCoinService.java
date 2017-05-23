package com.ecochain.ledger.service;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Lisandro on 2017/5/23.
 */
@Service("digitalCoinService")
public interface DigitalCoinService {

    Map getCoinPrice(String coinName);

}
