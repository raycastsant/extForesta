package org.geocuba.foresta.analisis_terreno.listeners.MDEDirecto;

import org.geocuba.foresta.analisis_terreno.interseccion.recortes.raster.ClipRaster;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.geocuba.foresta.herramientas.utiles.Global;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.grid.GridException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.exceptions.layers.LoadLayerException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import es.unex.sextante.exceptions.GeoAlgorithmExecutionException;

/**Clase para obtener la orientacion de la pendiente
 * en cada punto del terreno*/
public class RelieveMDE extends ClipRaster
{
	
	public RelieveMDE(FLyrRasterSE raster,  MapControl _mapCtrl)throws ReadDriverException, LoadLayerException, GridException, InterruptedException {
		super(raster, _mapCtrl);
	}
	
	protected void NextProcess()
	{
		FLayer pend = null;
		
		try {
			
		   setCurrentStep(0);
		   setNote("Hallando Relieve");
		  
		        if(!isCanceled() && clip!=null)
			    {
		        	Global.setMDE(clip);
		        	
			        pend = (FLayer)AlgUtils.relieveSombreado(clip, 45, 315, 2, "");  
			        pend.setName("Relieve_sombreado");
			        
                    AlgUtils.addLayertoView(pend);
			     }
		        
		} catch (GeoAlgorithmExecutionException e) {
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		  reportStep();
	}
 	
	public void finished() 
	{
	 //inicializo el listener nuevamente	
	/*  RelieveSombreado relS;
	  try {
		relS = new RelieveSombreado(this.getLayer(), useLastMDE, mapCtrl);
		CuencasExt.initializeListener(relS);
		
	   } catch (ReadDriverException e) {
		e.printStackTrace();
	   }	*/
	}
}
