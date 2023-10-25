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
import com.iver.cit.gvsig.fmap.core.symbols.IFillSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.ILineSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.gui.GUIUtil;
import es.unex.sextante.exceptions.GeoAlgorithmExecutionException;

/**Clase para hallar las cuencas a partir de 
 * un area de relieve seleccionada*/
public class Cuencas extends _Intersect{
	
	public Cuencas(FLyrVect relieve, boolean _useLastMDE, MapControl _mapCtrl)throws ReadDriverException, LoadLayerException, GridException, InterruptedException {
		super(relieve, _mapCtrl, _useLastMDE);
		//mapCtrl = _mapCtrl;
	}
	
	protected void SiguienteProceso()
	{
		  Object acflujo = null;
		  Object redd = null;
		  Object cuencasRaster = null;
		  FLyrVect cuencas = null;
		  
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
				redd = AlgUtils.RedDrenaje(mde, acflujo, "");
			   else
				cancel();
			   reportStep();
			   
			   setCurrentStep(0);
			   setNote("Hallando cuencas en el área");
			   
			   if(!isCanceled() && redd!=null)
				cuencasRaster = AlgUtils.Cuencas(mde, redd, "");
			   else
				cancel();
			   reportStep();
			   
			   setCurrentStep(0);
			   setNote("Vectorizando resultado");
			   
			   if(!isCanceled() && cuencasRaster!=null)
				cuencas = (FLyrVect)AlgUtils.Vectorizar(cuencasRaster, "");
			   else
				cancel();
			  
				if(!isCanceled())	
				{
				 cuencas.setName("cuencas");	
				 AlgUtils.addLayertoView(cuencas);	
				 
				 ISymbol symb = cuencas.getLegend().getDefaultSymbol();
				 IFillSymbol fillsymb = (IFillSymbol)symb;
				 Color col = new Color(0, 255, 204, 0);
				 col = GUIUtil.alphaColor(col, 40);
				 fillsymb.setFillColor(col); 		
				 
				 ILineSymbol linesymb = fillsymb.getOutline();
				 col = new Color(0, 0, 0, 255);
				 linesymb.setLineColor(col); 
				 fillsymb.setOutline(linesymb);
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
	 Cuencas cuencas;
	 try {
		cuencas = new Cuencas(this.getLayer(), useLastMDE, mapCtrl);
		CuencasExt.initializeListener(cuencas);
		
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
