package com.colinear.graphstuff;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.colinear.graphstuff.DB.ChartEntity;
import com.colinear.graphstuff.DB.EntryEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChartsListActivity extends LifecycleActivity  implements ChartListAdapter.ChartClickListener {


    private RecyclerView mRecyclerView;
    private ChartListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ChartListViewModel chartListViewModel;
    Random r = new Random();
    public int idx = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts_list);

        Log.i("ChartListActivity", "Started");


        chartListViewModel = ViewModelProviders.of(this).get(ChartListViewModel.class);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ChartListAdapter(this, chartListViewModel, this);
        mRecyclerView.setAdapter(mAdapter);


        findViewById(R.id.button).setOnClickListener(v->{
            addChart();
        });












    }



    public void reloadCharts(){
        chartListViewModel.getCharts().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onChartsLoaded);
    }

    public void addChart(){
        idx++;
        String chartTitle = "Chart " + idx;
        chartListViewModel.addChart(new ChartEntity(chartTitle, "descr", "color", 1));
        reloadCharts();

    }


    public void onChartsLoaded(List<ChartEntity> chartEntities){

        mAdapter.onNewData(chartEntities);

        for(ChartEntity chart : chartEntities){
            Log.i("OnChartsLoaded", chart.toString());

            chartListViewModel.getEntriesByChart(chart.getTitle()).observe(this, entryEntities -> {
                Log.i("OnChartsLoaded","\n\n\n");

                    Log.i("OnChartsLoaded",chart.getTitle()+":"+chart.getEntries().size()+": " + entryEntities.size());
                    if(chart.getEntries().size() != entryEntities.size()) {
                        Log.i("OnChartsLoaded","TRUEEEEEE");
                        chart.setEntries(entryEntities);


                        mAdapter.notifyDataSetChanged();
                    }
            });


        }

    }

    @Override
    public void onPlusClicked(String chartTitle, int highestEntry) {
        Log.i("Chart List activity","On chart plus clicked " + chartTitle);
        ArrayList<EntryEntity> entries = new ArrayList<>();
        for(int i=0;i<1;i++){
            entries.add(new EntryEntity(highestEntry,"comment",r.nextInt(30),chartTitle));
        }
        chartListViewModel.addEntries(entries);
    }
}
