package sv.edu.ues.fia.sistema_pdm2.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.fia.sistema_pdm2.ControladorServicio;
import sv.edu.ues.fia.sistema_pdm2.R;
import sv.edu.ues.fia.sistema_pdm2.Urls;

public class EstadisticasActivity extends AppCompatActivity {

    private ListView listEstado, listTipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);
        listEstado = findViewById(R.id.listEstado);
        listTipo   = findViewById(R.id.listTipo);
    }

    public void consultar(View v) {
        int servidor = (v.getId() == R.id.btnLocal) ? 0 : 1;
        String url   = Urls.build(servidor, Urls.ESTADISTICAS);

        new Thread(() -> {
            String json = ControladorServicio.get(url, this);
            if (json == null) return;

            List<String> filasEstado = new ArrayList<>();
            List<String> filasTipo   = new ArrayList<>();

            try {
                JSONObject raiz      = new JSONObject(json);
                JSONArray  porEstado = raiz.getJSONArray("por_estado");
                JSONArray  porTipo   = raiz.getJSONArray("por_tipo");

                for (int i = 0; i < porEstado.length(); i++) {
                    JSONObject o = porEstado.getJSONObject(i);
                    filasEstado.add(o.getString("ESTADO_VEHICULO") + ": " + o.getString("TOTAL"));
                }
                for (int i = 0; i < porTipo.length(); i++) {
                    JSONObject o = porTipo.getJSONObject(i);
                    filasTipo.add(o.getString("DESCRIPCION_TIPO_VEHICULO") + ": " + o.getString("TOTAL"));
                }
            } catch (Exception ignored) {}

            runOnUiThread(() -> {
                listEstado.setAdapter(new ArrayAdapter<>(this, R.layout.item_lista, filasEstado));
                listTipo.setAdapter(new ArrayAdapter<>(this, R.layout.item_lista, filasTipo));
            });
        }).start();
    }
}
