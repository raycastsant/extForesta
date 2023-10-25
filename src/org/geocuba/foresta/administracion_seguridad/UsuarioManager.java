package org.geocuba.foresta.administracion_seguridad;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.administracion_seguridad.gui.dialogoUsuariosManager;
import org.geocuba.foresta.gestion_datos.IPersistenObjectManager;
import org.geocuba.foresta.gestion_datos.PersistentObject;

public class UsuarioManager implements IPersistenObjectManager{
	
	private static Usuario  usuario;
	public static final int NIVEL_ADMINISTRADOR = 1;   
	public static final int NIVEL_MEDIO = 2;
	//public static final int NIVEL_BAJO = 3;  
	
	@Override
	public PersistentObject Cargar_Objeto_BD(int id) //throws ReadDriverException 
	{
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 String sql = "select nombre, nivel, password, nombre_apellidos from usuarios inner join " +
		 		"tipos_usuario on usuarios.tipo_usuario=tipos_usuario.id where usuarios.id="+id;
		 db.ejecutarConsulta(sql);
		 
		 if(db.isEmpty())
		  return null;
		 
		 String nombre = db.getValueAsString(0,0);
		 int nivel = db.getValueAsInteger(0,1);
		 String pass = db.getValueAsString(0,2);
		 
		 Object value = db.getValueAt(0,3);
	     String nomb_apell = "";
	     if(value!=null)
	       nomb_apell = value.toString();
		 
		 Usuario usuario = new Usuario(id, nivel, nombre, pass, nomb_apell, false);
		 
		 return usuario;
	}
	
	public static void control_usuario()
	{
	 dialogoUsuariosManager.showDialogoUsuarios();	
	}
	
	public static boolean VerificarUsuario(String nombre, String pass)
	{
	 boolean exist = false;
	 
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 db.ejecutarConsulta("select usuarios.id, tipos_usuario.nivel, usuarios.nombre_apellidos from usuarios inner join tipos_usuario " +
	 		"on usuarios.tipo_usuario=tipos_usuario.id where usuarios.nombre='"+nombre+"' and usuarios.password='"+pass+"'");
	 
	 if(!db.isEmpty())
	 {
      exist = true;
      int id = Integer.parseInt(db.getValueAt(0,0).toString());
      int nivel = Integer.parseInt(db.getValueAt(0,1).toString());
      
      Object value = db.getValueAt(0,2);
      String nomb_apell = "";
      if(value!=null)
       nomb_apell = value.toString();	  
      
      usuario = new Usuario(id, nivel, nombre, pass, nomb_apell, false);
	 }	 
	 
	 return exist;
	}
	
	public static Usuario Usuario()
	{
	 return usuario;	
	}
	
	public static void setusuario(Usuario user)
	{
	 usuario = user;	
	}
	
	public static boolean usuarioExiste(String nuevoNombre)
	{
     JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 db.ejecutarConsulta("select * from usuarios where nombre='"+nuevoNombre+"'");
	 
	 return !db.isEmpty();
	}
	
	 public static boolean quedan_Administradores_BD(int idUsuarioExcluir, JDBCAdapter db) 
	 {
		    if(db == null)
			 db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		    
		    db.ejecutarConsulta("select usuarios.id from usuarios inner join tipos_usuario on usuarios.tipo_usuario=tipos_usuario.id" +
	           		" where tipos_usuario.nivel=1 and usuarios.id<>"+idUsuarioExcluir); 
		    
		    return !db.isEmpty();
	 }
	 
	 public static String getQueryTabla() 
	 {
		 return "select usuarios.id, nombre, nombre_apellidos, nivel, descripcion as descripción " +
	 		"from usuarios inner join tipos_usuario on usuarios.tipo_usuario=tipos_usuario.id";
	 }
}
