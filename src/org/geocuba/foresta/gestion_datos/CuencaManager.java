package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;

public class CuencaManager implements IPersistenObjectManager 
{
	/**Obtiene las cuencas con la lista de municipios en NULL*/
	public Cuenca[] get_Cuencas_Sin_Municipios() 
	{
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva()); 
	 db.ejecutarConsulta("select gid, nombre from cuencas");
	 if(db.isEmpty())
	  return null;
	 
	 int rowcount = db.getRowCount();
	 Cuenca []list = new Cuenca[rowcount];
	 for(int i=0; i<rowcount; i++)
	 {
	  list[i] = new Cuenca(Integer.parseInt(db.getValueAt(i,0).toString()), db.getValueAt(i,1).toString(), false);	 
	 }
	 
	 return list;
	}	
	
	@Override
	public Cuenca Cargar_Objeto_BD(int gid) //throws ReadDriverException 
	{
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva()); 
		 db.ejecutarConsulta("select nombre from cuencas where gid="+gid);
		 if(db.isEmpty())
		  return null;
		 
		 Cuenca cuenca = new Cuenca(gid, db.getValueAt(0,0).toString(), false);	 
		 
		 return cuenca;
	}
	
	public boolean eliminarCuencas()
	{
		 Cuenca []cuencas = get_Cuencas_Sin_Municipios();
		 if(cuencas!=null)
		 {
			 for(int i=0; i<cuencas.length; i++)
			  cuencas[i].delete();
		 }
		 
		 return cuencas!=null;
	}
	
	/*
	public static Cuenca[] listCuencasByQuery(String condition, String orderBy) {
		return null;
	}
	
	public static Cuenca loadCuencasByQuery(String condition, String orderBy) {
		return null;
	}
	
	public static Cuenca createCuenca() {
		return new Cuenca();
	}*/
}
