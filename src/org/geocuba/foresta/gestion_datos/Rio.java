package org.geocuba.foresta.gestion_datos;

import javax.swing.JOptionPane;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import com.iver.cit.gvsig.exceptions.visitors.ProcessVisitorException;

public class Rio extends Hidrografia {
	
    private Integer gid;
	private Double ancho;
	private Integer orden;
	//private int the_geom;
	
	public Rio() 
	{
	 super(true);
	 gid = null;
	 idHidrografia = null;
	 ancho = new Double(0);
	 orden = new Integer(0);
	 nombre = new String();
	 cuenca = null;
	 parteaguas = null;
	 Tipo_hidrografia = null;
	}
	
	public Rio(Integer _gid, Integer _idHidrografia, Double _ancho, Integer _orden, String _nombre, Cuenca _cuenca, Parteaguas _parteaguas, Tipo_hidrografia _tipo, boolean newobject) 
	{
	 super(newobject);	
	 gid = _gid;
	 idHidrografia = _idHidrografia;
	 ancho = _ancho;
	 orden = _orden;
	 nombre = _nombre;
	 cuenca = _cuenca;
	 parteaguas = _parteaguas;
	 Tipo_hidrografia = _tipo;
	}
	
	public void setGid(int value) {
		this.gid = value;
	}
	
	public Integer getGid() {
		return gid;
	}
	
	public void setAncho(double value) {
		setAncho(new Double(value));
	}
	
	public void setAncho(Double value) {
		this.ancho = value;
	}
	
	public Double getAncho() {
		return ancho;
	}
	
	public void setOrden(int value) {
		setOrden(new Integer(value));
	}
	
	public void setOrden(Integer value) {
		this.orden = value;
	}
	
	public Integer getOrden() {
		return orden;
	}
	
	@Override
	public boolean delete() 
	{
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 db.ejecutarConsulta("delete from rios where gid="+gid);
		 db.ejecutarConsulta("delete from hidrografia where id="+idHidrografia);
		 TrazasManager.insertar_Traza("Eliminó el río con identificador "+gid);
	     
		 return true;
	}

	@Override
	public boolean save() 
	{
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 
  if(Tipo_hidrografia!=null)
  {
   try {
	  String geomInfo = getGeometryInfo();
	   
	  if(isNewObject)   //-------INSERTAR---------------------------------------------------------------------
	  {
//		    if(geomInfo != null)
//			{	
//			    boolean correct = validarGeometriaLineas(db, geomInfo,"rios");
//			    if(!correct)
//			    {
//			     JOptionPane.showMessageDialog(null, "La línea digitalizada es incorrecta", "Error dibujando elemento", JOptionPane.ERROR_MESSAGE);
//			     return false;
//			    }
//			}  
		  
	   String columnas = "tipo_hidrografia, nombre, cuenca";
	   String values = Tipo_hidrografia.getId()+",'"+nombre+"'";
	   
	   if(cuenca!=null)
		values += ","+cuenca.getGid();
	   else     //Si cuenca esta vacio tengo que buscar la que se intersecta con el elemento
	   {
		String sql = "select gid from cuencas where st_intersects(cuencas.the_geom, "+geomInfo+")";
		 
		db.ejecutarConsulta(sql);
		System.out.println(sql);
		
		if(db.isEmpty())
		{
	     JOptionPane.showMessageDialog(null, "No se puede insertar el Río con nombre :"+nombre+"."+"\n"+
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
	   
	      columnas = "tipo_hidrografia, ancho, orden, the_geom";
	      values = id + "," + ancho.toString() + "," + orden.toString() + "," + geomInfo;
	      
	      sql = "insert into rios("+columnas+") values("+values+")";
	      db.ejecutarConsulta(sql);
	      System.out.println(sql);
	      TrazasManager.insertar_Traza("Insertó el río con identificador "+gid);
	      
	      return true;
	  }
	  else    //-----------------ACTUALIZAR-----------------------------------------------------------
	  {
//		    if(geomInfo != null)
//			{	
//			    boolean correct = validarGeometriaLineas(db, geomInfo,"rios");
//			    if(!correct)
//			    {
//			     JOptionPane.showMessageDialog(null, "La línea digitalizada es incorrecta", "Error dibujando elemento", JOptionPane.ERROR_MESSAGE);
//			     return false;
//			    }
//			}  
		  
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
	       
	      //-----------------------------Datos del rio----------------------------------------
	       sql = null;
		   //String geometry = getGeometryInfo();
		   if(geomInfo != null)
		     sql = "update rios set the_geom="+geomInfo;
		   
		   if(ancho != null)
		   {
			if(sql == null)
		     sql = "update rios set ancho="+ancho;
			else
			 sql += ", ancho="+ancho; 
		   }
		   
		   if(orden != null)
		   {
			if(sql == null)
		     sql = "update rios set orden="+orden;
			else
			 sql += ", orden="+orden; 
		   }
		   
		   if(sql != null)
		   {	   
			sql += " where gid="+gid;
			db.ejecutarConsulta(sql);
			TrazasManager.insertar_Traza("Actualizó el río con identificador "+gid);
			
		    return true;
		   }
		   else
		    return false;	
	  }
	  
       } catch (ProcessVisitorException e) {
		   e.printStackTrace();
		return false;
	   }  
	 } 
		 
		return false;
	}
	
}