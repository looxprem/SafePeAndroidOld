package com.safepayu.wallet.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.safepayu.wallet.R;

public class Utilities extends AppCompatActivity {
    Button backbtn;
    LinearLayout layout_electricity,layout_gas,layout_water,layout_broadband;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities);
        backbtn =(Button)findViewById(R.id.utilities_back_btn);
        layout_electricity=(LinearLayout)findViewById(R.id.layout_electricity);
        layout_gas=(LinearLayout)findViewById(R.id.layout_gas_recharge);
        layout_water=(LinearLayout)findViewById(R.id.layout_water);
        layout_broadband=(LinearLayout)findViewById(R.id.layout_broadband);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        layout_electricity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Utilities.this,ElectricityRecharge.class));
            }
        });
        layout_gas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Utilities.this,Gas.class));
            }
        });
        layout_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Utilities.this,"Coming Soon",Toast.LENGTH_SHORT);
              //  startActivity(new Intent(Utilities.this,Water.class));
            }
        }); layout_broadband.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Utilities.this,Broadband.class));
            }
        });

    }
}
