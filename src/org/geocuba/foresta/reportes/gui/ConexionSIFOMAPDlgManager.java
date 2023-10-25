package org.geocuba.foresta.reportes.gui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.herramientas.utiles.Global;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;
import com.iver.andami.PluginServices;

 public class ConexionSIFOMAPDlgManager {

	private static ConexionSIFOMAPDlg dialog = null;
	private static Connection conn;
	
	public static Connection mostrar_DialogoConexion() 
	{
		JFrame frame = new javax.swing.JFrame();
		frame.setIconImage(UIUtils.getThemeIcon());
		dialog = new ConexionSIFOMAPDlg(frame, "Configurar conexión");
		dialog.setLocationRelativeTo(null);
		  
		dialog.getFieldBD().setText(Global.postgresDBName);
		dialog.getFieldPassword().setText(Global.PostgresPw);
		dialog.getFieldPuerto().setText(Global.PostgresPort);
		dialog.getFieldServidor().setText(Global.PostgresServer);
		dialog.getFieldUsuario().setText(Global.PostgresUser);
		
		setComponentsListeners();
		conn = null;
		dialog.setVisible(true);
		
		//---------------------------------------------------------------
		return conn;
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
                conn = DriverManager.getConnection(url, user, pass);
                
                //Verificar existencia de tablas
                JDBCAdapter dbconn = new JDBCAdapter(conn);
                dbconn.ejecutarConsulta("select distinct b.relname from pg_attribute a, pg_class b " +
                		"where b.relfilenode=a.attrelid and (b.relname = 'rodales' or b.relname = 'lotes' " +
                		"or b.relname = 'unidadessilvicolas' or b.relname = 'empresas')");
                
                boolean flag = false;
                if(!dbconn.isEmpty())
                 if(dbconn.getRowCount() == 4)
                  flag = true;	 
                
                if(flag)
                {	
		         	JOptionPane.showMessageDialog(null, "Conexión establecida", "Información", JOptionPane.INFORMATION_MESSAGE);
		            dialog.dispose();
                }
                else
                	JOptionPane.showMessageDialog(null, "La estructura de la base de datos de SIFOMAP a la que se trata de conectar es incorrecta ", "Error", JOptionPane.ERROR_MESSAGE);
         		
         	} catch (java.lang.IndexOutOfBoundsException e) {
     			e.printStackTrace();
     		} catch (SQLException e) {
     			JOptionPane.showMessageDialog(null, "Error en los parámetros de la conexión", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
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
