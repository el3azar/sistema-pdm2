<?php
// Servicio 5 - GET lista de reparaciones de un vehículo
// Uso: ws_reparaciones_lista.php?id_vehiculo=1
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

if (!isset($_GET['id_vehiculo']) || intval($_GET['id_vehiculo']) <= 0) {
    echo json_encode(['error' => 'Parametro id_vehiculo requerido']);
    exit();
}

$id_vehiculo = intval($_GET['id_vehiculo']);

$sql = "SELECT
            r.ID_REPARACION,
            r.FECHA_INICIO,
            r.FECHA_FIN,
            r.DESCRIPCION_TRABAJO,
            r.APTO_PARA_VENTA,
            r.REQUIERE_OTRA_REPARACION,
            t.NOMBRE_TALLER,
            t.AUTORIZADO,
            v.VIN
        FROM REPARACION r
        LEFT JOIN TALLER   t ON r.ID_TALLER   = t.ID_TALLER
        LEFT JOIN VEHICULO v ON r.ID_VEHICULO  = v.ID_VEHICULO
        WHERE r.ID_VEHICULO = ?
        ORDER BY r.FECHA_INICIO DESC";

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
