package ec.ismf.debersemana5y6;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import ec.ismf.debersemana5y6.model.Estudiante;
import ec.ismf.debersemana5y6.MainActivity;

public class EstudianteAdaptador extends RecyclerView.Adapter<EstudianteAdaptador.MiVistaHolder> {
    private Context context;
    private ArrayList<Estudiante> estudiante;
    private String host = "http://ectur.php.ec";
    private DBHelper db;

    public EstudianteAdaptador(Context context, ArrayList<Estudiante> estudiante) {
        this.context = context;
        this.estudiante = estudiante;
    }

    @NonNull
    @Override
    public EstudianteAdaptador.MiVistaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        vista = layoutInflater.inflate(R.layout.estudiante_lista, parent, false);
        return new MiVistaHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull EstudianteAdaptador.MiVistaHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.nro.setText(String.valueOf(position + 1)+". ");
        holder.codigo.setText(estudiante.get(position).getCodigo());
        holder.nombre.setText(estudiante.get(position).getNombre()+ " "+estudiante.get(position).getApellido());
        holder.edad.setText(estudiante.get(position).getEdad());

        holder.btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codigo =  estudiante.get(position).getCodigo();
                String nombre = estudiante.get(position).getNombre();
                String apellido = estudiante.get(position).getApellido();
                String edad = estudiante.get(position).getEdad();
                editarEstudiante(codigo, nombre, apellido, edad);
            }
        });
        holder.btn_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codigo =  estudiante.get(position).getCodigo();
                String nombre = estudiante.get(position).getNombre();
                String apellido = estudiante.get(position).getApellido();
                String edad = estudiante.get(position).getEdad();
                eliminarEstudiante(codigo, nombre, apellido, edad);
            }
        });
    }

    private void eliminarEstudiante(String _codigo, String _nombre, String _apellido, String _edad) {
        Enviar("DELETE", host+"/ismfws/estudiante/" + _codigo , new Dialog(context), _codigo, _nombre, _apellido, _edad);
    }

    private void editarEstudiante(String _codigo, String _nombre, String _apellido, String _edad) {
        TextView cerrar, titulo;
        EditText nombre, apellido, edad;
        Button guardar;
        final Dialog dialog;

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.estudiante_modifica);

        cerrar = (TextView) dialog.findViewById(R.id.cerrar);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();;
            }
        });
        titulo = (TextView) dialog.findViewById(R.id.titulo);
        titulo.setText("Editar Estudiante");

        nombre = (EditText) dialog.findViewById(R.id.nombre);
        nombre.setText(_nombre);
        apellido = (EditText) dialog.findViewById(R.id.apellido);
        apellido.setText(_apellido);
        edad = (EditText) dialog.findViewById(R.id.edad);
        edad.setText(_edad);

        guardar = (Button) dialog.findViewById(R.id.guardar);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Enviar("PUT", host+"/ismfws/estudiante/" + _codigo, dialog, _codigo, nombre.getText().toString(), apellido.getText().toString(), edad.getText().toString());
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();;

    }

    private void Enviar(String metodo, String ws, final Dialog dialog, String _codigo, String _nombre, String _apellido, String _edad) {
        JsonObjectRequest jsonObjReq;
        DBHelper db = new DBHelper(context);
        if (metodo == "PUT") {
            Map<String,String> params = new HashMap<>();
            params.put("nombre",_nombre);
            params.put("apellido",_apellido);
            params.put("edad",_edad);
             jsonObjReq = new JsonObjectRequest(Request.Method.PUT, ws,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        Toast.makeText(context, "Datos modificados.", Toast.LENGTH_SHORT).show();
                        db.insertar(_codigo, _nombre + " " +_apellido, _edad, "ACTUALIZACION");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error en la actualización del estudiante.", Toast.LENGTH_SHORT).show();
                    }
                });
            Volley.newRequestQueue(context).add(jsonObjReq);
        }

        if (metodo == "DELETE") {
            jsonObjReq = new JsonObjectRequest(Request.Method.DELETE, ws,
                    null ,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            dialog.dismiss();
                            Toast.makeText(context, "Datos eliminados.", Toast.LENGTH_SHORT).show();
                            db.insertar(_codigo, _nombre + " " +_apellido, _edad, "ELIMINADO");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Error en la eliminación del estudiante.", Toast.LENGTH_SHORT).show();
                        }
                    });
            Volley.newRequestQueue(context).add(jsonObjReq);
        }
    }

    @Override
    public int getItemCount() {
        return estudiante.size();
    }

    public class MiVistaHolder extends RecyclerView.ViewHolder{
        private TextView nro, codigo, nombre, edad;
        private ImageView btn_editar, btn_borrar;

        public MiVistaHolder(@NonNull View itemView){
            super(itemView);
            nro = (TextView) itemView.findViewById(R.id.nro);
            codigo = (TextView) itemView.findViewById(R.id.codigo);
            nombre = (TextView) itemView.findViewById(R.id.nombre);
            edad = (TextView) itemView.findViewById(R.id.edad);
            btn_editar = (ImageView) itemView.findViewById(R.id.btn_editar);
            btn_borrar = (ImageView) itemView.findViewById(R.id.btn_borrar);
        }
    }
}
