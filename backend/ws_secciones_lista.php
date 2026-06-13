<?php
// Servicio - GET lista de secciones, opcionalmente filtradas por bodega
// Uso: ws_secciones_lista.php
//      ws_secciones_lista.php?id_bodega=1
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include 'conexion.php';

$id_bodega = isset($_GET['id_bodega']) ? intval($_GET['id_bodega']) : 0;

if ($id_bodega > 0) {
    $sql = "SELECT
                s.ID_SECCION,
                s.NIVEL,
                s.CAPACIDAD_MAXIMA,
                s.CAPACIDAD_ACTUAL,
                b.NOMBRE_BODEGA
            FROM SECCION s
            LEFT JOIN BODEGA b ON s.ID_BODEGA = b.ID_BODEGA
            WHERE s.ID_BODEGA = ?
            ORDER BY s.NIVEL";
    $stmt = $con->prepare($sql);
    $stmt->bind_param("i", $id_bodega);
    $stmt->execute();
    $resultado = $stmt->get_result();
    $stmt->close();
} else {
    $resultado = $con->query("SELECT
                                s.ID_SECCION,
                                s.NIVEL,
                                s.CAPACIDAD_MAXIMA,
                                s.CAPACIDAD_ACTUAL,
                                b.NOMBRE_BODEGA
                              FROM SECCION s
                              LEFT JOIN BODEGA b ON s.ID_BODEGA = b.ID_BODEGA
                              ORDER BY b.NOMBRE_BODEGA, s.NIVEL");
}

$lista = [];
while ($fila = $resultado->fetch_assoc()) {
    $lista[] = $fila;
}

echo json_encode($lista);
$con->close();
