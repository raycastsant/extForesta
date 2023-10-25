package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.PersistentObject;

public class Uso_catastro extends PersistentObject 
{
    private Integer id;
	private Agrupador_uso uso_suelo;
	private String descripcion_uso;
	private Integer codigo_uso;
	private Integer tipo_superficie;
	private Integer tipo_uso;
	private Integer esp_uso;
	
	public Uso_catastro(Integer _id, Agrupador_uso _uso_suelo, String _descripcion_uso, Integer _codigo_uso, Integer _tipo_superficie,
			Integer _tipo_uso, Integer _esp_uso, boolean isNew) 
	{
	 super(isNew);	
	 id = _id;
	 uso_suelo = _uso_suelo;
	 descripcion_uso = _descripcion_uso;
	 codigo_uso = _codigo_uso;
	 tipo_superficie = _tipo_superficie;
	 tipo_uso = _tipo_uso;
	 esp_uso = _esp_uso;
	}
	
	public Uso_catastro() 
	{
	 super(true);	
	 id = null;
	 uso_suelo = null;
	 descripcion_uso = null;
	 codigo_uso = null;
	 tipo_superficie = null;
	 tipo_uso = null;
	 esp_uso = null;
	}
	
	public void setId(int value) {
		this.id = value;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setDescripcion_uso(String value) {
		this.descripcion_uso = value;
	}
	
	public String getDescripcion_uso() {
		return descripcion_uso;
	}
	
	public void setCodigo_uso(int value) {
		setCodigo_uso(new Integer(value));
	}
	
	public void setCodigo_uso(Integer value) {
		this.codigo_uso = value;
	}
	
	public Integer getCodigo_uso() {
		return codigo_uso;
	}
	
	public void setTipo_superficie(int value) {
		setTipo_superficie(new Integer(value));
	}
	
	public void setTipo_superficie(Integer value) {
		this.tipo_superficie = value;
	}
	
	public Integer getTipo_superficie() {
		return tipo_superficie;
	}
	
	public void setTipo_uso(int value) {
		setTipo_uso(new Integer(value));
	}
	
	public void setTipo_uso(Integer value) {
		this.tipo_uso = value;
	}
	
	public Integer getTipo_uso() {
		return tipo_uso;
	}
	
	public void setEsp_uso(int value) {
		setEsp_uso(new Integer(value));
	}
	
	public void setEsp_uso(Integer value) {
		this.esp_uso = value;
	}
	
	public Integer getEsp_uso() {
		return esp_uso;
	}
	
	public void setUso_suelo(Agrupador_uso value) {
		uso_suelo = value;
	}
	
	public Agrupador_uso getUso_suelo() {
		return uso_suelo;
	}
	
	public String toString(){
	 return descripcion_uso;	
	}

	@Override
	public boolean save() 
	{
	 boolean isOk = false;
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 
	 if(isNewObject)   //-----------INSERTO-----------------------------------------------------------------
	 {
	  if(uso_suelo!=null && descripcion_uso!=null && codigo_uso!=null && tipo_superficie!=null && tipo_uso!=null && esp_uso!=null)
	  {
	   String columnas = "uso_suelo, descripcion_uso, codigo_uso, tipo_superficie, tipo_uso, esp_uso";
	   String values = uso_suelo.getId()+",'"+descripcion_uso+"',"+codigo_uso+","+tipo_superficie+","+tipo_uso+","+esp_uso;
	   
	   String sql = "insert into usos_catastro("+columnas+") values("+values+")";
	   db.ejecutarConsulta(sql);
	   System.out.println(sql);
	   
	   db.ejecutarConsulta("select max(id) from usos_catastro");
	   id = db.getValueAsInteger(0,0);
	   TrazasManager.insertar_Traza("Insertó el Uso de catastro con identificador "+id);
	   
	   isOk = true;
	  }	  
	 }
	 else    //--------------MODIFICO--------------------------------------------------------------
	 {
	  String sql = null;
	  if(uso_suelo != null)
	   sql = "update usos_catastro set uso_suelo="+uso_suelo.getId();
	  
	  if(descripcion_uso != null)
	  {
		if(sql == null)
	     sql = "update usos_catastro set descripcion_uso='"+descripcion_uso+"'";
		else
		 sql += ", descripcion_uso='"+descripcion_uso+"'";
	  }
	  
	  if(codigo_uso != null)
	  {
		if(sql == null)
	     sql = "update usos_catastro set codigo_uso="+codigo_uso;
		else
		 sql += ", codigo_uso="+codigo_uso;
	  }
	  
	  if(tipo_superficie != null)
	  {
		if(sql == null)
	     sql = "update usos_catastro set tipo_superficie="+tipo_superficie;
		else
		 sql += ", tipo_superficie="+tipo_superficie;
	  }
	  
	  if(tipo_uso != null)
	  {
		if(sql == null)
	     sql = "update usos_catastro set tipo_uso="+tipo_uso;
		else
		 sql += ", tipo_uso="+tipo_uso;
	  }
	  
	  if(esp_uso != null)
	  {
		if(sql == null)
	     sql = "update usos_catastro set esp_uso="+esp_uso;
		else
		 sql += ", esp_uso="+esp_uso;
	  }
	  		   
	  if(sql != null)
	  {	   
	   sql += " where id="+id;
	   db.ejecutarConsulta(sql);
	   TrazasManager.insertar_Traza("Actualizó el uso de catastro con identificador "+id);
	   
	   isOk = true;
	  }
	 }
			 
	 return isOk;
	}

	@Override
	public boolean delete() 
	{
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 db.ejecutarConsulta("delete from usos_catastro where id="+id);
	 TrazasManager.insertar_Traza("Eliminó el uso de catastro con identificador "+id);
	 
	 return true;
	}
}
