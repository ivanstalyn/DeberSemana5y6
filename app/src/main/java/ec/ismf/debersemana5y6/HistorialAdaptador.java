package ec.ismf.debersemana5y6;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import ec.ismf.debersemana5y6.model.Historial;

public class HistorialAdaptador extends RecyclerView.Adapter<HistorialAdaptador.MiVistaHolder> {
    private Context context;
    private ArrayList<Historial> historial;
    private DBHelper db;

    public HistorialAdaptador(Context context, ArrayList<Historial> historial) {
        this.context = context;
        this.historial = historial;
    }

    @NonNull
    @Override
    public HistorialAdaptador.MiVistaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        vista = layoutInflater.inflate(R.layout.historial_lista, parent, false);
        return new MiVistaHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialAdaptador.MiVistaHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.codigo.setText(historial.get(position).getCodigo());
        holder.nombre.setText(historial.get(position).getNombre());
        //holder.edad.setText(historial.get(position).getEdad());
        holder.evento.setText(historial.get(position).getEvento());
        holder.fecha.setText(historial.get(position).getFechaevento());
        
    }

  
    @Override
    public int getItemCount() {
        return historial.size();
    }

    public class MiVistaHolder extends RecyclerView.ViewHolder{
        private TextView nombre, codigo, evento, fecha, edad;


        public MiVistaHolder(@NonNull View itemView){
            super(itemView);
            codigo = (TextView) itemView.findViewById(R.id.codigo);
            nombre = (TextView) itemView.findViewById(R.id.nombre);
            edad = (TextView) itemView.findViewById(R.id.edad);
            evento = (TextView) itemView.findViewById(R.id.evento);
            fecha = (TextView) itemView.findViewById(R.id.fecha);
        }
    }
}
