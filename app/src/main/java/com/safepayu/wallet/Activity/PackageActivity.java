package com.safepayu.wallet.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.safepayu.wallet.R;

public class PackageActivity extends AppCompatActivity {

    private Button backButon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);

        backButon = (Button) findViewById(R.id.backbtn_package);

        // back_btn=(ImageView)findViewById(R.id.clear_btn_wallet_history);
        backButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



}
