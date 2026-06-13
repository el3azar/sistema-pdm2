package sv.edu.ues.fia.sistema_pdm2.activities;

import android.os.Bundle;
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

public class ImportacionesCompletasActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importaciones_completas);
        listView = findViewById(R.id.listView);

        int idImportador = getIntent().getIntExtra("id_importador", 0);
        int servidor     = getIntent().getIntExtra("servidor", 0);
        cargar(idImportador, servidor);
    }

    private void cargar(int idImportador, int servidor) {
        String url = Urls.build(servidor, Urls.IMPORTACIONES_COMPLETAS, "id_importador=" + idImportador);

        new Thread(() -> {
            String json      = ControladorServicio.get(url, this);
            JSONObject outer = ControladorServicio.parsearObjeto(json, this);
            List<String> filas = new ArrayList<>();

            if (outer != null && outer.optBoolean("success")) {
                try {
                    JSONArray importaciones = outer.getJSONObject("data").getJSONArray("importaciones");
                    for (int i = 0; i < importaciones.length(); i++) {
                        JSONObject im       = importaciones.getJSONObject(i);
                        JSONArray vehiculos = im.getJSONArray("vehiculos");
                        JSONObject totales  = im.getJSONObject("totales");

                        StringBuilder sb = new StringBuilder();
                        sb.append("Importación #").append(im.getInt("id"))
                          .append("  —  ").append(im.getString("fecha")).append("\n");
                        sb.append("Vehículos: ").append(totales.getInt("cantidadVehiculos"))
                          .append("   Desperfectos: ").append(totales.getInt("cantidadDesperfectos")).append("\n");

                        for (int j = 0; j < vehiculos.length(); j++) {
                            JSONObject veh = vehiculos.getJSONObject(j);
                            sb.append("  • ").append(veh.optString("marca")).append(" ")
                              .append(veh.optString("modelo")).append(" (").append(veh.optInt("anio")).append(")")
                              .append(" — ").append(veh.optString("color")).append("\n");

                            JSONArray desperfectos = veh.getJSONArray("desperfectos");
                            for (int k = 0; k < desperfectos.length(); k++) {
                                JSONObject d = desperfectos.getJSONObject(k);
                                sb.append("    - ").append(d.optString("tipo"))
                                  .append(": ").append(d.optString("descripcion")).append("\n");
                            }
                        }

                        filas.add(sb.toString().trim());
                    }
                } catch (Exception ignored) {}
            }

            if (filas.isEmpty()) filas.add("Sin importaciones disponibles");

            runOnUiThread(() -> listView.setAdapter(
                new ArrayAdapter<>(this, R.layout.item_lista, filas)));
        }).start();
    }
}
