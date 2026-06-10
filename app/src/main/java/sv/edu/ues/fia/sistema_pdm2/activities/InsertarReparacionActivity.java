package sv.edu.ues.fia.sistema_pdm2.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import sv.edu.ues.fia.sistema_pdm2.ControladorServicio;
import sv.edu.ues.fia.sistema_pdm2.R;
import sv.edu.ues.fia.sistema_pdm2.Urls;

public class InsertarReparacionActivity extends AppCompatActivity {

    private EditText editIdVehiculo, editIdTaller, editFecha, editDescripcion;
    private TextView txtResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_reparacion);
        editIdVehiculo  = findViewById(R.id.editIdVehiculo);
        editIdTaller    = findViewById(R.id.editIdTaller);
        editFecha       = findViewById(R.id.editFecha);
        editDescripcion = findViewById(R.id.editDescripcion);
        txtResultado    = findViewById(R.id.txtResultado);
    }

    public void registrar(View v) {
        String idVehiculo = editIdVehiculo.getText().toString().trim();
        String fecha      = editFecha.getText().toString().trim();

        if (idVehiculo.isEmpty() || fecha.isEmpty()) {
            txtResultado.setText("ID vehículo y fecha son obligatorios.");
            return;
        }

        String params = "id_vehiculo="  + idVehiculo +
                        "&id_taller="   + editIdTaller.getText().toString().trim() +
                        "&fecha_inicio=" + fecha +
                        "&descripcion=" + editDescripcion.getText().toString().trim();

        int servidor = (v.getId() == R.id.btnLocal) ? 0 : 1;
        String url   = Urls.build(servidor, Urls.REPARACIONES_INSERT, params);

        new Thread(() -> {
            String json   = ControladorServicio.get(url, this);
            boolean exito = ControladorServicio.fueExitoso(json, this);
            runOnUiThread(() -> txtResultado.setText(
                exito ? "Reparación registrada correctamente." : "Error al registrar reparación."));
        }).start();
    }
}
