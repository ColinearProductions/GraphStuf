package com.colinear.graphstuff;


import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
public class ChartDetailFragment extends LifecycleFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, OnChartValueSelectedListener, EntryListAdapter.OnEntryClickListener {

    FloatingActionButton fab;
    ChartListViewModel chartListViewModel;

    ChartEntity chartEntity;
    LineChart lineChart;

    TextView titleTextView;

    String[] intervals = new String[]{"All", "Last 7 Days", "Last 30 Days"};

    boolean lockZoom;
    boolean showValues;

    int visibleItems = -1;

    Button addEntryButton;

    boolean clickedOnEntityDirectly = false; // if true, dont scroll to the highlighted entry item in the list.
    //todo maybe instead of data point detail, also have a scrollable list of the entries
    // when you click an entry, highlight the datapoint, and the other way around
    // only after that show the details


    private RecyclerView mRecyclerView;
    private EntryListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    Spinner intervalSpinner;
    ChartStyle chartStyle;

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

        addEntryButton = view.findViewById(R.id.chart_detail_add_button);
        addEntryButton.setOnClickListener(this);

        chartListViewModel = ViewModelProviders.of(getActivity()).get(ChartListViewModel.class);

        Log.i("chart", chartListViewModel.getCurrentChartTitle());
        chartListViewModel.getChartByTitle(chartListViewModel.getCurrentChartTitle()).observe(this, this::onChartLoaded);

        lineChart = view.findViewById(R.id.full_chart);
        lineChart.setOnChartValueSelectedListener(this);
        titleTextView = view.findViewById(R.id.chart_detail_title_textview);

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "Bariol_Bold.otf");

        titleTextView.setTypeface(face);

        //view.findViewById(R.id.chart_detail_options_button).setOnClickListener(this);

        // Interval select intervalSpinner
        intervalSpinner = view.findViewById(R.id.spinner);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, intervals);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intervalSpinner.setAdapter(dataAdapter);

        chartStyle = ChartStyle.fromJson("chartDetailStyle.json", this.getActivity());

        mRecyclerView = view.findViewById(R.id.chart_detail_entry_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new EntryListAdapter(getActivity(), this, chartStyle);
        mRecyclerView.setAdapter(mAdapter);

    }


    private void onChartLoaded(ChartEntity chart) {
        Log.i("chart", chart.toString());
        this.chartEntity = chart;
        titleTextView.setText(chart.getTitle());
        mAdapter.setTheme(chart.getColorScheme());
        chartListViewModel.getEntriesByChart(chart.getTitle()).observe(this, this::onEntriesUpdated);
        intervalSpinner.setOnItemSelectedListener(this);


    }


    private void onEntriesUpdated(List<EntryEntity> entries) {

        chartEntity.setEntries(entries);
        redrawChartView();
        mAdapter.setEntries(entries);


    }

    private void redrawChartView() {


        ArrayList<Entry> mpEntries = new ArrayList<>();
        Long[] dateMapping = null;
        if (chartEntity.getEntries().size() <= 0) {
            dateMapping = new Long[2];
            mpEntries.add(new Entry(0, 1));
            mpEntries.add(new Entry(1, 1));
            dateMapping[0] = 0L;
            dateMapping[1] = 0L;
        } else {

            //mapping indexes to timestamps
            dateMapping = new Long[chartEntity.getEntries().size()];
            for (EntryEntity e : chartEntity.getEntries()) {
                dateMapping[e.getIndex()] = e.getTimestamp();
                mpEntries.add(new Entry(e.getIndex(), (float) e.getValue(), e));
            }
        }


        LineDataSet dataSet = new LineDataSet(mpEntries, "Label"); //apply styling to it
        chartStyle.applyStyle(dataSet, getContext(), chartEntity.getColorScheme());
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData); // apply styling to line chart
        chartStyle.applyStyle(lineChart, getContext(), chartEntity.getColorScheme());

        lineChart.getXAxis().setValueFormatter(new XAxisDateFormatter(dateMapping));

        applyChartOptions();


    }


    @Override
    public void onPause() {
        super.onPause();
        chartListViewModel.clearCurrentChart();
        fab.show();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        lineChart.fitScreen();
        switch (position) {
            case 0:
                visibleItems = lineChart.getLineData().getDataSets().get(0).getEntryCount();
                break;
            case 1:
                visibleItems = 7;
                break;
            case 2:
                visibleItems = 30;
                break;
        }
        applyChartOptions();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void toast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.chart_detail_add_button) {


            new MaterialDialog.Builder(getActivity())
                    .title("Value")
                    .content("Enter the value")
                    .inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL)
                    .input("0", "", new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {
                            chartListViewModel.addEntry(new EntryEntity("comment", Double.parseDouble(input.toString()), chartEntity.getTitle(), chartEntity.getLastIndex()));
                        }
                    }).show();

            return;
        }
        /*

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
        } */
    }


    public void applyChartOptions() {
        Log.i("Options", "Updated");
        lineChart.setPinchZoom(!lockZoom);
        lineChart.setDoubleTapToZoomEnabled(!lockZoom);
        lineChart.setScaleEnabled(!lockZoom);

        int itemsCount = lineChart.getLineData().getDataSets().get(0).getEntryCount();
        lineChart.setVisibleXRangeMaximum(visibleItems);
        lineChart.moveViewToX(itemsCount - visibleItems);

        lineChart.getData().setDrawValues(showValues);

        lineChart.invalidate();
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e.getData() == null)
            return;


        EntryEntity entryEntity = (EntryEntity) e.getData();
        mAdapter.setHighlightedIndex(entryEntity.getIndex());

        if (!clickedOnEntityDirectly)
            mLayoutManager.scrollToPosition(chartEntity.getEntries().size() - entryEntity.getIndex() - 1);
        else
            clickedOnEntityDirectly = false;

        Log.i("SelectedEntry", entryEntity.toString());


    }

    @Override
    public void onNothingSelected() {

        //todo add FAB to chart detail fragment to add entry only if that day there have not been an entry added
        //todo mark days that have been entered automatically
    }


    @Override
    public void onEntryClicked(int entryIndex) {
        Log.i("LOG", "idx: " + entryIndex);
        clickedOnEntityDirectly = true;
        lineChart.highlightValue((float) entryIndex, 0);

    }
}
