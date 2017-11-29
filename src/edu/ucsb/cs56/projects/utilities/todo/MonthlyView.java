package edu.ucsb.cs56.projects.utilities.todo;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;


/**
This class is the Monthly view class that opens a new frame and display the tasks of a certain list on calendar

@author Guancheng Ren
@version todo project for CS56 F17
*/


public class MonthlyView extends JFrame {
	 
	  DefaultTableModel model;
	  Calendar cal = new GregorianCalendar();
	  JLabel label;
	  
	  List list;
	 
	  MonthlyView(List list) {
		this.list = list;
	 
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    this.setTitle(list.getListName());
	    this.setSize(800,700);
	    this.setLayout(new BorderLayout());
	    this.setVisible(true);
	 
	 
	    label = new JLabel();
	    label.setHorizontalAlignment(SwingConstants.CENTER);
	 
	    JButton b1 = new JButton("<-");
	    b1.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent ae) {
	        cal.add(Calendar.MONTH, -1);
	        updateMonth();
	      }
	    });
	 
	    JButton b2 = new JButton("->");
	    b2.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent ae) {
	        cal.add(Calendar.MONTH, +1);
	        updateMonth();
	      }
	    });
	 
	    JPanel panel = new JPanel();
	    panel.setLayout(new BorderLayout());
	    panel.add(b1,BorderLayout.WEST);
	    panel.add(label,BorderLayout.CENTER);
	    panel.add(b2,BorderLayout.EAST);
	 
	 
	    String [] columns = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
	    model = new DefaultTableModel(null,columns);
	    JTable table = new JTable(model);
	    table.setRowHeight(100);
	    
	    JScrollPane pane = new JScrollPane(table);
	 
	    this.add(panel,BorderLayout.NORTH);
	    this.add(pane,BorderLayout.CENTER);
	 
	    this.updateMonth();
	 
	  }
	 
	  void updateMonth() {
	    cal.set(Calendar.DAY_OF_MONTH, 1);
	 
	    String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
	    int month_int = cal.get(Calendar.MONTH);
	    int year = cal.get(Calendar.YEAR);
	    label.setText(month + " " + year);
	 
	    int startDay = cal.get(Calendar.DAY_OF_WEEK);
	    int numberOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	    int weeks = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
	 
	    model.setRowCount(0);
	    model.setRowCount(weeks);
	 
	    int i = startDay-1;
	    for(int day=1;day<=numberOfDays;day++){
	    	model.setValueAt(list.getTaskOfACertainDay(day, month_int, year), i/7 , i%7 );    
	    	i = i + 1;
	    }
	  }
}
