package edu.ucsb.cs56.projects.utilties.todo;

import java.util.Calendar;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;


public class Task {

	private String taskName;
	private Calendar dueDate;
	private boolean completed = false;
	//private int numSubtasks;
	//private ArrayList<Task> subTasks;

	public Task(String taskName, int year, int month, int day, int hour, int min)
	{
		this.taskName = taskName;
		this.dueDate = new GregorianCalendar(year, month, day, hour, min);
	}

	public void getName()
	{
		System.out.println(this.taskName);
	}

	//TODO1: Constuctor

}