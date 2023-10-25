package org.geocuba.foresta.fajas;

import org.geocuba.foresta.administracion_seguridad.db.JDBCAdapter;
import org.geocuba.foresta.administracion_seguridad.extensiones.ConnectionExt;
import org.geocuba.foresta.gestion_datos.IPersistenObjectManager;
import org.geocuba.foresta.gestion_datos.Parcela;
import org.geocuba.foresta.gestion_datos.ParcelaManager;
import org.geocuba.foresta.herramientas.utiles.Funciones_Utiles;
import org.geocuba.foresta.herramientas.utiles.Global;

public class SubtramosManager implements IPersistenObjectManager
{
	@Override
	public SubtramoPerfil Cargar_Objeto_BD(int gid) //throws ReadDriverException 
	{
		 SubtramoPerfil subtramo = null;		
	     JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	     db.ejecutarConsulta("select perfil, parcela, pendiente, vel_infiltr_suelo, st_length(the_geom) from sub_tramos_perfil where gid="+gid);
	     
	     if(!db.isEmpty())
	     {
	      int idPerfil = db.getValueAsInteger(0,0); 	 
		  
		  int idParcela = db.getValueAsInteger(0,1); 	 
	      ParcelaManager pam = new ParcelaManager();
		  Parcela parcela = pam.Cargar_Objeto_BD(idParcela);
	      
		  double pendiente = db.getValueAsDouble(0,2);
		  double velocidadI = db.getValueAsDouble(0,3);
		  double longitud = db.getValueAsDouble(0,4);
		  
	      subtramo = new SubtramoPerfil(gid, pendiente, idPerfil, velocidadI, parcela, longitud, false);
	     } 	 
	     return subtramo;
	}
	
	public SubtramoPerfil [] Cargar_Subtramos(String  condicion) //throws ReadDriverException 
	{
		 SubtramoPerfil [] listaSubtramos = null;		
	     JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
	     db.ejecutarConsulta("select perfil, parcela, pendiente, vel_infiltr_suelo, st_length(the_geom), gid from sub_tramos_perfil "+condicion);
	     
	     if(!db.isEmpty())
	     {
	      try {
	    	  
	    	  int cant = db.getRowCount();	 
	    	  listaSubtramos = new SubtramoPerfil[cant];
	    	  
	    	  for(int i=0; i<cant; i++)
	    	  {  
	    	   int idPerfil = db.getValueAsInteger(i,0); 	 
			   int idParcela = db.getValueAsInteger(i,1); 	 
		       ParcelaManager pam = new ParcelaManager();
			   Parcela parcela = pam.Cargar_Objeto_BD(idParcela);
			   double pendiente = db.getValueAsDouble(i,2);
			   double velocidadI = db.getValueAsDouble(i,3);
			   double longitud = db.getValueAsDouble(i,4);
			   int gid = db.getValueAsInteger(i,5);
			  
			   listaSubtramos[i] = new SubtramoPerfil(gid, pendiente, idPerfil, velocidadI, parcela, longitud, false);
	    	  } 
	      
	      } catch (java.lang.NullPointerException e) {
				e.printStackTrace();
				return null;
	     }
	     } 	 
	     return listaSubtramos;
	}
	
	public static String getQueryTablaSubtramos(int idParteaguas)
	 {
		 return "select sub_tramos_perfil.gid as subtramo, perfiles.gid as perfil, case perfiles.orilla when 'I' then 'Izquierda' else 'Derecha' end as orilla, " +
	 		"perfiles.ancho, sub_tramos_perfil.pendiente, tipo_suelo.tipo as tipo_suelo, agrupadores_uso.tipo_uso as uso_suelo, textura_suelos.textura, " +
	 		"_suelos.materiaorganica, _suelos.estructura, sub_tramos_perfil.vel_infiltr_suelo from ((((((perfiles inner join sub_tramos_perfil on " +
	 		"perfiles.gid=sub_tramos_perfil.perfil)inner join parcelas on sub_tramos_perfil.parcela=parcelas.gid)inner join _suelos " +
	 		"on sub_tramos_perfil.suelo=_suelos.gid) inner join tipo_suelo on _suelos.tipo=tipo_suelo.id) " +
	 		"inner join textura_suelos on textura_suelos.id=_suelos.textura)left join usos_catastro on parcelas.uso=usos_catastro.id) " +
	 		"left join agrupadores_uso on usos_catastro.uso_suelo=agrupadores_uso.id where perfiles.parteaguas in(" + idParteaguas + ") " +
	 		"group by perfiles.gid, perfiles.orilla, sub_tramos_perfil.gid, agrupadores_uso.tipo_uso,sub_tramos_perfil.pendiente, tipo_suelo.tipo, " +
	 		"textura_suelos.textura, _suelos.materiaorganica, _suelos.estructura, sub_tramos_perfil.vel_infiltr_suelo, " +
	 		"perfiles.ancho order by perfiles.orilla, sub_tramos_perfil.gid, perfiles.gid";  //ST_Intersects(sub_tramos_perfil.the_geom, suelos.the_geom)
	 }
	
	public static boolean setVelocidadInfiltracion_Subtramo(int gid, double velInf)
	{
		JDBCAdapter db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		
		return db.ejecutarConsulta("update sub_tramos_perfil set vel_infiltr_suelo="+velInf+" where gid="+gid);
	}
	
	public static boolean redefinir_Tabla_tempPuntos(JDBCAdapter db)
	{
		if(db == null)
		 db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		
		String projabrev = Global.projeccionActiva.getAbrev();
		String srid = projabrev.substring(5, projabrev.length());
		
		return db.ejecutarConsulta("drop table if exists temp_puntos;" +
		           "CREATE TABLE temp_puntos(gid serial NOT NULL," +
		           "the_geom geometry,CONSTRAINT enforce_dims_the_geom CHECK (ndims(the_geom) = 2)," +
		           "CONSTRAINT enforce_geotype_the_geom CHECK (geometrytype(the_geom) = 'POINT'::text OR the_geom IS NULL)," +
		           "CONSTRAINT enforce_srid_the_geom CHECK (srid(the_geom) = "+ srid +"))WITH (OIDS=FALSE);" +
		           "ALTER TABLE temp_puntos OWNER TO postgres;");
	}
	
	public static void eliminar_Tablas_Temporales(JDBCAdapter db)
	{
		if(db == null)
		 db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		
		        db.ejecutarConsulta("drop table if exists temp_polilineas");
		        db.ejecutarConsulta("drop table if exists temp_polilineas_suelos");
		        db.ejecutarConsulta("drop table if exists temp_parcelas");
		        db.ejecutarConsulta("drop table if exists temp_polilineas_parcelas");
		        db.ejecutarConsulta("drop table if exists temp_union_parcelas");
	}
	
	public static boolean crear_Union_Parcelas_Uso(JDBCAdapter db)
	{
		if(db == null)
		 db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		
		return db.ejecutarConsulta("select usos_catastro.uso_suelo as gid, St_union(temp_parcelas.the_geom) as the_geom into temp_union_parcelas " +
                "from temp_parcelas inner join usos_catastro on temp_parcelas.uso=usos_catastro.id group by usos_catastro.uso_suelo");
		 
//		 boolean result = db.ejecutarConsulta("ALTER TABLE temp_union_parcelas  " +
//		 		"ADD CONSTRAINT enforce_dims_the_geom CHECK (ndims(the_geom) = 2); " +
//		 		"ALTER TABLE temp_union_parcelas " +
//		 		"ADD CONSTRAINT enforce_geotype_the_geom CHECK (geometrytype(the_geom) = 'MULTILINESTRING'::text OR the_geom IS NULL); " +
//		 		"ALTER TABLE temp_union_parcelas ADD CONSTRAINT enforce_srid_the_geom CHECK (srid(the_geom) = 2086);");
//		 
//		 return result;
	}
	
	public static SubtramosBasicGeometryInfo Determinar_Subtramos(int IdPerfil, JDBCAdapter db)
	{
		if(db == null)
		 db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		
		//Intersecciones con la pendiente----------------------------------------------
		String sql = "insert into temp_puntos(the_geom) select distinct(st_dump(st_intersection(perfiles.the_geom, temp_polilineas.the_geom))).geom " +
				"as the_geom " +
                "from perfiles inner join temp_polilineas on st_intersects(perfiles.the_geom, temp_polilineas.the_geom) " +
                "where  perfiles.gid="+IdPerfil;
        db.ejecutarConsulta(sql);
        System.out.println(sql);
        
      //Intersecciones con el tipo de suelo-----------------------------------------------
		sql = "insert into temp_puntos(the_geom) select distinct(st_dump(st_intersection(perfiles.the_geom, temp_polilineas_suelos.the_geom))).geom " +
				"as the_geom " +
                "from perfiles inner join temp_polilineas_suelos on st_intersects(perfiles.the_geom, temp_polilineas_suelos.the_geom) " +
                "where  perfiles.gid="+IdPerfil;
        db.ejecutarConsulta(sql);
        System.out.println(sql);
        
      //Intersecciones con el tipo de uso de suelo (Parcelas)-----------------------------------------------
		sql = "insert into temp_puntos(the_geom) select distinct(st_dump(st_intersection(perfiles.the_geom, temp_polilineas_parcelas.the_geom))).geom " +
				"as the_geom " +
                "from perfiles inner join temp_polilineas_parcelas on st_intersects(perfiles.the_geom, temp_polilineas_parcelas.the_geom) " +
                "where  perfiles.gid="+IdPerfil;
        db.ejecutarConsulta(sql);
        System.out.println(sql);
        
       /*Guardo el punto de origen y punto final del perfil*/
        db.ejecutarConsulta("select st_x(ST_StartPoint(perfiles.the_geom)), st_y(ST_StartPoint(perfiles.the_geom)), " +
        		"st_x(ST_EndPoint(perfiles.the_geom)), st_y(ST_EndPoint(perfiles.the_geom)), " +
        		"st_Length(perfiles.the_geom) from perfiles where gid="+IdPerfil);
        
        double x = db.getValueAsDouble(0,0);
        double y = db.getValueAsDouble(0,1);
        double x2 = db.getValueAsDouble(0,2);
        double y2 = db.getValueAsDouble(0,3);
        double dist = db.getValueAsDouble(0,4); //distancia del primer punto del perfil al segundo
        
        /*Obtengo los puntos de origen de cada tramo del perfil en análisis ordenados por distancia
         * al punto origen (el punto que intersecta el margen del agua) de menor a mayor*/
         sql = "select gid,st_distance(st_SetSRID(ST_Point("+x+","+y+"),ST_SRID(temp_puntos.the_geom)),temp_puntos.the_geom) as dist," +
         		" st_x(temp_puntos.the_geom), st_y(temp_puntos.the_geom) from temp_puntos order by dist";
         db.ejecutarConsulta(sql);

       //Guardo el gid, la X y la Y de los puntos de cada tramo
         //String []Gidpuntos;
         double []Xs;
         double []Ys;
         double []distancias;
         
        if(db.isEmpty())  //Es un perfil completo
        {
         Xs = new double[2];
         Ys = new double[2];
         distancias = new double[1];
         Xs[0] = x;   
         Ys[0] = y;   
         Xs[1] = x2;   
         Ys[1] = y2;
         distancias[0] = dist;
        }
        else   //Un perfil dividido en tramos
        {
          int cantIntersecciones = db.getRowCount();
          Xs = new double[cantIntersecciones+1];
          Ys = new double[cantIntersecciones+1];
          distancias = new double[cantIntersecciones];
            
        //punto inicial del perfil
          Xs[0] = x;  
          Ys[0] = y;
             
          for(int k=1; k<cantIntersecciones; k++)
          {	  
           Xs[k] = db.getValueAsDouble(k,2);
           Ys[k] = db.getValueAsDouble(k,3);
           
           if(k==1)
            distancias[k-1] = Funciones_Utiles.CalcularDistancia(x, y, Xs[k], Ys[k]);
           else
        	distancias[k-1] = Funciones_Utiles.CalcularDistancia(Xs[k-1], Ys[k-1], Xs[k], Ys[k]);
          }
          
          Xs[cantIntersecciones] = x2;  //x Final del perfil
          Ys[cantIntersecciones] = y2;  //y Final del perfil
          distancias[cantIntersecciones-1] = Funciones_Utiles.CalcularDistancia(Xs[cantIntersecciones-1], Ys[cantIntersecciones-1], 
        		                             Xs[cantIntersecciones], Ys[cantIntersecciones]);
       }
        
        SubtramosBasicGeometryInfo data = new SubtramosBasicGeometryInfo();
        data.Xs = Xs;
        data.Ys = Ys;
        data.distancias = distancias;
        
        return data;
	}
	
	public static boolean eliminar_Subtramos_Perfil(int idPerfil, JDBCAdapter db)
	{
		if(db == null)
		 db = new JDBCAdapter(ConnectionExt.getConexionActiva());
		
		return  db.ejecutarConsulta("delete from sub_tramos_perfil where perfil="+idPerfil);
	}
}
