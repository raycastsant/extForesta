package org.geocuba.foresta.analisis_terreno;

import java.awt.HeadlessException;
import javax.swing.JOptionPane;

import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.Red_drenaje;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FBitSet;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.project.documents.view.gui.View;
 
public class SaveRedDrenajeExt extends Extension
{
	public void initialize() 
	{
	}

	public void execute(String actionCommand) {
		
		if (actionCommand.compareTo("save_reddrenaje") == 0)
	    {
			 View vista = VistaManager.GetActiveView();
			 FLyrVect layer = (FLyrVect)vista.getMapControl().getMapContext().getLayers().getActives()[0];
			 
			try {
				
			  PluginServices.getMDIManager().setWaitCursor();		
			  
			  SelectableDataSource ds = layer.getRecordset();
			  FBitSet bs = layer.getSelectionSupport().getSelection();
			  int ordenIndex = ds.getFieldIndexByName("orden");
			  int sigteIndex = ds.getFieldIndexByName("siguiente");
			  int idIndex = ds.getFieldIndexByName("id");
			  
			  if(ordenIndex>=0 && sigteIndex>=0 && idIndex>=0) //Identifico que sea el shape generado
			  {	  
				IGeometry geom = null;
				for (int j=bs.nextSetBit(0); j >=0; j=bs.nextSetBit(j+1))
				{
				 int orden = Integer.parseInt(ds.getFieldValue(j, ordenIndex).toString());
				 int sigte = Integer.parseInt(ds.getFieldValue(j, sigteIndex).toString());
				 geom = layer.getSource().getShape(j);
				 
				 Red_drenaje red = new Red_drenaje();
				 red.setOrden(orden);
				 red.setSiguiente(sigte);
				 red.setGeometry(geom, layer.getShapeType());
				 
				 red.save();
				}
			  
					if(geom != null)
					{	
						vista.getMapControl().getMapContext().getLayers().getLayer("Redes_drenaje").reload();
						VistaManager.RefreshView();
					}	
					else
				 	{
					  JOptionPane.showMessageDialog(null, "Debe seleccionar uno o más elementos", "Información", JOptionPane.INFORMATION_MESSAGE);
					  PluginServices.getMDIManager().restoreCursor();
				 	}	 
			  }	
			  else
			  {
				  JOptionPane.showMessageDialog(null, "La capa seleccionada no corresponde a la red de drenaje generada a partir del MDE", "Error", JOptionPane.ERROR_MESSAGE);
				  PluginServices.getMDIManager().restoreCursor();
			  }	  
			  
//			  JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
//			  
//			  db.ejecutarConsulta("DROP TABLE IF EXISTS f_temp");
//			  Funciones_Utiles.saveToPostGIS((FLyrVect)layer[0], "f_temp", false, true);
//			  
//			  db.ejecutarConsulta("select orden, siguiente, st_astext(the_geom) as the_geom from f_temp");
//			  if(!db.isEmpty())
//			  {
//				  int orden = db.getValueAsInteger(0,0);
//				  int siguiente = db.getValueAsInteger(0,1);
//				  String geom = ""
//				  
//				  db.ejecutarConsulta("DROP TABLE IF EXISTS f_temp");
//				  db.ejecutarConsulta("select max(gid) from parteaguas");
//				  
//				  TrazasManager.insertar_Traza("Insertó el parteaguas "+db.getValueAsString(0,0));
//				  
//				  vista.getMapControl().getMapContext().getLayers().getLayer("Parteaguas").reload();
//				  AlgUtils.RefreshView();
//				  PluginServices.getMDIManager().restoreCursor(); 
//			  }	
//			  else
//			  {
//				  
//			  }	  
			  
				  
			} catch (ReloadLayerException e) {
				e.printStackTrace();
			} catch (HeadlessException e) {
				e.printStackTrace();
			} catch (ReadDriverException e) {
				e.printStackTrace();
			}
			
			PluginServices.getMDIManager().restoreCursor(); 
	    }
		
	    };
	
	public boolean isEnabled() 
	{
		if(!ConnectionExt.Conectado())
			 return false;
				  
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
		if(f == null)
		 return false;	
		
		if(!(f instanceof View))
		 return false;
		
		FLayer[] layer = ((View)f).getMapControl().getMapContext().getLayers().getActives();
		if(layer==null)
		 return false;
		
		if(layer.length != 1)
		 return false;	
		
		if(!(layer[0] instanceof FLyrVect))
	     return false;
		
		FLyrVect flyrvect = (FLyrVect)layer[0];
		try {
			if(flyrvect.getShapeType() != FShape.LINE)
			 return false;
			
		} catch (ReadDriverException e) {
			return false;
		}
		
		return true;
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

