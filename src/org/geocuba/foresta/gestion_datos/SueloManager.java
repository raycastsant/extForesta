package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;

public class SueloManager implements IPersistenObjectManager 
{
	@Override
	public Suelo Cargar_Objeto_BD(int gid) //throws ReadDriverException 
	{
		 Suelo suelo = null;
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
//		 db.executeQuery("select suelos.textura, textura_suelos.textura, suelos.tipo, tipo_suelo.tipo, suelos.uso, " +
//		 		"uso_suelos.tipo_uso, erosion, materiaorganica, profundidadefectiva, ph, " +
//		 		"pendiente, gravas, velocidad_infiltracion as velocidad_infiltración, piedras, rocas, estructura, tipo_suelo.clave " +
//		  		"from ((suelos left join textura_suelos on suelos.textura=textura_suelos.id) left join tipo_suelo on " +
//		  		"suelos.tipo=tipo_suelo.id) left join uso_suelos on suelos.uso=uso_suelos.id where suelos.gid="+gid);
		 
		 db.ejecutarConsulta("select _suelos.textura, textura_suelos.textura, _suelos.tipo, tipo_suelo.tipo, erosion, " +
		 		    "materiaorganica, profundidadefectiva, ph, " +
			 		"pendiente, gravas, piedras, rocas, estructura, tipo_suelo.clave " +
			  		"from (_suelos left join textura_suelos on _suelos.textura=textura_suelos.id) left join tipo_suelo on " +
			  		"_suelos.tipo=tipo_suelo.id where _suelos.gid="+gid);
		 
		 if(!db.isEmpty())
		 {
		  int idtextura = db.getValueAsInteger(0,0);
		  String textura = db.getValueAsString(0,1);
		  Textura_suelos Textura = new Textura_suelos(idtextura, textura, false);
		  int idtipo = db.getValueAsInteger(0,2);
		  String tiposuelo = db.getValueAsString(0,3);
		  String clave = db.getValueAsString(0,13);
		  Tipo_suelo TipoSuelo = new Tipo_suelo(idtipo, clave, tiposuelo, false);
//		  Uso_suelo uso = null;
//		  if(db.getValueAt(0,4) != null)
//		  {	  
//		   int idUso = db.getValueAsInteger(0,4);
//		   String tipoUso = db.getValueAsString(0,5);
//		   uso = new Uso_suelo(idUso, tipoUso, false);
//		  } 
		  String erosion = db.getValueAsString(0,4);
		  int matorg = db.getValueAsInteger(0,5);
		  double profefect = db.getValueAsDouble(0,6);
		  double ph = db.getValueAsDouble(0,7);
		  double pendiente = db.getValueAsDouble(0,8);
		  double gravas = db.getValueAsDouble(0,9);
//		  double velinf = db.getValueAsDouble(0,10);
		  double piedras = db.getValueAsDouble(0,10);
		  double rocas = db.getValueAsDouble(0,11);
		  String estructura = db.getValueAsString(0,12);
		  
		  suelo = new Suelo(gid, Textura, TipoSuelo, erosion, matorg, profefect, estructura, ph, pendiente, 
				  gravas, piedras, rocas, false);
		 }
		 
		 return suelo;
	}
	
	public static Suelo[] obtenerSuelos() throws ReadDriverException 
	{
		 Suelo []suelos = null;
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 db.ejecutarConsulta("select _suelos.textura, textura_suelos.textura, _suelos.tipo, tipo_suelo.tipo, erosion, " +
		 		    "materiaorganica, profundidadefectiva, ph, " +
			 		"pendiente, gravas, piedras, rocas, estructura, tipo_suelo.clave, _suelos.gid " +
			  		"from (_suelos left join textura_suelos on _suelos.textura=textura_suelos.id) left join tipo_suelo on " +
			  		"_suelos.tipo=tipo_suelo.id");

		 if(!db.isEmpty())
		 {
		  suelos = new Suelo[db.getRowCount()];
		  for(int i=0; i<db.getRowCount(); i++)
		  {	  
		   int idtextura = db.getValueAsInteger(i,0);
		   String textura = db.getValueAsString(i,1);
		   Textura_suelos Textura = new Textura_suelos(idtextura, textura, false);
		   int idtipo = db.getValueAsInteger(i,2);
		   String tiposuelo = db.getValueAsString(i,3);
		   String clave = db.getValueAsString(i,13);
		   Tipo_suelo TipoSuelo = new Tipo_suelo(idtipo, clave, tiposuelo, false);
		   String erosion = db.getValueAsString(i,4);
		   int matorg = db.getValueAsInteger(i,5);
		   double profefect = db.getValueAsDouble(i,6);
		   double ph = db.getValueAsDouble(i,7);
		   double pendiente = db.getValueAsDouble(i,8);
		   double gravas = db.getValueAsDouble(i,9);
		   double piedras = db.getValueAsDouble(i,10);
		   double rocas = db.getValueAsDouble(i,11);
		   String estructura = db.getValueAsString(i,12);
		   int gid = db.getValueAsInteger(i,14);
		  
		   Suelo suelo = new Suelo(gid, Textura, TipoSuelo, erosion, matorg, profefect, estructura, ph, pendiente, 
				  gravas, piedras, rocas, false);
		  
		   suelos[i] = suelo;
		 }
	   } 
	   
		 return suelos;
	}
	
	public static boolean eliminarSuelos() throws ReadDriverException
	{
	 Suelo []suelos = obtenerSuelos();
	 if(suelos!=null)
	 {	 
		 for(int i=0; i<suelos.length; i++)
			 suelos[i].delete();
	 } 
	 
	 return suelos!=null;
	}
}
