package org.geocuba.foresta.administracion_seguridad.extensiones;

import org.geocuba.foresta.administracion_seguridad.UsuarioManager;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.administracion_seguridad.gui.pGestionUsuariosManager;
import org.geocuba.foresta.administracion_seguridad.gui.pMonitorearTrazasManager;
import org.geocuba.foresta.administracion_seguridad.gui.pRestaurarBDManager;
import org.geocuba.foresta.administracion_seguridad.gui.pSalvarBDManager;

import com.iver.andami.plugins.Extension;

public class AdministracionExt extends Extension{
	
	public void initialize(){
	}

	public void execute(String actionCommand) 
	{
//	 if (actionCommand.compareTo("config_conexion") == 0)
//	 {
//	  ConfigurarConexionDlgManager.mostrar_DialogoConexion();	 	
//	 }
	 
	 if (actionCommand.compareTo("gestionar_usuarios") == 0)
	 {
	  pGestionUsuariosManager.mostrarPanel_GestionUsuarios();	 	
	 }
	 
	 if (actionCommand.compareTo("restaurar") == 0)
	 {
	  pRestaurarBDManager.MostrarPanel();	 	
	 }
	 
	 if (actionCommand.compareTo("salvar") == 0)
	 {
	  pSalvarBDManager.MostrarPanel();
	 }
	 
	 if (actionCommand.compareTo("trazas") == 0)
	 {
	  pMonitorearTrazasManager.mostrarPanel();
	 }
	}

   public boolean isEnabled() 
   {
	if(ConnectionExt.Conectado())
	 return UsuarioManager.Usuario().getNivel()==UsuarioManager.NIVEL_ADMINISTRADOR;
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
