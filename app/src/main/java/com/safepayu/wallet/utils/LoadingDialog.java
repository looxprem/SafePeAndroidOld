package com.safepayu.wallet.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.safepayu.wallet.R;

public class LoadingDialog extends Dialog {
    TextView text;
    String message;
    ImageView image;
    Context context;

    public LoadingDialog(Context context, String name) {
        super(context);
        this.context = context;
        this.message = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        setCancelable(false);
        setContentView(R.layout.loading_view);
        image = (ImageView) findViewById(R.id.loading_icon);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotate);
        image.setAnimation(animation);
        animation.start();

        text = (TextView) findViewById(R.id.dialogmessage);
        text.setText(message);

    }

    public void setMessage(String message) {
        text.setText(message);
    }

    @Override
    protected void onStop() {
        super.onStop();
        image.clearAnimation();
    }

}
