package com.scoto.partestestapplication.ui;

import android.content.Intent;
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
import com.scoto.partestestapplication.model.Quote;
import com.scoto.partestestapplication.viewmodel.QuoteViewModel;

public class AddQuotesActivity extends AppCompatActivity implements View.OnFocusChangeListener {
    private static final String TAG = "AddQuotesActivity";

    private EditText quoteTxt, authorTxt, bookTitleTxt, pageOfQuoteTxt, releaseDateTxt, publisherTxt;
    private String quote, author, bookTitle, pageOfQuote, releaseDate, publisher;
    private Button saveBtn;
    private ImageView quoteImageView, authorImageView, bookTitleImageView, publisherImageView, releaseDataImageView, pageOfQuoteImageView;
    private QuoteViewModel quoteViewModel;
    private Quote passedQuote = null;
    private Bundle quoteObjectBundle;
    private Dialogs dialogs;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quotes);
        quoteViewModel = ViewModelProviders.of(this).get(QuoteViewModel.class);
        Intent intent = getIntent();
        if (intent.getParcelableExtra("quoteInfo") != null) {
            passedQuote = intent.getParcelableExtra("quoteInfo");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setTitle("Add Quote");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setViewReference();
        setEditTextReference();
        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContents();
                if (passedQuote == null) {


                    if (quote.isEmpty() || author.isEmpty() || bookTitle.isEmpty()) {

                        dialogs = Dialogs.newInstance(getString(R.string.empty_fields), -1);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        dialogs.show(ft, Dialogs.TAG);
//                        Toast.makeText(AddQuotesActivity.this, "Quote, Author or Book Title can't be empty.", Toast.LENGTH_SHORT).show();


                    } else {

                        Quote data = new Quote(quote, author, bookTitle, pageOfQuote, publisher, releaseDate);
                        quoteViewModel.insert(data);

                        Log.d(TAG, "onClick: INSERT OPERATION");
                        dialogs = Dialogs.newInstance(getString(R.string.successfull_add), 1);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        dialogs.show(ft, Dialogs.TAG);
                        finish();
                    }


                } else {

                    if (quote.isEmpty() || author.isEmpty() || bookTitle.isEmpty()) {
                        dialogs = Dialogs.newInstance(getString(R.string.empty_fields), -1);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        dialogs.show(ft, Dialogs.TAG);
                    } else {
                        Quote data = new Quote(
                                quoteTxt.getText().toString(), authorTxt.getText().toString(), bookTitleTxt.getText().toString(),
                                pageOfQuoteTxt.getText().toString(), publisherTxt.getText().toString(), releaseDateTxt.getText().toString()
                        );
                        data.setId(passedQuote.getId());
                        quoteViewModel.update(data);
                        Log.d(TAG, "onClick: UPDATE OPERATION");
                        dialogs = Dialogs.newInstance("Updated", 2);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        dialogs.show(ft, Dialogs.TAG);
                        finish();
                    }


                }
            }
        });
        if (passedQuote != null)
            setDataToFields();

    }


    private void setDataToFields() {
        //When user edit the quotes,set fields with existence data.
        quoteTxt.setText(passedQuote.getQuote());
        authorTxt.setText(passedQuote.getAuthor());
        publisherTxt.setText(passedQuote.getPublisher());
        bookTitleTxt.setText(passedQuote.getBookTitle());
        pageOfQuoteTxt.setText(passedQuote.getPageOfQuote());
        releaseDateTxt.setText(passedQuote.getReleaseDate());
    }

    private void getContents() {
        //When user adding new quote,gets data from EditText
        quote = quoteTxt.getText().toString();
        author = authorTxt.getText().toString();
        bookTitle = bookTitleTxt.getText().toString();
        publisher = publisherTxt.getText().toString();
        pageOfQuote = pageOfQuoteTxt.getText().toString();
        releaseDate = releaseDateTxt.getText().toString();
    }

    private void setViewReference() {
        quoteImageView = findViewById(R.id.quoteImageView);
        authorImageView = findViewById(R.id.authorImageView);
        bookTitleImageView = findViewById(R.id.bookTitleImageView);
        publisherImageView = findViewById(R.id.publisherImageView);
        pageOfQuoteImageView = findViewById(R.id.pageOfQuoteImageView);
        releaseDataImageView = findViewById(R.id.releaseDateImageView);
    }

    private void setEditTextReference() {
        //EditTexts
        quoteTxt = findViewById(R.id.quote);
        quoteTxt.setOnFocusChangeListener(this);

        authorTxt = findViewById(R.id.author);
        authorTxt.setOnFocusChangeListener(this);

        bookTitleTxt = findViewById(R.id.bookTitle);
        bookTitleTxt.setOnFocusChangeListener(this);

        publisherTxt = findViewById(R.id.publisher);
        publisherTxt.setOnFocusChangeListener(this);

        pageOfQuoteTxt = findViewById(R.id.pageOfQuote);
        pageOfQuoteTxt.setOnFocusChangeListener(this);

        releaseDateTxt = findViewById(R.id.releaseDate);
        releaseDateTxt.setOnFocusChangeListener(this);
    }

    // TODO, write a CustomView for EditText or ImageView for onFocusChangeListener
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            Log.d(TAG, "onFocusChange: Focused...");
            switch (v.getId()) {
                case R.id.quote:
                    setImageViewParams(quoteImageView);
                    break;
                case R.id.author:
                    setImageViewParams(authorImageView);
                    break;
                case R.id.bookTitle:
                    setImageViewParams(bookTitleImageView);
                    break;
                case R.id.publisher:
                    setImageViewParams(publisherImageView);
                    break;
                case R.id.pageOfQuote:
                    setImageViewParams(pageOfQuoteImageView);
                    break;
                case R.id.releaseDate:
                    setImageViewParams(releaseDataImageView);
                    break;
                default:
                    return;
            }
        } else {
            Log.d(TAG, "onFocusChange: Loosing Focus...");
            switch (v.getId()) {
                case R.id.quote:
                    resetImageViewParams(quoteImageView);
                    break;
                case R.id.author:
                    resetImageViewParams(authorImageView);
                    break;
                case R.id.bookTitle:
                    resetImageViewParams(bookTitleImageView);
                    break;
                case R.id.publisher:
                    resetImageViewParams(publisherImageView);
                    break;
                case R.id.pageOfQuote:
                    resetImageViewParams(pageOfQuoteImageView);
                    break;
                case R.id.releaseDate:
                    resetImageViewParams(releaseDataImageView);
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
