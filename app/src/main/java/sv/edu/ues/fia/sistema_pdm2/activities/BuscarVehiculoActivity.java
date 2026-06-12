package sv.edu.ues.fia.sistema_pdm2.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import sv.edu.ues.fia.sistema_pdm2.ControladorServicio;
import sv.edu.ues.fia.sistema_pdm2.R;
import sv.edu.ues.fia.sistema_pdm2.Urls;

public class BuscarVehiculoActivity extends AppCompatActivity {

    private AutoCompleteTextView editVin;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_vehiculo);
        editVin  = findViewById(R.id.editVin);
        listView = findViewById(R.id.listView);
        editVin.setOnClickListener(view -> editVin.showDropDown());
        editVin.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) editVin.showDropDown();
        });
        cargarSugerenciasVin();
    }

    public void buscar(View v) {
        String vin = editVin.getText().toString().trim();
        
        if (vin.isEmpty()) { 
            editVin.setError("Ingrese un VIN"); 
            return; 
        }
        
        if (vin.length() != 17) {
            editVin.setError("El VIN debe tener exactamente 17 caracteres");
            Toast.makeText(this, "Longitud actual: " + vin.length(), Toast.LENGTH_SHORT).show();
            return;
        }

        int servidor = (v.getId() == R.id.btnLocal) ? 0 : 1;
        String url   = Urls.build(servidor, Urls.VEHICULOS_BUSCAR, "vin=" + encode(vin));

        new Thread(() -> {
            String json      = ControladorServicio.get(url, this);
            List<JSONObject> datos = ControladorServicio.parsearArray(json, this);
            List<String>     filas = new ArrayList<>();
            
            if (datos.isEmpty()) {
                runOnUiThread(() -> Toast.makeText(this, "No se encontró el vehículo", Toast.LENGTH_SHORT).show());
            }

            for (JSONObject o : datos) {
                try {
                    filas.add("VIN: "    + o.getString("VIN")           + "\n" +
                              "Marca: "  + o.getString("NOMBRE_MARCA")  + " " +
                                           o.getString("NOMBRE_MODELO") + "\n" +
                              "Año: "    + o.getString("ANIO")          + " | Color: " +
                                           o.getString("COLOR_VEHICULO")+ "\n" +
                              "Estado: " + o.getString("ESTADO_VEHICULO")+ " | Bodega: " +
                                           o.getString("NOMBRE_BODEGA"));
                } catch (Exception ignored) {}
            }
            runOnUiThread(() -> listView.setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filas)));
        }).start();
    }

    private void cargarSugerenciasVin() {
        new Thread(() -> {
            Set<String> vins = new LinkedHashSet<>();
            cargarVinsDesdeServidor(0, vins);
            cargarVinsDesdeServidor(1, vins);

            List<String> sugerencias = new ArrayList<>(vins);
            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        sugerencias);
                editVin.setAdapter(adapter);
                if (editVin.hasFocus()) {
                    editVin.showDropDown();
                }
            });
        }).start();
    }

    private void cargarVinsDesdeServidor(int servidor, Set<String> vins) {
        String url = Urls.build(servidor, Urls.VEHICULOS_LISTA);
        String json = ControladorServicio.get(url, this);
        List<JSONObject> datos = ControladorServicio.parsearArray(json, this);

        for (JSONObject o : datos) {
            String vin = o.optString("VIN", "").trim();
            if (!vin.isEmpty() && vin.length() == 17) {
                vins.add(vin);
            }
        }
    }

    private String encode(String valor) {
        try {
            return URLEncoder.encode(valor, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return valor;
        }
    }
}
