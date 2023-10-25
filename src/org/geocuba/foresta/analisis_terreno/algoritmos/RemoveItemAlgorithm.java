package org.geocuba.foresta.analisis_terreno.algoritmos;

import es.unex.sextante.additionalInfo.AdditionalInfoVectorLayer;
import es.unex.sextante.core.GeoAlgorithm;
import es.unex.sextante.core.Sextante;
import es.unex.sextante.dataObjects.IFeature;
import es.unex.sextante.dataObjects.IFeatureIterator;
import es.unex.sextante.dataObjects.IVectorLayer;
import es.unex.sextante.exceptions.GeoAlgorithmExecutionException;
import es.unex.sextante.exceptions.RepeatedParameterNameException;

/**Elimina un elemento segun un campo y un valor dados*/
public class RemoveItemAlgorithm extends GeoAlgorithm {

   public static final String LAYER                      = "LAYER";
   public static final String RESULT                     = "RESULT";
   public static final String FIELD                      = "FIELD";
   public static final String VALUE                      = "VALUE";

   private IVectorLayer       m_Output;
   private String             m_Field;
   private String             m_Value;
   private int                shpType;         


   @Override
   public boolean processAlgorithm() throws GeoAlgorithmExecutionException {

      final IVectorLayer layerIn = m_Parameters.getParameterValueAsVectorLayer(LAYER);
      shpType = layerIn.getShapeType();
      
      m_Field = m_Parameters.getParameterValueAsString(FIELD);
      m_Value = m_Parameters.getParameterValueAsString(VALUE);

      final int fieldIndex = layerIn.getFieldIndexByName(m_Field);
      final int iFieldCount = layerIn.getFieldCount();
      
      Class types[] = new Class[iFieldCount];
      String[]  sFieldNames = new String[iFieldCount];

      for (int i = 0; i < iFieldCount; i++) 
      {
       sFieldNames[i] = layerIn.getFieldName(i);
       types[i] = layerIn.getFieldType(i);
      }

      m_Output = getNewVectorLayer("RESULT", "LayerOut", layerIn.getShapeType(), types, sFieldNames);

      int i = 0;
      final int iTotal = layerIn.getShapesCount();
      final IFeatureIterator iter = layerIn.iterator();
      while (iter.hasNext() && setProgress(i, iTotal)) 
      {
         final IFeature feature = iter.next();
         if(feature.getRecord().getValue(fieldIndex).toString().equals(m_Value))
          continue;
         
         m_Output.addFeature(feature.getGeometry(), feature.getRecord().getValues());
         i++;
      }
      iter.close();

      return !m_Task.isCanceled();
   }


   @Override
   public void defineCharacteristics() {

      //final String[] sValues = {"0","100"};
      //final String[] sType = { Sextante.getText("Outer_buffer"), Sextante.getText("Inner_buffer"), Sextante.getText("Both"), };

      setName(Sextante.getText("RemoveElement"));
      //setGroup(Sextante.getText("Buffers"));

      try {
         m_Parameters.addInputVectorLayer(LAYER, Sextante.getText("Input_layer"), AdditionalInfoVectorLayer.SHAPE_TYPE_ANY, true);
         m_Parameters.addString(FIELD, Sextante.getText("Campo que contiene el valor a eliminar"), "gid");
         m_Parameters.addString(VALUE, Sextante.getText("Valor a eliminar"), "0");

         addOutputVectorLayer(RESULT, Sextante.getText("LayerOut"), shpType);
      }
      catch (final RepeatedParameterNameException e) {
         Sextante.addErrorToLog(e);
      }

   }

}
