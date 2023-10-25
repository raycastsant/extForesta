package org.geocuba.foresta.administracion_seguridad.extensiones;

import org.geocuba.foresta.administracion_seguridad.VistaManager;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.ProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.gui.ViewProperties;

public class ConfigurarVistaExt extends Extension{
	
	public void initialize(){
	}

	public void execute(String actionCommand) 
	{
	 if (actionCommand.compareTo("configurar_vista") == 0)
	 {
		 View vista = VistaManager.Vista();
	     IProjectView model = vista.getModel();

	        	ProjectView viewModel = (ProjectView)model;
	        	if (viewModel != null) 
	        	{
	              ViewProperties viewProperties = new ViewProperties(viewModel);
	              PluginServices.getMDIManager().addWindow(viewProperties);
	              if (viewProperties.isAcceppted()) 
	               viewModel.setModified(true);
	        	}
	 }
	 
	}
	
   public boolean isEnabled() 
   {
	return ConnectionExt.Conectado();
   }

	public boolean isVisible() 
	{
     if(!ConnectionExt.Conectado())
      return false;
     
     return VistaManager.GetActiveView()==null;
	}
}
