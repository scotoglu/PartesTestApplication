package com.scoto.partestestapplication;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.scoto.partestestapplication.adapter.ViewPagerAdapter;
import com.scoto.partestestapplication.ui.ImageQuotesList;
import com.scoto.partestestapplication.ui.QuotesList;
import com.scoto.partestestapplication.viewmodel.QuoteViewModel;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FragmentManager fm;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private QuotesList quotesList;
    private ImageQuotesList imageQuotesList;
    private QuoteViewModel quoteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        quoteViewModel = ViewModelProviders.of(this).get(QuoteViewModel.class);


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
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_twotone_list_alt_24).getOrCreateBadge().setNumber(10);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_image_search_24).getOrCreateBadge().setNumber(10);
        //     getBadgeNumbers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_bar_menu, menu);
        return true;
    }


//    private void getBadgeNumbers() {
//
//        int numOfQuotes = quoteViewModel.getQuotesNum();
//        int numOfImageQuotes = quoteViewModel.getImageQuoteNum();
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_twotone_list_alt_24).getOrCreateBadge().setNumber(numOfQuotes);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_image_search_24).getOrCreateBadge().setNumber(numOfImageQuotes);
//
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(this, "Search Menu", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.setting:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.about:
                Toast.makeText(this, "About Me", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}