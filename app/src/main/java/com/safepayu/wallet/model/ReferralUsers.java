package com.safepayu.wallet.model;

public class ReferralUsers {

    private String user_id;
    private String first_name;
    private String last_name;
    private String mobile;

    public ReferralUsers(String user_id, String first_name, String last_name, String mobile) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.mobile = mobile;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getMobile() {
        return mobile;
    }
}
