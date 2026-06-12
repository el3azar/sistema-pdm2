package sv.edu.ues.fia.sistema_pdm2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import sv.edu.ues.fia.sistema_pdm2.activities.BodegasActivity;
import sv.edu.ues.fia.sistema_pdm2.activities.BuscarVehiculoActivity;
import sv.edu.ues.fia.sistema_pdm2.activities.EstadisticasActivity;
import sv.edu.ues.fia.sistema_pdm2.activities.HistorialMovimientosActivity;
import sv.edu.ues.fia.sistema_pdm2.activities.InsertarMovimientoActivity;
import sv.edu.ues.fia.sistema_pdm2.activities.ReporteTransportePersonalActivity;
import sv.edu.ues.fia.sistema_pdm2.activities.InsertarReparacionActivity;
import sv.edu.ues.fia.sistema_pdm2.activities.InsertarVehiculoActivity;
import sv.edu.ues.fia.sistema_pdm2.activities.InsertarVentaActivity;
import sv.edu.ues.fia.sistema_pdm2.activities.MarcasActivity;
import sv.edu.ues.fia.sistema_pdm2.activities.ReparacionesActivity;
import sv.edu.ues.fia.sistema_pdm2.activities.SeccionesActivity;
import sv.edu.ues.fia.sistema_pdm2.activities.SeccionesDisponiblesActivity;
import sv.edu.ues.fia.sistema_pdm2.activities.VehiculosActivity;
import sv.edu.ues.fia.sistema_pdm2.activities.VentasActivity;
import sv.edu.ues.fia.sistema_pdm2.activities.VentasPorImportadorActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void lanzar(View v) {
        Class<?> destino = null;
        int id = v.getId();

        if      (id == R.id.btnMarcas)            destino = MarcasActivity.class;
        else if (id == R.id.btnVehiculos)          destino = VehiculosActivity.class;
        else if (id == R.id.btnBuscarVehiculo)     destino = BuscarVehiculoActivity.class;
        else if (id == R.id.btnInsertarVehiculo)   destino = InsertarVehiculoActivity.class;
        else if (id == R.id.btnReparaciones)       destino = ReparacionesActivity.class;
        else if (id == R.id.btnInsertarReparacion) destino = InsertarReparacionActivity.class;
        else if (id == R.id.btnVentas)             destino = VentasActivity.class;
        else if (id == R.id.btnInsertarVenta)      destino = InsertarVentaActivity.class;
        else if (id == R.id.btnEstadisticas)       destino = EstadisticasActivity.class;
        else if (id == R.id.btnHistorialMovimientos)        destino = HistorialMovimientosActivity.class;
        else if (id == R.id.btnInsertarMovimiento)          destino = InsertarMovimientoActivity.class;
        else if (id == R.id.btnReporteTransportePersonal)   destino = ReporteTransportePersonalActivity.class;
        else if (id == R.id.btnBodegas)            destino = BodegasActivity.class;
        else if (id == R.id.btnSecciones)          destino = SeccionesActivity.class;
        else if (id == R.id.btnSeccionesDisponibles) destino = SeccionesDisponiblesActivity.class;
        else if (id == R.id.btnVentasPorImportador) destino = VentasPorImportadorActivity.class;

        if (destino != null) startActivity(new Intent(this, destino));
    }
}
