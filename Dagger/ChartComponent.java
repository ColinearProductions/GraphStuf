package com.colinear.graphstuff.Dagger;


import com.colinear.graphstuff.ChartViewModel;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = {ChartModule.class})
public interface ChartComponent {

    void inject(ChartViewModel chartViewModel);

}
