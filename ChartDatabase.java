package com.colinear.graphstuff;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by Colinear on 7/15/2017.
 */


@Database(entities = {Entry.class}, version = 1)
public abstract class ChartDatabase  extends RoomDatabase{

    public abstract EntryDao entryDao();
}
