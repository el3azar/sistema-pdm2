package sv.edu.ues.fia.sistema_pdm2.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.fia.sistema_pdm2.ControladorServicio;
import sv.edu.ues.fia.sistema_pdm2.R;
import sv.edu.ues.fia.sistema_pdm2.Urls;

public class VentasPorImportadorActivity extends AppCompatActivity {

    private EditText editIdImportador;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas_por_importador);
        ((TextView) findViewById(R.id.tvTitulo)).setText("Ventas por Importador");
        editIdImportador = findViewById(R.id.editIdImportador);
        listView         = findViewById(R.id.listView);
    }

    public void consultar(View v) {
        String idImportador = editIdImportador.getText().toString().trim();
        if (idImportador.isEmpty()) {
            editIdImportador.setError("Ingrese un ID de importador");
            return;
        }

        int servidor = (v.getId() == R.id.btnLocal) ? 0 : 1;
        String url   = Urls.build(servidor, Urls.VENTAS_POR_IMPORTADOR, "id_importador=" + idImportador);

        new Thread(() -> {
            String json      = ControladorServicio.get(url, this);
            List<JSONObject> datos = ControladorServicio.parsearArray(json, this);
            List<String>     filas = new ArrayList<>();
            for (JSONObject o : datos) {
                try {
                    filas.add("Fecha: "    + o.getString("FECHA_VENTA")         + "\n" +
                              "Vehículo: " + o.getString("NOMBRE_MARCA") + " " +
                                             o.getString("NOMBRE_MODELO")       + "\n" +
                              "Precio: $"  + o.getString("PRECIO"));
                } catch (Exception ignored) {}
            }
            runOnUiThread(() -> listView.setAdapter(
                new ArrayAdapter<>(this, R.layout.item_lista, filas)));
        }).start();
    }
}
