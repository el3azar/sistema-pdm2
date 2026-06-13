package sv.edu.ues.fia.sistema_pdm2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import sv.edu.ues.fia.sistema_pdm2.ControladorServicio;
import sv.edu.ues.fia.sistema_pdm2.R;
import sv.edu.ues.fia.sistema_pdm2.Urls;

public class DetalleImportadorActivity extends AppCompatActivity {

    private TextView tvDatos, tvDistrito, tvTelefonos, tvImportaciones;
    private int importadorId;
    private int servidorActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_importador);

        tvDatos         = findViewById(R.id.tvDatos);
        tvDistrito      = findViewById(R.id.tvDistrito);
        tvTelefonos     = findViewById(R.id.tvTelefonos);
        tvImportaciones = findViewById(R.id.tvImportaciones);

        importadorId   = getIntent().getIntExtra("id", 0);
        servidorActual = getIntent().getIntExtra("servidor", 0);

        cargar();
    }

    private void cargar() {
        String url = Urls.build(servidorActual, Urls.IMPORTADOR_COMPLETO, "id=" + importadorId);

        new Thread(() -> {
            String json      = ControladorServicio.get(url, this);
            JSONObject outer = ControladorServicio.parsearObjeto(json, this);

            if (outer == null || !outer.optBoolean("success")) {
                runOnUiThread(() -> tvDatos.setText("Error al cargar los datos"));
                return;
            }

            try {
                JSONObject data = outer.getJSONObject("data");
                JSONObject imp  = data.getJSONObject("importador");
                JSONObject dis  = data.getJSONObject("distrito");
                JSONArray  tels = data.getJSONArray("telefonos");
                JSONArray  imas = data.getJSONArray("importaciones");

                String datos = imp.optString("nombreImportador") + " " +
                               imp.optString("apellidoImportador") + "\n" +
                               "NUI: "         + imp.optString("nui", "—") + "\n" +
                               "Género: "      + imp.optString("genero", "—") + "\n" +
                               "Nacimiento: "  + imp.optString("fechaNacimiento", "—") + "\n" +
                               "Correo: "      + imp.optString("correoElectronico", "—") + "\n" +
                               "Dirección: "   + imp.optString("direccion", "—");

                String distrito = dis.optString("nombre", "—") + "\n" +
                                  "Municipio:    " + dis.optString("municipio", "—") + "\n" +
                                  "Departamento: " + dis.optString("departamento", "—");

                StringBuilder sbTels = new StringBuilder();
                for (int i = 0; i < tels.length(); i++) {
                    JSONObject t = tels.getJSONObject(i);
                    if (i > 0) sbTels.append("\n");
                    sbTels.append(t.optString("tipo")).append(": ").append(t.optString("numero"));
                }

                StringBuilder sbImas = new StringBuilder();
                for (int i = 0; i < imas.length(); i++) {
                    JSONObject im2 = imas.getJSONObject(i);
                    if (i > 0) sbImas.append("\n");
                    sbImas.append("• ").append(im2.optString("fecha"))
                          .append("  —  ").append(im2.optInt("cantidadVehiculos"))
                          .append(" vehículo(s)");
                }

                runOnUiThread(() -> {
                    tvDatos.setText(datos);
                    tvDistrito.setText(distrito);
                    tvTelefonos.setText(tels.length() == 0 ? "Sin teléfonos registrados" : sbTels.toString());
                    tvImportaciones.setText(imas.length() == 0 ? "Sin importaciones" : sbImas.toString());
                });

            } catch (Exception e) {
                runOnUiThread(() -> tvDatos.setText("Error al procesar los datos"));
            }
        }).start();
    }

    public void verImportaciones(View v) {
        Intent intent = new Intent(this, ImportacionesCompletasActivity.class);
        intent.putExtra("id_importador", importadorId);
        intent.putExtra("servidor", servidorActual);
        startActivity(intent);
    }
}
