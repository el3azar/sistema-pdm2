<?php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

$id_reparacion = isset($_GET['id_reparacion']) ? intval($_GET['id_reparacion']) : 0;

if ($id_reparacion <= 0) {
    echo json_encode(['resultado' => 0, 'mensaje' => 'Parametro id_reparacion requerido']);
    exit();
}

$apto_para_venta        = isset($_GET['apto_para_venta'])          ? intval($_GET['apto_para_venta'])          : null;
$requiere_otra_reparacion = isset($_GET['requiere_otra_reparacion']) ? intval($_GET['requiere_otra_reparacion']) : null;
$fecha_fin               = isset($_GET['fecha_fin'])                ? trim($_GET['fecha_fin'])                  : null;

$campos   = [];
$tipos    = '';
$valores  = [];

if ($apto_para_venta !== null) {
    $campos[] = 'APTO_PARA_VENTA = ?';
    $tipos    .= 'i';
    $valores[] = $apto_para_venta;
}

if ($requiere_otra_reparacion !== null) {
    $campos[] = 'REQUIERE_OTRA_REPARACION = ?';
    $tipos    .= 'i';
    $valores[] = $requiere_otra_reparacion;
}

if ($fecha_fin !== null && $fecha_fin !== '') {
    $campos[] = 'FECHA_FIN = ?';
    $tipos    .= 's';
    $valores[] = $fecha_fin;
}

if (empty($campos)) {
    echo json_encode(['resultado' => 0, 'mensaje' => 'No hay campos para actualizar']);
    exit();
}

$tipos   .= 'i';
$valores[] = $id_reparacion;

$sql  = "UPDATE REPARACION SET " . implode(', ', $campos) . " WHERE ID_REPARACION = ?";
$stmt = $con->prepare($sql);
$stmt->bind_param($tipos, ...$valores);

if ($stmt->execute()) {
    echo json_encode(['resultado' => 1, 'mensaje' => 'Reparacion actualizada']);
} else {
    echo json_encode(['resultado' => 0, 'mensaje' => 'Error: ' . $stmt->error]);
}

$stmt->close();
$con->close();
