package org.geocuba.foresta.gestion_datos.gui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.Especie;
import org.geocuba.foresta.gestion_datos.EspecieManager;
import org.geocuba.foresta.gestion_datos.PersistentObject;
import org.geocuba.foresta.gestion_datos.Tipo_suelo;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;

 public class pTipo_sueloManager implements IPanelManager 
 {
	private pTipo_suelo panel;
	private Tipo_suelo tipo_suelo;
	//private List<Especie> especies_recomendadas;
	private List<String> especies_quitar;
	private List<String> especies_agregar;
	private DefaultTableModel model;
	private EspecieManager espman;
	
	public pTipo_sueloManager() {
	}
	
	@Override
	public void MostrarPanel(PersistentObject persistent) 
	{
	 panel = new pTipo_suelo("Tipo de suelo");
	 	
	 tipo_suelo = (Tipo_suelo) persistent;
	 
	//Lleno el combobox de especies a recomendar 
	 actualizar_combo_especies();
	 
	//Lleno la tabla de especies recomendadas
	 espman = new EspecieManager();
	 Especie []especies = espman.Cargar_Especies("from especies inner join tipo_suelo_especies on " +
              "especies.siglas=tipo_suelo_especies.especie where tipo_suelo="+tipo_suelo.getID()+
              " order by siglas, especies.id, ncientifico, ncomun");
	 model = (DefaultTableModel)panel.getTableEspecies().getModel();

	if(especies!=null)
	{	
	 //especies_recomendadas = new ArrayList<Especie>();
	 for(int i=0; i<especies.length; i++)
	 {
	  Object []row = new Object[4];
	  row[0] = especies[i].getId();
	  row[1] = especies[i].getSiglas();
	  row[2] = especies[i].getNcomun();
	  row[3] = especies[i].getNomb_cientifico();
	  
	  model.addRow(row);
	  
	 // especies_recomendadas.add(especies[i]);
	 }		 
	}	 
	 
	 if(!tipo_suelo.IsNewObject()) //Se está modificando
	 {
//	  panel.getLabelId().setText(tipo_suelo.getID().toString());
		 panel.SetTitle(panel.Title()+". Identificador "+tipo_suelo.getID());
		 String panelId = "_"+panel.Title()+tipo_suelo.getID();
		  Panel p = UIUtils.GetPanel(panelId);
		
		 /*Si se va a modificar y habia una ficha de informacion abierta
		  * la cierro. 
		  * Si se va a mostrar la informacion y ya habia una ficha de informacion
		  * abierta no hago nada*/  
		  if(p == null)       //Panel no existe
		   panel.set_panel_id(panelId);   //Se va a obtener informacion solamente
		  else
		  if(tipo_suelo.isEditing())  //Si se va a modificar
		  {	  
		   p.Close();
		   panel.set_panel_id(panelId);
		  } 
		  else
		   return;  	  //No muestro la ventana
	  
	  if(tipo_suelo.getClave()!=null)
	   panel.getTFClave().setText(tipo_suelo.getClave());
	  
	  if(tipo_suelo.getTipo()!=null)
	   panel.getTFTipo().setText(tipo_suelo.getTipo());
	 }
	 else
		  panel.set_panel_id("_"+panel.Title());
	 
	 especies_quitar = new ArrayList<String>();
	 especies_agregar = new ArrayList<String>();
	 
	 setComponentsListeners();
	 panel.Show();
	}
	
	private void setComponentsListeners()
	{
	 panel.getButtonGuardar().addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) 
        {
		 tipo_suelo.setClave(panel.getTFClave().getText());
		 tipo_suelo.setTipo(panel.getTFTipo().getText());
		 
		 tipo_suelo.save();
		 
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 
		//Elimino las especies que se dejaron de recomendar 
		 for(int i=0; i<especies_quitar.size(); i++)
		 {
		  db.ejecutarConsulta("delete from tipo_suelo_especies where especie='"+especies_quitar.get(i)+"' " +
		   "and tipo_suelo="+tipo_suelo.getID()); 
		  
		  TrazasManager.insertar_Traza("Dejó de recomendar la especie "+especies_quitar.get(i)+" para el tipo de suelo "+tipo_suelo.getTipo());
		 }	 
		 
		//Agrego las especies que se recomendaron
		 for(int i=0; i<especies_agregar.size(); i++)
		 {
		  db.ejecutarConsulta("insert into tipo_suelo_especies(especie,tipo_suelo) " +
			              "values('"+especies_agregar.get(i)+"',"+tipo_suelo.getID()+")");
		  
		  TrazasManager.insertar_Traza("Recomendó la especie "+especies_agregar.get(i)+" para el tipo de suelo "+tipo_suelo.getTipo());
		 }	
		 
		 panel.Close();
				
		 pGestionManager.ActualizarTabla(pGestionManager.SQL_TIPOS_SUELO, "");
        }
     });
	//--------------------------------------------------------------------------------- 
	 panel.getButtonCancelar().addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) 
	    {
		 panel.Close();
	    }
	 });
	//--------------------------------------------------------------------------------- 
	 panel.getButtonQuitar().addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) 
	    {
		 //JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 int index = panel.getTableEspecies().getSelectedRow(); 
//		 db.executeQuery("delete from tipo_suelo_especies where especie="+especies_recomendadas.get(index).getId()+" " +
//		 		"and tipo_suelo="+tipo_suelo.getID());
		 
		// actualizar_combo_especies();
		 
		 int id = Integer.parseInt(model.getValueAt(index, 0).toString());
		 
		 Especie esp = espman.Cargar_Objeto_BD(id);
		 panel.getComboEspesies().addItem(esp);   //Agrego a la lista de especies para recomendar
		 
		 String siglas = esp.getSiglas();
		 especies_quitar.add(siglas);
		 
		 for(int i=0; i<especies_agregar.size(); i++)
		 {
		  if(especies_agregar.get(i).equals(siglas))
		  {	  
		   especies_agregar.remove(i);
		   break;
		  } 
		 }
		 
		// especies_recomendadas.remove(index);
		 
		 model.removeRow(index); 
		 
		 if(model.getRowCount()<1)
		  panel.getButtonQuitar().setEnabled(false);
		 else
		  panel.getTableEspecies().getSelectionModel().setSelectionInterval(model.getRowCount()-1, model.getRowCount()-1); 
		 
		 if(!panel.getButtonRecomendar().isEnabled())
		  panel.getButtonRecomendar().setEnabled(true);
		 
		 panel.getButtonGuardar().setEnabled(true);
	    }
	 });
	//--------------------------------------------------------------------------------- 
	 panel.getButtonRecomendar().addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) 
	    {
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 //int index = panel.getTableEspecies().getSelectedRow(); 
		 Especie especie = (Especie)panel.getComboEspesies().getSelectedItem();
		 
//		 db.executeQuery("insert into tipo_suelo_especies(especie,tipo_suelo) " +
//		 		"values("+especie.getId()+","+tipo_suelo.getID()+")");
		 
		// especies_recomendadas.add(especie);
		 
			  Object []row = new Object[4];
			  row[0] = especie.getId();
			  row[1] = especie.getSiglas();
			  row[2] = especie.getNcomun();
			  row[3] = especie.getNomb_cientifico();
			  
			  model.addRow(row);
			  
			 //Si la especie no estaba recomendada (Si quito una y luego la recomiendo, está en la lista pero no es necesario volverla a recomendar)
			  db.ejecutarConsulta("select * from tipo_suelo_especies where especie='"+especie.getSiglas()+"' " +
			  		"and tipo_suelo="+tipo_suelo.getID());
			  if(db.isEmpty())
			   especies_agregar.add(especie.getSiglas());
				 
				 for(int i=0; i<especies_quitar.size(); i++)
				 {
				  if(especies_quitar.get(i).equals(especie.getSiglas()))
				  {	
				   especies_quitar.remove(i);
				   break;
				  } 
				 }
				 
				 panel.getComboEspesies().removeItemAt(panel.getComboEspesies().getSelectedIndex());
		 
		// actualizar_combo_especies();
		 
		 if(panel.getComboEspesies().getItemCount()<1)
		  panel.getButtonRecomendar().setEnabled(false);
		 
		 if(!panel.getButtonQuitar().isEnabled())
		  panel.getButtonQuitar().setEnabled(true);
		 
		 panel.getButtonGuardar().setEnabled(true); 
	    }
	 });
	//---------------------------------------------------------------------------------
	 DocumentListener doclist = new DocumentListener() { 
         public void changedUpdate(DocumentEvent e) { 
             panel.getButtonGuardar().setEnabled(true); 
         } 
         public void removeUpdate(DocumentEvent e) { 
        	 panel.getButtonGuardar().setEnabled(true); 
         } 
         public void insertUpdate(DocumentEvent e) { 
        	 panel.getButtonGuardar().setEnabled(true); 
         } 
     };
   //---------------------------------------------------------------------------------
	 panel.getTableEspecies().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
         public void valueChanged(ListSelectionEvent e) 
         {
          if(!panel.getButtonQuitar().isEnabled())	
           panel.getButtonQuitar().setEnabled(true);  
         }
     });
    //---------------------------------------------------------------------------------
	 panel.getTFClave().getDocument().addDocumentListener(doclist); 
	//---------------------------------------------------------------------------------
	 panel.getTFTipo().getDocument().addDocumentListener(doclist); 
	}

	@Override
	public void HabilitarGuardado() 
	{
	 if(panel != null)	
	  panel.getButtonGuardar().setEnabled(true);		
	}

	@Override
	public void DeshabilitarComponentes() 
	{
	 if(panel != null)
	 {	 
	  panel.getButtonGuardar().setVisible(false);
	  panel.getButtonCancelar().setVisible(false);
	  panel.getButtonRecomendar().setVisible(false);
	  panel.getButtonQuitar().setVisible(false);
	  panel.getComboEspesies().setVisible(false);
	  panel.getLabelEspeciesSinRecomendar().setVisible(false);
	  panel.getTFTipo().setEditable(false);
	  panel.getTFClave().setEditable(false);
	 }  
	}
	
	private void actualizar_combo_especies()
	{
		 EspecieManager espman = new EspecieManager();
		 Especie []listaEspecies = espman.Cargar_Especies("from especies where siglas not in" +
			 		"(select especie from tipo_suelo_especies where tipo_suelo="+tipo_suelo.getID()+") " +
			"order by ncomun, especies.id, ncientifico, siglas");
		 panel.getComboEspesies().removeAllItems();
		 
		if(listaEspecies!=null)
		{
		 panel.getButtonRecomendar().setEnabled(true);	
		 for(int i=0; i<listaEspecies.length; i++)
		  panel.getComboEspesies().addItem(listaEspecies[i]);
		} 
		else
		 panel.getButtonRecomendar().setEnabled(false);
	}
 }
