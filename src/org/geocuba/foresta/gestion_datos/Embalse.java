package org.geocuba.foresta.gestion_datos;

import javax.swing.JOptionPane;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import com.iver.cit.gvsig.exceptions.visitors.ProcessVisitorException;

public class Embalse extends Hidrografia {
	
	private Integer gid;
	//private Hidrografia Hidrografia;
	private String uso;
	private String naturaleza;
	private Double volumen;
	private Double nan;
	private Double nam;
	//private int the_geom;
	
	public Embalse(Integer _gid, String _uso, String _naturaleza, Double _volumen, Double _nan, Double _nam, 
			String _nombre, Cuenca _cuenca, Parteaguas _parteaguas, Tipo_hidrografia _tipo, int _idHidro, boolean isNewObject) 
	{
		super(isNewObject);
		this.gid = _gid;
		this.uso = _uso;
		this.naturaleza = _naturaleza;
		this.volumen = _volumen;
		this.nan = _nan;
		this.nam = _nam;
		this.cuenca = _cuenca;
		this.parteaguas = _parteaguas;
		this.Tipo_hidrografia = _tipo;
		this.nombre = _nombre;
		this.idHidrografia = _idHidro;
	}
	
	public Embalse() {
		super(true);
		this.gid = null;
		this.uso = null;
		this.naturaleza = null;
		this.volumen = null;
		this.nan = null;
		this.nam = null;
		this.cuenca = null;
		this.parteaguas = null;
		this.Tipo_hidrografia = null;
		this.nombre = null;
		this.idHidrografia = null;
	}
	
	public void setGid(int value) {
		this.gid = value;
	}
	
	public Integer getGid() {
		return gid;
	}
	
	public void setUso(String value) {
		this.uso = value;
	}
	
	public String getUso() {
		return uso;
	}
	
	public void setNaturaleza(String value) {
		this.naturaleza = value;
	}
	
	public String getNaturaleza() {
		return naturaleza;
	}
	
	public void setVolumen(double value) {
		setVolumen(new Double(value));
	}
	
	public void setVolumen(Double value) {
		this.volumen = value;
	}
	
	public Double getVolumen() {
		return volumen;
	}
	
	public void setNan(double value) {
		setNan(new Double(value));
	}
	
	public void setNan(Double value) {
		this.nan = value;
	}
	
	public Double getNan() {
		return nan;
	}
	
	public void setNam(double value) {
		setNam(new Double(value));
	}
	
	public void setNam(Double value) {
		this.nam = value;
	}
	
	public Double getNam() {
		return nam;
	}
	
	@Override
	public boolean delete() 
	{
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 db.ejecutarConsulta("delete from embalses where gid="+gid);
		 db.ejecutarConsulta("delete from hidrografia where id="+idHidrografia);
		 TrazasManager.insertar_Traza("Eliminó el embalse con identificador "+gid);
	     
		 return true;
	}

	@Override
	public boolean save() 
	{
		JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 
	 try {
		 
		 if(Tipo_hidrografia!=null)
		 {	 
		  if(isNewObject)   //------------Insertar----------------------------------------------------------------
		  {
		   String columnas = "tipo_hidrografia, nombre, cuenca";
		   String values = Tipo_hidrografia.getId()+",'"+nombre+"'";
		   
		   if(cuenca!=null)
			values += ","+cuenca.getGid();
		   else     //Si cuenca esta vacio tengo que buscar la que se intersecta con el elemento
		   {
			String sql;
				sql = "select gid from cuencas where st_intersects(cuencas.the_geom, "+getGeometryInfo()+")";
			  
			db.ejecutarConsulta(sql);
			System.out.println(sql);
			
			if(db.isEmpty())
			{
		     JOptionPane.showMessageDialog(null, "No se puede insertar el Embalse con nombre :"+nombre+"."+"\n"+
		    		 "Esto puede ser debido a que no existan datos de cuencas o que el elemento no se intersecte con las existentes."+"\n"+
		    		 "Considere cancelar la operación.");
		     return false; 
			}
			else
			{
		     String gidCuenca = db.getValueAt(0,0).toString();
		     values += ","+gidCuenca;
			}	
		   }	   

		   columnas += ",parteaguas";
		   if(parteaguas!=null)
			values += ","+parteaguas.getGid();
		   else
			values += ", null";

			  String sql = "insert into hidrografia("+columnas+") values("+values+")"; 
		      db.ejecutarConsulta(sql);
		      System.out.println(sql);
		      db.ejecutarConsulta("select max(id) from hidrografia");
		      String id = db.getValueAt(0,0).toString();
		   
		      String geomInfo = getGeometryInfo();
		      columnas = "tipo_hidrografia, uso, naturaleza, volumen, nan, nam, the_geom";
		      values = id+",'"+uso+"','"+naturaleza+"',"+volumen+","+nan+","+nam+","+geomInfo;
		      
		      sql = "insert into embalses("+columnas+") values("+values+")";
		      db.ejecutarConsulta(sql);
		      db.ejecutarConsulta("select max(gid) from embalses");
		      gid = db.getValueAsInteger(0,0);
//		        boolean correct = validarGeometria(db, geomInfo, gid, "embalses", getType());
//			    if(!correct)
//			    {
//			     JOptionPane.showMessageDialog(null, "El polígono digitalizado es incorrecto", "Error dibujando elemento", JOptionPane.ERROR_MESSAGE);
//			     return false;
//			    }
		      TrazasManager.insertar_Traza("Insertó el embalse con identificador "+gid);
		      
		      return true;
		   
		  }
		  else    //--------------------Modificar--------------------------------------------------------
		  {
			   String sql = null;
		       if(Tipo_hidrografia != null)
			    sql = "update hidrografia set tipo_hidrografia="+Tipo_hidrografia.getId();
			   
		       if(nombre != null)
			   {
				if(sql == null)
			     sql = "update hidrografia set nombre='"+nombre+"'";
				else
				 sql += ", nombre='"+nombre+"'"; 
			   }
		       
		       if(parteaguas != null)
			   {
				if(sql == null)
			     sql = "update hidrografia set parteaguas="+parteaguas.getGid();
				else
				 sql += ", parteaguas="+parteaguas.getGid(); 
			   }
		       
		       if(cuenca != null)
			   {
				if(sql == null)
			     sql = "update hidrografia set cuenca="+cuenca.getGid();
				else
				 sql += ", cuenca="+cuenca.getGid(); 
			   }
		       
		       if(sql != null)
			   {	   
				sql += " where id="+idHidrografia;
				db.ejecutarConsulta(sql);
			   }
			   else
			    return false;
		       
		      //-----------------------------Datos del embalse----------------------------------------
		       sql = null;
			   String geometry = getGeometryInfo();
			   if(geometry != null)
			     sql = "update embalses set the_geom="+geometry;
			   
			   if(naturaleza != null)
			   {
				if(sql == null)
			     sql = "update embalses set naturaleza='"+naturaleza+"'";
				else
				 sql += ", naturaleza='"+naturaleza+"'"; 
			   }
			   
			   if(uso != null)
			   {
				if(sql == null)
			     sql = "update embalses set uso='"+uso+"'";
				else
				 sql += ", uso='"+uso+"'"; 
			   }
			   
			   if(volumen != null)
			   {
				if(sql == null)
			     sql = "update embalses set volumen="+volumen;
				else
				 sql += ", volumen="+volumen; 
			   }
			   
			   if(nan != null)
			   {
				if(sql == null)
			     sql = "update embalses set nan="+nan;
				else
				 sql += ", nan="+nan; 
			   }
			   
			   if(nam != null)
			   {
				if(sql == null)
			     sql = "update embalses set nam="+nam;
				else
				 sql += ", nam="+nam; 
			   }
			   
			   if(sql != null)
			   {	   
				sql += " where gid="+gid;
				db.ejecutarConsulta(sql);
				
//				if(geometry !=null)
//				{
//					boolean correct = validarGeometria(db, geometry, gid, "embalses", getType());
//				    if(!correct)
//				    {
//				     JOptionPane.showMessageDialog(null, "El polígono digitalizado es incorrecto", "Error dibujando elemento", JOptionPane.ERROR_MESSAGE);
//				     return false;
//				    }
//				}   
			    
				TrazasManager.insertar_Traza("Actualizó el embalse con identificador "+gid);
				
			    return true;
			   }
			   else
			    return false;	   
		  }
		 } 
			 
	    } catch (ProcessVisitorException e) {
			e.printStackTrace();
			return false;
		} 
			return false;
	}
	
}
