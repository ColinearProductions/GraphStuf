package com.colinear.graphstuff.Dagger;


import com.colinear.graphstuff.ChartViewModel;
import com.colinear.graphstuff.LoadingActivity;
import com.colinear.graphstuff.LoadingViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {LoadingActivityModule.class})
public interface LoadingActivityComponent {

    void inject(LoadingViewModel loadingViewModel);

}
