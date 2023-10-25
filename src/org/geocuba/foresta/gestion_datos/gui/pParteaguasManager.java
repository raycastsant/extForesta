package org.geocuba.foresta.gestion_datos.gui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.geocuba.foresta.gestion_datos.Parteaguas;
import org.geocuba.foresta.gestion_datos.PersistentObject;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;

import com.iver.cit.gvsig.exceptions.layers.CancelEditingLayerException;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.exceptions.layers.StartEditionLayerException;
import com.iver.cit.gvsig.exceptions.table.CancelEditingTableException;

 public class pParteaguasManager implements IPanelManager 
 {
	private pParteaguas panel;
	private Parteaguas parteaguas;
	
	public pParteaguasManager()
	{
	 /*panel = new pParteaguas(null, false);
	 panel.setLocationRelativeTo(null);
     panel.setTitle("Cuenca");		  
	 setComponentsListeners();*/
	}
	
	@Override
	public void MostrarPanel(PersistentObject persistent) 
	{
	 panel = new pParteaguas("Parteaguas");
		
	 parteaguas = (Parteaguas) persistent;
	 
	 if(!parteaguas.IsNewObject())
	 {
//	  panel.getLabelGid().setText(parteaguas.getGid().toString());
	  panel.SetTitle(panel.Title()+". Identificador "+parteaguas.getGid());
	  String panelId = "_"+panel.Title()+parteaguas.getGid();
	  Panel p = UIUtils.GetPanel(panelId);
	
	 /*Si se va a modificar y habia una ficha de informacion abierta
	  * la cierro. 
	  * Si se va a mostrar la informacion y ya habia una ficha de informacion
	  * abierta no hago nada*/  
	  if(p == null)       //Panel no existe
	   panel.set_panel_id(panelId);   //Se va a obtener informacion solamente
	  else
	  if(parteaguas.isEditing())  //Si se va a modificar
	  {	  
	   p.Close();
	   panel.set_panel_id(panelId);
	  } 
	  else
	   return;  	  //No muestro la ventana
	  
	  if(parteaguas.getCuenca()!=null)
	   panel.getLabelCuenca().setText(parteaguas.getCuenca().getNombre());
	  
	  if(parteaguas.getDescripcion()!=null)
	   panel.getTFDescripcion().setText(parteaguas.getDescripcion());
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
				if(!parteaguas.IsNewObject())
			     parteaguas.setGeometry(pGestionManager.obtenerGeometriaModificada(), pGestionManager.TipoElementoGeometrico());		
					
				parteaguas.setDescripcion(panel.getTFDescripcion().getText());
				
				parteaguas.save();
				
				panel.Close();
				pGestionManager.ActualizarTabla(pGestionManager.SQL_PARTEAGUAS, "");
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
	 panel.getTFDescripcion().getDocument().addDocumentListener(new DocumentListener() { 
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
	  panel.getTFDescripcion().setEditable(false);
	 }  
	}
 }
