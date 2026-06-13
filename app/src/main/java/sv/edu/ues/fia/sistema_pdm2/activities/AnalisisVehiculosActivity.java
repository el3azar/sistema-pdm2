package sv.edu.ues.fia.sistema_pdm2.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import sv.edu.ues.fia.sistema_pdm2.ControladorServicio;
import sv.edu.ues.fia.sistema_pdm2.R;
import sv.edu.ues.fia.sistema_pdm2.Urls;

public class AnalisisVehiculosActivity extends AppCompatActivity {

    private TextView tvResumen, tvTopMarcas, tvTopColores, tvTopAnios, tvExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analisis_vehiculos);
        tvResumen    = findViewById(R.id.tvResumen);
        tvTopMarcas  = findViewById(R.id.tvTopMarcas);
        tvTopColores = findViewById(R.id.tvTopColores);
        tvTopAnios   = findViewById(R.id.tvTopAnios);
        tvExtras     = findViewById(R.id.tvExtras);
    }

    public void consultar(View v) {
        int servidor = (v.getId() == R.id.btnLocal) ? 0 : 1;
        String url   = Urls.build(servidor, Urls.ANALISIS_VEHICULOS);

        new Thread(() -> {
            String json      = ControladorServicio.get(url, this);
            JSONObject outer = ControladorServicio.parsearObjeto(json, this);

            if (outer == null || !outer.optBoolean("success")) {
                runOnUiThread(() -> tvResumen.setText("Error al cargar estadísticas"));
                return;
            }

            try {
                JSONObject stats = outer.getJSONObject("data").getJSONObject("estadisticas");

                String resumen = "Total vehículos : " + stats.getInt("totalVehiculos") + "\n" +
                                 "Total marcas    : " + stats.getInt("totalMarcas") + "\n" +
                                 "Total colores   : " + stats.getInt("totalColores");

                StringBuilder sbMarcas = new StringBuilder();
                JSONArray topMarcas = stats.getJSONArray("topMarcas");
                for (int i = 0; i < topMarcas.length(); i++) {
                    JSONObject m = topMarcas.getJSONObject(i);
                    sbMarcas.append(String.format("%2d. %-18s %3d veh. (%s%%)\n",
                        i + 1,
                        m.getString("marca"),
                        m.getInt("cantidad"),
                        m.getDouble("porcentaje")));
                }

                StringBuilder sbColores = new StringBuilder();
                JSONArray topColores = stats.getJSONArray("topColores");
                for (int i = 0; i < topColores.length(); i++) {
                    JSONObject c = topColores.getJSONObject(i);
                    sbColores.append(String.format("%2d. %-18s %3d veh. (%s%%)\n",
                        i + 1,
                        c.getString("color"),
                        c.getInt("cantidad"),
                        c.getDouble("porcentaje")));
                }

                StringBuilder sbAnios = new StringBuilder();
                JSONArray topAnios = stats.getJSONArray("topAnios");
                for (int i = 0; i < topAnios.length(); i++) {
                    JSONObject a = topAnios.getJSONObject(i);
                    sbAnios.append(String.format("%2d. %d   %3d veh. (%s%%)\n",
                        i + 1,
                        a.getInt("anio"),
                        a.getInt("cantidad"),
                        a.getDouble("porcentaje")));
                }

                String extras = "Edad promedio    : " + stats.getDouble("edadPromedio") + " años\n" +
                                "Año más antiguo  : " + stats.getInt("vehiculoMasAntiguo") + "\n" +
                                "Año más nuevo    : " + stats.getInt("vehiculoMasNuevo");

                runOnUiThread(() -> {
                    tvResumen.setText(resumen);
                    tvTopMarcas.setText(sbMarcas.toString().trim());
                    tvTopColores.setText(sbColores.toString().trim());
                    tvTopAnios.setText(sbAnios.toString().trim());
                    tvExtras.setText(extras);
                });

            } catch (Exception e) {
                runOnUiThread(() -> tvResumen.setText("Error al procesar estadísticas"));
            }
        }).start();
    }
}
