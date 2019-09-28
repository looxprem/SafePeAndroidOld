package com.safepayu.wallet.Activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.R;
import com.safepayu.wallet.model.LoginUser;
import com.safepayu.wallet.utils.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ChangePasscodeActivity extends AppCompatActivity {
    private ImageView delete;
    private EditText edtxPassCode, edtxConfPassCode, old_pass;
    private Button create_password, cancel;
    LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chnage_passcode);
        Button backBtn = (Button) findViewById(R.id.send_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        dialog = new LoadingDialog(ChangePasscodeActivity.this, "");

        edtxPassCode = findViewById(R.id.edtxPassCode);
        cancel = findViewById(R.id.cancel);
        old_pass = findViewById(R.id.old_pass);
        edtxConfPassCode = findViewById(R.id.edtxConfPassCode);
        create_password = findViewById(R.id.create_password);

        create_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtxPassCode.getText().toString().trim()) || edtxPassCode.getText().toString().trim().toCharArray().length < 4) {
                    edtxPassCode.setError("Must be 4 numbers");
                    return;
                } else if (TextUtils.isEmpty(edtxConfPassCode.getText().toString().trim()) || edtxConfPassCode.getText().toString().trim().toCharArray().length < 4) {
                    edtxConfPassCode.setError("Must be 4 numbers");
                    return;
                } else if (!edtxPassCode.getText().toString().trim().equals(edtxConfPassCode.getText().toString().trim())) {
                    edtxConfPassCode.setError("Confirm password mismatched");
                    return;
                } else if (!old_pass.getText().toString().equals(SharedPrefManager.getInstance(ChangePasscodeActivity.this).getUser().getPasscode())) {
                    old_pass.setError("Old passcode not matched");
                    return;
                } else {
                    new CreatePasscode().execute(edtxPassCode.getText().toString().trim());
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    class CreatePasscode extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressbar.setVisibility(View.VISIBLE);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("userid", SharedPrefManager.getInstance(ChangePasscodeActivity.this).getUser().getUserid());
            params.put("passcode", voids[0]);
            // params.put("order_amount", amoutPaid);
            //params.put("order_currency", "INR");
            return requestHandler.sendPostRequest(URLs.UPDATE_PASSCODE, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject obj = new JSONObject(s);

                if (obj.optString("status").equalsIgnoreCase("Response Success")) {
                    JSONObject jsonObject = obj.getJSONObject("data");
                    if (jsonObject.optInt("rescode") == 200) {
                        LoginUser user = SharedPrefManager.getInstance(ChangePasscodeActivity.this).getUser();
                        user.setPasscode(edtxPassCode.getText().toString());
                        SharedPrefManager.getInstance(ChangePasscodeActivity.this).userLogin(user);
                        Toast.makeText(ChangePasscodeActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } else {
                    Toast.makeText(ChangePasscodeActivity.this, "Failed.", Toast.LENGTH_SHORT).show();
                    finish();
                }

                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ChangePasscodeActivity.this, "Failed.", Toast.LENGTH_SHORT).show();
            }
        }


    }
}
