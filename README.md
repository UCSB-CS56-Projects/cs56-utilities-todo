cs56-utilties-todo
==================

W14 Ready! (Brynn Kiefer)

Multi-level todo list application.

When complete, this application will allow user to create a todo list of tasks and subtasks that can be edited and sorted using an easy to use GUI.

javadoc: http://www.cs.ucsb.edu/~khalid/cs56/cs56-utilities-todo/javadoc

Programmer Notes
================

The main class of this project is the Task class. It has the following private instance variables:

```java
	private String taskName;
	private Calendar dueDate;
	private boolean completed;
	private ArrayList<Task> subtasks = new ArrayList<Task>();
	private Task parentTask;

```

The Task class has underlying instance variables and methods that support a multi-level task system (tasks can have parent tasks and also subtasks), but this system has not been implemented in the application or any of its functions.
