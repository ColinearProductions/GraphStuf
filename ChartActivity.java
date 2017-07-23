package com.colinear.graphstuff;

import android.app.Application;
import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ChartActivity extends LifecycleActivity {


    private ChartViewModel chartViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);



        chartViewModel = ViewModelProviders.of(this).get(ChartViewModel.class);



        Entry entry = new Entry("timestamp", "comment", 0);
        chartViewModel.addEntry(entry);

        chartViewModel.getEntries().observe(this, entries->{
            for(Entry e:entries){
                Log.i("Entries", e.toString());
            }

        });

    }

}
