package com.scoto.partestestapplication.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BitmapManager {
    private static final String TAG = "BitmapManager";

    public Bitmap byteToBitmap(byte[] image) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        return bitmap;
    }

    public byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return baos.toByteArray();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    Log.e(BitmapManager.class.getSimpleName(), "ByteArrayOutputStream was not closed.");
                }
            }
        }

    }

    public String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        return Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT);
    }

    public Bitmap stringToBitmap(String encodedBitmap) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(encodedBitmap.substring(encodedBitmap.indexOf(",") + 1), Base64.DEFAULT);

        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        return bitmap;
    }
}
