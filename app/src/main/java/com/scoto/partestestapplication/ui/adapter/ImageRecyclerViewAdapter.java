package com.scoto.partestestapplication.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.scoto.partestestapplication.BuildConfig;
import com.scoto.partestestapplication.R;
import com.scoto.partestestapplication.data.model.Image;
import com.scoto.partestestapplication.databinding.ImageQuotesItemBinding;
import com.scoto.partestestapplication.ui.imagequote.AddImageQuotesActivity;
import com.scoto.partestestapplication.ui.imagequote.DisplayImageFullScreen;
import com.scoto.partestestapplication.ui.main.MainActivity;
import com.scoto.partestestapplication.ui.viewmodel.QuoteViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "ImageRecyclerViewAdapte";
    private Context mContext;
    private List<Image> imageQuotes;
    private List<Image> filteredList;
    private QuoteViewModel quoteViewModel;

    private Fragment fr;
    private MyFilter myFilter;

    public ImageRecyclerViewAdapter(Context context, List<Image> imageQuotes, Fragment fragment) {
        this.imageQuotes = imageQuotes;
        this.mContext = context;
        this.fr = fragment;
        this.filteredList = new ArrayList<>();//Initialize the filtered list
        quoteViewModel = ViewModelProviders.of(fragment).get(QuoteViewModel.class);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ImageQuotesItemBinding itemBinding = ImageQuotesItemBinding.inflate(inflater);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Image image = imageQuotes.get(position);
        holder.bind(image);
    }

    @Override
    public int getItemCount() {
        return imageQuotes != null ? imageQuotes.size() : 0;
    }

    public List<Image> getImageList() {
        return imageQuotes;
    }

    public void setImageList(List<Image> imageList) {
        this.imageQuotes = imageList;
        notifyDataSetChanged();
    }

    public void removeItem(int pos, Image image) {
        imageQuotes.remove(pos);
        notifyItemRemoved(pos);
    }

    public void restoreItem(Image image, int pos) {
        imageQuotes.add(pos, image);
        notifyItemInserted(pos);
    }

    @Override
    public Filter getFilter() {
        Log.d(TAG, "getFilter: CALLED");
        if (myFilter == null) {
            filteredList.clear();
            filteredList.addAll(imageQuotes);
            myFilter = new ImageRecyclerViewAdapter.MyFilter(this, filteredList);//Initialize filter class
        }
        return myFilter;
    }


    private static class MyFilter extends Filter {

        private ImageRecyclerViewAdapter imageRecyclerViewAdapter;
        private List<Image> originalList;
        private List<Image> filteredList;


        private MyFilter(ImageRecyclerViewAdapter adapter, List<Image> originalList) {
            this.imageRecyclerViewAdapter = adapter;
            this.originalList = originalList;
            this.filteredList = new ArrayList<>();

        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d(TAG, "performFiltering: Method CALLED.");
            filteredList.clear();
            final FilterResults filterResults = new FilterResults();
            if (constraint.length() == 0) {
                //Searching not started,yet
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (Image img : originalList) {
                    if (img.getQuoteTag().toLowerCase().contains(filterPattern)
                            || img.getAuthor().toLowerCase().contains(filterPattern)
                            || img.getBookTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(img);
                    }
                }
            }
            filterResults.values = filteredList;
            filterResults.count = filteredList.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Log.d(TAG, "publishResults: Method CALLED.");
            imageRecyclerViewAdapter.imageQuotes.clear();
            imageRecyclerViewAdapter.imageQuotes.addAll((ArrayList<Image>) results.values);
            imageRecyclerViewAdapter.notifyDataSetChanged();

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageQuotesItemBinding binding;
        private ImageView delete, srcImage, edit, share;

        public ViewHolder(final ImageQuotesItemBinding imageQuotesItemBinding) {
            super(imageQuotesItemBinding.getRoot());
            this.binding = imageQuotesItemBinding;


            edit = imageQuotesItemBinding.getRoot().findViewById(R.id.editImageQuote);
            share = imageQuotesItemBinding.getRoot().findViewById(R.id.shareImageQuote);
            delete = imageQuotesItemBinding.getRoot().findViewById(R.id.delete);
            srcImage = imageQuotesItemBinding.getRoot().findViewById(R.id.imageView);
            srcImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final ImageView imageView = imageQuotesItemBinding.getRoot().findViewById(R.id.imageView);

                    Intent displayImageIntent = new Intent(mContext, DisplayImageFullScreen.class);
                    displayImageIntent.putExtra("IMAGE_OBJ", imageQuotes.get(getAdapterPosition()));

                    if (Build.VERSION.SDK_INT > 20) {
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(fr.getActivity(),
                                imageView, ViewCompat.getTransitionName(imageView));
                        mContext.startActivity(displayImageIntent, optionsCompat.toBundle());
                    } else {
                        mContext.startActivity(displayImageIntent);
                    }

                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    MainActivity mainActivity = (MainActivity) mContext;
                    Intent editIntent = new Intent(mainActivity, AddImageQuotesActivity.class);
                    editIntent.putExtra("IMAGE_OBJ", imageQuotes.get(pos));
                    mainActivity.startActivity(editIntent);

                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    File imageFile = new File(imageQuotes.get(pos).getPath());
                    Uri uri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", imageFile);//targetSdkVersion=>24 File Provider

                    Log.d(TAG, "onClick: getPath: " + imageQuotes.get(pos).getPath());
                    Log.d(TAG, "onClick: uritoString " + uri.toString());


                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/jpg");
                    shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    mContext.startActivity(shareIntent);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int pos = getAdapterPosition();
                    final Image img = getImageList().get(pos);
                    removeItem(pos, img);
                    Snackbar snackbar = Snackbar.make(v, "Item was removed", 2000);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            restoreItem(img, pos);
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
                                quoteViewModel.deleteImages(img);
                            }
                        }
                    });
                }
            });
        }

        public void bind(Image image) {
            binding.setImagesQuote(image);
            binding.executePendingBindings();
        }
    }


}
