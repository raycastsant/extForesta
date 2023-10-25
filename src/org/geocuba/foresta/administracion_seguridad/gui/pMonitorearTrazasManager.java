package org.geocuba.foresta.administracion_seguridad.gui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextField;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.herramientas.utiles.PrintReport;
import org.geocuba.foresta.herramientas.utiles.gui.UIUtils;
import org.geocuba.foresta.herramientas.writerTasks.BackgroundExecution;

import com.iver.andami.PluginServices;
import com.iver.utiles.DateTime;

 public class pMonitorearTrazasManager 
 {
	private static pMonitorearTrazas  panel = null;
	
	public static void mostrarPanel() 
	{
	    String title = "Monitoreo de trazas";
		panel = (pMonitorearTrazas)UIUtils.GetPanel("_"+title);
		if(panel == null)
		{
		     panel = new pMonitorearTrazas(title);
		 
			 ActualizarTabla("");
			 ActualizarComponentes();
			 setComponentsListeners();
			 panel.Show();
		}
	}
	
	private static void setComponentsListeners()
	{
	 if(panel == null)
	  return;
	
	 //---------------------------------------------------------------------------------
//	 panel.getButtonAdd().addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(java.awt.event.ActionEvent evt) 
//         {
//			 Usuario usuario = new Usuario(); 
//			 pUsuarioManager pum = new pUsuarioManager();
//             pum.MostrarPanel(usuario);            			
//         }
//     });
	 //---------------------------------------------------------------------------------
	 panel.getButtonCerrar().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) 
         {
          panel.Close();	
         }
     });
	//---------------------------------------------------------------------------------
	 panel.getButtonFiltros().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) 
         {
          String where = obtenerCondicion();	 
          ActualizarTabla(where);	
         }
     });
	//---------------------------------------------------------------------------------
	 panel.getButtonReporte().addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) 
         {
          String where = obtenerCondicion();
          String query = TrazasManager.getQueryReporte(where);
          
          BackgroundExecution.cancelableBackgroundExecution(new PrintReport("Trazas", query, null));	
         }
     });
	//---------------------------------------------------------------------------------
	 for(int i=0; i<panel.getjDCHDesde().getComponentCount(); i++)
	 {
	  if(panel.getjDCHDesde().getComponents()[i] instanceof JTextField)
	  {	  
	   panel.getjDCHDesde().getComponents()[i].addKeyListener(new java.awt.event.KeyAdapter() {
	         public void keyTyped(java.awt.event.KeyEvent evt) {
	             evt.consume();
	         }
	     });
	   
	   panel.getjDCHHasta().getComponents()[i].addKeyListener(new java.awt.event.KeyAdapter() {
	         public void keyTyped(java.awt.event.KeyEvent evt) {
	             evt.consume();
	         }
	     });
	   
	   break;
	  }	                                        
	 }	 
	}
	
	public static void ActualizarTabla(String where)
	{
	 PluginServices.getMDIManager().setWaitCursor();
	 
	 panel.ActualizarTabla(TrazasManager.getQueryTabla(where));
	 panel.getTable().getTableHeader().setReorderingAllowed(false);
	 
     PluginServices.getMDIManager().restoreCursor();
	}
	
	private static void ActualizarComponentes()
	{
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 String []lista = TrazasManager.obtenerUsuarios(db);
	 
	 panel.getjCBXUsuarios().removeAllItems();
	 panel.getjCBXUsuarios().addItem("--Todos--");
	 
	 if(lista != null)
	 {		 
		 for(int i=0; i<lista.length; i++)
		  panel.getjCBXUsuarios().addItem(lista[i]);	
	 }
	 
	 ////////////////////////////////////////////////////////////////////
	 String fecha = TrazasManager.obtener_minimaFecha(db);
	 if(fecha != null)
	 {
		try {
			  //Date d = DateTime.stringToDate(fecha);
			  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			   
			  Date d = sdf.parse(fecha);
			  panel.getjDCHDesde().setDate(d);
			  
		} catch (ParseException e) {
			e.printStackTrace();
		}
	 }	 
	}
	
	private static String obtenerCondicion()
	{
	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");	 
  	  String desde = sdf.format(panel.getjDCHDesde().getDate());
  	  String hasta = sdf.format(panel.getjDCHHasta().getDate());
  	  
//  	  if(desde.equals(hasta))
//  	  {
  	   desde += " 00:00:00";
  	   hasta += " 23:59:59";
//  	  }	 
  	  
//  	  String where = "where fecha>='"+desde+"' and fecha <='"+hasta+"'";
  	   
  	  String where = null;
  	 
  	  if(panel.getjCBXUsuarios().getSelectedIndex()>0) //Condicion de usuario
  	   where = TrazasManager.getFilterCondition(desde, hasta, true, panel.getjCBXUsuarios().getSelectedItem().toString());
  	  else
  	   where = TrazasManager.getFilterCondition(desde, hasta, false, "");
  	  
  	  return where;
	}
}

 