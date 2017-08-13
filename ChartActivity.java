package com.colinear.graphstuff;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.util.Log;

import com.colinear.graphstuff.DB.ChartEntity;
import com.colinear.graphstuff.DB.EntryEntity;

public class ChartActivity extends LifecycleActivity {


    private ChartViewModel chartViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);



        chartViewModel = ViewModelProviders.of(this).get(ChartViewModel.class);








    }

}
