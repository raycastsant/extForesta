package org.geocuba.foresta.herramientas.utiles;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TablaFilasColoresRender extends DefaultTableCellRenderer 
{
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) 
		{
//			Component cell = super.getTableCellRendererComponent(table, value,
//					isSelected, hasFocus, row, column);
				
			if(row %2== 0)
			{
			 setBackground(new Color(204,217,252));
			 setForeground(Color.BLACK);
			}
			else
			{
			 setBackground(Color.WHITE);
			 setForeground(Color.BLACK);
			}
			
//			return cell;
			
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            return this;
		}
}
