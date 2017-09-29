package com.colinear.graphstuff.DB.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Colinear on 7/15/2017.
 */


@Entity(tableName = "entries", foreignKeys = @ForeignKey(entity =  ChartEntity.class, parentColumns = "title", childColumns = "chartTitle", onUpdate = CASCADE))
public class EntryEntity {

    @PrimaryKey(autoGenerate = true)
    int id;
    int timestamp; //todo right now it's just an int index, change later
    String comment;
    double value;

    @ColumnInfo(name = "chartTitle")
    String chartTitle;


    public EntryEntity(int timestamp, String comment, double value, String chartTitle) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.value = value;
        this.chartTitle = chartTitle;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
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

    @Override
    public String toString() {
        return "EntryEntity{" +
                "id=" + id +
                ", timestamp='" + timestamp + '\'' +
                ", comment='" + comment + '\'' +
                ", value=" + value +
                ", title=" + chartTitle +
                '}';
    }
}
