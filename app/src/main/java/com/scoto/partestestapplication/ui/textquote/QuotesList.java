package com.scoto.partestestapplication.ui.textquote;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.scoto.partestestapplication.R;
import com.scoto.partestestapplication.ui.adapter.TextQuoteRecyclerViewAdapter;
import com.scoto.partestestapplication.ui.adapter.callback.SwipeToDeleteCallback;
import com.scoto.partestestapplication.data.model.Quote;
import com.scoto.partestestapplication.ui.viewmodel.QuoteViewModel;

import java.util.List;


public class QuotesList extends Fragment {
    private static final String TAG = "QuotesList";

    private FloatingActionButton addTxtQuote;
    private Animation fabOpen, fabClose, rotateBackward, rotateForward;
    private RecyclerView recyclerView;
    private TextQuoteRecyclerViewAdapter viewAdapter;
    private List<Quote> quotes;
    private QuoteViewModel quoteViewModel;
    private FrameLayout frameLayout;
    private boolean isOpen;
    private TextView emptyList;
    private MenuItem menuItem;
    private SearchView searchView;


    public QuotesList() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ACTIVE");
        super.onCreate(savedInstanceState);
        fabOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        rotateBackward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_backward);
        rotateForward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_forward);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ACTIVE");
        View v = inflater.inflate(R.layout.fragment_quotes_list, container, false);
        recyclerView = v.findViewById(R.id.recyclerView);
        emptyList = v.findViewById(R.id.emptyListText);

        addTxtQuote = v.findViewById(R.id.addTxtQuote);

        frameLayout = v.findViewById(R.id.frameLayout);

        addTxtQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addQuoteIntent = new Intent(getActivity(), AddQuotesActivity.class);
                startActivity(addQuoteIntent);

            }
        });

        setRecyclerViewList();
        return v;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void setRecyclerViewList() {
        Log.d(TAG, "setRecyclerViewList: Active...");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        viewAdapter = new TextQuoteRecyclerViewAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.scrollToPosition(0);
        recyclerView.setAdapter(viewAdapter);
        swipeToDelete();

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

                //  recyclerView.scrollToPosition(quoteList.size() - 1);
            }
        });
    }


    private void swipeToDelete() {
        /*First remove from recyclerview then a snackbar appears. When snackbar dismissed then object deleted from database.*/
        Log.d(TAG, "swipeToDelete: ");
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                super.onSwiped(viewHolder, direction);
                final int position = viewHolder.getAdapterPosition();
                final Quote q = viewAdapter.getQuoteList().get(position);
                viewAdapter.removeItem(position, q);//Remove from only list

                Snackbar snackbar = Snackbar.make(frameLayout, getResources().getString(R.string.snackbar_item_remove), Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewAdapter.restoreItem(q, position);
                        recyclerView.scrollToPosition(position);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
                snackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onShown(Snackbar sb) {
                        super.onShown(sb);
                        Log.d(TAG, "onShown: Snackbar on shown");
                    }

                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                            quoteViewModel.delete(q);
                        }
                    }
                });
            }

        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
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

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Fragment onStopped");
        if (menuItem != null) {
            menuItem.collapseActionView();
        }


    }
}
