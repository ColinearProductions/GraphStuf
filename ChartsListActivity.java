package com.colinear.graphstuff;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.colinear.graphstuff.DB.Entities.ChartEntity;
import com.colinear.graphstuff.DB.Entities.EntryEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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














    }

    @Override
    protected void onResume() {
        super.onResume();
        chartListViewModel = ViewModelProviders.of(this).get(ChartListViewModel.class);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ChartListAdapter(this, chartListViewModel, this);
        mRecyclerView.setAdapter(mAdapter);


        findViewById(R.id.button).setOnClickListener(v->{
            Log.i("FAB","in activity");
            addChart();
        });


        chartListViewModel.getChartsLiveData().observe(this,entries -> onChartsLoaded(entries));


       /* chartListViewModel.getCharts().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onChartsLoaded);   */

    }


    public void addChart(){

        String FRAGMENT_NAME ="CreateChartFragment";

        Fragment createChartFragment =  new CreateChartFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fadein,
                R.anim.fadeout, R.anim.fadein, R.anim.fadeout);
        fragmentTransaction.add(R.id.outer_layout, createChartFragment,FRAGMENT_NAME);
        fragmentTransaction.addToBackStack(FRAGMENT_NAME);

        fragmentTransaction.commit();




    }


    public void onChartsLoaded(List<ChartEntity> chartEntities){
        Log.i("OnChartsLoaded","Charts Loaded");
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
    public void onAddEntryClicked(String chartTitle, int highestEntry) {
        Log.i("Chart List activity","On chart plus clicked " + chartTitle);
        ArrayList<EntryEntity> entries = new ArrayList<>();

            entries.add(new EntryEntity(highestEntry,"comment",r.nextInt(30),chartTitle));

        chartListViewModel.addEntries(entries);
    }

    @Override
    public void onChartClicked(String chartTitle) {
        Log.i("Chart callback","Chart clicked "+ chartTitle);

        chartListViewModel.setCurrentChart(chartTitle);

        String FRAGMENT_NAME ="ChartDetailFragment";

        Fragment chartDetailFragment =  new ChartDetailFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fadein,
                R.anim.fadeout, R.anim.fadein, R.anim.fadeout);
        fragmentTransaction.add(R.id.outer_layout, chartDetailFragment,FRAGMENT_NAME);
        fragmentTransaction.addToBackStack(FRAGMENT_NAME);

        fragmentTransaction.commit();
    }
}
