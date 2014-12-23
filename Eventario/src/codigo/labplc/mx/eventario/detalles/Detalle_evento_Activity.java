package codigo.labplc.mx.eventario.detalles;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import codigo.labplc.mx.eventario.R;
import codigo.labplc.mx.eventario.detalles.mapa.Mapa_llegar_evento;
import codigo.labplc.mx.eventario.dialogos.Dialogos;
import codigo.labplc.mx.eventario.web.PaginaWebEvento;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * clase que 
 * @author mikesaurio
 *
 */
public class Detalle_evento_Activity extends Activity implements OnClickListener{

	private String nombre;
	private String lugar;
	private String hora_inicio;
	private String hora_fin;
	private int imagen;
	private String descripcion;
	private String precio;
	private String direccion;
	private String fecha_inicio;
	private String fecha_fin;
	private String categoria;
	private String contacto;
	private String pagina;
	private String latitud;
	private String longitud;
	private String url;
	private Double mi_lat;
	private Double mi_lon;

	
	private GoogleMap map;
	private MarkerOptions marker;
	
	
	
	public static final String CONSUMER_KEY = "D4ABzYy7ZWF38WxNRQMrprbnn";
	public static final String CONSUMER_SECRET = "jZoHRWIqi91kBqWlt6plc5em2TmccdqnH0kWNakiFynU2uvi03";
	public static final String CALLBACK_URL = "http://eventario.mx";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_evento);
		

		
		//propiedades del action bar
		 final ActionBar ab = getActionBar();
	     ab.setDisplayShowHomeEnabled(false);
	     ab.setDisplayShowTitleEnabled(false);     
	     final LayoutInflater inflater = (LayoutInflater)getSystemService("layout_inflater");
	     View view = inflater.inflate(R.layout.abs_layout,null);   
	     ab.setDisplayShowCustomEnabled(true);
	     ((ImageView) view.findViewById(R.id.abs_layout_iv_logo)).setOnClickListener(this);;
	     ab.setCustomView(view,new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
	     ab.setCustomView(view);
	     
	     
	   //obtenemos los adicionales 
			Bundle bundle = getIntent().getExtras();
			if(bundle!=null){
				 this.nombre =bundle.getString("nombre");
				 this.lugar =bundle.getString("lugar");
				 this.hora_inicio =bundle.getString("hora_inicio");
				 this.hora_fin =bundle.getString("hora_fin");
				 this.imagen =bundle.getInt("imagen");
				 this.descripcion =bundle.getString("descripcion");
				 this.precio =bundle.getString("precio");
				 this.direccion =bundle.getString("direccion");
				 this.fecha_inicio =bundle.getString("fecha_inicio");
				 this.fecha_fin =bundle.getString("fecha_fin");
				 this.categoria =bundle.getString("categoria");
				 this.contacto =bundle.getString("contacto");
				 this.pagina =bundle.getString("pagina");
				 this.latitud =bundle.getString("latitud") ;
				 this.longitud =bundle.getString("longitud");
				 this.url =bundle.getString("url");
				 this.mi_lat =bundle.getDouble("mi_latitud");
				 this.mi_lon =bundle.getDouble("mi_longitud");
				 
				 setUpMapIfNeeded();
				 llenarValores();
				
			}
			
			
			
			
			

	}
	
	/**
	 * llena la vista con los valores recibidos
	 */
	public void llenarValores() {
        ImageView row_iv_evento =(ImageView)findViewById(R.id.detalle_evento_iv_evento);
        row_iv_evento.setImageDrawable(getResources().getDrawable(this.imagen));
        
        TextView detalle_evento_iv_evento =(TextView)findViewById(R.id.detalle_evento_tv_categoria);
        detalle_evento_iv_evento.setText(this.categoria);
        
        TextView detalle_evento_tv_categoria =(TextView)findViewById(R.id.detalle_evento_tv_titulo);
        detalle_evento_tv_categoria.setText(this.nombre);
        
        
        TextView detalle_evento_tv_hora =(TextView)findViewById(R.id.detalle_evento_tv_hora);
        detalle_evento_tv_hora.setText(this.hora_inicio+"-"+this.hora_fin);
     
        
        TextView detalle_evento_tv_fecha =(TextView)findViewById(R.id.detalle_evento_tv_fecha);
        detalle_evento_tv_fecha.setText(this.fecha_inicio+" a "+this.fecha_fin);
      
        
        TextView detalle_evento_tv_donde =(TextView)findViewById(R.id.detalle_evento_tv_donde);
        detalle_evento_tv_donde.setText(this.lugar);
        
      //  TextView detalle_evento_tv_direccion =(TextView)findViewById(R.id.detalle_evento_tv_direccion);
       // detalle_evento_tv_direccion.setText(this.direccion);  
        
      /*  
        
        ImageView detalle_evento_iv_compartir =(ImageView)findViewById(R.id.detalle_evento_iv_compartir);
        detalle_evento_iv_compartir.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				String shareBody = "visitaré "+url +" @eventarioCDMX #eventario";
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Eventario");
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
				startActivity(Intent.createChooser(sharingIntent, "Share via"));
				
			}
		});
        
     
        
        ImageView detalle_evento_iv_dinero =(ImageView)findViewById(R.id.detalle_evento_iv_dinero);
        if(precio.equals("No disponible")){
        	detalle_evento_iv_dinero.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_dinero));
        	detalle_evento_iv_dinero.setEnabled(false);
		}
        detalle_evento_iv_dinero.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Dialogos().showDialogExtras(Detalle_evento_Activity.this, getResources().getString(R.string.detalle_precio), precio).show();
				
			}
		});
        
        
        ImageView detalle_evento_iv_info =(ImageView)findViewById(R.id.detalle_evento_iv_info);
        if(descripcion.equals("No disponible")){
        	detalle_evento_iv_info.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_info));
        	detalle_evento_iv_info.setEnabled(false);
		}
        detalle_evento_iv_info.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Dialogos().showDialogExtras(Detalle_evento_Activity.this, getResources().getString(R.string.detalle_descripcion), descripcion).show();
			}
		});
        
        ImageView detalle_evento_iv_tel =(ImageView)findViewById(R.id.detalle_evento_iv_tel);
        if(contacto.equals("No disponible")){
        	detalle_evento_iv_tel.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_tel));
        	detalle_evento_iv_tel.setEnabled(false);
		}
        detalle_evento_iv_tel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Dialogos().showDialogExtras(Detalle_evento_Activity.this, getResources().getString(R.string.detalle_telefono), contacto).show();
						}
		});
        
        ImageView detalle_evento_iv_www =(ImageView)findViewById(R.id.detalle_evento_iv_www);
        if(pagina.equals("No disponible")){
        	detalle_evento_iv_www.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_www));
        	detalle_evento_iv_www.setEnabled(false);
		}
        detalle_evento_iv_www.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!pagina.equals("No disponible")){
					Intent intent = new Intent(Detalle_evento_Activity.this, PaginaWebEvento.class);
					intent.putExtra("pagina", pagina);
					startActivity(intent);
				}
			}
		});
        
        
        */
        
       
		
				
        
        

	}


	/**
	 * inicializa el mapa y muestra la ubicacion inicial
	 */
	private void setUpMapIfNeeded() {
		if (map == null) {
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.detalle_evento_trip_map)).getMap();
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
		
		map.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public void onMapClick(LatLng arg0) {
				Intent intent = new Intent(Detalle_evento_Activity.this,Mapa_llegar_evento.class);
				intent.putExtra("lat", latitud);
				intent.putExtra("lng", longitud);
				startActivity(intent);
			//Toast.makeText(Detalle_evento_Activity.this, "hola", Toast.LENGTH_SHORT).show();
				
			}
		});
		
		// create marker
		marker = new MarkerOptions();
		marker.position(new LatLng(Double.parseDouble(this.latitud), Double.parseDouble(this.longitud)));
		marker.title(getResources().getString(R.string.detalle_ab_titulo));
		marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_pin));
		
		MarkerOptions marker_ = new MarkerOptions();
		marker_.position(new LatLng(this.mi_lat, this.mi_lon));
		marker_.title(getResources().getString(R.string.mapa_inicio_de_viaje));
		marker_.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_chinche_llena));
		
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(Double.parseDouble(this.latitud), Double.parseDouble(this.longitud)))
				.zoom(15).build();
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

		// adding marker
		map.addMarker(marker);
		map.addMarker(marker_);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.abs_layout_iv_logo) {
	        	atras();
			}

	       
	    }
	
	/**
	 * sobreEscritura de onBack press
	 */
	public void atras(){
		super.onBackPressed();
	}
	
	
	
	 @Override
	public void onBackPressed() {
		
		 atras();
	}
	 
	 
	 
		
		
}
