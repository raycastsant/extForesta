package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;

public class Faja_realManager implements IPersistenObjectManager 
{
	@Override
	public Faja_real Cargar_Objeto_BD(int gid)// throws ReadDriverException 
	{
		 Faja_real fajareal = null;		
	     JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	     db.ejecutarConsulta("select gid,faja from fajas_real where gid="+gid);
	     
	     if(!db.isEmpty())
	     {
	      int idFaja = db.getValueAsInteger(0,1);
	      FajaManager fajaman = new FajaManager();
	      Faja faja = fajaman.Cargar_Objeto_BD(idFaja);
	      
	      fajareal = new Faja_real(gid, faja, false);
	     } 	 
	     
	     return fajareal;
	}
}
