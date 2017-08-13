package com.colinear.graphstuff;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.util.Log;

import com.colinear.graphstuff.DB.ChartEntity;
import com.colinear.graphstuff.DB.ChartRepository;
import com.colinear.graphstuff.DB.EntryEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;



public class LoadingViewModel extends AndroidViewModel {

    @Inject
    ChartRepository chartRepository;


    public LoadingViewModel(Application application) {
        super(application);
        ((MyApp) application).getLoadingActivityComponent().inject(this);




    }




}
