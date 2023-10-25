package org.geocuba.foresta.administracion_seguridad.gui;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.geocuba.foresta.administracion_seguridad.Usuario;
import org.geocuba.foresta.administracion_seguridad.UsuarioManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;

 public class pGestionUsuariosManager 
 {
	private static pGestionUsuarios  panel = null;
	
	public static void mostrarPanel_GestionUsuarios() 
	{
	    String title = "Gestionar usuarios";
		panel = (pGestionUsuarios)UIUtils.GetPanel("_"+title);
		if(panel == null)
		{
		 panel = new pGestionUsuarios(title);	
		 
		 ActualizarTabla();
		 setComponentsListeners();
		 panel.Show();
		}
	}
	
	private static void setComponentsListeners()
	{
	 if(panel == null)
	  return;
	
	 //---------------------------------------------------------------------------------
	 panel.getButtonAdd().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) 
         {
			 Usuario usuario = new Usuario(); 
			 pUsuarioManager pum = new pUsuarioManager();
             pum.MostrarPanel(usuario);            			
         }
     });
	 //---------------------------------------------------------------------------------
	 panel.getButtonCerrar().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) 
         {
          panel.Close();	
         }
     });
	//---------------------------------------------------------------------------------
	 panel.getButtonModificar().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) 
         {
		  try {
			  Usuario usuario = cargarObjetoSeleccionado();
			  pUsuarioManager pum = new pUsuarioManager();
	          pum.MostrarPanel(usuario);
	          
		   } catch (ReadDriverException e) {
			   e.printStackTrace();
		   }
         }
     });
	//---------------------------------------------------------------------------------
	 panel.getButtonEliminar().addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) 
      {
       Object[] options = { "Si", "No" };
       int result = JOptionPane.showOptionDialog(null, "¿Está seguro de eliminar el usuario seleccionado?", "Advertencia", 
        			 JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
               
       if(result == 0)
       {	   
           int row = panel.getTable().getSelectionModel().getMaxSelectionIndex();
           int id = Integer.parseInt(panel.getTable().getValueAt(row,0).toString());  //la primera columna siempre debe ser el Id o el Gid
           
           if(id == UsuarioManager.Usuario().getId())
        	JOptionPane.showMessageDialog(null, "No se puede eliminar el usuario actual", "Información", JOptionPane.INFORMATION_MESSAGE);
           else
           {
            boolean existen = UsuarioManager.quedan_Administradores_BD(id, null);
            
            //Si quedan administradores 
			if(existen)
			{
				UsuarioManager usman = new UsuarioManager();
				Usuario usuario = (Usuario)usman.Cargar_Objeto_BD(id);
				usuario.delete();
				ActualizarTabla();
			}	
			else
			 JOptionPane.showMessageDialog(null, "No se puede eliminar el usuario."+"\n"+
					 "Causa: El sistema no debe carecer de usuarios administradores", "Información", JOptionPane.INFORMATION_MESSAGE);
           }	   
         }
      }
     });
	//---------------------------------------------------------------------------------
		 panel.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
	         public void valueChanged(ListSelectionEvent e) 
	         {
	          panel.getButtonEliminar().setEnabled(true);
	          panel.getButtonModificar().setEnabled(true);
	         }
	     });
	}
	
	public static void ActualizarTabla()
	{
	 PluginServices.getMDIManager().setWaitCursor();
	 
	 panel.ActualizarTabla(UsuarioManager.getQueryTabla());
	 panel.getTable().getTableHeader().setReorderingAllowed(false);
	 
	 panel.getButtonEliminar().setEnabled(false);
     panel.getButtonModificar().setEnabled(false);
     
     PluginServices.getMDIManager().restoreCursor();
	}
	
	private static Usuario cargarObjetoSeleccionado() throws ReadDriverException
	{
		 PluginServices.getMDIManager().setWaitCursor();
		 
		 int row = panel.getTable().getSelectedRow();
     	 if(row < 0)
     	 {
     	  PluginServices.getMDIManager().restoreCursor();	 
     	  JOptionPane.showMessageDialog(null, "No se obtuvo la fila seleccionada", "ERROR", JOptionPane.ERROR_MESSAGE);
     	  return null;
     	 }
     	 
		 int id = Integer.parseInt(panel.getTable().getValueAt(row,0).toString());
		
	     UsuarioManager usman = new UsuarioManager();
	     Usuario usuario = (Usuario)usman.Cargar_Objeto_BD(id);
	
		 if(usuario == null)
		 {
		  PluginServices.getMDIManager().restoreCursor();
		  JOptionPane.showMessageDialog(null, "No se pudo cargar el objeto");
		  return null;
		 }
		 
		 PluginServices.getMDIManager().restoreCursor();
		 return usuario;
	}
	
	public static void cerrarVentana()
	{
	 panel.Close();	
	}
}

 