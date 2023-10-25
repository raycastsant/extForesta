package org.geocuba.foresta.gestion_datos.extensiones;

import org.geocuba.foresta.gestion_datos.listeners.EditarVerticeCADTool;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.EditionUtilities;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.gui.cad.tools.PolygonCADTool;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * Extensión que gestiona los vertices en edición
 */
public class EditarVerticeExtension extends Extension
{
	protected View view;

	protected MapControl mapControl;
	//protected PolygonCADTool polygon;

	public void initialize() 
	{
//		polygon = new PolygonCADTool();
        EditarVerticeCADTool editarvertex = new EditarVerticeCADTool();
	    CADExtension.addCADTool("_editarvertice", editarvertex);
	}

	public void execute(String s) 
	{
		CADExtension.initFocus();
		if (s.equals("_editarvertice")) 
        	CADExtension.setCADTool(s,true);
        
		CADExtension.getEditionManager().setMapControl(mapControl);
		CADExtension.getCADToolAdapter().configureMenu();
	}

	public boolean isEnabled() 
	{
		try {
			if (EditionUtilities.getEditionStatus() == EditionUtilities.EDITION_STATUS_ONE_VECTORIAL_LAYER_ACTIVE_AND_EDITABLE) {
				view = (View) PluginServices.getMDIManager().getActiveWindow();
				mapControl = view.getMapControl();
				if (CADExtension.getEditionManager().getActiveLayerEdited()==null)
					return false;
				FLyrVect lv=(FLyrVect)CADExtension.getEditionManager().getActiveLayerEdited().getLayer();
				
				switch (lv.getShapeType()) 
				{
					case FShape.POINT:
					case FShape.MULTIPOINT:
						return false;
				}
				return true;
			}
		} catch (ReadDriverException e) {
			NotificationManager.addError(e.getMessage(),e);
		}
		return false;
	}

	public boolean isVisible() 
	{
		if (EditionUtilities.getEditionStatus() == EditionUtilities.EDITION_STATUS_ONE_VECTORIAL_LAYER_ACTIVE_AND_EDITABLE)
		{	
			com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
			.getActiveWindow();

			View vista = (View) f;
			IProjectView model = vista.getModel();
			MapContext mapa = model.getMapContext();
			FLayers layers = mapa.getLayers();
			
				for (int i =0;i<layers.getLayersCount();i++)
				{
					if (layers.getLayer(i) instanceof FLyrVect)
					{
						FLyrVect lyrVect = (FLyrVect)layers.getLayer(i);
						
//						if (lyrVect.isEditing() && lyrVect.isActive())
//						{
//						 if(GvSigForestaGlobal.isForestaLayer(lyrVect.getName()))
//						  return true;
//						}
				    }    
					
		         }	
		  }
		return false;
	}
}
