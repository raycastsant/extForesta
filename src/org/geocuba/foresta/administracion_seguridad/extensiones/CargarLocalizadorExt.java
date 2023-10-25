package org.geocuba.foresta.administracion_seguridad.extensiones;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.project.documents.view.gui.View;

public class CargarLocalizadorExt extends Extension 
{
    public void initialize() {
    }

    public void execute(String s) 
    {
        View vista = (View) PluginServices.getMDIManager().getActiveWindow();

        if (s.compareTo("localizador") == 0) 
        {
          FLayer municipios = vista.getMapControl().getMapContext().getLayers().getLayer("Municipios");
          if(municipios != null)
           vista.getMapOverview().getMapContext().getLayers().addLayer(municipios);
        }
    }

     public boolean isEnabled() {
        return true;
    }

    public boolean isVisible() 
    {
        com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
                                                             .getActiveWindow();
        if (f == null) {
            return false;
        }
        return (f instanceof View);
    }
}
