package org.geocuba.foresta.gestion_datos;

import javax.swing.JOptionPane;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import com.iver.cit.gvsig.exceptions.visitors.ProcessVisitorException;

public class Municipio extends PersistentGeometricObject {
	
	private Integer gid;
	private String nombre;
	private Provincia provincia;
	
	 public Municipio()
	 {
	  super(true);
	 }
	 
	 public Municipio(int _gid, String _nombre, Provincia _provincia, boolean isNew)
	 {
	  super(isNew);
	  this.gid = _gid;
	  this.nombre = _nombre;
	  this.provincia = _provincia;
	 }
	
	public void setGid(int value) {
		this.gid = value;
	}
	
	public Integer getGid() {
		return gid;
	}
	
	public void setNombre(String value) {
		this.nombre = value;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setProvincia(Provincia value) {
		this.provincia = value;
	}
	
	public Provincia getProvincia() {
		return provincia;
	}
	
	@Override
	public boolean delete() 
	{
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 db.ejecutarConsulta("delete from municipios_cuencas where municipio="+gid);
		 db.ejecutarConsulta("delete from Municipios where gid="+gid);
		 TrazasManager.insertar_Traza("Eliminó el Municipio con identificador "+gid);
	     
		 return true;
	}

	@Override
	public boolean save() 
	{
		 boolean isOk = false;
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 try {
			 
		 if(isNewObject)   //-----------INSERTO-----------------------------------------------------------------
		 {
		  String geomInfo = getGeometryInfo();	 
		  String columnas = "provincia, nombre, the_geom";
		  String values = provincia.getId()+",'"+nombre+"',"+geomInfo;
		
		  String sql = "insert into municipios("+columnas+") values("+values+")";
		  db.ejecutarConsulta(sql);
		  
		  db.ejecutarConsulta("select max(gid) from municipios");
		  gid = db.getValueAsInteger(0,0);
		  
//		    boolean correct = validarGeometria(db, geomInfo, gid, "municipios", getType());
//		    if(!correct)
//		    {
//		     JOptionPane.showMessageDialog(null, "El polígono digitalizado es incorrecto", "Error dibujando elemento", JOptionPane.ERROR_MESSAGE);
//		     return false;
//		    }
		  
		 /*Actualizo los cuencas que se intersecten*/ 	  	   
		  db.ejecutarConsulta("select cuencas.gid from municipios inner join cuencas on st_intersects(" +
		  		"municipios.the_geom, cuencas.the_geom) where municipios.gid="+gid);
		  if(!db.isEmpty())
		  {
		   JDBCAdapter dbaux = new JDBCAdapter(ConnectionExt.getConexionActiva());
		   for(int i=0; i<db.getRowCount(); i++)
			dbaux.ejecutarConsulta("insert into municipios_cuencas(municipio,cuenca) values("+gid+","+db.getValueAt(i,0).toString()+")");   
		  }	
		 
		  TrazasManager.insertar_Traza("Insertó el municipio con identificador "+gid);
		  
		  
		  isOk = true;
		 }
		 else    //--------------MODIFICO--------------------------------------------------------------
		 {
			   String sql = null;
		       if(nombre != null)
			    sql = "update municipios set nombre='"+nombre+"'";
			   
		       if(provincia != null)
			   {
				if(sql == null)
			     sql = "update municipios set provincia="+provincia.getId();
				else
				 sql += ", provincia="+provincia.getId(); 
			   }
		       
			   String geometry = getGeometryInfo();
			   if(geometry != null)
			   {
				if(sql == null)
			     sql = "update municipios set the_geom="+geometry;
				else
				 sql += ", the_geom="+geometry; 
			   }
			   
			   if(sql != null)
			   {	   
				sql += " where gid="+gid;
				db.ejecutarConsulta(sql);
				
//				if(geometry !=null)
//				{	
//					boolean correct = validarGeometria(db, geometry, gid, "municipios", getType());
//				    if(!correct)
//				    {
//				     JOptionPane.showMessageDialog(null, "El polígono digitalizado es incorrecto", "Error dibujando elemento", JOptionPane.ERROR_MESSAGE);
//				     return false;
//				    }
//				}    
			    
				TrazasManager.insertar_Traza("Actualizó el municipio con identificador "+gid);
				
			    return true;
			   }
			   else
			    return false;	   
		   }
		 
		    } catch (ProcessVisitorException e) {
			  e.printStackTrace();
			  isOk = false;
		    }
				 
		 return isOk;
	}
}
