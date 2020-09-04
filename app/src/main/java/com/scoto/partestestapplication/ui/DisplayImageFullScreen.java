package com.scoto.partestestapplication.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Slide;
import androidx.transition.Transition;

import com.scoto.partestestapplication.R;
import com.scoto.partestestapplication.helper.BitmapManager;

public class DisplayImageFullScreen extends AppCompatActivity {

    private ImageView displayImageFull;
    private String bitmapStr, tag;
    private TextView tagTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setAnimation();
        setContentView(R.layout.activity_display_image_full_screen);


        hideSystemUI();

        displayImageFull = findViewById(R.id.display_image);
        tagTxt = findViewById(R.id.tagTxt);
        Intent intent = getIntent();
        if (intent.getStringExtra("BITMAP_STR") != null) {
            bitmapStr = intent.getStringExtra("BITMAP_STR");
        }
        if (intent.getStringExtra("TAG") != null) {
            tag = intent.getStringExtra("TAG");
        }


        if (!bitmapStr.isEmpty() || bitmapStr != null) {
            BitmapManager bm = new BitmapManager();
            Bitmap bitmap = bm.stringToBitmap(bitmapStr);
            displayImageFull.setImageBitmap(bitmap);
        }
        if (!tag.isEmpty() || tag != null) {
            tagTxt.setText(tag);
        }
    }


    public void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

//    private void setAnimation() {
//        if (Build.VERSION.SDK_INT > 20) {
//            Slide slide = new Slide();
//
//            slide.setSlideEdge(Gravity.LEFT);
//            slide.setDuration(400);
//            slide.setInterpolator(new DecelerateInterpolator());
//            getWindow().setEnterTransition(slide);
//            getWindow().setEnterTransition(slide);
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}