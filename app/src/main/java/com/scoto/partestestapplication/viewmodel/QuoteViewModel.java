package com.scoto.partestestapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.scoto.partestestapplication.model.Image;
import com.scoto.partestestapplication.model.Quote;
import com.scoto.partestestapplication.repository.QuoteRepository;

import java.util.List;

public class QuoteViewModel extends AndroidViewModel {
    private QuoteRepository repository;
    private LiveData<List<Quote>> allQuotes;
    private LiveData<List<Image>> allImages;


    public QuoteViewModel(@NonNull Application application) {
        super(application);
        repository = new QuoteRepository(application);
        allQuotes = repository.getAllQuotes();
        allImages = repository.getAllImages();
    }

    public void insert(Quote quote) {
        repository.insert(quote);
    }

    public void update(Quote quote) {
        repository.update(quote);
    }

    public void delete(Quote quote) {
        repository.delete(quote);
    }

    public void deleteAll() {
        repository.deleteAllQuotes();
    }


    public LiveData<List<Quote>> getAllQuotes() {
        return allQuotes;
    }

    /*  Image CRUD operations in ViewModel*/

    public void deleteAllImages() {
        repository.deleteAllImages();
    }

    public void deleteImages(Image image) {
        repository.deleteImages(image);
    }

    public void updateImages(Image image) {
        repository.updateImages(image);
    }

    public void insertImage(Image image) {
        repository.insertImage(image);
    }

    public LiveData<List<Image>> getAllImages() {
        return allImages;
    }
}
