package edu.ucsb.cs56.projects.utilties.todo;

import edu.ucsb.cs56.projects.utilties.todo.Task;
import edu.ucsb.cs56.projects.utilties.todo.Todo;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.io.*;

/**
	Contains the main class. This handles selecting which action to do after tasks are printed, as well as
	instigating the methods contained in TodoList.

	@author Brandon Newman
	@version todo project for CS56 S13
*/
public class Todo implements Serializable {

	/**
	The main method
	*/
	public static void main(String[] args)
	{
		boolean end = true;
		TodoList taskList = new TodoList();

		try
		{
			ObjectInputStream iStream = 
			new ObjectInputStream(
				new FileInputStream("todo.ser"));
			taskList = (TodoList) iStream.readObject();
		}
		catch(IOException e)
		{
			if (e.getMessage().equals(e.getMessage()))
				;
			else
				System.err.println("Caught IOException: " + e.getMessage());
		}
		catch(ClassNotFoundException e)
		{
			System.err.println("Caught ClassNotFoundException: " + e.getMessage());
		}

		while (end)
		{
			System.out.println("");
			System.out.println("--------TODO--------");

			taskList.printTasks(taskList.getTasks(),0);

			System.out.println("--------------------");

			Scanner scanner = new Scanner(System.in);
			System.out.println("add, delete, or mark a task, or exit.");
			String input = scanner.nextLine();

			if (input.equals("add"))
				taskList.addTasks();
			else if (input.equals("delete"))
				taskList.deleteTasks();
			else if (input.equals("mark"))
				taskList.markTasks();
			else if (input.equals("sort by completion"))
			{
				System.out.println("");
				taskList.printByCompletion(taskList.getTasks());
			}
			else if (input.equals("sort by name"))
			{
				taskList.getSortedTasks().clear();
				taskList.updateSortedList(taskList.getTasks(), taskList.getSortedTasks());
				Collections.sort(taskList.getSortedTasks(), taskList.compareByName());

				System.out.println("");
				for (int i = 0; i < taskList.getSortedTasks().size(); i++)
				{
					System.out.println(taskList.getSortedTasks().get(i).toString());
				}

			}
			else if (input.equals("sort by date"))
			{
				taskList.getSortedTasks().clear();
				taskList.updateSortedList(taskList.getTasks(), taskList.getSortedTasks());
				Collections.sort(taskList.getSortedTasks(), taskList.compareByDate());

				System.out.println("");
				for (int i = 0; i < taskList.getSortedTasks().size(); i++)
				{
					System.out.println(taskList.getSortedTasks().get(i).toString());
				}

			}
			else if(input.equals("today"))
			{
				System.out.println("");
				Calendar cal = new GregorianCalendar();
				int day   = cal.get(Calendar.DAY_OF_MONTH);
				int month = cal.get(Calendar.MONTH);
				int year  = cal.get(Calendar.YEAR);
				taskList.printToday(taskList.getTasks(), day, month, year);

			}
			else if (input.equals("exit"))
			{
				System.out.println("Would you like to save?");
				String response = scanner.nextLine();

				if (response.equals("yes") || response.equals("Yes"))
				{
					try
					{
						ObjectOutputStream oStream = new
							ObjectOutputStream(
								new FileOutputStream("todo.ser"));
						oStream.writeObject(taskList);
						oStream.close();
					}
					catch(IOException e)
					{
						System.err.println("Caught IOException: " + e.getMessage());
					}
				}
				System.out.println("Good bye!");
				end = false;
			}
			else
				System.out.println("Not a valid input :(");	
		}

	}

}