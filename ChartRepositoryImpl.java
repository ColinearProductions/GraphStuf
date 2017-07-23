package com.colinear.graphstuff;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;


public class ChartRepositoryImpl implements ChartRepository {

    @Inject
    ChartDatabase chartDatabase;


    public ChartRepositoryImpl(ChartDatabase chartDatabase){
        this.chartDatabase = chartDatabase;
    }


    @Override
    public Completable addEntry(Entry entry) {

        return Completable.fromAction(() -> chartDatabase.entryDao().addEntry(entry));
    }

    @Override
    public LiveData<List<Entry>> getEntries() {
        //Here is where we would do more complex logic, like getting events from a cache
        //then inserting into the database etc. In this example we just go straight to the dao.
        return chartDatabase.entryDao().getEntries();
    }

    @Override
    public Completable deleteEntry(Entry entry) {
        return Completable.fromAction(() -> chartDatabase.entryDao().deleteEntry(entry));
    }
}
