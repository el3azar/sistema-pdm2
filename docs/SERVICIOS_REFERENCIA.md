# Referencia de Servicios Web
**PDM115 — Grupo 02 | Etapa 2**

Base URL local:    `http://TU_IP/inventario_gpo02/`
Base URL externa:  `https://serviciosguia8.page.gd/inventario_gpo02/`

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

---

## Servicios adicionales recomendados por integrante

Extensiones naturales basadas en los módulos que cada integrante desarrolló en Etapa 1. No son obligatorios, pero cubren las tablas que cada quien ya conoce.

---

### Gaby — Importador, Importación, Teléfono Importador

#### ws_importadores_lista.php
| Campo | Valor |
|---|---|
| Archivo | `ws_importadores_lista.php` |
| Método | GET |
| Parámetros | Ninguno |
| Ejemplo | `ws_importadores_lista.php` |

Respuesta:
```json
[
  { "ID_IMPORTADOR": "1", "NOMBRE_IMPORTADOR": "Carlos", "APELLIDO_IMPORTADOR": "Pérez" }
]
```

#### ws_importaciones_lista.php
| Campo | Valor |
|---|---|
| Archivo | `ws_importaciones_lista.php` |
| Método | GET |
| Parámetros | Ninguno |
| Ejemplo | `ws_importaciones_lista.php` |

Respuesta:
```json
[
  { "ID_IMPORTACION": "1", "FECHA_IMPORTACION": "2026-01-15", "NOMBRE_IMPORTADOR": "Carlos", "APELLIDO_IMPORTADOR": "Pérez" }
]
```

#### ws_importaciones_por_importador.php
| Campo | Valor |
|---|---|
| Archivo | `ws_importaciones_por_importador.php` |
| Método | GET |
| Parámetros | `id_importador` (obligatorio) |
| Ejemplo | `ws_importaciones_por_importador.php?id_importador=1` |

Respuesta:
```json
[
  { "ID_IMPORTACION": "1", "FECHA_IMPORTACION": "2026-01-15" }
]
```

#### ws_telefonos_importador.php
| Campo | Valor |
|---|---|
| Archivo | `ws_telefonos_importador.php` |
| Método | GET |
| Parámetros | `id_importador` (obligatorio) |
| Ejemplo | `ws_telefonos_importador.php?id_importador=1` |

Respuesta:
```json
[
  { "ID_TELEFONO": "1", "NUMERO_TELEFONO": "7555-1234", "ID_IMPORTADOR": "1" }
]
```

---

### Yami — Vehículo, Detalle Desperfecto, Foto Desperfecto

#### ws_vehiculos_por_estado.php
| Campo | Valor |
|---|---|
| Archivo | `ws_vehiculos_por_estado.php` |
| Método | GET |
| Parámetros | `estado` (obligatorio) |
| Ejemplo | `ws_vehiculos_por_estado.php?estado=en bodega` |

Respuesta: mismo formato que `ws_vehiculos_lista.php` pero filtrado por estado.

#### ws_desperfectos_lista.php
| Campo | Valor |
|---|---|
| Archivo | `ws_desperfectos_lista.php` |
| Método | GET |
| Parámetros | `id_vehiculo` (obligatorio) |
| Ejemplo | `ws_desperfectos_lista.php?id_vehiculo=1` |

Respuesta:
```json
[
  { "ID_DETALLE": "1", "DESCRIPCION_DESPERFECTO": "Golpe en puerta", "ID_VEHICULO": "1" }
]
```

#### ws_desperfectos_insert.php
| Campo | Valor |
|---|---|
| Archivo | `ws_desperfectos_insert.php` |
| Método | GET |
| Parámetros | `id_vehiculo` (obligatorio), `descripcion` (obligatorio) |
| Ejemplo | `ws_desperfectos_insert.php?id_vehiculo=1&descripcion=Golpe en parachoque` |

Respuesta:
```json
{ "resultado": 1, "mensaje": "Desperfecto registrado", "id": 3 }
```

#### ws_fotos_desperfecto.php
| Campo | Valor |
|---|---|
| Archivo | `ws_fotos_desperfecto.php` |
| Método | GET |
| Parámetros | `id_detalle` (obligatorio) |
| Ejemplo | `ws_fotos_desperfecto.php?id_detalle=1` |

Respuesta:
```json
[
  { "ID_FOTO": "1", "RUTA_FOTO": "/fotos/desperfecto_1.jpg", "ID_DETALLE": "1" }
]
```

---

### Eleazar — Transporte, Personal Interno, Movimiento

#### ws_transportes_lista.php
| Campo | Valor |
|---|---|
| Archivo | `ws_transportes_lista.php` |
| Método | GET |
| Parámetros | Ninguno |
| Ejemplo | `ws_transportes_lista.php` |

Respuesta:
```json
[
  { "ID_TRANSPORTE": "1", "PLACA": "P-123-456", "CAPACIDAD": "5", "NOMBRE_EMPRESA": "Transportes SA" }
]
```

#### ws_personal_lista.php
| Campo | Valor |
|---|---|
| Archivo | `ws_personal_lista.php` |
| Método | GET |
| Parámetros | Ninguno |
| Ejemplo | `ws_personal_lista.php` |

Respuesta:
```json
[
  { "ID_PERSONAL": "1", "NOMBRE_PERSONAL": "Ana", "APELLIDO_PERSONAL": "García", "CARGO": "Bodeguero" }
]
```

#### ws_movimientos_lista.php
| Campo | Valor |
|---|---|
| Archivo | `ws_movimientos_lista.php` |
| Método | GET |
| Parámetros | `id_vehiculo` (opcional) |
| Ejemplo sin filtro | `ws_movimientos_lista.php` |
| Ejemplo con filtro | `ws_movimientos_lista.php?id_vehiculo=1` |

Respuesta:
```json
[
  { "ID_MOVIMIENTO": "1", "FECHA_MOVIMIENTO": "2026-06-01", "VIN": "JT2S3FEJ3M1234567", "PLACA": "P-123-456", "NOMBRE_PERSONAL": "Ana" }
]
```

#### ws_movimientos_insert.php
| Campo | Valor |
|---|---|
| Archivo | `ws_movimientos_insert.php` |
| Método | GET |
| Parámetros | `id_vehiculo` (obligatorio), `id_transporte` (obligatorio), `id_personal` (obligatorio), `fecha` (obligatorio) |
| Ejemplo | `ws_movimientos_insert.php?id_vehiculo=1&id_transporte=1&id_personal=1&fecha=2026-06-10` |

Respuesta:
```json
{ "resultado": 1, "mensaje": "Movimiento registrado", "id": 5 }
```

---

### Ricardo — Taller, Reparación

#### ws_talleres_lista.php
| Campo | Valor |
|---|---|
| Archivo | `ws_talleres_lista.php` |
| Método | GET |
| Parámetros | Ninguno |
| Ejemplo | `ws_talleres_lista.php` |

Respuesta:
```json
[
  { "ID_TALLER": "1", "NOMBRE_TALLER": "Taller Martínez", "AUTORIZADO": "1", "DIRECCION": "Col. Escalón" }
]
```

#### ws_talleres_autorizados.php
| Campo | Valor |
|---|---|
| Archivo | `ws_talleres_autorizados.php` |
| Método | GET |
| Parámetros | Ninguno |
| Ejemplo | `ws_talleres_autorizados.php` |

Respuesta: mismo formato que `ws_talleres_lista.php` pero solo donde `AUTORIZADO = 1`.

#### ws_reparaciones_update.php
| Campo | Valor |
|---|---|
| Archivo | `ws_reparaciones_update.php` |
| Método | GET |
| Parámetros | `id_reparacion` (obligatorio), `apto_para_venta` (0 o 1), `requiere_otra_reparacion` (0 o 1), `fecha_fin` (opcional) |
| Ejemplo | `ws_reparaciones_update.php?id_reparacion=1&apto_para_venta=1&fecha_fin=2026-06-10` |

Respuesta:
```json
{ "resultado": 1, "mensaje": "Reparacion actualizada" }
```

#### ws_reparaciones_por_taller.php
| Campo | Valor |
|---|---|
| Archivo | `ws_reparaciones_por_taller.php` |
| Método | GET |
| Parámetros | `id_taller` (obligatorio) |
| Ejemplo | `ws_reparaciones_por_taller.php?id_taller=1` |

Respuesta: mismo formato que `ws_reparaciones_lista.php` pero filtrado por taller.

---

### Javier — Bodega, Sección, Venta

#### ws_bodegas_lista.php
| Campo | Valor |
|---|---|
| Archivo | `ws_bodegas_lista.php` |
| Método | GET |
| Parámetros | Ninguno |
| Ejemplo | `ws_bodegas_lista.php` |

Respuesta:
```json
[
  { "ID_BODEGA": "1", "NOMBRE_BODEGA": "Bodega Central", "DIRECCION": "Soyapango" }
]
```

#### ws_secciones_lista.php
| Campo | Valor |
|---|---|
| Archivo | `ws_secciones_lista.php` |
| Método | GET |
| Parámetros | `id_bodega` (opcional) |
| Ejemplo sin filtro | `ws_secciones_lista.php` |
| Ejemplo con filtro | `ws_secciones_lista.php?id_bodega=1` |

Respuesta:
```json
[
  { "ID_SECCION": "1", "NIVEL_SECCION": "A", "CAPACIDAD_MAX": "10", "CAPACIDAD_ACTUAL": "7", "NOMBRE_BODEGA": "Bodega Central" }
]
```

#### ws_secciones_disponibles.php
| Campo | Valor |
|---|---|
| Archivo | `ws_secciones_disponibles.php` |
| Método | GET |
| Parámetros | Ninguno |
| Ejemplo | `ws_secciones_disponibles.php` |

Respuesta: mismo formato que `ws_secciones_lista.php` pero solo secciones donde `CAPACIDAD_ACTUAL < CAPACIDAD_MAX`.

#### ws_ventas_por_importador.php
| Campo | Valor |
|---|---|
| Archivo | `ws_ventas_por_importador.php` |
| Método | GET |
| Parámetros | `id_importador` (obligatorio) |
| Ejemplo | `ws_ventas_por_importador.php?id_importador=1` |

Respuesta: mismo formato que `ws_ventas_lista.php` pero filtrado por importador.
