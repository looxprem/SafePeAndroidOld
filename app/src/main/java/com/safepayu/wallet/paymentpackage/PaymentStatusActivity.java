package com.safepayu.wallet.paymentpackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.safepayu.wallet.Activity.Navigation;
import com.safepayu.wallet.R;

public class PaymentStatusActivity extends AppCompatActivity {
    TextView status, transaction_id, id, isFromOrder, amount;
    String str_status, str_amount;
    String str_transaction_id;
    String str_id;
    double dbl_amount;
    String str_isFromOrder;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status);
        status = (TextView) findViewById(R.id.status);
        transaction_id = (TextView) findViewById(R.id.transaction_id);
        back = (Button) findViewById(R.id.back_btn_success);
        // id=(TextView)findViewById(R.id.id);
        // isFromOrder=(TextView)findViewById(R.id.id4);
        amount = (TextView) findViewById(R.id.amount_status);
        Bundle bundle = getIntent().getExtras();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(PaymentStatusActivity.this, Navigation.class));
            }
        });
        dbl_amount = bundle.getDouble("amount");
        str_status = bundle.getString("status");
        str_transaction_id = bundle.getString("transaction_id");
        //int int_id = bundle.getInt("id");
        // str_isFromOrder = bundle.getString("isFromOrder");
        // mId = bundle.getInt("id");
        //isFromOrder = bundle.getBoolean("isFromOrder");
        str_amount = String.valueOf(dbl_amount);
        status.setText(str_status);
        transaction_id.setText("Transaction id: " + str_transaction_id);
        // id.setText(int_id);
        amount.setText("Rs. " + str_amount);
//        isFromOrder.setText(str_isFromOrder);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(PaymentStatusActivity.this, Navigation.class));
    }
}
