<?php
/**
 * Servicio: ws_desperfectos_lista.php
 * Integrante: Yami
 * Descripción: Lista los desperfectos de un vehículo específico.
 */
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

if (!isset($_GET['id_vehiculo'])) {
    echo json_encode(["error" => "Falta parametro id_vehiculo"]);
    exit;
}

$id_vehiculo = $_GET['id_vehiculo'];

$sql = "SELECT
            ID_DETALLE_DESPERFECTO AS ID_DETALLE,
            DESCRIPCION_DETALLE AS DESCRIPCION_DESPERFECTO,
            ID_VEHICULO
        FROM DETALLE_DESPERFECTO
        WHERE ID_VEHICULO = ?";

$stmt = $con->prepare($sql);
$stmt->bind_param("i", $id_vehiculo);
$stmt->execute();
$resultado = $stmt->get_result();

$lista = [];
while ($fila = $resultado->fetch_assoc()) {
    $lista[] = $fila;
}

echo json_encode($lista);
$stmt->close();
$con->close();
