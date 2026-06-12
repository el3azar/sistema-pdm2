<?php
// Servicio 12 - GET/POST registrar movimiento y actualizar estado del vehículo
// entrada -> ESTADO_VEHICULO = 'en bodega' | salida -> ESTADO_VEHICULO = 'en transito'
// Uso: ws_movimientos_insert.php?id_vehiculo=1&id_transporte=1&id_personal=1&id_bodega=1&tipo_movimiento=entrada&fecha=2026-06-11&motivo=Ingreso
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

$id_vehiculo   = isset($_GET['id_vehiculo'])     ? intval($_GET['id_vehiculo'])      : 0;
$id_transporte = isset($_GET['id_transporte'])   ? intval($_GET['id_transporte'])    : 0;
$id_personal   = isset($_GET['id_personal'])     ? intval($_GET['id_personal'])      : 0;
$id_bodega     = isset($_GET['id_bodega'])       ? intval($_GET['id_bodega'])        : 0;
$tipo          = isset($_GET['tipo_movimiento']) ? strtolower(trim($_GET['tipo_movimiento'])) : '';
$fecha         = isset($_GET['fecha'])           ? trim($_GET['fecha'])              : '';
$motivo        = isset($_GET['motivo'])          ? trim($_GET['motivo'])             : '';

if ($id_vehiculo === 0 || $id_transporte === 0 || $id_personal === 0 ||
    $id_bodega === 0 || $tipo === '' || $fecha === '' || $motivo === '') {
    echo json_encode(['resultado' => 0, 'mensaje' => 'Faltan parametros requeridos']);
    exit();
}

if ($tipo !== 'entrada' && $tipo !== 'salida') {
    echo json_encode(['resultado' => 0, 'mensaje' => 'tipo_movimiento debe ser entrada o salida']);
    exit();
}

// Validar que el vehículo exista antes de insertar
$stmt = $con->prepare("SELECT ID_VEHICULO FROM VEHICULO WHERE ID_VEHICULO = ?");
$stmt->bind_param("i", $id_vehiculo);
$stmt->execute();
if ($stmt->get_result()->fetch_assoc() === null) {
    echo json_encode(['resultado' => 0, 'mensaje' => 'El vehiculo no existe']);
    $stmt->close();
    $con->close();
    exit();
}
$stmt->close();

$sql  = "INSERT INTO MOVIMIENTO (ID_TRANSPORTE, ID_PERSONAL, ID_VEHICULO, ID_BODEGA, TIPO_MOVIMIENTO, FECHA_MOVIMIENTO, MOTIVO)
         VALUES (?, ?, ?, ?, ?, ?, ?)";

$stmt = $con->prepare($sql);
$stmt->bind_param("iiiisss", $id_transporte, $id_personal, $id_vehiculo, $id_bodega, $tipo, $fecha, $motivo);

if ($stmt->execute()) {
    $id_movimiento = $con->insert_id;
    $stmt->close();

    // Actualizar el estado del vehículo según el tipo de movimiento
    $nuevo_estado = ($tipo === 'entrada') ? 'en bodega' : 'en transito';
    $stmt = $con->prepare("UPDATE VEHICULO SET ESTADO_VEHICULO = ? WHERE ID_VEHICULO = ?");
    $stmt->bind_param("si", $nuevo_estado, $id_vehiculo);
    $stmt->execute();

    echo json_encode([
        'resultado'    => 1,
        'mensaje'      => 'Movimiento registrado, vehiculo ahora: ' . $nuevo_estado,
        'id'           => $id_movimiento,
        'nuevo_estado' => $nuevo_estado
    ]);
} else {
    echo json_encode(['resultado' => 0, 'mensaje' => 'Error: ' . $stmt->error]);
}

$stmt->close();
$con->close();
