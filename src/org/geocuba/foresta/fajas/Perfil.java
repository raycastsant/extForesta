package org.geocuba.foresta.fajas;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.Parteaguas;
import org.geocuba.foresta.gestion_datos.PersistentGeometricObject;

import com.iver.cit.gvsig.exceptions.visitors.ProcessVisitorException;

 /**Clase para almacenar los datos de un perfil.
  * Pensada para utilizarse en el método Herrero-Melchanov*/
 public class Perfil  extends PersistentGeometricObject
 {
  private Double ancho;   //Ancho calculado para la faja forestal hidrorreguladora
  //private SubtramoPerfil []listaSubtramos;
  private Integer gid;  
  private String orilla;
  private Parteaguas parteaguas;
  private SubtramoPerfil []listaSubtramos;
  
  public Perfil(int _gid, double _ancho, String _orilla, Parteaguas _parteaguas, SubtramoPerfil []_listaSubtramos, boolean isNew)
  {
   super(isNew);	  
   ancho = _ancho;
  // listaSubtramos = _listaSubtramos;
   gid = _gid;
   orilla = _orilla;
   parteaguas = _parteaguas;
   listaSubtramos = _listaSubtramos;
  }
  
  public void setAncho(double value){
	  ancho = new Double(value);
  }
  
  public Double getAncho(){
	return ancho;
  }
  
//  public void setListaSubtramos(SubtramoPerfil []value){
//	listaSubtramos = value;
//  }
//	  
//  public SubtramoPerfil [] getListaSubtramos(){
//	return listaSubtramos;
//  }
  
  public void setGid(int value){
	   gid = new Integer(value);
  }
		  
  public Integer getGid(){
	   return gid;
 }
  
  public void setOrilla(String value){
	orilla = value;
  }
		  
  public String getOrilla(){
   return orilla;
  }
  
  public void setParteaguas(Parteaguas value){
		parteaguas = value;
   }
			  
  public Parteaguas getParteaguas(){
	   return parteaguas;
  }
  
  public SubtramoPerfil [] getListaSubtramos(){
	   return listaSubtramos;
 }

  @Override
	public boolean delete() 
	{
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 db.ejecutarConsulta("delete from perfiles where gid="+gid);
	     TrazasManager.insertar_Traza("Eliminó el perfil con identificador "+gid);
		 
		 return true;
	}

	@Override
	public boolean save() 
	{
		 boolean isOk = false;
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
			
	  try {
			 
		 if(isNewObject)   //--------INSERTAR--------------------------------------------------------------------
		 {
		  String columnas = "ancho, orilla, the_geom";
		  String values = ancho+",'"+orilla+"',"+getGeometryInfo();
		  
		  if(parteaguas!=null)
		  {
		   columnas += ",parteaguas";
		   values += ","+parteaguas.getGid();
		  } 	
		  
		  String sql = "insert into perfiles("+columnas+") values("+values+")";
		  db.ejecutarConsulta(sql);
		  System.out.println(sql);
		  
		  db.ejecutarConsulta("select max(gid) from perfiles");
		  gid = db.getValueAsInteger(0,0);;
		  TrazasManager.insertar_Traza("Insertó el perfil con identificador "+gid);
		  
		  isOk = true;  
		 }
		 else    //---------------ACTUALIZAR-------------------------------------------------------------
		 {
			   String sql = null;
		       if(ancho != null)
			    sql = "update perfiles set ancho="+ancho;
			   
		       if(orilla != null)
			   {
				if(sql == null)
			     sql = "update perfiles set orilla='"+orilla+"'";
				else
				 sql += ", orilla='"+orilla+"'";
			   }
		       
		       if(parteaguas != null)
			   {
				if(sql == null)
			     sql = "update perfiles set parteaguas="+parteaguas.getGid();
				else
				 sql += ", parteaguas="+parteaguas.getGid();
			   }
		       
		       String geometry = getGeometryInfo();
			   if(geometry != null)
			   {
				if(sql == null)
			     sql = "update perfiles set the_geom="+geometry;
				else
				 sql += ", the_geom="+geometry; 
			   }
			   
			   if(sql != null)
			   {	   
				sql += " where gid="+gid; 
			    db.ejecutarConsulta(sql);
			    TrazasManager.insertar_Traza("Se actualizaron los datos del perfil con identificador "+gid);
			    
			    return true;
			   }
			   else
			    return false;	 
		 }
		 
		 } catch (ProcessVisitorException e) {
				e.printStackTrace();
			}
				 
		 return isOk;
	}
 }
