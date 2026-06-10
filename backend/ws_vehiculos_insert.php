<?php
// Servicio 4 - GET/POST insertar vehículo
// Uso: ws_vehiculos_insert.php?id_tipo_vehiculo=1&id_seccion=1&id_modelo=1&id_importacion=1&vin=XXX&anio=2024&color=Rojo&estado=Disponible
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

$id_tipo_vehiculo = isset($_GET['id_tipo_vehiculo']) ? intval($_GET['id_tipo_vehiculo']) : null;
$id_seccion       = isset($_GET['id_seccion'])       ? intval($_GET['id_seccion'])       : null;
$id_modelo        = isset($_GET['id_modelo'])        ? intval($_GET['id_modelo'])        : null;
$id_importacion   = isset($_GET['id_importacion'])   ? intval($_GET['id_importacion'])   : null;
$vin              = isset($_GET['vin'])              ? trim($_GET['vin'])               : '';
$anio             = isset($_GET['anio'])             ? intval($_GET['anio'])            : 0;
$color            = isset($_GET['color'])            ? trim($_GET['color'])             : '';
$estado           = isset($_GET['estado'])           ? trim($_GET['estado'])            : '';

if ($vin === '' || $anio === 0 || $color === '' || $estado === '') {
    echo json_encode(['resultado' => 0, 'mensaje' => 'Faltan parametros requeridos']);
    exit();
}

$sql  = "INSERT INTO VEHICULO (ID_TIPO_VEHICULO, ID_SECCION, ID_MODELO, ID_IMPORTACION, VIN, ANIO, COLOR_VEHICULO, ESTADO_VEHICULO)
         VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

$stmt = $con->prepare($sql);
$stmt->bind_param("iiiiisis", $id_tipo_vehiculo, $id_seccion, $id_modelo, $id_importacion, $vin, $anio, $color, $estado);

if ($stmt->execute()) {
    echo json_encode(['resultado' => 1, 'mensaje' => 'Vehiculo registrado', 'id' => $con->insert_id]);
} else {
    echo json_encode(['resultado' => 0, 'mensaje' => 'Error: ' . $stmt->error]);
}

$stmt->close();
$con->close();
