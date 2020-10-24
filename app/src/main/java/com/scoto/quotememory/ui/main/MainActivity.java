package com.scoto.quotememory.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.scoto.quotememory.R;
import com.scoto.quotememory.ui.about.ActivityAbout;
import com.scoto.quotememory.ui.adapter.ViewPagerAdapter;
import com.scoto.quotememory.ui.imagequote.ImageQuotesList;
import com.scoto.quotememory.ui.textquote.QuotesList;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private QuotesList quotesList;
    private ImageQuotesList imageQuotesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        setSupportActionBar(toolbar);

        tabLayout.setupWithViewPager(viewPager);


        quotesList = new QuotesList();
        imageQuotesList = new ImageQuotesList();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);


        viewPagerAdapter.addFragment(quotesList, getApplicationContext().getResources().getString(R.string.tab_text_quote));
        viewPagerAdapter.addFragment(imageQuotesList, getApplicationContext().getResources().getString(R.string.tab_image_quote));

        viewPager.setAdapter(viewPagerAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.about:
                Intent aboutIntent = new Intent(MainActivity.this, ActivityAbout.class);
                startActivity(aboutIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}