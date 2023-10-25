package org.geocuba.foresta.analisis_terreno.interseccion.recortes;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.io.RasterDriverException;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.grid.OutOfGridException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.cit.gvsig.exceptions.layers.LoadLayerException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.core.FPoint2D;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.GeometryUtilities;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.drivers.ConcreteMemoryDriver;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;
import com.iver.utiles.swing.threads.AbstractMonitorableTask;

public abstract class Cortar extends AbstractMonitorableTask{
	
	//protected int number = 0;
	protected Rectangle2D rect;
	protected MapControl mapCtrl;
	protected FLayer clip;
	protected FLayer layer;
	protected boolean useLastMDE;
	
	@Override
	public void run() throws Exception, InterruptedException, RasterBufferInvalidException, GridException, OutOfGridException, LoadLayerException, NotSupportedExtensionException, RasterDriverException 
	{
	  if(!useLastMDE)	
	  {	
		Rectangle2D imageExtent = layer.getFullExtent();
		if(!rect.intersects(imageExtent))
		 return;

		double max_x = rect.getMaxX();
		double max_y = rect.getMaxY();
		double min_x = rect.getMinX();
		double min_y = rect.getMinY();
		if(imageExtent.getMaxX() < rect.getMaxX())
		{
			max_x = imageExtent.getMaxX();
		}
		if(imageExtent.getMaxY() < rect.getMaxY())
		{
			max_y = imageExtent.getMaxY();
		}
		if(imageExtent.getMinX() > rect.getMinX())
		{
			min_x = imageExtent.getMinX();
		}
		if(imageExtent.getMinY() > rect.getMinY())
		{
			min_y = imageExtent.getMinY();
		}
		
		setCurrentStep(0);
    	setNote("Hallando intersección...");
    	
	    	 clip = clipProcess(min_x, min_y, max_x, max_y, layer);
	    	 
	  }
	  
        NextProcess();
		
		reportStep();
//		if(isCanceled())
//		 setCanceled(false);	
	}
	
	//Metodo a redefinir
	protected abstract void NextProcess();
	
	public void setRectangle(Rectangle2D _rect)
	{
	 rect = _rect;	
	}
	
	/**Método que realiza el proceso de corte de un ráster con un rectángulo
	 * Devuelve el pedazo de ráster que se cortó*/
	protected abstract FLayer clipProcess(double min_x, double min_y, double max_x, double max_y, FLayer layer);
	
	protected static FLayer getClippingPoly(double min_x, double min_y, double max_x, double max_y)
	{
		FPoint2D []points = new FPoint2D[4];
		points[0] = new FPoint2D(min_x, min_y);
		points[1] = new FPoint2D(min_x, max_y);
		points[2] = new FPoint2D(max_x, max_y);
		points[3] = new FPoint2D(max_x, min_y);
		
		//Creando el polígono
		  ConcreteMemoryDriver driver = new ConcreteMemoryDriver();
		  driver.setShapeType(FShape.POLYGON);
		  List<String> campos = new ArrayList<String>();
		  campos.add("gid");
		  Value[] row = new Value[1];
		  row[0] = ValueFactory.createValue("1");
		  driver.getTableModel().setColumnIdentifiers(campos.toArray());
		  
		  IGeometry geom = GeometryUtilities.getPolygon2D(points);
		  driver.addGeometry(geom, row);
		  
		return LayerFactory.createLayer("Poly", driver, VistaManager.GetActiveView().getProjection());
	}

}
