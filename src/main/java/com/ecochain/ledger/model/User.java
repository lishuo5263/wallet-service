package com.ecochain.ledger.model;

/**
 * Created by Lisandro on 2017年5月15日15:02:33.
 */
public class User {
    private String name;
    private Integer age;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
