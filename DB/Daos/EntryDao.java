package com.colinear.graphstuff.DB.Daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.colinear.graphstuff.DB.Entities.ChartEntity;
import com.colinear.graphstuff.DB.Entities.EntryEntity;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

import java.util.List;



@Dao
public interface EntryDao {


    @Query("SELECT * FROM entries")
    LiveData<List<EntryEntity>> getEntries();


    @Query("SELECT * FROM entries where chartTitle = :chartTitle")
    LiveData<List<EntryEntity>> getEntriesByChart(String chartTitle);




    @Query("SELECT * FROM entries WHERE chartTitle = :chartTitle ORDER BY timestamp DESC LIMIT 1 ")
    EntryEntity getLastEntryByChart(String chartTitle);

    @Query("SELECT * FROM entries WHERE chartTitle = :chartTitle ORDER BY value LIMIT 1")
    EntryEntity getMinValueByChart(String chartTitle);

    @Query("SELECT * FROM entries WHERE chartTitle = :chartTitle ORDER BY value DESC LIMIT 1")
    EntryEntity getMaxValueByChart(String chartTitle);





    @Insert
    void addEntry(EntryEntity entryEntity);


    @Insert
    void addEntries(List<EntryEntity> entries);

    @Delete
    void deleteEntry(EntryEntity entryEntity);

    @Update(onConflict = REPLACE)
    void updateEntry(EntryEntity e);
}
