package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.IPersistenObjectManager;

public class Textura_suelosManager implements IPersistenObjectManager 
{
	@Override
	public Textura_suelos Cargar_Objeto_BD(int id) //throws ReadDriverException 
	{
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 String sql = "select textura from textura_suelos where id="+id;
		 db.ejecutarConsulta(sql);
		 
		 if(db.isEmpty())
		  return null;
		 
		 Textura_suelos textura = new Textura_suelos(id, db.getValueAsString(0,0), false);
		 
		 return textura;
	}
	
	public static Textura_suelos[] get_Texturas_suelos() 
	{
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 String sql = "select id,textura from textura_suelos ";
	 db.ejecutarConsulta(sql);
	 
	 if(db.isEmpty())
	  return null;
	 
	 int rowcount = db.getRowCount();
	 Textura_suelos []list = new Textura_suelos[rowcount];
	 for(int i=0; i<rowcount; i++)
	  list[i] = new Textura_suelos(db.getValueAsInteger(i,0), db.getValueAsString(i,1), false);
	 
	 return list;
	}
}
