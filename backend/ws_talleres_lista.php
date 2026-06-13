<?php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

$resultado = $con->query("SELECT ID_TALLER, NOMBRE_TALLER, DIRECCION_TALLER, TELEFONO_TALLER, AUTORIZADO FROM TALLER ORDER BY NOMBRE_TALLER");

$lista = [];
while ($fila = $resultado->fetch_assoc()) {
    $lista[] = $fila;
}

echo json_encode($lista);
$con->close();
