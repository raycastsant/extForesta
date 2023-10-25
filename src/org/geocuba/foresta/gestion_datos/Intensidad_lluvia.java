package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.PersistentObject;

public class Intensidad_lluvia extends PersistentObject {
	
	private Integer ID;
	private Double prob;
	private Double tiempo;
	private Double prec;
	
	public Intensidad_lluvia() 
	{
		super(true);
		this.ID = null;
		this.prob = null;
		this.tiempo = null;
		this.prec = null;
	}
	
	public Intensidad_lluvia(int _ID, Double _prob, Double _tiempo, Double _prec, boolean isNewObject) 
	{
	 super(isNewObject);
	 this.ID = _ID;
	 this.prob = _prob;
	 this.tiempo = _tiempo;
	 this.prec = _prec;
	}
	
	public void setID(int value) {
		this.ID = value;
	}
	
	public Integer getID() {
		return ID;
	}
	
	public void setProbabilidad(Double value) {
		this.prob = value;
	}
	
	public Double getProbabilidad() {
		return prob;
	}
	
	public void setTiempo_Duracion(double value) {
		this.tiempo = value;
	}
	
	public Double getTiempo_Duracion() {
		return tiempo;
	}
	
	public void setPrecipitacion(Double value) {
		this.prec = value;
	}
	
	public Double getPrecipitacion() {
		return prec;
	}
	
	@Override
	public boolean save() 
	{
	 boolean isOk = false;
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 
	 if(isNewObject)   //-----------INSERTO-----------------------------------------------------------------
	 {
	  if(prob!=null && tiempo!=null && prec!=null)
	  {
	   String columnas = "prob, tiempo, prec";
	   String values = prob+","+tiempo+","+prec;
	   
	   String sql = "insert into intensidad_lluvia("+columnas+") values("+values+")";
	   db.ejecutarConsulta(sql);
	   System.out.println(sql);
	   
	   db.ejecutarConsulta("select max(id) from intensidad_lluvia");
	   ID = db.getValueAsInteger(0,0);
	   TrazasManager.insertar_Traza("Insertó el valor de intensidad de lluvia con identificador "+ID);
	   
	   isOk = true;
	  }	  
	 }
	 else    //--------------MODIFICO--------------------------------------------------------------
	 {
	  String sql = null;
	  if(prob != null)
	   sql = "update intensidad_lluvia set prob="+prob;
	  
	  if(tiempo != null)
	  {
		if(sql == null)
	     sql = "update intensidad_lluvia set tiempo="+tiempo;
		else
		 sql += ", tiempo="+tiempo;
	  }
	  
	  if(prec != null)
	  {
		if(sql == null)
	     sql = "update intensidad_lluvia set prec="+prec;
		else
		 sql += ", prec="+prec;
	  }
		   
	  if(sql != null)
	  {	   
	   sql += " where id="+ID;
	   db.ejecutarConsulta(sql);
	   TrazasManager.insertar_Traza("Actualizó el valor de intensidad de lluvia con identificador "+ID);
	   
	   isOk = true;
	  }
	 }
			 
	 return isOk;
	}

	@Override
	public boolean delete() 
	{
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 db.ejecutarConsulta("delete from intensidad_lluvia where id="+ID);
	 TrazasManager.insertar_Traza("Eliminó el valor de intensidad de lluvia con identificador "+ID);
	 
	 return true;
	}
}
