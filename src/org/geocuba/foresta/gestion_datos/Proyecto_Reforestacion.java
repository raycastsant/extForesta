package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.PersistentObject;

public class Proyecto_Reforestacion extends PersistentObject 
{
    private Integer id;
	private Boolean tenente;
	private Double area_tenente;
	private Double area_reforestar;
	private String nombre_productor;
	private String organismo;
	private String nombre_lugar;
	private String tipo_erosion_suelo;
	private String grado_erosion_suelo;
	private Double profundidad_efectiva;
	private Double ph;
	private Double materiaorganica;
	private String vegetacion;
	private String densidad;
	private String observaciones_veg;
	private Double marcox;
	private Double marcoy;
	private Integer hileras;
	private Double precipitacion;
	private String metodo_plantacion;
	private Boolean proyecto_general;
	private String especificacion;
	private String medidas_suelos;
	private String medidas_pastoreo;
	private String medidas_incendios;
	private Faja faja;
	private String nombre_tenente;
	private Especie []listaEspecies;
	private Tipo_suelo []listaSuelos;
	
	public Proyecto_Reforestacion(Integer _id, Boolean _tenente, Double _area_tenente, Double _area_reforestar,
			String _nombre_productor, String _organismo, String _nombre_lugar, String _tipo_erosion_suelo,
            String _grado_erosion_suelo, Double _profundidad_efectiva, Double _ph, Double _materiaorganica, String _vegetacion, 
            String _densidad, String _observaciones_veg, Double _marcox, Double _marcoy, Integer _hileras, Double _precipitacion,
            String _metodo_plantacion, Boolean _proyecto_general, String _especificacion, String _medidas_suelos, 
            String _medidas_pastoreo, String _medidas_incendios, Faja _faja, String _nombre_tenente, Especie []_listaEspecies, Tipo_suelo []_listasuelos, boolean isNew) 
	{
	 super(isNew);	
	  id = _id;
	  tenente = _tenente;
	  area_tenente = _area_tenente;
	  area_reforestar = _area_reforestar;
	  nombre_productor = _nombre_productor;
	  organismo = _organismo;
	  nombre_lugar = _nombre_lugar;
	  tipo_erosion_suelo = _tipo_erosion_suelo;
	  grado_erosion_suelo = _grado_erosion_suelo;
	  profundidad_efectiva = _profundidad_efectiva;
	  ph = _ph;
	  materiaorganica = _materiaorganica;
	  vegetacion = _vegetacion;
	  densidad = _densidad;
	  observaciones_veg = _observaciones_veg;
	  marcox = _marcox;
	  marcoy = _marcoy;
	  hileras = _hileras;
	  precipitacion = _precipitacion;
	  metodo_plantacion = _metodo_plantacion;
	  proyecto_general = _proyecto_general;
	  especificacion = _especificacion;
	  medidas_suelos = _medidas_suelos;
	  medidas_pastoreo = _medidas_pastoreo;
	  medidas_incendios = _medidas_incendios;
	  faja = _faja;
	  nombre_tenente = _nombre_tenente;
	  listaEspecies = _listaEspecies;
	  listaSuelos = _listasuelos;
	}
	
	public Proyecto_Reforestacion() 
	{
	  super(true);	
	  id = null;
	  tenente = null;
	  area_tenente = null;
	  area_reforestar = null;
	  nombre_productor = null;
	  organismo = null;
	  nombre_lugar = null;
	  tipo_erosion_suelo = null;
	  grado_erosion_suelo = null;
	  profundidad_efectiva = null;
	  ph = null;
	  materiaorganica = null;
	  vegetacion = null;
	  densidad = null;
	  observaciones_veg = null;
	  marcox = null;
	  marcoy = null;
	  hileras = null;
	  precipitacion = null;
	  metodo_plantacion = null;
	  proyecto_general = null;
	  especificacion = null;
	  medidas_suelos = null;
	  medidas_pastoreo = null;
	  medidas_incendios = null;
	  faja = null;
	  listaEspecies = null;
	  listaSuelos = null;
	}

    public Double getArea_reforestar() {
        return area_reforestar;
    }

    public Double getArea_tenente() {
        return area_tenente;
    }

    public String getDensidad() {
        return densidad;
    }

    public String getEspecificacion() {
        return especificacion;
    }

    public Faja getFaja() {
        return faja;
    }

    public String getGrado_erosion_suelo() {
        return grado_erosion_suelo;
    }

    public Integer getHileras() {
        return hileras;
    }

    public Integer getId() {
        return id;
    }

    public Double getMarcox() {
        return marcox;
    }

    public Double getMarcoy() {
        return marcoy;
    }

    public Double getMateriaorganica() {
        return materiaorganica;
    }

    public String getMedidas_incendios() {
        return medidas_incendios;
    }

    public String getMedidas_pastoreo() {
        return medidas_pastoreo;
    }

    public String getMedidas_suelos() {
        return medidas_suelos;
    }

    public String getMetodo_plantacion() {
        return metodo_plantacion;
    }

    public String getNombre_lugar() {
        return nombre_lugar;
    }

    public String getNombre_productor() {
        return nombre_productor;
    }

    public String getObservaciones_veg() {
        return observaciones_veg;
    }

    public String getOrganismo() {
        return organismo;
    }

    public Double getPh() {
        return ph;
    }

    public Double getPrecipitacion() {
        return precipitacion;
    }

    public Double getProfundidad_efectiva() {
        return profundidad_efectiva;
    }

    public Boolean getProyecto_general() {
        return proyecto_general;
    }

    public Boolean IsTenente() {
        return tenente;
    }

    public String getTipo_erosion_suelo() {
        return tipo_erosion_suelo;
    }

    public String getVegetacion() {
        return vegetacion;
    }

    public void setArea_reforestar(Double area_reforestar) {
        this.area_reforestar = area_reforestar;
    }

    public void setArea_tenente(Double area_tenente) {
        this.area_tenente = area_tenente;
    }

    public void setDensidad(String densidad) {
        this.densidad = densidad;
    }

    public void setEspecificacion(String especificacion) {
        this.especificacion = especificacion;
    }

    public void setFaja(Faja faja) {
        this.faja = faja;
    }

    public void setGrado_erosion_suelo(String grado_erosion_suelo) {
        this.grado_erosion_suelo = grado_erosion_suelo;
    }

    public void setHileras(Integer hileras) {
        this.hileras = hileras;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setMarcox(Double marcox) {
        this.marcox = marcox;
    }

    public void setMarcoy(Double marcoy) {
        this.marcoy = marcoy;
    }

    public void setMateriaorganica(Double materiaorganica) {
        this.materiaorganica = materiaorganica;
    }

    public void setMedidas_incendios(String medidas_incendios) {
        this.medidas_incendios = medidas_incendios;
    }

    public void setMedidas_pastoreo(String medidas_pastoreo) {
        this.medidas_pastoreo = medidas_pastoreo;
    }

    public void setMedidas_suelos(String medidas_suelos) {
        this.medidas_suelos = medidas_suelos;
    }

    public void setMetodo_plantacion(String metodo_plantacion) {
        this.metodo_plantacion = metodo_plantacion;
    }

    public void setNombre_lugar(String nombre_lugar) {
        this.nombre_lugar = nombre_lugar;
    }

    public void setNombre_productor(String nombre_productor) {
        this.nombre_productor = nombre_productor;
    }

    public void setObservaciones_veg(String observaciones_veg) {
        this.observaciones_veg = observaciones_veg;
    }

    public void setOrganismo(String organismo) {
        this.organismo = organismo;
    }

    public void setPh(Double ph) {
        this.ph = ph;
    }

    public void setPrecipitacion(Double precipitacion) {
        this.precipitacion = precipitacion;
    }

    public void setProfundidad_efectiva(Double profundidad_efectiva) {
        this.profundidad_efectiva = profundidad_efectiva;
    }

    public void setProyecto_general(Boolean proyecto_general) {
        this.proyecto_general = proyecto_general;
    }

    public void setTenente(Boolean tenente) {
        this.tenente = tenente;
    }

    public void setTipo_erosion_suelo(String tipo_erosion_suelo) {
        this.tipo_erosion_suelo = tipo_erosion_suelo;
    }

    public void setVegetacion(String vegetacion) {
        this.vegetacion = vegetacion;
    }
    
    public String getNombre_tenente() {
        return nombre_tenente;
    }

    public void setNombre_tenente(String value) {
    	this.nombre_tenente = value;
    }
    
    public Especie[] getListaespecies() {
        return listaEspecies;
    }

    public void setListaEspecies(Especie[] value) {
    	this.listaEspecies = value;
    }
    
    public Tipo_suelo[] getListaSuelos() {
        return listaSuelos;
    }

    public void setListaSuelos(Tipo_suelo[] value) {
    	this.listaSuelos = value;
    }
	
	@Override
	public boolean save() 
	{
	 boolean isOk = false;
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 
	 if(isNewObject)   //-----------INSERTO-----------------------------------------------------------------
	 {
	   String columnas = "tenente";
	   String values = tenente.toString();
	   
	   if(area_tenente!=null)
	   {
		   columnas += ",area_tenente";
		   values += ","+area_tenente;
	   }
	   
	   if(area_reforestar!=null)
	   {
		   columnas += ",area_reforestar";
		   values += ","+area_reforestar;
	   }
	   
	   if(nombre_productor!=null)
	   {
		   columnas += ",nombre_productor";
		   values += ",'"+nombre_productor+"'";
	   }
	   
	   if(organismo!=null)
	   {
		   columnas += ",organismo";
		   values += ",'"+organismo+"'";
	   }
	   
	   if(nombre_lugar!=null)
	   {
		   columnas += ",nombre_lugar";
		   values += ",'"+nombre_lugar+"'";
	   }
	   
	   if(tipo_erosion_suelo!=null)
	   {
		   columnas += ",tipo_erosion_suelo";
		   values += ",'"+tipo_erosion_suelo+"'";
	   }
	   
	   if(grado_erosion_suelo!=null)
	   {
		   columnas += ",grado_erosion_suelo";
		   values += ",'"+grado_erosion_suelo+"'";
	   }
	   
	   if(profundidad_efectiva!=null)
	   {
		   columnas += ",profundidad_efectiva";
		   values += ","+profundidad_efectiva;
	   }
	   
	   if(ph!=null)
	   {
		   columnas += ",ph";
		   values += ","+ph;
	   }
	   
	   if(materiaorganica!=null)
	   {
		   columnas += ",materiaorganica";
		   values += ","+materiaorganica;
	   }
	   
	   if(vegetacion!=null)
	   {
		   columnas += ",vegetacion";
		   values += ",'"+vegetacion+"'";
	   }
	   
	   if(densidad!=null)
	   {
		   columnas += ",densidad";
		   values += ",'"+densidad+"'";
	   }
	   
	   if(observaciones_veg!=null)
	   {
		   columnas += ",observaciones_veg";
		   values += ",'"+observaciones_veg+"'";
	   }
	   
	   if(marcox!=null)
	   {
		   columnas += ",marcox";
		   values += ","+marcox;
	   }
	   
	   if(marcoy!=null)
	   {
		   columnas += ",marcoy";
		   values += ","+marcoy;
	   }
	   
	   if(hileras!=null)
	   {
		   columnas += ",hileras";
		   values += ","+hileras;
	   }
	   
	   if(precipitacion!=null)
	   {
		   columnas += ",precipitacion";
		   values += ","+precipitacion;
	   }
	   
	   if(metodo_plantacion!=null)
	   {
		   columnas += ",metodo_plantacion";
		   values += ",'"+metodo_plantacion+"'";
	   }
	   
	   if(proyecto_general!=null)
	   {
		   columnas += ",proyecto_general";
		   values += ","+proyecto_general;
	   }
	   
	   if(especificacion!=null)
	   {
		   columnas += ",especificacion";
		   values += ",'"+especificacion+"'";
	   }
	   
	   if(medidas_suelos!=null)
	   {
		   columnas += ",medidas_suelos";
		   values += ",'"+medidas_suelos+"'";
	   }
	   
	   if(medidas_pastoreo!=null)
	   {
		   columnas += ",medidas_pastoreo";
		   values += ",'"+medidas_pastoreo+"'";
	   }
	   
	   if(medidas_incendios!=null)
	   {
		   columnas += ",medidas_incendios";
		   values += ",'"+medidas_incendios+"'";
	   }
	   
	   if(faja!=null)
	   {
		   columnas += ",faja";
		   values += ","+faja.getGid();
	   }
	   
	   if(nombre_tenente!=null)
	   {
		   columnas += ",nombre_tenente";
		   values += ",'"+nombre_tenente+"'";
	   }
	   
	   String sql = "insert into proyecto("+columnas+") values("+values+")";
	   db.ejecutarConsulta(sql);
	   System.out.println(sql);
	   
	   db.ejecutarConsulta("select max(id) from proyecto");
	   id = db.getValueAsInteger(0,0);
	   
	   actualizarEspecies();
	   actualizarTiposSuelos();
	   
	   TrazasManager.insertar_Traza("Insertó el proyecto de reforestación con identificador "+id);
	   
	   isOk = true;
	 }
	 else    //--------------MODIFICO--------------------------------------------------------------
	 {
	  String sql = null;
	  if(tenente != null)
	   sql = "update proyecto set tenente="+tenente.toString();
	  
	  if(area_tenente!=null)
	   {
		  if(sql == null)
			sql = "update proyecto set area_tenente="+area_tenente;
		  else
		    sql += ", area_tenente="+area_tenente;
	   }
	   
	   if(area_reforestar!=null)
	   {
		  if(sql == null)
			sql = "update proyecto set area_reforestar="+area_reforestar;
		  else
		    sql += ", area_reforestar="+area_reforestar;
	   }
	   
	   if(nombre_productor!=null)
	   {
		   if(sql == null)
			sql = "update proyecto set nombre_productor='"+nombre_productor+"'";
		   else
		    sql += ", nombre_productor='"+nombre_productor+"'";
	   }
	   
	   if(organismo!=null)
	   {
		   if(sql == null)
			sql = "update proyecto set organismo='"+organismo+"'";
		   else
		    sql += ", organismo='"+organismo+"'";
	   }
	   
	   if(nombre_lugar!=null)
	   {
		   if(sql == null)
			sql = "update proyecto set nombre_lugar='"+nombre_lugar+"'";
		   else
		    sql += ", nombre_lugar='"+nombre_lugar+"'";
	   }
	   
	   if(tipo_erosion_suelo!=null)
	   {
		   if(sql == null)
			sql = "update proyecto set tipo_erosion_suelo='"+tipo_erosion_suelo+"'";
		   else
		    sql += ", tipo_erosion_suelo='"+tipo_erosion_suelo+"'";
	   }
	   
	   if(grado_erosion_suelo!=null)
	   {
		   if(sql == null)
			sql = "update proyecto set grado_erosion_suelo='"+grado_erosion_suelo+"'";
		   else
			sql += ", grado_erosion_suelo='"+grado_erosion_suelo+"'";
	   }
	   
	   if(profundidad_efectiva!=null)
	   {
		   if(sql == null)
			sql = "update proyecto set profundidad_efectiva="+profundidad_efectiva;
		   else
			sql += ", profundidad_efectiva="+profundidad_efectiva;
	   }
	   
	   if(ph!=null)
	   {
		   if(sql == null)
			sql = "update proyecto set ph="+ph;
		   else
			sql += ", ph="+ph;
	   }
	   
	   if(materiaorganica!=null)
	   {
		   if(sql == null)
			sql = "update proyecto set materiaorganica="+materiaorganica;
		   else
			sql += ", materiaorganica="+materiaorganica;
	   }
	   
	   if(vegetacion!=null)
	   {
		   if(sql == null)
			sql = "update proyecto set vegetacion='"+vegetacion+"'";
		   else
			sql += ", vegetacion='"+vegetacion+"'";
	   }
	   
	   if(densidad!=null)
	   {
		   if(sql == null)
			sql = "update proyecto set densidad='"+densidad+"'";
		   else
			sql += ", densidad='"+densidad+"'";
	   }
	   
	   if(observaciones_veg!=null)
	   {
		   if(sql == null)
			sql = "update proyecto set observaciones_veg='"+observaciones_veg+"'";
		   else
			sql += ", observaciones_veg='"+observaciones_veg+"'";
	   }
	   
	   if(marcox!=null)
	   {
		   if(sql == null)
			sql = "update proyecto set marcox="+marcox;
		   else
			sql += ", marcox="+marcox;
	   }
	   
	   if(marcoy!=null)
	   {
		   if(sql == null)
			sql = "update proyecto set marcoy="+marcoy;
		   else
			sql += ", marcoy="+marcoy;
	   }
	   
	   if(hileras!=null)
	   {
		   if(sql == null)
			sql = "update proyecto set hileras="+hileras;
		   else
			sql += ", hileras="+hileras;
	   }
	   
	   if(precipitacion!=null)
	   {
		   if(sql == null)
			sql = "update proyecto set precipitacion="+precipitacion;
		   else
			sql += ", precipitacion="+precipitacion;
	   }
	   
	   if(metodo_plantacion!=null)
	   {
		   if(sql == null)
			sql = "update proyecto set metodo_plantacion='"+metodo_plantacion+"'";
		   else
			sql += ", metodo_plantacion='"+metodo_plantacion+"'";
	   }
	   
	   if(proyecto_general!=null)
	   {
		   if(sql == null)
			sql = "update proyecto set proyecto_general="+proyecto_general;
		   else
			sql += ", proyecto_general="+proyecto_general;
	   }
	   
	   if(especificacion!=null)
	   {
		   if(sql == null)
			sql = "update proyecto set especificacion='"+especificacion+"'";
		   else
			sql += ", especificacion='"+especificacion+"'";
	   }
	   
	   if(medidas_suelos!=null)
	   {
		   if(sql == null)
			sql = "update proyecto set medidas_suelos='"+medidas_suelos+"'";
		   else
			sql += ", medidas_suelos='"+medidas_suelos+"'";
	   }
	   
	   if(medidas_pastoreo!=null)
	   {
		   if(sql == null)
			sql = "update proyecto set medidas_pastoreo='"+medidas_pastoreo+"'";
		   else
			sql += ", medidas_pastoreo='"+medidas_pastoreo+"'";
	   }
	   
	   if(medidas_incendios!=null)
	   {
		   if(sql == null)
			sql = "update proyecto set medidas_incendios='"+medidas_incendios+"'";
		   else
			sql += ", medidas_incendios='"+medidas_incendios+"'";
	   }
	   
	   if(faja!=null)
	   {
		   if(sql == null)
			sql = "update proyecto set faja="+faja.getGid();
		  else
			sql += ", faja="+faja.getGid();
	   }
	   
	   if(nombre_tenente!=null)
	   {
		   if(sql == null)
			sql = "update proyecto set nombre_tenente='"+nombre_tenente+"'";
		   else
			sql += ", nombre_tenente='"+nombre_tenente+"'";
	   }
	  		   
	  if(sql != null)
	  {	   
	   sql += " where id="+id;
	   db.ejecutarConsulta(sql);
	   
	   actualizarEspecies();
	   actualizarTiposSuelos();
	   
	   TrazasManager.insertar_Traza("Actualizó el proyecto de reforestación con identificador "+id);
	   
	   isOk = true;
	  }
	 }
			 
	 return isOk;
	}

	@Override
	public boolean delete() 
	{
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 db.ejecutarConsulta("delete from proyecto where id="+id);
	 TrazasManager.insertar_Traza("Eliminó el proyecto de reforestación con identificador "+id);
	 
	 return true;
	}
	
	/**Se encarga de actualizar la reacion varios a varios
	 * con las especies*/
	private void actualizarEspecies()
	{
	 if(listaEspecies!=null)
	 {	 
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());  
		 db.ejecutarConsulta("delete from proyecto_especies where proyecto="+id); 
		 
		 for(int i=0; i<listaEspecies.length; i++)
		 {	 
		  db.ejecutarConsulta("insert into proyecto_especies(especie,proyecto) " +
		  "values('" + listaEspecies[i].getSiglas() + "'," + id + ")");
		 }
	 }
	}
	
	/**Se encarga de actualizar la reacion varios a varios
	 * con los tipos de suelo*/
	private void actualizarTiposSuelos()
	{
	 if(listaSuelos!=null)
	 {	 
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());  
		 db.ejecutarConsulta("delete from proyecto_suelos where proyecto="+id); 
		 
		 for(int i=0; i<listaSuelos.length; i++)
		 {	 
		  db.ejecutarConsulta("insert into proyecto_suelos(suelo,proyecto) " +
		  "values(" + listaSuelos[i].getID()+ "," + id + ")");
		 }
	 }
	}
}
