package org.geocuba.foresta.gestion_datos.listeners;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.InputEvent;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import org.geocuba.foresta.gestion_datos.gui.pGestionManager;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileWriteException;
import com.iver.cit.gvsig.exceptions.validate.ValidateRowException;
import com.iver.cit.gvsig.fmap.ViewPort;
import com.iver.cit.gvsig.fmap.core.DefaultFeature;
import com.iver.cit.gvsig.fmap.core.FGeometryCollection;
import com.iver.cit.gvsig.fmap.core.FPoint2D;
import com.iver.cit.gvsig.fmap.core.FPolygon2D;
import com.iver.cit.gvsig.fmap.core.FPolyline2D;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.GeneralPathX;
import com.iver.cit.gvsig.fmap.core.Handler;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.IRow;
import com.iver.cit.gvsig.fmap.core.ShapeFactory;
import com.iver.cit.gvsig.fmap.core.v02.FConverter;
import com.iver.cit.gvsig.fmap.core.v02.FGraphicUtilities;
import com.iver.cit.gvsig.fmap.edition.DefaultRowEdited;
import com.iver.cit.gvsig.fmap.edition.EditionEvent;
import com.iver.cit.gvsig.fmap.edition.IRowEdited;
import com.iver.cit.gvsig.fmap.edition.VectorialEditableAdapter;
import com.iver.cit.gvsig.gui.cad.DefaultCADTool;
import com.iver.cit.gvsig.gui.cad.exception.CommandException;
import com.iver.cit.gvsig.gui.cad.tools.EditVertexCADTool;
import com.iver.cit.gvsig.gui.cad.tools.SelectionCADTool;
import com.iver.cit.gvsig.gui.cad.tools.smc.EditVertexCADToolContext;
import com.iver.cit.gvsig.gui.cad.tools.smc.EditVertexCADToolContext.EditVertexCADToolState;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;

public class EditarVerticeCADTool extends EditVertexCADTool 
{
    private EditVertexCADToolContext _fsm;
    private int numSelect=0;
	private int numHandlers;
	private boolean addVertex=false;
    
    public EditarVerticeCADTool() {
    }

    public void init() {
        _fsm = new EditVertexCADToolContext(this);
    }

    public void transition(double x, double y, InputEvent event) {
        _fsm.addPoint(x, y, event);
    }

    public void transition(double d) {
    	_fsm.addValue(d);
    }

    public void transition(String s) throws CommandException {
    	if (!super.changeCommand(s)){
    		_fsm.addOption(s);
    	}
    }

    /**
     * DOCUMENT ME!
     */
    public void selection() {
    	ArrayList selectedRow=getSelectedRows();
        if (selectedRow.size() == 0 && !CADExtension.getCADTool().getClass().getName().equals("org.geocuba.foresta.gestion_datos.listeners.ModificarGeometriaListener")) 
        {
            CADExtension.setCADTool("modificarGEom", false);
            ((ModificarGeometriaListener) CADExtension.getCADTool()).setNextTool("_editarvertice");
        }
    }

    /**
     * Equivale al transition del prototipo pero sin pasarle como parámetro el
     * editableFeatureSource que ya estará creado.
     *
     * @param x parámetro x del punto que se pase en esta transición.
     * @param y parámetro y del punto que se pase en esta transición.
     */
    public void addPoint(double x, double y,InputEvent event) 
    {
    	selectHandler(x,y);
    	addVertex=false;
    }

    private IGeometry getSelectedGeometry() 
    {
        ArrayList selectedRows=getSelectedRows();
//    	VectorialEditableAdapter vea = getCadToolAdapter().getVectorialAdapter();
//        FBitSet selection = vea.getSelection();
        IRowEdited row=null;
        IGeometry ig=null;
        if (selectedRows.size()==1){
			row=(DefaultRowEdited) selectedRows.get(0);
				//row = getCadToolAdapter().getVectorialAdapter().getRow(selection.nextSetBit(0));
			ig=((IFeature)row.getLinkedRow()).getGeometry().cloneGeometry();
        	return ig;
        }

		return null;
	}

	/**
     * Método para dibujar la lo necesario para el estado en el que nos
     * encontremos.
     *
     * @param g Graphics sobre el que dibujar.
     * @param x parámetro x del punto que se pase para dibujar.
     * @param y parámetro x del punto que se pase para dibujar.
     */
    public void drawOperation(Graphics g, double x, double y) {
        drawVertex(g,getCadToolAdapter().getMapControl().getViewPort());
    }

    /**
     * Add a diferent option.
     *
     * @param s Diferent option.
     */
    public void addOption(String s) {
    	EditVertexCADToolState actualState = (EditVertexCADToolState) _fsm.getPreviousState();
        String status = actualState.getName();
        VectorialLayerEdited vle=getVLE();
        VectorialEditableAdapter vea = vle.getVEA();
        ArrayList selectedRows=vle.getSelectedRow();
        IRowEdited row=null;
        IGeometry ig=null;
        Handler[] handlers=null;
        if (selectedRows.size()==1){
				row =  (DefaultRowEdited) selectedRows.get(0);
			ig=((IFeature)row.getLinkedRow()).getGeometry().cloneGeometry();
        	handlers=ig.getHandlers(IGeometry.SELECTHANDLER);
        	numHandlers=handlers.length;
        	if (numHandlers ==0){
        		try {
					vea.removeRow(row.getIndex(),getName(),EditionEvent.GRAPHIC);
				} catch (ReadDriverException e) {
					NotificationManager.addError(e.getMessage(),e);
				}
        	}
        }else{
        	JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),PluginServices.getText(this,"hay_mas_de_una_geometria_seleccionada"));
        }
        int dif=1;//En el caso de ser polígono.
        if (ig instanceof FGeometryCollection){
        	dif=2;
        }
        if (status.equals("EditVertex.SelectVertexOrDelete")){
        	if(s.equalsIgnoreCase(PluginServices.getText(this,"EditVertexCADTool.nextvertex")) || s.equals(PluginServices.getText(this,"next"))){
        		numSelect=numSelect-dif;
        		if (numSelect<0){
        			numSelect=numHandlers-1+(numSelect+1);
        		}
           }else if(s.equalsIgnoreCase(PluginServices.getText(this,"EditVertexCADTool.previousvertex")) || s.equals(PluginServices.getText(this,"previous"))){
        	   	numSelect=numSelect+dif;
       			if (numSelect>(numHandlers-1)){
       				numSelect=numSelect-(numHandlers);
       			}

        	}else if(s.equalsIgnoreCase(PluginServices.getText(this,"EditVertexCADTool.delvertex")) || s.equals(PluginServices.getText(this,"del"))){
        		if (handlers!=null){
        			IGeometry newGeometry=null;
        			if (ig instanceof FGeometryCollection) {
        				newGeometry=removeVertexGC((FGeometryCollection)ig,handlers[numSelect]);
        			}else {
        				newGeometry=removeVertex(ig,handlers,numSelect);
        			}
        			//numSelect=0;

        			IRow newRow=new DefaultFeature(newGeometry,row.getAttributes(),row.getID());
        			
        			pGestionManager.setCurrentEditedGeometry(newGeometry);
        			
        			try {
						vea.modifyRow(row.getIndex(),newRow,getName(),EditionEvent.GRAPHIC);
						clearSelection();
        			} catch (ValidateRowException e) {
        				NotificationManager.addError(e.getMessage(),e);
					} catch (ExpansionFileWriteException e) {
						NotificationManager.addError(e.getMessage(),e);
					} catch (ReadDriverException e) {
						NotificationManager.addError(e.getMessage(),e);
					}
					vle.addSelectionCache(new DefaultRowEdited(newRow,
							IRowEdited.STATUS_MODIFIED, row.getIndex()));

//					vle.refreshSelectionCache(new Point2D.Double(0,0),getCadToolAdapter());
//					refresh();

        		}
        	}else if(s.equalsIgnoreCase(PluginServices.getText(this,"EditVertexCADTool.addvertex")) || s.equals(PluginServices.getText(this,"add"))){
            		addVertex=true;
            	}
        }
    }
    private void drawVertex(Graphics g,ViewPort vp){
		ArrayList selectedRows=getSelectedRows();
    	for (int i = 0; i<selectedRows.size();
		 		i++) {
    		DefaultFeature fea = (DefaultFeature) ((DefaultRowEdited) selectedRows
					.get(i)).getLinkedRow();
			IGeometry ig = fea.getGeometry().cloneGeometry();
			if (ig == null) continue;
			ig.drawInts((Graphics2D)g,vp,DefaultCADTool.geometrySelectSymbol);
			Handler[] handlers=ig.getHandlers(IGeometry.SELECTHANDLER);
			if (numSelect>=handlers.length)
				numSelect=0;
			if (handlers.length==0)
				continue;
			FGraphicUtilities.DrawVertex((Graphics2D)g,vp.getAffineTransform(),handlers[numSelect]);
		}
	}

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.CADTool#addvalue(double)
     */
    public void addValue(double d) {
    }
    private IGeometry removeVertex(IGeometry gp,Handler[] handlers,int numHandler) {
        GeneralPathX newGp = new GeneralPathX();
        double[] theData = new double[6];

        PathIterator theIterator;
        int theType;
        int numParts = 0;

        Point2D ptSrc = new Point2D.Double();
        boolean bFirst = false;

        theIterator = gp.getPathIterator(null, FConverter.FLATNESS);
        int numSegmentsAdded = 0;
        while (!theIterator.isDone()) {
            theType = theIterator.currentSegment(theData);
            if (bFirst){
        		newGp.moveTo(theData[0], theData[1]);
        		numSegmentsAdded++;
        		bFirst=false;
        		theIterator.next();
        		continue;
        	}
            switch (theType) {

                case PathIterator.SEG_MOVETO:
                    numParts++;
                    ptSrc.setLocation(theData[0], theData[1]);
                    if (ptSrc.equals(handlers[numHandler].getPoint())){
                    	numParts--;
                    	bFirst=true;
                    	break;
                    }
                    newGp.moveTo(ptSrc.getX(), ptSrc.getY());
                    numSegmentsAdded++;
                    bFirst = false;
                    break;

                case PathIterator.SEG_LINETO:
                    ptSrc.setLocation(theData[0], theData[1]);
                    if (ptSrc.equals(handlers[numHandler].getPoint())){
                    	break;
                    }
                    newGp.lineTo(ptSrc.getX(), ptSrc.getY());
                    bFirst = false;
                    numSegmentsAdded++;
                    break;

                case PathIterator.SEG_QUADTO:
                    newGp.quadTo(theData[0], theData[1], theData[2], theData[3]);
                    numSegmentsAdded++;
                    break;

                case PathIterator.SEG_CUBICTO:
                    newGp.curveTo(theData[0], theData[1], theData[2], theData[3], theData[4], theData[5]);
                    numSegmentsAdded++;
                    break;

                case PathIterator.SEG_CLOSE:
                    if (numSegmentsAdded < 3)
                        newGp.lineTo(theData[0], theData[1]);
                    newGp.closePath();

                    break;
            } //end switch

            theIterator.next();
        } //end while loop
        FShape shp = null;
        switch (gp.getGeometryType())
        {
            case FShape.POINT: //Tipo punto
            case FShape.POINT + FShape.Z:
                shp = new FPoint2D(ptSrc.getX(), ptSrc.getY());
                break;

            case FShape.LINE:
            case FShape.LINE + FShape.Z:
                shp = new FPolyline2D(newGp);
                break;
            case FShape.POLYGON:
            case FShape.POLYGON + FShape.Z:
                shp = new FPolygon2D(newGp);
                break;
        }
        IGeometry ig=ShapeFactory.createGeometry(shp);
        int dif=1;//En el caso de ser polígono.
       	numSelect=numSelect-dif;
		if (numSelect<0){
			numSelect=numHandlers-1+(numSelect+1);
		}
        return ig;
    }

    private IGeometry removeVertexGC(FGeometryCollection gc,Handler handler) {
        IGeometry[] geoms=gc.getGeometries();
    	ArrayList geomsAux=new ArrayList();
        int pos=-1;
    	for (int i=0;i<geoms.length;i++) {
    		Handler[] handlers=geoms[i].getHandlers(IGeometry.SELECTHANDLER);
    		for (int j=0;j<handlers.length;j++) {
    			if (handlers[j].equalsPoint(handler)) {
    				geomsAux.add(geoms[i]);
    				if (pos==-1)
    					pos=i;
    			}
    		}
    	}
    	int numGeomsAux=geomsAux.size();
    	GeneralPathX gpx=new GeneralPathX();
        for (int i=0;i<numGeomsAux;i++) {
    		Handler[] handlers=((IGeometry)geomsAux.get(i)).getHandlers(IGeometry.SELECTHANDLER);
    		if (numGeomsAux == 2) {
				for (int j = 0; j < handlers.length; j++) {
					if (handlers[j].equalsPoint(handler)) {
						if (j == (handlers.length - 1)) {
							Point2D ph = handlers[0].getPoint();
							gpx.moveTo(ph.getX(), ph.getY());
						} else {
							Point2D ph = handlers[handlers.length - 1]
									.getPoint();
							gpx.lineTo(ph.getX(), ph.getY());
						}
					}
				}
			}

    	}
        ArrayList newGeoms=new ArrayList();
        for (int i=0;i<pos;i++) {
        	newGeoms.add(geoms[i]);
        }
        newGeoms.add(ShapeFactory.createPolyline2D(gpx));
        for (int i=pos+numGeomsAux;i<geoms.length;i++) {
        	newGeoms.add(geoms[i]);
        }

    	return new FGeometryCollection((IGeometry[])newGeoms.toArray(new IGeometry[0]));
    }



    private IGeometry addVertex(IGeometry geome,Point2D p,Rectangle2D rect) {
    	IGeometry geometryCloned=geome.cloneGeometry();
    	IGeometry geom1=null;
    	GeneralPathX gpxAux;
    	boolean finish=false;
    	//FGeometry geom2=null;

    	//if (geometryCloned.getGeometryType() == FShape.POLYGON){
    		/////////////////

    		GeneralPathX newGp = new GeneralPathX();
            double[] theData = new double[6];

            PathIterator theIterator;
            int theType;
            int numParts = 0;
            Point2D pLast=new Point2D.Double();
            Point2D pAnt = new Point2D.Double();
            Point2D firstPoint=null;
            theIterator = geome.getPathIterator(null,FConverter.FLATNESS); //, flatness);
            int numSegmentsAdded = 0;
            while (!theIterator.isDone()) {
                theType = theIterator.currentSegment(theData);
                switch (theType) {
                    case PathIterator.SEG_MOVETO:
                    	pLast.setLocation(theData[0], theData[1]);
                    	if (numParts==0)
                    		firstPoint=(Point2D)pLast.clone();
                    	numParts++;

                    	gpxAux=new GeneralPathX();
                    	gpxAux.moveTo(pAnt.getX(),pAnt.getY());
                    	gpxAux.lineTo(pLast.getX(),pLast.getY());
                    	geom1=ShapeFactory.createPolyline2D(gpxAux);
                    	if (geom1.intersects(rect)){
                    		finish=true;
                    		newGp.moveTo(pLast.getX(), pLast.getY());
                    		//newGp.lineTo(pLast.getX(),pLast.getY());
                    	}else{
                    		newGp.moveTo(pLast.getX(), pLast.getY());
                    	}
                        pAnt.setLocation(pLast.getX(), pLast.getY());
                        numSegmentsAdded++;
                        break;

                    case PathIterator.SEG_LINETO:
                    	pLast.setLocation(theData[0], theData[1]);
                    	gpxAux=new GeneralPathX();
                    	gpxAux.moveTo(pAnt.getX(),pAnt.getY());
                    	gpxAux.lineTo(pLast.getX(),pLast.getY());
                    	geom1=ShapeFactory.createPolyline2D(gpxAux);
                    	if (geom1.intersects(rect)){
                    		newGp.lineTo(p.getX(), p.getY());
                    		newGp.lineTo(pLast.getX(),pLast.getY());
                    	}else{
                    		newGp.lineTo(pLast.getX(), pLast.getY());
                    	}
                    	pAnt.setLocation(pLast.getX(), pLast.getY());
                        numSegmentsAdded++;
                        break;

                    case PathIterator.SEG_QUADTO:
                        newGp.quadTo(theData[0], theData[1], theData[2], theData[3]);
                        numSegmentsAdded++;
                        break;

                    case PathIterator.SEG_CUBICTO:
                        newGp.curveTo(theData[0], theData[1], theData[2], theData[3], theData[4], theData[5]);
                        numSegmentsAdded++;
                        break;

                    case PathIterator.SEG_CLOSE:
                        //if (numSegmentsAdded < 3){
                        	gpxAux=new GeneralPathX();
                        	gpxAux.moveTo(pAnt.getX(),pAnt.getY());
                        	gpxAux.lineTo(firstPoint.getX(),firstPoint.getY());
                        	geom1=ShapeFactory.createPolyline2D(gpxAux);
                        	if (geom1.intersects(rect)|| finish){
                        		newGp.lineTo(p.getX(), p.getY());
                        		newGp.lineTo(pLast.getX(),pLast.getY());
                        	}else{
                        		newGp.lineTo(pLast.getX(), pLast.getY());
                        	}
                        //}
                        newGp.closePath();
                        break;
                } //end switch

                theIterator.next();
            } //end while loop
            FShape shp = null;
            switch (geometryCloned.getGeometryType())
            {
                case FShape.POINT: //Tipo punto
                case FShape.POINT + FShape.Z:
                    shp = new FPoint2D(pLast.getX(), pLast.getY());
                    break;

                case FShape.LINE:
                case FShape.LINE + FShape.Z:
                    shp = new FPolyline2D(newGp);
                    break;
                case FShape.POLYGON:
                case FShape.POLYGON + FShape.Z:
                case FShape.CIRCLE:
                case FShape.ELLIPSE:
                    shp = new FPolygon2D(newGp);
                    break;
            }
            return ShapeFactory.createGeometry(shp);


    		/////////////////////
    	//}else if (geometryCloned.getGeometryType() == FShape.LINE){

    	//}




    }
    private IGeometry addVertexGC(FGeometryCollection gc,Point2D p,Rectangle2D rect) {
    	IGeometry[] geoms=gc.getGeometries();
    	int pos=-1;
    	for (int i=0;i<geoms.length;i++) {
    		if (geoms[i].intersects(rect)) {
    			pos=i;
    		}
    	}
    	ArrayList newGeoms=new ArrayList();
    	for (int i=0;i<pos;i++) {
    		newGeoms.add(geoms[i]);
    	}
    	if (pos!=-1) {
    	GeneralPathX gpx1=new GeneralPathX();
    	GeneralPathX gpx2=new GeneralPathX();
    	Handler[] handlers=geoms[pos].getHandlers(IGeometry.SELECTHANDLER);
    	Point2D p1=handlers[0].getPoint();
    	Point2D p2=p;
    	Point2D p3=handlers[handlers.length-1].getPoint();
    	gpx1.moveTo(p1.getX(),p1.getY());
    	gpx1.lineTo(p2.getX(),p2.getY());
    	gpx2.moveTo(p2.getX(),p2.getY());
    	gpx2.lineTo(p3.getX(),p3.getY());
    	newGeoms.add(ShapeFactory.createPolyline2D(gpx1));
    	newGeoms.add(ShapeFactory.createPolyline2D(gpx2));
    	for (int i=pos+1;i<geoms.length;i++) {
    		newGeoms.add(geoms[i]);
    	}
    	return new FGeometryCollection((IGeometry[])newGeoms.toArray(new IGeometry[0]));
    	}else {
    		return null;
    	}
    }
	public String getName() {
		return PluginServices.getText(this,"edit_vertex_");
	}
	private void selectHandler(double x, double y) {
		Point2D firstPoint = new Point2D.Double(x, y);
		VectorialLayerEdited vle = getVLE();
		VectorialEditableAdapter vea=vle.getVEA();
		ArrayList selectedRows = vle.getSelectedRow();
		double tam = getCadToolAdapter().getMapControl().getViewPort()
				.toMapDistance(SelectionCADTool.tolerance);
		Rectangle2D rect = new Rectangle2D.Double(firstPoint.getX() - tam,
				firstPoint.getY() - tam, tam * 2, tam * 2);
		if (selectedRows.size() > 0) {
			boolean isSelectedHandler = false;
			IGeometry geometry = getSelectedGeometry();
			if (geometry != null) {
				Handler[] handlers = geometry
						.getHandlers(IGeometry.SELECTHANDLER);
				for (int h = 0; h < handlers.length; h++) {
					if (handlers[h].getPoint().distance(firstPoint) < tam) {
						numSelect = h;
						isSelectedHandler = true;
					}
				}

				if (!isSelectedHandler) {
					boolean isSelectedGeometry = false;
					try {

							if (geometry.intersects(rect)) { 
								isSelectedGeometry = true;
							}
						
						if (isSelectedGeometry && addVertex) {
							selectedRows = getSelectedRows();
							DefaultFeature fea = null;
							DefaultRowEdited row = null;
							row = (DefaultRowEdited) selectedRows.get(0);
							fea = (DefaultFeature) row.getLinkedRow();
							Point2D posVertex = new Point2D.Double(x, y);
							IGeometry geom1=fea.getGeometry().cloneGeometry();
							IGeometry geom=null;
							if (geom1 instanceof FGeometryCollection) {
								geom = addVertexGC((FGeometryCollection)geom1, posVertex, rect);
							}else {
								geom = addVertex(geom1, posVertex, rect);
							}
							if (geom!=null) {
							DefaultFeature df = new DefaultFeature(geom, fea
									.getAttributes(),row.getID());
							
							pGestionManager.setCurrentEditedGeometry(geom);
							
							vea.modifyRow(row.getIndex(), df,
									PluginServices.getText(this,"add_vertex"),EditionEvent.GRAPHIC);

							Handler[] newHandlers = geom
									.getHandlers(IGeometry.SELECTHANDLER);
							for (int h = 0; h < newHandlers.length; h++) {
								if (newHandlers[h].getPoint().distance(
										posVertex) < tam) {
									numSelect = h;
									isSelectedHandler = true;
								}
							}

							clearSelection();
							vle.addSelectionCache(new DefaultRowEdited(df,
									IRowEdited.STATUS_MODIFIED, row.getIndex()));
							}
						}
					} catch (ValidateRowException e) {
						NotificationManager.addError(e.getMessage(),e);
					} catch (ExpansionFileWriteException e) {
						NotificationManager.addError(e.getMessage(),e);
					} catch (ReadDriverException e) {
						NotificationManager.addError(e.getMessage(),e);
					}
				}
			}
		}

	}

	public String toString() {
		return "_editvertex";
	}

	public boolean isApplicable(int shapeType) {
		switch (shapeType) {
		case FShape.POINT:
		case FShape.MULTIPOINT:
			return false;
		}
		return true;
	}


}
