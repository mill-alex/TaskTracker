package com.example.tasktracker;

import com.google.gson.Gson;

// Class which represents the instance of a Task/ collection of string data that the user wants stored.
// Tasks are the basis for the entire application.
public class Task {

    //Data elements for this task
    private String name;
    private boolean complete;
    private String details;

    //Simple constructor for an incomplete task with a name and no details
    Task(String task_name)
    {
        name = task_name;
        details = "";
        complete = false;
    }

    //Constructor for a given name and details.  Assumes tasks is complete upon creation
    Task(String task_name, String task_details)
    {
        name = task_name;
        details = task_details;
        complete = false;
    }

    //Constructor which sets the data elements to equal the provided parameters
    Task(String task_name, String task_details, boolean complete)
    {
        this(task_name, task_details);
        this.complete = complete;
    }

    //Returns this task's name
    public String getName()
    {
        return name;
    }

    //Returns this task's details
    public String getDetails()
    {
        return details;
    }

    //Returns whether or not this task is completed
    public boolean getComplete()
    {
        return complete;
    }

    //Sets the complete status of the task to the given completeness status (newState).
    public void setComplete(boolean newState)
    {
        complete = newState;
    }

    //Sets the name for this task to the provided parameter newName
    public void setName(String newName) { name = newName;}

    //Updates the details for this task to the provided parameter newDetails.
    public void setDetails(String newDetails) { details = newDetails;}

    //Converts this Task object in a json String which is useful
    //for passing the entire object as a String within a Bundle
    public String toJson()
    {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    //Static method for converting a json representation of a task
    //back into an instance of Task. The task is then returned.
    public static Task fromJson(String json)
    {
        Gson gson = new Gson();
        return gson.fromJson(json, Task.class);
    }
}
