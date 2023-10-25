package org.geocuba.foresta.gestion_datos.gui;

import org.geocuba.foresta.gestion_datos.Faja_real;
import org.geocuba.foresta.gestion_datos.PersistentObject;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;
import com.iver.cit.gvsig.exceptions.layers.CancelEditingLayerException;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.exceptions.layers.StartEditionLayerException;
import com.iver.cit.gvsig.exceptions.table.CancelEditingTableException;

 public class pFaja_realManager implements IPanelManager 
 {
	private pFaja_real panel;
	private Faja_real fajareal;
	
	public pFaja_realManager() {
	}
	
	@Override
	public void MostrarPanel(PersistentObject persistent) 
	{
	 panel = new pFaja_real("Faja Real");
		
	 fajareal = (Faja_real) persistent;
	 
	 if(!fajareal.IsNewObject()) //Se está modificando
	 {
	 // panel.getLabelGid().setText(fajareal.getGid().toString());
		 panel.SetTitle(panel.Title()+". Identificador "+fajareal.getGid());
		 String panelId = "_"+panel.Title()+fajareal.getGid();
		  Panel p = UIUtils.GetPanel(panelId);
		
		 /*Si se va a modificar y habia una ficha de informacion abierta
		  * la cierro. 
		  * Si se va a mostrar la informacion y ya habia una ficha de informacion
		  * abierta no hago nada*/  
		  if(p == null)       //Panel no existe
		   panel.set_panel_id(panelId);   //Se va a obtener informacion solamente
		  else
		  if(fajareal.isEditing())  //Si se va a modificar
		  {	  
		   p.Close();
		   panel.set_panel_id(panelId);
		  } 
		  else
		   return;  	  //No muestro la ventana
	  
	  if(fajareal.getFaja().getHidrografia().getCuenca()!=null)
	   panel.getLabelCuenca().setText(fajareal.getFaja().getHidrografia().getCuenca().getNombre());
	  
//	  if(fajareal.getFaja().getHidrografia().getTipo_hidrografia().getTipo_elemento().equals("rio"))
	   panel.getLabelOrilla().setText(fajareal.getFaja().getOrilla());
//	  else
//	   panel.getLabelOrilla().setVisible(false);
	  
	   panel.getLabelTipoHidrografia().setText(fajareal.getFaja().getHidrografia().getTipo_hidrografia().getTipo_elemento());
	   panel.getLabelNombreNHidrografia().setText(fajareal.getFaja().getHidrografia().getNombre());
	   
	   if(fajareal.getFaja().getNombre_Lugar() != null)
		panel.getLabelbNombreLugar().setText(fajareal.getFaja().getNombre_Lugar());
	   
	   if(fajareal.getFaja().getAncho() != null)
	    panel.getLabelAncho().setText(fajareal.getFaja().getAncho().toString());
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
				if(!fajareal.IsNewObject())
					fajareal.setGeometry(pGestionManager.obtenerGeometriaModificada(), pGestionManager.TipoElementoGeometrico());		
					
				fajareal.save();
				
				panel.Close();
				pGestionManager.ActualizarTabla(pGestionManager.SQL_FAJAS_REAL, "");
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
	 }  
	}
 }
