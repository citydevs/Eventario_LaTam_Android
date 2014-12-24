package codigo.labplc.mx.eventario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import codigo.labplc.mx.eventario.Eventario.TrackerName;
import codigo.labplc.mx.eventario.bean.InfoPointBean;
import codigo.labplc.mx.eventario.bean.beanEventos;
import codigo.labplc.mx.eventario.customs.CustomList;
import codigo.labplc.mx.eventario.detalles.Detalle_evento_Activity;
import codigo.labplc.mx.eventario.dialogos.Dialogos;
import codigo.labplc.mx.eventario.servicio.ServicioGeolocalizacion;
import codigo.labplc.mx.eventario.utils.Utils;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.internal.ml;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * clase principal  muestra Dashboard de eventos
 * @author mikesaurio
 *
 */
public class Eventario_main extends Activity {

	public final String TAG = this.getClass().getSimpleName();
	
	private GoogleMap map;
	public static double lat=19.0;
	public static double lon=-99.0;
	private MarkerOptions marker;
	private ProgressDialog pDialog;
	private int isLocalizado = 0;
    private ListView list;
	private beanEventos bean,bean_cat;
	private String radio="2";
	private String progreso;
	private String id_ubicacion;
	private String[] id_markers;
	private boolean pause=false;
	public String lat_,lon_;
	private  CustomList adapter;
	private LinearLayout ll_main_categorias;
	private boolean conImagenes = true;
	private TextView tv_main_titulo;
	public  AlertDialog customDialog= null;
	public String fecha_seleccionada = null;
	public boolean cambio_fecha=true;
	private  Button eventario_main_btn_busca_aqui;
	private String progreso_busqueda = "2"; //por defaul son 2 km de busqueda
	private TextView configuracion_tv_distancia; //tv que muestra el radio de busqueda
	private String progreso_busqueda_temp; //auxiliar que permite ver ver al usuario el radio de busqueda de manera TEMPORAL

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eventario_main);
		
		
		if (!Utils.isNetworkConnectionOk(Eventario_main.this)) {
			new Dialogos().showDialogGPS(Eventario_main.this).show();		
		}else{
			init();
		}
		
			
	}
	
	/**
	 * inflado de la vista
	 */
	public void init(){
		
		ServicioGeolocalizacion.taxiActivity = Eventario_main.this;
		startService(new Intent(Eventario_main.this,ServicioGeolocalizacion.class));
		
		 SharedPreferences prefs = getSharedPreferences("MisPreferenciasEventario",Context.MODE_PRIVATE);
		 progreso = prefs.getString("progreso", null);
		 
		
		
	     if(lat==19.0){
	    	pDialog=Utils.anillo(Eventario_main.this,pDialog);
	    	pDialog.show();
	     }
	    	
	     list=(ListView)findViewById(R.id.list);
	     list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
	       
	            	if(bean_cat==null){
	            		bean_cat=bean;
	            	}
	            	abrirDetalles(bean_cat.getId_marker()[position]);
	            }
	        });
	     
	     

		ImageView eventario_main_iv_gps =(ImageView)findViewById(R.id.eventario_main_iv_gps);
		eventario_main_iv_gps.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CameraPosition cameraPosition;
				cameraPosition = new CameraPosition.Builder().target(new LatLng(lat, lon)).zoom(map.getCameraPosition().zoom).build();
				map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
				Uploaded nuevaTareas = new Uploaded();
				nuevaTareas.execute(lat+"",lon+"");
				
			}
		});
		
		
		ImageView eventario_main_iv_config =(ImageView)findViewById(R.id.eventario_main_iv_config);
		eventario_main_iv_config.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				showDialogConfig(Eventario_main.this).show();
				
			}
		});
		
	
		
		ImageView eventario_main_iv_calendar =(ImageView)findViewById(R.id.eventario_main_iv_calendar);
		eventario_main_iv_calendar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialogCalendario(Eventario_main.this).show();
			}
		});
		
		
		ll_main_categorias=(LinearLayout)findViewById(R.id.ll_main_categorias);
		tv_main_titulo=(TextView)findViewById(R.id.tv_main_titulo);
		
		
		
		
		eventario_main_btn_busca_aqui=(Button)findViewById(R.id.eventario_main_btn_busca_aqui);
				eventario_main_btn_busca_aqui.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Uploaded nuevaTareas = new Uploaded();
						nuevaTareas.execute(map.getCameraPosition().target.latitude+"",map.getCameraPosition().target.longitude+"");
						
					}
				});
		
		
		
		setUpMapIfNeeded();
		
	}
	
	/**
	 * carga en la lista los eventos 
	 * @return
	 */
	public beanEventos  cargarEventos(String tipo){
		try{
			beanEventos b= new beanEventos();
			if(tipo.equals("")){
				b=bean;
				
			}else {
				b = Utils.getFiltraCategoria(tipo,bean);
				
			}
			
			 bean_cat=b;
			 adapter = new CustomList(Eventario_main.this, b.getNombre(), b.getHora_inicio(),b.getHora_fin(),b.getDistancia(),b.getImagen(),conImagenes);
			    
			return b;
		}catch(Exception e){
			e.printStackTrace();
		return null;
		}
		
	}
	
	
	
	
	
	
	/**
	 * carga eventos en el mapa
	 * @param b (beanEventos)
	 * @return beanEventos
	 */
	public beanEventos catalogo_Mapa(beanEventos b){
		
		map.clear();
		marker.position(new LatLng(Double.parseDouble(lat_),Double.parseDouble(lon_)));
		Marker m=map.addMarker(marker);
		id_ubicacion=m.getId();
	   	
	   if(b!=null){
		id_markers = new String[b.getLatitud().length];
		for(int i=0;i<b.getLatitud().length;i++){
			MarkerOptions markerte= new MarkerOptions();
			markerte.position(new LatLng(Double.parseDouble(b.getLatitud()[i]), Double.parseDouble(b.getLongitud()[i])));
			markerte.title(b.getNombre()[i]+"@@"+b.getLugar()[i]);
			markerte.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_pin));
			Marker ma =map.addMarker(markerte);
			id_markers[i] = ma.getId();
		}
	   	b.setId_marker(id_markers);
	   }
	   return b;
	}

	
	
	
	
	/**
	 * inicializa el mapa y muestra la ubicacion inicial
	 */
	private void setUpMapIfNeeded() {
		if (map == null) {
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapa)).getMap();
			if (map != null) {
				if(setUpMap()) {
					initMap();
				}
			}
		}
	}
	
	/**
	 * Revisa que el mapa esta listo
	 */
	public boolean setUpMap() {
		if (!checkReady()) {
            return false;
        } else {
        	return true;
        }
	}
    
	/**
	 * revisa que el mapa no sea nulo
	 */
	private boolean checkReady() {
        if (map == null) {
            return false;
        }
        return true;
    }

	/**
	 * inicializa el mapa y coloca todos sus atributos
	 */
	public void initMap() {
		map.setMyLocationEnabled(false);//quitar circulo azul;
		map.setBuildingsEnabled(true);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		map.getUiSettings().setZoomControlsEnabled(true); //ZOOM
		map.getUiSettings().setCompassEnabled(true); //COMPASS
		map.getUiSettings().setZoomGesturesEnabled(true); //GESTURES ZOOM
		map.getUiSettings().setRotateGesturesEnabled(true); //ROTATE GESTURES
		map.getUiSettings().setScrollGesturesEnabled(true); //SCROLL GESTURES
		map.getUiSettings().setTiltGesturesEnabled(true); //TILT GESTURES
		map.getUiSettings().setZoomControlsEnabled(false);
		
		// create marker
		marker = new MarkerOptions();
		marker.position(new LatLng(lat, lon));
		marker.title(getResources().getString(R.string.mapa_inicio_de_viaje));
		marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_chinche_llena));
		
	
		Marker m = map.addMarker(marker);	
		id_ubicacion=m.getId();
	}
	
	
	/**
	 * manejo de transmiciones
	 */
	private BroadcastReceiver onBroadcast = new BroadcastReceiver() {

		@SuppressLint("SimpleDateFormat")
		@Override
		public void onReceive(Context ctxt, Intent t) {
			
			lat = t.getDoubleExtra("latitud", 19.0f);
			lon = t.getDoubleExtra("longitud",-99.0f);
			
			if(isLocalizado==0){
				Uploaded nuevaTareas = new Uploaded();
				nuevaTareas.execute(lat+"",lon+"");
			}
			
				
			map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
				@Override
				public void onInfoWindowClick(Marker marker) {
					if(!marker.getId().toString().equals(id_ubicacion)){
						
						abrirDetalles(marker.getId().toString());
					}	
				}
			});
			map.setInfoWindowAdapter(new InfoWindowAdapter() {
	            @Override
	            public View getInfoWindow(Marker marker) {              
	                return null;
	            }           
	            @Override
	            public View getInfoContents(Marker marker) {
	            	if(!marker.getId().toString().equals(id_ubicacion)){
			            	View v = getLayoutInflater().inflate(R.layout.windowlayout, null);
			                String s[] = marker.getTitle().split("@@");
			                TextView   pupop_nombre = (TextView) v.findViewById(R.id.pupop_nombre);
			                pupop_nombre.setText(s[0] );
			                TextView pupop_lugar = (TextView) v.findViewById(R.id.pupop_lugar);
			                pupop_lugar.setText(s[1]);
			                return v;
		            	
	            	}else{
	            		View v = getLayoutInflater().inflate(R.layout.windowlayout_simple, null);
						 TextView   pupop_nombre = (TextView) v.findViewById(R.id.pupop_simple_nombre);
			              pupop_nombre.setText(getResources().getString(R.string.mapa_inicio_de_viaje));
						 return v;
	            	}
	            }
	        });
			if(isLocalizado>=1){
		 		
		 		stopService(new Intent(Eventario_main.this, ServicioGeolocalizacion.class));
		 	}else{
		 		if(pDialog!=null){
			 		pDialog.dismiss();
			 	}
		 		isLocalizado+=1;
		 	}
			
		}
	};
	
	
	
	
	
	
				
		/**
		 * Crea un intent para pasar toda la informacion
		 * @param id (String) identificador 
		 */
		public void abrirDetalles(String id) {
			if(bean_cat==null){
				bean_cat=bean;
			}
			for(int i=0;i<bean_cat.getId_marker().length;i++){
				if(bean_cat.getId_marker()[i].toString().equals(id)){
						Intent intent = new Intent(Eventario_main.this,Detalle_evento_Activity.class);
						intent.putExtra("nombre", bean_cat.getNombre()[i]);
						intent.putExtra("lugar", bean_cat.getLugar()[i]);
						intent.putExtra("hora_inicio", bean_cat.getHora_inicio()[i]);
						intent.putExtra("hora_fin", bean_cat.getHora_fin()[i]);
						intent.putExtra("imagen", bean_cat.getImagen()[i]);
						intent.putExtra("descripcion", bean_cat.getDescripcion()[i]);
						intent.putExtra("precio", bean_cat.getPrecio()[i]);
						intent.putExtra("direccion", bean_cat.getDireccion()[i]);
						intent.putExtra("fuente", bean_cat.getFuente()[i]);
						intent.putExtra("fecha_inicio", bean_cat.getFecha_inicio()[i]);
						intent.putExtra("fecha_fin", bean_cat.getFecha_fin()[i]);
						intent.putExtra("categoria", bean_cat.getCategoria()[i]);
						intent.putExtra("contacto", bean_cat.getContacto()[i]);
						intent.putExtra("pagina", bean_cat.getPagina()[i]);
						intent.putExtra("latitud", bean_cat.getLatitud()[i]);
						intent.putExtra("longitud", bean_cat.getLongitud()[i]);
						intent.putExtra("distancia", bean_cat.getDistancia()[i]);
						intent.putExtra("url", bean_cat.getUrl()[i]);
						intent.putExtra("id_marker", bean_cat.getId_marker()[i]);
						intent.putExtra("mi_latitud", lat);
						intent.putExtra("mi_longitud", lon);
						startActivity(intent);
						break;
				}
			}

			
		}

		@Override
		public void onBackPressed() {
			if(Dialogos.customDialog!=null){
				Dialogos.customDialog.dismiss();
				finish();
			}else{
				super.onBackPressed();	
			}
		}
		
		
		
		/**
		 * hilo que controla las actualizaciones del mapa
		 * @author mikesaurio
		 *
		 */
		class Uploaded extends AsyncTask<String, Void, Void> {
			
			public static final int HTTP_TIMEOUT = 60 * 1000;
			private ProgressDialog pDialog_hilo;
			@SuppressLint("SimpleDateFormat")
			@Override
			protected Void doInBackground(String... params) {
				lat_ =(String) params[0];
				lon_ =(String) params[1];
				try
				{	
					Calendar c = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String horaInicio = sdf.format(c.getTime());
					if(fecha_seleccionada!=null){
						horaInicio= fecha_seleccionada;
					}
					bean = Utils.llenarEventos(lat_+"",lon_+"",radio,horaInicio);
					
					if(bean!=null){
						conImagenes=true;
						cargarEventos("");
					}
				} catch (Exception e) {
					e.getStackTrace();
				}
				return null;
			}

			protected void onPreExecute() {
				
				pDialog_hilo = new ProgressDialog(Eventario_main.this);
				pDialog_hilo.setCanceledOnTouchOutside(false);
				pDialog_hilo.setMessage(getResources().getString(R.string.mapa_localizando));
				pDialog_hilo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pDialog_hilo.setCancelable(false);
				pDialog_hilo.show();
				bean = null;
				 if(progreso!=null){
					 radio=progreso;
				 };
				super.onPreExecute();
				
			}

			protected void onPostExecute(Void result) {
				
				if(bean!=null){
					list.setAdapter(null);
					list.setAdapter(adapter);
				}else{
					new Dialogos().Toast(Eventario_main.this,getResources().getString(R.string.toast_no_eventos), Toast.LENGTH_SHORT);
					list.setAdapter(null);
				}
				
				map.clear();
				marker.position(new LatLng(Double.parseDouble(lat_),Double.parseDouble(lon_)));
				Marker m=map.addMarker(marker);
				id_ubicacion=m.getId();
			   	
			   if(bean!=null){
				id_markers = new String[bean.getLatitud().length];
				for(int i=0;i<bean.getLatitud().length;i++){
					MarkerOptions markerte= new MarkerOptions();
					markerte.position(new LatLng(Double.parseDouble(bean.getLatitud()[i]), Double.parseDouble(bean.getLongitud()[i])));
					markerte.title(bean.getNombre()[i]+"@@"+bean.getLugar()[i]);
					markerte.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_pin));
					Marker ma =map.addMarker(markerte);
					id_markers[i] = ma.getId();
				}
			   	bean.setId_marker(id_markers);
			   
			   }
				LLenarCatalogos();
			   CameraPosition cameraPosition;
				cameraPosition = new CameraPosition.Builder().target(new LatLng(Double.parseDouble(lat_), Double.parseDouble(lon_))).zoom(14).build();
				map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));	

			   pDialog_hilo.dismiss();
			   eventario_main_btn_busca_aqui.setVisibility(Button.INVISIBLE);
				 
			   super.onPostExecute(result);	
					
					
				}			
			
		}
		
		/**
		 * Permite llenar la lista de eventos dependiendo la categiria seleccionada
		 */
		private void LLenarCatalogos() {

			
			ll_main_categorias.removeAllViews();
			
	if(bean!=null){	
		final	ImageView imagen_todos = new ImageView(Eventario_main.this);
			imagen_todos.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
			imagen_todos.setTag("todos");
			imagen_todos.setPadding(10, 0, 10, 0);
			imagen_todos.setBackground(getResources().getDrawable(R.drawable.marco_todo));
			imagen_todos.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					conImagenes=true;
					cargarEventos("");
					list.setAdapter(null);
					list.setAdapter(adapter);
					catalogo_Mapa(bean);
					limpiarLinearLayout();
					tv_main_titulo.setText(getResources().getString(R.string.mapa_titulo_drawer));
					imagen_todos.setBackground(getResources().getDrawable(R.drawable.marco_todo));
					
				}
			});
			ll_main_categorias.addView(imagen_todos);
			
			boolean unico= true;
			
			for(int i=0;i<bean.getCategoria().length;i++){
				unico= true;
				ImageView imagen = new ImageView(Eventario_main.this);
				int recurso=0;
				
				for(int j=0;j<ll_main_categorias.getChildCount();j++){
					if(ll_main_categorias.getChildAt(j).getTag().toString().equals(bean.getCategoria()[i])){
						unico=false;
					}
				}
				if(unico){
					
					if(bean.getCategoria()[i].equals("Aprendizaje")){
						recurso=(R.drawable.ic_launcher_aprendizaje);
					}else if(bean.getCategoria()[i].equals("Tecnología")){
						recurso=(R.drawable.ic_launcher_tecnologia);
					}else if(bean.getCategoria()[i].equals("Teatro")){
						recurso=(R.drawable.ic_launcher_teatro);
					}else if(bean.getCategoria()[i].equals("Música")){
						recurso=(R.drawable.ic_launcher_musica);
					}else if(bean.getCategoria()[i].equals("Infantiles")){
						recurso=(R.drawable.ic_launcher_infantiles);
					}else if(bean.getCategoria()[i].equals("Exposiciones")){
						recurso=(R.drawable.ic_launcher_exposiciones);
					}else if(bean.getCategoria()[i].equals("Deportivo")){
						recurso=(R.drawable.ic_launcher_deportivo);
					}else if(bean.getCategoria()[i].equals("Cultura")){
						recurso=(R.drawable.ic_launcher_cultura);
					}else if(bean.getCategoria()[i].equals("Cine")){
						recurso=(R.drawable.ic_launcher_cine);
					}else {
						recurso=(R.drawable.ic_launcher);
					}
					
					imagen.setImageDrawable(getResources().getDrawable(recurso));
					imagen.setTag(bean.getCategoria()[i]);
					imagen.setBackground(getResources().getDrawable(R.drawable.marco_left));
					imagen.setPadding(10, 0, 10, 0);
					imagen.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							conImagenes=false;
							beanEventos b_=cargarEventos(v.getTag().toString());
							limpiarLinearLayout();
							imagen_todos.setBackground(getResources().getDrawable(R.drawable.marco_nada));
							v.setBackground(getResources().getDrawable(R.drawable.marco_todo));
							v.setPadding(10, 0, 10, 0);
							tv_main_titulo.setText(v.getTag().toString());
							
							list.setAdapter(null);
							list.setAdapter(adapter);
							catalogo_Mapa(b_);
						}

						
					});
					ll_main_categorias.addView(imagen);
				}
				
			}
		}
			
		}
		
		private void limpiarLinearLayout() {
		for(int i=0;i<ll_main_categorias.getChildCount();i++){
			ll_main_categorias.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.marco_left));
			ll_main_categorias.getChildAt(i).setPadding(10, 0, 10, 0);
		}
			
		}
		
		
		public Dialog showDialogCalendario(final Activity activity)
	    {
			
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		    View view = activity.getLayoutInflater().inflate(R.layout.dialogo_calendario, null);
		    builder.setView(view);
		    builder.setCancelable(true);
		    
		    final CalendarView dialogo_calendario = (CalendarView) view.findViewById(R.id.dialogo_calendario);
		    dialogo_calendario.setMinDate(Utils.getFechaHoy());
		    
		    if(fecha_seleccionada!=null){
		    	SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date d = f.parse(fecha_seleccionada);
					dialogo_calendario.setDate (d.getTime(), true, true);
				} catch (ParseException e) {
					e.printStackTrace();
				}
		    	
		    }else{
		    	dialogo_calendario.setDate (Utils.getFechaHoy(), true, true);
		    }
			
			dialogo_calendario.setOnDateChangeListener(new OnDateChangeListener() {

	            @Override
	            public void onSelectedDayChange(CalendarView view, int year, int month,
	                    int dayOfMonth) {
	                
	            	fecha_seleccionada= year+"-"+(month+1)+"-"+dayOfMonth;
	            }
	        });
			
			Button dialogo_calendario_aceptar =(Button)view.findViewById(R.id.dialogo_calendario_aceptar);
			dialogo_calendario_aceptar.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Uploaded nuevaTareas = new Uploaded();
					nuevaTareas.execute(map.getCameraPosition().target.latitude+"",map.getCameraPosition().target.longitude+"");
					customDialog.dismiss();
					
				}
			});
			

	        return (customDialog=builder.create());// return customDialog;//regresamos el di���logo
	    }  
		
		
		public Dialog showDialogConfig(final Activity activity) 
	    {
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		    View view = activity.getLayoutInflater().inflate(R.layout.activity_configuracion, null);
		    builder.setView(view);
		    builder.setCancelable(true);
		    
		    Button configuracion_btn_aceptar=(Button)view.findViewById(R.id.configuracion_btn_aceptar);
		    configuracion_btn_aceptar.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					progreso_busqueda =  progreso_busqueda_temp;

					SharedPreferences prefs = getSharedPreferences("MisPreferenciasEventario", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("progreso", progreso_busqueda);
					editor.commit();
					
					progreso = progreso_busqueda;
					
					Uploaded nuevaTareas = new Uploaded();
					nuevaTareas.execute(lat+"",lon+"");
					customDialog.dismiss();
					
					
				}
			});
		    
		    configuracion_tv_distancia =(TextView) view.findViewById(R.id.configuracion_tv_distancia);
			SeekBar    seekbar = (SeekBar)view.findViewById(R.id.configuracion_sb_distancia); // make seekbar object
	      
			 SharedPreferences prefs = getSharedPreferences("MisPreferenciasEventario",Context.MODE_PRIVATE);
			 progreso_busqueda = prefs.getString("progreso", null);
			 if(progreso_busqueda!=null){
				 seekbar.setProgress(Integer.parseInt(progreso_busqueda));
				 configuracion_tv_distancia.setText(progreso_busqueda+" Km");
			 }else{
				 seekbar.setProgress(Integer.parseInt("2"));
				 
				 configuracion_tv_distancia.setText("2 Km"); 
			 }
			
	        
	        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

	            @Override
	            public void onStopTrackingTouch(SeekBar seekBar) {

	            }

	            @Override
	            public void onStartTrackingTouch(SeekBar seekBar) {

	            }
	            @Override
	            public void onProgressChanged(SeekBar seekBar, int progress,
	                    boolean fromUser) {
	            	progreso_busqueda_temp = progress+"";

	       		 	configuracion_tv_distancia.setText(progreso_busqueda_temp+" Km");

	            }
	        });
	        return (customDialog=builder.create());// return customDialog;//regresamos el dialogo
	    }  
		
		
		
		
		
		@Override
		public void onStart() {
		  super.onStart();
	        Tracker t = ((Eventario) getApplication()).getTracker(
	            TrackerName.APP_TRACKER);
	        t.setScreenName("EventarioMain");
	        t.send(new HitBuilders.AppViewBuilder().build());
		}
		@Override
		protected void onDestroy() {
		if(pDialog!=null){
	    	pDialog.dismiss();
	    }
		
			isLocalizado=0;
			super.onDestroy();
		}

		@Override
		protected void onPause() {
			pause= true;
			try{
				unregisterReceiver(onBroadcast);
				if(pDialog!=null){
			 		pDialog.dismiss();	
			 	}
				
		 		stopService(new Intent(Eventario_main.this, ServicioGeolocalizacion.class));
			}catch(Exception e){
				
			}
			super.onPause();
		}

		@Override
		protected void onResume() {
			if(Dialogos.customDialog!=null){
				Dialogos.customDialog.dismiss();
			}
			if (!Utils.isNetworkConnectionOk(getApplicationContext())) {
				new Dialogos().showDialogGPS(Eventario_main.this).show();		
			}else{
				if(pause){
					 init();
					 pause=false;
				}
			}
			try{
				registerReceiver(onBroadcast, new IntentFilter("key"));
			}catch(Exception e){}
			
			super.onResume();
		}
		
		
		@Override
		public boolean dispatchTouchEvent(MotionEvent ev) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_UP:
				if(Utils.getDistanceMeters(Double.parseDouble(lat_), Double.parseDouble(lon_),map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude)>=1000) {
					eventario_main_btn_busca_aqui.setVisibility(Button.VISIBLE);
				}else{
					eventario_main_btn_busca_aqui.setVisibility(Button.INVISIBLE);
				}
				break;
			}
			return super.dispatchTouchEvent(ev);
		}

		
}
