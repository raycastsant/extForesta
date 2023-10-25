package org.geocuba.foresta.fajas.writerTasks;

import java.awt.geom.Point2D;
import java.io.IOException;
import javax.swing.JOptionPane;

import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.geocuba.foresta.administracion_seguridad.db.CargadorCapas;
import org.geocuba.foresta.administracion_seguridad.db.DBUtils;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.analisis_terreno.listeners.Pendiente;
import org.geocuba.foresta.fajas.Perfil;
import org.geocuba.foresta.fajas.SubtramoPerfil;
import org.geocuba.foresta.fajas.SubtramosBasicGeometryInfo;
import org.geocuba.foresta.fajas.SubtramosManager;
import org.geocuba.foresta.fajas.gui.pSubtramosManager;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import org.gvsig.raster.grid.GridException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.exceptions.layers.LoadLayerException;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.core.FPoint2D;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.GeneralPathX;
import com.iver.cit.gvsig.fmap.core.GeometryUtilities;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.ShapeFactory;
import com.iver.cit.gvsig.fmap.edition.UtilFunctions;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import es.unex.sextante.exceptions.GeoAlgorithmExecutionException;

 /**Writer task para aplicar el método de Herrero-Melchanov*/
 public class Generar_Subtramos extends Pendiente
 {
	//private double rasterCellZize;
	private double Intensidad_aguacero; //intensidad máxima del aguacero
	private double Wf; //velocidad de infiltración de los suelos de la FAJA
	private Perfil []listaPerfiles;
	private int idParteaguas;
	private CargadorCapas cargador;
	
	public Generar_Subtramos(FLyrVect relieve, MapControl mapCtrl, double _Wf, double _i, int _idParteaguas, Perfil []_listaPerfiles) throws ReadDriverException, LoadLayerException, GridException, InterruptedException
	{
	 super(relieve, false, mapCtrl);
	 
//		rasterCellZize = Funciones_Utiles.getRasterCellZize();
//		Funciones_Utiles.setRasterCellZize(10);
		
	 Wf = _Wf;
	 Intensidad_aguacero = _i;
	 idParteaguas = _idParteaguas;
//	 PerfilManager pmanager = new PerfilManager();
//	 listaPerfiles = pmanager.Cargar_Perfiles("where parteaguas="+idParteaguas);
	 
	 listaPerfiles = _listaPerfiles;
	 if(listaPerfiles.length < 2)
	  listaPerfiles = null;	 
	 
//	 boolean flag = true;
//	 if(listaPerfiles!=null)
//	 {	 
//		 if(listaPerfiles.length < 2)
//		  flag = false;	 
//	 }
//	 else 
//	  flag = false;	 
//	 
//	 if(!flag)
//	 {	 
//		JOptionPane.showMessageDialog(null, "No se encontraron perfiles para el parteaguas seleccionado. "+"\n"+
//				   "El proceso será interrumpido.", "Información", JOptionPane.WARNING_MESSAGE);
////		   setCurrentStep(this.getFinishStep());
////		   reportStep();
//		   setCanceled(true);
//		   return;
//	 }
	 
	 setInitialStep(0);
	 setDeterminatedProcess(false);
	 setNote(PluginServices.getText(this, "Procesando información"));
	 setFinalStep(1);
	}
	
	protected void SiguienteProceso()
	{
	 if(listaPerfiles==null)
	  return;	 
			
	 super.SiguienteProceso();  //Hallar la pendiente
	 
	 if(isCanceled())
	 {
	  this.setCanceled(true);
	  return;
	 }
	 
	 AlgUtils.addLayertoView(pendienteFinal);	 
	 
	 try {
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 
		 SubtramosManager.redefinir_Tabla_tempPuntos(db);
		 
		 SubtramosManager.eliminar_Tablas_Temporales(db);
		 
		//Convierto la pendiente en polilineas------------------------------------
		 setNote("Procesando mapa de pendientes");
		 FLyrVect pendiente_polilineas = (FLyrVect)AlgUtils.PolygonToPolylines(pendienteFinal, "");
		 DBUtils.saveToPostGIS(pendiente_polilineas, "temp_polilineas", false, false);
		 
		//Procesando informacion de suelos------------------------------------------
		 setNote("Procesando mapa de suelos");
		 FLyrVect suelos = (FLyrVect)mapCtrl.getMapContext().getLayers().getLayer("Suelos");
		 if(suelos == null)
		 {
		  JOptionPane.showMessageDialog(null, "No se pudo obtener la capa 'Suelos'"+"\n"+"Verifique que la capa se encuentre disponible'", "Error", JOptionPane.ERROR_MESSAGE);
		  setCanceled(true);
		  return;
		 }		 
		
		 //Busco el Rectangle2D del mapa de pendientes  
		 double maxX = rect.getMaxX();
		 double maxY = rect.getMaxY();
		 double minX = rect.getMinX();
		 double minY = rect.getMinY();
		 FPoint2D []points = new FPoint2D[4];
		 points[0] = new FPoint2D(minX, minY);
		 points[1] = new FPoint2D(minX, maxY);
		 points[2] = new FPoint2D(maxX, maxY);
		 points[3] = new FPoint2D(maxX, minY);
		 IGeometry geom = GeometryUtilities.getPolygon2D(points);
		 FLyrVect rectangulo = Funciones_Utiles.Crear_Capa_en_Memoria(FShape.POLYGON, geom, "1", "Rectangulo");
		 
         //Hallo los suelos que se intersectan con el rectangulo		 
		 FLyrVect suelos_intersect = AlgUtils.intersectObjects(suelos, rectangulo, null);
		 
		 //Convierto los suelos en polilineas 
		 FLyrVect suelos_polilineas = (FLyrVect)AlgUtils.PolygonToPolylines(suelos_intersect, "");
		 DBUtils.saveToPostGIS(suelos_polilineas, "temp_polilineas_suelos", false, false);
		 
		//Procesando informacion de parcelas------------------------------------------
		 setNote("Procesando mapa de parcelas");
		 FLyrVect parcelas = (FLyrVect)mapCtrl.getMapContext().getLayers().getLayer("Parcelas");
		 if(parcelas == null)
		 {
		  JOptionPane.showMessageDialog(null, "No se pudo obtener la capa 'Parcelas'"+"\n"+"Verifique que la capa se encuentre disponible'", "Error", JOptionPane.ERROR_MESSAGE);
		  setCanceled(true);
		  return;
		 }	
		 
		 //Hallo las parecelas que se intersectan con el rectangulo		 
		 FLyrVect parcelas_intersect = AlgUtils.intersectObjects(parcelas, rectangulo, null);
		 DBUtils.saveToPostGIS(parcelas_intersect, "temp_parcelas", false, false);
		 
		 //Hallo la union con postgis. Agrupo en la consulta por USO de SUELO, y utilizando la funcion St_union realizo la Union de las parcelas con igual uso de suelo
		  SubtramosManager.crear_Union_Parcelas_Uso(db);
		 
		 //Convierto las parcelas en polilineas 
		 cargador = new CargadorCapas();
		 FLyrVect union_parcelas = cargador.cargarTabla("temp_union_parcelas", "temp_union_parcelas", 0, "", mapCtrl.getProjection());
		 FLyrVect parcelas_polilineas = (FLyrVect)AlgUtils.PolygonToPolylines(union_parcelas, "");
		 DBUtils.saveToPostGIS(parcelas_polilineas, "temp_polilineas_parcelas", false, false);
		 
		 //JTSFacade.difference(parcelas_intersect.getSource().getShape(0).toJTSGeometry(), parcelas_intersect.getSource().getShape(0).toJTSGeometry());
		 union_parcelas = null;
		 cargador.CloseConnection();
//		 db.executeQuery("select gid from perfiles order by gid");
//		 PerfilManager pmanager = new PerfilManager();
//		 listaPerfiles = pmanager.Cargar_Perfiles("where parteaguas="+idParteaguas);
//		 sinAnalizar = new HashMap<String,String>(); //ID, Causa

 //=========================Analizando cada perfil===============================================================
		 if(listaPerfiles != null)      //if(!db.isEmpty())
		 {
		   for(int i=0; i<listaPerfiles.length  && !isCanceled(); i++)    //for(int i=0; i<gidPerfiles.length  && !isCanceled(); i++)
		   {
			setNote("Analizando perfil "+(i+1)+" de "+listaPerfiles.length);
			
			DBUtils.Vaciar_Tabla("temp_puntos", db);
			SubtramosBasicGeometryInfo data = SubtramosManager.Determinar_Subtramos(listaPerfiles[i].getGid(), db);
			
			 double []Xs = data.Xs;
             double []Ys = data.Ys;
             double []distancias = data.distancias;
             
        //--------------Analizando cada punto (Tramo) del perfil-----------------------------------------------------
             int cantPtos = Xs.length;
             SubtramosManager.eliminar_Subtramos_Perfil(listaPerfiles[i].getGid(), db);
             
             for(int k=0; k<cantPtos-1 && !isCanceled(); k++)
             {
	              String note = "Analizando perfil "+(i+1)+" de "+listaPerfiles.length+". "+"\n"+
	    		  " Tramo "+(k+1)+" de "+cantPtos;	 
	              setNote(note);
	//              
	//               SueloManager suelosMan = new SueloManager();
	//               Suelo []listasuelos = suelosMan.Cargar_Suelos("where st_intersects(suelos.the_geom, st_setsrid(ST_MakePoint("+x+","+y+"),st_srid(suelos.the_geom)))"); 
	               
	              System.out.println(listaPerfiles[i].getGid());
	              double dist = distancias[k]/2;  //Para analizar el punto medio del subtramo
	              
	              GeneralPathX gp = new GeneralPathX();  //para crear el subtramo
	              Point2D p1 = new Point2D.Double(Xs[k], Ys[k]);
	              gp.moveTo(Xs[k], Ys[k]);
	              Point2D p2 = new Point2D.Double(Xs[k+1], Ys[k+1]);
	              gp.lineTo(Xs[k+1], Ys[k+1]);
	              
	              /*Se haya el punto medio para seleccionar el poligono
	               * de la pendiente correspondiente, porque si selecciona
	               * la punta de un subtramo se obtienen varias pendientes*/
	              Point2D point = UtilFunctions.getPoint(p1, p2, dist);    //Punto Medio
	              IGeometry pointGeom = ShapeFactory.createPoint2D(point.getX(), point.getY());
	              
	                FLyrVect punto = Funciones_Utiles.Crear_Capa_en_Memoria(FShape.POINT, pointGeom, ((Integer)k).toString(), "Punto");
	                FLyrVect interseccion =  AlgUtils.intersectObjects(pendienteFinal, punto, "");
	
	                int column = interseccion.getRecordset().getFieldIndexByName("pendiente");
	                double pendiente = Double.parseDouble(interseccion.getRecordset().getFieldValue(0,column).toString());
	                
	                SubtramoPerfil subtramo = new SubtramoPerfil();
	                subtramo.setPendiente(pendiente);
	//                subtramo.setSuelo(suelos[0]);
	                subtramo.setPerfil(listaPerfiles[i].getGid());
	                IGeometry linegeom = ShapeFactory.createPolyline2D(gp);
	                //linegeom = GeometryUtil.removeDuplicates(linegeom.toJTSGeometry());
	                subtramo.setGeometry(linegeom, FShape.LINE);
	                subtramo.save();
             } //Fin del analisis de los subtramos del perfil 	 
		   }	  
		 }
		 else
		 {
		  setCanceled(true);
		  return;
		 }		 
		
		 reportStep();

		} catch (GeoAlgorithmExecutionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ReadDriverException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void finished() 
	{
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
     SubtramosManager.eliminar_Tablas_Temporales(db);
	 
//	 if(!sinAnalizar.isEmpty())
//	  mostrarPanelErrorres();
//	 else
//	 {
	  FLayer subtramos = VistaManager.GetActiveView().getMapControl().getMapContext().getLayers().getLayer("Subtramos");
	  System.out.println(isCanceled());
	  if(listaPerfiles!=null && !isCanceled())
	  {
//	   String gidPerfiles = "";	  
//	   for(int i=0; i<listaPerfiles.length; i++)
//	    gidPerfiles += listaPerfiles[i].getGid().toString()+",";
//	   gidPerfiles = gidPerfiles.substring(0,gidPerfiles.length()-1);
		   
		try { 
		   if(subtramos==null)
		   {	   
			   cargador = new CargadorCapas();
			   //subtramos = cargador.cargarTabla("sub_tramos_perfil", "Subtramos", 0, " where perfil in("+gidPerfiles+")", org.geocuba.foresta.herramientas.utiles.Global.projeccionActiva);
			   subtramos = cargador.cargarTabla("sub_tramos_perfil", "Subtramos", 0, "", org.geocuba.foresta.herramientas.utiles.Global.projeccionActiva);
			   AlgUtils.addLayertoView(subtramos);
		   }
		   else
		   {	   
		    subtramos.reload();
		    VistaManager.RefreshView();
		   }
		   
	  // TrazasManager.insertar_Traza("Generó subtramos de perfiles en el parteaguas "+listaPerfiles[0].getParteaguas().getGid());
	   
			pSubtramosManager.showPanel_Gestion(idParteaguas, Intensidad_aguacero, Wf);
			//Funciones_Utiles.setRasterCellZize(rasterCellZize);
			
		   } catch (ReloadLayerException e) {
			     JOptionPane.showMessageDialog(null, "Error al recargar la capa de subtramos"); 	
				 e.printStackTrace();
		   }
	  }
	  
	 reportStep();
	}
	
//	private void mostrarPanelErrorres()
//	{
//	 Object []keys = sinAnalizar.keySet().toArray();
//	 pErrores p = new pErrores();
//	 p.getListModel().removeAllElements();
//	 
//	 for(int i=0; i<keys.length; i++)
//	  p.getListModel().addElement(keys[i]+"---"+sinAnalizar.get(keys[i]));
//	 
//	 p.Show();
//	}
}

 