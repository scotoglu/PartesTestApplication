package com.scoto.partestestapplication.ui;
/*
 * I have tried to use with TabLayout, the fragment started from QuoteList fragment but I failed to use properly.
 * Problem is I have couldn't change fragment, overlap to previous fragment.
 * After a few different ways still continue,then I have change fragment to activity.
 *
 *
 * AddImageQuotesActivity is used now.
 *
 *
 * Sefa ÇOTOĞLU
 * 24/08/2020
 * * */
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.scoto.partestestapplication.R;
import com.scoto.partestestapplication.helper.BitmapManager;
import com.scoto.partestestapplication.model.Image;
import com.scoto.partestestapplication.viewmodel.QuoteViewModel;

public class AddImageQuotes extends Fragment {
    private static final String TAG = "AddImageQuotes";

    private ImageView imageQuote;
    private EditText authorTxt, bookTitleTxt, tagTxt;
    private Button saveBtn;
    private String bitmapStr;
    private BitmapManager bm;
    private Bitmap bitmap;
    private QuoteViewModel quoteViewModel;

    public AddImageQuotes() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        quoteViewModel = ViewModelProviders.of(this).get(QuoteViewModel.class);
        Bundle bundle = new Bundle();
        if (getArguments() != null) {
            bitmapStr = getArguments().get("BITMAP").toString();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_add_image_quotes, container, false);

        imageQuote = v.findViewById(R.id.imageQuote);
        authorTxt = v.findViewById(R.id.author);
        bookTitleTxt = v.findViewById(R.id.bookTitle);
        tagTxt = v.findViewById(R.id.tag);
        saveBtn = v.findViewById(R.id.saveBtn);
        if (!bitmapStr.isEmpty() || bitmapStr.length() > 0) {
            bm = new BitmapManager();
            bitmap = bm.stringToBitmap(bitmapStr);
            imageQuote.setImageBitmap(bitmap);
        } else {
            Log.d(TAG, "onCreateView: Bitmap is null...");
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Save Button Clicked...");
                saveImageQuotes();

            }
        });

        return v;
    }

    private void getTextData() {

    }

    private void saveImageQuotes() {
        if (bitmap != null) {
            byte[] images = bm.bitmapToByte(bitmap);
            String author = authorTxt.getText().toString();
            String bookTitle = bookTitleTxt.getText().toString();
            String tag = tagTxt.getText().toString();
            Image imageData = new Image(images, author, bookTitle, tag);
            quoteViewModel.insertImage(imageData);
            Toast.makeText(getContext(), "INSERTED IMAGES", Toast.LENGTH_SHORT).show();
        }
    }

    private void setDataToField() {

    }
}