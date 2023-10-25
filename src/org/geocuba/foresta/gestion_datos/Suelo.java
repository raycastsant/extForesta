package org.geocuba.foresta.gestion_datos;

import javax.swing.JOptionPane;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import com.iver.cit.gvsig.exceptions.visitors.ProcessVisitorException;

public class Suelo extends PersistentGeometricObject 
{
	private Integer gid;
	private Textura_suelos textura;
	private Tipo_suelo tipo;
	//private Double velocidadInfiltracion;
	private String erosion;
	private Integer materiaorganica;
	private Double profundidadefectiva;
	private String estructura;
	//private Uso_suelo uso;
	private Double ph;
	private Double pendiente;
	private Double gravas;
	private Double piedras;
	private Double rocas;
	//private int the_geom;
	
	public Suelo(Integer _gid, Textura_suelos _textura, Tipo_suelo _tipo, 
			String _erosion, Integer _materiaorganica, Double _profundidadefectiva, String _estructura, Double _ph, 
			Double _pendiente, Double _gravas, Double _piedras, Double _rocas, boolean isNewObject) 
	{
		super(isNewObject);
		this.gid = _gid;
		this.textura = _textura;
		this.tipo = _tipo;
		//this.velocidadInfiltracion = _velocidadInfiltracion;
		this.erosion = _erosion;
		this.materiaorganica = _materiaorganica;
		this.profundidadefectiva = _profundidadefectiva;
		this.estructura = _estructura;
		this.ph = _ph;
		this.pendiente = _pendiente;
		this.gravas = _gravas;
		this.piedras = _piedras;
		this.rocas = _rocas;
		//this.uso = _uso;
	}
	
	public Suelo(boolean isNewObject) 
	{
		super(isNewObject);
		this.gid = null;
		this.textura = null;
		this.tipo = null;
		//this.velocidadInfiltracion = null;
		this.erosion = null;
		this.materiaorganica = null;
		this.profundidadefectiva = null;
		this.estructura = null;
		this.ph = null;
		this.pendiente = null;
		this.gravas = null;
		this.piedras = null;
		this.rocas = null;
	}
	
	public Suelo() 
	{
		super(true);
		this.gid = null;
		this.textura = null;
		this.tipo = null;
		//this.velocidadInfiltracion = null;
		this.erosion = null;
		this.materiaorganica = null;
		this.profundidadefectiva = null;
		this.estructura = null;
		this.ph = null;
		this.pendiente = null;
		this.gravas = null;
		this.piedras = null;
		this.rocas = null;
	}
	
	public Integer getGid() {
		return gid;
	}
	
	public void setGid(int valor) {
	 this.gid = valor;
	}
	
	public void setErosion(String value) {
		this.erosion = value;
	}
	
	public String getErosion() {
		return erosion;
	}
	
	public void setMateriaorganica(int value) {
		setMateriaorganica(new Integer(value));
	}
	
	public void setMateriaorganica(Integer value) {
		this.materiaorganica = value;
	}
	
	public Integer getMateriaorganica() {
		return materiaorganica;
	}
	
	public void setProfundidadefectiva(double value) {
		setProfundidadefectiva(new Double(value));
	}
	
	public void setProfundidadefectiva(Double value) {
		this.profundidadefectiva = value;
	}
	
	public Double getProfundidadefectiva() {
		return profundidadefectiva;
	}
	
	public void setEstructura(String value) {
		this.estructura = value;
	}
	
	public String getEstructura() {
		return estructura;
	}
	
	public void setPh(double value) {
		setPh(new Double(value));
	}
	
	public void setPh(Double value) {
		this.ph = value;
	}
	
	public Double getPh() {
		return ph;
	}
	
	public void setPendiente(double value) {
		setPendiente(new Double(value));
	}
	
	public void setPendiente(Double value) {
		this.pendiente = value;
	}
	
	public Double getPendiente() {
		return pendiente;
	}
	
	public void setGravas(double value) {
		setGravas(new Double(value));
	}
	
	public void setGravas(Double value) {
		this.gravas = value;
	}
	
	public Double getGravas() {
		return gravas;
	}
	
	public void setPiedras(double value) {
		setPiedras(new Double(value));
	}
	
	public void setPiedras(Double value) {
		this.piedras = value;
	}
	
	public Double getPiedras() {
		return piedras;
	}
	
	public void setRocas(double value) {
		setRocas(new Double(value));
	}
	
	public void setRocas(Double value) {
		this.rocas = value;
	}
	
	public Double getRocas() {
		return rocas;
	}
	
	public void setTextura(Textura_suelos value) 
	{
	 this.textura = value;	
	}
	
	public Textura_suelos getTextura() {
		return textura;
	}
	
	public void setTipo_suelo(Tipo_suelo value) 
	{
	 this.tipo = value;	
	}
	
	public Tipo_suelo getTipo_suelo() {
		return tipo;
	}
	
//	public void setVelocidad_Infiltracion(double value) 
//	{
//	 this.velocidadInfiltracion = value;	
//	}
//	
//	public Double getVelocidad_Infiltracion() {
//		return velocidadInfiltracion;
//	}
	
//	public void setUso_suelo(Uso_suelo value) {
//		this.uso = value;
//	}
//	
//	public Uso_suelo getUso_suelo() {
//		return uso;
//	}
	
	@Override
	public boolean delete() {
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 db.ejecutarConsulta("delete from parcelas_suelos where suelo="+gid);
		 db.ejecutarConsulta("delete from suelos_fajas where suelo="+gid);
		 db.ejecutarConsulta("delete from _suelos where gid="+gid);
		 TrazasManager.insertar_Traza("Eliminó el suelo con identificador "+gid);
	     
		 return true;
	}

	@Override
	public boolean save() 
	{
		 boolean isOk = false;
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 
	   try {			 
		 if(isNewObject)   //----------INSERTAR NUEVO------------------------------------------------------------------
		 {
			 String geomInfo = getGeometryInfo();
			 String columnas = "erosion, materiaorganica, profundidadefectiva, estructura, ph, pendiente, gravas, piedras, rocas, the_geom";
			 String values = "'"+erosion+"',"+materiaorganica+","+profundidadefectiva+",'"+estructura+"',"+ph+","+pendiente+","+gravas+","+piedras+","+rocas+","+geomInfo;
		  
		  if(textura!=null)
		  {
		   columnas += ",textura";
		   values += ","+textura.getId();
		  }
		  
//		  if(uso!=null)
//		  {
//		   columnas += ",uso";
//		   values += ","+uso.getId();
//		  }
		  
		  if(tipo!=null)
		  {
		   columnas += ",tipo";
		   values += ","+tipo.getID();
		  }
		  
//		   columnas += ",velocidad_infiltracion";
//		   if(velocidadInfiltracion!=null && velocidadInfiltracion>=0)
//			values += ","+velocidadInfiltracion;
//		   else
//			values += ","+recalcularVelocidad_infiltracion();
		   
		  ////////////////////////////////////////////////////////////////////////////////////
		  String sql = "insert into _suelos("+columnas+") values("+values+")";
		  db.ejecutarConsulta(sql);
		  System.out.println(sql);
		  
		  /*Actualizando elementos que se intersecten*/
		  db.ejecutarConsulta("select max(gid) from _suelos");
		  gid = db.getValueAsInteger(0,0);;
		  JDBCAdapter dbaux = new JDBCAdapter(ConnectionExt.getConexionActiva());
		  
//		    boolean correct = validarGeometria(db, geomInfo, gid, "_suelos", getType());
//		    if(!correct)
//		    {
//		     JOptionPane.showMessageDialog(null, "El polígono digitalizado es incorrecto", "Error dibujando elemento", JOptionPane.ERROR_MESSAGE);
//		     return false;
//		    }
		  
		  //Fajas
		   db.ejecutarConsulta("select fajas.gid from fajas inner join _suelos on st_intersects(" +
		  		"fajas.the_geom, _suelos.the_geom) where _suelos.gid="+gid);
		   if(!db.isEmpty())
		   {
		    for(int i=0; i<db.getRowCount(); i++)
		    {
		     int idFaja = db.getValueAsInteger(i,0); 	
		     db.ejecutarConsulta("select * from _suelos_fajas where suelo="+gid+" and faja="+idFaja);
			 if(db.isEmpty())
			  dbaux.ejecutarConsulta("insert into suelos_fajas(suelo,faja) values("+gid+","+idFaja+")");
		    } 
		   }
		   
		 //Parcelas
		   db.ejecutarConsulta("select Parcelas.gid from Parcelas inner join _suelos on st_intersects(" +
		  		"Parcelas.the_geom, _suelos.the_geom) where _suelos.gid="+gid);
		   if(!db.isEmpty())
		   {
		    for(int i=0; i<db.getRowCount(); i++)
		    {
		     int idParcela = db.getValueAsInteger(i,0); 	
		     db.ejecutarConsulta("select * from parcelas_suelos where suelo="+gid+" and parcela="+idParcela);
			 if(db.isEmpty())
			  dbaux.ejecutarConsulta("insert into parcelas_suelos(suelo,parcela) values("+gid+","+idParcela+")");
		    } 
		   }
		   
		  TrazasManager.insertar_Traza("Insertó el suelo con identificador "+gid); 
		   
		  isOk = true;   
		 }
		 else    //--------------------Modificar--------------------------------------------------------
		 {
			   String sql = null;
			   String geometry = getGeometryInfo();
			   if(geometry != null)
			    sql = "update _suelos set the_geom="+geometry;
			   
			   if(textura != null)
			   {
				if(sql == null)
			     sql = "update _suelos set textura="+textura.getId();
				else
				 sql += ", textura="+textura.getId();
			   }
			   
			   if(tipo != null)
			   {
				if(sql == null)
			     sql = "update _suelos set tipo="+tipo.getID();
				else
				 sql += ", tipo="+tipo.getID();
			   }
			   
//			   if(uso != null)
//			   {
//				if(sql == null)
//			     sql = "update suelos set uso="+uso.getId();
//				else
//				 sql += ", uso="+uso.getId();
//			   }
			   
//			   if(velocidadInfiltracion != null)
//			   {
//				if(sql == null)
//			     sql = "update suelos set velocidad_infiltracion="+velocidadInfiltracion;
//				else
//				 sql += ", velocidad_infiltracion="+velocidadInfiltracion;
//			   }
			   
			   if(erosion != null)
			   {
				if(sql == null)
			     sql = "update _suelos set erosion='"+erosion+"'";
				else
				 sql += ", erosion='"+erosion+"'";
			   }
			   
			   if(materiaorganica != null)
			   {
				if(sql == null)
			     sql = "update _suelos set materiaorganica="+materiaorganica;
				else
				 sql += ", materiaorganica="+materiaorganica;
			   }
			   
			   if(profundidadefectiva != null)
			   {
				if(sql == null)
			     sql = "update _suelos set profundidadefectiva="+profundidadefectiva;
				else
				 sql += ", profundidadefectiva="+profundidadefectiva;
			   }
			   
			   if(estructura != null)
			   {
				if(sql == null)
			     sql = "update _suelos set estructura='"+estructura+"'";
				else
				 sql += ", estructura='"+estructura+"'";
			   }
			   
			   if(ph != null)
			   {
				if(sql == null)
			     sql = "update _suelos set ph="+ph;
				else
				 sql += ", ph="+ph;
			   }
			   
			   if(pendiente != null)
			   {
				if(sql == null)
			     sql = "update _suelos set pendiente="+pendiente;
				else
				 sql += ", pendiente="+pendiente;
			   }
			   
			   if(gravas != null)
			   {
				if(sql == null)
			     sql = "update _suelos set gravas="+gravas;
				else
				 sql += ", gravas="+gravas;
			   }
			   
			   if(piedras != null)
			   {
				if(sql == null)
			     sql = "update _suelos set piedras="+piedras;
				else
				 sql += ", piedras="+piedras;
			   }
			   
			   if(rocas != null)
			   {
				if(sql == null)
			     sql = "update _suelos set rocas="+rocas;
				else
				 sql += ", rocas="+rocas;
			   }
			   
			   if(sql != null)
			   {	   
				sql += " where gid="+gid;
				db.ejecutarConsulta(sql);
				
//				if(geometry != null)
//				{	
//				    boolean correct = validarGeometria(db, geometry, gid, "_suelos", getType());
//				    if(!correct)
//				    {
//				     JOptionPane.showMessageDialog(null, "El polígono digitalizado es incorrecto", "Error dibujando elemento", JOptionPane.ERROR_MESSAGE);
//				     return false;
//				    }
//				}    
				    
				TrazasManager.insertar_Traza("Actualizó el suelo con identificador "+gid);
				
			    return true;
			   }
			   else
			    return false;	 
		 }
		 
		      } catch (ProcessVisitorException e) {
				    e.printStackTrace();
			  }
				 
		 return isOk;
	}
	
//	public double recalcularVelocidad_infiltracion()
//	{
//		if(uso == null)
//		 return -1;	
//			
//		//Velocidad de infiltración
//		   String valor = "";
//		   if(profundidadefectiva == null)
//			profundidadefectiva = new Double(0);
//		   
//		   if(materiaorganica == null)
//			materiaorganica = 0;
//		   
//		   if((profundidadefectiva>50) && 
//			 (textura.getTextura().equals("Arenosa")||textura.getTextura().equals("Loam")) && 
//			 (estructura.equals("Desarrollada")) && 
//			 (materiaorganica>3) )
//			        valor = "Máximos";
//		   else
//		   if((profundidadefectiva>=25 && profundidadefectiva<=50) && 
//		     (textura.getTextura().equals("Loam arcilloso")) && 
//		     (estructura.equals("Medianamente desarrollada")) && 
//		     (materiaorganica>=1 && materiaorganica<=3) )
//					valor = "Medios";
//		   else
//		   if((profundidadefectiva<25) && 
//			 (textura.getTextura().equals("Arcillosa")) && 
//			 (estructura.equals("Poco desarrollada")) && 
//			 (materiaorganica<1) )
//			        valor = "Mínimos";
//		   else
//			valor = "Mínimos";   //!!OJO!!! REGLA NO PROBADA
//		   
//		   Velocidad_infiltracionManager velManager = new Velocidad_infiltracionManager();
//		   Velocidad_infiltracion vel = velManager.Cargar_Objeto_BD("where uso="+uso.getId()+" and " +
//		   		"clase='"+valor+"' and estructura='"+estructura+"'");
//		   if(vel != null)
//			return vel.getVelocidad();
//		   else
//			return -1;   
//	}
	
}
