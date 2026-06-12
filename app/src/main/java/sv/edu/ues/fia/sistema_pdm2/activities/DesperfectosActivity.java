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

import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.fia.sistema_pdm2.ControladorServicio;
import sv.edu.ues.fia.sistema_pdm2.R;
import sv.edu.ues.fia.sistema_pdm2.Urls;

public class DesperfectosActivity extends AppCompatActivity {

    private ListView listView;
    private Spinner spinnerVehiculo;
    private List<Integer> listaIdsVehiculos = new ArrayList<>();
    private List<String> listaNombresVehiculos = new ArrayList<>();
    
    private int idVehiculoIntent;
    private String vinVehiculoIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desperfectos);

        listView = findViewById(R.id.listView);
        spinnerVehiculo = findViewById(R.id.spinnerVehiculo);

        idVehiculoIntent  = getIntent().getIntExtra("id_vehiculo", -1);
        vinVehiculoIntent = getIntent().getStringExtra("vin_vehiculo");

        cargarVehiculos();
    }

    private void cargarVehiculos() {
        // Intentamos cargar de local, si falla de externo
        new Thread(() -> {
            String url = Urls.build(0, Urls.VEHICULOS_LISTA);
            String json = ControladorServicio.get(url, this);
            if (json == null) {
                url = Urls.build(1, Urls.VEHICULOS_LISTA);
                json = ControladorServicio.get(url, this);
            }

            List<JSONObject> datos = ControladorServicio.parsearArray(json, this);
            
            listaIdsVehiculos.clear();
            listaNombresVehiculos.clear();

            for (JSONObject obj : datos) {
                try {
                    int id = obj.getInt("ID_VEHICULO");
                    String vin = obj.getString("VIN");
                    listaIdsVehiculos.add(id);
                    listaNombresVehiculos.add(id + " - " + vin);
                } catch (Exception ignored) {}
            }

            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                        android.R.layout.simple_spinner_item, listaNombresVehiculos);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerVehiculo.setAdapter(adapter);

                if (idVehiculoIntent != -1) {
                    for (int i = 0; i < listaIdsVehiculos.size(); i++) {
                        if (listaIdsVehiculos.get(i) == idVehiculoIntent) {
                            spinnerVehiculo.setSelection(i);
                            break;
                        }
                    }
                }
            });
        }).start();
    }

    public void consultar(View v) {
        if (spinnerVehiculo.getSelectedItem() == null) {
            Toast.makeText(this, "Debe seleccionar un vehículo para ver sus daños", Toast.LENGTH_SHORT).show();
            return;
        }

        int idSeleccionado = listaIdsVehiculos.get(spinnerVehiculo.getSelectedItemPosition());
        int servidor = (v.getId() == R.id.btnLocal) ? 0 : 1;
        
        // Llamada: ws_desperfectos_lista.php?id_vehiculo=X
        String url = Urls.build(servidor, Urls.DESPERFECTOS_LISTA, "id_vehiculo=" + idSeleccionado);

        new Thread(() -> {
            String json = ControladorServicio.get(url, this);
            List<JSONObject> datos = ControladorServicio.parsearArray(json, this);
            List<String> filas = new ArrayList<>();

            if (datos.isEmpty()) {
                filas.add("No se encontraron desperfectos registrados para este vehículo.");
            } else {
                for (JSONObject o : datos) {
                    try {
                        // Formato más descriptivo según la respuesta del WS
                        filas.add("Registro de Daño #" + o.getString("ID_DETALLE") + "\n" +
                                  "Descripción: " + o.getString("DESCRIPCION_DESPERFECTO") + "\n" +
                                  "ID Vehículo Asociado: " + o.getString("ID_VEHICULO"));
                    } catch (Exception ignored) {}
                }
            }

            runOnUiThread(() -> {
                listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filas));
                if (datos.isEmpty()) {
                    Toast.makeText(this, "Sin resultados", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    public void irAInsertar(View v) {
        Intent intent = new Intent(this, InsertarDesperfectoActivity.class);
        if (spinnerVehiculo.getSelectedItem() != null) {
            int idSel = listaIdsVehiculos.get(spinnerVehiculo.getSelectedItemPosition());
            String nombreSel = listaNombresVehiculos.get(spinnerVehiculo.getSelectedItemPosition());
            intent.putExtra("id_vehiculo", idSel);
            intent.putExtra("vin_vehiculo", nombreSel.split(" - ")[1]);
        }
        startActivity(intent);
    }
}
