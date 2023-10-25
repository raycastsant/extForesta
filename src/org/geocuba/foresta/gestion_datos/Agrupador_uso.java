package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.PersistentObject;

public class Agrupador_uso extends PersistentObject
{
    private Integer id;
	private String tipo_uso;
	
	public Agrupador_uso() 
	{
	 super(true);
	 id = null;
	 tipo_uso = null;
	}
	
	public Agrupador_uso(Integer _id, String _tipo_uso, boolean isNew) 
	{
	 super(isNew);	
	 id = _id;
	 tipo_uso = _tipo_uso;
	}
	
	public void setId(int value) {
		this.id = value;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setTipo_uso(String value) {
		this.tipo_uso = value;
	}
	
	public String getTipo_uso() {
		return tipo_uso;
	}
	
	public String toString(){
	 return tipo_uso;		
	}

	@Override
	public boolean save() 
	{
	 boolean isOk = false;
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 
	 if(isNewObject)   //-----------INSERTO-----------------------------------------------------------------
	 {
	  String columnas = null;
	  String values = null;
	  
	  if(tipo_uso != null)
	  {
	   columnas = "tipo_uso";
	   values = "'"+tipo_uso+"'";	  
	  }	  
	  
	  if(columnas!=null)
	  {	  
	   String sql = "insert into agrupadores_uso("+columnas+") values("+values+")";
	   db.ejecutarConsulta(sql);
	   System.out.println(sql);
	   
	   db.ejecutarConsulta("select max(id) from agrupadores_uso");
	   id = db.getValueAsInteger(0,0);
	   TrazasManager.insertar_Traza("Insertó el agrupador de uso con identificador "+id);
	   
	   isOk = true;
	  }
	  
	  
	 }
	 else    //--------------MODIFICO--------------------------------------------------------------
	 {
		   String sql = null;
	       if(tipo_uso != null)
		    sql = "update agrupadores_uso set tipo_uso='"+tipo_uso+"'";
		   
		   if(sql != null)
		   {	   
			sql += " where id="+id;
			db.ejecutarConsulta(sql);
			TrazasManager.insertar_Traza("Actualizó el agrupador de uso con identificador"+id);
			
		    return true;
		   }
		   else
		    return false;	   
	 }
			 
	 return isOk;
	}

	@Override
	public boolean delete() 
	{
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 db.ejecutarConsulta("delete from agrupadores_uso where id="+id);
     TrazasManager.insertar_Traza("Eliminó el agrupador de uso con identificador"+id);
	 
	 return true;
	}
}
