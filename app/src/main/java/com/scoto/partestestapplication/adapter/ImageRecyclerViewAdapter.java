package com.scoto.partestestapplication.adapter;

import android.content.Context;
import android.content.Intent;
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
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.scoto.partestestapplication.R;
import com.scoto.partestestapplication.databinding.ImageQuotesItemBinding;
import com.scoto.partestestapplication.model.Image;
import com.scoto.partestestapplication.ui.DisplayImageFullScreen;

import java.util.ArrayList;
import java.util.List;

public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "ImageRecyclerViewAdapte";
    private Context mContext;
    private List<Image> imageQuotes;
    private List<Image> filteredList;

    private Fragment fr;
    private ImageRecyclerViewAdapter imageRecyclerViewAdapter;
    private MyFilter myFilter;

    public ImageRecyclerViewAdapter(Context context, List<Image> imageQuotes, Fragment fragment) {
        this.imageQuotes = imageQuotes;
        this.mContext = context;
        this.fr = fragment;
        this.filteredList = new ArrayList<>();//Initialize the filtered list

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
                    if (img.getQuoteTag().toLowerCase().contains(filterPattern)) {
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
        private ImageView delete, srcİmage;

        public ViewHolder(final ImageQuotesItemBinding imageQuotesItemBinding) {
            super(imageQuotesItemBinding.getRoot());
            this.binding = imageQuotesItemBinding;
            srcİmage = imageQuotesItemBinding.getRoot().findViewById(R.id.imageView);
            srcİmage.setOnClickListener(new View.OnClickListener() {
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

        }

        public void bind(Image image) {
            binding.setImagesQuote(image);
            binding.executePendingBindings();
        }
    }


}
