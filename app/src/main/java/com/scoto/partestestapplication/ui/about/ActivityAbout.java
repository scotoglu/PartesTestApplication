package com.scoto.partestestapplication.ui.about;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.scoto.partestestapplication.BuildConfig;
import com.scoto.partestestapplication.R;

public class ActivityAbout extends AppCompatActivity {
    private TextView versionTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("About App");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        versionTxt = findViewById(R.id.version);

        String versionName = BuildConfig.VERSION_NAME;
        versionTxt.setText(versionName);


    }
}