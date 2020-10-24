package com.scoto.quotememory.utils;

import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.Locale;

public class BindingAdapters {

    @BindingAdapter("setImage")
    public static void setImageViewResource(ImageView imageView, String path) {

        if (!path.isEmpty()) {
//            Bitmap bitmap = BitmapFactory.decodeFile(path);//API level 24 can cause OutOfMemory exception to handle use Glide.
            Glide.with(imageView.getContext()).load(path).fitCenter().into(imageView);
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
            customText.setText("#" + tag.toLowerCase(Locale.getDefault()));
            customText.setTextColor(Color.RED);
        } else {
            customText.setText(" ");
        }

    }


    @BindingAdapter("setPhotoViewImage")
    public static void setPhotoViewImage(PhotoView photoView, String path) {

        Glide.with(photoView.getContext()).load(path).fitCenter().into(photoView);

    }


}

