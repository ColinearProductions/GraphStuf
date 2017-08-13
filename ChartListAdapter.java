package com.colinear.graphstuff;


import android.content.Context;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.colinear.graphstuff.DB.ChartEntity;
import com.colinear.graphstuff.DB.EntryEntity;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class ChartListAdapter extends RecyclerView.Adapter<ChartListAdapter.ViewHolder> {

    List<ChartEntity> charts = new ArrayList<>();

    String labelTextColor = "#ffffff";
    String cellColor = "#30b3ff";
    String graphLineColor = cellColor;
    String gridColor = "#393d44";
    String valueTextColor = "#ffffff";
    String backgroundColor = "#30343a";

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




        holder.addButton.setOnClickListener(v -> {
            chartClickListener.onPlusClicked(charts.get(position).getTitle(), charts.get(position).getEntries().size() + 1);
        });

        List<EntryEntity> entries = charts.get(position).getEntries();

        if (entries.size() < 1)
            return;

        ArrayList<Entry> mpEntries = new ArrayList<>();

        for (EntryEntity e : entries)
            mpEntries.add(new Entry(e.getTimestamp(), e.getValue()));






        LineDataSet dataSet = new LineDataSet(mpEntries, "Label");
        dataSet.setDrawFilled(true);

        Drawable drawable = ContextCompat.getDrawable(ctx, R.drawable.ping_blue_gradient);
        dataSet.setFillDrawable(drawable);





        dataSet.setColor(Color.parseColor(graphLineColor));
        dataSet.setLineWidth(2);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);

        LineChart lineChart = holder.chartView;
        LineData lineData = new LineData(dataSet);

        lineChart.setData(lineData);

        lineChart.setBackgroundColor(Color.parseColor(backgroundColor));
        lineChart.setDrawBorders(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(false);
        lineChart.getXAxis().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setEnabled(false);




        Paint paint = lineChart.getRenderer().getPaintRender();
        int height = lineChart.getHeight();
        int width = lineChart.getWidth();

        LinearGradient linGrad = new LinearGradient(0, 0, 0, height,
                Color.parseColor("#f700ff"),
                Color.parseColor("#0066ff"),
                Shader.TileMode.REPEAT);

        paint.setShader(linGrad);





       LayerDrawable drawablez =  (LayerDrawable) ContextCompat.getDrawable(ctx, R.drawable.t);




        dataSet.setFillDrawable(drawablez);




        lineChart.animateY(1000, Easing.EasingOption.Linear);
        lineChart.invalidate();

    }



    @Override
    public int getItemCount() {
        return charts.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        Button addButton;
        LineChart chartView;


        ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.chart_title);
            addButton = (Button) v.findViewById(R.id.chart_log_button);
            chartView = (LineChart) v.findViewById(R.id.chart_linechart);
        }


    }


    void onNewData(List<ChartEntity> chartEntities) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyDiffCallback(this.charts, chartEntities));

        this.charts.clear();
        this.charts.addAll(chartEntities);

        diffResult.dispatchUpdatesTo(this);
    }

    interface ChartClickListener {
        void onPlusClicked(String chartTitle, int highestEntry);
    }
}
