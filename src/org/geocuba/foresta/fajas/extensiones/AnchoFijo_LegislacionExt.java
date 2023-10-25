package org.geocuba.foresta.fajas.extensiones;

import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.fajas.gui.pAnchoFijo_Legislacion_areal;
import org.geocuba.foresta.fajas.gui.pAnchoFijo_Legislacion_lineal;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.edition.UtilFunctions;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
*
* @author Raisel
*/
public class AnchoFijo_LegislacionExt extends Extension{
	
	//private static View vista = null;
	//private FLayer [] layers = null;
	
	public static final String HIDRO_LINEAL_LAYER_NAME = "Hidrografía_lineal";
	public static final String HIDRO_AREAL_LAYER_NAME = "Hidrografía_areal";
	//public static final String RED_DRENAJE_LAYER_NAME = "Red_drenaje";
	public static FLyrVect selectedLayer;
	
	public void initialize() {
	
	}

	public void execute(String actionCommand) {
		
		if (actionCommand.compareTo("AnchoFijo") == 0)
	    {
		 View vista = VistaManager.GetActiveView();
		 FLayer [] layers = vista.getModel().getMapContext().getLayers().getActives();
		 
	    if(layers[0].getName().equals(HIDRO_LINEAL_LAYER_NAME))
	    {	
		 pAnchoFijo_Legislacion_lineal pa = new pAnchoFijo_Legislacion_lineal();
		 PluginServices.getMDIManager().addCentredWindow(pa);
		 pa.Show();
	    }
	    else
	    if(layers[0].getName().equals(HIDRO_AREAL_LAYER_NAME))
	    {	
	     pAnchoFijo_Legislacion_areal pa = new pAnchoFijo_Legislacion_areal();
	     PluginServices.getMDIManager().addCentredWindow(pa);
	     pa.Show();
	    }	
	   }
      };

	public boolean isEnabled() {
		
		if(!ConnectionExt.Conectado())
		 return false;
			  
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
		if(f == null)
		 return false;	
		
		if(!(f instanceof View))
		 return false;
		
		FLayer [] layers = ((View)f).getModel().getMapContext().getLayers().getActives();
		 
		 if(layers != null && layers.length == 1)
		 {	
		   if(!(layers[0] instanceof FLyrVect))
			return false;
		   
		   selectedLayer = (FLyrVect)layers[0];
		   
		   if(!selectedLayer.isVisible() || selectedLayer.isEditing())
			return false;
		   
		   if(Funciones_Utiles.GetSelectionCount(selectedLayer) < 1)
			 return false;  
			 
		   if(selectedLayer.getName().equals(HIDRO_LINEAL_LAYER_NAME) || selectedLayer.getName().equals(HIDRO_AREAL_LAYER_NAME) /*|| selectedLayer.getName().equals(RED_DRENAJE_LAYER_NAME)*/)
		    return true;
		   else
			return false;
		 }
		 else
		  return false;	 
		}

	public boolean isVisible() {
		if(!ConnectionExt.Conectado())
			 return false;
				  
			com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
			if(f == null)
			 return false;	
			
			if(!(f instanceof View))
			 return false;
			
	  return true;		
	}
}

