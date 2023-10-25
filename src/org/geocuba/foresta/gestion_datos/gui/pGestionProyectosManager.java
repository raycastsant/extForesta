package org.geocuba.foresta.gestion_datos.gui;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.Faja;
import org.geocuba.foresta.gestion_datos.FajaManager;
import org.geocuba.foresta.gestion_datos.PersistentObject;
import org.geocuba.foresta.gestion_datos.Proyecto_Reforestacion;
import org.geocuba.foresta.gestion_datos.Proyecto_ReforestacionManager;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;
import org.geocuba.foresta.reportes.Proyecto_reforestacionWT;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;

 public class pGestionProyectosManager {

	private static pGestionProyectos dialogo = null;
	private static int idFaja = -1;
	
	public static void mostrarDialogo(int _idFaja) 
	{
	 idFaja = _idFaja;
	 dialogo = (pGestionProyectos)UIUtils.GetPanel("_Proyectos de reforestación");
	 if(dialogo == null)
	 {
	  dialogo = new pGestionProyectos("Proyectos de reforestación");
	  setComponentsListeners();
	  ActualizarTabla();
	  
 	  dialogo.Show();
	 } 
	 else
	 {
	   ActualizarTabla();	  
	 }	 
	}
	
	private static void setComponentsListeners()
	{
	 if(dialogo == null)
	  return;
	
	 //---------------------------------------------------------------------------------
	 dialogo.getButtonAdd().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) 
         {
          nuevoProyectoReforestacion();
         }
     });
	 //---------------------------------------------------------------------------------
	 dialogo.getButtonCancelar().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) 
         {
          dialogo.Close();
         }
     });
	//---------------------------------------------------------------------------------
	 dialogo.getButtonModificar().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) 
         {
				try {
					Proyecto_Reforestacion proyecto = cargarObjetoSeleccionado();
					proyecto.setEditing(true);
					showProyectoReforestacion(proyecto);
					
				} catch (ReadDriverException e) {
					e.printStackTrace();
				}
         }
     });
	//---------------------------------------------------------------------------------
	 dialogo.getButtonEliminar().addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) 
      {
       Object[] options = { "Si", "No" };
       int result = JOptionPane.showOptionDialog(null, "¿Está seguro de eliminar el elemento seleccionado?", "Advertencia", 
        			 JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
               
       if(result == 0)
       {	
	        try {
	           int row = dialogo.getTable().getSelectionModel().getMaxSelectionIndex();
	           int id = Integer.parseInt(dialogo.getTable().getValueAt(row,0).toString());  //la primera columna siempre debe ser el Id o el Gid
	           
	           Proyecto_ReforestacionManager Prman = new Proyecto_ReforestacionManager(); 
	  		   Proyecto_Reforestacion proyecto = Prman.Cargar_Objeto_BD(id);
	  		   proyecto.delete();
	           ActualizarTabla();
	  		   
			} catch (ReadDriverException e) {
				e.printStackTrace();
			}
       }
      }
     });
	//---------------------------------------------------------------------------------
	 dialogo.getButtonReporte().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) 
         {
          try {
        	Proyecto_Reforestacion proyecto = cargarObjetoSeleccionado();
        	 
        	String table = getHidroTableName();
        	if(table==null)
           	 return;
         	
         	  String ftable = "fajas";
     	    
     			pProyecto_ReforestacionManager prman = new pProyecto_ReforestacionManager();
     		     
     			if(proyecto.IsNewObject())
     			{		
     		     FajaManager fajaman = new FajaManager();
     		     Faja faja;
				
					faja = fajaman.Cargar_Objeto_BD(idFaja);
     			  proyecto.setFaja(faja);
     			}
     			
     			 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
     			 db.ejecutarConsulta("select round(CAST(sum(st_area("+ ftable +".the_geom)/10000) as numeric),2) from "+ ftable +" where "+ ftable +".gid="+idFaja);
     		     Double area = new Double(0);
     		     if(!db.isEmpty())
     		      area = db.getValueAsDouble(0,0);
     		     
     		  //Medidas de conservación de los suelos
     		      String medSuelos = "";
     			  if(proyecto.getMedidas_suelos()!=null)
     			  {	  
     			   String []medidas = proyecto.getMedidas_suelos().split(":");
     			   if(medidas!=null)
     			   {
     				for(int i=0; i<medidas.length; i++)
     				{
     				 if(medidas[i].equals(pProyecto_ReforestacionManager.Barreras_Vivas))
     				  medSuelos += "Barreras Vivas, ";
     				 else
     			     if(medidas[i].equals(pProyecto_ReforestacionManager.Zanjas_de_absorción))
     			      medSuelos += "Zanjas de absorción, ";
     			     else
     				 if(medidas[i].equals(pProyecto_ReforestacionManager.Terrazas_Individuales))
     				  medSuelos += "Terrazas Individuales, ";
     				 else
     				 if(medidas[i].equals(pProyecto_ReforestacionManager.Acordonamiento_de_residuos))
     				  medSuelos += "Acordonamiento de residuos, ";
     				 else
     				 if(medidas[i].equals(pProyecto_ReforestacionManager.Barreras_muertas))
     					medSuelos += "Barreras muertas, ";
     				 else
     				 if(medidas[i].equals(pProyecto_ReforestacionManager.Terrazas_Continuas))
     					medSuelos += "Terrazas Continuas, ";
     				 else
     				 if(medidas[i].equals(pProyecto_ReforestacionManager.Hoyos))
     					medSuelos += "Hoyos, ";
     				 else
     				 if(medidas[i].equals(pProyecto_ReforestacionManager.Otras))
     					medSuelos += "Otras, ";
     				 else
     			     if(medidas[i].equals(pProyecto_ReforestacionManager.Control_de_cárcavas))
     			    	medSuelos += "Control de cárcavas, ";
     			     else
     				 if(medidas[i].equals(pProyecto_ReforestacionManager.Terrazas_Banco))
     					medSuelos += "Terrazas Banco, ";
     				 else
     				 if(medidas[i].equals(pProyecto_ReforestacionManager.Surcos_en_curvas_de_nivel))
     					medSuelos += "Surcos en curvas de nivel, ";
     				}	
     				
     				if(medSuelos.indexOf(',')>0)
     			     medSuelos = medSuelos.substring(0, medSuelos.length()-2);		
     			   }	   
     			  } 
     			  
     			//Medidas contra el libre pastoreo
     			  String medPastoreo = "";
     			  if(proyecto.getMedidas_pastoreo()!=null)
     			  {	  
     			   String []medidas = proyecto.getMedidas_pastoreo().split(":");
     			   if(medidas!=null)
     			   {
     				for(int i=0; i<medidas.length; i++)
     				{
     				 if(medidas[i].equals(pProyecto_ReforestacionManager.Cercados))
     					medPastoreo += "Cercados, ";
     				 else
     				 if(medidas[i].equals(pProyecto_ReforestacionManager.Otras))
     					medPastoreo += "Otras, ";
     				}	
     				
     				if(medPastoreo.indexOf(',')>0)
     					medPastoreo = medPastoreo.substring(0, medPastoreo.length()-2);		
     			   }	   
     			  }
     			  
     			//Medidas contra incendios
     			  String medIncendios = "";
     			  if(proyecto.getMedidas_incendios()!=null)
     			  {	  
     			   String []medidas = proyecto.getMedidas_incendios().split(":");
     			   if(medidas!=null)
     			   {
     				for(int i=0; i<medidas.length; i++)
     				{
     				 if(medidas[i].equals(pProyecto_ReforestacionManager.Trochas_cortafuegos))
     					medIncendios += "Trochas cortafuegos, ";
     				 else
     				 if(medidas[i].equals(pProyecto_ReforestacionManager.Fajas_verdes))
     					medIncendios += "Fajas verdes, ";
     				 else
     				 if(medidas[i].equals(pProyecto_ReforestacionManager.Otras))
     					medIncendios += "Otras, ";
     				}
     				
     				if(medIncendios.indexOf(',')>0)
     					medIncendios = medIncendios.substring(0, medIncendios.length()-2);		
     			   }	   
     			  }
     		     
        	     PluginServices.cancelableBackgroundExecution(new Proyecto_reforestacionWT(ftable, table, area.toString(), proyecto, medSuelos, medPastoreo, medIncendios));
        	   
     			} catch (ReadDriverException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
         }
     });
	//---------------------------------------------------------------------------------
//	 panel.getButtonInformacion().addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(java.awt.event.ActionEvent evt) 
//         {
//          mostrar_Info();
//         }
//     });
	//---------------------------------------------------------------------------------
		 dialogo.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
	         public void valueChanged(ListSelectionEvent e) 
	         {
	        	 dialogo.getButtonEliminar().setEnabled(true);
	        	 dialogo.getButtonModificar().setEnabled(true);
	        	 dialogo.getButtonReporte().setEnabled(true);
//	        	 dialogo.getButtonInformacion().setEnabled(true);
	         }
	     });
		//---------------------------------------------------------------------------------
//		 dialogo.getWindowInfo().getaddWindowListener(new java.awt.event.WindowAdapter() {
//	            public void windowActivated(java.awt.event.WindowEvent evt)
//	            { 
//	             ActualizarTabla();
//	             if(dialogo.getTable().getRowCount()<1)
//	              nuevoProyectoReforestacion();	 
//	            }
//	        });
  //---------------------------------------------------------------------------------
//		 panel.getTable().addMouseListener(new MouseAdapter() 
//		  {
//			public void mouseClicked(MouseEvent e) 
//			{
//		     if(e.getClickCount() == 2)
//		     {	
//		      mostrar_Info();       
//			 } 
//		    }
//		  }); 
	}
	
	public static void ActualizarTabla()
	{
	 PluginServices.getMDIManager().setWaitCursor();
	 
	 dialogo.ActualizarTabla("select id, area_reforestar as área_reforestar, nombre_productor as productor, organismo, " +
	 		"nombre_lugar, tipo_erosion_suelo as tipo_erosión_suelo, grado_erosion_suelo as grado_erosión_suelo, " +
	 		"profundidad_efectiva, ph, materiaorganica as materia_orgánica, vegetacion as vegetación, densidad, " +
	 		"observaciones_veg as observaciones, marcox as marco_x, marcoy as marco_y, hileras, precipitacion as precipitación, " +
	 		"metodo_plantacion as método_plantación, proyecto_general, especificacion as especificación, " +
	 		"medidas_suelos, medidas_pastoreo, medidas_incendios from proyecto where faja="+idFaja);
	 
	 dialogo.getTable().getTableHeader().setReorderingAllowed(false);
	 dialogo.getButtonEliminar().setEnabled(false);
	 dialogo.getButtonModificar().setEnabled(false);
	 dialogo.getButtonReporte().setEnabled(false);
     
     PluginServices.getMDIManager().restoreCursor();
	}
	
	private static Proyecto_Reforestacion cargarObjetoSeleccionado() throws ReadDriverException 
	{
		 PluginServices.getMDIManager().setWaitCursor();
		 
		 int row = dialogo.getTable().getSelectedRow();
     	 if(row < 0)
     	 {
     	  PluginServices.getMDIManager().restoreCursor();	 
     	  JOptionPane.showMessageDialog(null, "No se obtuvo la fila seleccionada", "ERROR", JOptionPane.ERROR_MESSAGE);
     	  return null;
     	 }
     	 
		 int id = Integer.parseInt(dialogo.getTable().getValueAt(row,0).toString());
		
		 Proyecto_ReforestacionManager Prman = new Proyecto_ReforestacionManager(); 
		 Proyecto_Reforestacion proyecto = Prman.Cargar_Objeto_BD(id);
	
		 if(proyecto == null)
		 {
		  PluginServices.getMDIManager().restoreCursor();
		  JOptionPane.showMessageDialog(null, "No se pudo cargar el proyecto de reforestación");
		  return null;
		 }
		 
		 PluginServices.getMDIManager().restoreCursor();
		 return proyecto;
	}
	
//	private static void activar_seleccion_mapa(ListSelectionEvent e)
//	{
//		SelectableDataSource dataModel = null;
//
//        try {
//           AlphanumericData ad = (AlphanumericData)capa;                  	          
//      	   dataModel = ad.getRecordset();
//           DefaultListSelectionModel model = (DefaultListSelectionModel)panel.getTable().getSelectionModel();
//           BitSet selection = dataModel.getSelection();
//           int firstIndex=e.getFirstIndex();
//           
//           //capa.getSelectionSupport().clearSelection();
//           
//           if (firstIndex >= 0) 
//           {
//			  for (int i = firstIndex; i <= e.getLastIndex(); i++)
//			  {	  
//             selection.set(i, model.isSelectedIndex(i));
//			  } 
//		     }
//           
//           } catch (ReadDriverException e1) {
//					e1.printStackTrace();
//					return;
//				}
//           
//          // if (e.getValueIsAdjusting() == false) 
//            dataModel.fireSelectionEvents();
//	}
	
//	private static void mostrar_Info()
//	{
//		try {
//			PersistentObject persistent = cargarObjetoSeleccionado();
//			if(persistent != null )
//			{	
//			 IPanelManager pm = mostrarFicha_Gestion(persistent);
//			 pm.DeshabilitarComponentes();
//			} 
//			
//		} catch (ReadDriverException e) {
//			e.printStackTrace();
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//	}
	
	private static void showProyectoReforestacion(Proyecto_Reforestacion proyecto)
	{
	    JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	    db.ejecutarConsulta("select distinct hidrografia from fajas where gid="+idFaja);
	    
	  //Busco el tipo de hidrografia 
	    String table = getHidroTableName();
	    if(table==null)
    	 return;
    	
    	String ftable = "fajas";
	    
	  //Busco el ancho de las orillas
		db.ejecutarConsulta("select "+ ftable +".orilla, "+ ftable +".ancho from "+ ftable +" where "+ ftable +".gid="+idFaja+" Order by orilla");
		String ancho = "";

		if(table.equals("rios"))
		{
		 if(db.getRowCount()>1)	
		  ancho = "Ancho de la faja (m). Orilla Izquierda : "+ Math.round(Double.parseDouble(db.getValueAt(1, 1).toString())) +"      Derecha : "+ Math.round(Double.parseDouble(db.getValueAt(0, 1).toString()));
		 else
		  ancho = "Ancho de la faja (m). Orilla "+ db.getValueAt(0,0).toString() +" : "+ Math.round(Double.parseDouble(db.getValueAt(0, 1).toString()));	 
		} 
		else
		 ancho = "Ancho de la faja (m) : "+ Math.round(Double.parseDouble(db.getValueAt(0, 1).toString()));
		
	 //Busco el area de la faja
	     db.ejecutarConsulta("select round(CAST(sum(st_area("+ ftable +".the_geom)/10000) as numeric),2) from "+ ftable +" where "+ ftable +".gid="+idFaja);
	     double area = 0;
	     if(!db.isEmpty())
	      area = db.getValueAsDouble(0,0);
	     
		try {
			 pProyecto_ReforestacionManager prman = new pProyecto_ReforestacionManager();
		     
			if(proyecto.IsNewObject())
			{		
		     FajaManager fajaman = new FajaManager();
		     Faja faja = fajaman.Cargar_Objeto_BD(idFaja);
			 proyecto.setFaja(faja);
			}
			
		    prman.MostrarPanel((PersistentObject)proyecto, table, ancho, area, ftable);
		     
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	private static void nuevoProyectoReforestacion()
	{
		Proyecto_Reforestacion proyecto = new Proyecto_Reforestacion();
        showProyectoReforestacion(proyecto);
	}
	
	private static String getHidroTableName()
	{
		JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	     db.ejecutarConsulta("select distinct hidrografia from fajas where gid="+idFaja);
	    
	  //Busco el tipo de hidrografia 
	    String table = null;
    	
	    String sql = "select distinct tipo_hidrografia.tipo_elemento " +
        "from (tipo_hidrografia inner join hidrografia on tipo_hidrografia.id=hidrografia.tipo_hidrografia)" +
        "inner join fajas on fajas.hidrografia=hidrografia.id where fajas.gid="+idFaja;
    	db.ejecutarConsulta(sql);
    	System.out.println(sql);
    	
    	if(!db.isEmpty())
    	{
    	 if(db.getRowCount() == 1)
    	 {
    	  if(db.getValueAsString(0,0).equals("rio"))
    	   table = "rios";
    	  else
    	   table = "embalses";
    	 }
    	}	
    	
    	return table;
	}
	
}

 