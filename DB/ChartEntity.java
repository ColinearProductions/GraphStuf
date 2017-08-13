package com.colinear.graphstuff.DB;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;

import java.util.ArrayList;
import java.util.List;


@Entity(tableName = "charts")
public class ChartEntity {

    @PrimaryKey()
    private String title;
    private String description;
    private String color;
    private int iconId;

    @Ignore
    List<EntryEntity> entries = new ArrayList<>();


    public ChartEntity(String title, String description, String color, int iconId) {
        this.title = title;
        this.description = description;
        this.color = color;
        this.iconId = iconId;
    }


    public void setEntries(List<EntryEntity> entries){
        this.entries = entries;
    }

    public List<EntryEntity>   getEntries(){
        return entries;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    @Override
    public String toString() {
        return "ChartEntity{" +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", color='" + color + '\'' +
                ", iconId=" + iconId +
                '}';
    }
}


