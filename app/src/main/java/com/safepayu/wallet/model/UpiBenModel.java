package com.safepayu.wallet.model;

public class UpiBenModel {
    private String benId;
    private String upi;

    public UpiBenModel(String benId, String upi) {
        this.benId = benId;
        this.upi = upi;
    }

    public String getBenId() {
        return benId;
    }

    public String getUpi() {
        return upi;
    }

    @Override
    public String toString() {
        return upi;
    }
}
