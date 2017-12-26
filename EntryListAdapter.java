package com.colinear.graphstuff;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.colinear.graphstuff.DB.Entities.ChartEntity;
import com.colinear.graphstuff.DB.Entities.EntryEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class EntryListAdapter extends RecyclerView.Adapter<EntryListAdapter.ViewHolder> {

    List<EntryEntity> entries = new ArrayList<>();

    private int highlightedIndex;
    private Context context;
    private int colorScheme= Const.COLOR_SCHEME_GREEN;
    private OnEntryClickListener onEntryClickListener;


    public EntryListAdapter(Context context, OnEntryClickListener onEntryClickListener) {
        this.context = context;
        this.onEntryClickListener = onEntryClickListener;

    }

    public void setHighlightedIndex(int highlightedIndex){
        this.highlightedIndex = highlightedIndex;
        notifyDataSetChanged();
    }

    public void setColorScheme(int colorScheme){
        this.colorScheme = colorScheme;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View chartListElement = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_list_element, parent, false);

        ViewHolder vh = new ViewHolder(chartListElement);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date( entries.get(position).getTimestamp());
        holder.entryTimestamp.setText("Added on: "+df.format(date));
        holder.entryValue.setText(""+entries.get(position).getValue());
        holder.layout.setOnClickListener(v -> {
            onEntryClickListener.onEntryClicked(entries.get(position).getIndex());
        });

        if(entries.get(position).getIndex() == highlightedIndex){
            holder.entryValue.setTextColor(Const.GET_COLOR_BY_SCHEME(colorScheme,context));
            holder.editButton.setTextColor(Const.GET_COLOR_BY_SCHEME(colorScheme,context));
            holder.entryTimestamp.setTextColor(Const.GET_COLOR_BY_SCHEME(colorScheme,context));
        }else{
            holder.entryValue.setTextColor(context.getResources().getColor(R.color.cardview_dark_background));
            holder.editButton.setTextColor(context.getResources().getColor(R.color.cardview_dark_background));
            holder.entryTimestamp.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }


    }

    public void setEntries(List<EntryEntity> entries){
        this.entries=entries;
        Collections.sort(entries, new Comparator<EntryEntity>() {
            @Override
            public int compare(EntryEntity lhs, EntryEntity rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return lhs.getTimestamp() > rhs.getTimestamp() ? -1 : (lhs.getTimestamp() < rhs.getTimestamp()) ? 1 : 0;
            }
        });
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return entries.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView entryTimestamp;
        TextView entryValue;
        TextView editButton;
        View layout;



        ViewHolder(View v) {
            super(v);
            entryTimestamp = v.findViewById(R.id.entry_timestamp);
            entryValue = v.findViewById(R.id.entry_value);
            editButton = v.findViewById(R.id.entry_edit_button);
            layout = v.findViewById(R.id.linearLayout);
        }



    }

    public interface OnEntryClickListener{
        public void onEntryClicked(int entryIndex);
    }

}
