package org.geocuba.foresta.analisis_terreno.interseccion;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.geocuba.foresta.analisis_terreno.interseccion.recortes.vectorial.CortarVectorial;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import org.geocuba.foresta.herramientas.utiles.Global;
import org.gvsig.raster.grid.GridException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.DataSource;
import com.iver.andami.ConfigurationException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.exceptions.layers.LoadLayerException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.layerOperations.AlphanumericData;
import es.unex.sextante.core.Sextante;
import es.unex.sextante.exceptions.GeoAlgorithmExecutionException;

public abstract class _Intersect extends CortarVectorial{
	
	//private FLayer capa;
//	protected boolean useLastMDE;
	//protected MapControl mapCtrl;
	protected Object mde = null;	
	
	
	public _Intersect(FLyrVect _capa, MapControl mapCtrl, boolean _useLastMDE) throws ReadDriverException, LoadLayerException, GridException, InterruptedException
	{
	 super(_capa, mapCtrl);
	 useLastMDE = _useLastMDE;
	 
//     capa = _capa;
     //rectangle = _rectangle;
//     setInitialStep(0);
//	 setDeterminatedProcess(false);
//	 //setStatusMessage(PluginServices.getText(this, "Hallando intersección"));
//	 setNote(PluginServices.getText(this, "Procesando información"));
//	 setFinalStep(1);
	}		
	
	@Override
	protected void NextProcess() 
	{
		FLayer sdep = null;
		
		setCurrentStep(0);
		setNote("Iniciando proceso");
		
		if(!useLastMDE)	
	    {	
		    Sextante.initialize();
			try {
			  //Hallo el mde con el resultado de la interseccion
			  if(!isCanceled())
			  {	  
				 setCurrentStep(0);
				 setNote("Convirtiendo a puntos las curvas de nivel");
				 FLyrVect ptos = (FLyrVect)AlgUtils.LinesToPoints(clip, 10, "");
				 
               if(!isCanceled())	
               {
            	setCurrentStep(0);
  				setNote("Creando Modelo Digital de Elevaciones");
            	double cellZize = Funciones_Utiles.getRasterCellZize();
				mde = (FLayer)AlgUtils.HallarMDE(ptos, "elevacion", null, cellZize);
               }	
			  }	
			   reportStep();
			   
			   if(!isCanceled() && mde!=null)
			   {
				setCurrentStep(0);
				setNote("Eliminando depresiones del modelo");   
			    sdep = AlgUtils.eliminarDepresiones(mde, "");
			   } 
			   else
				cancel();    
				   
			   if(!isCanceled() && sdep!=null)
			   {	
					 sdep.setName("MDE");	
					 AlgUtils.addLayertoView(sdep);  	
					 mde = sdep;
		             Global.setMDE(mde);
			   }	
			   
			   reportStep();
				
				
			} catch (GeoAlgorithmExecutionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ConfigurationException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error leyendo el tamaño de celdas ráster");
			}
		
	     }  //MDE Anterior -----------------------------------------------
		 else
		  mde = Global.getMDE();
		
		 SiguienteProceso();
		 
		 reportStep();
	    	
	}
	
	//Metodo a redefinir
	protected abstract void SiguienteProceso(); 
	
	//Metodo a redefinir
	public abstract void finished(); 
	
	public void setRectangle(Rectangle2D _rect)
	{
	 rect = _rect;	
	}
	
	public void initialize()
	{
	 setInitialStep(0);
	 setDeterminatedProcess(false);
	 //setStatusMessage(PluginServices.getText(this, "Hallando intersección"));
	 setNote(PluginServices.getText(this, "Hallando intersección"));
	 setFinalStep(1);
	}
	
	public FLyrVect getLayer()
	{
	 return (FLyrVect)layer;
	}
	
}
