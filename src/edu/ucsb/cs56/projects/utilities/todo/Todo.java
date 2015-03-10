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
    private TodoList taskList;
    private JPanel mainPanel;
    private JTextField addField;
    private boolean sorted;

    /**
       The main method
    */
    public static void main(String[] args)
    {
      	boolean end = true;
      	TodoList taskList = new TodoList();
       	Scanner scanner = new Scanner(System.in);  
       	Todo todo = new Todo();
	todo.go();
    } //main method

    public void go() {
	//build gui
	frame = new JFrame("Todo List");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	taskList = new TodoList();
	mainPanel = new JPanel();
	sorted = false;
	
	JPanel sortPanel = new JPanel();
	JPanel addPanel = new JPanel();
	mainPanel.setPreferredSize(new Dimension(550, 600));
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
	    File file = new File("savedLists/todo.ser");
	    if (!file.exists() || file.length() == 0) ;
	    else {
		ObjectInputStream iStream = 
		    new ObjectInputStream(new FileInputStream(file));
		taskList = (TodoList) iStream.readObject();
	    }
	}
	catch(IOException e){
		if (e.getMessage().equals(e.getMessage()))
		    ;
		else
		    System.err.println("Caught IOException: " + e.getMessage());
	    }
	catch(ClassNotFoundException e){
		System.err.println("Caught ClassNotFoundException: " + e.getMessage());
	    }

	//adds mainPanel components to their respective ArrayLists
	for (int i=0; i< taskList.getTasks().size(); i++) {

	    //checks for completion and removes "[]" or "[x]"
	    String taskInfo = taskList.getTasks().get(i).toString();
	    if (taskInfo.indexOf("x") == 1) {
		taskInfo = taskInfo.substring(4);
	    }
	    else {
		taskInfo = taskInfo.substring(3);
	    }
	    JCheckBox checkTemp = new JCheckBox();
	    JButton buttonTemp = new JButton("Delete");
	    buttonTemp.addActionListener(new DeleteListener(taskList.getTasks().get(i)));
	    JButton editTemp = new JButton("Edit");
	    editTemp.addActionListener(new EditListener(taskList.getTasks().get(i)));
	    taskList.getTasks().get(i).setEdit(editTemp);
	    taskList.getTasks().get(i).setCheck(checkTemp);
	    taskList.getTasks().get(i).setDelete(buttonTemp);
	}
	
	//displays the mainPanel components
        this.displayTasks();
					    
       	JButton sortNameButton = new JButton("SORT BY NAME");
       	JButton sortCompletionButton = new JButton("SORT BY COMPLETION");
       	JButton sortDateButton = new JButton("SORT BY DATE");
	JButton deleteCompletedButton = new JButton("DELETE COMPLETED");

       	sortNameButton.addActionListener(new sortNameListener());
       	sortCompletionButton.addActionListener(new sortCompletionListener());
       	sortDateButton.addActionListener(new sortDateListener());
	deleteCompletedButton.addActionListener(new deleteCompleted());

        addField = new JTextField("", 20);
	JButton addButton = new JButton("ADD TASK");
	
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
	frame.getContentPane().add(BorderLayout.NORTH, mainPanel);
	frame.getContentPane().add(BorderLayout.SOUTH, addPanel);
	frame.getContentPane().add(BorderLayout.CENTER, sortPanel);
        sortPanel.add(sortNameButton);
        sortPanel.add(sortDateButton);
	sortPanel.add(sortCompletionButton);
        sortPanel.add(deleteCompletedButton);
	addPanel.add(addField);
        addPanel.add(addButton);
	addPanel.add(new JLabel("(Taskname MM/DD/YY HH:TT)"));
	addButton.addActionListener(new AddListener());
	frame.setSize(600,800);
	frame.setVisible(true);	

    }

    //load from file
    public class LoadListener implements ActionListener {
	public void actionPerformed(ActionEvent ev) {
	    try {
		JFileChooser fileOpen = new JFileChooser();
		int retrieval = fileOpen.showOpenDialog(null);
		if (retrieval == JFileChooser.APPROVE_OPTION) {
		    taskList = taskList.readFile(fileOpen.getSelectedFile());
		    mainPanel.removeAll();
		    mainPanel.repaint();
		    displayTasks();  
		}
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
    }

    public class sortNameListener implements ActionListener {
	public void actionPerformed(ActionEvent ev) {
	    sorted = true;
	    taskList.getSortedTasks().clear();
	    taskList.updateSortedList(taskList.getTasks(), taskList.getSortedTasks());
	    Collections.sort(taskList.getSortedTasks(), taskList.compareByName());
	    mainPanel.removeAll();
	    mainPanel.repaint();
	    displayTasks();
	}
    }
    public class sortCompletionListener implements ActionListener {
	public void actionPerformed(ActionEvent ev) {
	    sorted = true;
	    taskList.getSortedTasks().clear();
	    taskList.updateSortedList(taskList.getTasks(), taskList.getSortedTasks());
	    Collections.sort(taskList.getSortedTasks(), taskList.compareByCompleted());
	    mainPanel.removeAll();
	    mainPanel.repaint();
	    displayTasks();
	}

    }
    public class sortDateListener implements ActionListener {
	public void actionPerformed(ActionEvent ev) {
	    sorted = true;
	    taskList.getSortedTasks().clear();
	    taskList.updateSortedList(taskList.getTasks(), taskList.getSortedTasks());
	    Collections.sort(taskList.getSortedTasks(), taskList.compareByDate());
	    mainPanel.removeAll();
	    mainPanel.repaint();
	    displayTasks();
	}
    }

    public class DeleteListener implements ActionListener {
	private Task myTask;
	public DeleteListener(Task myTask){
	    this.myTask = myTask;
	}
	public void actionPerformed(ActionEvent ev) {
	    taskList.getTasks().remove(this.myTask);
	    taskList.getSortedTasks().remove(this.myTask);
	    mainPanel.removeAll();
	    mainPanel.repaint();
	    displayTasks();

	}
    }
	    
    public class AddListener implements ActionListener {
	public void actionPerformed(ActionEvent ev) {
	    sorted = false;
	    taskList.updateSortedList(taskList.getSortedTasks(), taskList.getTasks());
	    taskList.addTask(taskList.makeTask(addField.getText()));
	    //add new checkbox and buttons
	    JCheckBox checkTemp = new JCheckBox();
	    JButton buttonTemp = new JButton("Delete");
	    int i = taskList.getTasks().size()-1;
	    buttonTemp.addActionListener(new DeleteListener(taskList.getTasks().get(i)));
	    JButton editTemp = new JButton("Edit");
	    editTemp.addActionListener(new EditListener(taskList.getTasks().get(i)));
	    taskList.getTasks().get(i).setEdit(editTemp);
	    taskList.getTasks().get(i).setCheck(checkTemp);
	    taskList.getTasks().get(i).setDelete(buttonTemp);
	    addField.setText("");
	    mainPanel.removeAll();
	    mainPanel.repaint();
	    displayTasks();
	}
    }

    public class EditListener implements ActionListener{
	private Task myTask;
	public EditListener(Task myTask){
	    this.myTask = myTask;
	}
	public void actionPerformed(ActionEvent ev) {
	    mainPanel.removeAll();
	    mainPanel.repaint();
	    displayTasks(myTask);
	}
    }

    public class SaveListener implements ActionListener {
	public void actionPerformed(ActionEvent ev) {
	    JFileChooser fileSave = new JFileChooser();
	    File f = new File("/savedLists");
	    fileSave.setCurrentDirectory(f);
	    int retrieval = fileSave.showSaveDialog(null);
	    if (retrieval == JFileChooser.APPROVE_OPTION) {
		taskList.printTasksToFile(taskList.getTasks(), 0, fileSave.getSelectedFile());
	    }	   
	}
    }

    public void displayTasks() {
	if(sorted==true) {
	    for(int i=0; i<taskList.getSortedTasks().size(); i++) {
		JButton buttonTemp = new JButton("Delete");
		buttonTemp.addActionListener(new DeleteListener(taskList.getSortedTasks().get(i)));
		JButton editTemp = new JButton("Edit");
		editTemp.addActionListener(new EditListener(taskList.getSortedTasks().get(i)));
		taskList.getSortedTasks().get(i).setEdit(editTemp);
		taskList.getSortedTasks().get(i).setDelete(buttonTemp);
		mainPanel.add(taskList.getSortedTasks().get(i).getCheck());
		mainPanel.add(taskList.getSortedTasks().get(i).getLabel());
		mainPanel.add(taskList.getSortedTasks().get(i).getEdit());
		mainPanel.add(taskList.getSortedTasks().get(i).getDelete());
	    }
	mainPanel.validate();
	}
	else {
  	    for(int i=0; i<taskList.getTasks().size(); i++) {
		JButton buttonTemp = new JButton("Delete");
		buttonTemp.addActionListener(new DeleteListener(taskList.getTasks().get(i)));
		JButton editTemp = new JButton("Edit");
		editTemp.addActionListener(new EditListener(taskList.getTasks().get(i)));
		taskList.getTasks().get(i).setEdit(editTemp);
		taskList.getTasks().get(i).setDelete(buttonTemp);
		mainPanel.add(taskList.getTasks().get(i).getCheck());
		mainPanel.add(taskList.getTasks().get(i).getLabel());
		mainPanel.add(taskList.getTasks().get(i).getEdit());
		mainPanel.add(taskList.getTasks().get(i).getDelete());
	    }
	mainPanel.validate();
	}
    }
    public void displayTasks(Task myTask) {
	if(sorted) {
	    for(int i=0; i<taskList.getSortedTasks().size(); i++) {
		Task thisTask = taskList.getSortedTasks().get(i);
		if(thisTask==myTask){
		    mainPanel.add(thisTask.getCheck());
		    JTextField input = new JTextField(thisTask.getName(),35);
		    thisTask.setUserInput(input);
		    mainPanel.add(thisTask.getUserInput());
		    JButton enterButton = new JButton("Enter");
		    enterButton.addActionListener(new enterListener(thisTask));
		    mainPanel.add(enterButton);
		    mainPanel.add(thisTask.getDelete());
		}
		else{
		    mainPanel.add(thisTask.getCheck());
		    mainPanel.add(thisTask.getLabel());
		    mainPanel.add(thisTask.getEdit());
		    mainPanel.add(thisTask.getDelete());
		}
	    }
	    mainPanel.validate();
	}
	else {
  	    for(int i=0; i<taskList.getTasks().size(); i++) {
		Task thisTask = taskList.getTasks().get(i);
		if(thisTask==myTask){
		    mainPanel.add(thisTask.getCheck());
		    JTextField input = new JTextField(thisTask.getName(),35);
		    thisTask.setUserInput(input);
		    mainPanel.add(thisTask.getUserInput());
		    JButton enterButton = new JButton("Enter");
		    enterButton.addActionListener(new enterListener(thisTask));
		    mainPanel.add(enterButton);
		    mainPanel.add(thisTask.getDelete());
		}
		else{
		    mainPanel.add(thisTask.getCheck());
		    mainPanel.add(thisTask.getLabel());
		    mainPanel.add(thisTask.getEdit());
		    mainPanel.add(thisTask.getDelete());
		}
	    }
	    mainPanel.validate();
	}
    }
    class deleteCompleted implements ActionListener{
	public void actionPerformed(ActionEvent ev){
	    if(sorted==true){
		for(int i = 0; i<taskList.getSortedTasks().size();i++){
		    if(taskList.getSortedTasks().get(i).getCheck().isSelected()==true){
			taskList.getTasks().remove(taskList.getSortedTasks().get(i));
			taskList.getSortedTasks().remove(taskList.getSortedTasks().get(i));
		    }
		}
	    }
	    else{
		for(int i = 0; i<taskList.getTasks().size();i++){
		    if(taskList.getTasks().get(i).getCheck().isSelected()==true){
			taskList.getTasks().remove(taskList.getTasks().get(i));
		    }
		}
	    }
	    mainPanel.removeAll();
	    mainPanel.repaint();
	    displayTasks();
	}
    }
    class enterListener implements ActionListener{
	private Task myTask;
	public enterListener(Task myTask){
	    this.myTask = myTask;
	}
	public void actionPerformed(ActionEvent ev){
	    int i;
	    for(i = 0; i<taskList.getTasks().size(); i++){
		if(taskList.getTasks().get(i)==this.myTask)
		    break;
	    }
	    taskList.getTasks().remove(myTask);
	    taskList.getTasks().add(i, taskList.makeTask(this.myTask.getUserInput().getText()));
	    taskList.getSortedTasks().remove(myTask);
	    taskList.getSortedTasks().add(i, taskList.makeTask(this.myTask.getUserInput().getText()));
	    JCheckBox checkTemp = new JCheckBox();
	    JButton buttonTemp = new JButton("Delete");
	    buttonTemp.addActionListener(new DeleteListener(taskList.getTasks().get(i)));
	    JButton editTemp = new JButton("Edit");
	    editTemp.addActionListener(new EditListener(taskList.getTasks().get(i)));
	    taskList.getTasks().get(i).setEdit(editTemp);
	    taskList.getTasks().get(i).setCheck(checkTemp);
	    taskList.getTasks().get(i).setDelete(buttonTemp);
	    mainPanel.removeAll();
	    mainPanel.repaint();
	    displayTasks();
	}
    }
}
	

   

