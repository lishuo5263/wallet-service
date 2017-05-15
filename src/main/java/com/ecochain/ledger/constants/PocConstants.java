package com.ecochain.ledger.constants;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by LiShuo on 2017/03/14.
 */
public class PocConstants {
    public static final Logger logger = Logger.getLogger(PocConstants.class);
    private static Properties constant = new Properties();
    public static String BlockChainURLTest;
    public static String BlockChainURLTest2;
    static {
        InputStreamReader is = null;
        try {
            is = new InputStreamReader(PocConstants.class.getResourceAsStream("/url.properties"), "UTF-8");
            constant.load(is);
            BlockChainURLTest = getProperty("Poc.BlockChainURLTest");
            BlockChainURLTest2= getProperty("Poc.BlockChainURLTest2");
        } catch (IOException ex) {
            logger.error("url.properties出错", ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    logger.error("url.properties出错", ex);
                }
            }
        }
    }

    public static String getProperty(String key) {
        return constant.getProperty(key, "");
    }

    public static int getInt(String key) {
        return Integer.parseInt(constant.getProperty(key, "0"));
    }

    public static double getDouble(String key) {
        return Double.parseDouble(constant.getProperty(key));
    }

}
