package sv.edu.ues.fia.sistema_pdm2;

public final class Urls {

    private Urls() {}

    // ── Cambiar la IP según la máquina de cada integrante ──────────────
    private static final String LOCAL   = "http://10.142.17.92/inventario_gpo02/";
    // ───────────────────────────────────────────────────────────────────

    private static final String EXTERNO = "https://serviciosguia8.page.gd/inventario_gpo02/";

    // Servicios disponibles
    public static final String MARCAS_LISTA          = "ws_marcas_lista.php";
    public static final String VEHICULOS_LISTA       = "ws_vehiculos_lista.php";
    public static final String VEHICULOS_BUSCAR      = "ws_vehiculos_buscar.php";
    public static final String VEHICULOS_INSERT      = "ws_vehiculos_insert.php";
    public static final String REPARACIONES_LISTA    = "ws_reparaciones_lista.php";
    public static final String REPARACIONES_INSERT   = "ws_reparaciones_insert.php";
    public static final String VENTAS_LISTA          = "ws_ventas_lista.php";
    public static final String VENTAS_INSERT         = "ws_ventas_insert.php";
    public static final String ESTADISTICAS          = "ws_estadisticas.php";
    public static final String MODELOS_POR_MARCA     = "ws_modelos_por_marca.php";

    // Servicios adicionales — Transporte / Personal Interno / Movimiento
    public static final String MOVIMIENTOS_HISTORIAL       = "ws_movimientos_historial.php";
    public static final String MOVIMIENTOS_INSERT          = "ws_movimientos_insert.php";
    public static final String REPORTE_TRANSPORTE_PERSONAL = "ws_reporte_transporte_personal.php";
    public static final String MOVIMIENTOS_CATALOGOS       = "ws_movimientos_catalogos.php";

    //Servicios adicionales
    public static final String VEHICULOS_POR_ESTADO  = "ws_vehiculos_por_estado.php";
    public static final String DESPERFECTOS_LISTA    = "ws_desperfectos_lista.php";
    public static final String DESPERFECTOS_INSERT   = "ws_desperfectos_insert.php";
    public static final String FOTOS_DESPERFECTO     = "ws_fotos_desperfecto.php";

    // Devuelve la URL base según el servidor elegido (0=Local, 1=Externo)
    public static String base(int servidor) {
        return servidor == 1 ? EXTERNO : LOCAL;
    }

    // Construye la URL completa: base + servicio + parámetros
    // Ejemplo: Urls.build(0, Urls.VEHICULOS_BUSCAR, "vin=JT2S3FEJ")
    public static String build(int servidor, String servicio, String parametros) {
        String url = base(servidor) + servicio;
        if (parametros != null && !parametros.isEmpty()) {
            url += "?" + parametros;
        }
        return url;
    }

    public static String build(int servidor, String servicio) {
        return build(servidor, servicio, null);
    }
}
