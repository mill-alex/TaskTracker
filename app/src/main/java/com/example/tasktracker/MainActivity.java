package com.example.tasktracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

//Main Activity which controls the TaskTracker app and its initialization,
//destruction, and other activity calls
public class MainActivity extends AppCompatActivity {

    private RecyclerView recycler_view;
    private AdapterListenerHelper alh;
    private List<Task> tasks;
    private SharedPreferences sp; //SHOULD NOT BE REASSIGNED after oncreate(). Could be made 'final' in a helper class.
    private Toolbar toolbar;
    private CoordinatorLayout cl;
    private TextView notasks_textview;

    //Fields used for activity results
    public static final int NORMAL_ADD_TASK = 1;
    public static final int DELETED_VIEW_TASK = 5;
    public static final int VIEWED_OR_EDITED_TASK = 6;

    //Defines app behavior upon being opened
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        cl = findViewById(R.id.mainCoordinatorLayout);
        notasks_textview = findViewById(R.id.noTasksTextView);

        //Set floating action button to open 'AddTask' activity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getBaseContext(), AddTask.class), NORMAL_ADD_TASK); //1 is request code
            }
        });

        //Define interface behavior used in RecyclerView & TaskAdapter
        alh = position -> {
            Task task = tasks.get(position);
            Intent intent = new Intent(getBaseContext(), ViewTask.class);
            intent.putExtra("t", task.toJson());
            startActivityForResult(intent, position);
        };

        recycler_view = findViewById(R.id.recyclerview);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));

        tasks = new ArrayList<Task>();
        loadAllTasks();

        refreshRecycler(tasks);
    }

    //Responds to an activity result. The meaning of request_code and relevance of the intent
    //data are dependant on the result_code.
    @Override
    protected void onActivityResult(int request_code, int result_code, Intent data) {
        super.onActivityResult(request_code, result_code, data);

        if (result_code == NORMAL_ADD_TASK) {
            tasks.add(Task.fromJson(data.getStringExtra("task")));

            //This is where every time tasks.size goes from 0 to 1 is flagged
            if (tasks.size() == 1) {
                cl.removeView(notasks_textview);
            }
        } else if (result_code == DELETED_VIEW_TASK) {
            tasks.remove(request_code);
        } else if (result_code == VIEWED_OR_EDITED_TASK) {
            tasks.set(request_code, Task.fromJson(data.getStringExtra("task")));
        }

        refreshRecycler(tasks);
    }

    //Ensures app data is stored upon being paused
    @Override
    protected void onPause() {
        super.onPause();

        storeTasks();
    }

    //Loads all tasks into the recycler view from SharedPreferences
    public void loadAllTasks() {
        boolean flag = false;
        int i = 0;

        while (!flag) {
            String name = sp.getString(String.valueOf(i), "invalid");

            if (!name.equals("invalid")) {
                tasks.add(loadTask(name));
                i++;
            } else {
                flag = true;
            }
        }
    }

    //Loads a single task from SharedPreferences
    public Task loadTask(String task_name) {
        String details = sp.getString(task_name, "invalid");

        if (details.equals("invalid")) {
            return new Task(task_name, "", sp.getBoolean(task_name + "bool", false));
        } else {
            return new Task(task_name, details, sp.getBoolean(task_name + "bool", false));
        }
    }

    //Delete all previously stored data and stores all tasks into SharedPreferences
    public void storeTasks() {
        SharedPreferences.Editor sp_editor = sp.edit();
        sp_editor.clear();
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            sp_editor.putString(String.valueOf(i), t.getName());
            sp_editor.putString(t.getName(), t.getDetails());
            sp_editor.putBoolean(t.getName() + "bool", t.getComplete());
        }

        sp_editor.apply();
    }

    //Refreshes the recycler view of tasks with the given task list
    //Also shows or hides a message when there are no tasks to display, along with onActivityResult()
    public void refreshRecycler(List<Task> task_list) {
        if (task_list.size() == 0) {
            if (notasks_textview.getParent() == null) {
                cl.addView(notasks_textview);
            }

            recycler_view.setAlpha(0); //Hide Empty RecyclerView
            recycler_view.setClickable(false);
        } else {
            if (task_list.size() == 1) {
                recycler_view.setClickable(true);
                recycler_view.setAlpha(1);
            }

            if (notasks_textview.getParent() != null) {
                cl.removeView(notasks_textview);
            }

            RecyclerView.Adapter<TaskAdapter.ViewHolder> adapter = new TaskAdapter(task_list, alh);
            recycler_view.setAdapter(adapter);
        }
    }
}
    /* Standard code for options menu left commented out here
        but intended for use in future version.
     */

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }