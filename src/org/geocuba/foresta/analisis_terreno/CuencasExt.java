package org.geocuba.foresta.analisis_terreno;

import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.analisis_terreno.interseccion._Intersect;
import org.geocuba.foresta.analisis_terreno.listeners.Cuencas;
import org.geocuba.foresta.analisis_terreno.listeners.MDE;
import org.geocuba.foresta.analisis_terreno.listeners.Pendiente;
import org.geocuba.foresta.analisis_terreno.listeners.RectangleSelectionCoordsListener;
import org.geocuba.foresta.analisis_terreno.listeners.RedDrenaje;
import org.geocuba.foresta.analisis_terreno.listeners.RelieveSombreado;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.geocuba.foresta.herramientas.utiles.Global;
import org.gvsig.raster.grid.GridException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.LayoutInsertToolsExtension;
import com.iver.cit.gvsig.exceptions.layers.LoadLayerException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.tools.Behavior.Behavior;
import com.iver.cit.gvsig.fmap.tools.Behavior.MouseMovementBehavior;
import com.iver.cit.gvsig.fmap.tools.Behavior.RectangleBehavior;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.toolListeners.StatusBarListener;

public class CuencasExt extends Extension{
 	
	private View vista = null;
	private static MapControl mapCtrl = null;
	private static Logger logger = Logger.getLogger(LayoutInsertToolsExtension.class.getName());
	
	public void initialize() {
	}

	public void execute(String actionCommand) {
		//=======//=======//=======//=======//=======//=======//=======//=======//	
			if (actionCommand.compareTo("Redd") == 0)  //Usando un nuevo mde
		    {
		     executeRedDrenaje(false, actionCommand);
		    }
		//=======//=======//=======//=======//=======//=======//=======//=======//	
			if (actionCommand.compareTo("Redd_mdeant") == 0)
		    {
		     executeRedDrenaje(true, actionCommand);  //Usando un mde anteriormente generado
		    }
		//=======//=======//=======//=======//=======//=======//=======//=======//
			if (actionCommand.compareTo("relSomb") == 0)
		    {
			 executeRelieveSombreado(false, actionCommand);  //Usando un nuevo mde
		    }
		//=======//=======//=======//=======//=======//=======//=======//=======//
			if (actionCommand.compareTo("relSomb_mdeant") == 0)
		    {
			 executeRelieveSombreado(true, actionCommand); //Usando un mde anteriormente generado
		    }
		//=======//=======//=======//=======//=======//=======//=======//=======//	
			if (actionCommand.compareTo("mde") == 0)
		    {
				vista = AlgUtils.GetView(null);
				if(vista==null)
					return;
				
				mapCtrl = vista.getMapControl();
				logger.debug("Comand : " + actionCommand);  
			
				FLyrVect lyrel = (FLyrVect)mapCtrl.getMapContext().getLayers().getLayer("Relieve");
				if(lyrel == null)
				 return;
				
				MDE mde;
				try {
					mde = new MDE(lyrel, mapCtrl);
					initializeListener(mde);
					
				} catch (ReadDriverException e) {
					e.printStackTrace();
				} catch (LoadLayerException e) {
					e.printStackTrace();
				} catch (GridException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    }
		//=======//=======//=======//=======//=======//=======//=======//=======//	
			if (actionCommand.compareTo("cuencas") == 0)
		    {
			 executeCuencas(false, actionCommand);
		    }
		//=======//=======//=======//=======//=======//=======//=======//=======//	
			if (actionCommand.compareTo("cuencas_mdeant") == 0)
		    {
			 executeCuencas(true, actionCommand);
		    }
	    //=======//=======//=======//=======//=======//=======//=======//=======//	
			if (actionCommand.compareTo("pend") == 0)
		    {
			 executePendiente(false, actionCommand);
		    }
	    //=======//=======//=======//=======//=======//=======//=======//=======//	
			if (actionCommand.compareTo("pend_mdeant") == 0)
		    {
			 executePendiente(true, actionCommand);
		    }
		  };
		    
		    public static void initializeListener(_Intersect inter)
		    {
		    	//FLayer lyrel = mapCtrl.getMapContext().getLayers().getLayer("Relieve");
			 	   //if (mapCtrl.getNamesMapTools().get("rectListener") == null) 
				 //{	
				  RectangleSelectionCoordsListener rectListener = new RectangleSelectionCoordsListener(mapCtrl, inter);//crear el listener
				  StatusBarListener sbl = new StatusBarListener(mapCtrl); // para que muestre las coordenadas en la barra de estado 
				  mapCtrl.addMapTool("rectListener", new Behavior[]{new RectangleBehavior(rectListener), new MouseMovementBehavior(sbl)});//ponerlo a escuchar
				// }	
				
				mapCtrl.setTool("rectListener");//seleccionar nuestro listener como el listener activo	
		    }
		    
		    private void executeRedDrenaje(boolean usingAntMDE, String actionCommand)
		    {
		    	vista = AlgUtils.GetView(null);
				if(vista==null)
					return;
				
				mapCtrl = vista.getMapControl();
				logger.debug("Comand : " + actionCommand);  
			
				FLyrVect lyrel = (FLyrVect)mapCtrl.getMapContext().getLayers().getLayer("Relieve");
				if(lyrel == null)
				 return;	
				
				RedDrenaje reddren;
				try {
					reddren = new RedDrenaje(lyrel, usingAntMDE, mapCtrl);
					if(!usingAntMDE)
					 initializeListener(reddren);
					else
					{
					 if(Global.getMDE() == null)
					  JOptionPane.showMessageDialog(null, "No se ha generado ningún MDE anteriormente", "Error", JOptionPane.ERROR_MESSAGE);
					 else
					  PluginServices.cancelableBackgroundExecution(reddren);
					} 
					
				} catch (ReadDriverException e) {
					e.printStackTrace();
				} catch (LoadLayerException e) {
					e.printStackTrace();
				} catch (GridException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    }
		    
		    private void executeRelieveSombreado(boolean usingAntMDE, String actionCommand)
		    {
		    	vista = AlgUtils.GetView(null);
				if(vista==null)
					return;
				
				mapCtrl = vista.getMapControl();
				logger.debug("Comand : " + actionCommand);  
			
				FLyrVect lyrel = (FLyrVect)mapCtrl.getMapContext().getLayers().getLayer("Relieve");
				if(lyrel == null)
				 return;	
				
				RelieveSombreado relS;
				try {
					relS = new RelieveSombreado(lyrel, usingAntMDE, mapCtrl);
					if(!usingAntMDE)
					 initializeListener(relS);
					else
					{
					 if(Global.getMDE() == null)
					  JOptionPane.showMessageDialog(null, "No se ha generado ningún MDE anteriormente", "Error", JOptionPane.ERROR_MESSAGE);
					 else
					  PluginServices.cancelableBackgroundExecution(relS);
					} 
					
				} catch (ReadDriverException e) {
					e.printStackTrace();
				} catch (LoadLayerException e) {
					e.printStackTrace();
				} catch (GridException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    }
		    
		    private void executeCuencas(boolean usingAntMDE, String actionCommand)
		    {
		    	vista = AlgUtils.GetView(null);
				if(vista==null)
					return;
				
				mapCtrl = vista.getMapControl();
				logger.debug("Comand : " + actionCommand);  
			
				FLyrVect lyrel = (FLyrVect)mapCtrl.getMapContext().getLayers().getLayer("Relieve");
				if(lyrel == null)
				 return;	
				
				Cuencas cuencas;
				try {
					cuencas = new Cuencas(lyrel, usingAntMDE, mapCtrl);
					if(!usingAntMDE)
					 initializeListener(cuencas);
					else
					{
					 if(Global.getMDE() == null)
					  JOptionPane.showMessageDialog(null, "No se ha generado ningún MDE anteriormente", "Error", JOptionPane.ERROR_MESSAGE);
					 else
					  PluginServices.cancelableBackgroundExecution(cuencas);
					} 
					
				} catch (ReadDriverException e) {
					e.printStackTrace();
				} catch (LoadLayerException e) {
					e.printStackTrace();
				} catch (GridException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    }
		    
		    private void executePendiente(boolean usingAntMDE, String actionCommand)
		    {
		    	vista = AlgUtils.GetView(null);
				if(vista==null)
					return;
				
				mapCtrl = vista.getMapControl();
				logger.debug("Comand : " + actionCommand);  
			
				FLyrVect lyrel = (FLyrVect)mapCtrl.getMapContext().getLayers().getLayer("Relieve");
				if(lyrel == null)
				 return;	
				
				Pendiente pendiente;
				try {
					pendiente = new Pendiente(lyrel, usingAntMDE, mapCtrl);
					if(!usingAntMDE)
					 initializeListener(pendiente);
					else
					{
					 if(Global.getMDE() == null)
					  JOptionPane.showMessageDialog(null, "No se ha generado ningún MDE anteriormente", "Error", JOptionPane.ERROR_MESSAGE);
					 else
					  PluginServices.cancelableBackgroundExecution(pendiente);
					} 
					
				} catch (ReadDriverException e) {
					e.printStackTrace();
				} catch (LoadLayerException e) {
					e.printStackTrace();
				} catch (GridException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    }
		    
	public boolean isEnabled() 
	{
//		if(!ConnectionExt.Conectado())
//			 return false;
//				  
//			com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
//			if(f == null)
//			 return false;	
//			
//			if(!(f instanceof View))
//			 return false;
//			
//			FLayer []layers = ((View)f).getModel().getMapContext().getLayers().getActives();
//			 
//			 if(layers != null && layers.length == 1)
//			 {	
//			   if(!layers[0].isVisible() || layers[0].isEditing())
//				return false;
//				 
//			   if(layers[0].getName().equals("Relieve"))
//			    return true;
//			   else
//				return false;
//			 }
//			 else
//			  return false;
		
		return true;
	}

	public boolean isVisible() 
	{
//		if(!ConnectionExt.Conectado())
//			 return false;
//				  
//			com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
//			if(f == null)
//			 return false;	
//			
//			if(!(f instanceof View))
//			 return false;
//			
//	  return true;
		
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
				 
			   if(layers[0].getName().equals("Relieve"))
			    return true;
			   else
				return false;
			 }
			 else
			  return false;
	}
	
}

