package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.IPersistenObjectManager;

public class Tipo_hidrografiaManager implements IPersistenObjectManager 
{
	@Override
	public Tipo_hidrografia Cargar_Objeto_BD(int id)// throws ReadDriverException 
	{
		 Tipo_hidrografia tipo_hidrografia = null;	
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 db.ejecutarConsulta("select id, codigo, descripcion, ancho_faja, tipo_elemento from tipo_hidrografia where id="+id);
		 
		 if(!db.isEmpty())
		 {
		  tipo_hidrografia = new Tipo_hidrografia(db.getValueAsInteger(0,0), 
				                                  db.getValueAsString(0,1),
				                                  db.getValueAsString(0,2),
				                                  db.getValueAsDouble(0,3),
				                                  db.getValueAsString(0,4), false);	 
		 }	 
		 return tipo_hidrografia;
	}
	
	public static Tipo_hidrografia[] listTipo_hidrografiaByQuery(String condition, String orderBy) {
	 return null;
	}
	
	public Tipo_hidrografia[] get_Tipos_hidrografia(String whereCondition) 
	{
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 String sql = "select id,codigo,descripcion,ancho_faja,tipo_elemento from tipo_hidrografia "+whereCondition;
	 db.ejecutarConsulta(sql);
	 System.out.println(sql);
	 
	 if(db.isEmpty())
	  return null;
	 
	 int rowcount = db.getRowCount();
	 Tipo_hidrografia []list = new Tipo_hidrografia[rowcount];
	 for(int i=0; i<rowcount; i++)
	 {
	  list[i] = new Tipo_hidrografia(Integer.parseInt(db.getValueAt(i,0).toString()), db.getValueAt(i,1).toString(), db.getValueAt(i,2).toString(), 
			              db.getValueAsDouble(i,3), db.getValueAt(i,4).toString(), false);
	 }
	 
	 return list;
	}
}
