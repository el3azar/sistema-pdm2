<?php
// Servicio 13 - GET reporte de TRANSPORTE y PERSONAL_INTERNO: movimientos por bodega,
// actividad por personal y uso de transportes
// Uso: ws_reporte_transporte_personal.php
//      ws_reporte_transporte_personal.php?fecha_inicio=2024-01-01&fecha_fin=2024-12-31
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

// Filtro opcional por rango de fechas (FECHA_MOVIMIENTO en formato YYYY-MM-DD)
$fecha_inicio = isset($_GET['fecha_inicio']) ? trim($_GET['fecha_inicio']) : '';
$fecha_fin    = isset($_GET['fecha_fin'])    ? trim($_GET['fecha_fin'])    : '';

$filtro = '';
if ($fecha_inicio !== '' && $fecha_fin !== '') {
    $filtro = " AND m.FECHA_MOVIMIENTO BETWEEN '" . $con->real_escape_string($fecha_inicio) . "'
                                           AND '" . $con->real_escape_string($fecha_fin) . "'";
} elseif ($fecha_inicio !== '') {
    $filtro = " AND m.FECHA_MOVIMIENTO >= '" . $con->real_escape_string($fecha_inicio) . "'";
} elseif ($fecha_fin !== '') {
    $filtro = " AND m.FECHA_MOVIMIENTO <= '" . $con->real_escape_string($fecha_fin) . "'";
}

// ── Entradas y salidas por bodega ───────────────────────────
$por_bodega = [];
$resultado  = $con->query(
    "SELECT b.NOMBRE_BODEGA,
            SUM(m.TIPO_MOVIMIENTO = 'entrada') AS ENTRADAS,
            SUM(m.TIPO_MOVIMIENTO = 'salida')  AS SALIDAS,
            COUNT(*)                           AS TOTAL
     FROM MOVIMIENTO m
     INNER JOIN BODEGA b ON m.ID_BODEGA = b.ID_BODEGA
     WHERE 1=1 $filtro
     GROUP BY b.ID_BODEGA
     ORDER BY TOTAL DESC"
);
while ($fila = $resultado->fetch_assoc()) {
    $por_bodega[] = $fila;
}

// ── Actividad por personal interno ──────────────────────────
$por_personal = [];
$resultado    = $con->query(
    "SELECT CONCAT(p.NOMBRE_PERSONAL, ' ', p.APELLIDO_PERSONAL) AS PERSONAL,
            p.CARGO,
            COUNT(m.ID_MOVIMIENTO) AS TOTAL
     FROM PERSONAL_INTERNO p
     LEFT JOIN MOVIMIENTO m ON m.ID_PERSONAL = p.ID_PERSONAL $filtro
     GROUP BY p.ID_PERSONAL
     ORDER BY TOTAL DESC"
);
while ($fila = $resultado->fetch_assoc()) {
    $por_personal[] = $fila;
}

// ── Uso de transportes vs capacidad máxima ──────────────────
$por_transporte = [];
$resultado      = $con->query(
    "SELECT t.PLACA,
            tt.DESCRIPCION_TIPO_TRANSPORTE,
            tt.CAPACIDAD_MAX_VEHICULOS,
            COUNT(m.ID_MOVIMIENTO) AS VIAJES
     FROM TRANSPORTE t
     LEFT JOIN TIPO_TRANSPORTE tt ON t.ID_TIPO_TRANSPORTE = tt.ID_TIPO_TRANSPORTE
     LEFT JOIN MOVIMIENTO m ON m.ID_TRANSPORTE = t.ID_TRANSPORTE $filtro
     GROUP BY t.ID_TRANSPORTE
     ORDER BY VIAJES DESC"
);
while ($fila = $resultado->fetch_assoc()) {
    $por_transporte[] = $fila;
}

echo json_encode([
    'por_bodega'     => $por_bodega,
    'por_personal'   => $por_personal,
    'por_transporte' => $por_transporte
]);

$con->close();
