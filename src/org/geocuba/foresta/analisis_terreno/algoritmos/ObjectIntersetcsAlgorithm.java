package org.geocuba.foresta.analisis_terreno.algoritmos;

import com.vividsolutions.jts.geom.Geometry;
import es.unex.sextante.additionalInfo.AdditionalInfoVectorLayer;
import es.unex.sextante.core.GeoAlgorithm;
import es.unex.sextante.core.Sextante;
import es.unex.sextante.dataObjects.IFeature;
import es.unex.sextante.dataObjects.IFeatureIterator;
import es.unex.sextante.dataObjects.IVectorLayer;
import es.unex.sextante.exceptions.GeoAlgorithmExecutionException;
import es.unex.sextante.exceptions.RepeatedParameterNameException;

/**
* 
* @author Raisel
* 
* Devuelve los objetos que intersectan con la segunda geometria pasada*/
public class ObjectIntersetcsAlgorithm extends GeoAlgorithm {

   public static final String RESULT = "RESULT";
   public static final String LAYER1 = "LAYER1";
   public static final String LAYER2 = "LAYER2";

   private IVectorLayer       m_layer1;
   private IVectorLayer       m_layer2;
   private IVectorLayer       m_Result;

   @Override
   public boolean processAlgorithm() throws GeoAlgorithmExecutionException {

	   m_layer1 = m_Parameters.getParameterValueAsVectorLayer(LAYER1);
	   m_layer2 = m_Parameters.getParameterValueAsVectorLayer(LAYER2);

      if (m_layer1.getShapesCount()== 0  ||  m_layer2.getShapesCount()== 0) 
      {
         throw new GeoAlgorithmExecutionException("zero shapes in layer");
      }

      m_Result = getNewVectorLayer(RESULT, Sextante.getText("Intersect_objects"), m_layer1.getShapeType(), m_layer1.getFieldTypes(), m_layer1.getFieldNames());

      final IFeatureIterator iter = m_layer1.iterator();
      IFeatureIterator iter2 = m_layer2.iterator();
      
      while (iter.hasNext())
      {
          final IFeature feature = iter.next();
          final Geometry g = feature.getGeometry();
          iter2 = m_layer2.iterator();
          
          while (iter2.hasNext()) 
          {
             final IFeature feature2 = iter2.next();
             final Geometry g2 = feature2.getGeometry();
             if (g2.intersects(g)) 
             {
               // final Geometry inter = g.intersection(g2);
                m_Result.addFeature(g, feature.getRecord().getValues());
                break;
             }
          }
       }
      
       iter.close();
       iter2.close();

      return !m_Task.isCanceled();
   }

@Override
   public void defineCharacteristics() {

      setName(Sextante.getText("Intersection_Objects"));
      setGroup(Sextante.getText("Utils"));

      try {
         m_Parameters.addInputVectorLayer(LAYER1, Sextante.getText("Layer to extract elements"), AdditionalInfoVectorLayer.SHAPE_TYPE_ANY, true);
         m_Parameters.addInputVectorLayer(LAYER2, Sextante.getText("Layer to interset"), AdditionalInfoVectorLayer.SHAPE_TYPE_ANY, true);

         addOutputVectorLayer(RESULT, Sextante.getText("Intersect_objects"), AdditionalInfoVectorLayer.SHAPE_TYPE_ANY);
      }
      catch (final RepeatedParameterNameException e) {
         Sextante.addErrorToLog(e);
      }

   }

}
