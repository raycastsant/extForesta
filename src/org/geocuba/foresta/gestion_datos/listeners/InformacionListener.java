package org.geocuba.foresta.gestion_datos.listeners;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import javax.swing.JOptionPane;
import org.geocuba.foresta.gestion_datos.IPersistenObjectManager;
import org.geocuba.foresta.gestion_datos.PersistentObject;
import org.geocuba.foresta.gestion_datos.extensiones.InfoExtension;
import org.geocuba.foresta.gestion_datos.gui.IPanelManager;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.exceptions.visitors.VisitorException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FBitSet;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.fmap.tools.BehaviorException;
import com.iver.cit.gvsig.fmap.tools.Events.PointEvent;
import com.iver.cit.gvsig.fmap.tools.Listeners.PointListener;

public class InformacionListener implements PointListener {
	
	private final Image img = PluginServices.getIconTheme().get("cursor_information").getImage();
	private Cursor cur = Toolkit.getDefaultToolkit().createCustomCursor(img, new Point(16, 16), "");
	private MapControl mapCtrl;
	
	public InformacionListener(MapControl mc) 
	{
		this.mapCtrl = mc;
	}

	public void point(PointEvent event) throws BehaviorException 
	{
		PluginServices.getMDIManager().setWaitCursor();
		
		FLyrVect layer = (FLyrVect)mapCtrl.getMapContext().getLayers().getActives()[0];
		
		Point2D p = event.getPoint();
		Point2D mapPoint = mapCtrl.getViewPort().toMapPoint((int) p.getX(), (int) p.getY());

		// Tolerancia de 3 pixels
		double tol = mapCtrl.getViewPort().toMapDistance(3);

				try {
					
					FBitSet newBitSet = layer.queryByPoint(mapPoint, tol);					
					layer.getRecordset().setSelection(newBitSet);
					SelectableDataSource ds = layer.getRecordset();
					FBitSet bs = layer.queryByPoint(mapPoint, tol);
					int fieldindex = ds.getFieldIndexByName("gid");
					
					for (int j=bs.nextSetBit(0); j >=0; j=bs.nextSetBit(j+1))
					{
					 int gid = Integer.parseInt(ds.getFieldValue(j, fieldindex).toString());
					 mostrar_Info(gid);
					}
					
				} catch (ReadDriverException e1) {
					e1.printStackTrace();
				}
				catch (VisitorException e1) {
					e1.printStackTrace();
				}
				
				PluginServices.getMDIManager().restoreCursor();
	}

	public Cursor getCursor() {
		return cur;
	}

	public boolean cancelDrawing() {
		return false;
	}

	public void pointDoubleClick(PointEvent event) throws BehaviorException {

	}
	
	private void mostrar_Info(int gid)
	{
		try {
			PersistentObject persistent = cargarObjeto(gid);
			if(persistent != null )
			{	
			 IPanelManager pm = mostrarFicha_Gestion(persistent);
			 pm.DeshabilitarComponentes();
			} 
			
		} catch (ReadDriverException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private PersistentObject cargarObjeto(int gid) throws InstantiationException, IllegalAccessException, ClassNotFoundException, ReadDriverException
	{
	     IPersistenObjectManager POM = (IPersistenObjectManager)Class.forName("org.geocuba.foresta.gestion_datos."+InfoExtension.getClasName()+"Manager").newInstance();
		 PersistentObject persistent = POM.Cargar_Objeto_BD(gid);
	
		 if(persistent == null)
		 {
		  PluginServices.getMDIManager().restoreCursor();
		  JOptionPane.showMessageDialog(null, "No se pudo cargar el objeto");
		  return null;
		 }
		 
		 PluginServices.getMDIManager().restoreCursor();
		 return persistent;
	}
	
	/**Muestra la ventana de gestion del elemento 
	 * seleccionado, el cual es un PersistentObject. En el caso de que sea un PersistentGeometricObject este 
	 * metodo se llamará desde el Listener de dibujar, después de que se haya dibujado.
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException */
	private IPanelManager mostrarFicha_Gestion(PersistentObject persistent) throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
	 /*Obtengo el manejador de la clase. La interfaz IPanelManager la deben implementar los managers de las ventanas de gestión de 
	  * cada clase persistente. Convenientemente y para que esto funcione, le pongo como nombre al manejador p+[nombreclase]+Manager. 
	  * Por ejemplo para la clase Cuenca el manejador de su ventana se llamaría pCuencaManager. El método MostrarPanel(persistent) de la 
	  * interfaz debe ser implementado por cada clase manejadora para mostrar el panel correspondiente.*/
		
	    IPanelManager pm = (IPanelManager)Class.forName("org.geocuba.foresta.gestion_datos.gui.p"+InfoExtension.getClasName()+"Manager").newInstance();
	    pm.MostrarPanel(persistent);
	 
	 return pm;
	}
}