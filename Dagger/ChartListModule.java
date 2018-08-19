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
public class ChartListModule {

    private Application application;

    public ChartListModule(Application application){
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
        return Room.databaseBuilder(context.getApplicationContext(), ChartDatabase.class,"provis").fallbackToDestructiveMigration().build();
      //  return Room.inMemoryDatabaseBuilder(context.getApplicationContext(), ChartDatabase.class).build();
    }

}
