package com.scoto.partestestapplication.ui.imagequote;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.scoto.partestestapplication.R;
import com.scoto.partestestapplication.data.model.Image;
import com.scoto.partestestapplication.ui.adapter.ImageRecyclerViewAdapter;
import com.scoto.partestestapplication.ui.viewmodel.ImageQuoteViewModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.List;

import static android.app.Activity.RESULT_OK;


public class ImageQuotesList extends Fragment implements View.OnClickListener {

    private ProgressBar progressBar;
    private FloatingActionButton addImgQuote;
    private RecyclerView recyclerView;
    private ImageRecyclerViewAdapter viewAdapter;
    private List<Image> imageList;
    private TabLayout tabLayout;
    private FrameLayout frameLayout;
    private TextView emptyList;
    private ImageQuoteViewModel viewModel;

    private String bitmapStr;
    private MenuItem menuItem;

    public ImageQuotesList() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ImageQuoteViewModel.class);
        viewModel.getAllImages().observe(getViewLifecycleOwner(), new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> imageList) {

                viewAdapter.setImageList(imageList);
                if (imageList.size() > 0) {
                    setEmptyList(false);
                } else {
                    setEmptyList(true);
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_quotes_list, container, false);
        recyclerView = v.findViewById(R.id.imageRecyclerList);
        frameLayout = v.findViewById(R.id.imageFrameLayout);
        emptyList = v.findViewById(R.id.empty_and_add);

        emptyList.setOnClickListener(this);
        tabLayout = v.findViewById(R.id.tab_layout);
        addImgQuote = v.findViewById(R.id.addImgQuote);
        addImgQuote.setOnClickListener(this);

        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        setRecyclerView();

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri();


                Intent newIntent = new Intent(getActivity(), AddImageQuotesActivity.class);
                newIntent.putExtra("IMAGE_URI", imageUri.toString());
                startActivity(newIntent);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getContext(), "ERROR... Activity Result..", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.app_bar_menu, menu);

        final MenuItem item = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Author, BookTitle or Tag.");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewAdapter.getFilter().filter(newText);
                return true;
            }

        });


    }


    private void setRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        viewAdapter = new ImageRecyclerViewAdapter(getContext(), imageList, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.scrollToPosition(0);
        recyclerView.setAdapter(viewAdapter);

    }

    private void setEmptyList(boolean isEmptyList) {
        if (isEmptyList)
            emptyList.setVisibility(View.VISIBLE);
        else
            emptyList.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (menuItem != null) {
            menuItem.collapseActionView();
        }
    }


    @Override
    public void onClick(View v) {
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAutoZoomEnabled(true)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setAspectRatio(1, 1)
                .setNoOutputImage(false)
                .start(getContext(), ImageQuotesList.this);
    }
}