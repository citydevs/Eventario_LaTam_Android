package codigo.labplc.mx.eventario.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import android.app.ProgressDialog;
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
				  String Sjson=  doHttpConnection("http://eventario.mx/eventos.json?lat="+lat+"&lon="+lon+"&dist="+radio+
						  "&fecha="+fecha);
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
							}else if(categoria[i].equals("Tecnología")){
								imagen[i]=(R.drawable.ic_launcher_tecnologia);
							}else if(categoria[i].equals("Teatro")){
								imagen[i]=(R.drawable.ic_launcher_teatro);
							}else if(categoria[i].equals("Música")){
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
		 * obtienes el tamaño de pantalla
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
			
			
			
			public static beanEventos getFiltraCategoria(String tipo, beanEventos bean) {
				beanEventos b= new beanEventos();
				
				List<String> nombre = new ArrayList<String>();
				List<String> lugar = new ArrayList<String>();
				List<String> hora_inicio = new ArrayList<String>();
				List<String>  hora_fin = new ArrayList<String>();
				List<Integer>  imagen= new ArrayList<Integer>();
				List<String> descripcion = new ArrayList<String>();
				List<String> precio = new ArrayList<String>();
				List<String>  direccion= new ArrayList<String>();
				List<String> fuente = new ArrayList<String>();
				List<String> fecha_inicio = new ArrayList<String>();
				List<String> fecha_fin = new ArrayList<String>();
				List<String> categoria = new ArrayList<String>();
				List<String>  contacto= new ArrayList<String>();
				List<String> pagina = new ArrayList<String>();
				List<String> latitud = new ArrayList<String>();
				List<String>  longitud= new ArrayList<String>();
				List<String>  distancia= new ArrayList<String>();
				List<String> url = new ArrayList<String>();
				List<String> id_marker = new ArrayList<String>();
				
				
				
				
				
				for(int i=0;i<bean.getCategoria().length;i++){
					if(bean.getCategoria()[i].equals(tipo)){
						nombre.add(bean.getNombre()[i]); 
					  	lugar.add(bean.getLugar()[i]); 
					  	hora_inicio.add(bean.getHora_inicio()[i]); 
					  	hora_fin.add(bean.getHora_fin()[i]); 
					  	imagen.add(bean.getImagen()[i]); 
					  	descripcion.add(bean.getDescripcion()[i]);  
					  	precio.add(bean.getPrecio()[i]);  
					  	direccion .add(bean.getDireccion()[i]); 
					  	fuente.add(bean.getFuente()[i]);  
					  	fecha_inicio.add(bean.getFecha_inicio()[i]);  
					  	fecha_fin.add(bean.getFecha_fin()[i]); 
					  	categoria.add(bean.getCategoria()[i]);  
					  	contacto .add(bean.getContacto()[i]); 
					  	pagina.add(bean.getPagina()[i]);  
					  	latitud.add(bean.getLatitud()[i]);  
					  	longitud.add(bean.getLongitud()[i]);  
					  	distancia.add(bean.getDistancia()[i]);  
					  	url.add(bean.getUrl()[i]);
					  	id_marker.add(bean.getId_marker()[i]); 
					}
				}
				
				b.setNombre(nombre.toArray(new String[nombre .size()]));
				b.setLugar(lugar.toArray(new String[lugar .size()]));
				b.setHora_inicio(hora_inicio.toArray(new String[hora_inicio .size()]));
				b.setHora_fin(hora_fin.toArray(new String[hora_fin .size()]));
				b.setImagen(imagen.toArray(new Integer[imagen .size()]));
				b.setDescripcion(descripcion.toArray(new String[descripcion .size()]));
				b.setPrecio(precio.toArray(new String[precio .size()]));
				b.setDireccion(direccion.toArray(new String[direccion .size()]));
				b.setFuente(fuente.toArray(new String[fuente .size()]));
				b.setFecha_inicio(fecha_inicio.toArray(new String[fecha_inicio .size()]));
				b.setFecha_fin(fecha_fin.toArray(new String[fecha_fin .size()]));
				b.setCategoria(categoria.toArray(new String[categoria .size()]));
				b.setContacto(contacto.toArray(new String[contacto .size()]));
				b.setPagina(pagina.toArray(new String[pagina .size()]));
				b.setLatitud(latitud.toArray(new String[latitud .size()]));
				b.setLongitud(longitud.toArray(new String[longitud .size()]));
				b.setUrl(url.toArray(new String[url .size()]));
				b.setDistancia(distancia.toArray(new String[distancia .size()]));
				b.setId_marker(id_marker.toArray(new String[id_marker .size()]));
				
				
				
				return b;
			}
			
			
			
			/**
			 * dialogo de espera
			 */
			public static ProgressDialog anillo(Activity activity, ProgressDialog pDialog){
				pDialog = new ProgressDialog(activity);
		 		pDialog.setCanceledOnTouchOutside(false);
		 		pDialog.setMessage(activity.getString(R.string.mapa_texto_significado_el_viaje_inicio));
		 		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		 		pDialog.setCancelable(false);
		 		return pDialog;

			}
			
			
			/**
			 * obtener los milisegundos de una fecha
			 * 
			 * @return
			 */
			public static long getFechaHoy() {
				Calendar now = Calendar.getInstance();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String fechaCel = now.get(Calendar.YEAR) + "-"
						+ ((now.get(Calendar.MONTH)) + 1) + "-"
						+ now.get(Calendar.DAY_OF_MONTH);
				try {
					return (formatter.parse(fechaCel)).getTime();
				} catch (java.text.ParseException e) {
					e.printStackTrace();
					return 0;
				}
			}
	 
}
