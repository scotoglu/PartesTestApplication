package com.scoto.partestestapplication.ui.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.scoto.partestestapplication.R;
import com.scoto.partestestapplication.data.model.Quote;
import com.scoto.partestestapplication.databinding.ListItemQuotesBinding;
import com.scoto.partestestapplication.ui.main.MainActivity;
import com.scoto.partestestapplication.ui.textquote.AddQuotesActivity;
import com.scoto.partestestapplication.ui.viewmodel.QuoteViewModel;
import com.scoto.partestestapplication.utils.StringFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class TextQuoteRecyclerViewAdapter extends RecyclerView.Adapter<TextQuoteRecyclerViewAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<Quote> quoteTextList;
    private QuoteViewModel quoteViewModel;
    private List<Quote> filteredList;
    private MyFilter filter;
    private Fragment fragment;
    private List<Quote> deleteQuery = new ArrayList<>();

    public TextQuoteRecyclerViewAdapter(Fragment fragment) {

        this.fragment = fragment;
        quoteViewModel = ViewModelProviders.of(fragment).get(QuoteViewModel.class);
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

        return quoteTextList;
    }

    public void setQuoteList(List<Quote> quoteList) {

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

        if (filter == null) {
            filteredList = new ArrayList<>();
            filteredList.clear();
            filteredList.addAll(quoteTextList);
            filter = new MyFilter(this, filteredList);
        }
        return filter;
    }

    public void setContext(Context context) {

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

            filteredList.clear();
            final FilterResults filterResults = new FilterResults();
            if (constraint.length() == 0) {
                //Searching not started,yet
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase(Locale.getDefault()).trim();
                for (Quote q : originalList) {
                    if (q.getAuthor().toLowerCase(Locale.getDefault()).contains(filterPattern)
                            || q.getBookTitle().toLowerCase(Locale.getDefault()).contains(filterPattern)) {
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

            viewAdapter.quoteTextList.clear();
            viewAdapter.quoteTextList.addAll((ArrayList<Quote>) results.values);
            viewAdapter.notifyDataSetChanged();//Without doesn't refresh list.

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ListItemQuotesBinding binding;
        private ImageView share, copy, edit, delete;

        public ViewHolder(ListItemQuotesBinding listItemQuotesBinding) {
            super(listItemQuotesBinding.getRoot());
            this.binding = listItemQuotesBinding;
            share = listItemQuotesBinding.getRoot().findViewById(R.id.shareQuote);
            copy = listItemQuotesBinding.getRoot().findViewById(R.id.copyQuote);
            edit = listItemQuotesBinding.getRoot().findViewById(R.id.editQuote);
            delete = listItemQuotesBinding.getRoot().findViewById(R.id.delete);

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, new StringFormatter().sendIntentText(quoteTextList.get(getAdapterPosition()).getQuote(), quoteTextList.get(getAdapterPosition()).getAuthor()));
                    shareIntent.setType("text/plain");
                    context.startActivity(Intent.createChooser(shareIntent, "Quote"));
                }
            });
            copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("text", quoteTextList.get(getAdapterPosition()).getQuote().toString());
                    clipboardManager.setPrimaryClip(clipData);
                    if (clipData != null)
                        Toast.makeText(context, "Quote Copied...", Toast.LENGTH_SHORT).show();

                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MainActivity mainActivity = (MainActivity) context;

                    Intent editIntent = new Intent(mainActivity, AddQuotesActivity.class);
                    editIntent.putExtra("quoteInfo", quoteTextList.get(getAdapterPosition()));
                    mainActivity.startActivity(editIntent);

                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final int pos = getAdapterPosition();
                    final Quote quote = quoteTextList.get(pos);
                    removeItem(pos, quote);

                    if (!deleteQuery.contains(quote)) {
                        deleteQuery.add(quote);
                    }

                    final Snackbar snackbar = Snackbar.make(v, "Item was removed.", 2000);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            restoreItem(quote, pos);
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
                                quoteViewModel.delete(quote);

                            }
                        }
                    });


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
