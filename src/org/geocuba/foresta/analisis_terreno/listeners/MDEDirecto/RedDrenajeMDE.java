package org.geocuba.foresta.analisis_terreno.listeners.MDEDirecto;

import java.awt.Color;
import java.io.IOException;
import org.geocuba.foresta.analisis_terreno.interseccion.recortes.raster.ClipRaster;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.geocuba.foresta.herramientas.utiles.Global;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.grid.GridException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.exceptions.layers.LoadLayerException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.core.symbols.ILineSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import es.unex.sextante.exceptions.GeoAlgorithmExecutionException;
import es.unex.sextante.gvsig.core.gvRasterLayer;

/**Clase para obtener red de drenaje de nivel a partir de un ráster*/
public class RedDrenajeMDE extends ClipRaster
{
	public RedDrenajeMDE(FLyrRasterSE raster,  MapControl _mapCtrl)throws ReadDriverException, LoadLayerException, GridException, InterruptedException 
	{
		super(raster, _mapCtrl);
	}
	
	protected void NextProcess()
	{ 
		Object acflujo = null;
		Object sdep = null;
		FLyrVect reddren = null;
		  
		try {
		   setCurrentStep(0);
		   setNote("Eliminando depresiones");
			  
		   if(!isCanceled() && clip!=null)
		   {
			gvRasterLayer rclip = new gvRasterLayer();
			rclip.create((FLyrRasterSE)clip);
			   
			sdep = AlgUtils.eliminarDepresiones(rclip, "");
		   }	
		   else
		   {	   
			setCanceled(true);   
		    reportStep();
		    return;
		   } 
			
		   setCurrentStep(0);
		   setNote("Hallando acumulación de flujo");
		  
		   if(!isCanceled() && sdep!=null)
		   {	   
			Global.setMDE(sdep);
			acflujo = AlgUtils.AcumulacionFlujo(sdep, "");
		   }	
		   else
		   {	   
				setCanceled(true);   
			    reportStep();
			    return;
		   };
		   
		   setCurrentStep(0);
		   setNote("Hallando red de drenaje");
		   
		   if(!isCanceled() && acflujo!=null)
		   {
			reddren = (FLyrVect)AlgUtils.RedDrenajeV(sdep, acflujo, "", "");
			reddren.setName("Red_Drenaje");
		   }	
		   else
		   {	   
				setCanceled(true);   
			    reportStep();
			    return;
		    }   
		  
			if(!isCanceled())	
			{
			 ISymbol symb = reddren.getLegend().getDefaultSymbol();
			 ILineSymbol linesymb = (ILineSymbol)symb;
			 Color col = new Color(0, 0, 255, 255);
			 linesymb.setLineColor(col); 
			 
			 AlgUtils.addLayertoView(reddren);
			} 
			
		  reportStep();
		  
		} catch (GeoAlgorithmExecutionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
