package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.example.todoapp.MainActivity.ITEM_POSITION;
import static com.example.todoapp.MainActivity.ITEM_TEXT;

public class EditTaskActivity extends AppCompatActivity {

    EditText editTextItem;
    int itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        String itemText = getIntent().getStringExtra(ITEM_TEXT);
        itemPosition = getIntent().getIntExtra(ITEM_POSITION, 0);

        // Get Edit Text item and set original text value to be the current task's text
        editTextItem = findViewById(R.id.EditTaskItem);
        editTextItem.setText(itemText);

        // Get the Save Task button and create onclick listener to save new task info
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditTaskActivity.this, MainActivity.class);
                i.putExtra(ITEM_TEXT, editTextItem.getText().toString());
                i.putExtra(ITEM_POSITION, itemPosition);
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }
}