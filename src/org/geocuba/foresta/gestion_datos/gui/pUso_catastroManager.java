package org.geocuba.foresta.gestion_datos.gui;

import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.geocuba.foresta.gestion_datos.Agrupador_uso;
import org.geocuba.foresta.gestion_datos.Agrupador_usoManager;
import org.geocuba.foresta.gestion_datos.PersistentObject;
import org.geocuba.foresta.gestion_datos.Uso_catastro;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;

 public class pUso_catastroManager implements IPanelManager 
 {
	private pUso_catastro panel;
	private Uso_catastro uso;
	
	public pUso_catastroManager() {
	}
	
	@Override
	public void MostrarPanel(PersistentObject persistent) 
	{
	 panel = new pUso_catastro("Uso de catastro");
	 
	 uso = (Uso_catastro) persistent;
	 
	 //Lleno el combobox de agrupadores
	  panel.getjCBXAgrupador().removeAllItems();
	  Agrupador_uso []agrupadores = Agrupador_usoManager.obtener_Usos_suelos();
	  for(int i=0; i<agrupadores.length; i++)
	   panel.getjCBXAgrupador().addItem(agrupadores[i]);
	 
	 if(!uso.IsNewObject()) //Se está modificando
	 {
//	  panel.getLabelId().setText(uso.getId().toString());
		 panel.SetTitle(panel.Title()+". Identificador "+uso.getId());
		 String panelId = "_"+panel.Title()+uso.getId();
		  Panel p = UIUtils.GetPanel(panelId);
		
		 /*Si se va a modificar y habia una ficha de informacion abierta
		  * la cierro. 
		  * Si se va a mostrar la informacion y ya habia una ficha de informacion
		  * abierta no hago nada*/  
		  if(p == null)       //Panel no existe
		   panel.set_panel_id(panelId);   //Se va a obtener informacion solamente
		  else
		  if(uso.isEditing())  //Si se va a modificar
		  {	  
		   p.Close();
		   panel.set_panel_id(panelId);
		  } 
		  else
		   return;  	  //No muestro la ventana
	  
	  if(uso.getUso_suelo()!=null)
	  {
	   for(int i=0; i<panel.getjCBXAgrupador().getItemCount(); i++)	
	   {
	    if(panel.getjCBXAgrupador().getItemAt(i).toString().equals(uso.getUso_suelo().toString()))
	    { 	
	     panel.getjCBXAgrupador().setSelectedIndex(i);
	     break;
	    } 
	   } 
	  } 
	  
	     if(uso.getDescripcion_uso()!=null)
		  panel.getjTFDescripcion().setText(uso.getDescripcion_uso().toString());	 
		 
		 if(uso.getCodigo_uso()!=null)
		  panel.getjTFCodigo().setText(uso.getCodigo_uso().toString());
		 
		 if(uso.getTipo_superficie()!=null)
		  panel.getjTFTipoSuperficie().setText(uso.getTipo_superficie().toString());
		 
		 if(uso.getTipo_uso()!=null)
		  panel.getjTFTipoUso().setText(uso.getTipo_uso().toString());
		 
		 if(uso.getEsp_uso()!=null)
		  panel.getjTFEspecificacion().setText(uso.getEsp_uso().toString());
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
		 uso.setUso_suelo((Agrupador_uso)panel.getjCBXAgrupador().getSelectedItem());
		 uso.setDescripcion_uso(panel.getjTFDescripcion().getText());
		 
         String codigo = panel.getjTFCodigo().getText();
         if(codigo.equals(""))
          codigo = "0";	 
		 uso.setCodigo_uso(Integer.parseInt(codigo));
		 
		 String tipoSup = panel.getjTFTipoSuperficie().getText();
         if(tipoSup.equals(""))
          tipoSup = "0";	 
		 uso.setTipo_superficie(Integer.parseInt(tipoSup));
		 
		 String tipoUso = panel.getjTFTipoUso().getText();
         if(tipoUso.equals(""))
          tipoUso = "0";
		 uso.setTipo_uso(Integer.parseInt(tipoUso));
		 
		 String Esp = panel.getjTFEspecificacion().getText();
         if(Esp.equals(""))
          Esp = "0";
		 uso.setEsp_uso(Integer.parseInt(Esp));
		 
		 uso.save();
		 
		 panel.Close();
				
		 pGestionManager.ActualizarTabla(pGestionManager.SQL_USOS_CATASTRO, "");
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
	 panel.getjTFCodigo().getDocument().addDocumentListener(doclist); 
	//---------------------------------------------------------------------------------
	 panel.getjTFDescripcion().getDocument().addDocumentListener(doclist); 
	//---------------------------------------------------------------------------------
	 panel.getjTFEspecificacion().getDocument().addDocumentListener(doclist); 
	//---------------------------------------------------------------------------------
	 panel.getjTFTipoSuperficie().getDocument().addDocumentListener(doclist); 
	//---------------------------------------------------------------------------------
	 panel.getjTFTipoUso().getDocument().addDocumentListener(doclist); 
	//---------------------------------------------------------------------------------
	 panel.getjCBXAgrupador().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
          panel.getButtonGuardar().setEnabled(true);
         }
     });
	//---------------------------------------------------------------------------------
	 panel.getjTFCodigo().addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyTyped(java.awt.event.KeyEvent evt) {
        	 char caracter = evt.getKeyChar();
             if(!Funciones_Utiles.formatoInputTextField(panel.getjTFCodigo().getText(), caracter, 'I', 100))
              evt.consume();
         }
     });
	//---------------------------------------------------------------------------------
	 panel.getjTFEspecificacion().addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyTyped(java.awt.event.KeyEvent evt) {
        	 char caracter = evt.getKeyChar();
             if(!Funciones_Utiles.formatoInputTextField(panel.getjTFEspecificacion().getText(), caracter, 'I', 100))
              evt.consume();
         }
     });
	//---------------------------------------------------------------------------------
	 panel.getjTFTipoSuperficie().addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyTyped(java.awt.event.KeyEvent evt) {
        	 char caracter = evt.getKeyChar();
             if(!Funciones_Utiles.formatoInputTextField(panel.getjTFTipoSuperficie().getText(), caracter, 'I', 100))
              evt.consume();
         }
     });
	//---------------------------------------------------------------------------------
	 panel.getjTFTipoUso().addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyTyped(java.awt.event.KeyEvent evt) {
        	 char caracter = evt.getKeyChar();
             if(!Funciones_Utiles.formatoInputTextField(panel.getjTFTipoUso().getText(), caracter, 'I', 100))
              evt.consume();
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
	  panel.getjTFCodigo().setEditable(false);
	  panel.getjTFDescripcion().setEditable(false);
	  panel.getjTFEspecificacion().setEditable(false);
	  panel.getjTFTipoSuperficie().setEditable(false);
	  panel.getjTFTipoUso().setEditable(false);
	  panel.getjCBXAgrupador().setEnabled(false);
	  UIManager.getDefaults().put("ComboBox.disabledForeground", Color.BLACK);
	 }  
	}
 }
