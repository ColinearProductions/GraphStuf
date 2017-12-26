package com.colinear.graphstuff;

import android.app.Application;

import com.colinear.graphstuff.Dagger.ChartListComponent;
import com.colinear.graphstuff.Dagger.ChartListModule;
import com.colinear.graphstuff.Dagger.DaggerChartListComponent;
import com.facebook.stetho.Stetho;


public class    MyApp extends Application{


    ChartListComponent chartListComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);

        chartListComponent =
                DaggerChartListComponent.builder()
                        .chartListModule(new ChartListModule(this))
                        .build();
    }



    public ChartListComponent getChartListComponent() {
        return chartListComponent;
    }
}


