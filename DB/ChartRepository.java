package com.colinear.graphstuff.DB;

import android.arch.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface ChartRepository {

    Single<Boolean> addEntry(EntryEntity entryEntity);
    Single<Boolean> addEntries(List<EntryEntity> entryEntities);

    Single<Boolean> createChart(ChartEntity chartEntity);
    Single<List<ChartEntity>> getCharts();


    LiveData<List<EntryEntity>> getEntries();
    LiveData<List<EntryEntity>> getEntriesByChart(String chartTitle);


}
