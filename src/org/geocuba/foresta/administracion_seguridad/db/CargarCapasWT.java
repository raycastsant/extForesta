package org.geocuba.foresta.administracion_seguridad.db;

import java.awt.Color;
import java.io.FileNotFoundException;

import javax.swing.JOptionPane;

import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.geocuba.foresta.herramientas.utiles.XMLUtils;
import com.iver.andami.ConfigurationException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.symbols.IFillSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.ILineSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.IMarkerSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.gui.GUIUtil;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.NotExistInXMLEntity;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.swing.threads.AbstractMonitorableTask;

/**Metodo para cargar las capas desde un XMLEntity*/
public class CargarCapasWT extends AbstractMonitorableTask
{
 private XMLEntity xml;
 private CargadorCapas cargador;
 private View view;
 
	public CargarCapasWT(View _view)
    {
	 view = _view;
     
	 try {
			
		//iniGlogalConfigs(); //Cargo configuracion del xml
			
		//Cargando fichero de configuracion de capas
		        String sep = System.getProperty("file.separator");
		        String url = PluginServices.getPluginServices("org.geocuba.foresta").getPluginDirectory()+sep+"configForesta.xml";
				xml = XMLUtils.persistenceFromXML(url);
				int index = xml.firstIndexOfChild("name", "config_capas");
				xml = xml.getChild(index);
				
			} catch (ConfigurationException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "No se encontró el fichero de configuración del sistema 'configForesta.xml'.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			
		//PluginServices.getMDIManager().closeAllWindows(); //cierro el project manager
		//PluginServices.getMDIManager().addWindow(view);
		
		setInitialStep(0);
		setDeterminatedProcess(true);
		setStatusMessage(PluginServices.getText(this, "Cargando capa:"));
		setFinalStep((xml.getChildrenCount()*2)+1);	
    }
	
		public void run() throws Exception {
			  
			   FLyrVect l = null;	  
			   ISymbol symb = null;
			   ILineSymbol linesymb = null;
			   IFillSymbol fillsymb = null;
			   IMarkerSymbol pointsymb = null;
			   Color col = null;
			   
			   cargador = new CargadorCapas();
               FLyrVect [] layers = new FLyrVect[xml.getChildrenCount()];
				
				for(int i=0; i<xml.getChildrenCount() && !isCanceled(); i++)
				{
				  String table = xml.getChild(i).getStringProperty("tabla");
				  String layername = xml.getChild(i).getName();
				  String whereClause = xml.getChild(i).getStringProperty("whereClause");
				  
				  setNote(layername);
				  reportStep();
				  
				  int c1 = xml.getChild(i).getIntProperty("c1");
				  int c2 = xml.getChild(i).getIntProperty("c2");
				  int c3 = xml.getChild(i).getIntProperty("c3");
				  int transp = xml.getChild(i).getIntProperty("cTransp");
				  
				  l = cargador.cargarTabla(table, layername, i, whereClause, view.getProjection());
				  symb = l.getLegend().getDefaultSymbol();
				  
				  switch (l.getShapeType()) 
				  {
				   case FShape.POLYGON:{
					 fillsymb = (IFillSymbol)symb;
					   col = new Color(c1, c2, c3, 0);//f.getColor();
					   col = GUIUtil.alphaColor(col, transp);
					   fillsymb.setFillColor(col); 		
					   
					   linesymb = fillsymb.getOutline();
					   
					   try {
						      c1 = xml.getChild(i).getIntProperty("borderc1");
							  c2 = xml.getChild(i).getIntProperty("borderc2");
							  c3 = xml.getChild(i).getIntProperty("borderc3");
							  int borderwith = xml.getChild(i).getIntProperty("borderwith");
							  col = new Color(c1, c2, c3, 0);
							  col = GUIUtil.alphaColor(col, 255);
							  linesymb.setLineColor(col);
							  linesymb.setLineWidth(borderwith);
							  fillsymb.setOutline(linesymb);
							  
					    } catch (NotExistInXMLEntity e) {
						   col = new Color(0, 0, 0, 0);
						   col = GUIUtil.alphaColor(col, 255);
						   linesymb.setLineColor(col);
						   fillsymb.setOutline(linesymb);
					     }
					   
					   break;
				    }
				   case FShape.LINE:{
					   linesymb = (ILineSymbol)symb;
				       col = new Color(c1, c2, c3, 0);
				       col = GUIUtil.alphaColor(col, transp);
				       linesymb.setLineColor(col); 	
						   
						   break;
					    }
				   case FShape.POINT:{
					       pointsymb = (IMarkerSymbol)symb;
					       col = new Color(c1, c2, c3, 0);
					       col = GUIUtil.alphaColor(col, transp);
					       pointsymb.setColor(col); 	
						   
						   break;
					    }
				   case FShape.MULTIPOINT:{
					   pointsymb = (IMarkerSymbol)symb;
				       col = new Color(c1, c2, c3, 0);
				       col = GUIUtil.alphaColor(col, transp);
				       pointsymb.setColor(col); 	
					   
					   break;
				    }
    				default:
					break;
				}
				  
				  layers[i] = l;
				 }
				
//Agrego las capas a la vista
 view.getMapControl().getMapContext().beginAtomicEvent();
 
 /*setNote("Agregando "+Global.mdtLayername+" a la vista");	
 reportStep();
 
 FLayer mdt = IOUtils.loadRaster(Global.mdtURL, Global.mdtLayername, view.getProjection()); //Cargo mdt
 view.getMapControl().getMapContext().getLayers().addLayer(mdt);*/
	
 for(int i=0 ;i<layers.length && !isCanceled(); i++)
 {
  String name = layers[i].getName(); 	 
  setNote("Agregando "+name+" a la vista");	
  reportStep();
  //layers[i].setInTOC(false);
  
  while(view.getMapControl().getMapContext().getLayers().getLayer(name) != null)
   view.getMapControl().getMapContext().getLayers().removeLayer(name);	  
  
  view.getMapControl().getMapContext().getLayers().addLayer(layers[i]);
  
  System.out.println("CAPA ---> "+name);
 }
 
 view.getMapControl().getMapContext().endAtomicEvent();
 reportStep();
}
		
 public void finished() 
 {
  if(!isCanceled())
  {
	  PluginServices.getMDIManager().addWindow(view);	
	  PluginServices.getMainFrame().enableControls();
//	  view.getTOC().setSize(250, view.getTOC().getHeight());
//	  view.getTOC().refresh();
  }
 } 

 /*private void iniGlogalConfigs() throws ConfigurationException
 {
		xml = XMLUtils.persistenceFromXML(Global.configsURL);
		int index = xml.firstIndexOfChild("name", "globalConfigs");
		xml = xml.getChild(index);
		
		Global.PostgresSQLBinPath = xml.getStringProperty("PostgresSQLBinPath");
		System.out.println(Global.PostgresSQLBinPath);
		Global.PostgresUser = xml.getStringProperty("PostgresUser");
		System.out.println(Global.PostgresUser);
		Global.PostgresPw = xml.getStringProperty("PostgresPw");
		System.out.println(Global.PostgresPw);
		Global.PostgresServer = xml.getStringProperty("PostgresServer");
		System.out.println(Global.PostgresServer);
		Global.PostgresPort = xml.getStringProperty("PostgresPort");
		System.out.println(Global.PostgresPort);
		Global.PostgresDBName = xml.getStringProperty("PostgresDBName");
		System.out.println(Global.PostgresDBName);
		Global.projection = xml.getStringProperty("projection");
		System.out.println(Global.projection);
		Global.configsURL = xml.getStringProperty("configsURL");
		System.out.println(Global.configsURL);
		Global.mdtURL = xml.getStringProperty("mdtURL");
		System.out.println(Global.mdtURL);
		
		Global.PostgresURL = "jdbc:postgresql://"+Global.PostgresServer+":"+Global.PostgresPort+"/"+Global.PostgresDBName;		
		System.out.println(Global.PostgresURL);
}*/

}
