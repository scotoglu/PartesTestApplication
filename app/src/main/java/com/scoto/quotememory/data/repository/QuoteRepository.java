package com.scoto.quotememory.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.scoto.quotememory.data.database.AppDatabase;
import com.scoto.quotememory.data.database.ImageDao;
import com.scoto.quotememory.data.database.QuoteDao;
import com.scoto.quotememory.data.model.Image;
import com.scoto.quotememory.data.model.Quote;

import java.util.List;

public class QuoteRepository {

    private QuoteDao quoteDao;
    private ImageDao imageDao;
    private LiveData<List<Quote>> allQuotes;
    private LiveData<List<Image>> allImages;

    public QuoteRepository(Application application) {
        AppDatabase database = AppDatabase.getINSTANCE(application);
        quoteDao = database.quoteDao();
        imageDao = database.imageDao();
        allQuotes = quoteDao.getAll();
        allImages = imageDao.getAllImages();
    }

    public void insert(Quote quote) {
        quoteDao.insert(quote);
    }


    public void update(Quote quote) {
        quoteDao.update(quote);
    }


    public void delete(Quote quote) {
        quoteDao.delete(quote);
    }


    public void deleteAllQuotes() {
        quoteDao.deleteAll();
    }

    public int getQuotesNum() {
        return quoteDao.getNumOfQuotes();
    }


    public LiveData<List<Quote>> getAllQuotes() {
        return allQuotes;
    }

    /*image CRUD operations in Repository*/
    public LiveData<List<Image>> getAllImages() {
        return allImages;
    }

    public void deleteAllImages() {
        imageDao.deleteAllImages();
    }

    public void deleteImages(Image image) {
        imageDao.deleteImage(image);
    }

    public void updateImages(Image image) {
        imageDao.updateImage(image);

    }

    public void insertImage(Image image) {
        imageDao.insertImage(image);
    }

    public int getImageQuoteNum() {
        return imageDao.getNumOfImageQuotes();
    }

}
