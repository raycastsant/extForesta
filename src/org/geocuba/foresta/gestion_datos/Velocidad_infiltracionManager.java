package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.IPersistenObjectManager;

public class Velocidad_infiltracionManager implements IPersistenObjectManager 
{
	@Override
	public Velocidad_infiltracion Cargar_Objeto_BD(int id) //throws ReadDriverException 
	{
		 Velocidad_infiltracion velocidad = null;
	     JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 
	     String sql = "select velocidades_infiltracion.uso, velocidades_infiltracion.clase, velocidades_infiltracion.velocidad," +
			"velocidades_infiltracion.estructura, agrupadores_uso.tipo_uso from velocidades_infiltracion inner join " +
			"agrupadores_uso on velocidades_infiltracion.uso=agrupadores_uso.id where velocidades_infiltracion.id="+id;
	     db.ejecutarConsulta(sql);
	     System.out.println(sql);
	     
	     if(!db.isEmpty() && db.getRowCount()==1)
	     {
	      int idUso = db.getValueAsInteger(0,0);
	      String clase = db.getValueAsString(0,1);
	      double veloc = db.getValueAsDouble(0,2);
	      String estructura = db.getValueAsString(0,3);
	      String tipoUso = db.getValueAsString(0,4);
	      Agrupador_uso uso = new Agrupador_uso(idUso, tipoUso, false);
	      
	      velocidad = new Velocidad_infiltracion(id, uso, clase, veloc, estructura, false);
	     } 
			
	     return velocidad;
	}
	
	public Velocidad_infiltracion Cargar_Objeto_BD(String condition) 
	{
	 Velocidad_infiltracion velocidad = null;
     JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 
     String sql = "select velocidades_infiltracion.id, velocidades_infiltracion.uso, velocidades_infiltracion.clase, velocidades_infiltracion.velocidad," +
		"velocidades_infiltracion.estructura, Agrupadores_uso.tipo_uso from velocidades_infiltracion inner join Agrupadores_uso on velocidades_infiltracion.uso=Agrupadores_uso.id "+condition;
     db.ejecutarConsulta(sql);
     System.out.println(sql);
     
     if(!db.isEmpty() && db.getRowCount()==1)
     {
      int id = db.getValueAsInteger(0,0);
      int idUso = db.getValueAsInteger(0,1);
      String clase = db.getValueAsString(0,2);
      double veloc = db.getValueAsDouble(0,3);
      String estructura = db.getValueAsString(0,4);
      String tipoUso = db.getValueAsString(0,5);
      Agrupador_uso uso = new Agrupador_uso(idUso, tipoUso, false);
      
      velocidad = new Velocidad_infiltracion(id, uso, clase, veloc, estructura, false);
     } 
		
     return velocidad;
	}
	
	public static String[] Obtener_Estructuras() 
	{
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 String sql = "select distinct estructura from velocidades_infiltracion";
	 db.ejecutarConsulta(sql);
	 System.out.println(sql);
	 
	 if(db.isEmpty())
	  return null;
	 
	 int rowcount = db.getRowCount();
	 String []list = new String[rowcount];
	 for(int i=0; i<rowcount; i++)
	  list[i] = db.getValueAsString(i,0);
	 
	 return list;
	}
}
