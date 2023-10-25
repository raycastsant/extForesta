package org.geocuba.foresta.administracion_seguridad.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import org.geocuba.foresta.administracion_seguridad.TrazasManager;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.drivers.DBException;
import com.iver.utiles.swing.threads.AbstractMonitorableTask;

public class SalvarBD extends AbstractMonitorableTask
{
	private String dbname;
	private String path;
	
	public SalvarBD(String _dbname, String _path)
	{
		this.dbname = _dbname;
		this.path = _path;
		
		setInitialStep(0);
		setDeterminatedProcess(false);
		setStatusMessage(PluginServices.getText(this, "Salvando base de datos:'"+dbname+"'"));
		setFinalStep(1);
	}
	
	public void run() throws SQLException, DBException
	{
				try 
				{
					Funciones_Utiles.SalvarBD(dbname, path);
					reportStep();
					
				} catch (Exception e) {
					e.printStackTrace();
					setCanceled(true);
					reportStep();
				}
	}
	
	public void finished() 
	{
	 JOptionPane.showMessageDialog(null, "Base de datos guardada correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
	 TrazasManager.insertar_Traza("Realizó una salva de la base de datos");
	}
}
