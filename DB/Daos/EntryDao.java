package com.colinear.graphstuff.DB.Daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.colinear.graphstuff.DB.Entities.EntryEntity;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

import java.util.List;



@Dao
public interface EntryDao {


    @Query("SELECT * FROM entries")
    LiveData<List<EntryEntity>> getEntries();


    @Query("SELECT * FROM entries where chartTitle = :chartTitle")
    LiveData<List<EntryEntity>> getEntriesByChart(String chartTitle);


    @Query("SELECT * FROM entries WHERE id = :entryId")
    EntryEntity getEntryById(int entryId);



    @Insert
    void addEntry(EntryEntity entryEntity);


    @Insert
    void addEntries(List<EntryEntity> entries);

    @Delete
    void deleteEntry(EntryEntity entryEntity);

    @Update(onConflict = REPLACE)
    void updateEntry(EntryEntity e);
}
