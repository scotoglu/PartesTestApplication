package com.scoto.partestestapplication.ui.imagequote;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.scoto.partestestapplication.R;
import com.scoto.partestestapplication.data.model.Image;
import com.scoto.partestestapplication.databinding.ActivityDisplayImageFullScreenBinding;

public class DisplayImageFullScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideSystemUI();

        ActivityDisplayImageFullScreenBinding activityDisplayImageFullScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_display_image_full_screen);

        Bundle data = getIntent().getExtras();
        Image image = (Image) data.getParcelable("IMAGE_OBJ");
        activityDisplayImageFullScreenBinding.setImage(image);

    }


    public void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }

}