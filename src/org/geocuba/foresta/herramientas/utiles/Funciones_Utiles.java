package org.geocuba.foresta.herramientas.utiles;

import java.awt.Color;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.geocuba.foresta.administracion_seguridad.VistaManager;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import com.hardcode.driverManager.DriverLoadException;
import com.hardcode.gdbms.driver.exceptions.InitializeWriterException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.DataSource;
import com.hardcode.gdbms.engine.values.StringValue;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.andami.ConfigurationException;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.exceptions.layers.LegendLayerException;
import com.iver.cit.gvsig.exceptions.visitors.VisitorException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.core.DefaultFeature;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.styles.ArrowDecoratorStyle;
import com.iver.cit.gvsig.fmap.core.styles.SimpleLineStyle;
import com.iver.cit.gvsig.fmap.core.symbols.ArrowMarkerSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.ILineSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.IMarkerSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;
import com.iver.cit.gvsig.fmap.core.v02.FSymbol;
import com.iver.cit.gvsig.fmap.drivers.ConcreteMemoryDriver;
import com.iver.cit.gvsig.fmap.drivers.ConnectionFactory;
import com.iver.cit.gvsig.fmap.drivers.DBException;
import com.iver.cit.gvsig.fmap.drivers.DBLayerDefinition;
import com.iver.cit.gvsig.fmap.drivers.FieldDescription;
import com.iver.cit.gvsig.fmap.drivers.IConnection;
import com.iver.cit.gvsig.fmap.drivers.IVectorialDatabaseDriver;
import com.iver.cit.gvsig.fmap.drivers.db.utils.ConnectionWithParams;
import com.iver.cit.gvsig.fmap.drivers.db.utils.SingleVectorialDBConnectionManager;
import com.iver.cit.gvsig.fmap.drivers.jdbc.postgis.PostGISWriter;
import com.iver.cit.gvsig.fmap.drivers.jdbc.postgis.PostGisDriver;
import com.iver.cit.gvsig.fmap.edition.DefaultRowEdited;
import com.iver.cit.gvsig.fmap.edition.IRowEdited;
import com.iver.cit.gvsig.fmap.edition.IWriter;
import com.iver.cit.gvsig.fmap.layers.FBitSet;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.fmap.layers.layerOperations.AlphanumericData;
import com.iver.cit.gvsig.fmap.rendering.FInterval;
import com.iver.cit.gvsig.fmap.rendering.ILegend;
import com.iver.cit.gvsig.fmap.rendering.IVectorLegend;
import com.iver.cit.gvsig.fmap.rendering.LegendFactory;
import com.iver.cit.gvsig.fmap.rendering.VectorialUniqueValueLegend;
import com.iver.cit.gvsig.gui.styling.LineProperties;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.PostProcessSupport;
import com.iver.utiles.XMLEntity;

@SuppressWarnings("deprecation")
public class Funciones_Utiles {

    //static String WIN_PROGRAMFILES = System.getenv("programfiles");
    //static String FILE_SEPARATOR = System.getProperty("file.separator");

    public static void ExecuteProgram(String[] commands) throws IOException {
        Runtime.getRuntime().exec(commands);
    }

//    public static void ExecuteProgram(String command, String[] arguments) throws IOException
//    {
//    	Runtime.getRuntime().exec(command, arguments);    	
//    }
    public static int ExecuteProgram(String command) throws IOException, InterruptedException
    {
    	String line;
    	//ArrayList<String> errores=new ArrayList<String>();
    	
    	int _exit_value=1;
    	try
    	{
    		Process proc=Runtime.getRuntime().exec(command);
    		
    		//Obtener la salida estandar de errores y guardarlos en una lista
    		InputStream error_stream=proc.getErrorStream();
    		BufferedReader br = new BufferedReader (new InputStreamReader(error_stream));
    		saveStringToFile("gvSIG"+Global.fileSeparator+"extensiones"+Global.fileSeparator+"org.geocuba.foresta"+Global.fileSeparator+"data"+Global.fileSeparator+"command.log", "", false);
    		while ((line=br.readLine())!=null)
    		{
    			System.out.println(line);
    			saveStringToFile("gvSIG"+Global.fileSeparator+"extensiones"+Global.fileSeparator+"org.geocuba.foresta"+Global.fileSeparator+"data"+Global.fileSeparator+"command.log", line, true);
    			//errores.add(line);
    		}

    		//Esperar a que termine el proceso
    		proc.waitFor();
    		//saveStringToFile("d:"+Global.fileSeparator+"command.log", "El valor de retorno del comando "+command+" ha sido "+proc.exitValue(), true);
    		_exit_value=proc.exitValue();
    	}    	    
    	catch(IOException e)
    	{
    		e.printStackTrace();
    		JOptionPane.showMessageDialog(null, "Error "+e.getMessage());
    	}
    	//String[] result=new String[errores.size()];
    	return _exit_value;
    }

    public static void ExecuteProgram(String command, String[] env) throws IOException {
        try {
           /* Process proc;

            proc =*/ Runtime.getRuntime().exec(command, env);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error " + e.getMessage());
        }
//    	catch(InterruptedException e)
//    	{
//    		JOptionPane.showMessageDialog(null, e.getMessage());
//    	}
    }

    public static boolean saveStringToFile(String fileName, String saveString,boolean append) {
        boolean saved = false;
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(
                    new FileWriter(fileName,append));
            try {
                bw.write(saveString);
                bw.newLine();
                saved = true;
            } finally {
                bw.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return saved;
    }

    public static String getStringFromFile(String fileName) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(
                    new FileReader(fileName));
            try {
                String s;
                while ((s = br.readLine()) != null) {
//  				add linefeed back since stripped by readline()
                    sb.append(s);
                    sb.append("\n");
                }
            } finally {
                br.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    public static String UpCaseFirstCharacter(String source) {
//		char[] chars=source.toCharArray();
        String result = Character.toString(source.charAt(0)).toUpperCase();
        return result += source.substring(1);
    }
    
     /**Crea un mapa tematico por valores de un campo*/
    public static void LeyendaValoresUnicos(String layerName, String fieldname, boolean defaultvalues) throws LegendLayerException, ArrayIndexOutOfBoundsException 
    {
   	 VectorialUniqueValue variants = new VectorialUniqueValue(fieldname);
   	 View vista = AlgUtils.GetView(null);
        MapControl mapCtrl = vista.getMapControl();
   	 FLyrVect capa = (FLyrVect)mapCtrl.getMapContext().getLayers().getLayer(layerName);
   	 if(capa != null)
   	 {	 
   	  variants.setData(capa, capa.getLegend());
   	  variants.fillTableValues();
   	 
        ILegend legend = variants.getLegend(defaultvalues);
     //   try {
   		capa.setLegend((IVectorLegend)legend);
   		capa.getMapContext().callLegendChanged();
   	/*} catch (LegendLayerException e) {
   		// TODO Auto-generated catch block
   		e.printStackTrace();
   	}*/
    }
   	else
     JOptionPane.showMessageDialog(null, "Error; No se obtuvo la capa "+layerName);	
  } 
    
    /**Crea un mapa tematico por valores de un campo*/
    public static void LeyendaValoresUnicos(FLayer layer, String fieldname, boolean defaultvalues) 
    {
   	 VectorialUniqueValue variants = new VectorialUniqueValue(fieldname);
   	// View vista = AlgUtils.GetView(null);
      //  MapControl mapCtrl = vista.getMapControl();
   	 //FLyrVect capa = (FLyrVect)mapCtrl.getMapContext().getLayers().getLayer(layerName);
   	 
   	 FLyrVect capa = (FLyrVect)layer;
   	 if(capa != null)
   	 {	 
   	  variants.setData(capa, capa.getLegend());
   	  variants.fillTableValues();
   	 
        ILegend legend = variants.getLegend(defaultvalues);
        try {
   		capa.setLegend((IVectorLegend)legend);
   		capa.getMapContext().callLegendChanged();
   	} catch (LegendLayerException e) {
   		e.printStackTrace();
   	}
    }
   	else
     JOptionPane.showMessageDialog(null, "Error; La capa esta vacia");	
  } 
    
    /**Crea un mapa tematico por valores de un campo con 
     * la descripcion de la leyenda desde una tabla externa*/
    public static void LeyendaValoresUnicos(String layerName, String fieldname, String selectField, String Table, String filterField, boolean isString, boolean defaultvalues) 
    {
   	 VectorialUniqueValue variants = new VectorialUniqueValue(fieldname);
   	 variants.setLegendDescription_FromRelatedTable(selectField, Table, filterField, isString);
   	 
   	 View vista = AlgUtils.GetView(null);
        MapControl mapCtrl = vista.getMapControl();
   	 FLyrVect capa = (FLyrVect)mapCtrl.getMapContext().getLayers().getLayer(layerName);
   	 if(capa != null)
   	 {	 
   	  variants.setData(capa, capa.getLegend());
   	  variants.fillTableValues();
   	 
        ILegend legend = variants.getLegend(defaultvalues);
        try {
   		capa.setLegend((IVectorLegend)legend);
   		capa.getMapContext().callLegendChanged();
   	} catch (LegendLayerException e) {
   		// TODO Auto-generated catch block
   		e.printStackTrace();
   	}
    }
   	else
     JOptionPane.showMessageDialog(null, "Error; No se obtuvo la capa "+layerName);	
  }   
    
    /**Crea un mapa tematico con las descripciones pasadas por parametro*/
    public static void LeyendaValoresUnicos(String layerName, String fieldname, boolean defaultvalues, HashMap<String,String> extDescs) throws LegendLayerException, ArrayIndexOutOfBoundsException 
    {
   	 VectorialUniqueValue variants = new VectorialUniqueValue(fieldname, extDescs);
   	 View vista = AlgUtils.GetView(null);
        MapControl mapCtrl = vista.getMapControl();
   	 FLyrVect capa = (FLyrVect)mapCtrl.getMapContext().getLayers().getLayer(layerName);
   	 if(capa != null)
   	 {	 
   	  variants.setData(capa, capa.getLegend());
   	  variants.fillTableValues();
   	 
        ILegend legend = variants.getLegend(defaultvalues);
     //   try {
   		capa.setLegend((IVectorLegend)legend);
   		capa.getMapContext().callLegendChanged();
   	/*} catch (LegendLayerException e) {
   		// TODO Auto-generated catch block
   		e.printStackTrace();
   	}*/
    }
   	else
     JOptionPane.showMessageDialog(null, "Error; No se obtuvo la capa "+layerName);	
  }
    
    /**Crea un mapa tematico con las descripciones pasadas por parametro*/
    public static void LeyendaValoresUnicos(FLyrVect capa, String fieldname, boolean defaultvalues, HashMap<String,String> extDescs) throws LegendLayerException, ArrayIndexOutOfBoundsException 
    {
   	 if(capa != null)
   	 {
   	  VectorialUniqueValue variants = new VectorialUniqueValue(fieldname, extDescs);
   	  variants.setData(capa, capa.getLegend());
   	  variants.fillTableValues();
   	 
        ILegend legend = variants.getLegend(defaultvalues);
   		capa.setLegend((IVectorLegend)legend);
   		//capa.getMapContext().callLegendChanged();
    }
   	else
     JOptionPane.showMessageDialog(null, "Error; No se obtuvo la capa ");	
  } 
    /**Para ponerle una leyenda de valores unicos a una capa*/
	public static void ValorUnico(FLyrVect capa,String nombreCampo) 
    {
     int k/*, j*/;
     String filterName=nombreCampo;
     SelectableDataSource dsCaminos;
     //String value;
     int campo;
     
     try {
          dsCaminos = ((FLyrVect) capa).getRecordset();
          campo = dsCaminos.getFieldIndexByName(filterName);
          //String numero= dsCaminos.getFieldName(1);
          
          // Creamos el primer y ï¿½ltimo color.
          Color startColor = new Color(20,255,20);
          Color endColor = new Color(40,150,40);

          VectorialUniqueValueLegend legend = null;
          legend = LegendFactory.createVectorialUniqueValueLegend(FShape.MULTI);

          FSymbol myDefaultSymbol=null;
          Vector valoresClave=new Vector();
          StringValue nombre;

          nombre= (StringValue)dsCaminos.getFieldValue(0, campo);
          valoresClave.addElement(nombre);
          nombre= (StringValue)dsCaminos.getFieldValue(1, campo);
          valoresClave.addElement(nombre);
          nombre= (StringValue)dsCaminos.getFieldValue(2, campo);
          valoresClave.addElement(nombre);
        
          int r;
          int g;
          int b;
          int stepR;
          int stepG;
          int stepB;
          
          r = startColor.getRed();
          g = startColor.getGreen();
          b = startColor.getBlue();
          stepR = (endColor.getRed() - r) /2;
          stepG = (endColor.getGreen() - g) /1;
          stepB = (endColor.getBlue() - b) /6;

          legend.clear();
          legend.useDefaultSymbol(false);
          StringValue clave;

          for (k = 0; k < valoresClave.size(); k++) 
          {
           clave = (StringValue) valoresClave.get(k);
           Color c = new Color(r, g, b);
           String etiqueta="Municipio"+k;

           //si no esta creado el simbolo se crea
           myDefaultSymbol = new FSymbol(FShape.MULTI);
           myDefaultSymbol.setColor(c);
           myDefaultSymbol.setDescription(etiqueta);
           myDefaultSymbol.setSize(3);
           myDefaultSymbol.setSizeInPixels(true);

           // CALCULAMOS UN COLOR APROPIADO
           r = r + stepR;
           g = g + stepG;
           b = b + stepB;

           legend.addSymbol(clave, myDefaultSymbol);
         }
         capa.setLegend(legend);

        }catch(NullPointerException e){
                e.printStackTrace();
        }
catch (LegendLayerException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (ReadDriverException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
    }
    
    /**
     * EQUAL INTERVAL Devuelve un Array con el nï¿½mero de intervalos que se
     * quieren crear. Los intervalos se crean con un tamaï¿½o igual entre ellos.
     * @param numIntervals nï¿½mero de intervalos
     * @param minValue Valor mï¿½nimo.
     * @param maxValue Valor mï¿½ximo.
     * @param fieldName Nombre del campo
     * @return Array con los intervalos.
     */
  private static FInterval[] calculateEqualIntervals(int numIntervals, double maxValue, double minValue) 
  {
        FInterval[] theIntervalArray = new FInterval[numIntervals];
        double step = (maxValue - minValue) / numIntervals;
        
        if (numIntervals > 1) 
        {
         theIntervalArray[0] = new FInterval(minValue, minValue + step);
         
         for (int i = 1; i < (numIntervals - 1); i++) 
          theIntervalArray[i] = new FInterval(minValue+(i * step)+0.01, minValue + ((i + 1) * step));

         theIntervalArray[numIntervals - 1] = new FInterval(minValue +
                                              ((numIntervals - 1) * step) + 0.01, maxValue);
        }
        else 
            theIntervalArray[0] = new FInterval(minValue, maxValue);

        return theIntervalArray;
  }
  
  /**Retorna una cadena con los gids de los elementos seleccionados*/
	public static String GetSelectedGids(FLayer layer)
	{
		String ids = "";
		try {	
			BitSet sel = ((FLyrVect)layer).getSelectionSupport().getSelection();
	   	    DataSource ds = ((AlphanumericData)layer).getRecordset();
	        ds.start();
	        
	        if(sel.isEmpty())
	         return ids;	
	        
	        int fieldIndex= ds.getFieldIndexByName("gid");
	        
	        for (int i = sel.nextSetBit(0); i >= 0; i = sel.nextSetBit(i+1)) 
		     ids += ds.getFieldValue(sel.nextSetBit(i), fieldIndex).toString()+",";
	        
	        ids = ids.substring(0, ids.length()-1);
	        ds.stop();	
			
	        } catch (ReadDriverException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      return ids;  
	}
	
	/**Retorna un arreglo con los gids de los elementos seleccionados*/
	public static ArrayList<String> GetSelectedGidsArray(FLayer layer)
	{
		ArrayList<String> ids = new ArrayList<String>();
		try {	
			BitSet sel = ((FLyrVect)layer).getSelectionSupport().getSelection();
	   	    DataSource ds = ((AlphanumericData)layer).getRecordset();
	        ds.start();
	        
	        if(sel.isEmpty())
	         return ids;	
	        
	        int fieldIndex= ds.getFieldIndexByName("gid");
	        
	        for (int i = sel.nextSetBit(0); i >= 0; i = sel.nextSetBit(i+1)) 
		     ids.add(ds.getFieldValue(sel.nextSetBit(i), fieldIndex).toString());
	        
	        ds.stop();	
			
	        } catch (ReadDriverException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      return ids;  
	}
	
	/**Devuelve la cantidad de elementos seleccionados*/
	public static int GetSelectionCount(FLayer layer)
	{
		int count = 0;
		
		try {	
			BitSet sel = ((FLyrVect)layer).getSelectionSupport().getSelection();
	   	    DataSource ds = ((AlphanumericData)layer).getRecordset();
	        ds.start();
	        
	        if(sel.isEmpty())
	         return -1;	
	        
	        for (int i = sel.nextSetBit(0); i >= 0; i = sel.nextSetBit(i+1)) 
	         count++;
	        
	        ds.stop();	
			
	        } catch (ReadDriverException e) {
				e.printStackTrace();
			}
	      return count;  
	}
	
	public static double getRasterCellZize() throws ConfigurationException, FileNotFoundException
	{
		XMLEntity xml;
		double value = 25;
		
			xml = XMLUtils.persistenceFromXML("gvSIG"+Global.fileSeparator+"extensiones"+Global.fileSeparator+"org.geocuba.foresta"+Global.fileSeparator+"configForesta.xml");
			int index = xml.firstIndexOfChild("name", "raster_options");
			xml = xml.getChild(index);
			value = xml.getDoubleProperty("cellsize");
			
		return value;	
	}
	
	public static void setRasterCellZize(double value) throws ConfigurationException, FileNotFoundException
	{
			XMLEntity xml = XMLUtils.persistenceFromXML("gvSIG"+Global.fileSeparator+"extensiones"+Global.fileSeparator+"org.geocuba.foresta"+Global.fileSeparator+"configForesta.xml");
			int index = xml.firstIndexOfChild("name", "raster_options");
			xml.getChild(index).putProperty("cellsize", value);
			XMLUtils.persistenceToXML(xml, "gvSIG"+Global.fileSeparator+"extensiones"+Global.fileSeparator+"org.geocuba.foresta"+Global.fileSeparator+"configForesta.xml");
	}
	
	public static void setReddUmbral(double value) throws ConfigurationException, FileNotFoundException
	{
			XMLEntity xml = XMLUtils.persistenceFromXML("gvSIG"+Global.fileSeparator+"extensiones"+Global.fileSeparator+"org.geocuba.foresta"+Global.fileSeparator+"configForesta.xml");
			int index = xml.firstIndexOfChild("name", "algutils_options");
			xml.getChild(index).putProperty("reddrenaje_umbral", value);
			XMLUtils.persistenceToXML(xml, "gvSIG"+Global.fileSeparator+"extensiones"+Global.fileSeparator+"org.geocuba.foresta"+Global.fileSeparator+"configForesta.xml");
	}
	
	public static double getReddUmbral() throws ConfigurationException, FileNotFoundException
	{
		XMLEntity xml;
		double value = 1000000.0;
		
			xml = XMLUtils.persistenceFromXML("gvSIG"+Global.fileSeparator+"extensiones"+Global.fileSeparator+"org.geocuba.foresta"+Global.fileSeparator+"configForesta.xml");
			int index = xml.firstIndexOfChild("name", "algutils_options");
			xml = xml.getChild(index);
			value = xml.getDoubleProperty("reddrenaje_umbral");
			
		return value;	
	}
	
	/**Para asignarle a una capa de lineas vectorial decoracion de flechas*/
	public static boolean setArrowToLines(FLyrVect layer, int lineWith, int agudeza, int arrowSize, int arrowCount)
	{
		try {
			if(layer == null)
			 return false;
			
			if(layer.getShapeType() != FShape.LINE)
			 return false;
			
		  ISymbol symb = layer.getLegend().getDefaultSymbol();
		  ILineSymbol linesymb = (ILineSymbol)symb;
		  
		  //col = new Color(19, 22, 164, 0);//f.getColor();
		  //col = GUIUtil.alphaColor(col, 200);
		  
		  LineProperties lineProperties;
		  lineProperties = new LineProperties(lineWith);
		  
		  SimpleLineStyle simplLine= new SimpleLineStyle();

		  simplLine.setStroke(lineProperties.getLinePropertiesStyle());
		  simplLine.setOffset(2);
		  
		  //ArrowDecorator arrowDecorator = new ArrowDecorator();
		  //arrowDecorator.setArrowDecoratorStyle(linesymb.getLineStyle().getArrowDecorator());
		  ArrowDecoratorStyle ads = new ArrowDecoratorStyle();//linesymb.getLineStyle().getArrowDecorator();
		  
			if (ads != null) {
				ads.getMarker().setColor(linesymb.getColor());
			}
			
			 IMarkerSymbol marker = ads.getMarker();

			ArrowMarkerSymbol arrow = (ArrowMarkerSymbol) marker;
			arrow.setSharpness(agudeza);
			marker.setSize(arrowSize);
			ads.setMarker(marker);
			ads.setArrowMarkerCount(arrowCount);
			ads.setFlipAll(false);
			ads.setFlipFirst(false);
			ads.setFollowLineAngle(true);
			
			simplLine.setArrowDecorator(ads);
			linesymb.setLineStyle(simplLine);
		  
		    linesymb.setLineColor(linesymb.getColor()); 
		    VistaManager.RefreshView();
		    return true;
		
		} catch (ReadDriverException e) {
			e.printStackTrace();
			return false;
		}	
	}
	
      public static void abrirURL(String url) 
      {
        String nombreSO = System.getProperty("os.name");
        try {
            if (nombreSO.startsWith("Mac OS")){
                Class manager = Class.forName("com.apple.eio.FileManager");
                Method openURL = manager.getDeclaredMethod("openURL", new Class[] {String.class});
                openURL.invoke(null, new Object[] {url});
            }
            if (nombreSO.startsWith("Windows"))              
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);            
            else{
                //se asume  Unix or Linux
                String[] navegadores = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
                String navegador = null; 
                for (int contador = 0; contador < navegadores.length && navegador == null; contador++)
                {                    
                    if (Runtime.getRuntime().exec( new String[] {"which", navegadores[contador]}).waitFor() == 0)
                        navegador = navegadores[contador];
                }
                
                if (navegador == null) throw new Exception("No se encuentra navegador web");
                else
                    Runtime.getRuntime().exec(new String[] {navegador, url});
                }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al abrir"+url + ":\n" + e.getLocalizedMessage());
        }
    }
      
      /**Metodo encargado de importar el archivo shp_file_path hacia la base de datos db_name. 
  	 * Primeramente se crea un script de postrgresql con nombre "query_"+tabla+".sql" utilizando 
  	 * shp2pgsql.exe. Los datos son insertados realmente a una tabla temporal llamada tabla+".tmp" 
  	 * en el esquema plublic. Posteriormente se ejecuta el script usando psql.exe. Durante el 
  	 * proceso es necesario realizar cierta correccion al script. 
  	 * @param srid
  	 * @param shp_file_path
  	 * @param tabla
  	 * @param db_name
  	 * @return
  	 * @throws ExecuteException
  	 * @throws IOException
  	 */
  	public static int LoadShapeFile(String srid,String shp_file_path,String tabla,String db_name) throws ExecuteException, IOException
  	{
  		String postgresPath = System.getenv("PGLOCALEDIR"); //direccion de la carpeta locale de postgres
  		postgresPath = postgresPath.substring(0, postgresPath.indexOf("8.3")+3) + ""+Global.fileSeparator+"bin"+Global.fileSeparator+"";
  		
  		String command="\"" + postgresPath+"shp2pgsql.exe\" " +
  		" -d -D" +
  		" -s "+ srid + 
  		" \"" + shp_file_path+"\"" +
  		" public."+tabla+"_temp";

  		File f=new File(shp_file_path);
  		String shp_dir=f.getParent();
  		
  		String query_file_path=shp_dir+""+Global.fileSeparator+"query_"+tabla+".sql";
  		f=new File(query_file_path);
  		
  		FileOutputStream fos=new FileOutputStream(f);
//  		ByteArrayOutputStream bos = new ByteArrayOutputStream();
  		CommandLine cmdLine = CommandLine.parse(command);
  		PumpStreamHandler psh = new PumpStreamHandler(fos, System.out);
  		DefaultExecutor executor = new DefaultExecutor();
  		executor.setStreamHandler(psh);
  		int ret_val=executor.execute(cmdLine);
  		
  		fos.close();
  		
  		RemoveEmptyLines(query_file_path);
  		FixDateFormat(query_file_path);
  		
  		command="\"" + postgresPath +"psql.exe\"" +
  		" -f \"" + f.getPath() + "\"" +
  			" -d " + db_name + " " + //				
  			Global.PostgresUser;
  		
  		ByteArrayOutputStream bos = new ByteArrayOutputStream();
  		cmdLine = CommandLine.parse(command);
  		psh = new PumpStreamHandler(bos, System.out);
  		executor = new DefaultExecutor();
  		executor.setStreamHandler(psh);
  		ret_val=executor.execute(cmdLine);
  		bos.close();
  		
  		return ret_val;
  	}
  	
  	/**Elimina las lineas vacias creadas incorrectamente por shp2psql y que cortan las filas 
     * por algun campo 
     * @param fileName
     * @throws IOException
     */
    public static void RemoveEmptyLines(String fileName) throws IOException
	{
    	File src_file=new File(fileName);
		FileReader fileReader=new FileReader(src_file);
		File dest_file=new File(fileName+"_tmp");
		FileWriter fileWriter=new FileWriter(dest_file);
//		ArrayList<String> lines=new ArrayList<String>();

		LineNumberReader in = new LineNumberReader(fileReader);
		
		String line="";
//		int index=-1;
		System.out.println("Removing empty lines from "+fileName);
		boolean empty_lines_found=true;
		
		while(in.ready())
		{			
			line=in.readLine();
			if(line.equals(""))
			{
				empty_lines_found=true;
			}
			else
			{
				if(empty_lines_found)
					fileWriter.write(line);
				else
				{
					fileWriter.write("\n");
					fileWriter.write(line);
				}
				empty_lines_found=false;
			}
			
		}
		in.close();
		fileReader.close();
		fileWriter.close();
		if(!src_file.delete()) JOptionPane.showMessageDialog(null, "el archivo "+fileName+" no se ha eliminado");
		if(!dest_file.renameTo(src_file)) JOptionPane.showMessageDialog(null, "el archivo "+dest_file.getName()+" no se ha renombrado");
	}
    
    public static void FixDateFormat(String fileName) throws IOException
	{
    	File src_file=new File(fileName);
		FileReader fileReader=new FileReader(src_file);
		File dest_file=new File(fileName+"_tmp");
		FileWriter fileWriter=new FileWriter(dest_file);

		LineNumberReader in = new LineNumberReader(fileReader);
		
		String line="";
		System.out.println("Fixing date format from "+fileName);
		ArrayList<Integer> dateFieldIndexes=new ArrayList<Integer>();
		
		//Avanzar hasta encontrar la sentencia CREATE donde empieza la descripcion de los campos. 
		while(in.ready())
		{			
			line=in.readLine();
			if(!line.split(" ")[0].equals("CREATE"))
			{
				fileWriter.write(line+'\n');
			}
			else//encontre la linea que empieza con create
			{
				fileWriter.write(line+'\n');
				break;
			}
			
		}
		
		//Leer la descripcion de los campos e ir tomando los indices de los campos de tipo date
		int i=0;	//empezar en cero pq el que contamos arriba es el gid
		while(in.ready())
		{
			line=in.readLine();
			if(line.split(" ")[1].startsWith("date"))
			{
				dateFieldIndexes.add(i);
			}
			fileWriter.write(line+'\n');
			if(line.charAt(line.length()-1)==';')
				break;
			i++;
		}
		
		//si se encontraron campos de tipo date, procesar las filas y reparar si es necesario
		//(si el tamanno la cadena de la fecha es distinto de 8)
		if(dateFieldIndexes.size()!=0)
		{
			//Avanzar hasta encontrar la sentencia COPY detras de la cual comienzan las filas
			while(!(line=in.readLine()).startsWith("COPY"))
				fileWriter.write(line+'\n');
			fileWriter.write(line+'\n');
			
			String[] tokens;
			int j,pos,tmp_pos,count;
			String tmp_line;
			while(in.ready()&&!(line=in.readLine()).equals(""+Global.fileSeparator+"."))
			{
				line=line.replaceAll(""+Global.fileSeparator+""+Global.fileSeparator+"\t", "");
				
				tokens=line.split("\t");
				for(i=0;i<dateFieldIndexes.size();i++)
				{
					j=dateFieldIndexes.get(i);
					if(!tokens[j].equals(""+Global.fileSeparator+"N") && tokens[j].length()!=8)
						tokens[j]=""+Global.fileSeparator+"N";	//definitivamente mejor que el valor que teniacuyo anno era de tres o menos cifras
				}
				line="";
				for(i=0;i<tokens.length-1;i++)
				{
					line+=tokens[i]+'\t';
				}
				line+=tokens[tokens.length-1];//se hace separado del for para no poner el ultimo caracter tab
				fileWriter.write(line+'\n');
			}
			fileWriter.write(line+'\n');
			fileWriter.write("END;");
			
			in.close();
			fileReader.close();
			fileWriter.close();
			src_file.delete();
			dest_file.renameTo(src_file);
		}
		else
		{
			in.close();
			fileReader.close();
			fileWriter.close();
			dest_file.delete();
		}
	}
    
    public static void FillComboWithCWPs(javax.swing.JComboBox jCmbDB, ConnectionWithParams cwp)
	{
    	//PluginServices.cancelableBackgroundExecution(new DBSincronizator());	
    	
    	
		ConnectionWithParams[] cwps=null;
			cwps = SingleVectorialDBConnectionManager.instance().getAllConnections();
		if(cwps!=null)
		{
			jCmbDB.removeAllItems();
			for(int i=0;i<cwps.length;i++)
			{
				jCmbDB.addItem(cwps[i]);
			}
			
			if(cwp!=null)
			{
				jCmbDB.setSelectedItem(cwp);
			}
		}
	}
    
    /**Devuelve un array con las bases de datos exsistentes en el servidor posgresql
	 * @return
	 * @throws SQLException 
	 */
//	public static String[] ListDatabases() throws SQLException
//	{
//		String query = "SELECT datname FROM pg_database";
//		String[] dbs=null;
//		//Statement st;
//		Connection conn=null;
//		
//		conn = ConnectionExt.CreateConnection();
//		
//		JDBCAdapter db = new JDBCAdapter(conn);
//		//ResultSet resultset;
//		try 
//		{
//			//st = conn.createStatement();
//			//resultset = st.executeQuery(query);
//			
//			db.executeQuery(query);
//			int c = db.getRowCount();
//			
//			if(c == -1)
//			 return null;
//			
//			dbs = new String[c];
//			
//			for(int i=0; i<c; i++)
//		     dbs[i] = db.getValueAt(i,0).toString();
//			
//			db.close();
//			
//			/*int i=0;
//			while (resultset.next()) {
//				dbs[i]=(String)resultset.getObject(1);
//				i++;
//			}*/
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();			
////			JOptionPane.showMessageDialog(null, PluginServices.getText(ext, "list_db_error")+" Detalles: "+e.getMessage());
//			return null;
//		}
//		
//		return dbs;
//	}
	
	 public static void RemoveUnusedConnections()
	    {
	    	SingleVectorialDBConnectionManager svdbcm=SingleVectorialDBConnectionManager.instance();
	    	
	    	ConnectionWithParams[] conns=svdbcm.getAllConnections();
			
			for(int i=0;i<conns.length;i++)
			{
				System.out.println(conns[i].getDb());
				
				if(!conns[i].isConnected())
				{
					svdbcm.closeAndRemove(conns[i]);
				}
			}
	    }
	 
	 public static boolean IsValidDBName(String dbname)
	    {
	    	if(dbname==null||dbname.equals(""))
	    		return false;
	    	for(int i=0;i<dbname.length();i++)
	    	{
	    		if(!Character.isLetterOrDigit(dbname.charAt(i))&&dbname.charAt(i)!='_')
	    			return false;
	    	}
	    	return true;
	    }
	 
		public static void RestaurarBD(String dbase, String path) throws Exception
	    {
	    	ManageDB(dbase, path, false);
	    }
		
		public static void SalvarBD(String dbase, String path) throws Exception
	    {
	    	ManageDB(dbase, path, true);
	    }
		
		/**
	     * Mï¿½todo encargado de hacer backups(dumps) y restaurar(restore) de bases de datos de postgres
	     * @param dbase Base de datos a la cual hacer backup o restore
	     * @param path Camini al fichero backup
	     * @param backup Si es true se hace backuo, en otro caso se hace restore
	     * @throws SifomapException 
	     */
	    public static void ManageDB(String dbase, String path, boolean backup) throws Exception
	    {
	    	String command;
	    	if(backup)
	    	{		
	    	 command="pg_dump -p "+Global.PostgresPort+" -U "+Global.PostgresUser+" -Fc -f \""+path+"\"";
	    	 
	    	 String ext = path.substring(path.length()-5, path.length());
	    	 if(!ext.equals(".dump"))
              command += ".dump";	
	    	 
	    	 command += " "+dbase;
	    	}		
	    	else
	    		command="pg_restore -p "+Global.PostgresPort+" -U "+Global.PostgresUser+" -d "+dbase+" \""+path+"\"";
	    	
//	    	command=command.replace(":user", "postgres");
//	    	command=command.replace(":path", path);
//	    	command=command.replace(":dbase", dbase);
//	    	command=command.replace(":port", Global.PostgresPort);
	    	
	    	command = "gvSIG"+Global.fileSeparator+"extensiones"+Global.fileSeparator+"org.geocuba.foresta"+Global.fileSeparator+"data"+Global.fileSeparator+"postgres"+Global.fileSeparator+""+command;
	    	
	    	System.out.println(command);
	    	
	    	try {
				ExecuteProgram(command);
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("Error al guardar/restaurar base de datos "+ dbase);
			}
	    }
	    
	    public static boolean formatoInputTextField(String valueText, char caracter, char formato, int maxlength)
	    {
	        //java.text.DecimalFormatSymbols dfs = new java.text.DecimalFormatSymbols();
	        boolean value = true;
	        
	     try {
	    	 
		        if (formato == 'N') //--------------------------------------------------------------------------
	            {
		        	if(valueText.length()>=maxlength)
	       	        { 	
	                        if(caracter == '\b')
	                         return true;
	                        else
	                        {	 
	                         Toolkit.getDefaultToolkit().beep();	 
	       	                 return false;
	                        } 
	       	        }
		        	
		        	value = validarCaracteresEspeciales(caracter); 	
	            }
		        else
		              if (formato == 'I')//--------------------------------------------------------------------------
		              {
		            	    if(valueText.length() > 0) //Si se escribió algo
			       	        { 	
			            	 Integer intvalue = Integer.parseInt(valueText);	//Elimino los ceros a la izquierda
			            	 valueText = intvalue.toString();  
			            	 
			            	 if(valueText.length() >= maxlength) //Verifico tamaño de número
			        	     { 
		                        if(caracter == '\b')
		                         return true;
		                        else
		                        {	 
		                         Toolkit.getDefaultToolkit().beep();	 
		       	                 return false;
		                        }
			        	     }   
			       	        }
	
		                // Verificar si la tecla pulsada no es un digito
		                    if(((caracter < '0') ||
		                            (caracter > '9')) &&
		                                (caracter != '\b' /*corresponde a BACK_SPACE*/)
		                                 )
		                        {
		                           value = false;  // ignorar el evento de teclado
		                        }
	
		              }
		               else
		               if (formato == 'D')//--------------------------------------------------------------------------
		              {
		            	   if(!validarCaracteresEspeciales(caracter))
		            		return false;
		            	   
		                // Verificar si la tecla pulsada no es un digito
		                   if (caracter == '.')
		                   {
		                        if (valueText.indexOf(caracter) != -1 || valueText.length() == 0)
		                            value = false;
	
		                   }
		                   else
		                   if(valueText.length() > 0) //Si se escribió algo
			       	        { 	
			            	 int index = valueText.indexOf('.'); //Verifico si tiene .
			            	 if(index >=0 )
			            	  valueText = valueText.substring(0, index);	
			            	 
			            	 Double doubvalue = Double.parseDouble(valueText);	//Elimino los ceros a la izquierda
			            	 String val = doubvalue.toString();  
			            	 
			            	 if(val.length()-2 >= maxlength) //Verifico tamaño de número
			        	     { 
		                        if(caracter == '\b')
		                         return true;
		                        else
		                        {	 
		                         Toolkit.getDefaultToolkit().beep();	 
		       	                 return false;
		                        }
			        	     }   
			       	        }
		                
		                    if(((caracter < '0') ||
		                            (caracter > '9')) &&
		                                (caracter != '\b' /*corresponde a BACK_SPACE*/)
		                                &&caracter != '.'//dfs.getDecimalSeparator()
		                                 )
		                       {
		                            value = false;  // ignorar el evento de teclado
		                        }
	
		              }
	                  else
		              if (formato == 'S')//--------------------------------------------------------------------------
		              {
		            	  System.out.println(valueText.length());
		            	     if(valueText.length() >= maxlength) //Verifico tamaño del campo
			        	     { 
		                        if(caracter == '\b')
		                         return true;
		                        else
		                        {	 
		                         Toolkit.getDefaultToolkit().beep();	 
		       	                 return false;
		                        }
			        	     } 
		            	  
		            	  if(!validarCaracteresEspeciales(caracter))
			            		return false;
		            	  
		                       if((Character.isDigit(caracter)) && (caracter != '\b'))
		                        {
		                            value = false;  // ignorar el evento de teclado
		                        }
		              }
	        
			    } catch (NumberFormatException e) {
			      value = false;	
			    }
	               
	               if(value==false)
	            	Toolkit.getDefaultToolkit().beep();
	               
	               return value;
	    }
	    
	    private static boolean validarCaracteresEspeciales(char caracter)
	    {
	    	boolean caracterValido = true;
	    	CharSequence seq = "'+^\\/=:,;ï¿½?*{}ï¿½$%&()@#|!ï¿½";
			 for (int i = 0; i < seq.length(); i++) 
			 {	 
			  if(caracter == seq.charAt(i))
			  {
				caracterValido = false;	
				break;
			  }
			 }
			 
			 return caracterValido;
	    }
		
	 public static FLyrVect Crear_Capa_en_Memoria(int type, IGeometry geom, String idF, String layerName)
	 {
		  ConcreteMemoryDriver driver = new ConcreteMemoryDriver();
		  driver.setShapeType(type);
		  List<String> campos = new ArrayList<String>();
		  campos.add("gid");
		  Value[] row = new Value[campos.size()];
		  driver.getTableModel().setColumnIdentifiers(campos.toArray());
		  
		  row[0] = ValueFactory.createValue(Integer.parseInt(idF));
		  driver.addGeometry(geom, row);
		  
		  FLyrVect ltemp = (FLyrVect)LayerFactory.createLayer(layerName, driver, Global.projeccionActiva);  //CRSFactory.getCRS("EPSG:"+Global.projection));
		  
		  return ltemp;
	 }
	 
	 public static double CalcularDistancia(double x_1, double y_1, double x_2, double y_2)
	 {
	 	    double res = Math.sqrt( Math.pow ((x_2-x_1),2) + Math.pow((y_2-y_1),2));
//	 	    DecimalFormat dosdecimales = new DecimalFormat("0.00");
//	 	    res = Double.parseDouble(dosdecimales.format(res));
	 	    
		    return res;
	 }
}
