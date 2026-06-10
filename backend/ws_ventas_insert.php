<?php
// Servicio 8 - GET/POST insertar venta
// Uso: ws_ventas_insert.php?id_importador=1&id_vehiculo=1&fecha_venta=2026-06-09&precio=15000.00
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

$id_importador = isset($_GET['id_importador']) ? intval($_GET['id_importador']) : null;
$id_vehiculo   = isset($_GET['id_vehiculo'])   ? intval($_GET['id_vehiculo'])   : null;
$fecha_venta   = isset($_GET['fecha_venta'])   ? trim($_GET['fecha_venta'])     : '';
$precio        = isset($_GET['precio'])        ? floatval($_GET['precio'])      : 0;

if (!$id_vehiculo || $fecha_venta === '' || $precio <= 0) {
    echo json_encode(['resultado' => 0, 'mensaje' => 'Faltan parametros requeridos']);
    exit();
}

$sql  = "INSERT INTO VENTA (ID_IMPORTADOR, ID_VEHICULO, FECHA_VENTA, PRECIO)
         VALUES (?, ?, ?, ?)";

$stmt = $con->prepare($sql);
$stmt->bind_param("iisd", $id_importador, $id_vehiculo, $fecha_venta, $precio);

if ($stmt->execute()) {
    echo json_encode(['resultado' => 1, 'mensaje' => 'Venta registrada', 'id' => $con->insert_id]);
} else {
    echo json_encode(['resultado' => 0, 'mensaje' => 'Error: ' . $stmt->error]);
}

$stmt->close();
$con->close();
