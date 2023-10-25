package org.geocuba.foresta.analisis_terreno.listeners;

import java.io.IOException;
import java.util.HashMap;
import javax.swing.JOptionPane;
import org.geocuba.foresta.analisis_terreno.CuencasExt;
import org.geocuba.foresta.analisis_terreno.gui.PendienteDlgManager;
import org.geocuba.foresta.analisis_terreno.interseccion._Intersect;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import org.gvsig.raster.grid.GridException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.exceptions.layers.LegendLayerException;
import com.iver.cit.gvsig.exceptions.layers.LoadLayerException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import es.unex.sextante.dataObjects.IRasterLayer;
import es.unex.sextante.exceptions.GeoAlgorithmExecutionException;
import es.unex.sextante.gvsig.core.gvRasterLayer;
import es.unex.sextante.parameters.FixedTableModel;

/**Clase para hallar la pendiente a partir de 
 * un area de relieve seleccionada*/
public class Pendiente extends _Intersect{
	
	protected FLyrVect pendienteFinal;
	
	public Pendiente(FLyrVect relieve, boolean _useLastMDE, MapControl _mapCtrl)throws ReadDriverException, LoadLayerException, GridException, InterruptedException {
		super(relieve, _mapCtrl, _useLastMDE);
		//mapCtrl = _mapCtrl;
	}
	
	protected void SiguienteProceso()
	{
		Object pend = null;
		Object reclas = null;
		
		try {
			
		   setCurrentStep(0);
		   setNote("Hallando Pendiente");
		  
		   if(!isCanceled() && mde!=null)
			pend = AlgUtils.Pendiente(mde, null);
		    double max = ((IRasterLayer)pend).getMaxValue();
		    double min = ((IRasterLayer)pend).getMinValue();
		    double range = Math.abs(max - min);
		    
//		    String selected = null;	
//			 selected = JOptionPane.showInputDialog(null, "Rango de pendientes ", "Introduzca un valor de rango para la pendiente :", JOptionPane.QUESTION_MESSAGE);
		    
		     PendienteDlgManager penman = new PendienteDlgManager();
		     Double rango = penman.mostrar_Dialogo();
		     
			if(rango==null || rango==-1)
			{
			 setCanceled(true);	
			 reportStep();	
			 return;
			}		
			
			double rang = Math.abs(rango); 
			if((rang)>range || range<1)
			{
				 JOptionPane.showMessageDialog(null, "Rango no válido. La máxima pendiente del área es "+max);
				 setCanceled(true);	
				 reportStep();	
				 return;
		    }
			
		    double partsValue = rang;
		    String value = (new Double(Math.ceil(range/partsValue))).toString();
		    int rows = Integer.parseInt(value.substring(0, value.length()-2));
		    
		    String []cols = {"Valor mínimo","Valor máximo","Nuevo valor"};
		    FixedTableModel table = new FixedTableModel(cols, rows, false);
		    Double fvalue = new Double(0);
		    Double maxvalue = new Double(0);
		    HashMap<String,String> descs = new HashMap<String, String>();
		    
		    for(int i=0; i<rows; i++)
		    {
		     fvalue = maxvalue;
		     maxvalue += partsValue;	
		    
		    //1ra columna  
		     if(i==0)
		      table.setValueAt("-1", i, 0);
		     else
		      table.setValueAt(fvalue, i, 0);	 
			
		   //2da columna
		     table.setValueAt(maxvalue, i, 1);
			 
		   //3ra columna 
			 if(i+1 == rows)
			 {
			  table.setValueAt(Math.round(max)+".0", i, 2);
			  descs.put( new String(""+Math.round(max)+".0") , new String(fvalue+" - "+Math.round(max)+".0") );
			 } 
			 else	 
			 {
			  table.setValueAt(maxvalue, i, 2);	 
			  descs.put( new String(""+maxvalue) , new String(fvalue+" - "+maxvalue) );
			 } 
		    } 	
		    
		    reclas = AlgUtils.reclassifyRaster(pend, table, null);
		    ((gvRasterLayer)reclas).setName("pendiente");
		    pendienteFinal = (FLyrVect)AlgUtils.Vectorizar(reclas, "");
		    pendienteFinal.setName("Pendientes");
		    
			if(!isCanceled())	
			{
			 if(mapCtrl.getMapContext().getLayers().getLayer("Pendientes") != null)
			   mapCtrl.getMapContext().getLayers().removeLayer("Pendientes");
			 
			 //AlgUtils.addLayertoView(pendienteFinal);	 
			 Funciones_Utiles.LeyendaValoresUnicos(pendienteFinal, "Pendiente", false, descs);
			 
			 //RasterToolsUtil.loadLayer(PluginServices.getMDIManager().getWindowInfo(AlgUtils.GetView(null)).getTitle(), "pendiente.tif", "Pendiente");
			} 
			
		  //reportStep();
		  
		} catch (GeoAlgorithmExecutionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (LegendLayerException e) {
			e.printStackTrace();
		}
		
		//AlgUtils.GetView(null).getTOC().refresh();
	}
 	
	public void finished() 
	{
	 Pendiente pend;
	 try {
		 pend = new Pendiente(this.getLayer(), useLastMDE, mapCtrl);
		CuencasExt.initializeListener(pend);
		
		AlgUtils.addLayertoView(pendienteFinal);	 
				
	 } catch (ReadDriverException e) {
		e.printStackTrace();
	 } catch (LoadLayerException e) {
		e.printStackTrace();
	} catch (GridException e) {
		e.printStackTrace();
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	 
	 reportStep();
	}
}
