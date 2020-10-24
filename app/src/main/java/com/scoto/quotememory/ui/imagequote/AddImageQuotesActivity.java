package com.scoto.quotememory.ui.imagequote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
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

import com.scoto.quotememory.R;
import com.scoto.quotememory.data.model.Image;
import com.scoto.quotememory.ui.Dialogs;
import com.scoto.quotememory.ui.viewmodel.ImageQuoteViewModel;
import com.scoto.quotememory.utils.OperationTypeDef;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddImageQuotesActivity extends AppCompatActivity implements View.OnFocusChangeListener {


    private ImageView imageQuote;
    private ImageView focusAuthor, focusBookTitle, focusTag;
    private EditText authorTxt, bookTitleTxt, tagTxt;
    private Button saveBtn;
    private Image image;
    private Uri imageUri;
    private String uri;
    private ImageQuoteViewModel viewModel;
    private Handler handler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image_quotes);
        viewModel = ViewModelProviders.of(this).get(ImageQuoteViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setViewReference();

        getSupportActionBar().setTitle(getApplicationContext().getResources().getString(R.string.actionbar_image_add_quote));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle bundle = getIntent().getExtras();

        handler = new Handler();
        //Adding
        if (bundle.getString("IMAGE_URI") != null) {
            imageUri = Uri.parse(bundle.getString("IMAGE_URI"));
            imageQuote.setImageURI(imageUri);
        }


        //Editing
        if (bundle.getParcelable("IMAGE_OBJ") != null) {
            image = (Image) bundle.getParcelable("IMAGE_OBJ");
            setDataToFields();
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
            final Dialogs dialogs = Dialogs.newInstance(getString(R.string.empty_fields), OperationTypeDef.EMPTY_FIELD);


            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            dialogs.show(ft, Dialogs.TAG);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialogs.dismiss();
                }
            }, 2500);

        } else {


            if (imageUri != null) {
                Image image = new Image(image_path, author, bookTitle, tag);
                viewModel.insert(image);
                Dialogs dialogs = Dialogs.newInstance(getString(R.string.successfull_add), OperationTypeDef.SUCCESS);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                dialogs.show(ft, Dialogs.TAG);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);


            } else if (image != null) {

                if (!author.isEmpty() || !bookTitle.isEmpty() || !tag.isEmpty()) {
                    Image updatedImage = new Image(image_path, author, bookTitle, tag);
                    updatedImage.setId(image.getId());
                    viewModel.update(updatedImage);
                    Dialogs dialogs = Dialogs.newInstance(getString(R.string.update), OperationTypeDef.UPDATE);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    dialogs.show(ft, Dialogs.TAG);
                    handler.postDelayed(new Runnable() {
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
        }
    }

    private String saveImageToFileSys() {

        String imageFileName = "Partes_" + System.currentTimeMillis() + ".webp";
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
            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outFile.getPath();

    }


    // TODO, write a CustomView for EditText or ImageView for onFocusChangeListener
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler = null;
        }
    }
}
