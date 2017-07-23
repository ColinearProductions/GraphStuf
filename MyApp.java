package com.colinear.graphstuff;

import android.app.Application;

import com.colinear.graphstuff.Dagger.ChartComponent;
import com.colinear.graphstuff.Dagger.ChartModule;

/**
 * Created by Colinear on 7/15/2017.
 */

public class MyApp extends Application{

    ChartComponent chartComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        chartComponent = DaggerChartComponent.builder()
                .chartModule(new ChartModule(this))
                .build();
    }

    public ChartComponent getChartComponent(){
        return chartComponent;
    }
}
