package com.scoto.partestestapplication.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.scoto.partestestapplication.R;
import com.scoto.partestestapplication.ui.about.ActivityAbout;
import com.scoto.partestestapplication.ui.adapter.ViewPagerAdapter;
import com.scoto.partestestapplication.ui.imagequote.ImageQuotesList;
import com.scoto.partestestapplication.ui.textquote.QuotesList;


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


        viewPagerAdapter.addFragment(quotesList, "Quotes");
        viewPagerAdapter.addFragment(imageQuotesList, "image List");

        viewPager.setAdapter(viewPagerAdapter);


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.about:
                Intent aboutIntent = new Intent(MainActivity.this, ActivityAbout.class);
                startActivity(aboutIntent);
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}