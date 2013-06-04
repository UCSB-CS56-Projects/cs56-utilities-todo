package edu.ucsb.cs56.projects.utilties.todo;

import edu.ucsb.cs56.projects.utilties.todo.Task;
import edu.ucsb.cs56.projects.utilties.todo.Todo;
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