package org.geocuba.foresta.analisis_terreno.gui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import com.iver.andami.PluginServices;

 public class PendienteDlgManager {

	private PendienteDlg dialog = null;
	private double resultado;
	
	public double mostrar_Dialogo() 
	{
		JFrame frame = new javax.swing.JFrame();
//		ImageIcon ii = new ImageIcon(PluginServices.getPluginServices("org.geocuba.foresta").getPluginDirectory().getAbsolutePath()+"\\images\\password.gif");
//		frame.setIconImage(ii.getImage());
		
		PluginServices.getIconTheme().registerDefault("inconprueba", this.getClass().getClassLoader().getResource("images/cerrar.png"));
		ImageIcon icon = PluginServices.getIconTheme().get("inconprueba");
		frame.setIconImage(icon.getImage());
		
		dialog = new PendienteDlg(frame, "Establecer rango de pendientes");
		dialog.setLocationRelativeTo(null);
		
		setComponentsListeners();
		resultado = -1;
		dialog.setVisible(true);
		
		return resultado;
	}
	
	private void setComponentsListeners()
	{
	 if(dialog == null)
	  return;
    
	 //---------------------------------------------------------------------------------
	 dialog.getButtonAceptar().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) 
         {
        	 String rango = dialog.getFieldPendiente().getText();
        	 if(rango==null || rango.equals(""))
        	  JOptionPane.showMessageDialog(null, "Debe establecer un valor de rango", "Rango no válido", JOptionPane.ERROR_MESSAGE);
			else
			{	
			 resultado = Double.parseDouble(rango);
			 dialog.dispose();
			}
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
