package org.geocuba.foresta.fajas.extensiones;

import java.util.BitSet;

import javax.swing.JOptionPane;

import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.fajas.gui.pAncho_Calculos;
import org.geocuba.foresta.gestion_datos.Intensidad_lluviaManager;
import org.geocuba.foresta.gestion_datos.Velocidad_infiltracionManager;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.DataSource;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.core.GeometryUtilities;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.edition.UtilFunctions;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.layerOperations.AlphanumericData;
import com.iver.cit.gvsig.geoprocess.core.util.GeometryUtil;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
*
* @author Raisel
*/
public class AnchoCalculosExt extends Extension{
	
	//private static JDBCAdapter db = null;
//	private static View vista = null;
//	private static MapControl mapCtrl = null;
	private FLayer [] layers = null;
	public static FLyrVect selectedLayer;
	public static FLyrVect parteaguas;
	
	public void initialize() {
	}

	public void execute(String actionCommand) {
		
		if (actionCommand.compareTo("AnchoCalc") == 0)
	    {
			View vista = VistaManager.GetActiveView();
			layers = vista.getModel().getMapContext().getLayers().getActives();
			 
		    if(layers[0].getName().equals("Hidrografía_lineal"))
		    {	
//		     String gid = Funciones_Utiles.GetSelectedGids(layers[0]);
//		     JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
//		     db.ejecutarConsulta("select ST_Length(rios.the_geom) from rios where gid in("+ gid +")");
		    	
		    	try {	
					 BitSet sel = ((FLyrVect)layers[0]).getSelectionSupport().getSelection();
					        
					 IGeometry geom = null;
                     for (int i = sel.nextSetBit(0); i >= 0; i = sel.nextSetBit(i+1)) 
					  geom = ((FLyrVect)layers[0]).getSource().getShape(i);
					        
					 		
				     double length = GeometryUtilities.getLength(vista.getMapOverview().getViewPort(), geom);
				     length = Math.round(length);
				     
					 pAncho_Calculos pc = new pAncho_Calculos("Faja Forestal (Hidrografía lineal)", "Longitud del río (m): "+((Double)length).toString());
					// PluginServices.getMDIManager().addCentredWindow(pc);
					 
					//Llenando combo de tiempos de duración 
					 Double []tiempos = Intensidad_lluviaManager.Obtener_Tiempos();
		             if(tiempos != null)
		             {
			              pc.getComboDuracion().setEnabled(true);	 
			              pc.getComboDuracion().removeAllItems();
			              
			              for(int i=0; i<tiempos.length; i++)
			               pc.getComboDuracion().addItem(tiempos[i]);
			              
			              pc.getComboDuracion().setEnabled(false);
			              
			            //Llenando combo de probabilidades
			              Double []probabilidades = Intensidad_lluviaManager.Obtener_Probabilidades();
			              if(probabilidades != null)
			              {
				               pc.getComboProbabilidad().setEnabled(true);	   
				               pc.getComboProbabilidad().removeAllItems();	
				               
				               for(int i=0; i<probabilidades.length; i++)
				                pc.getComboProbabilidad().addItem(probabilidades[i]);
				               
				               pc.getComboProbabilidad().setEnabled(false);
							   pc.Show();
			              }
			              else
			               JOptionPane.showMessageDialog(null, "No se obtuvieron datos de tiempo en la tabla 'intensidad_lluvia'");	  
		             } 
		             else
		              JOptionPane.showMessageDialog(null, "No se obtuvieron datos de probabilidad en la tabla 'intensidad_lluvia'"); 	 
		    
		    } catch (ReadDriverException e) {
					JOptionPane.showMessageDialog(null,"Error accediendo a la selección de la capa", "Información", JOptionPane.WARNING_MESSAGE);
					return;
		    }
		  }
//		    else
//		    if(layers[0].getName().equals("Red"))
//		    {	
//		    	 String gid = Funciones_Utiles.GetSelectedGids(layers[0]);
//		    	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
//			     db.ejecutarConsulta("select ST_Length(f_reddrenaje.the_geom) from f_reddrenaje where gid in("+ gid +")");
//			     
//			     double length = 0;
//			     if(db.getRowCount() == 1)
//			      length = Math.round(Double.parseDouble(db.getValueAt(0,0).toString()));
//			     
//		     pAncho_Calculos pc = new pAncho_Calculos("Faja Forestal (Hidrografía areal)","Longitud del río (m): "+ length);
//		     PluginServices.getMDIManager().addCentredWindow(pc);
//			 pc.Show();
//		    }
	    }
		
	    };

//		/**Calcula el ancho de una faja por el metodo de la 3ra parte
//		 * de la longitud de la ladera*/
//		public static void GenerarPerfiles(double distancia, boolean showEditing)
//		{
//		// if(isLineal)
//	      PluginServices.cancelableBackgroundExecution(new genPerfiles(distancia, "Generando Perfiles...", showEditing));
//		 //else
//		  //PluginServices.cancelableBackgroundExecution(new genPerfilesAreal(distancia, "Generando Perfiles...", showEditing)); 	 
//		}
		
//		/**Genera las fajas forestales segun los perfiles establecidos*/
//		public static void GenerarFaja_Perfiles()
//		{
//		 PluginServices.cancelableBackgroundExecution(new GenerarFaja_Perfiles("Generando Fajas", selectedLayer));	
//		}
		
	public boolean isEnabled() 
	{
		if(!ConnectionExt.Conectado())
		 return false;
			  
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
		if(f == null)
		 return false;	
		
		if(!(f instanceof View))
		 return false;
		
		layers = ((View)f).getModel().getMapContext().getLayers().getActives();
		 
		 if(layers != null && layers.length == 1)
		 {	
		   if(!layers[0].isVisible() || layers[0].isEditing())
			return false;
			 
		   if(layers[0].getName().equals(AnchoFijo_LegislacionExt.HIDRO_LINEAL_LAYER_NAME))
		   {
			 FLayer lyr = ((View)f).getModel().getMapContext().getLayers().getLayer("Parteaguas");
			 if(lyr == null)
		      return false;		 
			   
			if(Funciones_Utiles.GetSelectionCount(layers[0]) == 1 && Funciones_Utiles.GetSelectionCount(lyr) == 1)
			{
			 selectedLayer = (FLyrVect)layers[0];
			 parteaguas = (FLyrVect)lyr;
		     return true;
			} 
			else
			 return false;	
		   } 
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

