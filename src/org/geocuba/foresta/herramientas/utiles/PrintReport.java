package org.geocuba.foresta.herramientas.utiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import com.iver.andami.PluginServices;
import com.iver.utiles.swing.threads.AbstractMonitorableTask;

public class PrintReport extends AbstractMonitorableTask {

	   protected Connection conexion;
	   protected String reportName;
	   //private String [] paramNames = null;
	  // private String [] paramValues = null;
	   protected String Sqlquery;
	   protected Map parameters = null;
	    
	   public PrintReport() 
	   { 
	 
	   } 
	    
	    public PrintReport(String ReportName, String sql, Map manualParameters) 
	    { 
	   	 reportName = ReportName;
	   	 Sqlquery = sql;
	   	 conexion = ConnectionExt.getConexionActiva();
	   	 
	   	 if(manualParameters != null)
	   		parameters = manualParameters;	 
         //paramNames = pNames;
        // paramValues = pValues;
	   	setInitialStep(0);
		setDeterminatedProcess(false);
		setStatusMessage(PluginServices.getText(this, "Generando reporte..."));
		setFinalStep(1);
	    } 
	    
	    public void run() throws Exception 
	    { 
	        try 
	        { 
	            //Ruta de Archivo Jasper 
	            String fileName = "gvSIG"+Global.fileSeparator+"extensiones"+Global.fileSeparator+"org.geocuba.foresta"+Global.fileSeparator+"data"+Global.fileSeparator+"reports"+Global.fileSeparator+""+reportName+".jasper"; 
	            if(fileName == null)
	             return;	
	            //Obtener una conexión a la base de datos 
	            //conexion = ConnectionExt.getConexionActiva(); //DriverManager.getConnection(Global.GetConString(), Global.PostgresUser, Global.PostgresPw);
	 
	            //Pasamos parametros al reporte Jasper.
	            if(parameters == null)
	            {	
	             parameters = new HashMap();
	             parameters.put("sql", Sqlquery);
	            }
	            
	            //Preparacion del reporte (en esta etapa llena el diseño de reporte) 
	            //Reporte diseñado y compilado con iReport 
	            JasperPrint jasperPrint = JasperFillManager.fillReport(fileName, parameters, conexion); 
	            
	            //Se lanza el Viewer de Jasper, no termina aplicación al salir 
	            JasperViewer jviewer = new JasperViewer(jasperPrint,false); 
	            jviewer.show(); 
	       }    
	       catch (Exception j) 
	       { 
	           System.out.println("Mensaje de Error:"+j.getMessage()); 
	       } 
	       finally{ 
	          /* try {
				conexion.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} */
	       } 
	       reportStep();
	    } 
	    
	    public void finished() {
			PluginServices.getMainFrame().enableControls();
		} 
	    
}
