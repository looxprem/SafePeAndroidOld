package com.safepayu.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.safepayu.wallet.Activity.AddMoneySuccess;
import com.safepayu.wallet.Activity.Navigation;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.MainActivity;
import com.safepayu.wallet.R;

public class SplashScreen extends Activity implements Animation.AnimationListener  {
    Animation animFadeIn;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
        }
//         load the animation
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.animation_fade_in);
        // set animation listener
        animFadeIn.setAnimationListener(this);
        // animation for image
        linearLayout =  findViewById(R.id.layout_linear);
        // start the animation
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.startAnimation(animFadeIn);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
// Start Main Screen

        if(SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserid().equalsIgnoreCase("")){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }else{
            Intent i = new Intent(getApplicationContext(), Navigation.class);
            startActivity(i);
            finish();
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
