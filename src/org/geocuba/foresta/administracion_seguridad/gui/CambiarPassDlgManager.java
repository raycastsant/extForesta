package org.geocuba.foresta.administracion_seguridad.gui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.Usuario;
import org.geocuba.foresta.administracion_seguridad.UsuarioManager;
import org.geocuba.foresta.herramientas.utiles.Global;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;
import com.iver.andami.PluginServices;
import com.twmacinta.util.MD5;

 public class CambiarPassDlgManager {

	private CambiarPassDlg dialog = null;
	private Usuario usuario;
	
	public void mostrar_DialogoContrasenna() 
	{
		JFrame frame = new javax.swing.JFrame();
		ImageIcon ii = new ImageIcon(PluginServices.getPluginServices("org.geocuba.foresta").getPluginDirectory().getAbsolutePath()+""+Global.fileSeparator+"images"+Global.fileSeparator+"password.gif");
		frame.setIconImage(ii.getImage());
		
		dialog = new CambiarPassDlg(frame, "Cambiar contraseña");
		dialog.setLocationRelativeTo(null);
		  
		usuario = UsuarioManager.Usuario();
		dialog.getFieldUsuario().setText(usuario.getNombre());
		dialog.getFieldUsuario().setEditable(false);
		
		setComponentsListeners();
		dialog.setVisible(true);
	}
	
	private void setComponentsListeners()
	{
	 if(dialog == null)
	  return;
    
	 //---------------------------------------------------------------------------------
	 dialog.getButtonAceptar().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) 
         {
          PluginServices.getMDIManager().setWaitCursor();
        	
          if(dialog.getFieldPassword().getText().trim().equals("") ||
             dialog.getFieldPasswordConfirm().getText().trim().equals(""))
          {
           JOptionPane.showMessageDialog(null, "Faltan datos por llenar", "Error", JOptionPane.ERROR_MESSAGE);
           return;
          }	 
          
          String newPass = dialog.getFieldPassword().getText();
          String confirmPass = dialog.getFieldPasswordConfirm().getText();
          
          if(!newPass.equals(confirmPass))
          {	  
           JOptionPane.showMessageDialog(null, "Error al confirmar contraseña", "Error", JOptionPane.ERROR_MESSAGE);
           dialog.getFieldPassword().setText("");
           dialog.getFieldPasswordConfirm().setText("");
          } 
          else
          {	
	           if(newPass.length() >= 8)
	           {	   
		           usuario.setPassword(new MD5(newPass).asHex());
		           usuario.save();
		           UsuarioManager.setusuario(usuario);
		           JOptionPane.showMessageDialog(null, "Contraseña cambiada satisfactoriamente", "Información", JOptionPane.INFORMATION_MESSAGE);
		           dialog.dispose();
	           } 
	           else
	        	JOptionPane.showMessageDialog(null, "El tamaño de la contraseña debe ser mayor o igual que 8 caracteres", "Información", JOptionPane.INFORMATION_MESSAGE);
          } 
          
          PluginServices.getMDIManager().restoreCursor();
         }
     });
	 //---------------------------------------------------------------------------------
	 dialog.getButtonCancelar().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
          dialog.dispose();
         }
     });
	//---------------------------------------------------------------------------------
	 DocumentListener dc = new DocumentListener() { 
         public void changedUpdate(DocumentEvent e) { 
          dialog.getButtonAceptar().setEnabled(true); 
         } 
         public void removeUpdate(DocumentEvent e) { 
          dialog.getButtonAceptar().setEnabled(true); 
         } 
         public void insertUpdate(DocumentEvent e) { 
          dialog.getButtonAceptar().setEnabled(true);  
         } 
     };
   //---------------------------------------------------------------------------------
   //  dialog.getFieldUsuario().getDocument().addDocumentListener(dc); 
   //---------------------------------------------------------------------------------
     dialog.getFieldPassword().getDocument().addDocumentListener(dc);
   //---------------------------------------------------------------------------------
     dialog.getFieldPasswordConfirm().getDocument().addDocumentListener(dc);
	}
}
