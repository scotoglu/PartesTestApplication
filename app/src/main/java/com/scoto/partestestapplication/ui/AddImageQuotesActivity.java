package com.scoto.partestestapplication.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.scoto.partestestapplication.R;
import com.scoto.partestestapplication.helper.BitmapManager;
import com.scoto.partestestapplication.helper.Constants;
import com.scoto.partestestapplication.model.Image;
import com.scoto.partestestapplication.viewmodel.QuoteViewModel;

public class AddImageQuotesActivity extends AppCompatActivity implements View.OnFocusChangeListener {
    private static final String TAG = "AddImageQuotesActivity";

    private ImageView imageQuote;
    private ImageView focusAuthor, focusBookTitle, focusTag;
    private EditText authorTxt, bookTitleTxt, tagTxt;
    private Button saveBtn;
    private String bitmapStr;
    private BitmapManager bm;
    private Bitmap bitmap;
    private QuoteViewModel quoteViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image_quotes);
        quoteViewModel = ViewModelProviders.of(this).get(QuoteViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar_add_image);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("PartesTest");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        if (intent.getStringExtra("BITMAP") != null) {
            bitmapStr = intent.getStringExtra("BITMAP").toString();
        }
        setViewReference();
        if (!bitmapStr.isEmpty() || bitmapStr.length() > 0) {
            bm = new BitmapManager();
            bitmap = bm.stringToBitmap(bitmapStr);
            imageQuote.setImageBitmap(bitmap);
        }
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImageQuotes();
            }
        });
    }

    private void setViewReference() {
        imageQuote = findViewById(R.id.imageQuote);
        imageQuote.setOnFocusChangeListener(this);


        authorTxt = findViewById(R.id.author);
        authorTxt.setOnFocusChangeListener(this);
        focusAuthor = findViewById(R.id.authorImageViewFocus);

        tagTxt = findViewById(R.id.tag);
        tagTxt.setOnFocusChangeListener(this);
        focusTag = findViewById(R.id.tagImageViewFocus);

        bookTitleTxt = findViewById(R.id.bookTitle);
        bookTitleTxt.setOnFocusChangeListener(this);
        focusBookTitle = findViewById(R.id.bookTitleImageViewFocus);

        saveBtn = findViewById(R.id.saveBtn);
    }

    private void saveImageQuotes() {
        if (bitmap != null) {
            byte[] imageByte = bm.bitmapToByte(bitmap);
            String author = authorTxt.getText().toString();
            String bookTitle = bookTitleTxt.getText().toString();
            String tag = tagTxt.getText().toString();


            if (author.isEmpty() || bookTitle.isEmpty()) {
                Dialogs dialogs = Dialogs.newInstance(getString(R.string.empty_fields), Constants.OPERATION_CODE_EMPTY);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                dialogs.show(ft, Dialogs.TAG);
            } else {
                Image image = new Image(imageByte, author, bookTitle, tag);
                quoteViewModel.insertImage(image);
                Dialogs dialogs = Dialogs.newInstance(getString(R.string.successfull_add), Constants.OPERATION_CODE_SUCCESS);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                dialogs.show(ft, Dialogs.TAG);
                finish();
            }


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: AddImageQuotesActivity...");
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            Log.d(TAG, "onFocusChange: Focused...");
            switch (v.getId()) {

                case R.id.author:
                    setImageViewParams(focusAuthor);
                    break;
                case R.id.bookTitle:
                    setImageViewParams(focusBookTitle);
                    break;
                case R.id.tag:
                    setImageViewParams(focusTag);
                    break;
                default:
                    return;
            }
        } else {
            Log.d(TAG, "onFocusChange: Loosing Focus...");
            switch (v.getId()) {

                case R.id.author:
                    resetImageViewParams(focusAuthor);
                    break;
                case R.id.bookTitle:
                    resetImageViewParams(focusBookTitle);
                    break;
                case R.id.tag:
                    resetImageViewParams(focusTag);
                    break;
                default:
                    return;

            }

        }
    }

    private void setImageViewParams(View v) {
        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
        params.setMargins(0, 30, -5, 0);
        params.width = 40;
        params.height = 40;
        v.setLayoutParams(params);
    }

    private void resetImageViewParams(View v) {
        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
        params.setMargins(0, 0, -0, 0);
        params.width = 0;
        params.height = 0;
        v.setLayoutParams(params);
    }
}
