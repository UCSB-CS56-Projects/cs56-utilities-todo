package edu.ucsb.cs56.projects.utilties.todo;

import edu.ucsb.cs56.projects.utilties.todo.Task;
import edu.ucsb.cs56.projects.utilties.todo.Lists;
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
    private javax.swing.Timer timer = new javax.swing.Timer(5000,new refreshListener()); // Timer to refresh task list
    private JFrame frame;
    private TodoList taskList;
    private JPanel taskPanel;
    private JPanel categoryPanel;
    private JPanel mainPanel;
    private JPanel leftPanel;
    private Box taskBox;
    private Box categoryBox;
    private JTextField addField;
    private boolean sorted;
    private boolean hidden;
    private ButtonGroup cbg;
    private JCheckBox one;
    private JCheckBox two;
    private JCheckBox three;
    private SpinnerNumberModel red;
    private SpinnerNumberModel green;
    private SpinnerNumberModel blue;

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

	taskPanel = new JPanel(); // Holds Box of Tasks
	categoryPanel = new JPanel(); // Holds Box of categories

	categoryBox = Box.createVerticalBox();
	categoryBox.setBorder(BorderFactory.createTitledBorder("Lists"));
	categoryBox.setSize(300,600);
	
	taskBox = Box.createVerticalBox(); // Contains mainPanel
	taskBox.setBorder(BorderFactory.createTitledBorder("Tasks"));
	taskBox.setSize(600,600);

	leftPanel = new JPanel(); // Holds all list names
	mainPanel = new JPanel(); // Holds all the printed tasks

	categoryBox.add(leftPanel);
	taskBox.add(mainPanel);

	sorted = false;
	hidden = false;
	
	JPanel midPanel = new JPanel(new GridLayout(2,0));
	JPanel addPanel = new JPanel();

	leftPanel.setPreferredSize(new Dimension(250, 575));
	mainPanel.setPreferredSize(new Dimension(550,575));

	categoryPanel.setPreferredSize(new Dimension(300,700));
	categoryPanel.add(categoryBox);
	
	taskPanel.setPreferredSize(new Dimension(600,700));
	taskPanel.add(taskBox);
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
	    JButton editTemp = new JButton("Edit");

	    buttonTemp.addActionListener(new DeleteListener(taskList.getTasks().get(i)));
	    editTemp.addActionListener(new EditListener(taskList.getTasks().get(i)));
	    

	    taskList.getTasks().get(i).setEdit(editTemp);
	    taskList.getTasks().get(i).setCheck(checkTemp);
	    taskList.getTasks().get(i).setDelete(buttonTemp);
	}
	
	//displays the taskPanel components
        this.displayTasks();

	JButton sortPriorityButton = new JButton("SORT BY PRIORITY");
       	JButton sortNameButton = new JButton("SORT BY NAME");
       	JButton sortCompletionButton = new JButton("SORT BY COMPLETION");
       	JButton sortDateButton = new JButton("SORT BY DATE");
	JButton toggleCompletedButton = new JButton("SHOW/HIDE COMPLETED");
	JButton deleteCompletedButton = new JButton("DELETE COMPLETED");

	sortPriorityButton.addActionListener(new sortPriorityListener());
       	sortNameButton.addActionListener(new sortNameListener());
       	sortCompletionButton.addActionListener(new sortCompletionListener());
       	sortDateButton.addActionListener(new sortDateListener());
	toggleCompletedButton.addActionListener(new toggleCompletedListener());
	deleteCompletedButton.addActionListener(new deleteCompletedListener());

        addField = new JTextField("", 20);
	
	Box cbb = Box.createHorizontalBox();
	one = new JCheckBox("!");
	two = new JCheckBox("!!");
	three = new JCheckBox("!!!");
	cbg = new ButtonGroup();
	cbb.add(one);
	cbb.add(two);
	cbb.add(three);
	cbg.add(one);
	cbg.add(two);
	cbg.add(three);
	cbb.setBorder(BorderFactory.createTitledBorder("Priority"));

	red = new SpinnerNumberModel(0,0,255,1);
	green = new SpinnerNumberModel(0,0,255,1);
	blue = new SpinnerNumberModel(0,0,255,1);
	JSpinner spin1 = new JSpinner(red);
	JSpinner spin2 = new JSpinner(green);
	JSpinner spin3 = new JSpinner(blue);
	Box colorBox = Box.createHorizontalBox();
	colorBox.add(spin1);
	colorBox.add(spin2);
	colorBox.add(spin3);
	colorBox.setBorder(BorderFactory.createTitledBorder("Color (RGB)"));
	

	JLabel formatLabel = new JLabel("(Taskname MM/DD/YY HH:TT)");
	formatLabel.setFont(new Font("Serif", Font.PLAIN, 11));
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
	frame.getContentPane().add(categoryPanel,BorderLayout.WEST);
	frame.getContentPane().add(taskPanel,BorderLayout.CENTER);
	frame.getContentPane().add(addPanel,BorderLayout.SOUTH);
	frame.getContentPane().add(midPanel,BorderLayout.NORTH);

	JPanel sortPanel = new JPanel();
	JPanel comPanel = new JPanel();
	
	sortPanel.add(sortPriorityButton);
        sortPanel.add(sortNameButton);
        sortPanel.add(sortDateButton);
	sortPanel.add(sortCompletionButton);
	comPanel.add(toggleCompletedButton);
        comPanel.add(deleteCompletedButton);

	midPanel.add(sortPanel);
	midPanel.add(comPanel);
	
	addPanel.add(addField);
	addPanel.add(cbb);
	addPanel.add(colorBox);
        addPanel.add(addButton);
	addPanel.add(formatLabel);
	addButton.addActionListener(new AddListener());

	frame.pack();
	frame.setVisible(true);
	
	timer.start();
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

    public class sortPriorityListener implements ActionListener {
	public void actionPerformed(ActionEvent ev) {
	    sorted = true;
	    taskList.getSortedTasks().clear();
	    taskList.updateSortedList(taskList.getTasks(), taskList.getSortedTasks());
	    Collections.sort(taskList.getSortedTasks(), taskList.compareByPriority());
	    mainPanel.removeAll();
	    mainPanel.repaint();
	    if(hidden == true) {
		displayComTasks();
	    } else {
		displayTasks();
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
	    if(hidden == true) {
		displayComTasks();
	    } else {
		displayTasks();
	    }
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
	    if(hidden == true) {
		displayComTasks();
	    } else {
		displayTasks();
	    }
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
	    if(hidden == true) {
		displayComTasks();
	    } else {
		displayTasks();
	    }
	}
    }

    public class refreshListener implements ActionListener {
	public void actionPerformed(ActionEvent ev) {
	    mainPanel.removeAll();
	    mainPanel.repaint();
	    if(hidden == true) {
		displayComTasks();
	    } else {
		displayTasks();
	    }
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
	    if(hidden == true) {
		displayComTasks();
	    } else {
		displayTasks();
	    }
	}
    }
	    
    public class AddListener implements ActionListener {
	public void actionPerformed(ActionEvent ev) {
	    sorted = false;
	    int redInt = (Integer) red.getValue();
	    int greenInt = (Integer) green.getValue();
	    int blueInt = (Integer) blue.getValue();
	    Color c = new Color(redInt, greenInt, blueInt);
	    //taskList.updateSortedList(taskList.getSortedTasks(), taskList.getTasks());
	    if(one.isSelected() == true) {
		taskList.addTask(taskList.makeTask(addField.getText(),1,c));
	    } else if(two.isSelected() == true) {
		taskList.addTask(taskList.makeTask(addField.getText(),2,c));
	    } else if(three.isSelected() == true) {
		taskList.addTask(taskList.makeTask(addField.getText(),3,c));
	    } else {
		taskList.addTask(taskList.makeTask(addField.getText(),0,c));
	    }
	    
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

	    if(hidden == true) {
		displayComTasks();
	    } else {
		displayTasks();
	    }
	    
	    cbg.clearSelection();
	    red.setValue(0);
	    green.setValue(0);
	    blue.setValue(0);
	}
    }

    public class EditListener implements ActionListener{
	private Task myTask;
	public EditListener(Task myTask){
	    this.myTask = myTask;
	    timer.start();
	}
	public void actionPerformed(ActionEvent ev) {
	    mainPanel.removeAll();
	    mainPanel.repaint();
	    displayTasks(myTask);
	    timer.stop();
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
	if(sorted == true) {
	    for(int i=0; i<taskList.getSortedTasks().size(); i++) {
		JButton buttonTemp = new JButton("Delete");
		JButton editTemp = new JButton("Edit");

		buttonTemp.addActionListener(new DeleteListener(taskList.getSortedTasks().get(i)));
		editTemp.addActionListener(new EditListener(taskList.getSortedTasks().get(i)));

		taskList.getSortedTasks().get(i).setDelete(buttonTemp);
		taskList.getSortedTasks().get(i).setEdit(editTemp);
		
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
		JButton editTemp = new JButton("Edit");
		
		buttonTemp.addActionListener(new DeleteListener(taskList.getTasks().get(i)));
		editTemp.addActionListener(new EditListener(taskList.getTasks().get(i)));

		taskList.getTasks().get(i).setDelete(buttonTemp);
		taskList.getTasks().get(i).setEdit(editTemp);
		
		mainPanel.add(taskList.getTasks().get(i).getCheck());
		mainPanel.add(taskList.getTasks().get(i).getLabel());
		mainPanel.add(taskList.getTasks().get(i).getEdit());
		mainPanel.add(taskList.getTasks().get(i).getDelete());
	    }
	mainPanel.validate();
	}
    }

    public void displayComTasks() {
	if(sorted == true) {
	    for(int i = 0; i < taskList.getSortedTasks().size(); i++) {
		if(taskList.getSortedTasks().get(i).getCheck().isSelected() == false) {
		    JButton buttonTemp = new JButton("Delete");
		    JButton editTemp = new JButton("Edit");
		    
		    buttonTemp.addActionListener(new DeleteListener(taskList.getSortedTasks().get(i)));
		    editTemp.addActionListener(new EditListener(taskList.getSortedTasks().get(i)));

		    taskList.getSortedTasks().get(i).setDelete(buttonTemp);
		    taskList.getSortedTasks().get(i).setEdit(editTemp);

		    mainPanel.add(taskList.getSortedTasks().get(i).getCheck());
		    mainPanel.add(taskList.getSortedTasks().get(i).getLabel());
		    mainPanel.add(taskList.getSortedTasks().get(i).getEdit());
		    mainPanel.add(taskList.getSortedTasks().get(i).getDelete());
		}
	    }
	    mainPanel.validate();
	} else {
	    for(int i = 0; i < taskList.getTasks().size(); i++) {
		if(taskList.getTasks().get(i).getCheck().isSelected() == false) {
		    JButton buttonTemp = new JButton("Delete");
		    JButton editTemp = new JButton("Edit");
		    
		    buttonTemp.addActionListener(new DeleteListener(taskList.getTasks().get(i)));
		    editTemp.addActionListener(new EditListener(taskList.getTasks().get(i)));

		    taskList.getTasks().get(i).setDelete(buttonTemp);
		    taskList.getTasks().get(i).setEdit(editTemp);
		    
		    mainPanel.add(taskList.getTasks().get(i).getCheck());
		    mainPanel.add(taskList.getTasks().get(i).getLabel());
		    mainPanel.add(taskList.getTasks().get(i).getEdit());
		    mainPanel.add(taskList.getTasks().get(i).getDelete());
		}
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
		    JTextField input = new JTextField(thisTask.getName(),30);
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
		    JTextField input = new JTextField(thisTask.getName(),30);
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

    public class toggleCompletedListener implements ActionListener {
	public void actionPerformed(ActionEvent ev) {
	    if(hidden == true) {
		mainPanel.removeAll();
		mainPanel.repaint();
		displayTasks();
		hidden = false;
		timer.stop();
	    } else {
		mainPanel.removeAll();
		mainPanel.repaint();
		displayComTasks();
		hidden = true;
		timer.start();
	    }
	}
    }
    
    public class deleteCompletedListener implements ActionListener{
	public void actionPerformed(ActionEvent ev){
	    if(sorted==true){
       		for(int i = 0; i<taskList.getSortedTasks().size();i++){
		    if(taskList.getSortedTasks().get(i).getCheck().isSelected()==true){
			taskList.getTasks().remove(taskList.getSortedTasks().get(i));
			taskList.getSortedTasks().remove(taskList.getSortedTasks().get(i));
			i = 0;
		    }
		}
	    }
	    else{
		for(int i = 0; i< taskList.getTasks().size();i++){
		    if(taskList.getTasks().get(i).getCheck().isSelected()==true){
			taskList.getTasks().remove(taskList.getTasks().get(i));
			i = 0;
		    }
		}
	    }
	    mainPanel.removeAll();
	    mainPanel.repaint();
	    displayTasks();
	}
    }

    public class enterListener implements ActionListener{
	private Task myTask;
	public enterListener(Task myTask){
	    this.myTask = myTask;
	}
	public void actionPerformed(ActionEvent ev){
	    int i;
	    if(sorted==true){
		for(i = 0; i<taskList.getSortedTasks().size(); i++){
		    if(taskList.getSortedTasks().get(i)==this.myTask)
			break;
		}
		if(i<taskList.getSortedTasks().size()){
		    Task newTask = taskList.makeTask(this.myTask.getUserInput().getText(),
						     taskList.getSortedTasks().get(i).getPriority(),
						     taskList.getSortedTasks().get(i).getColor());
		    taskList.getSortedTasks().set(i, newTask);
		    JCheckBox checkTemp = new JCheckBox();
		    JButton buttonTemp = new JButton("Delete");
		    buttonTemp.addActionListener(new DeleteListener(newTask));
		    JButton editTemp = new JButton("Edit");
		    editTemp.addActionListener(new EditListener(newTask));
		    newTask.setEdit(editTemp);
		    newTask.setCheck(checkTemp);
		    newTask.setDelete(buttonTemp);
		    mainPanel.removeAll();
		    mainPanel.repaint();
		    displayTasks();
		}
	    }
	    else{
		for(i = 0; i<taskList.getTasks().size(); i++){
		    if(taskList.getTasks().get(i)==this.myTask)
			break;
		}
		if(i<taskList.getTasks().size()){
		    Task newTask = taskList.makeTask(this.myTask.getUserInput().getText(),
						     taskList.getTasks().get(i).getPriority(),
						     taskList.getTasks().get(i).getColor());
		    taskList.getTasks().set(i, newTask);
		    JCheckBox checkTemp = new JCheckBox();
		    JButton buttonTemp = new JButton("Delete");
		    buttonTemp.addActionListener(new DeleteListener(newTask));
		    JButton editTemp = new JButton("Edit");
		    editTemp.addActionListener(new EditListener(newTask));
		    newTask.setEdit(editTemp);
		    newTask.setCheck(checkTemp);
		    newTask.setDelete(buttonTemp);
		    mainPanel.removeAll();
		    mainPanel.repaint();
		    displayTasks();
		}
	    }
	}
    }
}
	

   

