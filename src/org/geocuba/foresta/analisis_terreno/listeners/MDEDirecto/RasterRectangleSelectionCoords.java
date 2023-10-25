package org.geocuba.foresta.analisis_terreno.listeners.MDEDirecto;

import org.geocuba.foresta.analisis_terreno.interseccion.recortes.raster.ClipRaster;
import org.gvsig.fmap.raster.tools.SaveRasterListenerImpl;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.tools.Events.RectangleEvent;

public class RasterRectangleSelectionCoords extends SaveRasterListenerImpl 
{
    private ClipRaster inter = null;
	
	public RasterRectangleSelectionCoords(MapControl mapCtrl, ClipRaster _inter) 
	{
		super(mapCtrl);
        inter = _inter;  
	}

	public void rectangle(RectangleEvent event) 
	{
		super.rectangle(event);

		if (PluginServices.getMainFrame() != null)
		 PluginServices.getMainFrame().enableControls();

		 if(inter!=null && rect!=null) 
		 {
		  inter.setRectangle(rect);  	 
		  PluginServices.cancelableBackgroundExecution(inter);
		 }
	}
}
