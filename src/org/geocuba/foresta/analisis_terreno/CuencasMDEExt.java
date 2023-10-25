package org.geocuba.foresta.analisis_terreno;

import java.io.File;
import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.geocuba.foresta.analisis_terreno.interseccion.recortes.raster.ClipRaster;
import org.geocuba.foresta.analisis_terreno.listeners.MDEDirecto.CuencasMDE;
import org.geocuba.foresta.analisis_terreno.listeners.MDEDirecto.PendienteMDE;
import org.geocuba.foresta.analisis_terreno.listeners.MDEDirecto.RasterRectangleSelectionCoords;
import org.geocuba.foresta.analisis_terreno.listeners.MDEDirecto.RedDrenajeMDE;
import org.geocuba.foresta.analisis_terreno.listeners.MDEDirecto.RelieveMDE;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.grid.GridException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.exceptions.layers.LoadLayerException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.tools.Behavior.Behavior;
import com.iver.cit.gvsig.fmap.tools.Behavior.MouseMovementBehavior;
import com.iver.cit.gvsig.fmap.tools.Behavior.RectangleBehavior;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.toolListeners.StatusBarListener;

public class CuencasMDEExt extends Extension{
	
	public void initialize()
	{
	    clearTemps();
	}

	public void execute(String actionCommand) 
	{
		if (actionCommand.compareTo("relSomb") == 0)
	    {
		 executeRelieveSombreado();
	    }
	
		//============================================================================//	
		if (actionCommand.compareTo("pend") == 0)
	    {
		 getPendiente();
	    }
		
		//============================================================================//	
		if (actionCommand.compareTo("Redd") == 0)
	    {
		 getRedDrenaje();
	    }
		
		//============================================================================//	
		if (actionCommand.compareTo("cuencas") == 0)
	    {
		 getCuencas();
	    }
    };

    /**Activa un listener de selección por rectángulo 
     * para obtener la representacion del relieve*/
    private void executeRelieveSombreado()
    {
     View vista = VistaManager.GetActiveView();
   	 if(vista==null)
   	  return;
   		
   	  MapControl mapCtrl = vista.getMapControl();
   	
   		FLayer []lyrel = mapCtrl.getMapContext().getLayers().getActives();
   		if(lyrel[0] == null)
   		 return;	
   		
   		RelieveMDE rel;
   		try {
   			rel = new RelieveMDE((FLyrRasterSE)lyrel[0], mapCtrl);
   			 initializeListener(rel, mapCtrl);
   			
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
   
    /**Activa un listener de selección por rectángulo 
     * para obtener la pendiente en un área*/
    private void getPendiente()
    {
     View vista = VistaManager.GetActiveView();
	 if(vista==null)
	  return;
		
	  MapControl mapCtrl = vista.getMapControl();
	
		FLayer []lyrel = mapCtrl.getMapContext().getLayers().getActives();
		if(lyrel[0] == null)
		 return;	
		
		PendienteMDE pend;
		try {
			pend = new PendienteMDE((FLyrRasterSE)lyrel[0], mapCtrl);
			 initializeListener(pend, mapCtrl);
			
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
    
    /**Activa un listener de selección por rectángulo 
     * para obtener la red de drenaje en un área*/
    private void getRedDrenaje()
    {
     View vista = VistaManager.GetActiveView(); 
	 if(vista==null)
	  return;
		
	  MapControl mapCtrl = vista.getMapControl();
	
		FLayer []lyrel = mapCtrl.getMapContext().getLayers().getActives();
		if(lyrel[0] == null)
		 return;	
		
		RedDrenajeMDE red;
		try {
			red = new RedDrenajeMDE((FLyrRasterSE)lyrel[0], mapCtrl);
			 initializeListener(red, mapCtrl);
			
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
    
    /**Activa un listener de selección por rectángulo 
     * para obtener las cuencas en un área*/
    private void getCuencas()
    {
     View vista = VistaManager.GetActiveView();
	 if(vista==null)
	  return;
		
	  MapControl mapCtrl = vista.getMapControl();
	
		FLayer []lyrel = mapCtrl.getMapContext().getLayers().getActives();
		if(lyrel[0] == null)
		 return;	
		
		CuencasMDE cuencas;
		try {
			cuencas = new CuencasMDE((FLyrRasterSE)lyrel[0], mapCtrl);
			 initializeListener(cuencas, mapCtrl);
			
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
    
    /**Inicializa el listener de selección por rectángulo*/
    public static void initializeListener(ClipRaster inter, MapControl mapCtrl)
    {
    	  RasterRectangleSelectionCoords rectListener = new RasterRectangleSelectionCoords(mapCtrl, inter);//crear el listener
		  StatusBarListener sbl = new StatusBarListener(mapCtrl); // para que muestre las coordenadas en la barra de estado 
		  mapCtrl.addMapTool("rectListener", new Behavior[]{new RectangleBehavior(rectListener), new MouseMovementBehavior(sbl)});//ponerlo a escuchar
		
		  mapCtrl.setTool("rectListener");//seleccionar nuestro listener como el listener activo	
    }
    
    private void clearTemps()
    {
    	File f = new File("clip0.tif");
		File f1 = new File("clip0.rmf");
		File f2 = new File("clip0.rmf~");
		int i = 0;
		
		while(f.exists())
		{
		 f.delete();
		 f1.delete();
		 f2.delete();
		 
		 i++;
		 
		 f = new File("clip"+i+".tif");
		 f1 = new File("clip"+i+".rmf");
		 f2 = new File("clip"+i+".rmf~");
		}	
    }
    
    public boolean isEnabled() 
    {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
		if(f == null)
		 return false;	
		
		if(!(f instanceof View))
		 return false;
		
		FLayer []layers = ((View)f).getModel().getMapContext().getLayers().getActives();
		 
		 if(layers != null && layers.length == 1)
		 {	
		   if(!layers[0].isVisible())
			return false;
			 
		  if(layers[0] instanceof FLyrRasterSE) 
		   return true;
		  else
		   return false;	  
		 }
		 else
		  return false;	 
	}
	
	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
		if(f == null)
		 return false;	
		
		if(!(f instanceof View))
		 return false;
		
		FLayer []layers = ((View)f).getModel().getMapContext().getLayers().getActives();
		 
		 if(layers != null && layers.length == 1)
		 {	
		   if(!layers[0].isVisible())
			return false;
			 
		  if(layers[0] instanceof FLyrRasterSE) 
		   return true;
		  else
		   return false;	  
		 }
		 else
		  return false;	 	
	}
}


