package org.geocuba.foresta.gestion_datos;

import javax.swing.JOptionPane;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import com.iver.cit.gvsig.exceptions.visitors.ProcessVisitorException;

public class Parcela extends PersistentGeometricObject
{
	private Integer gid;
	private Municipio municipio;
	private Uso_catastro uso;
	//private Suelo suelo;
	private String nombre;
	private String poseedor;
	private Integer zc;
	//private int the_geom;
	
	public Parcela() 
	{
		super(true);
		gid = null;
		municipio = null;
		poseedor = null;
		uso = null;
		nombre = null;
		zc = null;
	}
	
	public Parcela(Integer _gid, Municipio _municipio, String _poseedor, Uso_catastro _uso, String _nombre, Integer _zc, 
			boolean isNew) 
	{
		super(isNew);
		gid = _gid;
		municipio = _municipio;
		poseedor = _poseedor;
		uso = _uso;
		nombre = _nombre;
		zc = _zc;
//		suelo = _suelo;
	}
	
	public void setGid(int value) {
		this.gid = value;
	}
	
	public Integer getGid() {
		return gid;
	}
	
	public void setNombre(String value) {
		this.nombre = value;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setZc(int value) {
		setZc(new Integer(value));
	}
	
	public void setZc(Integer value) {
		this.zc = value;
	}
	
	public Integer getZc() {
		return zc;
	}
	
	public void setCuenca(Municipio municipio) 
	{
		this.municipio = municipio;
	}
	
	public Municipio getMunicipio() {
		return municipio;
	}
	
	public void setPoseedores(String value) 
	{
			this.poseedor = value;
	}
	
	public String getPoseedor() {
		return poseedor;
	}
	
	public void setUso_catastro(Uso_catastro value) {
			this.uso = value;
	}
	
	public Uso_catastro getUso_catastro() {
		return uso;
	}
	
//	public void setSuelo(Suelo value) {
//		this.suelo = value;
//    }
//
//    public Suelo getSuelo() {
//	   return suelo;
//    }
	
	public String toString() {
		return poseedor;
	}

	@Override
	public boolean delete() 
	{
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 db.ejecutarConsulta("delete from parcelas_suelos where parcela="+gid);
		 db.ejecutarConsulta("delete from parcelas where gid="+gid);
		 TrazasManager.insertar_Traza("Eliminó la parcela con identificador "+gid);
	     
		 return true;
	}

	@Override
	public boolean save() 
	{
		 boolean isOk = false;
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 
	  try {	 
		 if(isNewObject)   //--------INSERTAR--------------------------------------------------------------------
		 {
		  String columnas = "nombre, poseedor, zc, the_geom";
		  String values;
		
		  String geometryInfo = getGeometryInfo();
		  
		   values = "'"+nombre+"','"+poseedor+"',"+zc+","+geometryInfo;
		  
		  if(municipio!=null)
		  {
		   columnas += ",municipio";
		   values += ","+municipio.getGid();
		  } 	
		  else
		  {
		   String sql = "select municipios.gid from municipios where st_intersects(municipios.the_geom,"+geometryInfo+")";	  
		   db.ejecutarConsulta(sql);
		   
		   if(!db.isEmpty())
		   {
			int gidmun = db.getValueAsInteger(0,0);
			columnas += ",municipio";
			values += ","+gidmun;
		   }	   
		   else
			return false;    
		  }	 
		  
		  if(uso!=null)
		  {
		   columnas += ",uso";
		   values += ","+uso.getId();
		  } 
		  
//		  if(suelo!=null)
//		  {
//		   columnas += ",suelo";
//		   values += ","+suelo.getGid();
//		  } 	
//		  else
//		  {
//		   String sql = "select suelos.gid from suelos where st_intersects(suelos.the_geom,"+geometryInfo+")";	  
//		   db.executeQuery(sql);
//		   
//		   if(!db.isEmpty())
//		   {
//			int gidsuelo = db.getValueAsInteger(0,0);
//			columnas += ",suelo";
//			values += ","+gidsuelo;
//		   }	   
//		   else
//			return false;    
//		  }
		  
		  String sql = "insert into parcelas("+columnas+") values("+values+")";
		  db.ejecutarConsulta(sql);
		  System.out.println(sql);
		  
		  db.ejecutarConsulta("select max(gid) from parcelas");
		  gid = db.getValueAsInteger(0,0);
		  
//		    boolean correct = validarGeometria(db, geometryInfo, gid, "parcelas", getType());
//		    if(!correct)
//		    {
//		     JOptionPane.showMessageDialog(null, "El polígono digitalizado es incorrecto", "Error dibujando elemento", JOptionPane.ERROR_MESSAGE);
//		     return false;
//		    }
		  
		  /*Actualizo los suelos que se intersecten*/ 	  	   
		  db.ejecutarConsulta("select _suelos.gid from _suelos inner join parcelas on st_intersects(" +
		  		"_suelos.the_geom, parcelas.the_geom) where parcelas.gid="+gid);
		  if(!db.isEmpty())
		  {
		   int []idsuelo = new int[db.getRowCount()];	  
		   for(int i=0; i<db.getRowCount(); i++)
			idsuelo[i] = db.getValueAsInteger(i,0);
		   
		   for(int i=0; i<idsuelo.length; i++)
		   {
			db.ejecutarConsulta("select * from parcelas_suelos where suelo="+idsuelo[i]+" and parcela="+gid);
			if(db.isEmpty())
			 db.ejecutarConsulta("insert into parcelas_suelos(suelo,parcela) values("+idsuelo[i]+","+gid+")");
		   }	
		  }	 
		  
		  TrazasManager.insertar_Traza("Insertó la parcela con identificador "+gid);
		  isOk = true;  
		 }
		 else    //---------------ACTUALIZAR-------------------------------------------------------------
		 {
			   String sql = null;
		       if(nombre != null)
			    sql = "update parcelas set nombre='"+nombre+"'";
			   
		       if(poseedor != null)
			   {
				if(sql == null)
			     sql = "update parcelas set poseedor='"+poseedor+"'";
				else
				 sql += ", poseedor='"+poseedor+"'"; 
			   }
		       
		       if(uso != null)
			   {
				if(sql == null)
			     sql = "update parcelas set uso="+uso.getId();
				else
				 sql += ", uso="+uso.getId(); 
			   }
		       
		       if(municipio != null)
			   {
				if(sql == null)
			     sql = "update parcelas set municipio="+municipio.getGid();
				else
				 sql += ", municipio="+municipio.getGid();
			   }
		       
//		       if(suelo != null)
//			   {
//				if(sql == null)
//			     sql = "update parcelas set suelo="+suelo.getGid();
//				else
//				 sql += ", suelo="+suelo.getGid();
//			   }

		       if(zc != null)
			   {
				if(sql == null)
			     sql = "update parcelas set zc="+zc;
				else
				 sql += ", zc="+zc; 
			   }
		       
			   String geometry = getGeometryInfo();
			   if(geometry != null)
			   {
				if(sql == null)
			     sql = "update parcelas set the_geom="+geometry;
				else
				 sql += ", the_geom="+geometry; 
			   }
			   
			   if(sql != null)
			   {	   
				sql += " where gid="+gid; 
			    db.ejecutarConsulta(sql);
			    
//			    if(geometry != null)
//				{
//				    boolean correct = validarGeometria(db, geometry, gid, "parcelas", getType());
//				    if(!correct)
//				    {
//				     JOptionPane.showMessageDialog(null, "El polígono digitalizado es incorrecto", "Error dibujando elemento", JOptionPane.ERROR_MESSAGE);
//				     return false;
//				    }
//				}   
			    
			    TrazasManager.insertar_Traza("Actualizó la parcela con identificador "+gid);
			    
			    return true;
			   }
			   else
			    return false;	   
		 }
		 
	  } catch (ProcessVisitorException e) {
			e.printStackTrace();
			isOk = false;
		}
				 
		 return isOk;
	}
}
