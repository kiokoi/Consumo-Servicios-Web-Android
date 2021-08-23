package com.ramirez.soto.dev.ejemplo_consumo_servicios_web;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class Adaptador_Producto extends ArrayAdapter {
    String[][] Invitados_r;

    public Adaptador_Producto(@NonNull Context context, String[][] invitados_enviados) {
        super(context, R.layout.elemento_lista, invitados_enviados);
        Invitados_r= invitados_enviados;
    }
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View Fila = inflater.inflate(R.layout.elemento_lista,parent,false);

            TextView Text_Nombre =  Fila.findViewById(R.id.elemento_nombre);
            TextView Text_Desc = Fila.findViewById(R.id.elemento_descripcion);

            Text_Nombre.setText(Invitados_r[position][0]);
            Text_Desc.setText(Invitados_r[position][1]);

        return Fila;
    }

}
