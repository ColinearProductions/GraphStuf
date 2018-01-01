package com.colinear.graphstuff.DB;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.colinear.graphstuff.DB.Entities.ChartEntity;
import com.colinear.graphstuff.DB.Entities.EntryEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ChartRepositoryImpl implements ChartRepository {

    @Inject
    ChartDatabase chartDatabase;


    public ChartRepositoryImpl(ChartDatabase chartDatabase) {
        this.chartDatabase = chartDatabase;

    }


    @Override
    public Single<Boolean> createChart(ChartEntity chartEntity) {
        return Single.fromCallable(() -> {
                    chartDatabase.chartDao().createChart(chartEntity);
                    return true;
                }
        );
    }


    @Override
    public Single<Boolean> addEntry(EntryEntity entryEntity) {
        return Single.fromCallable(() -> {
                    chartDatabase.entryDao().addEntry(entryEntity);
                    return true;
                }
        );
    }


    @Override
    public Single<Boolean> updateEntry(EntryEntity entryEntity) {
        return Single.fromCallable(() -> {
                    chartDatabase.entryDao().updateEntry(entryEntity);
                    return true;
                }
        );
    }

    @Override
    public Single<Boolean> addEntries(List<EntryEntity> entryEntities) {
        return Single.fromCallable(() -> {
            chartDatabase.entryDao().addEntries(entryEntities);
            Log.i("ChartRepoImpl", "Added many entries");
            return true;

        });
    }


    @Override
    public LiveData<List<EntryEntity>> getEntries() {
        //Here is where we would do more complex logic, like getting events from a cache
        //then inserting into the database etc. In this example we just go straight to the dao.
        return chartDatabase.entryDao().getEntries();
    }

    @Override
    public LiveData<List<EntryEntity>> getEntriesByChart(String chartTitle) {
        return chartDatabase.entryDao().getEntriesByChart(chartTitle);
    }

    @Override
    public LiveData<ChartEntity> getChartLiveData(String title) {
        return chartDatabase.chartDao().getChartLiveData(title);
    }


    @Override
    public Single<EntryEntity> getLastEntry(String chartTitle) {
        Log.i("VALUES", chartTitle);


        return Single.fromCallable(() -> {
            EntryEntity ee = chartDatabase.entryDao().getLastEntryByChart(chartTitle);
            if (ee == null)
                return null;
            return ee;
        });

    }


    @Override
    public Single<List<ChartEntity>> getCharts() {
        return Single.fromCallable(() -> {
            return chartDatabase.chartDao().getCharts();
        });

    }


    @Override
    public LiveData<List<ChartEntity>> getChartsLiveData() {

        return chartDatabase.chartDao().getChartsLiveData();

    }


}
