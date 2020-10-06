package com.scoto.partestestapplication.ui.imagequote;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.github.chrisbanes.photoview.PhotoView;
import com.scoto.partestestapplication.R;
import com.scoto.partestestapplication.data.model.Image;
import com.scoto.partestestapplication.databinding.ActivityDisplayImageFullScreenBinding;

public class DisplayImageFullScreen extends AppCompatActivity {
    private static final String TAG = "DisplayImageFullScreen";
    private PhotoView displayImageFull;
    private String bookInfo, tag;
    private TextView tagTxt, bookInfoTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityDisplayImageFullScreenBinding activityDisplayImageFullScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_display_image_full_screen);

        Bundle data = getIntent().getExtras();
        Image image = (Image) data.getParcelable("IMAGE_OBJ");

        hideSystemUI();

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
    protected void onDestroy() {
        super.onDestroy();
    }
}