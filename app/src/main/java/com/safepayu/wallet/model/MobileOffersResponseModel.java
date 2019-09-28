package com.safepayu.wallet.model;

public class MobileOffersResponseModel {


    /**
     * category : SPL/RATE CUTTER
     * subCategory : Local
     * description : Talktime Rs. 55 - Local @ Rs. 0.012/sec - STD @ Rs. 0.012/sec - 200 MB 2G/3G/4G Data - Data Validity 28 day(s) - For 2G/3G/4G user
     * amount : 65
     * talktime : 55
     * validity : 28 Days
     * productId : 1
     * productName : Mobile
     * operatorId : 14
     * operatorName : Vodafone
     * circleId : 6
     * circleName : Delhi
     */

    private String category;
    private String subCategory;
    private String description;
    private int amount;
    private int talktime;
    private String validity;
    private int productId;
    private int operatorId;
    private String operatorName;
    private int circleId;
    private String circleName;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTalktime() {
        return talktime;
    }

    public void setTalktime(int talktime) {
        this.talktime = talktime;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public int getCircleId() {
        return circleId;
    }

    public void setCircleId(int circleId) {
        this.circleId = circleId;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }
}
