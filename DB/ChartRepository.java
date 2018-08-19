package com.colinear.graphstuff.DB;

import android.arch.lifecycle.LiveData;

import com.colinear.graphstuff.DB.Entities.ChartEntity;
import com.colinear.graphstuff.DB.Entities.EntryEntity;

import java.util.List;

import io.reactivex.Single;

public interface ChartRepository {

    Single<Boolean> addEntry(EntryEntity entryEntity);
    Single<Boolean> addEntries(List<EntryEntity> entryEntities);
    Single<Boolean> deleteEntry(EntryEntity entryEntity);

    Single<Boolean> updateEntry(EntryEntity entryEntity);

    Single<Boolean> createChart(ChartEntity chartEntity);
    Single<List<ChartEntity>> getCharts();

    LiveData<List<ChartEntity>> getChartsLiveData();


    LiveData<List<EntryEntity>> getEntries();
    LiveData<List<EntryEntity>> getEntriesByChart(String chartTitle);

    LiveData<ChartEntity> getChartLiveData(String title);



    Single<EntryEntity> getLastEntry(String chartTitle);







}
