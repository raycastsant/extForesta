package org.geocuba.foresta.gestion_datos.listeners;

import org.geocuba.foresta.gestion_datos.PersistentGeometricObject;
import org.geocuba.foresta.gestion_datos.gui.pGestionManager;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.StartEditing;
import com.iver.cit.gvsig.StopEditing;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.core.FGeometryCollection;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.GeneralPathX;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.ShapeFactory;
import com.iver.cit.gvsig.fmap.core.v02.FConverter;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.gui.cad.tools.PolylineCADTool;
import com.iver.cit.gvsig.gui.cad.tools.smc.PolylineCADToolContext;

public class DibujarPolilineaListener extends PolylineCADTool
{
	private FLyrVect capa;
	private StopEditing stopEd;
	private MapControl mapCtrol;
	private PersistentGeometricObject persistent;
	
	public DibujarPolilineaListener(MapControl mc, FLyrVect _capa, PersistentGeometricObject _persistent)
	{
        mapCtrol = mc;
 		capa = _capa;
 		
 		if(capa == null)
 	     return;		
 		
 		persistent = _persistent;
 		
 		AlgUtils.DesactivateLayers();
 		capa.setActive(true);
 		
 		StartEditing se = new StartEditing();
		se.execute("");
		
		stopEd = new StopEditing();
		stopEd.initialize();
	}
	
	public void endGeometry() 
	{
    	if (gpx==null) {
    		gpx=new GeneralPathX();
    		IGeometry[] geoms = (IGeometry[]) list.toArray(new IGeometry[0]);
    		FGeometryCollection fgc = new FGeometryCollection(geoms);
    		gpx.append(fgc.getPathIterator(null,FConverter.FLATNESS), true);
    	}
    	
           try {
 			if (getVLE().getVEA().getShapeType()==FShape.POLYGON && !close){
 				closeGeometry();
 			}
 			
 		    } catch (ReadDriverException e) {
 			      NotificationManager.addError(e.getMessage(),e);
		    }

        IGeometry newGeom = null;
        int type=getCadToolAdapter().getActiveLayerType();
        
        if (type==FShape.POLYGON)
         newGeom = ShapeFactory.createPolygon2D(gpx);
        else
         newGeom = ShapeFactory.createPolyline2D(gpx);
        
        addGeometry(newGeom);
        _fsm = new PolylineCADToolContext(this);
        list.clear();
        clearTemporalCache();
        close=false;
        gpx=null;
        antantPoint=antCenter=antInter=antPoint=firstPoint=null;
        
        try {
//			stopEd.cancelEdition(capa);
//			capa.setEditing(false);
//			mapCtrol.setTool("zoomIn");
		    
        	persistent.setGeometry(newGeom, type);
        	//persistent.setType(type);
			pGestionManager.mostrarFicha_Gestion(persistent);
			mapCtrol.setTool("zoomIn");
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
	
	public String getName() {
        return "Foresta_Polyline";
    }
	
	/**Cancela la edición de la capa. La idea es que se capte la geometría de lo que
	 * se dibujó y luego se inserte a través del PersistenGeometricObject*/
//	public void terminarEdición() throws CancelEditingTableException, CancelEditingLayerException, StartEditionLayerException
//	{
//		stopEd.cancelEdition(capa);
//		capa.setEditing(false);
//		mapCtrol.setTool("zoomIn");
//	}
}
