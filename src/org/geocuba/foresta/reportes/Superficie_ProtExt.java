package org.geocuba.foresta.reportes;

import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import org.cresques.cts.IProjection;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.geocuba.foresta.herramientas.utiles.Global;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import org.geocuba.foresta.administracion_seguridad.db.CargadorCapas;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.herramientas.utiles.PrintReport;
import org.geocuba.foresta.herramientas.writerTasks.BackgroundExecution;
import com.hardcode.driverManager.DriverLoadException;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.swing.threads.AbstractMonitorableTask;
import es.unex.sextante.core.Sextante;
import es.unex.sextante.exceptions.GeoAlgorithmExecutionException;
 
public class Superficie_ProtExt extends Extension{
	private View vista = null;
	public void initialize() {
	}

	public void execute(String actionCommand) {
		
//		if (actionCommand.compareTo("sup_prot") == 0)
//	    {
//			vista = AlgUtils.GetView(null);
//			FLayer layer = vista.getMapControl().getMapContext().getLayers().getLayer("Cuencas_Interés_Nacional");
//			
//			if(layer == null)
//			{ 
//			 JOptionPane.showMessageDialog(null, "No se obtuvo la capa de fajas");
//			 return;
//			}	
//			
//			//Obtengo los gid de los elementos seleccionados
//			String ids = Funciones_Utiles.GetSelectedGids(layer);
//			
//		    if(ids.indexOf(",") != -1)  //Si tiene coma es porque hay mas de un gid	   
//		    {	 
//		     JOptionPane.showMessageDialog(null, "Debe seleccionar una cuenca solamente");
//		     return;
//		    }	   
//			
//	       if(!ids.equals(""))
//	       {	
//	    	  BackgroundExecution.cancelableBackgroundExecution(new SupProteccionRodales(ids, vista.getProjection()));
//	       }
//	       else
//	    	  JOptionPane.showMessageDialog(null, "No se obtuvieron elementos");
//	    }
	
    };
	
    /*
    private class superficieProtWT extends AbstractMonitorableTask 
    {
    	 protected String id;
    	 
    	public superficieProtWT(String ids)
    	{
   	   	    id = ids;
   	   	    
    		setInitialStep(0);
    		setDeterminatedProcess(false);
    		setStatusMessage(PluginServices.getText(this, "Generando reporte..."));
    		setFinalStep(1);
    	}

		public void run() throws Exception {
		 doProcess();
		}
		
		protected void doProcess()
		{
			updateReportTable(id);
	        String query = "select * from f_superficieprot";
	        PrintReport print = new PrintReport("Superficie_Proteccion", query, null);
	        try {
				print.run();
			} catch (Exception e) {
				e.printStackTrace();
			}
	        reportStep();	
		}
    }
    */
    
    /**Actualiza la tabla que brinda los datos del reporte*/
//    private void updateReportTable(String ids)
//    {
//    	//Nombres de las tablas hidrograficas con sus tipos para las condicionales del sql   
//    	String []tipohid = {"'Embalse'", "'Microembalse'"};//, "'Río' or f_hidlineal.tipo='Canal'"}};   
//    	JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
//    	JDBCAdapter dbaux = new JDBCAdapter(ConnectionExt.getConexionActiva());
//    	
//    	db.ejecutarConsulta("delete from f_superficieprot");
//    	for(int i=0; i<tipohid.length; i++)
//    	{
//    	//Inserto los datos de los embalses
//    	 db.ejecutarConsulta("insert into f_superficieprot(cuenca,provincia,municipio,tipohidro,cant,totalarea) " +
//    	  "(select f_cuencas.nombre, provincias.nombre as provincia, municipios.nombre as municipio, f_hidareal.tipo, " +
//    	  "count(f_hidareal.tipo) as cant, round(CAST(Sum(ST_Area(f_fajas.the_geom)/10000) as numeric),2) AS areatot " +
//    	  "from ((((f_cuencas inner join f_fajas on f_cuencas.gid=f_fajas.cuenca)inner join f_hidareal on f_fajas.orilla='embalse' " +
//    	  "and f_hidareal.gid=f_fajas.idrio)inner join municipios on f_fajas.codmun=municipios.codigo)inner join provincias on f_fajas.codprov=provincias.codigo) " +
//    	  "where f_cuencas.gid="+ids+" and f_hidareal.tipo="+ tipohid[i] +" group by f_cuencas.nombre, provincias.nombre, municipios.nombre, f_hidareal.tipo " +
//    	  "Order by f_cuencas.nombre, provincias.nombre, municipios.nombre, f_hidareal.tipo)");
//    	 
//      //Actualizo el valor de las areas protegidas
//    	 db.executeQuery("select provincias.nombre as provincia, municipios.nombre as municipio, " +
//    	  "round(CAST(Sum(ST_Area(st_intersection(f_parcelas.the_geom, f_fajas.the_geom))/10000) as numeric),2) as proteg " +
//    	  "from (((((f_cuencas inner join f_fajas on f_cuencas.gid=f_fajas.cuenca)inner join f_hidareal on st_Intersects(f_hidareal.the_geom, f_fajas.the_geom)" +
//    	  " and f_hidareal.gid=f_fajas.idrio)inner join municipios on f_fajas.codmun=municipios.codigo)inner join provincias " +
//    	  "on f_fajas.codprov=provincias.codigo)left join f_parcelas on st_intersects(f_fajas.the_geom, f_parcelas.the_geom))" +
//    	  "left join f_tusoscat on (f_tusoscat.tsup=CAST(f_parcelas.tiposup as numeric) and f_tusoscat.tuso=CAST(f_parcelas.tipouso as numeric) " +
//    	  "and f_tusoscat.coduso=f_parcelas.coduso) where f_cuencas.gid="+ids+" and f_hidareal.tipo="+ tipohid[i] +" and f_tusoscat.reforestar='No reforestar' " +
//    	  "group by provincias.nombre, municipios.nombre, f_hidareal.tipo, f_tusoscat.reforestar " +
//    	  "Order by provincias.nombre, municipios.nombre, f_hidareal.tipo");
//    	 
//    	 if(!db.isEmpty()) 
//    	  for(int j=0; j<db.getRowCount(); j++)
//    	   dbaux.executeQuery("update f_superficieprot set protegida="+db.getValueAt(j,2).toString()+" where provincia='"+db.getValueAt(j,0).toString()+"' and municipio='"+db.getValueAt(j,1).toString()+"' and tipohidro="+tipohid[i]); 	 
//    	 
//    		
//         //Actualizo el valor del marabu
//       	 db.executeQuery("select provincias.nombre as provincia, municipios.nombre as municipio, " +
//       	  "round(CAST(Sum(ST_Area(st_intersection(f_parcelas.the_geom, f_fajas.the_geom))/10000) as numeric),2) as marabu " +
//       	  "from (((((f_cuencas inner join f_fajas on f_cuencas.gid=f_fajas.cuenca)inner join f_hidareal on st_Intersects(f_hidareal.the_geom, f_fajas.the_geom)" +
//       	  " and f_hidareal.gid=f_fajas.idrio)inner join municipios on f_fajas.codmun=municipios.codigo)inner join provincias " +
//       	  "on f_fajas.codprov=provincias.codigo)left join f_parcelas on st_intersects(f_fajas.the_geom, f_parcelas.the_geom))" +
//       	  "left join f_tusoscat on (f_tusoscat.tsup=CAST(f_parcelas.tiposup as numeric) and f_tusoscat.tuso=CAST(f_parcelas.tipouso as numeric) " +
//       	  "and f_tusoscat.coduso=f_parcelas.coduso) where f_cuencas.gid="+ids+" and f_hidareal.tipo="+ tipohid[i] +" and f_tusoscat.reforestar='Reforestar' and f_tusoscat.coduso='201400'" +
//       	  "group by provincias.nombre, municipios.nombre, f_hidareal.tipo, f_tusoscat.reforestar " +
//       	  "Order by provincias.nombre, municipios.nombre, f_hidareal.tipo");
//       	 
//       	 if(!db.isEmpty()) 
//       	  for(int j=0; j<db.getRowCount(); j++)
//       	   dbaux.executeQuery("update f_superficieprot set marabu="+db.getValueAt(j,2).toString()+" where provincia='"+db.getValueAt(j,0).toString()+"' and municipio='"+db.getValueAt(j,1).toString()+"' and tipohidro="+tipohid[i]);
//       	 
//       //Hallo el valor de la superficie forestal para luego sumarlo con el marabu, y el resultado restarselo al area total
//       	 db.executeQuery("select provincias.nombre as provincia, municipios.nombre as municipio, " +
//       	  "round(CAST(Sum(ST_Area(st_intersection(f_parcelas.the_geom, f_fajas.the_geom))/10000) as numeric),2) as forestal " +
//       	  "from (((((f_cuencas inner join f_fajas on f_cuencas.gid=f_fajas.cuenca)inner join f_hidareal on st_Intersects(f_hidareal.the_geom, f_fajas.the_geom)" +
//       	  " and f_hidareal.gid=f_fajas.idrio)inner join municipios on f_fajas.codmun=municipios.codigo)inner join provincias " +
//       	  "on f_fajas.codprov=provincias.codigo)left join f_parcelas on st_intersects(f_fajas.the_geom, f_parcelas.the_geom))" +
//       	  "left join f_tusoscat on (f_tusoscat.tsup=CAST(f_parcelas.tiposup as numeric) and f_tusoscat.tuso=CAST(f_parcelas.tipouso as numeric) " +
//       	  "and f_tusoscat.coduso=f_parcelas.coduso) where f_cuencas.gid="+ids+" and f_hidareal.tipo="+ tipohid[i] +" and (f_tusoscat.coduso='230000' or f_tusoscat.coduso='230100' or f_tusoscat.coduso='230150')" +
//       	  "group by provincias.nombre, municipios.nombre, f_hidareal.tipo, f_tusoscat.reforestar " +
//       	  "Order by provincias.nombre, municipios.nombre, f_hidareal.tipo");
//       	 
//       	 if(!db.isEmpty()) 
//       	  for(int j=0; j<db.getRowCount(); j++)
//       	  {
//       	   dbaux.executeQuery("update f_superficieprot set deforestada = totalarea-("+Double.parseDouble(db.getValueAt(j,2).toString())+"+marabu) where provincia='"+db.getValueAt(j,0).toString()+"' and municipio='"+db.getValueAt(j,1).toString()+"' and tipohidro="+tipohid[i]);
//       	   dbaux.executeQuery("update f_superficieprot set totalref = deforestada + marabu where provincia='"+db.getValueAt(j,0).toString()+"' and municipio='"+db.getValueAt(j,1).toString()+"' and tipohidro="+tipohid[i]);
//    	  }	
//    	}
//    ///////////////////////////////////////////////////////////////////////////////////////////////	
//    	//Inserto los datos de los rios
//    	db.executeQuery("insert into f_superficieprot(cuenca,provincia,municipio,tipohidro,cant,totalarea) " +
//    	 "(select f_cuencas.nombre, provincias.nombre as provincia, municipios.nombre as municiprio, f_hidlineal.tipo, count(f_hidlineal.tipo) as cant, " +
//    	 "round(CAST(Sum(ST_Area(f_fajas.the_geom)/10000) as numeric),2) AS areatot	from ((((f_cuencas inner join f_fajas on f_cuencas.gid=f_fajas.cuenca)inner join " +
//    	 "f_hidlineal on f_hidlineal.gid=f_fajas.idrio and f_fajas.orilla<>'embalse')inner join	municipios on f_fajas.codmun=municipios.codigo)inner join provincias on " +
//    	 "f_fajas.codprov=provincias.codigo) where f_cuencas.gid="+ids+" and f_hidlineal.tipo='Río' or f_hidlineal.tipo='Canal' group by f_cuencas.nombre, provincias.nombre, " +
//    	 "municipios.nombre, f_hidlineal.tipo Order by f_cuencas.nombre, provincias.nombre, municipios.nombre, f_hidlineal.tipo)");
//    		
//       //Actualizo el valor de las areas protegidas de los rios
//   	    db.executeQuery("select provincias.nombre as provincia, municipios.nombre as municipio, round(CAST(Sum(ST_Area(st_intersection(f_parcelas.the_geom, f_fajas.the_geom))/10000) as numeric),2) as proteg " +
//   	     "from (((((f_cuencas inner join f_fajas on f_cuencas.gid=f_fajas.cuenca)inner join f_hidlineal on f_hidlineal.gid=f_fajas.idrio)inner join municipios on " +
//   	     "f_fajas.codmun=municipios.codigo)inner join provincias on f_fajas.codprov=provincias.codigo)left join f_parcelas on st_intersects(f_fajas.the_geom, f_parcelas.the_geom))left join " +
//   	     "f_tusoscat on (f_tusoscat.tsup=CAST(f_parcelas.tiposup as numeric) and f_tusoscat.tuso=CAST(f_parcelas.tipouso as numeric) and f_tusoscat.coduso=f_parcelas.coduso) " +
//   	     "where f_cuencas.gid="+ids+" and (f_hidlineal.tipo='Río' or f_hidlineal.tipo='Canal') and f_tusoscat.reforestar='No reforestar'	group by provincias.nombre, " +
//   	     "municipios.nombre, f_hidlineal.tipo, f_tusoscat.reforestar Order by provincias.nombre, municipios.nombre, f_hidlineal.tipo");
//   	 
//   	    if(!db.isEmpty())
//   	     for(int j=0; j<db.getRowCount(); j++)
//   	      dbaux.executeQuery("update f_superficieprot set protegida="+db.getValueAt(j,2).toString()+" where provincia='"+db.getValueAt(j,0).toString()+"' and municipio='"+db.getValueAt(j,1).toString()+"' and tipohidro='Río'");
//
//   	   //Actualizo el valor de marabu de los rios
//   	     db.executeQuery("select provincias.nombre as provincia, municipios.nombre as municipio, round(CAST(Sum(ST_Area(st_intersection(f_parcelas.the_geom, f_fajas.the_geom))/10000) as numeric),2) as marabu " +
//   	      "from (((((f_cuencas inner join f_fajas on f_cuencas.gid=f_fajas.cuenca)inner join f_hidlineal on f_hidlineal.gid=f_fajas.idrio)inner join municipios on f_fajas.codmun=municipios.codigo)inner join provincias " +
//   	      "on f_fajas.codprov=provincias.codigo)left join f_parcelas on st_intersects(f_fajas.the_geom, f_parcelas.the_geom))left join f_tusoscat on (f_tusoscat.tsup=CAST(f_parcelas.tiposup as numeric) and " +
//   	      "f_tusoscat.tuso=CAST(f_parcelas.tipouso as numeric) and f_tusoscat.coduso=f_parcelas.coduso) where f_cuencas.gid="+ids+" and (f_hidlineal.tipo='Río' or f_hidlineal.tipo='Canal') and " +
//   	      "f_tusoscat.reforestar='Reforestar' and f_tusoscat.coduso='201400' group by provincias.nombre, municipios.nombre, f_hidlineal.tipo, f_tusoscat.reforestar " +
//   	      "Order by provincias.nombre, municipios.nombre, f_hidlineal.tipo");
//   	     
//   	    if(!db.isEmpty())
//    	 for(int j=0; j<db.getRowCount(); j++)
//    	  dbaux.executeQuery("update f_superficieprot set marabu="+db.getValueAt(j,2).toString()+" where provincia='"+db.getValueAt(j,0).toString()+"' and municipio='"+db.getValueAt(j,1).toString()+"' and tipohidro='Río'");
//   	     
//   	    //Hallo el valor de la superficie forestal para luego sumarlo con el marabu, y el resultado restarselo al area total
//  	     db.executeQuery("select provincias.nombre as provincia, municipios.nombre as municipio, round(CAST(Sum(ST_Area(st_intersection(f_parcelas.the_geom, f_fajas.the_geom))/10000) as numeric),2) as marabu " +
//  	      "from (((((f_cuencas inner join f_fajas on f_cuencas.gid=f_fajas.cuenca)inner join f_hidlineal on f_hidlineal.gid=f_fajas.idrio)inner join municipios on f_fajas.codmun=municipios.codigo)inner join provincias " +
//  	      "on f_fajas.codprov=provincias.codigo)left join f_parcelas on st_intersects(f_fajas.the_geom, f_parcelas.the_geom))left join f_tusoscat on (f_tusoscat.tsup=CAST(f_parcelas.tiposup as numeric) and " +
//  	      "f_tusoscat.tuso=CAST(f_parcelas.tipouso as numeric) and f_tusoscat.coduso=f_parcelas.coduso) where f_cuencas.gid="+ids+" and (f_hidlineal.tipo='Río' or f_hidlineal.tipo='Canal') and " +
//  	      "(f_tusoscat.coduso='230000' or f_tusoscat.coduso='230100' or f_tusoscat.coduso='230150') group by provincias.nombre, municipios.nombre, f_hidlineal.tipo, f_tusoscat.reforestar " +
//  	      "Order by provincias.nombre, municipios.nombre, f_hidlineal.tipo");
//  	     
//  	    if(!db.isEmpty())
//   	     for(int j=0; j<db.getRowCount(); j++)
//   	     {	 
//   	      dbaux.executeQuery("update f_superficieprot set deforestada = totalarea-("+Double.parseDouble(db.getValueAt(j,2).toString())+"+marabu) where provincia='"+db.getValueAt(j,0).toString()+"' and municipio='"+db.getValueAt(j,1).toString()+"' and tipohidro='Río'");
//   	      dbaux.executeQuery("update f_superficieprot set totalref = deforestada + marabu where provincia='"+db.getValueAt(j,0).toString()+"' and municipio='"+db.getValueAt(j,1).toString()+"' and tipohidro='Río'");
//   	      dbaux.executeQuery("update f_superficieprot set cant = cant/2 where provincia='"+db.getValueAt(j,0).toString()+"' and municipio='"+db.getValueAt(j,1).toString()+"' and tipohidro='Río'");
//   	     } 
//    }
    
//    private class SupProteccionRodales extends superficieProtWT
//    {
//    	private JDBCAdapter db;
//    	private IProjection proj;
//    	
//		public SupProteccionRodales(String ids, IProjection _proj) {
//			super(ids);
//			proj = _proj;
//		}
//    	
//		protected void doProcess()
//		{
//		 db = new JDBCAdapter(ConnectionExt.getConexionActiva());
//		 
//		 db.executeQuery("drop table if exists f_temp");
//		 db.executeQuery("drop table if exists f_tempp");
//		 
//		 String sql = "select distinct rios.gid, tipo_hidrografia.ancho_faja, rios.the_geom into f_tempp " +
//	 		"from (((rios inner join rodales on st_intersects(rios.the_geom, rodales.the_geom)) inner join hidrografia on " +
//	 		"rios.tipo_hidrografia=hidrografia.id)inner join tipo_hidrografia on hidrografia.tipo_hidrografia=tipo_hidrografia.id)" +
//	 		"inner join cuencas on st_intersects(rodales.the_geom, cuencas.the_geom) where cuencas.gid=" + id;
//		 db.executeQuery(sql);
//		 System.out.println(sql);
//		 
//		 db.executeQuery("insert into f_tempp(gid,ancho_faja,the_geom) select distinct embalses.gid, tipo_hidrografia.ancho_faja, embalses.the_geom " +
//		 		"from (((embalses inner join rodales on st_intersects(embalses.the_geom, rodales.the_geom)) inner join hidrografia on " +
//		 		"embalses.tipo_hidrografia=hidrografia.id)inner join tipo_hidrografia on hidrografia.tipo_hidrografia=tipo_hidrografia.id)" +
//		 		"inner join cuencas on st_intersects(rodales.the_geom, cuencas.the_geom) where cuencas.gid=" + id);
//		 
//		 CargadorCapas cargador = new CargadorCapas();
//		 FLayer l = cargador.cargarTabla("f_tempp", "hidro_temp", 0, "", proj);
//		 //cargador.CloseConnection();
//		
//		 
//	  try {
//		   Sextante.initialize();
//		   FLyrVect res = (FLyrVect)AlgUtils.BufferByField(l, "ancho_faja", true, "buffer1");
//		   
//		   Funciones_Utiles.saveToPostGIS(res, "f_temp", false, true);
//		   
//		   String query = "select provincias.nombre as prov, empresas.nombre as empresa, " +
//	          "unidadessilvicolas.nombre as us, lotes.numero as lote, rodales.numero as rodal, especies.ncomun as especie, " +
//	          "round(CAST(Sum(ST_Area(st_intersection(rodales.the_geom, f_temp.the_geom))/10000) as numeric),2) AS area " +
//	          "from ((((rodales inner join especies on rodales.especie=especies.siglas) inner join f_temp on st_intersects(f_temp.the_geom, rodales.the_geom) " +
//	          "inner join lotes on rodales.lote=lotes.gid)inner join unidadessilvicolas on lotes.unidadsilvicola=unidadessilvicolas.gid)" +
//	          "inner join empresas on unidadessilvicolas.empresa=empresas.gid)inner join provincias on empresas.provincia=provincias.gid " +
//	          "Group By provincias.nombre, empresas.nombre, unidadessilvicolas.nombre, especies.ncomun, lotes.numero, rodales.numero " +
//	          "Order By provincias.nombre, empresas.nombre, unidadessilvicolas.nombre, especies.ncomun, lotes.numero, rodales.numero";
//		   
//	        System.out.println(query);
//	        
//	        
//	        if(isCanceled())
//	         return;
//	        
//	        PrintReport print = new PrintReport("Fajas_RodalesEspecies", query, null);
//			print.run();
//			
//	        reportStep();
//		   
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}catch (GeoAlgorithmExecutionException e) {
//			e.printStackTrace();
//		}  catch (IOException e) {
//			e.printStackTrace();
//		} catch (DriverLoadException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		      reportStep();
//		}
//		
//		public void finished() 
//		{
//			// db.executeQuery("drop table if exists f_temp");
//			// db.executeQuery("drop table if exists f_tempp");
//		}
//    }
    
public boolean isEnabled() {
		
		if(!ConnectionExt.Conectado())
		 return false;
			  
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
		if(f == null)
		 return false;	
		
		if(!(f instanceof View))
		 return false;
		
		FLayer [] layers = ((View)f).getModel().getMapContext().getLayers().getActives();
		 
		 if(layers != null && layers.length == 1)
		 {	
		   if(!layers[0].isVisible() || layers[0].isEditing())
			return false;
			 
		   if(layers[0].getName().equals("Cuencas_Interés_Nacional"))
		    return true;
		   else
			return false;
		 }
		 else
		  return false;	 
		}

	public boolean isVisible() {
		if(!ConnectionExt.Conectado())
			 return false;
				  
			com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
			if(f == null)
			 return false;	
			
			if(!(f instanceof View))
			 return false;
			
	  return true;		
	}

}

