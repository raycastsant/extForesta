package org.geocuba.foresta.reportes;

import javax.swing.JOptionPane;

import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import org.geocuba.foresta.herramientas.utiles.PrintReport;
import org.geocuba.foresta.herramientas.writerTasks.BackgroundExecution;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.project.documents.view.gui.View;
 
public class Suelos_EspForestalExt extends Extension{
	private View vista = null;
	public void initialize() {
	}

	public void execute(String actionCommand) {
		
	if (actionCommand.compareTo("suelos_esp") == 0)
    {
		vista = AlgUtils.GetView(null);
		FLayer layer = vista.getMapControl().getMapContext().getLayers().getLayer("Suelos");
		
		if(layer == null)
		{ 
		 JOptionPane.showMessageDialog(null, "No se obtuvo la capa Suelos");
		 return;
		}	
		
		//Obtengo los gid de los elementos seleccionados
		String ids = Funciones_Utiles.GetSelectedGids(layer);
		
       if(!ids.equals(""))
       {	   
        String query = "select tipo_suelo.tipo as suelo, _suelos.erosion, textura_suelos.textura, _suelos.materiaorganica, _suelos.profundidadefectiva, _suelos.ph, _suelos.pendiente," +
        		"_suelos.gravas, _suelos.piedras, _suelos.rocas, _suelos.estructura, especies.ncomun as especie, especies.siglas, especies.ncientifico " +
        		"from (((tipo_suelo inner join tipo_suelo_especies on tipo_suelo.id=tipo_suelo_especies.tipo_suelo) inner join especies on " +
        		"tipo_suelo_especies.especie=especies.siglas)inner join _suelos on tipo_suelo.id=_suelos.tipo)inner join textura_suelos on _suelos.textura=textura_suelos.id " +
        		"where _suelos.gid in("+ids+") Group by tipo_suelo.tipo, _suelos.erosion, _suelos.materiaorganica, _suelos.profundidadefectiva, _suelos.ph, _suelos.pendiente," +
        		" _suelos.gravas, _suelos.piedras, _suelos.rocas, _suelos.estructura, textura_suelos.textura, especies.ncomun, especies.siglas, especies.ncientifico " +
        		"order by tipo_suelo.tipo, especies.ncomun";
	   
       // PrintReport rep = new PrintReport("Suelos_espForestales", query, null);
	   // rep.start();
        BackgroundExecution.cancelableBackgroundExecution(new PrintReport("Suelos_espForestales", query, null));
       }
       else
    	  JOptionPane.showMessageDialog(null, "No se obtuvieron elementos"); 
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
		   if(!layers[0].isVisible() || layers[0].isEditing())
			return false;
			 
		   if(layers[0].getName().equals("Suelos"))
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

