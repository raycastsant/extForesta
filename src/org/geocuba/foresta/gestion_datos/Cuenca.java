package org.geocuba.foresta.gestion_datos;

import javax.swing.JOptionPane;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import com.iver.cit.gvsig.exceptions.visitors.ProcessVisitorException;

public class Cuenca extends PersistentGeometricObject {
	
	private Integer gid;
	private String nombre;
	
	public Cuenca(int _gid, String _nombre, boolean _isNewObject) 
	{
     super(_isNewObject);		
	 gid = _gid;
	 nombre = _nombre;
	}
	
	public Cuenca() 
	{
     super(true);		
	 gid = null;
	 nombre = null;
	 //listaMunicipios = null;
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
	
	@Override
	public boolean delete() 
	{
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 db.ejecutarConsulta("delete from municipios_cuencas where cuenca="+gid);
		 db.ejecutarConsulta("delete from cuencas where gid="+gid);
		 TrazasManager.insertar_Traza("Eliminó la cuenca de interés nacional con identificador "+gid);
	     
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
	  String columnas = "nombre, the_geom";
	  String values;
	  
	    String geomInfo = getGeometryInfo();
		values = "'"+nombre+"',"+geomInfo;
	
	  String sql = "insert into cuencas("+columnas+") values("+values+")";
	  db.ejecutarConsulta(sql);
	  System.out.println(sql);
	  
      /*Actualizo los municipios que se intersecten*/ 	  	   
	  db.ejecutarConsulta("select max(gid) from cuencas");
	  gid = db.getValueAsInteger(0,0);
	  
//	    boolean correct = validarGeometria(db, geomInfo, gid, "cuencas", getType());
//	    if(!correct)
//	    {
//	     JOptionPane.showMessageDialog(null, "El polígono digitalizado es incorrecto", "Error dibujando elemento", JOptionPane.ERROR_MESSAGE);
//	     return false;
//	    }
	    
	  TrazasManager.insertar_Traza("Insertó la cuenca de interés nacional con identificador "+gid);
	  
	  db.ejecutarConsulta("select municipios.gid from municipios inner join cuencas on st_intersects(" +
	  		"municipios.the_geom, cuencas.the_geom) where cuencas.gid="+gid);
	  if(!db.isEmpty())
	  {
	   JDBCAdapter dbaux = new JDBCAdapter(ConnectionExt.getConexionActiva());
	   for(int i=0; i<db.getRowCount(); i++)
		dbaux.ejecutarConsulta("insert into municipios_cuencas(cuenca,municipio) values("+gid+","+db.getValueAt(i,0).toString()+")");   
	  }	  
	  
	  isOk = true;
	  
	 }
	 else    //--------------MODIFICO--------------------------------------------------------------
	 {
		   String sql = null;
	       if(nombre != null)
		    sql = "update cuencas set nombre='"+nombre+"'";
		   
		   String geometry = getGeometryInfo();
		   if(geometry != null)
		   {
			if(sql == null)
		     sql = "update cuencas set the_geom="+geometry;
			else
			 sql += ", the_geom="+geometry; 
		   }
		   
		   if(sql != null)
		   {	   
			sql += " where gid="+gid;
			db.ejecutarConsulta(sql);
			
//			if(geometry !=null)
//			{
//			    boolean correct = validarGeometria(db, geometry, gid, "cuencas", getType());
//			    if(!correct)
//			    {
//			     JOptionPane.showMessageDialog(null, "El polígono digitalizado es incorrecto", "Error dibujando elemento", JOptionPane.ERROR_MESSAGE);
//			     return false;
//			    }
//			}    
			    
			TrazasManager.insertar_Traza("Actualizó la cuenca de interés nacional con identificador "+gid);
			
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
