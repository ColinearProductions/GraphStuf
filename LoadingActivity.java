package com.colinear.graphstuff;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;

public class LoadingActivity extends LifecycleActivity {


    private LoadingViewModel loadingViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);


        loadingViewModel = ViewModelProviders.of(this).get(LoadingViewModel.class);


        Intent i = new Intent(this, ChartsListActivity.class);
        startActivity(i);

    }
}
