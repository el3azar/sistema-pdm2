package sv.edu.ues.fia.sistema_pdm2.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import sv.edu.ues.fia.sistema_pdm2.ControladorServicio;
import sv.edu.ues.fia.sistema_pdm2.R;
import sv.edu.ues.fia.sistema_pdm2.Urls;

public class InsertarVehiculoActivity extends AppCompatActivity {

    private EditText editVin, editAnio, editColor, editEstado,
                     editIdModelo, editIdTipo, editIdSeccion, editIdImportacion;
    private TextView txtResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_vehiculo);
        editVin           = findViewById(R.id.editVin);
        editAnio          = findViewById(R.id.editAnio);
        editColor         = findViewById(R.id.editColor);
        editEstado        = findViewById(R.id.editEstado);
        editIdModelo      = findViewById(R.id.editIdModelo);
        editIdTipo        = findViewById(R.id.editIdTipo);
        editIdSeccion     = findViewById(R.id.editIdSeccion);
        editIdImportacion = findViewById(R.id.editIdImportacion);
        txtResultado      = findViewById(R.id.txtResultado);
    }

    public void registrar(View v) {
        String vin    = editVin.getText().toString().trim();
        String anio   = editAnio.getText().toString().trim();
        String color  = editColor.getText().toString().trim();
        String estado = editEstado.getText().toString().trim();

        if (vin.isEmpty() || anio.isEmpty() || color.isEmpty() || estado.isEmpty()) {
            txtResultado.setText("Complete todos los campos obligatorios.");
            return;
        }

        String params = "vin="             + vin    +
                        "&anio="           + anio   +
                        "&color="          + color  +
                        "&estado="         + estado +
                        "&id_modelo="      + editIdModelo.getText().toString().trim()      +
                        "&id_tipo_vehiculo=" + editIdTipo.getText().toString().trim()      +
                        "&id_seccion="     + editIdSeccion.getText().toString().trim()     +
                        "&id_importacion=" + editIdImportacion.getText().toString().trim();

        int servidor = (v.getId() == R.id.btnLocal) ? 0 : 1;
        String url   = Urls.build(servidor, Urls.VEHICULOS_INSERT, params);

        new Thread(() -> {
            String json   = ControladorServicio.get(url, this);
            boolean exito = ControladorServicio.fueExitoso(json, this);
            runOnUiThread(() -> txtResultado.setText(
                exito ? "Vehículo registrado correctamente." : "Error al registrar vehículo."));
        }).start();
    }
}
