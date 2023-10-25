package org.geocuba.foresta.herramientas.utiles;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

public class MyTableCellEditor extends AbstractCellEditor implements TableCellEditor {

 JComponent component = new JTextField(); 
 public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) 
 { 
	 if (isSelected) 
	 {  
		
	 }  
	 
	 ((JTextField)component).setText((String)value);  
		return component; 
 }
 
	public Object getCellEditorValue() { 
		return ((JTextField)component).getText(); 
	} 
}