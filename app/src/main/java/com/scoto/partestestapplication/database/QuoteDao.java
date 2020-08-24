package com.scoto.partestestapplication.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.scoto.partestestapplication.model.Quote;

import java.util.List;

@Dao
public interface QuoteDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Quote quote);

    @Query("SELECT * FROM quotes ORDER BY created_at DESC")
    LiveData<List<Quote>> getAll();

    @Query("DELETE FROM quotes")
    void deleteAll();

    @Update
    void update(Quote... quote);

    @Delete
    void delete(Quote quote);



}
