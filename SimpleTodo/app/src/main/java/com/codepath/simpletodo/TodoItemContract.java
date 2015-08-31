package com.codepath.simpletodo;

import android.provider.BaseColumns;

/**
 * Created by Himanshu on 8/30/2015.
 */
public final class TodoItemContract {

    public TodoItemContract() {}

    public static abstract class TodoItemEntry implements BaseColumns {

        public static final String TABLE_NAME = "TodoItemTable";
        public static final String COLUMN_ITEM_ID = "itemID";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_STATUS = "status";
    }
}
