package org.geocuba.foresta.reportes;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.Proyecto_Reforestacion;
import org.geocuba.foresta.herramientas.utiles.PrintReport;
import org.geocuba.foresta.herramientas.writerTasks.BackgroundExecution;
import com.iver.andami.PluginServices;
import com.iver.utiles.swing.threads.AbstractMonitorableTask;

 public class Proyecto_reforestacionWT extends AbstractMonitorableTask
 {
	 private boolean ok = false; 
	 private JDBCAdapter db;
	 private String gids;
	 //private String id;
	 //private double plant;
	 private String table;
	 private String ancho;
	 private String ftable;
	 private String areaElemento;
	 private String medSuelos;
	 private String medPastoreo;
	 private String medIncendios;
	 private Proyecto_Reforestacion proyecto;
 
	public Proyecto_reforestacionWT(String tableFajas, String tableHidro, String _areaElemento, 
			Proyecto_Reforestacion _proyecto, String _medSuelos, String _medPastoreo, String _medIncendios)
    {
	 //id = pid;
	 table = tableHidro;
	 ftable = tableFajas;
	 areaElemento = _areaElemento;
	 proyecto = _proyecto;
	 gids = proyecto.getFaja().getGid().toString();
	 ancho = proyecto.getFaja().getAncho().toString();
	 medSuelos = _medSuelos;
	 medPastoreo = _medPastoreo;
	 medIncendios = _medIncendios;
	 
       db = new JDBCAdapter(ConnectionExt.getConexionActiva());
       
//       plant = Double.parseDouble(jTFMarcoX.getText()) * Double.parseDouble(jTFMarcoY.getText());
//       if(plant <= 0)
//       {	 
//	    JOptionPane.showMessageDialog(null, "Error en los valores del marco de plantaci�n");
//	    return;
//	   }		   
		
       ok = true;
       
		setInitialStep(0);
		setDeterminatedProcess(false);
		setStatusMessage(PluginServices.getText(this, "Generando reporte..."));
		setFinalStep(1);	
    }
	
	public void run() throws Exception 
	{      if(ok)
      {	  
  	  //Busco los datos de los suelos que atraviesa la faja
//       String tipoSuelos = "";
//       double profeft = 0;
//       double ph = 0;
//       double matorg = 0;
       double pendiente = 0;
       
     /** if(!jCBDatoSuelos.isSelected())
      {	*/  
//       db.ejecutarConsulta("select distinct tipo_suelo.tipo from (tipo_suelo inner join suelos on tipo_suelo.id=suelos.tipo) " +
//       		"inner join "+ ftable +" on st_intersects("+ ftable +".the_geom, suelos.the_geom) where "+ ftable +".gid in("+gids+")");
//       for(int i=0; i<db.getRowCount(); i++)
//    	tipoSuelos += db.getValueAt(i, 0).toString()+",";

//       db.ejecutarConsulta("select distinct suelos.profundidadefectiva from suelos inner join "+ ftable +" on " +
//       		"st_intersects("+ ftable +".the_geom, suelos.the_geom) where "+ ftable +".gid in("+gids+")");
//       for(int i=0; i<db.getRowCount(); i++)
//       {	   
//        Double pe = Double.parseDouble(db.getValueAt(i, 0).toString());
//    	if(pe > profeft)
//    	 profeft = pe;
//       }	
//    	
//       db.ejecutarConsulta("select distinct suelos.ph from suelos inner join "+ ftable +" on " +
//       		"st_intersects("+ ftable +".the_geom, suelos.the_geom) where "+ ftable +".gid in("+gids+")");
//       for(int i=0; i<db.getRowCount(); i++)
//       {	
//    	Double PH = Double.parseDouble(db.getValueAt(i, 0).toString());
//    	if(PH > ph)
//    	 ph = PH;
//       }	
    	
       String sql = "select distinct _suelos.pendiente from _suelos inner join "+ ftable +" on " +
  		"st_intersects("+ ftable +".the_geom, _suelos.the_geom) where "+ ftable +".gid in("+gids+")";
       System.out.println(sql);
       db.ejecutarConsulta(sql);
       for(int i=0; i<db.getRowCount(); i++)
       {	
//    	Double mo = Double.parseDouble(db.getValueAt(i, 0).toString());
//    	if(mo > matorg)
//    	 matorg = mo;
    	
    	Double pend = Double.parseDouble(db.getValueAt(i,0).toString());
    	if(pend > pendiente)
    	 pendiente = pend;
       }	
       
//       if(!tipoSuelos.equals("")) 
//        tipoSuelos = tipoSuelos.substring(0, tipoSuelos.length()-1);
       
//       if(!profeft.equals("0"))
//        profeft = profeft.substring(0, profeft.length()-1);
//       
//       if(!ph.equals("0"))
//        ph = ph.substring(0, ph.length()-1);
//       
//       if(!matorg.equals("0"))
//        matorg = matorg.substring(0, matorg.length()-1);
//       
//       if(!pendiente.equals("0"))
//    	pendiente = pendiente.substring(0, pendiente.length()-1);
     /** }
      else
      {
       tipoSuelos = jTFTiposuelos.getText();
	   profeft = Double.parseDouble(jTFProfefect.getText());
	   ph = Double.parseDouble(jTFPH.getText());
	   matorg = Double.parseDouble(jTFMatorg.getText());   
      }	  */
     
     //Si es proyecto tecnico general o no
//    	String proytgen;
//    	if(jCBProyTec.isSelected())
//    	 proytgen = "Si";
//    	else
//         proytgen = "No";	
    	
    //Hallo el porciento de pendiente
//    	db.executeQuery("select distinct f_pendiente.pend from f_pendiente inner join "+ ftable +" on st_intersects(f_pendiente.the_geom,"+ ftable +".the_geom) where "+ ftable +".gid in("+gids+")");
//    	double mayorPend = 0;
//    	for(int i = 0; i<db.getRowCount(); i++)
//    	{
//    	 double val = Double.parseDouble(db.getValueAt(0,0).toString());	
//    	 if(val > mayorPend)
//    	  mayorPend = val;
//    	}	  
//    	
//    	//convierto a %
//    	mayorPend = Math.round(Math.tan(Math.toRadians(mayorPend))*100);
    	
      //Busco los datos de las medidas
//    	String medSuelos = "";
//    	String medPastoreo = "";
//    	String medIncendios = "";
//     	
//    	for(int i=0; i<jPConservSuelos.getComponentCount(); i++)
//    	{	
//    	 if(((javax.swing.JCheckBox)jPConservSuelos.getComponent(i)).isSelected())	
//    	  medSuelos += ((javax.swing.JCheckBox)jPConservSuelos.getComponent(i)).getText()+",";
//    	} 
//    	if(!medSuelos.equals(""))
//    	 medSuelos = medSuelos.substring(0,medSuelos.length()-1);
//    	
//    	for(int i=0; i<jPPastoreo.getComponentCount(); i++)
//    	{	
//    	 if(((javax.swing.JCheckBox)jPPastoreo.getComponent(i)).isSelected())	
//    	  medPastoreo += ((javax.swing.JCheckBox)jPPastoreo.getComponent(i)).getText()+",";
//    	} 
//    	if(!medPastoreo.equals(""))
//    	 medPastoreo = medPastoreo.substring(0,medPastoreo.length()-1);
//    	
//    	for(int i=0; i<jPIncendios.getComponentCount(); i++)
//    	{	
//    	 if(((javax.swing.JCheckBox)jPIncendios.getComponent(i)).isSelected())	
//    	  medIncendios += ((javax.swing.JCheckBox)jPIncendios.getComponent(i)).getText()+",";
//    	}
//    	if(!medIncendios.equals(""))
//    	 medIncendios = medIncendios.substring(0,medIncendios.length()-1);
    	
       //Especies
          String composicion = "";
		  if(proyecto.getListaespecies()!=null)
		  {
			  for(int i=0; i<proyecto.getListaespecies().length; i++)
				  composicion += proyecto.getListaespecies()[i].getSiglas()+", ";
			   
			  if(composicion.indexOf(",") > 0)
			   composicion = composicion.substring(0, composicion.length()-2);
		  }	 
		  
		//Suelos
          String tipoSuelos = "";
		  if(proyecto.getListaSuelos()!=null)
		  {
			  for(int i=0; i<proyecto.getListaSuelos().length; i++)
			   tipoSuelos += proyecto.getListaSuelos()[i].getTipo()+", ";
			   
			  if(tipoSuelos.indexOf(",") > 0)
			   tipoSuelos = tipoSuelos.substring(0, tipoSuelos.length()-2);
		  }	
		  
        String query = "select cuencas.nombre as cuenca, "+ ftable +".nombre_lugar as faja, provincias.nombre as provincia, municipios.nombre as municipio, " +
         "nombre_productor as productor, organismo, proyecto.nombre_lugar as nomblugar, tipo_hidrografia.tipo_elemento as tipohidro, hidrografia.nombre as rio_embalse, " +
         "area_reforestar as areareforest,"+ pendiente +" as pendiente, precipitacion as precmedia, '"+ tipoSuelos +"' as suelos, profundidad_efectiva as profefect, " +
         " ph, materiaorganica as matorg, tipo_erosion_suelo as tipoerosion, grado_erosion_suelo as gradoerosion, vegetacion as vegetacion, " +
         "densidad||'. '||observaciones_veg as densidadveg, '"+ ancho +"' as ancho, tipo_hidrografia.tipo_elemento as tipo, case when tipo_hidrografia.tipo_elemento='embalse' " +
         "then round(CAST(ST_perimeter("+table+".the_geom) as numeric),2) when tipo_hidrografia.tipo_elemento='rio' then " +
         "round(CAST(ST_Length("+table+".the_geom) as numeric),2) end as long_perim,"+ areaElemento +" AS area, case proyecto_general when true then 'Si' else 'No' end as proyTecnicoGen, " +
         "especificacion as proyectespecif, metodo_plantacion as metodoplant, '"+ composicion +"' as compespecies, marcox, marcoy, " +
         "round(CAST((10000/(marcox*marcoy)) as numeric),2) as plantacion, hileras, '"+ medSuelos +"' as medconserv, '"+ medIncendios +"' as medincendios, '"+ 
         medPastoreo +"' as medpastoreo, tenente as istenente " +
         "from (((((("+ ftable +" inner join proyecto on proyecto.faja="+ ftable +".gid)inner join "+table+" on "+ ftable +".hidrografia="+table+".tipo_hidrografia)left join hidrografia on "+
         table+".tipo_hidrografia=hidrografia.id) left join tipo_hidrografia on hidrografia.tipo_hidrografia=tipo_hidrografia.id) " +
         " left join municipios on st_intersects("+ftable+".the_geom, municipios.the_geom)) " +
         "left join provincias on municipios.provincia=provincias.gid)inner join cuencas on st_intersects(cuencas.the_geom, municipios.the_geom) Where "+ ftable +".gid in("+gids+") " +
         "and st_intersects("+table+".the_geom, cuencas.the_geom)  and proyecto.id="+proyecto.getId()+
         " Group by cuencas.nombre, "+ ftable +".nombre_lugar, provincias.nombre, municipios.nombre, hidrografia.nombre, "+table+".the_geom, hidrografia.tipo_hidrografia, " +
         "tipo_hidrografia.tipo_elemento, nombre_productor, organismo, proyecto.nombre_lugar, area_reforestar, precipitacion, profundidad_efectiva, ph, materiaorganica, " +
         "tipo_erosion_suelo, grado_erosion_suelo, vegetacion, densidad, observaciones_veg, proyecto_general, especificacion, metodo_plantacion, marcox, marcoy, hileras, tenente";
	   
        System.out.println(query);
       
        //Actualizo la ficha de costo
         db.ejecutarConsulta("delete from f_tfichac");
         db.ejecutarConsulta("insert into f_tfichac (select actividades, um, cantidad, cup, cuc, total " +
          		"from ficha_costo where proyecto="+proyecto.getId()+")");
          
         db.ejecutarConsulta("insert into f_tfichac values('TOTAL','',(select sum(cantidad) from f_tfichac),(select sum(cup) from f_tfichac)," +
         		         "(select sum(cuc) from f_tfichac),(select sum(total) from f_tfichac))");
         
//         Map<String, Object> parameters = new HashMap();
//         parameters.put("sql", query);
//         parameters.put("idproyecto", proyecto.getId());
         
		 BackgroundExecution.cancelableBackgroundExecution(new PrintReport("Proy_Reforestacion", query, null));
      }		
      reportStep();
	}
	
	 public void finished()
	 {
			PluginServices.getMainFrame().enableControls();
	 } 
	 
	 public boolean isOK()
	 {
		 return ok;
	 }
}
