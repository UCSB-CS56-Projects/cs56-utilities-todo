package edu.ucsb.cs56.projects.utilties.todo;

import edu.ucsb.cs56.projects.utilties.todo.Task;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.io.*;

public class Todo implements Serializable {

	public static void main(String[] args)
	{
		boolean end = true;
		ArrayList<Task> tasks = new ArrayList<Task>();
		int numTasks = 0;

		while(end)
		{
			System.out.println("");
			System.out.println("--------TODO--------");
			for (int i = 0; i < numTasks; i++)
				System.out.println(tasks.get(i).toString());

			//Print the tasks
			System.out.println("--------------------");

			Scanner scanner = new Scanner(System.in);
			System.out.println("add, delete, or mark a task, or exit.");
			String input = scanner.nextLine();

			if (input.equals("add"))
			{

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

				tasks.add(numTasks, newTask);
				numTasks++;
				
			}
			else if (input.equals("delete"))
			{
				System.out.println("What's the task would you like to delete completed?");
				String taskName = scanner.nextLine();

				int deleteIndex = -1;

				for (int i = 0; i < numTasks; i++)
				{
					if (tasks.get(i).getName().equals(taskName))
					{
						deleteIndex = i;
						break;
					}
				}

				if (deleteIndex != -1)
				{
					tasks.remove(deleteIndex);
					numTasks--;
				}
				else
					System.out.println("Aww shucks. Not a valid task name.");

			}
			else if (input.equals("mark"))
			{
				System.out.println("What's the task would you like to mark completed?");
				String taskName = scanner.nextLine();

				int markIndex = -1;

				for (int i = 0; i < numTasks; i++)
				{
					if (tasks.get(i).getName().equals(taskName))
					{
						markIndex = i;
						break;
					}
				}

				if (markIndex != -1)
				{
					if (tasks.get(markIndex).getCompleted() == false)
						tasks.get(markIndex).markCompleted();
					else
						System.out.println("Task already marked completed");
				}
			}
			else if (input.equals("exit"))
			{
				System.out.println("Would you like to save?");
				String exitInput = scanner.nextLine();
			}
			else
				System.out.println("Not a valid input :(");		

		}

	}


}