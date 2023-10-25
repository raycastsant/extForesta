package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.IPersistenObjectManager;

public class Intensidad_lluviaManager implements IPersistenObjectManager 
{
	@Override
	public Intensidad_lluvia Cargar_Objeto_BD(int id) //throws ReadDriverException 
	{
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 String sql = "select prob, tiempo, prec from intensidad_lluvia where id="+id;
		 db.ejecutarConsulta(sql);
		 
		 if(db.isEmpty())
		  return null;
		 
		 double prob = db.getValueAsDouble(0,0);
		 double tiempo = db.getValueAsDouble(0,1);
		 double prec = db.getValueAsDouble(0,2);
		 
		 Intensidad_lluvia intensidad = new Intensidad_lluvia(id, prob, tiempo, prec, false);
		 
		 return intensidad;
	}
	
	public static Double[] Obtener_Tiempos() 
	{
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 db.ejecutarConsulta("select distinct tiempo from intensidad_lluvia order by tiempo");
	 
	 Double []tiempos = null; 
	 if(!db.isEmpty())
     {
		 tiempos = new Double[db.getRowCount()];
		 for(int i=0; i<db.getRowCount(); i++)
		  tiempos[i] = db.getValueAsDouble(i,0); 
     }
	 
	 return tiempos;
	}
	
	public static Double[] Obtener_Probabilidades() 
	{
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 db.ejecutarConsulta("select distinct prob from intensidad_lluvia order by prob");
	 
	 Double []probabilidades = null; 
	 if(!db.isEmpty())
     {
		 probabilidades = new Double[db.getRowCount()];
		 for(int i=0; i<db.getRowCount(); i++)
		  probabilidades[i] = db.getValueAsDouble(i,0); 
     }
	 
	 return probabilidades;
	}
	
	public static Double obtenerPrecipitación(double probabilidad, double duracion)
	{
		  JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		  Double prec = new Double(0);
		  String sql = "select prec from intensidad_lluvia where prob = "+probabilidad+" and tiempo="+duracion;
		  db.ejecutarConsulta(sql);
		  if(!db.isEmpty())
		   prec = db.getValueAsDouble(0,0);
		  
		  return prec;
	}
	
//	public Intensidad_lluvia Cargar_Objeto_BD(String condition) 
//	{
//	 Velocidad_infiltracion velocidad = null;
//     JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
//	 
//     String sql = "select velocidades_infiltracion.id, velocidades_infiltracion.uso, velocidades_infiltracion.clase, velocidades_infiltracion.velocidad," +
//		"velocidades_infiltracion.estructura, Agrupador_uso.tipo_uso from velocidades_infiltracion inner join Agrupador_uso on velocidades_infiltracion.uso=Agrupador_uso.id "+condition;
//     db.executeQuery(sql);
//     
//     if(!db.isEmpty() && db.getRowCount()==1)
//     {
//      int id = db.getValueAsInteger(0,0);
//      int idUso = db.getValueAsInteger(0,1);
//      String clase = db.getValueAsString(0,2);
//      double veloc = db.getValueAsDouble(0,3);
//      String estructura = db.getValueAsString(0,4);
//      String tipoUso = db.getValueAsString(0,5);
//      Agrupador_uso uso = new Agrupador_uso(idUso, tipoUso, false);
//      
//      velocidad = new Velocidad_infiltracion(id, uso, clase, veloc, estructura, false);
//     } 
//		
//     return null;
//	}
}
