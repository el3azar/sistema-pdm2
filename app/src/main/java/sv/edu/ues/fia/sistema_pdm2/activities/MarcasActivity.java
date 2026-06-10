package sv.edu.ues.fia.sistema_pdm2.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.fia.sistema_pdm2.ControladorServicio;
import sv.edu.ues.fia.sistema_pdm2.R;
import sv.edu.ues.fia.sistema_pdm2.Urls;

public class MarcasActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        listView = findViewById(R.id.listView);
    }

    public void consultar(View v) {
        int servidor = (v.getId() == R.id.btnLocal) ? 0 : 1;
        String url   = Urls.build(servidor, Urls.MARCAS_LISTA);

        new Thread(() -> {
            String json       = ControladorServicio.get(url, this);
            List<JSONObject>  datos = ControladorServicio.parsearArray(json, this);
            List<String>      filas = new ArrayList<>();
            for (JSONObject o : datos) {
                try { filas.add(o.getString("ID_MARCA") + " - " + o.getString("NOMBRE_MARCA")); }
                catch (Exception ignored) {}
            }
            runOnUiThread(() -> listView.setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filas)));
        }).start();
    }
}
