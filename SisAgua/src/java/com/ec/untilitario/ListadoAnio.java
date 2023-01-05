/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.untilitario;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Darwin
 */
public class ListadoAnio {

    private static List<ModeloMeses> listaAnio;
    private static ModeloMeses anioActual;

    static {
        listaAnio = new ArrayList<ModeloMeses>();
        listaAnio.add(new ModeloMeses(2021, "2021"));
        listaAnio.add(new ModeloMeses(2022, "2022"));
        listaAnio.add(new ModeloMeses(2023, "2023"));
        listaAnio.add(new ModeloMeses(2024, "2024"));
        listaAnio.add(new ModeloMeses(2025, "2025"));
        listaAnio.add(new ModeloMeses(2026, "2026"));
        listaAnio.add(new ModeloMeses(2027, "2027"));
        listaAnio.add(new ModeloMeses(2028, "2028"));
        listaAnio.add(new ModeloMeses(2029, "2029"));
        listaAnio.add(new ModeloMeses(2030, "2030"));
        listaAnio.add(new ModeloMeses(2031, "2031"));
        listaAnio.add(new ModeloMeses(2032, "2032"));

    }

    public static List<ModeloMeses> getListaAnio() {
        return listaAnio;
    }

    public static ModeloMeses getAnioActual() {
        Integer numeroMes = new Date().getYear()+1900;
        for (ModeloMeses listaMese : listaAnio) {
            if (listaMese.getNumero().intValue() == numeroMes.intValue()) {
                anioActual = listaMese;
                return anioActual;
            }
        }
        return anioActual;
    }

}
