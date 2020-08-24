package com.scoto.partestestapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.scoto.partestestapplication.helper.BitmapManager;
import com.scoto.partestestapplication.model.Image;
import com.scoto.partestestapplication.viewmodel.QuoteViewModel;

public class AddImageQuotesActivity extends AppCompatActivity {
    private static final String TAG = "AddImageQuotesActivity";

    private ImageView imageQuote;
    private EditText authorTxt, bookTitleTxt, tagTxt;
    private Button saveBtn;
    private String bitmapStr;
    private BitmapManager bm;
    private Bitmap bitmap;
    private QuoteViewModel quoteViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_image_quotes);
        quoteViewModel = ViewModelProviders.of(this).get(QuoteViewModel.class);

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
        authorTxt = findViewById(R.id.author);
        tagTxt = findViewById(R.id.tag);
        bookTitleTxt = findViewById(R.id.bookTitle);
        saveBtn = findViewById(R.id.saveBtn);
    }
    private void saveImageQuotes(){
        if (bitmap != null){
            byte[] imageByte = bm.bitmapToByte(bitmap);
            String author = authorTxt.getText().toString();
            String bookTitle = bookTitleTxt.getText().toString();
            String tag = tagTxt.getText().toString();

            Image image  =new Image(imageByte,author,bookTitle,tag);

            quoteViewModel.insertImage(image);
            Toast.makeText(this, "INSERTED IMAGE.", Toast.LENGTH_SHORT).show();
        }
    }
}
