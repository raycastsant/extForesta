package org.geocuba.foresta.herramientas;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import org.geocuba.foresta.herramientas.utiles.Global;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
 
public class F_HelpExt extends Extension{

	public void initialize() {
	}

	public void execute(String actionCommand) {
		
	if (actionCommand.compareTo("help") == 0)
    {
	 Funciones_Utiles.abrirURL(PluginServices.getPluginServices(this).getPluginDirectory()+""+Global.fileSeparator+"data"+Global.fileSeparator+"help"+Global.fileSeparator+"ayuda.chm");
    }
	
	/**Esto debe hacerse cuando se hallan creado las tablas temporales,
	 * poniendo un punto de ruptura en el principio del metodo Import(), de la
	 * clase ImportNode de SIFOMAP, por lo que este codigo debe ejecutarse en otro programa, u 
	 * otra instancia del gvSIG, al mismo tiempo que se debuguea el SIFOMAP*/
	if (actionCommand.compareTo("parche") == 0)
    {
	 JDBCAdapter db =  new JDBCAdapter("jdbc:postgresql://localhost:5432/habana","org.postgresql.Driver","postgres","postgres");
	 JDBCAdapter dbaux =  new JDBCAdapter("jdbc:postgresql://localhost:5432/habana","org.postgresql.Driver","postgres","postgres");
	 
	 //Empresas - US
	 db.ejecutarConsulta("select gid,objectid from empresas_temp");
	 if(!db.isEmpty())
	 {
		 for(int i=0; i<db.getRowCount(); i++)
			 dbaux.ejecutarConsulta("update unidadessilvicolas_temp set empresa="+db.getValueAsString(i,0)+" where empresa="+db.getValueAsString(i,1));
	 }	 
	 
	 //US - Lotes
	 db.ejecutarConsulta("select gid,objectid from unidadessilvicolas_temp");
	 if(!db.isEmpty())
	 {
		 for(int i=0; i<db.getRowCount(); i++)
			 dbaux.ejecutarConsulta("update lotes_temp set unidadsilv="+db.getValueAsString(i,0)+" where unidadsilv="+db.getValueAsString(i,1));
	 }	
	
	 //Lotes - Rodales
	 db.ejecutarConsulta("select gid,objectid from lotes_temp");
	 if(!db.isEmpty())
	 {
		 for(int i=0; i<db.getRowCount(); i++)
			 dbaux.ejecutarConsulta("update rodales_temp set lote="+db.getValueAsString(i,0)+" where lote="+db.getValueAsString(i,1));
	 }	 
    }
	
    };
	
	public boolean isEnabled() {
		return true;
	}

	public boolean isVisible() {
	  return true;		
	}

}

