package org.geocuba.foresta.fajas.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

	public class SubtramosCellRender extends DefaultTableCellRenderer
	{
	  private Color color = null;
	  
	 public SubtramosCellRender(Color _color)
	 {
		color = _color;   
	 }
	 
	 public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column)
	 {
	  setEnabled(table == null || table.isEnabled()); // see question above

	  if(value == null)
	   setBackground(Color.RED); 
	  else
	  if(value.toString().trim().equals(""))
	   setBackground(Color.RED); 
	  else
	  {	  
		  double val = Double.parseDouble(value.toString());
		  if(val <= 0)
		   setBackground(Color.RED);
		  else
		   setBackground(color);
		  
		  System.out.println(val);
	  }	  
	  
	  setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
	  
	  /*if ((row % 2) == 0)
	   setBackground(Color.green);
	  else
	   setBackground(null);*/

	  super.getTableCellRendererComponent(table, value, selected, focused, row, column);

	 return this;
	 }
	}