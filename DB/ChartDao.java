package com.colinear.graphstuff.DB;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;


@Dao
public interface ChartDao {



    @Query("SELECT * FROM charts")
    List<ChartEntity> getCharts();



    @Insert
    void createChart(ChartEntity chartEntity);






    @Update(onConflict = REPLACE)
    void updateChart(ChartEntity chartEntity);

}

