package com.example.taskmaster2;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TaskOld {

    @PrimaryKey(autoGenerate = true)
    public Long id;


    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "body")
    public String body;

    @ColumnInfo(name = "state")
    public String state;

    public TaskOld(String title, String body, String state) {
        this.title = title;
        this.body = body;
        this.state = state;
    }

}