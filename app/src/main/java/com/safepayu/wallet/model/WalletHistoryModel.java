package com.safepayu.wallet.model;

import java.util.Date;

public class WalletHistoryModel {
        private String transaction_id;
        private String amount;
        private String status;
        private String description;
        private String operation;
        private String date;

    public WalletHistoryModel(String transaction_id, String amount, String status, String description, String operation, String date) {
        this.transaction_id = transaction_id;
        this.amount = amount;
        this.status = status;
        this.description = description;
        this.operation = operation;
        this.date = date;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public String getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getOperation() {
        return operation;
    }

    public String getDate() {
        return date;
    }
}
