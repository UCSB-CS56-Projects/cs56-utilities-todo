package edu.ucsb.cs56.projects.utilities.todo;

import edu.ucsb.cs56.projects.utilities.todo.Task;
import edu.ucsb.cs56.projects.utilities.todo.List;
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

/**
	A represention of a list of Tasks. Does this by having an Arraylist as a private variable.

	@author Brandon Newman
	@version todo project for CS56 S13
*/
public class TodoList implements Serializable {
    
    private ArrayList<List> categories = new ArrayList<List>();
    private List currentList;
    private ButtonGroup listGroup = new ButtonGroup();

    /**
       Constructor for TodoList
    */
    public TodoList()
    {
	List starterList = new List("General");
	starterList.setCheck(new JRadioButton());
	starterList.getCheck().setSelected(true);
	this.categories.add(starterList);
	this.listGroup.add(this.categories.get(0).getCheck());
	this.currentList = this.categories.get(0);
    }

    /**
       Getter method for all the lists
       @return an ArrayList of the Lists
    */
    public ArrayList<List> getLists()
    {
	return this.categories;
    }

    /**
       Adds a List to the ArrayList
       @param list List to be added
    */
    public void addList(List list)
    {
	this.categories.add(list);
    }

    /**
       Removes a List from the ArrayList
       @param list List to be removed
    */
    public void removeList(List list)
    {
	this.categories.remove(list);
    }

    /**
       Getter method for the currently selected list
       @return a List
    */
    public List getCurrentList()
    {
	return this.currentList;
    }

    /**
       Setter method for currentList
       @param List to be made the currently selected list
    */
    public void setCurrentList(List list)
    {
	this.currentList = list;
    }

    /**
       Getter method for the radio button group
       @return ButtonGroup
    */
    public ButtonGroup getListGroup()
    {
	return this.listGroup;
    }
	    

    /**
       Method that instigates dialog asking for input of a List
       Then creates a new List, and puts that in the ArrayList
       @param quickInput computer asks for input from the user to add a List
       @return List that was just created
    */
    public List makeList(String quickInput, ArrayList<Task> tasks, ArrayList<Task> sortedTasks) {
	String listName = "";

	String[] listParts = quickInput.split(" ");

	boolean isListName = true;
	int i = 0;
	while(isListName) {
	    if(listParts.length == 0) {
		isListName = false;
	    }
	    if(listParts.length - i == 1) {
		isListName = false;
	    }
	    if(listParts[i].contains("/") || listParts[i].contains(":")) {
		isListName = false;
		break;
	    }
	    listName = listName + listParts[i] + " ";
	    i++;
	}
	listName = listName.substring(0,listName.length()-1);

	String nameDelims = "[/]";
	String[] nameTokens = listName.split(nameDelims);

	if(nameTokens.length == 1) {
	    listName = listName;
	} else {
	    listName = nameTokens[nameTokens.length - 1];
	}

	List newList = new List(listName,tasks,sortedTasks);
	listGroup.add(newList.getCheck()); // Adds the List's RadioButton to the group
	return newList;
    }


    public void printListNames()
    {
	for(int i = 0; i < this.categories.size(); i++) {
	    System.out.println(this.categories.get(i).getListName());
	}
    }
}

















