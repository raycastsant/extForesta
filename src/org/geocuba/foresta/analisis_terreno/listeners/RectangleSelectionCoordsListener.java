package org.geocuba.foresta.analisis_terreno.listeners;

import org.geocuba.foresta.analisis_terreno.interseccion._Intersect;
import org.gvsig.fmap.raster.tools.SaveRasterListenerImpl;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.tools.Events.RectangleEvent;

public class RectangleSelectionCoordsListener extends SaveRasterListenerImpl {
   // private FLayer lyr;
    private _Intersect inter;
	
	public RectangleSelectionCoordsListener(MapControl mapCtrl, _Intersect _inter) 
	{
		super(mapCtrl);
		//lyr = rel;
		inter = _inter;
	}

	public void rectangle(RectangleEvent event) {
		super.rectangle(event);

		if (PluginServices.getMainFrame() != null)
				PluginServices.getMainFrame().enableControls();

		 if(inter!=null && rect!=null) 
		 {
		  //inter.initialize();	 
		  inter.setRectangle(rect);  	 
		  PluginServices.cancelableBackgroundExecution(inter/*new Intersect(lyr, rect)*/);
		 }	
	}
}
