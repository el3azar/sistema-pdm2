package sv.edu.ues.fia.sistema_pdm2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import sv.edu.ues.fia.sistema_pdm2.ControladorServicio;
import sv.edu.ues.fia.sistema_pdm2.R;
import sv.edu.ues.fia.sistema_pdm2.Urls;

public class ReparacionesActivity extends AppCompatActivity {

    private Spinner spinnerVehiculo;
    private ListView listView;
    private TextView txtTitulo;
    private View layoutBusqueda;
    private final List<JSONObject> listaReparaciones = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reparaciones);
        
        spinnerVehiculo = findViewById(R.id.spinnerVehiculo);
        listView       = findViewById(R.id.listView);
        txtTitulo      = findViewById(R.id.txtTitulo);
        layoutBusqueda = findViewById(R.id.layoutBusqueda);

        // Click en un elemento de la lista para actualizar
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (listaReparaciones.isEmpty()) return;
            try {
                JSONObject obj = listaReparaciones.get(position);
                String idRep = obj.getString("ID_REPARACION");
                String fechaIni = obj.getString("FECHA_INICIO");
                
                Intent intent = new Intent(this, ActualizarReparacionActivity.class);
                intent.putExtra("ID_REPARACION", idRep);
                intent.putExtra("FECHA_INICIO", fechaIni);
                startActivity(intent);
            } catch (Exception e) {
                Log.e("ReparacionesActivity", "Error al abrir actualización: " + e.getMessage());
            }
        });

        // Verificar si viene de otra pantalla con datos pre-cargados
        String idTaller = getIntent().getStringExtra("ID_TALLER");
        String nombreTaller = getIntent().getStringExtra("NOMBRE_TALLER");

        if (idTaller != null) {
            if (txtTitulo != null) {
                String titulo = getString(R.string.reparaciones_taller_prefix, nombreTaller);
                txtTitulo.setText(titulo);
            }
            if (layoutBusqueda != null) layoutBusqueda.setVisibility(View.GONE);
            ejecutarConsulta(Urls.base(0) + Urls.REPARACIONES_POR_TALLER + "?id_taller=" + idTaller);
        } else {
            cargarVehiculos();
        }
    }

    private void cargarVehiculos() {
        new Thread(() -> {
            String url = Urls.build(0, Urls.VEHICULOS_LISTA);
            String json = ControladorServicio.get(url, this);
            List<JSONObject> data = ControladorServicio.parsearArray(json, this);
            
            List<String> items = new ArrayList<>();
            items.add("Seleccionar vehículo");

            for (JSONObject obj : data) {
                try {
                    String item = obj.getString("ID_VEHICULO") + " · " +
                                 obj.getString("NOMBRE_MARCA") + " " +
                                 obj.getString("NOMBRE_MODELO") + " (" +
                                 obj.getString("VIN") + ")";
                    items.add(item);
                } catch (Exception e) {
                    Log.e("ReparacionesActivity", "Error al procesar vehículo: " + e.getMessage());
                }
            }

            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this, android.R.layout.simple_spinner_dropdown_item, items);
                if (spinnerVehiculo != null) spinnerVehiculo.setAdapter(adapter);
            });
        }).start();
    }

    public void consultar(View v) {
        if (spinnerVehiculo == null || spinnerVehiculo.getSelectedItemPosition() <= 0) {
            return;
        }

        String item = spinnerVehiculo.getSelectedItem().toString();
        String idVehiculo = item.split(" · ")[0];

        int servidor = (v.getId() == R.id.btnLocal) ? 0 : 1;
        String url = Urls.build(servidor, Urls.REPARACIONES_LISTA, "id_vehiculo=" + idVehiculo);

        ejecutarConsulta(url);
    }

    private void ejecutarConsulta(String url) {
        new Thread(() -> {
            String json      = ControladorServicio.get(url, this);
            List<JSONObject> datos = ControladorServicio.parsearArray(json, this);
            List<String>     filas = new ArrayList<>();
            
            runOnUiThread(() -> {
                listaReparaciones.clear();
                if (!datos.isEmpty()) {
                    listaReparaciones.addAll(datos);
                    for (JSONObject o : datos) {
                        try {
                            StringBuilder sb = new StringBuilder();
                            if (o.has("NOMBRE_TALLER")) sb.append("Taller: ").append(o.getString("NOMBRE_TALLER")).append("\n");
                            if (o.has("VIN"))           sb.append("Vehículo: ").append(o.getString("VIN")).append("\n");

                            sb.append("Inicio: ").append(o.getString("FECHA_INICIO"));
                            if (o.has("FECHA_FIN") && !o.isNull("FECHA_FIN") && !Objects.equals(o.getString("FECHA_FIN"), "null")) {
                                sb.append(" | Fin: ").append(o.getString("FECHA_FIN"));
                            }
                            sb.append("\n").append(o.getString("DESCRIPCION_TRABAJO"));

                            filas.add(sb.toString());
                        } catch (Exception ignored) {}
                    }
                } else {
                    filas.add("No se encontraron reparaciones.");
                }
                listView.setAdapter(new ArrayAdapter<>(this, R.layout.item_lista, filas));
            });
        }).start();
    }
}
