# Estado del Proyecto — Etapa 2
**PDM115 — Grupo 02 | Sistema de Inventario de Vehículos**
Actualizado: 2026-06-10

---

## Completado

### Base de datos MySQL
- [x] Script de creación de tablas (`bd_mysql.sql`)
- [x] Script de datos iniciales (`bd_mysql_datos.sql`)
- [x] Llaves foráneas con `ON DELETE RESTRICT ON UPDATE CASCADE`

### Backend PHP (10 servicios)
- [x] `conexion.php` — configuración de conexión
- [x] `ws_marcas_lista.php`
- [x] `ws_vehiculos_lista.php`
- [x] `ws_vehiculos_buscar.php`
- [x] `ws_vehiculos_insert.php`
- [x] `ws_reparaciones_lista.php`
- [x] `ws_reparaciones_insert.php`
- [x] `ws_ventas_lista.php`
- [x] `ws_ventas_insert.php`
- [x] `ws_estadisticas.php`
- [x] `ws_modelos_por_marca.php`

### Android — Infraestructura
- [x] `Urls.java` — URLs centralizadas (solo cambiar IP aquí)
- [x] `ControladorServicio.java` — HTTP GET, POST y parseo JSON
- [x] `AndroidManifest.xml` — permisos de internet configurados

### Android — UI base (diseño aplicado, pendiente refinamiento por integrante)
- [x] `MainActivity` — menú principal con paleta navy blue
- [x] `MarcasActivity` — lista marcas
- [x] `VehiculosActivity` — lista vehículos
- [x] `BuscarVehiculoActivity` — buscar por VIN
- [x] `InsertarVehiculoActivity` — registrar vehículo
- [x] `ReparacionesActivity` — reparaciones por vehículo
- [x] `InsertarReparacionActivity` — registrar reparación
- [x] `VentasActivity` — lista ventas
- [x] `InsertarVentaActivity` — registrar venta
- [x] `EstadisticasActivity` — conteos por estado y tipo

### Servidor externo (InfinityFree)
- [x] BD `if0_41996611_inventario_gpo02` creada y con datos
- [x] PHP subidos a `https://serviciosguia8.page.gd/inventario_gpo02/`
- [x] Endpoints responden correctamente en navegador
- [ ] **Pendiente:** conexión desde emulador Android cuelga (problema de red del emulador con InfinityFree) — funciona en dispositivo físico

---

## Pendiente

### Prioridad alta (antes de entrega)
- [ ] Refinamiento visual de cada Activity (cada integrante refina las suyas)
- [ ] Implementar servicios adicionales propios (ver `SERVICIOS_REFERENCIA.md`)
- [ ] Validaciones en formularios de inserción
- [ ] Probar todas las Activities contra servidor local
- [ ] Probar servidor externo desde dispositivo físico

### Documento de entrega (Parte 2)
- [ ] Portada
- [ ] Índice
- [ ] Introducción y objetivos
- [ ] Capturas de pantalla de cada Activity (indicar autor)
- [ ] Código fuente PHP
- [ ] Código fuente Android
- [ ] Conclusiones

---

## Asignación de Activities y servicios adicionales por integrante

### Activities base (refinar UI + probar contra local y externo)

| Activity | Integrante | Tablas relacionadas |
|---|---|---|
| `MarcasActivity` | Eleazar | base del sistema |
| `EstadisticasActivity` | Eleazar | VEHICULO, VENTA |
| `VehiculosActivity` | Yami | VEHICULO |
| `BuscarVehiculoActivity` | Yami | VEHICULO |
| `InsertarVehiculoActivity` | Yami | VEHICULO |
| `ReparacionesActivity` | Ricardo | REPARACION, TALLER |
| `InsertarReparacionActivity` | Ricardo | REPARACION, TALLER |
| `VentasActivity` | Javier | VENTA, IMPORTADOR |
| `InsertarVentaActivity` | Javier | VENTA, IMPORTADOR |

### Servicios adicionales a implementar (PHP + Activity nueva)

| Integrante | Servicios PHP a crear | Tablas |
|---|---|---|
| **Gaby** | `ws_importadores_lista`, `ws_importaciones_lista`, `ws_importaciones_por_importador`, `ws_telefonos_importador` | IMPORTADOR, IMPORTACION, TELEFONO_IMPORTADOR |
| **Yami** | `ws_vehiculos_por_estado`, `ws_desperfectos_lista`, `ws_desperfectos_insert` | VEHICULO, DETALLE_DESPERFECTO |
| **Eleazar** | `ws_transportes_lista`, `ws_personal_lista`, `ws_movimientos_lista`, `ws_movimientos_insert` | TRANSPORTE, PERSONAL_INTERNO, MOVIMIENTO |
| **Ricardo** | `ws_talleres_lista`, `ws_talleres_autorizados`, `ws_reparaciones_update`, `ws_reparaciones_por_taller` | TALLER, REPARACION |
| **Javier** | `ws_bodegas_lista`, `ws_secciones_lista`, `ws_secciones_disponibles`, `ws_ventas_por_importador` | BODEGA, SECCION, VENTA |

> Ver `SERVICIOS_REFERENCIA.md` para el detalle de cada servicio (parámetros, respuesta esperada, ejemplo de URL).

---

## Fecha de entrega
**Viernes 12 de junio de 2026 a las 11:55 PM**
Subir archivo comprimido (zip/rar) en el aula virtual.
