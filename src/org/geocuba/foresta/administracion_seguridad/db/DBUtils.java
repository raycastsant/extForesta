package org.geocuba.foresta.administracion_seguridad.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;

import javax.swing.JOptionPane;

import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.herramientas.utiles.Global;

import com.hardcode.driverManager.DriverLoadException;
import com.hardcode.gdbms.driver.exceptions.InitializeWriterException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.exceptions.visitors.VisitorException;
import com.iver.cit.gvsig.fmap.core.DefaultFeature;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.drivers.ConnectionFactory;
import com.iver.cit.gvsig.fmap.drivers.DBException;
import com.iver.cit.gvsig.fmap.drivers.DBLayerDefinition;
import com.iver.cit.gvsig.fmap.drivers.FieldDescription;
import com.iver.cit.gvsig.fmap.drivers.IConnection;
import com.iver.cit.gvsig.fmap.drivers.IVectorialDatabaseDriver;
import com.iver.cit.gvsig.fmap.drivers.jdbc.postgis.PostGISWriter;
import com.iver.cit.gvsig.fmap.drivers.jdbc.postgis.PostGisDriver;
import com.iver.cit.gvsig.fmap.edition.DefaultRowEdited;
import com.iver.cit.gvsig.fmap.edition.IRowEdited;
import com.iver.cit.gvsig.fmap.edition.IWriter;
import com.iver.cit.gvsig.fmap.layers.FBitSet;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.utiles.PostProcessSupport;

public class DBUtils
{
 public static Connection makeNewConnection(String db) throws SQLException
 {
	 String url="jdbc:postgresql://"+Global.PostgresServer+":"+Global.PostgresPort+"/"+db;
	 Connection conn = DriverManager.getConnection(url, Global.PostgresUser, Global.PostgresPw);
	 
	 return conn;
 }
 
 public static boolean finalizar_conexiones_Activas(String dbname, JDBCAdapter db) 
 {
	 String []procpids = null;
	 boolean flag = true;
	 
	    if(db == null)
		 db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	    
	    flag = db.ejecutarConsulta("Select procpid from pg_stat_activity where datname='"+dbname+"'");
	    if(!db.isEmpty())
	    { 	
	      //Busco las conexiones	
			procpids = new String[db.getRowCount()];
			for(int i=0; i<db.getRowCount(); i++)
			 procpids[i] = db.getValueAsString(i,0);
			
		  //Cierro conexiones
			for(int i=0; i<procpids.length; i++)
			 flag = db.ejecutarConsulta("select pg_terminate_backend("+procpids[i]+")");
	    }
	    else
	     flag = false;
	    
	    return flag;
 }
 
 public static boolean Eliminar_BD(String dbname, JDBCAdapter db) 
 {
	 boolean flag = true;
	 
	    if(db == null)
		 db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	    
	    flag = db.ejecutarConsulta("DROP DATABASE "+dbname);
	    
	    return flag;
 }
 
 public static boolean Crear_Nueva_BD(String dbname, JDBCAdapter db) 
 {
	 boolean flag = true;
	 
	    if(db == null)
		 db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	    
	    String sql="CREATE DATABASE "+dbname+" WITH OWNER = postgres ENCODING = 'UTF8' TEMPLATE = template_postgis";
	    flag = db.ejecutarConsulta(sql);
	    flag = db.ejecutarConsulta("update pg_database set encoding=8 where datname='"+dbname+"'");
	    
	    return flag;
 }
 
 public static boolean Vaciar_Tabla(String tabla, JDBCAdapter db) 
 {
	    if(db == null)
		 db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	    
	    return db.ejecutarConsulta("delete from "+tabla);
 }
 
 /**Exporta una capa a postgis
	 * layer - capa a exportar
	 * table - nombre de la tabla a crear
	 * addFieldidrio - si se agrega el campo idrio
	 * addAnotherPostgisFields - si al encontrar un campo con los nombres gid o the_geom se cambian sus nombres y se agregan otros nuevos*/
	public static void saveToPostGIS(FLyrVect layer, String table, boolean addFieldidrio, boolean addAnotherPostgisFields)
	{
		try {
			String tableName = table; 
			
			CharSequence seq = "\\/=.:,;�?*{}�$%&()@#|!�";
			for (int i = 0; i < seq.length(); i++) {
				char c = seq.charAt(i);
				if(tableName != null && tableName.indexOf(c) != -1) {
					JOptionPane.showMessageDialog(null, "No se aceptan caracteres especiales en el nombre de la tabla");
					break;
				}
			} 
		
			if (tableName == null)
				return;
			tableName = tableName.toLowerCase();

			//DlgConnection dlg = new DlgConnection(new String[]{"PostGIS JDBC Driver"});
			//dlg.setModal(true);
			//dlg.setVisible(true);
			/*ConnectionSettings cs = Global.GetConSettings();//dlg.getConnSettings();
			if (cs == null)
				return;
			IConnection conex = ConnectionFactory.createConnection(cs
					.getConnectionString(), cs.getUser(), cs.getPassw());*/
			
			String url = ConnectionExt.getConexionActiva().getMetaData().getURL();
			IConnection conex = ConnectionFactory.createConnection(url, Global.PostgresUser, Global.PostgresPw);

			DBLayerDefinition originalDef = null;
			if (layer.getSource().getDriver() instanceof IVectorialDatabaseDriver) {
				originalDef=((IVectorialDatabaseDriver) layer.getSource().getDriver()).getLyrDef();
			}

			DBLayerDefinition dbLayerDef = new DBLayerDefinition();
			// Fjp:
			// Cambio: En Postgis, el nombre de cat�logo est� siempre vac�o. Es algo heredado de Oracle, que no se usa.
			// dbLayerDef.setCatalogName(cs.getDb());
			dbLayerDef.setCatalogName("");

			// A�adimos el schema dentro del layer definition para poder tenerlo en cuenta.
			dbLayerDef.setSchema("public");

			dbLayerDef.setTableName(tableName);
			dbLayerDef.setName(tableName);
			dbLayerDef.setShapeType(layer.getShapeType());
			SelectableDataSource sds = layer.getRecordset();

			FieldDescription[] fieldsDescrip = sds.getFieldsDescription();
			dbLayerDef.setFieldsDesc(fieldsDescrip);
	        // Creamos el driver. OJO: Hay que a�adir el campo ID a la
	        // definici�n de campos.

			if (originalDef != null){
				dbLayerDef.setFieldID(originalDef.getFieldID());
				dbLayerDef.setFieldGeometry(originalDef.getFieldGeometry());

			}else{
				// Search for id field name
				int index=0;
				String fieldName="gid";
				if(addAnotherPostgisFields)
				{	
				  while (findFileByName(fieldsDescrip,fieldName) != -1)
				  {
					index++;
					fieldName="gid"+index;
				  }
				}  
				dbLayerDef.setFieldID(fieldName);

				// search for geom field name
				index=0;
				fieldName="the_geom";
				if(addAnotherPostgisFields)
				{	
				 while (findFileByName(fieldsDescrip,fieldName) != -1)
				 {
					index++;
					fieldName="the_geom"+index;
				 }
				} 
				dbLayerDef.setFieldGeometry(fieldName);

			}

			// if id field dosen't exist we add it
			if (findFileByName(fieldsDescrip,dbLayerDef.getFieldID()) == -1)
			{
				int moreFcant = 1;
				//Si son secciones tranversales hay que agregarles el campo idrio
				if(addFieldidrio)
				 moreFcant++;
				
	        	int numFieldsAnt = fieldsDescrip.length;
	        	FieldDescription[] newFields = new FieldDescription[dbLayerDef.getFieldsDesc().length + moreFcant];
	            for (int i=0; i < numFieldsAnt; i++)
	            {
	            	newFields[i] = fieldsDescrip[i];
	            }
	            newFields[numFieldsAnt] = new FieldDescription();
	            newFields[numFieldsAnt].setFieldDecimalCount(0);
	            newFields[numFieldsAnt].setFieldType(Types.INTEGER);
	            newFields[numFieldsAnt].setFieldLength(7);
	            newFields[numFieldsAnt].setFieldName("gid");
	            
	            //agrego el campo rio
	            if(addFieldidrio)
	            {	
	             newFields[numFieldsAnt+1] = new FieldDescription();
	             newFields[numFieldsAnt+1].setFieldDecimalCount(0);
	             newFields[numFieldsAnt+1].setFieldType(Types.INTEGER);
	             newFields[numFieldsAnt+1].setFieldLength(7);
	             newFields[numFieldsAnt+1].setFieldName("idrio");
	             dbLayerDef.setFieldsDesc(newFields);
	            }
	        }
			
			// all fields to lowerCase
			FieldDescription field;
			for (int i=0;i<dbLayerDef.getFieldsDesc().length;i++){
				field = dbLayerDef.getFieldsDesc()[i];
				field.setFieldName(field.getFieldName().toLowerCase());
				System.out.println(field.getFieldName());
			}
			dbLayerDef.setFieldID(dbLayerDef.getFieldID().toLowerCase());
			dbLayerDef.setFieldGeometry(dbLayerDef.getFieldGeometry().toLowerCase());

			dbLayerDef.setWhereClause("");
			//String strSRID = layer.getProjection().getAbrev();
			//dbLayerDef.setSRID_EPSG(strSRID);
			//String strEPSG = Global.projection;
			dbLayerDef.setSRID_EPSG(Global.projeccionActiva.getAbrev());
			dbLayerDef.setConnection(conex);

			PostGISWriter writer=(PostGISWriter)LayerFactory.getWM().getWriter("PostGIS Writer");
			writer.setWriteAll(true);
			writer.setCreateTable(true);
			writer.initialize(dbLayerDef);
			PostGisDriver postGISDriver=new PostGisDriver();
			postGISDriver.setLyrDef(dbLayerDef);
			postGISDriver.open();
			PostProcessSupport.clearList();
			Object[] params = new Object[2];
			params[0] = conex;
			params[1] = dbLayerDef;
			PostProcessSupport.addToPostProcess(postGISDriver, "setData",
					params, 1);

			//Escribir en la bd
			writeFeaturesNoThread(layer, writer);

			conex.close();
			
		} catch (DriverLoadException e) {
			NotificationManager.addError(e.getMessage(),e);
		} catch (DBException e) {
			NotificationManager.showMessageError(e.getLocalizedMessage(),e);
		} catch (InitializeWriterException e) {
			NotificationManager.showMessageError(e.getMessage(),e);
		} catch (ReadDriverException e) {
			NotificationManager.addError(e.getMessage(),e);
		} catch (VisitorException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**Exporta una capa a postgis
	 * layer - capa a exportar
	 * table - nombre de la tabla a crear
	 * addFieldidrio - si se agrega el campo idrio
	 * addAnotherPostgisFields - si al encontrar un campo con los nombres gid o the_geom se cambian sus nombres y se agregan otros nuevos*/
	public static void saveToPostGIS(FLyrVect layer, String table, boolean addFieldidrio, boolean addAnotherPostgisFields, String projection)
	{
		try {
			String tableName = table; 
			
			CharSequence seq = "\\/=.:,;�?*{}�$%&()@#|!�";
			for (int i = 0; i < seq.length(); i++) {
				char c = seq.charAt(i);
				if(tableName != null && tableName.indexOf(c) != -1) {
					JOptionPane.showMessageDialog(null, "No se aceptan caracteres especiales en el nombre de la tabla");
					break;
				}
			} 
		
			if (tableName == null)
				return;
			tableName = tableName.toLowerCase();

			//DlgConnection dlg = new DlgConnection(new String[]{"PostGIS JDBC Driver"});
			//dlg.setModal(true);
			//dlg.setVisible(true);
			/*ConnectionSettings cs = Global.GetConSettings();//dlg.getConnSettings();
			if (cs == null)
				return;
			IConnection conex = ConnectionFactory.createConnection(cs
					.getConnectionString(), cs.getUser(), cs.getPassw());*/
			
			String url = ConnectionExt.getConexionActiva().getMetaData().getURL();
			IConnection conex = ConnectionFactory.createConnection(url, Global.PostgresUser, Global.PostgresPw);

			DBLayerDefinition originalDef = null;
			if (layer.getSource().getDriver() instanceof IVectorialDatabaseDriver) {
				originalDef=((IVectorialDatabaseDriver) layer.getSource().getDriver()).getLyrDef();
			}

			DBLayerDefinition dbLayerDef = new DBLayerDefinition();
			// Fjp:
			// Cambio: En Postgis, el nombre de cat�logo est� siempre vac�o. Es algo heredado de Oracle, que no se usa.
			// dbLayerDef.setCatalogName(cs.getDb());
			dbLayerDef.setCatalogName("");

			// A�adimos el schema dentro del layer definition para poder tenerlo en cuenta.
			dbLayerDef.setSchema("public");

			dbLayerDef.setTableName(tableName);
			dbLayerDef.setName(tableName);
			dbLayerDef.setShapeType(layer.getShapeType());
			SelectableDataSource sds = layer.getRecordset();

			FieldDescription[] fieldsDescrip = sds.getFieldsDescription();
			dbLayerDef.setFieldsDesc(fieldsDescrip);
	        // Creamos el driver. OJO: Hay que a�adir el campo ID a la
	        // definici�n de campos.

			if (originalDef != null){
				dbLayerDef.setFieldID(originalDef.getFieldID());
				dbLayerDef.setFieldGeometry(originalDef.getFieldGeometry());

			}else{
				// Search for id field name
				int index=0;
				String fieldName="gid";
				if(addAnotherPostgisFields)
				{	
				  while (findFileByName(fieldsDescrip,fieldName) != -1)
				  {
					index++;
					fieldName="gid"+index;
				  }
				}  
				dbLayerDef.setFieldID(fieldName);

				// search for geom field name
				index=0;
				fieldName="the_geom";
				if(addAnotherPostgisFields)
				{	
				 while (findFileByName(fieldsDescrip,fieldName) != -1)
				 {
					index++;
					fieldName="the_geom"+index;
				 }
				} 
				dbLayerDef.setFieldGeometry(fieldName);

			}

			// if id field dosen't exist we add it
			if (findFileByName(fieldsDescrip,dbLayerDef.getFieldID()) == -1)
			{
				int moreFcant = 1;
				//Si son secciones tranversales hay que agregarles el campo idrio
				if(addFieldidrio)
				 moreFcant++;
				
	        	int numFieldsAnt = fieldsDescrip.length;
	        	FieldDescription[] newFields = new FieldDescription[dbLayerDef.getFieldsDesc().length + moreFcant];
	            for (int i=0; i < numFieldsAnt; i++)
	            {
	            	newFields[i] = fieldsDescrip[i];
	            }
	            newFields[numFieldsAnt] = new FieldDescription();
	            newFields[numFieldsAnt].setFieldDecimalCount(0);
	            newFields[numFieldsAnt].setFieldType(Types.INTEGER);
	            newFields[numFieldsAnt].setFieldLength(7);
	            newFields[numFieldsAnt].setFieldName("gid");
	            
	            //agrego el campo rio
	            if(addFieldidrio)
	            {	
	             newFields[numFieldsAnt+1] = new FieldDescription();
	             newFields[numFieldsAnt+1].setFieldDecimalCount(0);
	             newFields[numFieldsAnt+1].setFieldType(Types.INTEGER);
	             newFields[numFieldsAnt+1].setFieldLength(7);
	             newFields[numFieldsAnt+1].setFieldName("idrio");
	             dbLayerDef.setFieldsDesc(newFields);
	            }
	        }
			
			// all fields to lowerCase
			FieldDescription field;
			for (int i=0;i<dbLayerDef.getFieldsDesc().length;i++){
				field = dbLayerDef.getFieldsDesc()[i];
				field.setFieldName(field.getFieldName().toLowerCase());
				System.out.println(field.getFieldName());
			}
			dbLayerDef.setFieldID(dbLayerDef.getFieldID().toLowerCase());
			dbLayerDef.setFieldGeometry(dbLayerDef.getFieldGeometry().toLowerCase());

			dbLayerDef.setWhereClause("");
			//String strSRID = layer.getProjection().getAbrev();
			//dbLayerDef.setSRID_EPSG(strSRID);
			String strEPSG = projection;
			dbLayerDef.setSRID_EPSG(strEPSG);
			dbLayerDef.setConnection(conex);

			PostGISWriter writer=(PostGISWriter)LayerFactory.getWM().getWriter("PostGIS Writer");
			writer.setWriteAll(true);
			writer.setCreateTable(true);
			writer.initialize(dbLayerDef);
			PostGisDriver postGISDriver=new PostGisDriver();
			postGISDriver.setLyrDef(dbLayerDef);
			postGISDriver.open();
			PostProcessSupport.clearList();
			Object[] params = new Object[2];
			params[0] = conex;
			params[1] = dbLayerDef;
			PostProcessSupport.addToPostProcess(postGISDriver, "setData",
					params, 1);

			//Escribir en la bd
			writeFeaturesNoThread(layer, writer);

			conex.close();
			
		} catch (DriverLoadException e) {
			NotificationManager.addError(e.getMessage(),e);
		} catch (DBException e) {
			NotificationManager.showMessageError(e.getLocalizedMessage(),e);
		} catch (InitializeWriterException e) {
			NotificationManager.showMessageError(e.getMessage(),e);
		} catch (ReadDriverException e) {
			NotificationManager.addError(e.getMessage(),e);
		} catch (VisitorException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static int findFileByName(FieldDescription[] fields, String fieldName)
	{
        for (int i=0; i < fields.length; i++)
        {
        	FieldDescription f = fields[i];
        	if (f.getFieldName().equalsIgnoreCase(fieldName))
        	{
        		return i;
        	}
        }

		return -1;

	}
	
	private static void writeFeaturesNoThread(FLyrVect layer, IWriter writer) throws ReadDriverException, VisitorException, ExpansionFileReadException
	{
		ReadableVectorial va = layer.getSource();
		SelectableDataSource sds = layer.getRecordset();

		// Creamos la tabla.
		writer.preProcess();

		int rowCount;
		FBitSet bitSet = layer.getRecordset().getSelection();

		if (bitSet.cardinality() == 0)
			rowCount = va.getShapeCount();
		else
			rowCount = bitSet.cardinality();

		if (bitSet.cardinality() == 0) {
			rowCount = va.getShapeCount();
			for (int i = 0; i < rowCount; i++) {
				IGeometry geom = va.getShape(i);

				if (geom != null) {
					Value[] values = sds.getRow(i);
					IFeature feat = new DefaultFeature(geom, values, "" + i);
					DefaultRowEdited edRow = new DefaultRowEdited(feat,
							IRowEdited.STATUS_ADDED, i);
					writer.process(edRow);
				}
			}
		} else {
			//int counter = 0;
			for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet
					.nextSetBit(i + 1)) {
				IGeometry geom = va.getShape(i);

				if (geom != null) {
					Value[] values = sds.getRow(i);
					IFeature feat = new DefaultFeature(geom, values, "" + i);
					DefaultRowEdited edRow = new DefaultRowEdited(feat,
							IRowEdited.STATUS_ADDED, i);

					writer.process(edRow);
				}
			}

		}

		writer.postProcess();
	}
	
}
