package com.ramirez.soto.dev.ejemplo_consumo_servicios_web;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador_selector extends ArrayAdapter<Elemento_selector>{
    public Adaptador_selector(Context context, ArrayList<Elemento_selector> Lista_Datos) { super(context, 0, Lista_Datos); }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) { return initView(position, convertView, parent); }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) { return initView(position, convertView, parent); }

    private View initView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.elemento_selector, parent, false);
        }

        TextView Medio_nombre = convertView.findViewById(R.id.nombre_selector);

        Elemento_selector currentItem = getItem(position);

        if (currentItem != null)
        {
            Medio_nombre.setText(currentItem.Obtener_Tipo());
        }

        return convertView;
    }
}
