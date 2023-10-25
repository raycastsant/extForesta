package org.geocuba.foresta.fajas.writerTasks;

import java.util.BitSet;

import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import com.hardcode.gdbms.engine.data.DataSource;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.layerOperations.AlphanumericData;

public class GenerarFaja_Embalse extends GenerarFaja{
    private double _ancho;
	
    public GenerarFaja_Embalse(double ancho, String menss, FLyrVect _capa) /*throws ReadDriverException*/
    {		
     super(menss, _capa, 3);
     _ancho = ancho;
    }
     
    public void run() throws Exception 
    {
    	BitSet sel = capa.getSelectionSupport().getSelection();//.getSelectionSupport().getSelection();
	    DataSource ds = ((AlphanumericData)capa).getRecordset();
	   
        ds.start();
        int fieldIndex= ds.getFieldIndexByName("gid");
        
        int featCount = Funciones_Utiles.GetSelectionCount(capa);
	    int actualCount = 0;
        
        for (int i = sel.nextSetBit(0); i >= 0; i = sel.nextSetBit(i+1)) 
        {
         if(isCanceled())
 	      return;		
        	
         String idF = ds.getFieldValue(sel.nextSetBit(i), fieldIndex).toString();
         
         actualCount++;
	     reportStep();
	     setNote("   Procesando objeto "+((Integer)actualCount).toString()+" de "+featCount+"  con gid = "+idF);
         
         doFajaEmbalse(_ancho, idF);
         
         reportStep();
        }
        
        ds.stop();
        
        VistaManager.RefreshView();
        VistaManager.GetActiveView().getMapControl().getMapContext().getLayers().getLayer("Fajas").reload();
    }
    
	/*public void finished() {
		PluginServices.getMainFrame().enableControls();
	}*/
	
}
