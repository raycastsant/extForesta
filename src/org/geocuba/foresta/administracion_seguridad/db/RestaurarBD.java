package org.geocuba.foresta.administracion_seguridad.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.Usuario;
import org.geocuba.foresta.administracion_seguridad.UsuarioManager;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import org.geocuba.foresta.herramientas.utiles.Global;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.drivers.DBException;
import com.iver.utiles.swing.threads.AbstractMonitorableTask;

public class RestaurarBD extends AbstractMonitorableTask
{
	private String dbname;
	private String path;
	private String []oldtrazas;
	private Usuario usuario;
	
	public RestaurarBD(String _dbname, String _path, String []_oldtrazas, Usuario _usuario)
	{
		this.dbname = _dbname;
		this.path = _path;
		this.oldtrazas = _oldtrazas;
		this.usuario = _usuario;
		
		setInitialStep(0);
		setDeterminatedProcess(false);
		setStatusMessage(PluginServices.getText(this, "Restaurando base de datos: '"+dbname+"'"));
		setFinalStep(1);
	}
	
	public void run() throws SQLException, DBException
	{
//				String query="CREATE DATABASE "+dbname+" WITH OWNER = postgres ENCODING = 'UTF8' TEMPLATE = template_postgis";
//				
//				Statement st;
//				Connection conn = ConnectionExt.getConexionActiva();
//				
				try 
				{
//					st = conn.createStatement();
					//st.executeUpdate("DROP DATABASE "+dbname);
//					st.execute(query);
//					st.execute("update pg_database set encoding=8 where datname='"+dbname+"'");
					
					setNote("Restaurando datos");
					
					//Establezco conexión con BD Postgres para cerrar las conexiones a la BD Activa
//					String url="jdbc:postgresql://"+Global.PostgresServer+":"+Global.PostgresPort+"/postgres";
//				    Connection conn = DriverManager.getConnection(url, Global.PostgresUser, Global.PostgresPw);
					JDBCAdapter db = new JDBCAdapter(DBUtils.makeNewConnection("postgres"));
					
				   //Finalizo Conexiones	
					DBUtils.finalizar_conexiones_Activas(dbname, db);
					
				   //Elimino BD
					DBUtils.Eliminar_BD(dbname, db);
					
				  //Creo BD nueva
					DBUtils.Crear_Nueva_BD(dbname, db);
					
					Funciones_Utiles.RestaurarBD(dbname, path);
					reportStep();
					
				} catch (Exception e) {
					e.printStackTrace();
					setCanceled(true);
					reportStep();
				}
				
//			cwp=SingleVectorialDBConnectionManager.instance()
//			.getConnection(Global.gvSIGPostgresSQLDriverName, 
//					Global.PostgresUser, 
//					Global.PostgresPw,	
//					dbname, 
//					Global.PostgresServer, 
//					Global.PostgresPort, 
//					dbname, 
//					true);
//			
//			//reproyectar, si es necesario, la nueva base de datos que por defecto es cuba norte(2085)
//			String query = "SELECT srid FROM geometry_columns WHERE f_table_name='cuencas'";
//			
//			Connection conn=null;
//
//			try {
//				conn = java.sql.DriverManager.getConnection(cwp.getConnectionStr(),
//						cwp.getUser(),
//						cwp.getPw());
//			}
//			catch (SQLException e) 
//			{
//				e.printStackTrace();
//				JOptionPane.showMessageDialog(null, PluginServices.getText(this, "server_connection_error"));
//				if(isCanceled())
//					return;
//			}
				
//			Statement st=conn.createStatement();
//			ResultSet rs=st.executeQuery(query);
//			String current_srid=null;
//			
//			if(rs.last())
//			{
//				int i=rs.getInt(1);
//				current_srid=String.valueOf(i);
//			}
//			
//			if(!current_srid.equals(srid))
//			{
//				query="SELECT f_table_name,f_geometry_column FROM geometry_columns";
//				Statement st2=conn.createStatement();
//				rs=st2.executeQuery(query);
//				String s;
//				while(rs.next())
//				{
//					if(isCanceled())
//					{
//						conn.close();
//						return;
//					}
//					try
//					{
//						s="SELECT ST_Transform("+rs.getString(2)+","+srid+") FROM "+rs.getString(1);
//						st.execute(s);
//						
//						s="SELECT updategeometrysrid('"+rs.getString(1)+"','"+rs.getString(2)+"',"+srid+")";
//						st.execute(s);
//					}
//					catch(SQLException se)
//					{
//						se.printStackTrace();
//					}
//				}    				
//			}
			
		/*if(al!=null)
		{
			al.actionPerformed(null);
		}    */		
	}
	
//	private static boolean dbExists() throws SQLException, DBException
//	{
//     boolean exists = false;
//     
//	 JDBCAdapter db = new JDBCAdapter("jdbc:postgresql://"+Global.PostgresServer+":"+Global.PostgresPort+"/postgres", Global.PostgresSQLDriverName, Global.PostgresUser, Global.PostgresPw);
//	 db.executeQuery("SELECT datname FROM pg_database");
//	 
//	 for(int i=0; i<db.getRowCount(); i++)
//	 {	 
//	  System.out.println(db.getValueAt(i, 0).toString());	 
//	  if(db.getValueAt(i, 0).toString().equals(Global.postgresDBName))
//	   exists = true;
//	 }
//	 
//	 db.close();
//	 
//	 return exists;
//	}
	
	public void finished() 
	{
	 if(!isCanceled())
	 {	 
		 JOptionPane.showMessageDialog(null, "Base de datos restaurada", "Información", JOptionPane.INFORMATION_MESSAGE);
		    
			try {
				Connection conn = DBUtils.makeNewConnection(dbname);
				ConnectionExt.setConexion(conn);
				
				TrazasManager.insertar_Trazas(oldtrazas);
				UsuarioManager.setusuario(usuario);
				TrazasManager.insertar_Traza("Restauró la base de datos > "+dbname);
				UsuarioManager.setusuario(null);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
	 }	
	}
}
