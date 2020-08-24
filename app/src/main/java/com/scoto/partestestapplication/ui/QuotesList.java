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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.google.android.material.snackbar.Snackbar;
import com.scoto.partestestapplication.AddImageQuotesActivity;
import com.scoto.partestestapplication.AddQuotesActivity;
import com.scoto.partestestapplication.R;
import com.scoto.partestestapplication.adapter.RecyclerViewAdapter;
import com.scoto.partestestapplication.callback.SwipeToDeleteCallback;
import com.scoto.partestestapplication.helper.BitmapManager;
import com.scoto.partestestapplication.model.Quote;
import com.scoto.partestestapplication.viewmodel.QuoteViewModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class QuotesList extends Fragment {
    private static final String TAG = "QuotesList";

    private FloatingActionButton fab, addTxt, addCam;
    private Animation fabOpen, fabClose, rotateBackward, rotateForward;
    private List<Quote> quoteList;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter viewAdapter;
    private QuoteViewModel quoteViewModel;
    private FrameLayout frameLayout;
    private boolean isOpen;
    private TextView emptyList;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ACTIVE");
        View v = inflater.inflate(R.layout.fragment_quotes_list, container, false);
        recyclerView = v.findViewById(R.id.recyclerView);
        emptyList = v.findViewById(R.id.emptyListText);
        fab = v.findViewById(R.id.fab);
        addTxt = v.findViewById(R.id.addTxt);
        addCam = v.findViewById(R.id.cam);
        frameLayout = v.findViewById(R.id.frameLayout);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
            }
        });
        addTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AddQuotes addQuoteFragment = new AddQuotes();
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                fm.beginTransaction()
//                        .replace(R.id.main_content, addQuoteFragment)
//                        .addToBackStack(null)
//                        .commit();
                Intent addQuoteIntent = new Intent(getActivity(), AddQuotesActivity.class);
                startActivity(addQuoteIntent);
            }
        });
        addCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(getActivity());
                 * .start(getActivity()) is only if you are calling from activity.
                 * In this case, I'm calling from fragment so use .start(getContext(),QuoteList.this)
                 * otherwise onActivityResult doesn't called.
                 * */
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAutoZoomEnabled(true)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setAspectRatio(1, 1)
                        .setNoOutputImage(false)

                        .start(getContext(), QuotesList.this);
            }
        });

        setRecyclerViewList();
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
                    saveToImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getContext(), "ERROR... Activity Result..", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveToImage(Bitmap bitmap) {
//        String fileName = String.format("%d", System.currentTimeMillis());
        String fileName = String.format("%s", "Quote_" + System.currentTimeMillis());
        String filePath = getContext().getExternalFilesDir(null).getAbsolutePath();

        File dir = new File(filePath + File.separator + "Quotes");

        if (!dir.exists()) {
            Log.d(TAG, "saveToImage: File created at :" + filePath);
            dir.mkdirs();
        }

        File file = new File(dir, fileName + ".jpeg");
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Log.d(TAG, "saveToImage: Path: " + file.getPath());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            String msg = e.getMessage();
            e.printStackTrace();
            Log.d(TAG, "saveToImage: Error.... : " + msg);
        }


    }

    private void setRecyclerViewList() {
        Log.d(TAG, "setRecyclerViewList: Active...");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        viewAdapter = new RecyclerViewAdapter(getContext(), quoteList);
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
                if (quoteList.size() <= 0)
                    setEmptyList(false);
                else
                    setEmptyList(true);

                viewAdapter.setQuoteList(quoteList);

                //  recyclerView.scrollToPosition(quoteList.size() - 1);
            }
        });
    }


    private void animateFab() {
        if (isOpen) {
            fab.startAnimation(rotateForward);
            addTxt.startAnimation(fabClose);
            addCam.startAnimation(fabClose);
            addTxt.setClickable(false);
            addCam.setClickable(false);
            isOpen = false;
        } else {
            fab.startAnimation(rotateBackward);
            addTxt.startAnimation(fabOpen);
            addCam.startAnimation(fabOpen);
            addTxt.setClickable(true);
            addCam.setClickable(true);
            isOpen = true;
        }
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
                        Log.d(TAG, "onDismissed: Snackbar on dismissed");
                        if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                            Log.d(TAG, "onDismissed: TIMEOUT");
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
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Called");
    }
}
