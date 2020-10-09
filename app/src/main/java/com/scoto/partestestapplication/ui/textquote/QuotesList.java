package com.scoto.partestestapplication.ui.textquote;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.scoto.partestestapplication.R;
import com.scoto.partestestapplication.data.model.Quote;
import com.scoto.partestestapplication.ui.adapter.TextQuoteRecyclerViewAdapter;
import com.scoto.partestestapplication.ui.viewmodel.QuoteViewModel;

import java.util.List;


public class QuotesList extends Fragment implements View.OnClickListener {
    private static final String TAG = "QuotesList";

    private FloatingActionButton addTxtQuote;
    private RecyclerView recyclerView;
    private TextQuoteRecyclerViewAdapter viewAdapter;
    private List<Quote> quotes;
    private QuoteViewModel quoteViewModel;
    private FrameLayout frameLayout;

    private LinearLayout emptyList;
    private MenuItem menuItem;
    private SearchView searchView;


    public QuotesList() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ACTIVE");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ACTIVE");
        View v = inflater.inflate(R.layout.fragment_quotes_list, container, false);
        recyclerView = v.findViewById(R.id.recyclerView);
        emptyList = v.findViewById(R.id.empty_and_add);
        emptyList.setOnClickListener(this);

        addTxtQuote = v.findViewById(R.id.addTxtQuote);

        frameLayout = v.findViewById(R.id.frameLayout);

        addTxtQuote.setOnClickListener(this);

        setRecyclerViewList();

        return v;
    }


    private void setRecyclerViewList() {
        Log.d(TAG, "setRecyclerViewList: Active...");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        viewAdapter = new TextQuoteRecyclerViewAdapter(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.scrollToPosition(0);
        recyclerView.setAdapter(viewAdapter);

    }

    private void setEmptyList(boolean isVisible) {
        if (!isVisible)
            emptyList.setVisibility(View.VISIBLE);
        else
            emptyList.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        quoteViewModel = ViewModelProviders.of(this).get(QuoteViewModel.class);
        quoteViewModel.getAllQuotes().observe(getViewLifecycleOwner(), new Observer<List<Quote>>() {
            @Override
            public void onChanged(List<Quote> quoteList) {
                Log.d(TAG, "onChanged: Observe Called...");
                viewAdapter.setQuoteList(quoteList);
                viewAdapter.setContext(getContext());
                viewAdapter.notifyDataSetChanged();

                if (quoteList.size() <= 0)
                    setEmptyList(false);
                else
                    setEmptyList(true);

            }
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.app_bar_menu, menu);

        final MenuItem item = menu.findItem(R.id.search);
        searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search Author or BookTitle.");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: CALLED....");

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange: CALLED...");
                viewAdapter.getFilter().filter(newText);
                return true;
            }

        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        if (menuItem != null) {
            menuItem.collapseActionView();
        }
    }

    @Override
    public void onClick(View v) {
        Intent addQuoteIntent = new Intent(getActivity(), AddQuotesActivity.class);
        startActivity(addQuoteIntent);
    }
}
