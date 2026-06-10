<?php
// Servicio 6 - GET/POST insertar reparación
// Uso: ws_reparaciones_insert.php?id_taller=1&id_vehiculo=1&fecha_inicio=2026-06-01&descripcion=Cambio+de+frenos
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

$id_taller    = isset($_GET['id_taller'])    ? intval($_GET['id_taller'])        : null;
$id_vehiculo  = isset($_GET['id_vehiculo'])  ? intval($_GET['id_vehiculo'])      : null;
$fecha_inicio = isset($_GET['fecha_inicio']) ? trim($_GET['fecha_inicio'])       : '';
$descripcion  = isset($_GET['descripcion'])  ? trim($_GET['descripcion'])        : '';

if (!$id_vehiculo || $fecha_inicio === '') {
    echo json_encode(['resultado' => 0, 'mensaje' => 'Faltan parametros requeridos']);
    exit();
}

$sql = "INSERT INTO REPARACION (ID_TALLER, ID_VEHICULO, FECHA_INICIO, DESCRIPCION_TRABAJO, APTO_PARA_VENTA, REQUIERE_OTRA_REPARACION)
        VALUES (?, ?, ?, ?, 0, 0)";

$stmt = $con->prepare($sql);
$stmt->bind_param("iiss", $id_taller, $id_vehiculo, $fecha_inicio, $descripcion);

if ($stmt->execute()) {
    echo json_encode(['resultado' => 1, 'mensaje' => 'Reparacion registrada', 'id' => $con->insert_id]);
} else {
    echo json_encode(['resultado' => 0, 'mensaje' => 'Error: ' . $stmt->error]);
}

$stmt->close();
$con->close();
