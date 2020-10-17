package com.scoto.partestestapplication.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.scoto.partestestapplication.R;
import com.scoto.partestestapplication.ui.main.MainActivity;

import java.io.File;

public class SplashScreenActivity extends AppCompatActivity {


    private static final String TAG = "SplashScreenActivity";
    private ImageView splashLogo;
    private TextView title, dev;
    private Animation top, bottom;
    private static int DURATION = 4000;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        handler = new Handler();


        hideSystemUI();


        splashLogo = findViewById(R.id.splash);
        title = findViewById(R.id.app_name);
        dev = findViewById(R.id.dev_scoto);

        top = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);


        splashLogo.setAnimation(top);
        title.setAnimation(bottom);
        dev.setAnimation(bottom);


//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//                clearCroppedImageCache();
//            }
//        });
        clearCroppedImageCache();

//        if (!SharedPreferencesUtils.getPrefValue(this, "FIRST_TIME")) {
//            SharedPreferencesUtils.setPrefValues(this, "FIRST_TIME", true);
//            Log.d(TAG, "onCreate: FIRST TIME");
//            populateDb();
//        }


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, DURATION);
    }

    private void hideSystemUI() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    private void clearCroppedImageCache() {
        //Android-Image-Cropper uses cache when image cropped and never delete itself
        //To avoid size matter, every app starts, clears image cache
        File cacheDir = getCacheDir();
        if (cacheDir != null) {
            File[] files = cacheDir.listFiles();

            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().startsWith("cropped")) {
                    files[i].delete();
                }
            }

        }
    }

//    private void populateDb() {
//        AppDatabase appDatabase = AppDatabase.getINSTANCE(this);
//        appDatabase.quoteDao().insert(insertTextQuote());
//        appDatabase.imageDao().insertImage(insertImageQuote());
//    }
//
//    private Image insertImageQuote() {
//        String path = drawableToFile();
//        String author = getApplicationContext().getResources().getString(R.string.default_author);
//        String bookTitle = getApplicationContext().getResources().getString(R.string.default_book_title);
//        String tag = getApplicationContext().getResources().getString(R.string.default_img_tag);
//
//        return new Image(path, author, bookTitle, tag);
//
//    }
//
//    private Quote insertTextQuote() {
//        String quote = getApplicationContext().getResources().getString(R.string.default_quote);
//        String author = getApplicationContext().getResources().getString(R.string.default_author);
//        String bookTitle = getApplicationContext().getResources().getString(R.string.default_book_title);
//        String publisher = getApplicationContext().getResources().getString(R.string.default_publisher);
//        String pageOfQuote = getApplicationContext().getResources().getString(R.string.default_page_of_quote);
//        String releaseDate = getApplicationContext().getResources().getString(R.string.default_release_date);
//
//        return new Quote(quote, author, bookTitle, pageOfQuote, publisher, releaseDate);
//    }
//
//
//    private String drawableToFile() {
//        Drawable drawable = getApplicationContext().getDrawable(R.drawable.sample);
//        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//        String fileName = "Default_" + System.currentTimeMillis() + ".webp";
//        String storageDir = getApplicationContext().getFilesDir().getPath();//Internal Storage
//        File path = new File(storageDir, "/Default");
//
//        if (!path.exists()) {
//            path.mkdirs();
//        }
//
//        File outFile = new File(path, fileName);
//        FileOutputStream fos = null;
//
//        try {
//            fos = new FileOutputStream(outFile);
//            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, fos);
//            fos.flush();
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return outFile.getPath();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(null);
    }
}