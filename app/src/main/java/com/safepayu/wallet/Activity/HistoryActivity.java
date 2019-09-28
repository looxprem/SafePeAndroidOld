package com.safepayu.wallet.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.safepayu.wallet.HisotyPackage.RedeemHistory;
import com.safepayu.wallet.HisotyPackage.WalletHistory;
import com.safepayu.wallet.R;

public class HistoryActivity extends AppCompatActivity {

    LinearLayout wallet_history, reddem_history, recharge_history;
    private Button his_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        his_back_btn = findViewById(R.id.his_back_btn);

        his_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        wallet_history = (LinearLayout) findViewById(R.id.wallet_history);
        reddem_history = (LinearLayout) findViewById(R.id.redeem_history);
        recharge_history = (LinearLayout) findViewById(R.id.recharge_history);

        wallet_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HistoryActivity.this, WalletHistory.class));
            }
        });
        reddem_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HistoryActivity.this, RedeemHistory.class));
            }
        });
        recharge_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HistoryActivity.this, RechargeHistory.class));
            }
        });

    }
}
