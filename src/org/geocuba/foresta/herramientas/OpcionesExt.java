package org.geocuba.foresta.herramientas;

import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.geocuba.foresta.herramientas.utiles.Global;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.geocuba.foresta.administracion_seguridad.db.CargarCapasWT;
import org.geocuba.foresta.administracion_seguridad.db.DBUtils;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import es.unex.sextante.core.Sextante;
import es.unex.sextante.exceptions.GeoAlgorithmExecutionException;

public class OpcionesExt extends Extension{
	
	public void initialize() {
	}

	public void execute(String actionCommand) {
		
//	if (actionCommand.compareTo("ActualizarTem") == 0)
//    {
//	  UpdateCapas w = new UpdateCapas();
//	  w.Show();
//    }
	
	if (actionCommand.compareTo("invertlines") == 0)
    {
		View vista = VistaManager.GetActiveView();
		MapControl map = AlgUtils.GetMapControl(vista);
		FLayer layer = vista.getMapControl().getMapContext().getLayers().getLayer("Hidrografía_lineal");
		
	   if(layer == null)
		{ 
		 JOptionPane.showMessageDialog(null, "No se obtuvo la capa de Hidrografía lineal");
		 return;
		}	
		
		//Obtengo los gid de los elementos seleccionados
		String gids = Funciones_Utiles.GetSelectedGids(layer);
		
       if(gids.equals(""))
       {
    	JOptionPane.showMessageDialog(null, "Debe seleccionar uno o más ríos");
    	return;
       }	   
      
       Sextante.initialize();
       try {
    	     FLayer lines = (FLayer)AlgUtils.changeLinesDirection(layer, null);
		    //FLayer lines = LayerFactory.createLayer("lines", (VectorialFileDriver)LayerFactory.getDM().getDriver("gvSIG shp driver"), new File("invLines.shp"),  CRSFactory.getCRS("EPSG:2085"));
	
		  //Exportando capas a postgis
		   /* Export ex = new Export(false);
			ex.saveToPostGIS(map.getMapContext(), (FLyrVect)lines, "f_temp", false);
			while(!ex.Terminate())
	         System.out.println("Procesando...");*/
    	     DBUtils.saveToPostGIS((FLyrVect)lines, "f_temp", false, true);
			 
		    JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		    db.ejecutarConsulta("select gid from f_temp");
		    if(db.isEmpty())
		    {
		     db.ejecutarConsulta("drop table f_temp");
		     JOptionPane.showMessageDialog(null, "No se completó la operación");
		     //db.close();
		     return;
		    }

		    String []ids = new String[db.getRowCount()];
		    for(int i=0; i<db.getRowCount(); i++)
		     ids[i] = db.getValueAt(i, 0).toString();
		    
		    for(int i=0; i<ids.length; i++)
		    { 	
		     db.ejecutarConsulta("update rios set the_geom=f_temp.the_geom from f_temp where rios.gid="+ ids[i] +" and f_temp.gid="+ ids[i]);
		     TrazasManager.insertar_Traza("Invirtó el sentido de digitalización del río con identificador "+ids[i]);
		    } 
		    
		    db.ejecutarConsulta("drop table f_temp");
		    VistaManager.RefreshView();
		    map.getMapContext().getLayers().getLayer("Hidrografía_lineal").reload();
		    db.close();
		    
       } catch (GeoAlgorithmExecutionException e) {
		e.printStackTrace();
	} catch (ReloadLayerException e) {
		e.printStackTrace();
	} catch (SQLException e) {
		e.printStackTrace();
	}
    }
	
	if (actionCommand.compareTo("show_arrows") == 0)
    {
	 FLayer []layers = AlgUtils.GetView(null).getMapControl().getMapContext().getLayers().getActives();
	 
	 for(int i=0; i<layers.length; i++)
	 {
	  try {
		if( ((FLyrVect)layers[i]).getShapeType() == FShape.LINE)
		{
		   if(!Funciones_Utiles.setArrowToLines((FLyrVect)layers[i], 1, 31, 11, 20))
			 JOptionPane.showMessageDialog(null, "Error", "Imposible mostrar sentido de lineas para la capa "+layers[i].getName(), JOptionPane.WARNING_MESSAGE);  
		}
		else
		 continue;
		
	     } catch (ReadDriverException e) {
		   e.printStackTrace();
	     } 	  
	 }	 
    }
	
	if (actionCommand.compareTo("loadlayers") == 0)
    {
	 View view = VistaManager.GetActiveView();	
     Global.projeccionActiva = view.getProjection();
	 PluginServices.cancelableBackgroundExecution((new CargarCapasWT(view)));
    }
   };
   
   public boolean isEnabled() 
	{
		return ConnectionExt.Conectado();
	}

	public boolean isVisible() 
	{
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

