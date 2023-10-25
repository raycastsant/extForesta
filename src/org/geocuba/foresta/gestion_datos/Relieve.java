package org.geocuba.foresta.gestion_datos;

import javax.swing.JOptionPane;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;

import com.iver.cit.gvsig.exceptions.visitors.ProcessVisitorException;

/**Clase que representa una curva 
 * de nivel*/
public class Relieve extends PersistentGeometricObject {

	private Integer gid;
	private Cuenca cuenca;
	private Double elevacion;
	//private int the_geom;
	
	 public Relieve() 
	 {
	  super(true);
	  gid = null;
	  cuenca = null;
	  elevacion = null;
	 }
	 
	 public Relieve(Integer _gid, Cuenca _cuenca, Double _elevacion, boolean isNew) 
	 {
	  super(isNew);
	  gid = _gid;
	  cuenca = _cuenca;
	  elevacion = _elevacion;
	 }
	
	public Integer getGid() {
		return gid;
	}
	
	public void setElevacion(double value) {
		this.elevacion = value;
	}
	
	public Double getElevacion() {
		return elevacion;
	}
	
	public void setCuenca(Cuenca value)
	{
	 this.cuenca = value;	
	}
	
	public Cuenca getCuenca() {
		return cuenca;
	}
	
	@Override
	public boolean delete() 
	{
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 db.ejecutarConsulta("delete from relieve where gid="+gid);
		 TrazasManager.insertar_Traza("Eliminó la curva de nivel con identificador "+gid);
	     
		 return true;
	}

	@Override
	public boolean save() 
	{
		 boolean isOk = false;
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
			
	  try {
		 String geomInfo = getGeometryInfo();
		 
		 if(isNewObject)   //--------INSERTAR--------------------------------------------------------------------
		 {
//			    if(geomInfo != null)
//				{	
//				    boolean correct = validarGeometriaLineas(db, geomInfo,"relieve");
//				    if(!correct)
//				    {
//				     JOptionPane.showMessageDialog(null, "La línea digitalizada es incorrecta", "Error dibujando elemento", JOptionPane.ERROR_MESSAGE);
//				     return false;
//				    }
//				}  	 
			 
		  String columnas = "elevacion, the_geom";
		  String values = elevacion+","+geomInfo;
		  
		  if(cuenca!=null)
		  {
		   columnas += ",cuenca";
		   values += ","+cuenca.getGid();
		  } 	
		  else
		  {
		   String sql = "select cuencas.gid from cuencas where st_intersects(cuencas.the_geom,"+geomInfo+")";	  
		   db.ejecutarConsulta(sql);
		   System.out.println(sql);
		   
		   if(!db.isEmpty())
		   {
			int gidcuenca = db.getValueAsInteger(0,0);
			columnas += ",cuenca";
			values += ","+gidcuenca;
		   }	   
		   else
		   {
			JOptionPane.showMessageDialog(null, "La geometría no se intersecta con ninguna cuenca");    
			return false;
		   }	
		  }	  
		  
		  String sql = "insert into relieve("+columnas+") values("+values+")";
		  db.ejecutarConsulta(sql);
		  System.out.println(sql);
		  
		  db.ejecutarConsulta("select max(gid) from relieve");
		  gid = db.getValueAsInteger(0,0);
		  TrazasManager.insertar_Traza("Insertó la curva de nivel con identificador "+gid);
		  
		  isOk = true;  
		 }
		 else    //---------------ACTUALIZAR-------------------------------------------------------------
		 {
//			    if(geomInfo != null)
//				{	
//				    boolean correct = validarGeometriaLineas(db, geomInfo,"relieve");
//				    if(!correct)
//				    {
//				     JOptionPane.showMessageDialog(null, "La línea digitalizada es incorrecta", "Error dibujando elemento", JOptionPane.ERROR_MESSAGE);
//				     return false;
//				    }
//				} 
			 
			   String sql = null;
		       if(elevacion != null)
			    sql = "update relieve set elevacion="+elevacion;
			   
			   //String geometry = getGeometryInfo();
			   if(geometry != null)
			   {
				if(sql == null)
			     sql = "update relieve set the_geom="+geomInfo;
				else
				 sql += ", the_geom="+geomInfo; 
			   }
			   
			   if(sql != null)
			   {	   
				sql += " where gid="+gid; 
			    db.ejecutarConsulta(sql);
			    TrazasManager.insertar_Traza("Actualizó la curva de nivel con identificador "+gid);
			    
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
