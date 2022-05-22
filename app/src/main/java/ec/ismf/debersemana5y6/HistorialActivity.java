package ec.ismf.debersemana5y6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import ec.ismf.debersemana5y6.model.Historial;

public class HistorialActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout refresh;
    private ArrayList<Historial> historial = new ArrayList<>();
    private RecyclerView recyclerView;
    private HistorialAdaptador historialAdaptador;
    private DBHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        DBHelper db = new DBHelper(HistorialActivity.this);

        refresh = (SwipeRefreshLayout) findViewById(R.id.swipedown);
        recyclerView = (RecyclerView) findViewById(R.id.rwhistorial);
        refresh.setOnRefreshListener(this);
        refresh.post(new Runnable() {
            @Override
            public void run() {
                historial.clear();
                getDatos();
            }
        });
    }


    @Override
    public void onRefresh() {
        historial.clear();
        getDatos();
    }

    private void getDatos() {
        DBHelper db = new DBHelper(this);
        refresh.setRefreshing(true);
        try {
            Cursor res = db.listar();
            int n = res.getCount();

            if (n>0) {
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    Historial his = new Historial();
                    his.setCodigo(res.getString(0));
                    his.setNombre(res.getString(1));
                    his.setEdad(res.getString(2));
                    his.setEvento(res.getString(3));
                    his.setFechaevento(res.getString(4));
                    historial.add(his);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapterPush(historial);
        refresh.setRefreshing(false);
    }
    private void adapterPush(ArrayList<Historial> his) {
        historialAdaptador = new HistorialAdaptador(this,his);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(historialAdaptador);
    }
    public void irAInicio(View v){

        Button his;
        his = findViewById(R.id.regresar);
        his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HistorialActivity.this, "Regresando a Inicio...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent( HistorialActivity.this, MainActivity.class );
                startActivity(intent);
            }
        });
    }

}