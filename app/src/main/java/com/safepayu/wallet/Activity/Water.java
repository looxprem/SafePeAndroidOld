package com.safepayu.wallet.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.R;
import com.safepayu.wallet.model.LoginUser;
import com.safepayu.wallet.model.OperatorDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Water extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView customerid_tv;
    EditText customeridEt, amountEt;
    Button sendButton;
    private String selectedOperator="",operator_id="",user_id="";
    int balance = 0;
    int Rechargeamount = 0;
    private Spinner operatorSP;
    List<OperatorDetail> data;
    String[] a = new String[50];
    String Alloperaors = "[\n" +
            "    {\n" +
            "        \"name\": \"Delhi Jal Board\",\n" +
            "        \"id\": \"46\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Uttarakhand Jal Sansthan(b2b)\",\n" +
            "        \"id\": \"44\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Uttarakhand Jal Sansthan(b2c)\",\n" +
            "        \"id\": \"45\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Bses Yamuna\",\n" +
            "        \"id\": \"10\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Municipal Corporation Of Gurugram\",\n" +
            "        \"id\": \"47\"\n" +
            "    }"+
            "]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);

        Button backbtn = (Button) findViewById(R.id.dth_back_btn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        customeridEt = (EditText) findViewById(R.id.customerid);
        customerid_tv = (TextView) findViewById(R.id.textView);
        operatorSP = (Spinner) findViewById(R.id.operator);
        LoginUser loginUser = SharedPrefManager.getInstance(this).getUser();
        user_id = loginUser.getUserid();
        amountEt = (EditText) findViewById(R.id.amountid);
        sendButton = (Button) findViewById(R.id.btn);

        SetOperatorStatic();

        operatorSP.setOnItemSelectedListener(this);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount;
                String Customerid = customeridEt.getText().toString();
                amount = (amountEt.getText().toString());
                if (operatorSP.getSelectedItem().equals("Select Operators")) {
                    Toast.makeText(getApplicationContext(), "Select Operator", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Customerid.equalsIgnoreCase("")) {
                    customeridEt.setError("Enter Customer Id");
                    customeridEt.requestFocus();
                    return;
                } else if (amount.equalsIgnoreCase("")) {
                    amountEt.setError("Enter Amount");
                    amountEt.requestFocus();
                    return;
                } else {

                    String amount1 = amountEt.getText().toString();
                    Intent intent = new Intent(Water.this, PaymentTypeActivity.class);
                    intent.putExtra("rechargeAmount", amount1);
                    intent.putExtra("customerid", Customerid);
                    intent.putExtra("opid", operator_id);
                    intent.putExtra("paymentFor", "waterbill");
                    intent.putExtra("rechType", "Water");
                    startActivity(intent);
                }
            }
        });

//        operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                operatorId = data.get(i).getId();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

    }

    private void SetOperatorStatic() {

        final List<String> listop = new ArrayList<String>();
        data = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(Alloperaors);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject userJson = jArray.getJSONObject(i);
                data.add(new OperatorDetail(
                        userJson.getString("id"),
                        userJson.getString("name")

                ));
                a[i] = userJson.getString("id");
            }
            for (OperatorDetail detail : data) {
                System.out.println("in detail" + detail);
                listop.add(detail.getName());
            }
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listop);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            operatorSP.setAdapter(spinnerArrayAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String s = String.valueOf(operatorSP.getSelectedItem());
        selectedOperator = operatorSP.getSelectedItem().toString();
        operator_id = data.get(position).getId();
        Log.e("Selected Operator", s);
        if (s.contains("Uttarakhand")) {
            customerid_tv.setText("Consumer Number (Last 7 Digits)");
        } else if (s.equalsIgnoreCase("Delhi Jal Board")) {
            customerid_tv.setText("K Number");
        }else {
            customerid_tv.setText("Customer Id ");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
