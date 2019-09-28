package com.safepayu.wallet.paymentpackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.safepayu.wallet.Activity.Navigation;
import com.safepayu.wallet.R;

public class SuccessAmount extends AppCompatActivity {
    TextView amount;
    Bundle bundle;
    String str_money;
    Button back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_amount);
        amount = (TextView) findViewById(R.id.amount_display);
        back_button = (Button) findViewById(R.id.back_btn_success_wallet);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(SuccessAmount.this, Navigation.class));
            }
        });

        Intent in = getIntent();
        bundle = in.getExtras();
        str_money = bundle.getString("amount");
        amount.setText("Rs. " + str_money);
    }
}
