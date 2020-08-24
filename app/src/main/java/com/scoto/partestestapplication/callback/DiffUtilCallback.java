package com.scoto.partestestapplication.callback;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.scoto.partestestapplication.model.Quote;

import java.util.List;

public class DiffUtilCallback extends DiffUtil.Callback {
    List<Quote> oldList;
    List<Quote> newList;


    public DiffUtilCallback(List<Quote> oldList, List<Quote> newList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
