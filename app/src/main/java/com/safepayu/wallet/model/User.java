package com.safepayu.wallet.model;
public class User {

    public void setId(String id) {
        this.id = id;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public void setD_o_b(String d_o_b) {
        this.d_o_b = d_o_b;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setContry(String contry) {
        this.contry = contry;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String id;
    private String full_name;
    private String pincode;
    private String mobile_number;
    private String password;
    private String address;
    private String email_id;
    private String d_o_b;
    private String date;
    private String sex;

    public String getState() {
        return state;
    }

    public String getContry() {
        return contry;
    }

    public String getCity() {
        return city;
    }

    public String getPassword() {
        return password;
    }

    public String getPincode() {
        return pincode;
    }

    private String city;
    private String state;
    private String contry;

    public String getImage() {
        return image;
    }

    private String image;

    public User(){

    }

    public String getWallet_amount() {
        return wallet_amount;
    }

    public void setWallet_amount(String wallet_amount) {
        this.wallet_amount = wallet_amount;
    }

    private String wallet_amount;
    public User(String wallet_amount){
        this.wallet_amount=wallet_amount;
    }
    public User(String id,String image, String full_name, String mobile_number, String address, String email_id,String d_o_b, String date, String sex, String pincode,String city,String state,String contry){
        this.id=id;
        this.image=image;
        this.full_name=full_name;
        this.mobile_number=mobile_number;
        this.password=password;
        this.address=address;
        this.email_id=email_id;
        this.d_o_b=d_o_b;
        this.date=date;
        this.sex=sex;
        this.city=city;
        this.state=state;
        this.contry=contry;
        this.pincode=pincode;

    }
    public User(String id,String full_name){
        this.id=id;
        this.full_name=full_name;
    }
    public String getId() {
        return id;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getMobile_number() {
        return mobile_number;
    }


    public String getAddress() {
        return address;
    }

    public String getEmail_id() {
        return email_id;
    }

    public String getD_o_b() {
        return d_o_b;
    }

    public String getDate() {
        return date;
    }

    public String getSex() {
        return sex;
    }

    // ----Wallet History---------
    private String wallet_id;
    private String amount;
    private String action;

    public String getRemarks() {
        return remarks;
    }

    private String remarks;

    public String getWallet_id() {
        return wallet_id;
    }

    public String getAmount() {
        return amount;
    }

    public String getAction() {
        return action;
    }

    public String getWallet_date() {
        return wallet_date;
    }

    private String wallet_date;
    public User(String wallet_id, String amount, String action, String wallet_date,String remarks){
        this.wallet_id=wallet_id;
        this.amount=amount;
        this.action=action;
        this.wallet_date=wallet_date;
        this.remarks=remarks;
    }
    private String redeemId;
    private String redeemDate;

    public String getRedeemDate() {
        return redeemDate;
    }

    private String redeemAmount;
    private String acHolderName;
    private String acNumber;
    private String bankName;
    private String ifscCode;
    private String pancard;
    private String adharcard;
    private String status;

    public String getRedeemId() {
        return redeemId;
    }

    public String getStatus() {
        return status;
    }

    public String getAdharcard() {
        return adharcard;
    }

    public String getPancard() {
        return pancard;
    }

    public String getBankName() {
        return bankName;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public String getAcNumber() {
        return acNumber;
    }

    public String getAcHolderName() {
        return acHolderName;
    }

    public String getRedeemAmount() {
        return redeemAmount;
    }

    public User(String redeemId, String redeemDate,String redeemAmount, String acHolderName, String acNumber, String bankName, String ifscCode, String pancard, String adharcard, String status){
        this.redeemId=redeemId;
        this.redeemDate=redeemDate;
        this.redeemAmount=redeemAmount;
        this.acHolderName=acHolderName;
        this.bankName=bankName;
        this.acNumber=acNumber;
        this.ifscCode=ifscCode;
        this.pancard=pancard;
        this.adharcard=adharcard;
        this.status=status;
    }

    public String getBet_date() {
        return bet_date;
    }

    public String getBet_number() {
        return bet_number;
    }

    public String getCoins() {
        return coins;
    }

    private String bet_date;
    private String bet_number;
    private String coins;

    public String getBetStatus() {
        return betStatus;
    }

    private String betStatus;
    public User( String bet_date, String bet_number, String coins,String betStatus){

        this.bet_date=bet_date;
        this.bet_number=bet_number;
        this.coins=coins;
        this.betStatus=betStatus;
    }

}
