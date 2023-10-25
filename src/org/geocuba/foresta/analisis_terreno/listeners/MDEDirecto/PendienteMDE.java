package org.geocuba.foresta.analisis_terreno.listeners.MDEDirecto;

import java.io.IOException;
import java.util.HashMap;
import javax.swing.JOptionPane;
import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.geocuba.foresta.analisis_terreno.gui.PendienteDlgManager;
import org.geocuba.foresta.analisis_terreno.interseccion.recortes.raster.ClipRaster;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import org.geocuba.foresta.herramientas.utiles.Global;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.grid.GridException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.exceptions.layers.LegendLayerException;
import com.iver.cit.gvsig.exceptions.layers.LoadLayerException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import es.unex.sextante.dataObjects.IRasterLayer;
import es.unex.sextante.exceptions.GeoAlgorithmExecutionException;
import es.unex.sextante.parameters.FixedTableModel;

/**Clase para obtener la pendiente en un rango deseado*/
public class PendienteMDE extends ClipRaster
{
	
	public PendienteMDE(FLyrRasterSE raster,  MapControl _mapCtrl)throws ReadDriverException, LoadLayerException, GridException, InterruptedException {
		super(raster, _mapCtrl);
	}
	
	protected void NextProcess()
	{
		Object pend = null;
		Object reclas = null;
		FLyrVect penvect = null;
		
		try {
			
		   setCurrentStep(0);
		   setNote("Hallando Pendiente");
		  
		   if(!isCanceled() && clip!=null)
		   {	    
			Global.setMDE(clip);
		    pend = AlgUtils.Pendiente(clip, null);
		   } 
		   
			    double max = ((IRasterLayer)pend).getMaxValue();
			    double min = ((IRasterLayer)pend).getMinValue();
			    double range = Math.abs(max - min);
			    
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
			    
		   if(!isCanceled())
		   {	   
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
		    penvect = (FLyrVect)AlgUtils.Vectorizar(reclas, "");
		   }  
		   else
		   {
				 reportStep();	
				 return;
		   }	
		    //penvect.setName("Pendiente");
		    
		        if(!isCanceled() && penvect!=null)
			    {
			        penvect = (FLyrVect)AlgUtils.RemoveItem(penvect, "Pendiente[", maxvalue.toString(), "");  
			        penvect.setName("Pendiente");
			        
                    AlgUtils.addLayertoView(penvect);
                    
                    View vista = VistaManager.GetActiveView();
                    vista.getMapControl().getMapContext().beginAtomicEvent();
                    Funciones_Utiles.LeyendaValoresUnicos(penvect, "Pendiente[", false, descs);
//     			    vista.getTOC().refresh();
                    vista.getMapControl().getMapContext().endAtomicEvent();

			     }
		        else
		        {
					 reportStep();	
					 return;
			   }
		        
		} catch (GeoAlgorithmExecutionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (LegendLayerException e) {
			e.printStackTrace();
		}
		  reportStep();
	}
 	
	public void finished() 
	{
	 	
	}
}
