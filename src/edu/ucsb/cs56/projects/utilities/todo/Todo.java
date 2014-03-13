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
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;


/**
   Contains the main class. This handles selecting which action to do after tasks are printed, as well as
   instigating the methods contained in TodoList.

   @author Brandon Newman
   @version todo project for CS56 S13
*/
public class Todo implements Serializable {

    private JFrame frame;
    private TodoList taskList = new TodoList();


    /**
       The main method
    */
    public static void main(String[] args)
    {
	Todo todo = new Todo();
	todo.go();
	     
	/*	boolean end = true;
		TodoList taskList = new TodoList();
		Scanner scanner = new Scanner(System.in);  
		System.out.println("Would you like to load a todo list from a text file? (yes or no)");
		String yesOrNo = scanner.nextLine();
		if (yesOrNo.equals("yes")){
		System.out.println("Filename (from savedLists folder):");
		String inputFile = "savedLists/"+ scanner.nextLine();
		try{
		//System.out.println("Filename (from savedLists folder):");
		//String inputFile = "savedLists/"+ scanner.nextLine();
		taskList.readFile(new File(inputFile));
		} 
		//this catch not working// 
		catch (Exception ex)
		{
		System.err.println("Caught FileNotFoundException:" + ex.getMessage());
		}
		}
		else{
		try
		{
		ObjectInputStream iStream = 
		new ObjectInputStream(
				new FileInputStream("savedLists/todo.ser"));
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
		}

		boolean printSorted = false;
		boolean byCompletion = false;
		while (end)
		{
			System.out.println("");
			System.out.println("--------TODO--------");
			
			if(printSorted) {
			if (byCompletion) {
			taskList.printByCompletion(taskList.getTasks());
			}
			else {
			for (int i = 0; i < taskList.getSortedTasks().size(); i++) {
			System.out.println(taskList.getSortedTasks().get(i).toString());
			}
			}
			}
			else {
			taskList.printTasks(taskList.getTasks(),0);
			}

			System.out.println("--------------------");

		        printSorted = false;
			byCompletion = false;
			System.out.println("add, delete, or mark a task, sort, or exit.");
			String input = scanner.nextLine();

			if (input.equals("add"))
			taskList.addTasks();
			else if (input.equals("delete"))
			taskList.deleteTasks();
			else if (input.equals("mark"))
			taskList.markTasks();
			else if (input.equals("sort")) {
			printSorted = true;
			System.out.println("Sort by name, date, or completion?");
			input = scanner.nextLine();
			if (input.equals("completion"))
			{
			byCompletion = true;
			}
			else if (input.equals("name"))
			{
			taskList.getSortedTasks().clear();
			taskList.updateSortedList(taskList.getTasks(), taskList.getSortedTasks());
			Collections.sort(taskList.getSortedTasks(), taskList.compareByName());
					
			}
			else if (input.equals("date"))
			{
			taskList.getSortedTasks().clear();
			taskList.updateSortedList(taskList.getTasks(), taskList.getSortedTasks());
			Collections.sort(taskList.getSortedTasks(), taskList.compareByDate());

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
			File f;
			if (response.equals("yes") || response.equals("Yes"))
			{
			try
			{
			ObjectOutputStream oStream = new
			ObjectOutputStream(
			new FileOutputStream("savedLists/todo.ser"));
			oStream.writeObject(taskList);
			oStream.close();
			}
			catch(IOException e)
			{
			System.err.println("Caught IOException: " + e.getMessage());
			}
			System.out.println("File name:");
			String filename= "savedLists/" + scanner.nextLine();
			f = new File(filename); 
			taskList.printTasksToFile(taskList.getTasks(),0,f);	
			}
			System.out.println("Good bye!");
			end = false;
			}
			else
			System.out.println("Not a valid input :(");	
			}*/

    }

    public void go() {

	//build gui

	frame = new JFrame("Todo List");
	JPanel mainPanel = new JPanel();
	
	//exit
	frame.addWindowListener(new WindowAdapter()
	    {
		public void windowClosing(WindowEvent e){
		    try {
			ObjectOutputStream oStream = new
			    ObjectOutputStream(new FileOutputStream("savedLists/todo.ser"));
			oStream.writeObject(taskList);
			oStream.close();
		    }
		    catch(IOException ex)
			{
			    System.err.println("Caught IOException: " + ex.getMessage());
			}			     
		    e.getWindow().dispose();
		}
	    });
	


		
	//default operation is for serialized list to be loaded
	try{
	    ObjectInputStream iStream = 
		new ObjectInputStream(new FileInputStream("savedLists/todo.ser"));
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
					    
		
	JMenuBar menuBar = new JMenuBar();
	JMenu fileMenu = new JMenu("File");
	JMenuItem loadMenuItem = new JMenuItem("Load");
	JMenuItem saveMenuItem = new JMenuItem("Save");
	loadMenuItem.addActionListener(new LoadListener());
	saveMenuItem.addActionListener(new SaveListener());
	fileMenu.add(loadMenuItem);
	fileMenu.add(saveMenuItem);
	menuBar.add(fileMenu);
	frame.setJMenuBar(menuBar);
	frame.getContentPane().add(BorderLayout.CENTER,mainPanel);
	frame.setSize(1580,1000);
	frame.setVisible(true);
	System.out.println("text");	

    }

    //load from file
    public class LoadListener implements ActionListener {
	public void actionPerformed(ActionEvent ev) {
	    JFileChooser fileOpen = new JFileChooser();
	    fileOpen.showOpenDialog(frame);
	    taskList.readFile(fileOpen.getSelectedFile());
		    
	}
    }


    // public class AddListener implements ActionListener {}

    // public class SortListener implements ActionListener {}

    // public class DeleteListener implements ActionListener {}
	    
    // public class QuickaddListener implements ActionListener {}

    public class SaveListener implements ActionListener {
	public void actionPerformed(ActionEvent ev) {
	    JFileChooser fileSave = new JFileChooser();
	    File f = new File("/savedLists");
	    fileSave.setCurrentDirectory(f);
	    int retrieval = fileSave.showSaveDialog(null);
	    if (retrieval == JFileChooser.APPROVE_OPTION) {
		taskList.printTasksToFile(taskList.getTasks(),0,f);
	    }	   
	}
    }
    

}
	

   

