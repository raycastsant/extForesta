package org.geocuba.foresta.fajas.writerTasks;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.BitSet;
import javax.swing.JOptionPane;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.geocuba.foresta.administracion_seguridad.db.CargadorCapas;
import org.geocuba.foresta.administracion_seguridad.db.DBUtils;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.fajas.PerfilManager;
import org.geocuba.foresta.fajas.gui.pAncho_Calculos;
import org.geocuba.foresta.gestion_datos.Parteaguas;
import org.geocuba.foresta.gestion_datos.ParteaguasManager;
import org.geocuba.foresta.gestion_datos.PersistentGeometricObject;
import org.geocuba.foresta.gestion_datos.Rio;
import org.geocuba.foresta.gestion_datos.RioManager;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import org.geocuba.foresta.herramientas.utiles.ToggleEditing;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.StartEditing;
import com.iver.cit.gvsig.exceptions.layers.LegendLayerException;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.fmap.core.FPoint2D;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.GeneralPathX;
import com.iver.cit.gvsig.fmap.core.GeometryUtilities;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.ShapeFactory;
import com.iver.cit.gvsig.fmap.edition.UtilFunctions;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.geoprocess.core.util.GeometryUtil;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.swing.threads.AbstractMonitorableTask;
import com.vividsolutions.jts.geom.GeometryFactory;

import es.unex.sextante.core.Sextante;
import es.unex.sextante.exceptions.GeoAlgorithmExecutionException;

 /**Writer task para generar perfiles en hidrografia lineal*/
 public class Generar_Perfiles extends AbstractMonitorableTask //Perfiles
 {
	private boolean _showEditingLines;
	private double _dist;
	private boolean result = false;
	private FLyrVect rios;
	private FLyrVect parteaguas;
      
	public Generar_Perfiles(double dist, String menss, FLyrVect _rios, FLyrVect _parteaguas, boolean showEditingLines)
	{
		_dist = dist;
		_showEditingLines = showEditingLines;
		rios = _rios;
		parteaguas = _parteaguas;
		
		setInitialStep(1);
		setDeterminatedProcess(true);
		setStatusMessage(PluginServices.getText(this, menss));
		setNote("procesando información...");
		setFinalStep(8);
	}
	
	public void run() throws Exception 
	{
	 String gidHidro = GetValidatedSelection(rios); 
	 String gidCuenca = GetValidatedSelection(parteaguas);
	 Parteaguas parteaguasObject = null;
	 
	 //String table = "rios";
		  
	 if(!gidHidro.equals("") && !gidCuenca.equals(""))
	 {
      //Actualizo la informacion del parteaguas del rio
	   RioManager riosManager = new RioManager();
	   Rio rio = riosManager.Cargar_Objeto_BD(Integer.parseInt(gidHidro));   //CargarRio(Integer.parseInt(gidHidro));
	   
	   Parteaguas [] lista = ParteaguasManager.obtenerParteaguas("from parteaguas inner join rios on st_intersects(rios.the_geom, parteaguas.the_geom)" +
       		                               "where rios.gid="+rio.getGid());
       
       if(lista != null)
       {	    
    	if(lista.length == 1)
    	{
    	 parteaguasObject = lista[0];
    	 
    	 if(parteaguas == null)
    	 {	 
    	  JOptionPane.showMessageDialog(null, "No se pudo obtener el parteaguas ", "ERROR", JOptionPane.ERROR_MESSAGE);
    	  setCanceled(true);
    	  return;
    	 } 
    	 else
    	 {
    	  rio.setParteaguasg(parteaguasObject);
    	  rio.save();
    	 }	 
    	}
    	else
    	{	
    	 JOptionPane.showMessageDialog(null, "Existen dos parteaguas que se intersectan con el rio con gid "+rio.getGid(), "ERROR", JOptionPane.ERROR_MESSAGE);
    	 setCanceled(true);
   	     return;
    	}
       }
       else
       {	   
    	JOptionPane.showMessageDialog(null, "No existen parteaguas que se intersecten con el rio con gid "+rio.getGid(), "ERROR", JOptionPane.ERROR_MESSAGE);
    	setCanceled(true);
  	    return;
       }	 
       
       reportStep();
      
//	  db.executeQuery("update "+table+" set cuenca=f_parteaguas.gid from f_parteaguas " +
//		    		"where st_intersects("+table+".the_geom, f_parteaguas.the_geom) and "+table+".gid in("+ gidHidro +") and f_parteaguas.gid in("+ gidCuenca +")");
            
		 //Hallo el largo maximo que deben tener las lineas
		    double value = 0;
		    BitSet sel = parteaguas.getSelectionSupport().getSelection();
		    for (int i = sel.nextSetBit(0); i >= 0; i = sel.nextSetBit(i+1)) 
		    {
		     Rectangle2D bb = parteaguas.getSource().getShape(i).getBounds2D();
		     
//		     ViewPort _vp = AlgUtils.GetView(null).getMapControl().getViewPort();
//		     
//		     if (bb.intersects(_vp.getAdjustedExtent())) //si intersecta la seleccion
//		     {	 
		    	 double difX = bb.getMaxX()- bb.getMinX();
			     double difY = bb.getMaxY()- bb.getMinY();
					
					if(difX < 0)
					 difX = difX * -1;
					if(difY < 0)
					 difY = difY * -1;
					
					if(difX > difY)
					 value = difX*2; 
					else
					 value = difY*2;
//		     }
		    } 
		    
			Sextante.initialize();
			try {
				reportStep();
				setNote(PluginServices.getText(this, "Creando secciones tranversales"));
				
				FLyrVect st = (FLyrVect)AlgUtils.SeparateCrossLines(AlgUtils.AMBOSLADOS, rios, _dist, value, ""); //Hallando las secciones tranversales del los rios seleccionados
			
				FLyrVect paguas = (FLyrVect)AlgUtils.PolygonToPolylines(parteaguas, "");
				
				DBUtils.saveToPostGIS((FLyrVect)st, "f_temp_SeccTranv", true, true);
				DBUtils.saveToPostGIS((FLyrVect)paguas, "f_temp_paguaslines", true, false);
				
	 		   //Calculos necesarios para buscar la posicion relativa de las secciones
				reportStep();
	 			setNote(PluginServices.getText(this, "realizando cálculos auxiliares"));
	 			
	 			//String idr = Funciones_Utiles.GetSelectedGids(l);
	 			
	 			JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 			
	 		    PerfilManager.setIdrio_To_f_temp_secctranv(db, parteaguasObject.getGid());
				
	 		    Integer []ids = PerfilManager.obtener_f_temp_secctranv_IDS(db);
	 		    String []lados = PerfilManager.obtener_f_temp_secctranv_LADOS(db); 
				
			    if(!db.isEmpty() && !isCanceled())
			    {	
				 for(int i=0; i<ids.length; i++)
				 {
				  if(isCanceled())	 
				   break;
				  
				  reportStep();
				  setNote(PluginServices.getText(this, "Creando intersección "+ i+1 +" de "+ids.length));
				  
						  String point;
						  if(lados[i].equals("D"))
						   point = "end";
						  else
						   point = "start";
						  
						  PerfilManager.recortar_Perfiles(db, ids[i], point);
						  
						//Valido que los perfiles no estén fuera del parteaguas 
						  PerfilManager.eliminar_Perfiles_Fuera(ids[i], parteaguasObject.getGid(), db);
				  } 
	 
                 reportStep();
				 setNote(PluginServices.getText(this, "Finalizando"));
				 
//				 db.executeQuery(/*"CREATE TABLE f_temp_lineas(gid serial NOT NULL," +
//				 		"idrio integer,the_geom geometry,lado character varying(2)," +
//				 		"CONSTRAINT f_temp_lineas_pkey PRIMARY KEY (gid)," +
//				 		"CONSTRAINT enforce_dims_the_geom CHECK (ndims(the_geom) = 2)," +
//				 		"CONSTRAINT enforce_geotype_the_geom CHECK (geometrytype(the_geom) = 'MULTILINESTRING'::text OR the_geom IS NULL)," +
//				 		"CONSTRAINT enforce_srid_the_geom CHECK (srid(the_geom) = 2085)) " +
//				 		"WITH (OIDS=FALSE); ALTER TABLE f_temp_lineas OWNER TO postgres; " +*/
//						"delete from f_temp_lineas;"+ 
//				 		"insert into f_temp_lineas(idrio, lado, the_geom) " +
//				 	    "select idrio, lado, the_geom from f_temp_secctranv");
				 
				 System.out.println(parteaguasObject.getGid());
				 PerfilManager.insertar_Perfiles(parteaguasObject.getGid(), db);
				 
			  //Si se desea editar los perfiles
				showEditingLines(_showEditingLines, isCanceled());
				
				//****************************************************************
				  double leftLinesValues[] = new double[16];
				  double rightLinesValues[] = new double[16];
//				  double anchoFajaIzq[] = new double[13];
//				  double anchoFajaDer[] = new double[13];
				  
				  leftLinesValues[0] = 600;
				  leftLinesValues[1] = 50;
				  leftLinesValues[2] = 80;
				  leftLinesValues[3] = 78;
				  leftLinesValues[4] = 60;
				  leftLinesValues[5] = 88;
				  leftLinesValues[6] = 62;
				  leftLinesValues[7] = 167;
				  leftLinesValues[8] = 72;
				  leftLinesValues[9] = 56;
				  leftLinesValues[10] = 31;
				  leftLinesValues[11] = 210;
				  leftLinesValues[12] = 55;
				  leftLinesValues[13] = 102;
				  leftLinesValues[14] = 112;
				  leftLinesValues[15] = 600;
				  
				  rightLinesValues[0] = 600;
				  rightLinesValues[1] = 30;
				  rightLinesValues[2] = 40;
				  rightLinesValues[3] = 98;
				  rightLinesValues[4] = 55;
				  rightLinesValues[5] = 170;
				  rightLinesValues[6] = 48;
				  rightLinesValues[7] = 157;
				  rightLinesValues[8] = 170;
				  rightLinesValues[9] = 98;
				  rightLinesValues[10] = 80;
				  rightLinesValues[11] = 63;
				  rightLinesValues[12] = 113;
				  rightLinesValues[13] = 24;
				  rightLinesValues[14] = 600;
				  rightLinesValues[15] = 600;
				  
				  double ptosIzq[] = new double[16];
				  double ptosDer[] = new double[16];
				  
  				  ptosIzq[0] = 22;
  				  ptosIzq[1] = 22;
   				  ptosIzq[2] = 80;
  				  ptosIzq[3] = 34;
  				  ptosIzq[4] = 26;
				  ptosIzq[5] = 38;
				  ptosIzq[6] = 27;
				  ptosIzq[7] = 72;
				  ptosIzq[8] = 21;
				  ptosIzq[9] = 42;
				  ptosIzq[10] = 27;
				  ptosIzq[11] = 24;
				  ptosIzq[12] = 24;
				  ptosIzq[13] = 44;
				  ptosIzq[14] = 48;
				  ptosIzq[15] = 48;
				  
				  ptosDer[0] = 13;
				  ptosDer[1] = 13;
				  ptosDer[2] = 40;
				  ptosDer[3] = 42;
				  ptosDer[4] = 24;
				  ptosDer[5] = 73;
				  ptosDer[6] = 21;
				  ptosDer[7] = 68;
				  ptosDer[8] = 56;
				  ptosDer[9] = 100;
				  ptosDer[10] = 80;
				  ptosDer[11] = 27;
				  ptosDer[12] = 49;
				  ptosDer[13] = 10;
				  ptosDer[14] = 10;
				  ptosDer[15] = 600;
				  
				  db.ejecutarConsulta("select gid from perfiles where parteaguas=10");
				  String []gids = new String[db.getRowCount()];
				  for(int i=0; i<db.getRowCount(); i++)
				   gids[i] = db.getValueAsString(i,0).toString();
				  
				  int r = 0;
				  int l = 0;
				  
//				  Point2D []rightOutPoints = new Point2D[13];
//				  Point2D []rightIntPoints = new Point2D[13];
//				  Point2D []leftOutPoints = new Point2D[13];
//				  Point2D []leftIntPoints = new Point2D[13];
				  
//				  db.ejecutarConsulta("delete from \"_PUNTOSHM\"");
				  
				  for(int i=0; i<gids.length; i++)
				  {
					db.ejecutarConsulta("select st_x(st_startPoint(perfiles.the_geom)) as x1, st_y(st_startPoint(perfiles.the_geom)) as y1, " +
							            "st_x(st_endPoint(perfiles.the_geom)) as x2, st_y(st_endPoint(perfiles.the_geom)) as y2, perfiles.orilla " +
							            "from perfiles where gid="+gids[i]);
					
					double x0 = db.getValueAsDouble(0,0);
					double y0 = db.getValueAsDouble(0,1);
					double x1 = db.getValueAsDouble(0,2);
					double y1 = db.getValueAsDouble(0,3);
					String lado = db.getValueAsString(0,4);
					
//					double a = x1-x0;
//				    double b = y1-y0;
//				    double m = b/a;
//				    
//				    double L = 0;
//				    double x2 = 0;
//				    double y2 = 0;
//				    
//				      if(x0 < x1) // Esta en el 1ro o 4to cuadrante
//				      {	  
//				       x2 = x1 + Math.sqrt((L*L) / ((m*m)+1));
//				       y2 = y1 + (m*(x2-x1));
//				      }
//				      else //Si esta en el 2ro o 3er cuadrante
//				      {	  
//				       x2 = x1 - Math.sqrt((L*L) / ((m*m)+1));
//				       y2 = y1 + (m*(x2-x1));
//				      }
				      
					if(i <= 15*2)
					{    
						 if(lado.equals("I"))
						 {		 
						      double val = leftLinesValues[l];
						      Point2D point = UtilFunctions.getPoint(new Point2D.Double(x0,y0), new Point2D.Double(x1,y1), val);
	 						  db.ejecutarConsulta("update perfiles set the_geom = " +
									              "(select ST_SetSRID(st_multi(st_makeline(ST_Point("+x0+", "+y0+"), ST_Point("+point.getX()+", "+point.getY()+"))), 2085)) " +
									              "where gid="+gids[i]);
	 						  
	 						  val = ptosIzq[l];
						      point = UtilFunctions.getPoint(new Point2D.Double(x0,y0), new Point2D.Double(x1,y1), val);
						      String sql = "insert into \"_PARCELASPTOS\"(the_geom) " +
				                           "(select ST_SetSRID(ST_Point("+point.getX()+", "+point.getY()+"), 2085)) ";
	 						  db.ejecutarConsulta(sql);
	 						  System.out.println(sql);
	 						  
	 						  
//						      val = anchoFajaIzq[l];
//						      point = UtilFunctions.getPoint(new Point2D.Double(x0,y0), new Point2D.Double(x1,y1), val);
//						      String sql = "insert into \"_PUNTOSHM\"(the_geom) " +
//				                           "(select ST_SetSRID(ST_Point("+point.getX()+", "+point.getY()+"), 2085)) ";
//	 						  db.ejecutarConsulta(sql);
//	 						  System.out.println(sql);
//	 						  
//	 						 sql = "insert into \"_PUNTOSHM\"(the_geom) " +
//	                               "(select ST_SetSRID(ST_Point("+x0+", "+y0+"), 2085)) ";
//				             db.ejecutarConsulta(sql);
//	 						  
//	 						  leftOutPoints[l] = point;
//	 						  leftIntPoints[l] = new Point2D.Double(x0,y0);
	 						  l++;
						 } 
						 else
						 {
						      double val = rightLinesValues[r];
						      Point2D point = UtilFunctions.getPoint(new Point2D.Double(x0,y0), new Point2D.Double(x1,y1), val);
	 						  db.ejecutarConsulta("update perfiles set the_geom = " +
									              "(select ST_SetSRID(st_multi(st_makeline(ST_Point("+x0+", "+y0+"), ST_Point("+point.getX()+", "+point.getY()+"))), 2085)) " +
									              "where gid="+gids[i]);
	 						  
	 						  val = ptosDer[r];
						      point = UtilFunctions.getPoint(new Point2D.Double(x0,y0), new Point2D.Double(x1,y1), val);
						      String sql = "insert into \"_PARCELASPTOS\"(the_geom) " +
				                           "(select ST_SetSRID(ST_Point("+point.getX()+", "+point.getY()+"), 2085)) ";
	 						  db.ejecutarConsulta(sql);
	 						  System.out.println(sql);
	 						  
//	 						  val = anchoFajaDer[r];
//						      point = UtilFunctions.getPoint(new Point2D.Double(x0,y0), new Point2D.Double(x1,y1), val);
//						      String sql = "insert into \"_PUNTOSHM\"(the_geom) " +
//				                           "(select ST_SetSRID(ST_Point("+point.getX()+", "+point.getY()+"), 2085)) ";
//	 						  db.ejecutarConsulta(sql);
//	 						  System.out.println(sql);
//	 						  
//	 						  sql = "insert into \"_PUNTOSHM\"(the_geom) " +
//	                                "(select ST_SetSRID(ST_Point("+x0+", "+y0+"), 2085)) ";
//		 					  db.ejecutarConsulta(sql);
//	 						  
//	 						  rightOutPoints[r] = point;
//	 						  rightIntPoints[r] = new Point2D.Double(x0,y0);
	 						  r++;
						 }		 
					} 
					else
					 db.ejecutarConsulta("delete from perfiles where gid="+gids[i]);	
				  }
				  
				  //Construyendo las fajas
				    //FPoint2D []fajaDerechaPoints = new FPoint2D[rightOutPoints.length*2];
//				    FPoint2D []fajaIzquierdaPoints = new FPoint2D[leftOutPoints.length*2];
//				    int k = 0;
//				    boolean outs = true;
//				    for(int i=0; i<rightOutPoints.length*2; i++)
//				    {
//				    	if(outs)
//				    	{		
//					    	 fajaIzquierdaPoints[i] = new FPoint2D(leftOutPoints[k]);
//					    	// fajaDerechaPoints[i] = new FPoint2D(rightOutPoints[k]);
//					    	 k++;
//					    	 
//					    	 if(k == 13)
//					    	 {
//					    	   k = 0;
//					    	   outs = false;
//					    	 }
//				    	}
//				    	else  //Puntos interiores
//				    	{		
//						     fajaIzquierdaPoints[i] = new FPoint2D(leftIntPoints[k]);
//						     //fajaDerechaPoints[i] = new FPoint2D(rightIntPoints[k]);
//						     k++;
//					   	}
//				    } 	
//				    
//                    db.ejecutarConsulta("delete from fajas_hm");
//                    
//				    PersistentGeometricObject pgo = new PersistentGeometricObject(true) 
//				    {
//						@Override
//						public boolean save() {
//							// TODO Auto-generated method stub
//							return false;
//						}
//						
//						@Override
//						public boolean delete() {
//							// TODO Auto-generated method stub
//							return false;
//						}
//						
//						@Override
//						public Integer getGid() {
//							// TODO Auto-generated method stub
//							return null;
//						}
//				    };
				    
				   // IGeometry fajaderecha = GeometryUtilities.getPolygon2D(fajaDerechaPoints);
//				    IGeometry fajaizquierda = GeometryUtilities.getPolygon2D(fajaIzquierdaPoints);
				    
//				    pgo.setGeometry(fajaderecha, FShape.POLYGON);
//				    String sql = "insert into fajas_hm(the_geom) select "+pgo.getGeometryInfo();
//				    db.ejecutarConsulta(sql);
//				    System.out.println(sql);
				    
				    //pgo.setGeometry(fajaizquierda, FShape.POLYGON);
				    //String sql = "insert into fajas_hm(the_geom) select "+pgo.getGeometryInfo();
				    //db.ejecutarConsulta(sql);
				    //System.out.println(sql);
				    
//				      geometry = ShapeFactory.createPolyline2D(new GeneralPathX(geometry.getInternalShape()));
				//****************************************************************
				
				 reportStep();
			    }
			    else
			     System.out.println("No se obtuvieron perfiles"); 	
			    
			} catch (GeoAlgorithmExecutionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
             result = !isCanceled();
		   }
		
		reportStep();
	}
	
	public void finished() 
	{
	  if(result)
	  {	  
		Panel p = UIUtils.GetPanel("_anchoCalc");
		if(p != null)
		{
		 ((pAncho_Calculos)p).enableAceptarButton();	
		}
		try {
			Funciones_Utiles.LeyendaValoresUnicos("Perfiles", "orilla", false);
			
		} catch (LegendLayerException e) {
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}	
		//TrazasManager.insertar_Traza("Generó perfiles longitudinales");
	  }	
	  
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 PerfilManager.Eliminar_TablasTemporales(db);
	 
	 PluginServices.getMainFrame().enableControls();
	}
	
	/**Para asegurar que este seleccionado un rio y un parteaguas*/
	private static String GetValidatedSelection(FLyrVect l)
	{
		String res = "";
		String gids = Funciones_Utiles.GetSelectedGids(l);
		if(gids.contains(","))
		 JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento solamente");
		else
	    if(gids.equals(""))
	     JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
	    else
	     res = gids;
		
		return res;
	}
	
	private static void showEditingLines(boolean _showEditingLines, boolean isCanceled)
    {
    	if(_showEditingLines && !isCanceled)
		 {
		   boolean isShowing = false;
		   FLayer perfiles = null;
		   
		   View vista = VistaManager.GetActiveView();
		   
		   FLayers layers = vista.getModel().getMapContext().getLayers();
		   for(int i=0; i<layers.getLayersCount(); i++)
		   {
			if(layers.getLayer(i).getName().equals("Perfiles"))
			 isShowing = true;
			
			if(isShowing)
			 break;	
		   }	
		   
		   if(!isShowing)
		   {
			CargadorCapas cargador = new CargadorCapas();   
			perfiles = cargador.cargarTabla("perfiles", "Perfiles", 0, "", vista.getProjection());
			vista.getMapControl().getMapContext().getLayers().addLayer(perfiles);
		    cargador.CloseConnection();
		   } 
		   else
		   {
			  try {
				   vista.getModel().getMapContext().getLayers().getLayer("Perfiles").reload();
				   
			      } catch (ReloadLayerException e) {
				     e.printStackTrace();
			      }
			perfiles = vista.getModel().getMapContext().getLayers().getLayer("Perfiles");
		   }	
		  
		   VistaManager.RefreshView();
		   
//		   if(perfiles != null)
//		   {	  
//			AlgUtils.DesactivateLayers();
//		 	perfiles.setActive(true);
//            StartEditing se = new StartEditing();
//			se.execute("");
//		   }
		  }	 	
    }
}
