package com.colinear.graphstuff;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;


import com.colinear.graphstuff.DB.Entities.ChartEntity;
import com.colinear.graphstuff.DB.ChartRepository;
import com.colinear.graphstuff.DB.Entities.EntryEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChartListViewModel extends AndroidViewModel {

    @Inject
    ChartRepository chartRepository;




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




}
