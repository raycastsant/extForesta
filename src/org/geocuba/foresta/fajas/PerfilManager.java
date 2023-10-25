package org.geocuba.foresta.fajas;

import java.awt.geom.Point2D;

import javax.swing.JOptionPane;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.IPersistenObjectManager;
import org.geocuba.foresta.gestion_datos.Parteaguas;
import org.geocuba.foresta.gestion_datos.ParteaguasManager;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.fmap.edition.UtilFunctions;

public class PerfilManager implements IPersistenObjectManager
{
	@Override
	public Perfil Cargar_Objeto_BD(int gid) //throws ReadDriverException 
	{
		 Perfil perfil = null;		
	     JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	     db.ejecutarConsulta("select ancho, orilla, parteaguas from perfiles where gid="+gid);
	     
	     if(!db.isEmpty())
	     {
	      int id = db.getValueAsInteger(0,2); 	 
	      ParteaguasManager parteaguasManager = new ParteaguasManager();
		  Parteaguas parteaguas = parteaguasManager.Cargar_Objeto_BD(id);
	      
		  double ancho = db.getValueAsDouble(0,0);
		  String orilla = db.getValueAsString(0,1);
		  
	      perfil = new Perfil(gid, ancho, orilla, parteaguas, null, false);
	     } 	 
	     return perfil;
	}
	
	public Perfil [] Cargar_Perfiles(int idParteaguas) throws ReadDriverException 
	{
		 Perfil [] listaPerfiles = null;		
	     JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	     db.ejecutarConsulta("select ancho, orilla, parteaguas, gid from perfiles where perfiles.parteaguas="+idParteaguas);
	     
	     if(!db.isEmpty())
	     {
	      try {
	       int cant = db.getRowCount();	 
	       listaPerfiles = new Perfil[cant];
	       int id = db.getValueAsInteger(0,2); 	 
	       ParteaguasManager parteaguasManager = new ParteaguasManager();
		   Parteaguas parteaguas = parteaguasManager.Cargar_Objeto_BD(id);
		   
		   SubtramosManager sm = new SubtramosManager();
		   
	      for(int i=0; i<cant; i++)
	      {	  
		   double ancho = db.getValueAsDouble(i,0);
		   String orilla = db.getValueAsString(i,1);
		   int gid = db.getValueAsInteger(i,3);
		   
		   SubtramoPerfil []subtramos = sm.Cargar_Subtramos("where perfil="+gid);
		  
		   listaPerfiles[i] = new Perfil(gid, ancho, orilla, parteaguas, subtramos, false);
	      } 
	      
	      } catch (java.lang.NullPointerException e) {
				e.printStackTrace();
				return null;
	     }
	     } 	 
	     return listaPerfiles;
	}
	
	 public static boolean setIdrio_To_f_temp_secctranv(JDBCAdapter db, int value)
	 {
		 if(db == null)
			 db = new JDBCAdapter(ConnectionExt.getConexionActiva());
			 
		 return db.ejecutarConsulta("update f_temp_secctranv set idrio = "+value);
	 }
	 
	 public static Integer[] obtener_f_temp_secctranv_IDS(JDBCAdapter db)
	 {
		 Integer []ids = null;
		 if(db == null)
			 db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 
		 db.ejecutarConsulta("select gid from f_temp_secctranv");
		 if(!db.isEmpty())
		 {
		  ids = new Integer[db.getRowCount()];
		  for(int i=0; i<db.getRowCount(); i++)  
		   ids[i] = db.getValueAsInteger(i,0);
		 }		 
			 
		 return ids;
	 }
	
	 public static String[] obtener_f_temp_secctranv_LADOS(JDBCAdapter db)
	 {
		 String []lados = null;
		 if(db == null)
			 db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 
		 db.ejecutarConsulta("select lado from f_temp_secctranv");
		 if(!db.isEmpty())
		 {
		  lados = new String[db.getRowCount()];
		  for(int i=0; i<db.getRowCount(); i++)  
		   lados[i] = db.getValueAsString(i,0);
		 }		 
			 
		 return lados;
	 }
	 
	 public static boolean recortar_Perfiles(JDBCAdapter db, int f_temp_secctranvId, String pointType)
	 {
		 boolean flag = false;
		 String type = null;
		 
		 if(db == null)
			 db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 
		 db.ejecutarConsulta("select ST_GeometryType(st_intersection(f_temp_secctranv.the_geom, f_temp_paguaslines.the_geom)) " +
			  		"from f_temp_secctranv inner join f_temp_paguaslines on st_intersects(f_temp_secctranv.the_geom, f_temp_paguaslines.the_geom) " +
			  		"where f_temp_secctranv.gid ="+f_temp_secctranvId);
		 
		 if(!db.isEmpty())
		 {	 
			  type = db.getValueAt(0,0).toString();
			  
			  String select = "";
			  if(type != null)
			  {	  
				   if(type.equals("ST_Point"))
					select = "select st_multi(st_makeline(ST_"+pointType+"Point(f_temp_secctranv.the_geom), st_intersection(f_temp_secctranv.the_geom, f_temp_paguaslines.the_geom)))";     
				   else  //Es multipunto
				   {
					int p = getCloseIntersectionPoint(f_temp_secctranvId, pointType, db);
					select = "select st_multi(st_makeline(ST_"+pointType+"Point(f_temp_secctranv.the_geom), ST_PointN(ST_LineFromMultiPoint(st_intersection(f_temp_secctranv.the_geom, f_temp_paguaslines.the_geom)),"+ p +")))";
				   }	
				   
				   flag = db.ejecutarConsulta("update f_temp_secctranv set the_geom = ("+ select +" from f_temp_secctranv inner join f_temp_paguaslines on st_intersects(f_temp_secctranv.the_geom, f_temp_paguaslines.the_geom) " +
					  		"where f_temp_secctranv.gid = "+ f_temp_secctranvId +") where f_temp_secctranv.gid = "+ f_temp_secctranvId);
			  }		
		 }  
			 
		 return flag;
	 }
	 
	    private static int getCloseIntersectionPoint(int idsecc, String point, JDBCAdapter db)
	    {
	     //Coordinate p = null;
	     int index = 0;	
	     int numP = 0;
	     double x1 = 0;
	     //double y1 = 0;
	     double x2 = 0;
	     //double y2 = 0;
	     double dif = 0;
	     
	     if(db == null)
			 db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	     
	     //Busco la x y la y desde donde parte el nuevo segmento
	     db.ejecutarConsulta("select st_x(ST_"+point+"Point(f_temp_secctranv.the_geom)), st_y(ST_"+point+"Point(f_temp_secctranv.the_geom)) " +
	     		"from f_temp_secctranv where  f_temp_secctranv.gid="+idsecc);
	     
	     if(!db.isEmpty())
	     {
	      x1 = Double.parseDouble(db.getValueAt(0,0).toString());	 
	      //y1 = Double.parseDouble(db.getValueAt(0,1).toString());
	     }	 
	     
	     //Selecciono el numero de puntos de intersecciona a analizar
	     db.ejecutarConsulta("select ST_NumPoints(ST_LineFromMultiPoint(st_intersection(f_temp_secctranv.the_geom, f_temp_paguaslines.the_geom))) " +
	      		"from f_temp_secctranv inner join f_temp_paguaslines on st_intersects(f_temp_secctranv.the_geom, f_temp_paguaslines.the_geom) " +
	      		"where f_temp_secctranv.gid = " + idsecc);
	     
	     if(!db.isEmpty())
	      numP = (Integer)db.getValueAt(0,0);	 
	     
	   //analizo cada uno de los puntos de interseccion buscando el mas cercano al punto de nacimiento del segmento
	     for(int i=0; i<numP; i++)
	     {
	      db.ejecutarConsulta("select st_x(ST_PointN(ST_LineFromMultiPoint(st_intersection(f_temp_secctranv.the_geom, f_temp_paguaslines.the_geom)),"+(i+1)+")) " +
	       ", st_y(ST_PointN(ST_LineFromMultiPoint(st_intersection(f_temp_secctranv.the_geom, f_temp_paguaslines.the_geom)),"+(i+1)+"))" +
	       "from f_temp_secctranv inner join f_temp_paguaslines on st_intersects(f_temp_secctranv.the_geom, f_temp_paguaslines.the_geom) " +
	       "where f_temp_secctranv.gid = " + idsecc);	 
	      
	      x2 = Double.parseDouble(db.getValueAt(0,0).toString());
	     // y2 = Double.parseDouble(db.getValueAt(0,1).toString());
	      
	      //Si es el 1er punto de interseccion solo guardo la informacion de proximidad
	      if(i == 0)
	      {
	       dif = x2-x1;
	       if(dif < 0)
	    	dif = dif*-1; 
	       
	       index = 1;
	       continue;
	      }	  
	      
	      //Analizo la informacion de proximidad buscando el mas cercano
	      if(x1 != x2)
	      {
	       double d = x2-x1;
	       if(d < 0)
	        d = d*-1;
	       
	       if(d < dif)
	       {
	    	dif = d;
	    	//p = new Coordinate(x2, y2);
	    	index = i+1;
	       } 	   
	      }  
	     }	
	     
	     return index;
	    }
	    
	     public static boolean eliminar_Perfiles_Fuera(int f_temp_secctranvId, int idParteaguas, JDBCAdapter db)
		 {
	    	 boolean flag = false;
	    	 
	    	 if(db == null)
				 db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	    	 
	    	 String sql = "select st_x(st_startPoint(f_temp_secctranv.the_geom)) as x1, st_y(st_startPoint(f_temp_secctranv.the_geom)) as y1, " +
	              "st_x(st_endPoint(f_temp_secctranv.the_geom)) as x2, st_y(st_endPoint(f_temp_secctranv.the_geom)) as y2, " +
	              " st_length(f_temp_secctranv.the_geom)/2 as radio from f_temp_secctranv where f_temp_secctranv.gid ="+f_temp_secctranvId;
			  db.ejecutarConsulta(sql);
			  System.out.println(sql);
		
			  if(!db.isEmpty())
			  {
				 //Calculo el punto medio 
				  double x1 = db.getValueAsDouble(0,0);
				  double y1 = db.getValueAsDouble(0,1);
				  double x2 = db.getValueAsDouble(0,2);
				  double y2 = db.getValueAsDouble(0,3);
				  double radio = db.getValueAsDouble(0,4);
				  Point2D point2d = UtilFunctions.getPoint(new Point2D.Double(x1,y1), new Point2D.Double(x2,y2), radio);
				  
				 //Elimino la linea cuyo punto medio no intercepta el parteaguas 
				  flag = db.ejecutarConsulta("delete from f_temp_secctranv where gid ="+f_temp_secctranvId+" and (" +
				  		"select st_intersects(parteaguas.the_geom, st_setsrid(st_point("+point2d.getX()+", "+point2d.getY()+"), " +
				  		"st_srid(parteaguas.the_geom))) from parteaguas where gid="+idParteaguas+")=false");
			  }	  
			  else
			   flag = false;	   
				 
			 return flag;
		 }
	     
	     public static boolean insertar_Perfiles(int idParteaguas, JDBCAdapter db)
		 {
	    	 boolean flag = false;
	    	 
	    	 if(db == null)
				 db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	    	 
	    	 db.ejecutarConsulta("delete from perfiles where parteaguas="+idParteaguas);
			 flag = db.ejecutarConsulta("insert into perfiles(parteaguas, orilla, the_geom) " +
		 	         "select idrio, lado, the_geom from f_temp_secctranv order by gid");	   
				 
			 return flag;
		 }
	     
	     public static boolean Eliminar_TablasTemporales(JDBCAdapter db)
		 {
	    	 boolean flag = false;
	    	 
	    	 if(db == null)
				 db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	    	 
	    	 flag = db.ejecutarConsulta("drop table if exists f_temp_paguaslines;" +
		                         "drop table if exists f_temp_secctranv;");  
				 
			 return flag;
		 }
	     
	     public static StructOrillas calcular_Longitud_Media_Ladera(int gidParteaguas, JDBCAdapter db)
		 {
		    	 StructOrillas data = null;
		    	 
		    	 if(db == null)
					 db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		    	 
		    	 db.ejecutarConsulta("select orilla, round(CAST(avg(st_length(perfiles.the_geom)) as numeric),2) as total " +
				            "from perfiles where parteaguas="+ gidParteaguas +" group by orilla Order by orilla");
		    	 
			      if(db.getRowCount() <2)
			   	  {
			   	   JOptionPane.showMessageDialog(null, "No se encontraron perfiles para el parteaguas seleccionado. "+"\n"+
			   			   "El proceso será interrumpido.", "Información", JOptionPane.WARNING_MESSAGE);
			   	  } 
			      else
			      {
			    	  data = new StructOrillas();
			    	  data.der = Double.parseDouble(db.getValueAt(0, 1).toString());
			    	  data.izq = Double.parseDouble(db.getValueAt(1, 1).toString());  
			      }	  
				 
			 return data;
		 }
}
