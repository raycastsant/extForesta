package org.geocuba.foresta.gestion_datos.gui;

import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.geocuba.foresta.gestion_datos.PersistentObject;
import org.geocuba.foresta.gestion_datos.Suelo;
import org.geocuba.foresta.gestion_datos.Textura_suelos;
import org.geocuba.foresta.gestion_datos.Textura_suelosManager;
import org.geocuba.foresta.gestion_datos.Tipo_suelo;
import org.geocuba.foresta.gestion_datos.Tipo_sueloManager;
import org.geocuba.foresta.gestion_datos.Velocidad_infiltracionManager;
import org.geocuba.foresta.herramientas.utiles.gui.Panel;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;
import com.iver.cit.gvsig.exceptions.layers.CancelEditingLayerException;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.exceptions.layers.StartEditionLayerException;
import com.iver.cit.gvsig.exceptions.table.CancelEditingTableException;

 public class pSueloManager implements IPanelManager 
 {
	private pSuelo panel;
	private Suelo suelo;
	
	public pSueloManager() {
	}
	
	@Override
	public void MostrarPanel(PersistentObject persistent) 
	{
	 panel = new pSuelo("Suelo");
		
	 suelo = (Suelo) persistent;
	 
	 panel.getComboTipo().removeAllItems();
	 Tipo_suelo []tiposuelos = Tipo_sueloManager.get_Tipos_suelos();
	 for(int i=0; i<tiposuelos.length; i++)
	  panel.getComboTipo().addItem(tiposuelos[i]);
	 
	 panel.getComboTextura().removeAllItems();
	 Textura_suelos []texturas = Textura_suelosManager.get_Texturas_suelos();
	 for(int i=0; i<texturas.length; i++)
	  panel.getComboTextura().addItem(texturas[i]);
	 
//	 panel.getComboUso().removeAllItems();
//	 Uso_suelo []usoSuelos = Uso_sueloManager.obtener_Usos_suelos();
//	 for(int i=0; i<usoSuelos.length; i++)
//	  panel.getComboUso().addItem(usoSuelos[i]);
	 
	 panel.getComboEstructura().removeAllItems();
	 String []estructuras = Velocidad_infiltracionManager.Obtener_Estructuras();
	 for(int i=0; i<estructuras.length; i++)
	  panel.getComboEstructura().addItem(estructuras[i]);
	  
	 if(!suelo.IsNewObject()) //Se está modificando
	 {
	  // panel.getLabelGid().setText(suelo.getGid().toString());
		 panel.SetTitle(panel.Title()+". Identificador "+suelo.getGid());
		 String panelId = "_"+panel.Title()+suelo.getGid();
		  Panel p = UIUtils.GetPanel(panelId);
		
		 /*Si se va a modificar y habia una ficha de informacion abierta
		  * la cierro. 
		  * Si se va a mostrar la informacion y ya habia una ficha de informacion
		  * abierta no hago nada*/  
		  if(p == null)       //Panel no existe
		   panel.set_panel_id(panelId);   //Se va a obtener informacion solamente
		  else
		  if(suelo.isEditing())  //Si se va a modificar
		  {	  
		   p.Close();
		   panel.set_panel_id(panelId);
		  } 
		  else
		   return;  	  //No muestro la ventana
	  
	  if(suelo.getTipo_suelo()!=null)
	  {
	   for(int i=0; i<panel.getComboTipo().getItemCount(); i++)	
	   {
	    if(panel.getComboTipo().getItemAt(i).toString().equals(suelo.getTipo_suelo().toString()))
	    { 	
	     panel.getComboTipo().setSelectedIndex(i);
	     break;
	    } 
	   } 
	  } 
	  
	  if(suelo.getTextura()!=null)
	  {
	   for(int i=0; i<panel.getComboTextura().getItemCount(); i++)	
	   {
	    if(panel.getComboTextura().getItemAt(i).toString().equals(suelo.getTextura().toString()))
	    { 	
	     panel.getComboTextura().setSelectedIndex(i);
	     break;
	    } 
	   } 
	  }
	  
//	  if(suelo.getUso_suelo()!=null)
//	  {
//	   for(int i=0; i<panel.getComboUso().getItemCount(); i++)	
//	   {
//	    if(panel.getComboUso().getItemAt(i).toString().equals(suelo.getUso_suelo().toString()))
//	    { 	
//	     panel.getComboUso().setSelectedIndex(i);
//	     break;
//	    } 
//	   } 
//	  }
	  
	  if(suelo.getEstructura()!=null)
	  {
	   for(int i=0; i<panel.getComboEstructura().getItemCount(); i++)	
	   {
	    if(panel.getComboEstructura().getItemAt(i).equals(suelo.getEstructura()))
	    { 	
	     panel.getComboEstructura().setSelectedIndex(i);
	     break;
	    } 
	   } 
	  }

	  if(suelo.getErosion()!=null)
	   panel.getTFErosion().setText(suelo.getErosion().toString());	  
	  
//	  if(suelo.getVelocidad_Infiltracion()!=null)
//	   panel.getTFVelocidadInf().setText(suelo.getVelocidad_Infiltracion().toString());
	  
	  if(suelo.getPendiente()!=null)
	   panel.getTFPendiente().setText(suelo.getPendiente().toString());
	  
	  if(suelo.getProfundidadefectiva()!=null)
	   panel.getTFProfEfect().setText(suelo.getProfundidadefectiva().toString());
	  
	  if(suelo.getPh()!=null)
	   panel.getTFPh().setText(suelo.getPh().toString());
	  
	  if(suelo.getMateriaorganica()!=null)
	   panel.getTFMateriaOrg().setText(suelo.getMateriaorganica().toString());
	  
	  if(suelo.getPiedras()!=null)
	   panel.getTFPiedras().setText(suelo.getPiedras().toString());
	  
	  if(suelo.getRocas()!=null)
	   panel.getTFRocas().setText(suelo.getRocas().toString());
	  
	  if(suelo.getGravas()!=null)
	   panel.getTFGravas().setText(suelo.getGravas().toString());
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
				if(!suelo.IsNewObject())
				 suelo.setGeometry(pGestionManager.obtenerGeometriaModificada(), pGestionManager.TipoElementoGeometrico());		
				
				suelo.setTipo_suelo((Tipo_suelo)panel.getComboTipo().getSelectedItem());
				suelo.setTextura((Textura_suelos)panel.getComboTextura().getSelectedItem());
//				suelo.setUso_suelo((Uso_suelo)panel.getComboUso().getSelectedItem());
				suelo.setErosion(panel.getTFErosion().getText());
//				suelo.setVelocidad_Infiltracion(Double.parseDouble(panel.getTFVelocidadInf().getText()));
				
				String pendiente = panel.getTFPendiente().getText();
				if(pendiente.equals(""))
				 pendiente = "0";
				suelo.setPendiente(Double.parseDouble(pendiente));
				
				String profefect = panel.getTFProfEfect().getText();
				if(profefect.equals(""))
				 profefect = "0";
				suelo.setProfundidadefectiva(Double.parseDouble(profefect));
				
				String ph = panel.getTFPh().getText();
				if(ph.equals(""))
				 ph = "0";
				suelo.setPh(Double.parseDouble(ph));
				
				String materiaorganica = panel.getTFMateriaOrg().getText();
				if(materiaorganica.equals(""))
				 materiaorganica = "0";
				suelo.setMateriaorganica(Integer.parseInt(materiaorganica));
				
				suelo.setEstructura(panel.getComboEstructura().getSelectedItem().toString());
				
				String piedras = panel.getTFPiedras().getText();
				if(piedras.equals(""))
				 piedras = "0";
				suelo.setPiedras(Double.parseDouble(piedras));
				
				String gravas = panel.getTFGravas().getText();
				if(gravas.equals(""))
				 gravas = "0";
				suelo.setGravas(Double.parseDouble(gravas));
				
				String rocas = panel.getTFRocas().getText();
				if(rocas.equals(""))
				 rocas = "0";
				suelo.setRocas(Double.parseDouble(rocas));
				
				suelo.save();
				
				panel.Close();
				pGestionManager.ActualizarTabla(pGestionManager.SQL_SUELOS, "");
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
	 panel.getTFErosion().getDocument().addDocumentListener(doclist); 
	//--------------------------------------------------------------------------------- 
//	 panel.getTFVelocidadInf().getDocument().addDocumentListener(new DocumentListener() { 
//         public void changedUpdate(DocumentEvent e) { 
//             panel.getButtonGuardar().setEnabled(true); 
//         } 
//         public void removeUpdate(DocumentEvent e) { 
//        	 panel.getButtonGuardar().setEnabled(true); 
//         } 
//         public void insertUpdate(DocumentEvent e) { 
//        	 panel.getButtonGuardar().setEnabled(true); 
//         } 
//     }); 
	//--------------------------------------------------------------------------------- 
	 panel.getTFPendiente().getDocument().addDocumentListener(doclist);
	//--------------------------------------------------------------------------------- 
	 panel.getTFProfEfect().getDocument().addDocumentListener(doclist);
	//--------------------------------------------------------------------------------- 
	 panel.getTFPh().getDocument().addDocumentListener(doclist);
	//--------------------------------------------------------------------------------- 
	 panel.getTFMateriaOrg().getDocument().addDocumentListener(doclist);
	//--------------------------------------------------------------------------------- 
	 panel.getTFPiedras().getDocument().addDocumentListener(doclist);
	//--------------------------------------------------------------------------------- 
	 panel.getTFGravas().getDocument().addDocumentListener(doclist);
	//--------------------------------------------------------------------------------- 
	 panel.getTFRocas().getDocument().addDocumentListener(doclist);
	//---------------------------------------------------------------------------------
	 panel.getComboTipo().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
          panel.getButtonGuardar().setEnabled(true);
         }
     });
	//---------------------------------------------------------------------------------
	 panel.getComboTextura().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
          panel.getButtonGuardar().setEnabled(true);
         }
     });
	//---------------------------------------------------------------------------------
//	 panel.getComboUso().addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(java.awt.event.ActionEvent evt) {
//          panel.getButtonGuardar().setEnabled(true);
//         }
//     });
	//---------------------------------------------------------------------------------
	 panel.getComboEstructura().addActionListener(new java.awt.event.ActionListener() {
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
	  panel.getTFErosion().setEditable(false);
	  panel.getComboTipo().setEnabled(false);
	  panel.getComboTextura().setEnabled(false);
//	  panel.getComboUso().setEnabled(false);
	  panel.getComboEstructura().setEnabled(false);
//	  panel.getTFVelocidadInf().setEditable(false);
	  panel.getTFPendiente().setEditable(false);
	  panel.getTFProfEfect().setEditable(false);
	  panel.getTFPh().setEditable(false);
	  panel.getTFMateriaOrg().setEditable(false);
	  panel.getTFPiedras().setEditable(false);
	  panel.getTFGravas().setEditable(false);
	  panel.getTFRocas().setEditable(false);
	  UIManager.getDefaults().put("ComboBox.disabledForeground", Color.BLACK);
	 }  
	}
 }
