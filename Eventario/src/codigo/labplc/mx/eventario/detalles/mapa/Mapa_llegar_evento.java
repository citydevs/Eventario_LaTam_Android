package codigo.labplc.mx.eventario.detalles.mapa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import codigo.labplc.mx.eventario.R;
import codigo.labplc.mx.eventario.utils.DirectionsJSONParser;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class Mapa_llegar_evento extends FragmentActivity implements OnClickListener {

	GoogleMap map;

	
	public  static  String lat_;
	public static String lng_;
	
	 private FollowMeLocationSource followMeLocationSource;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_mapa_llegar_evento);
		
		
		 final ActionBar ab = getActionBar();
	     ab.setDisplayShowHomeEnabled(false);
	     ab.setDisplayShowTitleEnabled(false);     
	     final LayoutInflater inflater = (LayoutInflater)getSystemService("layout_inflater");
	     View view = inflater.inflate(R.layout.abs_layout,null);   
	     ab.setDisplayShowCustomEnabled(true);
	     ((ImageView) view.findViewById(R.id.abs_layout_iv_logo)).setOnClickListener(this);
	     ((ImageView) view.findViewById(R.id.abs_layout_iv_compartir)).setVisibility(ImageView.INVISIBLE);
	     ab.setCustomView(view,new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
	     ab.setCustomView(view);

        
    	Bundle bundle = getIntent().getExtras();
    	
		if(bundle!=null){
			 lat_ =bundle.getString("lat");
			 lng_ =bundle.getString("lng");
		}
			
		 followMeLocationSource = new FollowMeLocationSource();
         
    }
    
    @Override
	protected void onResume() {
    	super.onResume();
        followMeLocationSource.getBestAvailableProvider();
        setUpMapIfNeeded();
        map.setMyLocationEnabled(true);
	
	}
    
    
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
            map = fm.getMap();
            if (map != null) {
                map.setLocationSource(followMeLocationSource);
                map.moveCamera(CameraUpdateFactory.zoomTo(15f));
            }
        }
    }
    @Override
    public void onPause() {
        /* Disable the my-location layer (this causes our LocationSource to be automatically deactivated.) */
        map.setMyLocationEnabled(false);

        super.onPause();
    }
	private String getDirectionsUrl(LatLng origin,LatLng dest){
                    
        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;
        
        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;        
        
                    
        // Sensor enabled
        String sensor = "sensor=false";     
        
        String mode = "mode=walking";
                    
        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+mode;
                    
        // Output format
        String output = "json";
        
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        
        
        return url;
    }
    
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
                URL url = new URL(strUrl);

                // Creating an http connection to communicate with url 
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url 
                urlConnection.connect();

                // Reading data from url 
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb  = new StringBuffer();

                String line = "";
                while( ( line = br.readLine())  != null){
                        sb.append(line);
                }
                
                data = sb.toString();

                br.close();

        }catch(Exception e){
                Log.d("Exception while downloading url", e.toString());
        }finally{
                iStream.close();
                urlConnection.disconnect();
        }
        return data;
     }

    
    
    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{            
                
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
                
            // For storing data from web service
            String data = "";
                    
            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;        
        }
        
        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {            
            super.onPostExecute(result);            
            
            ParserTask parserTask = new ParserTask();
            
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
                
        }        
    }
    
    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
        
        // Parsing the data in non-ui thread        
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            
            JSONObject jObject;    
            List<List<HashMap<String, String>>> routes = null;                       
            
            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                
                // Starts parsing data
                routes = parser.parse(jObject);    
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }
        
        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            try{
            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();
                
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
                
                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);                    
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);    
                    
                    points.add(position);                        
                }
                
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(6);
                lineOptions.color(getResources().getColor(R.color.rojo_logo));    
                
            }
            
            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions); 
            }catch(Exception e){
            	
            }
        }            
    }

	

	/* Our custom LocationSource. 
     * We register this class to receive location updates from the Location Manager
     * and for that reason we need to also implement the LocationListener interface. */
    private class FollowMeLocationSource implements LocationSource, LocationListener {

        private OnLocationChangedListener mListener;
        private LocationManager locationManager;
        private final Criteria criteria = new Criteria();
        private String bestAvailableProvider;
        /* Updates are restricted to one every 10 seconds, and only when
         * movement of more than 10 meters has been detected.*/
        private final int minTime = 10000;     // minimum time interval between location updates, in milliseconds
        private final int minDistance = 10;    // minimum distance between location updates, in meters

        private FollowMeLocationSource() {
            // Get reference to Location Manager
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            // Specify Location Provider criteria
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            criteria.setAltitudeRequired(true);
            criteria.setBearingRequired(true);
            criteria.setSpeedRequired(true);
            criteria.setCostAllowed(true);
        }

        private void getBestAvailableProvider() {
            /* The preffered way of specifying the location provider (e.g. GPS, NETWORK) to use 
             * is to ask the Location Manager for the one that best satisfies our criteria.
             * By passing the 'true' boolean we ask for the best available (enabled) provider. */
            bestAvailableProvider = locationManager.getBestProvider(criteria, true);
        }

        /* Activates this provider. This provider will notify the supplied listener
         * periodically, until you call deactivate().
         * This method is automatically invoked by enabling my-location layer. */
        @Override
        public void activate(OnLocationChangedListener listener) {
            // We need to keep a reference to my-location layer's listener so we can push forward
            // location updates to it when we receive them from Location Manager.
            mListener = listener;

            // Request location updates from Location Manager
            if (bestAvailableProvider != null) {
                locationManager.requestLocationUpdates(bestAvailableProvider, minTime, minDistance, this);
            } else {
                // (Display a message/dialog) No Location Providers currently available.
            }
        }

        /* Deactivates this provider.
         * This method is automatically invoked by disabling my-location layer. */
        @Override
        public void deactivate() {
            // Remove location updates from Location Manager
            locationManager.removeUpdates(this);

            mListener = null;
        }

        @Override
        public void onLocationChanged(Location location) {
            /* Push location updates to the registered listener..
             * (this ensures that my-location layer will set the blue dot at the new/received location) */
            if (mListener != null) {
                mListener.onLocationChanged(location);
            }

            map.clear();
            /* ..and Animate camera to center on that location !
             * (the reason for we created this custom Location Source !) */
            map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            
            
            
            MarkerOptions destino_marker = new MarkerOptions();
            destino_marker.position( new LatLng(Double.parseDouble(lat_), Double.parseDouble(lng_)));
            destino_marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_pin));
            destino_marker.title("Evento");
            map.addMarker(destino_marker); 
          
            
            String url = getDirectionsUrl(latLng, new LatLng(Double.parseDouble(lat_), Double.parseDouble(lng_)));                
           DownloadTask downloadTask = new DownloadTask();
           downloadTask.execute(url);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
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

