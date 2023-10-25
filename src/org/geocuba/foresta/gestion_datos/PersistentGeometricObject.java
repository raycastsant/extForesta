package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.herramientas.utiles.Global;
import com.iver.cit.gvsig.exceptions.visitors.ProcessVisitorException;
import com.iver.cit.gvsig.fmap.core.FPolygon2D;
import com.iver.cit.gvsig.fmap.core.FPolyline2D;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.FShape3D;
import com.iver.cit.gvsig.fmap.core.FShapeM;
import com.iver.cit.gvsig.fmap.core.GeneralPathX;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.IGeometry3D;
import com.iver.cit.gvsig.fmap.core.IGeometryM;
import com.iver.cit.gvsig.fmap.core.ShapeFactory;
import com.iver.cit.gvsig.fmap.core.ShapeMFactory;
import com.vividsolutions.jts.geom.Geometry;

/**Clase de base para los objetos geométricos persistentes*/
public abstract class PersistentGeometricObject extends PersistentObject {

	//protected String layerName = null;
    //protected GeometricData geodata;
    //protected String projection;
    //protected IFeature feat = null;
	protected IGeometry geometry = null;
    protected int featureType;
    
    public PersistentGeometricObject(boolean isNewObject) {
		super(isNewObject);
		featureType = -1111;
	}

    public abstract Integer getGid();  // Necesario a la hora de activar el listener de modificar geometrias
    
   /* public PersistentGeometricObject(boolean isNewObject, IFeature _feat, int type) {
		super(isNewObject);
		feat = _feat;
		featureType = type;
	}*/

//	public IFeature getFeature(){
//	 return feat;	
//	}
//	
//	public void setFeature(IFeature feature){
//	 feat = feature;	
//	}
	
    public void setGeometry(IGeometry geom, int type)
    {
   	 geometry = geom;
   	 featureType = type;
   	}
    
	public int getType(){
		 return featureType;	
	}
		
	public void setType(int _type){
		featureType = _type;	
	}
    
	/*protected String getInsertQuery() throws ReadDriverException, ProcessVisitorException
	{
	 PostGIS postgis = new PostGIS();
	 FLyrVect layer = (FLyrVect)AlgUtils.GetView(null).getMapControl().getMapContext().getLayers().getLayer(layerName);
	 
	 if(layer!=null && feat!=null)
	 {
	  DBLayerDefinition lyrDef = (DBLayerDefinition)EditionUtilities.createLayerDefinition(layer);
	  return postgis.getSqlInsertFeature(lyrDef, feat);
	 } 
	 else
	  return null;
	}
	
	public void setLayerName(String name)
	{
	 this.layerName = name;	
	}*/
	
	/*public void setFeature(IFeature _feat)
	{
	 geodata.setFeature(_feat);	
	}*/
	
//	public GeometricData getGeometryData()
//	{
//	 //CALCULAR SI SE VA A DEVOLVER UN AREAL O UN LINEAL 	
//	 return geodata;	
//	}
	
	/*public void setProjection(String _projection)
	{
	 this.projection = _projection;	
	}
	
	public String sgetProjection()
	{
	 return projection;	
	}*/
	
	public String getGeometryInfo() throws ProcessVisitorException
	{
	 if(geometry==null || featureType<0)
	  return null;
	 
	 //IGeometry geometry = feat.getGeometry();
	 
	 return getGeometryInfo(geometry);
    }
	
	private String getGeometryInfo(IGeometry geometry) throws ProcessVisitorException
	{
		StringBuffer sqlBuf = null;
		if (geometry.getGeometryType() != featureType)
		 {
				if(featureType == FShape.POLYGON)
				 geometry = ShapeFactory.createPolygon2D(new GeneralPathX(geometry.getInternalShape()));
				else 
				if(featureType == FShape.LINE)
				 geometry = ShapeFactory.createPolyline2D(new GeneralPathX(geometry.getInternalShape()));
				else 
				if(featureType == (FShape.POLYGON|FShape.Z))
				 geometry = ShapeFactory.createPolygon3D(new GeneralPathX(geometry.getInternalShape()),((IGeometry3D)geometry).getZs());
				else
				if(featureType == (FShape.LINE|FShape.Z))
				 geometry = ShapeFactory.createPolyline3D(new GeneralPathX(geometry.getInternalShape()),((IGeometry3D)geometry).getZs());
				else
				if(featureType == (FShape.LINE|FShape.M))
				 geometry=ShapeMFactory.createPolyline2DM(new GeneralPathX(geometry.getInternalShape()),((IGeometryM)geometry).getMs()); 
		  }
			
		  if (!isCorrectGeometry(geometry, featureType))
				throw new ProcessVisitorException("incorrect_geometry",new Exception());
			//MCoord
			if (((featureType & FShape.M) != 0) && ((featureType & FShape.MULTIPOINT) == 0)) 
			{
			 String pAbrev = Global.projeccionActiva.getAbrev();	
			 sqlBuf = new StringBuffer(" GeometryFromText( '"
						+ ((FShapeM)geometry.getInternalShape()).toText() + "', "+ pAbrev.substring(pAbrev.length()-4, pAbrev.length())+")");
			} 
			else  //ZCoord
			if((featureType & FShape.Z) != 0)
	        {
			 if((featureType & FShape.MULTIPOINT) != 0) 
			 {
						//TODO: Metodo toText 3D o 2DM 					
			 }
			 else		  //Its not a multipoint
			 {
			  String pAbrev = Global.projeccionActiva.getAbrev();
			  sqlBuf = new StringBuffer(" GeometryFromText( '"
							+ ((FShape3D)geometry.getInternalShape()).toText() + "', "+pAbrev.substring(pAbrev.length()-4, pAbrev.length())+")");
			 }
			}	
			else  //XYCoord 
			{
			 Geometry jtsGeom=geometry.toJTSGeometry();
			 if (jtsGeom==null || !isCorrectType(jtsGeom, featureType))
			  throw new ProcessVisitorException("incorrect_geometry",new Exception());
			 
			 String pAbrev = Global.projeccionActiva.getAbrev();
			 sqlBuf = new StringBuffer(" GeometryFromText( '"+jtsGeom.toText()+"', "+pAbrev.substring(pAbrev.length()-4, pAbrev.length())+")");	
		    }
			return sqlBuf.toString();
			/*Geometry jtsGeom = feat.getGeometry().toJTSGeometry();
			 return " GeometryFromText('"+ jtsGeom.toText()+"',"+projection+")";*/
	}
	
	private boolean isCorrectGeometry(IGeometry geometry, int type) 
	{
		if (FShape.POLYGON==type){
			FPolygon2D polygon = (FPolygon2D)geometry.getInternalShape();
			if (!(polygon.getBounds2D().getWidth()>0 && polygon.getBounds2D().getHeight()>0))
				return false;
		}else if (FShape.LINE==type){
			FPolyline2D line = (FPolyline2D)geometry.getInternalShape();
			if (!(line.getBounds2D().getWidth()>0 || line.getBounds2D().getHeight()>0))
				return false;
		}

		return true;
	}
	
	private boolean isCorrectType(Geometry jtsGeom, int type) {
		if (FShape.POLYGON==type){
			if (!jtsGeom.getGeometryType().equals("MultiPolygon") && !jtsGeom.getGeometryType().equals("Polygon") )
				return false;
		}
		return true;
	}
	
	/**Método encargado de validar las geometrías de los Plygonos.
	 * Actualiza el campo the_geom de los poligonos, en caso de:
	 * Splitts
	 * Huecos
	 * Contenedores*/
	public static boolean validarGeometria_NO(JDBCAdapter db, String geomInfo, int gid, String tableName, int featureType)
	{
	  if(db== null || geomInfo==null)
	   return false;
	  
//         String sql = "select gid from parteaguas where st_area(st_intersection("+geomInfo+
//		 ", parteaguas.the_geom)) > 0 and gid<>"+gid; 
	  
	  if(featureType == FShape.POLYGON)
	  {	  
	     String sql = "select gid from "+tableName+" where st_intersects("+geomInfo+
		 ", "+tableName+".the_geom) and gid<>"+gid;
		 db.ejecutarConsulta(sql);
		 System.out.println(sql);
		 
		 if(!db.isEmpty())
		 {
			  int []gids = new int[db.getRowCount()];
			  for(int i=0; i<db.getRowCount(); i++)
			   gids[i] = db.getValueAsInteger(i,0);
			  
			  int actualGid = 0; //Gid del elemento que se quiere insertar
			  int analizingGid = 0; //Gid del poligono contra el que se esta analizando
			  
			  for(int i=0; i<gids.length; i++)
			  {
			   actualGid = gid;  
			   analizingGid = gids[i];
			   
			   /*Si la diferencia con el objeto intersectado esta contenida en dicho objeto,
			    * al mismo se le debe abrir un HUECO	  
			   sql = "select st_covers(parteaguas.the_geom, (select st_multi(st_difference(parteaguas.the_geom, " +
			   		"(select the_geom from parteaguas where gid = "+gids[i]+"))) from parteaguas where gid = "+gid+
			   		")) from parteaguas where gid = "+gids[i];
			   db.ejecutarConsulta(sql);
			   System.out.println(sql);
			   if(db.isEmpty())
				return false;
			   
			   boolean value = db.getValueAsBoolean(0,0);
			   if(value)
			   {
				actualGid = gids[i];
				analizingGid = gid;
			   }	    */
			   
			   /*Si el elemento esta contenido en  el objeto intersectado,
			    * al mismo se le debe abrir un HUECO*/	  
			   sql = "select st_covers("+tableName+".the_geom, "+geomInfo+") from "+tableName+" where gid = "+analizingGid;
			   db.ejecutarConsulta(sql);
			   System.out.println(sql);
			   if(db.isEmpty())
				return false;
			   
			   boolean value = db.getValueAsBoolean(0,0);
			   if(value)
			   {
				actualGid = gids[i]; //Intercambio los gids para la consulta que viene a continuacion
				analizingGid = gid;
			   }	
			   
			   sql = "update "+tableName+" set the_geom=st_multi(st_difference("+tableName+".the_geom, " +
			   		"(select the_geom from "+tableName+" where gid = "+analizingGid+"))) where gid = "+actualGid;
			   db.ejecutarConsulta(sql);
			   System.out.println(sql);
			  }	  
		 }	 
		 
		 db.ejecutarConsulta("select st_area(the_geom) from "+tableName+" where gid="+gid);
		 if(db.isEmpty())
		  return false;
		 
		 if(db.getValueAsDouble(0,0) <=0)
		 {	 
	      db.ejecutarConsulta("delete from "+tableName+" where gid="+gid);
		  return false;
		 } 
	  }
//	  else
//	  if(featureType == FShape.LINE)	//--------------------------------------------------------------------------------------  
//	  {
//		  String sql = "select gid from "+tableName+" where st_intersects("+geomInfo+
//			 ", "+tableName+".the_geom) and gid<>"+gid;
//			 db.ejecutarConsulta(sql);
//			 System.out.println(sql);
//			 
//			 if(!db.isEmpty())
//			 {
//				  int []gids = new int[db.getRowCount()];
//				  for(int i=0; i<db.getRowCount(); i++)
//				   gids[i] = db.getValueAsInteger(i,0);
//				  
//				  int actualGid = 0; //Gid del elemento que se quiere insertar
//				  int analizingGid = 0; //Gid del poligono contra el que se esta analizando
//				  
//				  for(int i=0; i<gids.length; i++)
//				  {
//					  actualGid = gid;  
//					  analizingGid = gids[i];
//					  
//				  }
//			 }		  
//	  } 	
//	  else
//	   return false;	  
		
	 return true;
	}
	
	/**Método encargado de validar las geometrías de las lineas.
	 * Actualiza el campo the_geom de los poligonos, en caso de:
	 * Splitts
	 * Huecos
	 * Contenedores*/
	public boolean validarGeometriaLineas_NO(JDBCAdapter db, String geomInfo, String tableName)
	{
	  String sql = "select gid from "+tableName+" where st_contains(the_geom, "+geomInfo+")";
	  db.ejecutarConsulta(sql);
	  System.out.println(sql);
	  
	  System.out.println(db.isEmpty());
	  
	  return db.isEmpty();
	}
}
