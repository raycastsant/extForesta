package org.geocuba.foresta.reportes;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.Agrupador_uso;
import org.geocuba.foresta.gestion_datos.Agrupador_usoManager;
import org.geocuba.foresta.herramientas.utiles.Global;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.exceptions.layers.LegendLayerException;
import com.iver.cit.gvsig.project.documents.view.gui.View;

public class TemMapsExt extends Extension{

	public void initialize() {
	}

	public void execute(String actionCommand) {
		
		try {
			   
			if (actionCommand.compareTo("mt_tiposuelo") == 0)
		    {
			 Funciones_Utiles.LeyendaValoresUnicos("Suelos", "tipo", "tipo", "tipo_suelo", "id", true, true);	
		    }
			
			if (actionCommand.compareTo("mt_usosparcelas") == 0)
		    {
				Funciones_Utiles.LeyendaValoresUnicos("Parcelas", "uso", "descripcion_uso", "usos_catastro", "id", true, true);  //Funciones_Utiles.LeyendaValoresUnicos("Suelos", "textura", true);//Funciones_Utiles.LeyendaValoresUnicos("Parcelas", "uso", false);//Funciones_Utiles.LeyendaValoresUnicos("Parcelas", "coduso", "descuso", "f_tusoscat", "coduso", true, true);	
		    }
			
			if (actionCommand.compareTo("mt_textsuelos") == 0)
		    {
				Funciones_Utiles.LeyendaValoresUnicos("Suelos", "textura", "textura", "textura_suelos", "id", true, true);  //Funciones_Utiles.LeyendaValoresUnicos("Suelos", "textura", true);
		    }
			
			if (actionCommand.compareTo("mt_estructsuelos") == 0)
		    {
				Funciones_Utiles.LeyendaValoresUnicos("Suelos", "estructura", true);	
		    }
			
			if (actionCommand.compareTo("mt_matorgsuelos") == 0)
		    {
				Funciones_Utiles.LeyendaValoresUnicos("Suelos", "matorg", true);	
		    }
			
			if (actionCommand.compareTo("mt_uso_suelos") == 0)
		    {
				JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
				Agrupador_uso [] lista = Agrupador_usoManager.obtener_Usos_suelos();
				for(int i=0; i<lista.length; i++)
				{
				 String sql = "update parcelas set agrupador_uso='"+lista[i].getTipo_uso()+"' " +
		     		"where uso in(select usos_catastro.id from usos_catastro where uso_suelo="+lista[i].getId()+")";
				 System.out.println(sql);
			     db.ejecutarConsulta(sql);		
				}		
				
				Funciones_Utiles.LeyendaValoresUnicos("Parcelas", "agrupador_uso", false);
				
//				Funciones_Utiles.LeyendaValoresUnicos("Parcelas", "uso", "agrupadores_uso.tipo_uso", "(usos_catastro inner join agrupadores_uso " +
//						"on usos_catastro.uso_suelo=agrupadores_uso.id)inner join parcelas on parcelas.uso=usos_catastro.id",
//						"parcelas.uso", false, true);  //Funciones_Utiles.LeyendaValoresUnicos("Suelos", "textura", true);
		    }
			
		   } catch (LegendLayerException e) {
				e.printStackTrace();
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			}
    };
	
	public boolean isEnabled() {
		if(!ConnectionExt.Conectado())
			 return false;
				  
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
		if(f == null)
		 return false;	
		
		if(!(f instanceof View))
		 return false;
		
		Global.projeccionActiva = ((View)f).getProjection();
		return true;
	}

	public boolean isVisible() {
		if(!ConnectionExt.Conectado())
			 return false;
				  
			com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
			if(f == null)
			 return false;	
			
			if(!(f instanceof View))
			 return false;
			
	  return true;		
	}

}

