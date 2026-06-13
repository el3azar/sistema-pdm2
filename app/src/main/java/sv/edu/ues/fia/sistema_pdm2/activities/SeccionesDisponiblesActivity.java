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

public class SeccionesDisponiblesActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secciones_disponibles);
        ((TextView) findViewById(R.id.tvTitulo)).setText("Secciones Disponibles");
        listView = findViewById(R.id.listView);
    }

    public void consultar(View v) {
        int servidor = (v.getId() == R.id.btnLocal) ? 0 : 1;
        String url   = Urls.build(servidor, Urls.SECCIONES_DISPONIBLES);

        new Thread(() -> {
            String json      = ControladorServicio.get(url, this);
            List<JSONObject> datos = ControladorServicio.parsearArray(json, this);
            List<String>     filas = new ArrayList<>();
            for (JSONObject o : datos) {
                try {
                    filas.add("Bodega: " + o.getString("NOMBRE_BODEGA") + "\n" +
                              "Nivel: "   + o.getString("NIVEL") +
                              " | Disponible: " + o.getString("DISPONIBLE") +
                              " (" + o.getString("CAPACIDAD_ACTUAL") +
                              "/" + o.getString("CAPACIDAD_MAXIMA") + ")");
                } catch (Exception ignored) {}
            }
            runOnUiThread(() -> listView.setAdapter(
                new ArrayAdapter<>(this, R.layout.item_lista, filas)));
        }).start();
    }
}
