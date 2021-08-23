package com.ramirez.soto.dev.ejemplo_consumo_servicios_web;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class Inicio extends AppCompatActivity {
    // VARIABLES DE CONEXION SERVICIOS WEB

    String Controlador_web = "";

    // VARIABLES PARA DEPURAR

    private static final String TAG = "R_S:Login :";

    // VARIABLES PARA FUNCIONAMIENTO
    EditText Caja_texto_nom, Caja_texto_desc;
    Button Boton_entrar;
    Context Contexto_pantalla = this;
    Vibrator Objeto_para_vibrar;

    ListView Lista_Productos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        // Fucniones de inicio antes de arrancar
        Controlador_web = getApplicationContext().getString(R.string.Servidor);

        // Vincula elementos de la pantalla
        Lista_Productos = (ListView)findViewById(R.id.lista_prueba);

        Logueo();

        // Acciones para mejorar la vista
        Objeto_para_vibrar = (Vibrator) Contexto_pantalla.getSystemService(Context.VIBRATOR_SERVICE) ;
    }
    public void llenar_selector(View v){
        Intent Siguiente_ventana= new Intent(Inicio.this, Selector.class);
        startActivity(Siguiente_ventana);
        finish();
    }

    // Base de datos    ***************************************************************************

    public void Logueo()
    {

        // VARIABLES DE CONEXION AL SERVICIO WEB

        AsyncHttpClient cliente = new AsyncHttpClient();
        RequestParams parametros = new RequestParams();

        // SERVICIO WEB REQUERIDO

        String SW_Requerido = "Llenar/Lista";

        String url_Servicio_Web = Controlador_web + SW_Requerido;

        //Log.d(TAG,url_Servicio_Web + "?Cliente_Nombre="+Cliente_Nombre+"&Cliente_Pass="+Cliente_Pass);
       // String ruta = url_Servicio_Web+"?Cliente_Nombre="+Cliente_Nombre+"&Cliente_Pass="+Cliente_Pass;
        //Log.d(TAG,ruta);
        // EJECUCION DEL SERVICIO WEB

        cliente.get(url_Servicio_Web, parametros, new AsyncHttpResponseHandler()
        {
            // EXITO  DE LA EJECUCION
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody)
            {
                if (statusCode == 200)
                {
                    String String_json = new String(responseBody);

                    try
                    {

                        JSONObject json = new JSONObject(String_json);
                        String Existe_Exito_Servicio_Web = json.getString("EXITO");

                        //System.out.println("*************************++  "+json);

                        if(Existe_Exito_Servicio_Web.equals("Verdadero"))
                        {

                            // OBTENEMOS LA iNFORMACION DISPONIBLE ************************************************

                            JSONArray Datos_Obtenidos_Servicio_Web = (JSONArray)json.get("DATOS_OBTENIDOS");

                            if(Datos_Obtenidos_Servicio_Web.length() == 0 ) { Mensaje_Cuadro_Dialogo("Lo sentimos","Aun no tienes Archivos para mostrar"); }
                            else {

                                String[][] Productos = new String[Datos_Obtenidos_Servicio_Web.length()][];
                                for (int i = 0; i < Datos_Obtenidos_Servicio_Web.length(); i++) {
                                    String[] arreglo_aux = new String[3];

                                    JSONObject linea = Datos_Obtenidos_Servicio_Web.getJSONObject(i);
                                    arreglo_aux[0]= linea.getString("Producto_Nombre");
                                    arreglo_aux[1]= linea.getString("Producto_Descripcion");
                                    arreglo_aux[2]= linea.getString("Producto_Id");

                                    Productos[i] = arreglo_aux;
                                }
                               // System.out.println("*************************++  "+Productos[1][1]);
                                // LISTA *********************************************************************************
                                    Adaptador_Producto Adaptador_Invitados_Encontrados = new Adaptador_Producto(Contexto_pantalla,Productos);
                                    Lista_Productos.setAdapter(Adaptador_Invitados_Encontrados);

                                    Lista_Productos.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            //Toast.makeText(getApplicationContext(),"elemento: "+position, Toast.LENGTH_SHORT).show();
                                            Toast.makeText(getApplicationContext(),"elemento: "+Productos[position][2], Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                //*********************************************************************************
                            }
                        }
                        else{    Mensaje_Cuadro_Dialogo("Error","ERROR EN EL SERVIDOR"); }
                    }
                    catch (JSONException e) {   e.printStackTrace(); Mensaje_Cuadro_Dialogo("Error","Error con el servidor o la conexion de internet no estable"); }
                }
                else
                {
                    Log.d("Mowo_Login", "Statuscode: "+statusCode);
                }
            }

            // ERROR  DE LA EJECUCION
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) { }
        });

    }
    //******************************************************************************************************
    //public void Mensaje_Cuadro_Dialogo(String Titulo, String mensaje_aviso, Context contexto)
    public void Mensaje_Cuadro_Dialogo(String Titulo,String Mensaje)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(Titulo);
        builder.setMessage(Mensaje);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {

            @Override public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


}