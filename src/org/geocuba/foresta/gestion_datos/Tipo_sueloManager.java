package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.IPersistenObjectManager;

public class Tipo_sueloManager implements IPersistenObjectManager
{
	@Override
	public Tipo_suelo Cargar_Objeto_BD(int id) //throws ReadDriverException 
	{
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 String sql = "select clave, tipo from tipo_suelo where id="+id;
		 db.ejecutarConsulta(sql);
		 
		 if(db.isEmpty())
		  return null;
		 
		 Tipo_suelo tiposuelo = new Tipo_suelo(id, db.getValueAsString(0,0), db.getValueAsString(0,1), false);
		 
		 return tiposuelo;
	}
	
	public static Tipo_suelo[] get_Tipos_suelos() 
	{
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 String sql = "select id, clave, tipo from tipo_suelo ";
	 db.ejecutarConsulta(sql);
	 System.out.println(sql);
	 
	 if(db.isEmpty())
	  return null;
	 
	 int rowcount = db.getRowCount();
	 Tipo_suelo []list = new Tipo_suelo[rowcount];
	 for(int i=0; i<rowcount; i++)
	  list[i] = new Tipo_suelo(db.getValueAsInteger(i,0), db.getValueAsString(i,1), db.getValueAsString(i,2), false);
	 
	 return list;
	}
	
	public static Tipo_suelo[] get_Tipos_suelos(String from_where_condition) 
	{
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 String sql = "select id, clave, tipo "+from_where_condition;
	 db.ejecutarConsulta(sql);
	 System.out.println(sql);
	 
	 if(db.isEmpty())
	  return null;
	 
	 int rowcount = db.getRowCount();
	 Tipo_suelo []list = new Tipo_suelo[rowcount];
	 for(int i=0; i<rowcount; i++)
	  list[i] = new Tipo_suelo(db.getValueAsInteger(i,0), db.getValueAsString(i,1), db.getValueAsString(i,2), false);
	 
	 return list;
	}
}
