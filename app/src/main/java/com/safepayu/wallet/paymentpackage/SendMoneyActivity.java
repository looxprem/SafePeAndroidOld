package com.safepayu.wallet.paymentpackage;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.R;
import com.safepayu.wallet.model.LoginUser;

public class SendMoneyActivity extends AppCompatActivity {
    private String id,amount="0",strMobileNumber;
    private TextView txtAmount;
    private int int_wallet_amount;
    private EditText editMobileNumber;
    private Button sendMoney;
    private ImageView back_btn_sendMoney;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money_enternumber);
        Intent intent=getIntent();
        amount=intent.getStringExtra("amount");
        txtAmount=(TextView)findViewById(R.id.amount_sendMoney);
        txtAmount.setText("Rs."+amount);
        System.out.println("amount... "+amount);
        editMobileNumber=(EditText)findViewById(R.id.edit_mobile_number);
        sendMoney=(Button)findViewById(R.id.send_money_conform_button);
        progressDialog=new ProgressDialog(SendMoneyActivity.this);
        back_btn_sendMoney=(ImageView)findViewById(R.id.back_btn_sendMoney);
        LoginUser user= SharedPrefManager.getInstance(this).getUser();
        id= user.getUserid();
        back_btn_sendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMoneyMethod();
            }
        });
    }
  private   void sendMoneyMethod(){
        strMobileNumber=editMobileNumber.getText().toString().trim();
        if (!validMobile(strMobileNumber)) {
            editMobileNumber.setError("Enter a valid mobile Number");
            editMobileNumber.requestFocus();
            return;
        }
      class SendMoney extends AsyncTask<Void, Void, String> {
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
              params.put("amount",amount);
              params.put("mobile_number", strMobileNumber);
              return requestHandler.sendPostRequest(URLs.URL_SEND_MONEY, params);
          }
          @Override
          protected void onPostExecute(String s) {
              super.onPostExecute(s);
              progressDialog.dismiss();
              try {

                  JSONObject obj = new JSONObject(s);
                  //if no error in response
                  if (obj.getString("status").equals("TRUE")) {
                      Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                      new AlertDialog.Builder(SendMoneyActivity.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Send Amount")
                              .setMessage("Successful Rs. "+amount+" transaction to your friend "+strMobileNumber)
                              .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {
                                      finish();
                                     // startActivity(new Intent(SendMoneyActivity.this, Homepage.class));
                                  }
                              }).show();
                  }
                  else {
                      Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                  }
              } catch (JSONException e) {
                  e.printStackTrace();
              }
          }
      }

      SendMoney ur=  new SendMoney();
      ur.execute();

    }
    public static boolean validMobile(String in){
        return  in.length()==10 && android.util.Patterns.PHONE.matcher(in).matches();
    }

}
