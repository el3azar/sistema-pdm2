package sv.edu.ues.fia.sistema_pdm2;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ControladorServicio {

    private static final String TAG = "ControladorServicio";

    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(8, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .build();

    // ── GET ────────────────────────────────────────────────────────────────
    public static String get(String urlStr, android.content.Context ctx) {
        Request request = new Request.Builder()
                .url(urlStr)
                .header("User-Agent", "Mozilla/5.0 (Android)")
                .header("Accept", "application/json")
                .build();
        try {
            Response response = CLIENT.newCall(request).execute();
            int codigo = response.code();
            if (codigo != 200) {
                Log.e(TAG, "HTTP " + codigo + " en: " + urlStr);
                return null;
            }
            return response.body() != null ? response.body().string() : null;
        } catch (Exception e) {
            Log.e(TAG, "Error GET [" + e.getClass().getSimpleName() + "]: " + e.getMessage());
            return null;
        }
    }

    // ── POST ───────────────────────────────────────────────────────────────
    // parametros: "clave1=valor1&clave2=valor2"
    public static String post(String urlStr, String parametros, android.content.Context ctx) {
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (parametros != null && !parametros.isEmpty()) {
            for (String par : parametros.split("&")) {
                String[] kv = par.split("=", 2);
                if (kv.length == 2) bodyBuilder.addEncoded(kv[0], kv[1]);
            }
        }
        RequestBody body = bodyBuilder.build();
        Request request = new Request.Builder()
                .url(urlStr)
                .header("User-Agent", "Mozilla/5.0 (Android)")
                .post(body)
                .build();
        try {
            Response response = CLIENT.newCall(request).execute();
            int codigo = response.code();
            if (codigo != 200) {
                Log.e(TAG, "HTTP " + codigo + " en: " + urlStr);
                return null;
            }
            return response.body() != null ? response.body().string() : null;
        } catch (Exception e) {
            Log.e(TAG, "Error POST [" + e.getClass().getSimpleName() + "]: " + e.getMessage());
            return null;
        }
    }

    // ── Parseo JSON ────────────────────────────────────────────────────────
    public static List<JSONObject> parsearArray(String json, android.content.Context ctx) {
        List<JSONObject> lista = new ArrayList<>();
        if (json == null) return lista;
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) lista.add(array.getJSONObject(i));
        } catch (Exception e) {
            Log.e(TAG, "Error parseando array: " + e.getMessage());
        }
        return lista;
    }

    public static JSONObject parsearObjeto(String json, android.content.Context ctx) {
        if (json == null) return null;
        try {
            return new JSONObject(json);
        } catch (Exception e) {
            Log.e(TAG, "Error parseando objeto: " + e.getMessage());
            return null;
        }
    }

    public static boolean fueExitoso(String json, android.content.Context ctx) {
        JSONObject obj = parsearObjeto(json, ctx);
        if (obj == null) return false;
        try {
            return obj.getInt("resultado") == 1;
        } catch (Exception e) {
            return false;
        }
    }

    public static String obtenerMensaje(String json) {
        JSONObject obj = parsearObjeto(json, null);
        if (obj == null) return "Sin respuesta del servidor";
        return obj.optString("mensaje", "Respuesta sin mensaje");
    }
}
