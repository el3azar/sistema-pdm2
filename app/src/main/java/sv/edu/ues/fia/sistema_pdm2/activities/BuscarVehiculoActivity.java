package sv.edu.ues.fia.sistema_pdm2.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.fia.sistema_pdm2.ControladorServicio;
import sv.edu.ues.fia.sistema_pdm2.R;
import sv.edu.ues.fia.sistema_pdm2.Urls;

public class BuscarVehiculoActivity extends AppCompatActivity {

    private EditText editVin;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_vehiculo);
        editVin  = findViewById(R.id.editVin);
        listView = findViewById(R.id.listView);
    }

    public void buscar(View v) {
        String vin   = editVin.getText().toString().trim();
        if (vin.isEmpty()) { editVin.setError("Ingrese un VIN"); return; }

        int servidor = (v.getId() == R.id.btnLocal) ? 0 : 1;
        String url   = Urls.build(servidor, Urls.VEHICULOS_BUSCAR, "vin=" + vin);

        new Thread(() -> {
            String json      = ControladorServicio.get(url, this);
            List<JSONObject> datos = ControladorServicio.parsearArray(json, this);
            List<String>     filas = new ArrayList<>();
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
                new ArrayAdapter<>(this, R.layout.item_lista, filas)));
        }).start();
    }
}
