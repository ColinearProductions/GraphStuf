package com.colinear.graphstuff;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.colinear.graphstuff.DB.Entities.ChartEntity;

import java.util.List;

public class ChartsListActivity extends AppCompatActivity implements ChartListAdapter.ChartClickListener {


    private RecyclerView mRecyclerView;
    private ChartListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ChartListViewModel chartListViewModel;

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

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ChartListAdapter(this, chartListViewModel, this);
        mRecyclerView.setAdapter(mAdapter);


        findViewById(R.id.button).setOnClickListener(v->{
            addChart();
        });


        chartListViewModel.getChartsLiveData().observe(this,entries -> onChartsLoaded(entries));



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
    public void onChartClicked(ChartEntity chartEntity) {
        chartListViewModel.setCurrentChart(chartEntity);
        String FRAGMENT_NAME ="FragmentChartDetail";
        Fragment chartDetailFragment =  new FragmentChartDetail();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fadein,
                R.anim.fadeout, R.anim.fadein, R.anim.fadeout);
        fragmentTransaction.add(R.id.outer_layout, chartDetailFragment,FRAGMENT_NAME);
        fragmentTransaction.addToBackStack(FRAGMENT_NAME);
        fragmentTransaction.commit();
    }

    @Override
    public void onChartLongClicked(ChartEntity chartEntity) {
        chartListViewModel.setCurrentChart(chartEntity);
        chartListViewModel.setCurrentEntry(null);
        chartListViewModel.setAction("");

        String FRAGMENT_NAME ="FragmentEntryDetail";
        Fragment entryDetailFragment =  new FragmentEntryDetail();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fadein,
                R.anim.fadeout, R.anim.fadein, R.anim.fadeout);
        fragmentTransaction.add(R.id.outer_layout, entryDetailFragment,FRAGMENT_NAME);
        fragmentTransaction.addToBackStack(FRAGMENT_NAME);
        fragmentTransaction.commit();
    }

    public void addChart(){
        String FRAGMENT_NAME ="FragmentCreateChart";
        Fragment createChartFragment =  new FragmentCreateChart();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fadein,
                R.anim.fadeout, R.anim.fadein, R.anim.fadeout);
        fragmentTransaction.add(R.id.outer_layout, createChartFragment,FRAGMENT_NAME);
        fragmentTransaction.addToBackStack(FRAGMENT_NAME);
        fragmentTransaction.commit();




    }



}
