package com.codepath.simpletodo;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements EditItemDialog.EditItemDialogListener, View.OnClickListener {

    TodoListDbHelper dbHelper;
    ArrayList<TodoItem> arrayOfItems;
    TodoItemAdapter itemsAdapter;
    ListView lvItems;
    int editedItemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateListView();
    }

    private void updateListView() {
        dbHelper = TodoListDbHelper.getInstance(this);
        List<TodoItem> itemList = dbHelper.getAllItems();

        lvItems = (ListView) findViewById(R.id.lvItems);
        arrayOfItems = new ArrayList<>();

        for(int i = 0; i < itemList.size(); i++) {
            arrayOfItems.add(itemList.get(i));
        }

        itemsAdapter = new TodoItemAdapter(this, arrayOfItems);
        lvItems.setAdapter(itemsAdapter);
        lvItems.setItemsCanFocus(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        TodoItem newItem = new TodoItem(itemText, "", false);
        itemsAdapter.add(newItem);
        editedItemPosition = itemsAdapter.getPosition(newItem);
        dbHelper.addItem(newItem);
        etNewItem.setText("");
        showEditItemDialog(newItem);
    }

    private void showEditItemDialog(TodoItem item) {
        FragmentManager fm = getSupportFragmentManager();
        EditItemDialog editItemDialog = EditItemDialog.newInstance(item);
        editItemDialog.show(fm, "fragment_edit_item");
    }

//    private void setupListViewListeners() {
//        lvItems.setOnItemClickListener(
//                new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                editedItemPosition = position;
//                showEditItemDialog(arrayOfItems.get(position));
//            }
//        });
//
//        lvItems.setOnItemLongClickListener(
//                new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
//                TodoItem deletedItem = arrayOfItems.get(pos);
//                arrayOfItems.remove(pos);
//                itemsAdapter.notifyDataSetChanged();
//                dbHelper.deleteItem(deletedItem);
//                return true;
//            }
//        });
//    }

    @Override
    public void onFinishEditDialog(TodoItem newItem) {
        TodoItem oldItem = arrayOfItems.get(editedItemPosition);
        arrayOfItems.set(editedItemPosition, newItem);
        itemsAdapter.notifyDataSetChanged();
        dbHelper.updateItem(oldItem, newItem);
    }

    @Override
    /**
     * Handles clicking the checkbox for an item
     */
    public void onClick(View v) {

        editedItemPosition = lvItems.getPositionForView(v);
        TodoItem itemClicked = arrayOfItems.get(editedItemPosition);
        switch (v.getId()) {

            case R.id.tvName:
                itemClicked.status = !itemClicked.status;
                onFinishEditDialog(itemClicked);
                break;

            case R.id.editButton:
                showEditItemDialog(itemClicked);
                break;

            case R.id.deleteButton:
                arrayOfItems.remove(editedItemPosition);
                itemsAdapter.notifyDataSetChanged();
                dbHelper.deleteItem(itemClicked);
                break;
        }

    }
}
