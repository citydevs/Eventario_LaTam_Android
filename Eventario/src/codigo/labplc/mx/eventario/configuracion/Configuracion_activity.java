package codigo.labplc.mx.eventario.configuracion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import codigo.labplc.mx.eventario.Eventario_main;
import codigo.labplc.mx.eventario.R;
import codigo.labplc.mx.eventario.dialogos.Dialogos;

/**
 * actividad que muestra las herramientas de eventario
 * @author mikesaurio
 *
 */
public class Configuracion_activity extends Activity implements OnSeekBarChangeListener{

	String progreso = "2"; //por defaul son 2 km de busqueda
	TextView configuracion_tv_distancia;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuracion);
		
		
	    Button configuracion_btn_aceptar=(Button)findViewById(R.id.configuracion_btn_aceptar);
	    configuracion_btn_aceptar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				guardaPreferencia();
				
			}
		});
		
	    
			TextView  configuracion_tv_acercade =(TextView)findViewById(R.id.configuracion_tv_acercade);
			configuracion_tv_acercade.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

					new Dialogos().showDialogExtras(Configuracion_activity.this, getResources().getString(R.string.configuracion_acercade),getResources().getString(R.string.detalle_acerca_de)).show();
				}
			});
			
			TextView  configuracion_tv_terminosuso =(TextView)findViewById(R.id.configuracion_tv_terminosuso);
			configuracion_tv_terminosuso.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new Dialogos().showDialogExtras(Configuracion_activity.this,getResources().getString(R.string.configuracion_terminos), getResources().getString(R.string.detalle_terminos)).show();
					
				}
			});
			TextView  configuracion_tv_creditos =(TextView)findViewById(R.id.configuracion_tv_creditos);
			configuracion_tv_creditos.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new Dialogos().showDialogExtras(Configuracion_activity.this,getResources().getString(R.string.configuracion_creditos), getResources().getString(R.string.detalle_ab_creditos)).show();
					
				}
			});	
			
			
	    
		 	configuracion_tv_distancia =(TextView) findViewById(R.id.configuracion_tv_distancia);
			SeekBar    seekbar = (SeekBar)findViewById(R.id.configuracion_sb_distancia); // make seekbar object
	        seekbar.setOnSeekBarChangeListener(this); 
	      
	        SharedPreferences prefs = getSharedPreferences("MisPreferenciasEventario",Context.MODE_PRIVATE);
			 progreso = prefs.getString("progreso", null);
			 if(progreso!=null){
				 seekbar.setProgress(Integer.parseInt(progreso));
				 configuracion_tv_distancia.setText(progreso+" Km");
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
	            	progreso=progress+"";
	       		 	configuracion_tv_distancia.setText(progreso+" Km");

	            }
	        });

	}
	    @Override
	    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
	    	progreso=progress+"";
	    }
	    @Override
	    public void onStartTrackingTouch(SeekBar seekBar) {
	        // TODO Auto-generated method stub

	    }
	    @Override
	    public void onStopTrackingTouch(SeekBar seekBar) {
	        // TODO Auto-generated method stub

	    }
	    
	    

	    /**
	     * guarda las preferencias del usuario 
	     */
	   public void guardaPreferencia(){
			//pone en blanco las preferencias
			SharedPreferences prefs = getSharedPreferences("MisPreferenciasEventario", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString("progreso", progreso);
			editor.commit();
			Intent intent = new Intent(Configuracion_activity.this,Eventario_main.class);
			startActivity(intent);
			finish();
	   }
	@Override
	public void onBackPressed() {
		guardaPreferencia();
	}
	   
	   
	   
}
