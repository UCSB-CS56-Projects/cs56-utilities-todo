package edu.ucsb.cs56.projects.utilities.todo;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

public class TaskTest {

	/**
    	test no arg constructor from Task
    	@see Task#Task()
    */
	@Test
	public void NoArgConstructorTest() {
		Task t = new Task();
		//fail("Not yet implemented");
	}
	
	
	/**
    	test constructor that initializes from parameters
    	@see Task#Task(string taskName, int year, int month, int day, int hour, int min, Task parentTask, int priorityInteger, Color newColor)
    */
	@Test
	public void HasArgConstructorTest() {
		// String taskName, int year, int month, int day, int hour, int min, Task parentTask, int priorityInteger, Color newColor
		Task t = new Task("myTask", 2017, 3, 12, 1, 20, new Task(), 1, Color.BLACK);
		assertEquals("myTask", t.getName());
		assertEquals(1, t.getPriority());
		assertEquals("!", t.getPriorityString());
	}
	
	
	/**
		test constructor that initializes from parameters
		@see Task#Task(string taskName, int year, int month, int day, int hour, int min, Task parentTask, int priorityInteger, Color newColor)
	*/
	@Test
	public void HasArgConstructorTest_NoDates() {
		// String taskName, int year, int month, int day, int hour, int min, Task parentTask, int priorityInteger, Color newColor
		Task t = new Task("myTask", -1, -1, -1, -1, -1, new Task(), 1, Color.BLACK);
		assertEquals("myTask", t.getName());
		assertEquals(1, t.getPriority());
		assertEquals("!", t.getPriorityString());
	}

	
	/**
		test constructor that initializes from illegal parameters, like 13th month
		@see Task#Task(string taskName, int year, int month, int day, int hour, int min, Task parentTask, int priorityInteger, Color newColor)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void HasArgConstructor_ExpectErrorTest() {
		// String taskName, int year, int month, int day, int hour, int min, Task parentTask, int priorityInteger, Color newColor
		// When the month, day, time exceed their limits, should prompt error
		Task t = new Task("myTask", 2017, 13, 32, 25, 61, new Task(), 1, Color.BLACK);
	}
	
	
	/**
		test constructor that initializes from illegal parameters, leap year situation
		@see Task#Task(string taskName, int year, int month, int day, int hour, int min, Task parentTask, int priorityInteger, Color newColor)
	*/
	@Test(expected = IllegalArgumentException.class)
	public void HasArgConstructor_ExpectErrorTest_LeapYear() {
		// String taskName, int year, int month, int day, int hour, int min, Task parentTask, int priorityInteger, Color newColor
		// When the month does not have that day
		Task t = new Task("myTask", 2017, 02, 29, 23, 50, new Task(), 1, Color.BLACK);
	}
	
	/**
		test constructor that initializes from legal parameters, leap year situation
		@see Task#Task(string taskName, int year, int month, int day, int hour, int min, Task parentTask, int priorityInteger, Color newColor)
	*/
	@Test
	public void HasArgConstructorTest_LeapYear() {
		// String taskName, int year, int month, int day, int hour, int min, Task parentTask, int priorityInteger, Color newColor
		// When the month does not have that day
		Task t = new Task("myTask", 2020, 02, 29, 23, 50, new Task(), 1, Color.BLACK);
	}
	
	
	/**
		test get date to string
		@see Task#getDueDate()
	*/
	@Test
	public void getDueDateTest() {
		// String taskName, int year, int month, int day, int hour, int min, Task parentTask, int priorityInteger, Color newColor
		Task t = new Task("myTask", 2017, 3, 12, 1, 20, new Task(), 1, Color.BLACK);
		assertEquals("03/12/2017 01:20", t.getDueDate());
	}
	
	
	/**
		test get date to string
		@see Task#getDueDate()
	*/
	@Test
	public void getDueDateTest2() {
		// String taskName, int year, int month, int day, int hour, int min, Task parentTask, int priorityInteger, Color newColor
		Task t = new Task("myTask", 1993, 12, 24, 12, 25, new Task(), 1, Color.BLACK);
		assertEquals("12/24/1993 12:25", t.getDueDate());
	}
	
	
	/**
		test get color
		@see Task#getColor()
	*/
	@Test
		public void getColorTest() {
		// String taskName, int year, int month, int day, int hour, int min, Task parentTask, int priorityInteger, Color newColor
		Task t = new Task("myTask", 1993, 12, 24, 12, 25, new Task(), 1, Color.BLUE);
		assertEquals(Color.BLUE, t.getColor());
	}
	
	
	/**
		test get overdue checker
		@see Task#isOverdue()
	*/
	@Test
	public void isOverdueTest() {
		// String taskName, int year, int month, int day, int hour, int min, Task parentTask, int priorityInteger, Color newColor
		Task t = new Task("myTask", 1993, 12, 24, 12, 25, new Task(), 1, Color.BLUE);
		assertEquals(true, t.isOverdue());
	}
	
	
	/**
		test get overdue checker
		@see Task#isOverdue()
	*/
	@Test
	public void isOverdueTest2() {
		// String taskName, int year, int month, int day, int hour, int min, Task parentTask, int priorityInteger, Color newColor
		Task t = new Task("myTask", 4000, 12, 24, 12, 25, new Task(), 1, Color.BLUE);
		assertEquals(false, t.isOverdue());
	}
	
	
	/**
		test completed marker and checker
		@see Task#isOverdue()
	*/
	@Test
	public void completedMarkerAndCheckerTest() {
		// String taskName, int year, int month, int day, int hour, int min, Task parentTask, int priorityInteger, Color newColor
		Task t = new Task("myTask", 2017, 12, 24, 12, 25, new Task(), 1, Color.BLUE);
		t.markCompleted();
		assertEquals(true, t.getCompleted());
	}
	
	
	/**
		test toString method without date
		@see Task#toString()
	*/
	@Test
	public void toStringTest_NoDates() {
		// String taskName, int year, int month, int day, int hour, int min, Task parentTask, int priorityInteger, Color newColor
		Task t = new Task("myTask", -1, -1, -1, -1, -1, new Task(), 2, Color.BLACK);
		assertEquals("[ ] myTask     !!", t.toString());
	}

	
	/**
		test toString method with date
		@see Task#toString()
	*/
	@Test
	public void toStringTest_WithDates() {
		// String taskName, int year, int month, int day, int hour, int min, Task parentTask, int priorityInteger, Color newColor
		Task t = new Task("myTask", 2017, 12, 24, 12, 25, new Task(), 3, Color.BLUE);
		assertEquals("[ ] myTask 12/24/2017 12:25     !!!", t.toString());
	}

	
	/**
		test toString method with date, and completion
		@see Task#toString()
	*/
	@Test
	public void toStringTest_WithDatesAndCompleted() {
		// String taskName, int year, int month, int day, int hour, int min, Task parentTask, int priorityInteger, Color newColor
		Task t = new Task("myTask", 2017, 12, 24, 12, 25, new Task(), 3, Color.BLUE);
		t.markCompleted();
		assertEquals("[x] myTask 12/24/2017 12:25     !!!", t.toString());
	}
	
	// TODO: Test get calendar form
}
