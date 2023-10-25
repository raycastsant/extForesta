package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;

 public class MunicipioManager implements IPersistenObjectManager
 {
	@Override
	public Municipio Cargar_Objeto_BD(int gid) //throws ReadDriverException 
	{
		 Municipio municipio = null;		
	     JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	     db.ejecutarConsulta("select gid, provincia, nombre from municipios where gid="+gid);
	     
	     if(!db.isEmpty())
	     {
	      int idProvincia = db.getValueAsInteger(0,1);
	      ProvinciaManager provman = new ProvinciaManager();
	      Provincia provincia = provman.Cargar_Objeto_BD(idProvincia);
	      
	      municipio = new Municipio(gid, db.getValueAsString(0,2), provincia, false);
	     } 	 
	     return municipio;
	}

 }
