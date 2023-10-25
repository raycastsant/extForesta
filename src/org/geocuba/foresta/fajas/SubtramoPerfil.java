package org.geocuba.foresta.fajas;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.ParcelaManager;
import org.geocuba.foresta.gestion_datos.PersistentGeometricObject;
import org.geocuba.foresta.gestion_datos.Parcela;
import org.geocuba.foresta.gestion_datos.Suelo;
import org.geocuba.foresta.gestion_datos.SueloManager;
import org.geocuba.foresta.gestion_datos.Velocidad_infiltracion;
import org.geocuba.foresta.gestion_datos.Velocidad_infiltracionManager;
import com.iver.cit.gvsig.exceptions.visitors.ProcessVisitorException;

 /**Clase para almacenar los datos de un subtramo de perfil.
  * Pensada para utilizarse en el método Herrero-Melchanov*/
 public class SubtramoPerfil extends PersistentGeometricObject 
 {
  private Integer gid;
  private Double pendiente;
//  private Suelo suelo;
  private Integer perfil;
  private Double vel_infiltr_suelo;
  private Parcela parcela;
  private Suelo suelo = null;
  private double longHorizontal;  
  
  public SubtramoPerfil(int _gid, double _pendiente, Integer _perfil, Double _vel_infiltr_suelo, Parcela _parcela, double _distHorizontal, boolean isNew)
  {
   super(isNew);
   gid = _gid;
   pendiente = _pendiente;
//   suelo = _suelo;
   perfil = _perfil;
   vel_infiltr_suelo = _vel_infiltr_suelo;
   parcela = _parcela;
   longHorizontal = _distHorizontal;
  }
  
  public SubtramoPerfil()
  {
   super(true);
   gid = null;
   pendiente = null;
//   suelo = null;
   perfil = null;
   vel_infiltr_suelo = null;
   parcela = null;
  }
  
  public void setGid(Integer value){
	gid = value;
  }
	  
  public Integer getGid(){
	return gid;
  }
  
  public void setPendiente(double value){
   pendiente = new Double(value);
  }
	  
  public Double getPendiente(){
   return pendiente;
  }
  
  public void setVelocidad_infiltracion(double value){
	   vel_infiltr_suelo = new Double(value);
  }
		  
  public Double getVelocidad_infiltracion(){
	   return vel_infiltr_suelo;
  }
  
  public void setLongitud(double value){
   longHorizontal = new Double(value);
  }
		  
  public Double getLongitud(){
   return longHorizontal;
  }
  
//  public void setSuelo(Suelo value){
//	   suelo = value;
//  }
//		  
//  public Suelo getSuelo(){
//	   return suelo;
//  }

  public void setPerfil(Integer value){
	   perfil = value;
  }
		  
  public Integer getPerfil(){
	   return perfil;
  }
  
  public void setParcela(Parcela value){
	   parcela = value;
  }
		  
  public Parcela getParcela(){
	   return parcela;
  }
  
	@Override
	public boolean delete() 
	{
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 db.ejecutarConsulta("delete from sub_tramos_perfil where gid="+gid);
	     TrazasManager.insertar_Traza("Se eliminó el subtramo con identificador "+gid);
		 
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
		  String geometryInfo = getGeometryInfo();	 
		  String columnas = "perfil, pendiente, the_geom";
		  String values = perfil+","+pendiente+","+geometryInfo;
		  
//		  if(suelo!=null)
//		  {
//		   columnas += ",suelo";
//		   values += ","+suelo.getGid();
//		  } 
		  
//		   if(velocidadInfiltracion!=null && velocidadInfiltracion>=0)
//			values += ","+velocidadInfiltracion;
//		   else
//			values += ","+recalcularVelocidad_infiltracion();
		  
		  if(parcela!=null)   //Parcela
		  {
		   columnas += ",parcela";
		   values += ","+parcela.getGid();
		  } 
		  else
		  {
		   String sql = "select gid from parcelas where st_intersects(parcelas.the_geom,"+geometryInfo+")";	  
		   db.ejecutarConsulta(sql);
		   
		   if(!db.isEmpty())
		   {
			int gidparcela = db.getValueAsInteger(0,0);
			columnas += ",parcela";
			values += ","+gidparcela;
			ParcelaManager parcman = new ParcelaManager();
			parcela = parcman.Cargar_Objeto_BD(gidparcela);
		   }	   
		   else
			return false;    
		  }	 
		  
		  if(suelo!=null) //Suelo
		  {
		   columnas += ",suelo";
		   values += ","+suelo.getGid();
		  } 
		  else
		  {
		   String sql = "select gid from _suelos where st_intersects(_suelos.the_geom,"+geometryInfo+")";	  
		   db.ejecutarConsulta(sql);
		   
		   if(!db.isEmpty())
		   {
			int gidsuelo = db.getValueAsInteger(0,0);
			columnas += ",suelo";
			values += ","+gidsuelo;
			SueloManager sueloman = new SueloManager();
			suelo = sueloman.Cargar_Objeto_BD(gidsuelo);
		   }	   
		   else
			return false;    
		  }
		  
		  columnas += ",vel_infiltr_suelo";
		  if(vel_infiltr_suelo!=null && vel_infiltr_suelo>=0)
		   values += ","+vel_infiltr_suelo;
		  else
		   values += ","+recalcularVelocidad_infiltracion();
		  
		  String sql = "insert into sub_tramos_perfil("+columnas+") values("+values+")";
		  db.ejecutarConsulta(sql);
		  System.out.println(sql);
		  
		  db.ejecutarConsulta("select max(gid) from sub_tramos_perfil");
		  gid = db.getValueAsInteger(0,0);;
		  //TrazasManager.insertar_Traza("Se insertó el subtramo con identificador "+gid);
		  
		  isOk = true;  
		 }
		 else    //---------------ACTUALIZAR-------------------------------------------------------------
		 {
			   String sql = null;
		       if(pendiente != null)
			    sql = "update sub_tramos_perfil set pendiente="+pendiente;
			   
//		       if(suelo != null)
//			   {
//				if(sql == null)
//			     sql = "update sub_tramos_perfil set suelo="+suelo.getGid();
//				else
//				 sql += ", suelo="+suelo.getGid();
//			   }
		       
		       if(perfil != null)
			   {
				if(sql == null)
			     sql = "update sub_tramos_perfil set perfil="+perfil;
				else
				 sql += ", perfil="+perfil;
			   }
		       
		       if(vel_infiltr_suelo != null)
			   {
				if(sql == null)
			     sql = "update sub_tramos_perfil set vel_infiltr_suelo="+vel_infiltr_suelo;
				else
				 sql += ", vel_infiltr_suelo="+vel_infiltr_suelo;
			   }
		       
		       if(parcela != null)
			   {
				if(sql == null)
			     sql = "update sub_tramos_perfil set parcela="+parcela.getGid();
				else
				 sql += ", parcela="+parcela.getGid();
			   }
		       
		       String geometry = getGeometryInfo();
			   if(geometry != null)
			   {
				if(sql == null)
			     sql = "update sub_tramos_perfil set the_geom="+geometry;
				else
				 sql += ", the_geom="+geometry; 
			   }
			   
			   if(sql != null)
			   {	   
				sql += " where gid="+gid; 
			    db.ejecutarConsulta(sql);
			    TrazasManager.insertar_Traza("Se actualizó el subtramo con identificador "+gid);
			    
			    return true;
			   }
			   else
			    return false;	 
		 }
		 
		 } catch (ProcessVisitorException e) {
				e.printStackTrace();
				return false;
			}
				 
		 return isOk;
	}
	
	public double recalcularVelocidad_infiltracion()
	{
		if(parcela== null || suelo==null)
		 return 0;	
		
		Double profundidadefectiva = suelo.getProfundidadefectiva();
		Integer materiaorganica = suelo.getMateriaorganica();
		String textura = suelo.getTextura().getTextura();
		String estructura = suelo.getEstructura();
		
		//Velocidad de infiltración
		   String valor = "";
		   if(profundidadefectiva == null)
			profundidadefectiva = new Double(0);
		   
		   if(materiaorganica == null)
			materiaorganica = 0;
		   
		   if((profundidadefectiva>50) && 
			  (textura.equals("Arenosa") || textura.equals("Loam") || 
			  estructura.equals("Desarrollada") || materiaorganica>3))
			        valor = "Máximos";
		   else
		   if((profundidadefectiva>=25 && profundidadefectiva<=50) && 
		     (textura.equals("Loam arcilloso") || 
		     estructura.equals("Medianamente desarrollada") ||
		     (materiaorganica>=1 && materiaorganica<=3)) )
					valor = "Medios";
		   else
		   if((profundidadefectiva<25) && 
			 (textura.equals("Arcillosa") || estructura.equals("Poco desarrollada") || 
			  materiaorganica<1 ))
			        valor = "Mínimos";
		   
		   Velocidad_infiltracionManager velManager = new Velocidad_infiltracionManager();
		   Velocidad_infiltracion vel = velManager.Cargar_Objeto_BD("where uso="+parcela.getUso_catastro().getUso_suelo().getId()+" and " +
		   		"clase='"+valor+"'");
		   
		   if(vel != null)
			return vel.getVelocidad();
		   else
			return 0;   
	}
 }
