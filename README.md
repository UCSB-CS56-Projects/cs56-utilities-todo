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



W16 Final Remarks
=================

The code for this TodoList project was changed quite a bit going from 2015-2016.

- Essentially, this is a TodoList that lets you add Tasks to a TodoList.
- The TodoList is initialized with an empty List called "General", in which you can add tasks.
- User can also add more Lists if they want, and you can add Tasks to those Lists as well.
- Tasks can only be added to the currently selected List.
  -> If there aren't any Lists (i.e. all have been deleted by user), then you cannot add a Task.

The classes for this project, as you will see, are Task, List, TodoList, and Todo.

Task: This contains the code that defines a single Task.
List: This contains the code that defines a single List.
      -> A List is just an ArrayList that can hold Tasks.

TodoList: This class contains conde that defines all current Lists.
	  -> A TodoList contains two ArrayLists (tasks and sortedTasks) that hold Tasks.

Todo: This class initializes a TodoList and contais all the GUI code for the Todo List.
      -> You may find most of your bugs to stem from issues in this class.

- In these classes, you will find some miscellaneous code for implementing subTasks and parentTasks, which is no longer needed.
  -> This was fixed by adding List.java, which is an ArrayList that can contain Tasks.
  -> Although this isn't necessarily a bug, refactoring by removing useless code would make the project much cleaner.