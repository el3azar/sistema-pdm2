<?php
// Servicio - GET lista de bodegas
// Uso: ws_bodegas_lista.php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

$resultado = $con->query("SELECT ID_BODEGA, NOMBRE_BODEGA, DIRECCION_BODEGA FROM BODEGA ORDER BY NOMBRE_BODEGA");

$lista = [];
while ($fila = $resultado->fetch_assoc()) {
    $lista[] = $fila;
}

echo json_encode($lista);
$con->close();
