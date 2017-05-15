package com.ecochain.ledger.model;

/**
 * Created by Lisandro on 2017/5/15.
 */
public class R8ExChangeDTO {
    private  String  responseCode;

    private Object message;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
