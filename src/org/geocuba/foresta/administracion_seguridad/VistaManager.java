package org.geocuba.foresta.administracion_seguridad;

import org.geocuba.foresta.administracion_seguridad.db.CargarCapasWT;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.ProjectView;
import com.iver.cit.gvsig.project.documents.view.ProjectViewFactory;
import com.iver.cit.gvsig.project.documents.view.gui.View;

public class VistaManager 
{
  private static View v = null;
  
	public static void MostrarVista()
	{
	  if(v == null)
	   crearVista();	
	  
	  PluginServices.cancelableBackgroundExecution(new CargarCapasWT(v));	
	}
	
	public static void crearVista()
	{
		  ProjectExtension prext = (ProjectExtension)PluginServices.getExtension(ProjectExtension.class);
		  Project project = prext.getProject();
//		  ForestaProjectViewFactory foresta = new ForestaProjectViewFactory();
//		  ForestaProjectView vista = (ForestaProjectView)foresta.create(project);
		  
		  ProjectViewFactory PVF = new ProjectViewFactory();
		  ProjectView vista = (ProjectView)PVF.create(project);
		  vista.setProjection(CRSFactory.getCRS("EPSG:2085"));
		  ((ProjectDocument)vista).setName("Datos de FORESTA");
		  
		  v = (View)vista.createWindow();
	}
	
	public static void setVista(View vista)
	{
	 v = vista;	
	}
	
	public static View Vista()
	{
	 return v;
	}
	
	 public static View GetActiveView()
	 {
		 IWindow[] ws = PluginServices.getMDIManager().getAllWindows();
		  for (int i = 0; i < ws.length; i++)
		  {	  
		   if(ws[i] instanceof View)
			return (View)ws[i];	 
		  }
		   return null; 
	 } 
	 
	 /**Repinta el mapa*/
	 public static void RefreshView()
	 {
	  View vista = GetActiveView();
	  IProjectView model = vista.getModel();
	  MapContext mapa = model.getMapContext();
	  //mapa.getViewPort().setExtent(mapa.getLayers().getFullExtent());
	  mapa.clearErrors();
	  vista.repaintMap();
	  //Rectangle2D r2d = vista.getMapControl().getViewPort().getAdjustedExtent();

	 // try {
//	 	//mapa.getViewPort().setExtent(mapa.getFullExtent());
//	 	//mapa.getViewPort().setExtent(r2d);
	 //	
	 //} catch (ReadDriverException e) {
//	 	e.printStackTrace();
	 //}
	  //Zoom a todas las capas		
	  //mapa.getViewPort().setExtent(mapa.getLayers().getFullExtent());
	 }
}
