<?php
// Servicio 4 - GET/POST insertar vehículo
// Uso: ws_vehiculos_insert.php?id_tipo_vehiculo=1&id_seccion=1&id_modelo=1&id_importacion=1&vin=XXX&anio=2024&color=Rojo&estado=Disponible
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

$datos = $_REQUEST;

$id_tipo_vehiculo = isset($datos['id_tipo_vehiculo']) ? intval($datos['id_tipo_vehiculo']) : null;
$id_seccion       = isset($datos['id_seccion'])       ? intval($datos['id_seccion'])       : null;
$id_modelo        = isset($datos['id_modelo'])        ? intval($datos['id_modelo'])        : null;
$id_importacion   = isset($datos['id_importacion'])   ? intval($datos['id_importacion'])   : null;
$vin              = isset($datos['vin'])              ? trim($datos['vin'])               : '';
$anio             = isset($datos['anio'])             ? intval($datos['anio'])            : 0;
$color            = isset($datos['color'])            ? trim($datos['color'])             : '';
$estado           = isset($datos['estado'])           ? trim($datos['estado'])            : '';

if ($vin === '' || $anio === 0 || $color === '' || $estado === '') {
    echo json_encode(['resultado' => 0, 'mensaje' => 'Faltan parametros requeridos']);
    exit();
}

if (strlen($vin) !== 17) {
    echo json_encode(['resultado' => 0, 'mensaje' => 'El VIN debe tener exactamente 17 caracteres']);
    exit();
}

if (!preg_match('/^[\p{L}\s]+$/u', $color)) {
    echo json_encode(['resultado' => 0, 'mensaje' => 'El color solo debe contener letras']);
    exit();
}

$sql  = "INSERT INTO VEHICULO (ID_TIPO_VEHICULO, ID_SECCION, ID_MODELO, ID_IMPORTACION, VIN, ANIO, COLOR_VEHICULO, ESTADO_VEHICULO)
         VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

$stmt = $con->prepare($sql);
$stmt->bind_param("iiiisiss", $id_tipo_vehiculo, $id_seccion, $id_modelo, $id_importacion, $vin, $anio, $color, $estado);

if ($stmt->execute()) {
    echo json_encode(['resultado' => 1, 'mensaje' => 'Vehiculo registrado', 'id' => $con->insert_id]);
} else {
    echo json_encode(['resultado' => 0, 'mensaje' => 'Error: ' . $stmt->error]);
}

$stmt->close();
$con->close();
