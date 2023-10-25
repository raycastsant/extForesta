package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.IPersistenObjectManager;

public class ProvinciaManager implements IPersistenObjectManager
{
	@Override
	public Provincia Cargar_Objeto_BD(int id) //throws ReadDriverException 
 	{
     Provincia provincia = null;
     JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
     db.ejecutarConsulta("select nombre from provincias where gid="+id);
     
     if(!db.isEmpty())
      provincia = new Provincia(id, db.getValueAsString(0,0), false);
      
     return provincia;
	}
	
	public Provincia[] listarProvincias() 
 	{
     Provincia []provincias = null;
     JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
     db.ejecutarConsulta("select gid, nombre from provincias");
     
     if(!db.isEmpty())
     {	 
      provincias = new Provincia[db.getRowCount()];
      for(int i=0; i<db.getRowCount(); i++)
       provincias[i] = new Provincia(db.getValueAsInteger(i,0), db.getValueAsString(i,1), false);
     } 
      
     return provincias;
	}
}	
