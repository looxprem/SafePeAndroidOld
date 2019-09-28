package com.safepayu.wallet.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.safepayu.wallet.R;

public class NotificationActivity extends AppCompatActivity {
    Button button, backbtn;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        return;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        backbtn = (Button) findViewById(R.id.dth_back_btn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // finish();

            }
        });
        button=(Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NotificationActivity.this, AddressActivity.class);
                i.putExtra("Addressupdate", false);
                startActivity(i);
            }
        });




    }
}
