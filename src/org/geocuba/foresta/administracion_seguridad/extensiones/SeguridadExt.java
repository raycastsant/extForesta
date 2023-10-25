package org.geocuba.foresta.administracion_seguridad.extensiones;

import java.sql.SQLException;
import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.UsuarioManager;
import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.geocuba.foresta.administracion_seguridad.db.CargarCapasWT;
import org.geocuba.foresta.administracion_seguridad.gui.CambiarPassDlgManager;
import org.geocuba.foresta.analisis_terreno.gui.CellSizeDlgManager;
import org.geocuba.foresta.analisis_terreno.gui.UmbralDlgManager;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.view.ProjectView;
import com.iver.cit.gvsig.project.documents.view.ProjectViewFactory;
import com.iver.cit.gvsig.project.documents.view.gui.View;

public class SeguridadExt extends Extension{
	
	public void initialize(){
	}

	public void execute(String actionCommand) 
	{
	 if (actionCommand.compareTo("cambiar_pass") == 0)
	 {
	  CambiarPassDlgManager ccman = new CambiarPassDlgManager();
	  ccman.mostrar_DialogoContrasenna(); 	 	
	 }
	 
	 if (actionCommand.compareTo("desconectar") == 0)
	 {
	  PluginServices.getMDIManager().closeAllWindows();
	  //AlgUtils.GetActiveView().disable();
	  TrazasManager.insertar_Traza("Se desconectó");
	  UsuarioManager.setusuario(null);
	  
	   try {
		   ConnectionExt.getConexionActiva().close();
		   
	   } catch (SQLException e) {
		  e.printStackTrace();
	   }
	 }
	 
	 if (actionCommand.compareTo("cargar_vista") == 0)
	 {
	  VistaManager.MostrarVista();
	 }
	 
//	    if (actionCommand.compareTo("preferencias") == 0)
//	    {
//		 DlgPreferences dlgPreferences = PluginServices.getDlgPreferences();
//		 dlgPreferences.refreshExtensionPoints();
//		 PluginServices.getMDIManager().addWindow(dlgPreferences);
//	    }
	 
	    if (actionCommand.compareTo("umbral_redd") == 0)
	    {
		 UmbralDlgManager.mostrar_Dialogo();
	    }
		
		if (actionCommand.compareTo("cell_size") == 0)
	    {
		 CellSizeDlgManager.mostrar_Dialogo();
	    }
	}
	
   public boolean isEnabled() 
   {
	return ConnectionExt.Conectado();
   }

	public boolean isVisible() 
	{
     return ConnectionExt.Conectado();	
	}
}
