package com.scoto.partestestapplication.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.scoto.partestestapplication.model.Image;

import java.util.List;

@Dao
public interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertImage(Image... images);

    @Update
    void updateImage(Image image);

    @Delete
    void deleteImage(Image image);


    @Query("DELETE FROM table_images")
    void deleteAllImages();

    @Query("SELECT * FROM table_images ORDER BY timestamp DESC")
    LiveData<List<Image>> getAllImages();
}
