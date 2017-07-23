package com.colinear.graphstuff.Dagger;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.colinear.graphstuff.DB.ChartDatabase;
import com.colinear.graphstuff.DB.ChartRepository;
import com.colinear.graphstuff.DB.ChartRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ChartModule {

    private Application application;

    public ChartModule(Application application){
        this.application = application;
    }

    @Provides
    Context applicationContext(){
        return application;
    }


    @Provides
    @Singleton
    ChartRepository providesChartRepository(ChartDatabase chartDatabase){
        return  new ChartRepositoryImpl(chartDatabase);
    }


    @Provides
    @Singleton
    ChartDatabase providesChartDatabase(Context context){
        return Room.databaseBuilder(context.getApplicationContext(), ChartDatabase.class, "chart_db").build();
    }

}
