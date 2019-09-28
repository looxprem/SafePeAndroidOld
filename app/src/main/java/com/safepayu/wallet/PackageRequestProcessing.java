package com.safepayu.wallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.safepayu.wallet.Activity.Navigation;
import com.safepayu.wallet.Activity.WithdrawlamountSuccess;

public class PackageRequestProcessing extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_request_processing);

        button=(Button) findViewById(R.id.gotohomepage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getApplicationContext(), Navigation.class));
                finish();

            }
        });
    }
}
