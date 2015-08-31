package com.codepath.simpletodo;

/**
 * Created by Himanshu on 8/30/2015.
 */
public class TodoItem {

    public String name;
    public String description;
    public boolean status;

    public TodoItem(String nm, String desc, boolean stat) {
        name = nm;
        description = desc;
        status = stat;
    }
}
