package org.geocuba.foresta.administracion_seguridad.gui;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.geocuba.foresta.administracion_seguridad.Usuario;
import org.geocuba.foresta.administracion_seguridad.UsuarioManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;

import com.iver.andami.PluginServices;
import com.twmacinta.util.MD5;

 public class pUsuarioManager// implements IPanelManager 
 {
	private pUsuario panel;
	private Usuario usuario;
	private int antNivel;
	
	public pUsuarioManager() {
	}
	
	public void MostrarPanel(Usuario persistent) 
	{
	 panel = new pUsuario("Usuario");
	 	
	 usuario = persistent;
	 
	 if(!usuario.IsNewObject()) //Se está modificando
	 {
		  String panelId = "_"+panel.Title()+usuario.getId();
		  Panel p = UIUtils.GetPanel(panelId);
		  
		  panel.set_panel_id(panelId);
		
		  if(p != null)       
		   return;
		  
	  panel.getLabelId().setText(usuario.getId().toString());
	  
	  if(usuario.getNombre()!=null)
	   panel.getTFUsuario().setText(usuario.getNombre());
	  
//	  if(usuario.getPassword()!=null)
//	   panel.getFieldPassword().setText(usuario.getPassword());
	  
	  if(usuario.getNivel()!=null)
	  {	  
	   antNivel = usuario.getNivel();
	   if(antNivel == UsuarioManager.NIVEL_ADMINISTRADOR)
		panel.getRBAdministrador().setSelected(true);
	   else
		panel.getRBEspecialista().setSelected(true);
	  }	
	  
	  if(usuario.getNomb_apell()!=null && !usuario.getNomb_apell().equals(""))
	  {		  
	   String nombre = usuario.getNomb_apell().substring(0,usuario.getNomb_apell().indexOf("_"));	  
	   String apellidos = usuario.getNomb_apell().substring(usuario.getNomb_apell().indexOf("_")+1,usuario.getNomb_apell().length());
	   panel.getTFNombre().setText(nombre);
	   panel.getTFApellidos().setText(apellidos);
	  }	   
	 }	
	 else
	 {
		 panel.getFieldPassword().setEnabled(true);    
    	 panel.getjPFConfirmPass().setEnabled(true);
    	 panel.getjLPass().setEnabled(true);    
    	 panel.getjLConfirmPass().setEnabled(true);
    	 panel.getjCBXCambiarPass().setVisible(false);
	 }	 
	 
	 //panel.getTFNombre().requestFocusInWindow();
	 setComponentsListeners();
	 panel.Show();
	}
	
	private void setComponentsListeners()
	{
	 panel.getButtonGuardar().addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) 
        {
		 if(panel.getTFApellidos().getText().trim().equals("")  ||
			panel.getTFNombre().getText().trim().equals("")  ||
			panel.getTFUsuario().getText().trim().equals(""))
		 {
			 JOptionPane.showMessageDialog(null, "Faltan datos por llenar", "Información", JOptionPane.WARNING_MESSAGE);
			 return;
		 }	 
		 
		 if(panel.getRBAdministrador().isSelected())
		  usuario.setNivel(UsuarioManager.NIVEL_ADMINISTRADOR);
		 else
		 {
		  if(!usuario.IsNewObject() && antNivel==UsuarioManager.NIVEL_ADMINISTRADOR)  //Se está modificando y se trata de bajar el nivel del usuario
		  {
		   if(!UsuarioManager.quedan_Administradores_BD(usuario.getId(), null))
		   {	    
			JOptionPane.showMessageDialog(null, "No se puede cambiar el nivel del usuario."+"\n"+
		        		 "Causa: El sistema no debe carecer de usuarios administradores", "Información", JOptionPane.INFORMATION_MESSAGE);
			return;
		   }	
		   else
		    usuario.setNivel(UsuarioManager.NIVEL_MEDIO);   
		  }	
		  else
		   usuario.setNivel(UsuarioManager.NIVEL_MEDIO);
		 }		 
		 
		//Verifico que el nombre de usuario no exista
		 String nuevoNombre = panel.getTFUsuario().getText();
		 if(!usuario.IsNewObject())
		 {
		  if(!usuario.getNombre().equals(nuevoNombre)) 
		  {	  
			 if(UsuarioManager.usuarioExiste(nuevoNombre))
			 {
				 JOptionPane.showMessageDialog(null, "El usuario ya existe en la base de datos", "Información", JOptionPane.WARNING_MESSAGE);
				 return;
			 }
		  }		 
		 }	 
		 else
		 if(UsuarioManager.usuarioExiste(nuevoNombre))
		 {
				 JOptionPane.showMessageDialog(null, "El usuario que trata de insertar ya existe en la base de datos", "Información", JOptionPane.WARNING_MESSAGE);
				 return;
		 }	 
		 
		 usuario.setNombre(panel.getTFUsuario().getText());
		 
		 
		 usuario.setNomb_apell(panel.getTFNombre().getText()+"_"+panel.getTFApellidos().getText());
		 
		 if(panel.getjCBXCambiarPass().isSelected() || usuario.IsNewObject())
		 {
		  String pass = panel.getFieldPassword().getText();
		  String confirmpass = panel.getjPFConfirmPass().getText();
		  if(pass.equals(confirmpass))
		  {   
			if(pass.length() >= 8)
		     usuario.setPassword(new MD5(pass).asHex());
			else
			{		
			 JOptionPane.showMessageDialog(null, "El tamaño de la contraseña debe ser mayor o igual que 8 caracteres", "Información", JOptionPane.INFORMATION_MESSAGE);
			 return;
			} 
		  } 
		  else
		  {	  
		   JOptionPane.showMessageDialog(null, "Debe confirmar la contraseña correctamente", "Error al confirmar contraseña", JOptionPane.ERROR_MESSAGE);
		   panel.getFieldPassword().setText("");    
      	   panel.getjPFConfirmPass().setText("");
		   return;
		  } 
		 }	 
		 
		 usuario.save();
		 panel.Close();
				
		 pGestionUsuariosManager.ActualizarTabla();
		 
		//Si es el usuario actual 
		  if(usuario.getId() == UsuarioManager.Usuario().getId())
		  {	  
		   UsuarioManager.setusuario(usuario);
		   if(usuario.getNivel() == UsuarioManager.NIVEL_MEDIO)
		   {	  
		    pGestionUsuariosManager.cerrarVentana();
		    PluginServices.getMainFrame().enableControls();
		   }
		  } 
        }
     });
	//--------------------------------------------------------------------------------- 
	 panel.getButtonCancelar().addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) 
	    {
		 panel.Close();
	    }
	 });
	//---------------------------------------------------------------------------------
	 DocumentListener doclist = new DocumentListener() { 
         public void changedUpdate(DocumentEvent e) { 
             panel.getButtonGuardar().setEnabled(true); 
         } 
         public void removeUpdate(DocumentEvent e) { 
        	 panel.getButtonGuardar().setEnabled(true); 
         } 
         public void insertUpdate(DocumentEvent e) { 
        	 panel.getButtonGuardar().setEnabled(true); 
         } 
     };
	//---------------------------------------------------------------------------------
	 panel.getjCBXCambiarPass().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
        	 boolean flag = panel.getjCBXCambiarPass().isSelected();
        	 panel.getFieldPassword().setEnabled(flag);    
        	 panel.getjPFConfirmPass().setEnabled(flag);
        	 panel.getjLPass().setEnabled(flag);    
        	 panel.getjLConfirmPass().setEnabled(flag);
        	 panel.getButtonGuardar().setEnabled(true);
         }
     });
	//---------------------------------------------------------------------------------
	 panel.getRBEspecialista().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
          if(usuario !=null)
          {	  
           if(usuario.getNivel()==UsuarioManager.NIVEL_ADMINISTRADOR)
        	panel.getButtonGuardar().setEnabled(true);
          }  
         }
     });
	//---------------------------------------------------------------------------------
	 panel.getRBAdministrador().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
          if(usuario != null)
          {	  
           if(usuario.getNivel()==UsuarioManager.NIVEL_MEDIO)
        	panel.getButtonGuardar().setEnabled(true);
          }  
         }
     });
	//---------------------------------------------------------------------------------
	 panel.getTFNombre().getDocument().addDocumentListener(doclist);
	//---------------------------------------------------------------------------------
	 panel.getFieldPassword().getDocument().addDocumentListener(doclist);
	//---------------------------------------------------------------------------------
	 panel.getTFApellidos().getDocument().addDocumentListener(doclist);
	//---------------------------------------------------------------------------------
	 panel.getjPFConfirmPass().getDocument().addDocumentListener(doclist);
	//---------------------------------------------------------------------------------
	 panel.getTFUsuario().getDocument().addDocumentListener(doclist);
	}
 }
