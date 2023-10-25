package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.IPersistenObjectManager;

public class EspecieManager implements IPersistenObjectManager{

	@Override
	public Especie Cargar_Objeto_BD(int id)// throws ReadDriverException 
	{
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 String sql = "select siglas, ncientifico, ncomun from especies where id="+id;
		 db.ejecutarConsulta(sql);
		 
		 if(db.isEmpty())
		  return null;
		 String siglas = db.getValueAsString(0,0);
		 String ncientifico = db.getValueAsString(0,1);
		 String ncomun = db.getValueAsString(0,2);
		 
		 Especie especie = new Especie(id, siglas, ncientifico, ncomun, false);
		 
		 return especie;
	}
	
	public Especie []Cargar_Especies(String sqlFrom_Where)
	{
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 String sql = "select distinct especies.id, siglas, ncientifico, ncomun "+sqlFrom_Where;
		 System.out.println(sql);
		 db.ejecutarConsulta(sql);
		 
		 if(db.isEmpty())
		  return null;
		 
		 if(db.getColumnCount()!=4)
	      return null;
		 
		 Especie []lista = new Especie[db.getRowCount()];
		 for(int i=0; i<db.getRowCount(); i++)
		 {
			 int id = db.getValueAsInteger(i,0);
			 
			 Object value = db.getValueAt(i,1); 
			 String siglas = "";
			 if(value!=null)
			  siglas = value.toString();
			 
			 value = db.getValueAt(i,2); 
			 String ncientifico = "";
			 if(value!=null)
			  ncientifico = value.toString();
			 
			 value = db.getValueAt(i,3); 
			 String ncomun = "";
			 if(value!=null)
			  ncomun = value.toString();
			 
			 lista[i] = new Especie(id, siglas, ncientifico, ncomun, false);
		 }	
		 
		 
		 return lista;
	}
}
