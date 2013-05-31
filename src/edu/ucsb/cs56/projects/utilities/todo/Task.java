package edu.ucsb.cs56.projects.utilties.todo;

import java.util.Calendar;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class Task {

	private String taskName;
	private Calendar dueDate;
	private boolean completed;
	//private int numSubtasks;
	//private ArrayList<Task> subTasks;

	public Task(String taskName, int year, int month, int day, int hour, int min)
	{
		this.taskName = taskName;
		this.dueDate = new GregorianCalendar(year, month, day, hour, min);
		this.completed = false;
	}

	public String getName()
	{
		return this.taskName;
	}

	public String getDueDate()
	{
		SimpleDateFormat date_format = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		return date_format.format(this.dueDate.getTime());
	}

	public String toString()
	{
		String result = "";

		if (completed == false)
		{
			result = "[ ] " + this.getName() + " " + this.getDueDate();
		}
		return result;
	}

	//TODO1: Constuctor

}