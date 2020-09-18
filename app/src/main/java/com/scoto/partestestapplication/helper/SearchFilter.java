package com.scoto.partestestapplication.helper;

import android.widget.Filter;

import com.scoto.partestestapplication.adapter.RecyclerViewAdapter;
import com.scoto.partestestapplication.model.Image;
import com.scoto.partestestapplication.model.Quote;

import java.util.ArrayList;
import java.util.List;

public class SearchFilter<T> extends Filter {


    private List<T> originalList;
    private List<T> filteredList;
    private RecyclerViewAdapter viewAdapter;

    public SearchFilter(RecyclerViewAdapter viewAdapter, List<T> originalList) {
        this.viewAdapter = viewAdapter;
        this.originalList = originalList;
        this.filteredList = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        final FilterResults filterResults = new FilterResults();
        if (constraint.length() == 0) {
            filteredList.addAll(originalList);
        } else {
            final String searchPattern = constraint.toString().toLowerCase();
            for (T t : originalList) {
                if (t instanceof Quote) {
                    Quote quote = (Quote) t;
                    if (quote.getAuthor().toLowerCase().contains(searchPattern)
                            || quote.getBookTitle().toLowerCase().contains(searchPattern)) {
                        filteredList.add(t);
                    }
                } else if (t instanceof Image) {

                    Image image = (Image) t;
                    if (image.getQuoteTag().toLowerCase().contains(searchPattern)) {
                        filteredList.add(t);
                    }
                }
            }
        }

        filterResults.values = filteredList;
        filterResults.count = filteredList.size();
        return filterResults;

    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        viewAdapter.getQuoteList().clear();
        viewAdapter.getQuoteList().addAll((ArrayList<Quote>) results.values);
        viewAdapter.notifyDataSetChanged();

    }
}
