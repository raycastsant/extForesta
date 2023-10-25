package org.geocuba.foresta.gestion_datos;

import javax.swing.JOptionPane;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;

import com.iver.cit.gvsig.exceptions.visitors.ProcessVisitorException;

public class Faja extends PersistentGeometricObject{
	
	private Integer gid;
	private Hidrografia hidrografia;
	private String nombre_lugar;
	private String orilla;
	private Double ancho;
	//private HashSet<Suelo> listaSuelos = new HashSet<Suelo>();
	//private int the_geom;
	//private Fajas_real Fajas_real;   //la clase fajas_real es la que depende de la faja 
	
	public Faja(){
	 super(true);
	 this.setGid(null);
	 this.setHidrografia(null);
	 this.setNombre_Lugar(null);
	 this.setOrilla(null);
	 this.setAncho(null);
	}
	
	public Faja(int _gid, Hidrografia _hidrografia, String _nombre_lugar, String _orilla, double _ancho
			, boolean _isNewObject){
		
        super(_isNewObject);
		this.setGid(_gid);
		this.setHidrografia(_hidrografia);
		this.setNombre_Lugar(_nombre_lugar);
		this.setOrilla(_orilla);
		this.setAncho(_ancho);
	}
	
	public void setGid(Integer value) {
		this.gid = value;
	}
	
	public Integer getGid() {
		return gid;
	}
	
	public void setNombre_Lugar(String value) {
		this.nombre_lugar = value;
	}
	
	public String getNombre_Lugar() {
		return nombre_lugar;
	}
	
	public void setOrilla(String value) {
		this.orilla = value;
	}
	
	public String getOrilla() {
		return orilla;
	}
	
	public void setAncho(Double value) {
		this.ancho = value;
	}
	
	public Double getAncho() {
		return ancho;
	}
	
	public void setHidrografia(Hidrografia value) {
	 this.hidrografia = value;
	}
	
	public Hidrografia getHidrografia() {
		return hidrografia;
	}
	
	@Override
	public boolean delete() 
	{
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 db.ejecutarConsulta("delete from _suelos_fajas where faja="+gid);
		 db.ejecutarConsulta("delete from fajas where gid="+gid);
		 TrazasManager.insertar_Traza("Eliminó la faja con identificador "+gid);
	     
		 return true;
	}

	@Override
	public boolean save() 
	{
		 boolean isOk = false;
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 try {
			 
		 if(isNewObject)   //-----------INSERTO-----------------------------------------------------------------
		 {
	      String geomInfo = getGeometryInfo();		 
		  String columnas = "id, nombre_lugar, orilla, ancho, the_geom";
		  String values = hidrografia.getIdHidrografia()+",'"+nombre_lugar+"','"+orilla+"',"+ancho+","+geomInfo;
		
		  String sql = "insert into fajas("+columnas+") values("+values+")";
		  db.ejecutarConsulta(sql);
		  
	      /*Actualizo los suelos que se intersecten*/ 	  	   
		  db.ejecutarConsulta("select max(gid) from fajas");
		  gid = db.getValueAsInteger(0,0);
		  
//		    boolean correct = validarGeometria(db, geomInfo, gid, "fajas", getType());
//		    if(!correct)
//		    {
//		     JOptionPane.showMessageDialog(null, "El polígono digitalizado es incorrecto", "Error dibujando elemento", JOptionPane.ERROR_MESSAGE);
//		     return false;
//		    }
		    
		  db.ejecutarConsulta("select _suelos.gid from _suelos inner join fajas on st_intersects(" +
		  		"_suelos.the_geom, fajas.the_geom) where fajas.gid="+gid);
		  if(!db.isEmpty())
		  {
		   int []idsuelo = new int[db.getRowCount()];	  
		   for(int i=0; i<db.getRowCount(); i++)
			idsuelo[i] = db.getValueAsInteger(i,0);
		   
		   for(int i=0; i<idsuelo.length; i++)
		   {
			db.ejecutarConsulta("select * from _suelos_fajas where suelo="+idsuelo[i]+" and faja="+gid);
			if(db.isEmpty())
			 db.ejecutarConsulta("insert into _suelos_fajas(suelo,faja) values("+idsuelo[i]+","+gid+")");
		   }	
		  }	  
		  
		  TrazasManager.insertar_Traza("Insertó la faja con identificador "+gid);
		  
		  isOk = true;
		 }
		 else    //--------------MODIFICO--------------------------------------------------------------
		 {
			   String sql = null;
		       if(nombre_lugar != null)
			    sql = "update fajas set nombre_lugar='"+nombre_lugar+"'";
			   
		       if(orilla != null)
			   {
				if(sql == null)
			     sql = "update fajas set orilla='"+orilla+"'";
				else
				 sql += ", orilla='"+orilla+"'"; 
			   }
		       
		       if(ancho != null)
			   {
				if(sql == null)
			     sql = "update fajas set ancho="+ancho;
				else
				 sql += ", ancho="+ancho; 
			   }
		       
			   String geometry = getGeometryInfo();
			   if(geometry != null)
			   {
				if(sql == null)
			     sql = "update fajas set the_geom="+geometry;
				else
				 sql += ", the_geom="+geometry; 
			   }
			   
			   if(sql != null)
			   {	   
				sql += " where gid="+gid;
				db.ejecutarConsulta(sql);
				
//				if(geometry !=null)
//				{
//					boolean correct = validarGeometria(db, geometry, gid, "fajas", getType());
//				    if(!correct)
//				    {
//				     JOptionPane.showMessageDialog(null, "El polígono digitalizado es incorrecto", "Error dibujando elemento", JOptionPane.ERROR_MESSAGE);
//				     return false;
//				    }
//				}   
			    
				TrazasManager.insertar_Traza("Actualizó la faja con identificador "+gid);
				
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
