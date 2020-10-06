package com.scoto.partestestapplication.ui.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scoto.partestestapplication.ui.main.MainActivity;
import com.scoto.partestestapplication.R;
import com.scoto.partestestapplication.databinding.ListItemQuotesBinding;
import com.scoto.partestestapplication.utils.StringFormatter;
import com.scoto.partestestapplication.data.model.Quote;
import com.scoto.partestestapplication.ui.textquote.AddQuotesActivity;

import java.util.ArrayList;
import java.util.List;


public class TextQuoteRecyclerViewAdapter extends RecyclerView.Adapter<TextQuoteRecyclerViewAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "RecyclerViewAdapter";


    private Context context;
    private List<Quote> quoteTextList;

    private List<Quote> filteredList;
    private MyFilter filter;


    public TextQuoteRecyclerViewAdapter() {
        Log.d(TAG, "RecyclerViewAdapter: Constructor...");
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
        final Quote quote = quoteTextList.get(position);
        StringFormatter stringFormatter = new StringFormatter();
        holder.bind(quote, stringFormatter);
    }

    @Override
    public int getItemCount() {
        return quoteTextList != null ? quoteTextList.size() : 0;
    }

    public List<Quote> getQuoteList() {
        Log.d(TAG, "getQuoteList: GetQuoteList");
        return quoteTextList;
    }

    public void setQuoteList(List<Quote> quoteList) {
        Log.d(TAG, "setQuoteList: Quote List Setter");
        this.quoteTextList = quoteList;
        notifyDataSetChanged();//Do not forget !!!
        /*When I have forgotten to write. Didn't add new item properly.
         * i.e. add two item, then list pretend to added only first item !!1*/
    }

    public void removeItem(int position, Quote quote) {
        quoteTextList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Quote q, int position) {
        quoteTextList.add(position, q);
        notifyItemInserted(position);
    }


    @Override
    public Filter getFilter() {
        Log.d(TAG, "getFilter: Active...");
        if (filter == null) {
            filteredList = new ArrayList<>();
            filteredList.clear();
            filteredList.addAll(quoteTextList);
            filter = new MyFilter(this, filteredList);
        }
        return filter;
    }

    public void setContext(Context context) {
        Log.d(TAG, "setContext: Context Setter");
        this.context = context;
    }

    private static class MyFilter extends Filter {

        private TextQuoteRecyclerViewAdapter viewAdapter;
        private List<Quote> originalList;
        private List<Quote> filteredList;


        private MyFilter(TextQuoteRecyclerViewAdapter adapter, List<Quote> originalList) {
            this.viewAdapter = adapter;
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
                for (Quote q : originalList) {
                    if (q.getAuthor().toLowerCase().contains(filterPattern)
                            || q.getBookTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(q);
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
            viewAdapter.quoteTextList.clear();
            viewAdapter.quoteTextList.addAll((ArrayList<Quote>) results.values);
            viewAdapter.notifyDataSetChanged();//Without doesn't refresh list.

        }
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
                    shareIntent.putExtra(Intent.EXTRA_TEXT, new StringFormatter().sendIntentText(quoteTextList.get(getAdapterPosition()).getQuote(), quoteTextList.get(getAdapterPosition()).getQuote()));
                    shareIntent.setType("text/plain");
                    context.startActivity(Intent.createChooser(shareIntent, "Quote"));
                }
            });
            copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: Copy Called Adapter Position" + getAdapterPosition());
                    ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("text", quoteTextList.get(getAdapterPosition()).getQuote().toString());
                    clipboardManager.setPrimaryClip(clipData);
                    if (clipData != null)
                        Toast.makeText(context, "Quote Copied...", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onClick: Copied Data : " + clipData.getItemAt(0));
                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: Edit Called Layout Position" + getLayoutPosition());
                    MainActivity mainActivity = (MainActivity) context;

                    Intent editIntent = new Intent(mainActivity, AddQuotesActivity.class);
                    editIntent.putExtra("quoteInfo", quoteTextList.get(getAdapterPosition()));
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

}
