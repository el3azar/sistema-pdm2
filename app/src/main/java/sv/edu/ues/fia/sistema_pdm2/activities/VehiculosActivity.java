package sv.edu.ues.fia.sistema_pdm2.activities;

import android.os.Bundle;
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

public class VehiculosActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        ((TextView) findViewById(R.id.tvTitulo)).setText("Vehículos");
        listView = findViewById(R.id.listView);
    }

    public void consultar(View v) {
        int servidor = (v.getId() == R.id.btnLocal) ? 0 : 1;
        String url   = Urls.build(servidor, Urls.VEHICULOS_LISTA);

        new Thread(() -> {
            String json      = ControladorServicio.get(url, this);
            List<JSONObject> datos = ControladorServicio.parsearArray(json, this);
            List<String>     filas = new ArrayList<>();
            for (JSONObject o : datos) {
                try {
                    filas.add(o.getString("VIN") + " | " +
                              o.getString("NOMBRE_MARCA") + " " + o.getString("NOMBRE_MODELO") +
                              " " + o.getString("ANIO") + " | " + o.getString("ESTADO_VEHICULO"));
                } catch (Exception ignored) {}
            }
            runOnUiThread(() -> listView.setAdapter(
                new ArrayAdapter<>(this, R.layout.item_lista, filas)));
        }).start();
    }
}
