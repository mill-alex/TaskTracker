package com.example.tasktracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

/*
    An activity for the screens where users can view and edit tasks.
    This activity holds the tab layout for the two use cases and manages
    transactions between tabs, while TaskViewFragment and
    TaskEditFragment handle the processes within each screen.
 */
public class ViewTask extends AppCompatActivity {

    private Task task;
    public TabLayout tab_layout; //Public because of access from within TaskEditFragment
    private Fragment fragment;

    //Initialization of this activity being created.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        task = Task.fromJson(getIntent().getStringExtra("t"));
        updateTitle();

        tab_layout = findViewById(R.id.tab_layout);

        //Specify behavior for tabs selected/unselected/reselected in the tab layout
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //A new tab is selected, which indicates that a fragment transaction must occus
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentManager fragment_manager = getSupportFragmentManager();
                FragmentTransaction fragment_transaction = fragment_manager.beginTransaction();
                fragment_transaction.setReorderingAllowed(true);

                if(tab.getPosition() == 0)
                {
                    fragment = TaskViewFragment.newInstance(task);
                }
                else
                {
                    fragment = TaskEditFragment.newInstance(task);
                }

                fragment_transaction.replace(R.id.placeHolderLayout, fragment);
                fragment_transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragment_transaction.commit();
            }

            //Save the potentially edited task if the Edit tab is unselected
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition() == 1 && fragment instanceof TaskEditFragment)
                {
                    task = ((TaskEditFragment) fragment).getUpdatedTask();
                    updateTitle();
                }
            }

            //Update View tab to display the correct fragment if it is reselected
            //This is used upon the ViewTask Activity creation to initially populate
            //the activity with the TaskViewFragment.
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0)
                {
                    FragmentManager fragment_manager = getSupportFragmentManager();
                    FragmentTransaction fragment_transaction = fragment_manager.beginTransaction();
                    fragment_transaction.setReorderingAllowed(true);

                    fragment = TaskViewFragment.newInstance(task);

                    fragment_transaction.replace(R.id.placeHolderLayout, fragment);
                    fragment_transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragment_transaction.commit();
                }
            }
        });

        tab_layout.selectTab(tab_layout.getTabAt(0));

        Context cntx = this;

        //Define the 'delete' fab and specify that an Alert Dialog should be created upon it being clicked
        FloatingActionButton fab = findViewById(R.id.deleterFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertDialog(cntx);
            }
        });
    }

    //Ensure activity is finished upon the back button being pressed
    @Override
    public void onBackPressed()
    {
        if(fragment instanceof TaskViewFragment)
        {
            task = ((TaskViewFragment) fragment).task;
        }
        else if(fragment instanceof TaskEditFragment)
        {
            task = ((TaskEditFragment) fragment).getUpdatedTask();
        }

        returnToMain(MainActivity.VIEWED_OR_EDITED_TASK);
        super.onBackPressed();
    }

    //Create and display an Alert Dialog to ensure the user wants to delete a task
    public void createAlertDialog(Context cntx)
    {
        AlertDialog.Builder adb = new AlertDialog.Builder(cntx);
        adb.setMessage("Are you sure you want to permanently delete this task?");

        //Try to dismiss dialog & delete task on click
        adb.setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    dialog.dismiss();
                    returnToMain(MainActivity.DELETED_VIEW_TASK);
                } catch (java.lang.NullPointerException npe) {
                    returnToMain(MainActivity.DELETED_VIEW_TASK);
                }
            }
        });

        //Try to dismiss dialog on click
        adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    dialog.dismiss();
                } catch (java.lang.NullPointerException npe) {
                    returnToMain(MainActivity.VIEWED_OR_EDITED_TASK);
                }
            }
        });

        adb.show();
    }

    //Sets the title of the action bar to the current task name
    public void updateTitle()
    {
        getSupportActionBar().setTitle(task.getName());
    }

    //Ends this activity and returns this activity's result to the main activity
    public void returnToMain(int result)
    {
        Intent intent = new Intent();
        intent.putExtra("task", task.toJson());
        setResult(result, intent);
        finish();
    }

}