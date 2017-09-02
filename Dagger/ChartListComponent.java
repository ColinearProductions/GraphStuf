package com.colinear.graphstuff.Dagger;


import com.colinear.graphstuff.ChartListViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ChartListModule.class})
public interface ChartListComponent {

    void inject(ChartListViewModel chartListViewModel);

}
