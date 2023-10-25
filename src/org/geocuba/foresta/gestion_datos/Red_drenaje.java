package org.geocuba.foresta.gestion_datos;

import javax.swing.JOptionPane;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.exceptions.visitors.ProcessVisitorException;

public class Red_drenaje extends PersistentGeometricObject {
	
	private Integer gid;
	private Parteaguas parteaguas;
	private Integer orden;
	private Integer siguiente;
	//private Integer the_geom;
	
	public Integer getGid() {
		return gid;
	}
	
	public Red_drenaje(int _gid, Parteaguas _parteaguas, int _orden, int _siguiente, boolean isNewObject) 
	{
		super(isNewObject);
		gid = _gid;
		parteaguas = _parteaguas;
		orden = _orden;
		siguiente = _siguiente;
	}
	
	public Red_drenaje() 
	{
		super(true);
	}
	
	public void setOrden(int value) {
		setOrden(new Integer(value));
	}
	
	public void setOrden(Integer value) {
		this.orden = value;
	}
	
	public Integer getOrden() {
		return orden;
	}
	
	public void setSiguiente(int value) {
		setSiguiente(new Integer(value));
	}
	
	public void setSiguiente(Integer value) {
		this.siguiente = value;
	}
	
	public Integer getSiguiente() {
		return siguiente;
	}
	
	public void setParteaguasg(Parteaguas value) 
	{
	  this.parteaguas = value;	
		/*if (Parteaguasg != null) {
			Parteaguasg.red_drenaje.remove(this);
		}
		if (value != null) {
			value.red_drenaje.add(this);
		}*/
	}
	
	public Parteaguas getParteaguasg() {
		return parteaguas;
	}
	
	@Override
	public boolean delete() 
	{
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 db.ejecutarConsulta("delete from red_drenaje where gid="+gid);
	     TrazasManager.insertar_Traza("Eliminó el tramo de red de drenaje con identificador "+gid);
		 
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
		  String columnas = "orden, siguiente, the_geom";
		  String values = orden+","+siguiente+","+geomInfo;
		  
		  if(parteaguas!=null)
		  {
		   columnas += ",parteaguas";
		   values += ","+parteaguas.getGid();
		  } 	
		  else
		  {
			   db.ejecutarConsulta("select gid, cuenca, descripcion from parteaguas where " +
			   		"st_intersects(the_geom, "+geomInfo+")");
			   if(db.getRowCount() == 1)
			   {
				int pgid = db.getValueAsInteger(0,0);
				int pcuenca = db.getValueAsInteger(0,1);
				CuencaManager pcman = new CuencaManager();
				Cuenca cuenca = pcman.Cargar_Objeto_BD(pcuenca);
				String pdesc = db.getValueAsString(0,2);
				
				parteaguas = new Parteaguas(pgid, cuenca, pdesc, true);
				columnas += ",parteaguas";
				values += ","+parteaguas.getGid();
			   }	   
		  }	  
		  
//		    if(geomInfo != null)
//			{	
//			    boolean correct = validarGeometriaLineas(db, geomInfo,"red_drenaje");
//			    if(!correct)
//			    {
//			     JOptionPane.showMessageDialog(null, "La línea digitalizada es incorrecta", "Error dibujando elemento", JOptionPane.ERROR_MESSAGE);
//			     return false;
//			    }
//			}  
		  
		  String sql = "insert into red_drenaje("+columnas+") values("+values+")";
		  db.ejecutarConsulta(sql);
		  System.out.println(sql);
		  
		  db.ejecutarConsulta("select max(gid) from red_drenaje");
		  gid = db.getValueAsInteger(0,0);
		  
		  TrazasManager.insertar_Traza("Insertó el tramo de red de drenaje con identificador "+gid);
		  
		  isOk = true;  
		 }
		 else    //---------------ACTUALIZAR-------------------------------------------------------------
		 {
			   String sql = null;
		       if(orden != null)
			    sql = "update red_drenaje set orden="+orden;
			   
		       if(siguiente != null)
			   {
				if(sql == null)
			     sql = "update red_drenaje set siguiente="+siguiente;
				else
				 sql += ", siguiente="+siguiente;
			   }
		       
		       if(parteaguas != null)
			   {
				if(sql == null)
			     sql = "update red_drenaje set parteaguas="+parteaguas.getGid();
				else
				 sql += ", parteaguas="+parteaguas.getGid();
			   }
		       else
			   {
					   db.ejecutarConsulta("select gid, cuenca, descripcion from parteaguas where " +
					   		"st_intersects(the_geom, "+geomInfo+")");
					   if(db.getRowCount() == 1)
					   {
							int pgid = db.getValueAsInteger(0,0);
							int pcuenca = db.getValueAsInteger(0,1);
							CuencaManager pcman = new CuencaManager();
							Cuenca cuenca = pcman.Cargar_Objeto_BD(pcuenca);
							String pdesc = db.getValueAsString(0,2);
							
							parteaguas = new Parteaguas(pgid, cuenca, pdesc, true);
							if(sql == null)
							 sql = "update red_drenaje set parteaguas="+parteaguas.getGid();
							else
							 sql += ", parteaguas="+parteaguas.getGid();
					   }	   
			   }	  
		       
		       //String geometry = getGeometryInfo();
		       
//		       if(geomInfo != null)
//				{	
//				    boolean correct = validarGeometriaLineas(db, geomInfo,"red_drenaje");
//				    if(!correct)
//				    {
//				     JOptionPane.showMessageDialog(null, "La línea digitalizada es incorrecta", "Error dibujando elemento", JOptionPane.ERROR_MESSAGE);
//				     return false;
//				    }
//				}  
		       
			   if(geomInfo != null)
			   {
				if(sql == null)
			     sql = "update perfiles set the_geom="+geomInfo;
				else
				 sql += ", the_geom="+geomInfo; 
			   }
			   
			   if(sql != null)
			   {	   
				sql += " where gid="+gid; 
			    db.ejecutarConsulta(sql);
			    TrazasManager.insertar_Traza("Actualizó el tramo de red de drenaje con identificador "+gid);
			    
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
