package org.geocuba.foresta.analisis_terreno.interseccion.recortes.raster;

import javax.swing.JOptionPane;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.geocuba.foresta.herramientas.utiles.Global;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.grid.GridException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.exceptions.layers.LoadLayerException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import es.unex.sextante.core.Sextante;
import es.unex.sextante.exceptions.GeoAlgorithmExecutionException;

   public abstract class ClipRaster extends Clip
   {
	private MapRasterLayer raster;
	
	public ClipRaster(FLyrRasterSE l, MapControl mctrl) throws GridException, InterruptedException,LoadLayerException{
		
		layer = l;
		raster = MapRasterLayer.createLayer(layer.getName(), ((FLyrRasterSE)layer).getLoadParams(), layer.getProjection());
		raster.init(((FLyrRasterSE)layer).getDataSource().getColorInterpretation());
		raster.setBufferFactory(((FLyrRasterSE)layer).getBufferFactory());
		mapCtrl = mctrl;
		
		 setInitialStep(0);
		 setDeterminatedProcess(false);
		 setNote(PluginServices.getText(this, "Procesando informaci�n"));
		 setFinalStep(1);
	}
	
	/**M�todo que realiza el proceso de corte de un r�ster con un rect�ngulo
	 * Devuelve el pedazo de r�ster que se cort�*/
	protected FLayer clipProcess(double min_x, double min_y, double max_x, double max_y, FLayer layer)
	{
		FLayer poly = getClippingPoly(min_x, min_y, max_x, max_y);  
		
	    	 Sextante.initialize(); 
	    	 try {
	    		 
	    		 FLayer raster = (FLayer)AlgUtils.ClipRasterWithBBox(layer, (FLyrVect)poly, "");
	    		 //Global.setMDE(raster);
	    		 
	    		 return raster;  
				
			} catch (GeoAlgorithmExecutionException e) {
				e.printStackTrace();
				setCanceled(true);
			}
	    	 
			catch (java.lang.OutOfMemoryError e) {
				JOptionPane.showMessageDialog(null, "Memoria Insuficiente", "ERROR", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
	    return null;		
	}
}