package sv.edu.ues.fia.sistema_pdm2.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import sv.edu.ues.fia.sistema_pdm2.ControladorServicio;
import sv.edu.ues.fia.sistema_pdm2.R;
import sv.edu.ues.fia.sistema_pdm2.Urls;

public class InsertarVentaActivity extends AppCompatActivity {

    private EditText editIdVehiculo, editIdImportador, editFecha, editPrecio;
    private TextView txtResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_venta);
        editIdVehiculo   = findViewById(R.id.editIdVehiculo);
        editIdImportador = findViewById(R.id.editIdImportador);
        editFecha        = findViewById(R.id.editFecha);
        editPrecio       = findViewById(R.id.editPrecio);
        txtResultado     = findViewById(R.id.txtResultado);
    }

    public void registrar(View v) {
        String idVehiculo   = editIdVehiculo.getText().toString().trim();
        String fecha        = editFecha.getText().toString().trim();
        String precio       = editPrecio.getText().toString().trim();

        if (idVehiculo.isEmpty() || fecha.isEmpty() || precio.isEmpty()) {
            txtResultado.setText("ID vehículo, fecha y precio son obligatorios.");
            return;
        }

        String params = "id_vehiculo="   + idVehiculo +
                        "&id_importador=" + editIdImportador.getText().toString().trim() +
                        "&fecha_venta="  + fecha +
                        "&precio="       + precio;

        int servidor = (v.getId() == R.id.btnLocal) ? 0 : 1;
        String url   = Urls.build(servidor, Urls.VENTAS_INSERT, params);

        new Thread(() -> {
            String json   = ControladorServicio.get(url, this);
            boolean exito = ControladorServicio.fueExitoso(json, this);
            runOnUiThread(() -> txtResultado.setText(
                exito ? "Venta registrada correctamente." : "Error al registrar venta."));
        }).start();
    }
}
