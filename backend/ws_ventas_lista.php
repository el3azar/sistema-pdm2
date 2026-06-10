<?php
// Servicio 7 - GET lista de ventas, opcionalmente filtradas por fecha
// Uso: ws_ventas_lista.php
//      ws_ventas_lista.php?fecha=2026-01-01
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

$fecha = isset($_GET['fecha']) ? trim($_GET['fecha']) : '';

if ($fecha !== '') {
    $sql  = "SELECT
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
             WHERE vt.FECHA_VENTA >= ?
             ORDER BY vt.FECHA_VENTA DESC";
    $stmt = $con->prepare($sql);
    $stmt->bind_param("s", $fecha);
    $stmt->execute();
    $resultado = $stmt->get_result();
    $stmt->close();
} else {
    $sql       = "SELECT
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
                  ORDER BY vt.FECHA_VENTA DESC";
    $resultado = $con->query($sql);
}

$lista = [];
while ($fila = $resultado->fetch_assoc()) {
    $lista[] = $fila;
}

echo json_encode($lista);
$con->close();
