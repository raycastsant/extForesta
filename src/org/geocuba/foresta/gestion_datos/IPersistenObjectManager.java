package org.geocuba.foresta.gestion_datos;

//import com.hardcode.gdbms.driver.exceptions.ReadDriverException;

public interface IPersistenObjectManager {

	public PersistentObject Cargar_Objeto_BD(int id); //throws ReadDriverException;
	
}
