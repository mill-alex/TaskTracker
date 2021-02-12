package com.example.tasktracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/*
    Activity class responsible for handling events on the 'Add Task' screen (see add_task.xml).
    The purpose of this screen is to provide a way for users to add new tasks.
 */
public class AddTask extends AppCompatActivity{

    private Intent return_intent; //Could be made local in current activity configuration

    //Loads the UI elements into instance objects and specifies their behavior
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        EditText et_taskname = findViewById(R.id.editTextTaskName);
        et_taskname.setOnFocusChangeListener(HideKeyboardHelper.hideKeyboardOnUnfocus());

        EditText et_description = findViewById(R.id.editTextTaskDescription);
        et_description.setOnFocusChangeListener(HideKeyboardHelper.hideKeyboardOnUnfocus());

        Button create_button = findViewById(R.id.createButton);
        create_button.setOnClickListener(new View.OnClickListener() {

            /* Specifies the app behavior for when the 'CREATE' button is clicked. That is, this
               Constructs a task from the information entered in the UI fields and puts that task in the
               return intent, after which this entire activity is ended
            */
            @Override
            public void onClick(View v) {
                Task new_task = new Task(et_taskname.getText().toString(), et_description.getText().toString());
                return_intent.putExtra("task", new_task.toJson());
                setResult(MainActivity.NORMAL_ADD_TASK, return_intent);
                finish();
            }
        });

        return_intent = new Intent();
    }
}
