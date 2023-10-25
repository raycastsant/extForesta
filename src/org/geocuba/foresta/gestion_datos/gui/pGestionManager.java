package org.geocuba.foresta.gestion_datos.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.BitSet;
import java.util.HashMap;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.geocuba.foresta.gestion_datos.IPersistenObjectManager;
import org.geocuba.foresta.gestion_datos.PersistentGeometricObject;
import org.geocuba.foresta.gestion_datos.PersistentObject;
import org.geocuba.foresta.gestion_datos.listeners.DibujarPolilineaListener;
import org.geocuba.foresta.gestion_datos.listeners.ModificarGeometriaListener;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.DataSource;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.StartEditing;
import com.iver.cit.gvsig.StopEditing;
import com.iver.cit.gvsig.exceptions.layers.CancelEditingLayerException;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.exceptions.layers.StartEditionLayerException;
import com.iver.cit.gvsig.exceptions.table.CancelEditingTableException;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.fmap.layers.layerOperations.AlphanumericData;
import com.iver.cit.gvsig.project.documents.view.gui.View;

 public class pGestionManager {

	private static pGestion  panel = null;
	private static String    classname;
	private static String    layername;
	private static int       tipoElemento;
	private static boolean   objetoGeometrico;
	private static DibujarPolilineaListener tool;
	private static ModificarGeometriaListener modificarGeomTool;
	private static FLyrVect capa;
	private static int sqloption;
	private static boolean gestionando = false;
	private static IGeometry LastEditedGeometry; //Se encargará de guardar la última geometría editada,
	                                             //independientemente de la herramienta que la haya creado
	//private static HashMap<Integer, Integer> listaindices;   //<GID, INDICE de FILA>
	
	public static final int SQL_FAJAS = 1;
	public static final int SQL_PARTEAGUAS = 2;
	public static final int SQL_CUENCAS_IN = 3;
	public static final int SQL_EMBALSES = 4;
	public static final int SQL_FAJAS_REAL = 5;
	public static final int SQL_MUNICIPIOS = 6;
	public static final int SQL_PARCELAS = 7;
	public static final int SQL_RELIEVE = 8;
	public static final int SQL_RIOS = 9;
	public static final int SQL_SUELOS = 10;
	public static final int SQL_RED_DRENAJE = 20;
	
	public static final int SQL_AGRUPADORES_USO = 11;
	public static final int SQL_ESPECIES = 12;
	public static final int SQL_INTENSIDAD_LLUVIA = 13;
	public static final int SQL_PROVINCIAS = 14;
	public static final int SQL_TEXTURAS = 15;
	public static final int SQL_TIPOS_HIDROGRAFIA = 16;
	public static final int SQL_TIPOS_SUELO = 17;
	public static final int SQL_USOS_CATASTRO = 18;
	public static final int SQL_VELOCIDADES_INFILTRACION = 19;
	
	public static void showPanel_Gestion(int sqlOption, String condition) 
	{
		String title = getPanelTitle(sqlOption);
		panel = (pGestion)UIUtils.GetPanel("_"+title);
		if(panel == null)
		{
		 panel = new pGestion(title);	
		 
		 if(condition == null)
		  condition = "";	 
		 
		 ActualizarTabla(sqlOption, condition);
		 
		 if(objetoGeometrico)
		  capa = (FLyrVect)VistaManager.GetActiveView().getMapControl().getMapContext().getLayers().getLayer(layername);
		 
		 setComponentsListeners();

         sqloption = sqlOption;
         
         if(sqloption == SQL_FAJAS || sqloption == SQL_FAJAS_REAL)
          panel.getButtonAdd().setEnabled(false);
         
		 panel.Show();
		 gestionando = true;
		}
	}
	
	private static void setComponentsListeners()
	{
	 if(panel == null)
	  return;
	
	 //---------------------------------------------------------------------------------
	 panel.getButtonAdd().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) 
         {
          /*Como voy a insertar puedo crear un nuevo objeto cualquiera que sea, 
           * ya que llamo al constructor por defecto*/  	 
         	 
          try {
			PersistentObject persistent = (PersistentObject)Class.forName("org.geocuba.foresta.gestion_datos."+classname).newInstance();
			
			if(persistent instanceof PersistentGeometricObject)
			{
			 View vista = VistaManager.GetActiveView();
			 if(vista == null)
			  JOptionPane.showMessageDialog(null, "Debe existir una Vista creada para crear Geometrías", "ERROR", JOptionPane.ERROR_MESSAGE);
			 else
			 {	 
				 //capa = (FLyrVect)vista.getMapControl().getMapContext().getLayers().getLayer(layername);
				 tool = new DibujarPolilineaListener(vista.getMapControl(), capa, (PersistentGeometricObject)persistent); 
			     CADExtension.addCADTool("drawpoly", tool); 
			     
			     CADExtension.setCADTool("drawpoly",true);
			     vista.hideConsole();
			     setEstadoBotonesGestion(false);
			 }    
			}
			else
			 mostrarFicha_Gestion(persistent);  //Si no es geometrico
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
         }
     });
	 //---------------------------------------------------------------------------------
	 panel.getButtonCerrar().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) 
         {
          panel.Close();	
         }
     });
	//---------------------------------------------------------------------------------
	 panel.getButtonModificar().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) 
         {
			try {
				 PersistentObject persistent = cargarObjetoSeleccionado();
				 persistent.setEditing(true);
				 
				if(persistent instanceof PersistentGeometricObject)
				{
					 View vista = VistaManager.GetActiveView();
					 if(vista == null)
					  JOptionPane.showMessageDialog(null, "Debe existir una Vista creada para crear Geometrías", "ERROR", JOptionPane.ERROR_MESSAGE);
					 else
					 {
						 AlgUtils.DesactivateLayers();
				      	 capa.setActive(true);
				      	 StartEditing se = new StartEditing();
				     	 se.execute("");
				     	 
				     	 IPanelManager pm = mostrarFicha_Gestion(persistent);
						 modificarGeomTool = new ModificarGeometriaListener(((PersistentGeometricObject)persistent).getGid(), pm, capa); 
						 CADExtension.addCADTool("modificarGEom", modificarGeomTool);					     
					     CADExtension.setCADTool("modificarGEom",true);
					     vista.hideConsole();
					     setEstadoBotonesGestion(false);
					 }	
				}
				else
				 mostrarFicha_Gestion(persistent);
					
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (ReadDriverException e) {
				e.printStackTrace();
			}
         }
     });
	//---------------------------------------------------------------------------------
	 panel.getButtonEliminar().addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) 
      {
       Object[] options = { "Si", "No" };
       int result = JOptionPane.showOptionDialog(null, "¿Está seguro de eliminar el elemento seleccionado?", "Advertencia", 
        			 JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
               
       if(result == 0)
       {	   
          try {	 
           int row = panel.getTable().getSelectionModel().getMaxSelectionIndex();
           int id = Integer.parseInt(panel.getTable().getValueAt(row,0).toString());  //la primera columna siempre debe ser el Id o el Gid
           IPersistenObjectManager pom = (IPersistenObjectManager)Class.forName("org.geocuba.foresta.gestion_datos."+classname+"Manager").newInstance();
		
           PersistentObject persisObject = pom.Cargar_Objeto_BD(id);
           persisObject.delete();
           ActualizarTabla(sqloption, "");
           
           if(objetoGeometrico)
            capa.reload();
          
           } catch (InstantiationException e) {
  			e.printStackTrace();
  		   } catch (IllegalAccessException e) {
  			e.printStackTrace();
  		   } catch (ClassNotFoundException e) {
  			e.printStackTrace();
  		   } catch (ReloadLayerException e) {
			e.printStackTrace();
		   }
         }
      }
     });
	//---------------------------------------------------------------------------------
	 panel.getButtonInformacion().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) 
         {
          mostrar_Info();
         }
     });
	//---------------------------------------------------------------------------------
	 panel.getButtonZoom().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) 
         {
          ZoomElemetoSeleccionado();
         }
     });
	//---------------------------------------------------------------------------------
		 panel.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
	         public void valueChanged(ListSelectionEvent e) 
	         {
	         	 PluginServices.getMDIManager().setWaitCursor();
	             
	         	 if(objetoGeometrico)
	         	 {
	         	  activar_seleccion_mapa(e);
	         	  panel.getButtonZoom().setEnabled(true); 
	         	 } 	 

	             panel.getButtonEliminar().setEnabled(true);
	             panel.getButtonModificar().setEnabled(true);
	             panel.getButtonInformacion().setEnabled(true);
	             
	             PluginServices.getMDIManager().restoreCursor();
	         }
	     });
		 
	if(objetoGeometrico) 	
	{	
		//Limpio la selección	 
			 try {
				 AlphanumericData ad = (AlphanumericData)capa;                  	          
				 ad.getRecordset().getSelection().clear();
		        
			} catch (ReadDriverException e1) {
				e1.printStackTrace();
			}
	}	
  //---------------------------------------------------------------------------------
		 panel.getTable().addMouseListener(new MouseAdapter() 
		  {
			public void mouseClicked(MouseEvent e) 
			{
		     if(e.getClickCount() == 2)
		     {	
		      mostrar_Info();       
			 } 
		    }
		  }); 
	}
	
	/**Devuelve el SQL de la ventana de gestión
	 * en dependencia de la tabla deseada
	 * que se especifica en el parametro sqlOption*/
	private static String getSql(int sqlOption, String condition)
	{
	 String sql = "";	
	 switch (sqlOption) 
	 {
	  case SQL_FAJAS:{
		  sql = "select fajas.gid, fajas.nombre_lugar, fajas.orilla, fajas.ancho, tipo_hidrografia.tipo_elemento as tipo_hidrografía, " +
		  		"round(CAST(ST_Area(fajas.the_geom)/10000 as numeric),2) as área_ha, round(CAST(st_perimeter(fajas.the_geom) as numeric),2) as perímetro_metros " +
		  		"from (fajas inner join hidrografia on fajas.hidrografia=hidrografia.id) inner join tipo_hidrografia " +
		  		"on tipo_hidrografia.id=hidrografia.tipo_hidrografia "+condition+" order by fajas.gid";;
		  classname = "Faja";
		  layername = "Fajas";
		  tipoElemento = FShape.POLYGON;
		  objetoGeometrico = true;
		  break;	   	   
	  }
	  case SQL_PARTEAGUAS:{
		  sql = "select parteaguas.gid, descripcion as descripción, cuencas.nombre as Cuenca, round(CAST(ST_Area(parteaguas.the_geom)/10000 as numeric),2) as área_ha," +
		  		"round(CAST(st_perimeter(parteaguas.the_geom) as numeric),2) as perímetro_metros " +
		  		"from parteaguas inner join cuencas on parteaguas.cuenca=cuencas.gid "+condition+" order by parteaguas.gid";;
		  classname = "Parteaguas";
		  layername = "Parteaguas";
		  tipoElemento = FShape.POLYGON;
		  objetoGeometrico = true;
		  break;	   	   
	  }
	  case SQL_CUENCAS_IN:{
		  sql = "select cuencas.gid, cuencas.nombre, round(CAST(ST_Area(cuencas.the_geom)/10000 as numeric),2) as área_ha," +
		  		"round(CAST(st_perimeter(cuencas.the_geom) as numeric),2) as perímetro_metros " +
		  		"from cuencas "+condition+" order by cuencas.gid";;
		  classname = "Cuenca";
		  layername = "Cuencas_Interés_Nacional";
		  tipoElemento = FShape.POLYGON;
		  objetoGeometrico = true;
		  break;	   	   
	  }
	  case SQL_EMBALSES:{
		  sql = "select embalses.gid, tipo_hidrografia.descripcion as descripción, hidrografia.nombre, embalses.uso, embalses.naturaleza," +
		  		"embalses.volumen, embalses.nan, embalses.nam, round(CAST(ST_Area(embalses.the_geom)/10000 as numeric),2) as área_ha," +
		  		"round(CAST(st_perimeter(embalses.the_geom) as numeric),2) as perímetro_metros " +
		  		"from (embalses inner join hidrografia on embalses.tipo_hidrografia=hidrografia.id) inner join " +
		  		"tipo_hidrografia on hidrografia.tipo_hidrografia=tipo_hidrografia.id "+condition+" order by embalses.gid";;
		  classname = "Embalse";
		  layername = "Hidrografía_areal";
		  tipoElemento = FShape.POLYGON;
		  objetoGeometrico = true;
		  break;	   	   
	  }
	  case SQL_FAJAS_REAL:{
		  sql = "select fajas_real.gid, fajas.nombre_lugar, fajas.orilla, fajas.ancho, tipo_hidrografia.tipo_elemento as tipo_hidrografía, " +
		  		"round(CAST(ST_Area(fajas_real.the_geom)/10000 as numeric),2) as área_ha, round(CAST(st_perimeter(fajas_real.the_geom) as numeric),2) as perímetro_metros " +
		  		"from ((fajas inner join hidrografia on fajas.hidrografia=hidrografia.id) inner join tipo_hidrografia " +
		  		"on tipo_hidrografia.id=hidrografia.tipo_hidrografia)inner join fajas_real on fajas_real.faja=fajas.gid "+condition+" order by fajas_real.gid";;
		  classname = "Faja_real";
		  layername = "Fajas_Real";
		  tipoElemento = FShape.POLYGON;
		  objetoGeometrico = true;
		  break;	   	   
	  }
	  case SQL_MUNICIPIOS:{
		  sql = "select municipios.gid, provincias.nombre as provincia, municipios.nombre, round(CAST(ST_Area(municipios.the_geom)/10000 as numeric),2) as área_ha, " +
		  		"round(CAST(st_perimeter(municipios.the_geom) as numeric),2) as perímetro_metros " +
		  		"from municipios inner join provincias on municipios.provincia=provincias.gid "+condition+" order by municipios.gid";;
		  classname = "Municipio";
		  layername = "Municipios";
		  tipoElemento = FShape.POLYGON;
		  objetoGeometrico = true;
		  break;	   	   
	  }
	  case SQL_PARCELAS:{
		  sql = "select parcelas.gid, provincias.nombre as provincia, municipios.nombre as municipio, parcelas.nombre as parcela, " +
		  		"parcelas.poseedor, parcelas.uso, parcelas.zc, round(CAST(ST_Area(parcelas.the_geom)/10000 as numeric),2) as área_ha, " +
		  		"round(CAST(st_perimeter(parcelas.the_geom) as numeric),2) as perímetro_metros " +
		  		"from (parcelas inner join municipios on municipios.gid=parcelas.municipio) inner join provincias on municipios.provincia=provincias.gid "+condition+" order by parcelas.gid";;
		  classname = "Parcela";
		  layername = "Parcelas";
		  tipoElemento = FShape.POLYGON;
		  objetoGeometrico = true;
		  break;	   	   
	  }
	  case SQL_RELIEVE:{
		  sql = "select relieve.gid, cuencas.nombre as cuenca, relieve.elevacion as elevación, round(CAST(ST_length(relieve.the_geom) as numeric),2) as longitud_curva " +
		  		"from relieve inner join cuencas on cuencas.gid=relieve.cuenca "+condition+" order by relieve.gid";;
		  classname = "Relieve";
		  layername = "Relieve";
		  tipoElemento = FShape.LINE;
		  objetoGeometrico = true;
		  break;	   	   
	  }
	  case SQL_RIOS:{
		  sql = "select rios.gid, tipo_hidrografia.descripcion as descripción, hidrografia.nombre, rios.ancho, rios.orden, " +
		  		"round(CAST(ST_length(rios.the_geom) as numeric),2) as longitud " +
		  		"from (rios inner join hidrografia on rios.tipo_hidrografia=hidrografia.id) inner join " +
		  		"tipo_hidrografia on hidrografia.tipo_hidrografia=tipo_hidrografia.id "+condition+" order by rios.gid";
		  classname = "Rio";
		  layername = "Hidrografía_lineal";
		  tipoElemento = FShape.LINE;
		  objetoGeometrico = true;
		  break;
	  }	
	  case SQL_SUELOS:{
		  sql = "select _suelos.gid, textura_suelos.textura, tipo_suelo.tipo, erosion as erosión, " +
		  		"materiaorganica as materia_orgánica, profundidadefectiva as profundidad_efectiva, ph, pendiente, gravas, " +
		  		"piedras, rocas, estructura," +
		  		"round(CAST(ST_Area(_suelos.the_geom)/10000 as numeric),2) as área_ha, " +
		  		"round(CAST(st_perimeter(_suelos.the_geom) as numeric),2) as perímetro_metros " +
		  		"from (_suelos left join textura_suelos on _suelos.textura=textura_suelos.id) left join tipo_suelo on " +
		  		"_suelos.tipo=tipo_suelo.id "+condition+" order by _suelos.gid";;
		  classname = "Suelo";
		  layername = "Suelos";
		  tipoElemento = FShape.POLYGON;
		  objetoGeometrico = true;
		  break;
	  }
	  case SQL_RED_DRENAJE :{
		  sql = "select gid, orden, siguiente from red_drenaje order by gid";
		  classname = "Red_drenaje";
		  layername = "Redes_drenaje";
		  tipoElemento = FShape.LINE;
		  objetoGeometrico = true;
		  break;
	  }
	  //--------------Nomencladores------------------------------------------------
	  case SQL_AGRUPADORES_USO:{
		  sql = "select id, tipo_uso from agrupadores_uso";
		  classname = "Agrupador_uso";
		  objetoGeometrico = false;
		  break;
	  }
	  case SQL_ESPECIES:{
		  sql = "select id, siglas, ncientifico as nomb_científico, ncomun as Nomb_común from especies";
		  classname = "Especie";
		  objetoGeometrico = false;
		  break;
	  }
	  case SQL_INTENSIDAD_LLUVIA:{
		  sql = "select id, prob as probabilidad, tiempo as tiempo_duración, prec as precipitación from intensidad_lluvia";
		  classname = "Intensidad_lluvia";
		  objetoGeometrico = false;
		  break;
	  }
	  case SQL_PROVINCIAS:{
		  sql = "select gid as id, nombre from provincias";
		  classname = "Provincia";
		  objetoGeometrico = false;
		  break;
	  }
	  case SQL_TEXTURAS:{
		  sql = "select id, textura from textura_suelos";
		  classname = "Textura_suelos";
		  objetoGeometrico = false;
		  break;
	  }
	  case SQL_TIPOS_HIDROGRAFIA:{
		  sql = "select id, codigo as código, descripcion as descripción, ancho_faja as ancho_faja_por_ley, " +
		  		"case tipo_elemento when 'rio' then 'Lineal' else 'Areal' end as tipo_elemento from tipo_hidrografia";
		  classname = "Tipo_hidrografia";
		  objetoGeometrico = false;
		  break;
	  }
	  case SQL_TIPOS_SUELO:{
		  sql = "select id, clave, tipo from tipo_suelo";
		  classname = "Tipo_suelo";
		  objetoGeometrico = false;
		  break;
	  }
	  case SQL_USOS_CATASTRO :{
		  sql = "select usos_catastro.id, agrupadores_uso.tipo_uso as agrupador, usos_catastro.descripcion_uso as descripción, usos_catastro.codigo_uso as código, " +
		  		"usos_catastro.tipo_superficie, usos_catastro.tipo_uso, usos_catastro.esp_uso as especificación " +
		  		"from agrupadores_uso inner join usos_catastro on agrupadores_uso.id=usos_catastro.uso_suelo " +
		  		"Group by agrupadores_uso.tipo_uso, usos_catastro.id, usos_catastro.descripcion_uso, usos_catastro.codigo_uso, " +
		  		"usos_catastro.tipo_superficie, usos_catastro.tipo_uso, usos_catastro.esp_uso " +
		  		"order by agrupadores_uso.tipo_uso, usos_catastro.id";
		  classname = "Uso_catastro";
		  objetoGeometrico = false;
		  break;
	  }
	  case SQL_VELOCIDADES_INFILTRACION :{
		  sql = "select velocidades_infiltracion.id, agrupadores_uso.tipo_uso as agrupador, velocidades_infiltracion.clase, " +
		  		"velocidades_infiltracion.velocidad, velocidades_infiltracion.estructura " +
		  		"from agrupadores_uso inner join velocidades_infiltracion on agrupadores_uso.id=velocidades_infiltracion.uso " +
		  		"Group by agrupadores_uso.tipo_uso, velocidades_infiltracion.id, velocidades_infiltracion.clase, " +
		  		"velocidades_infiltracion.velocidad, velocidades_infiltracion.estructura " +
		  		"order by agrupadores_uso.tipo_uso, velocidades_infiltracion.id";
		  classname = "Velocidad_infiltracion";
		  objetoGeometrico = false;
		  break;
	  }
	  
	  default:{
		 layername = null;  
		 break;
	  }
	 }
	 System.out.println(sql);
	 return sql;
	}
	
	/**Devuelve el titulo de la ventana de gestion*/
	private static String getPanelTitle(int sqlOption)
	{
	 String title = "";	
	 switch (sqlOption) 
	 {
	  case SQL_FAJAS:{
		  title = "Datos de Fajas";
		  break;	   	   
	  }
	  case SQL_PARTEAGUAS:{
		  title = "Datos de Parteaguas";
		  break;	   	   
	  }
	  case SQL_CUENCAS_IN:{
		  title = "Datos de Cuencas";
		  break;	   	   
	  }
	  case SQL_EMBALSES:{
		  title = "Datos de Hidrografía areal";
		  break;	   	   
	  }
	  case SQL_FAJAS_REAL:{
		  title = "Datos de Fajas reales";
		  break;	   	   
	  }
	  case SQL_MUNICIPIOS:{
		  title = "Datos de Municipios";
		  break;	   	   
	  }
	  case SQL_PARCELAS:{
		  title = "Datos de Parcelas";
		  break;	   	   
	  }
	  case SQL_RELIEVE:{
		  title = "Datos de Curvas de Nivel";
		  break;	   	   
	  }
	  case SQL_RIOS:{
		  title = "Datos de Hidrografía lineal";
		  break;	   	   
	  }
	  case SQL_SUELOS:{
		  title = "Datos de Suelos";
		  break;	   	   
	  }
	  case SQL_AGRUPADORES_USO:{
		  title = "Agrupadores de tipo de suelos";
		  break;	   	   
	  }
	  case SQL_ESPECIES:{
		  title = "Especies";
		  break;	   	   
	  }
	  case SQL_INTENSIDAD_LLUVIA:{
		  title = "Valores de precipitación";
		  break;	   	   
	  }
	  case SQL_PROVINCIAS:{
		  title = "Provincias";
		  break;	   	   
	  }
	  case SQL_TEXTURAS:{
		  title = "Texturas de suelo";
		  break;	   	   
	  }
	  case SQL_TIPOS_HIDROGRAFIA:{
		  title = "Tipos de elementos Hidrográficos";
		  break;	   	   
	  }
	  case SQL_TIPOS_SUELO:{
		  title = "Tipos de suelo";
		  break;	   	   
	  }
	  case SQL_USOS_CATASTRO:{
		  title = "Usos de catastro";
		  break;	   	   
	  }
	  case SQL_VELOCIDADES_INFILTRACION:{
		  title = "Velocidades de infiltración";
		  break;	   	   
	  }
	  case SQL_RED_DRENAJE:{
		  title = "Redes de drenaje";
		  break;	   	   
	  }
		
	  default:
		break;
	 }
	 
	 return title;
	}
	
	/**Muestra la ventana de gestion del elemento 
	 * seleccionado, el cual es un PersistentObject. En el caso de que sea un PersistentGeometricObject este 
	 * metodo se llamará desde el Listener de dibujar, después de que se haya dibujado.
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException */
	public static IPanelManager mostrarFicha_Gestion(PersistentObject persistent) throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
	 /*Obtengo el manejador de la clase. La interfaz IPanelManager la deben implementar los managers de las ventanas de gestión de 
	  * cada clase persistente. Convenientemente y para que esto funcione, le pongo como nombre al manejador p+[nombreclase]+Manager. 
	  * Por ejemplo para la clase Cuenca el manejador de su ventana se llamaría pCuencaManager. El método MostrarPanel(persistent) de la 
	  * interfaz debe ser implementado por cada clase manejadora para mostrar el panel correspondiente.*/
		
	    IPanelManager pm = (IPanelManager)Class.forName("org.geocuba.foresta.gestion_datos.gui.p"+classname+"Manager").newInstance();
	    pm.MostrarPanel(persistent);
	 
	 return pm;
	}
	
	public static void terminarEdicion() throws CancelEditingTableException, CancelEditingLayerException, StartEditionLayerException, ReloadLayerException
	{
	 if(capa !=null)  //Cuando la informacion es directa desde el mapa no se asigna la capa
	 {	 
		 if(capa.isEditing())
		 {	 
			StopEditing stopEd = new StopEditing();
			stopEd.initialize();
			stopEd.cancelEdition(capa);
			capa.setEditing(false);
			VistaManager.GetActiveView().getMapControl().setTool("zoomIn");
			//tool.terminarEdición();
			
			try {
				 AlphanumericData ad = (AlphanumericData)capa;                  	          
				 SelectableDataSource dataModel = ad.getRecordset();
				 dataModel.clearSelection();
				 
			} catch (ReadDriverException e) {
				e.printStackTrace();
			}
	         ;
	         
	         VistaManager.RefreshView();
		    capa.reload();
		    setEstadoBotonesGestion(true);
		 }  
	 }	 
	}
	
	public static void ActualizarTabla(int sqlOption, String condition)
	{
	 PluginServices.getMDIManager().setWaitCursor();
	 panel.ActualizarTabla(getSql(sqlOption, condition));
	 
//	 if(objetoGeometrico)
//	 {	 
//		 listaindices = new HashMap<Integer, Integer>();
//		 JTable table = panel.getTable();
//		 for(int i=0; i<table.getRowCount(); i++)
//		 {		 
//		  int gid = Integer.parseInt(table.getValueAt(i,0).toString());	 
//		  listaindices.put(gid, i);
//		 }
//	 }
	 
	 panel.getTable().getTableHeader().setReorderingAllowed(false);
	 
	 panel.getButtonEliminar().setEnabled(false);
     panel.getButtonModificar().setEnabled(false);
     panel.getButtonInformacion().setEnabled(false);
     
     if(objetoGeometrico)
      panel.getButtonZoom().setEnabled(false);
     else
      panel.getButtonZoom().setVisible(false);
     
     PluginServices.getMDIManager().restoreCursor();
     
//	 TableColumn col = panel.getTable().getColumnModel().getColumn(0);
//	 
//	 col.setMaxWidth(0);
//	 col.setMinWidth(0);
//	 col.setWidth(0);
	}
	
	/**Se utiliza por los manejadores de las fichas de gestion de 
	 * datos geometricos, para guardar la geometria Editada*/
	public static IGeometry obtenerGeometriaModificada()
	{
	 return LastEditedGeometry;   //modificarGeomTool.getFinalGeometry();	
	}
	
	/**Almacena la ultima geometría editada, independientemenete
	 * de la herramienta que la haya creado*/
	public static void setCurrentEditedGeometry(IGeometry geometry)
	{
	 LastEditedGeometry = geometry;	
	}
	
	public static int TipoElementoGeometrico()
	{
	 return tipoElemento;	
	}
	
	private static PersistentObject cargarObjetoSeleccionado() throws InstantiationException, IllegalAccessException, ClassNotFoundException, ReadDriverException
	{
		 PluginServices.getMDIManager().setWaitCursor();
		 
		 int row = panel.getTable().getSelectedRow();
     	 if(row < 0)
     	 {
     	  PluginServices.getMDIManager().restoreCursor();	 
     	  JOptionPane.showMessageDialog(null, "No se obtuvo la fila seleccionada", "ERROR", JOptionPane.ERROR_MESSAGE);
     	  return null;
     	 }
     	 
		 int id = Integer.parseInt(panel.getTable().getValueAt(row,0).toString());
		
	     IPersistenObjectManager POM = (IPersistenObjectManager)Class.forName("org.geocuba.foresta.gestion_datos."+classname+"Manager").newInstance();
		 PersistentObject persistent = POM.Cargar_Objeto_BD(id);
	
		 if(persistent == null)
		 {
		  PluginServices.getMDIManager().restoreCursor();
		  JOptionPane.showMessageDialog(null, "No se pudo cargar el objeto");
		  return null;
		 }
		 
		 PluginServices.getMDIManager().restoreCursor();
		 return persistent;
	}
	
	private static void ZoomElemetoSeleccionado()
	{
			 DataSource ds;
				
			 PluginServices.getMDIManager().setWaitCursor();
			 
		     try {
					ds = capa.getRecordset();
		            ds.start();

	                 int fieldIndex = ds.getFieldIndexByName("Gid");
	                 int row = panel.getTable().getSelectedRow();
	                 int selGid = Integer.parseInt(panel.getTable().getValueAt(row, fieldIndex).toString());

	                 for(int i=0; i< ds.getRowCount(); i++)
	                 {
	                  if(Integer.parseInt(ds.getFieldValue(i,fieldIndex).toString()) == selGid)
	                  {	  
	                   IGeometry geom = capa.getSource().getFeature(i).getGeometry();
	                   Rectangle2D selectedExtent = geom.getBounds();
	                  
			           if (selectedExtent != null) 
			        	   VistaManager.GetActiveView().getMapControl().getViewPort().setExtent(selectedExtent);
			           else
			           {     
			        	PluginServices.getMDIManager().restoreCursor();
			        	JOptionPane.showMessageDialog(null, "No se obtuvieron bounds");
			           } 
	                  } 
	                 }	 
	                 
		        ds.stop();
		        
				} catch (ReadDriverException e) {
					e.printStackTrace();
				}
				
				PluginServices.getMDIManager().restoreCursor();
	}
	
	private static void activar_seleccion_mapa(ListSelectionEvent e)
	{
		SelectableDataSource dataModel = null;

        try {
           AlphanumericData ad = (AlphanumericData)capa;                  	          
      	   dataModel = ad.getRecordset();
           DefaultListSelectionModel model = (DefaultListSelectionModel)panel.getTable().getSelectionModel();
           BitSet selection = dataModel.getSelection();
           
           //dataModel.get
           int firstIndex = e.getFirstIndex();
          // int filaSeleccionada = panel.getTable().getSelectedRow();
           int lastIndex = e.getLastIndex();
          
//           if(filaSeleccionada != -1)
//           {	   
//        	   Object value = panel.getTable().getValueAt(filaSeleccionada,0);
//        	   
//	           if(lastIndex == filaSeleccionada)
//	        	lastIndex = listaindices.get(value);
//	           else
//	           if(firstIndex == filaSeleccionada)
//	        	firstIndex = listaindices.get(value);
//           }
           //capa.getSelectionSupport().clearSelection();
           
           if (firstIndex >= 0) 
           {
			  for (int i = firstIndex; i <= lastIndex; i++)
			  {	  
               selection.set(i, model.isSelectedIndex(i));
			  } 
		   }
           
           } catch (ReadDriverException e1) {
					e1.printStackTrace();
					return;
				}
           
          // if (e.getValueIsAdjusting() == false) 
            dataModel.fireSelectionEvents();
	}
	
	private static void mostrar_Info()
	{
		try {
			PersistentObject persistent = cargarObjetoSeleccionado();
			if(persistent != null )
			{	
			 IPanelManager pm = mostrarFicha_Gestion(persistent);
			 pm.DeshabilitarComponentes();
			} 
			
		} catch (ReadDriverException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**Para desactivar los menús de los demás elementos*/
	public static void setGestionando(boolean value)
	{
	 gestionando = value;	
	}
	
	public static boolean Gestionando()
	{
	 return gestionando;	
	}
	
	private static void setEstadoBotonesGestion(boolean enabled)
	{
	 panel.getButtonAdd().setEnabled(enabled);
	 panel.getButtonModificar().setEnabled(enabled);
	 panel.getButtonEliminar().setEnabled(enabled);
	 panel.getButtonInformacion().setEnabled(enabled);
	 panel.getButtonZoom().setEnabled(enabled);
	 panel.getTable().setEnabled(enabled);
	}
}

 
 