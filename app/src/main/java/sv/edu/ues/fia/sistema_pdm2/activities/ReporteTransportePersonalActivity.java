package sv.edu.ues.fia.sistema_pdm2.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import sv.edu.ues.fia.sistema_pdm2.ControladorServicio;
import sv.edu.ues.fia.sistema_pdm2.R;
import sv.edu.ues.fia.sistema_pdm2.Urls;

public class ReporteTransportePersonalActivity extends AppCompatActivity {

    private EditText editFechaInicio, editFechaFin;
    private ListView listBodega, listPersonal, listTransporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_transporte_personal);
        editFechaInicio = findViewById(R.id.editFechaInicio);
        editFechaFin    = findViewById(R.id.editFechaFin);
        listBodega      = findViewById(R.id.listBodega);
        listPersonal    = findViewById(R.id.listPersonal);
        listTransporte  = findViewById(R.id.listTransporte);

        configurarCalendario(editFechaInicio);
        configurarCalendario(editFechaFin);
    }

    // Abre un DatePickerDialog y escribe la fecha elegida en formato YYYY-MM-DD
    private void configurarCalendario(EditText campo) {
        campo.setFocusable(false);
        campo.setClickable(true);
        campo.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (vista, anio, mes, dia) ->
                    campo.setText(String.format(Locale.US, "%04d-%02d-%02d", anio, mes + 1, dia)),
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    public void consultar(View v) {
        int servidor = (v.getId() == R.id.btnLocal) ? 0 : 1;

        String inicio = editFechaInicio.getText().toString().trim();
        String fin    = editFechaFin.getText().toString().trim();
        StringBuilder params = new StringBuilder();
        if (!inicio.isEmpty()) params.append("fecha_inicio=").append(inicio);
        if (!fin.isEmpty()) {
            if (params.length() > 0) params.append("&");
            params.append("fecha_fin=").append(fin);
        }

        String url = Urls.build(servidor, Urls.REPORTE_TRANSPORTE_PERSONAL, params.toString());

        new Thread(() -> {
            String json = ControladorServicio.get(url, this);
            if (json == null) return;

            List<String> filasBodega     = new ArrayList<>();
            List<String> filasPersonal   = new ArrayList<>();
            List<String> filasTransporte = new ArrayList<>();

            try {
                JSONObject raiz = new JSONObject(json);

                JSONArray arr = raiz.getJSONArray("por_bodega");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    filasBodega.add(o.getString("NOMBRE_BODEGA") +
                            " — Entradas: " + o.getString("ENTRADAS") +
                            " | Salidas: "  + o.getString("SALIDAS") +
                            " | Total: "    + o.getString("TOTAL"));
                }
                arr = raiz.getJSONArray("por_personal");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    filasPersonal.add(o.getString("PERSONAL") +
                            " (" + o.getString("CARGO") + "): " +
                            o.getString("TOTAL") + " movimientos");
                }
                arr = raiz.getJSONArray("por_transporte");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    filasTransporte.add(o.getString("PLACA") +
                            " (" + o.optString("DESCRIPCION_TIPO_TRANSPORTE", "—") +
                            ", cap. " + o.optString("CAPACIDAD_MAX_VEHICULOS", "—") + "): " +
                            o.getString("VIAJES") + " viajes");
                }
            } catch (Exception ignored) {}

            runOnUiThread(() -> {
                listBodega.setAdapter(new ArrayAdapter<>(this, R.layout.item_lista, filasBodega));
                listPersonal.setAdapter(new ArrayAdapter<>(this, R.layout.item_lista, filasPersonal));
                listTransporte.setAdapter(new ArrayAdapter<>(this, R.layout.item_lista, filasTransporte));
            });
        }).start();
    }
}
