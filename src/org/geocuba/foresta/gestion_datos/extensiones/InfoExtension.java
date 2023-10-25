package org.geocuba.foresta.gestion_datos.extensiones;

import org.apache.log4j.Logger;
import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.geocuba.foresta.gestion_datos.listeners.InformacionListener;
import org.geocuba.foresta.herramientas.utiles.Global;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.tools.Behavior.Behavior;
import com.iver.cit.gvsig.fmap.tools.Behavior.MouseMovementBehavior;
import com.iver.cit.gvsig.fmap.tools.Behavior.PointBehavior;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.toolListeners.StatusBarListener;

public class InfoExtension extends Extension 
{
	private static Logger logger = Logger.getLogger(InfoExtension.class.getName());
	private static String classname;

	public void initialize() {
	}
	
	public void execute(String s) 
	{
		BaseView vista = (BaseView) VistaManager.GetActiveView();
		MapControl mapCtrl = vista.getMapControl();
		logger.debug("Comand : " + s);
		
		if (s.compareTo("INFO") == 0) 
		{
			InformacionListener il = new InformacionListener(mapCtrl);
			final StatusBarListener sbl = new StatusBarListener(mapCtrl);
			mapCtrl.addMapTool("forestainfo", new Behavior[]{new PointBehavior(il), new MouseMovementBehavior(sbl)});

		    mapCtrl.setTool("forestainfo");
		   ((ProjectDocument)vista.getModel()).setModified(true);
		}
	}

    public static String getClasName()
    {
     return classname;	
    }
    
    private void setClassName(String layerName)
    {
    	if(layerName.equals("Hidrografía_lineal"))
    	 classname = "Rio";
    	else
    	if(layerName.equals("Hidrografía_areal"))
    	 classname = "Embalse";
    	else
        if(layerName.equals("Fajas"))
         classname = "Faja";
        else
        if(layerName.equals("Fajas_Real"))
         classname = "Faja_real";
        else
        if(layerName.equals("Parteaguas"))
         classname = "Parteaguas";
        else
        if(layerName.equals("Relieve"))
         classname = "Relieve";
        else
        if(layerName.equals("Suelos"))
         classname = "Suelo";
        else
        if(layerName.equals("Parcelas"))
         classname = "Parcela";
        else
        if(layerName.equals("Cuencas_Interés_Nacional"))
         classname = "Cuenca";
        else
        if(layerName.equals("Municipios"))
         classname = "Municipio";		
    }
    
	public boolean isEnabled() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
															 .getActiveWindow();

		if (f == null) {
			return false;
		}

		if (f instanceof BaseView) {
			BaseView vista = (BaseView) f;
			IProjectView model = vista.getModel();
			MapContext mapa = model.getMapContext();

			FLayer[] layers =mapa.getLayers().getActives();
			if(layers.length == 1)
			{
			  if(layers[0] instanceof FLyrVect)
			  {	  
				for (int i=0;i<layers.length;i++) 
				{
				 if(Global.isForestaLayer(layers[i].getName()))
				 {	 
                  setClassName(layers[i].getName());	 
				  return true;
				 } 
				}
			  }	
			}	
		}
		return false;
	}

	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}

		if (f instanceof BaseView) {
			BaseView vista = (BaseView) f;
			IProjectView model = vista.getModel();
			MapContext mapa = model.getMapContext();
			return mapa.getLayers().getLayersCount() > 0;
		} else {
			return false;
		}
	}
}

