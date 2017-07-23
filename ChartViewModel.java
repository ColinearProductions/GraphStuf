package com.colinear.graphstuff;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Colinear on 7/15/2017.
 */

public class ChartViewModel extends AndroidViewModel {

    @Inject
    ChartRepository chartRepository;


    private LiveData<List<Entry>> entries = new MutableLiveData<>();


    int chartId;

    public ChartViewModel(Application application) {
        super(application);
        ((MyApp)application).getChartComponent().inject(this);

        entries = chartRepository.getEntries();
    }

    public void setChartId(int chartId) {
        this.chartId = chartId;
    }


    public void addEntry(Entry entry) {


        chartRepository.addEntry(entry).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {

                    //completable only receives success or error response
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e("LOG", "On Complete for addEntry");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Error", e.toString());

                    }
                });
    }


    public LiveData<List<Entry>> getEntries(){
        return entries;
    }



}
