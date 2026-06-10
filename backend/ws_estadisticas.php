<?php
// Servicio 9 - GET estadísticas de vehículos por estado y por tipo
// Uso: ws_estadisticas.php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

$por_estado = [];
$resultado  = $con->query(
    "SELECT ESTADO_VEHICULO, COUNT(*) AS TOTAL
     FROM VEHICULO
     GROUP BY ESTADO_VEHICULO
     ORDER BY TOTAL DESC"
);
while ($fila = $resultado->fetch_assoc()) {
    $por_estado[] = $fila;
}

$por_tipo  = [];
$resultado = $con->query(
    "SELECT tv.DESCRIPCION_TIPO_VEHICULO, COUNT(v.ID_VEHICULO) AS TOTAL
     FROM TIPO_VEHICULO tv
     LEFT JOIN VEHICULO v ON v.ID_TIPO_VEHICULO = tv.ID_TIPO_VEHICULO
     GROUP BY tv.ID_TIPO_VEHICULO
     ORDER BY TOTAL DESC"
);
while ($fila = $resultado->fetch_assoc()) {
    $por_tipo[] = $fila;
}

echo json_encode([
    'por_estado' => $por_estado,
    'por_tipo'   => $por_tipo
]);

$con->close();
