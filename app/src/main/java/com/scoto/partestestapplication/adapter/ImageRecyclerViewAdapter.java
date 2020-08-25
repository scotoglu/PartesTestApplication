package com.scoto.partestestapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.scoto.partestestapplication.R;
import com.scoto.partestestapplication.databinding.ImageQuotesItemBinding;
import com.scoto.partestestapplication.model.Image;
import com.scoto.partestestapplication.viewmodel.QuoteViewModel;

import java.util.List;

public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "ImageRecyclerViewAdapte";
    private Context mContext;
    private List<Image> imageList;
    private QuoteViewModel quoteViewModel;


    public ImageRecyclerViewAdapter(Context context, List<Image> imageQuotes) {
        this.imageList = imageQuotes;
        this.mContext = context;
        this.quoteViewModel = ViewModelProviders.of((FragmentActivity) context).get(QuoteViewModel.class);
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
        final Image image = imageList.get(position);
        holder.bind(image);
    }

    @Override
    public int getItemCount() {
        return imageList != null ? imageList.size() : 0;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
        notifyDataSetChanged();
    }

    public void removeItem(int pos, Image image) {
        imageList.remove(pos);
        notifyItemRemoved(pos);
    }

    public void restoreItem(Image image, int pos) {
        imageList.add(pos, image);
        notifyItemInserted(pos);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageQuotesItemBinding binding;
        private ImageView delete, srcİmage;

        public ViewHolder(ImageQuotesItemBinding imageQuotesItemBinding) {
            super(imageQuotesItemBinding.getRoot());
            this.binding = imageQuotesItemBinding;
//            delete = imageQuotesItemBinding.getRoot().findViewById(R.id.delete);
            srcİmage = imageQuotesItemBinding.getRoot().findViewById(R.id.imageView);
            srcİmage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });

//            delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d(TAG, "onClick:Delete in Image List ");
//                    int pos = getAdapterPosition();
//                }
//            });
        }

        public void bind(Image image) {
            binding.setImagesQuote(image);
            binding.executePendingBindings();
        }
    }
}
