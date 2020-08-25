package com.scoto.partestestapplication.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.scoto.partestestapplication.AddImageQuotesActivity;
import com.scoto.partestestapplication.R;
import com.scoto.partestestapplication.adapter.ImageRecyclerViewAdapter;
import com.scoto.partestestapplication.callback.SwipeToDeleteCallback;
import com.scoto.partestestapplication.helper.BitmapManager;
import com.scoto.partestestapplication.model.Image;
import com.scoto.partestestapplication.viewmodel.QuoteViewModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class ImageQuotesList extends Fragment {
    private static final String TAG = "ImageQuotesList";

    private FloatingActionButton addImgQuote;
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
        addImgQuote = v.findViewById(R.id.addImgQuote);
        addImgQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Add Image Quote Clicked...");

                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAutoZoomEnabled(true)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setAspectRatio(1, 1)
                        .setNoOutputImage(false)

                        .start(getContext(), ImageQuotesList.this);
            }
        });
        setRecyclerView();
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult: Called...");
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri();
//                CroppedImage croppedImage = new CroppedImage();
//                Bundle bundle = new Bundle();
//                bundle.putString("IMAGE_PATH", imageUri.getPath().toString());
//                croppedImage.setArguments(bundle);
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.main_content, croppedImage)
//                        .addToBackStack(null).commit();


                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), imageUri);
                    if (bitmap != null)
                        Log.d(TAG, "onActivityResult: Bitmap has a smth");
                    BitmapManager bm = new BitmapManager();
                    String bitmapStr = bm.bitmapToBase64(bitmap);
                    if (!bitmapStr.isEmpty() || bitmapStr.length() > 0) {
//                        AddImageQuotes imageQuotes = new AddImageQuotes();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("BITMAP", bitmapStr);
//                        imageQuotes.setArguments(bundle);
//                        FragmentManager fm = getActivity().getSupportFragmentManager();
//                        fm.beginTransaction().replace(R.id.main_content, imageQuotes)
//                                .addToBackStack(null)
//                                .commit();
                        Intent addImageIntent = new Intent(getActivity(), AddImageQuotesActivity.class);
                        addImageIntent.putExtra("BITMAP", bitmapStr);
                        startActivity(addImageIntent);
                    } else {
                        Log.d(TAG, "onActivityResult: Converting bitmap to string is empty...");
                    }
                    // saveToImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getContext(), "ERROR... Activity Result..", Toast.LENGTH_SHORT).show();
            }
        }
    }
//    private void saveToImage(Bitmap bitmap) {
////        String fileName = String.format("%d", System.currentTimeMillis());
//        String fileName = String.format("%s", "Quote_" + System.currentTimeMillis());
//        String filePath = getContext().getExternalFilesDir(null).getAbsolutePath();
//
//        File dir = new File(filePath + File.separator + "Quotes");
//
//        if (!dir.exists()) {
//            Log.d(TAG, "saveToImage: File created at :" + filePath);
//            dir.mkdirs();
//        }
//
//        File file = new File(dir, fileName + ".jpeg");
//        FileOutputStream fos = null;
//
//        try {
//            fos = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            Log.d(TAG, "saveToImage: Path: " + file.getPath());
//            fos.flush();
//            fos.close();
//        } catch (IOException e) {
//            String msg = e.getMessage();
//            e.printStackTrace();
//            Log.d(TAG, "saveToImage: Error.... : " + msg);
//        }
//
//
//    }


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