package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.PersistentObject;

public class Tipo_hidrografia extends PersistentObject{
	
	private Integer id;
	private String codigo;
	private String descripcion;
	private Double ancho_faja;
	private String tipo_elemento;
	//private Hidrografia Hidrografia;
	
	public Tipo_hidrografia() 
	{
	 super(true);	
	 this.id = null;
	 this.codigo = null;
	 this.descripcion = null;
	 this.ancho_faja = null;
	 this.tipo_elemento = null;
	}
	
	public Tipo_hidrografia(int _id, String _codigo, String _descripcion, Double _ancho_faja, String _tipo_elemento, boolean _isNewObject) 
	{
	 super(_isNewObject);	
	 this.id = _id;
	 this.codigo = _codigo;
	 this.descripcion = _descripcion;
	 this.ancho_faja = _ancho_faja;
	 this.tipo_elemento = _tipo_elemento;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setDescripcion(String value) {
		this.descripcion = value;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setAncho_faja(Double value) {
		this.ancho_faja = value;
	}
	
	public Double getAncho_faja() {
		return ancho_faja;
	}
	
	public void setTipo_elemento(String value) {
		this.tipo_elemento = value;
	}
	
	public String getTipo_elemento() {
		return tipo_elemento;
	}
	
	public void setCodigo(String value) {
		this.codigo = value;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	/*public void setHidrografia(Hidrografia value) {
		if (this.Hidrografia != value) {
			Hidrografia lHidrografia = this.Hidrografia;
			this.Hidrografia = value;
			if (value != null) {
				Hidrografia.setTipo_hidrografia(this);
			}
			else {
				lHidrografia.setTipo_hidrografia(null);
			}
		}
	}
	
	public Hidrografia getHidrografia() {
		return Hidrografia;
	}*/
	
	@Override
	public boolean save() 
	{
	 boolean isOk = false;
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 
	 if(isNewObject)   //-----------INSERTO-----------------------------------------------------------------
	 {
	  if(codigo!=null && descripcion!=null && ancho_faja!=null && tipo_elemento!=null)
	  {
	   String columnas = "codigo, descripcion, ancho_faja, tipo_elemento";
	   String values = "'"+codigo+"','"+descripcion+"',"+ancho_faja+",'"+tipo_elemento+"'";
	   
	   String sql = "insert into tipo_hidrografia("+columnas+") values("+values+")";
	   db.ejecutarConsulta(sql);
	   System.out.println(sql);
	   
	   db.ejecutarConsulta("select max(id) from tipo_hidrografia");
	   id = db.getValueAsInteger(0,0);
	   TrazasManager.insertar_Traza("Insertó el tipo de hidrografía con identificador "+id);
	   
	   isOk = true;
	  }	  
	 }
	 else    //--------------MODIFICO--------------------------------------------------------------
	 {
	  String sql = null;
	  if(codigo != null)
	   sql = "update tipo_hidrografia set codigo='"+codigo+"'";
	  
	  if(descripcion != null)
	  {
		if(sql == null)
	     sql = "update tipo_hidrografia set descripcion='"+descripcion+"'";
		else
		 sql += ", descripcion='"+descripcion+"'";
	  }
	  
	  if(ancho_faja != null)
	  {
		if(sql == null)
	     sql = "update tipo_hidrografia set ancho_faja="+ancho_faja;
		else
		 sql += ", ancho_faja="+ancho_faja;
	  }
	  
	  if(tipo_elemento != null)
	  {
		if(sql == null)
	     sql = "update tipo_hidrografia set tipo_elemento='"+tipo_elemento+"'";
		else
		 sql += ", tipo_elemento='"+tipo_elemento+"'";
	  }
		   
	  if(sql != null)
	  {	   
	   sql += " where id="+id;
	   db.ejecutarConsulta(sql);
	   TrazasManager.insertar_Traza("Actualizó el tipo de hidrografía con identificador "+id);
	   
	   isOk = true;
	  }
	 }
			 
	 return isOk;
	}

	@Override
	public boolean delete() 
	{
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 db.ejecutarConsulta("delete from tipo_hidrografia where id="+id);
	 TrazasManager.insertar_Traza("Eliminó el tipo de hidrografía con identificador "+id);
	 
	 return true;
	}
	
	public String toString()
	{
     return descripcion;		
	}
	
}
