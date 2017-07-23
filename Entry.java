package com.colinear.graphstuff;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Colinear on 7/15/2017.
 */


@Entity(tableName = "entries")
public class Entry {

    @PrimaryKey(autoGenerate = true)
    int id;
    String timestamp;
    String comment;
    int value;


    public Entry(String timestamp, String comment, int value) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.value = value;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "id=" + id +
                ", timestamp='" + timestamp + '\'' +
                ", comment='" + comment + '\'' +
                ", value=" + value +
                '}';
    }
}
