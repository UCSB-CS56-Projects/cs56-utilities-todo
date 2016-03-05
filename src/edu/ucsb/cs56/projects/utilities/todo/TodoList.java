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

/**
	A represention of a list of Tasks. Does this by having an Arraylist as a private variable.

	@author Brandon Newman
	@version todo project for CS56 S13
*/
public class TodoList implements Serializable {
    
    //private ArrayList<Task> tasks = new ArrayList<Task>();
    //private ArrayList<Task> sortedTasks = new ArrayList<Task>();
    private ArrayList<List> categories = new ArrayList<List>();

    /**
       Constructor for TodoList
    */
    public TodoList()
    {
	List starterList = new List("General");
	categories.add(starterList);
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
	int i;
	for(i = 0; i < this.categories.size(); i++) {
	    if(this.categories.get(i).getCheck().isSelected()) {
		break;
	    }
	}
	return this.categories.get(i);
    }
}

















