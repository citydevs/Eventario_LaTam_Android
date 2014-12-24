package codigo.labplc.mx.eventario.dialogos;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CalendarView.OnDateChangeListener;
import codigo.labplc.mx.eventario.R;

public class Dialogos {

	public static AlertDialog customDialog= null;	//Creamos el dialogo generico

	
	/**
	 * Dialogo para asegurar que quieres salir de la app
	 *
	 * @param Activity (actividad que llama al di���logo)
	 * @return Dialog (regresa el dialogo creado)
	 **/
	
	public Dialog showDialogGPS(final Activity activity)
    {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	    View view = activity.getLayoutInflater().inflate(R.layout.dialogo_gps, null);
	    builder.setView(view);
	    builder.setCancelable(true);
        //tipografias

	  //escucha del boton aceptar
        ((Button) view.findViewById(R.id.dialogo_salir_btnAceptar)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
            	Dialogos.customDialog.dismiss(); 
            	activity.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
        });

        ((Button) view.findViewById(R.id.dialogo_salir_btnCancelar)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
            	Dialogos.customDialog.dismiss(); 
                activity.finish();
            }
        });
        return (customDialog=builder.create());// return customDialog;//regresamos el di���logo
    }   
	
	
	
	
	
	
	
	
	/**
	 * Toast custom 
	 * @param context (Activity) actividad que lo llama
	 * @param text (String) texto a mostrar
	 * @param duration (int) duracion del toast
	 */
		public  void Toast(Activity context, String text, int duration) {
			LayoutInflater inflater = context.getLayoutInflater();
			View layouttoast = inflater.inflate(R.layout.toastcustom, (ViewGroup)context.findViewById(R.id.toastcustom));
			((TextView) layouttoast.findViewById(R.id.texttoast)).setText(text);
			((TextView) layouttoast.findViewById(R.id.texttoast)).setTextColor(context.getResources().getColor(R.color.negro));
			Toast mytoast = new Toast(context);
	        mytoast.setView(layouttoast);
	        mytoast.setDuration(Toast.LENGTH_LONG);
	        mytoast.show();
		}
	
		/**
		 * obtener los milisegundos de una fecha
		 * 
		 * @return
		 */
		public static long getFechaHoy() {
			Calendar now = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String fechaCel = now.get(Calendar.DAY_OF_MONTH) + "/"
					+ ((now.get(Calendar.MONTH)) + 1) + "/"
					+ now.get(Calendar.YEAR) + " " + now.get(Calendar.HOUR_OF_DAY)
					+ ":" + now.get(Calendar.MINUTE) + ":"
					+ now.get(Calendar.SECOND);
			try {
				return (formatter.parse(fechaCel)).getTime();
			} catch (java.text.ParseException e) {
				e.printStackTrace();
				return 0;
			}
		}
}
