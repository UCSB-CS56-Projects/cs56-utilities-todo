package edu.ucsb.cs56.projects.utilities.todo;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ListTest {
	
	private Task t1 = new Task();
	private Task t2 = new Task("apple", 2017, 3, 12, 1, 20, new Task(), 1, Color.BLACK);
	private Task t3 = new Task("bed", -1, -1, -1, -1, -1, new Task(), 2, Color.BLUE);
	private Task t4 = new Task("cat", 1993, 12, 24, 12, 25, new Task(), 3, Color.BLUE);
	private Task t5 = new Task("dad", 1997, 7, 12, 7, 3, new Task(), 3, Color.RED);
	private Task t6 = new Task("dead", 1995, 3, 6, 12, 9, new Task(), 3, Color.RED);
	private Task t7 = new Task("dead", 1995, 3, 6, 12, 9, new Task(), 3, Color.RED);
	
	// Random List
	private ArrayList<Task> l1 = new ArrayList<Task>();
	// Sorted List
	private ArrayList<Task> s1 = new ArrayList<Task>();
	
	@Before
	public void setUp() throws Exception {
		l1.add(t4);
		l1.add(t2);
		l1.add(t3);
		l1.add(t6);
		l1.add(t5);
		s1.add(t2);
		s1.add(t3);
		s1.add(t4);
		s1.add(t5);
		s1.add(t6);
	}

	@After
	public void tearDown() throws Exception {
		l1.clear();
		s1.clear();
	}
	
	
	/**
		test one arg constructor from List
		@see List#List(String listName)
	*/
	@Test
	public void OneArgConstructorTest() {
		List temp = new List("myList");
		assertEquals(temp.getListName(),"myList");
	}

	
	/**
		test three arg constructor from List
		@see List#List(String listName, ArrayList<Task> tasks, ArrayList<Task> sortedTasks)
	*/
	@Test
	public void ThreeArgConstructorTest() {
		List temp = new List("myList", l1, s1);
		assertEquals(temp.getListName(),"myList");
		assertEquals(temp.getTasks().equals(l1),true);
		assertEquals(temp.getSortedTasks().equals(s1),true);
	}
	
	
	/**
		test addTask and removeTask
		@see List#addTask(Task task)
		@see List#removeTask(Task task)
	*/
	@Test
	public void addTaskAndremoveTaskTest() {
		List temp = new List("myList");
		temp.addTask(t2);
		temp.addTask(t3);
		temp.addTask(t4);
		temp.addTask(t5);
		temp.addTask(t6);
		assertEquals(temp.getTasks().equals(s1), true);
		s1.remove(t6);
		temp.removeTask(t6);
		s1.remove(t3);
		temp.removeTask(t3);
		assertEquals(temp.getTasks().equals(s1), true);
	}
	
	
	/**
		test toString from List
		@see List#toString()
	*/
	@Test
	public void toStringTest() {
		List temp = new List("myList", l1, s1);
		assertEquals(temp.toString(),"myList");
	}
	
	
	/**
		test makeTask from List
		@see List#makeTask(String quickInput, int priorityNumber, java.awt.Color taskColor)
	*/
	@Test
	public void makeTaskTest() {
		List temp = new List("myList", l1, s1);
		Task tempTask = temp.makeTask("myTask 03/12/1997 10:30", 1, Color.BLACK);
		Task realTask = new Task("myTask", 2017, 03, 12, 10, 30, null, 1, Color.BLACK);
		assertEquals(tempTask.equals(realTask), true);
	}
	
	/**
		test makeTask from List, no dates situation
		@see List#makeTask(String quickInput, int priorityNumber, java.awt.Color taskColor)
	*/
	@Test
	public void makeTaskTest2() {
		List temp = new List("myList", l1, s1);
		Task tempTask = temp.makeTask("myTask", 1, Color.BLACK);
		Task realTask = new Task("myTask", -1, -1, -1, -1, -1, null, 1, Color.BLACK);
		assertEquals(tempTask.equals(realTask), true);
	}
	
	/**
		test makeTask from List, no dates situation
		@see List#makeTask(String quickInput, int priorityNumber, java.awt.Color taskColor)
	*/
	@Test
	public void sortByNameTest() {
		Collections.sort(l1, List.compareByName());
		assertEquals(l1.equals(s1), true);
	}
	
	// TODO: Test Other Comparators
}
