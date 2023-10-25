package org.geocuba.foresta.analisis_terreno.listeners.MDEDirecto;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.geocuba.foresta.analisis_terreno.interseccion.recortes.raster.ClipRaster;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.geocuba.foresta.herramientas.utiles.Global;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.grid.GridException;
import com.hardcode.driverManager.DriverLoadException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.exceptions.layers.LoadLayerException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.core.symbols.IFillSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.ILineSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;
import com.iver.cit.gvsig.fmap.drivers.VectorialFileDriver;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;
import com.iver.cit.gvsig.gui.GUIUtil;
import es.unex.sextante.exceptions.GeoAlgorithmExecutionException;
import es.unex.sextante.gvsig.core.gvRasterLayer;

/**Clase para obtener las cuencas en un área*/
public class CuencasMDE extends ClipRaster
{
	
	public CuencasMDE(FLyrRasterSE raster,  MapControl _mapCtrl)throws ReadDriverException, LoadLayerException, GridException, InterruptedException {
		super(raster, _mapCtrl);
	}
	
	protected void NextProcess()
	{ 
		Object acflujo = null;
		Object sdep = null;
		Object reddrenaje = null;
		Object cuencasRaster = null;
		FLyrVect cuencas = null;
		  
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
		   } 
		   
		   setCurrentStep(0);
		   setNote("Hallando red de drenaje");
		   
		   if(!isCanceled() && acflujo!=null)
			reddrenaje = AlgUtils.RedDrenaje(sdep, acflujo, "");
		   else
		   {	   
				setCanceled(true);   
			    reportStep();
			    return;
	       }  
		   
		   setCurrentStep(0);
		   setNote("Hallando cuencas en la zona");
		  
		   if(!isCanceled() && reddrenaje!=null)
			cuencasRaster = AlgUtils.Cuencas(sdep, reddrenaje, "");
		   else
		   {	   
				setCanceled(true);   
			    reportStep();
			    return;
	       }  
		   
		   setCurrentStep(0);
		   setNote("Vectorizando resultado");
		   
		   if(!isCanceled() && cuencasRaster!=null)
		   {	   
			   AlgUtils.Vectorizar(cuencasRaster, "vectorizado.shp");
			   cuencas = (FLyrVect)LayerFactory.createLayer("cuencas", (VectorialFileDriver)LayerFactory.getDM().getDriver("gvSIG shp driver"), new File("vectorizado.shp"), VistaManager.GetActiveView().getProjection());
			   cuencas = (FLyrVect)AlgUtils.RemoveItem(cuencas, "Cuencas", "-1.0", "");
			 
			 cuencas.setName("cuencas");
		   }	
		   else
		   {	   
				setCanceled(true);   
			    reportStep();
			    return;
	       }  
		  
			if(!isCanceled())	
			{
				 /*AlgUtils.addLayertoView("resultVect.shp", "Cuencas_Area");
				 FLyrVect l = (FLyrVect)mapCtrl.getMapContext().getLayers().getLayer("Cuencas_Area");*/
				
				 ISymbol symb = cuencas.getLegend().getDefaultSymbol();
				 IFillSymbol fillsymb = (IFillSymbol)symb;
				 Color col = new Color(0, 255, 204, 0);
				 col = GUIUtil.alphaColor(col, 40);
				 fillsymb.setFillColor(col); 		
				 
				 ILineSymbol linesymb = fillsymb.getOutline();
				 col = new Color(0, 0, 0, 255);
				 linesymb.setLineColor(col); 
				 fillsymb.setOutline(linesymb);
				 
				 AlgUtils.addLayertoView(cuencas);
			} 
			
		  reportStep();
		  
		} catch (GeoAlgorithmExecutionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DriverLoadException e) {
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
