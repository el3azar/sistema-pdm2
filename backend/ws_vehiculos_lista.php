<?php
// Servicio 2 - GET lista de vehículos con JOINs
// Uso: ws_vehiculos_lista.php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

$sql = "SELECT
            v.ID_VEHICULO,
            v.VIN,
            v.ANIO,
            v.COLOR_VEHICULO,
            v.ESTADO_VEHICULO,
            mo.NOMBRE_MODELO,
            ma.NOMBRE_MARCA,
            tv.DESCRIPCION_TIPO_VEHICULO,
            s.NIVEL AS NIVEL_SECCION,
            b.NOMBRE_BODEGA
        FROM VEHICULO v
        LEFT JOIN MODELO        mo ON v.ID_MODELO        = mo.ID_MODELO
        LEFT JOIN MARCA         ma ON mo.ID_MARCA         = ma.ID_MARCA
        LEFT JOIN TIPO_VEHICULO tv ON v.ID_TIPO_VEHICULO  = tv.ID_TIPO_VEHICULO
        LEFT JOIN SECCION       s  ON v.ID_SECCION        = s.ID_SECCION
        LEFT JOIN BODEGA        b  ON s.ID_BODEGA         = b.ID_BODEGA
        ORDER BY v.ID_VEHICULO DESC";

$resultado = $con->query($sql);

$lista = [];
while ($fila = $resultado->fetch_assoc()) {
    $lista[] = $fila;
}

echo json_encode($lista);
$con->close();
