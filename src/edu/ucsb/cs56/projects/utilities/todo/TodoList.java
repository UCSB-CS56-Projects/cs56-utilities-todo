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
    
    private ArrayList<List> lists = new ArrayList<List>();
    private ArrayList<List> sortedLists = new ArrayList<List>();
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
	this.lists.add(starterList);
	this.listGroup.add(this.lists.get(0).getCheck());
	this.currentList = this.lists.get(0);
    }

    /**
       Getter method for all the lists
       @return an ArrayList of the Lists
    */
    public ArrayList<List> getLists()
    {
	return this.lists;
    }

    /**
       Getter method for the ArrayList of all Lists, sorted
       @return a sorted ArrayList of the Lists
    */
    public ArrayList<List> getSortedLists()
    {
	return this.sortedLists;
    }
    
    
    /**
       Adds a List to the ArrayList
       @param list List to be added
    */
    public void addList(List list)
    {
	this.lists.add(list);
    }

    /**
       Removes a List from the ArrayList
       @param list List to be removed
    */
    public void removeList(List list)
    {
	this.lists.remove(list);
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
	for(int i = 0; i < this.lists.size(); i++) {
	    System.out.println(this.lists.get(i).getListName());
	}
    }


    /**
       The TodoList has a list of all Lists, but is only updated when the user
       wants to sort by name. The list of Lists is first emptied and refilled
       with this function.
       This method does not atually sort the list of Lists
       @param lists the unsorted ArrayList of Lists
       @param sortedLists the ArrayList that will be populated with the unsorted ArrayList of Lists
    */
    public void updateSortedLists(ArrayList<List> lists, ArrayList<List> sortedLists)
    {
	for(int i = 0; i < lists.size(); i++)
	    {
		sortedLists.add(lists.get(i));

	    }
    }

    /**
       A new Comparator tha allows to Lists to be compared by list name
    */
    static Comparator<List> compareByListName()
    {
	return new Comparator<List>() {
	    public int compare(List l1, List l2) {
		String listName1 = l1.getListName().toUpperCase();
		String listName2 = l2.getListName().toUpperCase();
		return listName1.compareTo(listName2);
	    }
	};
    }
}

















