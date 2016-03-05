package edu.ucsb.cs56.projects.utilities.todo;

import edu.ucsb.cs56.projects.utilities.todo.Task;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

/**
   A single list of tasks in a todo list

   @author William Stevenson
   @version todo project for CS56 W16
*/
public class List implements Serializable {

    private String listName;
    private ArrayList<Task> tasks;
    private ArrayList<Task> sortedTasks;
    private JCheckBox check;
    private JButton deleteButton;
    private JButton editButton;
    private JLabel label;
    private JTextField userInput;

    /**
       Constructor for a single list of tasks
       @param listName the name of the new list
    */
    public List(String listName)
    {
	this.listName = listName;
	this.tasks = new ArrayList<Task>();
	this.sortedTasks = new ArrayList<Task>();
    }

    /**
       Add task to the ArrayList
       @param task Task to add
    */
    public void addTask(Task task)
    {
	this.tasks.add(task);
    }

    /**
       Remove task from the ArrayList
       @param task Task to remove
    */
    public void removeTask(Task task)
    {
	this.tasks.remove(task);
    }

    /**
       Getter for the ArrayList of all tasks
       @return an ArrayList of the tasks that the user put in
    */
    public ArrayList<Task> getTasks()
    {
	return this.tasks;
    }

    /**
       Getter for the ArrayList of all tasks, sorted
       @return a sorted ArrayList of the tasks that the user put in
    */
    public ArrayList<Task> getSortedTasks()
    {
	return this.sortedTasks;
    }

    /**
       Returns the name of the list
    */
    public String getListName()
    {
	return this.listName;
    }


    /**
       Overwriting the toString method.
       Prints in the format of:
       "listName"
    */
    public String toString()
    {
	String result = "";
	result += this.getListName();
	return result;
    }


    /**
       Method that instigates dialog asking for input of a task, and due date from the user
       It then creates a new Task, and puts that in the ArrayList
       @param quickInput the computer asks for input from the user to add a task
       @return Task that was just created
    */
    public Task makeTask(String quickInput, String listName, int priorityNumber, java.awt.Color taskColor)
    {
	String taskName = "";
	String date = "";
	String time = "";
	int hour = -1;
	int min = -1;
	int month = -1;
	int day = -1;
	int year = -1;
	
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
			Task newTask = new Task(taskName, listName, year, month, day, hour, min, parentTask,priorityNumber, taskColor);
			parentTask.getSubTasksList().add(parentTask.getSubTasksList().size(), newTask);
			return newTask;
		    }
		else
		    System.out.println("Not a valid parent task!");
	    }
	else
	    {
		Task newTask = new Task(taskName, listName, year, month, day, hour, min, null, priorityNumber, taskColor);
		return newTask;
	    }
	return null;
	
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
       @param parentTaskName the name of the parent task
       @param desTask the task being searched for
       @return The task found
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
       Method that searches all of the tasks asscoiated with a TodoList object. Uses the findInOneComponent method on each level 1 task
       @param parentTaskName the name of the task being searched for
       @return the Task found
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
       @param tasklist list of tasks in todoList
       @param heriarchyNum 
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
       @param tasklist list of tasks in todoList
       @param heriarchyNum
    */
    public void printTasksToFile(ArrayList<Task> tasklist, int heriarchyNum, File f){
	try{
	    FileOutputStream fileOut = new FileOutputStream(f);
	    ObjectOutputStream out = new ObjectOutputStream(fileOut);
	    out.writeObject(this);
	    out.close();
	    fileOut.close();
	}
	catch(IOException ex) {
	    ex.printStackTrace();
	}
    }
    
    /**
       Uncompleted tasks are printed out in list form. I'd always envisioned this as a single list,
       but might add nested support, as it should be an easy fix
       @param tasklist list of tasks in todoList
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
       Prints the tasks that have a due date of the current day, month and year
       @param tasklist list of tasks in todoList
       @param day day of the month
       @param month the month of desired tasks
       @param year the year of desired tasks
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
       @return Todolist read from serialized file
       @param f file to be read from
    */
    public TodoList readFile(File f){
	TodoList newList=null;
	try{
	    FileInputStream fileIn = new FileInputStream(f);
	    ObjectInputStream in = new ObjectInputStream(fileIn);
	    newList = (TodoList) in.readObject();
	}catch(Exception ex) {
	    ex.printStackTrace();
	}
	return newList;
    }
    
    /**
       The idea is that TodoList has a list of all tasks but is only updated when the user
       wants to sort by name or date. The list is first emptied and refilled with this function.
       This method does not sort the list
       @param tasklist the unsorted list
       @param sortedList the list that will be populated with the unsorted list
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
       A new Comparator that allows two Tasks to be compared by priority
    */
    static Comparator<Task> compareByPriority()
    {
	return new Comparator<Task>() {
	    public int compare(Task t1, Task t2)
	    {
		String taskPriority1 = t1.getPriorityString();
		String taskPriority2 = t2.getPriorityString();
		return taskPriority2.compareTo(taskPriority1);
	    }
	};
    }
    
    
    
    /**
       A new Comparator that allows two strings to be compared alphabetically
    */
    static Comparator<Task> compareByName()
    {
	return new Comparator<Task>(){
	    public int compare(Task t1, Task t2)
	    {
		String taskName1 = t1.getName().toUpperCase();
		String taskName2 = t2.getName().toUpperCase();
		return taskName1.compareTo(taskName2);
	    }
	};
    }
    
    /**
       A new Comparator that allows two strings to be compared by whether or not they're completed
    */
    static Comparator<Task> compareByCompleted()
    {
	return new Comparator<Task>(){
	    public int compare(Task t1, Task t2)
	    {   
		if (t1.getCheck().isSelected()==true&&
		    t2.getCheck().isSelected()==false) {
		    return 1;
		}
		else if (t1.getCheck().isSelected()==false&&
			 t2.getCheck().isSelected()==true) {
		    return -1;
		}
		else {
		    String taskName1 = t1.getName().toUpperCase();
		    String taskName2 = t2.getName().toUpperCase();
		    return taskName1.compareTo(taskName2);
		}
	    }
	};
    }
    /**
       A new Comparator that allows two strings to be compared by date
    */
    static Comparator<Task> compareByDate()
    {
	return new Comparator<Task>(){
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


    public void setCheck(JCheckBox check)
    {
	this.check = check;
    }

    public JCheckBox getCheck()
    {
	return this.check;
    }

    public void setDelete(JButton deleteButton)
    {
	this.deleteButton = deleteButton;
    }

    public JButton getDelete()
    {
	return this.deleteButton;
    }


    public void setEdit(JButton editButton)
    {
	this.editButton = editButton;
    }

    public JButton getEdit()
    {
	return this.editButton;
    }

    public void setUserInput(JTextField userInput)
    {
	this.userInput = userInput;
    }

    public JTextField getUserInput()
    {
	return this.userInput;
    }

    public void setLabel()
    {
	String listInfo = this.toString();
	this.label = new JLabel(listInfo);
	this.label.setPreferredSize(new Dimension(360,20));
    }

    public JLabel getLabel()
    {
	this.setLabel();
	return this.label;
    }
}
