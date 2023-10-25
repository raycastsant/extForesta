package org.geocuba.foresta.gestion_datos;

import javax.swing.JOptionPane;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;

import com.iver.cit.gvsig.exceptions.visitors.ProcessVisitorException;

public class Faja_real extends PersistentGeometricObject {
	
	private Integer gid;
	private Faja faja;
	//private int the_geom;

	public Faja_real(int _gid, Faja _faja, boolean isNew) 
	{
		super(isNew);
		this.gid = _gid;
		this.faja = _faja;
	} 
	
	public Faja_real() 
	{
		super(true);
		this.gid = null;
		this.faja = null;
	}
	
	public void setGid(int value) {
		this.gid = value;
	}
	
	public Integer getGid() {
		return gid;
	}
	
	public void setFaja(Faja value) {
     this.faja = value;
	}
	
	public Faja getFaja() {
		return faja;
	}

	@Override
	public boolean delete() 
	{
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 db.ejecutarConsulta("delete from fajas_real where gid="+gid);
		 TrazasManager.insertar_Traza("Eliminó la Faja real con identificador "+gid);
	     
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
		  String columnas = "faja, the_geom";
		  String values = faja.getGid()+","+geomInfo;
		
		  String sql = "insert into fajas_real("+columnas+") values("+values+")";
		  db.ejecutarConsulta(sql);
		  
	      /*Actualizo los municipios que se intersecten*/ 	  	   
		  db.ejecutarConsulta("select max(gid) from fajas_real");
		  gid = db.getValueAsInteger(0,0);
		  
//		    boolean correct = validarGeometria(db, geomInfo, gid, "fajas_real", getType());
//		    if(!correct)
//		    {
//		     JOptionPane.showMessageDialog(null, "El polígono digitalizado es incorrecto", "Error dibujando elemento", JOptionPane.ERROR_MESSAGE);
//		     return false;
//		    }
		    
		  TrazasManager.insertar_Traza("Insertó la Faja real con identificador "+gid);
		 }
		 else    //--------------MODIFICO--------------------------------------------------------------
		 {
			   String sql = null;
			   String geometry = getGeometryInfo();
			   if(geometry != null)
			    sql = "update fajas_real set the_geom="+geometry;
			   
			   if(sql != null)
			   {	   
				sql += " where gid="+gid;
				db.ejecutarConsulta(sql);
				
//				if(geometry !=null)
//				{
//					boolean correct = validarGeometria(db, geometry, gid, "fajas_real", getType());
//				    if(!correct)
//				    {
//				     JOptionPane.showMessageDialog(null, "El polígono digitalizado es incorrecto", "Error dibujando elemento", JOptionPane.ERROR_MESSAGE);
//				     return false;
//				    }
//				}    
			    
				TrazasManager.insertar_Traza("Actualizó la Faja real con identificador "+gid);
				
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
