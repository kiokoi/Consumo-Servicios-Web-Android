package com.ramirez.soto.dev.ejemplo_consumo_servicios_web;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Selector extends AppCompatActivity {

    private ArrayList<Elemento_selector> Lista_Selector = new ArrayList<>();
    private Adaptador_selector Adaptador_Tipo_Servicios;

    String Tipo_Seleccionado = "";

    Spinner Selector_Tipo_Servicios;
    String Controlador_web = "";

    Context contexto = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);

        Controlador_web = getApplicationContext().getString(R.string.Servidor);
        Selector_Tipo_Servicios = (Spinner)findViewById(R.id.selector_prueba);

        Cargar_Datos_Selector();

    }

    public void ir_Registro(View v){
        Intent Siguiente_ventana= new Intent(Selector.this, Registro.class);
        startActivity(Siguiente_ventana);
        finish();
    }

    // Base de datos    ***************************************************************************

    public void Cargar_Datos_Selector()
    {

        // VARIABLES DE CONEXION AL SERVICIO WEB

        AsyncHttpClient cliente = new AsyncHttpClient();
        RequestParams parametros = new RequestParams();

        // SERVICIO WEB REQUERIDO

        String SW_Requerido = "Selector";

        String url_Servicio_Web = Controlador_web + SW_Requerido;

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
                                System.out.println("**********************1"+Lista_Selector);
                                for (int i = 0; i < Datos_Obtenidos_Servicio_Web.length(); i++) {
                                    JSONObject linea = Datos_Obtenidos_Servicio_Web.getJSONObject(i);
                                    Lista_Selector.add(new Elemento_selector(linea.getString("Producto_Nombre")));
                                }
                                System.out.println("**********************2"+Lista_Selector);
                                // SELECTOR ********************************************************************************
                                Adaptador_Tipo_Servicios = new Adaptador_selector(contexto, Lista_Selector);
                                Selector_Tipo_Servicios.setAdapter(Adaptador_Tipo_Servicios);

                                Selector_Tipo_Servicios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                                {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        Elemento_selector clickedItem = (Elemento_selector) parent.getItemAtPosition(position);
                                        Tipo_Seleccionado  = clickedItem.Obtener_Tipo();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) { }
                                });
                                // ************************************************************************************
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