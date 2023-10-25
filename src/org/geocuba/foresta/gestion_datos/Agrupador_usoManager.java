package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.IPersistenObjectManager;

public class Agrupador_usoManager implements IPersistenObjectManager 
{
	@Override
	public Agrupador_uso Cargar_Objeto_BD(int id) //throws ReadDriverException 
	{
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 String sql = "select tipo_uso from Agrupadores_uso where id="+id;
		 db.ejecutarConsulta(sql);
		 
		 if(db.isEmpty())
		  return null;
		 
		 Agrupador_uso uso = new Agrupador_uso(id, db.getValueAsString(0,0), false);
		 
		 return uso;
	}
	
	public static Agrupador_uso[] obtener_Usos_suelos() 
	{
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 String sql = "select id, tipo_uso from Agrupadores_uso";
	 db.ejecutarConsulta(sql);
	 
	 if(db.isEmpty())
	  return null;
	 
	 int rowcount = db.getRowCount();
	 Agrupador_uso []list = new Agrupador_uso[rowcount];
	 for(int i=0; i<rowcount; i++)
	  list[i] = new Agrupador_uso(db.getValueAsInteger(i,0), db.getValueAsString(i,1), false);
	 
	 return list;
	}
}
