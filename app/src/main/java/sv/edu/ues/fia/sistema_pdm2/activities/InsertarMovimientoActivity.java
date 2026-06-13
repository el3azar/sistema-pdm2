package sv.edu.ues.fia.sistema_pdm2.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sv.edu.ues.fia.sistema_pdm2.ControladorServicio;
import sv.edu.ues.fia.sistema_pdm2.R;
import sv.edu.ues.fia.sistema_pdm2.Urls;

public class InsertarMovimientoActivity extends AppCompatActivity {

    private Spinner spVehiculo, spTransporte, spPersonal, spBodega, spTipo;
    private EditText editFecha, editMotivo;
    private TextView txtResultado;

    // IDs reales paralelos a las opciones mostradas en cada Spinner
    private final List<String> idsVehiculo   = new ArrayList<>();
    private final List<String> idsTransporte = new ArrayList<>();
    private final List<String> idsPersonal   = new ArrayList<>();
    private final List<String> idsBodega     = new ArrayList<>();

    // Servidor elegido al cargar catálogos (0=Local, 1=Externo)
    private int servidor = 0;
    private boolean catalogosCargados = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_movimiento);
        spVehiculo   = findViewById(R.id.spVehiculo);
        spTransporte = findViewById(R.id.spTransporte);
        spPersonal   = findViewById(R.id.spPersonal);
        spBodega     = findViewById(R.id.spBodega);
        spTipo       = findViewById(R.id.spTipo);
        editFecha    = findViewById(R.id.editFecha);
        editMotivo   = findViewById(R.id.editMotivo);
        txtResultado = findViewById(R.id.txtResultado);

        spTipo.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"entrada", "salida"}));

        editFecha.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date()));
        configurarCalendario(editFecha);
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

    public void cargarCatalogos(View v) {
        servidor   = (v.getId() == R.id.btnLocal) ? 0 : 1;
        String url = Urls.build(servidor, Urls.MOVIMIENTOS_CATALOGOS);
        txtResultado.setText("Cargando catálogos...");

        new Thread(() -> {
            String json     = ControladorServicio.get(url, this);
            JSONObject raiz = ControladorServicio.parsearObjeto(json, this);

            List<String> vehiculos   = new ArrayList<>();
            List<String> transportes = new ArrayList<>();
            List<String> personal    = new ArrayList<>();
            List<String> bodegas     = new ArrayList<>();
            idsVehiculo.clear(); idsTransporte.clear();
            idsPersonal.clear(); idsBodega.clear();

            try {
                JSONArray arr = raiz.getJSONArray("vehiculos");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    idsVehiculo.add(o.getString("ID_VEHICULO"));
                    vehiculos.add(o.getString("VIN"));
                }
                arr = raiz.getJSONArray("transportes");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    idsTransporte.add(o.getString("ID_TRANSPORTE"));
                    transportes.add(o.getString("PLACA") + " — " +
                                    o.optString("DESCRIPCION_TIPO_TRANSPORTE", ""));
                }
                arr = raiz.getJSONArray("personal");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    idsPersonal.add(o.getString("ID_PERSONAL"));
                    personal.add(o.getString("PERSONAL") + " (" + o.getString("CARGO") + ")");
                }
                arr = raiz.getJSONArray("bodegas");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    idsBodega.add(o.getString("ID_BODEGA"));
                    bodegas.add(o.getString("NOMBRE_BODEGA"));
                }
            } catch (Exception ignored) {}

            boolean exito = !vehiculos.isEmpty();
            runOnUiThread(() -> {
                catalogosCargados = exito;
                spVehiculo.setAdapter(new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item, vehiculos));
                spTransporte.setAdapter(new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item, transportes));
                spPersonal.setAdapter(new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item, personal));
                spBodega.setAdapter(new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item, bodegas));
                txtResultado.setText(exito
                        ? "Catálogos cargados. Complete el formulario y registre."
                        : "No se pudieron cargar los catálogos. Verifique el servidor.");
            });
        }).start();
    }

    public void registrar(View v) {
        if (!catalogosCargados) {
            txtResultado.setText("Primero cargue los catálogos (paso 1).");
            return;
        }
        String fecha  = editFecha.getText().toString().trim();
        String motivo = editMotivo.getText().toString().trim();
        if (fecha.isEmpty() || motivo.isEmpty()) {
            txtResultado.setText("Fecha y motivo son obligatorios.");
            return;
        }

        String params = "id_vehiculo="     + idsVehiculo.get(spVehiculo.getSelectedItemPosition()) +
                        "&id_transporte="  + idsTransporte.get(spTransporte.getSelectedItemPosition()) +
                        "&id_personal="    + idsPersonal.get(spPersonal.getSelectedItemPosition()) +
                        "&id_bodega="      + idsBodega.get(spBodega.getSelectedItemPosition()) +
                        "&tipo_movimiento=" + spTipo.getSelectedItem().toString() +
                        "&fecha="          + fecha +
                        "&motivo="         + motivo.replace(" ", "%20");

        String url = Urls.build(servidor, Urls.MOVIMIENTOS_INSERT, params);

        new Thread(() -> {
            String json     = ControladorServicio.get(url, this);
            JSONObject resp = ControladorServicio.parsearObjeto(json, this);
            String mensaje;
            try {
                mensaje = (resp != null) ? resp.getString("mensaje") : "Sin respuesta del servidor.";
            } catch (Exception e) {
                mensaje = "Error al registrar movimiento.";
            }
            String mensajeFinal = mensaje;
            runOnUiThread(() -> txtResultado.setText(mensajeFinal));
        }).start();
    }
}
