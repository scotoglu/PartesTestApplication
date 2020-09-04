package com.scoto.partestestapplication.ui;
/*
* I have tried to use with TabLayout, the fragment started from QuoteList fragment but I failed to use properly.
* Problem is I have couldn't change fragment, overlap to previous fragment.
* After a few different ways still continue,then I have change fragment to activity.
*
*
* AddQuotesActivity is used now.
*
*
* I have spend to much time when learning after coding properly this fragment that's why I'm keeping instead of deleting.
*
*
* Sefa ÇOTOĞLU
* 24/08/2020
* * */
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.scoto.partestestapplication.R;
import com.scoto.partestestapplication.model.Quote;
import com.scoto.partestestapplication.viewmodel.QuoteViewModel;

public class AddQuotes extends Fragment implements View.OnFocusChangeListener {

    private static final String TAG = "AddQuotes";
    private EditText quoteTxt, authorTxt, bookTitleTxt, pageOfQuoteTxt, releaseDateTxt, publisherTxt;
    private String quote, author, bookTitle, pageOfQuote, releaseDate, publisher;
    private Button saveBtn;
    private ImageView quoteImageView, authorImageView, bookTitleImageView, publisherImageView, releaseDataImageView, pageOfQuoteImageView;
    private QuoteViewModel quoteViewModel;
    private Quote passedQuote;
    private Bundle quoteObjectBundle;

    public AddQuotes() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quoteViewModel = ViewModelProviders.of(this).get(QuoteViewModel.class);
        quoteObjectBundle = this.getArguments();
        if (quoteObjectBundle != null) {
            passedQuote = quoteObjectBundle.getParcelable("quoteInfo");
            /*I have used to get passed quote ID's
             * Then set the ID's to edited quote object, unless ROOM doesn't updated data. */
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_add_quotes, container, false);
        //OnFocus EditText, those ImageViews will be appear/disappear depends.
        quoteImageView = v.findViewById(R.id.quoteImageView);
        authorImageView = v.findViewById(R.id.authorImageView);
        bookTitleImageView = v.findViewById(R.id.bookTitleImageView);
        publisherImageView = v.findViewById(R.id.publisherImageView);
        pageOfQuoteImageView = v.findViewById(R.id.pageOfQuoteImageView);
        releaseDataImageView = v.findViewById(R.id.releaseDateImageView);

        //EditTexts
        quoteTxt = v.findViewById(R.id.quote);
        quoteTxt.setOnFocusChangeListener(this);

        authorTxt = v.findViewById(R.id.author);
        authorTxt.setOnFocusChangeListener(this);

        bookTitleTxt = v.findViewById(R.id.bookTitle);
        bookTitleTxt.setOnFocusChangeListener(this);

        publisherTxt = v.findViewById(R.id.publisher);
        publisherTxt.setOnFocusChangeListener(this);

        pageOfQuoteTxt = v.findViewById(R.id.pageOfQuote);
        pageOfQuoteTxt.setOnFocusChangeListener(this);
        releaseDateTxt = v.findViewById(R.id.releaseDate);
        releaseDateTxt.setOnFocusChangeListener(this);


        saveBtn = v.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContents();
                if (quoteObjectBundle == null) {
                    Quote data = new Quote(quote, author, bookTitle, pageOfQuote, publisher, releaseDate);
                    quoteViewModel.insert(data);
                    Toast.makeText(getContext(), "INSERT", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();//Close current fragment
                } else {

                    Quote data = new Quote(
                            quoteTxt.getText().toString(), authorTxt.getText().toString(), bookTitleTxt.getText().toString(),
                            pageOfQuoteTxt.getText().toString(), publisherTxt.getText().toString(), releaseDateTxt.getText().toString()
                    );
                    data.setId(passedQuote.getId());

                    quoteViewModel.update(data);
                    Toast.makeText(getContext(), "UPDATE", Toast.LENGTH_SHORT).show();

                }

            }

        });
        if (quoteObjectBundle != null)
            setDataToFields();
        return v;
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