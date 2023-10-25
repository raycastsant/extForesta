package org.geocuba.foresta.gestion_datos.gui;

import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.geocuba.foresta.gestion_datos.PersistentObject;
import org.geocuba.foresta.gestion_datos.Rio;
import org.geocuba.foresta.gestion_datos.Tipo_hidrografia;
import org.geocuba.foresta.gestion_datos.Tipo_hidrografiaManager;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;

import com.iver.cit.gvsig.exceptions.layers.CancelEditingLayerException;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.exceptions.layers.StartEditionLayerException;
import com.iver.cit.gvsig.exceptions.table.CancelEditingTableException;

 public class pRioManager implements IPanelManager 
 {
	private pRio panel;
	private Rio rio;
	
	public pRioManager() {
	}
	
	@Override
	public void MostrarPanel(PersistentObject persistent) 
	{
	 panel = new pRio("Hidrografía lineal");
		
	 rio = (Rio) persistent;
	 
	  panel.getComboTipo().removeAllItems();
	  Tipo_hidrografiaManager tipoHMan = new Tipo_hidrografiaManager();
	  Tipo_hidrografia []tipohidro = tipoHMan.get_Tipos_hidrografia("where tipo_elemento='rio'");
	  for(int i=0; i<tipohidro.length; i++)
	   panel.getComboTipo().addItem(tipohidro[i]);
	  
	 if(!rio.IsNewObject()) //Se está modificando
	 {
	  //panel.getLabelGid().setText(rio.getGid().toString());
		 panel.SetTitle(panel.Title()+". Identificador "+rio.getGid());
		 String panelId = "_"+panel.Title()+rio.getGid();
		  Panel p = UIUtils.GetPanel(panelId);
		
		 /*Si se va a modificar y habia una ficha de informacion abierta
		  * la cierro. 
		  * Si se va a mostrar la informacion y ya habia una ficha de informacion
		  * abierta no hago nada*/  
		  if(p == null)       //Panel no existe
		   panel.set_panel_id(panelId);   //Se va a obtener informacion solamente
		  else
		  if(rio.isEditing())  //Si se va a modificar
		  {	  
		   p.Close();
		   panel.set_panel_id(panelId);
		  } 
		  else
		   return;  	  //No muestro la ventana
	  
	  if(rio.getCuenca()!=null)
	   panel.getLabelCuenca().setText(rio.getCuenca().getNombre());
	  
	  if(rio.getTipo_hidrografia()!=null)
	  {
	   for(int i=0; i<panel.getComboTipo().getItemCount(); i++)	
	   {
	    if(panel.getComboTipo().getItemAt(i).toString().equals(rio.getTipo_hidrografia().toString()))
	    { 	
	     panel.getComboTipo().setSelectedIndex(i);
	     break;
	    } 
	   } 
	  } 
	  
	  if(rio.getNombre()!=null)
	   panel.getTFNombre().setText(rio.getNombre());
	  
	  if(rio.getAncho()!=null)
	   panel.getTFAncho().setText(rio.getAncho().toString());
	  
	  if(rio.getOrden()!=null)
	   panel.getTForden().setText(rio.getOrden().toString());
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
				if(!rio.IsNewObject())
					rio.setGeometry(pGestionManager.obtenerGeometriaModificada(), pGestionManager.TipoElementoGeometrico());		
				
				rio.setTipo_hidrografia((Tipo_hidrografia)panel.getComboTipo().getSelectedItem());
				rio.setNombre(panel.getTFNombre().getText());
				
				String ancho = panel.getTFAncho().getText();
				if(ancho.equals(""))
			     ancho = "0";		
				rio.setAncho(Double.parseDouble(ancho));
				
				String orden = panel.getTForden().getText();
				if(orden.equals(""))
				 orden = "0";
				rio.setOrden(Integer.parseInt(orden));
				
				rio.save();
				
				panel.Close();
				pGestionManager.ActualizarTabla(pGestionManager.SQL_RIOS, "");
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
	 panel.getTFAncho().getDocument().addDocumentListener(new DocumentListener() { 
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
	 panel.getTForden().getDocument().addDocumentListener(new DocumentListener() { 
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
	  panel.getTFAncho().setEditable(false);
	  panel.getTForden().setEditable(false);
	  UIManager.getDefaults().put("ComboBox.disabledForeground", Color.BLACK);
	 }  
	}
 }
