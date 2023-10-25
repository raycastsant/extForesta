package org.geocuba.foresta.gestion_datos;

public abstract class Hidrografia extends PersistentGeometricObject {
	
    public Hidrografia(boolean isNewObject) {
		super(isNewObject);
	}

	protected Integer idHidrografia;
    protected Tipo_hidrografia Tipo_hidrografia;
    protected Parteaguas parteaguas;  //puede o no tenerlo
    protected String nombre;
    protected Cuenca cuenca;
    
//	private Lineal Lineal;
//	private Areal Areal;
//	private Fajas Fajas;
	
    protected void setIdHidrografia(int value) {
		this.idHidrografia = value;
	}
	
    protected int getIdHidrografia() {
		return idHidrografia;
	}
	
    public void setNombre(String value) {
		this.nombre = value;
	}
	
    public String getNombre() {
		return nombre;
	}
	
    public void setCuenca(Cuenca value) {
		/*if (cuenca != null) {
			cuenca.removeHidrografia(this);
		}
		if (value != null) {
			value.addHidrografia(this);
		}*/
    	this.cuenca = value; 
	}
	
    public Cuenca getCuenca() {
		return cuenca;
	}
    
    public void setTipo_hidrografia(Tipo_hidrografia value) {
		/*if (this.Tipo_hidrografia != value) 
		{
			Tipo_hidrografia lTipo_hidrografia = this.Tipo_hidrografia;*/
			this.Tipo_hidrografia = value;
		/*	if (value != null) 
			 Tipo_hidrografia.setHidrografia(this);
			else 
			 lTipo_hidrografia.setHidrografia(null);
		}*/
	}
	
    public Tipo_hidrografia getTipo_hidrografia() {
		return Tipo_hidrografia;
	}
	
    public void setParteaguasg(Parteaguas value) {
		/*if (parteaguas != null) {
			parteaguas.hidrografia.remove(this);
		}
		if (value != null) {
			value.hidrografia.add(this);
		}*/
    	this.parteaguas = value;
	}
	
    public Parteaguas getParteaguas() {
		return parteaguas;
	}
	
}
