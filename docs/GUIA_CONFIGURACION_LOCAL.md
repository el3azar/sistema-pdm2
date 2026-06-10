# Guía de Configuración Local
**PDM115 — Grupo 02 | Etapa 2**

Seguir estos pasos en orden. No saltar ninguno.

---

## Requisitos previos
- WAMP instalado y funcionando (icono verde en la barra de tareas)
- Android Studio instalado
- Git instalado
- El repo clonado en tu máquina

---

## Paso 1 — Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/sistema-pdm2.git
```

Abrir Android Studio → `Open` → seleccionar la carpeta `sistema-pdm2/`.

---

## Paso 2 — Crear la base de datos en phpMyAdmin

1. Abrir el navegador y entrar a `http://localhost/phpmyadmin`
2. Ir a la pestaña **SQL**
3. Pegar el contenido de `docs/bd_mysql.sql` y ejecutar
4. Verificar que se creó la base de datos `inventario_gpo02`
5. Volver a la pestaña **SQL**
6. Pegar el contenido de `docs/bd_mysql_datos.sql` y ejecutar
7. Verificar que las tablas tienen datos (ej. tabla `VEHICULO` debe tener 10 registros)

---

## Paso 3 — Copiar los servicios PHP a WAMP

Copiar la carpeta `backend/` completa a:
```
C:\wamp\www\inventario_gpo02\
```

La estructura debe quedar así:
```
C:\wamp\www\inventario_gpo02\
    conexion.php
    ws_marcas_lista.php
    ws_vehiculos_lista.php
    ...
```

---

## Paso 4 — Encontrar tu dirección IP

1. Abrir CMD (tecla Windows → escribir `cmd` → Enter)
2. Ejecutar:
```
ipconfig
```
3. Buscar la línea **Dirección IPv4** dentro de tu adaptador de red activo
4. Anotar esa IP (ejemplo: `192.168.0.14`)

---

## Paso 5 — Cambiar la IP en el proyecto Android

Abrir el archivo:
```
app/src/main/java/sv/edu/ues/fia/sistema_pdm2/Urls.java
```

Buscar la línea 8 (única que debes cambiar):
```java
private static final String LOCAL = "http://192.168.0.14/inventario_gpo02/";
```

Reemplazar `192.168.0.14` por tu IP del paso anterior. Ejemplo:
```java
private static final String LOCAL = "http://192.168.1.25/inventario_gpo02/";
```

> **Importante:** este cambio es solo local, no hacer commit de este archivo con tu IP
> porque cada integrante tiene una IP diferente.

---

## Paso 6 — Probar los servicios en el navegador

Antes de correr Android, verificar que los PHP responden correctamente:

| URL | Resultado esperado |
|---|---|
| `http://localhost/inventario_gpo02/ws_marcas_lista.php` | JSON con 9 marcas |
| `http://localhost/inventario_gpo02/ws_vehiculos_lista.php` | JSON con 10 vehículos |
| `http://localhost/inventario_gpo02/ws_estadisticas.php` | JSON con por_estado y por_tipo |
| `http://localhost/inventario_gpo02/ws_vehiculos_buscar.php?vin=JT2S3FEJ3M1234567` | JSON con 1 vehículo |

Si ves JSON con datos, todo está listo. Si ves error, revisar que WAMP esté activo
y que la carpeta se llame exactamente `inventario_gpo02`.

---

## Paso 7 — Correr la aplicación Android

1. Conectar dispositivo Android o iniciar emulador
2. En Android Studio: `Run → Run 'app'`
3. Probar `Marcas` primero — debe mostrar la lista de marcas al presionar **Servidor Local**

---

## Solución de problemas frecuentes

| Problema | Causa probable | Solución |
|---|---|---|
| Error de conexión en Android | IP incorrecta | Verificar `Urls.java` con `ipconfig` |
| Error de conexión en Android | WAMP no activo | Verificar icono verde de WAMP |
| PHP devuelve error 500 | BD no creada | Ejecutar `bd_mysql.sql` en phpMyAdmin |
| PHP devuelve array vacío | Datos no cargados | Ejecutar `bd_mysql_datos.sql` en phpMyAdmin |
| Android no conecta al emulador | IP de red no alcanzable desde emulador | Usar `10.0.2.2` como IP en el emulador |
