package com.scoto.partestestapplication.utils;

import android.util.Log;

public class StringFormatter {
    private static final String TAG = "StringFormatter";


    public StringFormatter() {
        //Empty Constructor
        Log.d(TAG, "StringFormatter: Called");
    }

    public String strConcat(String author, String date, String pageOf) {
        Log.d(TAG, "strConcat: Method Called...");

        String dateDisplay, authorDisplay, pageOfDisplay;

        authorDisplay = author + " ,";
        dateDisplay = "(" + date + ")" + " ,";
        pageOfDisplay = "s. " + pageOf;

        if (author.isEmpty() || author == null) {
            authorDisplay = "";
        }
        if (date.isEmpty() || date == null) {
            dateDisplay = "";
        }
        if (pageOf.isEmpty() || pageOf == null) {
            pageOfDisplay = "";
        }


        return authorDisplay + dateDisplay + pageOfDisplay;
    }


    public String sendIntentText(String quote, String author) {

        return "\u201c" + quote + "\u201d\n" + "    " + author;

    }


}
