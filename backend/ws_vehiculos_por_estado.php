<?php
/**
 * Servicio: ws_vehiculos_por_estado.php
 * Integrante: Yami
 * Descripción: Lista vehículos filtrados por su estado.
 */
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

if (!isset($_GET['estado'])) {
    echo json_encode(["error" => "Falta parametro estado"]);
    exit;
}

$estado = $_GET['estado'];

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
        WHERE v.ESTADO_VEHICULO = ?
        ORDER BY v.ID_VEHICULO DESC";

$stmt = $con->prepare($sql);
$stmt->bind_param("s", $estado);
$stmt->execute();
$resultado = $stmt->get_result();

$lista = [];
while ($fila = $resultado->fetch_assoc()) {
    $lista[] = $fila;
}

echo json_encode($lista);
$stmt->close();
$con->close();
