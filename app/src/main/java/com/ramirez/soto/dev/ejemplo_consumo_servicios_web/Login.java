package com.ramirez.soto.dev.ejemplo_consumo_servicios_web;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    // VARIABLES DE CONEXION SERVICIOS WEB

    String Controlador_web = "";

    // VARIABLES PARA DEPURAR

    private static final String TAG = "R_S:Login :";

    //VARIABLES DE LOGUEO
    // VARIABLES PARA FUNCIONAMIENTO

    EditText Caja_texto_urs, Caja_texto_pass;
    Button Boton_entrar;
    Context Contexto_pantalla = this;
    Vibrator Objeto_para_vibrar;

    String Cliente_Nombre = "";
    String Cliente_Pass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Fucniones de inicio antes de arrancar

        Controlador_web = getApplicationContext().getString(R.string.Servidor);

        // Vincula elementos de la pantalla

        Caja_texto_urs = (EditText) findViewById(R.id.input_correo);
        Caja_texto_pass = (EditText) findViewById(R.id.input_pass);

        Boton_entrar =  (Button) findViewById(R.id.boton_entrar);

        // Acciones para mejorar la vista

        Objeto_para_vibrar = (Vibrator) Contexto_pantalla.getSystemService(Context.VIBRATOR_SERVICE) ;

        Caja_texto_urs.setOnFocusChangeListener(new View.OnFocusChangeListener() { @Override public void onFocusChange(View v, boolean hasFocus) { if (!hasFocus) { hideKeyboard(v);} }});
        Caja_texto_pass.setOnFocusChangeListener(new View.OnFocusChangeListener() { @Override public void onFocusChange(View v, boolean hasFocus) { if (!hasFocus) { hideKeyboard(v);} }});

    }
    public void hideKeyboard(View view)
    {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // funciones de acciones y eventos     *********************************************************

    public void Boton_Entrar_Presionado(View v)
    {
        Objeto_para_vibrar.vibrate(50);

        Cliente_Nombre = Caja_texto_urs.getText().toString().trim();
        Cliente_Pass = Caja_texto_pass.getText().toString().trim();

        if(Cliente_Nombre.equals("") != true && Cliente_Pass.equals("") != true)
        {
            Logueo();
        }
        else
        {
            Mensaje_Cuadro_Dialogo("Upps...","Te faltan datos por llenar");
        }
    }

    // Base de datos    ***************************************************************************

    public void Logueo()
    {

        // VARIABLES DE CONEXION AL SERVICIO WEB

        AsyncHttpClient cliente = new AsyncHttpClient();
        RequestParams parametros = new RequestParams();

        // SERVICIO WEB REQUERIDO

        String SW_Requerido = "LoginUser";

        parametros.put("Correo",Cliente_Nombre);
        parametros.put("Pass",Cliente_Pass);
        String url_Servicio_Web = Controlador_web + SW_Requerido;

        //Log.d(TAG,url_Servicio_Web + "?Cliente_Nombre="+Cliente_Nombre+"&Cliente_Pass="+Cliente_Pass);
        String ruta = url_Servicio_Web+"?Cliente_Nombre="+Cliente_Nombre+"&Cliente_Pass="+Cliente_Pass;
        Log.d(TAG,ruta);
        // EJECUCION DEL SERVICIO WEB

        cliente.get(ruta, parametros, new AsyncHttpResponseHandler()
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