package org.geocuba.foresta.herramientas.utiles;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

 public class ColoredTableCellRenderer extends DefaultTableCellRenderer
 {
  private Color color = null;
  
 public ColoredTableCellRenderer(Color _color)
 {
	color = _color;   
 }
 
 public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column)
 {
  setEnabled(table == null || table.isEnabled()); // see question above

  setBackground(color);
  
  /*if ((row % 2) == 0)
   setBackground(Color.green);
  else
   setBackground(null);*/

  super.getTableCellRendererComponent(table, value, selected, focused, row, column);

 return this;
 }
}