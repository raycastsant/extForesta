package org.geocuba.foresta.analisis_terreno.listeners;

import java.awt.Color;
import java.io.IOException;
import org.geocuba.foresta.analisis_terreno.CuencasExt;
import org.geocuba.foresta.analisis_terreno.interseccion._Intersect;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.gvsig.raster.grid.GridException;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.exceptions.layers.LoadLayerException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.core.symbols.ILineSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import es.unex.sextante.exceptions.GeoAlgorithmExecutionException;

/**Clase para hallar la red de drenaje a partir de
 *  un mde hallado por interseccion de relieve*/
public class RedDrenaje extends _Intersect {
	
	public RedDrenaje(FLyrVect relieve, boolean _useLastMDE, MapControl _mapCtrl)throws ReadDriverException, LoadLayerException, GridException, InterruptedException {
		super(relieve, _mapCtrl, _useLastMDE);
		//mapCtrl = _mapCtrl;
	}
	
	protected void SiguienteProceso()
	{
		  Object acflujo = null;
		  FLyrVect l = null;
		  
			try {
			     
			   setCurrentStep(0);
			   setNote("Hallando acumulación de flujo");
			  
			   if(!isCanceled() && mde!=null)
				acflujo = AlgUtils.AcumulacionFlujo(mde, "");
			   else
				cancel();   
			   reportStep();
			   
			   setCurrentStep(0);
			   setNote("Hallando red de drenaje");
			   
			   
			   if(!isCanceled() && acflujo!=null)
				 l = (FLyrVect)AlgUtils.RedDrenajeV(mde, acflujo, null, null);
			   else
				cancel();   
			  
				if(!isCanceled())	
				{
				 FLayer red = mapCtrl.getMapContext().getLayers().getLayer("Red_Drenaje");
				 if(red != null)
				  mapCtrl.getMapContext().getLayers().removeLayer("Red_Drenaje");
				 
				 l.setName("Red_Drenaje");
				 AlgUtils.addLayertoView(l);
				 
				 ISymbol symb = l.getLegend().getDefaultSymbol();
				 ILineSymbol linesymb = (ILineSymbol)symb;
				 Color col = new Color(255, 102, 0, 255);
				 linesymb.setLineColor(col); 
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
	 RedDrenaje reddren;
	try {
		reddren = new RedDrenaje(this.getLayer(), useLastMDE, mapCtrl);
		CuencasExt.initializeListener(reddren);
		
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
