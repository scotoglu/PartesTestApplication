package com.scoto.partestestapplication.data.database;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteStatement;

import com.scoto.partestestapplication.R;
import com.scoto.partestestapplication.data.model.Image;
import com.scoto.partestestapplication.data.model.Quote;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Database(entities = {Quote.class, Image.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static String DB_NAME = "partes_test.db";
    private static Application mApplication;

    public abstract QuoteDao quoteDao();

    public abstract ImageDao imageDao();

    private static AppDatabase INSTANCE;


    protected AppDatabase() {
    }

    private static RoomDatabase.Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            insertTextQuote(db);//To prepopulate db with text quote
            insertImageQuote(db);//To prepopulate db with image quote


        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

        }
    };

    public static synchronized AppDatabase getINSTANCE(Application application) {
        mApplication = application;
        if (INSTANCE == null) {
            INSTANCE = createAppDb(application);
        }
        return INSTANCE;
    }


    private static AppDatabase createAppDb(final Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .addCallback(callback)
                .build();
    }

    private static void insertTextQuote(SupportSQLiteDatabase db) {
        String quote = mApplication.getResources().getString(R.string.default_quote);
        String author = mApplication.getResources().getString(R.string.default_author);
        String bookTitle = mApplication.getResources().getString(R.string.default_book_title);
        String publisher = mApplication.getResources().getString(R.string.default_publisher);
        String pageOfQuote = mApplication.getResources().getString(R.string.default_page_of_quote);
        String releaseDate = mApplication.getResources().getString(R.string.default_release_date);

        long created_at = getTimestamp();


        String sql = "INSERT INTO" +
                " quotes(id,quote_content,quote_author,book_title,page_of_quote,publisher,release_date,created_at)" +
                "VALUES(?,?,?,?,?,?,?,?)";

        SupportSQLiteStatement sqLiteStatement = db.compileStatement(sql);
        sqLiteStatement.bindString(2, quote);
        sqLiteStatement.bindString(3, author);
        sqLiteStatement.bindString(4, bookTitle);
        sqLiteStatement.bindString(5, pageOfQuote);
        sqLiteStatement.bindString(6, publisher);
        sqLiteStatement.bindString(7, releaseDate);
        sqLiteStatement.bindLong(8, created_at);

        sqLiteStatement.executeInsert();
        
    }

    private static void insertImageQuote(SupportSQLiteDatabase db) {
        String path = drawableToFile();
        String author = mApplication.getResources().getString(R.string.default_author);
        String bookTitle = mApplication.getResources().getString(R.string.default_book_title);
        String tag = mApplication.getResources().getString(R.string.default_img_tag);
        long created_at = getTimestamp();

        String sql = "INSERT INTO table_images(q_id,image_path,author,book_title,quote_tag,timestamp)" +
                "VALUES(?,?,?,?,?,?)";

        SupportSQLiteStatement statement = db.compileStatement(sql);
        statement.bindString(2, path);
        statement.bindString(3, author);
        statement.bindString(4, bookTitle);
        statement.bindString(5, tag);
        statement.bindLong(6, created_at);

        statement.executeInsert();

    }

    @SuppressWarnings("")
    private static String drawableToFile() {
        Drawable drawable = ContextCompat.getDrawable(mApplication, R.drawable.sample);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        String fileName = "Default_" + System.currentTimeMillis() + ".webp";
        String storageDir = mApplication.getFilesDir().getPath();//Internal Storage
        File path = new File(storageDir, "/Default");

        if (!path.exists()) {
            path.mkdirs();
        }

        File outFile = new File(path, fileName);
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outFile.getPath();
    }

    private static long getTimestamp() {
        return System.currentTimeMillis();
    }
}
