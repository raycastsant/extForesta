package org.geocuba.foresta.herramientas.utiles;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.gvsig.raster.datastruct.ColorItem;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.DataSource;
import com.hardcode.gdbms.engine.values.NullValue;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.fmap.core.SymbologyFactory;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.XMLException;
import com.iver.cit.gvsig.fmap.layers.layerOperations.ClassifiableVectorial;
import com.iver.cit.gvsig.fmap.rendering.ILegend;
import com.iver.cit.gvsig.fmap.rendering.LegendFactory;
import com.iver.cit.gvsig.fmap.rendering.NullUniqueValue;
import com.iver.cit.gvsig.fmap.rendering.VectorialUniqueValueLegend;
import com.iver.cit.gvsig.fmap.rendering.ZSort;
import com.iver.cit.gvsig.gui.styling.JComboBoxColorScheme;
import com.iver.cit.gvsig.project.documents.view.legend.gui.JSymbolPreviewButton;
import com.iver.cit.gvsig.project.documents.view.legend.gui.SymbolTable;

public class VectorialUniqueValue {
	private static Logger logger = Logger.getLogger(VectorialUniqueValue.class.getName());

	public VectorialUniqueValueLegend theLegend;
	private ClassifiableVectorial layer;
	private SymbolTable symbolTable;
	private JSymbolPreviewButton defaultSymbolPrev;
	private VectorialUniqueValueLegend auxLegend;
	private JComboBoxColorScheme cmbColorScheme;
	private String field;
    private int idField;
    private String _selectField, _externTable, _filterField;
    private boolean _isString, _externalDesc;
    private HashMap<String,String> externalDesc;
	/**
	 *
	 */
	public VectorialUniqueValue(String fieldname) {
	 field = fieldname;	
	 cmbColorScheme = new JComboBoxColorScheme(false);
	 _externalDesc = false;
	}
	
	public VectorialUniqueValue(String fieldname, HashMap<String,String> extDescs) {
		 field = fieldname;	
		 cmbColorScheme = new JComboBoxColorScheme(false);
		 externalDesc = extDescs;
		 _externalDesc = externalDesc!=null;
		}

	public void fillTableValues() {
		DataSource elRs;

		try {
			elRs = ((FLyrVect) layer).getRecordset();
			logger.debug("elRs.start()");
			elRs.start();

			idField = -1;
			String fieldName = (String)field;
			if (fieldName==null) {
				JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),PluginServices.getText(this,"no_hay_campo_seleccionado"));
				return;
			}

			idField = elRs.getFieldIndexByName(fieldName);
			auxLegend = LegendFactory.createVectorialUniqueValueLegend(layer.getShapeType());
			auxLegend.setDefaultSymbol(defaultSymbolPrev.getSymbol());
			auxLegend.setClassifyingFieldNames(new String[] {fieldName});

			//long numReg = elRs.getRowCount();
			if (idField == -1) {
				NotificationManager.addWarning(
						PluginServices.getText(this, "unrecognized_field_name")+" " + fieldName, null);

				return;
			}

			symbolTable.removeAllItems();

			int numSymbols = 0;
			ISymbol theSymbol = null;

			Value clave;

			Random rand = new Random();
			int r = rand.nextInt(255);
			int g = rand.nextInt(255);
			int b = rand.nextInt(255);

			for (int j = 0; j < elRs.getRowCount(); j++) {
				clave = elRs.getFieldValue(j, idField);

				if (clave instanceof NullValue) {
					continue;
				}

				////Comprobar que no esta repetido y no hace falta introducir en el hashtable el campo junto con el simbolo.
				if (auxLegend.getSymbolByValue(clave) == null) {
					String value;
					
					if(_externalDesc)
					{
					 if(externalDesc == null)	
					  value = getDescValue(clave.toString());
					 else
                      value = externalDesc.get(clave.toString());  //Descripciones externas					  	 
					} 
					else
					 value = clave.toString();	
					
					Color c = new Color(r,g,b);
					
					theSymbol = SymbologyFactory.createDefaultSymbolByShapeType(layer.getShapeType(), c);//colorScheme[rand.nextInt(colorScheme.length)].getColor());
					theSymbol.setDescription(value);
					auxLegend.addSymbol(clave, theSymbol);

					numSymbols++;
					
					r = rand.nextInt(255);
					g = rand.nextInt(255);
					b = rand.nextInt(255);
					/*if (numSymbols == 100) {
						int resp = JOptionPane.showConfirmDialog(this,
								PluginServices.getText(this,
								"mas_de_100_simbolos"),
								PluginServices.getText(this, "quiere_continuar"),
								JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE);

						if ((resp == JOptionPane.NO_OPTION) ||
								(resp == JOptionPane.DEFAULT_OPTION)) {
							return;
						}
					}*/
				}
			} 

			symbolTable.fillTableFromSymbolList(auxLegend.getSymbols(),
					auxLegend.getValues(),auxLegend.getDescriptions());
			elRs.stop();
		} catch (ReadDriverException e) {
			NotificationManager.addError(PluginServices.getText(this, "recovering_recordset"), e);
		}
	}

	private boolean compareClassifyingFieldNames(String[] a, String[] b){
		if (a==b) return true;
		if (a == null || b == null) return false;
		if (a.length != b.length) return false;
		for (int i=0; i<a.length; i++){
			if (!a[i].equals(b[i])) return false;
		}
		return true;
	}

	private boolean compareClassifyingFieldTypes(int[] a, int[] b){
		if (a==b) return true;
		if (a == null || b == null) return false;
		if (a.length != b.length) return false;
		for (int i=0; i<a.length; i++){
			if (a[i]!=b[i]) return false;
		}
		return true;
	}
	/**
	 * A partir de los registros de la tabla, regenera el FRenderer. (No solo
	 * el symbolList, si no también el arrayKeys y el defaultRenderer
	 */
	private void fillSymbolListFromTable() {
		Value clave;
		ISymbol theSymbol;
		ArrayList<Value> visitedKeys = new ArrayList<Value>();
		boolean changedLegend = false;

		String fieldName = (String)field;
		String[] classifyingFieldNames = new String[] {fieldName};
		if(auxLegend!=null){
			if(!compareClassifyingFieldNames(classifyingFieldNames,auxLegend.getClassifyingFieldNames()))
			{
				auxLegend.setClassifyingFieldNames(classifyingFieldNames);
				changedLegend = true;
			}
		} 
		else {
			auxLegend.setClassifyingFieldNames(classifyingFieldNames);
			changedLegend = true;
		}

		FLyrVect m = (FLyrVect) layer;
		try {
            //AQUI HAY QUE PONER EL INDEX DEL CAMPO EN LA TABLA			
			int fieldType = m.getRecordset().getFieldType(idField);
			int[] classifyingFieldTypes = new int[] {fieldType};
			if(auxLegend!=null){
				if(!compareClassifyingFieldTypes(classifyingFieldTypes,auxLegend.getClassifyingFieldTypes()))
				{
					auxLegend.setClassifyingFieldTypes(classifyingFieldTypes);
					changedLegend = true;
				}
			}
			else {
				auxLegend.setClassifyingFieldTypes(classifyingFieldTypes);
				changedLegend = true;
			}
		} catch (ReadDriverException e) {
			NotificationManager.addError(PluginServices.getText(this, "could_not_setup_legend"), e);
		} catch (Exception e) {
			NotificationManager.showMessageWarning(PluginServices.getText(this, "could_not_setup_legend"), e);
		}

		if(changedLegend){
			auxLegend.clear();
		}

		for (int row = 0; row < symbolTable.getRowCount(); row++) {
			clave = (Value) symbolTable.getFieldValue(row, 1);
			theSymbol = (ISymbol) symbolTable.getFieldValue(row, 0);
			String description = (String) symbolTable.getFieldValue(row, 2);
			theSymbol.setDescription(description);
			ISymbol legendSymbol = null;
			if (auxLegend != null){
				legendSymbol = auxLegend.getSymbolByValue(clave);
			}
			if( legendSymbol == null || ( auxLegend.isUseDefaultSymbol() && legendSymbol == auxLegend.getDefaultSymbol())){
				if (auxLegend != null){
					auxLegend.addSymbol(clave, theSymbol);
				}
			} else {
				/* FIXME: Se optimizaría descomentarizando el if, pero el metodo equals del AbstractSymbol
				 * no tiene en cuenta determinadas propiedades del simbolo, como, por ejemplo, el tamaño.
				 * Descomentarizar al arreglar el metodo equals del AbstractSymbol.
				 */
//				if(!legendSymbol.equals(theSymbol)){
					auxLegend.replace(legendSymbol, theSymbol);
//				}
			}
			visitedKeys.add(clave);
		}
		if(auxLegend != null){
			Object[] keys = auxLegend.getValues();
			for(int i=0; i<keys.length; i++){
				Object key = keys[i];
				if(!visitedKeys.contains(key)){
					auxLegend.delSymbol(key);
				}
			}
		}

		clave = new NullUniqueValue();
					if (auxLegend != null){
				ISymbol legendSymbol = auxLegend.getSymbolByValue(clave);
				if( legendSymbol != null){
					auxLegend.replace(legendSymbol, null);
				}
		}
	}

	/*private void fillFieldNames() {
		SelectableDataSource rs;
		
		try {
			// rs = ((FLyrVect) layer).getSource().getRecordset();
			rs = ((FLyrVect) layer).getRecordset(); // Todos los campos, también los de uniones
			logger.debug("rs.start()");
			rs.start();
			int fieldCount=rs.getFieldCount();
			String[] nomFields = new String[fieldCount];

			for (int i = 0; i < fieldCount; i++) {
				nomFields[i] = rs.getFieldAlias(i);
			}

			rs.stop();

			// fieldsListValor.setSelectedIndex(0);
		} catch (ReadDriverException e) {
			NotificationManager.addError(PluginServices.getText(this, "recovering_recordset"), e);
		}
	}*/

	private void setColorScheme(){

		if(auxLegend.getColorScheme() != null) {
			ColorItem[] colors = new ColorItem[auxLegend.getColorScheme().length];
			for (int i = 0; i < auxLegend.getColorScheme().length; i++) {
				colors[i] = new ColorItem();
				colors[i].setColor(auxLegend.getColorScheme()[i]);
			}
			cmbColorScheme.setSelectedColors(colors);
		}
	}
	
	public void setData(FLayer layer, ILegend legend) {
		this.layer = (ClassifiableVectorial) layer;
		int shapeType = 0;
		try {
			shapeType = this.layer.getShapeType();
		} catch (ReadDriverException e) {
			NotificationManager.addError(PluginServices.getText(this, "accessing_to_the_layer"), e);
		}

		getDefaultSymbolPrev(shapeType);

		symbolTable = new SymbolTable(null, SymbolTable.VALUES_TYPE, shapeType);

		//fillFieldNames();

		symbolTable.removeAllItems();

		if (VectorialUniqueValueLegend.class.equals(legend.getClass())) {
			try {
				auxLegend = (VectorialUniqueValueLegend) legend.cloneLegend();
				//FIXME: parche
				ZSort legendZSort = ((VectorialUniqueValueLegend) legend).getZSort();
				if(legendZSort != null){
					ZSort auxZSort = new ZSort(auxLegend);
					auxZSort.copyLevels(legendZSort);
					auxZSort.setUsingZSort(legendZSort.isUsingZSort());
					auxLegend.setZSort(auxZSort);
				}
			} catch (XMLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setColorScheme();
			symbolTable.fillTableFromSymbolList(auxLegend.getSymbols(),
					auxLegend.getValues(),auxLegend.getDescriptions());
		} else {
			auxLegend = new VectorialUniqueValueLegend(shapeType);
		}
		
		defaultSymbolPrev.setSymbol(legend.getDefaultSymbol());
	}

	public ILegend getLegend(boolean defaultvalues) {
		fillSymbolListFromTable();

		if (auxLegend != null) 
		{
			ISymbol defaultSymbolLegend = auxLegend.getDefaultSymbol();
			ISymbol symbol = defaultSymbolPrev.getSymbol();
			if(symbol != null){
				if(symbol!=defaultSymbolLegend){
					auxLegend.setDefaultSymbol(symbol);
				}
			}
			auxLegend.useDefaultSymbol(defaultvalues); //Valores por defecto

			try {
				theLegend = (VectorialUniqueValueLegend) auxLegend.cloneLegend();
			} catch (XMLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//FIXME: parche
			ZSort auxZSort = ((VectorialUniqueValueLegend) auxLegend).getZSort();
			if(auxZSort != null){
				ZSort legendZSort = new ZSort(theLegend);
				legendZSort.copyLevels(auxZSort);
				legendZSort.setUsingZSort(auxZSort.isUsingZSort());
				theLegend.setZSort(legendZSort);
			}
			//Fin del parche
		}

		return theLegend;
	}

	private void getDefaultSymbolPrev(int shapeType) {
		if(defaultSymbolPrev == null){
			defaultSymbolPrev = new JSymbolPreviewButton(shapeType);
			defaultSymbolPrev.setPreferredSize(new Dimension(110,20));
		}
	}
	
	public boolean isSuitableFor(FLayer layer) {
		return (layer instanceof FLyrVect);
	}

	/**
	 *Metodo para mostar la descripcion de la leyenda desde una tabla relacionada
	 * selectField: campo descripcion a seleccionar
	 * externTable: tabla externa relacionada
	 * filterField: campo clave mediante el cual se relacionan las tablas*/
	public void setLegendDescription_FromRelatedTable(String selectField, String externTable, String filterField, boolean isString)
	{
	 _selectField = selectField;
	 _externTable = externTable;
	 _filterField = filterField;
	 
	 _isString = isString;
	 _externalDesc = true;
	}
	
	/**Metodo para conseguir un valor desde otra tabla*/
	private String getDescValue(String filter)
	{
	 String value = "";
	 String comilla ="";
	 if(_isString)
	  comilla = "'";	 
		 
	 JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	 db.ejecutarConsulta("select "+ _selectField +" from "+ _externTable +" where "+ _filterField +"="+comilla+ filter +comilla);
	 System.out.println("select "+ _selectField +" from "+ _externTable +" where "+ _filterField +"="+comilla+ filter +comilla);
	 if(!db.isEmpty())
	  value = db.getValueAt(0,0).toString();
	 
	/* try {
		db.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
	 return value;
	}
	
}
