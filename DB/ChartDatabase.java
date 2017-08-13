package com.colinear.graphstuff.DB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by Colinear on 7/15/2017.
 */


@Database(entities = {EntryEntity.class, ChartEntity.class}, version = 2)
public abstract class ChartDatabase  extends RoomDatabase{

    public abstract EntryDao entryDao();
    public abstract ChartDao chartDao();


}
