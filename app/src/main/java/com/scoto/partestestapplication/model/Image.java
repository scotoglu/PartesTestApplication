package com.scoto.partestestapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_images")
public class Image implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "q_id")
    private int id;

    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    @ColumnInfo(name = "author")
    private String author;

    @ColumnInfo(name = "book_title")
    private String bookTitle;

    @ColumnInfo(name = "quote_tag")
    private String quoteTag;

    @ColumnInfo(name = "timestamp")
    private long timestamp;


    public Image(byte[] image, String author, String bookTitle, String quoteTag) {
        this.image = image;
        this.bookTitle = bookTitle;
        this.author = author;
        this.quoteTag = quoteTag;
        this.timestamp = System.currentTimeMillis();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    protected Image(Parcel in) {
        id = in.readInt();
        image = in.createByteArray();
        author = in.readString();
        bookTitle = in.readString();
        quoteTag = in.readString();
        timestamp = in.readLong();
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getQuoteTag() {
        return quoteTag;
    }

    public void setQuoteTag(String quoteTag) {
        this.quoteTag = quoteTag;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByteArray(image);
        dest.writeString(author);
        dest.writeString(bookTitle);
        dest.writeString(quoteTag);
        dest.writeLong(timestamp);
    }
}
