package org.geocuba.foresta.gestion_datos.gui;

import org.geocuba.foresta.gestion_datos.PersistentObject;

public interface IPanelManager {

	public void MostrarPanel(PersistentObject persistent);
	
	public void HabilitarGuardado();
	
	public void DeshabilitarComponentes();
}
