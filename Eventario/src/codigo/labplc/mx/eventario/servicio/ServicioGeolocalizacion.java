package codigo.labplc.mx.eventario.servicio;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;
import codigo.labplc.mx.eventario.Eventario_main;
import codigo.labplc.mx.eventario.R;
import codigo.labplc.mx.eventario.dialogos.Dialogos;

/**
 * 
 * @author mikesaurio
 * 
 */
@SuppressWarnings("unused")
public class ServicioGeolocalizacion extends Service implements Runnable {
	/**
	 * Declaraci—n de variables
	 */
	
	public final String TAG = this.getClass().getSimpleName();
	
	public static Eventario_main taxiActivity;
	private LocationManager mLocationManager;
	private MyLocationListener mLocationListener;

	private  double latitud_inicial = 19.0f;
	private  double longitud_inicial = -99.0f;
	private  double latitud =0;
	private  double longitud=0;
	private Location currentLocation = null;
	private boolean isFirstLocation = true;
	private Thread thread;
	private Double pointsLat ;
	private Double pointsLon;
	private boolean isFirstTime = true;
	private static boolean serviceIsIniciado = false;
	private  static boolean countTimer = true;
	private static  boolean panicoActivado = false;
	private boolean isSendMesagge= false;
    private int intervaloLocation =5000;
   
    

	@Override
	public void onCreate() {
		super.onCreate();		   
		 //escucha para la location 
		mLocationListener = new MyLocationListener();
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	//	Toast.makeText(taxiActivity, "Servicio creado ", Toast.LENGTH_SHORT).show();
	}
	
	
	
	

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		//Toast.makeText(taxiActivity, "Servicio creado ", Toast.LENGTH_SHORT).show();
		if(isFirstTime){
			obtenerSenalGPS();
			isFirstTime=false;
		}
				
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		if (mLocationManager != null)
			if (mLocationListener != null)
				mLocationManager.removeUpdates(mLocationListener);
		super.onDestroy();
	}
	
	

	@Override
	public IBinder onBind(Intent intencion) {
		return null;
	}


	/**
	 * handler
	 */
	@SuppressLint("HandlerLeak")
	private Handler handler_eventos = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// mLocationManager.removeUpdates(mLocationListener);
			updateLocation(currentLocation);
		}
	};

	/**
	 * metodo para actualizar la localizaci—n
	 * 
	 * @param currentLocation
	 * @return void
	 */
	public void updateLocation(Location currentLocation) {
		if (currentLocation != null) {
			latitud = Double.parseDouble(currentLocation.getLatitude() + "");
			longitud = Double.parseDouble(currentLocation.getLongitude() + "");
		//	Toast.makeText(this, "latitud "+latitud+" longitud "+longitud, Toast.LENGTH_SHORT).show();

			if (isFirstLocation) {
				latitud_inicial = latitud;
				longitud_inicial = longitud;
				isFirstLocation = false;
			} 
			
			Intent intent = new Intent("key");
			intent.putExtra("latitud", latitud);
			intent.putExtra("longitud", longitud);
			getApplicationContext().sendBroadcast(intent);


		}
	}

	
	/**
	 * Hilo de la aplicacion para cargar las cordenadas del usuario
	 */
	public void run() {
		if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			Looper.prepare();
			mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, intervaloLocation, 0, mLocationListener);
			Looper.loop();
			Looper.myLooper().quit();
		} else {
			taxiActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					
					new Dialogos().Toast(taxiActivity,getResources().getString(R.string.mapa_GPS_OFF), Toast.LENGTH_LONG);
					
				}
			});
		}
	}

	
	/**
	 * Metodo para Obtener la se–al del GPS
	 */
	private void obtenerSenalGPS() {
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Metodo para asignar las cordenadas del usuario
	 * */
	private void setCurrentLocation(Location loc) {
		currentLocation = loc;
	}

	/**
	 * Metodo para obtener las cordenadas del GPS
	 */
	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location loc) {
			// Log.d("finura",loc.getAccuracy()+"");
			if (loc != null) {
				setCurrentLocation(loc);
				handler_eventos.sendEmptyMessage(1000);
			}
		}

		/**
		 * metodo que revisa si el GPS esta apagado
		 */
		public void onProviderDisabled(String provider) {
			taxiActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					new Dialogos().Toast(taxiActivity,getResources().getString(R.string.mapa_GPS_OFF), Toast.LENGTH_LONG);
				}
			});
		}

		// @Override
		public void onProviderEnabled(String provider) {
		}

		// @Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}
     
  
    
}