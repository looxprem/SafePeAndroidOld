package com.safepayu.wallet.paymentpackage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import com.safepayu.wallet.model.User;
import com.safepayu.wallet.AdditionalPage.DisplayConformationBet;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManagerLogin;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.R;
import com.safepayu.wallet.model.LoginUser;
import com.safepayu.wallet.model.RememberPassword;

public class AddMoney extends AppCompatActivity {
    ImageView back_addm_through;
    private String mobileNumber = "+919266331219",str_coupon;
    private Button callButton,couponRechargeBtn,recharViaCard;
    private EditText editCoupon;
    private ProgressDialog progressDialog;
    ProgressDialog pdLoading;
    EditText edit_money;
    Button btn_add_money,back_arrow;
    TextView text_money,add_100,add_500,add_1000;
    ImageView alert_moneyl;
    String str_text_money,name,str_money,id,str_mobile,email,str_current_date,remark="Amount Add to your Wallet",action="credit";
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        back_addm_through = (ImageView) findViewById(R.id.back_addm_through);
        editCoupon=(EditText)findViewById(R.id.edit_coupon);
        couponRechargeBtn=(Button)findViewById(R.id.recharge_btn);

        pdLoading = new ProgressDialog(AddMoney.this);
        //recharViaCard=(Button)findViewById(R.id.recharge_btn_card);
        progressDialog=new ProgressDialog(AddMoney.this);

        edit_money=(EditText)findViewById(R.id.edit_money);
        btn_add_money=(Button)findViewById(R.id.add_money_button);
       // text_money=(TextView)findViewById(R.id.text_money);
      //  back_arrow=(Button)findViewById(R.id.back_btn);
        add_100=(TextView)findViewById(R.id.add_100);
        add_500=(TextView)findViewById(R.id.add_500);
        add_1000=(TextView)findViewById(R.id.add_1000);
      //  alert_moneyl=(ImageView)findViewById(R.id.alert_money);
        progressBar=(ProgressBar)findViewById(R.id.progressBar_addmoney);
        edit_money.requestFocus();
        back_addm_through.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //callButton = (Button) findViewById(R.id.call_button);

        LoginUser user = SharedPrefManager.getInstance(this).getUser();
        id=user.getUserid();
        signIn();
        add_100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_money.setText("100");
            }
        });
        add_500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_money.setText("500");
            }
        });
        add_1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_money.setText("1000");
            }
        });
        btn_add_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMoney();
            }
        });


       /* callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(AddMoneyThroughcall.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    return;
                }
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:18001033188")));
            }
        });*/
        couponRechargeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                couponRecharge();
            }
        });

    }
    private void  signIn(){

        RememberPassword rememberPassword= SharedPrefManagerLogin.getInstance(this).getUser();
        final String rememberMobile=rememberPassword.getMobile();
        final String remember_password=rememberPassword.getPassword();
        class UserLogin extends AsyncTask<Void, Void, String>{

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pdLoading.setMessage("\t Loading...");
                pdLoading.show();
                pdLoading.setCancelable(false);
                //progressbar.setVisibility(View.VISIBLE);
            }
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("mobile_number", rememberMobile);
                params.put("password", remember_password);
                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //  progressbar.setVisibility(View.GONE);
                pdLoading.dismiss();
                try {
                    JSONObject obj = new JSONObject(s);
                    System.out.println("hellooooojson object"+obj);
                    if (obj.getString("status").equals("TRUE")) {

                        // JSONArray jArray = new JSONArray(obj.getString("data"));
                        //  System.out.println("after jsonarray"+jArray);
                        JSONObject userJson = obj.getJSONObject("data");
                        User user1= new User();
                       name= userJson.getString("full_name");
                        str_mobile= userJson.getString("mobile_number");
                        email=userJson.getString("email_id");
                        System.out.println("name: "+name+"str mobile:  "+str_mobile+"email: "+email);
                       /* user1.setImage(userJson.getString("image"));
                        user1.setFull_name(userJson.getString("full_name"));
                        user1.setMobile_number(userJson.getString("mobile_number"));
                        user1.setAddress(userJson.getString("address"));
                        user1.setEmail_id(userJson.getString("email_id"));
                        user1.setD_o_b(userJson.getString("d_o_b"));
                        user1.setDate(userJson.getString("date"));
                        user1.setSex(userJson.getString("sex"));
                        user1.setPincode(userJson.getString("zip_code"));
                        user1.setCity(userJson.getString("city"));
                        user1.setState(userJson.getString("state"));
                        user1.setContry(userJson.getString("country"));
                        String imageUrl=user1.getImage();
                        if(imageUrl!=null){
                            Picasso.with(getApplicationContext()).load(imageUrl).placeholder(R.drawable.profile_logo).error(R.drawable.profile_bg_top).into(profileImage);
                        }
                        profileImage.setImageResource(R.drawable.profile_logo);
                        //Picasso.with(this).load(user1.getImage()).placeholder(R.drawable.profile_logo).error(R.drawable.profile_bg_top).into(profileImage);
                        name.setText(user1.getFull_name());
                        mobile.setText(user1.getMobile_number());
                        address.setText(user1.getAddress());
                        email.setText(user1.getEmail_id());
                        dob.setText(user1.getD_o_b());
                        //date.setText(user1.getDate());
                        gender.setText(user1.getSex());
                        pincode.setText(user1.getPincode());
                        city.setText(user1.getCity());
                        state.setText(user1.getState());
                        //cont.setText(user1.getAddress());*/
                       // Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
                        //  finish();
                        //  startActivity(new Intent(getApplicationContext(),Profile.class));
                    }
                    else {
                        System.out.println("elseeeeeeeeeee");
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        UserLogin ul = new UserLogin();
        ul.execute();
        System.out.println("after executeddddddddddddd");

    }
    private void addMoney(){
        str_money=edit_money.getText().toString().trim();
        if (TextUtils.isEmpty(str_money)) {
            edit_money.setError("Enter Amount");
            edit_money.requestFocus();
            return;
        }
        Double dbl_money=Double.parseDouble(str_money);
        int int_id=Integer.parseInt(id);
        Intent intent= new Intent(AddMoney.this,PayUMoneyActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("email",email);
        intent.putExtra("amount",dbl_money);
        intent.putExtra("phone",str_mobile);
        intent.putExtra("id",int_id);
        intent.putExtra("isFromOrder",true);
        startActivity(intent);
    }
    public static boolean validCoupon(String in){
        return  in.length()==16;
    }
    private void couponRecharge(){
        str_coupon=editCoupon.getText().toString().trim();
        if(!validCoupon(str_coupon)){
            editCoupon.setError("Enter a valid Coupon");
            editCoupon.requestFocus();
            return;
        }
        class CouponRecharege extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setMessage("\t PLease Wait..");
                progressDialog.show();
                progressDialog.setCancelable(false);
            }
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("id",id);
                params.put("coupon",str_coupon);
                return requestHandler.sendPostRequest(URLs.URL_RECHARGE_COUPON, params);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(s);
                    //if no error in response
                    if (obj.getString("status").equals("TRUE")) {
                        Toast.makeText(AddMoney.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent= new Intent(AddMoney.this, DisplayConformationBet.class);
                        intent.putExtra("totalcoins","Coupon Recharge");
                        intent.putExtra("message","Your Recharge Successful");
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        CouponRecharege cRecharge=  new CouponRecharege();
        cRecharge.execute();
    }
}
