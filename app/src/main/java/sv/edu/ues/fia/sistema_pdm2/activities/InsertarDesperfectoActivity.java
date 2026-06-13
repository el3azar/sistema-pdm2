package sv.edu.ues.fia.sistema_pdm2.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

public class InsertarDesperfectoActivity extends AppCompatActivity {

    private Spinner spinnerVehiculo;
    private EditText editDescripcion;
    private TextView txtResultado;
    
    private List<Integer> listaIdsVehiculos = new ArrayList<>();
    private List<String> listaNombresVehiculos = new ArrayList<>();
    private int idVehiculoIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_desperfecto);

        spinnerVehiculo = findViewById(R.id.spinnerVehiculo);
        editDescripcion = findViewById(R.id.editDescripcion);
        txtResultado = findViewById(R.id.txtResultado);

        idVehiculoIntent = getIntent().getIntExtra("id_vehiculo", -1);

        // Cargamos los vehículos de ambos servidores para asegurar que el Spinner tenga datos
        cargarVehiculos();
    }

    private void cargarVehiculos() {
        // Intentamos cargar del local por defecto
        new Thread(() -> {
            String url = Urls.build(0, Urls.VEHICULOS_LISTA);
            String json = ControladorServicio.get(url, this);
            
            // Si el local falla, intentamos el externo
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
                
                if (listaNombresVehiculos.isEmpty()) {
                    txtResultado.setText("No se pudieron cargar vehículos. Verifique conexión.");
                }
            });
        }).start();
    }

    public void registrar(View v) {
        if (spinnerVehiculo.getSelectedItem() == null) {
            Toast.makeText(this, "Debe seleccionar un vehículo", Toast.LENGTH_SHORT).show();
            return;
        }

        int idSeleccionado = listaIdsVehiculos.get(spinnerVehiculo.getSelectedItemPosition());
        String descripcion = editDescripcion.getText().toString().trim();

        if (descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese la descripción", Toast.LENGTH_SHORT).show();
            return;
        }

        int servidor = (v.getId() == R.id.btnLocal) ? 0 : 1;
        String params = "id_vehiculo=" + idSeleccionado + "&descripcion=" + descripcion;
        String url = Urls.build(servidor, Urls.DESPERFECTOS_INSERT, params);

        new Thread(() -> {
            String json = ControladorServicio.get(url, this);
            boolean exito = ControladorServicio.fueExitoso(json, this);

            runOnUiThread(() -> {
                if (exito) {
                    txtResultado.setText("¡Desperfecto registrado exitosamente!");
                    Toast.makeText(this, "Guardado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    txtResultado.setText("Error al registrar: verifique que el vehículo exista en el servidor seleccionado.");
                }
            });
        }).start();
    }
}
