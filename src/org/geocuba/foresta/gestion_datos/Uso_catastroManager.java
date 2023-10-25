package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.IPersistenObjectManager;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;

public class Uso_catastroManager implements IPersistenObjectManager  
{
	@Override
	public Uso_catastro Cargar_Objeto_BD(int id)// throws ReadDriverException 
	{
		 Uso_catastro uso_catastro = null;
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 db.ejecutarConsulta("select uso_suelo, descripcion_uso, codigo_uso, tipo_superficie, tipo_uso, esp_uso " +
		 		         "from usos_catastro where id="+id);
		 
		 if(!db.isEmpty())
		 {
		  int idusosuelo = db.getValueAsInteger(0,0);
		  Agrupador_usoManager ausoman = new Agrupador_usoManager();
		  Agrupador_uso aguso = ausoman.Cargar_Objeto_BD(idusosuelo);
		  
		  String descuso = db.getValueAsString(0,1);
		  int coduso = db.getValueAsInteger(0,2);
		  int tsup = db.getValueAsInteger(0,3);
		  int tuso = db.getValueAsInteger(0,4);
		  int espuso = db.getValueAsInteger(0,5);
		  
		  uso_catastro = new Uso_catastro(id, aguso, descuso, coduso, tsup, tuso, espuso, false);
		 }
		 
		 return uso_catastro;
	}
	
	public static Uso_catastro [] Obtener_usos_catastro(String condicion) throws ReadDriverException  
	{
		 Uso_catastro []usos_catastro = null;
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 String sql = "select uso_suelo, descripcion_uso, codigo_uso, tipo_superficie, tipo_uso, esp_uso, id " +
	         "from usos_catastro "+condicion;
		 db.ejecutarConsulta(sql);
		 
		 if(!db.isEmpty())
		 {
		  usos_catastro = new Uso_catastro[db.getRowCount()];	 
		  for(int i=0; i<db.getRowCount(); i++)
		  {	  
		   int idusosuelo = db.getValueAsInteger(i,0);
		   Agrupador_usoManager ausoman = new Agrupador_usoManager();
		   Agrupador_uso aguso = ausoman.Cargar_Objeto_BD(idusosuelo);
		  
		   String descuso = db.getValueAsString(i,1);
		   int coduso = db.getValueAsInteger(i,2);
		   int tsup = db.getValueAsInteger(i,3);
		   int tuso = db.getValueAsInteger(i,4);
		   int espuso = db.getValueAsInteger(i,5);
		   int id = db.getValueAsInteger(i,6);
		  
		   usos_catastro[i] = new Uso_catastro(id, aguso, descuso, coduso, tsup, tuso, espuso, false);
		  } 
		 }
		 
		 return usos_catastro;
	}
	
	public static Uso_catastro [] Obtener_usos_catastro() throws ReadDriverException  
	{
		return Obtener_usos_catastro("");
	}
}
