package org.geocuba.foresta.administracion_seguridad.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.cresques.cts.IProjection;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.herramientas.utiles.Global;
import com.hardcode.driverManager.Driver;
import com.iver.cit.gvsig.fmap.core.ICanReproject;
import com.iver.cit.gvsig.fmap.drivers.ConnectionFactory;
import com.iver.cit.gvsig.fmap.drivers.ConnectionJDBC;
import com.iver.cit.gvsig.fmap.drivers.DBException;
import com.iver.cit.gvsig.fmap.drivers.DBLayerDefinition;
import com.iver.cit.gvsig.fmap.drivers.FieldDescription;
import com.iver.cit.gvsig.fmap.drivers.IConnection;
import com.iver.cit.gvsig.fmap.drivers.IVectorialJDBCDriver;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;

//import controlador.Controlador;

/**
 * @author Sergio Piñón Campañó
 *
 */
public class CargadorCapas {
    //private static String selectedDriver;
    private static String dbURL;
    private static String user;
    private static String pwd;
    private static IConnection conex = null;
    private static Logger log = Logger.getLogger(CargadorCapas.class);

public CargadorCapas(String url, String puser, String ppass) 
{
 //selectedDriver = "PostGIS JDBC Driver";
 dbURL = url;//"jdbc:postgresql://localhost:5432/Baracoa";
 user = puser;//"postgres";
 pwd = ppass;//"postgres";
   
try {
	
    conex = ConnectionFactory.createConnection(dbURL, user, pwd);

} catch (DBException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
}

 public CargadorCapas() 
 {
  try {
	String url = ConnectionExt.getConexionActiva().getMetaData().getURL();
	conex = ConnectionFactory.createConnection(url, Global.PostgresUser, Global.PostgresPw);

   } catch (SQLException e) {
	e.printStackTrace();
   } catch (DBException e) {
	e.printStackTrace();
   }	 
 }

 /**
 * Crea una capa a partir de una tabla de la BD y la añade al
 <code>mapControl y a la Vista <code>v
 * @param mapControl
 * @param v */
 public FLyrVect cargarTabla(String nombreTabla, String lyName, int posicion, String whereClause, IProjection _projection) {
       
 /*String dbURL = url;//"jdbc:postgresql://localhost:5432/Baracoa";

        String user = puser;//"postgres";
        String pwd = ppass;//"postgres";
       
        IConnection conex = null;*/
 try {
  IVectorialJDBCDriver dbDriver = (IVectorialJDBCDriver) LayerFactory.getDM().getDriver("PostGIS JDBC Driver");
  Driver drv = LayerFactory.getDM().getDriver("PostGIS JDBC Driver");

 /*conex = ConnectionFactory.createConnection(dbURL, user, pwd);*/

 /*creamos la definicion para la capa*/
 DBLayerDefinition dbLayerDef = new DBLayerDefinition();
 dbLayerDef.setCatalogName("Baracoa"); //Nombre de la base de datos
 dbLayerDef.setSchema("public"); //Nombre del esquema
 dbLayerDef.setTableName(nombreTabla); //Nombre de la tabla
 dbLayerDef.setFieldGeometry("the_geom");
 dbLayerDef.setFieldID("gid");
 dbLayerDef.setWhereClause(whereClause);
 dbLayerDef.setConnection(conex);
 //dbLayerDef.setName(lyName);

 /*obtenemos conexion JDBC*/

 Connection conexion = ((ConnectionJDBC) conex).getConnection();
 DatabaseMetaData metadataDB = conexion.getMetaData();

 String tipos[] = new String[1];
 tipos[0] = "TABLE";

 ResultSet tablas = metadataDB.getTables(null, null, nombreTabla, tipos);
 tablas.next();

 //String t = tablas.getString(tablas.findColumn("TABLE_NAME"));

 ResultSet columnas = metadataDB.getColumns(null,null,nombreTabla, "%");
 ResultSet claves = metadataDB.getPrimaryKeys(null, null, nombreTabla);

 ArrayList<FieldDescription> descripciones = new ArrayList <FieldDescription>();

 ArrayList<String> nombres = new ArrayList<String>();

 while(columnas.next()) {
  log.info("Tratando atributo: \""+columnas.getString("Column_Name")+"\" de la tabla: "+nombreTabla);
  if(columnas.getString("Type_Name").equalsIgnoreCase("geometry")) {
  /*si es la columna de geometria*/
 log.info("Encontrado atributo de geometria para la tabla:"+nombreTabla);
 dbLayerDef.setFieldGeometry(columnas.getString("Column_Name"));
 }
 else 
 {
  FieldDescription fieldDescription = new FieldDescription();
  fieldDescription.setFieldName(columnas.getString("Column_Name"));
  fieldDescription.setFieldType(columnas.getType());
  descripciones.add(fieldDescription);
  nombres.add(columnas.getString("Column_Name"));
 }
 }
 FieldDescription fields[] = new FieldDescription[descripciones.size()];
 String s[] = new String[nombres.size()];
 for(int i = 0; i < descripciones.size(); i++)  
 {
  fields[i] = descripciones.get(i);
  s[i] = nombres.get(i);
 }
 dbLayerDef.setFieldsDesc(fields);
 dbLayerDef.setFieldNames(s);

 /*buscamos clave primaria y la añadimos a la definicion de la capa*/
  while(claves.next()) {
   dbLayerDef.setFieldID(claves.getString("Column_Name"));
  }

 //PostGISWriter writer = new PostGISWriter();

 //writer.setWriteAll(false);
 //writer.setCreateTable(false);

 //IProjection proj = null;
 String strEPSG = _projection.getAbrev(); // "2085";

 if (drv instanceof ICanReproject)
 {                  
        dbLayerDef.setSRID_EPSG(strEPSG);
       
    if (dbDriver instanceof ICanReproject)
    {
        ((ICanReproject)dbDriver).setDestProjection(strEPSG);
    }
      dbDriver.setData(conex, dbLayerDef);
     
//    if (dbDriver instanceof ICanReproject)
//    {
//    // proj = CRSFactory.getCRS("EPSG:" + ((ICanReproject)dbDriver).getSourceProjection(null,dbLayerDef));
//     proj = v.getProjection(); //CRSFactory.getCRS("EPSG:"+strEPSG);//("EPSG:2085");
//    }
 }
 FLayer lyr =  LayerFactory.createDBLayer(dbDriver, lyName, _projection);

 /*if (lyr!=null && v!=null) {
    lyr.setVisible(true);
    v.getMapControl().getMapContext().beginAtomicEvent();
    // Comprobamos si es necesario reproyectar
    checkProjection(lyr, v.getMapControl());
   
    v.getMapControl().getMapContext().getLayers().addLayer(lyr);
    v.getMapControl().getMapContext().endAtomicEvent();
 }*/

 //conex.close();

 return (FLyrVect) lyr;
 } catch (Exception e) {
 log.warn("No se pudo cargar capa de la tabla: "+nombreTabla);
  e.printStackTrace();
 return null;
 }
}

// private void checkProjection(FLayer lyr, MapControl mc)
//    {
//        if (lyr instanceof FLyrVect)
//        {
//            FLyrVect lyrVect = (FLyrVect) lyr;
//            IProjection proj = lyr.getProjection();
//            // Comprobar que la proyección es la misma que la vista
//            if (proj == null)
//            {
//                 log.warn("Reproyectando capa "+lyr.getName()+" porque no tenía proyección asignada");
//                lyrVect.setProjection( mc.getViewPort().getProjection());
//                return;
//            }
//            if
//                (!proj.getAbrev().equals(mc.getViewPort().getProjection().getAbrev())) {
//                int option = JOptionPane.showConfirmDialog(null,PluginServices.getText(this, "reproyectar_aviso"),
//                             PluginServices.getText(this, "reproyectar_pregunta"),JOptionPane.YES_NO_OPTION);
//
//                if (option == JOptionPane.NO_OPTION) {
//                    return;
//                } else {
//                    log.error("coordTrans = " + proj.getAbrev() + " " + mc.getViewPort().getProjection().getAbrev());
//                   
//                    lyrVect.reProject((ICoordTrans) mc);
//                    System.err.println("coordTrans = " +
//                        proj.getAbrev() + " " +
//                        mc.getViewPort().getProjection().getAbrev());
//                }
//            }
//        }
//    }

 public void CloseConnection(){
 try {
	conex.close();
 } catch (DBException e) {
	e.printStackTrace();
 }
 }

}