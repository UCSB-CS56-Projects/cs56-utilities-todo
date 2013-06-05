package edu.ucsb.cs56.projects.utilties.todo;

import edu.ucsb.cs56.projects.utilties.todo.Task;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.io.*;

/**
	A represention of a list of Tasks. Does this by having an Arraylist be private

	@author Brandon Newman
	@version todo project for CS56 S13
*/
public class TodoList implements Serializable {

	private ArrayList<Task> tasks = new ArrayList<Task>();

    /**
	Getter for the number of task in a list
	*/

	public ArrayList<Task> getTasks()
	{
		return this.tasks;
	}

	/**
	Method that prints out a few lines of code to make a "pretty" interface on the command line
	*/
	public void printTasks(ArrayList<Task> tasklist, int heriarchyNum)
	{
		for (int i = 0; i < tasklist.size() ; i++)
		{
			String returnString = "";

			for(int j = 0; j < heriarchyNum; j++)
				returnString = returnString +"   ";

			returnString = returnString + tasklist.get(i).toString();

			System.out.println(returnString);

			if (tasklist.get(i).getSubTasksList() != null)
				printTasks(tasklist.get(i).getSubTasksList(), heriarchyNum+1);
		}
	}

	/**
	Method that instigates dialog asking for input of a task, and due date from the user
	It then creates a new Task, and puts that in the ArrayList
	*/
	public void addTasks()
	{
		Scanner scanner = new Scanner(System.in);
		System.out.println("What's the name of the task?");
		String taskName = scanner.nextLine();
		System.out.println("What's its due date?");
		String date = scanner.nextLine();
		//TODO: check and see if date is formmatted correctly
		System.out.println("At what time?");
		String time = scanner.nextLine();

		//PARING AND ASSIGNING NAME AND DATE VALUES
		//BEGINS
		String nameDelims = "[/]";
		String[] nameTokens = taskName.split(nameDelims);

		String dateDelims = "[/]";
		String[] dateTokens = date.split(dateDelims);
		
		String timeDelims = "[:]";
		String[] timeTokens = time.split(timeDelims);

		int month = Integer.parseInt(dateTokens[0]) -1;
		int day   = Integer.parseInt(dateTokens[1]);
		int year  = Integer.parseInt(dateTokens[2]) + 2000;
		int hour  = Integer.parseInt(timeTokens[0]);
		int min   = Integer.parseInt(timeTokens[1]);

		if (nameTokens.length == 1)
			taskName = taskName;
		else
			taskName = nameTokens[nameTokens.length -1];
		//ENDS

		if (nameTokens.length > 1)
		{
			Task parentTask = this.search(nameTokens[nameTokens.length-2]);

			if (parentTask != null)
			{
				Task newTask = new Task(taskName, year, month, day, hour, min, parentTask);
				parentTask.getSubTasksList().add(parentTask.getSubTasksList().size(), newTask);
			}
			else
				System.out.println("Not a valid parent task!");
		}
		else
		{
			Task newTask = new Task(taskName, year, month, day, hour, min, null);
			this.tasks.add(newTask);
		}

	}

	/**
	Method that instigates dialog asking for input to delete a task
	*/
	public void deleteTasks()
	{
		Scanner scanner = new Scanner(System.in);
		System.out.println("What's the task would you like to delete?");
		String taskName = scanner.nextLine();

		Task findTask = this.search(taskName);

		if (findTask != null && findTask.getParentTask() != null)
		{
			Task parentTask = findTask.getParentTask();
			parentTask.getSubTasksList().remove(findTask);
		}
		else if (findTask != null)
		{
			this.tasks.remove(findTask);
		}
		else
			System.out.println("Not a valid task name");

	}

	/**
	Method that instigates dialog asking for input to mark a task as completed
	*/
	public void markTasks()
	{
		Scanner scanner = new Scanner(System.in);
		System.out.println("What's the task would you like to mark completed?");
		String taskName = scanner.nextLine();

		Task findTask = this.search(taskName);

		if (findTask != null)
		{
			if (findTask.getCompleted() == false)
				findTask.markCompleted();
			else
				System.out.println("Task already marked completed");
		}
		else
			System.out.println("Silly you. That's not a real task!");

	}

	public Task find(String parentTaskName, Task desTask)
	{
		Task returnTask;

		if (desTask.getName().equals(parentTaskName))
			return desTask;
		else
		{	
			for (int i = 0; i < desTask.getSubTasksList().size(); i++)
			{
				returnTask = find(parentTaskName, desTask.getSubTasksList().get(i));
				if (returnTask != null)
					return returnTask;
			}
		}
		return null;
	}

	public Task search(String parentTaskName)
	{
		Task parentTask = new Task();

		for(int i = 0; i < this.tasks.size(); i++)
		{
			parentTask = this.find(parentTaskName, this.tasks.get(i));

			if (parentTask != null)
				break;
		}

		return parentTask;

	}

}




















