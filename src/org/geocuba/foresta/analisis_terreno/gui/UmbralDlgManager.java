package org.geocuba.foresta.analisis_terreno.gui;

import java.io.FileNotFoundException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import org.geocuba.foresta.herramientas.utiles.Global;

import com.iver.andami.ConfigurationException;
import com.iver.andami.PluginServices;

 public class UmbralDlgManager {

	private static UmbralDlg dialog = null;
	
	public static void mostrar_Dialogo() 
	{
		JFrame frame = new javax.swing.JFrame();
		ImageIcon ii = new ImageIcon(PluginServices.getPluginServices("org.geocuba.foresta").getPluginDirectory().getAbsolutePath()+""+Global.fileSeparator+"images"+Global.fileSeparator+"password.gif");
		frame.setIconImage(ii.getImage());
		
		dialog = new UmbralDlg(frame, "Umbral de red de drenaje");
		dialog.setLocationRelativeTo(null);
		
		String umbral = "0";
		try {
			umbral = ((Double)Funciones_Utiles.getReddUmbral()).toString();
			
		} catch (ConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "No se encontró el fichero de configuración del sistema 'configForesta.xml'.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		dialog.getFieldUmbral().setText(umbral);
		  
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
        	 String umbral = dialog.getFieldUmbral().getText();
        	 if(umbral==null || umbral.equals(""))
        	  JOptionPane.showMessageDialog(null, "Debe establecer un valor para el umbral", "Umbral no válido", JOptionPane.ERROR_MESSAGE);
			else
			{	
				try {
					
					Funciones_Utiles.setReddUmbral(Double.parseDouble(umbral));
					
				} catch (NumberFormatException e) {
					flag = false;
				} catch (ConfigurationException e) {
					flag = false;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "No se encontró el fichero de configuración del sistema 'configForesta.xml'.", "Error", JOptionPane.ERROR_MESSAGE);
					flag = false;
				}
			}
        	 
        	if(flag)
        	 JOptionPane.showMessageDialog(null, "Umbral establecido correctamente", "Imformación", JOptionPane.INFORMATION_MESSAGE); 	
        	else
        	 JOptionPane.showMessageDialog(null, "No se pudo establecer el umbral", "Error", JOptionPane.ERROR_MESSAGE);
        	
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
