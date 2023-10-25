package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;

public class EmbalseManager implements IPersistenObjectManager
{
	@Override
	public Embalse Cargar_Objeto_BD(int gid)// throws ReadDriverException 
	{
		 Embalse embalse = null;	
	     //CargadorCapas cargador = new CargadorCapas();
		 //FLyrVect embdata = cargador.cargarTabla("embalses", "embalse", 0, "where gid="+gid, Global.projeccionActiva);
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 //SelectableDataSource datasource = embdata.getSource().getRecordset();
		 
		 db.ejecutarConsulta("select tipo_hidrografia, uso, naturaleza, volumen, nan, nam from embalses where gid="+gid);
		 if(!db.isEmpty())
		 {	 
		     int hidroId = db.getValueAsInteger(0,0);
		     String uso = db.getValueAsString(0,1);
		     String naturaleza = db.getValueAsString(0,2);
		     double volumen = db.getValueAsDouble(0,3);
		     double nan = db.getValueAsDouble(0,4);
		     double nam = db.getValueAsDouble(0,5);
		     
		     db.ejecutarConsulta("select hidrografia.tipo_hidrografia, parteaguas, cuenca, nombre from hidrografia where id="+hidroId);
		     
		     if(!db.isEmpty())
		     {
		      int tipoHidroId = db.getValueAsInteger(0,0);
		      Tipo_hidrografiaManager tipo_hidrografiaManager = new Tipo_hidrografiaManager();
		      Tipo_hidrografia tipoHidro = tipo_hidrografiaManager.Cargar_Objeto_BD(tipoHidroId);   //CargarTipo_Hidrografia(tipoHidroId); //
		      
		      int cuencaId = db.getValueAsInteger(0,2);
		      CuencaManager cuencaManager = new CuencaManager();
		      Cuenca cuenca = cuencaManager.Cargar_Objeto_BD(cuencaId); // CargarCuenca(cuencaId);  //
		      
		      Object parteaguasId = db.getValueAt(0,1);
		      Parteaguas parteaguas = null; //
		      if(parteaguasId!=null)
		      {	  
		       ParteaguasManager parteaguasManager = new ParteaguasManager();	  
		       parteaguas = parteaguasManager.Cargar_Objeto_BD(Integer.parseInt(parteaguasId.toString()));   //CargarParteaguas(Integer.parseInt(parteaguasId.toString()));
		      } 
		       
		      String nombre = db.getValueAsString(0,3); //
		      
		      embalse = new Embalse(gid, uso, naturaleza, volumen, nan, nam, nombre, cuenca, parteaguas, tipoHidro, hidroId,  false);
		     }
		 }    
		     
		     return embalse;
	}
	
	public static Embalse[] obtenerEmbalses() throws ReadDriverException 
	{
		 Embalse []embalses = null;	
	     //CargadorCapas cargador = new CargadorCapas();
		 //FLyrVect embdata = cargador.cargarTabla("embalses", "embalse", 0, "where gid="+gid, Global.projeccionActiva);
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 JDBCAdapter dbaux = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 //SelectableDataSource datasource = embdata.getSource().getRecordset();
		 
		 db.ejecutarConsulta("select tipo_hidrografia, uso, naturaleza, volumen, nan, nam, gid from embalses");
		 if(!db.isEmpty())
		 {	
		  embalses = new Embalse[db.getRowCount()];	 
		  for(int i=0; i<db.getRowCount(); i++)
		  {	  
		     int hidroId = db.getValueAsInteger(i,0);
		     String uso = db.getValueAsString(i,1);
		     String naturaleza = db.getValueAsString(i,2);
		     double volumen = db.getValueAsDouble(i,3);
		     double nan = db.getValueAsDouble(i,4);
		     double nam = db.getValueAsDouble(i,5);
		     int gid = db.getValueAsInteger(i,6);
		     
		     dbaux.ejecutarConsulta("select hidrografia.tipo_hidrografia, parteaguas, cuenca, nombre from hidrografia where id="+hidroId);
		     
		     if(!dbaux.isEmpty())
		     {
		      int tipoHidroId = dbaux.getValueAsInteger(0,0);
		      Tipo_hidrografiaManager tipo_hidrografiaManager = new Tipo_hidrografiaManager();
		      Tipo_hidrografia tipoHidro = tipo_hidrografiaManager.Cargar_Objeto_BD(tipoHidroId);   //CargarTipo_Hidrografia(tipoHidroId); //
		      
		      int cuencaId = dbaux.getValueAsInteger(0,2);
		      CuencaManager cuencaManager = new CuencaManager();
		      Cuenca cuenca = cuencaManager.Cargar_Objeto_BD(cuencaId); // CargarCuenca(cuencaId);  //
		      
		      Object parteaguasId = dbaux.getValueAt(0,1);
		      Parteaguas parteaguas = null; //
		      if(parteaguasId!=null)
		      {	  
		       ParteaguasManager parteaguasManager = new ParteaguasManager();	  
		       parteaguas = parteaguasManager.Cargar_Objeto_BD(Integer.parseInt(parteaguasId.toString()));   //CargarParteaguas(Integer.parseInt(parteaguasId.toString()));
		      } 
		       
		      String nombre = dbaux.getValueAsString(0,3); //
		      
		      embalses[i] = new Embalse(gid, uso, naturaleza, volumen, nan, nam, nombre, cuenca, parteaguas, tipoHidro, hidroId,  false);
		     }
		   }  
		 }    
		     
		     return embalses;
	}
	
	public static boolean eliminarEmbalses() throws ReadDriverException
	{
		 Embalse []embalses = obtenerEmbalses();
		 if(embalses!=null)
		 {		 
			 for(int i=0; i<embalses.length; i++)
				 embalses[i].delete();
		 }	 
		 
		 return embalses!=null;
	}
}
