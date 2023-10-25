package org.geocuba.foresta.administracion_seguridad.gui;

import java.io.File;
import java.sql.SQLException;
import javax.swing.JFileChooser;
import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.Usuario;
import org.geocuba.foresta.administracion_seguridad.UsuarioManager;
import org.geocuba.foresta.administracion_seguridad.db.RestaurarBD;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.herramientas.utiles.Global;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;
import com.iver.andami.PluginServices;
import com.iver.utiles.GenericFileFilter;

 public class pRestaurarBDManager 
 {
    private static pRestaurarBD pRestaura;
    private static String path;
	 
	 public static void MostrarPanel() 
	{
     String title = "_Restaurar BD";
     Panel p = UIUtils.GetPanel(title);
     
     if(p == null)
     {	 
	  pRestaura = new pRestaurarBD("Restaurar BD");
	  setComponentsListeners();	
	  pRestaura.Show();
     } 
	}
	
	private static void setComponentsListeners()
	{
		pRestaura.getButtonSeleccionar().addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) 
        {
			 File file;
			 JFileChooser jfc = new JFileChooser();
			 jfc.setDialogTitle("Seleccione el fichero a restaurar");
			 jfc.setAcceptAllFileFilterUsed(false);
			 jfc.setFileFilter(new GenericFileFilter(new String[]{"dump"}, "Archivos .DUMP"));
			 
			 int retval = jfc.showOpenDialog(null);
			 if (retval == JFileChooser.APPROVE_OPTION) 
			 {
			  file = jfc.getSelectedFile();
		     
			  if (file != null)
			  {	  
			   path = file.getAbsolutePath();
			   pRestaura.getTFPath().setText(path);
			   pRestaura.getButtonRestaurar().setEnabled(true);
			  }
			  else
			   pRestaura.getButtonRestaurar().setEnabled(false);	  
			 }
			 else
			  pRestaura.getButtonRestaurar().setEnabled(false);	 
        }
     });
	//--------------------------------------------------------------------------------- 
		pRestaura.getButtonCancelar().addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) 
	    {
		 pRestaura.Close();
	    }
	 });
	//--------------------------------------------------------------------------------- 
		pRestaura.getButtonRestaurar().addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) 
	    {
//		 if(pRestaura.getTFNombreBD().getText().trim().equals(""))
//		 {
//		  JOptionPane.showMessageDialog(null, "Debe establecer un nombre de base de datos", "Error", JOptionPane.ERROR_MESSAGE);
//	      return;	 
//		 }	 
		 
		 String dbname = Global.postgresDBName;    //pRestaura.getTFNombreBD().getText();
			
//		 String srid;
//		 if(pRestaura.getRBCubaNorte().isSelected())
//		  srid = "2085";
//		 else
//	      srid = "2086";
		 
		 String []trazas = TrazasManager.ObtenerTrazas(); 
			 
	         try {
	        	 Usuario user = UsuarioManager.Usuario(); 
	        	 UsuarioManager.setusuario(null);
				 ConnectionExt.getConexionActiva().close();
				 ConnectionExt.setConexion(null);
			     PluginServices.getMDIManager().closeAllWindows();
				 PluginServices.getMainFrame().enableControls();
				 PluginServices.cancelableBackgroundExecution(new RestaurarBD(dbname, path, trazas, user));
				 
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
	 });
	}
 }
