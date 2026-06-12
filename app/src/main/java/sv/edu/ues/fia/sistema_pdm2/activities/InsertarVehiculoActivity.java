package sv.edu.ues.fia.sistema_pdm2.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import sv.edu.ues.fia.sistema_pdm2.ControladorServicio;
import sv.edu.ues.fia.sistema_pdm2.R;
import sv.edu.ues.fia.sistema_pdm2.Urls;

public class InsertarVehiculoActivity extends AppCompatActivity {

    private EditText editVin, editAnio, editColor,
                     editIdModelo, editIdTipo, editIdSeccion, editIdImportacion;
    private MaterialAutoCompleteTextView spinnerEstado;
    private TextView txtResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_vehiculo);
        editVin = findViewById(R.id.editVin);
        editAnio = findViewById(R.id.editAnio);
        editColor = findViewById(R.id.editColor);
        spinnerEstado = findViewById(R.id.spinnerEstado);
        editIdModelo = findViewById(R.id.editIdModelo);
        editIdTipo = findViewById(R.id.editIdTipo);
        editIdSeccion = findViewById(R.id.editIdSeccion);
        editIdImportacion = findViewById(R.id.editIdImportacion);
        txtResultado = findViewById(R.id.txtResultado);

        configurarListaEstados(estadosBase());
        prepararDropdown(spinnerEstado);
        cargarEstadosDesdeBd();
    }

    public void registrar(View v) {
        String vin = editVin.getText().toString().trim().toUpperCase();
        String anio = editAnio.getText().toString().trim();
        String color = editColor.getText().toString().trim();
        String estado = spinnerEstado.getText().toString().trim();
        String idModelo = editIdModelo.getText().toString().trim();
        String idTipo = editIdTipo.getText().toString().trim();
        String idSeccion = editIdSeccion.getText().toString().trim();
        String idImportacion = editIdImportacion.getText().toString().trim();

        if (vin.isEmpty() || anio.isEmpty() || color.isEmpty() ||
            idModelo.isEmpty() || idTipo.isEmpty() || idSeccion.isEmpty() || idImportacion.isEmpty()) {
            txtResultado.setText("Error: Todos los campos son obligatorios.");
            Toast.makeText(this, "Faltan campos por completar", Toast.LENGTH_SHORT).show();
            return;
        }

        if (estado.equals("Seleccionar")) {
            txtResultado.setText("Error: Seleccione el estado actual del vehículo.");
            spinnerEstado.setError("Seleccione un estado");
            return;
        }

        if (!color.matches("[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+")) {
            txtResultado.setText("Error: El color solo debe contener letras.");
            editColor.setError("Solo letras");
            return;
        }

        if (vin.length() != 17) {
            txtResultado.setText("Error: El VIN debe tener exactamente 17 caracteres.");
            editVin.setError("Longitud requerida: 17");
            return;
        }

        if (anio.length() != 4) {
            txtResultado.setText("Error: El año debe tener exactamente 4 dígitos.");
            editAnio.setError("Ingrese 4 dígitos");
            return;
        }

        try {
            int year = Integer.parseInt(anio);
            if (year < 1900 || year > 2100) {
                txtResultado.setText("Error: Año fuera de rango (1900-2100).");
                return;
            }
        } catch (NumberFormatException e) {
            txtResultado.setText("Error: El año debe ser un número.");
            return;
        }

        String params = "vin=" + encode(vin) +
                "&anio=" + encode(anio) +
                "&color=" + encode(color) +
                "&estado=" + encode(estado) +
                "&id_modelo=" + encode(idModelo) +
                "&id_tipo_vehiculo=" + encode(idTipo) +
                "&id_seccion=" + encode(idSeccion) +
                "&id_importacion=" + encode(idImportacion);

        int servidor = (v.getId() == R.id.btnLocal) ? 0 : 1;
        String url = Urls.build(servidor, Urls.VEHICULOS_INSERT, params);

        new Thread(() -> {
            String json = ControladorServicio.get(url, this);
            boolean exito = ControladorServicio.fueExitoso(json, this);
            runOnUiThread(() -> {
                if (exito) {
                    txtResultado.setText("Vehículo registrado correctamente.");
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                } else {
                    txtResultado.setText("Error: " + ControladorServicio.obtenerMensaje(json));
                }
            });
        }).start();
    }

    private void limpiarCampos() {
        editVin.setText("");
        editAnio.setText("");
        editColor.setText("");
        spinnerEstado.setText("Seleccionar", false);
        editIdModelo.setText("");
        editIdTipo.setText("");
        editIdSeccion.setText("");
        editIdImportacion.setText("");
    }

    private void prepararDropdown(MaterialAutoCompleteTextView dropdown) {
        dropdown.setTextSize(16);
        dropdown.setSingleLine(true);
        dropdown.setPadding(dropdown.getPaddingLeft(), 0, dropdown.getPaddingRight(), 0);
        dropdown.setOnClickListener(view -> dropdown.showDropDown());
        dropdown.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) dropdown.showDropDown();
        });
    }

    private String encode(String valor) {
        try {
            return URLEncoder.encode(valor, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return valor;
        }
    }

    private List<String> estadosBase() {
        List<String> estados = new ArrayList<>();
        for (String estado : getResources().getStringArray(R.array.estados_registro)) {
            estados.add(estado);
        }
        return estados;
    }

    private void configurarListaEstados(List<String> estados) {
        ArrayAdapter<String> adapter = new NoFilterArrayAdapter(estados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);
        spinnerEstado.setText(estados.get(0), false);
    }

    private void cargarEstadosDesdeBd() {
        new Thread(() -> {
            Set<String> estados = new LinkedHashSet<>(estadosBase());
            cargarEstadosDesdeServidor(0, estados);
            cargarEstadosDesdeServidor(1, estados);
            runOnUiThread(() -> configurarListaEstados(new ArrayList<>(estados)));
        }).start();
    }

    private void cargarEstadosDesdeServidor(int servidor, Set<String> estados) {
        String url = Urls.build(servidor, Urls.VEHICULOS_LISTA);
        String json = ControladorServicio.get(url, this);
        List<JSONObject> datos = ControladorServicio.parsearArray(json, this);

        for (JSONObject o : datos) {
            String estado = o.optString("ESTADO_VEHICULO", "").trim();
            if (!estado.isEmpty()) {
                estados.add(estado);
            }
        }
    }

    private class NoFilterArrayAdapter extends ArrayAdapter<String> {
        private final List<String> estados;

        NoFilterArrayAdapter(List<String> estados) {
            super(InsertarVehiculoActivity.this, android.R.layout.simple_dropdown_item_1line, estados);
            this.estados = estados;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    results.values = estados;
                    results.count = estados.size();
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    notifyDataSetChanged();
                }
            };
        }
    }
}
