package org.geocuba.foresta.gestion_datos.gui;

import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.geocuba.foresta.gestion_datos.Agrupador_uso;
import org.geocuba.foresta.gestion_datos.Agrupador_usoManager;
import org.geocuba.foresta.gestion_datos.PersistentObject;
import org.geocuba.foresta.gestion_datos.Velocidad_infiltracion;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;

 public class pVelocidad_infiltracionManager implements IPanelManager 
 {
	private pVelocidad_infiltracion panel;
	private Velocidad_infiltracion velocidad;
	
	public pVelocidad_infiltracionManager() {
	}
	
	@Override
	public void MostrarPanel(PersistentObject persistent) 
	{
	 panel = new pVelocidad_infiltracion("Velocidad de infiltración");
	 
	 velocidad = (Velocidad_infiltracion) persistent;
	 
	 //Lleno el combobox de agrupadores
	  panel.getjCBXAgrupador().removeAllItems();
	  Agrupador_uso []agrupadores = Agrupador_usoManager.obtener_Usos_suelos();
	  for(int i=0; i<agrupadores.length; i++)
	   panel.getjCBXAgrupador().addItem(agrupadores[i]);
	 
	 if(!velocidad.IsNewObject()) //Se está modificando
	 {
//	  panel.getjLId().setText(velocidad.getID().toString());
		 panel.SetTitle(panel.Title()+". Identificador "+velocidad.getID());
		 String panelId = "_"+panel.Title()+velocidad.getID();
		  Panel p = UIUtils.GetPanel(panelId);
		
		 /*Si se va a modificar y habia una ficha de informacion abierta
		  * la cierro. 
		  * Si se va a mostrar la informacion y ya habia una ficha de informacion
		  * abierta no hago nada*/  
		  if(p == null)       //Panel no existe
		   panel.set_panel_id(panelId);   //Se va a obtener informacion solamente
		  else
		  if(velocidad.isEditing())  //Si se va a modificar
		  {	  
		   p.Close();
		   panel.set_panel_id(panelId);
		  } 
		  else
		   return;  	  //No muestro la ventana
	  
	  if(velocidad.getUso_suelo()!=null)
	  {
	   for(int i=0; i<panel.getjCBXAgrupador().getItemCount(); i++)	
	   {
	    if(panel.getjCBXAgrupador().getItemAt(i).toString().equals(velocidad.getUso_suelo().toString()))
	    { 	
	     panel.getjCBXAgrupador().setSelectedIndex(i);
	     break;
	    } 
	   } 
	  } 
	  
	  if(velocidad.getVelocidad()!=null)
		  panel.getjTFVelocidad().setText(velocidad.getVelocidad().toString());
		 
		 if(velocidad.getClase()!=null)
		  panel.getjTFClase().setText(velocidad.getClase().toString());	
		 
		 if(velocidad.getEtructura()!=null)
		  panel.getjTFEstructura().setText(velocidad.getEtructura().toString());	
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
		 velocidad.setUso_suelo((Agrupador_uso)panel.getjCBXAgrupador().getSelectedItem());
		 
		 String vel = panel.getjTFVelocidad().getText();
		 if(vel.equals(""))
		  vel = "0";	 
		 velocidad.setVelocidad(Double.parseDouble(vel));
		 
		 velocidad.setClase(panel.getjTFClase().getText());
		 velocidad.setEstructura(panel.getjTFEstructura().getText());
		 
		 velocidad.save();
		 
		 panel.Close();
				
		 pGestionManager.ActualizarTabla(pGestionManager.SQL_VELOCIDADES_INFILTRACION, "");
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
	 panel.getjTFClase().getDocument().addDocumentListener(doclist); 
	//---------------------------------------------------------------------------------
	 panel.getjTFEstructura().getDocument().addDocumentListener(doclist); 
	//---------------------------------------------------------------------------------
	 panel.getjTFVelocidad().getDocument().addDocumentListener(doclist); 
	//---------------------------------------------------------------------------------
	 panel.getjCBXAgrupador().addActionListener(new java.awt.event.ActionListener() {
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
	  panel.getjTFClase().setEditable(false);
	  panel.getjTFEstructura().setEditable(false);
	  panel.getjTFVelocidad().setEditable(false);
	  panel.getjCBXAgrupador().setEnabled(false);
	  UIManager.getDefaults().put("ComboBox.disabledForeground", Color.BLACK);
	 }  
	}
 }
