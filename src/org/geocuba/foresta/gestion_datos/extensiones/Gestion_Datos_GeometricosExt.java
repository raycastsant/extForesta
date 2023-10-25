package org.geocuba.foresta.gestion_datos.extensiones;

import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.gui.pGestionManager;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import com.iver.andami.plugins.Extension;

public class Gestion_Datos_GeometricosExt extends Extension{
	
	public void initialize(){
	}

	public void execute(String actionCommand) 
	{
	 int sqlOption = Integer.parseInt(actionCommand);
	 pGestionManager.showPanel_Gestion(sqlOption, "");
	 
//     if (actionCommand.compareTo("cuencas") == 0)  
//	 {
//      pGestionManager.showPanel_Gestion(pGestionManager.SQL_FAJAS, "where gid in("+gids+")");			
//	 }
	}

   public boolean isEnabled() 
   {
		if(!ConnectionExt.Conectado())
		 return false;
		else
		if(VistaManager.GetActiveView() != null)	
		 return (!pGestionManager.Gestionando());
		else
		 return false;	 
   }

	public boolean isVisible() 
	{
		    if(!ConnectionExt.Conectado())
			 return false;
			else
			if(VistaManager.GetActiveView() != null)	
			 return true;
			else
			 return false;		
	}

}
