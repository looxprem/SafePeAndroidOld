package com.safepayu.wallet.model;

public class WalletModel {

    private String walletId;
    private String walletAmount;
    private String userId;
    private String status;

    public String getWalletId() {
        return walletId;
    }

    public String getWalletAmount() {
        return walletAmount;
    }



    public String getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public WalletModel(String walletId, String walletAmount, String userId, String status) {
        this.walletId = walletId;
        this.walletAmount = walletAmount;
        this.userId = userId;
        this.status = status;
    }
}
