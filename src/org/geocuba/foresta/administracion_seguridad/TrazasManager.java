package org.geocuba.foresta.administracion_seguridad;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;

	public class TrazasManager 
	{
	 public static void insertar_Traza(String traza)
	 {
		 System.out.println(traza);
//		if(traza!=null)
//		{	
//		 Date d = new Date();	 
//		 SimpleDateFormat sdfs = new SimpleDateFormat("yyyy/MM/dd h:mm a");
//		 
//		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
//		 db.ejecutarConsulta("insert into trazas(usuario,accion,fecha,nombre_apellidos_usuario) " +
//		 		"values('"+UsuarioManager.Usuario().getNombre()+"','"+traza+"','"+sdfs.format(d)+"','"+UsuarioManager.Usuario().getNomb_apell()+"')");
//		} 
	}
	 
	 public static String[] ObtenerTrazas()
	 {
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 db.ejecutarConsulta("select usuario,accion,fecha,nombre_apellidos_usuario from trazas");
		 
		 String []trazas = new String[db.getRowCount()];
		 for(int i=0; i<db.getRowCount(); i++)
		 {
		  String usuario = "'"+db.getValueAsString(i,0)+"'";
		  String accion = ",'"+db.getValueAsString(i,1)+"'";
		  String fecha = ",'"+db.getValueAsString(i,2)+"'";
		  String nombApell = ",'"+db.getValueAsString(i,3)+"'";
		  
		  trazas[i] = usuario+accion+fecha+nombApell;
		 }
		 
		 return trazas;
	}
	 
	 public static void insertar_Trazas(String []trazasRowValues)
	 {
//		if(trazasRowValues!=null)
//		{	
//			 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());	
//			 for(int i=0; i<trazasRowValues.length; i++)
//			 {	 
//			  db.ejecutarConsulta("insert into trazas(usuario,accion,fecha,nombre_apellidos_usuario) " +
//			 		"values("+trazasRowValues[i]+")");
//			 } 
//		} 
	}
	 
	 public static String [] obtenerUsuarios(JDBCAdapter db)
	 {
		 String []lista = null;
		 
		 if(db == null)
			 db = new JDBCAdapter(ConnectionExt.getConexionActiva());
			 
		 db.ejecutarConsulta("Select distinct usuario from trazas");
		 
		 if(!db.isEmpty())
		 {	
			 lista = new String[db.getRowCount()];
			 for(int i=0; i<db.getRowCount(); i++)
			  lista[i] = db.getValueAsString(i,0);	
		 }
		 
		 return lista;
	}
	 
	 public static String obtener_minimaFecha(JDBCAdapter db)
	 {
		 Object fecha = null;
		 
		 if(db == null)
			 db = new JDBCAdapter(ConnectionExt.getConexionActiva());
			 
		 db.ejecutarConsulta("select min(fecha) from trazas");
		 
		 if(!db.isEmpty())
		  fecha = db.getValueAt(0,0);
		 
		 if(fecha!=null)
		  return fecha.toString();
		 else
		  return null;	 
	}
	 
	 public static String getQueryTabla(String where)
	 {
		 return "select usuario, accion as acción, fecha, " +
	 		"nombre_apellidos_usuario from trazas " + where + " order by fecha";
	 }
	 
	 public static String getFilterCondition(String desde, String hasta, boolean withUserFilter, String userValue)
	 {
		 String where = "where fecha>='"+desde+"' and fecha <='"+hasta+"'";
		 
		 if(withUserFilter)
		  where += " and usuario='"+userValue+"'";	 
		 
		 return where;
	 }
	 
	 public static String getQueryReporte(String where)
	 {
		 return "select usuario, accion, fecha, " +
	 		    "nombre_apellidos_usuario from trazas " + where+ " order by usuario, fecha";
	 }
  }	 
