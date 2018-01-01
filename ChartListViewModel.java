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

    private ChartEntity currentChartEntity = null;
    private EntryEntity currentEntryEntity = null;
    private String action = null;




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


    public void setCurrentChart(ChartEntity chartEntity){

        this.currentChartEntity = chartEntity;

    }

    public void setCurrentEntry(EntryEntity entry){
        this.currentEntryEntity = entry;
    }

    public ChartEntity getCurrentChartEntity() {
        return currentChartEntity;
    }

    public EntryEntity getCurrentEntryEntity() {
        return currentEntryEntity;
    }



    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Single<EntryEntity> getLastEntry(String chartTitle){
        return chartRepository.getLastEntry(chartTitle);
    }

    public Single<Boolean> updateEntry(EntryEntity entryEntity){
        return chartRepository.updateEntry(entryEntity);
    }


    Single<Boolean> addEntryWithObservable(EntryEntity entryEntities) {
        return chartRepository.addEntry(entryEntities);
    }

}
