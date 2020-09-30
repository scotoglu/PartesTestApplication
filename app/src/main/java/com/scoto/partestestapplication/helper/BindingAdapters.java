package com.scoto.partestestapplication.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class BindingAdapters {

    @BindingAdapter("setImage")
    public static void setImageViewResource(ImageView imageView, byte[] image) {

        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

        imageView.setImageBitmap(bitmap);
    }

    @BindingAdapter(value = {"author", "bookTitle"}, requireAll = false)
    public static void setDisplayData(TextView textView, String author, String bookTitle) {
        String texts = null;
        if (!author.isEmpty() && !bookTitle.isEmpty())
            texts = author + ", " + bookTitle;
        else if (author.isEmpty())
            texts = "" + bookTitle;
        else
            texts = author + "";

        textView.setText(texts);
    }

}

