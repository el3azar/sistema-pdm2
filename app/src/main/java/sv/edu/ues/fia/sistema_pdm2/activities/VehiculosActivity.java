package sv.edu.ues.fia.sistema_pdm2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import sv.edu.ues.fia.sistema_pdm2.ControladorServicio;
import sv.edu.ues.fia.sistema_pdm2.R;
import sv.edu.ues.fia.sistema_pdm2.Urls;

public class VehiculosActivity extends AppCompatActivity {

    private ListView listView;
    private Spinner spinnerEstado;
    private List<JSONObject> listaVehiculos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculos);
        
        listView = findViewById(R.id.listView);
        spinnerEstado = findViewById(R.id.spinnerEstado);
        ((TextView) findViewById(R.id.tvTitulo)).setText("Vehículos");

        cargarEstadosDesdeBD(0);

        // Al hacer clic en un vehículo, vamos a ver sus desperfectos
        listView.setOnItemClickListener((parent, view, position, id) -> {
            try {
                JSONObject vehiculo = listaVehiculos.get(position);
                Intent intent = new Intent(this, DesperfectosActivity.class);
                intent.putExtra("id_vehiculo", vehiculo.getInt("ID_VEHICULO"));
                intent.putExtra("vin_vehiculo", vehiculo.getString("VIN"));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void consultar(View v) {
        int servidor = (v.getId() == R.id.btnLocal) ? 0 : 1;
        String estadoSeleccionado = spinnerEstado.getSelectedItem().toString();
        
        String url;
        if (estadoSeleccionado.equals("Todos")) {
            url = Urls.build(servidor, Urls.VEHICULOS_LISTA);
        } else {
            url = Urls.build(servidor, Urls.VEHICULOS_POR_ESTADO, "estado=" + encode(estadoSeleccionado));
        }

        new Thread(() -> {
            String json = ControladorServicio.get(url, this);
            listaVehiculos = ControladorServicio.parsearArray(json, this);
            List<String> filas = new ArrayList<>();
            for (JSONObject o : listaVehiculos) {
                try {
                    filas.add(o.getString("VIN") + " | " +
                              o.getString("NOMBRE_MARCA") + " " + o.getString("NOMBRE_MODELO") +
                              "\n" + o.getString("ANIO") + " | " + o.getString("COLOR_VEHICULO") + 
                              " | " + o.getString("ESTADO_VEHICULO"));
                } catch (Exception ignored) {}
            }
            runOnUiThread(() -> {
                if (filas.isEmpty()) {
                    Toast.makeText(this, "No se encontraron vehículos", Toast.LENGTH_SHORT).show();
                }
                listView.setAdapter(new ArrayAdapter<>(this, R.layout.item_lista_bonita, filas));
            });
        }).start();
    }

    private void cargarEstadosDesdeBD(int servidor) {
        new Thread(() -> {
            String json = ControladorServicio.get(Urls.build(servidor, Urls.VEHICULOS_LISTA), this);
            List<JSONObject> vehiculos = ControladorServicio.parsearArray(json, this);
            Set<String> estados = new LinkedHashSet<>();
            estados.add("Todos");
            for (JSONObject vehiculo : vehiculos) {
                String estado = vehiculo.optString("ESTADO_VEHICULO", "").trim();
                if (!estado.isEmpty()) estados.add(estado);
            }
            runOnUiThread(() -> spinnerEstado.setAdapter(new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    new ArrayList<>(estados)
            )));
        }).start();
    }

    private String encode(String valor) {
        try {
            return URLEncoder.encode(valor, "UTF-8");
        } catch (Exception e) {
            return valor;
        }
    }
}
