package org.geocuba.foresta.fajas.writerTasks;

import javax.swing.JOptionPane;

import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.fajas.PerfilManager;
import org.geocuba.foresta.fajas.StructOrillas;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

 /**Writer task para generar las fajas segun el método de la tercera 
  * parte de la longitud de la ladera*/
 public class GenerarFaja_TereceraParteLongitud extends GenerarFaja
 {
	private  FLyrVect parteaguas;
	
	public GenerarFaja_TereceraParteLongitud(String menss, FLyrVect _capa, FLyrVect _parteaguas)
	{
     super(menss, _capa, 13);
     crear_tablas_temporales();  
     parteaguas = _parteaguas;
	}
	
	public void run() throws Exception 
	{
	 double izq = 0;
	 double der = 0;
			
	 if(capa == null)
	 {
	  setCanceled(true);	 
	  return;
	 } 
				 
      JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	  String gid = Funciones_Utiles.GetSelectedGids(parteaguas);
	  StructOrillas data = PerfilManager.calcular_Longitud_Media_Ladera(Integer.parseInt(gid), db);

	  if(data == null)
	  {
	   setCurrentStep(this.getFinishStep());
	   reportStep();
	   setCanceled(true);	
	   try {
		this.finalize();
		
			} catch (Throwable e) {
				e.printStackTrace();
			}
	   return;
	  } 
	 		
	  der = data.der;
	  izq = data.izq;
	 		  
	  if(der == 0 || izq == 0)
	  {
	   setCanceled(true);	  
	   return;
	  }
	 		  
	 //Genero la faja para el feature obtenido  
	 setOIzq(izq/3);
	 setODer(der/3);
	 setMemLayerFlag(false);
	 		 
	 crear_Fajas(capa);
	         
	 VistaManager.RefreshView();
	 VistaManager.GetActiveView().getMapControl().getMapContext().getLayers().getLayer("Fajas").reload(); 
     
	 reportStep();
	}
	
	/*public void finished() {
	 PluginServices.getMainFrame().enableControls();
	}*/
}
