package com.colinear.graphstuff;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.Collections;
import java.util.List;

public class ChartListAdapter extends RecyclerView.Adapter<ChartListAdapter.ViewHolder> {

    List<ChartEntity> charts = new ArrayList<>();

    int minimumEntriesLength = 8;


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


        holder.title.setOnClickListener(v -> {
            chartClickListener.onChartClicked(charts.get(position));
        });

        holder.chartContainerLayout.setOnClickListener(v -> {
            chartClickListener.onChartClicked(charts.get(position));
        });

        holder.chartContainerLayout.setOnLongClickListener(v -> {
            chartClickListener.onChartLongClicked(charts.get(position));
            return true;
        });


        List<EntryEntity> entries = charts.get(position).getEntries();

        Log.i("NEW", entries.size() + "");

        if (entries.size() < minimumEntriesLength) {
            for (int i = entries.size(); i < minimumEntriesLength; i++) {
                EntryEntity ee = new EntryEntity("", 0, charts.get(position).getTitle());
                ee.setTimestamp(-1L);
                entries.add(ee);
            }


        }

        Collections.sort(entries, (lhs, rhs) -> {
            if (lhs.getTimestamp().equals(rhs.getTimestamp()))
                return 0;
            else
                return lhs.getTimestamp() > rhs.getTimestamp() ? 1 : -1;
        });


        for (int i = 0; i < entries.size(); i++)
            entries.get(i).setIndex(i);


        ArrayList<Entry> mpEntries = new ArrayList<>();

        if (entries.size() < 1) { // if no data, add 2 points with value 0
            mpEntries.add(new Entry(0, 1));
            mpEntries.add(new Entry(1, 1));
        } else {
            for (EntryEntity e : entries)
                mpEntries.add(new Entry(e.getIndex(), (float) e.getValue()));
            if (entries.size() == 1)
                mpEntries.add(new Entry(entries.get(0).getIndex(), (float) entries.get(0).getValue()));


        }


        ChartStyle chartStyle = null;

        if (charts.get(position).getColorScheme() == Const.COLOR_SCHEME_GREEN) {
            chartStyle = ChartStyle.fromJson("chartListStyle.json", ctx);
        } else if (charts.get(position).getColorScheme() == Const.COLOR_SCHEME_RED) {
            chartStyle = ChartStyle.fromJson("chartListStyle.json", ctx);
        } else {
            chartStyle = ChartStyle.fromJson("chartListStyle.json", ctx);
        }


        LineDataSet dataSet = new LineDataSet(mpEntries, "Label");
        chartStyle.applyStyle(dataSet, ctx, charts.get(position).getColorScheme());
        LineChart lineChart = holder.chartView;
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        chartStyle.applyStyle(lineChart, ctx, charts.get(position).getColorScheme());

        holder.setTypeface(ctx);


        int color = ChartStyle.getColorResourceByName(chartStyle.getChartLineColor(), ctx, charts.get(position).getColorScheme());

        holder.title.setTextColor(color);


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
        ConstraintLayout chartContainerLayout;


        ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.chart_title);
            chartView = v.findViewById(R.id.full_chart);


            chartContainerLayout = v.findViewById(R.id.chart_container_layout);

        }

        public void setTypeface(Context ctx) {
            Typeface face = Typeface.createFromAsset(ctx.getAssets(), "Bariol_Bold.otf");
            this.title.setTypeface(face);
        }


    }


    void onNewData(List<ChartEntity> chartEntities) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyDiffCallback(this.charts, chartEntities));

        this.charts.clear();
        this.charts.addAll(chartEntities);

        diffResult.dispatchUpdatesTo(this);
    }

    interface ChartClickListener {
        void onChartClicked(ChartEntity chartEntity);

        void onChartLongClicked(ChartEntity chartEntity);

    }
}
