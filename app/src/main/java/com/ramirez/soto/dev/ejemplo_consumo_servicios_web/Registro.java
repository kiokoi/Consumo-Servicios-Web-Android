package com.ramirez.soto.dev.ejemplo_consumo_servicios_web;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Registro extends AppCompatActivity {

    // VARIABLES DE CONEXION SERVICIOS WEB

    String Controlador_web = "";

    // VARIABLES PARA DEPURAR
    private static final String TAG = "R_S:Login :";

    // VARIABLES PARA FUNCIONAMIENTO

    EditText Caja_texto_nombre, Caja_texto_desc;
    Spinner tipos;
    Button Boton_registrar;
    Context Contexto_pantalla = this;
    Vibrator Objeto_para_vibrar;

    String nombre_producto;
    String descrip_producto;
    String selector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        tipos = (Spinner) findViewById(R.id.selector_producto);


        // Fucniones de inicio antes de arrancar
        Controlador_web = getApplicationContext().getString(R.string.Servidor);

        // Vincula elementos de la pantalla
        Caja_texto_nombre = (EditText) findViewById(R.id.producto_nombre);
        Caja_texto_desc = (EditText) findViewById(R.id.producto_descrip);

        Boton_registrar =  (Button) findViewById(R.id.button3_reg);

        // Acciones para mejorar la vista
        Objeto_para_vibrar = (Vibrator) Contexto_pantalla.getSystemService(Context.VIBRATOR_SERVICE) ;
    }

    // funciones de acciones y eventos     *********************************************************

    public void Boton_registrar_Presionado(View v)
    {
        Objeto_para_vibrar.vibrate(50);

        nombre_producto = Caja_texto_nombre.getText().toString().trim();
        descrip_producto = Caja_texto_desc.getText().toString().trim();
        selector = tipos.getSelectedItem().toString(); // Small, Medium, Large

        if(nombre_producto.equals("") != true && descrip_producto.equals("") != true && selector.equals("") != true)
        {
            Registro_P();
        }
        else
        {
            Mensaje_Cuadro_Dialogo("Upps...","Te faltan datos por llenar");
        }
    }

    // Base de datos    ***************************************************************************

    public void Registro_P()
    {

        // VARIABLES DE CONEXION AL SERVICIO WEB

        AsyncHttpClient cliente = new AsyncHttpClient();
        RequestParams parametros = new RequestParams();

        // SERVICIO WEB REQUERIDO

        String SW_Requerido = "Registro/Producto";

        parametros.put("Producto_Nombre",nombre_producto);
        parametros.put("Producto_Descripcion",descrip_producto);
        parametros.put("Producto_Tipo",selector);

        String url_Servicio_Web = Controlador_web + SW_Requerido;

        //Log.d(TAG,url_Servicio_Web + "?nombre_producto="+nombre_producto+"&descrip_producto="+descrip_producto);
       // String ruta = url_Servicio_Web+"?nombre_producto="+nombre_producto+"&descrip_producto="+descrip_producto;
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

                        System.out.println("*************************++  "+json);

                       /* if(Existe_Exito_Servicio_Web.equals("Verdadero"))
                        {

                            // OBTENEMOS LA iNFORMACION DISPONIBLE ************************************************

                            JSONArray Datos_Obtenidos_Servicio_Web = (JSONArray)json.get("DATOS_OBTENIDOS");

                            if(Datos_Obtenidos_Servicio_Web.length() == 1 )
                            {
                                JSONObject Usuario = Datos_Obtenidos_Servicio_Web.getJSONObject(0);

                                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor;
                                editor = settings.edit();

                                editor.putBoolean("logueo",true);
                                editor.putString("id_Administrador", Usuario.getString("id"));
                                editor.putString("Administrador_Nombre", Usuario.getString("Usuario_Nombre"));
                                editor.putString("Administrador_Pass", Usuario.getString("Usuario_Pass"));

                                editor.commit();

                                System.out.println("**************id: "+Usuario.getString("id"));
                                System.out.println("**************nombre : "+Usuario.getString("Usuario_Nombre"));
                                System.out.println("**************pass : "+Usuario.getString("Usuario_Pass"));

                                Intent Siguiente_ventana= new Intent(Login.this, Inicio.class);
                                startActivity(Siguiente_ventana);
                                // overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                                finish();
                            }
                            else
                            {
                                Mensaje_Cuadro_Dialogo("Error","El usuario o la contraseña no coincide");
                            }

                        }
                        else{    Mensaje_Cuadro_Dialogo("Error","ERROR EN EL SERVIDOR"); }*/
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