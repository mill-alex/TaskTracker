package com.example.tasktracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

/**
 * A Fragment subclass for editing a pre-existing Task.
 * Use the TaskEditFragment#newInstance factory method to
 * create an instance of this fragment.
 */
public class TaskEditFragment extends Fragment {

    // the fragment initialization parameters
    private static final String ARG_PARAM = "tef_param"; //Key to get task from bundle
    private Task task; //Task which is being edited

    //UI Elements for this fragment
    private CheckBox check_box;
    private EditText et_name;
    private EditText et_details;

    // Required empty public constructor
    public TaskEditFragment() { }

    /**
     * Factory method necessary to use in order to create a new
     * instance of this fragment using the provided parameters.
     *
     * @param task The task which is editable within this fragment view
     * @return A new instance of fragment TaskEditFragment.
     */
    public static TaskEditFragment newInstance(Task task) {
        TaskEditFragment fragment = new TaskEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, task.toJson());
        fragment.setArguments(args);
        return fragment;
    }

    //Initializes the empty fragment with the provided task
    @Override
    public void setArguments(Bundle bundle)
    {
        task = Task.fromJson(bundle.getString(ARG_PARAM));
    }

    //Inflates the view of this fragment when it is being displayed
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    //Populate the UI elements of this fragments after they are inflated and set click listeners
    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle)
    {
        et_name = getView().findViewById(R.id.etTaskName);
        et_name.setText(task.getName());
        et_name.setOnFocusChangeListener(HideKeyboardHelper.hideKeyboardOnUnfocus());

        et_details = getView().findViewById(R.id.etTaskDescription);
        et_details.setText(task.getDetails());
        et_details.setOnFocusChangeListener(HideKeyboardHelper.hideKeyboardOnUnfocus());

        check_box = getView().findViewById(R.id.completeCheckBox);
        check_box.setChecked(task.getComplete());

        //Change to 'View' tab upon save button being clicked
        getView().findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //No need to call getUpdatedTask() here because ViewTask calls that upon tab transition
                TabLayout tl =  ((ViewTask) getActivity()).tab_layout;
                tl.selectTab(tl.getTabAt(0));
            }
        });
    }

    //Updates the task being edited, and returns the updated task
    public Task getUpdatedTask()
    {
        task.setName(et_name.getText().toString());
        task.setDetails(et_details.getText().toString());
        task.setComplete(check_box.isChecked());
        return task;
    }
}