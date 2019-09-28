package com.safepayu.wallet.Activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.R;
import com.safepayu.wallet.model.LoginUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PackageDetailsActivity extends AppCompatActivity {

    TextView packageName, packageAmount, totalAmount, amountCredited, amountBalance, note;
    String userid = null;
    Button backButon;
    LinearLayout packageDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_details);


        packageName = (TextView) findViewById(R.id.txtPckgName);
        packageAmount = (TextView) findViewById(R.id.txtPckgAmount);
        totalAmount = (TextView) findViewById(R.id.txtPckgBonusAmnt);
        amountCredited = (TextView) findViewById(R.id.txtPckgAmountCredit);
        amountBalance = (TextView) findViewById(R.id.txtPckgAmountBalance);
        note = (TextView) findViewById(R.id.note);
        packageDetails = (LinearLayout) findViewById(R.id.packageDetails);


        backButon = (Button) findViewById(R.id.backbtn_packagedetails);

        // back_btn=(ImageView)findViewById(R.id.clear_btn_wallet_history);
        backButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        LoginUser loginUser = SharedPrefManager.getInstance(PackageDetailsActivity.this).getUser();
        userid =  loginUser.getUserid();


        new CheckPackage().execute();

    }



    class CheckPackage extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //otp = otpNum;
            //progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            //System.out.println("Userid: -"+userid);
            params.put("userid", userid);
            // params.put("otp",otpget);
            return requestHandler.sendPostRequest(URLs.URL_GET_PACKAGES, params);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("ERRORFORCHECKMOBILE", s);
            System.out.print("BLAAAAAAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHHH");
            System.out.print(s.getClass());

            //progressbar.setVisibility(View.GONE);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("status").equalsIgnoreCase("Success")) {
                    // ValidateOtp.Registeration registeration = new ValidateOtp.Registeration();
                    //registeration.execute();
                    // Toast.makeText(getApplicationContext(), "User Mobile already exists", Toast.LENGTH_SHORT).show();
                    // editmobile.setText("");
                    // signIn();

                    JSONObject userJson = jsonObject.getJSONObject("msg");
                    String package_name=userJson.getString("package_name");
                    String package_status=userJson.getString("status");
                    String package_amount=userJson.getString("package_amount");
                    String total_amount=userJson.getString("total_amount");
                    String balance_amount=userJson.getString("balance_amount");

                   if(package_status.equalsIgnoreCase("APPROVED")){
                       packageDetails.setVisibility(View.VISIBLE);
                       packageName.setText(package_name);
                       packageAmount.setText("₹"+package_amount);
                       totalAmount.setText("₹"+total_amount);
                       amountBalance.setText("₹"+balance_amount);
                       amountCredited.setText("₹" + (Integer.parseInt(total_amount) - Integer.parseInt(balance_amount)));
                   }else{
                       note.setVisibility(View.VISIBLE);
                       note.setText("Your package is not approved.");
                   }



                    // Log.e("ERROR FOR CHECK MOBILE", s);
                } else {
                    note.setVisibility(View.VISIBLE);
                    note.setText("You don't have any package.");


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
