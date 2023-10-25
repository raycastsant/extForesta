package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.PersistentObject;

public class Tipo_suelo extends PersistentObject {
	
	private Integer id;
	private String clave;
	private String tipo;
	//private Suelo Suelos;
	
	public Tipo_suelo() 
	{
	 super(true);
	 this.id = null;
	 this.clave = null;
	 this.tipo = null;
	}
	
	public Tipo_suelo(Integer _id, String _clave, String _tipo, boolean isNewObject) 
	{
	 super(isNewObject);
	 this.id = _id;
	 this.clave = _clave;
	 this.tipo = _tipo;
	}
	
	public Integer getID() {
		return id;
	}
	
	public void setClave(String value) {
		this.clave = value;
	}
	
	public String getClave() {
		return clave;
	}
	
	public void setTipo(String value) {
		this.tipo = value;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public String toString()
	{
	 return tipo;	
	}
	
	@Override
	public boolean save() 
	{
	 boolean isOk = false;
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 
	 if(isNewObject)   //-----------INSERTO-----------------------------------------------------------------
	 {
	  if(clave!=null && tipo!=null)
	  {
	   String columnas = "clave, tipo";
	   String values = "'"+clave+"','"+tipo+"'";
	   
	   String sql = "insert into tipo_suelo("+columnas+") values("+values+")";
	   db.ejecutarConsulta(sql);
	   System.out.println(sql);
	   
	   db.ejecutarConsulta("select max(id) from tipo_suelo");
	   id = db.getValueAsInteger(0,0);
	   TrazasManager.insertar_Traza("Insertó el tipo de suelo con identificador "+id);
	   
	   isOk = true;
	  }	  
	 }
	 else    //--------------MODIFICO--------------------------------------------------------------
	 {
	  String sql = null;
	  if(clave != null)
	   sql = "update tipo_suelo set clave='"+clave+"'";
	  
	  if(tipo != null)
	  {
		if(sql == null)
	     sql = "update tipo_suelo set tipo='"+tipo+"'";
		else
		 sql += ", tipo='"+tipo+"'";
	  }
	  
	  if(sql != null)
	  {	   
	   sql += " where id="+id;
	   db.ejecutarConsulta(sql);
	   TrazasManager.insertar_Traza("Actualizó el tipo de suelo con identificador "+id);
	   
	   isOk = true;
	  }
	 }
			 
	 return isOk;
	}

	@Override
	public boolean delete() 
	{
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 db.ejecutarConsulta("delete from tipo_suelo_especies where tipo_suelo="+id);
	 db.ejecutarConsulta("delete from tipo_suelo where id="+id);
	 TrazasManager.insertar_Traza("Eliminó el tipo de suelo con identificador "+id);
	 
	 return true;
	}
}
