package com.scoto.partestestapplication.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.scoto.partestestapplication.ui.AddQuotesActivity;
import com.scoto.partestestapplication.MainActivity;
import com.scoto.partestestapplication.R;
import com.scoto.partestestapplication.callback.DiffUtilCallback;
import com.scoto.partestestapplication.databinding.ListItemQuotesBinding;
import com.scoto.partestestapplication.helper.StringFormatter;
import com.scoto.partestestapplication.model.Quote;
import com.scoto.partestestapplication.viewmodel.QuoteViewModel;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private Context mContext;
    private List<Quote> quoteList;
    private FragmentManager fm;
    private QuoteViewModel quoteViewModel;


    public RecyclerViewAdapter(Context context, List<Quote> quoteList) {
        this.mContext = context;
        this.quoteList = quoteList;
        quoteViewModel = ViewModelProviders.of((FragmentActivity) context).get(QuoteViewModel.class);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemQuotesBinding itemQuotesBinding = ListItemQuotesBinding.inflate(inflater, parent, false);
        return new ViewHolder(itemQuotesBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Quote quote = quoteList.get(position);
        StringFormatter stringFormatter = new StringFormatter();
        holder.bind(quote, stringFormatter);
    }

    @Override
    public int getItemCount() {
        return quoteList != null ? quoteList.size() : 0;
    }

    public void setQuoteList(List<Quote> quoteList) {
        this.quoteList = quoteList;
        notifyDataSetChanged();//Do not forget !!!
        /*When I have forgotten to write. Didn't add new item properly.
         * i.e. add two item, then list pretend to added only first item !!1*/
    }

    public List<Quote> getQuoteList() {
        return quoteList;
    }

    public void removeItem(int position, Quote quote) {
        quoteList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Quote q, int position) {
        quoteList.add(position, q);
        notifyItemInserted(position);
    }

    public void updateItem(List<Quote> newList) {
        //Update Items using DiffUtil.Callback compare with old list then dispatches the new list.

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtilCallback(quoteList, newList));
        diffResult.dispatchUpdatesTo(this);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ListItemQuotesBinding binding;
        private ImageView share, copy, edit;

        public ViewHolder(ListItemQuotesBinding listItemQuotesBinding) {
            super(listItemQuotesBinding.getRoot());
            this.binding = listItemQuotesBinding;
            share = listItemQuotesBinding.getRoot().findViewById(R.id.shareQuote);
            copy = listItemQuotesBinding.getRoot().findViewById(R.id.copyQuote);
            edit = listItemQuotesBinding.getRoot().findViewById(R.id.editQuote);

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: Share Called..");
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, new StringFormatter().sendIntentText(quoteList.get(getAdapterPosition()).getQuote(),quoteList.get(getAdapterPosition()).getQuote()));
                    shareIntent.setType("text/plain");
                    mContext.startActivity(Intent.createChooser(shareIntent, "Quote"));
                }
            });
            copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: Copy Called Adapter Position" + getAdapterPosition());
                    ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("text", quoteList.get(getAdapterPosition()).getQuote().toString());
                    clipboardManager.setPrimaryClip(clipData);
                    if (clipData != null)
                        Toast.makeText(mContext, "Quote Copied...", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onClick: Copied Data : " + clipData.getItemAt(0));
                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: Edit Called Layout Position" + getLayoutPosition());
                    MainActivity mainActivity = (MainActivity) mContext;

//                    Bundle quoteObjectBundle = new Bundle();
//                    quoteObjectBundle.putParcelable("quoteInfo", quoteList.get(getAdapterPosition()));
                    Intent editIntent = new Intent(mainActivity, AddQuotesActivity.class);
                    editIntent.putExtra("quoteInfo",quoteList.get(getAdapterPosition()));
                    mainActivity.startActivity(editIntent);
                    //  mainActivity.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

                }
            });
        }

        public void bind(Quote quote, StringFormatter formatter) {
            binding.setItem(quote);
            binding.setStrFormat(formatter);
            binding.executePendingBindings();

        }
    }

//    private void passFragment(Fragment fr, FragmentManager fm) {
//        fm.beginTransaction().replace(R.id.main_content, fr)
//                .addToBackStack(null)
//                .commit();
//    }

}
