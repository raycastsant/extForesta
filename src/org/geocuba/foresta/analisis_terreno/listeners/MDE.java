package org.geocuba.foresta.analisis_terreno.listeners;

import org.geocuba.foresta.analisis_terreno.CuencasExt;
import org.geocuba.foresta.analisis_terreno.interseccion._Intersect;
import org.gvsig.raster.grid.GridException;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.exceptions.layers.LoadLayerException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

/**Clase para mostrar el mde correspondiente 
 * a una interseccion de relieve*/
public class MDE extends _Intersect{
	
	public MDE(FLyrVect relieve, MapControl _mapCtrl)throws ReadDriverException, LoadLayerException, GridException, InterruptedException {
		super(relieve, _mapCtrl, false);
		//mapCtrl = _mapCtrl;
	}
	
	public void finished() 
	{
	 MDE mdt;
	try {
		mdt = new MDE(this.getLayer(), mapCtrl);
		CuencasExt.initializeListener(mdt);
		
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

	@Override
	protected void SiguienteProceso() 
	{
		
	}
}
