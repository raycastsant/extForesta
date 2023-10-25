package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;

public class RioManager implements IPersistenObjectManager
{
	@Override
	public Rio Cargar_Objeto_BD(int gid) //throws ReadDriverException 
	{
		 Rio rio = null;	
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 db.ejecutarConsulta("select tipo_hidrografia, ancho, orden from rios where gid="+gid);
		 if(!db.isEmpty())
		 { 
		     int hidroId = db.getValueAsInteger(0,0);
		     double ancho = db.getValueAsDouble(0,1);
		     int orden = db.getValueAsInteger(0,2);
		     
		     db.ejecutarConsulta("select hidrografia.tipo_hidrografia, parteaguas, cuenca, nombre from hidrografia where id="+hidroId);
		     
		     if(!db.isEmpty())
		     {
		      int tipoHidroId = db.getValueAsInteger(0,0);
		      Tipo_hidrografiaManager tipo_hidrografiaManager = new Tipo_hidrografiaManager();
		      Tipo_hidrografia tipoHidro = tipo_hidrografiaManager.Cargar_Objeto_BD(tipoHidroId);   //CargarTipo_Hidrografia(tipoHidroId); //
		      
		      int cuencaId = db.getValueAsInteger(0,2);
		      CuencaManager cuencaManager = new CuencaManager();
		      Cuenca cuenca = cuencaManager.Cargar_Objeto_BD(cuencaId);   //CuencaManager.CargarCuenca(cuencaId);  //
		      
		      Object parteaguasId = db.getValueAt(0,1);
		      Parteaguas parteaguas = null; //
		      if(parteaguasId!=null)
		      {	  
		       ParteaguasManager parteaguasManager = new ParteaguasManager();
		       parteaguas = parteaguasManager.Cargar_Objeto_BD(Integer.parseInt(parteaguasId.toString()));    //CargarParteaguas(Integer.parseInt(parteaguasId.toString()));
		      } 
		       
		      String nombre = db.getValueAsString(0,3); //
		      
		      rio = new Rio(gid,hidroId,ancho,orden,nombre,cuenca,parteaguas,tipoHidro,false);
		     }
		}    
		     
		     return rio;
	}	
	
	public static Rio []obtenerRios() throws ReadDriverException 
	{
		 Rio []rios = null;	
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 db.ejecutarConsulta("select tipo_hidrografia, ancho, orden, gid from rios");
		 if(!db.isEmpty())
		 { 
			 rios = new Rio[db.getRowCount()];
			 JDBCAdapter dbaux = new JDBCAdapter(ConnectionExt.getConexionActiva());
			 for(int i=0; i<db.getRowCount(); i++)
			 {	 
			     int hidroId = db.getValueAsInteger(i,0);
			     double ancho = db.getValueAsDouble(i,1);
			     int orden = db.getValueAsInteger(i,2);
			     int gid = db.getValueAsInteger(i,3);
			     
			     dbaux.ejecutarConsulta("select hidrografia.tipo_hidrografia, parteaguas, cuenca, nombre from hidrografia where id="+hidroId);
			     
			     if(!dbaux.isEmpty())
			     {
			      int tipoHidroId = dbaux.getValueAsInteger(0,0);
			      Tipo_hidrografiaManager tipo_hidrografiaManager = new Tipo_hidrografiaManager();
			      Tipo_hidrografia tipoHidro = tipo_hidrografiaManager.Cargar_Objeto_BD(tipoHidroId);   //CargarTipo_Hidrografia(tipoHidroId); //
			      
			      int cuencaId = dbaux.getValueAsInteger(0,2);
			      CuencaManager cuencaManager = new CuencaManager();
			      Cuenca cuenca = cuencaManager.Cargar_Objeto_BD(cuencaId);   //CuencaManager.CargarCuenca(cuencaId);  //
			      
			      Object parteaguasId = dbaux.getValueAt(0,1);
			      Parteaguas parteaguas = null; //
			      if(parteaguasId!=null)
			      {	  
			       ParteaguasManager parteaguasManager = new ParteaguasManager();
			       parteaguas = parteaguasManager.Cargar_Objeto_BD(Integer.parseInt(parteaguasId.toString()));    //CargarParteaguas(Integer.parseInt(parteaguasId.toString()));
			      } 
			       
			      String nombre = dbaux.getValueAsString(0,3); //
			      
			      rios[i] = new Rio(gid,hidroId,ancho,orden,nombre,cuenca,parteaguas,tipoHidro,false);
			     }
			 }    
		}    
		     
		     return rios;
	}
	
	public static boolean eliminarRios() throws ReadDriverException
	{
		 Rio []rios = obtenerRios();
		 if(rios!=null)
		 {
			 for(int i=0; i<rios.length; i++)
				 rios[i].delete();
		 }	 
		 
		 return rios!=null;
	}
}
