package org.geocuba.foresta.analisis_terreno.listeners;

import org.geocuba.foresta.analisis_terreno.CuencasExt;
import org.geocuba.foresta.analisis_terreno.interseccion._Intersect;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.gvsig.raster.grid.GridException;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.exceptions.layers.LoadLayerException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.unex.sextante.exceptions.GeoAlgorithmExecutionException;

/**Clase para mostrar las elevaciones del relieve 
 * mediante un raster*/
public class RelieveSombreado extends _Intersect {
	
	public RelieveSombreado(FLyrVect relieve, boolean _useLastMDE, MapControl _mapCtrl)throws ReadDriverException, LoadLayerException, GridException, InterruptedException {
		super(relieve, _mapCtrl, _useLastMDE);
//		mapCtrl = _mapCtrl;
	}
	
	@Override
	protected void SiguienteProceso() 
	{
		  FLayer relsomb = null;	
		  
		  if(mapCtrl.getMapContext().getLayers().getLayer("Relieve_sombreado") != null)
		   mapCtrl.getMapContext().getLayers().removeLayer("Relieve_sombreado");
		  
			try {
			     
			   setCurrentStep(0);
			   setNote("Sombreando relieve");
			  
			   if(!isCanceled() && mde!=null)
				relsomb = (FLayer)AlgUtils.relieveSombreado(mde, 30, 315, 1, null);
			   else
				cancel();    
			   
				if(!isCanceled())	
				{
				 relsomb.setName("Relieve_sombreado");	
				 AlgUtils.addLayertoView(relsomb);
				} 
				
			  reportStep();
			  
			} catch (GeoAlgorithmExecutionException e) {
				e.printStackTrace();
			}
	}
 	
	public void finished() 
	{
	 //inicializo el listener nuevamente	
	  RelieveSombreado relS;
	  try {
		relS = new RelieveSombreado(this.getLayer(), useLastMDE, mapCtrl);
		CuencasExt.initializeListener(relS);
		
	   } catch (ReadDriverException e) {
		e.printStackTrace();
	   } catch (LoadLayerException e) {
		e.printStackTrace();
	} catch (GridException e) {
		e.printStackTrace();
	} catch (InterruptedException e) {
		e.printStackTrace();
	}	
	}
}
