package org.geocuba.foresta.gestion_datos.extensiones;

import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.geocuba.foresta.gestion_datos.listeners.ModificarGeometriaListener;
import org.geocuba.foresta.herramientas.utiles.Global;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.EditionUtilities;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * Extensión que activa la herramienta de modificar (Seleccionar)
 */
public class ModificarGeometriaExtension extends Extension 
{
	private MapControl mapControl;
	private static ModificarGeometriaListener modificarTool;
//	private static int gidGeom;
//	private static IPanelManager panelmanager;
//	private static FLyrVect capa;

	public void initialize() 
	{
//		modificarTool = new ModificarGeometriaListener(gidGeom, panelmanager, capa);
//		CADExtension.addCADTool("_selection", selection);
	}

	public void execute(String s) 
	{
		mapControl = VistaManager.GetActiveView().getMapControl();
		CADExtension.initFocus();
		if (s.equals("modificarGEom"))
        	CADExtension.setCADTool("modificarGEom", true);
		
		CADExtension.getEditionManager().setMapControl(mapControl);
		CADExtension.getCADToolAdapter().configureMenu();
	}

	/**Valor impuesto en el constructor de ModificarGeometriaListener*/
	public static void setModificarCADTool(ModificarGeometriaListener value)
	{
	 modificarTool = value;	
	}	
	
	public boolean isEnabled()
{
		if (CADExtension.getEditionManager().getActiveLayerEdited()==null)
			return false;
		FLyrVect lv=(FLyrVect)CADExtension.getEditionManager().getActiveLayerEdited().getLayer();
		try {
		    if(modificarTool == null)
		     return false;
		    
			return modificarTool.isApplicable(lv.getShapeType());
			
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
						
						if (lyrVect.isEditing() && lyrVect.isActive())
						{
						 if(Global.isForestaLayer(lyrVect.getName()))
						  return true;
						}
				    }    
					
		         }	
		  }	
		
		return false;
	}
}
