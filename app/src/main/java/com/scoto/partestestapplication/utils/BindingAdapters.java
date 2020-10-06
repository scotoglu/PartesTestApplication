package com.scoto.partestestapplication.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.github.chrisbanes.photoview.PhotoView;

public class BindingAdapters {

    @BindingAdapter("setImage")
    public static void setImageViewResource(ImageView imageView, String path) {

        if (!path.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            imageView.setImageBitmap(bitmap);
            imageView.setAdjustViewBounds(true);
        }

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

    @BindingAdapter("setTag")
    public static void setCustomText(TextView customText, String tag) {
        if (!tag.isEmpty()) {
            customText.setText("#" + tag.toLowerCase());
            customText.setTextColor(Color.RED);
        } else {
            customText.setText(" ");
        }

    }


    @BindingAdapter("setPhotoViewImage")
    public static void setPhotoViewImage(PhotoView photoView, String path) {

        Bitmap bitmap = BitmapFactory.decodeFile(path);
        photoView.setImageBitmap(bitmap);
        photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        photoView.setAdjustViewBounds(true);
    }


}

