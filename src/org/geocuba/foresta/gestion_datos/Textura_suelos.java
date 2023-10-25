package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.PersistentObject;

public class Textura_suelos extends PersistentObject{
	
	private Integer id;
	private String textura;
	
	public Textura_suelos() 
	{
		super(true);
		this.id = null;
		this.textura = null;
	}
	
	public Textura_suelos(Integer _id, String _textura, boolean isNewObject) 
	{
		super(isNewObject);
		this.id = _id;
		this.textura = _textura;
	}
	
	public void setId(int value) {
		this.id = value;
	}
	
	public Integer getId() {
		return id;
	}
	
	public int getORMID() {
		return getId();
	}
	
	public void setTextura(String value) {
		this.textura = value;
	}
	
	public String getTextura() {
		return textura;
	}
	
	public String toString(){
     return textura;		
	}

	@Override
	public boolean save() 
	{
	 boolean isOk = false;
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 
	 if(isNewObject)   //-----------INSERTO-----------------------------------------------------------------
	 {
	  if(textura!=null)
	  {
	   String columnas = "textura";
	   String values = "'"+textura+"'";
	   
	   String sql = "insert into textura_suelos("+columnas+") values("+values+")";
	   db.ejecutarConsulta(sql);
	   System.out.println(sql);
	   
	   db.ejecutarConsulta("select max(id) from textura_suelos");
	   id = db.getValueAsInteger(0,0);
	   TrazasManager.insertar_Traza("Insertó la textura de suelos con identificador "+id);
	   
	   isOk = true;
	  }	  
	 }
	 else    //--------------MODIFICO--------------------------------------------------------------
	 {
	  String sql = null;
	  if(textura != null)
	   sql = "update textura_suelos set textura='"+textura+"'";
	  
	  if(sql != null)
	  {	   
	   sql += " where id="+id;
	   db.ejecutarConsulta(sql);
	   TrazasManager.insertar_Traza("Actualizó la textura de suelos con identificador "+id);
	   
	   isOk = true;
	  }
	 }
			 
	 return isOk;
	}

	@Override
	public boolean delete() 
	{
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 db.ejecutarConsulta("delete from textura_suelos where id="+id);
	 TrazasManager.insertar_Traza("Eliminó la textura de suelos con identificador "+id);
	 
	 return true;
	}
}
