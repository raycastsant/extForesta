package org.geocuba.foresta.gestion_datos.extensiones;

import org.geocuba.foresta.administracion_seguridad.UsuarioManager;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.gui.pGestionManager;
import com.iver.andami.plugins.Extension;

public class Gestionar_NomencladoresExt extends Extension{
	
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
		if(ConnectionExt.Conectado())
		 return ((UsuarioManager.Usuario().getNivel()==UsuarioManager.NIVEL_ADMINISTRADOR)&&
				 (!pGestionManager.Gestionando()));
		else
		 return false;
	   }

		public boolean isVisible() 
		{
		 if(ConnectionExt.Conectado())
		  return UsuarioManager.Usuario().getNivel()==UsuarioManager.NIVEL_ADMINISTRADOR;
		 else
		  return false;	
		}

}
