package com.codepath.simpletodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Himanshu on 9/11/2015.
 */
public class TodoItemAdapter extends ArrayAdapter<TodoItem> {

    private static class ViewHolder {
        CheckBox name;
        TextView description;
        ImageButton editButton;
        ImageButton deleteButton;
    }

    public TodoItemAdapter (Context context, ArrayList<TodoItem> items){
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TodoItem item = getItem(position);

        final ViewHolder viewHolder;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
            viewHolder.name = (CheckBox) convertView.findViewById(R.id.tvName);
            viewHolder.description = (TextView) convertView.findViewById(R.id.tvDesc);
            viewHolder.editButton = (ImageButton) convertView.findViewById(R.id.editButton);
            viewHolder.deleteButton = (ImageButton) convertView.findViewById(R.id.deleteButton);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(item.name);
        viewHolder.description.setText(item.description);
        viewHolder.name.setChecked(item.status);

        viewHolder.name.setOnClickListener((MainActivity) getContext());
        viewHolder.editButton.setOnClickListener((MainActivity) getContext());
        viewHolder.deleteButton.setOnClickListener((MainActivity) getContext());

        return convertView;
    }
}
