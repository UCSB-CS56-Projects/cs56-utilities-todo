package edu.ucsb.cs56.projects.utilties.todo;

import java.util.Calendar;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.io.Serializable;

/**
	A single task in a todo list

	@author Brandon Newman
	@version todo project for CS56 S13
*/
public class Task implements Serializable {

	private String taskName;
	private Calendar dueDate;
	private boolean completed;
	private ArrayList<Task> subtasks = new ArrayList<Task>();
	private Task parentTask;

	/**
	Constructor for a single task
	@parem taskName the name of the new task
	@parem year the year in which the due date is
	@parem month the month in which the due date is
	@parem day the day in which the due date is
	@parem hour the hour in which the due date is
	@parem min the minute at which the due date is
	*/
	public Task(String taskName, int year, int month, int day, int hour, int min, Task parentTask)
	{
		this.taskName = taskName;
		this.dueDate = new GregorianCalendar(year, month, day, hour, min);
		this.completed = false;
		this.parentTask = parentTask;
	}

	/**
	No arg constructor for a single Task
	*/
	public Task() {}

	/**
	Returns the name of the task
	*/
	public String getName()
	{
		return this.taskName;
	}

	/**
	Returns the due date as a String in the format of "month/day/year hour:minute" ex. "05/21/13 04:00"
	*/
	public String getDueDate()
	{
		SimpleDateFormat date_format = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		return date_format.format(this.dueDate.getTime());
	}
	/**
	Returns the due date as a Calendar object
	*/
	public Calendar getCalendarForm()
	{
		return this.dueDate;
	}

	/**
	Returns the parent Task of the current Task
	*/
	public Task getParentTask()
	{
		return this.parentTask;
	}

	/**
	Marks a task as completed
	*/
	public void markCompleted()
	{
		this.completed = true;
	}

	/**
	Returns the completion status of a particular task
	*/
	public boolean getCompleted()
	{
		return this.completed;
	}

	/**
	Returns the ArrayList of subtasks for a given task
	*/
	public ArrayList<Task> getSubTasksList()
	{
		return subtasks;
	}

	/**
	Overwriting the toString method. Prints in the format of:
	"[ ] task name month/day/year hour:minute"
	example:
	"[ ] uncompleted task 05/22/13 04:22"
	*/
	public String toString()
	{
		String result = "";

		if (this.completed == false)
			result = "[ ] " + this.getName() + " " + this.getDueDate();
		else
			result = "[x] " + this.getName() + " " + this.getDueDate();

		return result;
	}

}