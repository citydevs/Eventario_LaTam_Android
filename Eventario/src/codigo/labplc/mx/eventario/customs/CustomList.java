package codigo.labplc.mx.eventario.customs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import codigo.labplc.mx.eventario.R;

/**
 * clase que personaliza la lista de eventos
 * Created by mikesaurio on 05/05/14.
 */
public class CustomList extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] nombre;
    private final String[] hora_inicio;
    private final String[] hora_fin;
    private final String[] distancia;
    private final int[] imagen;


    /**
     * cosntructor
     * @param context (Activity)
     * @param nombre
     * @param hora_inicio
     * @param hora_fin
     * @param distancia
     * @param imagen
     */
    public CustomList(Activity context,String[] nombre,String[] hora_inicio,String[] hora_fin,String[] distancia,int[] imagen) {
        super(context, R.layout.list_simple, nombre);
        this.context = context;
        this.nombre = nombre;
        this.hora_inicio=hora_inicio;
        this.hora_fin= hora_fin;
        this.distancia= distancia;
        this.imagen=imagen;
    }
    
    
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_simple, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.row_tv_titulo);
        if(nombre[position].length()<=50){
        	txtTitle.setText(nombre[position]);
        }else{
        	txtTitle.setText(nombre[position].substring(0, 41)+"...");
        }
        TextView txtHorario = (TextView) rowView.findViewById(R.id.row_tv_horario);
        txtHorario.setText(hora_inicio[position]+" - "+hora_fin[position]);
        TextView txtDistancia = (TextView) rowView.findViewById(R.id.row_tv_tiempo);
        txtDistancia.setText(distancia[position]);
        ImageView row_iv_evento =(ImageView)rowView.findViewById(R.id.row_iv_evento);
        row_iv_evento.setImageDrawable(context.getResources().getDrawable(imagen[position]));
        return rowView;
    }
    
   
	
   
}