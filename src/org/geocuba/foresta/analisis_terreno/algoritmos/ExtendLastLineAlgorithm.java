package org.geocuba.foresta.analisis_terreno.algoritmos;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import es.unex.sextante.additionalInfo.AdditionalInfoNumericalValue;
import es.unex.sextante.additionalInfo.AdditionalInfoVectorLayer;
import es.unex.sextante.core.GeoAlgorithm;
import es.unex.sextante.core.Sextante;
import es.unex.sextante.dataObjects.IFeature;
import es.unex.sextante.dataObjects.IFeatureIterator;
import es.unex.sextante.dataObjects.IVectorLayer;
import es.unex.sextante.exceptions.GeoAlgorithmExecutionException;
import es.unex.sextante.exceptions.NullParameterValueException;
import es.unex.sextante.exceptions.RepeatedParameterNameException;
import es.unex.sextante.exceptions.WrongParameterIDException;
import es.unex.sextante.exceptions.WrongParameterTypeException;
import es.unex.sextante.outputs.OutputVectorLayer;

/**
*
* @author Raisel
*/

public class ExtendLastLineAlgorithm extends GeoAlgorithm {

   public static final String RESULT = "RESULT";
   public static final String LINES  = "LINES";
   public static final String LARGE  = "LARGE";
   
   private IVectorLayer       m_Output;


   @Override
   public void defineCharacteristics() {

      setName(Sextante.getText("Split_polylines_at_nodes"));
      setGroup(Sextante.getText("Tools_for_line_layers"));

      try {
         m_Parameters.addInputVectorLayer(LINES, Sextante.getText("Lines"), AdditionalInfoVectorLayer.SHAPE_TYPE_LINE, true);
         m_Parameters.addNumericalValue(LARGE, Sextante.getText("Extended segments length"), 0.01,
                 AdditionalInfoNumericalValue.NUMERICAL_VALUE_DOUBLE);
         addOutputVectorLayer(RESULT, Sextante.getText("Lines"), OutputVectorLayer.SHAPE_TYPE_LINE);
      }
      catch (final RepeatedParameterNameException e) {
         Sextante.addErrorToLog(e);
      }

   }

   @Override
   public boolean processAlgorithm() throws GeoAlgorithmExecutionException {

      final IVectorLayer lines = m_Parameters.getParameterValueAsVectorLayer(LINES);

      m_Output = getNewVectorLayer(RESULT, Sextante.getText("Result"), IVectorLayer.SHAPE_TYPE_LINE, lines.getFieldTypes(),
               lines.getFieldNames());

      int i = 0;
      final int iShapeCount = lines.getShapesCount();
      final IFeatureIterator iter = lines.iterator();
      while (iter.hasNext() && setProgress(i, iShapeCount)) {
         final IFeature feature = iter.next();
         final Geometry geom = feature.getGeometry();
         for (int j = 0; j < geom.getNumGeometries(); j++) {
            final Geometry line = geom.getGeometryN(j); //numero de lineas seleccionadas
            addLine(line, feature.getRecord().getValues()); //aqui es donde se separan
         }
         i++;
      }
      iter.close();

      return !m_Task.isCanceled();

   }


   private void addLine(final Geometry line,
                        final Object[] attributes) throws WrongParameterTypeException, WrongParameterIDException, NullParameterValueException{
	   
	  double x0;
	  double y0;
	  double x1;
	  double y1;
	  //double x2;
	  //double y2;
	  
      final GeometryFactory gf = new GeometryFactory();

      final Coordinate[] coords = line.getCoordinates();
      final Coordinate[] segmentCoords = new Coordinate[2];

    /*  segmentCoords[0] = new Coordinate(coords[0].x, coords[0].y);
      segmentCoords[1] = new Coordinate(coords[1].x, coords[1].y);
      m_Output.addFeature(gf.createLineString(segmentCoords), attributes);
      
      segmentCoords[0] = new Coordinate(coords[coords.length-1].x, coords[coords.length-1].y);
      segmentCoords[1] = new Coordinate(coords[coords.length-2].x, coords[coords.length-2].y);
      m_Output.addFeature(gf.createLineString(segmentCoords), attributes);*/
      
    //Extiendo el primer segmento 
      x0 = coords[1].x;
      y0 = coords[1].y;
      x1 = coords[0].x;
      y1 = coords[0].y;
    
      Coordinate coord = new Coordinate(getLastCoordinates(x0, y0, x1, y1));
      
      segmentCoords[0] = new Coordinate(x1, y1);
      segmentCoords[1] = new Coordinate(coord.x, coord.y);
      m_Output.addFeature(gf.createLineString(segmentCoords), attributes);
      
      /*for (int i = 0; i < coords.length - 1; i++) 
      {
       segmentCoords[0] = new Coordinate(coords[i].x, coords[i].y);
       segmentCoords[1] = new Coordinate(coords[i + 1].x, coords[i + 1].y);
       m_Output.addFeature(gf.createLineString(segmentCoords), attributes);
      }*/
      
      //Extiendo el segundo segmento 
      x0 = coords[coords.length-2].x;
      y0 = coords[coords.length-2].y;
      x1 = coords[coords.length-1].x;
      y1 = coords[coords.length-1].y;
    
      coord = new Coordinate(getLastCoordinates(x0, y0, x1, y1));
      
      segmentCoords[0] = new Coordinate(x1, y1);
      segmentCoords[1] = new Coordinate(coord.x, coord.y);
      m_Output.addFeature(gf.createLineString(segmentCoords), attributes);

   }
   
   /**Devuelve las coordenadas del ultimo punto del segmento a crear
 * @throws NullParameterValueException 
 * @throws WrongParameterIDException 
 * @throws WrongParameterTypeException 
 * @throws NullParameterValueException 
 * @throws WrongParameterIDException 
 * @throws WrongParameterTypeException */
   private Coordinate getLastCoordinates(double x0, double y0, double x1, double y1) throws WrongParameterTypeException, WrongParameterIDException, NullParameterValueException
   {
	   double x2;
	   double y2;
	   double a;
	   double b;
		  //double c;
		 // double angle;
		 // double e;
		 // double d;
	   double m;
	   double L;
	   
		L = m_Parameters.getParameterValueAsDouble(LARGE);
		
	      a = x1-x0;
	      b = y1-y0;
	      m = b/a; //Hallo la pendiente
	      
	     /* c = Math.sqrt(a*a + b*b); //Distancia recta 1
	      angle = Math.asin(b/c);
	      angle = angle*57.32484076433121019; //(180/3.14)  convierto a grados
	      
	      if(angle < 0)
	       angle = angle + 180;  //Modulo	  
	      
	      /*
	      e = 20*Math.sin(angle);
	      d = Math.sqrt((20*20)-(e*e));
	      
	      x2 = d+x1;
	      y2 = e+y1;*/
	      
	     // m = (y1-y0)/(x1-x0);
	      //x2 = x1 + Math.sqrt(400/((m*m)+1));
	      //y2 = y1 + (m*(x2-x1));
	      
	     // if((angle>=0 && angle<90) || (angle>=270 && angle<=360)) //Si esta en el 1ro o 4to cuadrante
	      
	      if(x0 < x1) // Esta en el 1ro o 4to cuadrante
	      {	  
	       x2 = x1 + Math.sqrt((L*L) / ((m*m)+1));
	       y2 = y1 + (m*(x2-x1));
	      }
	      else //Si esta en el 2ro o 3er cuadrante
	      {	  
	       x2 = x1 - Math.sqrt((L*L) / ((m*m)+1));
	       y2 = y1 + (m*(x2-x1));
	      }	
	      
	   return new Coordinate(x2, y2);   
   }
}
