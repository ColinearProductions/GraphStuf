package com.colinear.graphstuff;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;


import com.colinear.graphstuff.DB.Entities.ChartEntity;
import com.colinear.graphstuff.DB.ChartRepository;
import com.colinear.graphstuff.DB.Entities.EntryEntity;
import com.github.mikephil.charting.charts.Chart;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChartListViewModel extends AndroidViewModel {

    @Inject
    ChartRepository chartRepository;

    String currentChartTitle = null;
    int lastIndex = 0;





    public ChartListViewModel(Application application) {
        super(application);
        ((MyApp) application).getChartListComponent().inject(this);
    }


    void addChart(ChartEntity chartEntity) {
        chartRepository.createChart(chartEntity).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result -> Log.i("ChartListViewModel", "Add Charts Result: " + result));
    }


    void addEntries(List<EntryEntity> entryEntities) {
        chartRepository.addEntries(entryEntities).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result -> Log.i("ChartListViewModel", "Add entries Result: " + result));
    }

    void addEntry(EntryEntity entryEntities) {

        chartRepository.addEntry(entryEntities).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result -> Log.i("ChartListViewModel", "Add entries Result: " + result));
    }

    Single<Boolean> addEntryWithObservable(EntryEntity entryEntities) {

       return chartRepository.addEntry(entryEntities);
    }






    public Single<List<ChartEntity>> getCharts(){
        Log.i("ChartListViewModel","Getting charts");
        return chartRepository.getCharts();
    }

    public LiveData<List<ChartEntity>> getChartsLiveData(){
        return chartRepository.getChartsLiveData();
    }

    public LiveData<List<EntryEntity>> getEntriesByChart(String chartTitle){
        return chartRepository.getEntriesByChart(chartTitle);
    }

    public LiveData<ChartEntity> getChartByTitle(String title){
        return chartRepository.getChartLiveData(title);
    }


    public void setCurrentChart(String chartTitle, int lastIndex){
        Log.i("chart", "set current chart: " +  chartTitle);
        this.currentChartTitle = chartTitle;
        this.lastIndex = lastIndex;
    }

    public String getCurrentChartTitle(){
        return this.currentChartTitle;
    }


    public int getCurrentChartLastIndex(){
        return this.lastIndex;
    }

    public void clearCurrentChart(){
        this.currentChartTitle = null;
    }

    public Single<EntryEntity[]> getExtremeEntries(String chartTitle){
        return chartRepository.getExtremeEntries(chartTitle);
    }

}
