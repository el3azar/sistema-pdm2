# Estado del Proyecto — Etapa 2
**PDM115 — Grupo 02 | Sistema de Inventario de Vehículos**
Actualizado: 2026-06-09

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

### Android — Activities base (funcionales, pendientes de refinamiento visual)
- [x] `MainActivity` — menú principal
- [x] `MarcasActivity` — lista marcas
- [x] `VehiculosActivity` — lista vehículos
- [x] `BuscarVehiculoActivity` — buscar por VIN
- [x] `InsertarVehiculoActivity` — registrar vehículo
- [x] `ReparacionesActivity` — reparaciones por vehículo
- [x] `InsertarReparacionActivity` — registrar reparación
- [x] `VentasActivity` — lista ventas
- [x] `InsertarVentaActivity` — registrar venta
- [x] `EstadisticasActivity` — conteos por estado y tipo

---

## Pendiente

### Prioridad alta (antes de entrega)
- [ ] Refinamiento visual de cada Activity (layouts, colores, tipografía)
- [ ] Validaciones más específicas en formularios de inserción
- [ ] Probar todas las Activities contra servidor local
- [ ] Subir `backend/` a onrender.com y actualizar `Urls.java` con URL real
- [ ] Probar todas las Activities contra servidor externo

### Documento de entrega (Parte 2)
- [ ] Portada
- [ ] Índice
- [ ] Introducción y objetivos
- [ ] Capturas de pantalla de cada Activity (indicar autor)
- [ ] Código fuente PHP
- [ ] Código fuente Android
- [ ] Conclusiones

---

## Asignación de Activities por integrante

Completar esta tabla antes de empezar a refinar:

| Activity | Integrante responsable | Estado |
|---|---|---|
| `MarcasActivity` | | pendiente refinamiento |
| `VehiculosActivity` | | pendiente refinamiento |
| `BuscarVehiculoActivity` | | pendiente refinamiento |
| `InsertarVehiculoActivity` | | pendiente refinamiento |
| `ReparacionesActivity` | | pendiente refinamiento |
| `InsertarReparacionActivity` | | pendiente refinamiento |
| `VentasActivity` | | pendiente refinamiento |
| `InsertarVentaActivity` | | pendiente refinamiento |
| `EstadisticasActivity` | | pendiente refinamiento |

---

## Fecha de entrega
**Viernes 12 de junio de 2026 a las 11:55 PM**
Subir archivo comprimido (zip/rar) en el aula virtual.
