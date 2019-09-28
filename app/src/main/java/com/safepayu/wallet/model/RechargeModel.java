package com.safepayu.wallet.model;

public class RechargeModel {


    private String recharge_id;
    private String amount;
    private String status;
    private String customer_number;
    private String rech_type;
    private String recharge_time;
    private String order_id;

    public RechargeModel(String recharge_id, String amount, String status, String customer_number, String rech_type, String recharge_time, String order_id) {
        this.recharge_id = recharge_id;
        this.amount = amount;
        this.status = status;
        this.customer_number = customer_number;
        this.rech_type = rech_type;
        this.recharge_time = recharge_time;
        this.order_id = order_id;
    }

    public String getRecharge_id() {
        return recharge_id;
    }

    public String getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String getCustomer_number() {
        return customer_number;
    }

    public String getRech_type() {
        return rech_type;
    }

    public String getRecharge_time() {
        return recharge_time;
    }

    public String getOrder_id() {
        return order_id;
    }
}
