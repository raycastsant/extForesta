package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;

public class Red_drenajeManager implements IPersistenObjectManager 
{
	@Override
	public Red_drenaje Cargar_Objeto_BD(int gid) //throws ReadDriverException 
	{
		Red_drenaje reddrenaje = null;
		JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		db.ejecutarConsulta("select orden, siguiente, parteaguas from red_drenaje where gid="+gid);
		
		if(!db.isEmpty())
		{
		 int orden = db.getValueAsInteger(0,0);
		 int sigte = db.getValueAsInteger(0,1);
		 
		 Object parteaguas = db.getValueAt(0,2);
		 Parteaguas p = null;
		 if(parteaguas != null)
		  p = new ParteaguasManager().Cargar_Objeto_BD(Integer.parseInt(parteaguas.toString()));	 
		 
		 reddrenaje = new Red_drenaje(gid, p, orden, sigte, false);
		}		
		
		return reddrenaje;
	}
	
}
