package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.PersistentObject;

public class Especie extends PersistentObject {
	
	private Integer id;
	private String siglas;
	private String nomb_cientifico;
	private String ncomun;
	
	public Especie() 
	{
		super(true);
		id = null;
		siglas = null;
		nomb_cientifico = null;
		ncomun = null;
	}
	
	public Especie(Integer _id, String _siglas, String _nomb_cientifico, String _ncomun,boolean isNewObject) 
	{
		super(isNewObject);
		id = _id;
		siglas = _siglas;
		nomb_cientifico = _nomb_cientifico;
		ncomun = _ncomun;
	}
	
	public void setId(int value) {
		this.id = value;
	}
	
	public Integer getId() {
		return id;
	}
	
	/**
	 * Siglas representativas
	 */
	public void setSiglas(String value) {
		this.siglas = value;
	}
	
	/**
	 * Siglas representativas
	 */
	public String getSiglas() {
		return siglas;
	}
	
	/**
	 * Nombre científico
	 */
	public void setNomb_cientifico(String value) {
		this.nomb_cientifico = value;
	}
	
	/**
	 * Nombre científico
	 */
	public String getNomb_cientifico() {
		return nomb_cientifico;
	}
	
	/**
	 * Nombre común
	 */
	public void setNcomun(String value) {
		this.ncomun = value;
	}
	
	/**
	 * Nombre común
	 */
	public String getNcomun() {
		return ncomun;
	}
	
	@Override
	public boolean save() 
	{
	 boolean isOk = false;
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 
	 if(isNewObject)   //-----------INSERTO-----------------------------------------------------------------
	 {
	  if(siglas!=null && nomb_cientifico!=null && ncomun!=null)
	  {
	   String columnas = "siglas, ncientifico, ncomun";
	   String values = "'"+siglas+"','"+nomb_cientifico+"','"+ncomun+"'";
	   
	   String sql = "insert into especies("+columnas+") values("+values+")";
	   db.ejecutarConsulta(sql);
	   System.out.println(sql);
	   
	   db.ejecutarConsulta("select max(id) from especies");
	   id = db.getValueAsInteger(0,0);
	   TrazasManager.insertar_Traza("Insertó la especie con identificador "+id);
	   
	   isOk = true;
	  }	  
	 }
	 else    //--------------MODIFICO--------------------------------------------------------------
	 {
	  String sql = null;
	  if(siglas != null)
	   sql = "update especies set siglas='"+siglas+"'";
	  
	  if(nomb_cientifico != null)
	  {
		if(sql == null)
	     sql = "update especies set ncientifico='"+nomb_cientifico+"'";
		else
		 sql += ", ncientifico='"+nomb_cientifico+"'"; 
	  }
	  
	  if(ncomun != null)
	  {
		if(sql == null)
	     sql = "update especies set ncomun='"+ncomun+"'";
		else
		 sql += ", ncomun='"+ncomun+"'"; 
	  }
		   
	  if(sql != null)
	  {	   
	   sql += " where id="+id;
	   db.ejecutarConsulta(sql);
	   TrazasManager.insertar_Traza("Actualizó la especie con identificador "+id);
	   
	   isOk = true;
	  }
	 }
			 
	 return isOk;
	}

	@Override
	public boolean delete() 
	{
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 db.ejecutarConsulta("delete from especies where id="+id);
     TrazasManager.insertar_Traza("Eliminó la especie con identificador "+id);
	 
	 return true;
	}
	
	public String toString()
	{
	 return ncomun;	
	}
}
