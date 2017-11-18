package com.colinear.graphstuff;


import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.colinear.graphstuff.DB.Entities.ChartEntity;
import com.colinear.graphstuff.DB.Entities.EntryEntity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartDetailFragment extends LifecycleFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, OnChartValueSelectedListener {

    FloatingActionButton fab;
    ChartListViewModel chartListViewModel;

    ChartEntity chartEntity;
    LineChart lineChart;

    TextView titleTextView;

    String[] intervals = new String[]{"All", "Last 7 Days", "Last 30 Days"};

    TextView highlightIndexTextView;
    TextView highlightValueTextView;
    TextView highlightCommentTextView;

    boolean lockZoom;
    boolean showValues;


    //todo maybe instead of data point detail, also have a scrollable list of the entries
    // when you click an entry, highlight the datapoint, and the other way around
    // only after that show the details

    public ChartDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chart_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        fab = getActivity().findViewById(R.id.button);
        fab.hide();
        chartListViewModel = ViewModelProviders.of(getActivity()).get(ChartListViewModel.class);

        Log.i("chart", chartListViewModel.getCurrentChartTitle());
        chartListViewModel.getChartByTitle(chartListViewModel.getCurrentChartTitle()).observe(this, this::onChartLoaded);

        lineChart = view.findViewById(R.id.full_chart);
        lineChart.setOnChartValueSelectedListener(this);
        titleTextView = view.findViewById(R.id.chart_detail_title_textview);


        highlightCommentTextView = view.findViewById(R.id.chart_detail_highlight_comment);
        highlightValueTextView = view.findViewById(R.id.chart_detail_highlight_value);
        highlightIndexTextView = view.findViewById(R.id.chart_detail_highlight_index);

        view.findViewById(R.id.chart_detail_options_button).setOnClickListener(this);

        // Interval select spinner
        Spinner spinner = view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, intervals);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

    }


    private void onChartLoaded(ChartEntity chart) {
        Log.i("chart", chart.toString());
        this.chartEntity = chart;
        titleTextView.setText(chart.getTitle());
        chartListViewModel.getEntriesByChart(chart.getTitle()).observe(this, this::onEntriesUpdated);
    }


    private void onEntriesUpdated(List<EntryEntity> entries) {
        chartEntity.setEntries(entries);
        redrawChartView();
    }

    private void redrawChartView() {
        if (chartEntity.getEntries().size() <= 0)
            return;

        ArrayList<Entry> mpEntries = new ArrayList<>();
        for (EntryEntity e : chartEntity.getEntries())
            mpEntries.add(new Entry(e.getTimestamp(), (float) e.getValue(), e));

        ChartStyle chartStyle = ChartStyle.fromJson("chartDetailStyle.json", this.getActivity());
        LineDataSet dataSet = new LineDataSet(mpEntries, "Label"); //apply styling to it
        chartStyle.applyStyle(dataSet);
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData); // apply styling to line chart
        chartStyle.applyStyle(lineChart);

        lineChart.invalidate();


    }


    @Override
    public void onPause() {
        super.onPause();
        chartListViewModel.clearCurrentChart();
        fab.show();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        toast(intervals[position]);
        int itemsCount = 0;
        lineChart.fitScreen();
        switch (position) {
            case 0:
                break;
            case 1:
                itemsCount = lineChart.getLineData().getDataSets().get(0).getEntryCount();
                lineChart.setVisibleXRangeMaximum(7);
                lineChart.moveViewToX(itemsCount - 7);
                break;
            case 2:
                itemsCount = lineChart.getLineData().getDataSets().get(0).getEntryCount();
                lineChart.setVisibleXRangeMaximum(30);
                lineChart.moveViewToX(itemsCount - 30);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void toast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        ArrayList<String> options = new ArrayList();
        options.add("Lock Zoom");
        options.add("Show Values");

        ArrayList<Integer> checkedState = new ArrayList<>();

        if(lockZoom)
            checkedState.add(Const.LOCK_ZOOM);
        if(showValues)
            checkedState.add(Const.SHOW_VALUES);

        Integer[] checkedStateArray = checkedState.toArray(new Integer[checkedState.size()]);

        if (v.getId() == R.id.chart_detail_options_button) {
            new MaterialDialog.Builder(getActivity())
                    .title("Options")
                    .items(options)
                    .itemsCallbackMultiChoice(checkedStateArray, new MaterialDialog.ListCallbackMultiChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                            lockZoom =Arrays.asList(which).contains(Const.LOCK_ZOOM);
                            showValues = Arrays.asList(which).contains(Const.SHOW_VALUES);
                            applyChartOptions();
                            return true;
                        }
                    })
                    .positiveText("Apply")
                    .show();
        }
        lineChart.invalidate();
    }


    public void applyChartOptions() {
        Log.i("Options","Updated");
        lineChart.setPinchZoom(!lockZoom);
        lineChart.setDoubleTapToZoomEnabled(!lockZoom);
        lineChart.setScaleEnabled(!lockZoom);

        lineChart.getData().setDrawValues(showValues);
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        EntryEntity entryEntity = (EntryEntity) e.getData();
        Log.i("SelectedEntry", entryEntity.toString());

        highlightCommentTextView.setText(entryEntity.getComment());
        highlightIndexTextView.setText(entryEntity.getTimestamp() + "");
        highlightValueTextView.setText(entryEntity.getValue() + "");
    }

    @Override
    public void onNothingSelected() {
        //todo mark commented entries on the chart
        //todo add FAB to chart detail fragment to add entry only if that day there have not been an entry added
        //todo when nothing is selected, show other values like min, max, interval etc.
        //todo mark days that have been entered automatically
    }


}
