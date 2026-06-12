package sv.edu.ues.fia.sistema_pdm2.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.fia.sistema_pdm2.ControladorServicio;
import sv.edu.ues.fia.sistema_pdm2.R;
import sv.edu.ues.fia.sistema_pdm2.Urls;

public class HistorialMovimientosActivity extends AppCompatActivity {

    private EditText editVin;
    private TextView txtVehiculo;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_movimientos);
        editVin     = findViewById(R.id.editVin);
        txtVehiculo = findViewById(R.id.txtVehiculo);
        listView    = findViewById(R.id.listView);
    }

    public void consultar(View v) {
        String vin = editVin.getText().toString().trim();
        if (vin.isEmpty()) { editVin.setError("Ingrese un VIN"); return; }

        int servidor = (v.getId() == R.id.btnLocal) ? 0 : 1;
        String url   = Urls.build(servidor, Urls.MOVIMIENTOS_HISTORIAL, "vin=" + vin);

        new Thread(() -> {
            String json = ControladorServicio.get(url, this);
            JSONObject raiz = ControladorServicio.parsearObjeto(json, this);

            String ficha = "";
            List<String> filas = new ArrayList<>();

            try {
                if (raiz == null || raiz.has("error")) {
                    ficha = (raiz != null) ? raiz.getString("error") : "Sin respuesta del servidor.";
                } else {
                    JSONObject ve = raiz.getJSONObject("vehiculo");
                    ficha = ve.getString("NOMBRE_MARCA") + " " + ve.getString("NOMBRE_MODELO") +
                            " " + ve.getString("ANIO") + " · " + ve.getString("COLOR_VEHICULO") +
                            "\nEstado actual: " + ve.getString("ESTADO_VEHICULO");

                    JSONArray movs = raiz.getJSONArray("movimientos");
                    for (int i = 0; i < movs.length(); i++) {
                        JSONObject m = movs.getJSONObject(i);
                        filas.add(m.getString("FECHA_MOVIMIENTO") + " · " +
                                  m.getString("TIPO_MOVIMIENTO").toUpperCase() + "\n" +
                                  "Bodega: "     + m.optString("NOMBRE_BODEGA", "—") + "\n" +
                                  "Transporte: " + m.optString("PLACA", "—") + " (" +
                                                   m.optString("DESCRIPCION_TIPO_TRANSPORTE", "—") + ")\n" +
                                  "Personal: "   + m.optString("PERSONAL", "—") + " (" +
                                                   m.optString("CARGO", "—") + ")\n" +
                                  "Motivo: "     + m.optString("MOTIVO", "—"));
                    }
                    if (filas.isEmpty()) filas.add("Este vehículo no tiene movimientos registrados.");
                }
            } catch (Exception ignored) {}

            String fichaFinal = ficha;
            runOnUiThread(() -> {
                txtVehiculo.setText(fichaFinal);
                listView.setAdapter(new ArrayAdapter<>(this, R.layout.item_lista, filas));
            });
        }).start();
    }
}
