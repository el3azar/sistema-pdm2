## **UNIVERSIDAD DE EL SALVADOR** 

**FACULTAD DE INGENIERIA Y ARQUITECTURA ESCUELA DE INGENIERIA DE SISTEMAS INFORMATICOS PROGRAMACION PARA DISPOSITIVOS MOVILES PDM115   Ciclo I-2026** 

## **GUIA DE LABORATORIO N° 10 CONSUMO DE SERVICIO WEB DESDE APLICACIONES MOVILES** 

Para Android 

**Objetivo** : Que el estudiante conozca las funciones básicas para consumir un servicio web de forma síncrona desde una aplicación móvil en 2 plataformas estudiadas en la asignatura, manejar los errores que pueda producirse en la conexión, y que además conozca las funciones básicas para realizar la lectura y análisis de un archivo XML o JSON. 

**Descripción** : En esta sesión de laboratorio se creará una función en la cual se establecerá una conexión con un servicio web local de forma síncrona, el dato recibido será un archivo XML, con el cual utilizaremos un parser para obtener el dato correspondiente. Posteriormente se hara un segundo proyecto en donde realizará una operación similar pero con Archivo recibido en formato JSON con conexión a Base de Datos MySQL. 

## **Requisitos:** 

*Haber Desarrollado las Guías de Creación de Servicios Web En (Netbeans y PHP) para la conversión de números a letras y los servicios de Conexión a Base de Datos (Netbeans y PHP) . 

*Desplegar los servicios en Glassfish y Apache 

*Diferenciar los datos de cada versión de Base de Datos que se utilice. Es decir que deberá introducir materias diferentes en las tres bases (Local, 000Webhost y Servidor EISI). 

*Preparar El controlador de Base de Datos móvil para las operaciones que se realizaran, para este caso, el código de creación de base e inserción de la tabla Materia. 

*Conocer comandos DML especialmente los referentes a funciones de agregación y establecimiento de filtros en clausula WHERE. 

## **Índice** 

Aplicaciones con Servicio Web ........................................................................................................................ 1 Desarrollo en Android Studio .......................................................................................................................... 3 Proyecto 1 Consumo de Servicio de conversión de número a letras en java y php ................................... 3 Modificar el archivo AndroidManifest.xml ............................................................................................. 5 Modificar el recurso String.xml ............................................................................................................... 7 Menú Principal ........................................................................................................................................ 8 Modificación de la Interfaz gráfica (layout activity_main.xml) ............................................................... 8 Aplicación (Archivo Java) ....................................................................................................................... 10 Controlador de Datos ............................................................................................................................ 15 Prueba de los Servicios en Browser ...................................................................................................... 19 Prueba de la aplicación ......................................................................................................................... 22 Proyecto 2 Consumo de Servicios en java y php para acceder a Base de Datos Carnet MySQL .............. 23 Creación de un Proyecto Android ......................................................................................................... 23 Agregar en librería de apache al gradle ................................................................................................ 23 Modificar el archivo AndroidManifest.xml ........................................................................................... 25 Modificar el recurso String.xml ............................................................................................................. 26 Modificación de la Interfaz gráfica (layout activity_main.xml) ............................................................. 26 Aplicación (Archivo MainActivity.Java) ................................................................................................. 27 Detalle de los archivos creados ............................................................................................................. 28 Controlador de Base de Datos Movil(ControlBDAlumno) ..................................................................... 30 Controlador de Base de Datos Servidor(ControladorServicio) ............................................................. 31 ActualizarMateria .................................................................................................................................. 36 Datos en Bases Servidor ........................................................................................................................ 40 Prueba de los Servicios que se utilizaran .............................................................................................. 41 Pruebe la Aplicación .............................................................................................................................. 42 Insertar Notas ........................................................................................................................................ 43 Consulta de Datos ................................................................................................................................. 46 Pruebe la Aplicación (Actualizar Notas) ................................................................................................ 47 Consultar Promedio de notas por Carnet ............................................................................................. 48 Activity(PromedioAlumnoActivity.java) .................................................................................................... 49 Pruebe la Aplicación (Promedio de Notas por carnet) .......................................................................... 51 Anexo 1 Buscar la dirección IP de nuestra pc por medio de consola ............................................................ 52 Anexo 2 Buscar la dirección IP de nuestra pc de forma visual ...................................................................... 53 

## **Aplicaciones con Servicio Web** 

Primeramente, debemos recordar que los servicio web se definen como sistemas de software diseñados para soportar una interacción maquina a máquina sobre una red, en otras palabras, podríamos decir que son como API's Web que pueden ser accedidas dentro de una red y son ejecutadas en el sistema que las aloja. Por ejemplo, se podría crear un servicio web que realice operaciones matemáticas, luego desde una aplicación podríamos invocar ese servicio siempre y cuando tenga conexión a la red en la cual se encuentra, para que de esa manera nuestra aplicación pueda realizar esas operaciones matemáticas definidas en el servicio web. Esto es muy útil cuando el dispositivo que ejecutara nuestra aplicación no posee los suficientes recursos para realizar ciertos procesos, imaginemos que las operaciones matemáticas son muy complejas, entonces estos se ejecutan en un servidor y luego solo es enviada la respuesta. 

Los servicios web más comunes son los que se refiere a clientes y servidor  que se comunican mediante mensajes XML que siguen el estándar SOAP. En los últimos años se ha popularizado un estilo de arquitectura Software conocido como REST. 

REST (Representational State Transfer) es un estilo de arquitectura de software para sistemas hipermedias (conjunto de métodos para escribir, diseñar y componer contenidos de multimedia) distribuidos tales como la Web. Este se refiere estrictamente a una colección de principios para el diseño de arquitecturas en red. Estos principios resumen como los recursos son definidos y diseccionados. Ahora cabe aclarar que REST no es un estándar sino solamente un estilo de arquitectura, pero a pesar que no es un estándar se base en estándares tales como HTTP, URL, Representación de Recursos (XML, HTML, GIF ,etc.) y tipo MIME (text/xml, text/html, etc.). 

Con REST tenemos la posibilidad de recibir dos tipos de respuesta, en XML y JSON. La forma como funciona es similar a un cliente/servidor web normal a diferencia que hoy nuestra aplicación procesara los datos, una característica de la arquitectura REST es que la petición se expone en la URL en forma de directorios  y recursos. 

Como se mencionó antes la respuesta puede ser en XML o JSON, por tal motivo la aplicación debe contener un parser (analizador sintáctico) para poder obtener la información contenida en estas respuestas enviadas desde el web service. Un parser puede ser un objeto que toma el archivo y lo analiza para obtener la información requerida. 

1 

Para la realización de los siguientes proyectos el servicio estaba alojado en la dirección http://172.16.14.227:8080 /WelcomeRESTXML/webresources/welcome   y la respuesta en todos los casos es: 

_**<respuesta>**_ 

_**<numero>uno</numero>**_ 

_**</respuesta>**_ 

2 

## **Desarrollo en Android Studio** 

**Proyecto 1 Consumo de Servicio de conversión de número a letras en java y php** 

- Entre a Android Studio 

- Cree un nuevo Proyecto de Android(Empty Views Activity)  con las siguientes características: 

3 

## Nombre de la Aplicación: C **onsumoWS1Carnet** (Nivel Mínimo API 24) 

## Presione **Finish** 

4 

## **Modificar el archivo AndroidManifest.xml** 

Agregar las líneas que se indican 

Dichos permisos son para habilitar uso de internet, permitir que se use la librería de http.legacy(obsoleta) y que se permita que se ejecuten los servicios locales. 

5 

## Modificar Script build.gradle.kts 

## Agregamos la linea 

6 

Sincronizamos la librería con el proyecto y se descargaran los componentes necesarios para el buen funcionamiento del programa. 

## **Modificar el recurso String.xml** 

Una vez que ya tengamos listo el proyecto agregaremos las variables string en res/values/string.xml 

`<?xml version=` _`"1.0"`_ `encoding=` _`"utf-8"`_ `?> <resources> <` **`string name="app_name"`** `>ConsumoWS1Carnet</` **`string`** `> <` **`string name="action_settings"`** `>Settings</` **`string`** `> <` **`string name="indicaciones"`** `>Ingrese un digito</` **`string`** `> <` **`string name="servicioLocalNb"`** `>Servicio Local NetBeans</` **`string`** `> <` **`string name="servicioLocal"`** `>Servidor Php local</` **`string`** `> <` **`string name="servicioUES"`** `>Servidor Php UES </` **`string`** `> <` **`string name="ServicioGratuito"`** `>Servidor Php Hosting </` **`string`** `> </resources>` 

7 

## **Menú Principal** 

## **Modificación de la Interfaz gráfica (layout activity_main.xml)** 

## Sustituimos en  su totalidad el código por el siguiente 

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:tools="http://schemas.android.com/tools"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:orientation="vertical"
tools:context=".MainActivity" >
    <TextView
android:id="@+id/textInidicaciones"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:text="@string/indicaciones" />
```

```
    <EditText
android:id="@+id/editEntrada"
android:layout_width="match_parent"
android:layout_height="wrap_content"
android:hint="@string/indicaciones"
android:inputType="number"
android:minHeight="48dp">
        <requestFocus />
    </EditText>
    <Button
android:id="@+id/Button1"
android:layout_width="318dp"
android:layout_height="wrap_content"
android:text="@string/servicioLocalNb" />
    <Button
android:id="@+id/Button2"
android:layout_width="318dp"
android:layout_height="wrap_content"
android:text="@string/servicioLocal" />
    <Button
android:id="@+id/Button3"
android:layout_width="318dp"
android:layout_height="wrap_content"
```

8 

```
android:text="@string/servicioUES" />
    <Button
android:id="@+id/Button4"
android:layout_width="match_parent"
android:layout_height="wrap_content"
android:text="@string/ServicioGratuito" />
    <TextView
android:id="@+id/textSalidaLocalNb"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:text="" />
    <TextView
android:id="@+id/textSalidaLocal"
android:layout_width="wrap_content"
android:layout_height="wrap_content" />
    <TextView
android:id="@+id/textSalidaPublicaUes"
android:layout_width="wrap_content"
android:layout_height="wrap_content" />
    <TextView
android:id="@+id/textSalidaHost"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:text="" />
</LinearLayout>
```

9 

## **Aplicación (Archivo Java)** 

Como se puede observar en la definición de Button el evento onClick responderá a la función obtenerDatos. Ahora modificaremos MainActivity para que realice la conexión, para esto se auxiliara de una clase “Controladora” que es la que realizara la conexión al servicio web y realizara el parseo de la respuesta. En la parte marcado por amarillo, se debe de sustituir, la dirección ip asignada por la dirección ip de la computadora en la que se está trabajando, es decir, en la cual se ha realizado el webservice. 

package sv.edu.ues.fia.eisi.consumows1carnet; 

import androidx.appcompat.app.AppCompatActivity; 

import android.os.Bundle; 

import org.json.JSONObject; import org.w3c.dom.Document; import org.w3c.dom.Node; import android.os.StrictMode; import android.util.Log; import android.view.View; import android.widget.Button; import android.widget.EditText; import android.widget.TextView; import android.annotation.SuppressLint; @SuppressLint("NewApi") 

public class MainActivity extends AppCompatActivity{ 

TextView indicaciones; 

EditText entrada; 

TextView salidalocalNb; TextView salidalocal; 

TextView salidapublicaues; 

TextView salidaHost; 

10 

Button BotonLocalNb; 

Button BotonLocal; 

Button BotonHosting; 

Button BotonPublicaUes; 

//para Java 

private static String urlNetbeansLocal = "http://192.168.159.1:8080/CarnetWebApplication/webresources/generic/"; 

//Para PHP 

private static String urlPhpLocal = "http://192.168.159.1/NumerosEnLetras.php?numero="; 

private static String urlPhpUES = "https://eisi.fia.ues.edu.sv/eisi25/MQ25001/NumerosEnLetras.php?numero="; 

//private static String urlPhpHosting = "http://192.168.159.1/NumerosEnLetras.php?numero="; 

private static String urlPhpHosting = "https://pdm115guia6.000webhostapp.com/NumerosLetras.php?numero="; 

@SuppressLint("NewApi") 

@Override 

protected void onCreate(Bundle savedInstanceState) { 

super.onCreate(savedInstanceState); 

setContentView(R.layout.activity_main); 

//Lineas de codigo solo para depuracion. 

StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); 

StrictMode.setThreadPolicy(policy); 

indicaciones = (TextView) findViewById(R.id.textInidicaciones); 

entrada = (EditText) findViewById(R.id.editEntrada); 

salidalocalNb = (TextView) findViewById(R.id.textSalidaLocalNb); 

salidalocal = (TextView) findViewById(R.id.textSalidaLocal); 

salidapublicaues = (TextView) findViewById(R.id.textSalidaPublicaUes); 

11 

salidaHost = (TextView) findViewById(R.id.textSalidaHost); BotonLocalNb=(Button) findViewById(R.id.Button1); BotonLocal=(Button) findViewById(R.id.Button2); BotonPublicaUes=(Button) findViewById(R.id.Button3); BotonHosting=(Button) findViewById(R.id.Button4); BotonLocalNb.setOnClickListener(onClick); BotonLocal.setOnClickListener(onClick); BotonPublicaUes.setOnClickListener(onClick); BotonHosting.setOnClickListener(onClick); } 

View.OnClickListener onClick=new View.OnClickListener() { @Override public void onClick(View v) { // TODO Auto-generated method stub if (v.getId()==R.id.Button1){ obtenerDatosNetbeansLocal(); } if (v.getId()==R.id.Button2){ obtenerDatosLocal(); } if (v.getId()==R.id.Button3){ obtenerDatosPublicaUES(); } if (v.getId()==R.id.Button4){ obtenerDatosHosting(); } } 

12 

}; 

public void obtenerDatosNetbeansLocal() { 

//Para Netbeans/java 

Controlador parser = new Controlador(); 

String dato = entrada.getText().toString(); 

String url = urlNetbeansLocal+ dato; 

String xml = parser.obtenerRespuestaDeURL(url,getApplicationContext()); 

Document doc = parser.mapeoXML(xml); 

Log.v("MI XML",xml); 

// ESTAS LINEAS DE CODIGO ES CUANDO SOLO EXISTE UN NODO SIN PADRE 

Node n = doc.getFirstChild(); 

String respuesta = parser.getElementValue(n); 

// MUESTRA LA RESPUESTA 

salidalocalNb.setText("Resultado de servicio local Netbeans: "+respuesta); 

} 

public void obtenerDatosLocal() { 

Controlador parser = new Controlador(); 

String dato = entrada.getText().toString(); 

String url = urlPhpLocal + dato; 

String json = parser.obtenerRespuestaDeURL(url,getApplicationContext()); 

try { 

JSONObject obj = new JSONObject(json); 

salidalocal.setText("Resultado de servicio local: "+obj.getString("numero")); 

} catch (Exception e) { 

salidalocal.setText(Controlador.informacionError); 

} 

} 

public void obtenerDatosPublicaUES() { 

13 

Controlador parser = new Controlador(); 

String dato = entrada.getText().toString(); 

String url = urlPhpUES + dato; 

String json = parser.obtenerRespuestaDeURL(url,getApplicationContext()); 

try { 

JSONObject obj = new JSONObject(json); 

salidapublicaues.setText("Resultado de servicio Publico(UES)"+obj.getString("numero")); 

} catch (Exception e) { 

salidapublicaues.setText(Controlador.informacionError); 

public void obtenerDatosHosting() { 

Controlador parser = new Controlador(); 

String dato = entrada.getText().toString(); 

String url = urlPhpHosting + dato; 

String json = parser.obtenerRespuestaDeURL(url,getApplicationContext()); 

try { 

JSONObject obj = new JSONObject(json); 

salidaHost.setText("Resultado de servicio hosting gratuito: "+obj.getString("numero")); 

} catch (Exception e) { 

salidaHost.setText(Controlador.informacionError); 

} 

14 

## **Controlador de Datos** 

Hoy crearemos dentro del mismo paquete la clase controlador(nueva clase de java) la cual deberá estar implementada de la siguiente manera. 

**`package sv.edu.ues.fia.eisi.consumows1carnet;`** import android.content.Context; import android.util.Log; import android.widget.Toast; import org.apache.http.HttpEntity; import org.apache.http.HttpResponse; import org.apache.http.client.ClientProtocolException; import org.apache.http.client.HttpClient; import org.apache.http.client.methods.HttpGet; import org.apache.http.impl.client.DefaultHttpClient; import org.apache.http.params.BasicHttpParams; import org.apache.http.params.HttpConnectionParams; import org.apache.http.params.HttpParams; import org.apache.http.util.EntityUtils; import org.w3c.dom.Document; import org.w3c.dom.Element; import org.w3c.dom.Node; import org.w3c.dom.NodeList; import org.xml.sax.InputSource; import org.xml.sax.SAXException; import java.io.IOException; import java.io.StringReader; import java.io.UnsupportedEncodingException; 

15 

import javax.xml.parsers.DocumentBuilder; import javax.xml.parsers.DocumentBuilderFactory; import javax.xml.parsers.ParserConfigurationException; public class Controlador { public static String informacionError = "Conexion Exitosa"; public String obtenerRespuestaDeURL(String url,Context ctx) { String respuesta = " "; try { HttpParams params = new BasicHttpParams(); int timeoutConnection = 3000; HttpConnectionParams.setConnectionTimeout(params, timeoutConnection); int timeoutSocket = 5000; HttpConnectionParams.setSoTimeout(params, timeoutSocket); HttpClient httpClient = new DefaultHttpClient(params); HttpGet httpGet = new HttpGet(url); HttpResponse httpResponse = httpClient.execute(httpGet); HttpEntity httpEntity = httpResponse.getEntity(); respuesta = EntityUtils.toString(httpEntity); } catch (UnsupportedEncodingException e) { Toast.makeText(ctx, "Error de conexion", Toast.LENGTH_LONG).show(); e.printStackTrace(); } catch (ClientProtocolException e) { 

16 

Toast.makeText(ctx, "Error de conexion", Toast.LENGTH_LONG).show(); e.printStackTrace(); } catch (IOException e) { Toast.makeText(ctx, "Error de conexion", Toast.LENGTH_LONG).show(); e.printStackTrace(); } return respuesta; } public Document mapeoXML(String xml) { Document doc = null; DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); try { DocumentBuilder db = dbf.newDocumentBuilder(); InputSource is = new InputSource(); is.setCharacterStream(new StringReader(xml)); doc = db.parse(is); } catch (ParserConfigurationException e) { Log.e("Error: ", e.getMessage()); return null; } catch (SAXException e) { Log.e("Error: ", e.getMessage()); return null; 

17 

} catch (IOException e) { Log.e("Error: ", e.getMessage()); return null; return doc; public String getValue(Element item, String str) { NodeList n = item.getElementsByTagName(str); return this.getElementValue(n.item(0)); 

public final String getElementValue( Node elem ) { Node child; if( elem != null){ if (elem.hasChildNodes()){ 

for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){ if( child.getNodeType() == Node.TEXT_NODE  ){ return child.getNodeValue(); } } } } return ""; } } 

18 

Como puede observarse en MainActivity se forma la cadena URL y se le manda a obtenerXMLdeURL para obtener el resultado del servicio web.  En Controlador obtenerXMLdeURL realiza la conexión al servicio web especificando el tiempo que debe de estar intentado realizar la conexión, de no producirse la conexión devuelve null. 

Luego cuando ya se tiene el resultado en la variable xml se verifica que no sea null, es decir que obtuvimos la respuesta correcta del servicio,  después se realiza el mapeo a un tipo Document, para que luego solo especifiquemos la etiqueta que deseemos, en nuestro la etiqueta _numero_ . Las siguientes dos funciones getValue y getElementValue son utilizados para realizar el recorrido por todas las etiquetas. En este ejemplo se especificó la _etiqueta padre respuesta_ y luego se busca la _etiqueta hijo numero_ con el fin de mostrar cómo se realizaría si la etiqueta padre tuviera más de un hijo. La etiqueta padre es respuesta y la etiqueta hijo es número. 

## **Prueba de los Servicios en Browser** 

- Primero deben de correrse el servidor Glassfish(Guia6A), Presione  Clic derecho sobre la Aplicación Web y luego Deploy 

19 

- Ademas de inicializar WampServer para correr el servidor Apache. Clic en Inicio, Start Wampserver 

Al iniciarlo debe aparecer el icono en la barra de tareas como este , si aparece en color rojo o en anaranjado es porque hay otra aplicación que utiliza el puerto de salida de Apache. Debera finalizar esa aplicación y luego intentar nuevamente 

Probamos con anticipación los servicios web que utilizaremos: los de conversión de números a letras 

Nota: si no conoce su dirección ip, puede consultar anexo 1 o anexo 2 

- 1) El Local(netbeans en pc local) 

Levante el servicio web de la guía 6A(Servicios de suma y conversión de número a letras) 

Y ejecute la url que se muestra en la barra de dirección de su navegador 

http://192.168.159.1:8080/CarnetWebApplication/webresources/generic/2 

- 2) El servicio en el Servidor Local(wamp/www) 

http://192.168.159.1/NumerosEnLetras.php?numero=4 

20 

3) El servicio en server eisi. 

https://eisi.fia.ues.edu.sv/eisi25/MQ25001/NumerosEnLetras.php?numero=4 

1) El servicio del 000Webhost, puede probarlo con el de la cátedra o con el suyo. 

https://pdm115guia6.000webhostapp.com/NumerosLetras.php?numero=4 

Notas: 

* Se le ha puesto un prefijo a cada servicio para diferenciar las respuestas en android. 

*Si no ha concluido sus servicios, puede utilizar los proporcionados por la catedra para conversión de números a letras, a excepción de los que insertan datos mas adelante 

21 

## **Prueba de la aplicación** 

Luego de comprobar los servicios en el Navegador(o Browser) ejecute la Aplicación Android para consumir los servicios 

**Si no le funciona correctamente puede ver el proyecto terminado en el la pagina de la asignatura  para verificar si alguna línea de código tiene error o si falta alguna configuracion.** 

22 

## **Proyecto 2 Consumo de Servicios en java y php para acceder a Base de Datos Carnet MySQL** 

## **Creación de un Proyecto Android** 

- Ejecutamos Android Studio 

- Creamos un nuevo Proyecto de Android(Empty Activity)  con las siguientes características: 

Nombre de la Aplicación: **ConsumoWS2CarnetBD** (Nivel Mínimo API 24) 

## **Agregar en librería de apache al gradle Busque en su árbol de proyecto el archivo build.gradle.kts(Module app)** 

## Agregamos la linea 

23 

Sincronizamos la librería con el proyecto y se descargaran los componentes necesarios para el buen funcionamiento del programa. 

24 

## **Modificar el archivo AndroidManifest.xml** 

25 

## **Modificar el recurso String.xml** 

Una vez que ya tengamos listo el proyecto agregaremos las variables string en res/values/string.xml 

```
<resources>
    <string name="app_name">ConsumoWS2CarnetBD</string>
    <string name="actualizar_materia">Actualizar Materias</string>
    <string name="promedio_alumno">Promedio de Alumno</string>
    <string name="ingresar_nota">Ingresar Nota</string>
    <string name="title_activity_materia">MateriaActivity</string>
    <string name="title_activity_promedio_alumno">PromedioAlumnoActivity</string>
    <string name="title_activity_ingresar_nota">IngresarNotaActivity</string>
    <string name="indicacion_materia">A partir de que fecha desea
actualizar?</string>
    <string name="formato_fecha">dd/mm/aaaa</string>
    <string name="servicio_PHPexterno">Servicio PHP Externo</string>
    <string name="servicio_PHPlocal">Servicio PHP Local</string>
    <string name="servicio_PublicoUES">EISI Publico</string>
    <string name="guardar">Guardar</string>
    <string name="carnet">Carnet</string>
    <string name="cod_materia">Codigo Materia</string>
    <string name="ciclo">Ciclo</string>
    <string name="nota_final">Nota Final</string>
    <string name="menu_settings">Settings</string>
    <string
name="title_activity_actualizar_alumno">ActualizarAlumnoActivity</string>
    <string
name="title_activity_actualizar_materia">ActualizarMateriaActivity</string>
</resources>
```

**Modificación de la Interfaz gráfica (layout activity_main.xml)** Sustituimos en su totalidad el código por el siguiente 

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:tools="http://schemas.android.com/tools"
android:id="@+id/LinearLayout1"
android:layout_width="match_parent"
android:layout_height="match_parent"
android:orientation="vertical"
tools:context=".MainActivity"
android:weightSum="1">
    <Button
android:id="@+id/button_materia"
android:layout_width="match_parent"
android:layout_height="wrap_content"
android:onClick="lanzarActividad"
android:text="@string/actualizar_materia"
android:layout_weight="0.10" />
    <Button
android:id="@+id/button_alumno"
android:layout_width="match_parent"
android:layout_height="wrap_content"
android:onClick="lanzarActividad"
```

26 

```
android:text="@string/promedio_alumno"
android:layout_weight="0.06" />
    <Button
android:id="@+id/button_nota"
android:layout_width="match_parent"
android:layout_height="wrap_content"
android:onClick="lanzarActividad"
android:text="@string/ingresar_nota"
android:layout_weight="0.06" />
</LinearLayout>
```

## **Aplicación (Archivo MainActivity.Java)** 

```
package sv.ues.edu.fia.eisi.consumows2carnetbd;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
public class MainActivity extends AppCompatActivity {
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
public void lanzarActividad(View v){
Intent i = null;
if (v.getId()==R.id.button_materia){
i = new
Intent(this,ActualizarMateriaActivity.class);
                startActivity(i);
        }
if (v.getId()==R.id.button_alumno){
i = new Intent(this,PromedioAlumnoActivity.class);
                startActivity(i);
        }
if (v.getId()==R.id.button_nota){
i = new Intent(this,IngresarNotaActivity.class);
                startActivity(i);
        }
    }
}
```

27 

Este Menu invoca 3 Activities para Estraer o Enviar Datos al Servidor-Base de Datos… estos se crearan a continuación, adicionalmente se crearán 3 clases más: una para Materia, Otra para El controlador de Base SQLIte, y uno para hacer los Parseos (ControladorServicio) 

## **Detalle de los archivos creados** 

## **ControlBDAlumno(Archivo de Clase java)** 

Controladora para interactuar con la Base SQLite, esta es una versión  limitada, solo para hacer las operaciones de esta guia 

## **Materia (Archivo de Clase java)** 

Clase para utilizar dentro del controlador SQLite. 

## **ControladorServicio(Archivo de Clase java)** 

Hace los parseos o conversiones necesarias entre las respuestas que son recibidas de los servicios y el formato que requieren para ser vistos en la App. 

## **ActualizarMateria(Activity)** 

Este formulario traerá los datos del servidor(Con Base de Datos MySQL) para mostrarse en una lista y luego insertarse en una Base de Datos SQLITE 

28 

## **Ingresar Nota (Activity)** 

Este formulario  Enviara los datos al servidor(Con Base de Datos MySQL) 

## **Promedio Nota (Activity)** 

Este formulario  consultara con una función de agregación,  los datos al servidor(Con Base de Datos MySQL) 

## **Ejecute la aplicación en este momento** 

29 

## **Controlador de Base de Datos Movil(ControlBDAlumno)** 

```
package sv.edu.ues.fia.eisi.consumows2carnetbd;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class ControlBDAlumno {
private final Context context;
private DatabaseHelper DBHelper;
private SQLiteDatabase db;
public ControlBDAlumno(Context ctx) {
this.context = ctx;
DBHelper = new DatabaseHelper(context);
    }
private static class DatabaseHelper extends SQLiteOpenHelper {
private static final String BASE_DATOS = "alumno.s3db";
private static final int VERSION = 1;
public DatabaseHelper(Context context) {
super(context, BASE_DATOS, null, VERSION);
        }
@Override
public void onCreate(SQLiteDatabase db) {
try {
                db.execSQL("CREATE TABLE alumno(carnet VARCHAR(7) NOT NULL
PRIMARY KEY,nombre VARCHAR(30),apellido VARCHAR(30),sexo VARCHAR(1),matganadas
INTEGER);");
                db.execSQL("CREATE TABLE materia(codmateria VARCHAR(6) NOT NULL
PRIMARY KEY,nommateria VARCHAR(30),unidadesval VARCHAR(1));");
                db.execSQL("CREATE TABLE nota(carnet VARCHAR(7) NOT NULL
,codmateria VARCHAR(6) NOT NULL ,ciclo VARCHAR(5) ,notafinal REAL ,PRIMARY
KEY(carnet,codmateria,ciclo));");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
{
// TODO Auto-generated method stub
}
    }
public void abrir() throws SQLException {
db = DBHelper.getWritableDatabase();
return;
    }
public void cerrar() {
DBHelper.close();
    }
public String insertar(Materia materia) {
        String regInsertados = "Registro Insertado NÂº= ";
long contador = 0;
        ContentValues mat = new ContentValues();
```

30 

```
        mat.put("codmateria", materia.getCodMateria());
        mat.put("nommateria", materia.getNombreMateria());
        mat.put("unidadesval", materia.getUnidadesVal());
        contador = db.insert("materia", null, mat);
if (contador == -1 || contador == 0) {
```

```
            regInsertados = "Error al Insertar el registro, Registro Duplicado.
Verificar inserciÃ³n";
        } else {
            regInsertados = regInsertados + contador;
        }
return regInsertados;
    }
}
```

## **Controlador de Base de Datos Servidor(ControladorServicio)** 

```
package sv.edu.ues.fia.eisi.consumows2carnetbd;
```

```
import android.content.Context;
```

```
import android.util.Log;
```

```
import android.widget.Toast;
```

```
import org.apache.http.HttpEntity;
```

```
import org.apache.http.HttpResponse;
```

```
import org.apache.http.StatusLine;
```

```
import org.apache.http.client.HttpClient;
```

```
import org.apache.http.client.methods.HttpGet;
```

- `import org.apache.http.client.methods.HttpPost;` 

```
import org.apache.http.entity.StringEntity;
```

- `import org.apache.http.impl.client.DefaultHttpClient;` 

```
import org.apache.http.params.BasicHttpParams;
```

```
import org.apache.http.params.HttpConnectionParams;
```

```
import org.apache.http.params.HttpParams;
```

- `import org.apache.http.util.EntityUtils;` 

```
import org.json.JSONArray;
```

```
import org.json.JSONException;
```

```
import org.json.JSONObject;
```

```
import java.util.ArrayList;
import java.util.List;
```

- `public class ControladorServicio {` 

```
public static String obtenerRespuestaPeticion(String url,
Context ctx) {
```

```
String respuesta = " ";
```

```
// Estableciendo tiempo de espera del servicio
```

```
HttpParams parametros = new BasicHttpParams();
```

31 

```
HttpConnectionParams.setConnectionTimeout(parametros, 3000);
HttpConnectionParams.setSoTimeout(parametros, 5000);
// Creando objetos de conexion
HttpClient cliente = new DefaultHttpClient(parametros);
HttpGet httpGet = new HttpGet(url);
try {
HttpResponse httpRespuesta = cliente.execute(httpGet);
StatusLine estado = httpRespuesta.getStatusLine();
int codigoEstado = estado.getStatusCode();
if (codigoEstado == 200) {
HttpEntity entidad = httpRespuesta.getEntity();
respuesta = EntityUtils.toString(entidad);
            }
        } catch (Exception e) {
Toast.makeText(ctx, "Error en la conexion",
Toast.LENGTH_LONG)
                    .show();
// Desplegando el error en el LogCat
Log.v("Error de Conexion", e.toString());
        }
return respuesta;
    }
public static String obtenerRespuestaPost(String url, JSONObject
obj,
Context ctx) {
String respuesta = " ";
try {
HttpParams parametros = new BasicHttpParams();
HttpConnectionParams.setConnectionTimeout(parametros,
3000);
HttpConnectionParams.setSoTimeout(parametros, 5000);
HttpClient cliente = new DefaultHttpClient(parametros);
HttpPost httpPost = new HttpPost(url);
httpPost.setHeader("content-type", "application/json");
StringEntity nuevaEntidad = new
StringEntity(obj.toString());
httpPost.setEntity(nuevaEntidad);
Log.v("Peticion",url);
Log.v("POST", httpPost.toString());
HttpResponse httpRespuesta = cliente.execute(httpPost);
```

32 

```
StatusLine estado = httpRespuesta.getStatusLine();
```

```
int codigoEstado = estado.getStatusCode();
if (codigoEstado == 200) {
respuesta = Integer.toString(codigoEstado);
Log.v("respuesta",respuesta);
            }
else{
Log.v("respuesta",Integer.toString(codigoEstado));
            }
        } catch (Exception e) {
Toast.makeText(ctx, "Error en la conexion",
Toast.LENGTH_LONG)
                    .show();
// Desplegando el error en el LogCat
Log.v("Error de Conexion", e.toString());
        }
return respuesta;
    }
public static List<Materia> obtenerMateriasLocal(String json,
Context ctx) {
List<Materia> listaMaterias = new ArrayList<Materia>();
```

```
try {
JSONArray materiasJSON = new JSONArray(json);
for (int i = 0; i < materiasJSON.length(); i++) {
JSONObject obj = materiasJSON.getJSONObject(i);
Materia materia = new Materia();
materia.setCodmateria(obj.getString("codmateria"));
materia.setNommateria(obj.getString("nommateria"));
```

```
materia.setUnidadesval(obj.getString("unidadesval"));
```

```
listaMaterias.add(materia);
            }
return listaMaterias;
        } catch (Exception e) {
Toast.makeText(ctx, "Error en parseO de JSON",
Toast.LENGTH_LONG)
```

```
                    .show();
return null;
        }
```

33 

```
    }
public static List<Materia> obtenerMateriasExterno(String json,
Context ctx) {
List<Materia> listaMaterias = new ArrayList<Materia>();
try {
JSONArray materiasJSON = new JSONArray(json);
for (int i = 0; i < materiasJSON.length(); i++) {
JSONObject obj = materiasJSON.getJSONObject(i);
Materia materia = new Materia();
materia.setCodmateria(obj.getString("CODMATERIA"));
materia.setNommateria(obj.getString("NOMMATERIA"));
```

```
Materia.setUnidadesval(obj.getString("UNIDADESVAL"));
listaMaterias.add(materia);
            }
return listaMaterias;
        } catch (Exception e) {
Toast.makeText(ctx, "Error en parseOO de JSON",
Toast.LENGTH_LONG)
                    .show();
return null;
        }
    }
public static void insertarNotaLocal(String url, JSONObject obj,
Context ctx) {
String respuesta = obtenerRespuestaPost(url, obj, ctx);
try {
if(respuesta.equals("200"))
Toast.makeText(ctx, "Insercion Correcta",
Toast.LENGTH_LONG).show();
else
Toast.makeText(ctx, "Error registro duplicado",
Toast.LENGTH_LONG).show();
Log.v("",respuesta);
        } catch (Exception e) {
Toast.makeText(ctx, "Error en la respuesta JSON",
Toast.LENGTH_LONG).show();
        }
    }
```

34 

```
public static void insertarNotaExterno(String peticion, Context
ctx) {
String json = obtenerRespuestaPeticion(peticion, ctx);
try {
JSONObject resultado = new JSONObject(json);
Toast.makeText(ctx, "Registro ingresado"+
resultado.getJSONArray("resultado").toString(), Toast.LENGTH_LONG)
                    .show();
int respuesta = resultado.getInt("resultado");
if (respuesta == 1)
Toast.makeText(ctx, "Registro ingresado",
Toast.LENGTH_LONG)
                        .show();
else
Toast.makeText(ctx, "Error registro duplicado",
Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
public static String obtenerPromedioJSON(String json, Context
ctx) {
try {
JSONArray objs = new JSONArray(json);
if (objs.length() != 0)
//NOTAFINAL PROMEDIO
return objs.getJSONObject(0).getString("PROMEDIO");
else {
Toast.makeText(ctx, "Error carnet no existe",
Toast.LENGTH_LONG)
                        .show();
return " ";
            }
        } catch (JSONException e) {
Toast.makeText(ctx, "Error con la respuesta JSON",
Toast.LENGTH_LONG).show();
return " ";
        }
    }
}
```

35 

## **ActualizarMateria** 

Este programa no es un update aunque su nombre lo sugiere, en él se hacen dos operaciones, primero se hace un consumo de un servicio GET desde MySQL y luego se realiza un ciclo de inserciones en SQLite de los datos extraidos. Es decir que es una inserción de Datos en MySQL, de Datos obtenidos de una selección desde MySQL. 

## _**Interfaz (activity_actualizar_materia.xml)**_ 

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout tools:context=".ActualizarMateriaActivity"
android:orientation="vertical"
android:layout_height="match_parent"
android:layout_width="match_parent"
android:id="@+id/LinearLayout1"
xmlns:tools="http://schemas.android.com/tools"
xmlns:android="http://schemas.android.com/apk/res/android">
    <TextView android:layout_height="wrap_content"
android:layout_width="wrap_content"
android:id="@+id/textView1"
android:text="@string/indicacion_materia"/>
```

```
    <EditText
```

```
android:id="@+id/editText_fecha"
```

```
android:layout_width="match_parent"
android:layout_height="wrap_content"
android:ems="10"
```

```
android:hint="@string/formato_fecha"
android:inputType="date"
android:minHeight="48dp">
```

```
        <requestFocus />
```

```
    </EditText>
```

```
    <LinearLayout
```

```
android:orientation="vertical"
```

```
android:layout_height="wrap_content"
android:layout_width="match_parent">
```

```
        <Button android:layout_height="wrap_content"
```

```
android:layout_width="wrap_content"
android:id="@+id/button1"
```

```
android:text="@string/servicio_PHPlocal"
android:onClick="servicioPHP"/>
```

```
        <Button android:layout_height="wrap_content"
```

```
android:layout_width="wrap_content"
android:id="@+id/button2"
```

36 

```
android:text="@string/servicio_PHPexterno"
android:onClick="servicioPHP"/>
        <Button android:layout_height="wrap_content"
android:layout_width="wrap_content"
android:id="@+id/button3"
android:text="@string/servicio_PublicoUES"
android:onClick="servicioPHP"/>
    </LinearLayout>
    <Button android:layout_height="wrap_content"
android:layout_width="match_parent"
android:id="@+id/button4"
android:text="@string/guardar"
android:onClick="guardar"/>
    <ListView
android:layout_height="wrap_content"
android:layout_width="match_parent"
android:id="@+id/listView1"> </ListView>
</LinearLayout>
```

## _**Activity(ActualizarMateria.java)**_ 

```
package sv.ues.edu.fia.eisi.consumows2carnetbd;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
@SuppressLint("NewApi")
```

```
public class ActualizarMateriaActivity extends AppCompatActivity {
ControlBDAlumno db;
```

```
static List<Materia> listaMaterias;
static List<String> nombreMaterias;
EditText fechaTxt;
```

37 

```
ListView listViewMaterias;
private final String urlLocal =
"http://192.168.1.18/ws_db_materia_fecha.php";
private final String urlHostingGratuito =
"https://eisicarnetpdm1152021.000webhostapp.com/ws_db_materia_fecha.
php";
private String urlPublicoUES =
"https://eisi.fia.ues.edu.sv/eisi25/MQ25001/ws_db_materia_fecha.php"
;
@SuppressLint("NewApi")
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_materia);
StrictMode.ThreadPolicy policy = new
StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
StrictMode.setThreadPolicy(policy);
db = new ControlBDAlumno(this);
```

```
listaMaterias = new ArrayList<Materia>();
nombreMaterias = new ArrayList<String>();
fechaTxt = (EditText) findViewById(R.id.editText_fecha);
listViewMaterias = (ListView) findViewById(R.id.listView1);
    }
```

```
public void servicioPHP(View v) {
```

```
String[] fecha = fechaTxt.getText().toString().split("/");
String url = "";
if(v.getId()==R.id.button1){
// it was the first button
url = urlLocal + "?day=" + fecha[0] + "&month="+
fecha[1] + "&year=" + fecha[2];
        }
if(v.getId()==R.id.button2){
```

```
// it was the second button
url = urlHostingGratuito + "?day=" + fecha[0
"&month="+ fecha[1] + "&year=" + fecha[2];
        }
if(v.getId()==R.id.button3){
// it was the third button
url = urlPublicoUES + "?day=" + fecha[0] +
```

38 

```
"&month="+ fecha[1] + "&year=" + fecha[2];
        }
String materiasExternas =
ControladorServicio.obtenerRespuestaPeticion(url, this);
try {
listaMaterias.addAll(ControladorServicio.obtenerMateriasExterno(mate
riasExternas, this));
            actualizarListView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
public void guardar(View v) {
db.abrir();
for(int i=0; i < listaMaterias.size();i++){
Log.v("guardar",db.insertar(listaMaterias.get(i)));
        }
db.cerrar();
Toast.makeText(this, "Guardado con exito",
Toast.LENGTH_LONG).show();
listaMaterias.removeAll(listaMaterias);
        actualizarListView();
    }
private void actualizarListView() {
String dato = "";
nombreMaterias.clear();
for (int i = 0; i < listaMaterias.size(); i++) {
dato = listaMaterias.get(i).getCodmateria() + "    "
+ listaMaterias.get(i).getNommateria();
nombreMaterias.add(dato);
        }
        eliminarElementosDuplicados();
ArrayAdapter<String> adaptador = new
ArrayAdapter<String>(this,
android.R.layout.simple_list_item_1,
nombreMaterias);
listViewMaterias.setAdapter(adaptador);
    }
private void eliminarElementosDuplicados() {
HashSet<Materia> conjuntoMateria = new HashSet<Materia>();
conjuntoMateria.addAll(listaMaterias);
```

39 

_`listaMaterias`_ `.clear();` _`listaMaterias`_ `.addAll(conjuntoMateria); HashSet<String> conjuntoNombre = new HashSet<String>(); conjuntoNombre.addAll(` _`nombreMaterias`_ `);` _`nombreMaterias`_ `.clear();` _`nombreMaterias`_ `.addAll(conjuntoNombre); } }` 

## Nota: El codigo fuente de la clase Materia.java esta en su proyecto de Guia 4 y en anexo 3 

## **Datos en Bases Servidor** 

Modifique los datos en las versiones de MySQL que tiene, si aun no lo ha hecho 

Ejemplo: El 000WebHost Materia 

En el Local Materia 

En Servidor EISI 

40 

## **Prueba de los Servicios que se utilizaran** 

*Primero investigue cual es su dirección ip, puede hacerlo con la ejecución de la ventana de comandos…. 

Puede consultar los anexos 

*Pruebe los servicios de ws_db_materia_fecha.php en sus tres versiones(Local, onRender y Servidor de la EISI) 

- http://172.16.112.50/WS2026/ws_db_materia_fecha.php?year=2025&month=01&day=01 

- https://pruebapdm115.onrender.com/Model/ws_db_materia_fecha.php?year=2025&month=01&da y=01 

- https://eisi.fia.ues.edu.sv/eisi25/MQ25001/ws_db_materia_fecha.php?year=2013&month=01&day= 01 (Pendiente de implementar 2026 por restricciones de Servidor) 

Nota: 

- Ajuste los datos de la IP según su máquina, y los datos de fecha según usted los haya introducido. 

41 

## **Pruebe la Aplicación** 

**==> picture [33 x 8] intentionally omitted <==**

**----- Start of picture text -----**<br>
Menu<br>**----- End of picture text -----**<br>


**==> picture [84 x 10] intentionally omitted <==**

**----- Start of picture text -----**<br>
(Actualizar Notas)<br>**----- End of picture text -----**<br>


42 

## **Insertar Notas** 

A Continuación, se presenta el código a sustituir en interfaz grafica y Activity para insertar datos en la base de Datos MySQL desde la Aplicación Móvil 

## _**Interfaz (activity_ingresar_nota.xml)**_ 

```
<?xml version="1.0" encoding="utf-8"?>
```

```
<LinearLayout tools:context=".IngresarNotaActivity"
android:orientation="vertical"
android:layout_height="match_parent"
android:layout_width="match_parent"
android:id="@+id/LinearLayout1"
xmlns:tools="http://schemas.android.com/tools"
xmlns:android="http://schemas.android.com/apk/res/android">
    <TableLayout android:layout_height="wrap_content"
android:layout_width="match_parent">
        <TableRow android:layout_height="wrap_content"
android:layout_width="wrap_content"
android:id="@+id/tableRow1">
```

```
            <TextView android:layout_height="wrap_content"
android:layout_width="wrap_content"
android:id="@+id/TextView03"
android:text="@string/carnet"/>
            <EditText android:layout_height="wrap_content"
android:layout_width="wrap_content"
android:id="@+id/editText_notaCarnet"
android:ems="10">
```

```
                <requestFocus/>
```

```
            </EditText>
```

```
        </TableRow>
```

```
        <TableRow android:layout_height="wrap_content"
android:layout_width="wrap_content"
android:id="@+id/tableRow2">
```

```
            <TextView android:layout_height="wrap_content"
android:layout_width="wrap_content"
android:id="@+id/TextView02"
android:text="@string/cod_materia"/>
            <EditText android:layout_height="wrap_content"
android:layout_width="wrap_content"
android:id="@+id/editText_notaMateria"
android:ems="10"/>
        </TableRow>
```

```
        <TableRow android:layout_height="wrap_content"
android:layout_width="wrap_content"
android:id="@+id/tableRow3">
```

```
            <TextView android:layout_height="wrap_content"
android:layout_width="wrap_content"
android:id="@+id/TextView01" android:text="@string/ciclo"/>
            <EditText android:layout_height="wrap_content"
android:layout_width="wrap_content"
android:id="@+id/editText_notaCiclo"
android:ems="10"/>
        </TableRow>
```

43 

```
        <TableRow android:layout_height="wrap_content"
android:layout_width="wrap_content"
android:id="@+id/tableRow4">
```

```
            <TextView android:layout_height="wrap_content"
android:layout_width="wrap_content"
android:id="@+id/textView1"
android:text="@string/nota_final"/>
```

```
            <EditText android:layout_height="wrap_content"
android:layout_width="wrap_content"
android:id="@+id/editText_notaFinal"
android:ems="10"/>
```

```
        </TableRow>
```

```
    </TableLayout>
```

```
    <LinearLayout
android:orientation="vertical"
android:layout_height="wrap_content"
android:layout_width="match_parent">
```

```
        <Button android:layout_height="wrap_content"
android:layout_width="wrap_content"
android:id="@+id/btn_notaLocal"
android:text="@string/servicio_PHPlocal"
android:onClick="insertarNota"/>
```

```
        <Button android:layout_height="wrap_content"
android:layout_width="wrap_content"
android:id="@+id/btn_notaExterno"
android:text="@string/servicio_PHPexterno"
android:onClick="insertarNota"/>
```

```
        <Button android:layout_height="wrap_content"
android:layout_width="wrap_content"
android:id="@+id/btn_notaPublicoUES"
android:text="@string/servicio_PublicoUES"
android:onClick="insertarNota"/>
```

```
    </LinearLayout>
```

```
</LinearLayout>
```

44 

## _**Activity(IngresarNotaActivity.java)**_ 

```
package sv.ues.edu.fia.eisi.consumows2carnetbd;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
```

```
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
@SuppressLint("NewApi")
public class IngresarNotaActivity extends AppCompatActivity {
    EditText carnetTxt;
    EditText codMateriaTxt;
    EditText cicloTxt;
    EditText notaTxt;
private final String urlLocal = "http://192.168.1.18/ws_nota_insert.php";
private final String urlHostingGratuito =
"https://eisicarnetpdm1152021.000webhostapp.com/ws_nota_insert.php";
private String urlPublicoUES =
"https://eisi.fia.ues.edu.sv/eisi25/MQ25001/ws_nota_insert.php";
@SuppressLint("NewApi")
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_nota);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
carnetTxt = (EditText) findViewById(R.id.editText_notaCarnet);
codMateriaTxt = (EditText) findViewById(R.id.editText_notaMateria);
cicloTxt = (EditText) findViewById(R.id.editText_notaCiclo);
notaTxt = (EditText) findViewById(R.id.editText_notaFinal);
    }
public void insertarNota(View v) {
        String carnet = carnetTxt.getText().toString();
        String codMateria = codMateriaTxt.getText().toString();
        String ciclo = cicloTxt.getText().toString();
        String notaFinal = notaTxt.getText().toString();
        String url = null;
        JSONObject datosNota = new JSONObject();
        JSONObject nota = new JSONObject();
switch (v.getId()) {
case R.id.btn_notaLocal:
                url = urlLocal+ "?carnet=" + carnet + "&codmateria="
+ codMateria + "&ciclo=" + ciclo + "&notafinal=" +
notaFinal;
                ControladorServicio.insertarNotaExterno(url, this);
break;
case R.id.btn_notaPublicoUES:
                url = urlPublicoUES+ "?carnet=" + carnet + "&codmateria="
```

45 

|||+ codMateria +**"&ciclo="**+ ciclo +**"&notafinal="**+|
|---|---|---|
|notaFinal;|||
|ControladorServicio.|ControladorServicio._insertarNotaExterno_(url,**this**);||
||**break**;|;|
||**case**R.id.**_btn_notaExterno_**:||
|url =|url =|**urlHostingGratuito**+**"?carnet="**+ carnet +**"&codmateria="**|
|||+ codMateria +**"&ciclo="**+ ciclo +**"&notafinal="**+|
|notaFinal;|||
|ControladorServicio.|ControladorServicio._insertarNotaExterno_(url,**this**);||
||**break**;|;|
|}|||
|}|||
|}|||



## **Consulta de Datos** 

Consulte los Datos antes de Probar la aplicación (en PhpMyAdmin) 

## Local Notas 

## Hosting Gratuito 

## En Servidor EISI 

46 

Puede probar inserciones desde navegador primero 

- Local 

http://172.16.111.62/WS2026/ws_nota_insert.php?carnet=SS00001&codmateria=MAT115&ciclo=12029&no tafinal=4 

- Hosting gratuito 

https://pruebapdm115.onrender.com/Model/ws_nota_insert.php?carnet=NN00001&codmateria=MAT115& ciclo=12029&notafinal=5 

## **Pruebe la Aplicación (Actualizar Notas)** 

Revise que el nuevo registro se inserto 

47 

Realice esta operación para los otros dos servidores (000webhost y eisi) 

## **Consultar Promedio de notas por Carnet** 

A Continuación, se presenta el código a sustituir en interfaz grafica y Activity para insertar datos en la base de Datos MySQL desde la Aplicación Móvil 

_**Interfaz (activity_promedio_alumno.xml)**_ 

_`<?`_ **`xml version="1.0" encoding="utf-8"`** _`?>`_ 

`<` **`LinearLayout tools:context=".PromedioAlumnoActivity" android:orientation="vertical" android:layout_height="match_parent" android:layout_width="match_parent" android:id="@+id/LinearLayout1" xmlns:tools="http://schemas.android.com/tools" xmlns:android="http://schemas.android.com/apk/res/android"`** `>` 

`<` **`LinearLayout android:layout_height="wrap_content" android:layout_width="match_parent"`** `> <` **`TextView android:layout_height="wrap_content" android:layout_width="wrap_content" android:id="@+id/textView1" android:textAppearance="?android:attr/textAppearanceLarge" android:text="@string/carnet" android:layout_weight="1"`** `/>` 

`<` **`EditText android:layout_height="wrap_content"`** 

**`android:layout_width="wrap_content" android:id="@+id/editText_alumnoCarnet" android:layout_weight="1" android:ems="10"`** `>` 

`<` **`requestFocus`** `/>` 

`</` **`EditText`** `>` 

`</` **`LinearLayout`** `>` 

`<` **`LinearLayout android:orientation="vertical"`** 

**`android:layout_height="wrap_content" android:layout_width="match_parent"`** `>` 

`<` **`Button android:layout_height="wrap_content" android:layout_width="wrap_content" android:id="@+id/button1" android:text="@string/servicio_PHPlocal" android:onClick="consultarPromedioLocal"`** `/>` 

`<` **`Button android:layout_height="wrap_content" android:layout_width="wrap_content" android:id="@+id/button2" android:text="@string/servicio_PHPexterno" android:onClick="consultarPromedioExterno"`** `/>` 

`<` **`Button android:layout_height="wrap_content"`** 

48 

```
android:layout_width="wrap_content"
android:id="@+id/button4"
android:text="@string/servicio_PublicoUES"
android:onClick="consultarPromedioPublicoUES"/>
    </LinearLayout>
    <TextView android:layout_height="wrap_content"
android:layout_width="match_parent"
android:id="@+id/textView_alumnoNota"
android:textAppearance="?android:attr/textAppearanceLarge"
android:layout_weight="0.08"/>
</LinearLayout>
```

## **Activity(PromedioAlumnoActivity.java)** 

```
package sv.ues.edu.fia.eisi.consumows2carnetbd;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
@SuppressLint("NewApi")
public class PromedioAlumnoActivity extends AppCompatActivity {
EditText carnetTxt;
TextView notaPromedioTxt;
private final String urlLocal =
"http://192.168.159.1/ws_db_carnet_group.php";
private final String urlHostingGratuito =
"https://pdm115guia6.000webhostapp.com/ws7/ws_db_carnet_group.php";
private String urlPublicoUES =
"https://eisi.fia.ues.edu.sv/eisi25/MQ25001/ws_db_carnet_group.php";
@SuppressLint("NewApi")
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promedio_alumno);
StrictMode.ThreadPolicy policy = new
StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
StrictMode.setThreadPolicy(policy);
carnetTxt = (EditText)
findViewById(R.id.editText_alumnoCarnet);
notaPromedioTxt = (TextView)
```

49 

```
findViewById(R.id.textView_alumnoNota);
    }
public void consultarPromedioLocal(View v) {
String carnet = carnetTxt.getText().toString();
String url = urlLocal + "?carnet=" + carnet;
String notaPromedioJSON =
ControladorServicio.obtenerRespuestaPeticion(
url, this);
notaPromedioTxt.setText("Nota Promedio Local: "
+
ControladorServicio.obtenerPromedioJSON(notaPromedioJSON,this));
    }
public void consultarPromedioPublicoUES(View v) {
String carnet = carnetTxt.getText().toString();
String url = urlPublicoUES + "?carnet=" + carnet;
String notaPromedioJSON =
ControladorServicio.obtenerRespuestaPeticion(
url, this);
notaPromedioTxt.setText("Nota Promedio Host.EISI: "
+
ControladorServicio.obtenerPromedioJSON(notaPromedioJSON,this));
    }
public void consultarPromedioExterno(View v) {
String carnet = carnetTxt.getText().toString();
String url = urlHostingGratuito + "?carnet=" + carnet;
String notaPromedioJSON =
ControladorServicio.obtenerRespuestaPeticion(
url, this);
notaPromedioTxt.setText("Nota Promedio Host.Grat.: "
+
ControladorServicio.obtenerPromedioJSON(notaPromedioJSON,this));
    }
}
```

Pruebe los Servicios en Navegador 

- Local 

http://172.16.111.62/WS2026/ws_db_carnet_group.php?carnet=SS00001 

- Hosting Gratuito 

https://pruebapdm115.onrender.com/Model/ws_db_carnet_group.php?carnet=NN00001 

50 

## **Pruebe la Aplicación (Promedio de Notas por carnet)** 

**Puede ver el proyecto terminado en la pagina de la asignatura** 

**No olvide subir su proyecto finalizado al link respectivo** 

51 

## **Anexo 1 Buscar la dirección IP de nuestra pc por medio de consola** 

En el botón de inicio ejecutar escribir la palabra “cmd” y luego enter 

Dentro de la consola ejecutar el comando ipconfig luego enter 

Buscamos la línea de dirección ipv4 dentro de Adaptador de Ethernet Conexión de área Local: 

52 

**Anexo 2 Buscar la dirección IP de nuestra pc de forma visual** En la barra de estado de Windows buscar el icono de red lan(presionar clic) 

Luego clic en “Abrir el centro de redes y recursos compartidos” 

Presionamos clic en la opción “Cambiar la configuración del adaptador” 

53 

## Presionamos doble clic en “Conexión de área local” 

Presionamos clic en detalles y en la cuarta línea veremos la dirección de ipv4 que necesitamos 

54 

55 

## **Anexo 3 Materia.java** 

```
package sv.edu.ues.fia.eisi.consumows2carnetbd;
public class Materia {
private String codmateria;
private String nommateria;
private String unidadesval;
public Materia(){
    }
public Materia(String codmateria, String nommateria, String
unidadesval) {
this.codmateria = codmateria;
this.nommateria = nommateria;
this.unidadesval = unidadesval;
    }
public String getCodmateria() {
return codmateria;
    }
public void setCodmateria(String codmateria) {
this.codmateria = codmateria;
    }
public String getNommateria() {
return nommateria;
    }
public void setNommateria(String nommateria) {
this.nommateria = nommateria;
    }
public String getUnidadesval() {
return unidadesval;
    }
public void setUnidadesval(String unidadesval) {
this.unidadesval = unidadesval;
    }
}
```

56 

