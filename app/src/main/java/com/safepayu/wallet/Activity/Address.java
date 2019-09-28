package com.safepayu.wallet.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.safepayu.wallet.R;

public class Address extends AppCompatActivity {

    private TextView heading ;
    private EditText location, city, state, pincode;
    private Button addbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);


        heading = (TextView) findViewById(R.id.heading);

    }
}
