package org.geocuba.foresta.analisis_terreno.gui;

import java.io.FileNotFoundException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import org.geocuba.foresta.herramientas.utiles.Global;

import com.iver.andami.ConfigurationException;
import com.iver.andami.PluginServices;

 public class CellSizeDlgManager {

	private static CellSizeDlg dialog = null;
	
	public static void mostrar_Dialogo() 
	{
		JFrame frame = new javax.swing.JFrame();
		ImageIcon ii = new ImageIcon(PluginServices.getPluginServices("org.geocuba.foresta").getPluginDirectory().getAbsolutePath()+""+Global.fileSeparator+"images"+Global.fileSeparator+"password.gif");
		frame.setIconImage(ii.getImage());
		
		dialog = new CellSizeDlg(frame, "Amplitud de las celdas del MDE");
		dialog.setLocationRelativeTo(null);
		
		String value = "0";
		try {
			value = ((Double)Funciones_Utiles.getRasterCellZize()).toString();
			
		} catch (ConfigurationException e) {
			e.printStackTrace();
			return;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "No se encontró el fichero de configuración del sistema 'configForesta.xml'.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		dialog.getFieldCeldas().setText(value);
		  
		setComponentsListeners();
		dialog.setVisible(true);
	}
	
	private static void setComponentsListeners()
	{
	 if(dialog == null)
	  return;
    
	 //---------------------------------------------------------------------------------
	 dialog.getButtonAceptar().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) 
         {
        	 boolean flag = true;
        	 String value = dialog.getFieldCeldas().getText();
        	 if(value==null || value.equals(""))
        	  JOptionPane.showMessageDialog(null, "Debe establecer un tamaño de celdas", "Tamaño de celdas no válido", JOptionPane.ERROR_MESSAGE);
			else
			{	
				try {
					
					Funciones_Utiles.setRasterCellZize(Double.parseDouble(value));
					
				} catch (NumberFormatException e) {
					flag = false;
				} catch (ConfigurationException e) {
					flag = false;
				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(null, "No se encontró el fichero de configuración del sistema 'configForesta.xml'.", "Error", JOptionPane.ERROR_MESSAGE);
					flag = false;
				}
			}
        	 
        	if(flag)
        	 JOptionPane.showMessageDialog(null, "Tamaño de celdas establecido correctamente", "Imformación", JOptionPane.INFORMATION_MESSAGE); 	
        	else
        	 JOptionPane.showMessageDialog(null, "No se pudo establecer el tamaño de celdas", "Error", JOptionPane.ERROR_MESSAGE);
        	
        	dialog.dispose();
         }
     });
	 //---------------------------------------------------------------------------------
	 dialog.getButtonCancelar().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
          dialog.dispose();
         }
     });
	}
}
