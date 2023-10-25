package org.geocuba.foresta.administracion_seguridad.gui;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.geocuba.foresta.administracion_seguridad.db.RestaurarBD;
import org.geocuba.foresta.administracion_seguridad.db.SalvarBD;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.herramientas.utiles.Global;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;
import com.iver.andami.PluginServices;
import com.iver.utiles.GenericFileFilter;

 public class pSalvarBDManager 
 {
    private static pSalvarBD pSalvarBD;
    private static String path;
	 
	 public static void MostrarPanel() 
	{
     String title = "Salvar BD";
     Panel p = UIUtils.GetPanel(title);
     
     if(p == null)
     {	 
      pSalvarBD = new pSalvarBD("Salvar BD");
	  setComponentsListeners();	
	  pSalvarBD.getjLBD().setText(Global.postgresDBName);
	  
	  pSalvarBD.Show();
     } 
	}
	
	private static void setComponentsListeners()
	{
		pSalvarBD.getButtonSeleccionar().addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) 
        {
			 File file;
			 JFileChooser jfc = new JFileChooser();
			 jfc.setDialogTitle("Seleccione la ruta del fichero");
			 jfc.setAcceptAllFileFilterUsed(false);
			 jfc.setFileFilter(new GenericFileFilter(new String[]{"dump"}, "Archivos .DUMP"));
			 
			 int retval = jfc.showSaveDialog(null);
			 if (retval == JFileChooser.APPROVE_OPTION) 
			 {
			  file = jfc.getSelectedFile();
		     
			  if (file != null)
			  {	  
			   path = file.getAbsolutePath();
			   pSalvarBD.getTFPath().setText(path);
			   pSalvarBD.getButtonRestaurar().setEnabled(true);
			  }
			  else
			   pSalvarBD.getButtonRestaurar().setEnabled(false);	  
			 }
			 else
			  pSalvarBD.getButtonRestaurar().setEnabled(false);	 
        }
     });
	//--------------------------------------------------------------------------------- 
		pSalvarBD.getButtonCancelar().addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) 
	    {
		 pSalvarBD.Close();
	    }
	 });
	//--------------------------------------------------------------------------------- 
		pSalvarBD.getButtonRestaurar().addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) 
	    {
		 String dbname = Global.postgresDBName;
			
		 PluginServices.cancelableBackgroundExecution(new SalvarBD(dbname, path));
		 pSalvarBD.Close();
	    }
	 });
	}
 }
