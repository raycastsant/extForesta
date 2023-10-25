package org.geocuba.foresta.gestion_datos.gui;

import java.text.DecimalFormat;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.Especie;
import org.geocuba.foresta.gestion_datos.EspecieManager;
import org.geocuba.foresta.gestion_datos.PersistentObject;
import org.geocuba.foresta.gestion_datos.Proyecto_Reforestacion;
import org.geocuba.foresta.gestion_datos.Tipo_suelo;
import org.geocuba.foresta.gestion_datos.Tipo_sueloManager;
import org.geocuba.foresta.herramientas.utiles.Global;
import org.geocuba.foresta.reportes.Proyecto_reforestacionWT;
import com.iver.andami.PluginServices;

 public class pProyecto_ReforestacionManager
 {
	private pProyecto_Reforestacion panel;
	private Proyecto_Reforestacion proyecto;
	//private String gids;
	private String table;
	private Double area;
	private String ftable;
	private int fichaCostoRowIndex;
	
	private Proyecto_EspeciesDlg especiesDlg;
//	private List<Integer> especies_quitar;
//	private List<Integer> especies_agregar;
//	private List<Integer> especies_quitarTemp;
//	private List<Integer> especies_agregarTemp;
	private DefaultTableModel model;
//	private EspecieManager espman;
	private Especie []listaEspecies;
	private Proyecto_SuelosDlg suelosDlg;
	private Tipo_suelo []listaSuelos;
	
	public static final String Barreras_Vivas = "BrV";
	public static final String Zanjas_de_absorción  = "ZjAb";
	public static final String Terrazas_Individuales  = "TrId";
	public static final String Acordonamiento_de_residuos  = "AcRes";
	public static final String Barreras_muertas  = "BrM";
	public static final String Terrazas_Continuas  = "TrCon";
	public static final String Hoyos  = "Hoy";
	public static final String Otras  = "Otr";
	public static final String Control_de_cárcavas  = "CtrlCv";
	public static final String Terrazas_Banco  = "TrBa";
	public static final String Surcos_en_curvas_de_nivel  = "ScCN";
	
	public static final String Cercados  = "Cerc";
	
	public static final String Trochas_cortafuegos  = "TrchCF";
	public static final String Fajas_verdes  = "FajVerd";
	
	public void MostrarPanel(PersistentObject persistent, String _table, String ancho, Double _area, String _ftable) 
	{
		JFrame frame = new javax.swing.JFrame();
		panel = new pProyecto_Reforestacion(frame, "Proyectos de reforestación");
		panel.setLocationRelativeTo(null);	
		
	// gids = _gids;
	 table = _table;
	 area = _area;
	 ftable = _ftable;
	 
	 proyecto = (Proyecto_Reforestacion) persistent;
	 
	 panel.getLancho().setText(ancho);
	 panel.getLArea().setText(area.toString());
	 UpdateTenetentes();
	 
	 panel.getCBXTenente().setEnabled(false);
	 
//	 especies_quitar = new ArrayList<Integer>();
//	 especies_agregar = new ArrayList<Integer>();
	 
	 if(!proyecto.IsNewObject()) //Se está modificando
	  cargarValores();	
	 else
	 {		 
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 db.ejecutarConsulta("select distinct tipo_suelo.tipo, tipo_suelo.id, tipo_suelo.clave, materiaorganica, profundidadefectiva, ph " +
		 		"from (tipo_suelo inner join _suelos on tipo_suelo.id=_suelos.tipo) inner join "+ ftable +
		 		" on st_intersects("+ ftable +".the_geom, _suelos.the_geom) where "+ ftable +".gid in("+proyecto.getFaja().getGid()+")");

		 Double matorg = new Double(0);
		 Double profefect = new Double(0);
		 Double ph = new Double(0);
		 if(!db.isEmpty())
		 {
			  int cant = db.getRowCount();
			  Tipo_suelo []lista = new Tipo_suelo[cant];
			  for(int i=0; i<cant; i++)
			  {
			   String tiposuelo = db.getValueAsString(i,0);
			   int id = db.getValueAsInteger(i,1);
			   String clave = db.getValueAsString(i,2);
			   lista[i] = new Tipo_suelo(id, clave, tiposuelo, false);
			   
			   double currentMatorg = db.getValueAsDouble(i,3);
			   if(currentMatorg > matorg)
				matorg = currentMatorg;
			   
			   double currentProfefect = db.getValueAsDouble(i,4);
			   if(currentProfefect > profefect)
				profefect = currentProfefect;
			   
			   double currentPh = db.getValueAsDouble(i,5);
			   if(currentPh > ph)
				ph = currentPh;
			  }
			  
			  listaSuelos = lista;
			  panel.getTFMatorg().setText(matorg.toString());
			  panel.getTFProfefect().setText(profefect.toString());
			  panel.getTFPH().setText(ph.toString());
		 }	 
	 } 
	 
	 setComponentsListeners();	
	 panel.show();
	}
	
	private void setComponentsListeners()
	{
	 panel.getBGuardar().addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) 
        {
			 //MarcoX
			   String value= panel.getTFMarcoX().getText();
			   if(value.equals(""))
				value = "0";	 
			   proyecto.setMarcox(Double.parseDouble(value)); 
			  
			  //MarcoY
			   value= panel.getjTFMarcoY().getText();
			   if(value.equals(""))
				value = "0";	 
			   proyecto.setMarcoy(Double.parseDouble(value)); 
			   
			   if(proyecto.getMarcox()*proyecto.getMarcoy() == 0)
			   {
				JOptionPane.showMessageDialog(null, "Los valores del marco de plantación deben ser mayores que cero", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			   }	    
			
			//Es proyecto de un tenente solamente
			 boolean istenente = panel.getRBRefTenente().isSelected(); 
			 proyecto.setTenente(istenente);
			 if(istenente)
			  proyecto.setNombre_tenente(panel.getCBXTenente().getSelectedItem().toString());	 
			 
			//Area que ocupa
			 value = panel.getLArea().getText();
			 if(value.equals(""))
			  value = "0";	 
			 proyecto.setArea_tenente(Double.parseDouble(value.replace(',', '.'))); 
				  
			 //Area a reforestar
			 value = panel.getTFAreaRef().getText();
			 if(value.equals(""))
				 value = "0";	 
			 proyecto.setArea_reforestar(Double.parseDouble(value)); 
				  
				  //Productor
				   proyecto.setNombre_productor(panel.getTFProductor().getText());
				   
				  //Organismo
				   proyecto.setOrganismo(panel.getTFOrganismo().getText());
				  
				  //Nombre del lugar
				   proyecto.setNombre_lugar(panel.getTFNombreLugar().getText());
				  
				  //Erosion de suelos
				   proyecto.setTipo_erosion_suelo(panel.getCBTipoErosion().getSelectedItem().toString());
				  
				  //Grado de erosion
				   proyecto.setGrado_erosion_suelo(panel.getCBGradoErosion().getSelectedItem().toString());
				  
				  //Profundidad efectiva
				   value = panel.getTFProfefect().getText();
				   if(value.equals(""))
					value = "0";	 
				   proyecto.setProfundidad_efectiva(Double.parseDouble(value)); 
				  
				  //Ph
				   value= panel.getTFPH().getText();
				   if(value.equals(""))
					value = "0";	 
				   proyecto.setPh(Double.parseDouble(value)); 
				  
				  //Materia organica
				   value= panel.getTFMatorg().getText();
				   if(value.equals(""))
					value = "0";	 
				   proyecto.setMateriaorganica(Double.parseDouble(value)); 
				  
				  //Vegetacion
				   proyecto.setVegetacion(panel.getTFTipoVegetacion().getText());
				   
				  //Densidad
				   proyecto.setDensidad(panel.getCBDensVegetacion().getSelectedItem().toString());
				   
				  //Observaciones
				   proyecto.setObservaciones_veg(panel.getTFDensObserv().getText());
				  
				  //Número de hileras en faja
				   value= panel.getTFHileras().getText();
				   if(value.equals(""))
					value = "0";	 
				   proyecto.setHileras(Integer.parseInt(value));
				  
				  //Precipitación media anual
				   value= panel.getTFPrecMedia().getText();
				   if(value.equals(""))
					value = "0";	 
				   proyecto.setPrecipitacion(Double.parseDouble(value));
				  
				  //Metodo de plantación
				   proyecto.setMetodo_plantacion(panel.getCBMetPlantacion().getSelectedItem().toString());
				  
				  //Proyecto general de una cuenca
				   boolean isgeneral = panel.getCBProyTec().isSelected();
				   proyecto.setProyecto_general(isgeneral);
				   
				   if(isgeneral)
					proyecto.setEspecificacion(panel.getTFEspecifProyecto().getText()); 
				   
				  //Medidas de conservación de los suelos
				   value = "";
				   if(panel.getCHBBarrerasVivas().isSelected())
					value = Barreras_Vivas+":";  
				   if(panel.getCHBZanjasAbsorc().isSelected())
					value += Zanjas_de_absorción+":"; 
				   if(panel.getCHBTerrazasInd().isSelected())
					value += Terrazas_Individuales+":";
				   if(panel.getCHBXAcordResiduos().isSelected())
					value += Acordonamiento_de_residuos+":";
				   if(panel.getCHBBarrerasMuert().isSelected())
					value += Barreras_muertas+":";
				   if(panel.getCHBTerrazasCont().isSelected())
					value += Terrazas_Continuas+":";
				   if(panel.getCHBHoyos().isSelected())
					value += Hoyos+":";
				   if(panel.getCHBOtrasSuelos().isSelected())
					value += Otras+":";
				   if(panel.getCHBControlCarc().isSelected())
					value += Control_de_cárcavas+":";
				   if(panel.getCHBTerrazasBanc().isSelected())
					value += Terrazas_Banco+":";
				   if(panel.getCHBSurcosCurvasNiv().isSelected())
					value += Surcos_en_curvas_de_nivel+":";
				   
				   if(value.indexOf(",")>0)
				    value = value.substring(0, value.length()-1);
				   
				   proyecto.setMedidas_suelos(value);
				   
				//Medidas contra el libre pastoreo
				   value = "";
				   if(panel.getCHBCercado().isSelected())
					value += Cercados+":";
				   if(panel.getCHBOtrasPastoreo().isSelected())
					value += Otras+":";
				   
				   if(value.indexOf(",")>0)
				    value = value.substring(0, value.length()-1);
				   
				   proyecto.setMedidas_pastoreo(value);
				   
				//Medidas contra incendios
				   value = "";
				   if(panel.getCHBTrochas().isSelected())
					value += Trochas_cortafuegos+":";
				   if(panel.getCHBFajasVerdes().isSelected())
					value += Fajas_verdes+":";
				   if(panel.getCHBOtrasIncendios().isSelected())
					value += Otras+":";
				   
				   if(value.indexOf(",")>0)
				    value = value.substring(0, value.length()-1);
				   
				   proyecto.setMedidas_incendios(value);
				   
				 //Especies  
				   proyecto.setListaEspecies(listaEspecies);
				   
				 //Suelos  
				   proyecto.setListaSuelos(listaSuelos);
				   
				proyecto.save();
				
				//Guardar la ficha de costo
				 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
				 db.ejecutarConsulta("delete from ficha_costo where proyecto="+proyecto.getId());
				 for(int i=0; i<panel.getTableFichaCosto().getRowCount(); i++)
				 {	 
					 String actividad = panel.getTableFichaCosto().getValueAt(i, 0).toString();
					 String cantidad = panel.getTableFichaCosto().getValueAt(i, 1).toString();
					 String um = panel.getTableFichaCosto().getValueAt(i, 2).toString();
					 String cup = panel.getTableFichaCosto().getValueAt(i, 3).toString();
					 String cuc = panel.getTableFichaCosto().getValueAt(i, 4).toString();
					 String total = panel.getTableFichaCosto().getValueAt(i, 5).toString();
					 
					 db.ejecutarConsulta("insert into ficha_costo(proyecto,actividades,um,cantidad,cup,cuc,total)" +
					 		"values("+proyecto.getId()+", '"+actividad+"', '"+um+"', "+cantidad+", "+cup+", "+cuc+", "+total+")");
				 }
				 
				//Mostrar el reporte---------------------------------------------------------------------------------------------------------
				 String medSuelos = "";
				 String medPastoreo = "";
				 String medIncendios = "";
				 
				    for(int i=0; i<panel.getPanelSuelos().getComponentCount(); i++)
			    	{	
					 JCheckBox chbx = (javax.swing.JCheckBox)panel.getPanelSuelos().getComponent(i);
			    	 if(chbx.isSelected())	
			    	  medSuelos += chbx.getText()+",";
			    	} 
			    	if(!medSuelos.equals(""))
			    	 medSuelos = medSuelos.substring(0,medSuelos.length()-1);
			    	
			    	for(int i=0; i<panel.getPanelPastoreo().getComponentCount(); i++)
			    	{	
			    	 JCheckBox chbx = (javax.swing.JCheckBox)panel.getPanelPastoreo().getComponent(i);	
			    	 if(chbx.isSelected())	
			    	  medPastoreo += chbx.getText()+",";
			    	} 
			    	if(!medPastoreo.equals(""))
			    	 medPastoreo = medPastoreo.substring(0,medPastoreo.length()-1);
			    	
			    	for(int i=0; i<panel.getPanelIncendios().getComponentCount(); i++)
			    	{	
			    	 JCheckBox chbx = (javax.swing.JCheckBox)panel.getPanelIncendios().getComponent(i);
			    	 if(chbx.isSelected())	
			    	  medIncendios += chbx.getText()+",";
			    	}
			    	if(!medIncendios.equals(""))
			    	 medIncendios = medIncendios.substring(0,medIncendios.length()-1);
			    	
				 PluginServices.cancelableBackgroundExecution(new Proyecto_reforestacionWT(ftable, table, panel.getLArea().getText(), 
						 proyecto, medSuelos, medPastoreo, medIncendios));
				 
				 pGestionProyectosManager.ActualizarTabla();
				 panel.dispose();
				
				//Inserto las especies de la composicion-------------------------------------------------------------------
//				   if(especies_agregar.size()>0 || especies_quitar.size()>0)
//				   {	   
//					 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());  
//					 
//		    		//Elimino las especies que se dejaron de recomendar 
//					if(!proyecto.IsNewObject())
//					{		
//		    		 for(int i=0; i<especies_quitar.size(); i++)
//		    		 {
//		    		  db.ejecutarConsulta("delete from proyecto_especies where especie="+especies_quitar.get(i)+" " +
//		    		   "and proyecto="+proyecto.getId()); 
//		    		 }	
//					} 
//		    		 
//		    		//Agrego las especies que se recomendaron
//		    		 for(int i=0; i<especies_agregar.size(); i++)
//		    		 {
//		    		  db.ejecutarConsulta("insert into proyecto_especies(especie,proyecto) " +
//		    			              "values(" + especies_agregar.get(i) + "," + proyecto.getId() + ")");
//		    		 }	
//				   }
				
//				panel.Close();
//				pGestionManager.ActualizarTabla(pGestionManager.SQL_MUNICIPIOS, "");
        }
     });
	//--------------------------------------------------------------------------------- 
	 panel.getBCancelar().addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) 
	    {
		 panel.dispose();
	    }
	 });
	//--------------------------------------------------------------------------------- 
	 panel.getBEspecies().addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) 
	    {
		 dialogoespecies();
	    }
	 });
	//--------------------------------------------------------------------------------- 
	 panel.getBSuelos().addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) 
	    {
		 dialogoSuelos();
	    }
	 });
	//--------------------------------------------------------------------------------- 
	 panel.getBAddActividad().addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) 
	    {
		 String actividad = panel.getTFActividad().getText();
		 String cantidad = panel.getTFCantidad().getText();
		 String um = panel.getTFUM().getText();
		 String cup = panel.getTFCUP().getText();
		 String cuc = panel.getTFCUC().getText();
		 String total = panel.getTFTotal().getText();
		 
		 model = (DefaultTableModel)panel.getTableFichaCosto().getModel();

			  Object []row = new Object[6];
			  row[0] = actividad;
			  row[1] = cantidad;
			  row[2] = um;
			  row[3] = cup;
			  row[4] = cuc;
			  row[5] = total;
			  
			  model.addRow(row);
			  
			     panel.getTFActividad().setText("");
				 panel.getTFCantidad().setText("");
				 panel.getTFUM().setText("");
				 panel.getTFCUP().setText("");
				 panel.getTFCUC().setText("");
				 panel.getTFTotal().setText("");
				 
				 panel.getTableFichaCosto().getSelectionModel().clearSelection();
				 panel.getBModificarActividad().setEnabled(false);
				 panel.getBEliminarActividad().setEnabled(false);
	    }
	 });
	//--------------------------------------------------------------------------------- 
	 panel.getBModificarActividad().addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) 
	    {
			 if(panel.getBAddActividad().isEnabled())
			 {		 
				 fichaCostoRowIndex = panel.getTableFichaCosto().getSelectedRow();
				 String actividad = panel.getTableFichaCosto().getValueAt(fichaCostoRowIndex, 0).toString();
				 String cantidad = panel.getTableFichaCosto().getValueAt(fichaCostoRowIndex, 1).toString();
				 String um = panel.getTableFichaCosto().getValueAt(fichaCostoRowIndex, 2).toString();
				 String cup = panel.getTableFichaCosto().getValueAt(fichaCostoRowIndex, 3).toString();
				 String cuc = panel.getTableFichaCosto().getValueAt(fichaCostoRowIndex, 4).toString();
				 String total = panel.getTableFichaCosto().getValueAt(fichaCostoRowIndex, 5).toString();
				 
				 panel.getTFActividad().setText(actividad);
				 panel.getTFCantidad().setText(cantidad);
				 panel.getTFUM().setText(um);
				 panel.getTFCUP().setText(cup);
				 panel.getTFCUC().setText(cuc);
				 panel.getTFTotal().setText(total);
				 
				 panel.getTableFichaCosto().getSelectionModel().clearSelection();
				 panel.getBModificarActividad().setIcon(new javax.swing.ImageIcon(PluginServices.getPluginServices("org.geocuba.foresta").getPluginDirectory()+""+Global.fileSeparator+"images"+Global.fileSeparator+"aceptar.png"));
				 panel.getBAddActividad().setEnabled(!panel.getBAddActividad().isEnabled());
				 //panel.getBEliminarActividad().setEnabled(!panel.getBEliminarActividad().isEnabled());
			 } 
			 else
			 {
				 String actividad = panel.getTFActividad().getText();
				 String cantidad = panel.getTFCantidad().getText();
				 String um = panel.getTFUM().getText();
				 String cup = panel.getTFCUP().getText();
				 String cuc = panel.getTFCUC().getText();
				 String total = panel.getTFTotal().getText();	
				 
				 panel.getTableFichaCosto().setValueAt(actividad, fichaCostoRowIndex, 0);
				 panel.getTableFichaCosto().setValueAt(cantidad, fichaCostoRowIndex, 1);
				 panel.getTableFichaCosto().setValueAt(um, fichaCostoRowIndex, 2);
				 panel.getTableFichaCosto().setValueAt(cup, fichaCostoRowIndex, 3);
				 panel.getTableFichaCosto().setValueAt(cuc, fichaCostoRowIndex, 4);
				 panel.getTableFichaCosto().setValueAt(total, fichaCostoRowIndex, 5);
				 
				 panel.getBModificarActividad().setIcon(new javax.swing.ImageIcon(PluginServices.getPluginServices("org.geocuba.foresta").getPluginDirectory()+""+Global.fileSeparator+"images"+Global.fileSeparator+"tableedit.png"));
				 panel.getBAddActividad().setEnabled(!panel.getBAddActividad().isEnabled());
				 
//				 if(panel.getTableFichaCosto().getRowCount()==1)
//				 {	 
				  panel.getTableFichaCosto().getSelectionModel().clearSelection();
				  panel.getBModificarActividad().setEnabled(false);
				  //panel.getBEliminarActividad().setEnabled(false);
//				 }
//				 else
//				  panel.getBModificarActividad().setEnabled(false);
				 
				 //panel.getBEliminarActividad().setEnabled(!panel.getBEliminarActividad().isEnabled());
				 
				 panel.getTFActividad().setText("");
				 panel.getTFCantidad().setText("");
				 panel.getTFUM().setText("");
				 panel.getTFCUP().setText("");
				 panel.getTFCUC().setText("");
				 panel.getTFTotal().setText("");
			 }	 
			 
			 panel.getTableFichaCosto().getSelectionModel().clearSelection();
			 panel.getBEliminarActividad().setEnabled(false);
	    }
	 });
	//--------------------------------------------------------------------------------- 
	 panel.getBEliminarActividad().addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) 
	    {
		 int index = panel.getTableFichaCosto().getSelectedRow();
		 
		 model = (DefaultTableModel)panel.getTableFichaCosto().getModel();
		 model.removeRow(index);
		 
		 panel.getTableFichaCosto().getSelectionModel().clearSelection();
		 panel.getBModificarActividad().setEnabled(false);
		 panel.getBEliminarActividad().setEnabled(false);
	    }
	 });
	//---------------------------------------------------------------------------------
	 panel.getTableFichaCosto().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
         public void valueChanged(ListSelectionEvent e) 
         {
        	 panel.getBModificarActividad().setEnabled(true);
			 panel.getBEliminarActividad().setEnabled(true);	 
         }
     });
	//---------------------------------------------------------------------------------
	 panel.getCBDatoSuelos().addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        	//jLTiposuelos.setEnabled(jCBDatoSuelos.isSelected());
	        	panel.getLProfundidad().setEnabled(panel.getCBDatoSuelos().isSelected());
	            panel.getLPh().setEnabled(panel.getCBDatoSuelos().isSelected());
	            panel.getLMateriaOrg().setEnabled(panel.getCBDatoSuelos().isSelected());

	            //jTFTiposuelos.setEnabled(jCBDatoSuelos.isSelected());
	            panel.getTFProfefect().setEnabled(panel.getCBDatoSuelos().isSelected());
	            panel.getTFPH().setEnabled(panel.getCBDatoSuelos().isSelected());
	            panel.getTFMatorg().setEnabled(panel.getCBDatoSuelos().isSelected());
	        }
	    });
	//---------------------------------------------------------------------------------
	 panel.getCBProyTec().addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        	 panel.getLEspecificacionProyecto().setEnabled(panel.getCBProyTec().isSelected());
	             panel.getTFEspecifProyecto().setEnabled(panel.getCBProyTec().isSelected());
	        }
	    });
	//---------------------------------------------------------------------------------
	 panel.getCBXTenente().addItemListener(new java.awt.event.ItemListener() {
	        public void itemStateChanged(java.awt.event.ItemEvent evt) {
	        	if(panel.getRBRefTenente().isSelected())
	                panel.getLArea().setText(getArea());
	        }
	    });
	//---------------------------------------------------------------------------------
	 panel.getRBRefTenente().addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        	panel.getCBXTenente().setEnabled(true);
	            if(panel.getCBXTenente().getItemCount() < 1)
	             UpdateTenetentes();
	            else
	             panel.getLArea().setText(getArea()); 
	        }
	    });
	//---------------------------------------------------------------------------------
	 panel.getRBRefFaja().addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        	panel.getCBXTenente().setEnabled(false);
	        	DecimalFormat df = new DecimalFormat("#.##");
	        	panel.getLArea().setText(df.format(area).toString());
	        }
	    });
	//---------------------------------------------------------------------------------
//	 panel.getTFNombre().getDocument().addDocumentListener(new DocumentListener() { 
//         public void changedUpdate(DocumentEvent e) { 
//             panel.getButtonGuardar().setEnabled(true); 
//         } 
//         public void removeUpdate(DocumentEvent e) { 
//        	 panel.getButtonGuardar().setEnabled(true); 
//         } 
//         public void insertUpdate(DocumentEvent e) { 
//        	 panel.getButtonGuardar().setEnabled(true); 
//         } 
//     }); 
	}

//	@Override
//	public void HabilitarGuardado() 
//	{
//	 if(panel != null)	
//	  panel.getButtonGuardar().setEnabled(true);		
//	}

//	@Override
//	public void DeshabilitarComponentes() 
//	{
//	 if(panel != null)
//	 {	 
//	  panel.getButtonGuardar().setVisible(false);
//	  panel.getButtonCancelar().setVisible(false);
//	  panel.getComboProvincia().setEnabled(false);
//	  panel.getTFNombre().setEditable(false);
//	 }  
//	}
	
	 private void UpdateTenetentes()
	    {
	     //Busco los tenentes
	      JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());	
	      db.ejecutarConsulta("select distinct parcelas.poseedor from parcelas inner join "+ ftable +" on st_intersects("+ ftable +".the_geom, parcelas.the_geom) " +
	    			"where "+ ftable +".gid in("+proyecto.getFaja().getGid()+")"); 
	      
	    
	    panel.getCBXTenente().removeAllItems();
	    if(!db.isEmpty())
	    {  	  
	      for(int i=0; i<db.getRowCount(); i++)
	      {	  
	       Object value = db.getValueAt(i,0);
	       
	       if(value == null)
	    	value = "Desconocido";   
	       panel.getCBXTenente().addItem(value);
	      }
	    } 
	    else
	    {
	     JOptionPane.showMessageDialog(null, "Información", "No existen poseedores disponibles", 1);
	     panel.getRBRefFaja().setSelected(true);
	    } 	
	   }
	 
	 private String getArea()
	    {
	     String Andwhere;
	     String value = "";
	     JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	     
	        if(panel.getCBXTenente().getSelectedItem().equals("Desconocido"))
	         Andwhere = " parcelas.poseedor is null";
	        else
	         Andwhere = " parcelas.poseedor='"+panel.getCBXTenente().getSelectedItem().toString()+"'";

	        db.ejecutarConsulta("select sum(st_area(st_intersection("+ ftable +".the_geom, parcelas.the_geom))/10000) " +
	         "from "+ ftable +" inner join parcelas on st_intersects("+ ftable +".the_geom, parcelas.the_geom) " +
	         "where "+ ftable +".gid in("+ proyecto.getFaja().getGid() +") and"+ Andwhere);
	        
	        if(!db.isEmpty())
	         value = ""+ Double.parseDouble(db.getValueAt(0,0).toString());
	       

	        DecimalFormat df = new DecimalFormat("#.##");
	        value = df.format(Double.parseDouble(value)).toString();
	        
	        return value;
	    }
	 
	 private void cargarValores()
	 {
		 //Es para un tenente solamente
		  boolean isTenente = false;
		  if(proyecto.IsTenente()!=null)
		   isTenente = proyecto.IsTenente();
		  
		  if(isTenente) //Actualizo los datos del tenente
		  {
			  panel.getCBXTenente().setEnabled(true);
			   boolean encontrado = false;	  
			   for(int i=0; i<panel.getCBXTenente().getItemCount(); i++)
			   {
				if(proyecto.getNombre_tenente().equals(panel.getCBXTenente().getItemAt(i)))
				{
				 panel.getCBXTenente().setSelectedIndex(i);
				 encontrado = true;
				 panel.getLArea().setText(getArea());
				 break;
				}	
			   }
			   
			   if(!encontrado)
			   {
				if(proyecto.getNombre_tenente()!=null)    
				 panel.getCBXTenente().addItem(proyecto.getNombre_tenente());
				else
			     panel.getCBXTenente().addItem("Indeterminado");	
								
				if(proyecto.getArea_tenente()!=null)
				 panel.getLArea().setText(proyecto.getArea_tenente().toString());  
				else
				 panel.getLArea().setText("0");
			   }
			   
			   panel.getRBRefTenente().setSelected(true);
		  }
		  else
		  {
		   panel.getRBRefFaja().setSelected(true);
		   panel.getCBXTenente().setEnabled(false);
		  }	
		
		  //Area a reforestar
		  if(proyecto.getArea_reforestar()!=null)
		   panel.getTFAreaRef().setText(proyecto.getArea_reforestar().toString());
		  
		  //Productor
		  if(proyecto.getNombre_productor()!=null)
		   panel.getTFProductor().setText(proyecto.getNombre_productor());
		 
		  //Organismo
		  if(proyecto.getOrganismo()!=null)
		   panel.getTFOrganismo().setText(proyecto.getOrganismo());
		  
		  //Nombre del lugar
		  if(proyecto.getNombre_lugar()!=null)
		   panel.getTFNombreLugar().setText(proyecto.getNombre_lugar());
		  
		  //Erosion de suelos
		  if(proyecto.getTipo_erosion_suelo()!=null)
		  {	  
			  for(int i=0; i<panel.getCBTipoErosion().getItemCount(); i++)
			  {
			   if(proyecto.getTipo_erosion_suelo().equals(panel.getCBTipoErosion().getItemAt(i)))
			   {
				panel.getCBTipoErosion().setSelectedIndex(i);
				break;
			   }				   
			  }	  
		  } 
		  
		  //Grado de erosion
		  if(proyecto.getGrado_erosion_suelo()!=null)
		  {	  
			  for(int i=0; i<panel.getCBGradoErosion().getItemCount(); i++)
			  {
			   if(proyecto.getGrado_erosion_suelo().equals(panel.getCBGradoErosion().getItemAt(i)))
			   {
				panel.getCBGradoErosion().setSelectedIndex(i);
				break;
			   }				   
			  }	  
		  }
		  
		  //Profundidad efectiva
		  if(proyecto.getProfundidad_efectiva()!=null)
		   panel.getTFProfefect().setText(proyecto.getProfundidad_efectiva().toString());
		  
		  //Ph
		  if(proyecto.getPh()!=null)
		   panel.getTFPH().setText(proyecto.getPh().toString());
		  
		  //Materia organica
		  if(proyecto.getMateriaorganica()!=null)
		   panel.getTFMatorg().setText(proyecto.getMateriaorganica().toString());
		  
		  //Vegetacion
		  if(proyecto.getVegetacion()!=null)
		   panel.getTFTipoVegetacion().setText(proyecto.getVegetacion().toString());
		  
		  //Densidad
		  if(proyecto.getDensidad()!=null)
		  {	  
			  for(int i=0; i<panel.getCBDensVegetacion().getItemCount(); i++)
			  {
			   if(proyecto.getDensidad().equals(panel.getCBDensVegetacion().getItemAt(i)))
			   {
				panel.getCBDensVegetacion().setSelectedIndex(i);
				break;
			   }				   
			  }	  
		  }
		  
		  //Observaciones
		  if(proyecto.getObservaciones_veg()!=null)
		   panel.getTFDensObserv().setText(proyecto.getObservaciones_veg().toString());
		  
		  //MarcoX
		  if(proyecto.getMarcox()!=null)
		   panel.getTFMarcoX().setText(proyecto.getMarcox().toString());
		  
		  //MarcoY
		  if(proyecto.getMarcoy()!=null)
	       panel.getjTFMarcoY().setText(proyecto.getMarcoy().toString());
		  
		  //Número de hileras en faja
		  if(proyecto.getHileras()!=null)
		   panel.getTFHileras().setText(proyecto.getHileras().toString());
		  
		  //Precipitación media anual
		  if(proyecto.getPrecipitacion()!=null)
		   panel.getTFPrecMedia().setText(proyecto.getPrecipitacion().toString());
		  
		  //Metodo de plantación
		  if(proyecto.getMetodo_plantacion()!=null)
		  {	  
			  for(int i=0; i<panel.getCBMetPlantacion().getItemCount(); i++)
			  {
			   if(proyecto.getMetodo_plantacion().equals(panel.getCBMetPlantacion().getItemAt(i)))
			   {
				panel.getCBMetPlantacion().setSelectedIndex(i);
				break;
			   }				   
			  }	  
		  }
		  
		  //Proyecto general de una cuenca
		  if(proyecto.getProyecto_general()!=null)
		  {	  
		   boolean proyectogeneral = proyecto.getProyecto_general();
		   if(proyectogeneral)
		   {
			panel.getCBProyTec().setSelected(true);
			panel.getTFEspecifProyecto().setEnabled(true);
			panel.getLEspecificacionProyecto().setEnabled(true);
			
			if(proyecto.getEspecificacion()!=null)
			 panel.getTFEspecifProyecto().setText(proyecto.getEspecificacion());
		   }	    
		  } 
		  
		  //Medidas de conservación de los suelos
		  if(proyecto.getMedidas_suelos()!=null)
		  {	  
		   String []medidas = proyecto.getMedidas_suelos().split(":");
		   if(medidas!=null)
		   {
			for(int i=0; i<medidas.length; i++)
			{
			 if(medidas[i].equals(Barreras_Vivas))
		      panel.getCHBBarrerasVivas().setSelected(true);
			 else
		     if(medidas[i].equals(Zanjas_de_absorción))
		      panel.getCHBZanjasAbsorc().setSelected(true);
		     else
			 if(medidas[i].equals(Terrazas_Individuales))
			  panel.getCHBTerrazasInd().setSelected(true);
			 else
			 if(medidas[i].equals(Acordonamiento_de_residuos))
			  panel.getCHBXAcordResiduos().setSelected(true);
			 else
			 if(medidas[i].equals(Barreras_muertas))
			  panel.getCHBBarrerasMuert().setSelected(true);
			 else
			 if(medidas[i].equals(Terrazas_Continuas))
		      panel.getCHBTerrazasCont().setSelected(true);
			 else
			 if(medidas[i].equals(Hoyos))
			  panel.getCHBHoyos().setSelected(true);
			 else
			 if(medidas[i].equals(Otras))
			  panel.getCHBOtrasSuelos().setSelected(true);
			 else
		     if(medidas[i].equals(Control_de_cárcavas))
			  panel.getCHBControlCarc().setSelected(true);
		     else
			 if(medidas[i].equals(Terrazas_Banco))
		      panel.getCHBTerrazasBanc().setSelected(true);
			 else
			 if(medidas[i].equals(Surcos_en_curvas_de_nivel))
			  panel.getCHBSurcosCurvasNiv().setSelected(true);
			}	
		   }	   
		  } 
		  
		//Medidas contra el libre pastoreo
		  if(proyecto.getMedidas_pastoreo()!=null)
		  {	  
		   String []medidas = proyecto.getMedidas_pastoreo().split(":");
		   if(medidas!=null)
		   {
			for(int i=0; i<medidas.length; i++)
			{
			 if(medidas[i].equals(Cercados))
		      panel.getCHBCercado().setSelected(true);
			 else
			 if(medidas[i].equals(Otras))
			  panel.getCHBOtrasPastoreo().setSelected(true);
			}	
		   }	   
		  }
		  
		//Medidas contra incendios
		  if(proyecto.getMedidas_incendios()!=null)
		  {	  
		   String []medidas = proyecto.getMedidas_incendios().split(":");
		   if(medidas!=null)
		   {
			for(int i=0; i<medidas.length; i++)
			{
			 if(medidas[i].equals(Trochas_cortafuegos))
		      panel.getCHBTrochas().setSelected(true);
			 else
			 if(medidas[i].equals(Fajas_verdes))
			  panel.getCHBFajasVerdes().setSelected(true);
			 else
			 if(medidas[i].equals(Otras))
			  panel.getCHBOtrasIncendios().setSelected(true);
			}	
		   }	   
		  }
		  
		 //Especies
		  listaEspecies = proyecto.getListaespecies();
		  if(listaEspecies!=null)
		  {
			  String value = "";
			  for(int i=0; i<listaEspecies.length; i++)
			   value += listaEspecies[i].getSiglas()+", ";
			   
			  if(value.indexOf(",") > 0)
			   value = value.substring(0, value.length()-2);
			   
			  panel.getTFCompEspecies().setText(value);
		  }	  
		  
		//Suelos
		  listaSuelos = proyecto.getListaSuelos();
		  
		  //Ficha de costo
			 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
			 db.ejecutarConsulta("select actividades,cantidad,um,cup,cuc,total " +
			 		"from ficha_costo where proyecto="+proyecto.getId());
			 if(!db.isEmpty())
			 {	 
				 model = (DefaultTableModel)panel.getTableFichaCosto().getModel();
				 for(int i=0; i<db.getRowCount(); i++)
				 {	 
					 String actividad = db.getValueAsString(i, 0).toString();
					 String cantidad = db.getValueAsString(i, 1).toString();
					 String um = db.getValueAsString(i, 2).toString();
					 String cup = db.getValueAsString(i, 3).toString();
					 String cuc = db.getValueAsString(i, 4).toString();
					 String total = db.getValueAsString(i, 5).toString();
					 
					  Object []row = new Object[6];
					  row[0] = actividad;
					  row[1] = cantidad;
					  row[2] = um;
					  row[3] = cup;
					  row[4] = cuc;
					  row[5] = total;
					  
					  model.addRow(row);
				 }
			 }
		  
//		  JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
//		  db.ejecutarConsulta("select especies.siglas from especies inner join proyecto_especies" +
//		  		" on especies.id=proyecto_especies.especie");
//		  if(!db.isEmpty())
//		  {
//		   String value = "";	  
//		   for(int i=0; i<db.getRowCount(); i++)
//		    value += db.getValueAsString(i,0)+", ";
//		   
//		   if(value.indexOf(",")>0)
//		    value = value.substring(0, value.length()-2);
//		   
//		   panel.getTFCompEspecies().setText(value);
//		  }	  
		  /***************************OJO!!!!
		   * Agregar gestion de suelos y especies!!!!!
		   */
	 }

	/**
	private void mostrar_reporte()
	{
		Proyecto_reforestacionWT reporte = new Proyecto_reforestacionWT(gids, table, ancho, ftable);
    	if(reporte.isOK())
    	{	
    	 BackgroundExecution.cancelableBackgroundExecution(reporte);
    	} 
	}*/

	//================================================================================================================================================= 
	   /**Muestra el diálogo de selección de las especies*/
	    private void dialogoespecies() 
		{
	    	JFrame frame = new javax.swing.JFrame();
//			ImageIcon ii = new ImageIcon(PluginServices.getPluginServices("org.geocuba.foresta").getPluginDirectory().getAbsolutePath()+""+Global.fileSeparator+"images"+Global.fileSeparator+"password.gif");
//			frame.setIconImage(ii.getImage());
			
		 especiesDlg = new Proyecto_EspeciesDlg(frame, "Selección de especies");
		 especiesDlg.setLocationRelativeTo(null);
		 	
		//Lleno el combobox de especies 
		 actualizar_combo_especies();
		 
		//Lleno la tabla de especies
//		 String where = "";
//    	 if(!proyecto.IsNewObject())
//          where = "where proyecto="+proyecto.getId();
    	 
//		 espman = new EspecieManager();
//		 Especie []especies = espman.Cargar_Especies("from especies inner join proyecto_especies on " +
//	              "especies.id=proyecto_especies.especie "+where+
//	              " order by siglas, especies.id, nomb_cientifico, ncomun");
		 
		// listaEspecies = _especies;
		 model = (DefaultTableModel)especiesDlg.getTableEspecies().getModel();

		if(listaEspecies!=null)
		{	
		 for(int i=0; i<listaEspecies.length; i++)
		 {
		  Object []row = new Object[4];
		  row[0] = listaEspecies[i].getId();
		  row[1] = listaEspecies[i].getSiglas();
		  row[2] = listaEspecies[i].getNcomun();
		  row[3] = listaEspecies[i].getNomb_cientifico();
		  
		  model.addRow(row);
		 }		 
		}	 
		 
//		 especies_quitarTemp = new ArrayList<Integer>();
//		 especies_agregarTemp = new ArrayList<Integer>();
		 
		 setDialogoEspeciesListeners();
		 especiesDlg.show();
		}
	    
	    /**Actualizar el combo de del dialogo de
	     * seleccion de especies*/
	    private void actualizar_combo_especies()
		{
//	    	 String where = "";
//	    	 if(!proyecto.IsNewObject())
//	          where = "where proyecto="+proyecto.getId();
	    	 
//			 EspecieManager espman = new EspecieManager();
//			 Especie []listaEspecies = espman.Cargar_Especies("from especies where id not in" +
//				 		"(select especie from proyecto_especies "+where+")" +
//				        " order by ncomun, especies.id, nomb_cientifico, siglas");
	    	
	    	 String value = "";
	    	 if(listaEspecies!=null)
	    	 {
	    	  for(int i=0; i<listaEspecies.length; i++)
	    	   value += listaEspecies[i].getId()+",";
	    	  value = " where id not in("+value.substring(0, value.length()-1)+") order by ncomun";
	    	 }	 
	    	 
	    	 EspecieManager espman = new EspecieManager();
	    	 Especie []lista = espman.Cargar_Especies("from especies "+value);
			 
			 especiesDlg.getComboEspecies().removeAllItems();
			 
			if(lista!=null)
			{
			 especiesDlg.getButtonAdd().setEnabled(true);	
			 for(int i=0; i<lista.length; i++)
			  especiesDlg.getComboEspecies().addItem(lista[i]);
			} 
			else
			 especiesDlg.getButtonAdd().setEnabled(false);
		}
	    
	    private void setDialogoEspeciesListeners()
	    {
//	      //---------------------------------------------------------------------------------
//	    	especiesDlg.addWindowListener(new WindowAdapter(){
//	    	    public void windowClosing(WindowEvent we)
//	    	    {
//	    	     especies_quitar = new ArrayList<Integer>();
//	    		 especies_agregar = new ArrayList<Integer>();     
//	    	    }
//	    	});
	    	//---------------------------------------------------------------------------------
	    	especiesDlg.getButtonAceptar().addActionListener(new java.awt.event.ActionListener() {
	    		public void actionPerformed(java.awt.event.ActionEvent evt) 
	            {
//	    		 especies_agregar = especies_agregarTemp;
//	    		 especies_quitar = especies_quitarTemp;
	    		 
	    		 panel.getTFCompEspecies().setText("");
	    		 String value = "";
	    		 int cant = especiesDlg.getTableEspecies().getRowCount();
	    		 listaEspecies = new Especie[cant];
	    		 
	    		 for(int i=0; i<cant; i++)
	    		 {	 
	    		  int id = Integer.parseInt(especiesDlg.getTableEspecies().getValueAt(i,0).toString());
	    		  String siglas = especiesDlg.getTableEspecies().getValueAt(i,1).toString();
	    		  String ncomun = especiesDlg.getTableEspecies().getValueAt(i,2).toString();
	    		  String ncientifico = especiesDlg.getTableEspecies().getValueAt(i,3).toString();
	    		  
	    		  Especie esp = new Especie(id, siglas, ncientifico, ncomun, false);
	    		  listaEspecies[i] = esp;
	    		  value += siglas+", ";
	    		 } 
	    		 
	    		 if(cant > 0)
	    		 {	 
	    		  if(value.indexOf(",")>0)	 
	    		   value = value.substring(0, value.length()-2);
	    		  
	    		  panel.getTFCompEspecies().setText(value);
	    		 } 
	    		 
	    		 especiesDlg.dispose();
	            }
	         });
	    	//--------------------------------------------------------------------------------- 
	    	especiesDlg.getButtonCancelar().addActionListener(new java.awt.event.ActionListener() {
	    		public void actionPerformed(java.awt.event.ActionEvent evt) 
	    	    {
	    			especiesDlg.dispose();
	    	    }
	    	 });
	    	//--------------------------------------------------------------------------------- 
	    	especiesDlg.getButtonQuitar().addActionListener(new java.awt.event.ActionListener() {
	    		public void actionPerformed(java.awt.event.ActionEvent evt) 
	    	    {
	    		 int index = especiesDlg.getTableEspecies().getSelectedRow(); 
	    		 int id = Integer.parseInt(model.getValueAt(index, 0).toString());
	    		 
//					 EspecieManager espman = new EspecieManager(); 
//					 Especie esp = espman.Cargar_Objeto_BD(id);
					  
		    		  String siglas = especiesDlg.getTableEspecies().getValueAt(index,1).toString();
		    		  String ncomun = especiesDlg.getTableEspecies().getValueAt(index,2).toString();
		    		  String ncientifico = especiesDlg.getTableEspecies().getValueAt(index,3).toString();
		    		  
		    		  Especie esp = new Especie(id, siglas, ncientifico, ncomun, false);
					
	    			 especiesDlg.getComboEspecies().addItem(esp);   //Agrego a la lista de especies
//	    			 especies_quitarTemp.add(id);
//	    			 
//	    			 for(int i=0; i<especies_agregarTemp.size(); i++)
//	    			 {
//	    			  if(especies_agregarTemp.get(i) == id)
//	    			  {	  
//	    			   especies_agregarTemp.remove(i);
//	    			   break;
//	    			  } 
//	    			 }
	    			 
	    			 model.removeRow(index); 
	    			 
	    			 if(model.getRowCount()<1)
	    			  especiesDlg.getButtonQuitar().setEnabled(false);
	    			 else
	    			  especiesDlg.getTableEspecies().getSelectionModel().setSelectionInterval(model.getRowCount()-1, model.getRowCount()-1); 
	    			 
	    			 if(!especiesDlg.getButtonAdd().isEnabled())
	    			  especiesDlg.getButtonAdd().setEnabled(true);
	    			 
	    			 especiesDlg.getButtonAceptar().setEnabled(true); 
	    	    }
	    	 });
	    	//--------------------------------------------------------------------------------- 
	    	especiesDlg.getButtonAdd().addActionListener(new java.awt.event.ActionListener() {
	    		public void actionPerformed(java.awt.event.ActionEvent evt) 
	    	    {
//	    		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	    		 Especie especie = (Especie)especiesDlg.getComboEspecies().getSelectedItem();
	    		 
	    			  Object []row = new Object[4];
	    			  row[0] = especie.getId();
	    			  row[1] = especie.getSiglas();
	    			  row[2] = especie.getNcomun();
	    			  row[3] = especie.getNomb_cientifico();
	    			  
	    			  model.addRow(row);
	    			  
//	    		 if(!proyecto.IsNewObject())
//	    		 {	 
//	    			 //Si la especie no estaba recomendada:
//       			     //(Si quito una y luego la recomiendo, está en la lista pero no es necesario volverla a recomendar)
//	    			  db.ejecutarConsulta("select * from proyecto_especies where especie="+especie.getId()+" " +
//	    			  		"and proyecto="+proyecto.getId());
//	    			  if(db.isEmpty())
//	    			   especies_agregarTemp.add(especie.getId());
//	    				 
//	    				 for(int i=0; i<especies_quitarTemp.size(); i++)
//	    				 {
//	    				  if(especies_quitarTemp.get(i) == especie.getId())
//	    				  {	
//	    				   especies_quitarTemp.remove(i);
//	    				   break;
//	    				  } 
//	    				 }
//	    		 }			 
	    				 
	    		 especiesDlg.getComboEspecies().removeItemAt(especiesDlg.getComboEspecies().getSelectedIndex());
	    		 
	    		 if(especiesDlg.getComboEspecies().getItemCount()<1)
	    		  especiesDlg.getButtonAdd().setEnabled(false);
	    		 
	    		 if(!especiesDlg.getButtonQuitar().isEnabled())
	    		  especiesDlg.getButtonQuitar().setEnabled(true);
	    		 
	    		 especiesDlg.getButtonAceptar().setEnabled(true); 
	    	    }
	    	 });
	       //---------------------------------------------------------------------------------
	    	especiesDlg.getTableEspecies().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
	             public void valueChanged(ListSelectionEvent e) 
	             {
	              if(!especiesDlg.getButtonQuitar().isEnabled())	
	               especiesDlg.getButtonQuitar().setEnabled(true);  
	             }
	         });
	    }
	    
	  //================================================================================================================================================= 
		   /**Muestra el diálogo de selección de los suelos*/
		    private void dialogoSuelos() 
			{
		     JFrame frame = new javax.swing.JFrame();
				
			 suelosDlg = new Proyecto_SuelosDlg(frame, "Selección de suelos");
			 suelosDlg.setLocationRelativeTo(null);
			 	
			//Lleno el combobox de suelos 
			 actualizar_combo_suelos();
			 
			// listaSuelos = _suelos;
			 model = (DefaultTableModel)suelosDlg.getTableSuelos().getModel();

			if(listaSuelos!=null)
			{	
			 for(int i=0; i<listaSuelos.length; i++)
			 {
			  Object []row = new Object[3];
			  row[0] = listaSuelos[i].getID();
			  row[1] = listaSuelos[i].getClave();
			  row[2] = listaSuelos[i].getTipo();
			  
			  model.addRow(row);
			 }		 
			}	 
			 
			 setDialogoSuelosListeners();
			 suelosDlg.show();
			}
		    
		    /**Actualizar el combo de del dialogo de
		     * seleccion de suelos*/
		    private void actualizar_combo_suelos()
			{
//		    	 String where = "";
//		    	 if(!proyecto.IsNewObject())
//		          where = "where proyecto="+proyecto.getId();
		    	 
		    	 String value = "";
		    	 if(listaSuelos!=null)
		    	 {
		    	  for(int i=0; i<listaSuelos.length; i++)
		    	   value += listaSuelos[i].getID()+",";
		    	  value = " where id not in("+value.substring(0, value.length()-1)+") order by tipo";
		    	 }	 
		    	 
				 Tipo_suelo []lista = Tipo_sueloManager.get_Tipos_suelos("from tipo_suelo "+value);
				 
				 suelosDlg.getComboSuelos().removeAllItems();
				 
				if(lista!=null)
				{
				 suelosDlg.getButtonAdd().setEnabled(true);	
				 for(int i=0; i<lista.length; i++)
				  suelosDlg.getComboSuelos().addItem(lista[i]);
				} 
				else
				 suelosDlg.getButtonAdd().setEnabled(false);
			}
		    
		    private void setDialogoSuelosListeners()
		    {
		    	//---------------------------------------------------------------------------------
		    	suelosDlg.getButtonAceptar().addActionListener(new java.awt.event.ActionListener() {
		    		public void actionPerformed(java.awt.event.ActionEvent evt) 
		            {
		    		 int cant = suelosDlg.getTableSuelos().getRowCount();
		    		 listaSuelos = new Tipo_suelo[cant];
		    		 
		    		 for(int i=0; i<cant; i++)
		    		 {	 
		    		  int id = Integer.parseInt(suelosDlg.getTableSuelos().getValueAt(i,0).toString());
		    		  String clave = suelosDlg.getTableSuelos().getValueAt(i,1).toString();
		    		  String suelo = suelosDlg.getTableSuelos().getValueAt(i,2).toString();
		    		  
		    		  Tipo_suelo tiposuelo = new Tipo_suelo(id, clave, suelo, false);
		    		  listaSuelos[i] = tiposuelo;
		    		 } 
		    		 
		    		 suelosDlg.dispose();
		            }
		         });
		    	//--------------------------------------------------------------------------------- 
		    	suelosDlg.getButtonCancelar().addActionListener(new java.awt.event.ActionListener() {
		    		public void actionPerformed(java.awt.event.ActionEvent evt) 
		    	    {
		    			suelosDlg.dispose();
		    	    }
		    	 });
		    	//--------------------------------------------------------------------------------- 
		    	suelosDlg.getButtonQuitar().addActionListener(new java.awt.event.ActionListener() {
		    		public void actionPerformed(java.awt.event.ActionEvent evt) 
		    	    {
		    		 int index = suelosDlg.getTableSuelos().getSelectedRow(); 
		    		 int id = Integer.parseInt(model.getValueAt(index, 0).toString());
		    		 
			    		  String clave = suelosDlg.getTableSuelos().getValueAt(index,1).toString();
			    		  String suelo = suelosDlg.getTableSuelos().getValueAt(index,2).toString();
			    		  
			    		  Tipo_suelo tiposuelo = new Tipo_suelo(id, clave, suelo, false);
						
			    		  suelosDlg.getComboSuelos().addItem(tiposuelo);   //Agrego a la lista de suelos
		    			 
		    			 model.removeRow(index); 
		    			 
		    			 if(model.getRowCount()<1)
		    			  suelosDlg.getButtonQuitar().setEnabled(false);
		    			 else
		    		      suelosDlg.getTableSuelos().getSelectionModel().setSelectionInterval(model.getRowCount()-1, model.getRowCount()-1); 
		    			 
		    			 if(!suelosDlg.getButtonAdd().isEnabled())
		    			  suelosDlg.getButtonAdd().setEnabled(true);
		    			 
		    			 suelosDlg.getButtonAceptar().setEnabled(true); 
		    	    }
		    	 });
		    	//--------------------------------------------------------------------------------- 
		    	suelosDlg.getButtonAdd().addActionListener(new java.awt.event.ActionListener() {
		    		public void actionPerformed(java.awt.event.ActionEvent evt) 
		    	    {
		    			Tipo_suelo tiposuelo = (Tipo_suelo)suelosDlg.getComboSuelos().getSelectedItem();
		    		 
		    			  Object []row = new Object[4];
		    			  row[0] = tiposuelo.getID();
		    			  row[1] = tiposuelo.getClave();
		    			  row[2] = tiposuelo.getTipo();
		    			  
		    			  model.addRow(row);
		    			  
		    			  suelosDlg.getComboSuelos().removeItemAt(suelosDlg.getComboSuelos().getSelectedIndex());
		    		 
		    		 if(suelosDlg.getComboSuelos().getItemCount()<1)
		    		  suelosDlg.getButtonAdd().setEnabled(false);
		    		 
		    		 if(!suelosDlg.getButtonQuitar().isEnabled())
		    		  suelosDlg.getButtonQuitar().setEnabled(true);
		    		 
		    		 suelosDlg.getButtonAceptar().setEnabled(true); 
		    	    }
		    	 });
		       //---------------------------------------------------------------------------------
		    	suelosDlg.getTableSuelos().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
		             public void valueChanged(ListSelectionEvent e) 
		             {
		              if(!suelosDlg.getButtonQuitar().isEnabled())	
		               suelosDlg.getButtonQuitar().setEnabled(true);  
		             }
		         });
		    }
 }

 