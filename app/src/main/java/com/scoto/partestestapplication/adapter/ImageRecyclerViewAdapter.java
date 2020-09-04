package com.scoto.partestestapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.scoto.partestestapplication.R;
import com.scoto.partestestapplication.databinding.ImageQuotesItemBinding;
import com.scoto.partestestapplication.helper.BitmapManager;
import com.scoto.partestestapplication.model.Image;
import com.scoto.partestestapplication.ui.DisplayImageFullScreen;
import com.scoto.partestestapplication.viewmodel.QuoteViewModel;

import java.util.List;

public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "ImageRecyclerViewAdapte";
    private Context mContext;
    private List<Image> imageList;
    private QuoteViewModel quoteViewModel;
    private Fragment fr;

    public ImageRecyclerViewAdapter(Context context, List<Image> imageQuotes, Fragment fragment) {
        this.imageList = imageQuotes;
        this.mContext = context;
        this.fr = fragment;
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

        public ViewHolder(final ImageQuotesItemBinding imageQuotesItemBinding) {
            super(imageQuotesItemBinding.getRoot());
            this.binding = imageQuotesItemBinding;
//            delete = imageQuotesItemBinding.getRoot().findViewById(R.id.delete);
            srcİmage = imageQuotesItemBinding.getRoot().findViewById(R.id.imageView);
            srcİmage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final ImageView imageView = imageQuotesItemBinding.getRoot().findViewById(R.id.imageView);

                    BitmapManager bm = new BitmapManager();

                    byte[] image = getImageList().get(getAdapterPosition()).getImage();

                    Bitmap bitmap = bm.byteToBitmap(image);
                    String bitmapStr = bm.bitmapToBase64(bitmap);


                    Intent displayImageIntent = new Intent(mContext, DisplayImageFullScreen.class);
                    displayImageIntent.putExtra("BITMAP_STR", bitmapStr);
                    displayImageIntent.putExtra("TAG", imageList.get(getAdapterPosition()).getQuoteTag());


                    if (Build.VERSION.SDK_INT > 20) {
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(fr.getActivity(),
                                imageView, ViewCompat.getTransitionName(imageView));
                        mContext.startActivity(displayImageIntent, optionsCompat.toBundle());
                    } else {
                        mContext.startActivity(displayImageIntent);
                    }

                }
            });

//            delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d(TAG, "onClick:Delete in image List ");
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
