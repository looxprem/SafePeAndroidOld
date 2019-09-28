package com.safepayu.wallet;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.model.LoginUser;
import com.safepayu.wallet.utils.LoadingDialog;
import com.safepayu.wallet.utils.PasscodeClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CreatePasscodeDialog extends Dialog {
    private ImageView delete;
    private EditText edtxPassCode, edtxConfPassCode;
    private Button create_password,cancel;
    Activity activity;
    LoadingDialog dialog;
    PasscodeClickListener clickListener;

    public CreatePasscodeDialog(@NonNull Activity activity) {
        super(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.activity = activity;
        this.clickListener =clickListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        setContentView(R.layout.create_passcode_layout);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        dialog = new LoadingDialog(activity, "");

        edtxPassCode = findViewById(R.id.edtxPassCode);
        cancel = findViewById(R.id.cancel);
        edtxConfPassCode = findViewById(R.id.edtxConfPassCode);
        create_password = findViewById(R.id.create_password);

        create_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtxPassCode.getText().toString().trim()) || edtxPassCode.getText().toString().trim().toCharArray().length < 4) {
                    edtxPassCode.setError("Must be 4 numbers");
                } else if (TextUtils.isEmpty(edtxConfPassCode.getText().toString().trim()) || edtxConfPassCode.getText().toString().trim().toCharArray().length < 4) {
                    edtxConfPassCode.setError("Must be 4 numbers");
                } else if (!edtxPassCode.getText().toString().trim().equals(edtxConfPassCode.getText().toString().trim())) {
                    edtxConfPassCode.setError("Must be 4 numbers");
                } else {
                    new CreatePasscode().execute(edtxPassCode.getText().toString().trim());
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
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
            params.put("userid", SharedPrefManager.getInstance(activity).getUser().getUserid());
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
                        LoginUser user = SharedPrefManager.getInstance(activity).getUser();
                        user.setPasscode(edtxPassCode.getText().toString());
                        SharedPrefManager.getInstance(activity).userLogin(user);
                        Toast.makeText(activity, "Passcode Created", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }

                } else {
                    Toast.makeText(activity, "Passcode creation failed.", Toast.LENGTH_SHORT).show();
                    dismiss();
                }

                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                dismiss();
            }
        }


    }


}
