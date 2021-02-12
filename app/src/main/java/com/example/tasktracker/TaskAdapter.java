package com.example.tasktracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;

/*
    Adapter which inflates and shows the content within the RecyclerView in the
    main activity. Uses a ViewHolder inner class.  This class is specifically meant
    for use from the main activity.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private final List<Task> tasks;
    public AdapterListenerHelper listener;

    //Constructor which takes in the required parameters for use within this adapter.
    //The task list should never be null and the AdapterListenerHelper should have its
    //method defined elsewhere
    public TaskAdapter(@NonNull List<Task> task_list, AdapterListenerHelper alh)
    {
        tasks = task_list;
        listener = alh;
    }

    //Inflates the adapter within the given parent view and returns
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int view_type)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_main, parent, false);
        return new ViewHolder(v, listener);
    }

    //Operate on RecyclerView UI elements once they're bound to the view holder
    @Override
    public void onBindViewHolder(TaskAdapter.ViewHolder view_holder, int index)
    {
        Task task = tasks.get(index);
        view_holder.textview_name.setText(task.getName());
        view_holder.textview_name.setHorizontallyScrolling(true);
        view_holder.textview_name.setSelected(true);
        view_holder.checkbox_done.setChecked(task.getComplete());
        view_holder.checkbox_done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tasks.get(index).setComplete(isChecked);
            }
        });
    }

    //Provides the number of items in this adapter
    @Override
    public int getItemCount()
    {
        return tasks.size();
    }


    //Inner class which sets the template for each view within the
    //Recycler view adapter.  The template is how each task is
    //represented by UI elements.
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textview_name;
        public CheckBox checkbox_done;
        public Button view_button;
        private final WeakReference<AdapterListenerHelper> weak_alh;

        //Finds the UI elements to use as the Adapter template and
        //sets the click listener for the 'View' button
        public ViewHolder(View content_view, AdapterListenerHelper alh)
        {
            super(content_view);
            weak_alh = new WeakReference<>(alh);
            textview_name = content_view.findViewById(R.id.textViewTaskName);
            checkbox_done = content_view.findViewById(R.id.completedTaskCheckBox);
            view_button = content_view.findViewById(R.id.taskDetailsButton);

            //Use the adapterlistenerhelper for when the view button is clicked
            view_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    weak_alh.get().onClickedTask(getBindingAdapterPosition());
                }
            });
        }
    }
}
