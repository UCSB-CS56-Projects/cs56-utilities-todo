package edu.ucsb.cs56.projects.utilties.todo;

import edu.ucsb.cs56.projects.utilties.todo.Task;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.io.*;


public class Todo{

	public void addTask(String name, String dueDate)
	{
		//Will create a new Task and stick it in the Arraylist
	}

	public static void main(String[] args)
	{
		boolean end = true;
		ArrayList<Task> tasks = new ArrayList<Task>();
		int numTasks = 0;

		while(end)
		{
			//Print the tasks
			System.out.println("--------------------");

			Scanner scanner = new Scanner(System.in);
			System.out.println("add/delete/mark");
			String input = scanner.nextLine();

			if (input.equals("add"))
			{
				System.out.println("What's the name of the task?");
				String taskName = scanner.nextLine();
				System.out.println("What's its due date?");
				String date = scanner.nextLine();
				//check and see if date is formmatted correctly
				System.out.println("At what time?");
				String time = scanner.nextLine();

				//TODO: Create a method that takes in the date and time, splits, and turns into date Array
				//TODO: Proposed method begins here
				String dateDelims = "[/]";
				String[] dateTokens = date.split(dateDelims);
				
				String timeDelims = "[ ]";
				String[] timeTokens = time.split(timeDelims);

				int month = Integer.parseInt(dateTokens[0]);
				int day   = Integer.parseInt(dateTokens[1]);
				int year  = Integer.parseInt(dateTokens[2]) + 1900;
				int hour  = Integer.parseInt(timeTokens[0]);
				int min   = Integer.parseInt(timeTokens[1]);

				Task newTask = new Task(taskName, year, month, day, hour, min);

				tasks.add(numTasks, newTask);
				numTasks++;
				
				//Check to see if time is formatted correctly
				//Add task
			}
			else if (input.equals("delete"))
			{

			}
			else if (input.equals("mark"))
			{

			}
			else
				System.out.println("Not a valid input :(");		
			//wait for command
			//add, delete, mark

		}

	}


}