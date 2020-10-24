package com.scoto.quotememory.utils;

public class StringFormatter {


    public StringFormatter() {
        //Empty Constructor

    }

    public String strConcat(String author, String date, String pageOf) {


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
