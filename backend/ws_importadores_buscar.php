<?php
// Servicio 14 - GET importadores que coincidan con un nombre o apellido
// Uso: ws_importadores_buscar.php?nombre=Carlos
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

$nombre = isset($_GET['nombre']) ? trim($_GET['nombre']) : '';

if ($nombre === '') {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'Parametro nombre requerido']);
    exit();
}

$busqueda = '%' . $nombre . '%';

$stmt = $con->prepare(
    "SELECT ID_IMPORTADOR, NOMBRE_IMPORTADOR, APELLIDO_IMPORTADOR,
            CORREO_ELECTRONICO, NUI
     FROM IMPORTADOR
     WHERE NOMBRE_IMPORTADOR LIKE ?
        OR APELLIDO_IMPORTADOR LIKE ?
     ORDER BY NOMBRE_IMPORTADOR, APELLIDO_IMPORTADOR"
);
$stmt->bind_param("ss", $busqueda, $busqueda);
$stmt->execute();
$resultado = $stmt->get_result();

$lista = [];
while ($fila = $resultado->fetch_assoc()) {
    $lista[] = [
        'id'                 => (int)$fila['ID_IMPORTADOR'],
        'nombreImportador'   => $fila['NOMBRE_IMPORTADOR'],
        'apellidoImportador' => $fila['APELLIDO_IMPORTADOR'],
        'correoElectronico'  => $fila['CORREO_ELECTRONICO'],
        'nui'                => $fila['NUI']
    ];
}
$stmt->close();

echo json_encode([
    'success' => true,
    'total'   => count($lista),
    'data'    => $lista
]);

$con->close();
