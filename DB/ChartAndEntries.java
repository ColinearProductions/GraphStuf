package com.colinear.graphstuff.DB;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class ChartAndEntries {

    @Embedded
    ChartEntity chartEntity;

    @Relation(parentColumn = "title",entityColumn = "chartTitle",entity = EntryEntity.class)
    List<EntryEntity> entryEntityList;
}
