package org.geocuba.foresta.herramientas.utiles;

import org.cresques.cts.IProjection;

public class Global
{	
	//private static boolean connected = false;
	public static String PostgresUser = "";
	public static String PostgresPw = "";
	public static String PostgresServer = "";
	public static String PostgresPort = "";
	public static String gvSIGPostgresSQLDriverName = "PostGIS JDBC Driver";
	public static String PostgresSQLDriverName = "org.postgresql.Driver";
	private static Object mde = null;
	//static ConnectionWithParams cwp = null;
	//private static Connection conn = null;
	public static IProjection projeccionActiva; // Cuba Norte
	//public static String PostgresSQLBinPath = "C:\\Program Files (x86)\\PostgreSQL\\9.0\\bin\\";
	//public static String ExtDirInAndami = "gvSIG\\extensiones\\org.geocuba.foresta\\";
	//public static String DefaultDBFileName = "forestadb_default.backup";
	public static String postgresDBName = "";
	public static String fileSeparator = "\\";
	
	/*public static void Connect()
	 {
		if(con == null)
		{
		 con = new TConexion();
		 con.setVisible(true);
		} 
		 //c = Globals.ActiveConnectionWithParams();//
		//con.CheckConection();
		c = con.getSelectedPostgresConn();
		connected = c.isConnected();
	}*/
	
	/*public static boolean Conectado() 
	{
	 if(conn == null)
	  return false;	 
	 
      try {
		return !conn.isClosed();
		
	  } catch (SQLException e) {
		e.printStackTrace();
		return false;
	  }
	}*/
	
	/*public static ConnectionWithParams ActiveConnectionWithParams()
	{
	 return cwp;	
	}
	
	public static ConnectionSettings GetConSettings()
	{
	        ConnectionSettings cs = new ConnectionSettings();
	        cs.setDb(cwp.getDb());
	        cs.setSchema("public");
	        cs.setDriver(gvSIGPostgresSQLDriverName);
	        cs.setHost(PostgresServer);
	        cs.setPort(PostgresPort);
	        cs.setUser(PostgresUser);
	        cs.setPassw(PostgresPw);
	        cs.setName(cwp.getDb()+" Connection");
	        return cs;
	}*/
	
	/*public static String GetUrl()
	{
	 //if(connected)	
	 if(cwp != null)
	  return "jdbc:postgresql://"+PostgresServer+":"+PostgresPort+"/"+cwp.getDb();
	 else
	  return null;	 
	}*/
	
	/*public static String GetUser()
	{
	 if(connected)
	  return c.getUser();
	 else
	  return null;	 
	}
	
	public static String GetPass()
	{
	 if(connected)	
	  return c.getPw();
	 else
	  return null;	 
	}
	
	public static String GetdbName()
	{
	 if(connected)	
	  return c.getDb();
	 else
	  return null;	 
	}
	*/
	/*public static String GetConString()
	{
	 return cwp.getConnectionStr();	
	}*/
	
	/*public static void setNewMDEGenerated(boolean value)
	{
	 newMDEGenerated = value;
	}
	
	public static boolean getisNewMDEGenerated()
	{
	 return newMDEGenerated;
	}*/
	
	public static void setMDE(Object mdt)
	{
	 mde = mdt;	
	}
	
	public static Object getMDE()
	{
	 return mde;	
	}
	
	/*public static void SetConexionActiva(ConnectionWithParams cwp) {
		Global.cwp = cwp;
		
		if(cwp!=null)
		{
			try {
				conn= DriverManager.getConnection(cwp.getConnectionStr(),
						cwp.getUser(), cwp.getPw());
				//connected = conn
			} catch (SQLException e) {
				e.printStackTrace();
			}		
		}
		else
			conn=null;
		
		if(cwp.isConnected())
			LoadAll();
	}*/
	
	/**Simplemente crea una nueva conexion(java.sql.Connection) a partir 
	 * de los parametros globales definidos en esta clase sin importar 
	 * si se ha establecido algun ConnectionWithParams para trabajar con 
	 * alguna base de datos en específico. 
	 * @throws SQLException
	 */
	/*public static Connection CreateConnection() throws SQLException
	{
		Connection connection = null;        
        String url = "jdbc:postgresql://"+ PostgresServer+":"+ PostgresPort;
        System.out.println("opening db connection. "+"url:"+url);
        connection = DriverManager.getConnection(url, PostgresUser,PostgresPw);

		return connection;
	}*/
	
	/*public static Connection CreateConnection(String dbName) throws SQLException
	{
		Connection connection = null;        
        String url = "jdbc:postgresql://"+ PostgresServer+":"+ PostgresPort + "//" + dbName;
        System.out.println("opening db connection. "+"url:"+url);
        connection = DriverManager.getConnection(url, PostgresUser,PostgresPw);

		return connection;
	}*/
	
	/*public static Connection getActiveConnection()
	{
     return conn;		
	}*/
	
	/*public static String GetdbName()
	{
     return cwp.getDb();		
	}*/
	
	public static boolean isForestaLayer(String layerName)
	{
	 if(layerName.equals("Hidrografía_lineal") ||
		layerName.equals("Hidrografía_areal") ||
		layerName.equals("Fajas") ||
		layerName.equals("Fajas_Real") ||
		layerName.equals("Parteaguas") ||
		layerName.equals("Relieve") ||
		layerName.equals("Suelos") ||
		layerName.equals("Parcelas") ||
		layerName.equals("Cuencas_Interés_Nacional") ||
		layerName.equals("Municipios") ||
		layerName.equals("Perfiles") ||
		layerName.equals("Subtramos") ||
		layerName.equals("Redes_drenaje"))
	   return true;
	 else
	  return false;	 
	}
}
