package com.colinear.graphstuff;


import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ChartModule.class})
public interface ChartComponent {

    void inject(ChartViewModel chartViewModel);

}
