package com.colinear.graphstuff.DB.Daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.colinear.graphstuff.DB.Entities.ChartEntity;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;


@Dao
public interface ChartDao {



    @Query("SELECT * FROM charts")
    List<ChartEntity> getCharts();

    @Query("SELECT * FROM charts")
    LiveData<List<ChartEntity>> getChartsLiveData();

    @Query("SELECT * FROM charts where title=:title")
    LiveData<ChartEntity> getChartLiveData(String title);

    @Query("SELECT * FROM charts where title=:title LIMIT 1")
    ChartEntity getChart(String title);



    @Insert
    void createChart(ChartEntity chartEntity);




    @Update(onConflict = REPLACE)
    void updateChart(ChartEntity chartEntity);



}

