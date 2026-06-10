# Referencia de Servicios Web
**PDM115 — Grupo 02 | Etapa 2**

Base URL local:  `http://TU_IP/inventario_gpo02/`
Base URL externa: `https://tu-app.onrender.com/inventario_gpo02/`

---

## Servicios disponibles

### 1. Listar marcas
| Campo | Valor |
|---|---|
| Archivo | `ws_marcas_lista.php` |
| Método | GET |
| Parámetros | Ninguno |
| Ejemplo | `http://localhost/inventario_gpo02/ws_marcas_lista.php` |

Respuesta:
```json
[
  { "ID_MARCA": "1", "NOMBRE_MARCA": "Toyota" },
  { "ID_MARCA": "2", "NOMBRE_MARCA": "Honda" }
]
```

---

### 2. Listar vehículos
| Campo | Valor |
|---|---|
| Archivo | `ws_vehiculos_lista.php` |
| Método | GET |
| Parámetros | Ninguno |
| Ejemplo | `http://localhost/inventario_gpo02/ws_vehiculos_lista.php` |

Respuesta: array con `ID_VEHICULO, VIN, ANIO, COLOR_VEHICULO, ESTADO_VEHICULO, NOMBRE_MODELO, NOMBRE_MARCA, DESCRIPCION_TIPO_VEHICULO, NIVEL_SECCION, NOMBRE_BODEGA`

---

### 3. Buscar vehículo por VIN
| Campo | Valor |
|---|---|
| Archivo | `ws_vehiculos_buscar.php` |
| Método | GET |
| Parámetros | `vin` (obligatorio) |
| Ejemplo | `ws_vehiculos_buscar.php?vin=JT2S3FEJ3M1234567` |

Respuesta: mismo formato que listar vehículos pero filtrado por VIN.

---

### 4. Registrar vehículo
| Campo | Valor |
|---|---|
| Archivo | `ws_vehiculos_insert.php` |
| Método | GET |
| Parámetros | `vin, anio, color, estado, id_modelo, id_tipo_vehiculo, id_seccion, id_importacion` |
| Ejemplo | `ws_vehiculos_insert.php?vin=ABC123&anio=2024&color=Rojo&estado=en bodega&id_modelo=1&id_tipo_vehiculo=1&id_seccion=1&id_importacion=1` |

Respuesta:
```json
{ "resultado": 1, "mensaje": "Vehiculo registrado", "id": 11 }
{ "resultado": 0, "mensaje": "Error: ..." }
```

---

### 5. Listar reparaciones por vehículo
| Campo | Valor |
|---|---|
| Archivo | `ws_reparaciones_lista.php` |
| Método | GET |
| Parámetros | `id_vehiculo` (obligatorio) |
| Ejemplo | `ws_reparaciones_lista.php?id_vehiculo=1` |

Respuesta: array con `ID_REPARACION, FECHA_INICIO, FECHA_FIN, DESCRIPCION_TRABAJO, APTO_PARA_VENTA, REQUIERE_OTRA_REPARACION, NOMBRE_TALLER, AUTORIZADO, VIN`

---

### 6. Registrar reparación
| Campo | Valor |
|---|---|
| Archivo | `ws_reparaciones_insert.php` |
| Método | GET |
| Parámetros | `id_vehiculo` (obligatorio), `id_taller, fecha_inicio` (obligatorio), `descripcion` |
| Ejemplo | `ws_reparaciones_insert.php?id_vehiculo=1&id_taller=1&fecha_inicio=2026-06-09&descripcion=Cambio de frenos` |

Respuesta:
```json
{ "resultado": 1, "mensaje": "Reparacion registrada", "id": 6 }
```

---

### 7. Listar ventas
| Campo | Valor |
|---|---|
| Archivo | `ws_ventas_lista.php` |
| Método | GET |
| Parámetros | `fecha` (opcional — devuelve ventas desde esa fecha) |
| Ejemplo sin filtro | `ws_ventas_lista.php` |
| Ejemplo con filtro | `ws_ventas_lista.php?fecha=2024-01-01` |

Respuesta: array con `ID_VENTA, FECHA_VENTA, PRECIO, VIN, NOMBRE_MODELO, NOMBRE_MARCA, NOMBRE_IMPORTADOR, APELLIDO_IMPORTADOR`

---

### 8. Registrar venta
| Campo | Valor |
|---|---|
| Archivo | `ws_ventas_insert.php` |
| Método | GET |
| Parámetros | `id_vehiculo` (obligatorio), `id_importador, fecha_venta` (obligatorio), `precio` (obligatorio) |
| Ejemplo | `ws_ventas_insert.php?id_vehiculo=2&id_importador=1&fecha_venta=2026-06-09&precio=18500.00` |

Respuesta:
```json
{ "resultado": 1, "mensaje": "Venta registrada", "id": 4 }
```

---

### 9. Estadísticas
| Campo | Valor |
|---|---|
| Archivo | `ws_estadisticas.php` |
| Método | GET |
| Parámetros | Ninguno |
| Ejemplo | `ws_estadisticas.php` |

Respuesta:
```json
{
  "por_estado": [
    { "ESTADO_VEHICULO": "en bodega", "TOTAL": "10" }
  ],
  "por_tipo": [
    { "DESCRIPCION_TIPO_VEHICULO": "Sedán", "TOTAL": "3" },
    { "DESCRIPCION_TIPO_VEHICULO": "SUV",   "TOTAL": "3" }
  ]
}
```

---

### 10. Modelos por marca
| Campo | Valor |
|---|---|
| Archivo | `ws_modelos_por_marca.php` |
| Método | GET |
| Parámetros | `id_marca` (obligatorio) |
| Ejemplo | `ws_modelos_por_marca.php?id_marca=1` |

Respuesta:
```json
[
  { "ID_MODELO": "1", "NOMBRE_MODELO": "Corolla", "NOMBRE_MARCA": "Toyota" },
  { "ID_MODELO": "2", "NOMBRE_MODELO": "Camry",   "NOMBRE_MARCA": "Toyota" }
]
```

---

## Constantes en Urls.java

```java
Urls.MARCAS_LISTA          // ws_marcas_lista.php
Urls.VEHICULOS_LISTA       // ws_vehiculos_lista.php
Urls.VEHICULOS_BUSCAR      // ws_vehiculos_buscar.php
Urls.VEHICULOS_INSERT      // ws_vehiculos_insert.php
Urls.REPARACIONES_LISTA    // ws_reparaciones_lista.php
Urls.REPARACIONES_INSERT   // ws_reparaciones_insert.php
Urls.VENTAS_LISTA          // ws_ventas_lista.php
Urls.VENTAS_INSERT         // ws_ventas_insert.php
Urls.ESTADISTICAS          // ws_estadisticas.php
Urls.MODELOS_POR_MARCA     // ws_modelos_por_marca.php
```

Ejemplo de uso en una Activity:
```java
// Sin parámetros
String url = Urls.build(servidor, Urls.MARCAS_LISTA);

// Con parámetros
String url = Urls.build(servidor, Urls.VEHICULOS_BUSCAR, "vin=" + vin);
String url = Urls.build(servidor, Urls.VENTAS_LISTA, "fecha=" + fecha);
```
