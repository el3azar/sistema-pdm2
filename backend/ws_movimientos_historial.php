<?php
// Servicio 11 - GET historial de movimientos de un vehículo (kardex de MOVIMIENTO)
// Uso: ws_movimientos_historial.php?vin=XXXXX
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

if (!isset($_GET['vin']) || trim($_GET['vin']) === '') {
    echo json_encode(['error' => 'Parametro vin requerido']);
    exit();
}

$vin = trim($_GET['vin']);

// ── Ficha del vehículo ──────────────────────────────────────
$sql = "SELECT
            v.ID_VEHICULO,
            v.VIN,
            v.ANIO,
            v.COLOR_VEHICULO,
            v.ESTADO_VEHICULO,
            mo.NOMBRE_MODELO,
            ma.NOMBRE_MARCA
        FROM VEHICULO v
        LEFT JOIN MODELO mo ON v.ID_MODELO = mo.ID_MODELO
        LEFT JOIN MARCA  ma ON mo.ID_MARCA  = ma.ID_MARCA
        WHERE v.VIN = ?";

$stmt = $con->prepare($sql);
$stmt->bind_param("s", $vin);
$stmt->execute();
$vehiculo = $stmt->get_result()->fetch_assoc();
$stmt->close();

if ($vehiculo === null) {
    echo json_encode(['error' => 'Vehiculo no encontrado']);
    $con->close();
    exit();
}

// ── Movimientos del vehículo ────────────────────────────────
$sql = "SELECT
            m.ID_MOVIMIENTO,
            m.TIPO_MOVIMIENTO,
            m.FECHA_MOVIMIENTO,
            m.MOTIVO,
            b.NOMBRE_BODEGA,
            t.PLACA,
            tt.DESCRIPCION_TIPO_TRANSPORTE,
            CONCAT(p.NOMBRE_PERSONAL, ' ', p.APELLIDO_PERSONAL) AS PERSONAL,
            p.CARGO
        FROM MOVIMIENTO m
        LEFT JOIN BODEGA          b  ON m.ID_BODEGA          = b.ID_BODEGA
        LEFT JOIN TRANSPORTE      t  ON m.ID_TRANSPORTE      = t.ID_TRANSPORTE
        LEFT JOIN TIPO_TRANSPORTE tt ON t.ID_TIPO_TRANSPORTE = tt.ID_TIPO_TRANSPORTE
        LEFT JOIN PERSONAL_INTERNO p ON m.ID_PERSONAL        = p.ID_PERSONAL
        WHERE m.ID_VEHICULO = ?
        ORDER BY m.FECHA_MOVIMIENTO DESC, m.ID_MOVIMIENTO DESC";

$stmt = $con->prepare($sql);
$stmt->bind_param("i", $vehiculo['ID_VEHICULO']);
$stmt->execute();
$resultado = $stmt->get_result();

$movimientos = [];
while ($fila = $resultado->fetch_assoc()) {
    $movimientos[] = $fila;
}

echo json_encode([
    'vehiculo'    => $vehiculo,
    'movimientos' => $movimientos
]);

$stmt->close();
$con->close();
