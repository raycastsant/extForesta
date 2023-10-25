package org.geocuba.foresta.gestion_datos;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.BitSet;
import javax.swing.JOptionPane;
import org.cresques.cts.IProjection;
import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.geocuba.foresta.administracion_seguridad.db.CargadorCapas;
import org.geocuba.foresta.administracion_seguridad.db.DBUtils;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.fajas.Perfil;
import org.geocuba.foresta.fajas.PerfilManager;
import org.geocuba.foresta.fajas.extensiones.AnchoCalculosExt;
import org.geocuba.foresta.fajas.extensiones.AnchoFijo_LegislacionExt;
import org.geocuba.foresta.fajas.writerTasks.GenerarFaja;
import org.geocuba.foresta.fajas.writerTasks.GenerarFaja_AnchoLegislacion;
import org.geocuba.foresta.fajas.writerTasks.GenerarFaja_TereceraParteLongitud;
import org.geocuba.foresta.fajas.writerTasks.GenerarFaja_Simetrica;
import org.geocuba.foresta.fajas.writerTasks.Generar_Subtramos;
import org.geocuba.foresta.gestion_datos.Hidrografia;
import org.geocuba.foresta.gestion_datos.HidrografiaManager;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import org.geocuba.foresta.herramientas.utiles.Global;
import org.gvsig.raster.grid.GridException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.DataSource;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.exceptions.layers.LoadLayerException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.fmap.layers.layerOperations.AlphanumericData;
import es.unex.sextante.exceptions.GeoAlgorithmExecutionException;

public class FajaManager implements IPersistenObjectManager
{
	@Override
	public Faja Cargar_Objeto_BD(int gid) //throws ReadDriverException 
	{
     Faja faja = null;		
     JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
     db.ejecutarConsulta("select gid,hidrografia,nombre_lugar,orilla,ancho from fajas where gid="+gid);
     
     if(!db.isEmpty())
     {
      int idHidro = db.getValueAsInteger(0,1);
      HidrografiaManager hidroman = new HidrografiaManager();
      Hidrografia hidro = hidroman.Cargar_Objeto_BD(idHidro);
      
      faja = new Faja(db.getValueAsInteger(0,0), hidro, db.getValueAsString(0,2), db.getValueAsString(0,3), 
    		  db.getValueAsDouble(0,4), false);
     } 	 
     return faja;
	}
	
	public static Faja[] listarFajas(String condition) throws ReadDriverException 
	{
	 Faja []lista = null;
	 if(condition == null)
	  condition = "";
	 
     CargadorCapas cargador = new CargadorCapas();
     FLyrVect datosFajas = cargador.cargarTabla("fajas", "fajas", 0, condition, Global.projeccionActiva);
     SelectableDataSource datasource = ((ReadableVectorial)datosFajas).getRecordset();
     lista = new Faja[(int)datasource.getRowCount()];
     
     //Se obtienen los Field Index de cada campo
     int gidFI = datasource.getFieldIndexByName("gid");
     int hidrografiaFI = datasource.getFieldIndexByName("hidrografia");
     int orillaFI = datasource.getFieldIndexByName("orilla");
     int nombre_lugarFI = datasource.getFieldIndexByName("nombre_lugar");
     int anchoFI = datasource.getFieldIndexByName("ancho");
     
     for(int i=0; i<datasource.getRowCount(); i++)
     {
      int gid = Integer.parseInt(datasource.getFieldValue(i,gidFI).toString()); // 
      int hidroId = Integer.parseInt(datasource.getFieldValue(i,hidrografiaFI).toString());
      HidrografiaManager hidrografiaManager = new HidrografiaManager();
      Hidrografia hidrografia = hidrografiaManager.Cargar_Objeto_BD(hidroId);   //.cargarHidrografia(hidroId); //
      
      String orilla = datasource.getFieldValue(i,orillaFI).toString(); //
      String nombre_lugar = datasource.getFieldValue(i,nombre_lugarFI).toString(); //
      double ancho = Double.parseDouble(datasource.getFieldValue(i,anchoFI).toString()); //
      
      Faja faja = new Faja(gid, hidrografia, nombre_lugar, orilla, ancho, false);
      lista[i] = faja;
     }	 
     
	 return lista;
	}
	
	public static Faja loadFajaByQuery(String condition, String orderBy) {
		return null;
	}
	
	  /**Les pone a las Secciones Tranversales y a los splitts el rio al que pertenecen y
     * actualiza la tabla temporal de ptos de interseccion**/
    public static void CalculosAuxiliares(FLyrVect layer, boolean MemoryLayer)
    {
     JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());	

     /*Le asigno a las secciones tranversales el rio que les corresponde*/
     try {
    	 
         if(!MemoryLayer)
         {	 
        	 BitSet sel = layer.getSelectionSupport().getSelection();
        	 DataSource ds = ((AlphanumericData)layer).getRecordset();
		   
	         ds.start();
	         int fieldIndex= ds.getFieldIndexByName("gid");
	         
	         for (int i = sel.nextSetBit(0); i >= 0; i = sel.nextSetBit(i+1)) 
	         {
	          String sql = "update f_temp_secctranv set idrio=rios.gid from rios " +
		         "where st_intersects(rios.the_geom, f_temp_secctranv.the_geom) " +
		         "and rios.gid=" + ds.getFieldValue(sel.nextSetBit(i), fieldIndex).toString();
	          db.ejecutarConsulta(sql);
	          System.out.println(sql);
	         }	 

	         ds.stop();
          }
          else //Si es una capa en memoria no puedo trabajar con la seleccion; en este caso se que tiene un solo feature
          {
           String idr = ((AlphanumericData)layer).getRecordset().getFieldValue(0,0).toString();
           
           String sql = "update f_temp_secctranv set idrio=rios.gid from rios " +
	         "where st_intersects(rios.the_geom, f_temp_secctranv.the_geom) " +
	         "and rios.gid=" + idr;
           db.ejecutarConsulta(sql);
           System.out.println(sql);
          }	
     
          } catch (ReadDriverException e) {
		    e.printStackTrace();
	     }     
///////////////////////////////////////////////////////////////////////////////////    
	/*Les pongo a los splitts el rio al que pertenecen*/ 
	db.ejecutarConsulta("update f_temp_splitt set idrio=f_temp_secctranv.idrio from f_temp_secctranv " +
		      "where st_intersects(f_temp_splitt.the_geom, f_temp_secctranv.the_geom) and f_temp_splitt.idrio is null");

///////////////////////////////////////////////////////////////////////////////////	 
     /*Actualizo la tabla de ptos de interseccion entre secciones tranversales y rios*/
     db.ejecutarConsulta("select gid from f_temp_secctranv");
     
     int [] idsec= new int[db.getRowCount()];
     for(int i=0; i<db.getRowCount(); i++)  //guardando los gid de las secciones tranversales
      idsec[i] = (Integer)db.getValueAt(i,0);

     //limpio la tabla temporal de puntos de interseccion
     db.ejecutarConsulta("delete from f_temp_points");
     
     //inserto en la tabla f_temp_points los puntos de interseccion
     for(int i=0; i<idsec.length; i++)
     {	 
      String sql = "insert into f_temp_points(select "+ ((Integer)(i+1)).toString() +", f_temp_secctranv.gid, ST_Intersection(f_temp_secctranv.the_geom, rios.the_geom) " +
       "from f_temp_secctranv inner join rios on f_temp_secctranv.idrio=rios.gid " +
        "where f_temp_secctranv.gid="+((Integer)idsec[i]).toString()+")";	 
      db.ejecutarConsulta(sql);
      System.out.println(sql);
     } 
     
     System.out.println("Información actualizada");
    }
    
	/**Lanza el proceso de generación de una faja de ancho fijo*/
	public static void crear_Faja_Ancho_Fijo(double anchoLeft, double anchoRight)
	{
	 if(anchoLeft != anchoRight)  
   	 {
   		if(AnchoFijo_LegislacionExt.selectedLayer != null)
		 try {//Faja con anchos diferentes
			PluginServices.cancelableBackgroundExecution(new GenerarFaja(AnchoFijo_LegislacionExt.selectedLayer, anchoLeft, anchoRight, false, "Generando fajas...", 14));
					
			 } catch (ReadDriverException e) {
					e.printStackTrace();
			 }
   	 }	 
   	 else
   	 {	
   	  //Faja  Simetrica
	  PluginServices.cancelableBackgroundExecution(new GenerarFaja_Simetrica(anchoLeft, AnchoFijo_LegislacionExt.selectedLayer, "Generando Fajas..."));
   	 }		
	}
	
	/**Lanza el proceso de generación de una faja de ancho por legislación*/
	public static void crear_Faja_Ancho_Legislacion()
	{
	 PluginServices.cancelableBackgroundExecution(new GenerarFaja_AnchoLegislacion("Generando Fajas...", AnchoFijo_LegislacionExt.selectedLayer));	
	}
	
	/**Genera las fajas forestales segun método de la tercera 
	 * parte de la longitud de la ladera*/
	public static void Crear_Faja_TereceraParteLongitud()
	{
	 PluginServices.cancelableBackgroundExecution(new GenerarFaja_TereceraParteLongitud("Generando Fajas", AnchoCalculosExt.selectedLayer, AnchoCalculosExt.parteaguas));	
	}
	
	/**Genera las fajas forestales segun los perfiles establecidos
	 * @throws ReadDriverException 
	 * @throws InterruptedException 
	 * @throws GridException 
	 * @throws LoadLayerException */
	public static void Aplicar_Herrero_Melchanov(double _Wf, double _i) throws ReadDriverException, LoadLayerException, GridException, InterruptedException
	{
	 MapControl mapctrl = VistaManager.GetActiveView().getMapControl();
	 FLyrVect relieve = (FLyrVect)mapctrl.getMapContext().getLayers().getLayer("Relieve");
	 
	 FLyrVect parteaguas = AnchoCalculosExt.parteaguas;
	 int gidParteaguas = Integer.parseInt(Funciones_Utiles.GetSelectedGids(parteaguas));
	 
	 PerfilManager pmanager = new PerfilManager();
	 Perfil []listaPerfiles = pmanager.Cargar_Perfiles(gidParteaguas);
	 boolean flag = true;
	 if(listaPerfiles!=null)
	 {	 
		 if(listaPerfiles.length < 2)
		  flag = false;	 
	 }
	 else 
	  flag = false;	 
	 
	 if(!flag)
	 {	 
		JOptionPane.showMessageDialog(null, "No se encontraron perfiles para el parteaguas seleccionado. "+"\n"+
				   "El proceso será interrumpido.", "Información", JOptionPane.WARNING_MESSAGE);
		   return;
	 }
	 
	 Generar_Subtramos wt = new Generar_Subtramos(relieve, mapctrl, _Wf, _i, gidParteaguas, listaPerfiles);
	 
	 Rectangle2D bb = null;
	 BitSet sel = parteaguas.getSelectionSupport().getSelection();
	 for (int i = sel.nextSetBit(0); i >= 0; i = sel.nextSetBit(i+1)) 
	  bb = parteaguas.getSource().getShape(i).getBounds2D();
	 
	 if(bb != null)
	 {
	  //bb.setRect(bb.getX()+50, bb.getY()+50, bb.getWidth()+50, bb.getHeight()+50);
	  bb = new Rectangle2D.Double(bb.getX()-(bb.getWidth()/2), bb.getY()-(bb.getHeight()/2), bb.getWidth()+(bb.getWidth()), bb.getHeight()+(bb.getHeight()));
	  wt.setRectangle(bb);	 
	  PluginServices.cancelableBackgroundExecution(wt);
	 }
	 else
	  JOptionPane.showMessageDialog(null, "No se pudo obtener el Bounding Box del Parteaguas seleccionado");	 
	}
	
	public static Integer[] obtener_Splits_Ids(JDBCAdapter db)
	{
	   Integer []Fids  = null;
	   
	   if(db == null)
		db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		   
		db.ejecutarConsulta("select distinct id from f_temp_splitt");
	    if(!db.isEmpty())
	    {	
		 Fids = new Integer[db.getRowCount()];
		 for(int i=0; i<db.getRowCount(); i++)   
		  Fids[i] = (Integer)db.getValueAt(i, 0);
	    }
	    
	  return Fids;  
	}
	
	public static Integer[] obtener_TempBuffer_Ids(JDBCAdapter db)
	{
	   Integer []Fids  = null;
	   
	   if(db == null)
		db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		   
	    //Selecciono los id de cada uno de los buffers
		 db.ejecutarConsulta("select distinct id from f_temp_buffer");
		 if(!db.isEmpty())
		 {
		  Fids = new Integer[db.getRowCount()];
		  for(int i=0; i<db.getRowCount(); i++)  
		   Fids[i] = (Integer)db.getValueAt(i,0);
		 } 
	    
	  return Fids;  
	}
	
	public static String getQuerySplittInterSection(int idSplitt)
	{
	 return	"select f_temp_splitt.gid, st_intersects(ST_MakeLine(ST_StartPoint(f_temp_secctranv.the_geom), " +
 		    "f_temp_points.the_geom), f_temp_splitt.the_geom) " +
            "from (f_temp_points inner join f_temp_secctranv on f_temp_points.idseccion=f_temp_secctranv.gid)inner join f_temp_splitt on  " +
            "f_temp_secctranv.idrio=f_temp_splitt.idrio where f_temp_splitt.id="+idSplitt+" order by f_temp_splitt.gid";
	}
	
	public static boolean eliminar_Faja_Embalse(int idHidrografia, JDBCAdapter db)
	{
	   boolean flag = false;
	   
	   if(db == null)
		db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		   
	   flag = db.ejecutarConsulta("delete from fajas where hidrografia=(select tipo_hidrografia from embalses where gid="+idHidrografia+")");
	    
	  return flag;  
	}
	
	public static int insertar_Faja_Embalse(int gidEmb, double ancho, String nombreLugar, String orilla, JDBCAdapter db)
	{
	   int gid = -1;
	   
	   if(db == null)
		db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		   
	   db.ejecutarConsulta("select hidrografia.id, ST_SRID(embalses.the_geom), st_astext(ST_multi(ST_Difference(ST_Buffer(embalses.the_geom, "+ ancho +"), embalses.the_geom))) " +
		            "from embalses inner join hidrografia on embalses.tipo_hidrografia=hidrografia.id where embalses.gid="+gidEmb);
	   if(!db.isEmpty())
	   {	   
	     int hidroId = db.getValueAsInteger(0,0);
	     String srid = db.getValueAsString(0,1); 
	     String geom = "st_geometryFromText('"+db.getValueAsString(0,2)+"', "+srid+")";
	     
		 String sql = "insert into fajas(hidrografia,nombre_lugar,orilla,ancho,the_geom) " +
		              "values("+hidroId+",'"+nombreLugar+"','"+orilla+"',"+ancho+","+geom+")";
		 
		 db.ejecutarConsulta(sql);
		 db.ejecutarConsulta("select max(gid) from fajas");
		   if(!db.isEmpty())
		   {
			   gid = db.getValueAsInteger(0,0);
			   
//	           if(PersistentGeometricObject.validarGeometria(db, geom, gid, "fajas", FShape.POLYGON))
	        	TrazasManager.insertar_Traza("Se creó la faja con identificador "+gid);
		   } 
	   }
		   
	  return gid;  
	}
	
	public static boolean eliminar_Faja_Rio(int idBuffer, JDBCAdapter db)
	{
	   boolean flag = false;
	   
	   if(db == null)
		db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		   
	   String sql = "delete from fajas where hidrografia=(select hidrografia.id from (f_temp_buffer inner join " +
	 	            "rios on f_temp_buffer.idrio=rios.gid) inner join hidrografia on hidrografia.id=rios.tipo_hidrografia where f_temp_buffer.id="+idBuffer+")";

		db.ejecutarConsulta(sql);
		if(!db.isEmpty())
		{
			String gids = "";
			for(int j=0; j<db.getRowCount(); j++)
			 gids += db.getValueAsString(j,0)+",";
			
			gids = gids.substring(0,gids.length()-1);
			db.ejecutarConsulta("delete from suelos_fajas where faja in("+gids+")");
			db.ejecutarConsulta("delete from fajas where gid in("+gids+")");
			
			flag = true;
        } 
	    
	  return flag;  
	}
	
	public static boolean eliminar_FajaSimetrica_Rio(JDBCAdapter db)
	{
	   boolean flag = false;
	   
	   if(db == null)
		db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		   
	     String sql = "select gid from fajas where hidrografia=(select rios.tipo_hidrografia from rios where gid=(select distinct f_temp_partes.idrio from f_temp_partes))";
		 db.ejecutarConsulta(sql);
		 
		 if(!db.isEmpty())
		 {
		  String gidFajas = "";	 
		  for(int k=0; k<db.getRowCount(); k++)	 
		   gidFajas += db.getValueAsString(k,0)+",";

		  gidFajas = gidFajas.substring(0,gidFajas.length()-1);
		  db.ejecutarConsulta("delete from suelos_fajas where faja in("+gidFajas+")");
		  db.ejecutarConsulta("delete from fajas where gid in("+gidFajas+")");
		  
		  flag = true;
		 }
	    
	  return flag;  
	}
	
	public static boolean Dividir_Buffer_Temporal(int idBuffer, JDBCAdapter db)
	{
	   boolean flag = false;
	   
	   if(db == null)
		db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		   
	   flag = db.ejecutarConsulta("insert into f_temp_dif(select 1 as gid, st_multi(ST_Difference(f_temp_buffer.the_geom, st_buffer(sT_union(rios.the_geom, f_temp_riverext.the_geom), 0.0001))), f_temp_buffer.idrio " +
			 	"from (rios inner join f_temp_buffer on rios.gid=f_temp_buffer.idrio) inner join f_temp_riverext on rios.gid=f_temp_riverext.gid " +
			 	"where f_temp_buffer.id="+ idBuffer +")"); 
	    
	  return flag;  
	}
	
	public static Integer[] obtener_TempPartes_GIDS(JDBCAdapter db)
	{
	   Integer []Fids  = null;
	   
	   if(db == null)
		db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		   
	    db.ejecutarConsulta("select distinct gid1 from f_temp_partes");
	    if(!db.isEmpty())
	    {	
	    	Fids = new Integer[2]; //el buffer dividido
	    	Fids[0] = db.getValueAsInteger(0,0);
	    	Fids[1] = db.getValueAsInteger(1,0);
	    }		
	    
	  return Fids;  
	}
	
	/**Une la parte del splitt que corresponda a la mayor orilla al buffer mas pequenno, 
	 * quedando como resultado la faja solicitada*/
	public static void UnirPoligonos(int idSplitt, int idFeature, double oriIzq, double oriDer, boolean isAtLeft, JDBCAdapter db, IProjection projection)
	{
	 //si habia una faja en ese pedazo de rio la elimino
	 db.ejecutarConsulta("delete from fajas where hidrografia=(select hidrografia.id from (f_temp_buffer inner join " +
			 	"rios on f_temp_buffer.idrio=rios.gid) inner join hidrografia on hidrografia.id=rios.tipo_hidrografia where f_temp_buffer.id="+idFeature+")");
	 
	 /*Divido el menor buffer en dos y lo guardo en una tabla temporal*/
	 db.ejecutarConsulta("delete from f_temp_dif");
	 
	 String sql = "insert into f_temp_dif(select 1 as gid, st_multi(ST_Difference(f_temp_buffer.the_geom, st_buffer(sT_union(rios.the_geom, f_temp_riverext.the_geom), 0.0001))), f_temp_buffer.idrio " +
	 	"from (rios inner join f_temp_buffer on rios.gid=f_temp_buffer.idrio) inner join f_temp_riverext on rios.gid=f_temp_riverext.gid " +
	 	"where f_temp_buffer.id="+ idFeature +")";
	 db.ejecutarConsulta(sql);
	 System.out.println(sql);
	 
	 CargadorCapas cargador = new CargadorCapas(); 
	 FLayer diff = cargador.cargarTabla("f_temp_dif", "Difference", 0, "", projection);
	 cargador.CloseConnection();
	
	 try {
		FLyrVect sp = (FLyrVect)AlgUtils.SepararEntidades(diff, ""); //Separo el menor buffer en dos entidades
		DBUtils.saveToPostGIS(sp, "f_temp_partes", false, true);
		
		Integer []gids = FajaManager.obtener_TempPartes_GIDS(db);
		
		//Busco la posicion relativa de cada parte del buffer
		db.ejecutarConsulta("select st_intersects(ST_MakeLine(ST_StartPoint(f_temp_secctranv.the_geom), " +
		 		"f_temp_points.the_geom), f_temp_partes.the_geom) " +
                "from (f_temp_points inner join f_temp_secctranv on f_temp_points.idseccion=f_temp_secctranv.gid)inner join f_temp_partes on  " +
                "f_temp_secctranv.idrio=f_temp_partes.idrio where f_temp_partes.gid1 ="+gids[0]);
		
		int intrsctVal = GetIntersectValue(gids[0], db);
		//int idfaja = getFajaId(db);
		//int idfaja = -1;
		 
		 sql = "select gid from fajas where hidrografia=(select rios.tipo_hidrografia from rios where gid=(select distinct f_temp_partes.idrio from f_temp_partes))";
		 db.ejecutarConsulta(sql);
		 
		 if(!db.isEmpty())
		 {
		  String gidFajas = "";	 
		  for(int k=0; k<db.getRowCount(); k++)	 
		   gidFajas += db.getValueAsString(k,0)+",";

		  gidFajas = gidFajas.substring(0,gidFajas.length()-1);
		  //db.executeQuery("delete from suelos_fajas where faja in("+gidFajas+")");
		  //db.executeQuery("delete from fajas_pendientes where faja in("+gidFajas+")");
		  db.ejecutarConsulta("delete from fajas where gid in("+gidFajas+")");
		 }
		
		if(intrsctVal < 0) //gids[0] esta a la izquierda
		{
		 if(isAtLeft)//Si el splitt a unir esta a la izquierda
		 {	 
		  InsertarFaja("Izquierda", oriIzq, gids[0], idSplitt, db); //uno con el splitt de la izq e inserto el resultado
		  InsertarFaja("Derecha", oriDer, gids[1], -1, db);
		 }
		 else  //Si el splitt a unir esta a la derecha
		 {	 
		  InsertarFaja("Izquierda", oriIzq, gids[0], -1, db); 
		  InsertarFaja("Derecha", oriDer, gids[1], idSplitt, db); //uno el splitt de la der e inserto el resultado
		 }	 
		}	
		else //gids[0] esta a la derecha
		{
			 if(!isAtLeft) //Si el splitt a unir esta a la derecha
			 {	 
			  InsertarFaja("Derecha", oriDer, gids[0], idSplitt, db); //uno con el splitt de la der e inserto el resultado
			  InsertarFaja("Izquierda", oriIzq, gids[1], -1, db);
			 }
			 else  //Si el splitt a unir esta a la izquierda
			 {	 
			  InsertarFaja("Derecha", oriDer, gids[0], -1, db); 
			  InsertarFaja("Izquierda", oriIzq, gids[1], idSplitt, db); //uno el splitt de la der e inserto el resultado
			 }	 
		}	
		
	    } catch (GeoAlgorithmExecutionException e) {
		   e.printStackTrace();
	    } catch (IOException e) {
		   e.printStackTrace();
	    }
	}
	
	/**Obtiene un valor de interseccion positivo o negativo*/
	public static int GetIntersectValue(int gid, JDBCAdapter db) 
	{
		String sql = "select st_intersects(ST_MakeLine(ST_StartPoint(f_temp_secctranv.the_geom), " +
 		"f_temp_points.the_geom), f_temp_partes.the_geom) " +
        "from (f_temp_points inner join f_temp_secctranv on f_temp_points.idseccion=f_temp_secctranv.gid)inner join f_temp_partes on  " +
        "f_temp_secctranv.idrio=f_temp_partes.idrio where f_temp_partes.gid1 ="+gid;
		
		db.ejecutarConsulta(sql);
		System.out.println(sql);
		
		int intrsctVal = 0;
		for(int j=0; j<db.getRowCount(); j++)
		{
		 //Valor de interseccion de los 1ros puntos de digitalizacion de las lineas
		 if(db.getValueAt(j, 0).toString().equals("false"))
		  intrsctVal--;	
		 else
		  intrsctVal++;	
		}
		
	  return intrsctVal;	
	}
	
	/**Inserta un lado de la faja en su tabla correspondiente*/
	public static int InsertarFaja(/*int idfaja, */String orilla, double ancho, int idparte, int idSplitt, JDBCAdapter db)
	{
     String sql = "";
     
     //DecimalFormat dc = new DecimalFormat("##.##");
     ancho = Math.round(ancho);
     
	 if(idSplitt > 0 ) //si se paso un splitt
	 {	 
	  sql = "insert into fajas(hidrografia,nombre_lugar,orilla,ancho,the_geom) " +
      "select hidrografia.id ,'','"+orilla+"',"+ancho+",st_multi(st_union(f_temp_partes.the_geom, f_temp_splitt.the_geom)) " +
	  "from ((f_temp_partes inner join f_temp_splitt on f_temp_partes.idrio=f_temp_splitt.idrio)inner join rios on f_temp_partes.idrio=rios.gid) " +
	  "inner join hidrografia on rios.tipo_hidrografia=hidrografia.id where f_temp_partes.gid1="+ idparte +" and f_temp_splitt.gid="+ idSplitt;	 
	 } 
	 else //Si no se paso ningun splitt es que no hay que unirlo
	 {
	   sql = "insert into fajas(hidrografia,nombre_lugar,orilla,ancho,the_geom) " +
       "select hidrografia.id ,'','"+orilla+"',"+ancho+",st_multi(f_temp_partes.the_geom) " +
	   "from (f_temp_partes inner join rios on f_temp_partes.idrio=rios.gid)inner join hidrografia on rios.tipo_hidrografia=hidrografia.id " +
	   "where f_temp_partes.gid1="+ idparte;	 
	 }
	 db.ejecutarConsulta(sql);	
	 db.ejecutarConsulta("select max(gid) from fajas");
	 if(!db.isEmpty())
	 {	 
	  int gid = db.getValueAsInteger(0,0);
	  sql = "insert into suelos_fajas(suelo,faja) select _suelos.gid, fajas.gid " +
		"from _suelos inner join fajas on st_intersects(_suelos.the_geom,fajas.the_geom) where fajas.gid="+gid;
	  db.ejecutarConsulta(sql);
	  
	  db.ejecutarConsulta("select ST_AsText(the_geom), st_srid(the_geom) from fajas where gid="+gid);
	  String geomInfo = "GeometryFromText('"+db.getValueAsString(0,0)+"', "+db.getValueAsString(0,1)+")";
//	  PersistentGeometricObject.validarGeometria(db, geomInfo, gid, "fajas", FShape.POLYGON);
		 
	  TrazasManager.insertar_Traza("Creó la faja con identificador "+gid);
//	  sql = "insert into fajas_pendientes(faja,pendiente) select fajas.gid,pendientes.gid " +
//		"from pendientes inner join fajas on st_intersects(pendientes.the_geom,fajas.the_geom) where fajas.gid="+gid;
//      db.executeQuery(sql);

      return gid;
	 } 
	 
	 return -1;
	}
	
	/**Procedimiento para crear las lineas de extensiones de los rios*/
    public static void Extender_Líneas(FLayer layer, double extdLinesLeng/*, boolean MemoryLayer*/, JDBCAdapter db)
    {
    	if(db == null)
    	 db = new JDBCAdapter(ConnectionExt.getConexionActiva());
        
        //Hallo las extensiones de los rios para picar el buffer
        FLyrVect lines = (FLyrVect)AlgUtils.getExtendedLines(layer, extdLinesLeng, null);//maxvalue+(maxvalue/3)
        DBUtils.saveToPostGIS(lines, "f_temp_riverext", false, true);
        
       //uno los extremos de un mismo rio en unasola entidad
        db.ejecutarConsulta("select distinct gid from f_temp_riverext");
        //JDBCAdapter dbaux = new JDBCAdapter(Global.GetUrl(), "org.postgresql.Driver", Global.GetUser(), Global.GetPass());
        
        String gids = "";
        for(int i=0; i<db.getRowCount(); i++)
         gids += db.getValueAt(i,0).toString()+",";
        
        gids = gids.substring(0, gids.length()-1);
        db.ejecutarConsulta("select gid, st_union(f_temp_riverext.the_geom) as geom into f_temp from f_temp_riverext " +
   	     "where f_temp_riverext.gid in("+ gids +") Group by gid ");	 
        
        db.ejecutarConsulta("delete from f_temp_riverext");
        db.ejecutarConsulta("select * from f_temp");
         //Si es una capa en memoria no se crean todos los campos de la capa hidrografia
         String cadena = "";
//         if(!MemoryLayer)
//          cadena = "(gid, tipo_hidro, ancho, clasrio, nombre, cuenca, tipohidro, the_geom) (select gid,'',0,'','',0,'',geom from f_temp)";   
//         else
          cadena = "(gid, the_geom) (select gid, geom from f_temp)";	  
         
          db.ejecutarConsulta("insert into f_temp_riverext "+ cadena);
          db.ejecutarConsulta("drop table f_temp");
    }
    
    public static String query_Crear_Tablas_Temporales()
    {
    	String abrev = Global.projeccionActiva.getAbrev();
    	String srid = abrev.substring(5, abrev.length());
    	
    	return "CREATE TABLE f_temp_points" +
  		"( gid serial NOT NULL,idseccion integer,the_geom geometry," +
  		"CONSTRAINT f_temp_points_pkey PRIMARY KEY (gid)," +
  		"CONSTRAINT enforce_dims_the_geom CHECK (ndims(the_geom) = 2)," +
  		"CONSTRAINT enforce_geotype_the_geom CHECK (geometrytype(the_geom) = 'POINT'::text OR the_geom IS NULL)," +
  		"CONSTRAINT enforce_srid_the_geom CHECK (srid(the_geom) = "+ srid +"))" +
  		"WITH (OIDS=FALSE); ALTER TABLE f_temp_points OWNER TO postgres;" +
  		"CREATE TABLE f_temp_dif(" +
  		"gid serial NOT NULL,the_geom geometry,idrio integer,CONSTRAINT f_temp_dif_pkey PRIMARY KEY (gid)," +
  		"CONSTRAINT enforce_dims_the_geom CHECK (ndims(the_geom) = 2)," +
  		"CONSTRAINT enforce_geotype_the_geom CHECK (geometrytype(the_geom) = 'MULTIPOLYGON'::text OR the_geom IS NULL)," +
  		"CONSTRAINT enforce_srid_the_geom CHECK (srid(the_geom) = "+ srid +")) " +
  		"WITH (OIDS=FALSE); ALTER TABLE f_temp_dif OWNER TO postgres;";
    }
    
    public static String query_Eliminar_Tablas_Temporales()
    {
    	return "DROP TABLE if exists f_temp_points;" +
              "DROP TABLE if exists f_temp_buffer;" +
	          "DROP TABLE if exists f_temp_dif;" +
	          "DROP TABLE if exists f_temp_partes;" +
	          "DROP TABLE if exists f_temp_riverext;" +
	          "DROP TABLE if exists f_temp_secctranv;" +
	          "DROP TABLE if exists f_temp_splitt;";
    }
}
