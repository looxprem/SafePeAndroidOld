package com.safepayu.wallet.paymentpackage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.safepayu.wallet.R;

public class AddmoneyThroughcall extends AppCompatActivity {
    private ImageView clear;
    private Button callbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmoney_throughcall);
        clear=(ImageView) findViewById(R.id.clear_btn_add_throughcall);
        callbtn=(Button)findViewById(R.id.call_button_addmoney);

        ActivityCompat.requestPermissions(AddmoneyThroughcall.this,
                new String[]{Manifest.permission.CALL_PHONE}, 1);
        //-------------END askr permission-------------------------
        callbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (ActivityCompat.checkSelfPermission(AddmoneyThroughcall.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //  return;
                }
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:18001033188")));
//
                return true;
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(AddmoneyThroughcall.this, "Permission denied to Call Phone", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
