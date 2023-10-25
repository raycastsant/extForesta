package org.geocuba.foresta.gestion_datos.gui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.geocuba.foresta.gestion_datos.PersistentObject;
import org.geocuba.foresta.gestion_datos.Tipo_hidrografia;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;

 public class pTipo_hidrografiaManager implements IPanelManager 
 {
	private pTipo_hidrografia panel;
	private Tipo_hidrografia tipo_hidrografia;
	
	public pTipo_hidrografiaManager() {
	}
	
	@Override
	public void MostrarPanel(PersistentObject persistent) 
	{
	 panel = new pTipo_hidrografia("Tipo de hidrografía");
	 
	 tipo_hidrografia = (Tipo_hidrografia) persistent;
	 
	 if(!tipo_hidrografia.IsNewObject()) //Se está modificando
	 {
//	  panel.getLabelId().setText(tipo_hidrografia.getId().toString());
		 panel.SetTitle(panel.Title()+". Identificador "+tipo_hidrografia.getId());
		 String panelId = "_"+panel.Title()+tipo_hidrografia.getId();
		  Panel p = UIUtils.GetPanel(panelId);
		
		 /*Si se va a modificar y habia una ficha de informacion abierta
		  * la cierro. 
		  * Si se va a mostrar la informacion y ya habia una ficha de informacion
		  * abierta no hago nada*/  
		  if(p == null)       //Panel no existe
		   panel.set_panel_id(panelId);   //Se va a obtener informacion solamente
		  else
		  if(tipo_hidrografia.isEditing())  //Si se va a modificar
		  {	  
		   p.Close();
		   panel.set_panel_id(panelId);
		  } 
		  else
		   return;  	  //No muestro la ventana
	  
	  if(tipo_hidrografia.getCodigo()!=null)
	   panel.getTFCodigo().setText(tipo_hidrografia.getCodigo());
	  
	  if(tipo_hidrografia.getDescripcion()!=null)
	   panel.getTFDescripcion().setText(tipo_hidrografia.getDescripcion());
	  
	  if(tipo_hidrografia.getAncho_faja()!=null)
		panel.getTFAncho().setText(tipo_hidrografia.getAncho_faja().toString());
	  
	  if(tipo_hidrografia.getTipo_elemento()!=null)
	  {
	   if(tipo_hidrografia.getTipo_elemento().equals("rio"))
	    panel.getRBLineal().setSelected(true);
	   else
		panel.getRBAreal().setSelected(true);
	  }	  
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
		 tipo_hidrografia.setCodigo(panel.getTFCodigo().getText());
		 tipo_hidrografia.setDescripcion(panel.getTFDescripcion().getText());
		 
		 String rio = panel.getTFAncho().getText();
		 if(rio.equals(""))
          rio = "0";			 
		 tipo_hidrografia.setAncho_faja(Double.parseDouble(rio));
		 
		 if(panel.getRBLineal().isSelected())
		  tipo_hidrografia.setTipo_elemento("rio");
		 else
		  tipo_hidrografia.setTipo_elemento("embalse");
		 
		 tipo_hidrografia.save();
		 panel.Close();
				
		 pGestionManager.ActualizarTabla(pGestionManager.SQL_TIPOS_HIDROGRAFIA, "");
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
	 panel.getTFCodigo().getDocument().addDocumentListener(doclist); 
	//---------------------------------------------------------------------------------
	 panel.getTFDescripcion().getDocument().addDocumentListener(doclist);
	//---------------------------------------------------------------------------------
	 panel.getTFAncho().getDocument().addDocumentListener(doclist);
	//---------------------------------------------------------------------------------
	 panel.getTFCodigo().getDocument().addDocumentListener(doclist);
	//---------------------------------------------------------------------------------
	 panel.getRBLineal().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
        	 panel.getButtonGuardar().setEnabled(true);
         }
     });
	//---------------------------------------------------------------------------------
	 panel.getRBAreal().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
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
	  panel.getTFCodigo().setEditable(false);
	  panel.getTFDescripcion().setEditable(false);
	  panel.getTFAncho().setEditable(false);
	  
	  if(panel.getRBLineal().isSelected())
	   panel.getRBAreal().setVisible(false);
	  else
	   panel.getRBLineal().setVisible(false);
	 }  
	}
 }
