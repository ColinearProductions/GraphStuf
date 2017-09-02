package com.colinear.graphstuff;


import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colinear.graphstuff.DB.Entities.ChartEntity;
import com.colinear.graphstuff.DB.Entities.EntryEntity;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartDetailFragment extends LifecycleFragment {


    FloatingActionButton fab;

    ChartListViewModel chartListViewModel;

    ChartEntity chartEntity;

    LineChart lineChart;

    public ChartDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chart_detail, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        fab = (FloatingActionButton) getActivity().findViewById(R.id.button);
        fab.hide();
        chartListViewModel = ViewModelProviders.of(getActivity()).get(ChartListViewModel.class);

        Log.i("chart", chartListViewModel.getCurrentChartTitle());
        chartListViewModel.getChartByTitle(chartListViewModel.getCurrentChartTitle()).observe(this, this::onChartLoaded);


        lineChart = (LineChart) view.findViewById(R.id.full_chart);
    }


    private void onChartLoaded(ChartEntity chart) {
        Log.i("chart", chart.toString());
        this.chartEntity = chart;
        chartListViewModel.getEntriesByChart(chart.getTitle()).observe(this, this::onEntriesUpdated);
    }


    private void onEntriesUpdated(List<EntryEntity> entries) {
        chartEntity.setEntries(entries);
        redrawChartView();
    }

    private void redrawChartView() {
        Log.i("chart", chartEntity.toString());

        if (chartEntity.getEntries().size() <= 0)
            return;


        List<EntryEntity> entries = chartEntity.getEntries();
        Log.i("chart",entries.toString());
        ArrayList<Entry> mpEntries = new ArrayList<>();

        for (EntryEntity e : entries)
            mpEntries.add(new Entry(e.getTimestamp(), e.getValue()));

        LineDataSet dataSet = new LineDataSet(mpEntries, "Label");
        dataSet.setDrawFilled(true);

        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.pink_blue_gradient);
        dataSet.setFillDrawable(drawable);


        dataSet.setColor(Color.RED);
        dataSet.setLineWidth(2);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);



        LineData lineData = new LineData(dataSet);

        lineChart.setData(lineData);


        lineChart.setBackgroundColor(Color.parseColor("#30343a"));



        Paint paint = lineChart.getRenderer().getPaintRender();
        int height = lineChart.getHeight();
        int width = lineChart.getWidth();

        LinearGradient linGrad = new LinearGradient(0, 0, 0, height,
                Color.parseColor("#f700ff"),
                Color.parseColor("#0066ff"),
                Shader.TileMode.REPEAT);

        paint.setShader(linGrad);


        LayerDrawable drawablez = (LayerDrawable) ContextCompat.getDrawable(getActivity(), R.drawable.t);


        dataSet.setFillDrawable(drawablez);


        lineChart.animateY(1000, Easing.EasingOption.Linear);
        lineChart.invalidate();


    }


    @Override
    public void onPause() {
        super.onPause();
        chartListViewModel.clearCurrentChart();
        fab.show();

    }
}
