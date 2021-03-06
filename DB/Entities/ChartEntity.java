package com.colinear.graphstuff.DB.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;




@Entity(tableName = "charts")
public class ChartEntity {


    @PrimaryKey()
    @NonNull
    private String title;
    private String colorScheme;
    private boolean onlyOneADay;

    @Ignore
    private List<EntryEntity> entries = new ArrayList<>();


    public ChartEntity( @NonNull String  title, String colorScheme, boolean onlyOneADay) {
        this.title = title;
        this.colorScheme = colorScheme;
        this.onlyOneADay = onlyOneADay;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColorScheme() {
        return colorScheme;
    }

    public void setColorScheme(String colorScheme) {
        this.colorScheme = colorScheme;
    }


    public boolean isOnlyOneADay() {
        return onlyOneADay;
    }

    public void setOnlyOneADay(boolean onlyOneADay) {
        this.onlyOneADay = onlyOneADay;
    }

    public List<EntryEntity> getEntries() {
        return entries;
    }

    public void setEntries(List<EntryEntity> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "ChartEntity{" +
                "title='" + title + '\'' +
                ", colorScheme='" + colorScheme + '\'' +
                ", onlyOneADay=" + onlyOneADay +
                ", entries=" + entries +
                '}';
    }
}


