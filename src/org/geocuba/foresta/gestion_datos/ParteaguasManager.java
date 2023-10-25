package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;

 public class ParteaguasManager implements IPersistenObjectManager
 {
	@Override
	public Parteaguas Cargar_Objeto_BD(int gid)// throws ReadDriverException 
	{ 
		 Parteaguas parteaguas = null;
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	     db.ejecutarConsulta("select cuenca, descripcion from Parteaguas where gid="+gid);
	     if(!db.isEmpty())
	     {	 
			     int cuencaId = db.getValueAsInteger(0,0);
			     CuencaManager cuencaManager = new CuencaManager();
			     Cuenca cuenca = cuencaManager.Cargar_Objeto_BD(cuencaId);   //CuencaManager.CargarCuenca(cuencaId);  //
			     String desc = db.getValueAsString(0,1);
			     
			     parteaguas = new Parteaguas(gid, cuenca, desc, false);
	     }	     
     return parteaguas;
	}
	
	public static Parteaguas[] obtenerParteaguas() throws ReadDriverException 
	{
		 Parteaguas []parteaguas = null;
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	     db.ejecutarConsulta("select cuenca, descripcion, gid from Parteaguas");
	     if(!db.isEmpty())
	     {	 
	    	 parteaguas = new Parteaguas[db.getRowCount()];
	    	 for(int i=0; i<db.getRowCount(); i++)
	    	 {	 
			     int cuencaId = db.getValueAsInteger(i,0);
			     CuencaManager cuencaManager = new CuencaManager();
			     Cuenca cuenca = cuencaManager.Cargar_Objeto_BD(cuencaId);   //CuencaManager.CargarCuenca(cuencaId);  //
			     String desc = db.getValueAsString(i,1);
			     int gid = db.getValueAsInteger(i,2);
			     
			     parteaguas[i] = new Parteaguas(gid, cuenca, desc, false);
	    	 }    
	     }	     
	     
     return parteaguas;
	}
	
	public static Parteaguas[] obtenerParteaguas(String from_where)// throws ReadDriverException 
	{ 
		 Parteaguas []parteaguas = null;
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	     db.ejecutarConsulta("select parteaguas.cuenca, parteaguas.descripcion, parteaguas.gid "+from_where);
	     if(!db.isEmpty())
	     {	 
	    	 parteaguas = new Parteaguas[db.getRowCount()];
	    	 for(int i=0; i<db.getRowCount(); i++)
	    	 {	 
			     int cuencaId = db.getValueAsInteger(i,0);
			     CuencaManager cuencaManager = new CuencaManager();
			     Cuenca cuenca = cuencaManager.Cargar_Objeto_BD(cuencaId);   //CuencaManager.CargarCuenca(cuencaId);  //
			     String desc = db.getValueAsString(i,1);
			     int gid = db.getValueAsInteger(i,2);
			     
			     parteaguas[i] = new Parteaguas(gid, cuenca, desc, false);
	    	 }    
	     }	     
	     
     return parteaguas;
	}
	
	public static boolean eliminarParteaguas() throws ReadDriverException
	{
	 Parteaguas []parteaguas = obtenerParteaguas();
	 if(parteaguas!=null)
	 {
		 for(int i=0; i<parteaguas.length; i++)
			 parteaguas[i].delete();
	 }
	 
	 return parteaguas!=null;
	}
	
}
