package com.colinear.graphstuff;


import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.colinear.graphstuff.DB.Entities.ChartEntity;
import com.colinear.graphstuff.DB.Entities.EntryEntity;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class ChartListAdapter extends RecyclerView.Adapter<ChartListAdapter.ViewHolder> {

    List<ChartEntity> charts = new ArrayList<>();



    ChartClickListener chartClickListener;
    ChartListViewModel chartListViewModel;

    Context ctx;

    public ChartListAdapter(ChartClickListener chartClickListener, ChartListViewModel chartListViewModel, Context ctx) {
        this.chartClickListener = chartClickListener;
        this.chartListViewModel = chartListViewModel;
        this.ctx = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View chartListElement = LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_list_element, parent, false);

        ViewHolder vh = new ViewHolder(chartListElement);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(charts.get(position).getTitle());




        holder.addEntryButton.setOnClickListener(v -> {
            chartClickListener.onAddEntryClicked(charts.get(position).getTitle(), charts.get(position).getEntries().size() + 1);
        });

        holder.title.setOnClickListener(v -> {
            chartClickListener.onChartClicked(charts.get(position).getTitle());
        });

        holder.chartContainerLayout.setOnClickListener(v -> {
            chartClickListener.onChartClicked(charts.get(position).getTitle());
        });

        List<EntryEntity> entries = charts.get(position).getEntries();




        if (entries.size() < 1)
            return;

        ArrayList<Entry> mpEntries = new ArrayList<>();

        for (EntryEntity e : entries)
            mpEntries.add(new Entry(e.getTimestamp(), (float)e.getValue()));


        ChartStyle chartStyle = ChartStyle.fromJson("chartListStyle.json", ctx);
        LineDataSet dataSet = new LineDataSet(mpEntries, "Label");
        chartStyle.applyStyle(dataSet);
        LineChart lineChart = holder.chartView;
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        chartStyle.applyStyle(lineChart);


        lineChart.setTouchEnabled(false);


        lineChart.animateY(1000, Easing.EasingOption.Linear);
        lineChart.invalidate();

    }



    @Override
    public int getItemCount() {
        return charts.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        LineChart chartView;
        FrameLayout chartContainerLayout;
        TextView addEntryButton;



        ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.chart_title);
            chartView = (LineChart) v.findViewById(R.id.full_chart);
            chartContainerLayout = (FrameLayout) v.findViewById(R.id.chart_container_layout);
            addEntryButton = (TextView) v.findViewById(R.id.add_entry_button);
        }




    }


    void onNewData(List<ChartEntity> chartEntities) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyDiffCallback(this.charts, chartEntities));

        this.charts.clear();
        this.charts.addAll(chartEntities);

        diffResult.dispatchUpdatesTo(this);
    }

    interface ChartClickListener {
        void onAddEntryClicked(String chartTitle, int highestEntry);
        void onChartClicked(String chartTitle);
    }
}
