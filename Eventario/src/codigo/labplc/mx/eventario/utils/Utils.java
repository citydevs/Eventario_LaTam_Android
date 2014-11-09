package codigo.labplc.mx.eventario.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.view.Display;
import codigo.labplc.mx.eventario.R;
import codigo.labplc.mx.eventario.bean.InfoPointBean;
import codigo.labplc.mx.eventario.bean.beanEventos;
/**
 * 
 * @author mikesaurio
 *
 */
public class Utils {
	
	
	/**
	 * valida si tiene internet
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnectionOk(Context context) {
		 boolean haveConnectedWifi = false;
		    boolean haveConnectedMobile = false;

		    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		    for (NetworkInfo ni : netInfo) {
		        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
		            if (ni.isConnected())
		                haveConnectedWifi = true;
		        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
		            if (ni.isConnected())
		                haveConnectedMobile = true;
		    }
		    return haveConnectedWifi || haveConnectedMobile;
	}

	/**
	 * metodo que hace la conexion al servidor con una url especifica
	 * 
	 * @param url
	 *            (String) ruta del web service
	 * @return (String) resultado del service
	 */
	public static String doHttpConnection(String url) {
		HttpClient Client = new DefaultHttpClient();
		try {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
			HttpGet httpget = new HttpGet(url);
			HttpResponse hhrpResponse = Client.execute(httpget);
			HttpEntity httpentiti = hhrpResponse.getEntity();
			return EntityUtils.toString(httpentiti);
		} catch (ParseException e) {
			e.getStackTrace();
			return null;
		} catch (IOException e) {
			e.getStackTrace();
			return null;
		}
	}
	
/**
 * llena un arreglo de eventos dado por un json
 * 
 * @param lat (String) latitud de la ubicacion
 * @param lon (String) longitud de la ubicacion
 * @param radio (String) radio de busqueda default: 2km
 * 
 * @return (BeanEventos) beasn que contiene todos los datos de la busqueda
 */
	public  static beanEventos llenarEventos(String lat,String lon,String radio,String fecha){
			try{
				  String Sjson=  doHttpConnection("http://eventario.mx/eventos.json?lat="+lat+"&lon="+lon+"&dist="+radio);
				  JSONArray jsonarray = new JSONArray(Sjson);
				  beanEventos bean = new    beanEventos();

				  	String[] nombre = new String[jsonarray.length()];
				  	String[] lugar = new String[jsonarray.length()];;
				  	String[] hora_inicio = new String[jsonarray.length()];
				  	String[] hora_fin = new String[jsonarray.length()];
				  	Integer[] imagen = new Integer[jsonarray.length()];
				  	String[] descripcion = new String[jsonarray.length()];
				  	String[] precio = new String[jsonarray.length()];
				  	String[] direccion = new String[jsonarray.length()];
				  	String[] fuente = new String[jsonarray.length()];
				  	String[] fecha_inicio = new String[jsonarray.length()];
				  	String[] fecha_fin = new String[jsonarray.length()];
				  	String[] categoria = new String[jsonarray.length()];
				  	String[] contacto = new String[jsonarray.length()];
				  	String[] pagina = new String[jsonarray.length()];
				  	String[] latitud = new String[jsonarray.length()];
				  	String[] longitud = new String[jsonarray.length()];
				  	String[] distancia = new String[jsonarray.length()];
				  	String[] url = new String[jsonarray.length()];
					
				  for (int i = 0; i < jsonarray.length(); i++) {
					  	JSONObject oneObject = jsonarray.getJSONObject(i);
							nombre[i]=((String) oneObject.getString("nombre"));
							lugar[i]=((String) oneObject.getString("lugar"));
							hora_inicio[i]=((String) oneObject.getString("hora_inicio").substring(11, 16));
							hora_fin[i]=((String) oneObject.getString("hora_fin").substring(11, 16));
							categoria[i]=((String) oneObject.getString("categoria"));
							if(categoria[i].equals("Aprendizaje")){
								imagen[i]=(R.drawable.ic_launcher_aprendizaje);
							}else if(categoria[i].equals("Tecnolog’a")){
								imagen[i]=(R.drawable.ic_launcher_tecnologia);
							}else if(categoria[i].equals("Teatro")){
								imagen[i]=(R.drawable.ic_launcher_teatro);
							}else if(categoria[i].equals("Mœsica")){
								imagen[i]=(R.drawable.ic_launcher_musica);
							}else if(categoria[i].equals("Infantiles")){
								imagen[i]=(R.drawable.ic_launcher_infantiles);
							}else if(categoria[i].equals("Exposiciones")){
								imagen[i]=(R.drawable.ic_launcher_exposiciones);
							}else if(categoria[i].equals("Deportivo")){
								imagen[i]=(R.drawable.ic_launcher_deportivo);
							}else if(categoria[i].equals("Cultura")){
								imagen[i]=(R.drawable.ic_launcher_cultura);
							}else if(categoria[i].equals("Cine")){
								imagen[i]=(R.drawable.ic_launcher_cine);
							}else{
								imagen[i]=(R.drawable.ic_launcher);
							}					
							
							descripcion[i]=((String) oneObject.getString("descripcion"));
							precio[i]=((String) oneObject.getString("precio"));
							direccion[i]=((String) oneObject.getString("direccion"));
							fuente[i]=((String) oneObject.getString("fuente"));
							fecha_inicio[i]=((String) oneObject.getString("fecha_inicio"));
							fecha_fin[i]=((String) oneObject.getString("fecha_fin"));
							contacto[i]=((String) oneObject.getString("contacto"));
							pagina[i]=((String) oneObject.getString("pagina"));
							latitud[i]=((String) oneObject.getString("latitud"));
							longitud[i]=((String) oneObject.getString("longitud"));
							distancia[i]=((String) oneObject.getString("distancia")+" Km.");
							url[i]=((String) oneObject.getString("url"));
		
				      }
				  
				  if(jsonarray.length()>0){
					bean.setNombre(nombre);
					bean.setLugar(lugar);
					bean.setHora_inicio(hora_inicio);
					bean.setHora_fin(hora_fin);
					bean.setImagen(imagen);
					bean.setDescripcion(descripcion);
					bean.setPrecio(precio);
					bean.setDireccion(direccion);
					bean.setFuente(fuente);
					bean.setFecha_inicio(fecha_inicio);
					bean.setFecha_fin(fecha_fin);
					bean.setCategoria(categoria);
					bean.setContacto(contacto);
					bean.setPagina(pagina);
					bean.setLatitud(latitud);
					bean.setLongitud(longitud);
					bean.setUrl(url);
					bean.setDistancia(distancia);
					return bean;
					
				  }
					return null;
					
			
				
				}catch(JSONException e){
					e.getStackTrace();
					return null;
				}
		
		}
	
	
	 public static Drawable LoadImageFromWebOperations(String url)
	    {
	          try{
	        InputStream is = (InputStream) new URL(url).getContent();
	        Drawable d = Drawable.createFromStream(is, "src name");
	        return d;
	      }catch (Exception e) {
	        System.out.println("Exc="+e);
	        return null;
	      }
	    }

	 
	 
	 /**
	  * metodo que regresa la distancia de 2 puntos en metros
	  * @param lat1 (Double) latitud inicial
	  * @param lng1 (Double) longitud inicial
	  * @param lat2 (Double) latitud final
	  * @param lng2 (Double) longitud final
	  * @return (long) diatancia entre puntos
	  */
	 public static long getDistanceMeters(double lat1, double lng1, double lat2, double lng2) {

		    double l1 = Math.toRadians(lat1);
		    double l2 = Math.toRadians(lat2);
		    double g1 = Math.toRadians(lng1);
		    double g2 = Math.toRadians(lng2);

		    double dist = Math.acos(Math.sin(l1) * Math.sin(l2) + Math.cos(l1) * Math.cos(l2) * Math.cos(g1 - g2));
		    if(dist < 0) {
		        dist = dist + Math.PI;
		    }

		    return Math.round(dist * 6378100);
		}
	 
	 public static ArrayList<InfoPointBean> busquedaDireccion(String direccion){
		 try{
		 	direccion = direccion.replaceAll(" ", "+");
     		String consulta = "http://maps.googleapis.com/maps/api/geocode/json?address="+direccion+"&sensor=true";
			String querty = Utils.doHttpConnection(consulta);
			return  (new DirectionsJSONParser().parsePoints(querty));
		 }catch(Exception e){
			 return null;
		 }
	 }
	 
	 
	 /**
		 * obtienes el tama–o de pantalla
		 * @param (activity) Activity
		 * @return (Point) .x = width
		 * 					.y = height 
		 */
			public static Point getTamanoPantalla(Activity activity){
				Display display = activity.getWindowManager().getDefaultDisplay();
				Point size = new Point();
				 display.getSize(size);
				int width = size.x;
				int height = size.y;
				return (new Point (width,height));
			}
	 
}
