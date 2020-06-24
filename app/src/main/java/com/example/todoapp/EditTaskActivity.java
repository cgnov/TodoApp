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
        // Collect information sent from MainActivity regarding task to edit
        String itemText = getIntent().getStringExtra(ITEM_TEXT);
        itemPosition = getIntent().getIntExtra(ITEM_POSITION, 0);

        // Set Edit Text item's original text value to be the task's current value
        editTextItem = findViewById(R.id.EditTaskItem);
        editTextItem.setText(itemText);

        // Create onclick listener for Save Task button to send information back to Main Activity
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