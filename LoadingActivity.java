package com.colinear.graphstuff;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.colinear.graphstuff.DB.ChartEntity;
import com.colinear.graphstuff.DB.ChartRepository;
import com.colinear.graphstuff.DB.EntryEntity;

import javax.inject.Inject;

public class LoadingActivity extends LifecycleActivity {


    private LoadingViewModel loadingViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);


        loadingViewModel = ViewModelProviders.of(this).get(LoadingViewModel.class);


        Intent i = new Intent(this, ChartsListActivity.class);
        startActivity(i);

    }
}
