cs56-utilties-todo
==================


project history
===============
```
 YES | mastergberry | sososohan,khaliddhanani | (bkiefer13) Multi-level todo list application

 W14 | jstaahl 5pm | sososohan,khaliddhanani | (bkiefer13) Multi-level todo list application
```

Multi-level todo list application.

When complete, this application will allow user to create a todo list of tasks and subtasks that can be edited and sorted using an easy to use GUI.

<img src="http://i742.photobucket.com/albums/xx64/khaliddhanani/todo_zpsd893facd.png">

run with 
```ant
ant run
```

TODO FIX javadoc: http://www.cs.ucsb.edu/~khalid/cs56/cs56-utilities-todo/javadoc

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

F17 final remarks
=================

This application allows user to create subtasks with date and time (ie. Taskname MM/DD/YY HH:TT). 
New added feature:
Monthly View: create a calendar view window with tasks.
Jar Task: create a jar file with commond ant jar.
Javadoc: https://guanguangua.github.io/cs56-utilities-todo/javadoc/
The library of Junit test is not included because we were using eclipse and eclipse have a build-in Junit library. So if you want to run the program on csil, remember to add a Junit library and edit build.xml accordingly, otherwise if you try and build (ant run), it may not compile. (Optional) Another way to bypass this is to delete all the test file that end with *Test.java.

