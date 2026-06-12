<?php
// Servicio de apoyo - GET catálogos para el formulario de movimiento
// Devuelve en una sola llamada: vehículos, transportes, personal y bodegas (para Spinners)
// Uso: ws_movimientos_catalogos.php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

$vehiculos = [];
$resultado = $con->query("SELECT ID_VEHICULO, VIN FROM VEHICULO ORDER BY ID_VEHICULO");
while ($fila = $resultado->fetch_assoc()) {
    $vehiculos[] = $fila;
}

$transportes = [];
$resultado   = $con->query(
    "SELECT t.ID_TRANSPORTE, t.PLACA, tt.DESCRIPCION_TIPO_TRANSPORTE
     FROM TRANSPORTE t
     LEFT JOIN TIPO_TRANSPORTE tt ON t.ID_TIPO_TRANSPORTE = tt.ID_TIPO_TRANSPORTE
     ORDER BY t.ID_TRANSPORTE"
);
while ($fila = $resultado->fetch_assoc()) {
    $transportes[] = $fila;
}

$personal  = [];
$resultado = $con->query(
    "SELECT ID_PERSONAL,
            CONCAT(NOMBRE_PERSONAL, ' ', APELLIDO_PERSONAL) AS PERSONAL,
            CARGO
     FROM PERSONAL_INTERNO
     ORDER BY ID_PERSONAL"
);
while ($fila = $resultado->fetch_assoc()) {
    $personal[] = $fila;
}

$bodegas   = [];
$resultado = $con->query("SELECT ID_BODEGA, NOMBRE_BODEGA FROM BODEGA ORDER BY ID_BODEGA");
while ($fila = $resultado->fetch_assoc()) {
    $bodegas[] = $fila;
}

echo json_encode([
    'vehiculos'   => $vehiculos,
    'transportes' => $transportes,
    'personal'    => $personal,
    'bodegas'     => $bodegas
]);

$con->close();
