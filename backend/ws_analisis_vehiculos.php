<?php
// Servicio 13 - GET análisis global de vehículos: totales, tops, promedios
// Uso: ws_analisis_vehiculos.php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

// Total vehículos
$total = (int)$con->query("SELECT COUNT(*) AS total FROM VEHICULO")
                   ->fetch_assoc()['total'];

if ($total === 0) {
    echo json_encode([
        'success' => true,
        'data'    => ['estadisticas' => ['totalVehiculos' => 0]]
    ]);
    $con->close();
    exit();
}

// Total marcas distintas
$totalMarcas = (int)$con->query(
    "SELECT COUNT(DISTINCT ma.ID_MARCA) AS totalMarcas
     FROM VEHICULO v
     LEFT JOIN MODELO mo ON v.ID_MODELO = mo.ID_MODELO
     LEFT JOIN MARCA  ma ON mo.ID_MARCA  = ma.ID_MARCA"
)->fetch_assoc()['totalMarcas'];

// Total colores distintos
$totalColores = (int)$con->query(
    "SELECT COUNT(DISTINCT COLOR_VEHICULO) AS totalColores FROM VEHICULO"
)->fetch_assoc()['totalColores'];

// Top 10 marcas
$topMarcas = [];
$res = $con->query(
    "SELECT ma.NOMBRE_MARCA, COUNT(v.ID_VEHICULO) AS cantidad
     FROM VEHICULO v
     LEFT JOIN MODELO mo ON v.ID_MODELO = mo.ID_MODELO
     LEFT JOIN MARCA  ma ON mo.ID_MARCA  = ma.ID_MARCA
     WHERE ma.NOMBRE_MARCA IS NOT NULL
     GROUP BY ma.ID_MARCA, ma.NOMBRE_MARCA
     ORDER BY cantidad DESC
     LIMIT 10"
);
while ($row = $res->fetch_assoc()) {
    $topMarcas[] = [
        'marca'      => $row['NOMBRE_MARCA'],
        'cantidad'   => (int)$row['cantidad'],
        'porcentaje' => round(($row['cantidad'] / $total) * 100, 1)
    ];
}

// Top 5 colores
$topColores = [];
$res = $con->query(
    "SELECT COLOR_VEHICULO, COUNT(*) AS cantidad
     FROM VEHICULO
     GROUP BY COLOR_VEHICULO
     ORDER BY cantidad DESC
     LIMIT 5"
);
while ($row = $res->fetch_assoc()) {
    $topColores[] = [
        'color'      => $row['COLOR_VEHICULO'],
        'cantidad'   => (int)$row['cantidad'],
        'porcentaje' => round(($row['cantidad'] / $total) * 100, 1)
    ];
}

// Top 5 años
$topAnios = [];
$res = $con->query(
    "SELECT ANIO, COUNT(*) AS cantidad
     FROM VEHICULO
     GROUP BY ANIO
     ORDER BY cantidad DESC
     LIMIT 5"
);
while ($row = $res->fetch_assoc()) {
    $topAnios[] = [
        'anio'       => (int)$row['ANIO'],
        'cantidad'   => (int)$row['cantidad'],
        'porcentaje' => round(($row['cantidad'] / $total) * 100, 1)
    ];
}

// Edad promedio, año más antiguo y más nuevo
$anioStats = $con->query(
    "SELECT ROUND(AVG(YEAR(CURDATE()) - ANIO), 1) AS edadPromedio,
            MIN(ANIO) AS masAntiguo,
            MAX(ANIO) AS masNuevo
     FROM VEHICULO"
)->fetch_assoc();

echo json_encode([
    'success' => true,
    'data'    => [
        'estadisticas' => [
            'totalVehiculos'     => $total,
            'totalMarcas'        => $totalMarcas,
            'totalColores'       => $totalColores,
            'topMarcas'          => $topMarcas,
            'topColores'         => $topColores,
            'topAnios'           => $topAnios,
            'edadPromedio'       => (float)$anioStats['edadPromedio'],
            'vehiculoMasAntiguo' => (int)$anioStats['masAntiguo'],
            'vehiculoMasNuevo'   => (int)$anioStats['masNuevo']
        ]
    ]
]);

$con->close();
