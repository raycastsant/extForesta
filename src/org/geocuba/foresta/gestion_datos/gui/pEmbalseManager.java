package org.geocuba.foresta.gestion_datos.gui;

import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.geocuba.foresta.gestion_datos.Embalse;
import org.geocuba.foresta.gestion_datos.PersistentObject;
import org.geocuba.foresta.gestion_datos.Tipo_hidrografia;
import org.geocuba.foresta.gestion_datos.Tipo_hidrografiaManager;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;

import com.iver.cit.gvsig.exceptions.layers.CancelEditingLayerException;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.exceptions.layers.StartEditionLayerException;
import com.iver.cit.gvsig.exceptions.table.CancelEditingTableException;

 public class pEmbalseManager implements IPanelManager 
 {
	private pEmbalse panel;
	private Embalse embalse;
	
	public pEmbalseManager() {
	}
	
	@Override
	public void MostrarPanel(PersistentObject persistent) 
	{
//	 panel = (pEmbalse)UIUtils.GetPanel("_Embalse");	
//	 if(panel != null)
//	  panel.Close();
	 
	 panel = new pEmbalse("Embalse");
		
	 embalse = (Embalse) persistent;
	 
	  panel.getComboTipo().removeAllItems();
	  Tipo_hidrografiaManager tipoHMan = new Tipo_hidrografiaManager();
	  Tipo_hidrografia []tipohidro = tipoHMan.get_Tipos_hidrografia("where tipo_elemento='embalse'");
	  for(int i=0; i<tipohidro.length; i++)
	   panel.getComboTipo().addItem(tipohidro[i]);
	  
	 if(!embalse.IsNewObject()) //Se está modificando
	 {
	  //panel.getLabelGid().setText(embalse.getGid().toString());
		  panel.SetTitle(panel.Title()+". Identificador "+embalse.getGid());
		  String panelId = "_"+panel.Title()+embalse.getGid();
		  Panel p = UIUtils.GetPanel(panelId);
		
		 /*Si se va a modificar y habia una ficha de informacion abierta
		  * la cierro. 
		  * Si se va a mostrar la informacion y ya habia una ficha de informacion
		  * abierta no hago nada*/  
		  if(p == null)       //Panel no existe
		   panel.set_panel_id(panelId);   //Se va a obtener informacion solamente
		  else
		  if(embalse.isEditing())  //Si se va a modificar
		  {	  
		   p.Close();
		   panel.set_panel_id(panelId);
		  } 
		  else
		   return;  	  //No muestro la ventana
		  
	  if(embalse.getCuenca()!=null)
	   panel.getLabelCuenca().setText(embalse.getCuenca().getNombre());
	  
	  if(embalse.getTipo_hidrografia()!=null)
	  {
	   for(int i=0; i<panel.getComboTipo().getItemCount(); i++)	
	   {
	    if(panel.getComboTipo().getItemAt(i).toString().equals(embalse.getTipo_hidrografia().toString()))
	    { 	
	     panel.getComboTipo().setSelectedIndex(i);
	     break;
	    } 
	   } 
	  } 
	  
	  if(embalse.getNombre()!=null)
	   panel.getTFNombre().setText(embalse.getNombre());
	  
	  if(embalse.getNaturaleza()!=null)
	   panel.getTFNaturaleza().setText(embalse.getNaturaleza());
	  
	  if(embalse.getUso()!=null)
	   panel.getTFUso().setText(embalse.getUso());
	  
	  if(embalse.getVolumen()!=null)
	   panel.getTFVolumen().setText(embalse.getVolumen().toString());
	  
	  if(embalse.getNan()!=null)
	   panel.getTFNan().setText(embalse.getNan().toString());
	  
	  if(embalse.getNam()!=null)
	   panel.getTFNam().setText(embalse.getNam().toString());
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
			try {
			   /*Si no es un nuevo objeto la geometria se puede estar modificando 
				* hasta el último momento (ModificarGeometriaListener)*/
				if(!embalse.IsNewObject())
				 embalse.setGeometry(pGestionManager.obtenerGeometriaModificada(), pGestionManager.TipoElementoGeometrico());		
				
				embalse.setTipo_hidrografia((Tipo_hidrografia)panel.getComboTipo().getSelectedItem());
				embalse.setNombre(panel.getTFNombre().getText());
				embalse.setNaturaleza(panel.getTFNaturaleza().getText());
				embalse.setUso(panel.getTFUso().getText());
				
				String volumen = panel.getTFVolumen().getText();
				if(volumen.equals(""))
			     volumen = "0";		
				embalse.setVolumen(Double.parseDouble(volumen));
				
				String nan = panel.getTFNan().getText();
				if(nan.equals(""))
				 nan = "0";		
				embalse.setNan(Double.parseDouble(nan));
				
				String nam = panel.getTFNam().getText();
				if(nam.equals(""))
				 nam = "0";	
				embalse.setNam(Double.parseDouble(nam));
				
				embalse.save();
				
				panel.Close();
				pGestionManager.ActualizarTabla(pGestionManager.SQL_EMBALSES, "");
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
	 DocumentListener dc = new DocumentListener() { 
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
	 panel.getTFNombre().getDocument().addDocumentListener(dc); 
	//--------------------------------------------------------------------------------- 
	 panel.getTFNaturaleza().getDocument().addDocumentListener(dc); 
	//--------------------------------------------------------------------------------- 
	 panel.getTFUso().getDocument().addDocumentListener(dc);
	//--------------------------------------------------------------------------------- 
	 panel.getTFVolumen().getDocument().addDocumentListener(dc);
	//--------------------------------------------------------------------------------- 
	 panel.getTFNan().getDocument().addDocumentListener(dc);
	//--------------------------------------------------------------------------------- 
	 panel.getTFNam().getDocument().addDocumentListener(dc);
	//---------------------------------------------------------------------------------
	 panel.getComboTipo().addActionListener(new java.awt.event.ActionListener() {
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
	  panel.getTFNombre().setEditable(false);
	  panel.getComboTipo().setEnabled(false);
	  panel.getTFNaturaleza().setEditable(false);
	  panel.getTFUso().setEditable(false);
	  panel.getTFVolumen().setEditable(false);
	  panel.getTFNan().setEditable(false);
	  panel.getTFNam().setEditable(false);
	  UIManager.getDefaults().put("ComboBox.disabledForeground", Color.BLACK);
	 }  
	}
 }
