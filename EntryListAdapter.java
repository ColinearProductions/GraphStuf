package com.colinear.graphstuff;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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

    private OnEntryClickListener onEntryClickListener;
    private ChartStyle chartStyle;
    private String chartTheme = "BLUE";

    public EntryListAdapter(Context context, OnEntryClickListener onEntryClickListener, ChartStyle chartStyle) {
        this.context = context;
        this.onEntryClickListener = onEntryClickListener;
        this.chartStyle = chartStyle;

    }

    public void setHighlightedIndex(int highlightedIndex) {
        this.highlightedIndex = highlightedIndex;
        notifyDataSetChanged();
    }

    public void setTheme(String chartTheme) {
        this.chartTheme = chartTheme;

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
        Date date = new Date(entries.get(position).getTimestamp());

        boolean isDummy = entries.get(position).getTimestamp() == -1;


        if (isDummy)
            holder.hide();
    

        holder.entryTimestamp.setText("Added on: " + df.format(date));


        holder.entryValue.setText("" + entries.get(position).getValue());
        holder.layout.setOnClickListener(v -> {
            onEntryClickListener.onEntryClicked(entries.get(position).getIndex());
        });

        holder.layout.setOnLongClickListener(v -> {

            onEntryClickListener.onEntryLongClicked(entries.get(position));
            return true;
        });

        Typeface face = Typeface.createFromAsset(context.getAssets(), "Bariol_Bold.otf");
        holder.entryValue.setTypeface(face);
        holder.entryTimestamp.setTypeface(face);



        if (entries.get(position).getIndex() == highlightedIndex) {
            int color = ChartStyle.getColorResourceByName(chartStyle.getHighlightLineColor(), context, chartTheme);
            holder.entryValue.setTextColor(color);
            holder.entryTimestamp.setTextColor(color);
        } else {
            int color = ChartStyle.getColorResourceByName(chartStyle.getChartLineColor(), context, chartTheme);
            holder.entryValue.setTextColor(color);
            holder.entryTimestamp.setTextColor(color);
        }

        holder.entryComment.setText(entries.get(position).getComment());

        int textColor = ChartStyle.getColorResourceByName(chartStyle.getBackgroundGradient()[0].getColors()[0], context, chartTheme);
        holder.entryComment.setTextColor(textColor);
        holder.entryTimestamp.setTextColor(textColor);
        holder.entryValue.setTextColor(textColor);
    }

    public void setEntries(List<EntryEntity> entries) {
        this.entries = entries;
        Collections.sort(entries, (lhs, rhs) -> lhs.getTimestamp() > rhs.getTimestamp() ? -1 : (lhs.getTimestamp() < rhs.getTimestamp()) ? 1 : 0);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return entries.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView entryTimestamp;
        TextView entryValue;

        TextView entryComment;
        View layout;


        ViewHolder(View v) {
            super(v);
            entryTimestamp = v.findViewById(R.id.entry_timestamp);
            entryValue = v.findViewById(R.id.entry_value);

            entryComment = v.findViewById(R.id.entry_comment);
            layout = v.findViewById(R.id.linearLayout);
        }

        public void hide() {
            layout.setVisibility(View.GONE);
        }

        public void show(){
            layout.setVisibility(View.VISIBLE);
        }


    }

    public interface OnEntryClickListener {
        public void onEntryClicked(int entryIndex);

        public void onEntryLongClicked(EntryEntity entryEntity);
    }

}
