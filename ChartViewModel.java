package com.colinear.graphstuff;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.colinear.graphstuff.DB.ChartEntity;
import com.colinear.graphstuff.DB.ChartRepository;
import com.colinear.graphstuff.DB.EntryEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;



public class ChartViewModel extends AndroidViewModel {

    @Inject
    ChartRepository chartRepository;


    private LiveData<List<EntryEntity>> entries = new MutableLiveData<>();


    public ChartViewModel(Application application) {
        super(application);
        ((MyApp) application).getChartComponent().inject(this);

        entries = chartRepository.getEntries();
    }


    public void addEntry(EntryEntity entryEntity) {

        chartRepository.addEntry(entryEntity).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(entity -> Log.i("ChartViewModel", "Entry created : " + entryEntity.toString())
                );
    }


    public void addChart(ChartEntity chartEntity) {
        chartRepository.createChart(chartEntity).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result -> Log.i("ChartViewModel", "Result: " + result));
    }



    public LiveData<List<EntryEntity>> getEntries() {
        return entries;
    }


}
