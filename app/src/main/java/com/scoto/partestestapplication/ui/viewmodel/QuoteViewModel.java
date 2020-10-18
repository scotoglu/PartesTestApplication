package com.scoto.partestestapplication.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.scoto.partestestapplication.data.model.Quote;
import com.scoto.partestestapplication.data.repository.QuoteRepository;

import java.util.List;

public class QuoteViewModel extends AndroidViewModel {
    private QuoteRepository repository;
    private LiveData<List<Quote>> allQuotes;
    private MutableLiveData<Boolean> isLoadingData;

    public QuoteViewModel(@NonNull Application application) {
        super(application);
        repository = new QuoteRepository(application);
        allQuotes = repository.getAllQuotes();

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

    public int getQuotesNum() {
        return repository.getQuotesNum();
    }

}
