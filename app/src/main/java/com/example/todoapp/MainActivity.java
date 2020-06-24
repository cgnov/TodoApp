package com.example.todoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Defines numeric code to identify Edit Activity
    public final static int EDIT_REQUEST_CODE = 20;
    // Defines keys used for passing data to Edit Activity
    public final static String ITEM_TEXT = "itemText";
    public final static String ITEM_POSITION = "itemPosition";

    List<String> tasks;

    Button btnAdd;
    EditText edTxt;
    RecyclerView rvTasks;
    TaskAdapter taskAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        edTxt = findViewById(R.id.edTxt);
        rvTasks = findViewById(R.id.taskList);

        loadItems();

        TaskAdapter.OnLongClickListener onLongClickListener = new TaskAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // Delete item from list;
                tasks.remove(position);
                // Notify the adapter that task was removed
                taskAdapter.notifyItemRemoved(position);
                // Confirm to user that task has been removed
                Toast.makeText(getApplicationContext(), "Task was removed", Toast.LENGTH_SHORT).show();
                // Save current list of items
                saveItems();
            }
        };

        TaskAdapter.OnClickListener onClickListener = new TaskAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                // Declare intent to go to Edit Task Activity
                Intent i = new Intent(MainActivity.this, EditTaskActivity.class);
                // Send info regarding position of task to edit
                i.putExtra(ITEM_POSITION, position);
                i.putExtra(ITEM_TEXT, tasks.get(position));
                // Bring up Edit Task Activity
                startActivityForResult(i, EDIT_REQUEST_CODE);
            }
        };

        taskAdapter = new TaskAdapter(tasks, onLongClickListener, onClickListener);
        rvTasks.setAdapter(taskAdapter);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get text from Edit Text field
                String task = edTxt.getText().toString();
                // Add item to task list
                tasks.add(task);
                // Notify adapter we've added a task
                taskAdapter.notifyItemInserted(tasks.size()-1);
                // Reset text in Edit Text field to be blank
                edTxt.setText("");
                // Confirm to user that task has been submitted
                Toast.makeText(getApplicationContext(), "Task was added", Toast.LENGTH_SHORT).show();
                // Save current list of items
                saveItems();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE){
            // Get new task text from extras
            assert data != null;
            String newTaskText = data.getExtras().getString(ITEM_TEXT);
            int position = data.getExtras().getInt(ITEM_POSITION);
            tasks.set(position, newTaskText);
            taskAdapter.notifyDataSetChanged();
            saveItems();
            Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show();
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    // Function reads lines of data.txt and loads them into task list
    // Called when app loads
    private void loadItems(){
        try {
            tasks = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch( IOException e) {
            Log.e("MainActivity", "Error reading in tasks", e);
            tasks = new ArrayList<>();
        }
    }

    // Function saves tasks by saving them into data.txt
    // Called every time item is added or removed
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), tasks);
        } catch (IOException e) {
            Log.e("MainActivity", "Error saving tasks", e);
        }
    }
}