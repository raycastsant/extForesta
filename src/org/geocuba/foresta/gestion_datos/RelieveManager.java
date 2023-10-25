package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;

public class RelieveManager implements IPersistenObjectManager
{
	@Override
	public Relieve Cargar_Objeto_BD(int gid) //throws ReadDriverException 
	{
		 Relieve relieve = null;		
	     JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	     db.ejecutarConsulta("select cuenca, elevacion from relieve where gid="+gid);
	     
	     if(!db.isEmpty())
	     {
	      int cuencaId = db.getValueAsInteger(0,0); 	 
	      CuencaManager cuencaManager = new CuencaManager();
		  Cuenca cuenca = cuencaManager.Cargar_Objeto_BD(cuencaId);
	      
	      relieve = new Relieve(gid, cuenca, db.getValueAsDouble(0,1), false);
	     } 	 
	     return relieve;
	}
	
	public static Relieve []obtenerCurvasDeNivel() throws ReadDriverException 
	{
		 Relieve []curvas = null;		
	     JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	     db.ejecutarConsulta("select cuenca, elevacion, gid from relieve");
	     
	     if(!db.isEmpty())
	     {
	       curvas = new Relieve[db.getRowCount()]; 	 
	       for(int i=0; i<db.getRowCount(); i++)
	       {	   
		      int cuencaId = db.getValueAsInteger(i,0); 	 
		      CuencaManager cuencaManager = new CuencaManager();
			  Cuenca cuenca = cuencaManager.Cargar_Objeto_BD(cuencaId);
			  double elevacion = db.getValueAsDouble(i,1);
			  int gid = db.getValueAsInteger(i,2); 
		      
			  curvas[i] = new Relieve(gid, cuenca, elevacion, false);
	       }   
	     } 	 
	     return curvas;
	}
	
	public static boolean eliminarCurvasDeNivel() throws ReadDriverException
	{
//	 Relieve []curvas = obtenerCurvasDeNivel();
//	 if(curvas!=null)
//	 {
//		 for(int i=0; i<curvas.length; i++)
//			 curvas[i].delete();
//	 }	 
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 db.ejecutarConsulta("delete from relieve");
	     
	 return true;
	}
	
}
