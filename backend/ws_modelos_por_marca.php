<?php
// Servicio 10 - GET modelos filtrados por marca
// Uso: ws_modelos_por_marca.php?id_marca=1
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

if (!isset($_GET['id_marca']) || intval($_GET['id_marca']) <= 0) {
    echo json_encode(['error' => 'Parametro id_marca requerido']);
    exit();
}

$id_marca = intval($_GET['id_marca']);

$sql  = "SELECT
            mo.ID_MODELO,
            mo.NOMBRE_MODELO,
            ma.NOMBRE_MARCA
         FROM MODELO mo
         LEFT JOIN MARCA ma ON mo.ID_MARCA = ma.ID_MARCA
         WHERE mo.ID_MARCA = ?
         ORDER BY mo.NOMBRE_MODELO";

$stmt = $con->prepare($sql);
$stmt->bind_param("i", $id_marca);
$stmt->execute();
$resultado = $stmt->get_result();

$lista = [];
while ($fila = $resultado->fetch_assoc()) {
    $lista[] = $fila;
}

echo json_encode($lista);
$stmt->close();
$con->close();
