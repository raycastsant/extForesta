package org.geocuba.foresta.fajas.writerTasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import javax.swing.JOptionPane;
import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.geocuba.foresta.administracion_seguridad.db.CargadorCapas;
import org.geocuba.foresta.administracion_seguridad.db.DBUtils;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.Faja;
import org.geocuba.foresta.gestion_datos.FajaManager;
import org.geocuba.foresta.gestion_datos.PersistentGeometricObject;
import org.geocuba.foresta.gestion_datos.gui.pGestionManager;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.DataSource;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.layerOperations.AlphanumericData;
import com.iver.utiles.swing.threads.AbstractMonitorableTask;
import es.unex.sextante.core.Sextante;
import es.unex.sextante.exceptions.GeoAlgorithmExecutionException;

public class GenerarFaja extends AbstractMonitorableTask
{
	//private List<Integer> listaGidFajas;
	protected FLyrVect capa;
	protected double oIzq;
	protected double oDer;
	/*protected double _ancho; //para fajas simetricas
	protected String lyrname; //para fajas simetricas
*/	protected boolean memoryLayer;
	protected boolean showedit;
	protected int featCount;
	
	/**Para fajas con anchos diferentes*/
    public GenerarFaja(FLyrVect _capa, double oriIzq, double oriDer, boolean IsmemoryLayer, String menss, int cantPasos) throws ReadDriverException
    {		
    	capa = _capa;
    	oIzq = oriIzq;
    	oDer = oriDer;
    	memoryLayer = IsmemoryLayer;
    	featCount = Funciones_Utiles.GetSelectionCount(capa);
//    	listaGidFajas = new ArrayList<Integer>();
    	
    	setInitialStep(0);
		setDeterminatedProcess(true);
		setStatusMessage(PluginServices.getText(this, menss));
		setFinalStep(cantPasos*featCount);
	}
    
    /**Para fajas con anchos diferentes*/
    public GenerarFaja(String menss, FLyrVect _capa, int cantPasos) /*throws ReadDriverException*/
    {		
    	capa = _capa;
    	oIzq = 0;
    	oDer = 0;
    	memoryLayer = false;
    	showedit = false;
    	featCount = Funciones_Utiles.GetSelectionCount(capa);
//    	listaGidFajas = new ArrayList<Integer>();
    	
    	setInitialStep(0);
		setDeterminatedProcess(true);
		setStatusMessage(PluginServices.getText(this, menss));
		setFinalStep(cantPasos*featCount);
	}
    
    public void run() throws Exception 
    {
		if(capa == null)
		{
		 JOptionPane.showMessageDialog(null, "No se pudo obtener la capa");	
		 return;
		} 
		
		setNote("Obteniendo selección");
		
		BitSet sel = ((FLyrVect)capa).getSelectionSupport().getSelection();
	    DataSource ds = ((AlphanumericData)capa).getRecordset();
		   
	    ds.start();
	    int fieldIndex= ds.getFieldIndexByName("gid");
	    
	    int actualCount = 0;
	    
	    crear_tablas_temporales(); ///////////////////////////////////////////
	    
	    for (int i=sel.nextSetBit(0); i>=0; i=sel.nextSetBit(i+1)) 
	    {
	      if(isCanceled())
	       return;	  
	      
	      String idF = ds.getFieldValue(sel.nextSetBit(i), fieldIndex).toString();	
	      
	      actualCount++;
	      //setCurrentStep(0);
	      reportStep();
	      setNote("Procesando objetos "+(getCurrentStep()*100)/getFinalStep()+"%");
    	
	      FLyrVect ltemp = Funciones_Utiles.Crear_Capa_en_Memoria(FShape.LINE, capa.getSource().getShape(i), idF, "HidroLayer");
		  
		  //setCapa(ltemp);
    	  setMemLayerFlag(true);
    	  crear_Fajas(ltemp);
    	  
    	  reportStep();
	    }
	    
	    reportStep();
    }
    
  /**Metodo encargado de generar la faja*/
    protected boolean crear_Fajas(FLyrVect capa)
    {
     double minvalue = 0;
     double maxvalue = 0;
        
	 //FLayer lyr = null;
		
     if(!(oIzq<=0) && !(oDer<=0))
     {  	
 	  if(oIzq < 10) //Segun lo establecido por Herrero en el taller
 	   oIzq = 10;
 	 
      if(oDer < 10) 
       oDer = 10.01;
 	 
     if(capa!=null && !isCanceled())
     {
      try { 
 	  
      if(oIzq > oDer)
      {	 
       minvalue = oDer;
       maxvalue = oIzq;
      } 
      else
      {	 
       minvalue = oIzq;
       maxvalue = oDer;
      }

       if(isCanceled())
        return false;
       
         JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
     	
     	/*Guardo el buffer mas pequenno para luego unirlo a la parte de la faja mayor*/
     	Sextante.initialize();
     	FLyrVect minbuffer = (FLyrVect)AlgUtils.Buffer(capa, minvalue, false,  "");  //realizo el 1er buffer. QUE SEA EL DE MENOR DIST
     	reportStep();
     	setNote("Procesando objetos "+(getCurrentStep()*100)/getFinalStep()+"%");
     	
     	FLyrVect maxbuffer = (FLyrVect)AlgUtils.Buffer(capa, maxvalue, true, "");  //realizo el 2do buffer. QUE SEA EL DE MAYOR DIST
     	reportStep();
     	setNote("Procesando objetos "+(getCurrentStep()*100)/getFinalStep()+"%");
     	
     	FLyrVect buffer3 = (FLyrVect)AlgUtils.Buffer(capa, minvalue, true, "");  
     	reportStep();
     	setNote("Procesando objetos "+(getCurrentStep()*100)/getFinalStep()+"%");
     	
     	FLyrVect secciones_tranv = (FLyrVect)AlgUtils.SeccionesTranversales(capa, 70, (maxvalue/2)+maxvalue, ""); //Hallando las secciones tranversales del los rios seleccionados
     	reportStep();
     	setNote("Procesando objetos "+(getCurrentStep()*100)/getFinalStep()+"%");

     	FLyrVect diferencia = (FLyrVect)AlgUtils.Diferencia(minbuffer, maxbuffer, null);  //Hallo la diferencia entre los buffers
     	reportStep();
     	setNote("Procesando objetos "+(getCurrentStep()*100)/getFinalStep()+"%");
     	
     	FLayer entidanes_separadas = (FLayer)AlgUtils.SepararEntidades(diferencia, ""); //Separo las entidades en multiples partes
     	reportStep();
     	setNote("Procesando objetos "+(getCurrentStep()*100)/getFinalStep()+"%");
     	
         //Exportando capas a postgis
     	  DBUtils.saveToPostGIS((FLyrVect)entidanes_separadas, "f_temp_splitt", false, true);
     	  reportStep();
     	  setNote("Procesando objetos "+(getCurrentStep()*100)/getFinalStep()+"%");
     	  
     	  DBUtils.saveToPostGIS((FLyrVect)buffer3, "f_temp_buffer", false, true);
     	  reportStep();
     	  setNote("Procesando objetos "+(getCurrentStep()*100)/getFinalStep()+"%");
     	  
     	  DBUtils.saveToPostGIS((FLyrVect)secciones_tranv, "f_temp_SeccTranv", true, true);
     	  reportStep();
     	  setNote("Procesando objetos "+(getCurrentStep()*100)/getFinalStep()+"%");
     	  
		  if(isCanceled())
		   return false; 
		  System.out.println("");
		   //calculo de las lineas extendidas 
		    FajaManager.Extender_Líneas(capa, maxvalue+(maxvalue/3), db);
		    reportStep();
		    setNote("Procesando objetos "+(getCurrentStep()*100)/getFinalStep()+"%");
		    
		   //Calculos necesatios para buscar la posicion relativa de los splitts
		    FajaManager.CalculosAuxiliares((FLyrVect)capa, memoryLayer);
		    reportStep();
		    setNote("Procesando objetos "+(getCurrentStep()*100)/getFinalStep()+"%");
		    
		   //Selecciono los fid de cada uno de los splitts
		    Integer []Fids = FajaManager.obtener_Splits_Ids(db);
		    if(Fids != null)
		    {	
				 for(int i=0; i<Fids.length; i++)  //recorriendo los fids
				 {
					  if(isCanceled())
					   return false;
					 
					 db.ejecutarConsulta(FajaManager.getQuerySplittInterSection(Fids[i]));	
						
					 if(!db.isEmpty() && !isCanceled())
					 {
					  int gid = (Integer)db.getValueAt(0,0); //guardo el gid de la 1ra faja que es la que analizo
					  int intrsctVal = 0;
					  
					  /*Hallando cantidad de intersecciones*/
					  for(int j=0; j<db.getRowCount(); j++)
					  {
						 if((Integer)db.getValueAt(j,0) != gid)
						  break;
						 
						//Valor de interseccion de los 1ros puntos de digitalizacion de las lineas
						if(db.getValueAt(j, 1).toString().equals("false"))
						 intrsctVal--;	
						else
						 intrsctVal++;	
					   }
					  
					  if(intrsctVal < 0)  //Si no hubo valores de interseccion con el 1er pto, entonces esta a la izquierda
					  {
					   if(oIzq > oDer)     //Si el mayor lado corresponde a la izquierda
						   FajaManager.UnirPoligonos(gid, Fids[i], oIzq, oDer, true, db, capa.getProjection()); //Uno con el splitt analizado
					   else
						   FajaManager.UnirPoligonos(gid+1, Fids[i], oIzq, oDer, false, db, capa.getProjection()); //Uno con el splitt que esta a la derecha   
					  }
					  else
					  if(intrsctVal > 0)  //Si los valores de interseccion con el 1er pto fueron positivos, entonces esta a la derecha	  
					  {
					   if(oIzq > oDer)     //Si el mayor lado corresponde a la izquierda
						   FajaManager.UnirPoligonos(gid+1, Fids[i], oIzq, oDer, true, db, capa.getProjection()); //Uno con el splitt que esta a la izquierda
					   else
						   FajaManager.UnirPoligonos(gid, Fids[i], oIzq, oDer, false, db, capa.getProjection()); //Uno con el splitt que esta a la derecha   
					  }
					  else
					  {	  
					   JOptionPane.showMessageDialog(null, "Las probabilidades de interseccion dieron como valor cero");
					   return false;
					  }
					  reportStep();
					  setNote("Procesando objetos "+(getCurrentStep()*100)/getFinalStep()+"%");
					 }	 
					 else
					 {		 
					  System.out.println("No se obtuvieron splitts");
					  return false;
					 } 
				}
				
				//db.close();
				return true;
		   }
		   else 
		   {	   
		    System.out.println("Error; la capa f_temp_splitt no tiene datos");
		    return false;
		   } 
		    
        } catch (GeoAlgorithmExecutionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
      }  
      else 
      {	 
       JOptionPane.showMessageDialog(null,"No se obtuvieron los valores de la capa");
       return false;
      } 
	}
	else
	{		
      JOptionPane.showMessageDialog(null, "Valores de ancho incorrectos");
      return false;
	}
	return false;  
   }
    
    /**Genera una faja para los embalses*/
	protected void doFajaEmbalse(double ancho, String gidEmb)
	{
		 setFinalStep(3*featCount);
		
		 if(ancho < 10) //Segun lo establecido por Herrero en el taller
		  ancho = 10;	
		 
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		
		  if(isCanceled())
		   return;   	 		  
		  
		 //si habia una faja en ese pedazo de rio la elimino
		  FajaManager.eliminar_Faja_Embalse(Integer.parseInt(gidEmb), db);
		  
		  FajaManager.insertar_Faja_Embalse(Integer.parseInt(gidEmb), ancho, "", "Alrededor", db);
//		  if(gidFaja > 0)
//		   listaGidFajas.add(gidFaja);
		  
		  reportStep();
	}
    
	/**Crear fajas de anchos iguales*/
	protected boolean crearFajaSimetrica(double ancho, FLayer layer)
	{
	       if(isCanceled())
	        return false;
	       
	         JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	     
	   try {     
	         Sextante.initialize();
	         
	     	FLyrVect buffer = (FLyrVect)AlgUtils.Buffer(layer, ancho, true, "");  
	     	reportStep();
	     	setNote("Procesando objetos "+(getCurrentStep()*100)/getFinalStep()+"%");
	     	
	     	FLyrVect secciones_tranv = (FLyrVect)AlgUtils.SeccionesTranversales(layer, 70, (ancho/2)+ancho, ""); //Hallando las secciones tranversales de los rios seleccionados
	     	reportStep();
	     	setNote("Procesando objetos "+(getCurrentStep()*100)/getFinalStep()+"%");

	     	  DBUtils.saveToPostGIS((FLyrVect)buffer, "f_temp_buffer", false, true);
	     	  reportStep();
	     	  setNote("Procesando objetos "+(getCurrentStep()*100)/getFinalStep()+"%");
	     	  
	     	  DBUtils.saveToPostGIS((FLyrVect)secciones_tranv, "f_temp_SeccTranv", true, true);
	     	  reportStep();
	     	  setNote("Procesando objetos "+(getCurrentStep()*100)/getFinalStep()+"%");
	     	  
			  if(isCanceled())
			   return false; 
			  
			   //calculo de las lineas extendidas 
			    FajaManager.Extender_Líneas(layer, ancho+(ancho/3), db);
			    reportStep();
			    setNote("Procesando objetos "+(getCurrentStep()*100)/getFinalStep()+"%");
			    
			   //Calculos necesatios para buscar la posicion relativa de los splitts
			    FajaManager.CalculosAuxiliares((FLyrVect)layer, true);
			    reportStep();
			    setNote("Procesando objetos "+(getCurrentStep()*100)/getFinalStep()+"%");
			    
						  if(isCanceled())
						   return false;
						 
						  Integer []Ids = FajaManager.obtener_TempBuffer_Ids(db);
						  
						  db.ejecutarConsulta("delete from fajas where hidrografia=(select hidrografia.id from (f_temp_buffer inner join " +
								 	"rios on f_temp_buffer.idrio=rios.gid) inner join hidrografia on hidrografia.id=rios.tipo_hidrografia where f_temp_buffer.id="+Ids[0]+")");
						 
						 String sql = "insert into f_temp_dif(the_geom, idrio) values((select st_multi(ST_Difference(f_temp_buffer.the_geom, st_buffer(sT_union(rios.the_geom, f_temp_riverext.the_geom), 0.0001))) " +
						 	"from (rios inner join f_temp_buffer on rios.gid=f_temp_buffer.idrio) inner join f_temp_riverext on rios.gid=f_temp_riverext.gid " +
						 	"where f_temp_buffer.id="+ Ids[0] +"), (select idrio from f_temp_buffer where f_temp_buffer.id="+ Ids[0] +"))";
						 db.ejecutarConsulta(sql);
						 System.out.println(sql);
						 
						 CargadorCapas cargador = new CargadorCapas(); 
						 FLayer diff = cargador.cargarTabla("f_temp_dif", "Difference", 0, "", layer.getProjection());
						 cargador.CloseConnection();
						 
						 db.ejecutarConsulta("delete from f_temp_dif");
						 
							FLyrVect sp = (FLyrVect)AlgUtils.SepararEntidades(diff, ""); //Separo el menor buffer en dos entidades
							DBUtils.saveToPostGIS(sp, "f_temp_partes", false, true);
							
							Integer []gids = FajaManager.obtener_TempPartes_GIDS(db);
							
							//Busco la posicion relativa de cada parte del buffer
//							db.ejecutarConsulta("select st_intersects(ST_MakeLine(ST_StartPoint(f_temp_secctranv.the_geom), " +
//							 		"f_temp_points.the_geom), f_temp_partes.the_geom) " +
//					                "from (f_temp_points inner join f_temp_secctranv on f_temp_points.idseccion=f_temp_secctranv.gid)inner join f_temp_partes on  " +
//					                "f_temp_secctranv.idrio=f_temp_partes.idrio where f_temp_partes.gid1 ="+gids[0]);
							
							 sql = "select gid from fajas where hidrografia=(select rios.tipo_hidrografia from rios where gid=(select distinct f_temp_partes.idrio from f_temp_partes))";
							 db.ejecutarConsulta(sql);
							 
							 if(!db.isEmpty())
							 {
							  String gidFajas = "";	 
							  for(int k=0; k<db.getRowCount(); k++)	 
							   gidFajas += db.getValueAsString(k,0)+",";

							  gidFajas = gidFajas.substring(0,gidFajas.length()-1);
							  db.ejecutarConsulta("delete from fajas where gid in("+gidFajas+")");
							 }
							 
							for(int i=0; i<gids.length; i++)
							{	
								int intrsctVal = FajaManager.GetIntersectValue(gids[i], db);
								
								if(intrsctVal < 0) //gids[0] esta a la izquierda
								 FajaManager.InsertarFaja("Izquierda", ancho, gids[i], -1, db); 
								else               //gids[0] esta a la derecha
								 FajaManager.InsertarFaja("Derecha", ancho, gids[i], -1, db);
							}		
							
						 return true;
			    
	        } catch (GeoAlgorithmExecutionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return false;
	}
	
	public void finished() 
	{
		try {
			eliminar_tablas_temporales();   ///////////////////////////////////////////
			
			VistaManager.RefreshView();
			VistaManager.GetActiveView().getMapControl().getMapContext().getLayers().getLayer("Fajas").reload();
//			String projection = VistaManager.GetActiveView().getProjection().getAbrev();
//			projection = projection.substring(projection.length()-4, projection.length());
//			
//			String gids = "";
//			//FajaManager fajaman = new FajaManager();
//			JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
//			for(int i=0; i<listaGidFajas.size(); i++)
//			{		
//			 int gidFaja = listaGidFajas.get(i);	
//			 db.ejecutarConsulta("select ST_AsText(the_geom) from fajas where gid="+gidFaja);
//			 String geomInfo = "GeometryFromText('"+db.getValueAsString(0,0)+"', "+projection+")";
//			 
//			 //Faja faja = fajaman.Cargar_Objeto_BD(gidFaja);	
//			 PersistentGeometricObject.validarGeometria(db, geomInfo, gidFaja, "fajas", FShape.POLYGON); //Valido que no se monte con otra faja
//			 
//		     gids += gidFaja+",";
//			} 
			
//			if(!gids.equals(""))
//			{	
//			 gids = gids.substring(0, gids.length()-1);
//			 pGestionManager.showPanel_Gestion(pGestionManager.SQL_FAJAS, "where gid in("+gids+")");
//			 //Faja []fajas = FajaManager.listarFajas("where gid in("+gids+")");
//			} 
			
		} catch (ReloadLayerException e) {
			e.printStackTrace();
		}
	}
	
	/*protected void setCapa(FLyrVect _capa)
	{
	 capa = _capa;	
	}*/
	
	protected void setOIzq(double izq)
	{
	 oIzq = izq;	
	}
	
	protected void setODer(double der)
	{
	 oDer = der;	
	}
	
	protected void setMemLayerFlag(boolean flag)
	{
	 memoryLayer = flag;	
	}
	
	 protected void crear_tablas_temporales()
	 {
	   JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	   String sql = FajaManager.query_Crear_Tablas_Temporales();
	   db.ejecutarConsulta(sql);
	 }	
	 
	 protected void eliminar_tablas_temporales()
	 {
	  JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	  db.ejecutarConsulta(FajaManager.query_Eliminar_Tablas_Temporales());
	 }
}
