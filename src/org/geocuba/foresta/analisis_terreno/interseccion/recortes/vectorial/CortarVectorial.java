package org.geocuba.foresta.analisis_terreno.interseccion.recortes.vectorial;

import org.geocuba.foresta.analisis_terreno.interseccion.recortes.Cortar;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.gvsig.raster.grid.GridException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.exceptions.layers.LoadLayerException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import es.unex.sextante.core.Sextante;
import es.unex.sextante.exceptions.GeoAlgorithmExecutionException;

public abstract class CortarVectorial extends Cortar {
	
	public CortarVectorial(FLyrVect l, MapControl mctrl) throws GridException, InterruptedException,LoadLayerException{
		
		layer = l;
		mapCtrl = mctrl;
		
		 setInitialStep(0);
		 setDeterminatedProcess(false);
		 setNote(PluginServices.getText(this, "Procesando información"));
		 setFinalStep(1);
	}
	
	
	/**Método que realiza el proceso de corte de una capa vectorial con un rectángulo*/
	protected FLayer clipProcess(double min_x, double min_y, double max_x, double max_y, FLayer layer)
	{
		FLayer poly = getClippingPoly(min_x, min_y, max_x, max_y);  
		//String clipName = "clip"+filenumber+".shp";
		   
	    	 Sextante.initialize(); 
	    	 try {
				
				return (FLayer)AlgUtils.ClipVectorial(layer, (FLyrVect)poly, null); 
				
			} catch (GeoAlgorithmExecutionException e) {
				e.printStackTrace();
			}
	    	 
	    return null;		
	}
	
}
