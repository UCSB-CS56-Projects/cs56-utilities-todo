package edu.ucsb.cs56.projects.utilities.todo;

import edu.ucsb.cs56.projects.utilities.todo.Task;
import edu.ucsb.cs56.projects.utilities.todo.TodoList;
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
    private JTextField addListField;
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
	categoryBox.setSize(280,550);
	
	taskBox = Box.createVerticalBox(); // Contains mainPanel
	taskBox.setBorder(BorderFactory.createTitledBorder("Tasks"));
	taskBox.setSize(600,550);

	leftPanel = new JPanel(); // Holds all list names
	mainPanel = new JPanel(); // Holds all the printed tasks

	categoryBox.add(leftPanel);
	taskBox.add(mainPanel);

	sorted = false;
	hidden = false;
	
	JPanel midPanel = new JPanel(new GridLayout(2,0));
	JPanel addPanel = new JPanel(new GridLayout(2,0));

	leftPanel.setPreferredSize(new Dimension(230, 525));
	mainPanel.setPreferredSize(new Dimension(550,525));

	categoryPanel.setPreferredSize(new Dimension(280,700));
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

	// Add code for List compoenents (need to be added before tasks)'
	if(!taskList.getLists().isEmpty()) {
	    for(int i=0; i < taskList.getLists().size(); i++) {
		String listInfo = taskList.getLists().get(i).toString();
		
		JRadioButton listCheckTemp = new JRadioButton();
		JButton listDeleteTemp = new JButton("X");
		JButton listEditTemp = new JButton("Edit");
		
		listCheckTemp.addActionListener(new radioButtonListener(taskList.getLists().get(i)));
		listDeleteTemp.addActionListener(new DeleteListListener(taskList.getLists().get(i)));
		listEditTemp.addActionListener(new EditListListener(taskList.getLists().get(i)));
		
		taskList.getLists().get(i).setEdit(listEditTemp);
		taskList.getLists().get(i).setCheck(listCheckTemp);
		taskList.getLists().get(i).setDelete(listDeleteTemp);
		
		taskList.getListGroup().add(taskList.getLists().get(i).getCheck());
	    }
	    
	    this.displayLists();
	    
	    
	    //adds mainPanel components to their respective ArrayLists
	    for (int i=0; i < taskList.getCurrentList().getTasks().size(); i++) {
		
		//checks for completion and removes "[]" or "[x]"
		String taskInfo = taskList.getCurrentList().getTasks().get(i).toString();
		if (taskInfo.indexOf("x") == 1) {
		    taskInfo = taskInfo.substring(4);
		}
		else {
		    taskInfo = taskInfo.substring(3);
		}
		
		JCheckBox checkTemp = new JCheckBox();
		JButton buttonTemp = new JButton("Delete");
		JButton editTemp = new JButton("Edit");
		
		buttonTemp.addActionListener(new DeleteListener(taskList.getCurrentList().getTasks().get(i)));
		editTemp.addActionListener(new EditListener(taskList.getCurrentList().getTasks().get(i)));
		
		
		taskList.getCurrentList().getTasks().get(i).setEdit(editTemp);
		taskList.getCurrentList().getTasks().get(i).setCheck(checkTemp);
		taskList.getCurrentList().getTasks().get(i).setDelete(buttonTemp);
	    }
	
	//displays the taskPanel components
	    this.displayTasks();
	}

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
       
	addListField = new JTextField("",20);
	addField = new JTextField("",20);
	
	JLabel formatListLabel = new JLabel("(Taskname)");
	formatListLabel.setFont(new Font("Serif", Font.PLAIN, 11));

	JLabel formatLabel = new JLabel("(Taskname MM/DD/YY HH:TT)");
	formatLabel.setFont(new Font("Serif", Font.PLAIN, 11));

	JButton addListButton = new JButton("ADD LIST");
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

	JPanel addListPanel = new JPanel();
	JPanel addTaskPanel = new JPanel();
	
	sortPanel.add(sortPriorityButton);
        sortPanel.add(sortNameButton);
        sortPanel.add(sortDateButton);
	sortPanel.add(sortCompletionButton);
	comPanel.add(toggleCompletedButton);
        comPanel.add(deleteCompletedButton);

	midPanel.add(sortPanel);
	midPanel.add(comPanel);

	addListPanel.add(addListField);
	addListPanel.add(addListButton);
	addListPanel.add(formatListLabel);
	addListButton.addActionListener(new AddListListener());
	addTaskPanel.add(addField);
	addTaskPanel.add(cbb);
	addTaskPanel.add(colorBox);
        addTaskPanel.add(addButton);
	addTaskPanel.add(formatLabel);
	addButton.addActionListener(new AddListener());

	addPanel.add(addListPanel);
	addPanel.add(addTaskPanel);
	
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
		    taskList = taskList.getCurrentList().readFile(fileOpen.getSelectedFile());
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
	    if(!taskList.getLists().isEmpty()) {
		sorted = true;
		taskList.getCurrentList().getSortedTasks().clear();
		taskList.getCurrentList().updateSortedList(taskList.getCurrentList().getTasks(), taskList.getCurrentList().getSortedTasks());
		Collections.sort(taskList.getCurrentList().getSortedTasks(), taskList.getCurrentList().compareByPriority());
		mainPanel.removeAll();
		mainPanel.repaint();
		if(hidden == true) {
		    displayComTasks();
		} else {
		    displayTasks();
		}
	    }
	}
    }
	    

    public class sortNameListener implements ActionListener {
	public void actionPerformed(ActionEvent ev) {
	    if(!taskList.getLists().isEmpty()) {
		sorted = true;
		taskList.getCurrentList().getSortedTasks().clear();
		taskList.getCurrentList().updateSortedList(taskList.getCurrentList().getTasks(), taskList.getCurrentList().getSortedTasks());
		Collections.sort(taskList.getCurrentList().getSortedTasks(), taskList.getCurrentList().compareByName());
		mainPanel.removeAll();
		mainPanel.repaint();
		if(hidden == true) {
		    displayComTasks();
		} else {
		    displayTasks();
		}
	    }
	}
    }
    
    public class sortCompletionListener implements ActionListener {
	public void actionPerformed(ActionEvent ev) {
	    if(!taskList.getLists().isEmpty()) {
		sorted = true;
		taskList.getCurrentList().getSortedTasks().clear();
		taskList.getCurrentList().updateSortedList(taskList.getCurrentList().getTasks(), taskList.getCurrentList().getSortedTasks());
		Collections.sort(taskList.getCurrentList().getSortedTasks(), taskList.getCurrentList().compareByCompleted());
		
		mainPanel.removeAll();
		mainPanel.repaint();
		if(hidden == true) {
		    displayComTasks();
		} else {
		    displayTasks();
		}
	    }
	}
    }
    
    public class sortDateListener implements ActionListener {
	public void actionPerformed(ActionEvent ev) {
	    if(!taskList.getLists().isEmpty()) {
		sorted = true;
		taskList.getCurrentList().getSortedTasks().clear();
		taskList.getCurrentList().updateSortedList(taskList.getCurrentList().getTasks(), taskList.getCurrentList().getSortedTasks());
		Collections.sort(taskList.getCurrentList().getSortedTasks(), taskList.getCurrentList().compareByDate());
		mainPanel.removeAll();
		mainPanel.repaint();
		if(hidden == true) {
		    displayComTasks();
		} else {
		    displayTasks();
		}
	    }
	}
    }

    public class refreshListener implements ActionListener {
	public void actionPerformed(ActionEvent ev) {
	    mainPanel.removeAll();
	    mainPanel.repaint();
	    if(!taskList.getLists().isEmpty()) {
		if(hidden == true) {
		    displayComTasks();
		} else {
		    displayTasks();
		}
	    }
	}
    }

    public class DeleteListListener implements ActionListener {
	private List myList;
	public DeleteListListener(List myList) {
	    this.myList = myList;
	}
	public void actionPerformed(ActionEvent ev) {
	    // Delete all the tasks in that List
	    // Then remove the list from taskList
	    boolean current = false;
	    if(taskList.getCurrentList() == this.myList) {
		current = true;
	    }

	    // Find where the list to be deleted is in the ArrayList of Lists
	    // Then go through the tasks in that List and delete all the tasks
	    int j = taskList.getLists().indexOf(this.myList);
	    for(int i = 0; i < taskList.getLists().get(j).getTasks().size(); i++) {
		taskList.getLists().get(j).getTasks().remove(taskList.getLists().get(j).getTasks().get(i));
		//taskList.getLists().get(j).getSortedTasks().remove(taskList.getLists().get(j).getSortedTasks().get(i));
	    }

	    taskList.getLists().remove(this.myList);

	    // If list being deleted was the currentList and new size isn't 0
	    // Make the first item in the list the currentList
	    if(!taskList.getLists().isEmpty()) {
		if(current) {
		    taskList.setCurrentList(taskList.getLists().get(0));
		}
	    } else {
		taskList.setCurrentList(null);
	    }
	    
	    leftPanel.removeAll();
	    leftPanel.repaint();
	    displayLists();
	    mainPanel.removeAll();
	    mainPanel.repaint();

	    // If new size is 0, don't call displayTasks(), because currentList is null
	    if(!taskList.getLists().isEmpty()) {
		if(hidden == true) {
		    displayComTasks();
		} else {
		    displayTasks();
		}
	    }
	}
    }
	    
    
    public class DeleteListener implements ActionListener {
	private Task myTask;
	public DeleteListener(Task myTask){
	    this.myTask = myTask;
	}
	public void actionPerformed(ActionEvent ev) {
	    taskList.getCurrentList().getTasks().remove(this.myTask);
	    taskList.getCurrentList().getSortedTasks().remove(this.myTask);
	    mainPanel.removeAll();
	    mainPanel.repaint();
	    if(hidden == true) {
		displayComTasks();
	    } else {
		displayTasks();
	    }
	}
    }

    public class AddListListener implements ActionListener {
	public void actionPerformed(ActionEvent ev) {

	    taskList.addList(taskList.makeList(addListField.getText(),
							  new ArrayList<Task>(),
							  new ArrayList<Task>()));
	    JRadioButton listCheckTemp = new JRadioButton();
	    JButton listDeleteTemp = new JButton("X");
	    JButton listEditTemp = new JButton("Edit");
	    int i = taskList.getLists().size() - 1;
	    
	    listCheckTemp.addActionListener(new radioButtonListener(taskList.getLists().get(i)));
	    listDeleteTemp.addActionListener(new DeleteListListener(taskList.getLists().get(i)));
	    listEditTemp.addActionListener(new EditListListener(taskList.getLists().get(i)));

	    taskList.getLists().get(i).setEdit(listEditTemp);
	    taskList.getLists().get(i).setCheck(listCheckTemp);
	    taskList.getLists().get(i).setDelete(listDeleteTemp);
	    taskList.getListGroup().add(taskList.getLists().get(i).getCheck());

	    // If newly added List is only List in TodoList, make it current List
	    if(taskList.getLists().size() == 1) {
		taskList.setCurrentList(taskList.getLists().get(0));
	    }
	    
	    addListField.setText("");
	    leftPanel.removeAll();
	    leftPanel.repaint();
	    displayLists();
	    taskList.printListNames();
	    System.out.println(taskList.getCurrentList().getListName());
	}
    }
	    
	    
    public class AddListener implements ActionListener {
	public void actionPerformed(ActionEvent ev) {
	    // There must be a list in order to make a task
	    if(!taskList.getLists().isEmpty()) {
		sorted = false;
		int redInt = (Integer) red.getValue();
		int greenInt = (Integer) green.getValue();
		int blueInt = (Integer) blue.getValue();
		Color c = new Color(redInt, greenInt, blueInt);
		//taskList.updateSortedList(taskList.getSortedTasks(), taskList.getTasks());
		if(one.isSelected() == true) {
		    taskList.getCurrentList().addTask(taskList.getCurrentList().makeTask(addField.getText(),1,c));
		} else if(two.isSelected() == true) {
		    taskList.getCurrentList().addTask(taskList.getCurrentList().makeTask(addField.getText(),2,c));
		} else if(three.isSelected() == true) {
		    taskList.getCurrentList().addTask(taskList.getCurrentList().makeTask(addField.getText(),3,c));
		} else {
		    taskList.getCurrentList().addTask(taskList.getCurrentList().makeTask(addField.getText(),0,c));
		}
		
		//add new checkbox and buttons
		JCheckBox checkTemp = new JCheckBox();
		JButton buttonTemp = new JButton("Delete");
		int i = taskList.getCurrentList().getTasks().size()-1;
		buttonTemp.addActionListener(new DeleteListener(taskList.getCurrentList().getTasks().get(i)));
		JButton editTemp = new JButton("Edit");
		editTemp.addActionListener(new EditListener(taskList.getCurrentList().getTasks().get(i)));
		taskList.getCurrentList().getTasks().get(i).setEdit(editTemp);
		taskList.getCurrentList().getTasks().get(i).setCheck(checkTemp);
		taskList.getCurrentList().getTasks().get(i).setDelete(buttonTemp);
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
    }

    public class EditListListener implements ActionListener {
	private List myList;
	public EditListListener(List myList) {
	    this.myList = myList;
	    timer.start();
	}
	public void actionPerformed(ActionEvent ev) {
	    leftPanel.removeAll();
	    leftPanel.repaint();
	    displayLists(myList);
	    timer.stop();
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
		taskList.getCurrentList().printTasksToFile(taskList.getCurrentList().getTasks(), 0, fileSave.getSelectedFile());
	    }	   
	}
    }

    public void displayLists() {
	for(int i = 0; i < taskList.getLists().size(); i++) {
	    JButton listDeleteTemp = new JButton("X");
	    JButton listEditTemp = new JButton("Edit");

	    listDeleteTemp.addActionListener(new DeleteListListener(taskList.getLists().get(i)));
	    listEditTemp.addActionListener(new EditListListener(taskList.getLists().get(i)));

	    taskList.getLists().get(i).setDelete(listDeleteTemp);
	    taskList.getLists().get(i).setEdit(listEditTemp);

	    if(taskList.getLists().get(i) == taskList.getCurrentList()) {
		taskList.getLists().get(i).getCheck().setSelected(true);
	    }

	    leftPanel.add(taskList.getLists().get(i).getCheck());
	    leftPanel.add(taskList.getLists().get(i).getLabel());
	    leftPanel.add(taskList.getLists().get(i).getEdit());
	    leftPanel.add(taskList.getLists().get(i).getDelete());
	}
	leftPanel.validate();
    }

    public void displayTasks() {
	if(sorted == true) {
	    for(int i=0; i<taskList.getCurrentList().getSortedTasks().size(); i++) {
		JButton buttonTemp = new JButton("Delete");
		JButton editTemp = new JButton("Edit");

		buttonTemp.addActionListener(new DeleteListener(taskList.getCurrentList().getSortedTasks().get(i)));
		editTemp.addActionListener(new EditListener(taskList.getCurrentList().getSortedTasks().get(i)));

		taskList.getCurrentList().getSortedTasks().get(i).setDelete(buttonTemp);
		taskList.getCurrentList().getSortedTasks().get(i).setEdit(editTemp);
		
		mainPanel.add(taskList.getCurrentList().getSortedTasks().get(i).getCheck());
		mainPanel.add(taskList.getCurrentList().getSortedTasks().get(i).getLabel());
		mainPanel.add(taskList.getCurrentList().getSortedTasks().get(i).getEdit());
		mainPanel.add(taskList.getCurrentList().getSortedTasks().get(i).getDelete());
	    }
	mainPanel.validate();
	}
	else {
  	    for(int i=0; i<taskList.getCurrentList().getTasks().size(); i++) {
		JButton buttonTemp = new JButton("Delete");
		JButton editTemp = new JButton("Edit");
		
		buttonTemp.addActionListener(new DeleteListener(taskList.getCurrentList().getTasks().get(i)));
		editTemp.addActionListener(new EditListener(taskList.getCurrentList().getTasks().get(i)));

		taskList.getCurrentList().getTasks().get(i).setDelete(buttonTemp);
		taskList.getCurrentList().getTasks().get(i).setEdit(editTemp);
		
		mainPanel.add(taskList.getCurrentList().getTasks().get(i).getCheck());
		mainPanel.add(taskList.getCurrentList().getTasks().get(i).getLabel());
		mainPanel.add(taskList.getCurrentList().getTasks().get(i).getEdit());
		mainPanel.add(taskList.getCurrentList().getTasks().get(i).getDelete());
	    }
	mainPanel.validate();
	}
    }

    public void displayComTasks() {
	if(sorted == true) {
	    for(int i = 0; i < taskList.getCurrentList().getSortedTasks().size(); i++) {
		if(taskList.getCurrentList().getSortedTasks().get(i).getCheck().isSelected() == false) {
		    JButton buttonTemp = new JButton("Delete");
		    JButton editTemp = new JButton("Edit");
		    
		    buttonTemp.addActionListener(new DeleteListener(taskList.getCurrentList().getSortedTasks().get(i)));
		    editTemp.addActionListener(new EditListener(taskList.getCurrentList().getSortedTasks().get(i)));

		    taskList.getCurrentList().getSortedTasks().get(i).setDelete(buttonTemp);
		    taskList.getCurrentList().getSortedTasks().get(i).setEdit(editTemp);

		    mainPanel.add(taskList.getCurrentList().getSortedTasks().get(i).getCheck());
		    mainPanel.add(taskList.getCurrentList().getSortedTasks().get(i).getLabel());
		    mainPanel.add(taskList.getCurrentList().getSortedTasks().get(i).getEdit());
		    mainPanel.add(taskList.getCurrentList().getSortedTasks().get(i).getDelete());
		}
	    }
	    mainPanel.validate();
	} else {
	    for(int i = 0; i < taskList.getCurrentList().getTasks().size(); i++) {
		if(taskList.getCurrentList().getTasks().get(i).getCheck().isSelected() == false) {
		    JButton buttonTemp = new JButton("Delete");
		    JButton editTemp = new JButton("Edit");
		    
		    buttonTemp.addActionListener(new DeleteListener(taskList.getCurrentList().getTasks().get(i)));
		    editTemp.addActionListener(new EditListener(taskList.getCurrentList().getTasks().get(i)));

		    taskList.getCurrentList().getTasks().get(i).setDelete(buttonTemp);
		    taskList.getCurrentList().getTasks().get(i).setEdit(editTemp);
		    
		    mainPanel.add(taskList.getCurrentList().getTasks().get(i).getCheck());
		    mainPanel.add(taskList.getCurrentList().getTasks().get(i).getLabel());
		    mainPanel.add(taskList.getCurrentList().getTasks().get(i).getEdit());
		    mainPanel.add(taskList.getCurrentList().getTasks().get(i).getDelete());
		}
	    }
	    mainPanel.validate();
	}
    }
	    

    public void displayLists(List myList) {
	for(int i = 0; i < taskList.getLists().size(); i++) {
	    List thisList = taskList.getLists().get(i);
	    if(thisList == myList) {
		leftPanel.add(thisList.getCheck());
		JTextField input = new JTextField(thisList.getListName(),6);
		thisList.setUserInput(input);
		leftPanel.add(thisList.getUserInput());
		JButton listEnterButton = new JButton("Enter");
		listEnterButton.addActionListener(new EnterListListener(thisList));
		leftPanel.add(listEnterButton);
		leftPanel.add(thisList.getDelete());
	    }
	    else {
		leftPanel.add(thisList.getCheck());
		leftPanel.add(thisList.getLabel());
		leftPanel.add(thisList.getEdit());
		leftPanel.add(thisList.getDelete());
	    }
	}
	leftPanel.validate();
    }

    
    public void displayTasks(Task myTask) {
	if(sorted) {
	    for(int i=0; i<taskList.getCurrentList().getSortedTasks().size(); i++) {
		Task thisTask = taskList.getCurrentList().getSortedTasks().get(i);
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
  	    for(int i=0; i<taskList.getCurrentList().getTasks().size(); i++) {
		Task thisTask = taskList.getCurrentList().getTasks().get(i);
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


    public class radioButtonListener implements ActionListener {
	private List myList;
	public radioButtonListener(List myList) {
	    this.myList = myList;
	}
	public void actionPerformed(ActionEvent ev) {
	    // Set the list that the radio button was clicked for to currentList
	    taskList.setCurrentList(this.myList);
	    mainPanel.removeAll();
	    mainPanel.repaint();
	    if(hidden == true) {
		displayComTasks();
	    } else {
		displayTasks();
	    }
	}
    }
	    

    public class toggleCompletedListener implements ActionListener {
	public void actionPerformed(ActionEvent ev) {
	    if(!taskList.getLists().isEmpty()) {
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
    }
    
    public class deleteCompletedListener implements ActionListener{
	public void actionPerformed(ActionEvent ev){
	    if(!taskList.getLists().isEmpty()) {
		if(sorted==true){
		    for(int i = 0; i<taskList.getCurrentList().getSortedTasks().size();i++){
			if(taskList.getCurrentList().getSortedTasks().get(i).getCheck().isSelected()==true){
			    taskList.getCurrentList().getTasks().remove(taskList.getCurrentList().getSortedTasks().get(i));
			    taskList.getCurrentList().getSortedTasks().remove(taskList.getCurrentList().getSortedTasks().get(i));
			    i = 0;
			}
		    }
		}
		else{
		    for(int i = 0; i< taskList.getCurrentList().getTasks().size();i++){
			if(taskList.getCurrentList().getTasks().get(i).getCheck().isSelected()==true){
			    taskList.getCurrentList().getTasks().remove(taskList.getCurrentList().getTasks().get(i));
			    i = 0;
			}
		    }
		}

		mainPanel.removeAll();
		mainPanel.repaint();
		displayTasks();
	    }
	}
    }

    public class EnterListListener implements ActionListener {
	private List myList;
	public EnterListListener(List myList) {
	    this.myList = myList;
	}
	public void actionPerformed(ActionEvent ev) {
	    int i;
	    for(i = 0; i < taskList.getLists().size(); i++) {
		if(taskList.getLists().get(i) == this.myList) {
		    break;
		}
	    }
	    if(i < taskList.getLists().size()) {
		List newList = taskList.makeList(this.myList.getUserInput().getText(), this.myList.getTasks(),this.myList.getSortedTasks());
		taskList.getLists().set(i,newList);
		JRadioButton listCheckTemp = new JRadioButton();
		JButton listDeleteTemp = new JButton("X");
		JButton listEditTemp = new JButton("Edit");

		listCheckTemp.addActionListener(new radioButtonListener(newList));
		listDeleteTemp.addActionListener(new DeleteListListener(newList));
		listEditTemp.addActionListener(new EditListListener(newList));

		newList.setEdit(listEditTemp);
		newList.setCheck(listCheckTemp);
		newList.setDelete(listDeleteTemp);

		taskList.getListGroup().add(taskList.getLists().get(i).getCheck());
		if(this.myList == taskList.getCurrentList()) {
		    taskList.setCurrentList(taskList.getLists().get(i));
		}

		System.out.println(taskList.getCurrentList().getListName());

		leftPanel.removeAll();
		mainPanel.removeAll();
		leftPanel.repaint();
		mainPanel.repaint();
		displayLists();
		displayTasks();
	    }
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
		for(i = 0; i<taskList.getCurrentList().getSortedTasks().size(); i++){
		    if(taskList.getCurrentList().getSortedTasks().get(i)==this.myTask)
			break;
		}
		if(i<taskList.getCurrentList().getSortedTasks().size()){
		    Task newTask = taskList.getCurrentList().makeTask(this.myTask.getUserInput().getText(),
								      taskList.getCurrentList().getSortedTasks().get(i).getPriority(),
								      taskList.getCurrentList().getSortedTasks().get(i).getColor());
		    taskList.getCurrentList().getSortedTasks().set(i, newTask);
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
		for(i = 0; i<taskList.getCurrentList().getTasks().size(); i++){
		    if(taskList.getCurrentList().getTasks().get(i)==this.myTask)
			break;
		}
		if(i<taskList.getCurrentList().getTasks().size()){
		    Task newTask = taskList.getCurrentList().makeTask(this.myTask.getUserInput().getText(),
								      taskList.getCurrentList().getTasks().get(i).getPriority(),
								      taskList.getCurrentList().getTasks().get(i).getColor());
		    taskList.getCurrentList().getTasks().set(i, newTask);
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
	

   

