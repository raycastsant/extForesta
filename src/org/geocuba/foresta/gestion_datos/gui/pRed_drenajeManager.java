package org.geocuba.foresta.gestion_datos.gui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.geocuba.foresta.gestion_datos.PersistentObject;
import org.geocuba.foresta.gestion_datos.Red_drenaje;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;
import com.iver.cit.gvsig.exceptions.layers.CancelEditingLayerException;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.exceptions.layers.StartEditionLayerException;
import com.iver.cit.gvsig.exceptions.table.CancelEditingTableException;

 public class pRed_drenajeManager implements IPanelManager 
 {
	private pRed_drenaje panel;
	private Red_drenaje reddrenaje;
	
	public pRed_drenajeManager(){
	}
	
	@Override
	public void MostrarPanel(PersistentObject persistent) 
	{
	 panel = new pRed_drenaje("Red de drenaje");
		
	 reddrenaje = (Red_drenaje) persistent;
	 
	 if(!reddrenaje.IsNewObject())
	 {
		 panel.SetTitle(panel.Title()+". Identificador "+reddrenaje.getGid());
		 String panelId = "_"+panel.Title()+reddrenaje.getGid();
		  Panel p = UIUtils.GetPanel(panelId);
		
		 /*Si se va a modificar y habia una ficha de informacion abierta
		  * la cierro. 
		  * Si se va a mostrar la informacion y ya habia una ficha de informacion
		  * abierta no hago nada*/  
		  if(p == null)       //Panel no existe
		   panel.set_panel_id(panelId);   //Se va a obtener informacion solamente
		  else
		  if(reddrenaje.isEditing())  //Si se va a modificar
		  {	  
		   p.Close();
		   panel.set_panel_id(panelId);
		  } 
		  else
		   return;  	  //No muestro la ventana
	  
	  if(reddrenaje.getParteaguasg()!=null)
	   panel.getLabelCuenca().setText(reddrenaje.getParteaguasg().getDescripcion());
	  
	  if(reddrenaje.getOrden()!=null)
	   panel.getTForden().setText(reddrenaje.getOrden().toString());
	  
	  if(reddrenaje.getSiguiente()!=null)
	   panel.getTFSiguiente().setText(reddrenaje.getSiguiente().toString());
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
			   /*Si no es un nuevo objeto la geometria se puedo estar modificando 
				* hasta el último momento (ModificarGeometriaListener)*/
				if(!reddrenaje.IsNewObject())
				 reddrenaje.setGeometry(pGestionManager.obtenerGeometriaModificada(), pGestionManager.TipoElementoGeometrico());		
					
				String orden = panel.getTForden().getText();
				if(orden.equals(""))
					orden = "0";
				reddrenaje.setOrden(Integer.parseInt(orden));
				
				String sigte = panel.getTFSiguiente().getText();
				if(sigte.equals(""))
					sigte = "0";
				reddrenaje.setSiguiente(Integer.parseInt(sigte));
				
				reddrenaje.save();
				
				panel.Close();
				pGestionManager.ActualizarTabla(pGestionManager.SQL_RED_DRENAJE, "");
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
	 panel.getTForden().getDocument().addDocumentListener(dc); 
   //---------------------------------------------------------------------------------
	 panel.getTFSiguiente().getDocument().addDocumentListener(dc);
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
	  panel.getTForden().setEditable(false);
	  panel.getTFSiguiente().setEditable(false);
	 }  
	}
 }
