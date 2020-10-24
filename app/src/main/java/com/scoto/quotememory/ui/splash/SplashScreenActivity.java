package com.scoto.quotememory.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.scoto.quotememory.R;
import com.scoto.quotememory.ui.main.MainActivity;

import java.io.File;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView splashLogo;
    private TextView title, dev;
    private Animation top, bottom;
    private static int DURATION = 4000;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        handler = new Handler();


        hideSystemUI();


        splashLogo = findViewById(R.id.splash);
        dev = findViewById(R.id.dev_scoto);

        top = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);


        splashLogo.setAnimation(top);

        dev.setAnimation(bottom);

        clearCroppedImageCache();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, DURATION);
    }

    private void hideSystemUI() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    private void clearCroppedImageCache() {
        //Android-Image-Cropper uses cache when image cropped and never delete itself
        //To avoid size matter, every app starts, clears image cache
        File cacheDir = getCacheDir();
        if (cacheDir != null) {
            File[] files = cacheDir.listFiles();

            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().startsWith("cropped")) {
                    files[i].delete();
                }
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler = null;
        }
    }
}