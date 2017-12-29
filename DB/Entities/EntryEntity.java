package com.colinear.graphstuff.DB.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;


@Entity(tableName = "entries")
public class EntryEntity {

    @PrimaryKey(autoGenerate = true)
    int id;
    int index;
    Long timestamp; //todo right now it's just an int index, change later
    String comment;
    double value;

    @ColumnInfo(name = "chartTitle")
    String chartTitle;

    //todo all entries should have a timestamp and and index


    public EntryEntity(String comment, double value, String chartTitle, int index) {
        this.timestamp = System.currentTimeMillis();
        this.comment = comment;
        this.value = value;
        this.chartTitle = chartTitle;
        this.index = index;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getChartTitle() {
        return chartTitle;
    }


    public void setChartTitle(String chartTitle) {
        this.chartTitle = chartTitle;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "EntryEntity{" +
                "id=" + id +
                ", index=" + index +
                ", timestamp=" + timestamp +
                ", comment='" + comment + '\'' +
                ", value=" + value +
                ", chartTitle='" + chartTitle + '\'' +
                '}';
    }
}
