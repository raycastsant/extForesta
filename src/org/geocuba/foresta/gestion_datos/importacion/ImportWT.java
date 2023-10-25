package org.geocuba.foresta.gestion_datos.importacion;

import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import javax.swing.JOptionPane;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.Cuenca;
import org.geocuba.foresta.gestion_datos.Embalse;
import org.geocuba.foresta.gestion_datos.Parcela;
import org.geocuba.foresta.gestion_datos.Parteaguas;
import org.geocuba.foresta.gestion_datos.PersistentGeometricObject;
import org.geocuba.foresta.gestion_datos.Relieve;
import org.geocuba.foresta.gestion_datos.Rio;
import org.geocuba.foresta.gestion_datos.Suelo;
import org.geocuba.foresta.gestion_datos.Textura_suelos;
import org.geocuba.foresta.gestion_datos.Textura_suelosManager;
import org.geocuba.foresta.gestion_datos.Tipo_hidrografia;
import org.geocuba.foresta.gestion_datos.Tipo_hidrografiaManager;
import org.geocuba.foresta.gestion_datos.Tipo_suelo;
import org.geocuba.foresta.gestion_datos.Tipo_sueloManager;
import org.geocuba.foresta.gestion_datos.Uso_catastro;
import org.geocuba.foresta.gestion_datos.Uso_catastroManager;
import org.geocuba.foresta.gestion_datos.Velocidad_infiltracionManager;
import org.geocuba.foresta.gestion_datos.importacion.gui.pAdvancedImport;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.utiles.swing.threads.AbstractMonitorableTask;

/**Clase para controlar mediante un thread*/
public class ImportWT extends AbstractMonitorableTask
{
 private HashMap _links;
 //private IProjection _projection;
 private JDBCAdapter _db;
 private FLyrVect _layer;
 private Object [] _fields;
 private ReadableVectorial va;
 private int rowCount;
 private String fieldsOrg;     //, tempfieldsOrg;
 private String table;
 private boolean canceled;
 
 private HashMap<String,String> tipohidrolinkValues;  //para almacenar los valores de tipo de hidrografia que el usuario selecciona del fichero
 private Tipo_hidrografia[] tipoHidroValues;
 private Object [] tipoHidroDesc;
 private HashMap<String,Integer> texturalinkValues;  //para almacenar los valores de textura que el usuario selecciona del fichero
 private Textura_suelos[] texturaValues;
 private Object [] texturaDesc;
 private HashMap<String,Integer> tipoSuelolinkValues;  //para almacenar los valores de tipo de suelo que el usuario selecciona del fichero
 private Tipo_suelo[] tipoSueloValues;
 private Object [] tipoSueloDesc;
 private HashMap<String,String> estructuraSuelolinkValues;  //para almacenar los valores de la estructura de los suelos
 //private Velocidad_infiltracion[] estructuraSueloValues;
 private Object [] estructuraSueloDesc;
 //private Cuenca cuenca;
 private HashMap<String,Integer> usosParcelaslinkValues;  //para almacenar los valores de los tipos de usos del catastro
 private Uso_catastro[] usosParcelasValues;
 private Object [] usosParcelasDesc;
 
 public ImportWT(HashMap<String,String> links/*, JDBCAdapter db*/, FLayer layer, Object []fields)throws ReadDriverException
 {
	 _links = links;
	  //_projection = projection;
	  _db = new JDBCAdapter(ConnectionExt.getConexionActiva());   //db;
	  _layer = (FLyrVect)layer;
	  _fields = new String[fields.length-1];//fields
		
	  va = _layer.getSource();
	  rowCount = va.getShapeCount();	
	  table = getTablename(_layer.getName());
	  canceled = false;
      
	  //===========================================================================================
	  if(table.equals(pAdvancedImport.RIOS) || table.equals(pAdvancedImport.EMBALSES))
	  {	  
	   tipohidrolinkValues = new HashMap<String,String>();
	   
	  //LLenando arreglo de Tipos de Hidrografia
	   /*Esto lo puedo hacer porque los tipos de elementos equivalen a los nombres de las tablas "rios" y 
	    * "embalses" en singular*/
	   Tipo_hidrografiaManager tipoman = new Tipo_hidrografiaManager();
	   tipoHidroValues = tipoman.get_Tipos_hidrografia("where tipo_elemento='"+table.substring(0, table.length()-1)+"'");  
	   tipoHidroDesc = new String[tipoHidroValues.length];
	   for(int i=0; i<tipoHidroValues.length; i++)
		tipoHidroDesc[i] = new String(tipoHidroValues[i].getDescripcion());
	   
	  // _db.executeQuery("update geometry_columns set srid=2085 where f_table_name='rios'");
	  } 
	  else  //===========================================================================================
	  if(table.equals(pAdvancedImport.SUELOS))
	  {	  
	   texturalinkValues = new HashMap<String,Integer>();
	   tipoSuelolinkValues = new HashMap<String,Integer>();
	   estructuraSuelolinkValues = new HashMap<String,String>();
	   
	  //LLenando arreglo de Tipos de Textura
	   texturaValues = Textura_suelosManager.get_Texturas_suelos();  
	   texturaDesc = new String[texturaValues.length];
	   for(int i=0; i<texturaValues.length; i++)
		texturaDesc[i] = new String(texturaValues[i].getTextura());
	   
	  //LLenando arreglo de Tipos de Suelo
	   tipoSueloValues = Tipo_sueloManager.get_Tipos_suelos();  
	   tipoSueloDesc = new String[tipoSueloValues.length];
	   for(int i=0; i<tipoSueloValues.length; i++)
		tipoSueloDesc[i] = new String(tipoSueloValues[i].getTipo());
	   
	  //LLenando arreglo de Estructura de suelos
	   estructuraSueloDesc = Velocidad_infiltracionManager.Obtener_Estructuras();
	  }
	  else  //===========================================================================================
	  if(table.equals(pAdvancedImport.PARCELAS))
	  {	  
	   usosParcelaslinkValues = new HashMap<String,Integer>();
	   usosParcelasValues = Uso_catastroManager.Obtener_usos_catastro(); 
	   usosParcelasDesc = new String[usosParcelasValues.length];
	   for(int i=0; i<usosParcelasValues.length; i++)
		usosParcelasDesc[i] = new String(usosParcelasValues[i].getDescripcion_uso());
      }
		  	  
	  
  setInitialStep(1);
  setDeterminatedProcess(true);
  setNote("Recopilando información de "+table);
  setFinalStep(rowCount*2);	 
	 
  fieldsOrg = "";
  int k = 0;
  for(int i=0; i<fields.length; i++)
  {
   fieldsOrg += fields[i]+",";
   //System.out.println(fieldsOrg);
   if(fields[i].equals("the_geom"))
	continue;
   
   _fields[k] = fields[i];
   k++;
   //tempfieldsOrg += fields[i]+",";
  }	  
  
  fieldsOrg = fieldsOrg.substring(0, fieldsOrg.length()-1);
  //tempfieldsOrg += "the_geom"; 
 }
	 
 public void run() throws Exception 
 {
   SelectableDataSource sds = _layer.getRecordset();
   //setNote("Importando hacia "+table);
   PersistentGeometricObject []geometricObject = new PersistentGeometricObject[rowCount];
   
  //Recopilando datos
   for (int i = 0; i < rowCount && !isCanceled(); i++)
   {
	setNote("Fase 1: Recopilando datos al "+((i*100)/rowCount)+"%"); 
	//setNote("Importando elemento "+i+" de "+rowCount);    
	   
	int type = va.getShapeType();
	
	if(table.equals(pAdvancedImport.CUENCAS))
     geometricObject[i] = getCuenca(sds, i);	
	else	
	if(table.equals(pAdvancedImport.RIOS))
	 geometricObject[i] = getRio(sds, i);
	else	
    if(table.equals(pAdvancedImport.EMBALSES))
	 geometricObject[i] = getEmbalse(sds, i );
    else	
    if(table.equals(pAdvancedImport.PARCELAS))
     geometricObject[i] = getParcela(sds, i);
    else	
    if(table.equals(pAdvancedImport.SUELOS))
     geometricObject[i] = getSuelo(sds, i);
    else	
    if(table.equals(pAdvancedImport.RELIEVE))
     geometricObject[i] = getRelieve(sds, i);
    else	
    if(table.equals(pAdvancedImport.PARTEAGUAS))
     geometricObject[i] = getParteaguas(sds, i);
	
	if(geometricObject != null)
	{
	 if(geometricObject[i] != null)	
	  geometricObject[i].setGeometry(va.getFeature(i).getGeometry(), type);  //setFeature(va.getFeature(i));
	 else
	 if(canceled)
	 {
	  setCanceled(true);
	  setFinalStep(getCurrentStep());
	  reportStep();
	  return;
	 }
		 
	 //geometricObject[i].setType(type);
	 //geometricObject.save();
	}
	
	reportStep();
   }
   
  //Importando Objetos 
   for (int i=0; i<geometricObject.length && !isCanceled(); i++)
   {
	if(!isCanceled())
	{	
		setNote("Fase 2: Importando hacia la tabla '"+table+"' al "+((i*100)/rowCount)+"%");
		geometricObject[i].save();
		reportStep();
	}
	else
	{
		  setFinalStep(getCurrentStep());
		  reportStep();
		  return;
	}	
   }	
  
   reportStep();
 }
	
  public void finished() 
  {
   if(!this.isCanceled())	  
	JOptionPane.showMessageDialog(null, "Importación terminada correctamente");
   else
	JOptionPane.showMessageDialog(null, "Operación cancelada por el usuario");
  }

 /**Obtener el nombre de la tabla relacionada 
  * a la capa*/
 private String getTablename(String layerName)
 {
  if(layerName.equals("Hidrografía_lineal"))
   return "rios";
  else
  if(layerName.equals("Hidrografía_areal"))
   return "embalses";
  else
   return layerName;	   
 }
 
 private Value getEmptyValue(String table, String field) throws SQLException
 {
	 _db.ejecutarConsulta("select * from "+table+" limit 1");	 
	   System.out.println("Campo vacío :"+field);
	   
	   int type = _db.getColumnType((String)field);	 
	   if((type == Types.INTEGER) || (type == Types.DOUBLE) || (type == Types.FLOAT))
	    return ValueFactory.createValue(0);
	   else
	   if(type == Types.VARCHAR)
	    return ValueFactory.createValue("");
	   else
	   if(type == Types.BOOLEAN)
		return ValueFactory.createValue(false);
	   else
		System.out.println(type);
	   
	   return null;
 }
 
 private String get_Clasification_Option_panel(Object []desc, Value val, String field)
 {
  return (String) JOptionPane.showInputDialog(null, "Seleccione una clasificación para el valor '"+val.toString()+"' del campo '"+field+"'", "Información", 
					JOptionPane.QUESTION_MESSAGE, null, desc, desc[0]); 
 }
 
 private Value getCurrentValue(String fvalue, String field, SelectableDataSource sds, int index) throws SQLException, ReadDriverException
 {
  Value val = null;
  
  if(fvalue.equals("Vacío"))  //Un campo que no se encuentra en el fichero
   val = getEmptyValue(table, field);    
  else
   val = sds.getFieldValue(index, sds.getFieldIndexByName(fvalue));  
  
  return val;
 }
 
 /**Devuelve un Objeto Rio
 * @throws SQLException 
 * @throws ReadDriverException */
 private PersistentGeometricObject getRio(SelectableDataSource sds, int index) throws SQLException, ReadDriverException
 {
  Rio rio = new Rio();
  
  for (int k=0; k<_fields.length; k++)
  {
   String field = _fields[k].toString();	  
   String fvalue = _links.get(field).toString(); //nombre del campo 	
   Value val = null;		 
   
   if(fvalue.equals("Vacío"))  //Un campo que no se encuentra en el fichero
    val = getEmptyValue(table, field);    
   else
	val = sds.getFieldValue(index, sds.getFieldIndexByName(fvalue));     //ValueFactory.createValue(sds.getFieldValue(i, sds.getFieldIndexByName(_links.get(_fields[k]).toString())));

   if(field.equals("nombre"))
	rio.setNombre(val.toString());
   else
   if(field.equals("ancho"))
	rio.setAncho(Double.parseDouble(val.toString()));
   else
   if(field.equals("orden"))
    rio.setOrden(Integer.parseInt(val.toString()));
   else
   if(field.equals("tipo_hidrografia"))
   {
	/*Si el valor del tipo de hidrografia del fichero no ha sido clasificado
	 * hay que ponerle uno de los tipos de hidrografia existentes*/   
	if(tipohidrolinkValues.get(val.toString()) == null)
	{
	 //Primero se verifica si el valor pertenece a algun nomenclador	
	  boolean encontrado = false;	
	  for(int j=0; j<tipoHidroValues.length; j++)
	  {
	   if(tipoHidroValues[j].getDescripcion().equals(val.toString()))
	   {
		tipohidrolinkValues.put(val.toString(), tipoHidroValues[j].getCodigo());
		rio.setTipo_hidrografia(tipoHidroValues[j]);
		encontrado = true;
		break;
	   }	  
	  }
	//si no pertenece a ningun nomenclador hay que clasificarlo
	 if(encontrado == false)  
	 {	 
	  String selected = get_Clasification_Option_panel(tipoHidroDesc, val, field);
	  
	     if(selected == null)
	     {	 
	      canceled = true;
	      return null;
	     } 
	 
	   for(int j=0; j<tipoHidroValues.length; j++)
	   {
	    if(tipoHidroValues[j].getDescripcion().equals(selected))
	    {
		 tipohidrolinkValues.put(val.toString(), tipoHidroValues[j].getCodigo());
		 rio.setTipo_hidrografia(tipoHidroValues[j]);
		 break;
	    }	  
	   }
	 }  
	}	
	else  //El valor del tipo de hidrografia ya esta clasificado
	{
		   String codigoClasificado = tipohidrolinkValues.get(val.toString());
		   for(int j=0; j<tipoHidroValues.length; j++)
		   {
		    if(tipoHidroValues[j].getCodigo().equals(codigoClasificado))   //busco el codigo asignado para ese valor
		    {
			 rio.setTipo_hidrografia(tipoHidroValues[j]);
			 break;
		    }	  
		   }	
	}	
   }
  }
  //rio.setProjection(_projection);
  
  return rio;
 }
 
 /**Devuelve un Objeto Embalse
  * @throws SQLException 
  * @throws ReadDriverException */
  private PersistentGeometricObject getEmbalse(SelectableDataSource sds, int index) throws SQLException, ReadDriverException
  {
   Embalse embalse = new Embalse();
   
   for (int k=0; k<_fields.length; k++)
   {
    String field = _fields[k].toString();	  
    String fvalue = _links.get(field).toString(); //nombre del campo 	
    Value val = null;		 
    
    if(fvalue.equals("Vacío"))  //Un campo que no se encuentra en el fichero
     val = getEmptyValue(table, field);    
    else
 	val = sds.getFieldValue(index, sds.getFieldIndexByName(fvalue));     //ValueFactory.createValue(sds.getFieldValue(i, sds.getFieldIndexByName(_links.get(_fields[k]).toString())));

    if(field.equals("nombre"))
 	 embalse.setNombre(val.toString());
    else
    if(field.equals("uso"))
     embalse.setUso(val.toString());
    else
    if(field.equals("naturaleza"))
     embalse.setNaturaleza(val.toString());
    else
    if(field.equals("volumen"))
     embalse.setVolumen(Double.parseDouble(val.toString()));
    else
    if(field.equals("nan"))
     embalse.setNan(Double.parseDouble(val.toString()));
    else
    if(field.equals("nam"))
     embalse.setNam(Double.parseDouble(val.toString()));
    else
    if(field.equals("tipo_hidrografia"))
    {
 	/*Si el valor del tipo de hidrografia del fichero no ha sido clasificado
 	 * hay que ponerle uno de los tipos de hidrografia existentes*/   
 	if(tipohidrolinkValues.get(val.toString()) == null)
 	{
 		//Primero se verifica si el valor pertenece a algun nomenclador	
 		  boolean encontrado = false;	
 		  for(int j=0; j<tipoHidroValues.length; j++)
 		  {
 		   if(tipoHidroValues[j].getDescripcion().equals(val.toString()))
 		   {
 			tipohidrolinkValues.put(val.toString(), tipoHidroValues[j].getCodigo());
 			embalse.setTipo_hidrografia(tipoHidroValues[j]);
 			encontrado = true;
 			break;
 		   }	  
 		  }
 		//si no pertenece a ningun nomenclador hay que clasificarlo
 		 if(encontrado == false)  
 		 {
 	      String selected = get_Clasification_Option_panel(tipoHidroDesc, val, field);
 	      
 	     if(selected == null)
	     {	 
	      canceled = true;
	      return null;
	     } 
 	 
 	      for(int j=0; j<tipoHidroValues.length; j++)
 	      {
 	       if(tipoHidroValues[j].getDescripcion().equals(selected))
 	       {
 		    tipohidrolinkValues.put(val.toString(), tipoHidroValues[j].getCodigo());
 		    embalse.setTipo_hidrografia(tipoHidroValues[j]);
 		    break;
 	       }	  
 	      }	 
 	     } 
 	}	
 	else  //El valor del tipo de hidrografia ya esta clasificado
 	{
 		   String codigoClasificado = tipohidrolinkValues.get(val.toString());
 		   for(int j=0; j<tipoHidroValues.length; j++)
 		   {
 		    if(tipoHidroValues[j].getCodigo().equals(codigoClasificado))   //busco el codigo asignado para ese valor
 		    {
 		     embalse.setTipo_hidrografia(tipoHidroValues[j]);
 			 break;
 		    }	  
 		   }	
 	}	
    }
   }
   //embalse.setProjection(_projection);
   
   return embalse;
  }
 
 /**Devuelve un Objeto Cuenca
  * @throws SQLException 
  * @throws ReadDriverException */
  private PersistentGeometricObject getCuenca(SelectableDataSource sds, int index) throws SQLException, ReadDriverException
  {
   Cuenca cuenca = new Cuenca();
   
   for (int k=0; k<_fields.length; k++)
   {
    String field = _fields[k].toString();	  
    String fvalue = _links.get(field).toString(); //nombre del campo 	
    Value val = null;		 
    
    if(fvalue.equals("Vacío"))  //Un campo que no se encuentra en el fichero
     val = getEmptyValue(table, field);    
    else
 	 val = sds.getFieldValue(index, sds.getFieldIndexByName(fvalue)); 

    if(field.equals("nombre"))
 	 cuenca.setNombre(val.toString());
   }
   //cuenca.setProjection(_projection);
   
   return cuenca;
  }
  
  /**Devuelve un Objeto Parcela
   * @throws SQLException 
   * @throws ReadDriverException */
   private PersistentGeometricObject getParcela(SelectableDataSource sds, int index) throws SQLException, ReadDriverException
   {
    Parcela parcela = new Parcela();
    
    for (int k=0; k<_fields.length; k++)
    {
     String field = _fields[k].toString();	  
     String fvalue = _links.get(field).toString(); //nombre del campo 	
     Value val = null;		 
     
     if(fvalue.equals("Vacío"))  //Un campo que no se encuentra en el fichero
      val = getEmptyValue(table, field);    
     else
  	 val = sds.getFieldValue(index, sds.getFieldIndexByName(fvalue)); 

     if(field.equals("nombre"))
      parcela.setNombre(val.toString());
     else
     if(field.equals("poseedor"))
      parcela.setPoseedores(val.toString());
     else
     if(field.equals("codigo_uso"))  //------------------------------------------------------------
     {	 
      		/*Si el valor del codigo de uso del fichero no ha sido clasificado
      		 * hay que ponerle uno de los codigo de uso existentes   */
      		if(usosParcelaslinkValues.get(val.toString()) == null)
      		{
      		 //Primero se verifica si el valor pertenece a algún nomenclador	
    		  boolean encontrado = false;
     		  for(int j=0; j<usosParcelasValues.length; j++)
  		      {
  		       if(usosParcelasValues[j].getCodigo_uso().toString().equals(val.toString()))
  		       {
  		    	usosParcelaslinkValues.put(val.toString(), usosParcelasValues[j].getId());
  		        parcela.setUso_catastro(usosParcelasValues[j]);
  		        encontrado = true;
  			    break;
  		       }	  
  		      }
     		    //si no pertenece a ningún nomenclador hay que clasificarlo
     	      if(encontrado == false)  
     		  {
      		   String selected = get_Clasification_Option_panel(usosParcelasDesc, val, field);
      		   
      		 if(selected == null)
     	     {	 
     	      canceled = true;
     	      return null;
     	     } 
      		 
      		   for(int j=0; j<usosParcelasValues.length; j++)
      		   {
      		    if(usosParcelasValues[j].getDescripcion_uso().equals(selected))
      		    {
      		     usosParcelaslinkValues.put(val.toString(), usosParcelasValues[j].getId());
      		     parcela.setUso_catastro(usosParcelasValues[j]);
      			 break;
      		    }	  
      		   }	 
     		  } 
      		}	
      		else  //El valor del uso ya está clasificado
      		{
      			   int iduso = usosParcelaslinkValues.get(val.toString());
      			   for(int j=0; j<usosParcelasValues.length; j++)
      			   {
      			    if(usosParcelasValues[j].getId() == iduso)   //busco el id del uso asignado para ese valor
      			    {
      			     parcela.setUso_catastro(usosParcelasValues[j]);
      				 break;
      			    }	  
      			   }	
      		}	
//      if(val.toString().trim().equals(""))
//       parcela.setUso_catastro(null);
//      else
//      {	  
//       Uso_catastro []usos = Uso_catastroManager.Obtener_usos_catastro(" where codigo_uso="+val.toString());
//       if(usos != null)
//        parcela.setUso_catastro(usos[0]);
//      } 
     } 
     else
     if(field.equals("zc"))
      parcela.setZc(Integer.parseInt(val.toString()));
    }
    
    //parcela.setProjection(_projection);
    
    return parcela;
   }
   
   /**Devuelve un Objeto Suelo
    * @throws SQLException 
    * @throws ReadDriverException */
    private PersistentGeometricObject getSuelo(SelectableDataSource sds, int index) throws SQLException, ReadDriverException
    {
    	Suelo suelo = new Suelo(true);
    	  
    	  for (int k=0; k<_fields.length; k++)
    	  {
    	   String field = _fields[k].toString();	  
    	   String fvalue = _links.get(field).toString(); //nombre del campo 	
    	   
    	   Value val = getCurrentValue(fvalue, field, sds, index);

    	   if(field.equals("textura"))//---------------------------------------------------
    	   {
    		/*Si el valor del tipo de textura del fichero no ha sido clasificado
    		 * hay que ponerle uno de los tipos de hidrografia existentes   */
    		if(texturalinkValues.get(val.toString()) == null)
    		{
    			//Primero se verifica si el valor pertenece a algun nomenclador	
    			  boolean encontrado = false;	
    			  for(int j=0; j<texturaValues.length; j++)
    			  {
    			   System.out.println(texturaValues[j].getTextura());	   
    			   System.out.println(val.toString());
    			   if(texturaValues[j].getTextura().equals(val.toString()))
    			   {
    				texturalinkValues.put(val.toString(), texturaValues[j].getId());
    	    		suelo.setTextura(texturaValues[j]);
    	    		encontrado = true;
    	    		break;
    			   }	  
    			  }
    			//si no pertenece a ningun nomenclador hay que clasificarlo
    		    if(encontrado == false)  
    			{
    		     String selected = get_Clasification_Option_panel(texturaDesc, val, field);
    		     
    		     if(selected == null)
    		     {	 
    		      canceled = true;
    		      return null;
    		     } 
    		 
    		     for(int j=0; j<texturaValues.length; j++)
    		     {
    		      if(texturaValues[j].getTextura().equals(selected))
    		      {
    		       texturalinkValues.put(val.toString(), texturaValues[j].getId());
    		       suelo.setTextura(texturaValues[j]);
    			   break;
    		      }	  
    		     }
    		    }
    		}	
    		else  //El valor del tipo de textura ya esta clasificado
    		{
    			   int idtext = texturalinkValues.get(val.toString());
    			   for(int j=0; j<texturaValues.length; j++)
    			   {
    			    if(texturaValues[j].getId() == idtext)   //busco el id de textura asignado para ese valor
    			    {
    				 suelo.setTextura(texturaValues[j]);
    				 break;
    			    }	  
    			   }	
    		}	
    	   }
    	   else
    	   if(field.equals("tipo")) //------------------------------------------------------------
           {
        		/*Si el valor del tipo de suelo del fichero no ha sido clasificado
        		 * hay que ponerle uno de los tipos de suelos existentes   */
        		if(tipoSuelolinkValues.get(val.toString()) == null)
        		{
        		 //Primero se verifica si el valor pertenece a algun nomenclador	
      			  boolean encontrado = false;
       		      for(int j=0; j<tipoSueloValues.length; j++)
    		      {
    		       if(tipoSueloValues[j].getTipo().equals(val.toString()))
    		       {
    		        tipoSuelolinkValues.put(val.toString(), tipoSueloValues[j].getID());
    		        suelo.setTipo_suelo(tipoSueloValues[j]);
    		        encontrado = true;
    			    break;
    		       }	  
    		      }
       		    //si no pertenece a ningun nomenclador hay que clasificarlo
       			 if(encontrado == false)  
       			 {
        		  String selected = get_Clasification_Option_panel(tipoSueloDesc, val, field);
        		  
        		     if(selected == null)
        		     {	 
        		      canceled = true;
        		      return null;
        		     } 
        		 
        		   for(int j=0; j<tipoSueloValues.length; j++)
        		   {
        		    if(tipoSueloValues[j].getTipo().equals(selected))
        		    {
        		     tipoSuelolinkValues.put(val.toString(), tipoSueloValues[j].getID());
        		     suelo.setTipo_suelo(tipoSueloValues[j]);
        			 break;
        		    }	  
        		   }	 
       			 } 
        		}	
        		else  //El valor del tipo de suelo ya esta clasificado
        		{
        			   int idtipo = tipoSuelolinkValues.get(val.toString());
        			   for(int j=0; j<tipoSueloValues.length; j++)
        			   {
        			    if(tipoSueloValues[j].getID() == idtipo)   //busco el id de tipo de suelo asignado para ese valor
        			    {
        				 suelo.setTipo_suelo(tipoSueloValues[j]);
        				 break;
        			    }	  
        			   }	
        		}	
           }
    	   else
    	   if(field.equals("erosion"))  //------------------------------------------------------------
    		suelo.setErosion(val.toString());
    	   else
           if(field.equals("materiaorganica"))  //------------------------------------------------------------
            suelo.setMateriaorganica(Integer.parseInt(val.toString()));
           else
           if(field.equals("profundidadefectiva"))  //------------------------------------------------------------
            suelo.setProfundidadefectiva(Double.parseDouble(val.toString()));
           else
           if(field.equals("estructura"))  //------------------------------------------------------------
           {
        		/*Si el valor de la estructura del fichero no ha sido clasificado
        		 * hay que clasificarlo con los existentes*/
        		if(estructuraSuelolinkValues.get(val.toString()) == null)
        		{
        			//Primero se verifica si el valor pertenece a algun nomenclador	
        			  boolean encontrado = false;
           		      for(int j=0; j<estructuraSueloDesc.length; j++)
        		      {
        		       if(estructuraSueloDesc[j].equals(val.toString()))
        		       {
        		        estructuraSuelolinkValues.put(val.toString(), estructuraSueloDesc[j].toString());
        		        suelo.setEstructura(estructuraSueloDesc[j].toString());
        		        encontrado = true;
        			    break;
        		       }	  
        		      }
        			
           		 //si no pertenece a ningun nomenclador hay que clasificarlo
           		 if(encontrado == false)  
           		 {
        		   String selected = get_Clasification_Option_panel(estructuraSueloDesc, val, field);
        		   
        		     if(selected == null)
        		     {	 
        		      canceled = true;
        		      return null;
        		     } 
        		 
        		   for(int j=0; j<estructuraSueloDesc.length; j++)
        		   {
        		    if(estructuraSueloDesc[j].equals(selected))
        		    {
        		     estructuraSuelolinkValues.put(val.toString(), estructuraSueloDesc[j].toString());
        		     suelo.setEstructura(estructuraSueloDesc[j].toString());
        			 break;
        		    }	  
        		   }
           		 } 
        		}	
        		else  //El valor del tipo de estructura ya esta clasificado
        		{
        		 String estructura = estructuraSuelolinkValues.get(val.toString());
        		 suelo.setEstructura(estructura);
        		}
           }
           else
           if(field.equals("ph"))  //------------------------------------------------------------
            suelo.setPh(Double.parseDouble(val.toString()));
           else
           if(field.equals("pendiente"))  //------------------------------------------------------------
            suelo.setPendiente(Double.parseDouble(val.toString()));
           else
           if(field.equals("gravas"))  //------------------------------------------------------------
            suelo.setGravas(Double.parseDouble(val.toString()));
           else
           if(field.equals("piedras"))  //------------------------------------------------------------
            suelo.setPiedras(Double.parseDouble(val.toString()));
           else
           if(field.equals("rocas"))  //------------------------------------------------------------
            suelo.setRocas(Double.parseDouble(val.toString()));
    	  }
    	  //suelo.setProjection(_projection);
    	  
    	  return suelo;
    }
    
    /**Devuelve un Objeto Relieve
     * @throws SQLException 
     * @throws ReadDriverException */
     private PersistentGeometricObject getRelieve(SelectableDataSource sds, int index) throws SQLException, ReadDriverException
     {
     	Relieve relieve = new Relieve();
     	  
     	  for (int k=0; k<_fields.length; k++)
     	  {
     	   String field = _fields[k].toString();	  
     	   String fvalue = _links.get(field).toString(); //nombre del campo 	
     	   
     	   Value val = getCurrentValue(fvalue, field, sds, index);

           if(field.equals("elevacion"))  //------------------------------------------------------------
            relieve.setElevacion(Double.parseDouble(val.toString()));
     	  }
     	  
     	  return relieve;
     }
     
     /**Devuelve un Objeto Parteaguas
      * @throws SQLException 
      * @throws ReadDriverException */
      private PersistentGeometricObject getParteaguas(SelectableDataSource sds, int index) throws SQLException, ReadDriverException
      {
       Parteaguas parteaguas = new Parteaguas();
       
       for (int k=0; k<_fields.length; k++)
       {
        String field = _fields[k].toString();	  
        String fvalue = _links.get(field).toString(); //nombre del campo 	
        Value val = null;		 
        
        if(fvalue.equals("Vacío"))  //Un campo que no se encuentra en el fichero
         val = getEmptyValue(table, field);    
        else
     	 val = sds.getFieldValue(index, sds.getFieldIndexByName(fvalue)); 

        if(field.equals("descripcion"))
         parteaguas.setDescripcion(val.toString());
       }
       //cuenca.setProjection(_projection);
       
       return parteaguas;
      }
}