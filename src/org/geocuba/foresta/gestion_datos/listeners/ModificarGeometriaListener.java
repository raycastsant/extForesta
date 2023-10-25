package org.geocuba.foresta.gestion_datos.listeners;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.geocuba.foresta.gestion_datos.PersistentGeometricObject;
import org.geocuba.foresta.gestion_datos.extensiones.ModificarGeometriaExtension;
import org.geocuba.foresta.gestion_datos.gui.IPanelManager;
import org.geocuba.foresta.gestion_datos.gui.pGestionManager;
import org.geocuba.foresta.gestion_datos.gui.pGestionProyectosManager;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.ViewPort;
import com.iver.cit.gvsig.fmap.core.GeneralPathX;
import com.iver.cit.gvsig.fmap.core.Handler;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.ShapeFactory;
import com.iver.cit.gvsig.fmap.edition.AnnotationEditableAdapter;
import com.iver.cit.gvsig.fmap.edition.DefaultRowEdited;
import com.iver.cit.gvsig.fmap.edition.IRowEdited;
import com.iver.cit.gvsig.fmap.edition.UtilFunctions;
import com.iver.cit.gvsig.fmap.edition.VectorialEditableAdapter;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.gui.cad.DefaultCADTool;
import com.iver.cit.gvsig.gui.cad.tools.SelectionCADTool;
import com.iver.cit.gvsig.gui.cad.tools.smc.SelectionCADToolContext.SelectionCADToolState;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;

public class ModificarGeometriaListener extends SelectionCADTool
{
 private int gidGeom;
 private IPanelManager panelmanager;
 private FLyrVect capa;
 //private IGeometry geometry;
 
 public ModificarGeometriaListener(int _gidGeom, IPanelManager _panelmanager, FLyrVect _capa)
 {
  gidGeom = _gidGeom;	 
  panelmanager = _panelmanager;
  capa = _capa;
  
  ModificarGeometriaExtension.setModificarCADTool(this);
 }
 
 public void drawOperation(Graphics g, double x, double y) 
 {
	 //Valido la seleccion
		String selected = Funciones_Utiles.GetSelectedGids(capa);
		if(!selected.equals("") && selected.indexOf(",")<0)
		{
		 int gid = Integer.parseInt(selected);
		 if( gid != gidGeom)
		  return;	 
		}
		else
		 return;	

		panelmanager.HabilitarGuardado();
		
		SelectionCADToolState actualState = _fsm.getState();
		String status = actualState.getName();
		VectorialLayerEdited vle = getVLE();
		if (vle == null) return;
		ArrayList selectedHandler = vle.getSelectedHandler();
		ViewPort vp=vle.getLayer().getMapContext().getViewPort();
		if (status.equals("Selection.SecondPoint")) {
			// Dibuja el rectángulo de selección
			GeneralPathX elShape = new GeneralPathX(GeneralPathX.WIND_EVEN_ODD,
					4);
			elShape.moveTo(firstPoint.getX(), firstPoint.getY());
			elShape.lineTo(x, firstPoint.getY());
			elShape.lineTo(x, y);
			elShape.lineTo(firstPoint.getX(), y);
			elShape.lineTo(firstPoint.getX(), firstPoint.getY());
			ShapeFactory.createPolyline2D(elShape).draw((Graphics2D) g,
					vp,
					DefaultCADTool.geometrySelectSymbol);
			Image img = vle.getSelectionImage();
	        g.drawImage(img, 0, 0, null);
	        return;
		}else if (status.equals("Selection.WithHandlers")) 
		{
			// Movemos los handlers que hemos seleccionado
			// previamente dentro del método select()
			
			double xPrev=0;
			double yPrev=0;
			for (int k = 0; k < selectedHandler.size(); k++) 
			{
				Handler h = (Handler)selectedHandler.get(k);
				xPrev=h.getPoint().getX();
				yPrev=h.getPoint().getY();
				h.set(x, y);
		    }
			// Y una vez movidos los vértices (handles)
			// redibujamos la nueva geometría.
			for (int i = 0; i < rowselectedHandlers.size(); i++) {
				IRowEdited rowEd = (IRowEdited) rowselectedHandlers.get(i);
				IGeometry geom = ((IFeature) rowEd.getLinkedRow())
						.getGeometry().cloneGeometry();
				g.setColor(Color.gray);
				geom.draw((Graphics2D) g, vp, DefaultCADTool.axisReferencesSymbol);
				
				//geometry = geom;
				pGestionManager.setCurrentEditedGeometry(geom);
			}
			for (int k = 0; k < selectedHandler.size(); k++) {
				Handler h = (Handler)selectedHandler.get(k);
				h.set(xPrev, yPrev);
			}
			return;
		}else{
			if (!vle.getLayer().isVisible())
				return;
			try{
			Image imgSel = vle.getSelectionImage();
	        if (imgSel!=null)
			g.drawImage(imgSel, 0, 0, null);
	        Image imgHand = vle.getHandlersImage();
	        if (imgHand!=null)
			g.drawImage(imgHand, 0, 0, null);
			}catch (Exception e) {
			}
		}
	}
 
 public void addPoint(double x, double y, InputEvent event) {
		if (event!=null && ((MouseEvent)event).getClickCount()==2){
			try {
				pointDoubleClick((MapControl)event.getComponent());
			} catch (ReadDriverException e) {
				NotificationManager.addError(e.getMessage(),e);
			}
			return;
		}
		SelectionCADToolState actualState = (SelectionCADToolState) _fsm
				.getPreviousState();
		String status = actualState.getName();
		System.out.println("PREVIOUSSTATE =" + status); // + "ESTADO ACTUAL: " +
														// _fsm.getState());
		VectorialLayerEdited vle = getVLE();
		VectorialEditableAdapter vea=vle.getVEA();
		ArrayList selectedHandler = vle.getSelectedHandler();
		ArrayList selectedRow = vle.getSelectedRow();
		System.out.println("STATUS ACTUAL = " + _fsm.getTransition());
		if (status.equals("Selection.FirstPoint")) {
			firstPoint=new Point2D.Double(x,y);
			pointsPolygon.add(firstPoint);
		} else if (status.equals("Selection.SecondPoint")) {
		} else if (status.equals("Selection.WithFeatures")) {
		} else if (status.equals("Selection.WithHandlers")) {
			vea.startComplexRow();
			ArrayList selectedRowsAux=new ArrayList();
			for (int i = 0; i < selectedRow.size(); i++) {
				IRowEdited row = (IRowEdited) selectedRow.get(i);
				IFeature feat = (IFeature) row.getLinkedRow().cloneRow();
				IGeometry ig = feat.getGeometry();
				if (vea instanceof AnnotationEditableAdapter) {
					// Movemos la geometría
                 UtilFunctions.moveGeom(ig, x -
                         firstPoint.getX(), y - firstPoint.getY());
				}else {
					// Movemos los handlers que hemos seleccionado
					// previamente dentro del método select()
					Handler[] handlers=ig.getHandlers(IGeometry.SELECTHANDLER);
					for (int k = 0; k < selectedHandler.size(); k++) {
						Handler h = (Handler)selectedHandler.get(k);
						for (int j=0;j<handlers.length;j++) {
							if (h.getPoint().equals(handlers[j].getPoint()))
								handlers[j].set(x,y);
						}
					}
				}
				modifyFeature(row.getIndex(), feat);
				selectedRowsAux.add(new DefaultRowEdited(feat,IRowEdited.STATUS_MODIFIED,row.getIndex()));
				
//				geometry = feat.getGeometry();
				pGestionManager.setCurrentEditedGeometry(feat.getGeometry());
				
			}
			firstPoint=new Point2D.Double(x,y);
			vle.setSelectionCache(VectorialLayerEdited.SAVEPREVIOUS, selectedRowsAux);
			//clearSelection();
			//selectedRow.addAll(selectedRowsAux);
			String description=PluginServices.getText(this,"move_handlers");
			vea.endComplexRow(description);
			
		}
	}
 
// public IGeometry sgetFinalGeometry()
// {
//  return geometry;	 
// }
 
 public String getName() {
     return "Foresta_Edicion";
 }

}
