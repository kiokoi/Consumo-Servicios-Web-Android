package com.ramirez.soto.dev.ejemplo_consumo_servicios_web;

public class Elemento_selector {
    private String tipo;

    public Elemento_selector(String Opcion_a_mostrar)
    {
        tipo = Opcion_a_mostrar;
    }

    public String Obtener_Tipo() { return tipo; }
}
