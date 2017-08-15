package com.colinear.graphstuff;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.colinear.graphstuff.DB.Entities.ChartEntity;

import java.util.List;

public class MyDiffCallback extends DiffUtil.Callback{

    List<ChartEntity> oldData;
    List<ChartEntity> newData;

    public MyDiffCallback( List<ChartEntity> oldData,  List<ChartEntity>  newData) {
        this.newData = newData;
        this.oldData = oldData;
    }

    @Override
    public int getOldListSize() {
        return oldData.size();
    }

    @Override
    public int getNewListSize() {
        return newData.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldData.get(oldItemPosition).getTitle().equals(newData.get(newItemPosition).getTitle());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldData.get(oldItemPosition).getEntries().size() == newData.get(newItemPosition).getEntries().size();
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}