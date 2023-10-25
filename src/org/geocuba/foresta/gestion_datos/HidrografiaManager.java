package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;

 public class HidrografiaManager implements IPersistenObjectManager
 {
	/**Devuelve un elemento hidrografico pasado un id de la Tabla Hidrografia.
	 * El elemento puede ser un Embalse o un Rio*/
	@Override
	public Hidrografia Cargar_Objeto_BD(int id) //throws ReadDriverException 
	{
		 Hidrografia hidro=null;
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 
		 db.ejecutarConsulta("select gid from rios where tipo_hidrografia="+id);
		 if(!db.isEmpty())
		 {
	      int idrio = db.getValueAsInteger(0,0);
	      RioManager riosManager = new RioManager();
	      hidro = riosManager.Cargar_Objeto_BD(idrio);   //.CargarRio(idrio);
		 }
		 else
		 {
		  db.ejecutarConsulta("select gid from embalses where tipo_hidrografia="+id);
		  if(!db.isEmpty())
		  {
		   int idembalse = db.getValueAsInteger(0,0);
		   EmbalseManager embalseManager = new EmbalseManager();
		   hidro = embalseManager.Cargar_Objeto_BD(idembalse);
		  } 
		 }
		 
		 return hidro;
	}
	
	public Hidrografia cargar_Hidrografia(String hidroTable, int hidroTableGid, JDBCAdapter db)
	{
		Hidrografia hidro = null;
		
		if(db == null)
		 db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		
		  db.ejecutarConsulta("select tipo_hidrografia from "+hidroTable+" where gid="+hidroTableGid);
	      if(!db.isEmpty())
	      {
	    	  int idHidro = db.getValueAsInteger(0,0);
	    	  hidro = Cargar_Objeto_BD(idHidro);
	      }	  
	      
	      return hidro;
	}
	
}
