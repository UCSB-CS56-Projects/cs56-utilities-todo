package edu.ucsb.cs56.projects.utilties.todo;

import java.util.Calendar;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.io.Serializable;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

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
        private JCheckBox check;
        private JButton deleteButton;
        private JLabel label;
	/**
	Constructor for a single task
	@param taskName the name of the new task
	@param year the year in which the due date is
	@param month the month in which the due date is
	@param day the day in which the due date is
	@param hour the hour in which the due date is
	@param min the minute at which the due date is
	*/
	public Task(String taskName, int year, int month, int day, int hour, int min, Task parentTask)
	{
		this.taskName = taskName;
		//if no date and time
		if (day == -1 && hour == -1)
		this.dueDate = null;

		//if no date//
		else if (day == -1){
		    this.dueDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 
							 Calendar.getInstance().get(Calendar.DATE), hour, min);
					 
		}    
		//if no time
		else if (hour == -1)
		    this.dueDate = new GregorianCalendar(year, month, day);
		    
		//default
		else
		    {this.dueDate = new GregorianCalendar(year, month, day, hour, min);}
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
	    if (this.dueDate == null) {
		return "";
	    }
	    else {
		SimpleDateFormat date_format = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		return date_format.format(this.dueDate.getTime());
	    }
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
	    if (subtasks.size() != 0)
		System.out.println("## THE SUBTASKS IS BEING USED!! ##");
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
		if (this.dueDate != null){
		    if (this.completed == false)
			result = "[ ] " + this.getName() + " " + this.getDueDate();
		    else
			result = "[x] " + this.getName() + " " + this.getDueDate();
		}
		else{
		    if (this.completed == false)
			result = "[ ]" + " " + this.getName();
		    else
			result = "[x]" + " " +  this.getName();
		}
		return result;
	}
    public void setCheck(JCheckBox check){this.check = check;}
    public JCheckBox getCheck(){return this.check;}
    public void setDelete(JButton deleteButton){this.deleteButton = deleteButton;}
    public JButton getDelete(){return this.deleteButton;}
    public void setLabel(){
	this.label = new JLabel(this.taskName);
	this.label.setPreferredSize(new Dimension(470,20));
    }
    public JLabel getLabel(){
	this.setLabel();
	return this.label;
    }

}
