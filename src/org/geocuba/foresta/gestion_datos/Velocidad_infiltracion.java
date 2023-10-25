package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.PersistentObject;

public class Velocidad_infiltracion extends PersistentObject {
	
	private Integer ID;
	private String clase;
	private Double velocidad;
	private String estructura;
	private Agrupador_uso uso;
	//private Suelo Suelos;
	
	public Velocidad_infiltracion() 
	{
		super(true);
		this.ID = null;
		this.clase = null;
		this.velocidad = null;
		this.estructura = null;
		this.uso = null;
	}
	
	public Velocidad_infiltracion(int _ID, Agrupador_uso _uso, String _clase, double _velocidad, String _estructura, boolean isNewObject) 
	{
	 super(isNewObject);
	 this.ID = _ID;
	 this.clase = _clase;
	 this.velocidad = _velocidad;
	 this.estructura = _estructura;
	 this.uso = _uso;
	}
	
	public void setID(int value) {
		this.ID = value;
	}
	
	public Integer getID() {
		return ID;
	}
	
	public void setClase(String value) {
		this.clase = value;
	}
	
	public String getClase() {
		return clase;
	}
	
	public void setVelocidad(double value) {
		this.velocidad = value;
	}
	
	public Double getVelocidad() {
		return velocidad;
	}
	
	public void setEstructura(String value) {
		this.estructura = value;
	}
	
	public String getEtructura() {
		return estructura;
	}
	
	public void setUso_suelo(Agrupador_uso value) {
		this.uso = value;
	}
	
	public Agrupador_uso getUso_suelo() {
		return uso;
	}
	
	@Override
	public boolean save() 
	{
	 boolean isOk = false;
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 
	 if(isNewObject)   //-----------INSERTO-----------------------------------------------------------------
	 {
	  if(clase!=null && velocidad!=null && estructura!=null && uso!=null)
	  {
	   String columnas = "uso, clase, velocidad, estructura";
	   String values = uso.getId()+",'"+clase+"',"+velocidad+",'"+estructura+"'";
	   
	   String sql = "insert into velocidades_infiltracion("+columnas+") values("+values+")";
	   db.ejecutarConsulta(sql);
	   System.out.println(sql);
	   
	   db.ejecutarConsulta("select max(id) from velocidades_infiltracion");
	   ID = db.getValueAsInteger(0,0);
	   TrazasManager.insertar_Traza("Insertó la velocidad de infiltración con identificador "+ID);
	   
	   isOk = true;
	  }	  
	 }
	 else    //--------------MODIFICO--------------------------------------------------------------
	 {
	  String sql = null;
	  if(uso != null)
	   sql = "update velocidades_infiltracion set uso="+uso.getId();
	  
	  if(clase != null)
	  {
		if(sql == null)
	     sql = "update velocidades_infiltracion set clase='"+clase+"'";
		else
		 sql += ", clase='"+clase+"'";
	  }
	  
	  if(velocidad != null)
	  {
		if(sql == null)
	     sql = "update velocidades_infiltracion set velocidad="+velocidad;
		else
		 sql += ", velocidad="+velocidad;
	  }
	  
	  if(estructura != null)
	  {
		if(sql == null)
	     sql = "update velocidades_infiltracion set estructura='"+estructura+"'";
		else
		 sql += ", estructura='"+estructura+"'";
	  }
	  
	  if(sql != null)
	  {	   
	   sql += " where id="+ID;
	   db.ejecutarConsulta(sql);
	   TrazasManager.insertar_Traza("Actualizó la velocidad de infiltración con identificador "+ID);
	   
	   isOk = true;
	  }
	 }
			 
	 return isOk;
	}

	@Override
	public boolean delete() 
	{
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 db.ejecutarConsulta("delete from velocidades_infiltracion where id="+ID);
	 TrazasManager.insertar_Traza("Eliminó la velocidad de infiltración con identificador "+ID);
	 
	 return true;
	}
}
