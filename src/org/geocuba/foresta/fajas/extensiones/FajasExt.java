package org.geocuba.foresta.fajas.extensiones;

import java.util.ArrayList;
import java.util.BitSet;

import javax.swing.JOptionPane;

import org.geocuba.foresta.gestion_datos.Faja;
import org.geocuba.foresta.gestion_datos.FajaManager;
import org.geocuba.foresta.gestion_datos.Faja_real;
import org.geocuba.foresta.gestion_datos.Faja_realManager;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.DataSource;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.layerOperations.AlphanumericData;
import com.iver.cit.gvsig.project.documents.view.gui.View;

public class FajasExt extends Extension{
 	
	public void initialize() {
	}

public void execute(String actionCommand) {
		
		if (actionCommand.compareTo("fajasreal") == 0)
	    {
              View vista = VistaManager.GetActiveView();
              FLyrVect layer = (FLyrVect)vista.getMapControl().getMapContext().getLayers().getLayer("Fajas");
              
//			  JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
			  
//			  ArrayList<String> gids = Funciones_Utiles.GetSelectedGidsArray(layer);
//			  if(gids == null)
//			  {
//			   JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento", "Información", 1);	  
//			   return;
//			  } 
			  
              try {	
	      			BitSet sel = layer.getSelectionSupport().getSelection();
	      	   	    DataSource ds = ((AlphanumericData)layer).getRecordset();
	      	        ds.start();
	      	        
	      	        int fieldIndex= ds.getFieldIndexByName("gid");
	      	        
	      	        for (int i = sel.nextSetBit(0); i >= 0; i = sel.nextSetBit(i+1)) 
	      	        { 	
	      		     int idFaja = Integer.parseInt(ds.getFieldValue(sel.nextSetBit(i), fieldIndex).toString());
	      		     Faja faja = new FajaManager().Cargar_Objeto_BD(idFaja);
	      		     IGeometry geom = layer.getSource().getShape(i);
	      		     
	      		     Faja_real fr = new Faja_real();
	      		     fr.setFaja(faja);
	      		     fr.setGeometry(geom, FShape.POLYGON);
	      		     fr.save();
	      	        } 
	      	        
	      	        ds.stop();	
      			
//			  for(int i=0; i<gids.size(); i++)
//			  {
//			   db.ejecutarConsulta("insert into fajas_real(faja, the_geom) " +
//			   		           "select gid, the_geom " +
//			   		           "from fajas where gid="+gids.get(i));	
//			   db.ejecutarConsulta("select max(gid) from fajas_real");
//			   TrazasManager.insertar_Traza("Insertó la Fajas_Real con identificador "+db.getValueAsString(0,0));
//			  }	  
			  
			      vista.getMapControl().getMapContext().getLayers().getLayer("Fajas_Real").reload();
			      VistaManager.RefreshView();
				 // db.close();
				  
			} catch (ReloadLayerException e) {
				JOptionPane.showMessageDialog(null, "Error recargando capa 'Fajas_Real'", "Información", JOptionPane.WARNING_MESSAGE);
			} catch (ReadDriverException e) {
				JOptionPane.showMessageDialog(null, "No se pudieron obtener los datos de las fajas seleccionadas", "Error", JOptionPane.ERROR_MESSAGE);
			}
	    }
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
			 
		   if(layers[0].getName().equals("Fajas"))
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

