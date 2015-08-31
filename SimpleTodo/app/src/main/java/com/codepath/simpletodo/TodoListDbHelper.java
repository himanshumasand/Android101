package com.codepath.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Himanshu on 8/30/2015.
 */
public class TodoListDbHelper extends SQLiteOpenHelper{

    private static TodoListDbHelper sInstance;

    public static final String DATABASE_NAME = "TodoList.db";
    public static final int DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TodoItemContract.TodoItemEntry.TABLE_NAME + " (" +
                    TodoItemContract.TodoItemEntry.COLUMN_ITEM_ID + " INTEGER PRIMARY KEY," +
                    TodoItemContract.TodoItemEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    TodoItemContract.TodoItemEntry.COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    TodoItemContract.TodoItemEntry.COLUMN_STATUS + TEXT_TYPE +
                    " )";
    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TodoItemContract.TodoItemEntry.TABLE_NAME;

    public static synchronized TodoListDbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TodoListDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public TodoListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public ContentValues createKeyValuePairs(TodoItem item) {
        ContentValues values = new ContentValues();

        values.put(TodoItemContract.TodoItemEntry.COLUMN_NAME, item.name);
        values.put(TodoItemContract.TodoItemEntry.COLUMN_DESCRIPTION, item.description);
        values.put(TodoItemContract.TodoItemEntry.COLUMN_STATUS, item.status);

        return values;
    }

    public void addItem(TodoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = createKeyValuePairs(item);

        // Inserting Row
        db.insert(TodoItemContract.TodoItemEntry.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public TodoItem getItem(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TodoItemContract.TodoItemEntry.TABLE_NAME,
                new String[] {
                        TodoItemContract.TodoItemEntry.COLUMN_ITEM_ID,
                        TodoItemContract.TodoItemEntry.COLUMN_NAME,
                        TodoItemContract.TodoItemEntry.COLUMN_DESCRIPTION,
                        TodoItemContract.TodoItemEntry.COLUMN_STATUS, },
                TodoItemContract.TodoItemEntry.COLUMN_NAME + "=?",
                new String[] { String.valueOf(name) },
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        TodoItem item = new TodoItem(cursor.getString(1), cursor.getString(2), Boolean.parseBoolean(cursor.getString(3)));

        return item;
    }

    public List<TodoItem> getAllItems()  {
        List<TodoItem> allItems = new ArrayList<TodoItem>();
        String selectQuery = "SELECT * FROM " + TodoItemContract.TodoItemEntry.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                TodoItem item = new TodoItem(cursor.getString(1), cursor.getString(2), Boolean.parseBoolean(cursor.getString(3)));
                allItems.add(item);
            } while (cursor.moveToNext());
        }

        return allItems;
    }

    public void updateItem(String oldName, TodoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = createKeyValuePairs(item);

        db.update(TodoItemContract.TodoItemEntry.TABLE_NAME, values, TodoItemContract.TodoItemEntry.COLUMN_NAME + " = ?", new String[] { String.valueOf(oldName)});
    }

    public void deleteItem(TodoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TodoItemContract.TodoItemEntry.TABLE_NAME, TodoItemContract.TodoItemEntry.COLUMN_NAME + " = ?", new String[] { String.valueOf(item.name) });
        db.close();
    }

    public long getItemCount() {
        return DatabaseUtils.queryNumEntries(this.getReadableDatabase(), TodoItemContract.TodoItemEntry.TABLE_NAME);
    }
}
