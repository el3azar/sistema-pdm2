package sv.edu.ues.fia.sistema_pdm2;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ControladorServicio {

    private static final String TAG             = "ControladorServicio";
    private static final int    TIMEOUT_CONEXION = 5000;
    private static final int    TIMEOUT_LECTURA  = 7000;

    // ── GET ────────────────────────────────────────────────────────────────
    // Realiza una petición HTTP GET y devuelve la respuesta como String JSON.
    // Devuelve null si hay error de conexión.
    public static String get(String urlStr, Context ctx) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(TIMEOUT_CONEXION);
            con.setReadTimeout(TIMEOUT_LECTURA);
            con.connect();

            int codigo = con.getResponseCode();
            if (codigo != 200) {
                Log.e(TAG, "HTTP " + codigo + " en: " + urlStr);
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) sb.append(linea);
            br.close();
            con.disconnect();
            return sb.toString();

        } catch (Exception e) {
            Log.e(TAG, "Error GET: " + e.getMessage());
            Toast.makeText(ctx, "Error de conexión", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    // ── POST ───────────────────────────────────────────────────────────────
    // Realiza una petición HTTP POST enviando parámetros en el body.
    // parametros: "clave1=valor1&clave2=valor2"
    public static String post(String urlStr, String parametros, Context ctx) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setConnectTimeout(TIMEOUT_CONEXION);
            con.setReadTimeout(TIMEOUT_LECTURA);
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.connect();

            if (parametros != null && !parametros.isEmpty()) {
                OutputStream os = con.getOutputStream();
                os.write(parametros.getBytes("UTF-8"));
                os.flush();
                os.close();
            }

            int codigo = con.getResponseCode();
            if (codigo != 200) {
                Log.e(TAG, "HTTP " + codigo + " en: " + urlStr);
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) sb.append(linea);
            br.close();
            con.disconnect();
            return sb.toString();

        } catch (Exception e) {
            Log.e(TAG, "Error POST: " + e.getMessage());
            Toast.makeText(ctx, "Error de conexión", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    // ── Parseo JSON ────────────────────────────────────────────────────────
    // Convierte un JSON array string en lista de JSONObject.
    // Usar cuando el servicio devuelve [ {...}, {...} ]
    public static List<JSONObject> parsearArray(String json, Context ctx) {
        List<JSONObject> lista = new ArrayList<>();
        if (json == null) return lista;
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                lista.add(array.getJSONObject(i));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parseando array: " + e.getMessage());
            Toast.makeText(ctx, "Error procesando respuesta", Toast.LENGTH_SHORT).show();
        }
        return lista;
    }

    // Convierte un JSON object string en JSONObject.
    // Usar cuando el servicio devuelve { "campo": valor }
    public static JSONObject parsearObjeto(String json, Context ctx) {
        if (json == null) return null;
        try {
            return new JSONObject(json);
        } catch (Exception e) {
            Log.e(TAG, "Error parseando objeto: " + e.getMessage());
            Toast.makeText(ctx, "Error procesando respuesta", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    // Extrae el valor de "resultado" de la respuesta de un INSERT.
    // Devuelve true si resultado == 1
    public static boolean fueExitoso(String json, Context ctx) {
        JSONObject obj = parsearObjeto(json, ctx);
        if (obj == null) return false;
        try {
            return obj.getInt("resultado") == 1;
        } catch (Exception e) {
            return false;
        }
    }
}
