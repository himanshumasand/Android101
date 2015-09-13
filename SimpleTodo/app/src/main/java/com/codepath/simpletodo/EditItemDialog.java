package com.codepath.simpletodo;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by Himanshu on 9/13/2015.
 */
public class EditItemDialog extends DialogFragment implements View.OnClickListener{

    private EditText mEditName;
    private EditText mEditDesc;

    public EditItemDialog() {}

    public interface EditItemDialogListener {
        void onFinishEditDialog(TodoItem newItem);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    public static EditItemDialog newInstance(TodoItem item) {
        EditItemDialog frag = new EditItemDialog();
        Bundle args = new Bundle();
        args.putString("title", null);
        args.putString("name", item.name);
        args.putString("desc", item.description);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_item, container);
        mEditName = (EditText) view.findViewById(R.id.txt_name);
        mEditDesc = (EditText) view.findViewById(R.id.txt_desc);
        String title = getArguments().getString("title", "Edit Item");
        String name = getArguments().getString("name", "");
        String desc = getArguments().getString("desc", "");
        getDialog().setTitle(title);
        mEditName.setText(name);
        mEditDesc.setText(desc);
        mEditDesc.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        Button saveButton = (Button) view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(this);
        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.save_button:
                EditItemDialogListener listener = (EditItemDialogListener) getActivity();
                TodoItem newItem = new TodoItem(mEditName.getText().toString(), mEditDesc.getText().toString(), false);
                listener.onFinishEditDialog(newItem);
                dismiss();
                break;

            case R.id.cancel_button:
                dismiss();
                break;
        }
    }
}
