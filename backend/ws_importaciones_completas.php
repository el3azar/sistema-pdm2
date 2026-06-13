<?php
// Servicio 12 - GET importaciones de un importador con vehículos y desperfectos
// Uso: ws_importaciones_completas.php?id_importador=1
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

$idImportador = isset($_GET['id_importador']) ? (int)$_GET['id_importador'] : 0;

if ($idImportador <= 0) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'id_importador requerido']);
    exit();
}

// Importador
$stmt = $con->prepare(
    "SELECT NOMBRE_IMPORTADOR, APELLIDO_IMPORTADOR, NUI
     FROM IMPORTADOR WHERE ID_IMPORTADOR = ?"
);
$stmt->bind_param("i", $idImportador);
$stmt->execute();
$importadorRow = $stmt->get_result()->fetch_assoc();
$stmt->close();

if (!$importadorRow) {
    http_response_code(404);
    echo json_encode(['success' => false, 'message' => 'Importador no encontrado']);
    exit();
}

$datosImportador = [
    'nombre'   => $importadorRow['NOMBRE_IMPORTADOR'],
    'apellido' => $importadorRow['APELLIDO_IMPORTADOR'],
    'nui'      => $importadorRow['NUI']
];

// Importaciones del importador
$stmt2 = $con->prepare(
    "SELECT ID_IMPORTACION, FECHA_IMPORTACION
     FROM IMPORTACION
     WHERE ID_IMPORTADOR = ?
     ORDER BY FECHA_IMPORTACION DESC"
);
$stmt2->bind_param("i", $idImportador);
$stmt2->execute();
$res2            = $stmt2->get_result();
$importacionIds  = [];
$importacionesMap = [];
while ($row = $res2->fetch_assoc()) {
    $iid = (int)$row['ID_IMPORTACION'];
    $importacionIds[]     = $iid;
    $importacionesMap[$iid] = [
        'id'         => $iid,
        'fecha'      => $row['FECHA_IMPORTACION'],
        'importador' => $datosImportador,
        'vehiculos'  => []
    ];
}
$stmt2->close();

if (empty($importacionIds)) {
    echo json_encode(['success' => true, 'data' => ['importaciones' => []]]);
    $con->close();
    exit();
}

// Vehículos de esas importaciones
$placeholders = implode(',', array_fill(0, count($importacionIds), '?'));
$types        = str_repeat('i', count($importacionIds));

$stmt3 = $con->prepare(
    "SELECT v.ID_VEHICULO, v.ID_IMPORTACION, v.VIN, v.ANIO,
            v.COLOR_VEHICULO, v.ESTADO_VEHICULO,
            mo.NOMBRE_MODELO, ma.NOMBRE_MARCA
     FROM VEHICULO v
     LEFT JOIN MODELO mo ON v.ID_MODELO = mo.ID_MODELO
     LEFT JOIN MARCA  ma ON mo.ID_MARCA  = ma.ID_MARCA
     WHERE v.ID_IMPORTACION IN ($placeholders)
     ORDER BY v.ID_VEHICULO"
);
$stmt3->bind_param($types, ...$importacionIds);
$stmt3->execute();
$res3       = $stmt3->get_result();
$vehiculosMap = [];
while ($v = $res3->fetch_assoc()) {
    $vid = (int)$v['ID_VEHICULO'];
    $vehiculosMap[$vid] = [
        '_importacionId' => (int)$v['ID_IMPORTACION'],
        'id'             => $vid,
        'marca'          => $v['NOMBRE_MARCA'],
        'modelo'         => $v['NOMBRE_MODELO'],
        'anio'           => (int)$v['ANIO'],
        'vin'            => $v['VIN'],
        'color'          => $v['COLOR_VEHICULO'],
        'estado'         => $v['ESTADO_VEHICULO'],
        'desperfectos'   => []
    ];
}
$stmt3->close();

// Desperfectos de esos vehículos
if (!empty($vehiculosMap)) {
    $vehiculoIds = array_keys($vehiculosMap);
    $plVeh       = implode(',', array_fill(0, count($vehiculoIds), '?'));
    $typesVeh    = str_repeat('i', count($vehiculoIds));

    $stmt4 = $con->prepare(
        "SELECT dd.ID_DETALLE_DESPERFECTO, dd.ID_VEHICULO,
                dd.DESCRIPCION_DETALLE, dd.FECHA_REGISTRO,
                td.NOMBRE_TIPO_DESPERFECTO
         FROM DETALLE_DESPERFECTO dd
         LEFT JOIN TIPO_DESPERFECTO td ON dd.ID_TIPO_DESPERFECTO = td.ID_TIPO_DESPERFECTO
         WHERE dd.ID_VEHICULO IN ($plVeh)
         ORDER BY dd.ID_DETALLE_DESPERFECTO"
    );
    $stmt4->bind_param($typesVeh, ...$vehiculoIds);
    $stmt4->execute();
    $res4 = $stmt4->get_result();
    while ($d = $res4->fetch_assoc()) {
        $vid = (int)$d['ID_VEHICULO'];
        if (isset($vehiculosMap[$vid])) {
            $vehiculosMap[$vid]['desperfectos'][] = [
                'id'          => (int)$d['ID_DETALLE_DESPERFECTO'],
                'descripcion' => $d['DESCRIPCION_DETALLE'],
                'tipo'        => $d['NOMBRE_TIPO_DESPERFECTO'],
                'fecha'       => $d['FECHA_REGISTRO'],
                'estado'      => 'Pendiente'
            ];
        }
    }
    $stmt4->close();
}

// Ensamblar vehículos (con desperfectos ya cargados) en sus importaciones
foreach ($vehiculosMap as $veh) {
    $iid = $veh['_importacionId'];
    unset($veh['_importacionId']);
    if (isset($importacionesMap[$iid])) {
        $importacionesMap[$iid]['vehiculos'][] = $veh;
    }
}

// Construir respuesta con totales
$importacionesList = [];
foreach ($importacionesMap as $imp) {
    $totalDesperfectos = 0;
    foreach ($imp['vehiculos'] as $veh) {
        $totalDesperfectos += count($veh['desperfectos']);
    }
    $imp['totales'] = [
        'cantidadVehiculos'    => count($imp['vehiculos']),
        'cantidadDesperfectos' => $totalDesperfectos
    ];
    $importacionesList[] = $imp;
}

echo json_encode([
    'success' => true,
    'data'    => ['importaciones' => $importacionesList]
]);

$con->close();
