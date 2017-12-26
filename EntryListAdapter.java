package com.colinear.graphstuff;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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



    public EntryListAdapter(  ) {


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



        ViewHolder(View v) {
            super(v);
            entryTimestamp = v.findViewById(R.id.entry_timestamp);
            entryValue = v.findViewById(R.id.entry_value);
        }




    }


}
