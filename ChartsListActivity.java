package com.colinear.graphstuff;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;


import com.afollestad.materialdialogs.MaterialDialog;
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

    //todo App name : Provis
    // progress visualizer

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
            Log.i("FAB","in activity");
            addChart();
        });


        chartListViewModel.getChartsLiveData().observe(this,entries -> onChartsLoaded(entries));


       /* chartListViewModel.getCharts().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onChartsLoaded);   */


      //  generateDummyData();
    }


    public void addChart(){

        String FRAGMENT_NAME ="CreateChartFragment";

        Fragment createChartFragment =  new CreateChartFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fadein,
                R.anim.fadeout, R.anim.fadein, R.anim.fadeout);
        fragmentTransaction.replace(R.id.outer_layout, createChartFragment,FRAGMENT_NAME);
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

    @Override
    public void onChartLongClicked(String chartTitle, int lastIndex) {

        /*
        String FRAGMENT_NAME ="EntryDetailFragment";

        Fragment entryDetailFragment =  new EntryDetailFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fadein,
                R.anim.fadeout, R.anim.fadein, R.anim.fadeout);
        fragmentTransaction.replace(R.id.outer_layout, entryDetailFragment,FRAGMENT_NAME);
        fragmentTransaction.addToBackStack(FRAGMENT_NAME);

        fragmentTransaction.commit();
        */



        new MaterialDialog.Builder(this)
                .title("Value")
                .content("Enter the value")
                .inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL )
                .input("0", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {

                        chartListViewModel.addEntry(new EntryEntity("comment",Double.parseDouble(input.toString()),chartTitle,lastIndex));
                    }
                }).show();




    }



    public void generateDummyData(){
        double[] data = {
                109.0,108.5,107.0,106.0,105.5,105.0,104.5,104.5,104.5,104.0,103.5,103.0,102.5,102.5,
                102.8,102.4,102.1,102.3,102.5,102.2,101.6,101.4,102.4,101.8,101.4,102.8,103.4,102.7,
                103.0,102.5,101.0,101.3,100.8,100.3,101.7,100.7,100.3,99.9,100.6,100.1,101.7,100.7,
                100.2,99.5,98.9,99.5,99.2,98.5,99.5,98.9,99.5,99.2,99.0,99.2,98.5,99.5,98.9,99.5,
                99.2,99.2,99.2,99.2,98.8,98.4,97.9,98.1,99.3,99.5,101.0,100.0,99.5,99.0,98.5,99.3,
                98.7,98.5,98.5,97.8,97.5,97.2,97.2,98.0,98.5,98.2,98.4,97.1,98.0,97.5,97.2,97.2,
                98.0,98.5,98.2,98.4,98.8,98.3,97.8,97.0,97.8,97.2,96.0,95.9,96.3,95.6,96.3,96.9,
                96.6,95.3,95.2,96.0,97.0,97.0,96.3,97.0,96.2,96.8,97.0,97.3,97.1,97.8,96.5,95.2,
                96.0,96.1,95.9,96.0,96.5,97.0,96.5,97.1,97.0,96.3,95.7,95.4,95.3,95.2,95.0,94.7,
                95.0,95.2,95.4,95.0,95.6,96.0,96.1,96.2,96.8,95.9,96.2,96.5,96.7,97.0,96.2,96.5,
                96.0,96.8,95.9,96.2,96.5,96.7,97.0,96.2,96.5,96.0,96.5,97.0,97.1,97.4,97.2,98.0,
                98.1,97.2,98.2,98.1,98.3,98.0,98.6,98.2,98.3,98.1,98.6,98.6,98.2,98.7,98.6,98.9,
                98.8,98.1,98.0,98.0,96.7,97.0,97.2,97.9,97.3,97.2,97.4};

        String chartTitle = "Demo";
        chartListViewModel.addChart(new ChartEntity(chartTitle, Const.COLOR_SCHEME_GREEN, true));
        ArrayList<EntryEntity> entries = new ArrayList<>();
        for(int i=0;i<data.length;i++){
            entries.add(new EntryEntity( "comment", data[i], chartTitle, i));
        }

        chartListViewModel.addEntries(entries);

    }
}
