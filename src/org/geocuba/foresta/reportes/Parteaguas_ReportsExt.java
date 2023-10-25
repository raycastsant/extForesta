package org.geocuba.foresta.reportes;

import javax.swing.JOptionPane;
import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.fajas.SubtramosManager;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import org.geocuba.foresta.herramientas.utiles.PrintReport;
import org.geocuba.foresta.herramientas.writerTasks.BackgroundExecution;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.project.documents.view.gui.View;
 
public class Parteaguas_ReportsExt extends Extension{
	
	private View vista = null;
	
	public void initialize() {
	}

	public void execute(String actionCommand)
	{
		if (actionCommand.compareTo("perfiles") == 0)
	    {
	     mostrarReportePerfiles();
	    }
	};
	
/** ====== Muestra la informacion del catastro para las fajas seleccionadas ======= */	
	private void mostrarReportePerfiles()
	{
		vista = VistaManager.GetActiveView();
		FLayer []layers = vista.getMapControl().getMapContext().getLayers().getActives();
		
		if(layers[0] == null)
		{ 
		 JOptionPane.showMessageDialog(null, "No se obtuvo la capa de Parteaguas");
		 return;
		}	
		
		//Obtengo los gid de los elementos seleccionados
		String gids = Funciones_Utiles.GetSelectedGids(layers[0]);
		
		if(gids.equals("") || gids==null)
		{ 
		 JOptionPane.showMessageDialog(null, "Debe seleccionar al menos un Parteaguas");
		 return;
		}	
		
        String query = SubtramosManager.getQueryTablaSubtramos(Integer.parseInt(gids));
	   
        System.out.println(query);
        
        BackgroundExecution.cancelableBackgroundExecution(new PrintReport("Perfiles", query, null));
	}
	
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
		   if(!layers[0].isVisible() || layers[0].isEditing())
			return false;
			 
		   if(layers[0].getName().equals("Parteaguas") && Funciones_Utiles.GetSelectionCount(layers[0])==1) //|| layers[0].getName().equals("Fajas_Real"))
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

