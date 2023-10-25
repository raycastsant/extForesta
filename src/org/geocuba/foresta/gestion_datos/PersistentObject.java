package org.geocuba.foresta.gestion_datos;

public abstract class PersistentObject {

	protected boolean isNewObject;  // Para identificar si se hace update o insert
	protected boolean isEditing;
	
	//protected JDBCAdapter db;
	
	//public PersistentObject() throws SQLException
///	{
	 //db = new JDBCAdapter(Global.CreateConnection(dbName));	
	//}
	
	public PersistentObject(boolean _isNewObject)
	{
	 isNewObject = _isNewObject;
	 isEditing = false;
	}
	
	public abstract boolean save();
	
	public abstract boolean delete();
	
	public void setIsNewObject(boolean value)
	{
	 isNewObject = value;	
	 isEditing = false;
	}
	
	public void setEditing(boolean value)
	{
	 isEditing = value;
	}
	
	public boolean isEditing()
	{
	 return isEditing;
	}
	
	public boolean IsNewObject()
	{
	 return isNewObject;	
	}
	
	//public boolean update();
	
}
