package com.colinear.graphstuff;


import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartDetailFragment extends LifecycleFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, OnChartValueSelectedListener, EntryListAdapter.OnEntryClickListener {


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

    private XAxisDateFormatter dateFormatter;

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




        addEntryButton = view.findViewById(R.id.chart_detail_add_button);
        addEntryButton.setOnClickListener(this);

        chartListViewModel = ViewModelProviders.of(getActivity()).get(ChartListViewModel.class);


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

        dateFormatter = new XAxisDateFormatter();



        // chartListViewModel.getChartByTitle(chartListViewModel.getCurrentChartTitle()).observe(this, this::onChartLoaded);
        onChartLoaded(chartListViewModel.getCurrentChartEntity());
    }


    private void onChartLoaded(ChartEntity chart) {
        Log.i("chart", chart.toString());
        this.chartEntity = chart;
        titleTextView.setText(chart.getTitle());
        mAdapter.setTheme(chart.getColorScheme());
        chartListViewModel.getEntriesByChart(chart.getTitle()).observe(this, this::onEntriesUpdated);
        intervalSpinner.setOnItemSelectedListener(this);

        titleTextView.setTextColor(ChartStyle.getColorResourceByName("@*highlight_color", getActivity(), chartEntity.getColorScheme()));



    }


    @Override
    public void onResume() {
        ((FloatingActionButton)getActivity().findViewById(R.id.button)).hide();
        super.onResume();
    }

    @Override
    public void onPause() {
        ((FloatingActionButton)getActivity().findViewById(R.id.button)).show();
        super.onPause();
    }


    private void onEntriesUpdated(List<EntryEntity> entries) {

        Collections.sort(entries, (lhs, rhs) -> lhs.getTimestamp() > rhs.getTimestamp() ? 1 : (lhs.getTimestamp() < rhs.getTimestamp()) ? 1 : 0);

        for(int i=0;i<entries.size();i++)
            entries.get(i).setIndex(i);

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
        dateFormatter.setDateMapping(dateMapping);


        LineDataSet dataSet = new LineDataSet(mpEntries, "Label"); //apply styling to it
        chartStyle.applyStyle(dataSet, getContext(), chartEntity.getColorScheme());
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData); // apply styling to line chart
        chartStyle.applyStyle(lineChart, getContext(), chartEntity.getColorScheme());

        lineChart.getXAxis().setValueFormatter(dateFormatter);

        applyChartOptions();


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


            chartListViewModel.setCurrentChart(chartEntity);
            chartListViewModel.setCurrentEntry(null);
            chartListViewModel.setAction("");

            String FRAGMENT_NAME ="EntryDetailFragment";
            Fragment entryDetailFragment =  new EntryDetailFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fadein,
                    R.anim.fadeout, R.anim.fadein, R.anim.fadeout);
            fragmentTransaction.add(R.id.outer_layout, entryDetailFragment,FRAGMENT_NAME);
            fragmentTransaction.addToBackStack(FRAGMENT_NAME);
            fragmentTransaction.commit();
        }
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
    public void onNothingSelected() {}


    @Override
    public void onEntryClicked(int entryIndex) {
        clickedOnEntityDirectly = true;
        lineChart.highlightValue((float) entryIndex, 0);

    }

    @Override
    public void onEntryLongClicked(EntryEntity entryEntity) {

        chartListViewModel.setCurrentChart(chartEntity);
        chartListViewModel.setCurrentEntry(entryEntity);
        chartListViewModel.setAction(Const.UPDATE_ENTRY_ACTION);

        String FRAGMENT_NAME ="EntryDetailFragment";
        Fragment entryDetailFragment =  new EntryDetailFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fadein,
                R.anim.fadeout, R.anim.fadein, R.anim.fadeout);
        fragmentTransaction.add(R.id.outer_layout, entryDetailFragment,FRAGMENT_NAME);
        fragmentTransaction.addToBackStack(FRAGMENT_NAME);
        fragmentTransaction.commit();
    }
}
