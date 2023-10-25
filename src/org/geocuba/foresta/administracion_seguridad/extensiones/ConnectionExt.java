package org.geocuba.foresta.administracion_seguridad.extensiones;

import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.geocuba.foresta.administracion_seguridad.StringEncrypter;
import org.geocuba.foresta.administracion_seguridad.UsuarioManager;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.gui.dialogoUsuariosManager;
import org.geocuba.foresta.herramientas.utiles.Global;
import org.geocuba.foresta.herramientas.utiles.XMLUtils;
import org.gvsig.sifomap.ordenacion.global.Globals;

import com.iver.andami.ConfigurationException;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.drivers.db.utils.ConnectionWithParams;
import com.iver.utiles.XMLEntity;
import com.twmacinta.util.MD5;

public class ConnectionExt extends Extension{
	
	private static Connection conn;
	
	public void initialize() {
		
		initializeConfigs();
		registrarIconos();
		//VistaManager.crearVista();
		PluginServices.getMainFrame().setTitle("FORESTA: SIG para el cálculo de ancho y manejo de Fajas Forestales Hidrorreguladoras");
	}
	
//	    public void postInitialize()
//	    {
//	    	try {
//				dialogoUsuariosManager.Conectar("sifomap");
//			
//				if(!UsuarioManager.VerificarUsuario("admin", new MD5("administrador").asHex()))
//		        {
//		         JOptionPane.showMessageDialog(null, "Usuario o Contraseña incorrectos", "Error", JOptionPane.WARNING_MESSAGE);
//		         ConnectionExt.setConexion(null);
//		        }	
//			
//	    	} catch (SQLException e) {
//				e.printStackTrace();
//			}
//	    }

	public void execute(String actionCommand) 
	{
		if (actionCommand.compareTo("Conectar") == 0)
	    {
		 //Conectar("foresta");
		 UsuarioManager.control_usuario();
	    }
		
//		if (actionCommand.compareTo("prueba") == 0)
//	    {
//		 JDBCAdapter db = new JDBCAdapter(conn);
//		 db.ejecutarConsulta("select siglas from especies order by siglas asc");
//			 if(!db.isEmpty())
//			 {
//				  JDBCAdapter dbaux = new JDBCAdapter(conn);
//				  for(int i=0; i<db.getRowCount(); i++)
//				   dbaux.ejecutarConsulta("update especies set id="+(i+1)+" where siglas='"+db.getValueAsString(i,0)+"'");	  
//			 }		 
//	    }
    };
    
    private void initializeConfigs()
    {
    	XMLEntity xml;
    	
    	try {
    		Global.fileSeparator = System.getProperty("file.separator");
    		 
    		xml = XMLUtils.persistenceFromXML("gvSIG"+Global.fileSeparator+"extensiones"+Global.fileSeparator+"org.geocuba.foresta"+Global.fileSeparator+"configForesta.xml");
    		int index = xml.firstIndexOfChild("name", "conexion");
    		xml = xml.getChild(index);
    		
    		String pass = xml.getStringProperty("PostgresPass");
    		pass = StringEncrypter.Desencriptar(pass);
    		
    		Global.postgresDBName = xml.getStringProperty("PostgresDBName");
    		Global.PostgresPort = xml.getStringProperty("PostgresPort");
    		Global.PostgresPw = pass;
    		Global.PostgresUser = xml.getStringProperty("PostgresUser");
    		Global.PostgresServer = xml.getStringProperty("PostgresServerDir");
    		
    		//Global.PostgresSQLBinPath = xml.getStringProperty("PostgresSQLBinPath");
    		/*String pg = System.getenv("PGLOCALEDIR");
    		Global.PostgresSQLBinPath = pg.substring(0, pg.indexOf("8.3") + 4) + "bin\\";
    		System.out.println(Global.PostgresSQLBinPath);*/
    		//JOptionPane.showMessageDialog(null, "El bin de postgres es  "+Global.PostgresSQLBinPath);
    		
    	} catch (ConfigurationException e) {
    		e.printStackTrace();
    	}
    	catch (java.lang.IndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "No se encontró el fichero de configuración del sistema 'configForesta.xml'.", "Error", JOptionPane.ERROR_MESSAGE);
		}
    }
    
    public static Connection getConexionActiva()
    {
     return conn; 	
    }
    
    public static boolean Conectado() 
	{
	 if(conn == null)
	  return false;	 
	 
      try {
    	  System.out.println(conn.isClosed());
		return !conn.isClosed();
		
	  } catch (SQLException e) {
		e.printStackTrace();
		return false;
	  }
	}
    
//    public static Connection CreateConnection() throws SQLException
//	{
//		Connection connection = null;        
//        String url = "jdbc:postgresql://"+ Global.PostgresServer+":"+ Global.PostgresPort;
//        System.out.println("opening db connection. "+"url:"+url);
//        connection = DriverManager.getConnection(url, Global.PostgresUser, Global.PostgresPw);
//
//		return connection;
//	}
    
    public static void setConexion(Connection c)
    {
     conn = c; 	
    }
    
    private void registrarIconos()
    {
     PluginServices.getIconTheme().registerDefault
     ("password",this.getClass().getClassLoader().getResource("images/password.png"));
     PluginServices.getIconTheme().registerDefault
     ("config_conexion",this.getClass().getClassLoader().getResource("images/database_connect.png"));
     PluginServices.getIconTheme().registerDefault
     ("login",this.getClass().getClassLoader().getResource("images/login.png"));
     PluginServices.getIconTheme().registerDefault
     ("restaurarbd",this.getClass().getClassLoader().getResource("images/restaurarbd.png"));
     PluginServices.getIconTheme().registerDefault
     ("salvarbd",this.getClass().getClassLoader().getResource("images/database_save.png"));
     PluginServices.getIconTheme().registerDefault
     ("gestion_usuarios",this.getClass().getClassLoader().getResource("images/gestion_usuarios.png"));
     PluginServices.getIconTheme().registerDefault
     ("desconectar",this.getClass().getClassLoader().getResource("images/disconnect.png"));
     PluginServices.getIconTheme().registerDefault(
      "cursor_information", this.getClass().getClassLoader().getResource("images/cursor-information.png"));
     PluginServices.getIconTheme().registerDefault(
      "informacion", this.getClass().getClassLoader().getResource("images/info.png"));
     PluginServices.getIconTheme().registerDefault(
     "crear_fajas", this.getClass().getClassLoader().getResource("images/calcularfaja.png"));
     PluginServices.getIconTheme().registerDefault(
     "importar", this.getClass().getClassLoader().getResource("images/importar.png"));
     PluginServices.getIconTheme().registerDefault(
     "trazas", this.getClass().getClassLoader().getResource("images/trazas.png"));
     PluginServices.getIconTheme().registerDefault(
     "cargar_vista", this.getClass().getClassLoader().getResource("images/cargar_vista.png"));
     PluginServices.getIconTheme().registerDefault(
     "help", this.getClass().getClassLoader().getResource("images/help.png"));
     PluginServices.getIconTheme().registerDefault(
     "invertir", this.getClass().getClassLoader().getResource("images/invertir.png"));
     PluginServices.getIconTheme().registerDefault(
     "mostrar_sentido", this.getClass().getClassLoader().getResource("images/mostrar_sentido.png"));
     PluginServices.getIconTheme().registerDefault(
     "reporte", this.getClass().getClassLoader().getResource("images/report.png"));
     PluginServices.getIconTheme().registerDefault(
     "map", this.getClass().getClassLoader().getResource("images/map.png"));
     PluginServices.getIconTheme().registerDefault(
     "configurar_vista", this.getClass().getClassLoader().getResource("images/configurar_vista.png"));
    } 
    
	public boolean isEnabled() 
	{
	 return true;	
	}

	public boolean isVisible() 
	{
      ConnectionWithParams c = Globals.ActiveConnectionWithParams();
		
		try {
					if(c==null || !c.isConnected())
					{	
					 setConexion(null);
					 UsuarioManager.setusuario(null);
					 return false;
					} 
		    
					String dbName = Globals.ActiveConnectionWithParams().getDb();
					dialogoUsuariosManager.Conectar(dbName);
				
					if(!UsuarioManager.VerificarUsuario("admin", new MD5("administrador").asHex()))
			        {
			         //JOptionPane.showMessageDialog(null, "Usuario o Contraseña incorrectos", "FORESTA_Error", JOptionPane.WARNING_MESSAGE);
			         setConexion(null);
			         UsuarioManager.setusuario(null);
			         return false;
			        }
					else
				     return true;		
				
		    	} catch (SQLException e) {
					e.printStackTrace();
					
					setConexion(null);
					UsuarioManager.setusuario(null);
					return false;
				}
	}
}

