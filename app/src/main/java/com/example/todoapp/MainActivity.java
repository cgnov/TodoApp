package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LauncherActivity;
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
        taskAdapter = new TaskAdapter(tasks, onLongClickListener);
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