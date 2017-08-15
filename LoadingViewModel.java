package com.colinear.graphstuff;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.colinear.graphstuff.DB.ChartRepository;

import javax.inject.Inject;


public class LoadingViewModel extends AndroidViewModel {

    @Inject
    ChartRepository chartRepository;


    public LoadingViewModel(Application application) {
        super(application);
        ((MyApp) application).getLoadingActivityComponent().inject(this);




    }




}
