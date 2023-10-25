package org.geocuba.foresta.reportes;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import javax.swing.JOptionPane;

import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.gui.pGestionProyectosManager;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import org.geocuba.foresta.herramientas.utiles.Global;
import org.geocuba.foresta.herramientas.utiles.PrintReport;
import org.geocuba.foresta.herramientas.writerTasks.BackgroundExecution;
import org.geocuba.foresta.reportes.gui.ConexionSIFOMAPDlgManager;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.project.documents.view.gui.View;
 
public class Fajas_ReportsExt extends Extension{
	
	private View vista = null;
	//private FLayer layer = null;
	private String ftable = null;
	
	public void initialize() {
	}

	public void execute(String actionCommand)
	{
		if (actionCommand.compareTo("fajas_catastro") == 0)
	    {
		 showFajasCatastro(); //ok
	    }
		
		if (actionCommand.compareTo("fajas_tsuelos_esp") == 0)
	    {
		 showFajas_SuelosEspecies(); //ok
	    }
		
		if (actionCommand.compareTo("fajas_tuso_suelo") == 0)
	    {
		 showFajas_UsoSuelos();
	    }
		
		if (actionCommand.compareTo("proy_ref") == 0)
	    {
		 showProyectoReforestacion();
	    }
		
		if (actionCommand.compareTo("potencial") == 0)
	    {
		 showPotencial_fajas();
	    }
		
		if (actionCommand.compareTo("rodales") == 0)
	    {
		 reporte_Rodales();
	    }
		
	};
	
/** ====== Muestra la informacion del catastro para las fajas seleccionadas ======= */	
	private void showFajasCatastro()
	{
		vista = VistaManager.GetActiveView();
		FLayer []layers = vista.getMapControl().getMapContext().getLayers().getActives();
		
		if(layers[0] == null)
		{ 
		 JOptionPane.showMessageDialog(null, "No se obtuvo la capa de fajas");
		 return;
		}	
		
		if(layers[0].getName().equals("Fajas"))
		 ftable = "fajas";
		else
	     ftable = "fajas_real";	
		
		//Obtengo los gid de los elementos seleccionados
		String ids = Funciones_Utiles.GetSelectedGids(layers[0]);
		
       if(!ids.equals(""))
       {	   
    	JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva()); 
    	String table = "";
    	String geometricInfo = "";
    	String valor = "";
    	
    	db.ejecutarConsulta("select distinct tipo_hidrografia.tipo_elemento from (tipo_hidrografia inner join hidrografia on tipo_hidrografia.id=hidrografia.tipo_hidrografia)" +
    			        "inner join fajas on fajas.hidrografia=hidrografia.id where fajas.gid in("+ids+")");
    	if(!db.isEmpty())
    	{
    	 if(db.getRowCount() == 1)
    	 {
    	  if(db.getValueAsString(0,0).equals("rio"))
    	  {	  
    	   table = "rios";
    	   geometricInfo = " round(CAST(ST_Length(rios.the_geom) as numeric),2) as long_perim ";
    	   valor = "'Longitud'";
    	  } 
    	  else
    	  {	  
    	   table = "embalses";
    	   geometricInfo = "round(CAST(Sum(ST_Area(embalses.the_geom)/10000) as numeric),2) as long_perim ";
    	   valor = "'Perímetro'";
    	  } 
    	 }
    	 else
    	 {
          JOptionPane.showMessageDialog(null, "Se seleccionaron fajas de diferentes elementos hidrográficos");
          return;
    	 }	 
    	}	
    	else
    	 return;	
    	
        String query = "select fajas.nombre_lugar as faja, fajas.gid as id, provincias.nombre as provincia, municipios.nombre as municipio, hidrografia.nombre as rio_canal_embalse, " +
        		geometricInfo+", fajas.orilla, round(CAST(fajas.ancho as numeric),2) as ancho, parcelas.nombre as parcela, 0 as div, parcelas.zc, parcelas.poseedor as nombre, usos_catastro.descripcion_uso as descuso," +
        		"round(CAST(Sum(ST_Area(st_intersection(parcelas.the_geom, fajas.the_geom))/10000) as numeric),2) AS area, "+valor+" as valor " +
        		"from (((((fajas inner join hidrografia on fajas.hidrografia=hidrografia.id)left join "+table+" on hidrografia.id="+table+".tipo_hidrografia) left join tipo_hidrografia on " +
        		"hidrografia.tipo_hidrografia=tipo_hidrografia.id) left join parcelas on st_intersects(fajas.the_geom, parcelas.the_geom)left join usos_catastro on parcelas.uso=usos_catastro.id)left join municipios on parcelas.municipio=municipios.gid) " +
        		"left join provincias on municipios.provincia=provincias.gid Where fajas.gid in("+ ids +") Group by  fajas.nombre_lugar, fajas.gid, provincias.nombre, municipios.nombre, " +
        		"hidrografia.nombre, "+table+".the_geom, tipo_hidrografia.tipo_elemento, fajas.orilla, fajas.ancho, usos_catastro.descripcion_uso, parcelas.nombre, parcelas.poseedor, parcelas.zc";
	   
        System.out.println(query);
        
        BackgroundExecution.cancelableBackgroundExecution(new PrintReport("Fajas_Catastro", query, null));
       }
       else
    	  JOptionPane.showMessageDialog(null, "No se obtuvieron elementos"); 		
	}
	
/** ====== Muestra la informacion de las especies por suelos para cada faja ======= */
	private void showFajas_SuelosEspecies()
	{
		vista = VistaManager.GetActiveView();
		FLayer []layers = vista.getMapControl().getMapContext().getLayers().getActives();
		
		if(layers[0] == null)
		{ 
		 JOptionPane.showMessageDialog(null, "No se obtuvo la capa Fajas");
		 return;
		}	
		
		if(layers[0].getName().equals("Fajas"))
		 ftable = "fajas";
		else
		 ftable = "fajas_real";	
		
//		JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
//		db.executeQuery("select * from f_tesptsuelo");
//		if(db.getRowCount()>444)
//		 db.executeQuery("select distinct ctipo, especie into esp from f_tesptsuelo order by ctipo;" +
//				        "delete from f_tesptsuelo;" +
//				        "insert into f_tesptsuelo(ctipo, especie) select ctipo, especie from esp;" +
//				        "drop table if exists esp;");
		
		//Obtengo los gid de los elementos seleccionados
		String ids = Funciones_Utiles.GetSelectedGids(layers[0]);
		
       if(!ids.equals(""))
       {	   
        String query = "select cuencas.nombre as cuenca, fajas.nombre_lugar as faja, hidrografia.nombre as hidrografia, " +
        		"tipo_hidrografia.descripcion as tipo_elemento, tipo_suelo.tipo as suelo, tipo_suelo.clave, " +
        		"round(CAST(Sum(ST_Area(st_intersection(_suelos.the_geom,  fajas .the_geom))/10000) as numeric),2) AS area, " +
        		"especies.ncomun as especie from (((((((fajas inner join hidrografia on fajas.hidrografia=hidrografia.id)" +
        		"inner join tipo_hidrografia on hidrografia.tipo_hidrografia=tipo_hidrografia.id)inner join suelos_fajas " +
        		"on suelos_fajas.faja=fajas.gid)inner join _suelos on suelos_fajas.suelo=_suelos.gid)inner join tipo_suelo" +
        		" on _suelos.tipo=tipo_suelo.id)inner join tipo_suelo_especies on tipo_suelo.id=tipo_suelo_especies.tipo_suelo)" +
        		"inner join especies on tipo_suelo_especies.especie=especies.siglas)inner join cuencas on " +
        		"hidrografia.cuenca=cuencas.gid where fajas.gid in ("+ids+") Group By cuencas.nombre, fajas.nombre_lugar, hidrografia.nombre, " +
        		"tipo_hidrografia.descripcion, tipo_suelo.tipo, tipo_suelo.clave, especies.ncomun order by tipo_suelo.tipo;";
	    System.out.println(query);
	    
        //PrintReport rep = new PrintReport("Fajas_TiposSuelosEsp", query, null);
	    //rep.start();
        BackgroundExecution.cancelableBackgroundExecution(new PrintReport("Fajas_TiposSuelosEsp", query, null));
       }
       else
    	  JOptionPane.showMessageDialog(null, "No se obtuvieron elementos");	
	}
	
/** ====== Muestra los usos de los suelos para cada faja ======= */
	private void showFajas_UsoSuelos()
	{
		vista = AlgUtils.GetView(null);
		FLayer []layers = vista.getMapControl().getMapContext().getLayers().getActives();
		
		if(layers[0] == null)
		{ 
		 JOptionPane.showMessageDialog(null, "No se obtuvo la capa de fajas");
		 return;
		}	
		
		if(layers[0].getName().equals("Fajas"))
	     ftable = "dfajas";
		else
	     ftable = "f_fajasreal";
		
		//Obtengo los gid de los elementos seleccionados
		String ids = Funciones_Utiles.GetSelectedGids(layers[0]);
		
       if(!ids.equals(""))
       {	   
        String query = "select hidrografia.id, hidrografia.nombre as hidrografia, case when tipo_hidrografia.tipo_elemento='rio' " +
        		"then 'Río' else 'Embalse' end as tipo_elemento_hidro, case when agrupadores_uso.tipo_uso='Bosques' then " +
        		"'Área protegida' when agrupadores_uso.tipo_uso='Otros' then 'Área no clasificada' else 'Área no protegida' end as clasifarea, " +
        		"usos_catastro.descripcion_uso as descuso, round(CAST(Sum(ST_Area(st_intersection(parcelas.the_geom, fajas.the_geom))/10000) as numeric),2)" +
        		" AS area from ((((fajas inner join parcelas on st_intersects(fajas.the_geom, parcelas.the_geom)) inner join usos_catastro on " +
        		"parcelas.uso=usos_catastro.id)inner join agrupadores_uso on usos_catastro.uso_suelo=agrupadores_uso.id)inner join hidrografia on " +
        		"fajas.hidrografia=hidrografia.id)inner join tipo_hidrografia on tipo_hidrografia.id=hidrografia.tipo_hidrografia " +
        		"Where fajas.gid in("+ids+") " +
        		"Group by hidrografia.id, hidrografia.nombre, tipo_hidrografia.tipo_elemento, case when agrupadores_uso.tipo_uso='Bosques' " +
        		"then 'Área protegida' when agrupadores_uso.tipo_uso='Otros' then 'Área no clasificada' else 'Área no protegida' end, " +
        		"usos_catastro.descripcion_uso Order by hidrografia.id, hidrografia.nombre, tipo_hidrografia.tipo_elemento, case when agrupadores_uso.tipo_uso='Bosques' " +
        		"then 'Área protegida' when agrupadores_uso.tipo_uso='Otros' then 'Área no clasificada' else 'Área no protegida' end, " +
        		"usos_catastro.descripcion_uso";
	   
        System.out.println(query);
        
       // PrintReport rep = new PrintReport("Fajas_TipoUsoSuelos", query, null);
	   // rep.start();
        BackgroundExecution.cancelableBackgroundExecution(new PrintReport("Fajas_TipoUsoSuelos", query, null));
       }
       else
    	  JOptionPane.showMessageDialog(null, "No se obtuvieron elementos"); 
	}
	
/** ====== Muestra el panel de configuracion del reporte de Proyecto de Reforestacion ======= */
	private void showProyectoReforestacion()
	{
		vista = VistaManager.GetActiveView();
		FLayer []layers = vista.getMapControl().getMapContext().getLayers().getActives();
		 
		 if(layers[0] == null)
			{ 
			 JOptionPane.showMessageDialog(null, "No se obtuvo la capa Fajas");
			 return;
			}	
		 
		 if(layers[0].getName().equals("Fajas"))
		  ftable = "fajas";
		 else
		  ftable = "fajas_real";
		 
		 //Obtengo los gid de los elementos seleccionados
		 String gids = Funciones_Utiles.GetSelectedGids(layers[0]);
			
	    if(gids.equals(""))
	    {
	 	 JOptionPane.showMessageDialog(null, "Debe seleccionar una faja");
	 	 return;
	    }	   
	   
//	    JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
//	    db.ejecutarConsulta("select distinct hidrografia from fajas where gid in("+ gids +")");
	    
	    if(gids.indexOf(",") > 0) //if(db.getRowCount() > 1)	   
	    {	 
	 	 JOptionPane.showMessageDialog(null, "Debe seleccionar una faja solamente");
	 	 return;
	    }
	    
	    pGestionProyectosManager.mostrarDialogo(Integer.parseInt(gids));
	    
//	    //String id = db.getValueAt(0,0).toString();
//
//	  //Busco el tipo de hidrografia 
//	    String table = "";
//    	
//    	db.ejecutarConsulta("select distinct tipo_hidrografia.tipo_elemento " +
//    			        "from (tipo_hidrografia inner join hidrografia on tipo_hidrografia.id=hidrografia.tipo_hidrografia)" +
//    			        "inner join fajas on fajas.hidrografia=hidrografia.id where fajas.gid in("+gids+")");
//    	if(!db.isEmpty())
//    	{
//    	 if(db.getRowCount() == 1)
//    	 {
//    	  if(db.getValueAsString(0,0).equals("rio"))
//    	   table = "rios";
//    	  else
//    	   table = "embalses";
//    	 }
//    	 else
//    	 {
//          JOptionPane.showMessageDialog(null, "Se seleccionaron fajas de diferentes elementos hidrográficos");
//          return;
//    	 }	 
//    	}	
//    	else
//    	 return;	
//	    
//	  //Busco el ancho de las orillas
//		db.ejecutarConsulta("select "+ ftable +".orilla, "+ ftable +".ancho from "+ ftable +" where "+ ftable +".gid in("+gids+") Order by orilla");//db.executeQuery("select f_fajas.orilla, f_fajas.ancho from f_fajas where f_fajas.id="+id+" Order by orilla");
//		String ancho = "";
//
//		if(table.equals("rios"))
//		{
//		 if(db.getRowCount()>1)	
//		  ancho = "Ancho de la faja (m). Orilla Izquierda : "+ Math.round(Double.parseDouble(db.getValueAt(1, 1).toString())) +"      Derecha : "+ Math.round(Double.parseDouble(db.getValueAt(0, 1).toString()));
//		 else
//		  ancho = "Ancho de la faja (m). Orilla "+ db.getValueAt(0,0).toString() +" : "+ Math.round(Double.parseDouble(db.getValueAt(0, 1).toString()));	 
//		} 
//		else
//		 ancho = "Ancho de la faja (m) : "+ Math.round(Double.parseDouble(db.getValueAt(0, 1).toString()));
//		
//	 //Busco el area de la faja
//	     db.ejecutarConsulta("select round(CAST(sum(st_area("+ ftable +".the_geom)/10000) as numeric),2) from "+ ftable +" where "+ ftable +".gid in("+gids+")");
//	     double area = 0;
//	     if(!db.isEmpty())
//	      area = db.getValueAsDouble(0,0);
//	     
//		try {
//			 pProyecto_ReforestacionManager prman = new pProyecto_ReforestacionManager();
//		     Proyecto_Reforestacion proyecto = new Proyecto_Reforestacion();
//		     
//		     FajaManager fajaman = new FajaManager();
//		     Faja faja = fajaman.Cargar_Objeto_BD(Integer.parseInt(gids));
//			 proyecto.setFaja(faja);
//		     prman.MostrarPanel((PersistentObject)proyecto, table, ancho, area, ftable);
//		     
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//		} catch (ReadDriverException e) {
//			e.printStackTrace();
//		}
	    
//	     pProyecto_Reforestacion proy = new pProyecto_Reforestacion(gids, table, ancho, area, ftable);
//		 proy.Show();
	}
	
	/** ====== Muestra las hectáreas que ocupan las fajas seleccionadas ======= */
	private void showPotencial_fajas()
	{
		vista = VistaManager.GetActiveView();
		FLayer []layers = vista.getMapControl().getMapContext().getLayers().getActives();
		
		if(layers[0] == null)
		{ 
		 JOptionPane.showMessageDialog(null, "No se obtuvo la capa de fajas");
		 return;
		}	
		
		if(layers[0].getName().equals("Fajas"))
	     ftable = "fajas";
		else
	     ftable = "fajas_real";
		
		//Obtengo los gid de los elementos seleccionados
		String ids = Funciones_Utiles.GetSelectedGids(layers[0]);
		
       if(!ids.equals(""))
       {	   
        String query = "select cuencas.nombre as cuenca,"+ ftable +".hidrografia as id, "+ ftable +".nombre_lugar as faja," +
        		"provincias.nombre as provincia,municipios.nombre as municipio,"+ ftable +".orilla,"+ ftable +".ancho, " +
        		"round(CAST(Sum(ST_Area("+ ftable +".the_geom)/10000) as numeric),4) AS area from (("+ ftable +" inner join " +
        		"cuencas on st_intersects("+ ftable +".the_geom, cuencas.the_geom))inner join municipios on " +
        		"st_intersects("+ ftable +".the_geom, municipios.the_geom))inner join provincias on municipios.provincia=provincias.gid " +
        		"where "+ ftable +".gid in ("+ids+") group by cuencas.nombre,provincias.nombre,municipios.nombre," +
        		ftable +".hidrografia, "+ ftable +".nombre_lugar,"+ ftable +".orilla,"+ ftable +".ancho " +
        		"order by cuencas.nombre,provincias.nombre,municipios.nombre,"+ ftable +".hidrografia, area";
	   
        System.out.println(query);
        
        BackgroundExecution.cancelableBackgroundExecution(new PrintReport("Potencial_Fajas", query, null));
       }
       else
    	  JOptionPane.showMessageDialog(null, "No se obtuvieron elementos"); 
	}
	
	/** ====== Muestra la superficie de protección en la ordenación ======= */
	private void reporte_Rodales()
	{
		vista = VistaManager.GetActiveView();
		FLayer []layers = vista.getMapControl().getMapContext().getLayers().getActives();
		
		if(layers[0] == null)
		{ 
		 JOptionPane.showMessageDialog(null, "No se obtuvo la capa de fajas");
		 return;
		}	
		
		if(layers[0].getName().equals("Fajas"))
	     ftable = "fajas";
		else
	     ftable = "fajas_real";
		
		//Obtengo los gid de los elementos seleccionados
		String ids = Funciones_Utiles.GetSelectedGids(layers[0]);
		
       if(!ids.equals(""))
       {	   
		Connection conn = ConexionSIFOMAPDlgManager.mostrar_DialogoConexion();
		
		if(conn != null)
		{
			JDBCAdapter db = new JDBCAdapter(conn);
			String sql = "select _fajas.the_geom into fajas FROM dblink('dbname="+Global.postgresDBName+" port="+Global.PostgresPort+" host="+Global.PostgresServer+" " +
			"user="+Global.PostgresUser+" password="+Global.PostgresPw+"','select fajas.the_geom from fajas where gid in("+ids+")') AS _fajas(" +
			"the_geom geometry)";
			db.ejecutarConsulta(sql);
			
			System.out.println(sql);
				
			String query = "select Rodales.numero as rodal, lotes.numero as lote, unidadessilvicolas.nombre as us, empresas.nombre as empresa," +
					"round(CAST(ST_Area(rodales.the_geom)/10000 as numeric),2) as arearodal, case Rodales.especie when null then 'Indeterminada' " +
					"else rodales.especie end as especie, round(cast(sum(st_area(st_intersection(rodales.the_geom, fajas.the_geom))/10000) as numeric),2) " +
					"as area from (((rodales inner join lotes on rodales.lote=lotes.gid)inner join unidadessilvicolas on " +
					"lotes.unidadsilvicola=unidadessilvicolas.gid) inner join empresas on unidadessilvicolas.empresa=empresas.gid) inner join " +
					"fajas on st_intersects(rodales.the_geom, fajas.the_geom) group by Rodales.numero, lotes.numero, unidadessilvicolas.nombre, " +
					"empresas.nombre, round(CAST(ST_Area(rodales.the_geom)/10000 as numeric),2), case Rodales.especie when null then 'Indeterminada' " +
					"else rodales.especie end";
			
			System.out.println(query);
			
			BackgroundExecution.cancelableBackgroundExecution(new PrintReportRodales("FajasRodales", query, conn, null));
		}
       }
       else
    	  JOptionPane.showMessageDialog(null, "No se obtuvieron elementos"); 
	}
	
   /**Clase que lanza el reporte*/	
	private class PrintReportRodales extends PrintReport
	{
		    public PrintReportRodales(String ReportName, String sql, Connection _conexion, Map manualParameters) 
		    { 
		   	 reportName = ReportName;
		   	 Sqlquery = sql;
		   	 conexion = _conexion;
		   	 
		   	 if(manualParameters != null)
		   		parameters = manualParameters;	 
	         //paramNames = pNames;
	        // paramValues = pValues;
		   	setInitialStep(0);
			setDeterminatedProcess(false);
			setStatusMessage(PluginServices.getText(this, "Generando reporte..."));
			setFinalStep(1);
		    } 
		    
		    public void finished() 
		    {
				PluginServices.getMainFrame().enableControls();
				JDBCAdapter db = new JDBCAdapter(conexion);
				db.ejecutarConsulta("drop table if exists fajas");
			} 
	}
	
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
			 
		   if(layers[0].getName().equals("Fajas"))//|| layers[0].getName().equals("Fajas_Real"))
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

