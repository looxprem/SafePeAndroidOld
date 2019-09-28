package com.safepayu.wallet;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.model.LoginUser;
import com.safepayu.wallet.R;

public class ChangePassword extends AppCompatActivity {
    private EditText new_password, old_password, conf_password;
    private Button reset_button;
    ProgressBar progressbar;
    private TextView back_chang_password;
    private String id, str_new_password, str_old_password, str_conf_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        old_password = (EditText) findViewById(R.id.old_pass);
        new_password = (EditText) findViewById(R.id.new_pass);
        conf_password = (EditText) findViewById(R.id.conf_pass);
        reset_button = (Button) findViewById(R.id.reset_pass);
        back_chang_password = (TextView) findViewById(R.id.back_chang_password);
        progressbar = (ProgressBar) findViewById(R.id.progressBar_reset);


        LoginUser user = SharedPrefManager.getInstance(this).getUser();
        id = user.getUserid();
        System.out.println("iddd change passwprd: " + id);
        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
        back_chang_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void resetPassword() {
        str_old_password = old_password.getText().toString().trim();
        str_new_password = new_password.getText().toString().trim();
        str_conf_password = conf_password.getText().toString().trim();
        if (TextUtils.isEmpty(str_old_password)) {
            old_password.setError("Enter valid old password");
            old_password.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_new_password)) {
            new_password.setError("Enter valid new password");
            new_password.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(str_conf_password)) {
            conf_password.setError("Enter valid conform password");
            conf_password.requestFocus();
            return;
        }

        class ResetPassword extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressbar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("old_password", str_old_password);
                params.put("password", str_new_password);
                params.put("confirm_password", str_conf_password);
                return requestHandler.sendPostRequest(URLs.URL_CHANGEPASSWORD, params);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressbar.setVisibility(View.GONE);
                try {
                    JSONObject obj = new JSONObject(s);
                    if (obj.getString("status").equals("TRUE")) {
                        Toast.makeText(ChangePassword.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(ChangePassword.this, ChangePassword.class));
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        ResetPassword resetPassword = new ResetPassword();
        resetPassword.execute();
    }
}
