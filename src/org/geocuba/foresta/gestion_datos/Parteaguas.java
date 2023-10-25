package org.geocuba.foresta.gestion_datos;

import javax.swing.JOptionPane;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;

import com.iver.cit.gvsig.exceptions.visitors.ProcessVisitorException;

public class Parteaguas extends PersistentGeometricObject{
	
	private Integer gid;
	private Cuenca cuenca;
	private String descripcion;
//	private int the_geom;
	//private HashSet<Faja> listaFajas = new HashSet<Faja>();
	//private HashSet<Perfil> listaPerfiles = new HashSet<Perfil>();
	//private HashSet<Hidrografia> listaHidrografias = new HashSet<Hidrografia>();
	///private HashSet<Red_drenaje> listaRed_drenaje = new HashSet<Red_drenaje>();
	
	public Parteaguas() {
	 super(true);	
	 gid = null;
	 cuenca = null;
	 descripcion = null;
	}
	
	public Parteaguas(int _gid, Cuenca _cuenca, String _descripcion, boolean isNewObject) 
	{
		super(isNewObject);
		this.gid = _gid;
		this.cuenca = _cuenca;
		this.descripcion = _descripcion;
	}
	
	public void setGid(int value) {
		this.gid = value;
	}
	
	public Integer getGid() {
		return gid;
	}
	
	public void setDescripcion(String value) {
		this.descripcion = value;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setCuenca(Cuenca value) {
		this.cuenca = value;
	}
	
	public Cuenca getCuenca() {
		return cuenca;
	}
	
	@Override
	public boolean save() 
	{
		JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		try {
			
		  if(isNewObject)   //-------INSERTAR---------------------------------------------------------------------
		  {
		   String columnas = "descripcion, cuenca";
		   String values = "'"+descripcion+"'";
		   
		   if(cuenca!=null)
			values += ","+cuenca.getGid();
		   else     //Si cuenca esta vacio tengo que buscar la que se intersecta con el elemento
		   {
			String sql = "select gid from cuencas where st_intersects(cuencas.the_geom, "+getGeometryInfo()+")";
			 
			db.ejecutarConsulta(sql);
			System.out.println(sql);
			
			if(db.isEmpty())
			{
		     JOptionPane.showMessageDialog(null, "No se puede insertar el Parteaguas."+
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

		   String geomInfo = getGeometryInfo();
		   
		      columnas += ", the_geom";
		      values += ","+getGeometryInfo();
		      String sql = "insert into parteaguas("+columnas+") values("+values+")";
		      db.ejecutarConsulta(sql);
		      System.out.println(sql);
		      
		      db.ejecutarConsulta("select max(gid) from parteaguas");
		      if(!db.isEmpty())
		      {	  
		       gid = db.getValueAsInteger(0,0);
//		        boolean correct = validarGeometria(db, geomInfo, gid, "parteaguas", getType());
//			    if(!correct)
//			    {
//			     JOptionPane.showMessageDialog(null, "El polígono digitalizado es incorrecto", "Error dibujando elemento", JOptionPane.ERROR_MESSAGE);
//			     return false;
//			    } 	
		       
		       TrazasManager.insertar_Traza("Insertó el parteaguas con identificador "+gid);
		      } 
		      
		      return gid>0;
		      
		  }
		  else    //-----------------ACTUALIZAR-----------------------------------------------------------
		  {
			       String sql = null;
			       if(descripcion != null)
				    sql = "update parteaguas set descripcion='"+descripcion+"'";//, the_geom="+getGeometryInfo()+" where gid="+gid;
				   
				   String geometry = getGeometryInfo();
				   if(geometry != null)
				   {
					if(sql == null)
				     sql = "update parteaguas set the_geom="+geometry;
					else
					 sql += ", the_geom="+geometry; 
				   }
				   
				   if(sql != null)
				   {	   
					sql += " where gid="+gid; 
				    db.ejecutarConsulta(sql);
				    System.out.println(sql);
//				    if(geometry != null)
//					{
//					    boolean correct = validarGeometria(db, geometry, gid, "parteaguas", getType());
//					    if(!correct)
//					    {
//					     JOptionPane.showMessageDialog(null, "El polígono digitalizado es incorrecto", "Error dibujando elemento", JOptionPane.ERROR_MESSAGE);
//					     return false;
//					    } 	
//					}    
				    
				    TrazasManager.insertar_Traza("Actualizó el parteaguas con identificador "+gid);
				    
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
	
	@Override
	public boolean delete() 
	{
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 
	    //Verifico que no exista una Hidrografía asociada
	     JDBCAdapter dbaux = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 db.ejecutarConsulta("select id from hidrografia where parteaguas="+gid);
		 if(!db.isEmpty())
		 {
		  for(int i=0; i<db.getRowCount(); i++)
		  {	  
		   int idhidro = db.getValueAsInteger(i,0);
		   dbaux.ejecutarConsulta("update hidrografia set parteaguas=null where id="+idhidro);
		  } 
		 }	 
	 
	 db.ejecutarConsulta("delete from parteaguas where gid="+gid);
	 TrazasManager.insertar_Traza("Eliminó el parteaguas con identificador "+gid);
     
	 return true;
	}
}
