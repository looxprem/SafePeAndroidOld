package com.safepayu.wallet.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.R;
import com.safepayu.wallet.model.LoginUser;

public class ReferralActivity extends AppCompatActivity {
    Button inviteFriends_btn;
    TextView referralCode;
    String strReferalcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_referral);
       // View rootView = inflater.inflate(R.layout.activity_referral, container, false);
        inviteFriends_btn=(Button) findViewById(R.id.referralbtn);
        referralCode=(TextView) findViewById(R.id.tv_referralcode);
        LoginUser user= SharedPrefManager.getInstance(getApplicationContext()).getUser();
        strReferalcode=user.getReferral_code();
        referralCode.setText(strReferalcode);
        inviteFriends_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"SafePay UPI Payments, Recharges & Money Transfer");
                String shareMessage="Checkout this amazing app for Recharges, Bill payments and UPI transfer. Use this code "+strReferalcode+" to signup and get rewards. Share this app to make money. Just download SafePe from https://play.google.com/store/apps/details?id=" + getPackageName() +"";
                intent.putExtra(Intent.EXTRA_TEXT,shareMessage);
                startActivity(Intent.createChooser(intent,"Sharing via"));
            }
        });


        Button backBUtton = (Button) findViewById(R.id.ref_back_btn);
        backBUtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
    }

}