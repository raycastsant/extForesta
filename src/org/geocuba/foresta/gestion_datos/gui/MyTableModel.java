package org.geocuba.foresta.gestion_datos.gui;

import javax.swing.table.DefaultTableModel;

public class MyTableModel extends DefaultTableModel
 {
 private static final long serialVersionUID = 1L;

 public MyTableModel(Object[][] data, Object[] columnNames)
 {
  setDataVector(data, columnNames);
 }

 @Override
 public boolean isCellEditable(int row, int column)
 {
  return false;
 }
}