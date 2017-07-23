package com.colinear.graphstuff.DB;

import android.arch.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Colinear on 7/15/2017.
 */

public interface ChartRepository {

    Completable addEntry(Entry entry);

    LiveData<List<Entry>> getEntries();

    Completable deleteEntry(Entry entry);

}
