package com.example.tasktracker;

/*
    This interface is to help Main Activity's Recycler View respond to any button clicks which
    start an activity. Since only activities can start other activities, and Main Activity's
    Recycler View only has access to the Adapter as a whole not its views, this interface allows
    the Adapter to know when and where a click occurs in TaskAdapter and pass on the appropriate task
     while defining the response to the click within Main Activity.
 */
public interface AdapterListenerHelper {

    /*
        Define how a clicked-on task view should be handled.
        @param Task The task that was clicked on.
     */
    void onClickedTask(int task_position);
}
