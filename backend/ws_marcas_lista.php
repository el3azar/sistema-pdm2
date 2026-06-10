<?php
// Servicio 1 - GET lista de marcas
// Uso: ws_marcas_lista.php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

$resultado = $con->query("SELECT ID_MARCA, NOMBRE_MARCA FROM MARCA ORDER BY NOMBRE_MARCA");

$lista = [];
while ($fila = $resultado->fetch_assoc()) {
    $lista[] = $fila;
}

echo json_encode($lista);
$con->close();
