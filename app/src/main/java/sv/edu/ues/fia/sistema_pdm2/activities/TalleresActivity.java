package sv.edu.ues.fia.sistema_pdm2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.fia.sistema_pdm2.ControladorServicio;
import sv.edu.ues.fia.sistema_pdm2.R;
import sv.edu.ues.fia.sistema_pdm2.Urls;

public class TalleresActivity extends AppCompatActivity {

    private ListView listView;
    private List<JSONObject> listaTalleres = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        
        TextView tvTitulo = findViewById(R.id.tvTitulo);
        if (tvTitulo != null) tvTitulo.setText("Catálogo de Talleres");
        
        listView = findViewById(R.id.listView);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (listaTalleres == null || listaTalleres.isEmpty()) return;
            try {
                JSONObject obj = listaTalleres.get(position);
                Intent intent = new Intent(this, ReparacionesActivity.class);
                intent.putExtra("ID_TALLER", obj.optString("ID_TALLER"));
                intent.putExtra("NOMBRE_TALLER", obj.optString("NOMBRE_TALLER"));
                startActivity(intent);
            } catch (Exception e) {
                Log.e("TalleresActivity", "Error al abrir reparaciones: " + e.getMessage());
            }
        });
    }

    public void consultar(View v) {
        int servidor = (v.getId() == R.id.btnLocal) ? 0 : 1;
        String url   = Urls.build(servidor, Urls.TALLERES_LISTA);

        new Thread(() -> {
            String json = ControladorServicio.get(url, this);
            List<JSONObject> datos = ControladorServicio.parsearArray(json, this);
            List<String> filas = new ArrayList<>();
            
            runOnUiThread(() -> {
                listaTalleres.clear();
                if (datos != null && !datos.isEmpty()) {
                    listaTalleres.addAll(datos);
                    for (JSONObject o : datos) {
                        // Usamos optString para evitar errores si algún campo es null en la BD
                        String nombre = o.optString("NOMBRE_TALLER", "Taller sin nombre");
                        String idT    = o.optString("ID_TALLER", "0");
                        String dir    = o.optString("DIRECCION_TALLER", "Sin dirección");
                        String tel    = o.optString("TELEFONO_TALLER", "");
                        String auth   = o.optString("AUTORIZADO", "0").equals("1") ? "[AUTORIZADO]" : "[NO AUTORIZADO]";
                        
                        String infoTel = tel.isEmpty() ? "" : " | Tel: " + tel;
                        
                        filas.add(idT + " - " + nombre + " " + auth + "\n" +
                                 "Dirección: " + dir + infoTel);
                    }
                } else {
                    filas.add("No se encontraron talleres o error de conexión.");
                }
                listView.setAdapter(new ArrayAdapter<>(this, R.layout.item_lista, filas));
            });
        }).start();
    }
}
