package org.geocuba.foresta.gestion_datos;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.IPersistenObjectManager;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;

public class Proyecto_ReforestacionManager// implements IPersistenObjectManager  
{
	//@Override
	public Proyecto_Reforestacion Cargar_Objeto_BD(int id) throws ReadDriverException 
	{
		 Proyecto_Reforestacion proyecto = null;
		 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		 String sql = "select tenente, area_tenente, area_reforestar, nombre_productor, " +
	 		"organismo, nombre_lugar, tipo_erosion_suelo, grado_erosion_suelo, profundidad_efectiva," +
	 		" ph, materiaorganica, vegetacion, densidad, observaciones_veg, marcox, marcoy, hileras, " +
	 		"precipitacion, metodo_plantacion, proyecto_general, especificacion, medidas_suelos, " +
	 		"medidas_pastoreo, medidas_incendios, faja, nombre_tenente from proyecto where id="+id;
		 db.ejecutarConsulta(sql);
		 System.out.println(sql);
		 
		 if(!db.isEmpty())
		 {
		     boolean istenente = db.getValueAsBoolean(0,0);
			 Double area_tenente = db.getValueAsDouble(0,1);
			 Double area_reforestar = db.getValueAsDouble(0,2);
			 
			 Object value = db.getValueAt(0,3);
			 String nombre_productor = "";
			 if(value !=null)
			  nombre_productor = value.toString();
			 
			 value = db.getValueAt(0,4);
			 String organismo = "";
			 if(value !=null)
			  organismo = value.toString();
			 
			 value = db.getValueAt(0,5);
			 String nombre_lugar = "";
			 if(value !=null)
				 nombre_lugar = value.toString();
			 
			 value = db.getValueAt(0,6);
			 String tipo_erosion_suelo = "";
			 if(value !=null)
				 tipo_erosion_suelo = value.toString();
			 
			 value = db.getValueAt(0,7);
			 String grado_erosion_suelo = "";
			 if(value !=null)
				 grado_erosion_suelo = value.toString();
			 
			 Double profundidad_efectiva = db.getValueAsDouble(0,8);
			 Double ph = db.getValueAsDouble(0,9);
			 Double materiaorganica = db.getValueAsDouble(0,10);
			 
			 value = db.getValueAt(0,11);
			 String vegetacion = "";
			 if(value !=null)
				 vegetacion = value.toString();
			 
			 value = db.getValueAt(0,12);
			 String densidad = "";
			 if(value !=null)
				 densidad = value.toString();
			 
			 value = db.getValueAt(0,13);
			 String observaciones_veg = "";
			 if(value !=null)
				 observaciones_veg = value.toString();
			 
			 Double marcox = db.getValueAsDouble(0,14);
			 Double marcoy = db.getValueAsDouble(0,15);
			 Integer hileras = db.getValueAsInteger(0,16);
			 Double precipitacion = db.getValueAsDouble(0,17);
			 
			 value = db.getValueAt(0,18);
			 String metodo_plantacion = "";
			 if(value !=null)
				 metodo_plantacion = value.toString();
			 
			 Boolean proyecto_general = db.getValueAsBoolean(0,19);
			 
			 value = db.getValueAt(0,20);
			 String especificacion = "";
			 if(value !=null)
				 especificacion = value.toString();
			 
			 value = db.getValueAt(0,21);
			 String medidas_suelos = "";
			 if(value !=null)
				 medidas_suelos = value.toString();
			 
			 value = db.getValueAt(0,22);
			 String medidas_pastoreo = "";
			 if(value !=null)
				 medidas_pastoreo = value.toString();
			 
			 value = db.getValueAt(0,23);
			 String medidas_incendios = "";
			 if(value !=null)
				 medidas_incendios = value.toString();
			 
			 int idfaja = db.getValueAsInteger(0,24);
			 FajaManager fm = new FajaManager();
			 Faja faja = fm.Cargar_Objeto_BD(idfaja);
			 
			 value = db.getValueAt(0,25);
			 String nombre_tenente = "";
			 if(value !=null)
				 nombre_tenente = value.toString();
			 
			 EspecieManager espman = new EspecieManager();
			 Especie []especies = espman.Cargar_Especies("from especies inner join proyecto_especies on " +
		              "especies.siglas=proyecto_especies.especie where proyecto=" + id +
		              " order by siglas, especies.id, ncientifico, ncomun");
			 
			 Tipo_suelo []listaSuelos = Tipo_sueloManager.get_Tipos_suelos("from tipo_suelo inner join proyecto_suelos " +
			 		"on tipo_suelo.id=proyecto_suelos.suelo  where proyecto=" + id +
				        " order by id, clave, tipo");
		  
			 proyecto = new Proyecto_Reforestacion(id, istenente, area_tenente, area_reforestar, nombre_productor,
					 organismo, nombre_lugar, tipo_erosion_suelo, grado_erosion_suelo, profundidad_efectiva, ph,
					 materiaorganica, vegetacion, densidad, observaciones_veg, marcox, marcoy, hileras, 
					 precipitacion, metodo_plantacion, proyecto_general, especificacion, medidas_suelos, medidas_pastoreo,
					 medidas_incendios, faja, nombre_tenente, especies, listaSuelos, false);
		 }
		 
		 return proyecto;
	}
}
