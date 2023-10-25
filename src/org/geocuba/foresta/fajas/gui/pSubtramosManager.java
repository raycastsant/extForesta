package org.geocuba.foresta.fajas.gui;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.BitSet;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.geocuba.foresta.fajas.Perfil;
import org.geocuba.foresta.fajas.PerfilManager;
import org.geocuba.foresta.fajas.SubtramoPerfil;
import org.geocuba.foresta.fajas.SubtramosManager;
import org.geocuba.foresta.gestion_datos.FajaManager;
import org.geocuba.foresta.herramientas.utiles.AlgUtils;
import org.geocuba.foresta.herramientas.utiles.ColoredTableCellRenderer;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.DataSource;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.fmap.layers.layerOperations.AlphanumericData;

 public class pSubtramosManager {

	private static pSubtramos  panel = null;
	private static FLyrVect capa;
	private static int idParteaguas;
	private static double Intensidad_aguacero;
	private static double Wf;
	private static double anchoI;
	private static double anchoD;
	
	public static void showPanel_Gestion(int _idParteaguas, double _Intensidad_aguacero, double _Wf) throws ReloadLayerException 
	{
		String title = "Subtramos de perfiles";
		panel = (pSubtramos)UIUtils.GetPanel("_"+title);
		if(panel == null)
		{
		 panel = new pSubtramos(title);	
		 
		 idParteaguas = _idParteaguas;
		 
		 ActualizarTabla(idParteaguas);
		 capa = (FLyrVect)VistaManager.GetActiveView().getMapControl().getMapContext().getLayers().getLayer("Subtramos");
		 capa.reload();
		 setComponentsListeners();

		 Intensidad_aguacero = _Intensidad_aguacero;
		 Wf = _Wf;
		 
		 panel.Show();
		}
	}
	
	private static void setComponentsListeners()
	{
	 if(panel == null)
	  return;
	 //---------------------------------------------------------------------------------
	 panel.getButtonCerrar().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) 
         {
          panel.Close();	
         }
     });
	 //---------------------------------------------------------------------------------
	 panel.getButtonZoom().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) 
         {
          ZoomElemetoSeleccionado();	
         }
     });
	//---------------------------------------------------------------------------------
		 panel.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
	         public void valueChanged(ListSelectionEvent e) 
	         {
	         	PluginServices.getMDIManager().setWaitCursor();
	            SelectableDataSource dataModel = null;

	          try {
                 AlphanumericData ad = (AlphanumericData)capa;                  	          
	        	 dataModel = ad.getRecordset();
	             DefaultListSelectionModel model = (DefaultListSelectionModel)panel.getTable().getSelectionModel();
	             BitSet selection = dataModel.getSelection();
	             int firstIndex=e.getFirstIndex();
	             
	             //capa.getSelectionSupport().clearSelection();
	             
	             if (firstIndex >= 0) 
	             {
				  for (int i = firstIndex; i <= e.getLastIndex(); i++)
				  {	  
	               selection.set(i, model.isSelectedIndex(i));
				  } 
			     }
	             
	             } catch (ReadDriverException e1) {
						e1.printStackTrace();
						return;
					}
	             
	            // if (e.getValueIsAdjusting() == false) 
	              dataModel.fireSelectionEvents();

//	             panel.getButtonInformacion().setEnabled(true);
	             panel.getButtonZoom().setEnabled(true);
	             
	             PluginServices.getMDIManager().restoreCursor();
	         }
	     });
		 //---------------------------------------------------------------------------------
		 panel.getButtonCalcularAnchos().addActionListener(new java.awt.event.ActionListener() {
	         public void actionPerformed(java.awt.event.ActionEvent evt) 
	         {
	          try {
				if(CalcularAnchos())
				{		
			     panel.getButtonFajas().setEnabled(true);
			     DecimalFormat dc = new DecimalFormat();
			     dc.setMaximumFractionDigits(2);
			     panel.getLabelAnchos().setText("Ancho calculado:  Izquierda_ "+dc.format(anchoI)+"  Derecha_ "+dc.format(anchoD));
				}  
				else
				{
				 panel.getButtonFajas().setEnabled(false);
				 panel.getLabelAnchos().setText("");					
				}
				
			    } catch (ReadDriverException e) {
				 e.printStackTrace();
			    }	
	         }
	     });
		//---------------------------------------------------------------------------------
		 panel.getButtonFajas().addActionListener(new java.awt.event.ActionListener() {
	         public void actionPerformed(java.awt.event.ActionEvent evt) 
	         {
	          AlgUtils.DesactivateLayers();
	          VistaManager.GetActiveView().getMapControl().getMapContext().getLayers().getLayer("Hidrografía_lineal").setActive(true);
	          PluginServices.getMainFrame().enableControls();
	          
			  FajaManager.crear_Faja_Ancho_Fijo(anchoI, anchoD);
	         }
	     });
		//---------------------------------------------------------------------------------
		 panel.getButtonValores().addActionListener(new java.awt.event.ActionListener() {
	         public void actionPerformed(java.awt.event.ActionEvent evt) 
	         {
	          JTable table = panel.getTable();	 
	          for(int i=0; i<table.getRowCount(); i++)
	          {
	           double val = Math.random();
	           val = val*10;
	          
	          //Valores tomados de Herrero 2003 
	           if(val<=1)
	            table.setValueAt("2.6", i, 10);
	           else
	           if(val>1 && val<=2)
	   	        table.setValueAt("1.46", i, 10);
	           else
			   if(val>2 && val<=3)
				table.setValueAt("0.84", i, 10);
	           else
		       if(val>3 && val<=4)
		   	    table.setValueAt("0.99", i, 10);
		       else
			   if(val>4 && val<=5)
			    table.setValueAt("1.66", i, 10);
			   else
			   if(val>5 && val<=6)
			    table.setValueAt("1.38", i, 10);
			   else
			   if(val>6 && val<=7)
			    table.setValueAt("0.98", i, 10);
			   else
			   if(val>7 && val<=8)
			    table.setValueAt("1.65", i, 10);
			   else
			   if(val>8 && val<=9)
			    table.setValueAt("0.78", i, 10);
			   else
			    table.setValueAt("0.56", i, 10);
	           
	           System.out.println(val);
	          }	  
	         }
	     });
	}
	
	public static void ActualizarTabla(int _idParteaguas)
	{
	 PluginServices.getMDIManager().setWaitCursor();
	 String Sql = SubtramosManager.getQueryTablaSubtramos(_idParteaguas);
	 
	 panel.ActualizarTabla(Sql);
	 panel.getTable().getTableHeader().setReorderingAllowed(false);
	 
	 Color col = new Color(255, 254, 0);
	   
	 panel.getTable().getColumnModel().getColumn(3).setCellRenderer(new ColoredTableCellRenderer(col));
	 panel.getTable().getColumnModel().getColumn(10).setCellRenderer(new SubtramosCellRender(col));
	 
//     panel.getButtonInformacion().setEnabled(false);
     panel.getButtonZoom().setEnabled(false);
     if(panel.getTable().getRowCount()>0)
     {
      panel.getButtonCalcularAnchos().setEnabled(true);
      //panel.getButtonFajas().setEnabled(true);
     }  
     else
     {
      panel.getButtonCalcularAnchos().setEnabled(false);
      //panel.getButtonFajas().setEnabled(false);
     }
     
     PluginServices.getMDIManager().restoreCursor();
	}
	
	private static void ZoomElemetoSeleccionado()
	{
			 DataSource ds;
				
			 PluginServices.getMDIManager().setWaitCursor();
			 
		     try {
					ds = capa.getRecordset();
		            ds.start();

	                 int fieldIndex = ds.getFieldIndexByName("Gid");
	                 int row = panel.getTable().getSelectedRow();
	                 int selGid = Integer.parseInt(panel.getTable().getValueAt(row, fieldIndex).toString());

	                 for(int i=0; i< ds.getRowCount(); i++)
	                 {
	                  System.out.println(Integer.parseInt(ds.getFieldValue(i,fieldIndex).toString()));	 
	                  if(Integer.parseInt(ds.getFieldValue(i,fieldIndex).toString()) == selGid)
	                  {	  
	                   IGeometry geom = capa.getSource().getFeature(i).getGeometry();
	                   Rectangle2D selectedExtent = geom.getBounds();
	                  
			           if (selectedExtent != null) 
			        	   VistaManager.GetActiveView().getMapControl().getViewPort().setExtent(selectedExtent);
			           else
			           {     
			        	PluginServices.getMDIManager().restoreCursor();
			        	JOptionPane.showMessageDialog(null, "No se obtuvieron bounds");
			           } 
	                  } 
	                 }	 
	                 
		        ds.stop();
		        
				} catch (ReadDriverException e) {
					e.printStackTrace();
				}
				
				PluginServices.getMDIManager().restoreCursor();
	}
	
	private static boolean CalcularAnchos() throws ReadDriverException
	{
	 PluginServices.getMDIManager().setWaitCursor();
	 
	 JTable table = panel.getTable();	 
     for(int i=0; i<table.getRowCount(); i++)
     {
      Object value = table.getValueAt(i,10);
      
      if(value==null || value.toString().equals(""))
      {	  
       JOptionPane.showMessageDialog(null, "Faltan datos de velocidad de infiltración por llenar", "Error", JOptionPane.WARNING_MESSAGE);
       PluginServices.getMDIManager().restoreCursor();
       return false;
      }
    	  
      double velInf = Double.parseDouble(value.toString());
      
      if(velInf <= 0)
      {
    	  JOptionPane.showMessageDialog(null, "Existen valores menores o iguales que cero", "Error", JOptionPane.WARNING_MESSAGE);
    	  PluginServices.getMDIManager().restoreCursor();
          return false;  
      }	  
      
      int gid = Integer.parseInt(table.getValueAt(i,0).toString());
      SubtramosManager.setVelocidadInfiltracion_Subtramo(gid, velInf);
     }
     
     PerfilManager pm = new PerfilManager();
     Perfil []perfiles = pm.Cargar_Perfiles(idParteaguas);
     double L;  //Longitud horizontal de la ladera 
     double Wm; //Velocidad de infirltracion media de los suelos del perfil
     double a; //Ancho de la faja calculado para un perfil
     double sumaVelocidades;
     SubtramoPerfil []subtramos;
     double sumaAnchosI = 0;
     double sumaAnchosD = 0;
     int cantPerfilesDerecha = 0;
     int cantPerfilesIzquierda = 0;
     anchoD = 0;
     anchoI = 0;
     
     for(int i=0; i<perfiles.length; i++)
     {
      L = 0;
      sumaVelocidades = 0;
      Wm = 0;
      a = 0;
      subtramos = perfiles[i].getListaSubtramos();
      if(subtramos == null)
       continue;
      
      for(int k=0; k<subtramos.length; k++)
      {
       double l = subtramos[k].getLongitud();	  
       L += l;     
       sumaVelocidades += (subtramos[k].getVelocidad_infiltracion()*l);
      }	 

      System.out.println(Intensidad_aguacero);
      System.out.println(Wf);
      
      Wm = sumaVelocidades/L;
      a = (L*(Intensidad_aguacero-Wm))/(Wf-Wm);   //Herrero 2003
      
      ////////////////////////////
      if(a<0)
       a = a*-1;	  
      //////////////////////////
      
      perfiles[i].setAncho(a);
      perfiles[i].save();
      
      if(perfiles[i].getOrilla().equals("I"))
      {	  
       sumaAnchosI += a;
       cantPerfilesIzquierda++;
      } 
      else
      {	  
       sumaAnchosD += a;
       cantPerfilesDerecha++;
      } 
      
      anchoD = sumaAnchosD/cantPerfilesDerecha;
      anchoI = sumaAnchosI/cantPerfilesIzquierda;
     }  	 
     
     ActualizarTabla(idParteaguas);    
     
    // TrazasManager.insertar_Traza("Se calculó el ancho de Fajas por Herrero-Melchanov");
	 PluginServices.getMDIManager().restoreCursor();
	 
	 return true;
	}
 }

 