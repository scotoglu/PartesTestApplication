package com.scoto.quotememory.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.scoto.quotememory.data.model.Image;
import com.scoto.quotememory.data.repository.QuoteRepository;

import java.util.List;

public class ImageQuoteViewModel extends AndroidViewModel {

    private QuoteRepository repository;
    private LiveData<List<Image>> allImages;

    public ImageQuoteViewModel(@NonNull Application application) {
        super(application);
        repository = new QuoteRepository(application);
        allImages = repository.getAllImages();
    }

    public LiveData<List<Image>> getAllImages() {
        return allImages;
    }

    public void insert(Image image) {
        repository.insertImage(image);
    }

    public void delete(Image image) {
        repository.deleteImages(image);
    }

    public void update(Image image) {
        repository.updateImages(image);
    }

    public void deleteAll() {
        repository.deleteAllImages();
    }
}
