package com.example.sqlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sqlite.adapter.TaskAdapter;
import com.example.sqlite.entity.Task;
import com.example.sqlite.helper.Database;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Database database;
    ArrayList<Task> taskArrayList;
    ListView taskList;
    TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskList = findViewById(R.id.task_list);
        taskArrayList = new ArrayList<>();
        taskAdapter = new TaskAdapter(this, R.layout.work_item, taskArrayList);
        taskList.setAdapter(taskAdapter);


        database = new Database(this, "Noted.sqlite", null, 1);

        database.queryData("Create table if not exists Task(id Integer Primary Key " +
                "Autoincrement, WorkName nvarchar(200))");

//        insert data
//        database.queryData("Insert into Task values(null, 'Project Android')");
//        database.queryData("Insert into Task values(null, 'Design App')");

        getDataTask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.add_work, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == R.id.add_task){
            dialogAdd();
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogAdd(){
        Dialog addDialog = new Dialog(this);
        addDialog.setContentView(R.layout.dialog_add_work);

        EditText taskEditText = addDialog.findViewById(R.id.add_task_text);
        Button cancelButton = addDialog.findViewById(R.id.cancel_button);
        Button addButton = addDialog.findViewById(R.id.add_button);


        addButton.setOnClickListener(v -> {
            String taskName = taskEditText.getText().toString();

            if (taskName.equals("")){
                Toast.makeText(this, "Please insert task!", Toast.LENGTH_SHORT).show();
            } else {
                database.queryData("Insert into Task values(null, '" + taskName + "')");
                Toast.makeText(this, "Add Successfully!", Toast.LENGTH_SHORT).show();
                addDialog.dismiss();
                getDataTask();
            }
        });

        cancelButton.setOnClickListener(v ->
            addDialog.dismiss()
        );

        addDialog.show();
    }

    private void getDataTask(){

        Cursor dataTask = database.getData("select * from Task");

        taskArrayList.clear();

        while (dataTask.moveToNext()){
            String name = dataTask.getString(1);
            int id = dataTask.getInt(0);
            taskArrayList.add(new Task(id, name));
        }

        taskAdapter.notifyDataSetChanged();
    }

    public void dialogEditTask(String name, int id){
        Dialog editDialog = new Dialog(this);
        editDialog.setContentView(R.layout.dialog_edit_task);

        EditText editTaskText = editDialog.findViewById(R.id.edit_task_text);
        Button cancelButton = editDialog.findViewById(R.id.cancel_button);
        Button updateButton = editDialog.findViewById(R.id.update_button);

        editTaskText.setText(name);

        updateButton.setOnClickListener(v -> {
            String taskName = editTaskText.getText().toString();

            if (taskName.equals("")){
                Toast.makeText(this, "Please insert task!", Toast.LENGTH_SHORT).show();
            } else {
                database.queryData("Update Task set WorkName = '" + taskName + "' Where id = '"
                        + id + "'");
                Toast.makeText(this, "Update Successfully!", Toast.LENGTH_SHORT).show();
                editDialog.dismiss();
                getDataTask();
            }
        });

        cancelButton.setOnClickListener(v ->
                editDialog.dismiss()
        );

        editDialog.show();
    }

    public void dialogDeleteTask(String name, int id){
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setMessage("Are you sure you want to delete " + name + " task?");
        deleteDialog.setPositiveButton("Yes", (dialog, which) -> {
            database.queryData("delete from Task where id = '" + id + "'");
            Toast.makeText(MainActivity.this, "You deleted " + name, Toast.LENGTH_SHORT).show();
            getDataTask();
        });

        deleteDialog.setNegativeButton("No", (dialog, which) -> {

        });

        deleteDialog.show();
    }
}