package com.scoto.partestestapplication.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.scoto.partestestapplication.R;
import com.scoto.partestestapplication.adapter.ImageRecyclerViewAdapter;
import com.scoto.partestestapplication.callback.SwipeToDeleteCallback;
import com.scoto.partestestapplication.model.Image;
import com.scoto.partestestapplication.viewmodel.QuoteViewModel;

import java.util.List;


public class ImageQuotesList extends Fragment {
    private static final String TAG = "ImageQuotesList";


    private RecyclerView recyclerView;
    private ImageRecyclerViewAdapter viewAdapter;
    private List<Image> imageList;
    private FrameLayout frameLayout;
    private TextView emptyList;
    private QuoteViewModel quoteViewModel;

    public ImageQuotesList() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        quoteViewModel = ViewModelProviders.of(this).get(QuoteViewModel.class);
        quoteViewModel.getAllImages().observe(getViewLifecycleOwner(), new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> imageList) {

                viewAdapter.setImageList(imageList);
                if (imageList.size() > 0)
                    setEmptyList(false);
                else
                    setEmptyList(true);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_quotes_list, container, false);
        recyclerView = v.findViewById(R.id.imageRecyclerList);
        frameLayout = v.findViewById(R.id.imageFrameLayout);
        emptyList = v.findViewById(R.id.imageEmptyList);
        setRecyclerView();
        return v;
    }

    private void setRecyclerView() {

        Log.d(TAG, "setRecyclerView: setRecyclerView Active ...");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        viewAdapter = new ImageRecyclerViewAdapter(getContext(), imageList);
        recyclerView.setHasFixedSize(true);
        recyclerView.scrollToPosition(0);
        recyclerView.setAdapter(viewAdapter);
        swipeToDelete();
    }

    private void swipeToDelete() {
        Log.d(TAG, "swipeToDelete: SwipeToDelete Active...");
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                super.onSwiped(viewHolder, direction);
                final int pos = viewHolder.getAdapterPosition();
                final Image image = viewAdapter.getImageList().get(pos);
                viewAdapter.removeItem(pos, image);

                Snackbar snackbar = Snackbar.make(frameLayout, "Item was removed.", BaseTransientBottomBar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewAdapter.restoreItem(image, pos);
                        recyclerView.scrollToPosition(pos);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
                snackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onShown(Snackbar sb) {
                        super.onShown(sb);
                    }

                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                            quoteViewModel.deleteImages(image);
                        }
                    }
                });
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setEmptyList(boolean isEmptyList) {
        if (isEmptyList)
            emptyList.setVisibility(View.VISIBLE);
        else
            emptyList.setVisibility(View.INVISIBLE);

    }
}