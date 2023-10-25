package org.geocuba.foresta.administracion_seguridad;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.PersistentObject;

 public class Usuario extends PersistentObject{
	
	 private Integer id;
	 private Integer nivel;
     private String nombre;
     private String password;
     private String nombre_apellidos;
     //private java.util.Set ORM_trazas = new java.util.HashSet();
		
    public Usuario() 
 	{
 	 super(true);	
 	 this.id = null;
 	 this.nivel = null;
 	 this.nombre = null;
 	 this.password = null;
 	 this.nombre_apellidos = null;
 	}
     
	public Usuario(int _id, int _nivel, String _nombre, String _pass, String _nombre_apellidos, boolean _isNewObject) 
	{
	 super(_isNewObject);	
	 this.id = _id;
	 this.nivel = _nivel;
	 this.nombre = _nombre;
	 this.password = _pass;
	 this.nombre_apellidos = _nombre_apellidos;
	}
	
	public void setPassword(String value) {
		this.password = value;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setId(int value) {
		this.id = value;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setNombre(String value) {
		this.nombre = value;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNivel(int value) {
		this.nivel = value;
	}
	
	public Integer getNivel() {
		return nivel;
	}
	
	public void setNomb_apell(String value) {
		this.nombre_apellidos = value;
	}
	
	public String getNomb_apell() {
		return nombre_apellidos;
	}

	@Override
	public boolean save() 
	{
	 boolean isOk = false;
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 
	 if(isNewObject)   //-----------INSERTO-----------------------------------------------------------------
	 {
	  if(nombre!=null)
	  {
	   String columnas = "nombre,password";
	   String values = "'"+nombre+"','"+password+"'";
	   
	   db.ejecutarConsulta("select id from tipos_usuario where nivel="+nivel);
	   if(!db.isEmpty())
	   {	   
		columnas += ",tipo_usuario";
	    values += ","+db.getValueAsInteger(0,0); 
	   }
	   
	   if(nombre_apellidos != null)
	   {	  
		columnas += ",nombre_apellidos";
		values += ",'"+nombre_apellidos+"'";   
	   } 
	   
	   String sql = "insert into usuarios("+columnas+") values("+values+")";
	   db.ejecutarConsulta(sql);
	   TrazasManager.insertar_Traza("Insertó el usuario "+nombre);
	   System.out.println(sql);
	   
	   db.ejecutarConsulta("select max(id) from usuarios");
	   id = db.getValueAsInteger(0,0);
	   isOk = true;
	  }	  
	 }
	 else    //--------------MODIFICO--------------------------------------------------------------
	 {
	  String sql = null;
	  if(nombre != null)
	   sql = "update usuarios set nombre='"+nombre+"'";
	  
	  if(password != null)
	  {	  
	   if(sql==null)
		sql = "update usuarios set password='"+password+"'";
	   else
		sql += ",password='"+password+"'";   
	  } 
	  
	  if(nivel != null)
	  {
	   db.ejecutarConsulta("select id from tipos_usuario where nivel="+nivel);
	   if(!db.isEmpty())
	   {
		if(sql==null)
		 sql = "update usuarios set tipo_usuario="+db.getValueAsInteger(0,0);
		else
		 sql += ",tipo_usuario="+db.getValueAsInteger(0,0);	
	   }
	  }	  
	  
	  if(nombre_apellidos != null)
	  {	  
	   if(sql==null)
		sql = "update usuarios set nombre_apellidos='"+nombre_apellidos+"'";
	   else
		sql += ",nombre_apellidos='"+nombre_apellidos+"'";   
	  }
	  
	  if(sql != null)
	  {	   
	   sql += " where id="+id;
	   db.ejecutarConsulta(sql);
	   TrazasManager.insertar_Traza("Actualizó los datos del usuario "+nombre);
	   System.out.println(sql);
	   isOk = true;
	  }
	 }
			 
	 return isOk;
	}

	@Override
	public boolean delete() 
	{
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 db.ejecutarConsulta("delete from usuarios where id="+id);
	 TrazasManager.insertar_Traza("Eliminó el usuario "+nombre);
	 return true;
	}
}
