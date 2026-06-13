package sv.edu.ues.fia.sistema_pdm2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.fia.sistema_pdm2.ControladorServicio;
import sv.edu.ues.fia.sistema_pdm2.R;
import sv.edu.ues.fia.sistema_pdm2.Urls;

public class BuscarImportadorActivity extends AppCompatActivity {

    private EditText editNombre;
    private ListView listView;
    private final List<JSONObject> resultados = new ArrayList<>();
    private int servidorActual = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_importador);
        editNombre = findViewById(R.id.editNombre);
        listView   = findViewById(R.id.listView);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position >= resultados.size()) return;
            try {
                int importadorId = resultados.get(position).getInt("id");
                Intent intent = new Intent(this, DetalleImportadorActivity.class);
                intent.putExtra("id", importadorId);
                intent.putExtra("servidor", servidorActual);
                startActivity(intent);
            } catch (Exception ignored) {}
        });
    }

    public void buscar(View v) {
        String nombre = editNombre.getText().toString().trim();
        if (nombre.isEmpty()) { editNombre.setError("Ingrese un nombre"); return; }

        servidorActual = (v.getId() == R.id.btnLocal) ? 0 : 1;
        String url = Urls.build(servidorActual, Urls.IMPORTADORES_BUSCAR, "nombre=" + nombre);

        new Thread(() -> {
            String json      = ControladorServicio.get(url, this);
            JSONObject outer = ControladorServicio.parsearObjeto(json, this);
            List<JSONObject> datos = new ArrayList<>();

            if (outer != null && outer.optBoolean("success")) {
                try {
                    JSONArray arr = outer.getJSONArray("data");
                    for (int i = 0; i < arr.length(); i++) datos.add(arr.getJSONObject(i));
                } catch (Exception ignored) {}
            }

            resultados.clear();
            resultados.addAll(datos);

            List<String> filas = new ArrayList<>();
            for (JSONObject o : datos) {
                try {
                    filas.add(o.getString("nombreImportador") + " " + o.getString("apellidoImportador") + "\n" +
                              "NUI: " + o.optString("nui", "—") + "\n" +
                              o.optString("correoElectronico", "Sin correo"));
                } catch (Exception ignored) {}
            }
            if (filas.isEmpty()) filas.add("No se encontraron resultados");

            runOnUiThread(() -> listView.setAdapter(
                new ArrayAdapter<>(this, R.layout.item_lista, filas)));
        }).start();
    }
}
