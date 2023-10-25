package org.geocuba.foresta.gestion_datos.gui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.geocuba.foresta.gestion_datos.Faja;
import org.geocuba.foresta.gestion_datos.PersistentObject;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;

import com.iver.cit.gvsig.exceptions.layers.CancelEditingLayerException;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.exceptions.layers.StartEditionLayerException;
import com.iver.cit.gvsig.exceptions.table.CancelEditingTableException;

 public class pFajaManager implements IPanelManager 
 {
	private pFaja panel;
	private Faja faja;
	
	public pFajaManager() {
	}
	
	@Override
	public void MostrarPanel(PersistentObject persistent) 
	{
	 panel = new pFaja("Faja");
		
	 faja = (Faja) persistent;
	 
	 if(!faja.IsNewObject()) //Se está modificando
	 {
	  // panel.getLabelGid().setText(faja.getGid().toString());
		 panel.SetTitle(panel.Title()+". Identificador "+faja.getGid());
		 String panelId = "_"+panel.Title()+faja.getGid();
		  Panel p = UIUtils.GetPanel(panelId);
		
		 /*Si se va a modificar y habia una ficha de informacion abierta
		  * la cierro. 
		  * Si se va a mostrar la informacion y ya habia una ficha de informacion
		  * abierta no hago nada*/  
		  if(p == null)       //Panel no existe
		   panel.set_panel_id(panelId);   //Se va a obtener informacion solamente
		  else
		  if(faja.isEditing())  //Si se va a modificar
		  {	  
		   p.Close();
		   panel.set_panel_id(panelId);
		  } 
		  else
		   return;  	  //No muestro la ventana
	  
	  if(faja.getHidrografia().getCuenca()!=null)
	   panel.getLabelCuenca().setText(faja.getHidrografia().getCuenca().getNombre());
	  
	 // if(faja.getHidrografia().getTipo_hidrografia().getTipo_elemento().equals("rio"))
	   panel.getLabelOrilla().setText(faja.getOrilla());
//	  else
//	   panel.getLabelOrilla().setVisible(false);
	  
	   panel.getLabelTipoHidrografia().setText(faja.getHidrografia().getTipo_hidrografia().getTipo_elemento());
	   panel.getLabelNombreNHidrografia().setText(faja.getHidrografia().getNombre());
	   
	   if(faja.getNombre_Lugar() != null)
		panel.getTFNombreLugar().setText(faja.getNombre_Lugar());
	   
	   if(faja.getAncho() != null)
	    panel.getTFAncho().setText(faja.getAncho().toString());
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
				if(!faja.IsNewObject())
			     faja.setGeometry(pGestionManager.obtenerGeometriaModificada(), pGestionManager.TipoElementoGeometrico());		
					
				faja.setNombre_Lugar(panel.getTFNombreLugar().getText());
				faja.setAncho(Double.parseDouble(panel.getTFAncho().getText()));
				
				faja.save();
				
				panel.Close();
				pGestionManager.ActualizarTabla(pGestionManager.SQL_FAJAS, "");
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
	 if(!faja.IsNewObject())
	 {	 
		 panel.getButtonProyectos().addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) 
		    {
			  pGestionProyectosManager.mostrarDialogo(faja.getGid());
		    }
		 });
	 }
	 else
	  panel.getButtonProyectos().setEnabled(false); 	 
	//---------------------------------------------------------------------------------
	 panel.getTFNombreLugar().getDocument().addDocumentListener(new DocumentListener() { 
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
	  panel.getTFNombreLugar().setEditable(false);
	  panel.getTFAncho().setEditable(false);
	 }  
	}
 }
