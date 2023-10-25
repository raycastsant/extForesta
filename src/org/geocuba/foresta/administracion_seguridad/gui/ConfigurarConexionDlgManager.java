package org.geocuba.foresta.administracion_seguridad.gui;

import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.geocuba.foresta.administracion_seguridad.StringEncrypter;
import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.UsuarioManager;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.herramientas.utiles.Global;
import org.geocuba.foresta.herramientas.utiles.XMLUtils;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;
import com.iver.andami.ConfigurationException;
import com.iver.andami.PluginServices;
import com.iver.utiles.XMLEntity;

 public class ConfigurarConexionDlgManager {

	private static ConfigurarConexionDlg dialog = null;
	
	public static void mostrar_DialogoConexion() 
	{
		JFrame frame = new javax.swing.JFrame();
		frame.setIconImage(UIUtils.getThemeIcon());
		dialog = new ConfigurarConexionDlg(frame, "Configurar conexión");
		dialog.setLocationRelativeTo(null);
		  
		dialog.getFieldBD().setText(Global.postgresDBName);
		dialog.getFieldPassword().setText(Global.PostgresPw);
		dialog.getFieldPuerto().setText(Global.PostgresPort);
		dialog.getFieldServidor().setText(Global.PostgresServer);
		dialog.getFieldUsuario().setText(Global.PostgresUser);
		
		setComponentsListeners();
		dialog.setVisible(true);
	}
	
	private static void setComponentsListeners()
	{
	 if(dialog == null)
	  return;
     //---------------------------------------------------------------------------------	  
	 dialog.getFieldPuerto().addKeyListener(new java.awt.event.KeyAdapter(){ 
	   public void keyTyped(java.awt.event.KeyEvent e) 
	   { 
	    char caracter = e.getKeyChar();
	    boolean value = true;
	    
	    if(((caracter < '0') || (caracter > '9')) && (caracter != '\b'))
         value = false; 
	    else
	    if(dialog.getFieldPuerto().getText().length() >= 4) 	
	     value = false; 

	    if(!value)
	     e.consume();
	   } 
	  }); 
	 //---------------------------------------------------------------------------------
	 dialog.getButtonAceptar().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) 
         {
          if(dialog.getFieldBD().getText().trim().equals("") ||
        	 dialog.getFieldPassword().getText().trim().equals("") ||
        	 dialog.getFieldPuerto().getText().trim().equals("") ||
        	 dialog.getFieldServidor().getText().trim().equals("") ||
        	 dialog.getFieldUsuario().getText().trim().equals(""))
          {
           JOptionPane.showMessageDialog(null, "Faltan datos por llenar", "Error", JOptionPane.ERROR_MESSAGE);
           return;
          }	  
        	 
        	PluginServices.getMDIManager().setWaitCursor();
        	
         	try {
         		
         		String db = dialog.getFieldBD().getText();
         		String port = dialog.getFieldPuerto().getText();
         		String pass = dialog.getFieldPassword().getText();
         		String user = dialog.getFieldUsuario().getText();
         		String server = dialog.getFieldServidor().getText();
         		
         		String url="jdbc:postgresql://"+server+":"+port+"/"+db;
                DriverManager.getConnection(url, user, pass);
                
	         		XMLEntity xmlPadre = XMLUtils.persistenceFromXML("gvSIG"+Global.fileSeparator+"extensiones"+Global.fileSeparator+"org.geocuba.foresta"+Global.fileSeparator+"configForesta.xml");
	         		int index = xmlPadre.firstIndexOfChild("name", "conexion");
	         		XMLEntity xmlconexion = xmlPadre.getChild(index);
	         	
	         		Global.postgresDBName = db;
	         		Global.PostgresPort = port;
	         		Global.PostgresPw = pass;
	         		Global.PostgresUser = user;
	         		Global.PostgresServer = server;
	         		
	         	//Encriptando datos
	         		pass = StringEncrypter.Encriptar(pass);
	         		
	         		xmlconexion.putProperty("PostgresDBName", db);
	         		xmlconexion.putProperty("PostgresPort", port);
	         		xmlconexion.putProperty("PostgresPass", pass);
	         		xmlconexion.putProperty("PostgresUser", user);
	         		xmlconexion.putProperty("PostgresServerDir", server);
	         		
	         		xmlPadre.removeChild(index);
	         		System.out.println(xmlconexion.getName());
	         		xmlPadre.addChild(xmlconexion);
	         		XMLUtils.persistenceToXML(xmlPadre, "gvSIG"+Global.fileSeparator+"extensiones"+Global.fileSeparator+"org.geocuba.foresta"+Global.fileSeparator+"configForesta.xml");
	         		
	         	  //Desconecto	
	         		PluginServices.getMDIManager().closeAllWindows();
	         		//TrazasManager.insertar_Traza("Cambió la configuración de la conexión");
	         		
	         		UsuarioManager.setusuario(null);
	         		if(ConnectionExt.getConexionActiva() !=null )
	         	     ConnectionExt.getConexionActiva().close();
	         		
	         		JOptionPane.showMessageDialog(null, "Configuración establecida", "Información", JOptionPane.INFORMATION_MESSAGE);
	         		dialog.dispose();
         		
         	} catch (ConfigurationException e) {
         		e.printStackTrace();
         	}
         	catch (java.lang.IndexOutOfBoundsException e) {
     			e.printStackTrace();
     		} catch (SQLException e) {
     			JOptionPane.showMessageDialog(null, "Error en los parámetros de la conexión", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "No se encontró el fichero de configuración del sistema 'configForesta.xml'.", "Error", JOptionPane.ERROR_MESSAGE);
			};
         	
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
     dialog.getFieldUsuario().getDocument().addDocumentListener(dc); 
   //---------------------------------------------------------------------------------
     dialog.getFieldPassword().getDocument().addDocumentListener(dc);
   //---------------------------------------------------------------------------------
     dialog.getFieldServidor().getDocument().addDocumentListener(dc);
   //---------------------------------------------------------------------------------
     dialog.getFieldBD().getDocument().addDocumentListener(dc);
   //---------------------------------------------------------------------------------
     dialog.getFieldPuerto().getDocument().addDocumentListener(dc);
	}
}
