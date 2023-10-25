package org.geocuba.foresta.analisis_terreno;

import org.apache.log4j.Logger;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.analisis_terreno.listeners.CuencaPointListener;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.InfoToolExtension;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.tools.Behavior.Behavior;
import com.iver.cit.gvsig.fmap.tools.Behavior.MouseMovementBehavior;
import com.iver.cit.gvsig.fmap.tools.Behavior.PointBehavior;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.toolListeners.StatusBarListener;

public class CuencaPointExt extends Extension{
	
	private static Logger logger = Logger.getLogger(InfoToolExtension.class.getName());

	public void initialize() {
	}

	public void execute(String actionCommand) {
		
		View vista = AlgUtils.GetView(null);
		if(vista==null)
			return;
		MapControl mapCtrl = vista.getMapControl();
		logger.debug("Comand : " + actionCommand);
		
		final CuencaPointListener cuencaListener = new CuencaPointListener(mapCtrl);//crear el listener
		final StatusBarListener sbl = new StatusBarListener(mapCtrl); // para que muestre las coordenadas en la barra de estado 
		mapCtrl.addMapTool("pto_cuenca", new Behavior[]{new PointBehavior(cuencaListener), new MouseMovementBehavior(sbl)});//ponerlo a escuchar
		
		if (actionCommand.compareTo("cuencapoint") == 0) //si se da click sobre el menu con action command "info_rodal" 
		{
			mapCtrl.setTool("pto_cuenca");//seleccionar nuestro listener como el listener activo
		}
		
	}

	public boolean isEnabled() {
		if(!ConnectionExt.Conectado())
			 return false;
				  
			com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
			if(f == null)
			 return false;	
			
			if(!(f instanceof View))
			 return false;
			
			FLayer []layers = ((View)f).getModel().getMapContext().getLayers().getActives();
			 
			 if(layers != null && layers.length == 1)
			 {	
			   if(!layers[0].isVisible() || layers[0].isEditing())
				return false;
			   else
				return true;   

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
