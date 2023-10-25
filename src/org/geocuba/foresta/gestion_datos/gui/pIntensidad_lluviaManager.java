package org.geocuba.foresta.gestion_datos.gui;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.geocuba.foresta.gestion_datos.Intensidad_lluvia;
import org.geocuba.foresta.gestion_datos.PersistentObject;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;

 public class pIntensidad_lluviaManager implements IPanelManager 
 {
	private pIntensidad_lluvia panel;
	private Intensidad_lluvia intensidad;
	
	public pIntensidad_lluviaManager() {
	}
	
	@Override
	public void MostrarPanel(PersistentObject persistent) 
	{
	 panel = new pIntensidad_lluvia("Valores de precipitación");
	 
	 intensidad = (Intensidad_lluvia) persistent;
	 
	 if(!intensidad.IsNewObject()) //Se está modificando
	 {
//	  panel.getLabelId().setText(intensidad.getID().toString());
		 panel.SetTitle(panel.Title()+". Identificador "+intensidad.getID());
		 String panelId = "_"+panel.Title()+intensidad.getID();
		  Panel p = UIUtils.GetPanel(panelId);
		
		 /*Si se va a modificar y habia una ficha de informacion abierta
		  * la cierro. 
		  * Si se va a mostrar la informacion y ya habia una ficha de informacion
		  * abierta no hago nada*/  
		  if(p == null)       //Panel no existe
		   panel.set_panel_id(panelId);   //Se va a obtener informacion solamente
		  else
		  if(intensidad.isEditing())  //Si se va a modificar
		  {	  
		   p.Close();
		   panel.set_panel_id(panelId);
		  } 
		  else
		   return;  	  //No muestro la ventana
	  
	  if(intensidad.getTiempo_Duracion()!=null)
	   panel.getTFTiempo().setText(intensidad.getTiempo_Duracion().toString());
	  
	  if(intensidad.getPrecipitacion()!=null)
	   panel.getTFPrecipitacion().setText(intensidad.getPrecipitacion().toString());
	  
	  if(intensidad.getProbabilidad()!=null)
	   panel.getTFProbabilidad().setText(intensidad.getProbabilidad().toString());
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
		 if(panel.getTFTiempo().getText().trim().length()<=0 ||
		    panel.getTFPrecipitacion().getText().trim().length()<=0 ||
		    panel.getTFProbabilidad().getText().trim().length()<=0)	
		 {
		  JOptionPane.showMessageDialog(null, "Faltan datos por llenar", "Error", JOptionPane.WARNING_MESSAGE);
		  return;
		 }	 
			 
		 intensidad.setTiempo_Duracion(Double.parseDouble(panel.getTFTiempo().getText()));
		 intensidad.setPrecipitacion(Double.parseDouble(panel.getTFPrecipitacion().getText()));
		 intensidad.setProbabilidad(Double.parseDouble(panel.getTFProbabilidad().getText().toString()));
		 
		 intensidad.save();
		 panel.Close();
				
		 pGestionManager.ActualizarTabla(pGestionManager.SQL_INTENSIDAD_LLUVIA, "");
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
	 panel.getTFTiempo().getDocument().addDocumentListener(doclist); 
	//---------------------------------------------------------------------------------
	 panel.getTFPrecipitacion().getDocument().addDocumentListener(doclist);
	//---------------------------------------------------------------------------------
	 panel.getTFProbabilidad().getDocument().addDocumentListener(doclist);
	/*
	//---------------------------------------------------------------------------------
	 panel.getTFTiempo().addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyTyped(java.awt.event.KeyEvent evt) {
        	 char caracter = evt.getKeyChar();
             if(!Funciones_Utiles.formatoInputTextField(panel.getTFTiempo().getText(), caracter, 'D', 100))
              evt.consume();
         }
     });
	//---------------------------------------------------------------------------------
	 panel.getSpinnerProbabilidad().addChangeListener(new javax.swing.event.ChangeListener() {
         public void stateChanged(javax.swing.event.ChangeEvent evt) {
        	 panel.getButtonGuardar().setEnabled(true);
        	 
        	 double value = Double.parseDouble(panel.getSpinnerProbabilidad().getValue().toString());
        	 if(value>100)
        	  panel.getSpinnerProbabilidad().setValue("100");
        	 else
        	 if(value<0)
              panel.getSpinnerProbabilidad().setValue("0");
         }
     });*/
	//---------------------------------------------------------------------------------
//	 final JTextField tf = ((JTextField)panel.getSpinnerProbabilidad().getEditor().getComponent(0));
//	 tf.addKeyListener(new java.awt.event.KeyAdapter() {
//         public void keyTyped(java.awt.event.KeyEvent evt) {
//        	 char caracter = evt.getKeyChar();
//             if(!Funciones_Utiles.formatoInputTextField(tf.getText(), caracter, 'D'))
//              evt.consume();
//             else
//             {
//              double value = Double.parseDouble(tf.getText()+caracter);	 
//              if(value>100 || value<0)
//               evt.consume();
//             }	 
//         }
//     });
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
	  panel.getTFPrecipitacion().setEditable(false);
	  panel.getTFTiempo().setEditable(false);
	  panel.getTFProbabilidad().setEditable(false);
	 }  
	}
 }
