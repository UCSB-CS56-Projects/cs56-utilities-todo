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
	private int numTasks = 0;

    /**
	Getter for the number of task in a list
	*/
	public int getNumTasks()
	{
		return this.numTasks;
	}

	/**
	Method that prints out a few lines of code to make a "pretty" interface on the command line
	*/
	public void printTasks()
	{
		System.out.println("");
		System.out.println("--------TODO--------");
		for (int i = 0; i < this.numTasks; i++)
			System.out.println(this.tasks.get(i).toString());

		//Print the tasks
		System.out.println("--------------------");
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

		//TODO: Create a method that takes in the date and time, splits, and turns into date Array
		//TODO: Proposed method begins here
		String dateDelims = "[/]";
		String[] dateTokens = date.split(dateDelims);
		
		String timeDelims = "[ ]";
		String[] timeTokens = time.split(timeDelims);

		int month = Integer.parseInt(dateTokens[0]) -1;
		int day   = Integer.parseInt(dateTokens[1]);
		int year  = Integer.parseInt(dateTokens[2]) + 2000;
		int hour  = Integer.parseInt(timeTokens[0]);
		int min   = Integer.parseInt(timeTokens[1]);
		//TODO: Proposed method ends here

		Task newTask = new Task(taskName, year, month, day, hour, min);

		this.tasks.add(this.numTasks, newTask);
		this.numTasks++;

	}
	/**
	Method that instigates dialog asking for input to delete a task
	*/
	public void deleteTasks()
	{
		Scanner scanner = new Scanner(System.in);
		System.out.println("What's the task would you like to delete?");
		String taskName = scanner.nextLine();

		int deleteIndex = -1;

		for (int i = 0; i < this.numTasks; i++)
		{
			if (this.tasks.get(i).getName().equals(taskName))
			{
				deleteIndex = i;
				break;
			}
		}

		if (deleteIndex != -1)
		{
			this.tasks.remove(deleteIndex);
			this.numTasks--;
		}
		else
			System.out.println("Aww shucks. Not a valid task name.");
	}

	/**
	Method that instigates dialog asking for input to mark a task as completed
	*/
	public void markTasks()
	{
		Scanner scanner = new Scanner(System.in);
		System.out.println("What's the task would you like to mark completed?");
		String taskName = scanner.nextLine();

		int markIndex = -1;

		for (int i = 0; i < this.numTasks; i++)
		{
			if (this.tasks.get(i).getName().equals(taskName))
			{
				markIndex = i;
				break;
			}
		}

		if (markIndex != -1)
		{
			if (this.tasks.get(markIndex).getCompleted() == false)
				this.tasks.get(markIndex).markCompleted();
			else if (this.tasks.get(markIndex).getCompleted() == true)
				System.out.println("Task already marked completed");
			else
				System.out.println("Silly you. That's not a real task!");
		}
	}

}




















