package ec.ismf.debersemana5y6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ec.ismf.debersemana5y6.model.Estudiante;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RequestQueue requestQueue;
    private SwipeRefreshLayout refresh;
    private ArrayList<Estudiante> estudiante = new ArrayList<>();
    private JsonObjectRequest jsonRequest;
    private RecyclerView recyclerView;
    private Dialog dialog;
    private EstudianteAdaptador estudianteAdaptador;
    private String host = "http://ectur.php.ec";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refresh = (SwipeRefreshLayout) findViewById(R.id.swipedown);
        recyclerView = (RecyclerView) findViewById(R.id.estudiante);
        dialog = new Dialog( this);
        refresh.setOnRefreshListener(this);
        refresh.post(new Runnable() {
                         @Override
                         public void run() {
                             estudiante.clear();
                             getDatos();
                         }
                     });
    }

    private void adapterPush(ArrayList<Estudiante> estudiante) {
        estudianteAdaptador = new EstudianteAdaptador(this,estudiante);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(estudianteAdaptador);
    }

    public void addEstudiante(View v){
        TextView cerrar, titulo;
        EditText nombre, apellido, edad;
        Button guardar;
        dialog.setContentView(R.layout.estudiante_modifica);

        cerrar = (TextView) dialog.findViewById(R.id.cerrar);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();;
            }
        });

        titulo = (TextView) dialog.findViewById(R.id.titulo);
        titulo.setText("Añadir Estudiante");

        nombre = (EditText) dialog.findViewById(R.id.nombre);
        apellido = (EditText) dialog.findViewById(R.id.apellido);
        edad = (EditText) dialog.findViewById(R.id.edad);
        guardar = (Button) dialog.findViewById(R.id.guardar);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Enviar(nombre.getText().toString(), apellido.getText().toString(), edad.getText().toString());
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();;

    }

    @Override
    public void onRefresh() {
        estudiante.clear();
        getDatos();
    }
    /* GET LISTA */
    public void getDatos() {
        refresh.setRefreshing(true);

        jsonRequest = new JsonObjectRequest(Request.Method.GET, host +"/ismfws/estudiantes", null ,
                new Response.Listener<JSONObject>()   {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray resultado = response.getJSONArray("resultado");
                            JSONObject jsonObject;

                            for (int i = 0; i < resultado.length(); i++) {
                                try {
                                    jsonObject = resultado.getJSONObject(i);
                                    Estudiante student = new Estudiante();
                                    student.setNombre(jsonObject.getString("nombre"));
                                    student.setCodigo(jsonObject.getString("codigo"));
                                    student.setApellido(jsonObject.getString("apellido"));
                                    student.setEdad(jsonObject.getString("edad"));
                                    estudiante.add(student);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapterPush(estudiante);
                        refresh.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonRequest);
    }

    /* POST CREAR NUEVO */
    private void Enviar(String _nombre, String _apellido, String _edad) {

        Map<String,String> params = new HashMap<>();
        params.put("nombre",_nombre);
        params.put("apellido",_apellido);
        params.put("edad",_edad);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, host+"/ismfws/estudiante",
                new JSONObject(params) ,
                new Response.Listener<JSONObject>()   {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        refresh.post(new Runnable() {
                            @Override
                            public void run() {
                                estudiante.clear();
                                getDatos();
                            }
                        });
                        Toast.makeText(getApplicationContext(), "Datos enviados.", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error de creación en la creación del estudiante.", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(jsonObjReq);
    }

}