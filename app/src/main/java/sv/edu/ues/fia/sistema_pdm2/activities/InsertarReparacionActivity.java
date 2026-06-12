package sv.edu.ues.fia.sistema_pdm2.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sv.edu.ues.fia.sistema_pdm2.ControladorServicio;
import sv.edu.ues.fia.sistema_pdm2.R;
import sv.edu.ues.fia.sistema_pdm2.Urls;

public class InsertarReparacionActivity extends AppCompatActivity {

    private Spinner spinnerVehiculo, spinnerTaller;
    private TextInputEditText editFechaInicio, editDescription;
    private TextInputLayout layoutFechaInicio;
    private TextView txtResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_reparacion);

        spinnerVehiculo   = findViewById(R.id.spinnerVehiculo);
        spinnerTaller     = findViewById(R.id.spinnerTaller);
        editFechaInicio   = findViewById(R.id.editFechaInicio);
        layoutFechaInicio = findViewById(R.id.layoutFechaInicio);
        editDescription   = findViewById(R.id.editDescripcion);
        txtResultado      = findViewById(R.id.txtResultado);

        // Configurar botones
        Button btnLocal = findViewById(R.id.btnLocal);
        Button btnExterno = findViewById(R.id.btnExterno);
        btnLocal.setOnClickListener(this::registrar);
        btnExterno.setOnClickListener(this::registrar);

        cargarSpinners();

        editFechaInicio.setOnClickListener(v -> abrirCalendario(editFechaInicio, 0));
        layoutFechaInicio.setEndIconOnClickListener(v -> abrirCalendario(editFechaInicio, 0));
    }

    private void cargarSpinners() {
        // Carga dinámica de Talleres desde el servidor local (Fase 1 - Opción B)
        new Thread(() -> {
            // Construimos la URL usando la constante centralizada
            String url = Urls.build(0, Urls.TALLERES_AUTORIZADOS);
            String json = ControladorServicio.get(url, this);
            List<JSONObject> data = ControladorServicio.parsearArray(json, this);

            List<String> listaTalleres = new ArrayList<>();
            listaTalleres.add("Seleccionar taller");

            if (!data.isEmpty()) {
                for (JSONObject obj : data) {
                    try {
                        String item = obj.getString("ID_TALLER") + " · " +
                                     obj.getString("NOMBRE_TALLER");
                        listaTalleres.add(item);
                    } catch (Exception e) {
                        Log.e("InsertarReparacion", "Error al procesar taller: " + e.getMessage());
                    }
                }
            }

            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this, android.R.layout.simple_spinner_dropdown_item, listaTalleres);
                spinnerTaller.setAdapter(adapter);
            });
        }).start();

        // Carga dinámica de Vehículos desde el servidor local
        new Thread(() -> {
            String url = Urls.build(0, Urls.VEHICULOS_LISTA);
            String json = ControladorServicio.get(url, this);
            List<JSONObject> data = ControladorServicio.parsearArray(json, this);
            
            List<String> listaVehiculos = new ArrayList<>();
            listaVehiculos.add("Seleccionar vehículo");

            if (!data.isEmpty()) {
                for (JSONObject obj : data) {
                    try {
                        String item = obj.getString("ID_VEHICULO") + " · " +
                                     obj.getString("NOMBRE_MARCA") + " " +
                                     obj.getString("NOMBRE_MODELO") + " (" +
                                     obj.getString("VIN") + ")";
                        listaVehiculos.add(item);
                    } catch (Exception e) {
                        Log.e("InsertarReparacion", "Error al procesar JSON: " + e.getMessage());
                    }
                }
            }

            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this, android.R.layout.simple_spinner_dropdown_item, listaVehiculos);
                spinnerVehiculo.setAdapter(adapter);
            });
        }).start();
    }

    private void abrirCalendario(TextInputEditText campo, long minDate) {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, day) -> {
            String fecha = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day);
            campo.setText(fecha);
        },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        if (minDate > 0) {
            dialog.getDatePicker().setMinDate(minDate);
        }
        dialog.show();
    }

    public void registrar(View v) {
        if (spinnerVehiculo.getSelectedItemPosition() == 0) {
            txtResultado.setText("Selecciona un vehículo."); return;
        }
        if (spinnerTaller.getSelectedItemPosition() == 0) {
            txtResultado.setText("Selecciona un taller."); return;
        }

        // Extraer solo el ID (número) antes del separador " · "
        String idVehiculo = spinnerVehiculo.getSelectedItem().toString().split(" · ")[0];
        String idTaller   = spinnerTaller.getSelectedItem().toString().split(" · ")[0];
        
        String fechaInicio = (editFechaInicio.getText() != null) ? editFechaInicio.getText().toString().trim() : "";
        String descripcion = (editDescription.getText() != null) ? editDescription.getText().toString().trim() : "";

        if (fechaInicio.isEmpty()) {
            txtResultado.setText("La fecha de inicio es obligatoria."); return;
        }

        try {
            String params = "id_vehiculo="  + idVehiculo  +
                    "&id_taller="           + idTaller    +
                    "&fecha_inicio="        + fechaInicio +
                    "&descripcion="         + URLEncoder.encode(descripcion, StandardCharsets.UTF_8.toString());

            int servidor = (v.getId() == R.id.btnLocal) ? 0 : 1;
            String url   = Urls.build(servidor, Urls.REPARACIONES_INSERT, params);

            new Thread(() -> {
                String json   = ControladorServicio.get(url, this);
                boolean exito = ControladorServicio.fueExitoso(json, this);
                runOnUiThread(() -> txtResultado.setText(
                        exito ? "Reparación registrada correctamente." : "Error al registrar reparación."));
            }).start();
            
        } catch (Exception e) {
            txtResultado.setText("Error al procesar datos.");
        }
    }
}
