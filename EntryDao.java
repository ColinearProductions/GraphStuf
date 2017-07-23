package com.colinear.graphstuff;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

import java.util.List;



@Dao
public interface EntryDao {


    @Query("SELECT * FROM entries")
    LiveData<List<Entry>> getEntries();


    @Insert
    void addEntry(Entry entry);

    @Delete
    void deleteEntry(Entry entry);

    @Update(onConflict = REPLACE)
    void updateEntry(Entry e);
}
