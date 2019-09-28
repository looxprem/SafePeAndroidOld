package com.safepayu.wallet.model;

public class RememberPassword {
    private String mobile,password;

    public String getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }

    public  RememberPassword(String mobile, String password){
        this.mobile=mobile;
        this.password=password;
    }
}
