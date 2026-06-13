<?php
// Servicio - GET ventas filtradas por importador
// Uso: ws_ventas_por_importador.php?id_importador=1
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

if (!isset($_GET['id_importador']) || intval($_GET['id_importador']) <= 0) {
    echo json_encode(['error' => 'Parametro id_importador requerido']);
    exit();
}

$id_importador = intval($_GET['id_importador']);

$sql = "SELECT
            vt.ID_VENTA,
            vt.FECHA_VENTA,
            vt.PRECIO,
            v.VIN,
            v.COLOR_VEHICULO,
            v.ESTADO_VEHICULO,
            mo.NOMBRE_MODELO,
            ma.NOMBRE_MARCA,
            i.NOMBRE_IMPORTADOR,
            i.APELLIDO_IMPORTADOR
        FROM VENTA vt
        LEFT JOIN VEHICULO   v  ON vt.ID_VEHICULO   = v.ID_VEHICULO
        LEFT JOIN MODELO     mo ON v.ID_MODELO       = mo.ID_MODELO
        LEFT JOIN MARCA      ma ON mo.ID_MARCA       = ma.ID_MARCA
        LEFT JOIN IMPORTADOR i  ON vt.ID_IMPORTADOR  = i.ID_IMPORTADOR
        WHERE vt.ID_IMPORTADOR = ?
        ORDER BY vt.FECHA_VENTA DESC";

$stmt = $con->prepare($sql);
$stmt->bind_param("i", $id_importador);
$stmt->execute();
$resultado = $stmt->get_result();

$lista = [];
while ($fila = $resultado->fetch_assoc()) {
    $lista[] = $fila;
}

echo json_encode($lista);
$stmt->close();
$con->close();
