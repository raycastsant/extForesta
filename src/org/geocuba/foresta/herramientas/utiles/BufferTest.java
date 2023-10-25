package org.geocuba.foresta.herramientas.utiles;

import java.io.File;
import java.util.HashMap;
import javax.swing.JOptionPane;
import junit.framework.TestCase;
import org.cresques.cts.IProjection;
import com.hardcode.driverManager.DriverLoadException;
import com.hardcode.gdbms.driver.exceptions.InitializeWriterException;
import com.iver.cit.gvsig.exceptions.layers.LoadLayerException;
import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.fmap.drivers.SHPLayerDefinition;
import com.iver.cit.gvsig.fmap.drivers.VectorialFileDriver;
import com.iver.cit.gvsig.fmap.edition.IWriter;
import com.iver.cit.gvsig.fmap.edition.ShpSchemaManager;
import com.iver.cit.gvsig.fmap.edition.writers.shp.MultiShpWriter;
import com.iver.cit.gvsig.fmap.edition.writers.shp.ShpWriter;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;
import com.iver.cit.gvsig.geoprocess.core.fmap.GeoprocessException;
import com.iver.cit.gvsig.geoprocess.core.fmap.XTypes;
import com.iver.cit.gvsig.geoprocess.impl.buffer.fmap.BufferGeoprocess;
import com.iver.cit.gvsig.geoprocess.impl.buffer.fmap.BufferVisitor;
import com.iver.cit.gvsig.project.documents.view.gui.View;

public class BufferTest extends TestCase {

	//static final String fwAndamiDriverPath = "../_fwAndami/gvSIG/extensiones/com.iver.cit.gvsig/drivers";
	//private static File baseDataPath;
	//private static File baseDriversPath;
	//public static String SHP_DRIVER_NAME = "gvSIG shp driver";

	static IProjection PROJECTION_DEFAULT = CRSFactory.getCRS("EPSG:23030");
    private static int i=0;
	
	
	/*protected void setUp() throws Exception {
		URL url = BufferTest.class.getResource("testdata");
		if (url == null)
			throw new Exception("No se encuentra el directorio con datos de prueba");

		baseDataPath = new File(url.getFile());
		if (!baseDataPath.exists())
			throw new Exception("No se encuentra el directorio con datos de prueba");

		baseDriversPath = new File(fwAndamiDriverPath);
		if (!baseDriversPath.exists())
			throw new Exception("Can't find drivers path: " + fwAndamiDriverPath);

		LayerFactory.setDriversPath(baseDriversPath.getAbsolutePath());
		LayerFactory.setWritersPath(baseDriversPath.getAbsolutePath());
		if (LayerFactory.getDM().getDriverNames().length < 1)
			throw new Exception("Can't find drivers in path: " + fwAndamiDriverPath);
		
	}*/


	/*public static  FLayer newLayer(String fileName,
									   String driverName)
								throws LoadLayerException {
		File file = new File(baseDataPath, fileName);
		return LayerFactory.createLayer(fileName,
										driverName,
										file, PROJECTION_DEFAULT);
	}*/
	
	
	
	public static Object doit(double dist, FLayer l, View vista, boolean addResultToMap) throws LoadLayerException, InitializeWriterException, GeoprocessException {
		FLyrVect inputLayer = (FLyrVect) l;//(FLyrVect) newLayer("parcelas.shp", SHP_DRIVER_NAME);
		File outputFile = new File(inputLayer.getName()+"_buffer"+((Integer)i).toString()+".shp");
		i++;
		BufferGeoprocess geoprocess = new BufferGeoprocess(inputLayer);
		
		HashMap params = new HashMap();
		params.put("layer_selection", new Boolean(true));
		params.put("dissolve_buffers", new Boolean(true));
		params.put("buffer_distance", new Double(dist));
		params.put("strategy_flag", new Byte(BufferGeoprocess.CONSTANT_DISTANCE_STRATEGY));
		params.put("numRings", new Integer(1));
		params.put("typePolBuffer", new Byte(BufferVisitor.BUFFER_OUTSIDE_POLY));
		params.put("cap", new Byte(BufferVisitor.CAP_SQUARE));
		params.put("projection", PROJECTION_DEFAULT);
		params.put("distanceunits", new Integer(1));
		params.put("mapunits", new Integer(1));
		
		geoprocess.setParameters(params);
		
		
		SHPLayerDefinition definition = (SHPLayerDefinition) geoprocess.createLayerDefinition();
		definition.setFile(outputFile);
		ShpSchemaManager schemaManager = new ShpSchemaManager(outputFile.getAbsolutePath());
		IWriter writer = null;
		int shapeType = definition.getShapeType();
		if(shapeType != XTypes.MULTI){
			writer = new ShpWriter();
			((ShpWriter) writer).setFile(definition.getFile());
			writer.initialize(definition);
		}else{
			writer = new MultiShpWriter();
			((MultiShpWriter) writer).setFile(definition.getFile());
			writer.initialize(definition);
		}
		geoprocess.setResultLayerProperties(writer, schemaManager);
		
		geoprocess.checkPreconditions();
		geoprocess.process();
	
		FLayer lyResult=null;;
		try {
			lyResult = LayerFactory.createLayer(outputFile.getName(),(VectorialFileDriver)LayerFactory.getDM().getDriver("gvSIG shp driver"),
				    outputFile,
				    CRSFactory.getCRS("EPSG:23030"));
		} catch (DriverLoadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	
	if(addResultToMap)
	{	
	  if(vista!=null && lyResult!=null)
	  { 	  
		vista.getMapControl().getMapContext().beginAtomicEvent();
		vista.getMapControl().getMapContext().getLayers().addLayer(lyResult);
		vista.getMapControl().getMapContext().endAtomicEvent();
	  }
	  else
	   JOptionPane.showMessageDialog(null, "No hubo resultados que pintar");
	}  
			
	  return lyResult;
	}	
	
}

