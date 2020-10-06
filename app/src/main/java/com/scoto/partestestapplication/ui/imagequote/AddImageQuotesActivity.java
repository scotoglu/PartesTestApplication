package com.scoto.partestestapplication.ui.imagequote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
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
import com.scoto.partestestapplication.data.model.Image;
import com.scoto.partestestapplication.ui.Dialogs;
import com.scoto.partestestapplication.ui.viewmodel.QuoteViewModel;
import com.scoto.partestestapplication.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddImageQuotesActivity extends AppCompatActivity implements View.OnFocusChangeListener {
    private static final String TAG = "AddImageQuotesActivity";

    private ImageView imageQuote;
    private ImageView focusAuthor, focusBookTitle, focusTag;
    private EditText authorTxt, bookTitleTxt, tagTxt;
    private Button saveBtn;
    private Image image;
    private Uri imageUri;
    private String uri;
    private QuoteViewModel quoteViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image_quotes);
        quoteViewModel = ViewModelProviders.of(this).get(QuoteViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setViewReference();

        getSupportActionBar().setTitle("Add Quote As Image");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle bundle = getIntent().getExtras();
        //Adding new image quote


        //update image quote


        if (bundle.getString("IMAGE_URI") != null) {
            imageUri = Uri.parse(bundle.getString("IMAGE_URI"));

            imageQuote.setImageURI(imageUri);
        } else {
            Log.d(TAG, "onCreate: ImageUri is empty");
        }

        if (bundle.getParcelable("IMAGE_OBJ") != null) {
            image = (Image) bundle.getParcelable("IMAGE_OBJ");
            setDataToFields();
        } else {
            Log.d(TAG, "onCreate: Image is empty");
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
        Log.d(TAG, "saveImageQuotes: Called");
        String image_path;
        if (imageUri != null) {
            image_path = saveImageToFileSys();
        } else {
            image_path = image.getPath();
        }

        String author = authorTxt.getText().toString();
        String bookTitle = bookTitleTxt.getText().toString();
        String tag = tagTxt.getText().toString();


        if (author.isEmpty() || bookTitle.isEmpty()) {
            Dialogs dialogs = Dialogs.newInstance(getString(R.string.empty_fields), Constants.OPERATION_CODE_EMPTY);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            dialogs.show(ft, Dialogs.TAG);
        } else {

            Log.d(TAG, "saveImageQuotes: else statement");


            if (imageUri != null) {
                Log.d(TAG, "saveImageQuotes: ImageUri");
                Image image = new Image(image_path, author, bookTitle, tag);
                quoteViewModel.insertImage(image);
                Dialogs dialogs = Dialogs.newInstance(getString(R.string.successfull_add), Constants.OPERATION_CODE_SUCCESS);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                dialogs.show(ft, Dialogs.TAG);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);

            } else if (image != null) {
                Log.d(TAG, "saveImageQuotes: image");
                if (!author.isEmpty() || !bookTitle.isEmpty() || !tag.isEmpty()) {
                    Image updatedImage = new Image(image_path, author, bookTitle, tag);
                    updatedImage.setId(image.getId());
                    quoteViewModel.updateImages(updatedImage);
                    Dialogs dialogs = Dialogs.newInstance(getString(R.string.update), Constants.OPERATION_CODE_UPDATE);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    dialogs.show(ft, Dialogs.TAG);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);
                }
            }
        }


    }

    private void setDataToFields() {
        if (image != null) {
            authorTxt.setText(image.getAuthor());
            bookTitleTxt.setText(image.getBookTitle());
            tagTxt.setText(image.getQuoteTag());
            imageQuote.setImageBitmap(BitmapFactory.decodeFile(image.getPath()));
        } else {
            Log.d(TAG, "setDataToFields: Image is Empty");
        }
    }


    private String saveImageToFileSys() {
        Log.d(TAG, "saveImageToFileSys: Active");
        String imageFileName = "Partes_" + System.currentTimeMillis() + ".jpg";
        String storageDir = this.getFilesDir().getPath();//Internal Storage
        File path = new File(storageDir, "/SavedImageQuotes");
        if (!path.exists()) {
            path.mkdirs();
        }

        File outFile = new File(path, imageFileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outFile);
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Log.d(TAG, "saveImageToFileSys: OutFile: " + outFile);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            String msg = e.getMessage();
            e.printStackTrace();
            Log.d(TAG, "saveToImage: Error.... : " + msg);
        }
        return outFile.getPath();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: AddImageQuotesActivity...");
    }

    // TODO, write a CustomView for EditText or ImageView for onFocusChangeListener
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
