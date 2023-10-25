package org.geocuba.foresta.analisis_terreno.listeners;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.io.IOException;
import javax.swing.ImageIcon;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.geocuba.foresta.herramientas.utiles.Global;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.core.symbols.IFillSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.ILineSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.tools.BehaviorException;
import com.iver.cit.gvsig.fmap.tools.Events.PointEvent;
import com.iver.cit.gvsig.fmap.tools.Listeners.PointListener;
import com.iver.cit.gvsig.gui.GUIUtil;
import com.iver.utiles.swing.threads.AbstractMonitorableTask;
import es.unex.sextante.core.Sextante;
import es.unex.sextante.exceptions.GeoAlgorithmExecutionException;
import es.unex.sextante.parameters.FixedTableModel;

public class CuencaPointListener implements PointListener
{
	private MapControl mapCtrl;
	private final Image img = new ImageIcon(MapControl.class.getResource("images/PointSelectCursor.gif")).getImage();
	private final Cursor m_Cursor = Toolkit.getDefaultToolkit().createCustomCursor(img, new Point(16, 16), "");

	public CuencaPointListener(MapControl mapCtrl)
	{
		this.mapCtrl=mapCtrl;
	}

	//Se activa cuando se de clic en un punto porque la clase hereda de un listener
	public void point(PointEvent event) throws BehaviorException 
	{
		Point2D p = event.getPoint();
		Point2D mapPoint = mapCtrl.getViewPort().toMapPoint((int) p.getX(), (int) p.getY());

		PluginServices.cancelableBackgroundExecution(new CreateWS(mapPoint));
	}
	
	/**Clase para controlar mediante un thread*/
    private class CreateWS extends AbstractMonitorableTask
    {
     private Point2D mapPoint;
    	
    public CreateWS(Point2D p)
     {
    	mapPoint = p;
    	
   	    setInitialStep(0);
 		setDeterminatedProcess(false);
 		setNote("iniciando...");
 	    setFinalStep(1);
     }
   	 
		public void run() throws Exception
	    {
          try {
				
				Sextante.initialize();			
				Object mde = Global.getMDE();
				
				setInitialStep(0);
				setNote("Hallando tiempos de salida");
				Object ts = AlgUtils.TiemposDeSalida(mde, null, 1, mapPoint, null);
				
				setInitialStep(0);
				setNote("Reclasificando resultado");
				
				String []cols = {"Valor mínimo","Valor máximo","Nuevo valor"};
			    FixedTableModel table = new FixedTableModel(cols, 1, false);
			    table.setValueAt("0", 0, 0);
			    table.setValueAt("50", 0, 1);
			    table.setValueAt("1", 0, 2);
			    
				Object rc = AlgUtils.reclassifyRaster(ts,table, null);
				
				setInitialStep(0);
				setNote("Vectorizando resultado");
				FLyrVect cuencas = (FLyrVect)AlgUtils.Vectorizar(rc, "");

                FLyrVect cuencasr = (FLyrVect)AlgUtils.RemoveItem(cuencas, "ID", "1", "");
                cuencasr.setName("Cuenca_Punto");
				AlgUtils.addLayertoView(cuencasr);
					 
					 ISymbol symb = cuencasr.getLegend().getDefaultSymbol();
					 IFillSymbol fillsymb = (IFillSymbol)symb;
					 Color col = new Color(254, 29, 49, 0);
					 col = GUIUtil.alphaColor(col, 40);
					 fillsymb.setFillColor(col); 		
					 
					 ILineSymbol linesymb = fillsymb.getOutline();
					 col = new Color(0, 0, 0, 255);
					 linesymb.setLineColor(col); 
					 fillsymb.setOutline(linesymb);
					
				  reportStep();
				
			} catch (GeoAlgorithmExecutionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
	    }
		
		public void finished() 
		{
		 reportStep();
		}
    }


	public void pointDoubleClick(PointEvent event) throws BehaviorException {
		
	}

	public boolean cancelDrawing() {
		return false;
	}

	public Cursor getCursor() {
		return m_Cursor;
	}

}
