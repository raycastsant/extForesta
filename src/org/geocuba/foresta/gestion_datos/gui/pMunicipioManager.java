package org.geocuba.foresta.gestion_datos.gui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.geocuba.foresta.gestion_datos.Municipio;
import org.geocuba.foresta.gestion_datos.PersistentObject;
import org.geocuba.foresta.gestion_datos.Provincia;
import org.geocuba.foresta.gestion_datos.ProvinciaManager;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;
import com.iver.cit.gvsig.exceptions.layers.CancelEditingLayerException;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.exceptions.layers.StartEditionLayerException;
import com.iver.cit.gvsig.exceptions.table.CancelEditingTableException;

 public class pMunicipioManager implements IPanelManager 
 {
	private pMunicipio panel;
	private Municipio municipio;
	
	public pMunicipioManager() {
	}
	
	@Override
	public void MostrarPanel(PersistentObject persistent) 
	{
	 panel = new pMunicipio("Municipio");
		
	 municipio = (Municipio) persistent;
	 
	  panel.getComboProvincia().removeAllItems();
	  ProvinciaManager ProvMan = new ProvinciaManager();
	  Provincia []provincias = ProvMan.listarProvincias();
	  
	  for(int i=0; i<provincias.length; i++)
	   panel.getComboProvincia().addItem(provincias[i]);
	 
	 if(!municipio.IsNewObject()) //Se está modificando
	 {
//	  panel.getLabelGid().setText(municipio.getGid().toString());
		 panel.SetTitle(panel.Title()+". Identificador "+municipio.getGid());
		 String panelId = "_"+panel.Title()+municipio.getGid();
		  Panel p = UIUtils.GetPanel(panelId);
		
		 /*Si se va a modificar y habia una ficha de informacion abierta
		  * la cierro. 
		  * Si se va a mostrar la informacion y ya habia una ficha de informacion
		  * abierta no hago nada*/  
		  if(p == null)       //Panel no existe
		   panel.set_panel_id(panelId);   //Se va a obtener informacion solamente
		  else
		  if(municipio.isEditing())  //Si se va a modificar
		  {	  
		   p.Close();
		   panel.set_panel_id(panelId);
		  } 
		  else
		   return;  	  //No muestro la ventana
	  
	  if(municipio.getNombre()!=null)
	   panel.getTFNombre().setText(municipio.getNombre());
	  
	  if(municipio.getProvincia()!=null)
	  {
		   for(int i=0; i<panel.getComboProvincia().getItemCount(); i++)	
		   {
		    if(panel.getComboProvincia().getItemAt(i).toString().equals(municipio.getProvincia().getNombre()))
		    { 	
		     panel.getComboProvincia().setSelectedIndex(i);
		     break;
		    } 
		   }	  
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
			try {
			   /*Si no es un nuevo objeto la geometria se puede estar modificando 
				* hasta el último momento (ModificarGeometriaListener)*/
				if(!municipio.IsNewObject())
				 municipio.setGeometry(pGestionManager.obtenerGeometriaModificada(), pGestionManager.TipoElementoGeometrico());		
					
				municipio.setNombre(panel.getTFNombre().getText());
				municipio.setProvincia((Provincia)panel.getComboProvincia().getSelectedItem());
				
				municipio.save();
				
				panel.Close();
				pGestionManager.ActualizarTabla(pGestionManager.SQL_MUNICIPIOS, "");
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
	  panel.getComboProvincia().setEnabled(false);
	  panel.getTFNombre().setEditable(false);
	 }  
	}
 }
