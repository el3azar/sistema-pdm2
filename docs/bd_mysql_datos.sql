-- ============================================================
-- Script de datos iniciales MySQL - Sistema de Inventario de Vehículos
-- PDM115 - Grupo 02
-- Datos migrados desde LlenarBDGpo02.java
-- Ejecutar DESPUÉS de bd_mysql.sql
-- ============================================================

USE inventario_gpo02;

-- ============================================================
-- Usuarios y control de acceso
-- ============================================================

INSERT INTO USUARIO (ID_USUARIO, NOM_USUARIO, CLAVE) VALUES
('1', 'admin',      'adm01'),
('2', 'importador', 'imp01'),
('3', 'personal',   'per01');

INSERT INTO OPCIONCRUD (ID_OPCION, DES_OPCION, NUM_CRUD) VALUES
('100', 'Importador',       0),
('200', 'Vehículo',         0),
('210', 'Importación',      0),
('300', 'Bodega',           0),
('310', 'Sección',          0),
('400', 'Movimiento',       0),
('410', 'Transporte',       0),
('500', 'Reparación',       0),
('510', 'Taller',           0),
('600', 'Venta',            0),
('610', 'Desperfecto',      0),
('620', 'Personal Interno', 0),
('630', 'Marca',            0);

-- Admin: acceso total
INSERT INTO ACCESOUSUARIO (ID_OPCION, ID_USUARIO) VALUES
('100','1'),('200','1'),('210','1'),('300','1'),('310','1'),
('400','1'),('410','1'),('500','1'),('510','1'),('600','1'),
('610','1'),('620','1'),('630','1');

-- Importador: acceso parcial
INSERT INTO ACCESOUSUARIO (ID_OPCION, ID_USUARIO) VALUES
('100','2'),('200','2'),('210','2'),('610','2');

-- Personal: acceso parcial
INSERT INTO ACCESOUSUARIO (ID_OPCION, ID_USUARIO) VALUES
('300','3'),('310','3'),('400','3'),('410','3'),('620','3');

-- ============================================================
-- Geografía
-- ============================================================

INSERT INTO PAIS (ID_PAIS, NOMBRE_PAIS) VALUES
(1, 'El Salvador');

INSERT INTO DEPARTAMENTO (ID_DEPARTAMENTO, ID_PAIS, NOMBRE_DEPARTAMENTO) VALUES
(1,  1, 'Ahuachapán'),
(2,  1, 'Santa Ana'),
(3,  1, 'Sonsonate'),
(4,  1, 'La Libertad'),
(5,  1, 'San Salvador'),
(6,  1, 'Cuscatlán'),
(7,  1, 'La Paz'),
(8,  1, 'Cabañas'),
(9,  1, 'San Vicente'),
(10, 1, 'Usulután'),
(11, 1, 'Morazán'),
(12, 1, 'La Unión'),
(13, 1, 'Chalatenango'),
(14, 1, 'San Miguel');

INSERT INTO MUNICIPIO (ID_MUNICIPIO, ID_DEPARTAMENTO, NOMBRE_MUNICIPIO) VALUES
(1,  1,  'Ahuachapán'),
(2,  2,  'Santa Ana'),
(3,  3,  'Sonsonate'),
(4,  4,  'Santa Tecla'),
(5,  5,  'San Salvador'),
(6,  6,  'Cojutepeque'),
(7,  7,  'Zacatecoluca'),
(8,  8,  'Sensuntepeque'),
(9,  9,  'San Vicente'),
(10, 10, 'Usulután'),
(11, 11, 'San Francisco Gotera'),
(12, 12, 'La Unión'),
(13, 13, 'Nueva Concepción'),
(14, 14, 'San Miguel');

INSERT INTO DISTRITO (ID_DISTRITO, ID_MUNICIPIO, NOMBRE_DISTRITO) VALUES
(1,  1,  'Ahuachapán Centro'),
(2,  2,  'Santa Ana Centro'),
(3,  3,  'Sonsonate Centro'),
(4,  4,  'Santa Tecla Centro'),
(5,  5,  'San Salvador Centro'),
(6,  6,  'Cojutepeque Centro'),
(7,  7,  'Zacatecoluca Centro'),
(8,  8,  'Sensuntepeque Centro'),
(9,  9,  'San Vicente Centro'),
(10, 10, 'Usulután Centro'),
(11, 11, 'Gotera Centro'),
(12, 12, 'La Unión Centro'),
(13, 13, 'Nueva Concepción Centro'),
(14, 14, 'San Miguel Centro');

-- ============================================================
-- Catálogos
-- ============================================================

INSERT INTO MARCA (ID_MARCA, NOMBRE_MARCA) VALUES
(1, 'Toyota'),
(2, 'Honda'),
(3, 'Ford'),
(4, 'Chevrolet'),
(5, 'Nissan'),
(6, 'Hyundai'),
(7, 'KIA'),
(8, 'BMW'),
(9, 'Mercedes Benz');

INSERT INTO MODELO (ID_MODELO, ID_MARCA, NOMBRE_MODELO) VALUES
(1,  1, 'Corolla'),
(2,  1, 'Camry'),
(3,  1, 'Hilux'),
(4,  2, 'Civic'),
(5,  2, 'Accord'),
(6,  2, 'CR-V'),
(7,  3, 'Fiesta'),
(8,  3, 'Fusion'),
(9,  4, 'Spark'),
(10, 4, 'Silverado'),
(11, 5, 'Sentra'),
(12, 5, 'Frontier'),
(13, 6, 'Tucson'),
(14, 6, 'Santa Fe'),
(15, 7, 'Sportage'),
(16, 7, 'Sorento'),
(17, 8, 'Serie 3'),
(18, 9, 'Clase C');

INSERT INTO TIPO_VEHICULO (ID_TIPO_VEHICULO, DESCRIPCION_TIPO_VEHICULO) VALUES
(1, 'Sedán'),
(2, 'SUV'),
(3, 'Pickup'),
(4, 'Minivan'),
(5, 'Hatchback');

INSERT INTO TIPO_TRANSPORTE (ID_TIPO_TRANSPORTE, DESCRIPCION_TIPO_TRANSPORTE, CAPACIDAD_MAX_VEHICULOS) VALUES
(1, 'Camión Plataforma', 8),
(2, 'Camión Cerrado',    6),
(3, 'Remolque',         10);

INSERT INTO TIPO_DESPERFECTO (ID_TIPO_DESPERFECTO, NOMBRE_TIPO_DESPERFECTO, DESCRIPCION_TIPO_DESPERFECTO) VALUES
(1, 'Rayón',        'Rayones en carrocería'),
(2, 'Golpe',        'Deformación en chapa'),
(3, 'Cristal roto', 'Ventana o parabrisas roto'),
(4, 'Mecánico',     'Defecto en motor o sistema');

INSERT INTO TALLER (ID_TALLER, NOMBRE_TALLER, DIRECCION_TALLER, TELEFONO_TALLER, AUTORIZADO) VALUES
(1, 'Taller Autorizado A', 'Av. Principal 123', '2234-5678', 1),
(2, 'Taller B',            'Calle 5 Oriente',   '2234-5679', 0),
(3, 'Taller C',            'Zona Industrial',   '2234-5680', 1);

INSERT INTO PERSONAL_INTERNO (ID_PERSONAL, NOMBRE_PERSONAL, APELLIDO_PERSONAL, CARGO) VALUES
(1, 'Juan',   'Pérez',    'Empleado'),
(2, 'María',  'González', 'Supervisor'),
(3, 'Carlos', 'López',    'Empleado');

-- ============================================================
-- Bodegas y secciones
-- ============================================================

INSERT INTO BODEGA (ID_BODEGA, ID_DISTRITO, NOMBRE_BODEGA, DIRECCION_BODEGA) VALUES
(1,  5,  'Bodega Central',     'Bulevar Los Héroes Km 2, San Salvador'),
(2,  2,  'Bodega Occidente',   'Carretera a Santa Ana Km 32'),
(3,  14, 'Bodega Oriente',     'Carretera Panamericana Km 138, San Miguel'),
(4,  7,  'Bodega Sur',         'Carretera al Puerto Km 56, Zacatecoluca'),
(5,  8,  'Bodega Paracentral', 'Carretera a Sensuntepeque Km 88'),
(6,  3,  'Bodega Costera',     'Carretera Litoral Km 72, Sonsonate');

INSERT INTO SECCION (ID_SECCION, ID_BODEGA, NIVEL, CAPACIDAD_MAXIMA, CAPACIDAD_ACTUAL) VALUES
(1,  1, 1, 50, 0), (2,  1, 2, 50, 0), (3,  1, 3, 50, 0),
(4,  2, 1, 50, 0), (5,  2, 2, 50, 0), (6,  2, 3, 50, 0),
(7,  3, 1, 50, 0), (8,  3, 2, 50, 0), (9,  3, 3, 50, 0),
(10, 4, 1, 50, 0), (11, 4, 2, 50, 0), (12, 4, 3, 50, 0),
(13, 5, 1, 50, 0), (14, 5, 2, 50, 0), (15, 5, 3, 50, 0),
(16, 6, 1, 50, 0), (17, 6, 2, 50, 0), (18, 6, 3, 50, 0);

-- ============================================================
-- Importadores y teléfonos
-- ============================================================

INSERT INTO IMPORTADOR (ID_IMPORTADOR, ID_DISTRITO, NOMBRE_IMPORTADOR, APELLIDO_IMPORTADOR, APELLIDO_CASADA, GENERO, DIRECCION_IMPORTADOR, FECHA_NACIMIENTO, CORREO_ELECTRONICO, NUI, NOMBRE_RESPONSABLE) VALUES
(1,  5,  'Carlos',  'Mendoza',   NULL, 'M', 'Colonia Escalón, San Salvador',              '1985-03-15', 'cmendoza@gmail.com',    '01234567890001', 'Luis Ramírez'),
(2,  2,  'María',   'González',  NULL, 'F', 'Residencial Santa Elena, Antiguo Cuscatlán', '1990-07-22', 'mgonzalez@hotmail.com', '02345678901234', 'Ana Flores'),
(3,  14, 'Roberto', 'Hernández', NULL, 'M', 'Colonia San Benito, San Salvador',           '1978-11-30', 'rhernandez@yahoo.com',  '03456789012345', 'Pedro Castillo');

INSERT INTO TELEFONO_IMPORTADOR (ID_TELEFONO, ID_IMPORTADOR, NUMERO, TIPO) VALUES
(1, 1, '7890-1234', 'Celular'),
(2, 1, '2222-5678', 'Casa'),
(3, 2, '7654-3210', 'Celular'),
(4, 2, '2345-6789', 'Oficina'),
(5, 3, '7777-8888', 'Celular'),
(6, 3, '2290-5555', 'Casa');

-- ============================================================
-- Transporte e importaciones
-- ============================================================

INSERT INTO TRANSPORTE (ID_TRANSPORTE, ID_TIPO_TRANSPORTE, PLACA, DESCRIPCION_TRANSPORTE) VALUES
(1, 1, 'P-123-456', 'Camión de traslado principal'),
(2, 2, 'P-789-012', 'Camión cerrado de carga'),
(3, 3, 'P-345-678', 'Remolque de gran capacidad');

INSERT INTO IMPORTACION (ID_IMPORTACION, ID_IMPORTADOR, FECHA_IMPORTACION) VALUES
(1, 1, '2024-01-15'),
(2, 1, '2024-02-20'),
(3, 2, '2024-03-10'),
(4, 3, '2024-04-05');

-- ============================================================
-- Vehículos
-- ============================================================

INSERT INTO VEHICULO (ID_VEHICULO, ID_TIPO_VEHICULO, ID_SECCION, ID_MODELO, ID_IMPORTACION, VIN, ANIO, COLOR_VEHICULO, ESTADO_VEHICULO) VALUES
(1,  1, 1,  1,  1,  'JT2S3FEJ3M1234567', 2022, 'Blanco',   'en bodega'),
(2,  1, 4,  4,  2,  '1HGCR2F39KA123456', 2023, 'Negro',    'en bodega'),
(3,  2, 12, 12, 4,  '1N4AL3AP4LC123456', 2021, 'Gris',     'en bodega'),
(4,  3, 13, 13, 2,  'KM8J33A45MU123456', 2023, 'Azul',     'en bodega'),
(5,  4, 17, 17, 4,  'WBA3B5C58MF123456', 2024, 'Plateado', 'en bodega'),
(6,  1, 9,  9,  3,  '1G1BC5SM5J7123456', 2023, 'Rojo',     'en bodega'),
(7,  2, 2,  2,  1,  '4T1BF1FK5GU123456', 2022, 'Blanco',   'en bodega'),
(8,  3, 6,  6,  2,  '5J6RM4H53EL123456', 2024, 'Negro',    'en bodega'),
(9,  4, 8,  8,  1,  '3FA6P0HD5GR123456', 2023, 'Plateado', 'en bodega'),
(10, 2, 15, 15, 3,  'KNDPC3AC5F7123456', 2022, 'Azul',     'en bodega');

-- ============================================================
-- Movimientos
-- ============================================================

INSERT INTO MOVIMIENTO (ID_MOVIMIENTO, ID_TRANSPORTE, ID_PERSONAL, ID_VEHICULO, ID_BODEGA, TIPO_MOVIMIENTO, FECHA_MOVIMIENTO, MOTIVO) VALUES
(1,  1, 1, 1,  1, 'entrada', '2024-01-15', 'Ingreso a bodega central'),
(2,  1, 1, 2,  1, 'entrada', '2024-01-16', 'Ingreso a bodega central'),
(3,  2, 2, 3,  2, 'entrada', '2024-02-20', 'Ingreso a bodega occidente'),
(4,  3, 3, 4,  3, 'entrada', '2024-03-10', 'Ingreso a bodega oriente'),
(5,  1, 3, 5,  4, 'entrada', '2024-04-05', 'Ingreso a bodega sur'),
(6,  2, 1, 6,  1, 'entrada', '2024-01-17', 'Ingreso a bodega central'),
(7,  3, 3, 7,  2, 'entrada', '2024-02-21', 'Ingreso a bodega occidente'),
(8,  2, 1, 8,  2, 'entrada', '2024-03-11', 'Ingreso a bodega occidente'),
(9,  1, 2, 9,  3, 'entrada', '2024-04-06', 'Ingreso a bodega oriente'),
(10, 3, 3, 10, 4, 'entrada', '2024-05-02', 'Ingreso a bodega sur');

-- ============================================================
-- Desperfectos y fotos
-- ============================================================

INSERT INTO DETALLE_DESPERFECTO (ID_DETALLE_DESPERFECTO, ID_VEHICULO, ID_TIPO_DESPERFECTO, DESCRIPCION_DETALLE, FECHA_REGISTRO) VALUES
(1, 1, 2, 'Golpe leve en puerta delantera derecha', '2024-01-15'),
(2, 2, 1, 'Rayón superficial en capó',              '2024-01-16'),
(3, 3, 3, 'Vidrio trasero con fisura',              '2024-02-20'),
(4, 4, 4, 'Falla leve en sistema de frenos',        '2024-03-10'),
(5, 9, 4, 'Falla en sistema de suspensión',         '2024-04-06');

INSERT INTO FOTO_DESPERFECTO (ID_FOTO_DESPERFECTO, ID_DETALLE_DESPERFECTO, RUTA_IMAGEN, FECHA_TOMA) VALUES
(1, 1, '/fotos/v1/golpe_puerta.jpg',   '2024-01-15'),
(2, 2, '/fotos/v2/rayon_capo.jpg',     '2024-01-16'),
(3, 3, '/fotos/v3/vidrio_trasero.jpg', '2024-02-20'),
(4, 4, '/fotos/v4/frenos.jpg',         '2024-03-10'),
(5, 5, '/fotos/v9/suspension.jpg',     '2024-04-06');

-- ============================================================
-- Reparaciones
-- ============================================================

INSERT INTO REPARACION (ID_REPARACION, ID_TALLER, ID_VEHICULO, FECHA_INICIO, FECHA_FIN, DESCRIPCION_TRABAJO, APTO_PARA_VENTA, REQUIERE_OTRA_REPARACION) VALUES
(1, 1, 1, '2024-01-20', '2024-01-25', 'Reparación de golpe en puerta',   0, 0),
(2, 3, 2, '2024-01-22', '2024-02-01', 'Pulido y pintura por rayones',    0, 0),
(3, 3, 3, '2024-02-25', '2024-03-05', 'Reemplazo de vidrio trasero',     0, 0),
(4, 1, 7, '2024-03-01', '2024-03-10', 'Revisión general previa a venta', 1, 0),
(5, 3, 9, '2024-04-15', '2024-04-25', 'Revisión mecánica completa',      0, 0);

-- ============================================================
-- Ventas
-- ============================================================

INSERT INTO VENTA (ID_VENTA, ID_IMPORTADOR, ID_VEHICULO, FECHA_VENTA, PRECIO) VALUES
(1, 1, 4,  '2024-03-15', 22000.00),
(2, 2, 5,  '2024-04-10', 45000.00),
(3, 3, 10, '2024-05-05', 28000.00);

-- ============================================================
-- Ajustar AUTO_INCREMENT para que nuevos registros
-- no colisionen con los datos insertados manualmente
-- ============================================================

ALTER TABLE PAIS                 AUTO_INCREMENT = 2;
ALTER TABLE DEPARTAMENTO         AUTO_INCREMENT = 15;
ALTER TABLE MUNICIPIO            AUTO_INCREMENT = 15;
ALTER TABLE DISTRITO             AUTO_INCREMENT = 15;
ALTER TABLE MARCA                AUTO_INCREMENT = 10;
ALTER TABLE MODELO               AUTO_INCREMENT = 19;
ALTER TABLE TIPO_VEHICULO        AUTO_INCREMENT = 6;
ALTER TABLE TIPO_TRANSPORTE      AUTO_INCREMENT = 4;
ALTER TABLE TIPO_DESPERFECTO     AUTO_INCREMENT = 5;
ALTER TABLE TALLER               AUTO_INCREMENT = 4;
ALTER TABLE PERSONAL_INTERNO     AUTO_INCREMENT = 4;
ALTER TABLE BODEGA               AUTO_INCREMENT = 7;
ALTER TABLE SECCION              AUTO_INCREMENT = 19;
ALTER TABLE IMPORTADOR           AUTO_INCREMENT = 4;
ALTER TABLE TELEFONO_IMPORTADOR  AUTO_INCREMENT = 7;
ALTER TABLE TRANSPORTE           AUTO_INCREMENT = 4;
ALTER TABLE IMPORTACION          AUTO_INCREMENT = 5;
ALTER TABLE VEHICULO             AUTO_INCREMENT = 11;
ALTER TABLE MOVIMIENTO           AUTO_INCREMENT = 11;
ALTER TABLE DETALLE_DESPERFECTO  AUTO_INCREMENT = 6;
ALTER TABLE FOTO_DESPERFECTO     AUTO_INCREMENT = 6;
ALTER TABLE REPARACION           AUTO_INCREMENT = 6;
ALTER TABLE VENTA                AUTO_INCREMENT = 4;
