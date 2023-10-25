package org.geocuba.foresta.herramientas.utiles;

/**
*
* @author Raisel
*/

import java.awt.Component;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import org.cresques.cts.IProjection;
import org.geocuba.foresta.analisis_terreno.algoritmos.BufferByFieldAlgorithm;
import org.geocuba.foresta.analisis_terreno.algoritmos.CrossSectionsAlgorithmM;
import org.geocuba.foresta.analisis_terreno.algoritmos.ExtendLastLineAlgorithm;
import org.geocuba.foresta.analisis_terreno.algoritmos.FixedDistanceBufferAlgorithm;
import org.geocuba.foresta.analisis_terreno.algoritmos.ObjectIntersetcsAlgorithm;
import org.geocuba.foresta.analisis_terreno.algoritmos.ProfileAlgorithm;
import org.geocuba.foresta.analisis_terreno.algoritmos.RemoveItemAlgorithm;
import org.geocuba.foresta.analisis_terreno.algoritmos.SeparateCrossLinesAlgorithm;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.serializer.RmfSerializerException;
import org.gvsig.raster.datastruct.NoData;
import com.hardcode.driverManager.DriverLoadException;
import com.hardcode.gdbms.driver.exceptions.InitializeWriterException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.andami.ConfigurationException;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.exceptions.layers.LoadLayerException;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.drivers.VectorialFileDriver;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.fmap.layers.layerOperations.AlphanumericData;
import com.iver.cit.gvsig.geoprocess.core.fmap.GeoprocessException;
import com.iver.cit.gvsig.project.documents.table.ProjectTable;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.XMLEntity;
import es.unex.sextante.core.AnalysisExtent;
import es.unex.sextante.core.GeoAlgorithm;
import es.unex.sextante.core.ObjectAndDescription;
import es.unex.sextante.core.OutputObjectsSet;
import es.unex.sextante.core.ParametersSet;
import es.unex.sextante.core.Sextante;
import es.unex.sextante.dataObjects.IRasterLayer;
import es.unex.sextante.dataObjects.ITable;
import es.unex.sextante.dataObjects.IVectorLayer;
import es.unex.sextante.exceptions.GeoAlgorithmExecutionException;
import es.unex.sextante.exceptions.WrongOutputIDException;
import es.unex.sextante.exceptions.WrongParameterIDException;
import es.unex.sextante.gridCategorical.reclassify.ReclassifyAlgorithm;
import es.unex.sextante.gridTools.clipBBoxGrid.clipBBoxGridAlgorithm;
import es.unex.sextante.gridTools.closeGaps.CloseGapsAlgorithm;
import es.unex.sextante.gui.additionalResults.AdditionalResults;
import es.unex.sextante.gui.additionalResults.TableTools;
import es.unex.sextante.gui.core.SextanteGUI;
import es.unex.sextante.gui.settings.SextanteGeneralSettings;
import es.unex.sextante.gvsig.core.FileTools;
import es.unex.sextante.gvsig.core.gvOutputFactory;
import es.unex.sextante.gvsig.core.gvRasterLayer;
import es.unex.sextante.gvsig.core.gvVectorLayer;
import es.unex.sextante.hydrology.accFlow.AccFlowAlgorithm;
import es.unex.sextante.hydrology.channelNetwork.ChannelNetworkAlgorithm;
import es.unex.sextante.hydrology.fillSinks.FillSinksAlgorithm;
import es.unex.sextante.hydrology.isocrones.IsocronesAlgorithm;
import es.unex.sextante.hydrology.upslopeAreaFromPoint.UpslopeAreaFromPointAlgorithm;
import es.unex.sextante.hydrology.watersheds.WatershedsAlgorithm;
import es.unex.sextante.lighting.hillshade.HillshadeAlgorithm;
import es.unex.sextante.morphometry.slope.SlopeAlgorithm;
import es.unex.sextante.outputs.FileOutputChannel;
import es.unex.sextante.outputs.IOutputChannel;
import es.unex.sextante.outputs.NullOutputChannel;
import es.unex.sextante.outputs.Output;
import es.unex.sextante.outputs.Output3DRasterLayer;
import es.unex.sextante.outputs.OutputRasterLayer;
import es.unex.sextante.outputs.OutputTable;
import es.unex.sextante.outputs.OutputText;
import es.unex.sextante.outputs.OutputVectorLayer;
import es.unex.sextante.outputs.OverwriteOutputChannel;
import es.unex.sextante.parameters.FixedTableModel;
import es.unex.sextante.profiles.crossSections.CrossSectionsAlgorithm;
import es.unex.sextante.rasterize.rasterizeVectorLayer.RasterizeVectorLayerAlgorithm;
import es.unex.sextante.vectorTools.changeLineDirection.ChangeLineDirectionAlgorithm;
import es.unex.sextante.vectorTools.clip.ClipAlgorithm;
import es.unex.sextante.vectorTools.difference.DifferenceAlgorithm;
import es.unex.sextante.vectorTools.linesToEquispacedPoints.LinesToEquispacedPointsAlgorithm;
import es.unex.sextante.vectorTools.polygonsToPolylines.PolygonsToPolylinesAlgorithm;
import es.unex.sextante.vectorTools.splitMultipart.SplitMultipartAlgorithm;
import es.unex.sextante.vectorize.vectorize.VectorizeAlgorithm;

public class AlgUtils {

private static gvOutputFactory m_OutputFactory = null;
private static ParametersSet params=null;
//private static GridExtent extent=null;
//private static AnalysisExtent extent=null;
private static OutputObjectsSet outputs=null;
private static Output out=null;
//Lados
public final static int AMBOSLADOS = 0;
public final static int DERECHA = 1;
public final static int IZQUIERDA = 2;

/** Realiza un proceso necesario al ráster resultante de un algoritmo*/
private static Object getPostProcessResult(GeoAlgorithm m_Algorithm, boolean addResultToView) {

    final boolean bUseInternalNames = new Boolean(
             SextanteGUI.getSettingParameterValue(SextanteGeneralSettings.USE_INTERNAL_NAMES)).booleanValue();
    final boolean bModiFyResultsNames = new Boolean(SextanteGUI.getSettingParameterValue(SextanteGeneralSettings.MODIFY_NAMES)).booleanValue();

    String sDescription;
    boolean bInvalidate = false;
    //boolean bShowAdditionalPanel = false;
    MapContext m_MapContext = null;
    Object result = null;
    
    if(addResultToView)
    {	 
     m_MapContext = AlgUtils.GetView(null).getMapControl().getMapContext();
    
     if (m_MapContext != null) {
       m_MapContext.beginAtomicEvent();
     }
    } 

    for (int i = 0; i < outputs.getOutputObjectsCount(); i++) {

       final Output out = outputs.getOutput(i);
       sDescription = out.getDescription();
       final IOutputChannel channel = out.getOutputChannel();
       final Object object = out.getOutputObject();

       if ((out instanceof OutputRasterLayer) || (out instanceof Output3DRasterLayer) || (out instanceof OutputTable)
           || (out instanceof OutputVectorLayer)) {
          if (bUseInternalNames) {
             sDescription = out.getName();
          }
          else if (bModiFyResultsNames) {
             sDescription = SextanteGUI.modifyResultName(sDescription);
          }
          if ((channel instanceof NullOutputChannel) || (channel == null)) {
             continue;
          }
       }
       if (out instanceof OutputVectorLayer) {
          String sFilename = null;
          if (channel instanceof FileOutputChannel) {
             sFilename = ((FileOutputChannel) channel).getFilename();
             final FLyrVect flayer = (FLyrVect) FileTools.openLayer(sFilename, sDescription,
                      (IProjection) m_Algorithm.getOutputCRS());
             if (flayer != null) {
                flayer.setName(sDescription);
                result = flayer;
                
                if(addResultToView)
                 m_MapContext.getLayers().addLayer(flayer);
                bInvalidate = true;
             }

          }
          else if (channel instanceof OverwriteOutputChannel) {
             final FLyrVect flayer = (FLyrVect) ((OverwriteOutputChannel) channel).getLayer().getBaseDataObject();
             try {
                flayer.reload();
                bInvalidate = true;
             }
             catch (final ReloadLayerException e) {

             }
          }

          if (object != null) {
             ((IVectorLayer) object).close();
          }

       }
       else if (out instanceof OutputTable) {
          try {
             final ProjectTable table = (ProjectTable) ((ITable) object).getBaseDataObject();
             if (table != null) {
                table.setName(sDescription);
                ((ProjectExtension) PluginServices.getExtension(ProjectExtension.class)).getProject().addDocument(table);
                final JScrollPane jScrollPane = TableTools.getScrollableTablePanelFromITable((ITable) object);
                AdditionalResults.addComponent(new ObjectAndDescription(sDescription, jScrollPane));
                //bShowAdditionalPanel = true;
             }
          }
          catch (final Exception e) {
             Sextante.addErrorToLog(e);
          }
       }
       else if (out instanceof OutputRasterLayer) {
          final IRasterLayer rasterLayer = (IRasterLayer) object;
          if (channel instanceof FileOutputChannel) {
             final String sFilename = ((FileOutputChannel) channel).getFilename();
             final FLyrRasterSE flayer = (FLyrRasterSE) FileTools.openLayer(sFilename, sDescription,
                      (IProjection) m_Algorithm.getOutputCRS());
             if (flayer != null) {
                if (rasterLayer != null) {
                   try {
                      flayer.setNoDataType(RasterLibrary.NODATATYPE_USER);
                      flayer.setNoDataValue(rasterLayer.getNoDataValue());
                      flayer.getDataSource().saveObjectToRmf(0, NoData.class,
                               new NoData(rasterLayer.getNoDataValue(), flayer.getNoDataType()));
                      rasterLayer.close();
                   }
                   catch (final RmfSerializerException e) {
                      Sextante.addErrorToLog(e);
                   }
                }
                flayer.setName(sDescription);
                result = flayer;

                if(addResultToView)
                 m_MapContext.getLayers().addLayer(flayer);
                bInvalidate = true;

             }
          }
       }
       else if (out instanceof OutputText) {
          JTextPane jTextPane;
          JScrollPane jScrollPane;
          jTextPane = new JTextPane();
          jTextPane.setEditable(false);
          jTextPane.setContentType("text/html");
          jTextPane.setText((String) object);
          jScrollPane = new JScrollPane();
          jScrollPane.setViewportView(jTextPane);
          jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
          jTextPane.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
          AdditionalResults.addComponent(new ObjectAndDescription(sDescription, jScrollPane));
          //bShowAdditionalPanel = true;
       }
       else if (object instanceof Component) {
          AdditionalResults.addComponent(new ObjectAndDescription(sDescription, object));
          //bShowAdditionalPanel = true;
       }
       else if (out instanceof Output3DRasterLayer) {
          JOptionPane.showMessageDialog(SextanteGUI.getMainFrame(), Sextante.getText("3d_not_supported"),
                   Sextante.getText("Warning"), JOptionPane.WARNING_MESSAGE);
       }

    }

    if(addResultToView)
    {	 
     if (m_MapContext != null) {
       m_MapContext.endAtomicEvent();
     }

     if (bInvalidate) {
       m_MapContext.invalidate();
     }
    } 

   /* if (bShowAdditionalPanel && m_bShowResultsWindows) {
       AdditionalResults.showPanel();
    }*/
   return result;     
 }

private static GeoAlgorithm depresiones(Object antAlgResult, String outUrl) throws GeoAlgorithmExecutionException
{
  gvRasterLayer rlayer = null;
  m_OutputFactory=new gvOutputFactory();
  	
   if(antAlgResult instanceof FLyrRasterSE)
   {	    
	rlayer = new gvRasterLayer();
	rlayer.create((FLyrRasterSE)antAlgResult);
   }
   else
    rlayer = (gvRasterLayer)antAlgResult;	   
	
	FillSinksAlgorithm fill = new FillSinksAlgorithm();
	params = fill.getParameters();
	params.getParameter(FillSinksAlgorithm.DEM).setParameterValue(rlayer);
	params.getParameter(FillSinksAlgorithm.MINSLOPE).setParameterValue((Double)0.01);
	
	outputs = fill.getOutputObjects();
	out = outputs.getOutput(FillSinksAlgorithm.RESULT);
	if(outUrl != null && !outUrl.equals(""))
	 out.setOutputChannel(new FileOutputChannel(outUrl));
	//fillLayer.setOutputChannel(new FileOutputChannel(outUrl+"sindepresiones.tif"));

	fill.execute(null, m_OutputFactory);	
	
	return fill;
}

private static gvRasterLayer validateInput(Object raster)
{
	gvRasterLayer rlayer = null;
	if(raster instanceof FLyrRasterSE)
	{	    
		rlayer = new gvRasterLayer();
		rlayer.create((FLyrRasterSE)raster);
	}
	else
	 rlayer = (gvRasterLayer)raster;
	
	return rlayer;
}

/** Realiza un proceso necesario al resultado Vectorial de un algoritmo*/
private static Object getVectorialResult(GeoAlgorithm m_Algorithm, boolean addResultToView) {

    String sDescription;
    boolean bInvalidate = false;
    MapContext m_MapContext = null;
    Object result = null;
    
    if(addResultToView)
    {	 
     m_MapContext = AlgUtils.GetView(null).getMapControl().getMapContext();
    
     if (m_MapContext != null) {
       m_MapContext.beginAtomicEvent();
     }
    } 

    for (int i = 0; i < outputs.getOutputObjectsCount(); i++) {

       final Output out = outputs.getOutput(i);
       sDescription = out.getDescription();
       final IOutputChannel channel = out.getOutputChannel();
       final Object object = out.getOutputObject();

       if (out instanceof OutputVectorLayer) {
          String sFilename = null;
          if (channel instanceof FileOutputChannel) {
             sFilename = ((FileOutputChannel) channel).getFilename();
             final FLyrVect flayer = (FLyrVect) FileTools.openLayer(sFilename, sDescription,
                      (IProjection) m_Algorithm.getOutputCRS());
             if (flayer != null) {
                flayer.setName(sDescription);
                result = flayer;
                
                if(addResultToView)
                 m_MapContext.getLayers().addLayer(flayer);
                bInvalidate = true;
             }

          }
          else if (channel instanceof OverwriteOutputChannel) {
             final FLyrVect flayer = (FLyrVect) ((OverwriteOutputChannel) channel).getLayer().getBaseDataObject();
             try {
                flayer.reload();
                bInvalidate = true;
             }
             catch (final ReloadLayerException e) {
             }
          }

          if (object != null) 
           ((IVectorLayer) object).close();
       }
    }

    if(addResultToView)
    {	 
     if (m_MapContext != null) {
       m_MapContext.endAtomicEvent();
     }

     if (bInvalidate) {
       m_MapContext.invalidate();
     }
    } 

   return result;     
 }

/**Halla la acumulacion de flujo*/
public static Object AcumulacionFlujo(Object antAlgResult, String outUrl) throws GeoAlgorithmExecutionException, IOException
{
	gvRasterLayer rlayer = validateInput(antAlgResult);
	
	m_OutputFactory=new gvOutputFactory();
	
	XMLEntity xml;
	double CONVERGENCE = 1.1;
	
	try {
		xml = XMLUtils.persistenceFromXML("gvSIG"+Global.fileSeparator+"extensiones"+Global.fileSeparator+"org.geocuba.foresta"+Global.fileSeparator+"configForesta.xml");
		int index = xml.firstIndexOfChild("name", "algutils_options");
		xml = xml.getChild(index);
		CONVERGENCE = xml.getDoubleProperty("acumflujo_convergence");
		
	} catch (ConfigurationException e) {
		e.printStackTrace();
	}
	
	AccFlowAlgorithm acumalg = new AccFlowAlgorithm();
	params = acumalg.getParameters();
	params.getParameter(AccFlowAlgorithm.DEM).setParameterValue(rlayer);
	params.getParameter(AccFlowAlgorithm.METHOD).setParameterValue(AccFlowAlgorithm.MFD);
	params.getParameter(AccFlowAlgorithm.CONVERGENCE).setParameterValue(CONVERGENCE);
	
	//acumalg.setGridExtent(extent);
	outputs = acumalg.getOutputObjects();
	out = outputs.getOutput(AccFlowAlgorithm.FLOWACC);
	if(outUrl != null && !outUrl.equals(""))
	 out.setOutputChannel(new FileOutputChannel(outUrl));
	//ac.setOutputChannel(new FileOutputChannel(outUrl+"acumflujo.tif"));

	acumalg.execute(null, m_OutputFactory);
	
	return out.getOutputObject();
}

/*private static DataStore openShapefile(String sFile) throws IOException {

		File file = new File( sFile );

		Map connect = new HashMap();
		connect.put( "url", file.toURL() );

		DataStore dataStore = DataStoreFinder.getDataStore( connect );

		return dataStore;

	}*/
 
 /**Agrega una capa desde un fichero**/
 public static boolean addLayertoView(String filename, String layername)
	{
		View vista = GetView(null);
		MapControl mapCtrl = GetMapControl(vista); 	
		
		if(mapCtrl!=null && filename!=null && filename!="")
		{	
		//LayerFactory.setDriversPath("C:"+Global.fileSeparator+"eclipse3"+Global.fileSeparator+"workspace"+Global.fileSeparator+"Andami"+Global.fileSeparator+"gvSIG"+Global.fileSeparator+"extensiones"+Global.fileSeparator+"com.iver.cit.gvsig"+Global.fileSeparator+"drivers");
	try {
	      FLayer l = LayerFactory.createLayer(layername,(VectorialFileDriver)LayerFactory.getDM().getDriver("gvSIG shp driver"),
		    new File(filename), Global.projeccionActiva);
		  mapCtrl.getMapContext().getLayers().addLayer(l);
          return true;
		  
	} catch (DriverLoadException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		 return false;
	}	
	}
		else	
		 return false;
	}

/**Agrega una capa a la vista activa**/
 public static boolean addLayertoView(FLayer layer)
 {
		View vista = GetView(null);
		MapControl mapCtrl = GetMapControl(vista); 	
		
		if(mapCtrl!=null && layer!=null)
		{	
	     mapCtrl.getMapContext().getLayers().addLayer(layer);
	     return true;	
	    }
		else	
		 return false;
 }

/**Crear un Buffer*/
public static Object Buffer(Object vectorly, double dist, boolean NotRounded, String outUrl) throws GeoAlgorithmExecutionException, IOException
{
    m_OutputFactory=new gvOutputFactory();
	
	gvVectorLayer layer = new gvVectorLayer();
	layer.create((FLyrVect)vectorly);
	
	FixedDistanceBufferAlgorithm bufferalg = new FixedDistanceBufferAlgorithm();
	ParametersSet paramets = bufferalg.getParameters();
	
	paramets.getParameter(FixedDistanceBufferAlgorithm.LAYER).setParameterValue(layer);
	paramets.getParameter(FixedDistanceBufferAlgorithm.DISTANCE).setParameterValue(dist);
	paramets.getParameter(FixedDistanceBufferAlgorithm.TYPE).setParameterValue(FixedDistanceBufferAlgorithm.BUFFER_OUTSIDE_POLY);
	paramets.getParameter(FixedDistanceBufferAlgorithm.RINGS).setParameterValue(0);
	paramets.getParameter(FixedDistanceBufferAlgorithm.NOTROUNDED).setParameterValue(NotRounded);
	
	outputs = bufferalg.getOutputObjects();
	out = outputs.getOutput(FixedDistanceBufferAlgorithm.RESULT);
	
	if(outUrl != null && !outUrl.equals(""))
	  out.setOutputChannel(new FileOutputChannel(outUrl));
	
	bufferalg.execute(null, m_OutputFactory);
	
	return getPostProcessResult(bufferalg, false);  //out.getOutputObject();	
}

/**Crear un Buffer a la distancia de un campo definido*/
public static Object BufferByField(Object vectorly, String field, boolean NotRounded, String outUrl) throws GeoAlgorithmExecutionException, IOException
{
    m_OutputFactory=new gvOutputFactory();
	
	gvVectorLayer layer = new gvVectorLayer();
	layer.create((FLyrVect)vectorly);
	
	BufferByFieldAlgorithm bufferalg = new BufferByFieldAlgorithm();
	ParametersSet paramets = bufferalg.getParameters();
	
	paramets.getParameter(BufferByFieldAlgorithm.LAYER).setParameterValue(layer);
	paramets.getParameter(BufferByFieldAlgorithm.FIELD).setParameterValue(field);
	paramets.getParameter(BufferByFieldAlgorithm.TYPE).setParameterValue(FixedDistanceBufferAlgorithm.BUFFER_OUTSIDE_POLY);
	paramets.getParameter(BufferByFieldAlgorithm.RINGS).setParameterValue(0);
	paramets.getParameter(BufferByFieldAlgorithm.NOTROUNDED).setParameterValue(NotRounded);
	
	outputs = bufferalg.getOutputObjects();
	out = outputs.getOutput(BufferByFieldAlgorithm.RESULT);
	
	if(outUrl != null && !outUrl.equals(""))
     out.setOutputChannel(new FileOutputChannel(outUrl));

	bufferalg.execute(null, m_OutputFactory);
	
	return getPostProcessResult(bufferalg, false);   //out.getOutputObject();	
}

/**Halla las cuencas vertientes*/
public static Object Cuencas(Object mde, Object reddren, String outUrl) throws GeoAlgorithmExecutionException, IOException
{
  gvRasterLayer rlayer = validateInput(mde);
	
	m_OutputFactory=new gvOutputFactory();
	
	WatershedsAlgorithm cuencas = new WatershedsAlgorithm();
	params = cuencas.getParameters();
	params.getParameter(WatershedsAlgorithm.DEM).setParameterValue(rlayer);
	params.getParameter(WatershedsAlgorithm.NETWORK).setParameterValue(reddren);
	
	outputs = cuencas.getOutputObjects();
	out = outputs.getOutput(WatershedsAlgorithm.WATERSHEDS);
	if(outUrl != null && !outUrl.equals(""))
	 out.setOutputChannel(new FileOutputChannel(outUrl));

	cuencas.execute(null, m_OutputFactory);
	
	return out.getOutputObject();
}

/**Cuenca Vertiente a un punto dado*/
public static Object CuencaVertiente(Object mde, String outUrl) throws GeoAlgorithmExecutionException, IOException
{
    gvRasterLayer rlayer = validateInput(mde);
	
	m_OutputFactory=new gvOutputFactory();
	Point2D p=new java.awt.Point();
	p.setLocation(581733.6478882824, 248493.52960305003);
	
	UpslopeAreaFromPointAlgorithm cuenca = new UpslopeAreaFromPointAlgorithm();
	params = cuenca.getParameters();
	params.getParameter(UpslopeAreaFromPointAlgorithm.DEM).setParameterValue(rlayer);
	params.getParameter(UpslopeAreaFromPointAlgorithm.OUTLET).setParameterValue(p);
	
	outputs = cuenca.getOutputObjects();
	out = outputs.getOutput(UpslopeAreaFromPointAlgorithm.RESULT);
	if(outUrl != null && !outUrl.equals(""))
	 out.setOutputChannel(new FileOutputChannel(outUrl));
	
	//out.setOutputChannel(new FileOutputChannel(outUrl+"cuencapunto.shp"));
	cuenca.execute(null, m_OutputFactory);
	
	return out.getOutputObject();		
}

/** Cortar capa vectorial con capa de polygonos*/
public static Object ClipVectorial(Object vectLayer, FLyrVect poly, String resultName) throws GeoAlgorithmExecutionException
{
	m_OutputFactory = new gvOutputFactory();

	gvVectorLayer input = new gvVectorLayer();
	input.create((FLyrVect)vectLayer);
	
    gvVectorLayer polygon = new gvVectorLayer();
    polygon.create(poly);
	
    ClipAlgorithm clipalg = new ClipAlgorithm();
	ParametersSet params = clipalg.getParameters();
	params.getParameter(ClipAlgorithm.LAYER).setParameterValue(input);
	params.getParameter(ClipAlgorithm.CLIPLAYER).setParameterValue(polygon);

	outputs = clipalg.getOutputObjects();
	out = outputs.getOutput(ClipAlgorithm.RESULT);
	if(resultName != null && !resultName.equals(""))
	 out.setOutputChannel(new FileOutputChannel(resultName));
	
	clipalg.execute(null, m_OutputFactory);
	
	return  getPostProcessResult(clipalg, false);  //out.getOutputObject();
}

/** Cortar raster con capa de polygonos*/
public static Object ClipRasterWithBBox(Object raster, FLyrVect poly, String resultName) throws GeoAlgorithmExecutionException, java.lang.OutOfMemoryError
{
	m_OutputFactory = new gvOutputFactory();

	gvRasterLayer rlayer = new gvRasterLayer();
    rlayer.create((FLyrRasterSE)raster);
    
    gvVectorLayer vlayer = new gvVectorLayer();
    vlayer.create(poly);
	
    clipBBoxGridAlgorithm clipalg = new clipBBoxGridAlgorithm();
	ParametersSet params = clipalg.getParameters();
	params.getParameter(clipBBoxGridAlgorithm.INPUT).setParameterValue(rlayer);
	params.getParameter(clipBBoxGridAlgorithm.POLYGONS).setParameterValue(vlayer);

	outputs = clipalg.getOutputObjects();
	out = outputs.getOutput(clipBBoxGridAlgorithm.RESULT);
	if(resultName != null && !resultName.equals(""))
	 out.setOutputChannel(new FileOutputChannel(resultName));
	
	clipalg.execute(null, m_OutputFactory);
	return getPostProcessResult(clipalg, false);    //out.getOutputObject();
}

/** Cambia el sentido de digitalizacion de las lineas
 * @throws GeoAlgorithmExecutionException */
public static Object changeLinesDirection(FLayer lyr, String outUrl) throws GeoAlgorithmExecutionException
{
	 m_OutputFactory = new gvOutputFactory();

		gvVectorLayer layer = new gvVectorLayer();
		layer.create((FLyrVect)lyr);
		
		ChangeLineDirectionAlgorithm alg = new ChangeLineDirectionAlgorithm();
		ParametersSet params = alg.getParameters();
		params.getParameter(ChangeLineDirectionAlgorithm.LINES).setParameterValue(layer);

		outputs = alg.getOutputObjects();
		out = outputs.getOutput("RESULT");
		if(outUrl != null && !outUrl.equals(""))
			 out.setOutputChannel(new FileOutputChannel(outUrl));
		
		//out.setOutputChannel(new FileOutputChannel("invLines.shp"));
		alg.execute(null, m_OutputFactory);
		
		return getPostProcessResult(alg, false);   //out.getOutputObject();
}

/**Diferencia entre dos poligonos*/
public static Object Diferencia(Object polyCortar, Object polyRef, String outUrl) throws GeoAlgorithmExecutionException, IOException
{
	Value d1=null;
	Value d2=null;
	FLyrVect lyr=null;
	SelectableDataSource ds=null;
	
	m_OutputFactory=new gvOutputFactory();
	
  //Obteniendo los valores de distancia
	lyr=(FLyrVect)polyCortar;
	try {
		    ds = ((AlphanumericData)lyr).getRecordset();
		    ds.start();
		    d1 = ds.getFieldValue(0,1); //Obtengo la distancia del a 1er buffer
		    ds.stop();
	} catch (ReadDriverException e) {
		e.printStackTrace();
	}
    
	lyr=(FLyrVect)polyRef;
	try {
		    ds = ((AlphanumericData)lyr).getRecordset();
		    ds.start();
		    d2 = ds.getFieldValue(0,1); //Obtengo la distancia del a 2do buffer
		    ds.stop();
	} catch (ReadDriverException e) {
		e.printStackTrace();
	}
	
	gvVectorLayer layer1 = new gvVectorLayer();
	layer1.create((FLyrVect)polyCortar);
	gvVectorLayer layer2 = new gvVectorLayer();
	layer2.create((FLyrVect)polyRef);
	
	DifferenceAlgorithm difalg = new DifferenceAlgorithm();
	params = difalg.getParameters();
	
	double a=Double.parseDouble(d1.toString());
	double b=Double.parseDouble(d2.toString());
	
	if(a > b)
	{	
	 params.getParameter(DifferenceAlgorithm.LAYER).setParameterValue(layer1);
	 params.getParameter(DifferenceAlgorithm.CLIPLAYER).setParameterValue(layer2);
	}
	else
	{
	 params.getParameter(DifferenceAlgorithm.LAYER).setParameterValue(layer2);
	 params.getParameter(DifferenceAlgorithm.CLIPLAYER).setParameterValue(layer1);	
	}	
	
	outputs = difalg.getOutputObjects();
	out = outputs.getOutput(DifferenceAlgorithm.RESULT);
	if(outUrl != null && !outUrl.equals(""))
	 out.setOutputChannel(new FileOutputChannel(outUrl));
	
	//out.setOutputChannel(new FileOutputChannel("Diferencia.shp"));
	difalg.execute(null, m_OutputFactory);
	
	return getPostProcessResult(difalg, false);   //out.getOutputObject();		
}

/**Elimina las depresiones de un MDE*/
public static FLayer eliminarDepresiones(Object rlayer, String outUrl) throws GeoAlgorithmExecutionException, IOException
{
 return (FLayer)getPostProcessResult(depresiones(rlayer, outUrl), false);
}

/**Elimina las depresiones de un MDE*/
public static Object eliminarDepresiones_GVRasterresult(Object antAlgResult, String outUrl) throws GeoAlgorithmExecutionException, IOException
{
	depresiones(antAlgResult, outUrl);
	
	return out.getOutputObject();
}

/**Dibuja un buffer por el metodo de gvSig*/
public static Object gvBuffer(double dist, View vista, FLayer l, boolean addResultToMap)
{
	Object res=null;
//	org.geocuba.foresta.utils.BufferTest buffer = new org.geocuba.foresta.utils.BufferTest();	
  	 try {
  		res= BufferTest.doit(dist, l, vista, addResultToMap);
  	} catch (LoadLayerException e) {
  		// TODO Auto-generated catch block
  		e.printStackTrace();
  	} catch (InitializeWriterException e) {
  		// TODO Auto-generated catch block
  		e.printStackTrace();
  	} catch (GeoprocessException e) {
  		// TODO Auto-generated catch block
  		e.printStackTrace();
  	}	
  	
  	return res;
}

/*private static DataStore openShapefile(String sFile) throws IOException {

		File file = new File( sFile );

		Map connect = new HashMap();
		connect.put( "url", file.toURL() );

		DataStore dataStore = DataStoreFinder.getDataStore( connect );

		return dataStore;

	}*/
 
 /**Devuelve la vista con el nombre entrado. Si este es null devuelve la 1ra vista encontrada*/
 public static View GetView(String viewName)
 {
  //View v = null;
  IWindow[] ws = PluginServices.getMDIManager().getAllWindows();
  for (int i = 0; i < ws.length/* && v == null*/; i++)
  {	  
   if(viewName!=null && !viewName.equals(""))
    {
	 if( (ws[i] instanceof View) && (ws[i].getWindowInfo().getTitle().equals(viewName)) )
	  return (View)ws[i];	 
    }
   else
	 if( ws[i] instanceof View )
	  return (View)ws[i];	   
  }
   return null; 
 }

/**Devuelve el map control de la vista*/
 public static MapControl GetMapControl(View vista)
 {
  if(vista!=null)
   return vista.getMapControl();
  else
   return null;	  
 }

/**Devuelve la capa activa; Si hay mas de una capa seleccionada
  * devuelve null*/
 public static FLayer GetActiveLayer(MapControl mapc)
 {
  FLayer [] lyrs = mapc.getMapContext().getLayers().getActives();
  if(lyrs.length > 1)
  {
   System.out.println("Existe mas de una capa activa");
   return null;
  }
  else
   return lyrs[0];	   
 }
 
 public static void DesactivateLayers()
 {
  FLayer []lyrs = GetView(null).getMapControl().getMapContext().getLayers().getActives();	 
  for(int i=0; i<lyrs.length; i++)
   lyrs[i].setActive(false);	  
 }
 
/**Devuelve la posicion relativa de un punto respecto a una linea
 0-derecha 1-izquierda 2-delante 3-detras 4-contenido
 x0 y y0 coordenadas del pto de analisis
 x1 y y1 coordenadas del 1er pto del segmento
 x2 y y2 coordenadas del 2do pto del segmento
 Los dos ultimos indicaran el sentido del vector**/
public static int GetRelativePosition(double x0, double y0, double x1, double y1, double x2, double y2)
{
 double d = (y2-y1)*x0+(x1-x2)*y0+(x2*y1-y2*x1);
 if(d!=0)
 {	 
  if(d<0)
	return 1; // a la izquierda
  else
    return 0; // a la derecha	  
 }
 else
 {
  double t=0;
  if(x1==x2)
   t = (y0-y1)/(y2-y1);	  
  else
   t = (x0-x1)/(x2-x1);	 
  
  if(t>1)
   return 2;	  
  if(t<0)
   return 3;	
  else
   return 4;	  
 }	 
}

/** Dada una linea de vuelve la misma linea con las puntas extendidas a la distancia deseada*/
public static Object getExtendedLines(FLayer lyr, double dist, String outUrl)
{
	try {
	    m_OutputFactory = new gvOutputFactory();

		gvVectorLayer layer = new gvVectorLayer();
		layer.create((FLyrVect)lyr);
		
		ExtendLastLineAlgorithm alg = new ExtendLastLineAlgorithm();
		ParametersSet params = alg.getParameters();
		params.getParameter(ExtendLastLineAlgorithm.LINES).setParameterValue(layer);
		params.getParameter(ExtendLastLineAlgorithm.LARGE).setParameterValue(dist);

		outputs = alg.getOutputObjects();
		out = outputs.getOutput("RESULT");
		if(outUrl != null && !outUrl.equals(""))
			 out.setOutputChannel(new FileOutputChannel(outUrl));
		
		//out.setOutputChannel(new FileOutputChannel("Liness.shp"));
		alg.execute(null, m_OutputFactory);
		
		return getPostProcessResult(alg, false);   //out.getOutputObject();
		
		
	} catch (WrongParameterIDException e) {
		e.printStackTrace();
	} catch (WrongOutputIDException e) {
		e.printStackTrace();
	} catch (GeoAlgorithmExecutionException e) {
		e.printStackTrace();
	}	
	
	return null;
}

/**Obttiene el MDE*/
public static Object HallarMDE(FLayer player, String field, String outUrl, double cellSize) throws GeoAlgorithmExecutionException, IOException
{
m_OutputFactory = new gvOutputFactory();
	
	gvVectorLayer layer = new gvVectorLayer();
	layer.create((FLyrVect)player);
	
	RasterizeVectorLayerAlgorithm alg = new RasterizeVectorLayerAlgorithm();
	params = alg.getParameters();
	params.getParameter(RasterizeVectorLayerAlgorithm.LAYER).setParameterValue(layer);
	params.getParameter(RasterizeVectorLayerAlgorithm.FIELD).setParameterValue(layer.getFieldIndexByName(field));

	/*extent = new GridExtent(layer);
	extent.setCellSize(cellSize);
	alg.setGridExtent(extent);*/
	
	AnalysisExtent extent = new AnalysisExtent(layer);
	extent.setCellSize(cellSize);
	alg.setAnalysisExtent(extent);
	
	alg.setUserCanDefineAnalysisExtent(true);
	System.out.println("Se ajusto ? : " + alg.adjustOutputExtent());
	//alg.getAnalysisExtent().setCellSize(cellSize);

	outputs = alg.getOutputObjects();
	Output rasterizedLayer = outputs.getOutput(RasterizeVectorLayerAlgorithm.RESULT);
	rasterizedLayer.setOutputChannel(new FileOutputChannel(outUrl+"MDERasterizado.tif"));
	
	alg.execute(null, m_OutputFactory);
	gvRasterLayer rlayer = new gvRasterLayer();
	Object rasterized = getPostProcessResult(alg, false);
	rlayer.create((FLyrRasterSE)rasterized);
	
	    CloseGapsAlgorithm cgaps = new CloseGapsAlgorithm();
		params = cgaps.getParameters();
		params.getParameter(CloseGapsAlgorithm.INPUT).setParameterValue(rlayer);
		params.getParameter(CloseGapsAlgorithm.THRESHOLD).setParameterValue(0.01);
		outputs = cgaps.getOutputObjects();
		out = outputs.getOutput(CloseGapsAlgorithm.RESULT);
		if(outUrl != null && !outUrl.equals(""))
		 out.setOutputChannel(new FileOutputChannel(outUrl));
		//filledLayer.setOutputChannel(new FileOutputChannel(outUrl+"MDERellenado.tif"));
		
		cgaps.execute(null, m_OutputFactory);
		
		return getPostProcessResult(cgaps, false);  //out.getOutputObject();
}

/** Obtiene los objetos de la 1ra capa que intersectan la segunda */
public static FLyrVect intersectObjects(FLyrVect layer1, FLyrVect layer2, String outUrl) throws GeoAlgorithmExecutionException
{
	m_OutputFactory = new gvOutputFactory();

	gvVectorLayer l1 = new gvVectorLayer();
	gvVectorLayer l2 = new gvVectorLayer();
	l1.create(layer1);
	l2.create(layer2);
	
	    ObjectIntersetcsAlgorithm io = new ObjectIntersetcsAlgorithm();
		ParametersSet params = io.getParameters();
		params.getParameter(ObjectIntersetcsAlgorithm.LAYER1).setParameterValue(l1);
		params.getParameter(ObjectIntersetcsAlgorithm.LAYER2).setParameterValue(l2);

		outputs = io.getOutputObjects();
		out = outputs.getOutput(ObjectIntersetcsAlgorithm.RESULT);
		if(outUrl != null && !outUrl.equals(""))
			 out.setOutputChannel(new FileOutputChannel(outUrl));
		
		//out.setOutputChannel(new FileOutputChannel("intres.shp"));
		io.execute(null, m_OutputFactory);
		
		return (FLyrVect)getPostProcessResult(io, false);
}

/**Convierte de lineas a puntos*/
public static Object LinesToPoints(Object vectorly, double dist, String outUrl) throws GeoAlgorithmExecutionException, IOException
{
	m_OutputFactory=new gvOutputFactory();
	gvVectorLayer layer = new gvVectorLayer();
	layer.create((FLyrVect)vectorly);
	
	LinesToEquispacedPointsAlgorithm points = new LinesToEquispacedPointsAlgorithm();
	ParametersSet paramets = points.getParameters();
	paramets.getParameter(LinesToEquispacedPointsAlgorithm.LINES).setParameterValue(layer);
	paramets.getParameter(LinesToEquispacedPointsAlgorithm.DISTANCE).setParameterValue(dist);
	
	outputs = points.getOutputObjects();
	out = outputs.getOutput(LinesToEquispacedPointsAlgorithm.RESULT);
	if(outUrl != null && !outUrl.equals(""))
     out.setOutputChannel(new FileOutputChannel(outUrl));
	//out.setOutputChannel(new FileOutputChannel(outUrl+"points.shp"));

	points.execute(null, m_OutputFactory);
	
	return   getPostProcessResult(points, false); //out.getOutputObject();	
}

/**Convierte de polygonos a lineas*/
public static Object PolygonToPolylines(Object vectorly, String outUrl) throws GeoAlgorithmExecutionException, IOException
{
	m_OutputFactory=new gvOutputFactory();
	gvVectorLayer layer = new gvVectorLayer();
	layer.create((FLyrVect)vectorly);
	
	PolygonsToPolylinesAlgorithm polylines = new PolygonsToPolylinesAlgorithm();
	ParametersSet paramets = polylines.getParameters();
	paramets.getParameter(PolygonsToPolylinesAlgorithm.LAYER).setParameterValue(layer);
	
	outputs = polylines.getOutputObjects();
	out = outputs.getOutput(PolygonsToPolylinesAlgorithm.RESULT);
	if(outUrl != null && !outUrl.equals(""))
		 out.setOutputChannel(new FileOutputChannel(outUrl));

	polylines.execute(null, m_OutputFactory);
	
	return getPostProcessResult(polylines, false);  //out.getOutputObject();	
}

/** Obtiene la Pendiente*/
public static Object Pendiente(Object mde, String outUrl) throws GeoAlgorithmExecutionException
{
    gvRasterLayer rlayer = validateInput(mde);
 	
	m_OutputFactory = new gvOutputFactory();

    SlopeAlgorithm pend = new SlopeAlgorithm();
	ParametersSet params = pend.getParameters();
	params.getParameter(SlopeAlgorithm.DEM).setParameterValue(rlayer);
	params.getParameter(SlopeAlgorithm.METHOD).setParameterValue(SlopeAlgorithm.METHOD_ZEVENBERGEN);
	params.getParameter(SlopeAlgorithm.UNITS).setParameterValue(SlopeAlgorithm.UNITS_DEGREES);

	outputs = pend.getOutputObjects();
	out = outputs.getOutput(SlopeAlgorithm.SLOPE);
	if(outUrl != null && !outUrl.equals(""))
		 out.setOutputChannel(new FileOutputChannel(outUrl));
	
	//out.setOutputChannel(new FileOutputChannel("pendiente.tif"));
	
	pend.execute(null, m_OutputFactory);
	
	return /*getPostProcessResult(pend, false);*/   out.getOutputObject();
}

/**Halla la red de drenaje y devuelve un raster*/
public static Object RedDrenaje(Object mde, Object acumFluj,String outUrl) throws GeoAlgorithmExecutionException, IOException
{
gvRasterLayer rlayer = validateInput(mde);
	
	m_OutputFactory=new gvOutputFactory();
	
	XMLEntity xml;
	double THRESHOLD = 1000000.0;
	
	try {
		xml = XMLUtils.persistenceFromXML("gvSIG"+Global.fileSeparator+"extensiones"+Global.fileSeparator+"org.geocuba.foresta"+Global.fileSeparator+"configForesta.xml");
		int index = xml.firstIndexOfChild("name", "algutils_options");
		xml = xml.getChild(index);
		THRESHOLD = xml.getDoubleProperty("reddrenaje_umbral");
		
	} catch (ConfigurationException e) {
		e.printStackTrace();
	}
	
	ChannelNetworkAlgorithm drenalg = new ChannelNetworkAlgorithm();
	params = drenalg.getParameters();
	params.getParameter(ChannelNetworkAlgorithm.DEM).setParameterValue(rlayer);
	params.getParameter(ChannelNetworkAlgorithm.THRESHOLDLAYER).setParameterValue(acumFluj);
	params.getParameter(ChannelNetworkAlgorithm.METHOD).setParameterValue("Menor que");
	params.getParameter(ChannelNetworkAlgorithm.THRESHOLD).setParameterValue(THRESHOLD);
	
	outputs = drenalg.getOutputObjects();
	out = outputs.getOutput(ChannelNetworkAlgorithm.NETWORK);
	if(outUrl != null && !outUrl.equals(""))
     out.setOutputChannel(new FileOutputChannel(outUrl));

	drenalg.execute(null, m_OutputFactory);
	
	return   /* getPostProcessResult(drenalg, true); */  out.getOutputObject();
}

/**Halla la red de drenaje y devuelve una capa vectorial*/
public static Object RedDrenajeV(Object mde, Object acumFluj, String vectUrl, String raterUrl) throws GeoAlgorithmExecutionException, IOException
{
gvRasterLayer rlayer = validateInput(mde);
	
	m_OutputFactory=new gvOutputFactory();
	
	XMLEntity xml;
	double THRESHOLD = 1000000.0;
	
	try {
		xml = XMLUtils.persistenceFromXML("gvSIG"+Global.fileSeparator+"extensiones"+Global.fileSeparator+"org.geocuba.foresta"+Global.fileSeparator+"configForesta.xml");
		int index = xml.firstIndexOfChild("name", "algutils_options");
		xml = xml.getChild(index);
		THRESHOLD = xml.getDoubleProperty("reddrenaje_umbral");
		
	} catch (ConfigurationException e) {
		e.printStackTrace();
	}
	
	ChannelNetworkAlgorithm drenalg = new ChannelNetworkAlgorithm();
	params = drenalg.getParameters();
	params.getParameter(ChannelNetworkAlgorithm.DEM).setParameterValue(rlayer);
	params.getParameter(ChannelNetworkAlgorithm.THRESHOLDLAYER).setParameterValue(acumFluj);
	params.getParameter(ChannelNetworkAlgorithm.METHOD).setParameterValue("Menor que");
	params.getParameter(ChannelNetworkAlgorithm.THRESHOLD).setParameterValue(THRESHOLD);
	
	outputs = drenalg.getOutputObjects();
	out = outputs.getOutput(ChannelNetworkAlgorithm.NETWORKVECT);
	if(vectUrl != null && !vectUrl.equals(""))
	 out.setOutputChannel(new FileOutputChannel(vectUrl));
	
	Output out2 = outputs.getOutput(ChannelNetworkAlgorithm.NETWORK);
	if(raterUrl != null && !raterUrl.equals(""))
	 out2.setOutputChannel(new FileOutputChannel(raterUrl));

	drenalg.execute(null, m_OutputFactory);
	
	return getVectorialResult(drenalg,false);   //out.getOutputObject();
}

/** Sombrea el relieve segun los parametros entrados */
public static Object relieveSombreado(Object mde, double dec, double azimut, double ex, String outUrl) throws GeoAlgorithmExecutionException
{
    gvRasterLayer rlayer = validateInput(mde);
	
	m_OutputFactory = new gvOutputFactory();

		HillshadeAlgorithm alg = new HillshadeAlgorithm();
		ParametersSet params = alg.getParameters();
		params.getParameter(HillshadeAlgorithm.DEM).setParameterValue(rlayer);
		params.getParameter(HillshadeAlgorithm.METHOD).setParameterValue(HillshadeAlgorithm.METHOD_STANDARD);
		params.getParameter(HillshadeAlgorithm.DECLINATION).setParameterValue(dec);
		params.getParameter(HillshadeAlgorithm.AZIMUTH).setParameterValue(azimut);
		params.getParameter(HillshadeAlgorithm.EXAGGERATION).setParameterValue(ex);

		outputs = alg.getOutputObjects();
		out = outputs.getOutput(HillshadeAlgorithm.SHADED);
		if(outUrl != null && !outUrl.equals(""))
			 out.setOutputChannel(new FileOutputChannel(outUrl));
		
		//out.setOutputChannel(new FileOutputChannel("sombRel.tif"));
		alg.execute(null, m_OutputFactory);
		
		return getPostProcessResult(alg, false);  //out.getOutputObject();
}

/** Reclasifica una capa raster en 1 solo valor (puede modificarse para varios) */
public static Object reclassifyRaster(Object raster, FixedTableModel table, String outUrl) throws GeoAlgorithmExecutionException
{
gvRasterLayer rlayer = validateInput(raster);
	
	m_OutputFactory = new gvOutputFactory();

	    ReclassifyAlgorithm reclsf = new ReclassifyAlgorithm();
		ParametersSet params = reclsf.getParameters();
		params.getParameter(ReclassifyAlgorithm.INPUT).setParameterValue(rlayer);
		params.getParameter(ReclassifyAlgorithm.METHOD).setParameterValue(ReclassifyAlgorithm.METHOD_LOWER_THAN_OR_EQUAL);
		params.getParameter(ReclassifyAlgorithm.LUT).setParameterValue(table);

		outputs = reclsf.getOutputObjects();
		out = outputs.getOutput(ReclassifyAlgorithm.RECLASS);
		if(outUrl != null && !outUrl.equals(""))
			 out.setOutputChannel(new FileOutputChannel(outUrl));
		
		//out.setOutputChannel(new FileOutputChannel("reclasify.tif"));
		
		reclsf.execute(null, m_OutputFactory);
		
		return out.getOutputObject();
}

/** Elimina un elemento segun un campo y un valor dados*/
public static Object RemoveItem(FLayer layer, String field, String value, String outUrl) throws GeoAlgorithmExecutionException
{
    m_OutputFactory = new gvOutputFactory();
	
	gvVectorLayer lyr = new gvVectorLayer();
	lyr.create((FLyrVect)layer);

	    RemoveItemAlgorithm ralg = new RemoveItemAlgorithm();
		ParametersSet params = ralg.getParameters();
		params.getParameter(RemoveItemAlgorithm.LAYER).setParameterValue(lyr);
		params.getParameter(RemoveItemAlgorithm.FIELD).setParameterValue(field);
		params.getParameter(RemoveItemAlgorithm.VALUE).setParameterValue(value);

		outputs = ralg.getOutputObjects();
		out = outputs.getOutput(RemoveItemAlgorithm.RESULT);
		if(outUrl != null && !outUrl.equals(""))
			 out.setOutputChannel(new FileOutputChannel(outUrl));
		//out.setOutputChannel(new FileOutputChannel("itemRemovedLayer.shp"));
		
		ralg.execute(null, m_OutputFactory);
		
		return getPostProcessResult(ralg, false); //out.getOutputObject();
}

/**Separar entidades*/
public static Object SepararEntidades(Object vectorly, String outUrl) throws GeoAlgorithmExecutionException, IOException
{
	m_OutputFactory=new gvOutputFactory();
	gvVectorLayer layer = new gvVectorLayer();
	layer.create((FLyrVect)vectorly);
	
	SplitMultipartAlgorithm split = new SplitMultipartAlgorithm();
	ParametersSet paramets = split.getParameters();
	paramets.getParameter(SplitMultipartAlgorithm.INPUT).setParameterValue(layer);
	
	outputs = split.getOutputObjects();
	out = outputs.getOutput(SplitMultipartAlgorithm.RESULT);
	if(outUrl != null && !outUrl.equals(""))
		 out.setOutputChannel(new FileOutputChannel(outUrl));

	split.execute(null, m_OutputFactory);
	return getPostProcessResult(split, false);  //out.getOutputObject();
}

/**Devuelve una capa de secciones tranversales a un rio*/
public static Object SeccionesTranversales(Object mde, Object linealhidro, double dist, double width, String outUrl) throws GeoAlgorithmExecutionException, IOException
{
gvRasterLayer rlayer = validateInput(mde);
	
	m_OutputFactory=new gvOutputFactory();
	gvVectorLayer layer = new gvVectorLayer();
	layer.create((FLyrVect)linealhidro);
	
	CrossSectionsAlgorithm sectranv = new CrossSectionsAlgorithm();
	ParametersSet paramets = sectranv.getParameters();
	paramets.getParameter(CrossSectionsAlgorithm.ROUTE).setParameterValue(layer);
	paramets.getParameter(CrossSectionsAlgorithm.DEM).setParameterValue(rlayer);
	paramets.getParameter(CrossSectionsAlgorithm.DISTANCE).setParameterValue(dist);
	paramets.getParameter(CrossSectionsAlgorithm.WIDTH).setParameterValue(width);
	paramets.getParameter(CrossSectionsAlgorithm.NUMPOINTS).setParameterValue(1);
	
	outputs = sectranv.getOutputObjects();
	out = outputs.getOutput(CrossSectionsAlgorithm.RESULT);
	if(outUrl != null && !outUrl.equals(""))
		 out.setOutputChannel(new FileOutputChannel(outUrl));
	
	sectranv.execute(null, m_OutputFactory);
	
	return out.getOutputObject();	
}

/**Devuelve una capa de secciones tranversales a un rio sin necesidad de MDE*/
public static Object SeccionesTranversales(Object linealhidro, double dist, double width, String outUrl) throws GeoAlgorithmExecutionException, IOException
{
	m_OutputFactory=new gvOutputFactory();
	gvVectorLayer layer = new gvVectorLayer();
	layer.create((FLyrVect)linealhidro);
	
	CrossSectionsAlgorithmM sectranv = new CrossSectionsAlgorithmM();
	ParametersSet paramets = sectranv.getParameters();
	paramets.getParameter(CrossSectionsAlgorithmM.ROUTE).setParameterValue(layer);
	paramets.getParameter(CrossSectionsAlgorithmM.DISTANCE).setParameterValue(dist);
	paramets.getParameter(CrossSectionsAlgorithmM.WIDTH).setParameterValue(width);
	paramets.getParameter(CrossSectionsAlgorithmM.NUMPOINTS).setParameterValue(1);
	
	outputs = sectranv.getOutputObjects();
	out = outputs.getOutput(CrossSectionsAlgorithmM.RESULT);
	if(outUrl != null && !outUrl.equals(""))
		 out.setOutputChannel(new FileOutputChannel(outUrl));
	
	//out.setOutputChannel(new FileOutputChannel("seccTranv.shp"+outUrl));

	sectranv.execute(null, m_OutputFactory);
	
	return getPostProcessResult(sectranv, false);  //out.getOutputObject();	
}

/** Devuelve las secciones Tranversales separadas por la linea principal*/
public static Object SeparateCrossLines(int lado, FLyrVect linealhidro, double dist, double width, String outUrl) throws GeoAlgorithmExecutionException, IOException
{
m_OutputFactory=new gvOutputFactory();
	
	gvVectorLayer layer = new gvVectorLayer();
	layer.create(linealhidro);
	
	SeparateCrossLinesAlgorithm sectranv = new SeparateCrossLinesAlgorithm();
	ParametersSet paramets = sectranv.getParameters();
	paramets.getParameter(SeparateCrossLinesAlgorithm.ROUTE).setParameterValue(layer);
	//paramets.getParameter(SeparateCrossLinesAlgorithm.DEM).setParameterValue(mde);
	paramets.getParameter(SeparateCrossLinesAlgorithm.DISTANCE).setParameterValue(dist);
	paramets.getParameter(SeparateCrossLinesAlgorithm.WIDTH).setParameterValue(width);
	paramets.getParameter(SeparateCrossLinesAlgorithm.NUMPOINTS).setParameterValue(1);
	paramets.getParameter(SeparateCrossLinesAlgorithm.SIDE).setParameterValue(lado);
	
	outputs = sectranv.getOutputObjects();
	out = outputs.getOutput(SeparateCrossLinesAlgorithm.RESULT);
	if(outUrl != null && !outUrl.equals(""))
		 out.setOutputChannel(new FileOutputChannel(outUrl));
	
	//out.setOutputChannel(new FileOutputChannel("sepLines.shp"+outUrl));

	sectranv.execute(null, m_OutputFactory);
	
	return getPostProcessResult(sectranv, false);   //out.getOutputObject();
}

/**Cuenca Vertiente a un punto dado*/
public static Object TiemposDeSalida(Object mde, Object redDrenaje, double ratio, Point2D point, String outUrl) throws GeoAlgorithmExecutionException, IOException
{
    gvRasterLayer rlayer = validateInput(mde);
	
	m_OutputFactory=new gvOutputFactory();
	
	IsocronesAlgorithm tsalidalg = new IsocronesAlgorithm();
	params = tsalidalg.getParameters();
	params.getParameter(IsocronesAlgorithm.DEM).setParameterValue(rlayer);
	params.getParameter(IsocronesAlgorithm.NETWORK).setParameterValue(redDrenaje);
	params.getParameter(IsocronesAlgorithm.RATIO).setParameterValue(ratio);
	params.getParameter(IsocronesAlgorithm.OUTLET).setParameterValue(point);
	
	outputs = tsalidalg.getOutputObjects();
	out = outputs.getOutput(IsocronesAlgorithm.TIME);
	if(outUrl != null && !outUrl.equals(""))
	 out.setOutputChannel(new FileOutputChannel(outUrl));
	
	tsalidalg.execute(null, m_OutputFactory);
	
	return out.getOutputObject();		
}

/**Vectoriza un raster*/
public static Object Vectorizar(Object raster, String outUrl) throws GeoAlgorithmExecutionException, IOException
{
    gvRasterLayer rlayer = validateInput(raster);
	m_OutputFactory=new gvOutputFactory();
	
	VectorizeAlgorithm vect = new VectorizeAlgorithm();
	params = vect.getParameters();
	params.getParameter(VectorizeAlgorithm.LAYER).setParameterValue(rlayer);
	
	outputs = vect.getOutputObjects();
	out = outputs.getOutput(VectorizeAlgorithm.RESULT);
	
	if(outUrl != null && !outUrl.equals(""))
	 out.setOutputChannel(new FileOutputChannel(outUrl));
	
	vect.execute(null, m_OutputFactory);
	
	return getPostProcessResult(vect, false);  //out.getOutputObject();	
}

/** Determinar un perfil longitudinal*/
public static Object LongPerfil(Object mde, FLayer route, String outUrl) throws GeoAlgorithmExecutionException
{
	m_OutputFactory = new gvOutputFactory();

	gvVectorLayer layer = new gvVectorLayer();
	layer.create((FLyrVect)route);
	
    gvRasterLayer rlayer = new gvRasterLayer();
    rlayer.create((FLyrRasterSE)mde);
	
    ProfileAlgorithm perfil = new ProfileAlgorithm();
	ParametersSet params = perfil.getParameters();
	params.getParameter(ProfileAlgorithm.DEM).setParameterValue(rlayer);
	params.getParameter(ProfileAlgorithm.ROUTE).setParameterValue(layer);
	params.getParameter(ProfileAlgorithm.INTERPOLATE).setParameterValue(true);
	params.getParameter(ProfileAlgorithm.LAYERS).setParameterValue(new ArrayList());
	params.getParameter(ProfileAlgorithm.TITLE).setParameterValue(route.getName());

	outputs = perfil.getOutputObjects();
	out = outputs.getOutput(ProfileAlgorithm.PROFILEPOINTS);
	if(outUrl != null && !outUrl.equals(""))
     out.setOutputChannel(new FileOutputChannel(outUrl));
	
	perfil.execute(null, m_OutputFactory);
	
	//AdditionalResults.addComponent(new ObjectAndDescription(perfil.getName() + "[" + route.getName() + "]", outputs.getOutput(ProfileAlgorithm.GRAPH).getOutputObject()));
	return getPostProcessResult(perfil, false);//out.getOutputObject();
}
  
}
