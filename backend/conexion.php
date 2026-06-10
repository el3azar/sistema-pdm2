<?php
// ============================================================
// Configuración de conexión a MySQL
// Ajustar host, usuario y clave según el servidor que se use
// ============================================================

define('DB_HOST',   'localhost');
define('DB_USER',   'root');
define('DB_PASS',   '');
define('DB_NAME',   'inventario_gpo02');
define('DB_CHARSET','utf8mb4');

$con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);
$con->set_charset(DB_CHARSET);

if ($con->connect_error) {
    http_response_code(500);
    echo json_encode(['error' => 'Error de conexion: ' . $con->connect_error]);
    exit();
}