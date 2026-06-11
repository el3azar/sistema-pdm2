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

public class ReparacionesActivity extends AppCompatActivity {

    private EditText editIdVehiculo;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reparaciones);
        editIdVehiculo = findViewById(R.id.editIdVehiculo);
        listView       = findViewById(R.id.listView);
    }

    public void consultar(View v) {
        String idVehiculo = editIdVehiculo.getText().toString().trim();
        if (idVehiculo.isEmpty()) { editIdVehiculo.setError("Ingrese un ID"); return; }

        int servidor = (v.getId() == R.id.btnLocal) ? 0 : 1;
        String url   = Urls.build(servidor, Urls.REPARACIONES_LISTA, "id_vehiculo=" + idVehiculo);

        new Thread(() -> {
            String json      = ControladorServicio.get(url, this);
            List<JSONObject> datos = ControladorServicio.parsearArray(json, this);
            List<String>     filas = new ArrayList<>();
            for (JSONObject o : datos) {
                try {
                    filas.add("Taller: "  + o.getString("NOMBRE_TALLER")      + "\n" +
                              "Inicio: "  + o.getString("FECHA_INICIO")       +
                              " | Fin: "  + o.getString("FECHA_FIN")          + "\n" +
                              o.getString("DESCRIPCION_TRABAJO"));
                } catch (Exception ignored) {}
            }
            runOnUiThread(() -> listView.setAdapter(
                new ArrayAdapter<>(this, R.layout.item_lista, filas)));
        }).start();
    }
}
