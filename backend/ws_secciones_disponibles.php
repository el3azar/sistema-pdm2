<?php
// Servicio - GET secciones con espacio disponible
// Uso: ws_secciones_disponibles.php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

$resultado = $con->query("SELECT
                            s.ID_SECCION,
                            s.NIVEL,
                            s.CAPACIDAD_MAXIMA,
                            s.CAPACIDAD_ACTUAL,
                            (s.CAPACIDAD_MAXIMA - s.CAPACIDAD_ACTUAL) AS DISPONIBLE,
                            b.NOMBRE_BODEGA
                          FROM SECCION s
                          LEFT JOIN BODEGA b ON s.ID_BODEGA = b.ID_BODEGA
                          WHERE s.CAPACIDAD_ACTUAL < s.CAPACIDAD_MAXIMA
                          ORDER BY b.NOMBRE_BODEGA, s.NIVEL");

$lista = [];
while ($fila = $resultado->fetch_assoc()) {
    $lista[] = $fila;
}

echo json_encode($lista);
$con->close();
