package com.ecochain.ledger.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.RandomStringUtils;
/**
 * @author zhangchunming
 * @describe 订单生成器
 */
public class FlowNoGenerater {
  
    public static String generateOrderNo(){
    	StringBuilder sb = new StringBuilder();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        sb.append(sf.format(new Date()))
            .append(RandomStringUtils.randomNumeric(6));
        return sb.toString();
    }
}
