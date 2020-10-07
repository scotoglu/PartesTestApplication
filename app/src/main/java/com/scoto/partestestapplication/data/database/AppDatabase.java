package com.scoto.partestestapplication.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.scoto.partestestapplication.data.model.Image;
import com.scoto.partestestapplication.data.model.Quote;

@Database(entities = {Quote.class, Image.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract QuoteDao quoteDao();

    public abstract ImageDao imageDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getINSTANCE(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "test.db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}