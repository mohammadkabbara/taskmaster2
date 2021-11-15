package com.example.taskmaster2;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.amplifyframework.core.model.temporal.Temporal;
@Entity
public class Task {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "task_title")
    public String title;
    @ColumnInfo(name = "task_body")
    public String body;
    @ColumnInfo(name = "task_state")
    public String state;

    public Task(String title, String body, String state) {
        this.title = title;
        this.body = body;
        this.state = state;
    }
}