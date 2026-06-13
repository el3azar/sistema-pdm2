<?php
// Servicio 11 - GET importador completo con distrito, teléfonos e importaciones
// Uso: ws_importador_completo.php?id=1
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

$id = isset($_GET['id']) ? (int)$_GET['id'] : 0;

if ($id <= 0) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'ID de importador requerido']);
    exit();
}

// Importador + Distrito + Municipio + Departamento
$sql = "SELECT
            i.ID_IMPORTADOR, i.NOMBRE_IMPORTADOR, i.APELLIDO_IMPORTADOR,
            i.APELLIDO_CASADA, i.NUI, i.GENERO, i.FECHA_NACIMIENTO,
            i.CORREO_ELECTRONICO, i.DIRECCION_IMPORTADOR, i.NOMBRE_RESPONSABLE,
            d.ID_DISTRITO, d.NOMBRE_DISTRITO,
            m.NOMBRE_MUNICIPIO,
            dep.NOMBRE_DEPARTAMENTO
        FROM IMPORTADOR i
        LEFT JOIN DISTRITO     d   ON i.ID_DISTRITO       = d.ID_DISTRITO
        LEFT JOIN MUNICIPIO    m   ON d.ID_MUNICIPIO      = m.ID_MUNICIPIO
        LEFT JOIN DEPARTAMENTO dep ON m.ID_DEPARTAMENTO   = dep.ID_DEPARTAMENTO
        WHERE i.ID_IMPORTADOR = ?";

$stmt = $con->prepare($sql);
$stmt->bind_param("i", $id);
$stmt->execute();
$row = $stmt->get_result()->fetch_assoc();
$stmt->close();

if (!$row) {
    http_response_code(404);
    echo json_encode(['success' => false, 'message' => 'Importador no encontrado']);
    exit();
}

$importador = [
    'id'                 => (int)$row['ID_IMPORTADOR'],
    'nombreImportador'   => $row['NOMBRE_IMPORTADOR'],
    'apellidoImportador' => $row['APELLIDO_IMPORTADOR'],
    'apellidoCasada'     => $row['APELLIDO_CASADA'],
    'nui'                => $row['NUI'],
    'genero'             => $row['GENERO'],
    'fechaNacimiento'    => $row['FECHA_NACIMIENTO'],
    'correoElectronico'  => $row['CORREO_ELECTRONICO'],
    'direccion'          => $row['DIRECCION_IMPORTADOR'],
    'nombreResponsable'  => $row['NOMBRE_RESPONSABLE']
];

$distrito = [
    'id'           => $row['ID_DISTRITO'] ? (int)$row['ID_DISTRITO'] : null,
    'nombre'       => $row['NOMBRE_DISTRITO'],
    'municipio'    => $row['NOMBRE_MUNICIPIO'],
    'departamento' => $row['NOMBRE_DEPARTAMENTO']
];

// Teléfonos
$stmt2 = $con->prepare(
    "SELECT ID_TELEFONO, NUMERO, TIPO
     FROM TELEFONO_IMPORTADOR
     WHERE ID_IMPORTADOR = ?
     ORDER BY ID_TELEFONO"
);
$stmt2->bind_param("i", $id);
$stmt2->execute();
$res2     = $stmt2->get_result();
$telefonos = [];
while ($t = $res2->fetch_assoc()) {
    $telefonos[] = [
        'id'     => (int)$t['ID_TELEFONO'],
        'numero' => $t['NUMERO'],
        'tipo'   => $t['TIPO']
    ];
}
$stmt2->close();

// Importaciones con cantidad de vehículos
$stmt3 = $con->prepare(
    "SELECT im.ID_IMPORTACION, im.FECHA_IMPORTACION,
            COUNT(v.ID_VEHICULO) AS CANTIDAD_VEHICULOS
     FROM IMPORTACION im
     LEFT JOIN VEHICULO v ON v.ID_IMPORTACION = im.ID_IMPORTACION
     WHERE im.ID_IMPORTADOR = ?
     GROUP BY im.ID_IMPORTACION, im.FECHA_IMPORTACION
     ORDER BY im.FECHA_IMPORTACION DESC"
);
$stmt3->bind_param("i", $id);
$stmt3->execute();
$res3          = $stmt3->get_result();
$importaciones = [];
while ($imp = $res3->fetch_assoc()) {
    $importaciones[] = [
        'id'               => (int)$imp['ID_IMPORTACION'],
        'fecha'            => $imp['FECHA_IMPORTACION'],
        'cantidadVehiculos'=> (int)$imp['CANTIDAD_VEHICULOS']
    ];
}
$stmt3->close();

echo json_encode([
    'success' => true,
    'data'    => [
        'importador'   => $importador,
        'distrito'     => $distrito,
        'telefonos'    => $telefonos,
        'importaciones'=> $importaciones
    ]
]);

$con->close();
