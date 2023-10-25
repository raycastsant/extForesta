package org.geocuba.foresta.fajas.writerTasks;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import org.geocuba.foresta.fajas.extensiones.AnchoFijo_LegislacionExt;
import org.geocuba.foresta.herramientas.utiles.Global;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.DataSource;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.drivers.ConcreteMemoryDriver;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;
import com.iver.cit.gvsig.fmap.layers.layerOperations.AlphanumericData;

public class GenerarFaja_Simetrica extends GenerarFaja
{
    private double _ancho;
	
  /**Este metodo se usa cuando en el ancho fijo se introducen valores iguales*/
    public GenerarFaja_Simetrica(double ancho, FLyrVect layer, String menss)
    {		
     super(menss, layer, 8);
     //setFinalStep(8);
     _ancho = ancho;
    }
     
    public void run() throws Exception 
    {
    	crear_tablas_temporales();
    	
	    if(capa == null)
		 return;
		
		int shapeType = -1;
		
		if(capa.getName().equals(AnchoFijo_LegislacionExt.HIDRO_LINEAL_LAYER_NAME))
		 shapeType = FShape.LINE;
		else
		 return;
		
		try {
		     BitSet sel = ((FLyrVect)capa).getSelectionSupport().getSelection();
		     DataSource ds = ((AlphanumericData)capa).getRecordset();
		   
	         ds.start();
	         int fieldIndex= ds.getFieldIndexByName("gid");
	         
	 	     int actualCount = 0;
	         
	         for (int i=sel.nextSetBit(0); i>=0; i=sel.nextSetBit(i+1)) 
	         {
	          if(isCanceled())
	           return;	  
	        	  
	          Integer gid = Integer.parseInt(ds.getFieldValue(sel.nextSetBit(i), fieldIndex).toString());	 
	 		  
	          actualCount++;
	          reportStep();
		      setNote("Procesando objetos "+(getCurrentStep()*100)/getFinalStep()+"%");   //((Integer)actualCount).toString()+" de "+featCount+"  con gid = "+gid);
	          
	 	     //Obteniendo el feature actual como una capa
		      FLayer ltemp = null;
		      
		      if(shapeType == FShape.LINE) //Si la hidrografia es areal no hace falta pasar la capa
		      {	  
	 		   ConcreteMemoryDriver driver = new ConcreteMemoryDriver();
	 		   driver.setShapeType(shapeType);
	 		   List<String> campos = new ArrayList<String>();
	 		   campos.add("gid");
	 		   Value[] row = new Value[campos.size()];
	 		   driver.getTableModel().setColumnIdentifiers(campos.toArray());
	 		  
	 		  //obtengo la geometria del feature
	 		   IGeometry geom = ((FLyrVect)capa).getSource().getShape(i); 
	 		   row[0] = ValueFactory.createValue(gid);
	 		   driver.addGeometry(geom, row);
			  
	 		   ltemp = LayerFactory.createLayer("Hidro", driver, Global.projeccionActiva);
		      }
		      
	 		  crearFajaSimetrica(_ancho, ltemp);      
	 		  
	 		  reportStep();
	 		  setNote("Procesando objetos "+(getCurrentStep()*100)/getFinalStep()+"%");
	         }
	         
	         ds.stop();
	         
	 } catch (ReadDriverException e) {
		e.printStackTrace();
	} 
		
	reportStep();
   }
}
