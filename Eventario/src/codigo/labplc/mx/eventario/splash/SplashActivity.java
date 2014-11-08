package codigo.labplc.mx.eventario.splash;

import java.util.Timer;
import java.util.TimerTask;

import codigo.labplc.mx.eventario.Eventario_main;
import codigo.labplc.mx.eventario.R;
import codigo.labplc.mx.eventario.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {

	
	 private static final long SPLASH_SCREEN_DELAY = 1000;
	 public final String TAG = this.getClass().getSimpleName();
	 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		iniciarSplash();
	}
	
	/**
	 * Muestra la pantalla splash, inicia la actividad principal 
	 * 
	 */
	public void iniciarSplash(){
	        
	        TimerTask task = new TimerTask() {
	            @Override
	            public void run() {
	            	
	            		Intent mainIntent = new Intent().setClass(SplashActivity.this, Eventario_main.class);
	            		 startActivity(mainIntent);
	            	
	                finish();
	            }
	        };
	        Timer timer = new Timer();
	        timer.schedule(task, SPLASH_SCREEN_DELAY);
	    }

	
	@Override
	public void onBackPressed() {
	}
}
