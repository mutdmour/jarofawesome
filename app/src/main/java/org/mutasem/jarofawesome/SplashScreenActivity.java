package org.mutasem.jarofawesome;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    private ImageView mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LOW_PROFILE
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable runMain = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
            startActivity(intent);
            finish();
        }
    };

    private final Handler mHideHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mContentView = (ImageView) findViewById(R.id.splash_screen_image);
        mHideHandler.postDelayed(mHidePart2Runnable, 0);
        mHideHandler.postDelayed(runMain, SPLASH_TIME_OUT);


    }
}
