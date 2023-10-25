package org.geocuba.foresta.gestion_datos.gui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.geocuba.foresta.gestion_datos.Agrupador_uso;
import org.geocuba.foresta.gestion_datos.PersistentObject;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;

 public class pAgrupador_usoManager implements IPanelManager 
 {
	private pAgrupador_uso panel;
	private Agrupador_uso agrupador_uso;
	
	public pAgrupador_usoManager() {
	}
	
	@Override
	public void MostrarPanel(PersistentObject persistent) 
	{
	 panel = new pAgrupador_uso("Tipo de uso");
		
	 agrupador_uso = (Agrupador_uso) persistent;
	 
	 if(!agrupador_uso.IsNewObject()) //Se está modificando
	 {
	  //panel.getLabelId().setText(agrupador_uso.getId().toString());
		  panel.SetTitle(panel.Title()+". Identificador "+agrupador_uso.getId());
		  String panelId = "_"+panel.Title()+agrupador_uso.getId();
		  Panel p = UIUtils.GetPanel(panelId);
		
		 /*Si se va a modificar y habia una ficha de informacion abierta
		  * la cierro. 
		  * Si se va a mostrar la informacion y ya habia una ficha de informacion
		  * abierta no hago nada*/  
		  if(p == null)       //Panel no existe
		   panel.set_panel_id(panelId);   //Se va a obtener informacion solamente
		  else
		  if(agrupador_uso.isEditing())  //Si se va a modificar
		  {	  
		   p.Close();
		   panel.set_panel_id(panelId);
		  } 
		  else
		   return;  	  //No muestro la ventana
	  
	  if(agrupador_uso.getTipo_uso()!=null)
	   panel.getTFTipoUso().setText(agrupador_uso.getTipo_uso());
	 }
	 else
	  panel.set_panel_id("_"+panel.Title());
		 
	 setComponentsListeners();	
	 panel.Show();
	}
	
	private void setComponentsListeners()
	{
	 panel.getButtonGuardar().addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) 
        {
				agrupador_uso.setTipo_uso(panel.getTFTipoUso().getText());
				agrupador_uso.save();
				panel.Close();
				
				pGestionManager.ActualizarTabla(pGestionManager.SQL_AGRUPADORES_USO, "");
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
	 panel.getTFTipoUso().getDocument().addDocumentListener(new DocumentListener() { 
         public void changedUpdate(DocumentEvent e) { 
             panel.getButtonGuardar().setEnabled(true); 
         } 
         public void removeUpdate(DocumentEvent e) { 
        	 panel.getButtonGuardar().setEnabled(true); 
         } 
         public void insertUpdate(DocumentEvent e) { 
        	 panel.getButtonGuardar().setEnabled(true); 
         } 
     }); 
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
	  panel.getTFTipoUso().setEditable(false);
	 }  
	}
 }
