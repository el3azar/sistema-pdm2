package sv.edu.ues.fia.sistema_pdm2.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import sv.edu.ues.fia.sistema_pdm2.ControladorServicio;
import sv.edu.ues.fia.sistema_pdm2.R;
import sv.edu.ues.fia.sistema_pdm2.Urls;

public class ActualizarReparacionActivity extends AppCompatActivity {

    private String idReparacion, fechaInicio;
    private TextInputEditText editFechaFin;
    private CheckBox cbAptoVenta, cbRequiereRep;
    private TextView txtResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_reparacion);

        // Obtener datos enviados desde la lista
        idReparacion = getIntent().getStringExtra("ID_REPARACION");
        fechaInicio  = getIntent().getStringExtra("FECHA_INICIO");
        
        TextView txtInfo = findViewById(R.id.txtInfoReparacion);
        editFechaFin = findViewById(R.id.editFechaFin);
        cbAptoVenta  = findViewById(R.id.cbAptoVenta);
        cbRequiereRep = findViewById(R.id.cbRequiereRep);
        txtResultado = findViewById(R.id.txtResultado);
        
        txtInfo.setText(getString(R.string.reparacion_id_label, Objects.requireNonNullElse(idReparacion, "N/A")));

        editFechaFin.setOnClickListener(v -> abrirCalendario());
        findViewById(R.id.btnActualizarLocal).setOnClickListener(v -> actualizar(0));
        findViewById(R.id.btnActualizarExterno).setOnClickListener(v -> actualizar(1));
    }

    private void abrirCalendario() {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, day) -> {
            String fecha = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day);
            editFechaFin.setText(fecha);
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        // Validación: Deshabilitar fechas anteriores a la de inicio
        if (fechaInicio != null && !fechaInicio.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date date = sdf.parse(fechaInicio);
                if (date != null) {
                    dialog.getDatePicker().setMinDate(date.getTime());
                }
            } catch (Exception e) {
                Log.e("ActualizarReparacion", "Error al parsear fecha inicio: " + e.getMessage());
            }
        }

        dialog.show();
    }

    private void actualizar(int servidor) {
        if (idReparacion == null) return;

        String fechaFin = (editFechaFin.getText() != null) ? editFechaFin.getText().toString().trim() : "";
        int aptoVenta   = cbAptoVenta.isChecked() ? 1 : 0;
        int requiereRep = cbRequiereRep.isChecked() ? 1 : 0;

        String params = "id_reparacion=" + idReparacion +
                        "&apto_para_venta=" + aptoVenta +
                        "&requiere_otra_reparacion=" + requiereRep +
                        "&fecha_fin=" + fechaFin;

        String url = Urls.build(servidor, Urls.REPARACIONES_UPDATE, params);

        new Thread(() -> {
            String json = ControladorServicio.get(url, this);
            boolean exito = ControladorServicio.fueExitoso(json, this);
            runOnUiThread(() -> {
                txtResultado.setText(exito ? "Reparación actualizada" : "Error al actualizar");
            });
        }).start();
    }
}
