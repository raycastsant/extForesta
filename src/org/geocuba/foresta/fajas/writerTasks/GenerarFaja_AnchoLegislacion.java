package org.geocuba.foresta.fajas.writerTasks;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.fajas.extensiones.AnchoFijo_LegislacionExt;
import org.geocuba.foresta.gestion_datos.Hidrografia;
import org.geocuba.foresta.gestion_datos.HidrografiaManager;
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

 public class GenerarFaja_AnchoLegislacion extends GenerarFaja
 {
    public GenerarFaja_AnchoLegislacion(String menss, FLyrVect _capa) 
    {		
     super(menss, _capa, 8);
     
     crear_tablas_temporales();
    }
     
    public void run() throws Exception 
    {
		    if(capa == null)
			 return;
			
			String tablename = "";
			int shapeType = -1;
			
			if(capa.getName().equals(AnchoFijo_LegislacionExt.HIDRO_LINEAL_LAYER_NAME))
			{	
			 tablename = "rios";
			 shapeType = FShape.LINE;
			} 
			else
			if(capa.getName().equals(AnchoFijo_LegislacionExt.HIDRO_AREAL_LAYER_NAME))
			{		
			 tablename = "embalses";
			 shapeType = FShape.POLYGON;
			} 
			else
			 return;
			
			try {
			     BitSet sel = ((FLyrVect)capa).getSelectionSupport().getSelection();
			     DataSource ds = ((AlphanumericData)capa).getRecordset();
			   
		         ds.start();
		         int fieldIndex= ds.getFieldIndexByName("gid");
		         
		         //int featCount = Funciones_Utiles.GetSelectionCount(capa);
		 	     int actualCount = 0;
		         
		         for (int i=sel.nextSetBit(0); i>=0; i=sel.nextSetBit(i+1)) 
		         {
		          if(isCanceled())
		           return;	  
		        	  
		          //id del rio o embalse actual 	 
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
			      
			      HidrografiaManager hidrografiaManager = new HidrografiaManager();
			      JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
			      Hidrografia hidrografia = hidrografiaManager.cargar_Hidrografia(tablename, gid, db);
			      if(hidrografia == null)
			      {
			       setCanceled(true);
			       return;
			      }	  
			      
			      double ancho = hidrografia.getTipo_hidrografia().getAncho_faja();
			      
//		 		  db.executeQuery("select ancho_faja from f_tipo_hidrografia inner join "+tablename+" on f_tipo_hidrografia.codigo=" +
//		 		  	tablename+".tipohidro where "+tablename+".gid="+idF);
//		 		  
//		 		  if(db.isEmpty())
//		 		  {
//		 			JOptionPane.showMessageDialog(null, "No se realizó la operación; Faltan datos en el embalse");
//		 			return;
//		 		  }	  
		 		  
//		 		  double ancho = Double.parseDouble(db.getValueAt(0,0).toString());
		 		  
		 		 //Genero la faja para el feature obtenido 
		 		  if(tablename.equals("rios"))
		 		   crearFajaSimetrica(ancho, ltemp);      //F_AnchoFijoALegislExt.GenerarFajaSimetrica(ancho, null, ltemp);
		 		  else
		 		  if(tablename.equals("embalses"))	
		 		   doFajaEmbalse(ancho, gid.toString());                     //F_AnchoFijoALegislExt.GenerarFajaEmbalse(ancho);
		 		  else
		 			System.out.println("Nombre de capa incorrecto");
		 		  
		 		  reportStep();
		 		  setNote("Procesando objetos "+(getCurrentStep()*100)/getFinalStep()+"%");
		         }
		         
		         ds.stop();
		         
		 } catch (ReadDriverException e) {
			e.printStackTrace();
		} 
			
    	reportStep();
    }
    
	/*public void finished() {
		PluginServices.getMainFrame().enableControls();
		AlgUtils.RefreshView();
		try {
			AlgUtils.GetView(null).getMapControl().getMapContext().getLayers().getLayer("Fajas").reload();
			
		} catch (ReloadLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
}
