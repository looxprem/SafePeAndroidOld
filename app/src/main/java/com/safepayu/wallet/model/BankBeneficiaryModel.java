package com.safepayu.wallet.model;

public class BankBeneficiaryModel {
    private String benId;
    private String bankAccount;
    private String bankIFSC;
    private String name;

    public BankBeneficiaryModel(String benId, String name, String bankAccount, String bankIFSC) {
        this.benId = benId;
        this.bankAccount = bankAccount;
        this.bankIFSC = bankIFSC;
        this.name = name;
    }

    public String getBenId() {
        return benId;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public String getBankIFSC() {
        return bankIFSC;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return bankAccount.equalsIgnoreCase("") ? name : name+" ("+bankAccount+")";
        //return name+" ("+bankAccount+")";
    }
}
