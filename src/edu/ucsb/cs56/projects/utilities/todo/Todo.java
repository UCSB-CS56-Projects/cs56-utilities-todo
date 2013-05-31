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

		while (end)
		{
			taskList.printTasks();

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
				;//TODO
			else
				System.out.println("Not a valid input :(");	

		}

	}

}