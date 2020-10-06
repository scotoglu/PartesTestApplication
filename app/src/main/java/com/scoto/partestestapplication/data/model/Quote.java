package com.scoto.partestestapplication.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quotes")
public class Quote implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "quote_content")
    private String quote;

    @ColumnInfo(name = "quote_author")
    private String author;

    @ColumnInfo(name = "book_title")
    private String bookTitle;

    @ColumnInfo(name = "page_of_quote")
    private String pageOfQuote;
    @ColumnInfo(name = "publisher")
    private String publisher;


    @ColumnInfo(name = "release_date")
    private String releaseDate;


    @ColumnInfo(name = "created_at")
    private long created_at;


    public Quote(String quote, String author, String bookTitle, String pageOfQuote, String publisher, String releaseDate) {
        this.quote = quote;
        this.author = author;
        this.bookTitle = bookTitle;
        this.pageOfQuote = pageOfQuote;
        this.publisher = publisher;
        this.releaseDate = releaseDate;
        this.created_at = System.currentTimeMillis();
    }

    protected Quote(Parcel in) {
        id = in.readInt();
        quote = in.readString();
        author = in.readString();
        bookTitle = in.readString();
        pageOfQuote = in.readString();
        publisher = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<Quote> CREATOR = new Creator<Quote>() {
        @Override
        public Quote createFromParcel(Parcel in) {
            return new Quote(in);
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getPageOfQuote() {
        return pageOfQuote;
    }

    public void setPageOfQuote(String pageOfQuote) {
        this.pageOfQuote = pageOfQuote;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(quote);
        dest.writeString(author);
        dest.writeString(bookTitle);
        dest.writeString(pageOfQuote);
        dest.writeString(publisher);
        dest.writeString(releaseDate);
    }
}
