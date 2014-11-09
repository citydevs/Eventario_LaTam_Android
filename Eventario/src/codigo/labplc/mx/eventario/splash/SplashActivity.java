package codigo.labplc.mx.eventario.splash;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import codigo.labplc.mx.eventario.Eventario_main;
import codigo.labplc.mx.eventario.R;
import codigo.labplc.mx.eventario.utils.Utils;

public class SplashActivity extends Activity {

	
	 private static final long SPLASH_SCREEN_DELAY = 1000;
	 public final String TAG = this.getClass().getSimpleName();
	 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		FrameLayout frame_splash = (FrameLayout) findViewById(R.id.frame_splash);
		Point p = Utils.getTamanoPantalla(SplashActivity.this); //tama–o de pantalla
		
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(p.x / 2, p.y / 3);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		frame_splash.setLayoutParams(lp);
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
