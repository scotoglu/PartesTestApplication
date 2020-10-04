package com.scoto.partestestapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.scoto.partestestapplication.adapter.ViewPagerAdapter;
import com.scoto.partestestapplication.ui.ActivityAbout;
import com.scoto.partestestapplication.ui.ImageQuotesList;
import com.scoto.partestestapplication.ui.QuotesList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private QuotesList quotesList;
    private ImageQuotesList imageQuotesList;
    SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(final String query) {
            Log.d(TAG, "onQueryTextSubmit: " + query);

            return true;
        }

        @Override
        public boolean onQueryTextChange(final String newText) {
            Log.d(TAG, "onQueryTextChange: " + newText);

            return true;
        }
    };
    private SearchView searchView;

    //    private RecyclerViewAdapter viewAdapter;
    private MenuItem menuItem;
    private int TAB_LAYOUT_POSITION_QUOTE_TEXT = 0;
    private int TAB_LAYOUT_POSITION_QUOTE_IMAGE = 1;


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        Log.d(TAG, "onCreateOptionsMenu: Called.");
//        getMenuInflater().inflate(R.menu.app_bar_menu, menu);
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        menuItem = menu.findItem(R.id.search);
//        searchView = (SearchView) menuItem.getActionView();
//        searchView.setQueryHint("Search Author or Book Title.");
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setOnQueryTextListener(onQueryTextListener);
//
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
                return true;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        viewAdapter = new RecyclerViewAdapter();


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


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                if (!searchView.isIconified()) {
//                    Log.d(TAG, "onTabSelected: ICONIFIED");
//                    menuItem.collapseActionView();
//                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: MainActivity....");
    }
}