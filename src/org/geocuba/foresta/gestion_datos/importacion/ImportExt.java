package org.geocuba.foresta.gestion_datos.importacion;

import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.importacion.gui.pAdvancedImport;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

public class ImportExt extends Extension{
	
	public void initialize() {
	}

	public void execute(String actionCommand) {
		
	if (actionCommand.compareTo("Import") == 0)
    {
	 pAdvancedImport imp = new pAdvancedImport();
	 PluginServices.getMDIManager().addCentredWindow(imp);
	 //imp.Show();
    }
	
    };
	
	public boolean isEnabled() 
	{
	 if(ConnectionExt.getConexionActiva() != null)
	  return ConnectionExt.Conectado();
	 else
	  return false;
	}

	public boolean isVisible() 
	{
	 if(ConnectionExt.getConexionActiva() != null)
	  return ConnectionExt.Conectado();
	 else
	  return false;
	}
}

