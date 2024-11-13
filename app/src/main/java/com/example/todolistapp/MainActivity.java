// MainActivity.java
package com.example.todolistapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskClickListener {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tasks = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(tasks, this);
        recyclerView.setAdapter(taskAdapter);

        Button addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(v -> showTaskDialog(null, -1));
    }

    private void showTaskDialog(String taskTitle, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(position == -1 ? "Add Task" : "Edit Task");

        final EditText input = new EditText(this);
        input.setText(taskTitle);
        builder.setView(input);

        builder.setPositiveButton(position == -1 ? "Add" : "Update", (dialog, which) -> {
            String title = input.getText().toString();
            if (title.isEmpty()) {
                Toast.makeText(this, "Task cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                if (position == -1) {
                    tasks.add(new Task(title));
                } else {
                    tasks.get(position).setTitle(title);
                }
                taskAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public void onEditClick(int position) {
        Task task = tasks.get(position);
        showTaskDialog(task.getTitle(), position);
    }

    @Override
    public void onDeleteClick(int position) {
        tasks.remove(position);
        taskAdapter.notifyItemRemoved(position);
    }
}
