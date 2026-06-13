# Problema con InfinityFree como servidor externo
**PDM115 — Grupo 02 | Etapa 2**

Este documento explica **por qué la app no logra consumir los servicios PHP alojados en
InfinityFree** (`https://serviciosguia8.page.gd/inventario_gpo02/`) y cómo **detectar el
problema**. La elección del hosting de reemplazo está aún en discusión y no se decide aquí.

---

## Resumen del problema

InfinityFree (plan gratuito) **bloquea por diseño a cualquier cliente que no sea un
navegador web**. No es un fallo del código Android, ni del emulador, ni del parseo JSON.

Cuando la app (o `curl`, o cualquier librería HTTP) pide un endpoint, InfinityFree **no
devuelve el JSON**: responde `HTTP 200` con una **página HTML de desafío anti-bot**. Esa
página trae JavaScript que descifra un valor con AES, lo guarda en una cookie llamada
`__test` y recarga la URL. Solo un navegador, que ejecuta ese JavaScript, supera el
desafío y recibe el JSON en una segunda petición.

`HttpURLConnection`, OkHttp, `curl`, etc. **no ejecutan JavaScript**, así que se quedan
con la página de desafío y nunca ven los datos.

---

## Por qué cada síntoma encaja

| Síntoma observado | Causa real |
|---|---|
| "La pantalla queda en blanco, sin error" | La app recibe `200 + HTML`. El chequeo `codigo != 200` pasa (el desafío responde 200), luego `new JSONArray(html)` lanza excepción, el `catch` la traga y devuelve lista vacía. |
| "A veces sí llegó el JSON en el logcat" | La cookie `__test` dura ~6 horas. Si una sesión coló una cookie válida, las siguientes peticiones devolvían JSON hasta que expiraba; luego HTML otra vez. Parecía aleatorio. |
| "Cambié a otro cliente HTTP y fue peor" | El bloqueo es del servidor, no del cliente. Cambiar de librería no puede arreglarlo. |
| "En el navegador del teléfono sí funciona" | El navegador ejecuta el JavaScript del desafío; la app no. Lo que funciona es el navegador, **no** la app. |

> **Corrección:** la idea de que "funciona en dispositivo físico" es incorrecta. Lo que
> funcionaba era el *navegador* del teléfono, no la aplicación. Tanto el emulador como el
> dispositivo físico fallan por la misma razón.

---

## Cómo DETECTAR el problema (reproducir el diagnóstico)

### Opción A — desde la PC con curl (la más rápida)

Pide el endpoint imitando a la app (sin navegador) y mira el cuerpo de la respuesta:

```bash
curl -s -i "https://serviciosguia8.page.gd/inventario_gpo02/ws_marcas_lista.php"
```

- **Si está bloqueado** verás `Content-Type: text/html` y un cuerpo como:
  ```html
  <html><body><script src="/aes.js"></script><script>
  ... document.cookie="__test="+toHex(slowAES.decrypt(...));
  location.href="...ws_marcas_lista.php?i=1";</script>
  <noscript>This site requires Javascript to work...</noscript></body></html>
  ```
  Las señales claras: aparece `aes.js`, la cookie `__test`, `slowAES.decrypt`, o el
  texto `This site requires Javascript`.

- **Si el host sirviera bien la API** verías `Content-Type: application/json` y el JSON
  directo (`[{"ID_MARCA":...}]`).

> El navegador NO sirve para este diagnóstico: como ejecuta el JavaScript, siempre te
> mostrará el JSON y ocultará el problema. Hay que pedir con un cliente que NO ejecute JS
> (curl, Postman con JS desactivado, o la propia app).

### Opción B — desde la app, haciendo visible el error

El parseo actual falla en silencio. Para diagnosticarlo, en `ControladorServicio` conviene
detectar cuando la respuesta **no es JSON** (no empieza con `[` ni `{`) y registrar los
primeros caracteres del cuerpo. Ejemplo del chequeo a agregar tras leer la respuesta:

```java
if (cuerpo != null) {
    String t = cuerpo.trim();
    if (!t.startsWith("[") && !t.startsWith("{")) {
        // El servidor devolvió HTML/desafío, no JSON
        Log.e(TAG, "Respuesta NO es JSON. Primeros 200 chars: "
                + t.substring(0, Math.min(200, t.length())));
        // (opcional) avisar al usuario con un Toast
    }
}
```

Con esto, en el logcat se vería el HTML del desafío en lugar de una lista vacía sin
explicación, y el problema se identifica en segundos.

---

## Conclusión

- El backend PHP y la base de datos **están correctos** (responden bien en local y el JSON
  es válido).
- El código Android **está correcto**; el cliente HTTP no es el problema.
- InfinityFree gratuito **no puede usarse como API** para una app móvil porque exige
  ejecución de JavaScript en el cliente. Su propia documentación lo indica.
- **Acción pendiente (en discusión):** mover los mismos archivos de `backend/` y el script
  `docs/bd_infinityfree.sql` a un hosting que sirva APIs sin desafío de navegador. El único
  cambio en Android sería la constante `EXTERNO` en `Urls.java`. El hosting concreto **no se
  ha decidido todavía**.