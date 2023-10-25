package org.geocuba.foresta.administracion_seguridad.extensiones;

import org.geocuba.foresta.administracion_seguridad.gui.ConfigurarConexionDlgManager;
import com.iver.andami.plugins.Extension;
 
public class ConfigurarConexionExt extends Extension{

	public void initialize() {
	}

	public void execute(String actionCommand) {
		
	if (actionCommand.compareTo("config_conexion") == 0)
    {
		 ConfigurarConexionDlgManager.mostrar_DialogoConexion();	 
    }
	
    };
	
	public boolean isEnabled() {
		return true;
	}

	public boolean isVisible() {
	  return true;		
	}

}

