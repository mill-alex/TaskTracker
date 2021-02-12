package com.example.tasktracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * A Fragment subclass.
 * Use the TaskViewFragment#newInstance factory method to
 * create an instance of this fragment.
 */
public class TaskViewFragment extends Fragment {

    public Task task;
    private final static String ARG_PARAM = "key";

    public TaskViewFragment() { }    // Required empty public constructor

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param task The task which this fragment provides a view for.
     * @return A new instance of fragment TaskViewFragment.
     */
    public static TaskViewFragment newInstance(Task task) {
        TaskViewFragment fragment = new TaskViewFragment();
        Bundle args = new Bundle();
        args.putString(TaskViewFragment.ARG_PARAM, task.toJson());
        fragment.setArguments(args);
        return fragment;
    }

    //Retrieves the task being viewed from the arguments bundle
    @Override
    public void setArguments(Bundle bundle)
    {
        task = Task.fromJson(bundle.getString(ARG_PARAM));
    }

    //Inflates the view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view, container, false);
    }

    //Initialize UI elements for this fragment's view
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        TextView title_textview = getView().findViewById(R.id.TextViewTaskName);
        title_textview.setText(task.getName());
        TextView description_textview = getView().findViewById(R.id.TextViewTaskDescription);
        description_textview.setText(task.getDetails());

        TextView completion_textview = getView().findViewById(R.id.textViewCompletion);
        if(task.getComplete())
        {
            completion_textview.setText(R.string.completed);
            completion_textview.setTextColor(0xFF03DAC5);
        }
        else
        {
            completion_textview.setText(R.string.incomplete);
            completion_textview.setTextColor(0xFFFFC107);
        }
    }
}