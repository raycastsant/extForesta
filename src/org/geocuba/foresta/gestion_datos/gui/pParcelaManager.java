package org.geocuba.foresta.gestion_datos.gui;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.geocuba.foresta.gestion_datos.Parcela;
import org.geocuba.foresta.gestion_datos.PersistentObject;
import org.geocuba.foresta.gestion_datos.Uso_catastro;
import org.geocuba.foresta.gestion_datos.Uso_catastroManager;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.exceptions.layers.CancelEditingLayerException;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.exceptions.layers.StartEditionLayerException;
import com.iver.cit.gvsig.exceptions.table.CancelEditingTableException;

 public class pParcelaManager implements IPanelManager 
 {
	private pParcela panel;
	private Parcela parcela;
	
	public pParcelaManager() {
	}
	
	@Override
	public void MostrarPanel(PersistentObject persistent) 
	{
	 panel = new pParcela("Parcela");
		
	 parcela = (Parcela) persistent;
	 
	   try {
		    panel.getComboDescUso().removeAllItems();
			Uso_catastro[] usos_catastro;
			usos_catastro = Uso_catastroManager.Obtener_usos_catastro();
			for(int i=0; i<usos_catastro.length; i++)
			 panel.getComboDescUso().addItem(usos_catastro[i]);
			
		   } catch (ReadDriverException e) {
			   e.printStackTrace();
		   }
	 
	 if(!parcela.IsNewObject()) //Se está modificando
	 {
//	  panel.getLabelGid().setText(parcela.getGid().toString());
		 panel.SetTitle(panel.Title()+". Identificador "+parcela.getGid());
		 String panelId = "_"+panel.Title()+parcela.getGid();
		  Panel p = UIUtils.GetPanel(panelId);
		
		 /*Si se va a modificar y habia una ficha de informacion abierta
		  * la cierro. 
		  * Si se va a mostrar la informacion y ya habia una ficha de informacion
		  * abierta no hago nada*/  
		  if(p == null)       //Panel no existe
		   panel.set_panel_id(panelId);   //Se va a obtener informacion solamente
		  else
		  if(parcela.isEditing())  //Si se va a modificar
		  {	  
		   p.Close();
		   panel.set_panel_id(panelId);
		  } 
		  else
		   return;  	  //No muestro la ventana
	  
	  if(parcela.getMunicipio()!=null)
	  {	  
	   panel.getLabelProvincia().setText(parcela.getMunicipio().getProvincia().getNombre());
	   panel.getLabelMunicipio().setText(parcela.getMunicipio().getNombre());
	  } 
	  
//	  if(parcela.getSuelo()!=null)
//	   panel.getLabelSuelo().setText(parcela.getSuelo().getTipo_suelo().getTipo());
	  
	  if(parcela.getNombre()!=null)
	   panel.getTFNombre().setText(parcela.getNombre());
	  
	  if(parcela.getPoseedor()!=null)
	   panel.getTFPoseedor().setText(parcela.getPoseedor());
	  
	  Uso_catastro uso = parcela.getUso_catastro();
	  if(uso!=null)
	  {
	   for(int i=0; i<panel.getComboDescUso().getItemCount(); i++)	
	   {
	    if(panel.getComboDescUso().getItemAt(i).toString().equals(uso.toString()))
	    { 	
	     panel.getComboDescUso().setSelectedIndex(i);
	     break;
	    } 
	   } 
	   actualizar_info_uso(uso);
	  } 
	  
	  if(parcela.getZc()!=null)
	   panel.getTFZc().setText(parcela.getZc().toString());
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
		  String nombre = panel.getTFNombre().getText();	
		  String poseedor = panel.getTFPoseedor().getText();
		  String zc = panel.getTFZc().getText();
		  
		  if(nombre.trim().equals("") || poseedor.trim().equals("") || zc.trim().equals(""))
		  {
		   JOptionPane.showMessageDialog(null, "Faltan datos por llenar", "Error", JOptionPane.ERROR_MESSAGE);
		   return;
		  }	  
			  
			try {
			   /*Si no es un nuevo objeto la geometria se puede estar modificando 
				* hasta el último momento (ModificarGeometriaListener)*/
				if(!parcela.IsNewObject())
			    parcela.setGeometry(pGestionManager.obtenerGeometriaModificada(), pGestionManager.TipoElementoGeometrico());		
					
				parcela.setNombre(panel.getTFNombre().getText());
				parcela.setPoseedores(panel.getTFPoseedor().getText());
				parcela.setUso_catastro((Uso_catastro)panel.getComboDescUso().getSelectedItem());
				parcela.setZc(Integer.parseInt(zc));
				
				parcela.save();
				
				panel.Close();
				pGestionManager.ActualizarTabla(pGestionManager.SQL_PARCELAS, "");
				pGestionManager.terminarEdicion();
				
			    } catch (CancelEditingTableException e) {
				    e.printStackTrace();
			    } catch (CancelEditingLayerException e) {
				    e.printStackTrace();
			    } catch (StartEditionLayerException e) {
				    e.printStackTrace();
			    } catch (ReloadLayerException e) {
					e.printStackTrace();
				}	
        }
     });
	//--------------------------------------------------------------------------------- 
	 panel.getButtonCancelar().addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) 
	    {
		 try {
			pGestionManager.terminarEdicion();
			
		    } catch (CancelEditingTableException e) {
			    e.printStackTrace();
		    } catch (CancelEditingLayerException e) {
			    e.printStackTrace();
		    } catch (StartEditionLayerException e) {
			    e.printStackTrace();
		    } catch (ReloadLayerException e) {
				e.printStackTrace();
			}	
		
		 panel.Close();
	    }
	 });
	//---------------------------------------------------------------------------------
	 panel.getTFNombre().getDocument().addDocumentListener(new DocumentListener() { 
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
	//---------------------------------------------------------------------------------
	 panel.getTFZc().getDocument().addDocumentListener(new DocumentListener() { 
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
	//---------------------------------------------------------------------------------
	 panel.getTFPoseedor().getDocument().addDocumentListener(new DocumentListener() { 
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
	//---------------------------------------------------------------------------------
	 panel.getComboDescUso().addItemListener(new java.awt.event.ItemListener() {
         public void itemStateChanged(java.awt.event.ItemEvent evt) {
           HabilitarGuardado();
           actualizar_info_uso((Uso_catastro)panel.getComboDescUso().getSelectedItem());
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
	  panel.getTFNombre().setEditable(false);
	  panel.getTFPoseedor().setEditable(false);
	  panel.getComboDescUso().setEnabled(false);
	  panel.getTFZc().setEditable(false);
	 }  
	}
	
	private void actualizar_info_uso(Uso_catastro uso)
	{
		if(uso!=null)
		  {	  
		   panel.getTFCodigoUso().setText(uso.getCodigo_uso().toString());
		   panel.getTFAgrupadorUso().setText(uso.getUso_suelo().getTipo_uso());
		   panel.getTFEspUso().setText(uso.getEsp_uso().toString());
		   panel.getTFTipoUso().setText(uso.getTipo_uso().toString());
		   panel.getTFTipoSup().setText(uso.getTipo_superficie().toString());
		  } 
	}
 }
