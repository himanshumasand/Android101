package com.codepath.simpletodo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

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
        setupListViewListeners();
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
        TodoItem newItem = new TodoItem(itemText, "temp desc", false);
        itemsAdapter.add(newItem);
        dbHelper.addItem(newItem);
        etNewItem.setText("");
    }

    private void setupListViewListeners() {
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editedItemPosition = position;
                Intent editItemIntent = new Intent(MainActivity.this, EditItemActivity.class);
                editItemIntent.putExtra("itemName", arrayOfItems.get(position).name);
                startActivityForResult(editItemIntent, 1);
            }
        });

        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                TodoItem deletedItem = arrayOfItems.get(pos);
                arrayOfItems.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                dbHelper.deleteItem(deletedItem);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && data.getStringExtra("newItemName") != null) {
            String newItemName = data.getStringExtra("newItemName");
            TodoItem oldItem = arrayOfItems.get(editedItemPosition);
            TodoItem newItem = new TodoItem(newItemName, oldItem.description, oldItem.status);

            arrayOfItems.set(editedItemPosition, newItem);
            itemsAdapter.notifyDataSetChanged();

            dbHelper.updateItem(oldItem, newItem);
        }
    }
}
