<?php
/**
 * Servicio: ws_desperfectos_insert.php
 * Integrante: Yami
 * Descripción: Registra un nuevo desperfecto para un vehículo.
 */
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

if (!isset($_GET['id_vehiculo']) || !isset($_GET['descripcion'])) {
    echo json_encode(["resultado" => 0, "mensaje" => "Faltan parametros"]);
    exit;
}

$id_vehiculo = $_GET['id_vehiculo'];
$descripcion = $_GET['descripcion'];
$fecha_registro = date("Y-m-d");
// Se asume un ID_TIPO_DESPERFECTO por defecto (ej: 1 para Rayón o similar)
// si no se proporciona en la referencia de la guía.
$id_tipo = 1;

$sql = "INSERT INTO DETALLE_DESPERFECTO (ID_VEHICULO, ID_TIPO_DESPERFECTO, DESCRIPCION_DETALLE, FECHA_REGISTRO)
        VALUES (?, ?, ?, ?)";

$stmt = $con->prepare($sql);
$stmt->bind_param("iiss", $id_vehiculo, $id_tipo, $descripcion, $fecha_registro);

if ($stmt->execute()) {
    echo json_encode([
        "resultado" => 1,
        "mensaje" => "Desperfecto registrado",
        "id" => $stmt->insert_id
    ]);
} else {
    echo json_encode([
        "resultado" => 0,
        "mensaje" => "Error al registrar: " . $stmt->error
    ]);
}

$stmt->close();
$con->close();
