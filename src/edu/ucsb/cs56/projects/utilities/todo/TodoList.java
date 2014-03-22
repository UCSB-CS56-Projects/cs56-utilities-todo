package edu.ucsb.cs56.projects.utilties.todo;

import edu.ucsb.cs56.projects.utilties.todo.Task;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.io.*;

/**
	A represention of a list of Tasks. Does this by having an Arraylist as a private variable.

	@author Brandon Newman
	@version todo project for CS56 S13
*/
public class TodoList implements Serializable {

	private ArrayList<Task> tasks = new ArrayList<Task>();
	private ArrayList<Task> sortedTasks = new ArrayList<Task>();

    /**
	Getter for ArrayList of level 1 tasks
	*/
	public ArrayList<Task> getTasks()
	{
		return this.tasks;
	}

	/**
	Getter for the ArrayList of all tasks, sorted
	*/
	public ArrayList<Task> getSortedTasks()
	{
		return this.sortedTasks;
	}

	/**
	Method that instigates dialog asking for input of a task, and due date from the user
	It then creates a new Task, and puts that in the ArrayList
	*/
	public void addTask(String quickInput)
	{
	    /*
		Scanner scanner = new Scanner(System.in);
		System.out.println("Quick add: y or n?");
		String quickAdd= scanner.nextLine();
		String quickInput = "";
	    */
      		String taskName = "";
		String date = "";
		String time = "";
		int hour = -1;
		int min = -1;
		int month = -1;
		int day = -1;
		int year = -1;
       	/*
      		if (quickAdd.equals("y")){
		    System.out.println("Enter in following format: Taskname MM/DD/YY HH:mm");
		    quickInput = scanner.nextLine();
	    */
		    String[] taskParts = quickInput.split(" ");

		    boolean isTaskName = true;
		    int i = 0;
		    while (isTaskName) {
			if (taskParts.length == 0) {
			    isTaskName = false;
			}
			if (taskParts.length - i == 1) {
			    isTaskName = false;
			}
			if (taskParts[i].contains("/") || taskParts[i].contains(":")) {
			    isTaskName = false;
			    break;
			}
			taskName = taskName + taskParts[i] + " ";
			i++;
		    }
		    taskName = taskName.substring(0,taskName.length()-1);

		    if (quickInput.contains("/") && quickInput.contains(":")) {
			date = taskParts[i];
			time = taskParts[i+1];
		    }
		    else if (quickInput.contains("/")) {
			date = taskParts[i];
		    }
		    else if (quickInput.contains(":")) {
			time = taskParts[i];
		    }
	     /*
		    
      		}
    		else{
		    System.out.println("What's the name of the task?");
		    taskName = scanner.nextLine();
		    System.out.println("What's its due date? (MM/DD/YY)");
		    date = scanner.nextLine();
		    //TODO: check and see if date is formmatted correctly
		    System.out.println("At what time? (HH:MM)");
		    time = scanner.nextLine();
		}
	     */
		
		
		//PARING AND ASSIGNING NAME AND DATE VALUES
		//BEGINS
		String nameDelims = "[/]";
		String[] nameTokens = taskName.split(nameDelims);

		String dateDelims = "[/]";
		String[] dateTokens = date.split(dateDelims);
		
		String timeDelims = "[:]";
		String[] timeTokens = time.split(timeDelims);
		if (time.equals("")){
		    hour = -1;
		    min = -1;
		}
		else{
		    hour  = Integer.parseInt(timeTokens[0]);
		    min   = Integer.parseInt(timeTokens[1]);
		}
		if (date.equals("")){
		    month = -1;
		    day = -1;
		    year = -1;
		}
		else {
		    month = Integer.parseInt(dateTokens[0]) -1;                                                                                                        
                    day   = Integer.parseInt(dateTokens[1]);                                                                                                           
                    year  = Integer.parseInt(dateTokens[2]) + 2000;      
		}
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
	public void deleteTask(int index)
	{
		// Scanner scanner = new Scanner(System.in);
		// System.out.println("What's the task would you like to delete?");
		// String taskName = scanner.nextLine();

	        // Task findTask = this.search(taskName);

		// if (findTask != null && findTask.getParentTask() != null)
		// {
		// 	Task parentTask = findTask.getParentTask();
		// 	parentTask.getSubTasksList().remove(findTask);
		// }
		// else if (findTask != null)
		// {
		// 	this.tasks.remove(findTask);
		// }
		// else
		// 	System.out.println("Not a valid task name");
	    this.tasks.remove(index);

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

	/**
	Method that searches one item in the ArrayList, and all associted subtasks
	*/
	public Task findInOneComponent(String parentTaskName, Task desTask)
	{
		Task returnTask;

		if (desTask.getName().equals(parentTaskName))
			return desTask;
		else
		{	
			for (int i = 0; i < desTask.getSubTasksList().size(); i++)
			{
				returnTask = findInOneComponent(parentTaskName, desTask.getSubTasksList().get(i));
				if (returnTask != null)
					return returnTask;
			}
		}
		return null;
	}
	/**
	Method that searches all of the tasks asscoiated with a TodoList object. Uses the findInOneComponent method on each level 1 taske

	@see findInOneComponent
	*/
	public Task search(String parentTaskName)
	{
		Task parentTask = new Task();

		for(int i = 0; i < this.tasks.size(); i++)
		{
			parentTask = this.findInOneComponent(parentTaskName, this.tasks.get(i));

			if (parentTask != null)
				break;
		}

		return parentTask;
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
       Method that prints the list of tasks onto a text file
    */
    public void printTasksToFile(ArrayList<Task> tasklist, int hierarchyNum, File f)
        {
	    
	    try{
		FileWriter writer= new FileWriter(f);
		for (Task t: tasklist){
		    writer.write(t.toString() + "\n");
		}
		writer.close();
	    } catch(IOException ex) {
		ex.printStackTrace();
	    }
	}

	/**
	Uncompleted tasks are printed out in list form. I'd always envisioned this as a single list,
	but might add nested support, as it should be an easy fix
	*/
	public void printByCompletion(ArrayList<Task> tasklist)
	{
		for (int i = 0; i < tasklist.size() ; i++)
		{
			if (tasklist.get(i).getCompleted() == false)
			{
				String returnString = tasklist.get(i).toString();
				System.out.println(returnString);
			}

			if (tasklist.get(i).getSubTasksList() != null)
				printByCompletion(tasklist.get(i).getSubTasksList());
		}
	}

	/**
	Prints the tasks that have a due date of the current day, month and year.
	*/
	public void printToday(ArrayList<Task> tasklist, int day, int month, int year)
	{
		for (int i = 0; i < tasklist.size() ; i++)
		{
			if (tasklist.get(i).getCalendarForm().get(Calendar.DAY_OF_MONTH) == day   && 
				tasklist.get(i).getCalendarForm().get(Calendar.MONTH) 		 == month &&
				tasklist.get(i).getCalendarForm().get(Calendar.YEAR)		 == year    )
			{
				String returnString = tasklist.get(i).toString();
				System.out.println(returnString);
			}

			if (tasklist.get(i).getSubTasksList() != null)
				printToday(tasklist.get(i).getSubTasksList(), day, month, year);
		}
	}
    
    /**
    Reads a todo list from a text file.
    */
    public ArrayList<Task> readFile(File f){
	try{

	    FileReader fileReader = new FileReader(f);
	    BufferedReader reader = new BufferedReader(fileReader);
	    String line= null;
	    Task parentTask = new Task();
	    while ((line=reader.readLine()) !=null) {
		String[] bracketSplit = line.split("]");
		String complete = bracketSplit[0];
      
		String[] spaceSplit = bracketSplit[1].split(" ");
		String name = spaceSplit[1];
		String fullDate;
		String time;

		String[] slashSplit;
		String[] colonSplit;

		int month = -1;
		int day = -1;
		int year = -1;

		int hour = -1;
		int min = -1;
		
		if (spaceSplit.length > 2) {

		    fullDate = spaceSplit[2];
		    time = spaceSplit[3];

		    slashSplit = fullDate.split("/");
		    colonSplit = time.split(":");

		    month = Integer.parseInt(slashSplit[0]);
		    day   = Integer.parseInt(slashSplit[1]);
		    year  = Integer.parseInt(slashSplit[2]);

		    hour  = Integer.parseInt(colonSplit[0]);
		    min   = Integer.parseInt(colonSplit[1]);
		}
		
		Task task = new Task(name, year, month, day, hour, min, parentTask);

		if (complete.contains("x")){task.markCompleted();}
		tasks.add(task);
		reader.close();
	    }
	}catch(Exception ex) {
	    ex.printStackTrace();
	}

	return tasks;
    }
		
	/**
	The idea is that TodoList has a list of all tasks but is only updated when the user
	wants to sort by name or date. The list is first emptied and refilled with this function.
	*/
	public void updateSortedList(ArrayList<Task> tasklist, ArrayList<Task> sortedList)
	{
		for (int i = 0; i <tasklist.size(); i++)
		{
			sortedList.add(tasklist.get(i));

			if (tasklist.get(i).getSubTasksList() != null)
				updateSortedList(tasklist.get(i).getSubTasksList(), sortedList);
		}
	}

	/**
	A new Comparator that allows two strings to be compared alphabetically
	*/
	static Comparator<Task> compareByName()
	{
		return new Comparator<Task>()
		{

			public int compare(Task t1, Task t2)
			{
				String taskName1 = t1.getName().toUpperCase();
				String taskName2 = t2.getName().toUpperCase();

				return taskName1.compareTo(taskName2);
			}

		};
	}

    /**
	A new Comparator that allows two strings to be compared by date
	*/
	static Comparator<Task> compareByDate()
	{
		return new Comparator<Task>()
		{

			public int compare(Task t1, Task t2)
			{   
			    if (t1.getDueDate().equals("")) {
				return 1;
			    }
			    else if (t2.getDueDate().equals("")) {
				return -1;
			    }
			    else {
				Calendar date1 = t1.getCalendarForm();
				Calendar date2 = t2.getCalendarForm();

				return date1.compareTo(date2);
			    }
			}

		};
	}


}




















