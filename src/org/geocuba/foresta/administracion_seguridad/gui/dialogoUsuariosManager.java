package org.geocuba.foresta.administracion_seguridad.gui;

import java.awt.Component;
import java.awt.Cursor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.UsuarioManager;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import org.geocuba.foresta.herramientas.utiles.Global;
import com.iver.andami.PluginServices;
import com.twmacinta.util.MD5;

 public class dialogoUsuariosManager {

	private static dialogoUsuarios diagUsuarios = null;
//	private static boolean loginOk = false;
	
	public static void showDialogoUsuarios() 
	{
		//ImageIcon ii = new ImageIcon("gvSIG\\extensiones\\org.geocuba.foresta\\images\\guardar.png", "theme");
		JFrame frame = new javax.swing.JFrame();
		//frame.setIconImage(ii.getImage());
		diagUsuarios = new dialogoUsuarios(frame, "Entrada");
		diagUsuarios.setLocationRelativeTo(null);
		  
		setComponentsListeners();
		diagUsuarios.setVisible(true);
	}
	
	private static void setComponentsListeners()
	{
	 if(diagUsuarios == null)
	  return;
     //---------------------------------------------------------------------------------	  
	 /*diagUsuarios.getFieldUsuario().addKeyListener(new java.awt.event.KeyAdapter(){ 
	   public void keyTyped(java.awt.event.KeyEvent e) 
	   { 
	      char caracter = e.getKeyChar(); 
	      // Verificar si la tecla pulsada no es un digito 
	      if(((caracter < '0') || 
	         (caracter > '9')) && 
	         (caracter != java.awt.event.KeyEvent.VK_BACK_SPACE) 
	          && caracter != java.text.DecimalFormatSymbols.getInstance().getDecimalSeparator()) 
	       { 
	         e.consume();  // ignorar el evento de teclado 
	       } 
	    } 
	    }); */
	 //---------------------------------------------------------------------------------
	 diagUsuarios.getButtonAceptar().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) 
         {
        	 acceder_sistema();
         }
     });
	//---------------------------------------------------------------------------------
	 diagUsuarios.addWindowListener(new java.awt.event.WindowAdapter() {
         public void windowClosed(java.awt.event.WindowEvent evt) {
        	/*if(!loginOk) 
             showDialogoUsuarios();*/
         }
     });
	 //---------------------------------------------------------------------------------
	 diagUsuarios.getButtonCancelar().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
		  diagUsuarios.dispose();
         }
     });
	//---------------------------------------------------------------------------------
	 diagUsuarios.getFieldPassword().addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyTyped(java.awt.event.KeyEvent evt) 
         {
        	 char caracter = evt.getKeyChar();
             if (caracter == '\n')
              acceder_sistema();
             else
             if(!Funciones_Utiles.formatoInputTextField(diagUsuarios.getFieldPassword().getText(), caracter, 'N', 22))
              evt.consume();
 
         }
     });
	}
	
	public static void Conectar(String dbname) throws SQLException
	{
        String url="jdbc:postgresql://"+Global.PostgresServer+":"+Global.PostgresPort+"/"+dbname;
        System.out.println(Global.PostgresPw);
        Connection conn = DriverManager.getConnection(url, Global.PostgresUser, Global.PostgresPw);
        ConnectionExt.setConexion(conn);
	}
	
	private static void acceder_sistema()
	{
		String bd = Global.postgresDBName;  //diagUsuarios.getComboBD().getSelectedItem().toString();
		Component root = SwingUtilities.getRoot(diagUsuarios);
		
    	try {
    	    //Cursor de espera	
    	     root.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    		
			Conectar(bd);
			if(UsuarioManager.VerificarUsuario(diagUsuarios.getFieldUsuario().getText(), new MD5(diagUsuarios.getFieldPassword().getText()).asHex()))
			{	
	         diagUsuarios.dispose();
//             loginOk = true;	
	         TrazasManager.insertar_Traza("Accedió al sistema");

	        //Cursor normal
	         root.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			} 
	        else
	        {
	        	//Cursor normal
		         root.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		         
	         JOptionPane.showMessageDialog(null, "Usuario o Contraseña incorrectos", "Error", JOptionPane.WARNING_MESSAGE);
	         diagUsuarios.getFieldPassword().setText("");
	         ConnectionExt.setConexion(null);
	        }	
			
		} catch (SQLException e) {
			e.printStackTrace();

			//Cursor normal
	         root.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	         
			JOptionPane.showMessageDialog(null, "No se pudo establecer la conexión con la base de datos "+bd, "Error", JOptionPane.WARNING_MESSAGE);
		} 
	}
}
