package org.geocuba.foresta.gestion_datos.gui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.geocuba.foresta.gestion_datos.Especie;
import org.geocuba.foresta.gestion_datos.PersistentObject;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;

 public class pEspecieManager implements IPanelManager 
 {
	private pEspecie panel;
	private Especie especie;
	
	public pEspecieManager() {
	}
	
	@Override
	public void MostrarPanel(PersistentObject persistent) 
	{
	 panel = new pEspecie("Especie");
		
	 especie = (Especie) persistent;
	 
	 if(!especie.IsNewObject()) //Se está modificando
	 {
	  //panel.getLabelId().setText(especie.getId().toString());
		 panel.SetTitle(panel.Title()+". Identificador "+especie.getId());
		 String panelId = "_"+panel.Title()+especie.getId();
		  Panel p = UIUtils.GetPanel(panelId);
		
		 /*Si se va a modificar y habia una ficha de informacion abierta
		  * la cierro. 
		  * Si se va a mostrar la informacion y ya habia una ficha de informacion
		  * abierta no hago nada*/  
		  if(p == null)       //Panel no existe
		   panel.set_panel_id(panelId);   //Se va a obtener informacion solamente
		  else
		  if(especie.isEditing())  //Si se va a modificar
		  {	  
		   p.Close();
		   panel.set_panel_id(panelId);
		  } 
		  else
		   return;  	  //No muestro la ventana
	  
	  if(especie.getSiglas()!=null)
	   panel.getTFSiglas().setText(especie.getSiglas());
	  
	  if(especie.getNcomun()!=null)
	   panel.getTFNComun().setText(especie.getNcomun());
	  
	  if(especie.getNomb_cientifico()!=null)
	   panel.getTFNCientifico().setText(especie.getNomb_cientifico());
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
		 especie.setSiglas(panel.getTFSiglas().getText());
		 especie.setNcomun(panel.getTFNComun().getText());
		 especie.setNomb_cientifico(panel.getTFNCientifico().getText());
		 
		 especie.save();
		 panel.Close();
				
		 pGestionManager.ActualizarTabla(pGestionManager.SQL_ESPECIES, "");
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
	 panel.getTFSiglas().getDocument().addDocumentListener(doclist); 
	//---------------------------------------------------------------------------------
	 panel.getTFNComun().getDocument().addDocumentListener(doclist);
	//---------------------------------------------------------------------------------
	 panel.getTFNCientifico().getDocument().addDocumentListener(doclist);
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
	  panel.getTFSiglas().setEditable(false);
	  panel.getTFNComun().setEditable(false);
	  panel.getTFNCientifico().setEditable(false);
	 }  
	}
 }
