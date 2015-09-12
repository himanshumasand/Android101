package com.codepath.simpletodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Himanshu on 9/11/2015.
 */
public class TodoItemAdapter extends ArrayAdapter<TodoItem> {

    private static class ViewHolder {
        TextView name;
        TextView description;
        TextView status;
    }

    public TodoItemAdapter (Context context, ArrayList<TodoItem> items){
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TodoItem item = getItem(position);

        ViewHolder viewHolder;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.description = (TextView) convertView.findViewById(R.id.tvDesc);
            viewHolder.status = (TextView) convertView.findViewById(R.id.tvStatus);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(item.name);
        viewHolder.description.setText(item.description);
        if(item.status == true) {
            viewHolder.status.setText("Complete");
        }
        else {
            viewHolder.status.setText("Pending");
        }

        return convertView;
    }
}
