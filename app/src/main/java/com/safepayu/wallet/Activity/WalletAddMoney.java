package com.safepayu.wallet.Activity;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.safepayu.wallet.HisotyPackage.RedeemHistory;
import com.safepayu.wallet.HisotyPackage.WalletHistory;
import com.safepayu.wallet.R;

import static java.lang.Integer.parseInt;

public class WalletAddMoney extends AppCompatActivity {
    private EditText et_amount;
    private Button btn_addMoneyType;
    private Button sendmoney_back_btn;
    double MaxAmount = 12000, MinAMount = 100;
    TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_add_money);

        sendmoney_back_btn=findViewById(R.id.sendmoney_back_btn);
        et_amount=findViewById(R.id.et_amount);
        btn_addMoneyType=findViewById(R.id.btn_addMoneyType);
        errorText = (TextView) findViewById(R.id.errorText);
        btn_addMoneyType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_amount.getText().toString().equalsIgnoreCase("") || Double.parseDouble(et_amount.getText().toString()) < MinAMount){
                    Toast.makeText(getApplicationContext(),"Minimum amount to add is 100.",Toast.LENGTH_LONG).show();
                    errorText.setText("Minimum amount to add is ₹ 100");

                }
                else if(et_amount.getText().toString().equalsIgnoreCase("") || Double.parseDouble(et_amount.getText().toString()) > MaxAmount ){
                    Toast.makeText(getApplicationContext(),"Maximum amount to add is 12000.",Toast.LENGTH_LONG).show();
                    errorText.setText("Maximum amount to add is ₹ 12000");


                }
                else{
                    errorText.setText("");
                    Intent intent=new Intent(getApplicationContext(),PaymentTypeActivity.class);
                    intent.putExtra("amountToAdd",et_amount.getText().toString());
                    intent.putExtra("paymentFor", "wallet");
                    startActivity(intent);
                    finish();
                }
            }
        });
        sendmoney_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



    }

}
