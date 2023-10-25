package org.geocuba.foresta.analisis_terreno;

import java.awt.HeadlessException;
import javax.swing.JOptionPane;

import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.Parteaguas;
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
 
public class SaveCuencasExt extends Extension{
	public void initialize() {
	}

	public void execute(String actionCommand) {
		
		if (actionCommand.compareTo("savecuencas") == 0)
	    {
		 View vista = AlgUtils.GetView(null);
		 FLayer[] layer = vista.getMapControl().getMapContext().getLayers().getActives();
		 
		try {
			
		 if(layer.length > 1)
		 {
		  JOptionPane.showMessageDialog(null, "Existen más de una capa activas", "Error", JOptionPane.ERROR_MESSAGE);
		  return;
		 } 	 
		 else
		 if( ((FLyrVect)layer[0]).getShapeType() != FShape.POLYGON)	 
		 {
		  JOptionPane.showMessageDialog(null, "Debe seleccionar una capa de polígonos", "Error", JOptionPane.ERROR_MESSAGE);
		  return; 
		 }
		 else 
		 {
		  PluginServices.getMDIManager().setWaitCursor();	
		  
		  FLyrVect lyr = (FLyrVect)layer[0];
		  
		  SelectableDataSource ds = lyr.getRecordset();
		  FBitSet bs = lyr.getSelectionSupport().getSelection();
		  int idIndex = ds.getFieldIndexByName("ID");
		  int cuencasIndex = ds.getFieldIndexByName("Cuencas");
		  
		  if(idIndex>=0 && cuencasIndex>=0 && idIndex>=0) //Identifico que sea el shape generado
		  {	  
			IGeometry geom = null;
			for (int j=bs.nextSetBit(0); j >=0; j=bs.nextSetBit(j+1))
			{
			 geom = lyr.getSource().getShape(j);
			 
			 Parteaguas parteaguas = new Parteaguas();
			 parteaguas.setGeometry(geom, lyr.getShapeType());
			 parteaguas.setDescripcion("");
			 parteaguas.save();
			}
		  
				if(geom != null)
				{	
					vista.getMapControl().getMapContext().getLayers().getLayer("Parteaguas").reload();
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
			  JOptionPane.showMessageDialog(null, "La capa seleccionada no corresponde a las cuencas generadas a partir del MDE", "Error", JOptionPane.ERROR_MESSAGE);
			  PluginServices.getMDIManager().restoreCursor();
		  }	  
			  
		PluginServices.getMDIManager().restoreCursor(); 
		
		 }
		} catch (ReloadLayerException e) {
			e.printStackTrace();
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (ReadDriverException e) {
			e.printStackTrace();
		}
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
		
		FLayer[] layer = ((View)f).getMapControl().getMapContext().getLayers().getActives();
		if(layer==null)
		 return false;
		
		if(layer.length != 1)
		 return false;	
		
		if(!(layer[0] instanceof FLyrVect))
	     return false;
		
		FLyrVect flyrvect = (FLyrVect)layer[0];
		try {
			if(flyrvect.getShapeType() != FShape.POLYGON)
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

