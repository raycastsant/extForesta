package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.PersistentObject;

public class Provincia extends PersistentObject
{
	private Integer id;
	private String nombre;
	
	 public Provincia() 
	 {
	  super(true);
      this.id = null;
      this.nombre = null;
	 }
	 
	 public Provincia(int _id, String _nombre, boolean isNew) 
	 {
	  super(isNew);
      this.id = _id;
      this.nombre = _nombre;
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
	
	/*public void set_Municipios(HashSet<Municipio> value) {
		this.listaMunicipios = value;
	}
	
	public void addMunicipio(Municipio value) {
		this.listaMunicipios.add(value);
	}*/

	@Override
	public boolean save() 
	{
	 boolean isOk = false;
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 
	 if(isNewObject)   //-----------INSERTO-----------------------------------------------------------------
	 {
	  if(nombre!=null)
	  {
	   String columnas = "nombre";
	   String values = "'"+nombre+"'";
	   
	   String sql = "insert into provincias("+columnas+") values("+values+")";
	   db.ejecutarConsulta(sql);
	   System.out.println(sql);
	   
	   db.ejecutarConsulta("select max(gid) from provincias");
	   id = db.getValueAsInteger(0,0);
	   TrazasManager.insertar_Traza("Insertó la provincia con identificador "+id);
	   
	   isOk = true;
	  }	  
	 }
	 else    //--------------MODIFICO--------------------------------------------------------------
	 {
	  String sql = null;
	  if(nombre != null)
	   sql = "update provincias set nombre='"+nombre+"'";
	  
	  if(sql != null)
	  {	   
	   sql += " where gid="+id;
	   db.ejecutarConsulta(sql);
	   TrazasManager.insertar_Traza("Actualizó la provincia con identificador "+id);
	   
	   isOk = true;
	  }
	 }
			 
	 return isOk;
	}

	@Override
	public boolean delete() 
	{
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 db.ejecutarConsulta("delete from provincias where gid="+id);
	 TrazasManager.insertar_Traza("Eliminó la provincia con identificador "+id);
	 
	 return true;
	}
	
	public String toString()
	{
	 return nombre;	
	}
	
}
