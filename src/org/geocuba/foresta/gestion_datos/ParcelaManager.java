package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;

 public class ParcelaManager implements IPersistenObjectManager 
 {
	@Override
	public Parcela Cargar_Objeto_BD(int gid) //throws ReadDriverException 
	{
		 Parcela parcela = null;		
	     JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	     db.ejecutarConsulta("select municipio, uso, nombre, poseedor, zc from parcelas where gid="+gid);
	     
	     if(!db.isEmpty())
	     {
	      int idmun = db.getValueAsInteger(0,0);
	      MunicipioManager munman = new MunicipioManager();
	      Municipio municipio = munman.Cargar_Objeto_BD(idmun);
	      
//	      Object value = db.getValueAt(0,1);
//	      Suelo suelo = null;
//	      if(value!=null)
//	      {	  
//	       int idsuelo = db.getValueAsInteger(0,1);
//	       SueloManager sueloman = new SueloManager();
//	       suelo = sueloman.Cargar_Objeto_BD(idsuelo);
//	      } 
	      
	      Object value = db.getValueAt(0,1);
	      Uso_catastro usoCat = null;
	      if(value!=null)
	      {
	       int idusocatastro = Integer.parseInt(value.toString()); //db.getValueAsInteger(0,2);
	       Uso_catastroManager usocatman = new Uso_catastroManager();
	       usoCat = usocatman.Cargar_Objeto_BD(idusocatastro);
	      } 
	      
	      String nombre = db.getValueAsString(0,2);
	      String poseedor = db.getValueAsString(0,3);
	      int zc = db.getValueAsInteger(0,4);
	      
	      parcela = new Parcela(gid, municipio, poseedor, usoCat, nombre, zc, false);
	     } 	 
	     return parcela;
	}
	 
	public static Parcela[] obtener_Parcelas() throws ReadDriverException 
	{
		 Parcela []parcelas = null;		
	     JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	     db.ejecutarConsulta("select municipio, uso, nombre, poseedor, zc, gid from parcelas ");
	     
	     if(!db.isEmpty())
	     {
	       parcelas = new Parcela[db.getRowCount()];	 
	       for(int i=0; i<db.getRowCount(); i++)
	       {	    
		      int idmun = db.getValueAsInteger(i,0);
		      MunicipioManager munman = new MunicipioManager();
		      Municipio municipio = munman.Cargar_Objeto_BD(idmun);
		      
		      Object value = db.getValueAt(i,1);
		      Uso_catastro usoCat = null;
		      if(value!=null)
		      {
		       int idusocatastro = Integer.parseInt(value.toString()); //db.getValueAsInteger(0,2);
		       Uso_catastroManager usocatman = new Uso_catastroManager();
		       usoCat = usocatman.Cargar_Objeto_BD(idusocatastro);
		      } 
		      
		      String nombre = db.getValueAsString(i,2);
		      String poseedor = db.getValueAsString(i,3);
		      int zc = db.getValueAsInteger(i,4);
		      int gid = db.getValueAsInteger(i,5);
		      
		      parcelas[i] = new Parcela(gid, municipio, poseedor, usoCat, nombre, zc, false);
	       }   
	     } 	 
	     
	     return parcelas;
	}
	
	public static boolean eliminarParcelas() throws ReadDriverException
	{
	 Parcela []parcelas = obtener_Parcelas();
	 if(parcelas!=null)
	 {
		 for(int i=0; i<parcelas.length; i++)
	      parcelas[i].delete();
	 }	 
	 
	 return parcelas!=null;
	}
 }
