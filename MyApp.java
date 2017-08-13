package com.colinear.graphstuff;

import android.app.Application;

import com.colinear.graphstuff.Dagger.ChartComponent;
import com.colinear.graphstuff.Dagger.ChartListComponent;
import com.colinear.graphstuff.Dagger.ChartListModule;
import com.colinear.graphstuff.Dagger.ChartModule;
import com.colinear.graphstuff.Dagger.DaggerChartComponent;
import com.colinear.graphstuff.Dagger.DaggerChartListComponent;
import com.colinear.graphstuff.Dagger.DaggerLoadingActivityComponent;
import com.colinear.graphstuff.Dagger.LoadingActivityComponent;
import com.colinear.graphstuff.Dagger.LoadingActivityModule;


public class MyApp extends Application{

    ChartComponent chartComponent;
    LoadingActivityComponent loadingActivityComponent;
    ChartListComponent chartListComponent;


    @Override
    public void onCreate() {
        super.onCreate();

        chartComponent =
                DaggerChartComponent.builder()
                .chartModule(new ChartModule(this))
                .build();



        loadingActivityComponent =
                DaggerLoadingActivityComponent.builder()
                        .loadingActivityModule(new LoadingActivityModule(this))
                        .build();


        chartListComponent =
                DaggerChartListComponent.builder()
                        .chartListModule(new ChartListModule(this))
                        .build();
    }

    public ChartComponent getChartComponent(){
        return chartComponent;
    }

    public LoadingActivityComponent getLoadingActivityComponent(){
        return loadingActivityComponent;
    }

    public ChartListComponent getChartListComponent() {
        return chartListComponent;
    }
}


