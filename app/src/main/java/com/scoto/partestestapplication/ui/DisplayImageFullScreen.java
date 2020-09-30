package com.scoto.partestestapplication.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;
import com.scoto.partestestapplication.R;
import com.scoto.partestestapplication.helper.BitmapManager;
import com.scoto.partestestapplication.model.Image;

public class DisplayImageFullScreen extends AppCompatActivity {
    private static final String TAG = "DisplayImageFullScreen";
    private PhotoView displayImageFull;
    private String bookInfo, tag;
    private TextView tagTxt, bookInfoTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image_full_screen);

        Bundle data = getIntent().getExtras();
        Image image = (Image) data.getParcelable("IMAGE_OBJ");
        Log.d(TAG, "onCreate: Image TAG: " + image.getQuoteTag());

        hideSystemUI();


        displayImageFull = (PhotoView) findViewById(R.id.display_image);
        tagTxt = findViewById(R.id.tagTxt);
        bookInfoTxt = findViewById(R.id.bookInfo);

        tagTxt.setText("#" + image.getQuoteTag());
        tagTxt.setTextColor(Color.RED);


        bookInfoTxt.setText(image.getAuthor() + ", " + image.getBookTitle());
        displayImageFull.setImageBitmap(new BitmapManager().byteToBitmap(image.getImage()));


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


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}